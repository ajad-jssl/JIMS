package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Contractmedcheckupmodel;
import com.JIMS.integration.repository.Contractmedcheckuprepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/contractmedcheckup")
public class Contractmedcheckupcontroller {

    @Autowired
    private Contractmedcheckuprepository cmcRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<Contractmedcheckupmodel> getAllContractmedcheckups() {
        return cmcRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contractmedcheckupmodel> getContractmedcheckupById(@PathVariable int id) {
        Optional<Contractmedcheckupmodel> item = cmcRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> saveContractmedcheckup(@RequestBody Contractmedcheckupmodel item) {
        try {
        	Contractmedcheckupmodel savedItem = cmcRepo.save(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "Success", "message", "Record inserted successfully"));
        } catch (Exception e) {
        	 e.printStackTrace(); // Add this line to print the real error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "Error", "message", "Record not inserted. Please try again."));
        }
    }

    // Check if NoJ Description already exists
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkcmcExists(@RequestParam("mcDesc") String mcDesc) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_CONTRACTMEDICALCHECKUP_MASTER WHERE MC_desc = ?")) {
            ps.setString(1, mcDesc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                response.put("exists", rs.getInt(1) > 0);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.put("exists", false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Update by ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateContractmedcheckupById(@PathVariable int id, @RequestBody Contractmedcheckupmodel updatedItem) {
        Optional<Contractmedcheckupmodel> existingOpt = cmcRepo.findById(id);
        if (existingOpt.isPresent()) {
        	Contractmedcheckupmodel existing = existingOpt.get();

            if (updatedItem.getmcDesc() != null)
                existing.setmcDesc(updatedItem.getmcDesc());

            if (updatedItem.getCreatedby() != null)
                existing.setCreatedby(updatedItem.getCreatedby());

            if (updatedItem.getCreateddt() != null)
                existing.setCreateddt(updatedItem.getCreateddt());

            if (updatedItem.getUpdatedBy() != null)
                existing.setUpdatedBy(updatedItem.getUpdatedBy());

            if (updatedItem.getUpdatedDate() != null)
                existing.setUpdatedDate(updatedItem.getUpdatedDate());

			/*
			 * if (updatedItem.getFactory_id() != 0)
			 * existing.setFactory_id(updatedItem.getFactory_id());
			 */
        
            cmcRepo.save(existing);
            return ResponseEntity.ok("Contract Medical Checkup updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    
    @GetMapping("/check-update")
    public ResponseEntity<Map<String, Boolean>> checkMedCheckupDuplicateForUpdate(
            @RequestParam("mcDesc") String mcDesc,
            @RequestParam("mcId") int mcId) {

        boolean exists = cmcRepo.existsByMcDescIgnoreCaseAndMcidNot(mcDesc, mcId);

        return ResponseEntity.ok(Map.of("exists", exists));
    }
    @GetMapping("/is-used/{mcId}")
    public ResponseEntity<Map<String, Boolean>> isMedicalCheckupUsed(@PathVariable int mcId) {
        Map<String, Boolean> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE medical = ?"
             )) {
            ps.setInt(1, mcId); // mcId corresponds to the Medical Checkup ID
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                response.put("used", rs.getInt(1) > 0);
            } else {
                response.put("used", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("used", false);
        }
        return ResponseEntity.ok(response);
    }

}
