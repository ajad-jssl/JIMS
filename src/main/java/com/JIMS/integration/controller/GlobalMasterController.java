package com.JIMS.integration.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.GloblaModule;
import com.JIMS.integration.entity.PageActionsModel;
import com.JIMS.integration.entity.Pagesmodel;
import com.JIMS.integration.repository.GlobalModulesRepository;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin
public class GlobalMasterController {

    @Autowired
    private GlobalModulesRepository modulesRepository;

    
    
    // ================= GET ALL =================

    @GetMapping("/getAll")
    public List<GloblaModule> getAllModules() {

        return modulesRepository.findByStatusOrderByModuleIDAsc(0);
    }

    
    
    // ================= GET BY ID =================

    @GetMapping("/get/{id}")
    public ResponseEntity<GloblaModule> getModuleById(
            @PathVariable Long id) {

        Optional<GloblaModule> module =
                modulesRepository.findById(id);

        return module.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    
    // ================= INSERT =================

    @PostMapping
    public ResponseEntity<?> createModule(
            @RequestBody GloblaModule module) {

        boolean exists = modulesRepository
                .existsByModuleNameIgnoreCaseAndStatus(
                        module.getModuleName(),
                        0);

        if (exists) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Module already exists");
        }

        GloblaModule saved = modulesRepository.save(module);

        return ResponseEntity.ok(saved);
    }

    
    
    // ================= DUPLICATE CHECK =================

    @GetMapping("/check/module")
    public ResponseEntity<?> checkModule(
            @RequestParam String moduleName) {

        boolean exists = modulesRepository
                .existsByModuleNameIgnoreCaseAndStatus(
                        moduleName,
                        0);

        return ResponseEntity.ok(
                java.util.Collections.singletonMap(
                        "exists",
                        exists));
    }

    
    
    // ================= UPDATE =================

    @PutMapping("/modified/{id}")
    public ResponseEntity<?> updateModule(
            @PathVariable Long id,
            @RequestBody GloblaModule updatedModule) {

        Optional<GloblaModule> existingOpt =
                modulesRepository.findById(id);

        if (!existingOpt.isPresent()) {

            return ResponseEntity.notFound().build();
        }

        boolean duplicateExists =
                modulesRepository
                .existsByModuleNameIgnoreCaseAndStatusAndModuleIDNot(
                        updatedModule.getModuleName(),
                        0,
                        id);

        if (duplicateExists) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Module already exists");
        }

        GloblaModule existing = existingOpt.get();

        existing.setModuleName(updatedModule.getModuleName());

        existing.setModifiedBy(
                updatedModule.getModifiedBy());

        existing.setModifiedDate(
                updatedModule.getModifiedDate());

        modulesRepository.save(existing);

