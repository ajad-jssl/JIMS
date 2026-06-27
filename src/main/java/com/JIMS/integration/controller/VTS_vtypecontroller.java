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

import com.JIMS.integration.entity.VTS_vtypemodel;
import com.JIMS.integration.interfaces.VTS_vtypeservice;
@CrossOrigin
@RestController
@RequestMapping("/api/vtype")
public class VTS_vtypecontroller {
	
	
	@Autowired
	private VTS_vtypeservice vechicleTypeService;
	@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody VTS_vtypemodel item) {
        try {
        	VTS_vtypemodel savedItem = vechicleTypeService.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
    }
	
	@PutMapping("/{vid}")
	public ResponseEntity<Object> updateVechicleTypeById(@PathVariable int vid, @RequestBody VTS_vtypemodel vechicleType) {
	    Optional<VTS_vtypemodel> existingVechicleType = vechicleTypeService.getItemById(vid);

	    if (existingVechicleType.isPresent()) {
	        VTS_vtypemodel updatedVechicleType = existingVechicleType.get();

	        // Check and update only the provided fields
	        if (vechicleType.getVtype() != null) {
	            updatedVechicleType.setVtype(vechicleType.getVtype());
	        }
	        if (vechicleType.getCreated_by() != null) {
	            updatedVechicleType.setCreated_by(vechicleType.getCreated_by());
	        }
	        if (vechicleType.getCreated_date() != null) {
	            updatedVechicleType.setCreated_date(vechicleType.getCreated_date());
	        }
	        if (vechicleType.getModified_by() != null) {
	            updatedVechicleType.setModified_by(vechicleType.getModified_by());
	        }
	        if (vechicleType.getModified_date() != null) {
	            updatedVechicleType.setModified_date(vechicleType.getModified_date());
	        }

	        // Save the updated vechicle type
	        vechicleTypeService.saveOrUpdateItem(updatedVechicleType);

	        return ResponseEntity.status(HttpStatus.OK)
	                             .body(new ResponseMessage("Success", "Vehicle type updated successfully"));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(new ResponseMessage("Error", "Vehicle type not found"));
	    }
	}
	// Helper class for structured response messages
	public static class ResponseMessage {
	    private String status;
	    private String message;

	    public ResponseMessage(String status, String message) {
	        this.status = status;
	        this.message = message;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public String getMessage() {
	        return message;
	    }
	}
	@GetMapping
	public ResponseEntity<List<VTS_vtypemodel>> getAllItems() {
	    List<VTS_vtypemodel> items = vechicleTypeService.getAllItems();
	    if (items.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
	    }
	    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
	}

	@GetMapping("/{id}")
	public ResponseEntity<VTS_vtypemodel> getItemById(@PathVariable int id) {
	    Optional<VTS_vtypemodel> item = vechicleTypeService.getItemById(id);
	    if (item.isPresent()) {
	        return new ResponseEntity<>(item.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	@Autowired
	  @Qualifier("jimsDataSource")
	    private DataSource jimsDataSource;
	@GetMapping("/check")
	public ResponseEntity<Map<String, Object>> checkvehicleExists(@RequestParam("Vtype") String Vtype) {
	    String sql = "SELECT COUNT(*) FROM VTS_Vechicletype WHERE Vtype = ?";
	    Map<String, Object> response = new HashMap<>();
	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, Vtype);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            int count = resultSet.getInt(1);
	            response.put("exists", count > 0); // true if exists, false otherwise
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    response.put("exists", false);
	    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@GetMapping("/vehicletype/is-used/{vtypeId}")
	public ResponseEntity<Map<String, Boolean>> isVehicleTypeUsed(
	        @PathVariable String vtypeId) {

	    String sql = "SELECT COUNT(*) FROM VTS_invoice_exit WHERE vtype = ? AND outtime is null";

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, vtypeId);
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
