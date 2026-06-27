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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.JIMS.integration.entity.VTS_incponumbermodel;
import com.JIMS.integration.interfaces.VTS_incponumberservice;

@CrossOrigin
@RestController
@RequestMapping("/api/incponumber")
public class VTS_incponumbercontroller {
	@Autowired
	private VTS_incponumberservice invoicepoService;
	@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody VTS_incponumbermodel item) {
        try {
        	VTS_incponumbermodel savedItem = invoicepoService.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
    }
	@PutMapping("/{pono_id}")
	public ResponseEntity<Object> updatePoNumberById(@PathVariable int pono_id, @RequestBody VTS_incponumbermodel updatedData) {
	    Optional<VTS_incponumbermodel> existingRecordOpt = invoicepoService.getItemById(pono_id);

	    if (existingRecordOpt.isPresent()) {
	        VTS_incponumbermodel existing = existingRecordOpt.get();

	        if (updatedData.getVechile_id() != null) existing.setVechile_id(updatedData.getVechile_id());
	        if (updatedData.getCompany_id() != null) existing.setCompany_id(updatedData.getCompany_id());
	        if (updatedData.getPo_number() != null) existing.setPo_number(updatedData.getPo_number());
	        if (updatedData.getPo_date() != null) existing.setPo_date(updatedData.getPo_date());
	        if (updatedData.getInv_no() != null) existing.setInv_no(updatedData.getInv_no());
	        if (updatedData.getInv_date() != null) existing.setInv_date(updatedData.getInv_date());
	        if (updatedData.getDC_no() != null) existing.setDC_no(updatedData.getDC_no());
	        if (updatedData.getDC_date() != null) existing.setDC_date(updatedData.getDC_date());
	        if (updatedData.getQty() != 0.0) existing.setQty(updatedData.getQty());
	        if (updatedData.getWeight() != 0.0) existing.setWeight(updatedData.getWeight());
	        if (updatedData.getDescp() != null) existing.setDescp(updatedData.getDescp());
	        if (updatedData.getReject() != null) existing.setReject(updatedData.getReject());
	        if (updatedData.getCreated_by() != null) existing.setCreated_by(updatedData.getCreated_by());
	        if (updatedData.getCreated_date() != null) existing.setCreated_date(updatedData.getCreated_date());
	        if (updatedData.getModified_by() != null) existing.setModified_by(updatedData.getModified_by());
	        if (updatedData.getModified_date() != null) existing.setModified_date(updatedData.getModified_date());

	        // Save updated entity
	        invoicepoService.saveOrUpdateItem(existing);
	        
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(new ResponseMessage("Success", " invoice po updated successfully"));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("PO Number record not found");
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
	public ResponseEntity<List<VTS_incponumbermodel>> getAllItems() {
	    List<VTS_incponumbermodel> items = invoicepoService.getAllItems();
	    if (items.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
	    }
	    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<VTS_incponumbermodel> getItemById(@PathVariable int id) {
	    Optional<VTS_incponumbermodel> item = invoicepoService.getItemById(id);
	    if (item.isPresent()) {
	        return new ResponseEntity<>(item.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	
    // Delete an item by its ID
    @DeleteMapping("/{pono_id}")
    public ResponseEntity<Object> deleteItemById(@PathVariable int pono_id) {
        Optional<VTS_incponumbermodel> item = invoicepoService.getItemById(pono_id);
        if (item.isPresent()) {
        	invoicepoService.deleteItemById(pono_id); // Delete the item from the service layer
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new ResponseMessage("Success", "Record deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ResponseMessage("Error", "Item not found"));
        }
    }
    
	 @Autowired
	    @Qualifier("jimsDataSource")
	    private DataSource jimsDataSource;
	 @GetMapping("/jims/vehiclepo")
	    public @ResponseBody List<Map<String, Object>> getVehiclePODetails(
	            @RequestParam(value = "filterType") String filterType,
	            @RequestParam(value = "vechile_id") String vehicleId) {

	        String sql;
	     
	  
	            if (vehicleId == null || vehicleId.trim().isEmpty()) {
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehicle_id is required for filterType=byVehicleId");
	            }
	            if ("byVehicleId".equalsIgnoreCase(filterType)) {
	            sql = "SELECT        pon.Pono_id, pon.Company_id, cm.Company_name, cm.Address, sm.State_name, city.City_name, pon.Po_number, pon.Po_date, pon.Inv_no, pon.Inv_date, pon.DC_no, pon.DC_date, pon.qty, pon.weight, pon.Descp, "
	            		+ "                         pon.vechile_id "
	            		+ "FROM            dbo.VTS_invexitponumber AS pon LEFT OUTER JOIN "
	            		+ "                         dbo.VTS_invoice_exit AS ve ON pon.vechile_id = ve.vechile_id LEFT OUTER JOIN "
	            		+ "                         dbo.VMS_Companymaster AS cm ON pon.Company_id = cm.Company_id LEFT OUTER JOIN "
	            		+ "                         dbo.VMS_Citymaster AS city ON cm.City_id = city.City_id LEFT OUTER JOIN "
	            		+ "                         dbo.VMS_Statemaster AS sm ON cm.State_id = sm.State_id WHERE pon.vechile_id = ? AND pon.Reject IS NULL and (pon.is_deleted =0 or pon.is_deleted is null)";
	        } else if ("byReject".equalsIgnoreCase(filterType)) {
	            sql = "SELECT        pon.Pono_id, pon.Company_id, cm.Company_name, cm.Address, sm.State_name, city.City_name, pon.Po_number, pon.Po_date, pon.Inv_no, pon.Inv_date, pon.DC_no, pon.DC_date, pon.qty, pon.weight, pon.Descp, "
	            		+ "                         pon.vechile_id "
	            		+ "FROM            dbo.VTS_invexitponumber AS pon LEFT OUTER JOIN "
	            		+ "                         dbo.VTS_invoice_exit AS ve ON pon.vechile_id = ve.vechile_id LEFT OUTER JOIN "
	            		+ "                         dbo.VMS_Companymaster AS cm ON pon.Company_id = cm.Company_id LEFT OUTER JOIN "
	            		+ "                         dbo.VMS_Citymaster AS city ON cm.City_id = city.City_id LEFT OUTER JOIN "
	            		+ "                         dbo.VMS_Statemaster AS sm ON cm.State_id = sm.State_id WHERE pon.vechile_id = ? and pon.Reject = '1' AND ve.Securityreject_remarks IS NULL";
	        } else {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filterType. Use 'byVehicleId' or 'byReject'");
	        }

	        List<Map<String, Object>> resultList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        
	                preparedStatement.setString(1, vehicleId);
	        

	            try (ResultSet rs = preparedStatement.executeQuery()) {
	                while (rs.next()) {
	                    Map<String, Object> resultMap = new HashMap<>();
	                    resultMap.put("vechile_id", rs.getString("vechile_id"));
	                    resultMap.put("pono_id", rs.getInt("pono_id"));
	                    resultMap.put("Company_name", rs.getString("Company_name"));
	                    resultMap.put("Address", rs.getString("Address"));
	                    resultMap.put("State_name", rs.getString("State_name"));
	                    resultMap.put("City_name", rs.getString("City_name"));
	                    resultMap.put("Po_number", rs.getString("Po_number"));
	                    resultMap.put("Po_date", rs.getDate("Po_date")); // Convert to string if needed
	                    resultMap.put("Inv_no", rs.getString("Inv_no"));
	                    resultMap.put("Inv_date", rs.getDate("Inv_date"));
	                    resultMap.put("DC_no", rs.getString("DC_no"));
	                    resultMap.put("DC_date", rs.getDate("DC_date"));
	                    resultMap.put("qty", rs.getObject("qty")); // Could be Integer/BigDecimal
	                    resultMap.put("weight", rs.getObject("weight")); // Could be Double/BigDecimal
	                    resultMap.put("Descp", rs.getString("Descp"));
	                    resultMap.put("company_id", rs.getInt("company_id"));
	                    resultList.add(resultMap);
	                }
	            }

	        } catch (SQLException e) {
	            // Replace with proper logging (e.g., SLF4J)
	            e.printStackTrace();
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching vehicle PO details", e);
	        }

	        return resultList;
	    }
	 
	
	 
	 
	 @PutMapping("/soft/{pono_id}/{id}")
	 public ResponseEntity<Object> softDelete(@PathVariable int pono_id,
	                                          @PathVariable int id) {

	     String sql = "UPDATE dbo.VTS_invexitponumber SET is_deleted = 1, Modified_by = ?, Modified_date = GETDATE() WHERE Pono_id = ?";

	     try (Connection connection = jimsDataSource.getConnection();
	          PreparedStatement ps = connection.prepareStatement(sql)) {

	         ps.setInt(1, id);
	         ps.setInt(2, pono_id);

	         int rowsUpdated = ps.executeUpdate();

	         if (rowsUpdated > 0) {
	             return ResponseEntity.status(HttpStatus.OK)
	                     .body(new ResponseMessage("Success", "Successfully the row is deleted"));
	         } else {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                     .body(new ResponseMessage("Error", "Record not found"));
	         }

	     } catch (SQLException e) {
	         e.printStackTrace();
	         throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting", e);
	     }
	 }
	 
	 
	 
	 }
	 

