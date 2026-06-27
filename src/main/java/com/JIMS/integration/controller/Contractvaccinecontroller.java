package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Contractvaccinemodel;
import com.JIMS.integration.repository.Contractvaccinerepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/contractvaccine")
public class Contractvaccinecontroller {

    @Autowired
    private Contractvaccinerepository cvRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<Contractvaccinemodel> getAllcontractvaccines() {
        return cvRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contractvaccinemodel> getcontractvaccineById(@PathVariable int id) {
        Optional<Contractvaccinemodel> item = cvRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> saveContractvaccine(@RequestBody Contractvaccinemodel item) {
        try {
        	Contractvaccinemodel savedItem = cvRepo.save(item);
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
    public ResponseEntity<Map<String, Object>> checkcvExists(@RequestParam("VaccineDesc") String vaccineDesc) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_CONTRACTVACCINE_MASTER WHERE Vaccine_desc = ?")) {
            ps.setString(1, vaccineDesc);
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
    public ResponseEntity<Object> updateContractvaccineyId(@PathVariable int id, @RequestBody Contractvaccinemodel updatedItem) {
        Optional<Contractvaccinemodel> existingOpt = cvRepo.findById(id);
        if (existingOpt.isPresent()) {
        	Contractvaccinemodel existing = existingOpt.get();

            if (updatedItem.getvaccineDesc() != null)
                existing.setvaccineDesc(updatedItem.getvaccineDesc());

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
        
            cvRepo.save(existing);
            return ResponseEntity.ok("Contract Vaccine updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    
    @GetMapping("/check-duplicate-update")
    public ResponseEntity<Map<String, Boolean>> checkVaccineDuplicateForUpdate(
            @RequestParam("VaccineDesc") String vaccineDesc,
            @RequestParam("vaccineId") int vaccineId) {

        boolean exists = cvRepo.existsByVaccineDescIgnoreCaseAndVacidNot(vaccineDesc, vaccineId);

        return ResponseEntity.ok(Map.of("exists", exists));
    }
    
    @GetMapping("/is-used/{vacId}")
    public ResponseEntity<Map<String, Boolean>> isVaccineUsed(
            @PathVariable int vacId) {

        String sql = "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE Vac_id = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vacId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean used = rs.getInt(1) > 0;
                return ResponseEntity.ok(Map.of("used", used));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // If error occurs, assume not used
        return ResponseEntity.ok(Map.of("used", false));
    }
}
