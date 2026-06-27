package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Contractbloodgroupmodel;
import com.JIMS.integration.repository.Contractbloodgrouprepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/contractbloodgroup")
public class Contractbloodgroupcontroller {

    @Autowired
    private Contractbloodgrouprepository cbgRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<Contractbloodgroupmodel> getAllContractbloodgroups() {
        return cbgRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contractbloodgroupmodel> getContractbloodgrpyId(@PathVariable int id) {
        Optional<Contractbloodgroupmodel> item = cbgRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> saveNatureOfJob(@RequestBody Contractbloodgroupmodel item) {
        try {
        	Contractbloodgroupmodel savedItem = cbgRepo.save(item);
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
    public ResponseEntity<Map<String, Object>> checkcbgExists(@RequestParam("blg") String blg) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_BLOODGROUP WHERE blg = ?")) {
            ps.setString(1, blg);
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
    public ResponseEntity<Object> updateNatureOfJobById(@PathVariable int id, @RequestBody Contractbloodgroupmodel updatedItem) {
        Optional<Contractbloodgroupmodel> existingOpt = cbgRepo.findById(id);
        if (existingOpt.isPresent()) {
        	Contractbloodgroupmodel existing = existingOpt.get();

            if (updatedItem.getblg() != null)
                existing.setblg(updatedItem.getblg());

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
        
            cbgRepo.save(existing);
            return ResponseEntity.ok("Nature of Job updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    
 // Duplicate check before updating Blood Group
    @GetMapping("/check-update")
    public ResponseEntity<Map<String, Boolean>> checkBloodGroupDuplicateForUpdate(
            @RequestParam("blg") String blg,
            @RequestParam("blgId") int blgId) {

        // Make sure this method exists in your repository:
        // boolean existsByBlgIgnoreCaseAndBlIdNot(String blg, int blId);
        boolean exists = cbgRepo.existsByBlgIgnoreCaseAndBlIdNot(blg, blgId);

        return ResponseEntity.ok(Map.of("exists", exists));
    }
    
    @GetMapping("/is-used/{blgId}")
    public ResponseEntity<Map<String, Boolean>> isBloodGroupUsed(@PathVariable int blgId) {

        String sql = "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE BG_id = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blgId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean used = rs.getInt(1) > 0;
                return ResponseEntity.ok(Map.of("used", used));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Map.of("used", false));
    }

    
}
