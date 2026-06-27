package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Contractbusmodel;
import com.JIMS.integration.repository.Contractbusrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/contractbus")
public class Contractbuscontroller {

    @Autowired
    private Contractbusrepository cbRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<Contractbusmodel> getAllContractbus() {
        return cbRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contractbusmodel> getContractbusById(@PathVariable int id) {
        Optional<Contractbusmodel> item = cbRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> saveContractbus(@RequestBody Contractbusmodel item) {
        try {
        	Contractbusmodel savedItem = cbRepo.save(item);
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
    public ResponseEntity<Map<String, Object>> checkcbExists(@RequestParam("busDesc") String busDesc) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_CONTRACTBUS_MASTER WHERE Bus_desc = ?")) {
            ps.setString(1, busDesc);
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
    public ResponseEntity<Object> updateContractbusyId(@PathVariable int id, @RequestBody Contractbusmodel updatedItem) {
        Optional<Contractbusmodel> existingOpt = cbRepo.findById(id);
        if (existingOpt.isPresent()) {
        	Contractbusmodel existing = existingOpt.get();

            if (updatedItem.getbusDesc() != null)
                existing.setbusDesc(updatedItem.getbusDesc());

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
        
            cbRepo.save(existing);
            return ResponseEntity.ok("Contract Bus updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    @GetMapping("/check-update")
    public ResponseEntity<Map<String, Boolean>> checkBusDuplicateForUpdate(
            @RequestParam("busDesc") String busDesc,
            @RequestParam("busId") int busId) {

        boolean exists = cbRepo.existsByBusDescIgnoreCaseAndBusIdNot(busDesc, busId);

        return ResponseEntity.ok(Map.of("exists", exists));

}
    
    @GetMapping("/is-used/{busId}")
    public ResponseEntity<Map<String, Boolean>> isBusUsed(
            @PathVariable int busId) {

        String sql = "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE Bus_id = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, busId);
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
