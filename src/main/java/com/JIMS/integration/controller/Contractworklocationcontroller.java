package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Contractworklocationmodel;
import com.JIMS.integration.repository.Contractworklocationrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/contractworklocation")
public class Contractworklocationcontroller {

    @Autowired
    private Contractworklocationrepository cwlRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<Contractworklocationmodel> getAllcontractworklocations() {
        return cwlRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contractworklocationmodel> getNatureOfJobById(@PathVariable int id) {
        Optional<Contractworklocationmodel> item = cwlRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> saveContractworklocn(@RequestBody Contractworklocationmodel item) {
        try {
        	Contractworklocationmodel savedItem = cwlRepo.save(item);
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
    public ResponseEntity<Map<String, Object>> checkcwlExists(@RequestParam("wlDesc") String wlDesc) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_CONTRACTWORKLOCATION_MASTER WHERE wldesc = ?")) {
            ps.setString(1, wlDesc);
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
    public ResponseEntity<Object> updatecontractworklocationyId(@PathVariable int id, @RequestBody Contractworklocationmodel updatedItem) {
        Optional<Contractworklocationmodel> existingOpt = cwlRepo.findById(id);
        if (existingOpt.isPresent()) {
        	Contractworklocationmodel existing = existingOpt.get();

            if (updatedItem.getwldesc() != null)
                existing.setwldesc(updatedItem.getwldesc());

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
        
            cwlRepo.save(existing);
            return ResponseEntity.ok("Contract Work Location updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    
    @GetMapping("/check-update")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateForUpdate(
            @RequestParam String wlDesc,
            @RequestParam int id) {

        boolean exists = cwlRepo
                .existsByWlDescIgnoreCaseAndWlidNot(wlDesc.trim(), id);

        return ResponseEntity.ok(Map.of("exists", exists));
    }
    
    
    @GetMapping("/is-used/{wlId}")
    public ResponseEntity<Map<String, Boolean>> isWorkLocationUsed(
            @PathVariable int wlId) {

        String sql = "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE work_loc = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wlId);
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
