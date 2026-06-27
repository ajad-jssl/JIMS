package com.JIMS.integration.controller;

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

import com.JIMS.integration.entity.ReasonSelection;
import com.JIMS.integration.interfaces.Reason_Selection_Service;

@CrossOrigin
@RestController
@RequestMapping("/api/reason_selection")
public class ReasonSelectionMasterController {

    @Autowired
    private Reason_Selection_Service reason_service;

    @PostMapping
    public ResponseEntity<Object> addReason(@RequestBody ReasonSelection reasonSelection) {
        reason_service.addReason(reasonSelection);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("New Reason is Added Successfully");
    }

    @GetMapping("getAll")
    public ResponseEntity<Object> getAllReasons() {
        try {
            List<ReasonSelection> allReasons = reason_service.getAllReason();
            return ResponseEntity.status(HttpStatus.OK).body(allReasons);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @GetMapping("get/{id}")
    public ResponseEntity<Object> getReasonById(@PathVariable Integer id) {
        try {
            Optional<ReasonSelection> reasonById = reason_service.getReasonById(id);

            if (reasonById.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(reasonById.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reason not found");
            }

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @GetMapping("check/reason")
    public ResponseEntity<Object> checkDuplicate(@RequestParam String reasonName) {

        List<ReasonSelection> duplicate = reason_service.checkDuplicate(reasonName);

        if (!duplicate.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of("exists", true,
                           "reasonId", duplicate.get(0).getReasonId())
            );
        } else {
            return ResponseEntity.ok(Map.of("exists", false));
        }
    }

    @PutMapping("modified/{id}")
    public ResponseEntity<Object> modifyReason(
            @PathVariable Integer id,
            @RequestBody ReasonSelection reasonSelection) {

        Optional<ReasonSelection> reasonById = reason_service.getReasonById(id);
        
        int count = reason_service.checkReason(id);
        
        if(count>0) {
        	  return ResponseEntity.status(HttpStatus.CONFLICT).body("The Transcation is Already exists for this Reason");
        }

        if (reasonById.isPresent()) {

            ReasonSelection existingReason = reasonById.get();

            if (reasonSelection.getReasonName() != null) {
                existingReason.setReasonName(reasonSelection.getReasonName());
            }
            if (reasonSelection.getCreatedBy() != null) {
                existingReason.setCreatedBy(reasonSelection.getCreatedBy());
            }
            if (reasonSelection.getCreatedDate() != null) {
                existingReason.setCreatedDate(reasonSelection.getCreatedDate());
            }
            if (reasonSelection.getModifiedBy() != null) {
                existingReason.setModifiedBy(reasonSelection.getModifiedBy());
            }
            if (reasonSelection.getModifiedDate() != null) {
                existingReason.setModifiedDate(reasonSelection.getModifiedDate());
            }
            if (reasonSelection.getStatus() != null) {
                existingReason.setStatus(reasonSelection.getStatus());
            }

            reason_service.addReason(existingReason);
            return ResponseEntity.ok(existingReason);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Reason not found with ID: " + id);
    }

    @GetMapping("/mapping")
    public ResponseEntity<Object> getReasonIdAndNameOnly() {
        try {
            List<Object[]> list = reason_service.getReasonIdAndNameOnly();
            List<Map<String, Object>> result = new ArrayList<>();

            for (Object[] row : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("reasonId", ((Number) row[0]).intValue());
                map.put("reasonName", (String) row[1]);
                result.add(map);
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getReasonCounts() {
        Long count = reason_service.getReasonCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}
