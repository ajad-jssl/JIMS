package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Educationmodel;
import com.JIMS.integration.repository.Educationrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/education")
public class Educationcontroller {

    @Autowired
    private Educationrepository eduRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<Educationmodel> getAlleducation() {
        return eduRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Educationmodel> geteducationById(@PathVariable int id) {
        Optional<Educationmodel> item = eduRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> saveeducation(@RequestBody Educationmodel item) {
        try {
        	Educationmodel savedItem = eduRepo.save(item);
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
    public ResponseEntity<Map<String, Object>> checkeduExists(@RequestParam("eduDesc") String eduDesc) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_EDUCATION_MASTER WHERE edudesc = ?")) {
            ps.setString(1, eduDesc);
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
    public ResponseEntity<Object> updateeducationById(@PathVariable int id, @RequestBody Educationmodel updatedItem) {
        Optional<Educationmodel> existingOpt = eduRepo.findById(id);
        if (existingOpt.isPresent()) {
        	Educationmodel existing = existingOpt.get();

            if (updatedItem.geteduDesc() != null)
                existing.seteduDesc(updatedItem.geteduDesc());

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
        
            eduRepo.save(existing);
            return ResponseEntity.ok("Education updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    
    @GetMapping("/check-update")
    public ResponseEntity<Map<String, Boolean>> checkEducationDuplicateForUpdate(
            @RequestParam("eduDesc") String eduDesc,
            @RequestParam("eduId") int eduId) {

        boolean exists = eduRepo.existsByEduDescIgnoreCaseAndEduIdNot(eduDesc, eduId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
    @GetMapping("/is-used/{eduId}")
    public ResponseEntity<Map<String, Boolean>> isEducationUsed(@PathVariable int eduId) {

        String sql = "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE Education = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eduId);
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
