package com.JIMS.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.UserPageAction;
import com.JIMS.integration.entity.UserPermissionTabsModel;
import com.JIMS.integration.entity.UserPermissionsmodel;
import com.JIMS.integration.entity.UserTabPageActionsModel;
import com.JIMS.integration.entity.modulemodel;
import com.JIMS.integration.entity.usermastermodel;
import com.JIMS.integration.interfaces.PageActionProjection;
import com.JIMS.integration.repository.PageActionRepository;
import com.JIMS.integration.repository.UserPermissionTabsRepository;
import com.JIMS.integration.repository.UserPermissionsinterface;
import com.JIMS.integration.repository.UserTabPageActionsRepository;
import com.JIMS.integration.repository.moduleinterface;
import com.JIMS.integration.repository.pageinterface;
import com.JIMS.integration.repository.usermasterinterface;
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserPermissionsController {

    @Autowired private usermasterinterface userRepo;
    @Autowired private moduleinterface moduleRepo;
    @Autowired private pageinterface pageRepo;
    @Autowired private UserPermissionsinterface  permissionRepo;
    

    
    @GetMapping("/user-permissions")
    public List<Integer> getUserPermissions(@RequestParam("userId") String userId, @RequestParam(value = "moduleId", required = false) Integer moduleId) {
        if (moduleId != null) {
            return permissionRepo.findPageIdsByUserIdAndModuleId(userId, moduleId);
        }
        return permissionRepo.findPageIdsByUserId(userId);
    }

    @GetMapping("/assign-permissions")
    public String assignPermissionsForm(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("modules", moduleRepo.findAll());
        model.addAttribute("pages", pageRepo.findAll());
        return "assign-permissions";
    }

   

    
    
    
    @PostMapping("/save-permissions")
    public ResponseEntity<Object> savePermissions(@RequestBody Map<String, Object> payload) {
        try {

            List<Map<String, Object>> newPermissions =
                    (List<Map<String, Object>>) payload.get("newPermissions");

            List<Map<String, Object>> oldPermissions =
                    (List<Map<String, Object>>) payload.get("oldPermissions");

            String userId = (String) payload.get("userId");
            String createdBy = (String) payload.get("createdBy");

            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(new ResponseMessage("Error", "UserId not found"));
            }

            System.out.println("INSERT COUNT: " + newPermissions.size());
            System.out.println("DELETE COUNT: " + oldPermissions.size());

            // ✅ INSERT directly
            for (Map<String, Object> item : newPermissions) {
                UserPermissionsmodel entity = new UserPermissionsmodel();
                entity.setUserId(userId);
                entity.setModuleId((Integer) item.get("moduleId"));
                entity.setPageId((Integer) item.get("pageId"));
                entity.setCreatedBy(createdBy);
                entity.setCreatedDate(LocalDateTime.now());
                entity.setIsDelete(0);
                permissionRepo.save(entity);
            }

            // ✅ DELETE directly
            for (Map<String, Object> item : oldPermissions) {
                permissionRepo.softDelete(
                        userId,
                        (Integer) item.get("moduleId"),
                        (Integer) item.get("pageId"),
                        createdBy
                );
            }

            return ResponseEntity.ok(new ResponseMessage("Success", "Permissions updated"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error", e.getMessage()));
        }
    }
	public static class ResponseMessage {
	    private String status;
	    private String message;

	    public ResponseMessage(String status, String message) {
	        this.status = status;
	        this.message = message;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public String getMessage() {
	        return message;
	    }
	}
	
	@GetMapping("/user-permissions-details")
	public ResponseEntity<List<PageWithModuleDTO>> getUserPermissionsWithModules(@RequestParam("userId") String userId) {
	    List<Object[]> rawResults = permissionRepo.findPagesWithModulesByUserId(userId);
	    List<PageWithModuleDTO> dtoList = rawResults.stream()
	        .map(row -> new PageWithModuleDTO((String) row[0], (String) row[1], String.valueOf (row[2]),(String) row[3]))
	        .collect(Collectors.toList());

	    return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/user-permissions-module-details")
	public ResponseEntity<List<PageWithModuleDTO>> getUserPermissionsWithModules(
	        @RequestParam("userId") String userId,
	        @RequestParam("moduleName") String moduleName) {
	    
	    List<Object[]> rawResults = permissionRepo.findModulepagesByUserId(userId, moduleName);
	    
	    List<PageWithModuleDTO> dtoList = rawResults.stream()
	        .map(row -> new PageWithModuleDTO((String) row[0], (String) row[1], String.valueOf(row[2]),(String) row[3]))
	        .collect(Collectors.toList());

	    return ResponseEntity.ok(dtoList);
	}

	



	@GetMapping("/user-permissions/modules")
	public List<Integer> getUserModulesWithPermissions(@RequestParam String userId) {
	    return permissionRepo.findModuleIdsByUserId(userId);
	}


	/*
	 * @GetMapping("/users") public List<usermastermodel> getUsers() { return
	 * userRepo.getActiveUsers(); }
	 */
	
	

@Autowired
@Qualifier("misDataSource")
private DataSource misDataSource;

@GetMapping("/users")
public List<usermastermodel> getUsers() {

    // JIMS users
    List<usermastermodel> users = userRepo.getActiveUsers();

    // MIS users query
    String sql = """
    		SELECT
    u.user_id,
    u.user_name
FROM MIS.dbo.Users u
INNER JOIN JIMS.dbo.UserFactoryMappingJIMS ufm
    ON u.user_id = ufm.User_Id
WHERE u.deleted = 0;
    		""";

    try (
            Connection con = misDataSource.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
    ) {

        while (rs.next()) {

            usermastermodel user = new usermastermodel();

            user.setUser_id(rs.getString("user_name"));

            // because frontend uses username
            user.setUsername(rs.getString("user_name"));

            users.add(user);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return users;
}
        @GetMapping("/modules")
        public List<modulemodel> getModules() {
            return moduleRepo.findAll();
        }

	

        @DeleteMapping("/clear-permissions")
        @Transactional("integrationTransactionManager")
        public ResponseEntity<?> clearPermissions(@RequestParam String userId, @RequestParam int moduleId) {
            permissionRepo.deleteByUserIdAndModuleId(userId, moduleId);
            return ResponseEntity.ok(new ResponseMessage("Success", "Page permissions cleared."));
        }
        
        
        
        
        
        
        
        
        
        
        
        
//         From this Tab persmisssion Controller started
        
        
        @Autowired
        private UserPermissionTabsRepository tabRepo;
        @PostMapping("/save-tab-permissions")
        public ResponseEntity<Object> saveTabPermissions(@RequestBody Map<String, Object> payload) {

            try {

                List<Map<String, Object>> addedTabs =
                        (List<Map<String, Object>>) payload.getOrDefault("newTabs", new ArrayList<>());

                List<Map<String, Object>> removedTabs =
                        (List<Map<String, Object>>) payload.getOrDefault("oldTabs", new ArrayList<>());

                String userId = (String) payload.get("userId");
                String createdBy = (String) payload.get("createdBy");

                System.out.println("========= FINAL TAB DEBUG =========");
                System.out.println("ADDED TABS: " + addedTabs.size());
                System.out.println("REMOVED TABS: " + removedTabs.size());
                System.out.println("==================================");

                if (userId == null) {
                    return ResponseEntity.badRequest()
                        .body(new ResponseMessage("Error", "UserId not found"));
                }

                // ✅ INSERT (ONLY added tabs)
                for (Map<String, Object> item : addedTabs) {
                    if (item == null) continue;

                    UserPermissionTabsModel tab = new UserPermissionTabsModel();
                    tab.setUserId(userId);
                    tab.setModuleId((Integer) item.get("moduleId"));
                    tab.setPageId((Integer) item.get("pageId"));
                    tab.setTabName((String) item.get("tabId"));
                    tab.setCreatedBy(createdBy);
                    tab.setCreatedDate(LocalDateTime.now());
                    tab.setIsDelete(0);

                    tabRepo.save(tab);
                }

                // ✅ DELETE (ONLY removed tabs)
                for (Map<String, Object> item : removedTabs) {
                    if (item == null) continue;

                    tabRepo.softDeleted(
                            userId,
                            (Integer) item.get("moduleId"),
                            (Integer) item.get("pageId"),
                            (String) item.get("tabId"),
                            createdBy
                    );
                }

                return ResponseEntity.ok(new ResponseMessage("Success", "Tab permissions updated"));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMessage("Error", e.getMessage()));
            }
        }
        
        
        @GetMapping("/user-tab-permissions")
        public List<String> getUserTabs(@RequestParam String userId,
                                       @RequestParam int moduleId,
                                       @RequestParam int pageId) {
            return tabRepo.findTabsByUserIdAndModuleIdAndPageId(userId, moduleId, pageId);
        }
        
        
        @GetMapping("/user-tab-permissions/invoceModule")
        public List<String> getUserTabsforinvoicemodule(@RequestParam String userId,
                                       @RequestParam String moduleId,
                                       @RequestParam String pageId) {
            return tabRepo.findTabsByUserIdAndModuleIdAndPageIdInVoceModule(userId, moduleId, pageId);
        }
        
        
        @GetMapping("/tabpage-actions")
        public List<Map<String, Object>> getPageActionsTAb(@RequestParam int pageId) {

            List<Object[]> results = tabRepo.findActionsByPageId(pageId);

            List<Map<String, Object>> response = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();

                map.put("pageId", row[0]);
                map.put("actionName", row[1]);

                response.add(map);
            }

            return response;
        }
        

        // GET /api/invoice-tab-pages?tabID=1
        @GetMapping("/invoice-tab-pages")
        public ResponseEntity<?> getPagesByTab(@RequestParam int tabID) {
            try {
                List<Map<String, Object>> pages = tabRepo.getPagesByTabID(tabID);
                return ResponseEntity.ok(pages);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error fetching pages: " + e.getMessage());
            }
        }
        
        
        @Autowired
        private UserTabPageActionsRepository actionRepo;

        @PostMapping("/save-tab-page-actions")
        public ResponseEntity<Object> saveTabPageActions(@RequestBody Map<String, Object> payload) {

            try {

                List<Map<String, Object>> addedActions =
                        (List<Map<String, Object>>) payload.getOrDefault("newActions", new ArrayList<>());

                List<Map<String, Object>> removedActions =
                        (List<Map<String, Object>>) payload.getOrDefault("oldActions", new ArrayList<>());

                String userId = (String) payload.get("userId");
                String createdBy = (String) payload.get("createdBy");

                System.out.println("===== TAB PAGE ACTION DEBUG =====");
                System.out.println("ADDED: " + addedActions.size());
                System.out.println("REMOVED: " + removedActions.size());

                if (userId == null) {
                    return ResponseEntity.badRequest()
                            .body(new ResponseMessage("Error", "UserId missing"));
                }

                //  INSERT
                for (Map<String, Object> item : addedActions) {

                    UserTabPageActionsModel act = new UserTabPageActionsModel();

                    act.setUserId(userId);
                    act.setTabId((Integer) item.get("tabId"));
                    act.setTabPageId((Integer) item.get("tabPageId"));
                    act.setActionName((String) item.get("action"));
                    act.setCreatedBy(createdBy);
                    act.setCreatedDate(LocalDateTime.now());
                    act.setIsDelete(0);

                    actionRepo.save(act);
                }

                // DELETE (soft delete)
                for (Map<String, Object> item : removedActions) {

                    actionRepo.softDelete(
                            userId,
                            (Integer) item.get("tabId"),
                            (Integer) item.get("tabPageId"),
                            (String) item.get("action"),
                            createdBy
                    );
                }

                return ResponseEntity.ok(new ResponseMessage("Success", "Tab Page Actions saved"));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMessage("Error", e.getMessage()));
            }
        }
        
        
        @GetMapping("/user-tab-page-actions")
        public List<Map<String, Object>> getUserTabPageActions(@RequestParam String userId) {
            return actionRepo.findByUser(userId).stream().map(a -> {
                Map<String, Object> map = new HashMap<>();
                map.put("tabId", a.getTabId());
                map.put("tabPageId", a.getTabPageId());
                map.put("action", a.getActionName());
                return map;
            }).toList();
        }
        
        
        
        
        
        @GetMapping("/user-tab-pages-grouped")
        public List<Map<String, Object>> getGrouped(@RequestParam String userId) {

            List<Object[]> rows = tabRepo.findUserTabPages(userId);

            Map<Integer, Map<String, Object>> tabMap = new LinkedHashMap<>();

            for (Object[] row : rows) {

                Integer tabId = ((Number) row[0]).intValue();
                String tabName = (String) row[1];
                Integer pageId = ((Number) row[2]).intValue();
                String pageName = (String) row[3];

                tabMap.putIfAbsent(tabId, new HashMap<>());
                Map<String, Object> tab = tabMap.get(tabId);

                tab.put("tabId", tabId);
                tab.put("tabName", tabName);

                List<Map<String, Object>> pages =
                    (List<Map<String, Object>>) tab.getOrDefault("pages", new ArrayList<>());

                boolean exists = pages.stream()
                        .anyMatch(p -> p.get("pageId").equals(pageId));

                if (!exists) {
                    Map<String, Object> page = new HashMap<>();
                    page.put("pageId", pageId);
                    page.put("pageName", pageName);
                    pages.add(page);
                }

                tab.put("pages", pages);
            }

            return new ArrayList<>(tabMap.values());
        }
        
        @GetMapping("/invoice-tabs")
        public ResponseEntity<?> getAllInvoiceTabs() {
            try {
                List<Map<String, Object>> tabs = tabRepo.getAllTabs();
                return ResponseEntity.ok(tabs);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error fetching tabs: " + e.getMessage());
            }
        }
        
        
        
        
        @GetMapping("/user-tab-actions-by-page")
        public ResponseEntity<List<String>> getActions(
                @RequestParam("userId") String userId,
                @RequestParam("pageName") String pageName) {

            List<String> actions = tabRepo.getActionsByUserAndPageName(userId, pageName);

            return ResponseEntity.ok(actions);
        }
        
       

        
        
        // pageAction Based Controller Started from here
        
        
        @GetMapping("/page-actions")
        public List<PageActionProjection> getPageActions(@RequestParam int pageId) {
            return tabRepo.getActionsByPageId(pageId);
        }
        
        
        @Autowired
        private PageActionRepository pageActionRepo;

        @PostMapping("/save-page-actions")
        public ResponseEntity<Object> savePageActions(@RequestBody Map<String, Object> payload) {

            try {

                List<Map<String, Object>> addedActions =
                        (List<Map<String, Object>>) payload.getOrDefault("newActions", new ArrayList<>());

                List<Map<String, Object>> removedActions =
                        (List<Map<String, Object>>) payload.getOrDefault("oldActions", new ArrayList<>());

                String userId = (String) payload.get("userId");
                String createdBy = (String) payload.get("createdBy");

                if (userId == null) {
                    return ResponseEntity.badRequest()
                            .body(new ResponseMessage("Error", "UserId missing"));
                }

                // ✅ INSERT
                for (Map<String, Object> item : addedActions) {

                    UserPageAction act = new UserPageAction();

                    act.setUserId(userId);
                    act.setModuleId((Integer) item.get("moduleId"));
                    act.setPageId((Integer) item.get("pageId"));
                    act.setActionName((String) item.get("action"));
                    act.setCreatedBy(createdBy);
                    act.setCreatedDate(LocalDateTime.now());
                    act.setIsDeleted(0);

                    pageActionRepo.save(act);
                }

                // ✅ SOFT DELETE
                for (Map<String, Object> item : removedActions) {

                    pageActionRepo.softDelete(
                            userId,
                            (Integer) item.get("moduleId"),
                            (Integer) item.get("pageId"),
                            (String) item.get("action"),
                            createdBy
                    );
                }

                return ResponseEntity.ok(new ResponseMessage("Success", "Page Actions saved"));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMessage("Error", e.getMessage()));
            }
        }
        
        @GetMapping("/user-page-actions")
        public List<Map<String, Object>> getUserPageActions(@RequestParam String userId) {

            return pageActionRepo.findByUser(userId).stream().map(a -> {
                Map<String, Object> map = new HashMap<>();
                map.put("moduleId", a.getModuleId());
                map.put("pageId", a.getPageId());
                map.put("action", a.getActionName());
                return map;
            }).toList();
        }
        
        
        @GetMapping("/page/actions")
        public ResponseEntity<List<String>> getPageActions(
                @RequestParam String userId,
                @RequestParam String pageName) {

            List<String> actions = pageActionRepo.findActionsByUserAndPage(userId, pageName);
            return ResponseEntity.ok(actions);
        }
        
}
