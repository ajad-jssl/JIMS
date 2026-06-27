
package com.JIMS.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.JIMS.integration.entity.ResponseMessage;
import com.JIMS.integration.entity.useractionpermissionmodel;
import com.JIMS.integration.repository.useractinpermissioninterface;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class useractionpermissioncontroller {

    private static final int INVOICE_MODULE_ID = 1; // Replace with actual Invoice module ID

    @Autowired
    private useractinpermissioninterface userActionPermissionRepository;

    @GetMapping("/user-action-permissions")
    public List<String> getUserActionPermissions(@RequestParam("userId") String userId, @RequestParam("moduleId") int moduleId) {
        List<String> actions = userActionPermissionRepository.findActionsByUserIdAndModuleId(userId, moduleId);
        // Filter out Print for non-Invoice modules
        if (moduleId != INVOICE_MODULE_ID) {
            actions.remove("Print");
        }
        return actions;
    }

	/*
	 * @PostMapping("/save-action-permissions") public ResponseEntity<String>
	 * saveActionPermissions(@RequestBody List<useractionpermissionmodel>
	 * permissions) { if (permissions.isEmpty()) { return
	 * ResponseEntity.ok("No action permissions provided"); } String userId =
	 * permissions.get(0).getUserId();
	 * userActionPermissionRepository.deleteByUserId(userId); // Filter out Print
	 * for non-Invoice modules List<useractionpermissionmodel> validPermissions =
	 * permissions.stream() .filter(p -> !p.getAction().equals("Print") ||
	 * p.getModuleId() == INVOICE_MODULE_ID) .toList();
	 * userActionPermissionRepository.saveAll(validPermissions); return
	 * ResponseEntity.ok("Action permissions saved successfully"); }
	 */
//    @PostMapping("/save-action-permissions")
//    public ResponseEntity<String> saveActionPermissions(@RequestBody JsonNode jsonNode) {
//        ObjectMapper mapper = new ObjectMapper();
//        List<useractionpermissionmodel> permissions;
//
//        try {
//            if (jsonNode.isArray()) {
//                // JSON is an array of permissions
//                permissions = mapper.convertValue(jsonNode, new TypeReference<List<useractionpermissionmodel>>() {});
//            } else if (jsonNode.isObject()) {
//                // JSON is a single permission object
//                useractionpermissionmodel singlePermission = mapper.convertValue(jsonNode, useractionpermissionmodel.class);
//                permissions = List.of(singlePermission);
//            } else {
//                return ResponseEntity.badRequest().body("Invalid JSON format");
//            }
//
//            if (permissions.isEmpty()) {
//                return ResponseEntity.ok("No action permissions provided");
//            }
//
//            // Group permissions by moduleId
//            String userId = permissions.get(0).getUserId();
//            permissions.stream()
//                    .map(useractionpermissionmodel::getModuleId)
//                    .distinct()
//                    .forEach(moduleId -> userActionPermissionRepository.deleteByUserIdAndModuleId(userId, moduleId));
//
//            // Filter out Print for non-Invoice modules
//            List<useractionpermissionmodel> validPermissions = permissions.stream()
//                    .filter(p -> !p.getAction().equals("Print") || p.getModuleId() == INVOICE_MODULE_ID)
//                    .toList();
//
//            userActionPermissionRepository.saveAll(validPermissions);
//            return ResponseEntity.ok("Action permissions saved successfully");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Failed to parse request body");
//        }
//    }
    
    
    
    @PostMapping("/save-action-permissions")
    public ResponseEntity<Object> saveActionPermissions(@RequestBody Map<String, Object> payload) {
        try {

            List<Map<String, Object>> addedActions =
                    (List<Map<String, Object>>) payload.get("newActions");

            List<Map<String, Object>> removedActions =
                    (List<Map<String, Object>>) payload.get("oldActions");

            String userId = (String) payload.get("userId");
            String createdBy = (String) payload.get("createdBy");

            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(new ResponseMessage("Error", "UserId not found"));
            }
            System.out.println("INSERT COUNT: " + addedActions.size());
            System.out.println("DELETE COUNT: " + removedActions.size());

            // ✅ INSERT
            for (Map<String, Object> item : addedActions) {
                useractionpermissionmodel entity = new useractionpermissionmodel();
                entity.setUserId(userId);
                entity.setModuleId((Integer) item.get("moduleId"));
                entity.setAction((String) item.get("action"));
                entity.setCreatedBy(createdBy);
                entity.setCreatedDate(LocalDateTime.now());
                entity.setIsDelete(0);

                userActionPermissionRepository.save(entity);
            }

            // ✅ DELETE
            for (Map<String, Object> item : removedActions) {
                userActionPermissionRepository.softDelete(
                        userId,
                        (Integer) item.get("moduleId"),
                        (String) item.get("action"),
                        createdBy
                );
            }

            return ResponseEntity.ok(new ResponseMessage("Success", "Action permissions updated"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error", e.getMessage()));
        }
    }
    @DeleteMapping("/clear-action-permissions")
    @Transactional("integrationTransactionManager") // or correct bean name if using multiple managers
    public ResponseEntity<String> clearActionPermissions(@RequestParam("userId") String userId,
                                                         @RequestParam("moduleId") int moduleId) {
        userActionPermissionRepository.deleteByUserIdAndModuleId(userId, moduleId);
        return ResponseEntity.ok("Action permissions cleared.");
    }

}