package com.JIMS.integration.controller;

import com.JIMS.integration.entity.NatureOfJobModel;
import com.JIMS.integration.repository.NatureOfJobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/natureofjob")
public class Natureofjobcontroller {

    @Autowired
    private NatureOfJobRepository nojRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<NatureOfJobModel> getAllNatureOfJobs() {
        return nojRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<NatureOfJobModel> getNatureOfJobById(@PathVariable int id) {
        Optional<NatureOfJobModel> item = nojRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> saveNatureOfJob(@RequestBody NatureOfJobModel item) {
        try {
        	NatureOfJobModel savedItem = nojRepo.save(item);
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
    public ResponseEntity<Map<String, Object>> checkNojExists(@RequestParam("nojDesc") String nojDesc) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_NATURE_OF_JOB_MASTER WHERE Nojdesc = ?")) {
            ps.setString(1, nojDesc);
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
    public ResponseEntity<Object> updateNatureOfJobById(@PathVariable int id, @RequestBody NatureOfJobModel updatedItem) {
        Optional<NatureOfJobModel> existingOpt = nojRepo.findById(id);
        if (existingOpt.isPresent()) {
        	NatureOfJobModel existing = existingOpt.get();

            if (updatedItem.getNojDesc() != null)
                existing.setNojDesc(updatedItem.getNojDesc());

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
        
            nojRepo.save(existing);
            return ResponseEntity.ok("Nature of Job updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    @GetMapping("/check-update")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateForUpdate(
            @RequestParam String nojDesc,
            @RequestParam int id) {

        boolean exists = nojRepo
                .existsByNojDescIgnoreCaseAndNojIdNot(nojDesc.trim(), id);

        return ResponseEntity.ok(Map.of("exists", exists));
    }
    
    @GetMapping("/is-used/{nojId}")
    public ResponseEntity<Map<String, Boolean>> isNatureOfJobUsed(
            @PathVariable int nojId) {

        String sql = "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE noj = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nojId);
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
