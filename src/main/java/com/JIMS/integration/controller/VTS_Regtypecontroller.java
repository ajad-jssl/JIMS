package com.JIMS.integration.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.VTS_Regtypemodel;
import com.JIMS.integration.interfaces.VTS_regtypeservice;
@CrossOrigin
@RestController
@RequestMapping("/api/Regtype")
public class VTS_Regtypecontroller {
	
	@Autowired
	private VTS_regtypeservice regtypeservice;
	@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody VTS_Regtypemodel item) {
        try {
        	VTS_Regtypemodel savedItem = regtypeservice.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
    }
	
	@PutMapping("/{tid}")
	public ResponseEntity<Object> updateRegtypeById(@PathVariable int tid, @RequestBody VTS_Regtypemodel regtype) {
	    Optional<VTS_Regtypemodel> existingRegtype = regtypeservice.getItemById(tid);

	    if (existingRegtype.isPresent()) {
	        VTS_Regtypemodel updatedRegtype = existingRegtype.get();

	        // Check and update only the provided fields
	        if (regtype.getRegtype() != null) {
	            updatedRegtype.setRegtype(regtype.getRegtype());
	        }
	        if (regtype.getCreated_by() != null) {
	            updatedRegtype.setCreated_by(regtype.getCreated_by());
	        }
	        if (regtype.getCreated_date() != null) {
	            updatedRegtype.setCreated_date(regtype.getCreated_date());
	        }
	        if (regtype.getModified_by() != null) {
	            updatedRegtype.setModified_by(regtype.getModified_by());
	        }
	        if (regtype.getModified_date() != null) {
	            updatedRegtype.setModified_date(regtype.getModified_date());
	        }

	        // Save the updated regtype
	        regtypeservice.saveOrUpdateItem(updatedRegtype);

	        return ResponseEntity.status(HttpStatus.OK)
	                             .body(new ResponseMessage("Success", "Registration type updated successfully"));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(new ResponseMessage("Error", "Registration type not found"));
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
	public ResponseEntity<List<VTS_Regtypemodel>> getAllItems() {
	    List<VTS_Regtypemodel> items = regtypeservice.getAllItems();
	    if (items.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
	    }
	    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
	}

	@GetMapping("/{id}")
	public ResponseEntity<VTS_Regtypemodel> getItemById(@PathVariable int id) {
	    Optional<VTS_Regtypemodel> item = regtypeservice.getItemById(id);
	    if (item.isPresent()) {
	        return new ResponseEntity<>(item.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	
	@Autowired
	@Qualifier("jimsDataSource")
	private DataSource jimsDataSource;

	@GetMapping("/filter")
	public @ResponseBody List<Map<String, Object>> getFilteredRegtypes(
	        @RequestParam("include") boolean include) {

	    // Build SQL query based on `include` flag
	    String sql = "SELECT regtype, tid FROM VTS_Regtype WHERE tid " + (include ? "IN" : "NOT IN") + " (1, 2)";

	    List<Map<String, Object>> resultList = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            Map<String, Object> resultMap = new HashMap<>();
	            resultMap.put("regtype", resultSet.getString("regtype"));
	            resultMap.put("tid", resultSet.getInt("tid"));
	            resultList.add(resultMap);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return resultList;
	}
	
	@GetMapping("/check")
	public ResponseEntity<Map<String, Object>> checkcompanyExists(@RequestParam("regtype") String regtype) {
	    String sql = "SELECT COUNT(*) FROM VTS_Regtype WHERE regtype = ?";
	    Map<String, Object> response = new HashMap<>();
	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, regtype);
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
	
	
	
	@GetMapping("/is-regtyp-used/{regtyp}")
	public ResponseEntity<Map<String, Boolean>> isRegTypeUsed(
	        @PathVariable String regtyp) {

	    String sql = "SELECT COUNT(*) FROM VTS_invoice_exit WHERE regtyp = ? AND outtime is null";

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, regtyp);
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
