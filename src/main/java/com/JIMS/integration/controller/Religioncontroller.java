package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Religionmodel;
import com.JIMS.integration.repository.Religionrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/religion")
public class Religioncontroller {

    @Autowired
    private Religionrepository religionRepo;

    @Autowired
    private DataSource jimsDataSource;

    // Get all records
    @GetMapping
    public List<Religionmodel> getAllreligions() {
        return religionRepo.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Religionmodel> getreligionById(@PathVariable int id) {
        Optional<Religionmodel> item = religionRepo.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create or Update
    @PostMapping
    public ResponseEntity<Object> savereligion(@RequestBody Religionmodel item) {
        try {
        	Religionmodel savedItem = religionRepo.save(item);
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
    public ResponseEntity<Map<String, Object>> checkreliExists(@RequestParam("religionDesc") String religionDesc) {
        Map<String, Object> response = new HashMap<>();
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_RELIGION_MASTER WHERE religiondesc = ?")) {
            ps.setString(1, religionDesc);
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
    public ResponseEntity<Object> updatreligionById(@PathVariable int id, @RequestBody Religionmodel updatedItem) {
        Optional<Religionmodel> existingOpt = religionRepo.findById(id);
        if (existingOpt.isPresent()) {
        	Religionmodel existing = existingOpt.get();

            if (updatedItem.getreligionDesc() != null)
                existing.setreligionDesc(updatedItem.getreligionDesc());

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
        
            religionRepo.save(existing);
            return ResponseEntity.ok("Education updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }
    
    @GetMapping("/check-update")
    public ResponseEntity<Map<String, Boolean>> checkReligionDuplicateForUpdate(
            @RequestParam("religionDesc") String religionDesc,
            @RequestParam("reliId") int reliId) {

        boolean exists =
                religionRepo.existsByReligionDescIgnoreCaseAndReliIdNot(religionDesc, reliId);

        return ResponseEntity.ok(Map.of("exists", exists));
    }
    @GetMapping("/is-used/{reliId}")
    public ResponseEntity<Map<String, Boolean>> isReligionUsed(@PathVariable int reliId) {

        String sql = "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE Religion = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reliId);  // Bind the religion ID
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
