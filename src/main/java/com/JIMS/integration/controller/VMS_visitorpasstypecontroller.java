package com.JIMS.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import com.JIMS.integration.controller.VMS_visitorcontroller.ResponseMessage;
import com.JIMS.integration.entity.VMS_passtypemodel;
import com.JIMS.integration.entity.VTS_Regtypemodel;
import com.JIMS.integration.repository.VMS_passtyperepository;


@CrossOrigin
@RestController
@RequestMapping("/api/vms")

public class VMS_visitorpasstypecontroller {

	// ============ Pass Type APIs ============
	
	 @Autowired
	    private VMS_passtyperepository passTypeRepository;
   

    @PostMapping
    public ResponseEntity<Object> createPassType(@RequestBody VMS_passtypemodel passType) {
        try {
            VMS_passtypemodel saved = passTypeRepository.save(passType);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Success", "Pass type inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error", "Failed to insert pass type"));
        }
    }

    @GetMapping
    public List<VMS_passtypemodel> getAllPassTypes() {
        return passTypeRepository.findAll();
    }
    
    @GetMapping("/{pass_id}")
    public ResponseEntity<VMS_passtypemodel> getItemById(@PathVariable int pass_id) {
        Optional<VMS_passtypemodel> item = passTypeRepository.findById(pass_id);
        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/passtype/{pass_id}")
    public ResponseEntity<Object> updatePassTypeById(@PathVariable String pass_id,
                                                     @RequestBody VMS_passtypemodel input) {
        try {
            int id = Integer.parseInt(pass_id);
            Optional<VMS_passtypemodel> existingOpt = passTypeRepository.findById(id);

            if (existingOpt.isPresent()) {
                VMS_passtypemodel existing = existingOpt.get();

                Optional.ofNullable(input.getPassName()).ifPresent(existing::setPassName);
                Optional.ofNullable(input.getCreatedBy()).ifPresent(existing::setCreatedBy);
                Optional.ofNullable(input.getCreatedDate()).ifPresent(existing::setCreatedDate);
                Optional.ofNullable(input.getModifiedBy()).ifPresent(existing::setModifiedBy);
                Optional.ofNullable(input.getModifiedDate()).ifPresent(existing::setModifiedDate);

                passTypeRepository.save(existing);
                return ResponseEntity.ok(new ResponseMessage("Success", "Pass type updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Error", "Pass type not found"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Error", "Invalid pass_id format"));
        }
    }

    @Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;
    @GetMapping("/checkvisitorpasstype")
    public ResponseEntity<Map<String, Object>> checkIfPassTypeExists(@RequestParam("Pass_name") String passName) {
        String sql = "SELECT COUNT(*) FROM VMS_Pass_type WHERE Pass_name = ?";
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = jimsDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    response.put("exists", rs.getInt(1) > 0);
                }
            }
        } catch (SQLException e) {
            response.put("error", "Database error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