        return ResponseEntity.ok("Updated Successfully");
    }
    
    
    
    
    // page Controller starts from here 
    
    
    
    @GetMapping("/getAllPages")
    public List<Object[]> getAllPages() {
        return modulesRepository.getAllPages();
    }
    
    
    @GetMapping("/getAllModules")
    public List<Map<String, Object>> getAllpageModules() {

        List<Object[]> rows = modulesRepository.getAllModules();

        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : rows) {

            Map<String, Object> map = new HashMap<>();

            map.put("moduleID", row[0]);
            map.put("moduleName", row[1]);

            response.add(map);
        }

        return response;
    }
    
    @PostMapping("/insertPage")
    public Map<String, Object> insertPage(
            @RequestBody Pagesmodel page) {

        Map<String, Object> response = new HashMap<>();

        int count = modulesRepository.checkDuplicatePage(
                page.getPageName(),
                page.getDisplayName(),
                page.getModuleID()
        );

        if (count > 0) {

            response.put("status", "duplicate");

            response.put("message",
                    "Page URL or Display Name already exists for this module");

            return response;
        }

        modulesRepository.insertPage(
                page.getModuleID(),
                page.getPageName(),
                page.getDisplayName(),
                page.getCreated_by()
        );

        response.put("status", "success");

        response.put("message", "Page created successfully");

        return response;
    }
    
    @GetMapping("/getPageById/{id}")
    public Map<String, Object> getPageById(
            @PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();

        List<Object[]> rows = modulesRepository.getPageById(id);

        if (rows == null || rows.isEmpty()) {

            response.put("status", "error");
            response.put("message", "Page not found");

            return response;
        }

        Object[] obj = rows.get(0);

        response.put("moduleID", obj[0]);
        response.put("pageName", obj[1]);
        response.put("displayName", obj[2]);

        return response;
    }
    
    
    @PutMapping("/updatePage/{id}")
    public Map<String, Object> updatePage(
            @PathVariable Integer id,
            @RequestBody Pagesmodel page) {

        Map<String, Object> response = new HashMap<>();

        int displayCount =
                modulesRepository.checkDuplicateDisplayNameForUpdate(
                        page.getDisplayName(),
                        id
                );

        if (displayCount > 0) {

            response.put("status", "duplicate");

            response.put("message", "Display Name already exists");

            return response;
        }

        int pageCount =
                modulesRepository.checkDuplicatePageNameForUpdate(
                        page.getPageName(),
                        id
                );

        if (pageCount > 0) {

            response.put("status", "duplicate");

            response.put("message", "Page URL already exists");

            return response;
        }

        modulesRepository.updatePage(
                page.getModuleID(),
                page.getPageName(),
                page.getDisplayName(),
                page.getModified_by(),
                id
        );

        response.put("status", "success");

        response.put("message", "Page updated successfully");

        return response;
    }
    
    
    
    // pageAction controllers 
    
    
    
    @GetMapping("/getAllpageAction")
    public List<Map<String, Object>> getAllpageactions() {

        List<Object[]> rows = modulesRepository.getAllpagesac();

        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : rows) {

            Map<String, Object> map = new HashMap<>();

            map.put("pageID", row[0]);
            map.put("pagNameName", row[1]);

            response.add(map);
        }

        return response;
    }
    
    
    @GetMapping("/getAllAction")
    public List<Map<String, Object>> getAllactions() {

        List<Object[]> rows = modulesRepository.getAllActions();

        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : rows) {

            Map<String, Object> map = new HashMap<>();

            map.put("actionId", row[0]);
            map.put("actionName", row[1]);

            response.add(map);
        }

        return response;
    }
    
    @GetMapping("/getAllPageActionsTable")
    public List<Object[]> getAllPageActionsTable() {

        return modulesRepository.getAllPageActionsTable();
    }
    
    
    
 // ================= CONTROLLER =================

    @PostMapping("/insertPageAction")
    public Map<String, Object> insertPageAction(
            @RequestBody PageActionsModel action) {

        Map<String, Object> response = new HashMap<>();

        int count =
                modulesRepository.checkDuplicatePageAction(
                        action.getPageID(),
                        action.getActionID()
                );

        if (count > 0) {

            response.put("status", "duplicate");

            response.put(
                    "message",
                    "Page Action already exists"
            );

            return response;
        }

        modulesRepository.insertPageAction(
                action.getPageID(),
                action.getActionID(),
                action.getCreated_by()
        );

        response.put("status", "success");

        response.put(
                "message",
                "Page Action created successfully"
        );

        return response;
    }
    
    
    
    @GetMapping("/getPageActionById/{id}")
    public Map<String, Object> getPageActionById(
            @PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();

        List<Object[]> rows =
                modulesRepository.getPageActionById(id);

        if(rows == null || rows.isEmpty()){

            response.put("status", "error");

            return response;
        }

        Object[] row = rows.get(0);

        response.put("pageID", row[0]);
        response.put("actionID", row[1]);

        return response;
    }
    
    
    @PutMapping("/updatePageAction/{id}")
    public Map<String, Object> updatePageAction(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> payload) {

        Map<String, Object> response = new HashMap<>();

        Integer pageID =
                Integer.parseInt(payload.get("pageID").toString());

        Integer actionID =
                Integer.parseInt(payload.get("actionID").toString());

        String modifiedBy =
                payload.get("modifiedBy").toString();

        int count =
                modulesRepository
                .checkDuplicatePageActionForUpdate(
                        pageID,
                        actionID,
                        id
                );

        if(count > 0){

            response.put("status", "duplicate");

            response.put(
                    "message",
                    "Combination already exists"
            );

            return response;
        }

        modulesRepository.updatePageAction(
                pageID,
                actionID,
                modifiedBy,
                id
        );

        response.put("status", "success");

        response.put(
                "message",
                "Updated Successfully"
        );

        return response;
    }
}