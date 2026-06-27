package com.JIMS.integration.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.server.ResponseStatusException;

import com.JIMS.integration.entity.VTS_invoiceexitmodel;
import com.JIMS.integration.interfaces.VTS_invoiceexitservice;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/api/invoiceexit")
public class VTS_invoiceexitcontroller {
	@Autowired
	private VTS_invoiceexitservice invoiceexitService;
	@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody VTS_invoiceexitmodel item) {
        try {
        	VTS_invoiceexitmodel savedItem = invoiceexitService.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
    }
	
	@PutMapping("/{ine_id}")
	public ResponseEntity<Object> updateVehicleInvoiceById(@PathVariable int ine_id, @RequestBody VTS_invoiceexitmodel vehicleInvoice) {
	    Optional<VTS_invoiceexitmodel> existingInvoiceOpt = invoiceexitService.getItemById(ine_id);

	    if (existingInvoiceOpt.isPresent()) {
	    	VTS_invoiceexitmodel updatedInvoice = existingInvoiceOpt.get();

	        // Example field updates — add checks for other fields as needed
	        if (vehicleInvoice.getVechile_id() != null) {
	            updatedInvoice.setVechile_id(vehicleInvoice.getVechile_id());
	        }
	        if (vehicleInvoice.getInvoice_id() != null) {
	            updatedInvoice.setInvoice_id(vehicleInvoice.getInvoice_id());
	        }
	        if (vehicleInvoice.getDdate() != null) {
	            updatedInvoice.setDdate(vehicleInvoice.getDdate());
	        }
	        if (vehicleInvoice.getVdate() != null) {
	            updatedInvoice.setVdate(vehicleInvoice.getVdate());
	        }
	        if (vehicleInvoice.getDmbno() != null) {
	            updatedInvoice.setDmbno(vehicleInvoice.getDmbno());
	        }
	        if (vehicleInvoice.getDname() != null) {
	            updatedInvoice.setDname(vehicleInvoice.getDname());
	        }
	        if (vehicleInvoice.getVecno() != null) {
	            updatedInvoice.setVecno(vehicleInvoice.getVecno());
	        }
	        if (vehicleInvoice.getTransname() != null) {
	            updatedInvoice.setTransname(vehicleInvoice.getTransname());
	        }
	        if (vehicleInvoice.getRegtyp() != null) {
	            updatedInvoice.setRegtyp(vehicleInvoice.getRegtyp());
	        }
	        if (vehicleInvoice.getDl() != null) {
	            updatedInvoice.setDl(vehicleInvoice.getDl());
	        }
	        if (vehicleInvoice.getVtype() != null) {
	            updatedInvoice.setVtype(vehicleInvoice.getVtype());
	        }
	        if (vehicleInvoice.getGrn() != null) {
	            updatedInvoice.setGrn(vehicleInvoice.getGrn());
	        }
	        if (vehicleInvoice.getIntime() != null) {
	            updatedInvoice.setIntime(vehicleInvoice.getIntime());
	        }
	        if (vehicleInvoice.getOuttime() != null) {
	            updatedInvoice.setOuttime(vehicleInvoice.getOuttime());
	        }
	        if (vehicleInvoice.getDelvchaln() != null) {
	            updatedInvoice.setDelvchaln(vehicleInvoice.getDelvchaln());
	        }
	        if (vehicleInvoice.getHelper() != null) {
	            updatedInvoice.setHelper(vehicleInvoice.getHelper());
	        }
	        if (vehicleInvoice.getGrndate() != null) {
	            updatedInvoice.setGrndate(vehicleInvoice.getGrndate());
	        }
	        if (vehicleInvoice.getDlvchndate() != null) {
	            updatedInvoice.setDlvchndate(vehicleInvoice.getDlvchndate());
	        }
	        if (vehicleInvoice.getVuser() != null) {
	            updatedInvoice.setVuser(vehicleInvoice.getVuser());
	        }
	        if (vehicleInvoice.getInremarks() != null) {
	            updatedInvoice.setInremarks(vehicleInvoice.getInremarks());
	        }
	        if (vehicleInvoice.getOutremarks() != null) {
	            updatedInvoice.setOutremarks(vehicleInvoice.getOutremarks());
	        }
	        if (vehicleInvoice.getWtm() != null) {
	            updatedInvoice.setWtm(vehicleInvoice.getWtm());
	        }
	        if (vehicleInvoice.getInvoicedate() != null) {
	            updatedInvoice.setInvoicedate(vehicleInvoice.getInvoicedate());
	        }
	        if (vehicleInvoice.getVendorid() != null) {
	            updatedInvoice.setVendorid(vehicleInvoice.getVendorid());
	        }
	        if (vehicleInvoice.getInvoice_no() != null) {
	            updatedInvoice.setInvoice_no(vehicleInvoice.getInvoice_no());
	        }
	        if (vehicleInvoice.getRcflag() != null) {
	            updatedInvoice.setRcflag(vehicleInvoice.getRcflag());
	        }
	        if (vehicleInvoice.getRcValidThrough() != null) {
	            updatedInvoice.setRcValidThrough(vehicleInvoice.getRcValidThrough());
	        }
	        if (vehicleInvoice.getInsuflag() != null) {
	            updatedInvoice.setInsuflag(vehicleInvoice.getInsuflag());
	        }
	        if (vehicleInvoice.getInsuValidThrough() != null) {
	            updatedInvoice.setInsuValidThrough(vehicleInvoice.getInsuValidThrough());
	        }
	        if (vehicleInvoice.getFitflag() != null) {
	            updatedInvoice.setFitflag(vehicleInvoice.getFitflag());
	        }
	        if (vehicleInvoice.getFitValidThrough() != null) {
	            updatedInvoice.setFitValidThrough(vehicleInvoice.getFitValidThrough());
	        }
	        if (vehicleInvoice.getPucflag() != null) {
	            updatedInvoice.setPucflag(vehicleInvoice.getPucflag());
	        }
	        if (vehicleInvoice.getPucValidThrough() != null) {
	            updatedInvoice.setPucValidThrough(vehicleInvoice.getPucValidThrough());
	        }
	        if (vehicleInvoice.getDescription() != null) {
	            updatedInvoice.setDescription(vehicleInvoice.getDescription());
	        }
	        if (vehicleInvoice.getFpath() != null) {
	            updatedInvoice.setFpath(vehicleInvoice.getFpath());
	        }
	        if (vehicleInvoice.getLr_no() != null) {
	            updatedInvoice.setLr_no(vehicleInvoice.getLr_no());
	        }
	        if (vehicleInvoice.getReject() != null) {
	            updatedInvoice.setReject(vehicleInvoice.getReject());
	        }
	        if (vehicleInvoice.getReject_remarks() != null) {
	            updatedInvoice.setReject_remarks(vehicleInvoice.getReject_remarks());     
	        }
	        if (vehicleInvoice.getSecurityreject_remarks() != null) {
	            updatedInvoice.setSecurityreject_remarks(vehicleInvoice.getSecurityreject_remarks());     
	        }
	        if (vehicleInvoice.getCreated_by() != null) {
	            updatedInvoice.setCreated_by(vehicleInvoice.getCreated_by());
	        }
	        if (vehicleInvoice.getCreated_date() != null) {
	            updatedInvoice.setCreated_date(vehicleInvoice.getCreated_date());
	        }
	        if (vehicleInvoice.getModified_by() != null) {
	            updatedInvoice.setModified_by(vehicleInvoice.getModified_by());
	        }
	        if (vehicleInvoice.getModified_date() != null) {
	            updatedInvoice.setModified_date(vehicleInvoice.getModified_date());
	        }

	        invoiceexitService.saveOrUpdateItem(updatedInvoice);

	        return ResponseEntity.status(HttpStatus.OK)
	                .body(new ResponseMessage("Success", "Vehicle invoice updated successfully"));

	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ResponseMessage("Error", "Vehicle invoice not found"));
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
//	@GetMapping
//	public ResponseEntity<List<VTS_invoiceexitmodel>> getAllItems(@RequestParam("factory_id") String factoryId) {
//	    List<VTS_invoiceexitmodel> items = invoiceexitService.getAllItems(factoryId);
//	    if (items.isEmpty()) {
//	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
//	    }
//	    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
//	}
	
	
	@GetMapping
    public ResponseEntity<Map<String, Object>> getAllItems(
            @RequestParam("factory_id") String factoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search) {
 
        try {
            // Validate pagination parameters
            if (page < 0) page = 0;
            if (size <= 0 || size > 100) size = 10;
 
            Pageable pageable = PageRequest.of(page, size);
 
            // Fetch data from service
            Page<VTS_invoiceexitmodel> itemsPage;
            if (search != null && !search.trim().isEmpty()) {
                itemsPage = invoiceexitService.searchItems(factoryId, search.trim(), pageable);
            } else {
                itemsPage = invoiceexitService.getAllItems(factoryId, pageable);
            }
 
            // Build response for DataTable
            Map<String, Object> response = new HashMap<>();
            response.put("data", itemsPage.getContent());
            response.put("totalItems", itemsPage.getTotalElements());
            response.put("totalRecords", itemsPage.getTotalElements());
            response.put("totalPages", itemsPage.getTotalPages());
            response.put("currentPage", itemsPage.getNumber());
            response.put("hasNextPage", itemsPage.hasNext());
            response.put("hasPreviousPage", itemsPage.hasPrevious());
            response.put("pageSize", size);
 
            if (itemsPage.isEmpty()) {
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
 
            return new ResponseEntity<>(response, HttpStatus.OK);
 
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch data: " + e.getMessage());
            errorResponse.put("data", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 

	@GetMapping("/{id}")
	public ResponseEntity<VTS_invoiceexitmodel> getItemById(@PathVariable int id) {
	    Optional<VTS_invoiceexitmodel> item = invoiceexitService.getItemById(id);
	    if (item.isPresent()) {
	        return new ResponseEntity<>(item.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	 @Autowired
	    @Qualifier("jimsDataSource")
	    private DataSource jimsDataSource;

	    // Get GP number based on GP_type and order by GP_no DESC
	    @GetMapping("/jims/autovechileno")
	    public @ResponseBody int getvechicleData() {

	        // SQL query to get the top GP_no based on GP_type
	        String sql = " SELECT TOP 1 count(vechile_id) as vech_id FROM [VTS_invoice_exit]  where vechile_id like'%VL%' ORDER BY vech_id DESC";
	        int count = 0;

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        
	            // Execute the query
	            ResultSet resultSet = preparedStatement.executeQuery();

	            // Check if there are results
	            if (resultSet.next()) {
	                count = resultSet.getInt(1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        // Return the result containing the GP_no
	        return count;
	        
	    }
	    

		    // Get GP number based on GP_type and order by GP_no DESC
		    @GetMapping("/jims/autovechilenomaterial")
		    public @ResponseBody int getmaterialvechicleData() {

		        // SQL query to get the top GP_no based on GP_type
		        String sql = "SELECT TOP 1 count(vechile_id) as vech_id FROM [VTS_invoice_exit]  where vechile_id like'%MI%' ORDER BY vech_id DESC";
		        int count = 0;

		        try (Connection connection = jimsDataSource.getConnection();
		             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
		        
		            // Execute the query
		            ResultSet resultSet = preparedStatement.executeQuery();

		            // Check if there are results
		            if (resultSet.next()) {
		                count = resultSet.getInt(1);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }

		        // Return the result containing the GP_no
		        return count;
		        
		    }
		    
	    @GetMapping("/jims/vehicleDetails")
	    public @ResponseBody List<Map<String, Object>> getVehicleDetails(
	            @RequestParam(value = "filterType") String filterType,
	            @RequestParam(value = "vechile_id", required = false) String vehicleId,
	            @RequestParam(value = "factory_id", required = false) String factoryId) {

	        String sql;
	        boolean isByVehicleId = "byVehicleId".equalsIgnoreCase(filterType);

	        // Select query based on filterType
	        if (isByVehicleId) {
	            if (vehicleId == null || vehicleId.trim().isEmpty()) {
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehicle_id is required for filterType=byVehicleId");
	            }
	            sql = "SELECT        ve.ine_id, ve.vechile_id, ve.lr_no, ve.outremarks, ve.vecno, ve.vdate, cm.Company_name, rt.regtype, ve.dname, ve.dmbno, ve.dl, vt.Vtype, ve.inremarks, ve.helper, ve.intime, dbo.VMS_Citymaster.City_name, \r\n"
	            		+ "                         dbo.VMS_Statemaster.State_name, dbo.USERS_MASTER.user_id, dbo.TAB_DEPARTMENTS.Department_name,ve.wtm \r\n"
	            		+ "FROM            dbo.VMS_Statemaster RIGHT OUTER JOIN\r\n"
	            		+ "                         dbo.VMS_Companymaster AS cm ON dbo.VMS_Statemaster.State_id = cm.State_id LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.VMS_Citymaster ON cm.City_id = dbo.VMS_Citymaster.City_id RIGHT OUTER JOIN\r\n"
	            		+ "                         dbo.VTS_invoice_exit AS ve LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.USERS_MASTER ON ve.wtm = dbo.USERS_MASTER.id LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.TAB_DEPARTMENTS ON dbo.USERS_MASTER.Department = dbo.TAB_DEPARTMENTS.Dept_id LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.VTS_Vechicletype AS vt ON ve.vtype = vt.vid LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.VTS_Regtype AS rt ON ve.regtyp = rt.tid ON cm.Company_id = ve.transname " +
	                  "WHERE ve.vechile_id = ?";
	        } else if ("byReject".equalsIgnoreCase(filterType)) {
	        	 if (factoryId == null || factoryId.trim().isEmpty()) {
	        	     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "factory_id is required for filterType=byReject");
	             }
	            sql = "SELECT        ve.ine_id, ve.vechile_id, ve.lr_no, ve.outremarks, ve.vecno, ve.vdate, cm.Company_name, rt.regtype, ve.dname, ve.dmbno, ve.dl, vt.Vtype, ve.inremarks, ve.helper, ve.intime, dbo.VMS_Citymaster.City_name, \r\n"
	            		+ "                         dbo.VMS_Statemaster.State_name, dbo.TAB_DEPARTMENTS.Department_name, dbo.USERS_MASTER.user_id\r\n"
	            		+ "FROM            dbo.VMS_Statemaster RIGHT OUTER JOIN\r\n"
	            		+ "                         dbo.VMS_Companymaster AS cm ON dbo.VMS_Statemaster.State_id = cm.State_id LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.VMS_Citymaster ON cm.City_id = dbo.VMS_Citymaster.City_id RIGHT OUTER JOIN\r\n"
	            		+ "                         dbo.VTS_invoice_exit AS ve LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.USERS_MASTER ON ve.wtm = dbo.USERS_MASTER.id LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.TAB_DEPARTMENTS ON dbo.USERS_MASTER.Department = dbo.TAB_DEPARTMENTS.Dept_id LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.VTS_Vechicletype AS vt ON ve.vtype = vt.vid LEFT OUTER JOIN\r\n"
	            		+ "                         dbo.VTS_Regtype AS rt ON ve.regtyp = rt.tid ON cm.Company_id = ve.transname WHERE ve.Reject = '1' AND ve.Securityreject_remarks IS NULL AND ve.Factory_id = ?";
	        } else {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filterType. Use 'byVehicleId' or 'byReject'");
	        }

	        List<Map<String, Object>> resultList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            // Set parameters for byVehicleId query
	            if (isByVehicleId) {
	                preparedStatement.setString(1, vehicleId);
	            } else { // byReject
	                preparedStatement.setString(1, factoryId);
	            }

	            try (ResultSet rs = preparedStatement.executeQuery()) {
	                while (rs.next()) {
	                    Map<String, Object> resultMap = new HashMap<>();
	                    resultMap.put("ine_id", rs.getInt("ine_id"));
	                    resultMap.put("vechile_id", rs.getString("vechile_id"));
	                    resultMap.put("vecno", rs.getString("vecno"));
	                    resultMap.put("vdate", rs.getTimestamp("vdate")); // Can convert to ISO string if needed
	                    resultMap.put("Company_name", rs.getString("Company_name"));
	                    resultMap.put("regtype", rs.getString("regtype"));
	                    resultMap.put("dname", rs.getString("dname"));
	                    resultMap.put("dmbno", rs.getString("dmbno"));
	                    resultMap.put("dl", rs.getString("dl"));
	                    resultMap.put("Vtype", rs.getString("Vtype"));
	                    resultMap.put("inremarks", rs.getString("inremarks"));
	                    resultMap.put("helper", rs.getString("helper"));
	                    resultMap.put("lr_no", rs.getString("lr_no"));
	                    resultMap.put("outremarks", rs.getString("outremarks"));
	                    resultMap.put("intime", rs.getTime("intime"));
	                    resultMap.put("State_name", rs.getString("State_name"));
	                    resultMap.put("City_name", rs.getString("City_name"));
	                    resultMap.put("user_id", rs.getString("user_id"));
	                    resultMap.put("Department_name", rs.getString("Department_name"));
	                    resultMap.put("wtm", rs.getString("wtm"));
	                    resultList.add(resultMap);
	                }
	            }

	        } catch (SQLException e) {
	            // Replace with proper logging (e.g., SLF4J)
	            e.printStackTrace();
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching vehicle details", e);
	        }

	        return resultList;
	    }
	    
	    @GetMapping("/jims/vechileoutpending")
	    public @ResponseBody List<Map<String, String>> getPendingVehicleIds(@RequestParam("factoryid") String factoryId) {
	        String sql = "SELECT ine_id,vechile_id,Reject FROM VTS_invoice_exit WHERE outremarks IS NULL AND lr_no IS NULL and Reject is null AND factory_id = ?";
	        List<Map<String, String>> vehicleList = new ArrayList<>();
	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement ps = connection.prepareStatement(sql)) {
	            ps.setString(1, factoryId);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Map<String, String> map = new HashMap<>();
	                    map.put("ine_id", rs.getString("ine_id"));
	                    map.put("vechile_id", rs.getString("vechile_id"));
	                    vehicleList.add(map);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return vehicleList;
	    }

	    @GetMapping("/jims/vecnofetch")
	    public @ResponseBody List<Map<String, String>> getvechno() {
	        String sql = "select distinct vecno,ine_id,vechile_id from VTS_invoice_exit where vecno <>''";

	        List<Map<String, String>> vehicleList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql);
	             ResultSet rs = preparedStatement.executeQuery()) {

	            while (rs.next()) {
	                Map<String, String> vehicleMap = new HashMap<>();
	                vehicleMap.put("vecno", rs.getString("vecno"));
	                vehicleMap.put("ine_id", rs.getString("ine_id"));
	                vehicleMap.put("vechile_id", rs.getString("vechile_id"));
	                vehicleList.add(vehicleMap);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace(); // Replace with proper logging
	        }

	        return vehicleList;
	    }
	    
	    @GetMapping("/jims/rejectvechfetch")
	    public @ResponseBody List<Map<String, String>> rejectvechfetch() {
	        String sql = "select distinct vecno,ine_id,vechile_id from VTS_invoice_exit where vecno <>''";

	        List<Map<String, String>> vehicleList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql);
	             ResultSet rs = preparedStatement.executeQuery()) {

	            while (rs.next()) {
	                Map<String, String> vehicleMap = new HashMap<>();
	                vehicleMap.put("vecno", rs.getString("vecno"));
	                vehicleMap.put("ine_id", rs.getString("ine_id"));
	                vehicleMap.put("vechile_id", rs.getString("vechile_id"));
	                vehicleList.add(vehicleMap);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace(); // Replace with proper logging
	        }

	        return vehicleList;
	    }
	    
	    @GetMapping("/jims/vecnofetchdata")
	    public @ResponseBody List<Map<String, String>> getVehicleData(@RequestParam("vechile_id") String vechileId) {
	        String sql = "SELECT DISTINCT ine_id, vechile_id, vdate, dmbno, dname, vecno, transname, regtyp, dl, " +
	                     "vtype, helper, rcflag, rcValidThrough, insuflag, insuValidThrough, fitflag, fitValidThrough, " +
	                     "pucflag, pucValidThrough FROM VTS_invoice_exit WHERE vechile_id = ?";

	        List<Map<String, String>> vehicleList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            preparedStatement.setString(1, vechileId);

	            try (ResultSet rs = preparedStatement.executeQuery()) {
	                while (rs.next()) {
	                    Map<String, String> vehicleMap = new HashMap<>();
	                    vehicleMap.put("ine_id", rs.getString("ine_id"));
	                    vehicleMap.put("vechile_id", rs.getString("vechile_id"));
	                    vehicleMap.put("vdate", rs.getString("vdate"));
	                    vehicleMap.put("dmbno", rs.getString("dmbno"));
	                    vehicleMap.put("dname", rs.getString("dname"));
	                    vehicleMap.put("vecno", rs.getString("vecno"));
	                    vehicleMap.put("transname", rs.getString("transname"));
	                    vehicleMap.put("regtyp", rs.getString("regtyp"));
	                    vehicleMap.put("dl", rs.getString("dl"));
	                    vehicleMap.put("vtype", rs.getString("vtype"));
	                    vehicleMap.put("helper", rs.getString("helper"));
	                    vehicleMap.put("rcflag", rs.getString("rcflag"));
	                    vehicleMap.put("rcValidThrough", rs.getString("rcValidThrough"));
	                    vehicleMap.put("insuflag", rs.getString("insuflag"));
	                    vehicleMap.put("insuValidThrough", rs.getString("insuValidThrough"));
	                    vehicleMap.put("fitflag", rs.getString("fitflag"));
	                    vehicleMap.put("fitValidThrough", rs.getString("fitValidThrough"));
	                    vehicleMap.put("pucflag", rs.getString("pucflag"));
	                    vehicleMap.put("pucValidThrough", rs.getString("pucValidThrough"));
	                    vehicleList.add(vehicleMap);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace(); // Ideally use logger here
	        }

	        return vehicleList;
	    }
	    
	    @GetMapping("/jims/getinvoicexitrejecttable")
	    public @ResponseBody List<Map<String, String>> getinvoicexitrejecttable(@RequestParam("factoryid") String factoryId) {
	        String sql = "SELECT        dbo.VTS_invoice_exit.ine_id,VTS_invexitponumber.Pono_id, dbo.VTS_invoice_exit.vechile_id, dbo.VTS_invoice_exit.vecno, dbo.VTS_invoice_exit.dname, dbo.VTS_invoice_exit.dmbno, dbo.VTS_invoice_exit.dl, dbo.VTS_invoice_exit.helper, \r\n"
	        		+ "                         dbo.VTS_invoice_exit.description, dbo.VTS_invoice_exit.outremarks\r\n"
	        		+ "FROM            dbo.VTS_invoice_exit INNER JOIN\r\n"
	        		+ "                         dbo.VTS_invexitponumber ON dbo.VTS_invoice_exit.vechile_id = dbo.VTS_invexitponumber.vechile_id where outtime is not null and VTS_invexitponumber.Pono_id is not null and VTS_invexitponumber.reject<>1 and  VTS_invoice_exit.factory_id = ?";

	        List<Map<String, String>> vehicleList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            // Set the factory_id parameter
	            preparedStatement.setString(1, factoryId);
	            try (ResultSet rs = preparedStatement.executeQuery()) {

	            while (rs.next()) {
	                Map<String, String> vehicleMap = new HashMap<>();
	                vehicleMap.put("vechile_id", rs.getString("vechile_id"));
	                vehicleMap.put("ine_id", rs.getString("ine_id"));
	                vehicleMap.put("vecno", rs.getString("vecno"));
	                vehicleMap.put("dname", rs.getString("dname"));
	                vehicleMap.put("dmbno", rs.getString("dmbno"));
	                vehicleMap.put("dl", rs.getString("dl"));
	                vehicleMap.put("helper", rs.getString("helper"));
	                vehicleMap.put("description", rs.getString("description"));
	                vehicleMap.put("outremarks", rs.getString("outremarks"));
	                vehicleList.add(vehicleMap);
	            }
	          }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Replace with proper logging
	        }

	        return vehicleList;
	    }

	    @GetMapping("/jims/getinvoicexitinvoicenumber")
	    public @ResponseBody List<Map<String, String>> getinvoicexitinvoicenumber() {
	        String sql = "SELECT i.[invoice_no], i.id FROM INVOICE_MASTER as i  where invoice_type='Steel' and id not in (select isnull(invoice_id,0) from VTS_invoice_exit) and invoice_no<>0  order by invoice_no";

	        List<Map<String, String>> vehicleList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	       
	            try (ResultSet rs = preparedStatement.executeQuery()) {

	            while (rs.next()) {
	                Map<String, String> vehicleMap = new HashMap<>();
	                vehicleMap.put("invoice_no", rs.getString("invoice_no"));
	                vehicleMap.put("id", rs.getString("id"));
	                vehicleList.add(vehicleMap);
	            }
	          }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Replace with proper logging
	        }

	        return vehicleList;
	    }
	    
	    @GetMapping("/jims/vehiclegatepass")
		public @ResponseBody List<String> getAllVehicleIds() {
		    String sql = "SELECT vechile_id FROM VTS_invoice_exit ORDER BY vechile_id DESC";
		    List<String> vehicleIds = new ArrayList<>();

		    try (Connection connection = jimsDataSource.getConnection();
		         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

		        // No need to set any parameters if you don't have any placeholders in the query
		        ResultSet resultSet = preparedStatement.executeQuery();

		        while (resultSet.next()) {
		            // Add the Visitor_id from the result set to the list
		        	vehicleIds.add(resultSet.getString("vechile_id"));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return vehicleIds; 
		}
	    
	    @GetMapping("/jims/getVehicleDetails")
	    public @ResponseBody List<Map<String, Object>> getVehicleDetails(@RequestParam("vehicle_id") String vehicleId) {

	        // SQL query to fetch details based on the Vehicle ID (vehicle_id)
	        String sql = "SELECT DISTINCT ie.vechile_id, (ie.dname + '+' + ie.helper) AS name, c.Company_name, ie.dl, ie.dmbno, ie.vdate, ie.vecno, ie.inremarks, u.username, d.Department_name,ie.Capture,ie.wtm     "
	                   + "FROM VTS_invoice_exit ie "
	                   + "LEFT OUTER JOIN VMS_Companymaster c ON c.Company_id = ie.transname "
	                   + "LEFT OUTER JOIN USERS_MASTER u ON u.id = ie.wtm "
	                   + "LEFT OUTER JOIN TAB_DEPARTMENTS d ON d.Dept_id = u.Department "
	                   + "WHERE ie.vechile_id = ?";

	        List<Map<String, Object>> resultList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            // Set the vehicle_id parameter for SQL query
	            preparedStatement.setString(1, vehicleId);

	            // Execute the query
	            ResultSet resultSet = preparedStatement.executeQuery();

	            // Iterate through the results and map them to resultList
	            while (resultSet.next()) {
	                Map<String, Object> resultMap = new HashMap<>();
	                resultMap.put("vechile_id", resultSet.getString("vechile_id"));
	                resultMap.put("name", resultSet.getString("name"));
	                resultMap.put("Company_name", resultSet.getString("Company_name"));
	                resultMap.put("dl", resultSet.getString("dl"));
	                resultMap.put("dmbno", resultSet.getString("dmbno"));
	                resultMap.put("vdate", resultSet.getString("vdate"));
	                resultMap.put("vecno", resultSet.getString("vecno"));
	                resultMap.put("inremarks", resultSet.getString("inremarks"));
	                resultMap.put("username", resultSet.getString("username"));
	                resultMap.put("Department_name", resultSet.getString("Department_name"));
	                resultMap.put("wtm", resultSet.getString("wtm"));
	                
	                String photoFullPath = resultSet.getString("Capture");
	                if (photoFullPath != null && !photoFullPath.trim().isEmpty()) {
	                    // new File() correctly handles both \ and / on any OS
	                    String photoFileName = new File(photoFullPath).getName();
	                    resultMap.put("photo_filename", photoFileName);
	                } else {
	                	resultMap.put("photo_filename", "");
	                }
	                resultMap.put("photo_url", resultSet.getString("Capture"));
	                resultList.add(resultMap);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        // Return the result list containing visitor details
	        return resultList;
	    }
	    
	    @GetMapping("/jims/getRegisterTypes")
	    public @ResponseBody List<Map<String, Object>> getRegisterTypes() {

	        List<Map<String, Object>> list = new ArrayList<>();

	        list.add(Map.of("id", 0, "name", "All"));
	        list.add(Map.of("id", 2, "name", "Material Inward"));
	        list.add(Map.of("id", 1, "name", "Vehicle Loading"));

	        return list;
	    }
	    
	    @GetMapping("/jims/getSelectType")
	    public @ResponseBody List<String> getSelectType(@RequestParam("registerType") int registerType) {

	        List<String> list = new ArrayList<>();

	        if (registerType == 0) {
	            // All selected in dropdown
	            list = Arrays.asList("All", "Vehicle Loading", "Stores Material", "Raw Material", "Site Return");
	        } 
	        else if (registerType == 2) {
	            // Material Inward selected
	            list = Arrays.asList("All", "Stores Material", "Raw Material", "Site Return");
	        } 
	        else if (registerType == 1) {
	            // Vehicle Loading → no dropdown options
	            list = new ArrayList<>();
	        }

	        return list;
	    }
	    
	    @GetMapping("/jims/getRegisterReport")
	    public @ResponseBody List<Map<String, Object>> getRegisterReport(
	            @RequestParam("registerType") int registerType,
	            @RequestParam("selectType") String selectType,
	            @RequestParam("fromDate") String fromDate,
	            @RequestParam("toDate") String toDate) {

	        List<Map<String, Object>> resultList = new ArrayList<>();

	        // Normalize input
	        selectType = selectType.replaceAll("\\s+", "").trim().toLowerCase();

	        StringBuilder sql = new StringBuilder(
	            "SELECT ve.vechile_id, vr.regtype, vt.Vtype, ve.vecno, ve.intime,vp.weight,vp.Po_number,vp.Po_date,vp.DC_no,vp.DC_date,vp.Inv_no,vp.Inv_date,ve.wtm,  " +
	            "vc.Company_name, ve.dname, ve.dmbno, um.username, ve.inremarks, " +
	            "ve.content_desc, fr.factory_name " +
	            "FROM VTS_invoice_exit ve " +
	            "LEFT JOIN VTS_invexitponumber vp on vp.vechile_id=ve.vechile_id " +
	            "LEFT JOIN VTS_Regtype vr ON vr.tid = ve.regtyp " +
	            "LEFT JOIN VTS_Vechicletype vt ON vt.vid = ve.vtype " +
	            "LEFT JOIN VMS_Companymaster vc ON vc.Company_id = ve.transname " +
	            "LEFT JOIN USERS_MASTER um ON um.id = ve.wtm " +
	            "LEFT JOIN FACTORY_MASTER fr ON fr.id = ve.Factory_id " +
	            "WHERE CAST(ve.Created_date AS date) BETWEEN CAST(? AS date) AND CAST(? AS date)"
	        );

	        // Map frontend selectType -> regtyp numbers
	        Map<String, Integer> typeMap = Map.of(
	                "storesmaterial", 3,
	                "rawmaterial", 4,
	                "sitereturn", 5,
	                "materialinward", 2,
	                "vehicleloading", 1
	        );

	        List<Integer> allowed = new ArrayList<>();

	     // ==========================================================
	     // CASE 0 → ALL Register Types
	     // ==========================================================
	     if (registerType == 0) {

	         if (selectType.equals("all")) {
	             allowed = Arrays.asList(1, 2, 3, 4, 5); // Return all categories
	         }
	         else if (typeMap.containsKey(selectType)) {
	             allowed.add(typeMap.get(selectType));  // return exact mapped type
	         }
	         else {
	             sql.append(" AND 1 = 0 "); // invalid selection → no data
	         }
	     }

	     // ==========================================================
	     // CASE 1 → Vehicle Loading
	     // ==========================================================
	     else if (registerType == 1) {

	         if (!selectType.equals("vehicleloading") && !selectType.equals("all")) {
	             sql.append(" AND 1 = 0 ");
	         } else {
	             allowed.add(1);
	         }
	     }

	     // ==========================================================
	     // CASE 2 → Stores/Raw/SiteReturn/MI
	     // ==========================================================
	     else if (registerType == 2) {

	         if (selectType.equals("all")) {
	             allowed = Arrays.asList(2, 3, 4, 5);
	         } 
	         else if (typeMap.containsKey(selectType)) {
	             int mapped = typeMap.get(selectType);
	             if (mapped >= 2 && mapped <= 5) allowed.add(mapped);
	             else sql.append(" AND 1 = 0 ");
	         }
	         else sql.append(" AND 1 = 0 ");
	     }

	     // ==========================================================
	     // CASE 5 → Material Inward (same as registerType = 2)
	     // ==========================================================
	     else if (registerType == 2) {

	         if (selectType.equals("all")) {
	             allowed = Arrays.asList(2, 3, 4, 5);
	         } 
	         else if (typeMap.containsKey(selectType)) {
	             int mapped = typeMap.get(selectType);
	             if (mapped >= 2 && mapped <= 2) allowed.add(mapped);
	             else sql.append(" AND 1 = 0 ");
	         }
	         else sql.append(" AND 1 = 0 ");
	     }

	        // ==========================================================
	        // APPLY IN() FILTER
	        // ==========================================================
	        if (!allowed.isEmpty()) {
	            sql.append(" AND vr.tid IN (")
	                    .append(
	                            allowed.stream()
	                                   .map(String::valueOf)
	                                   .collect(Collectors.joining(","))
	                    )
	                    .append(")");
	        }

	        System.out.println("FINAL SQL = " + sql);

	        // ==========================================================
	        // EXECUTE QUERY
	        // ==========================================================
	        try (Connection con = jimsDataSource.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql.toString())) {

	            ps.setString(1, fromDate);
	            ps.setString(2, toDate);

	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {

	                Map<String, Object> row = new HashMap<>();
	                row.put("vechile_id", rs.getString("vechile_id"));
	                row.put("regtype", rs.getString("regtype"));
	                row.put("Vtype", rs.getString("Vtype"));
	                row.put("vecno", rs.getString("vecno"));
	                row.put("intime", rs.getString("intime"));
	                row.put("weight", rs.getString("weight"));
	                row.put("Po_number", rs.getString("Po_number"));
	                row.put("Po_date", rs.getString("Po_date"));
	                row.put("DC_no", rs.getString("DC_no"));
	                row.put("DC_date", rs.getString("DC_date"));
	                row.put("Inv_no", rs.getString("Inv_no"));
	                row.put("Inv_date", rs.getString("Inv_date"));
	                row.put("Company_name", rs.getString("Company_name"));
	                row.put("dname", rs.getString("dname"));
	                row.put("dmbno", rs.getString("dmbno"));
	                row.put("username", rs.getString("username"));
	                row.put("inremarks", rs.getString("inremarks"));
	                row.put("content_desc", rs.getString("content_desc"));
	                row.put("factory_name", rs.getString("factory_name"));
	                row.put("wtm", rs.getString("wtm"));

	                resultList.add(row);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return resultList;
	    }
	    
	    @GetMapping("/jims/getvehicleoutRegisterTypes")
	    public @ResponseBody List<Map<String, Object>> getvehicleoutRegisterTypes() {

	        List<Map<String, Object>> list = new ArrayList<>();
	      
	        list.add(Map.of("id", 2, "name", "Material Inward"));
	        list.add(Map.of("id", 1, "name", "Vehicle Loading"));

	        return list;
	    }
	    
	    @GetMapping("/jims/getvehicleoutSelectType")
	    public @ResponseBody List<String> getvehicleoutSelectType(@RequestParam("vehicleType") int vehicleType) {

	        List<String> list = new ArrayList<>();
	        
	         if (vehicleType == 2) {
	            // Material Inward selected
	            list = Arrays.asList("All", "Stores Material", "Raw Material", "Site Return");
	        } 
	        else if (vehicleType == 1) {
	            // Vehicle Loading → no dropdown options
	            list = new ArrayList<>();
	        }

	        return list;
	    }
	    
	    @GetMapping("/jims/getvehicleoutRegisterReport")
	    public @ResponseBody List<Map<String, Object>> getvehicleoutRegisterReport(
	            @RequestParam("registerType") int registerType,
	            @RequestParam("fromDate") String fromDate,
	            @RequestParam("toDate") String toDate) {

	        List<Map<String, Object>> resultList = new ArrayList<>();

	        StringBuilder sql = new StringBuilder(
	            "SELECT ve.vechile_id, vr.regtype, vt.Vtype, ve.vecno, ve.intime, ve.outtime, " +
	            "vc.Company_name, ve.dname, ve.dmbno, um.username, ve.outremarks, ve.wtm , " +
	            "im.invoice_no, fr.factory_name " +
	            "FROM VTS_invoice_exit ve " +
	            "LEFT JOIN INVOICE_MASTER im ON im.id = ve.invoice_id " +
	            "LEFT JOIN VTS_Regtype vr ON vr.tid = ve.regtyp " +
	            "LEFT JOIN VTS_Vechicletype vt ON vt.vid = ve.vtype " +
	            "LEFT JOIN VMS_Companymaster vc ON vc.Company_id = ve.transname " +
	            "LEFT JOIN USERS_MASTER um ON um.id = ve.wtm " +
	            "LEFT JOIN FACTORY_MASTER fr ON fr.id = ve.Factory_id " +
	            "WHERE CAST(ve.Created_date AS date) BETWEEN CAST(? AS date) AND CAST(? AS date) "
	        );

	        // VEHICLE LOADING FILTER LOGIC
	        if (registerType == 1) {
	            sql.append(" AND vr.tid = 1 ");   // Only Vehicle Loading
	        } 
	        else {
	            sql.append(" AND 1 = 0 ");        // Invalid → Return no data
	        }

	        System.out.println("FINAL SQL → " + sql);

	        try (Connection con = jimsDataSource.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql.toString())) {

	            ps.setString(1, fromDate);
	            ps.setString(2, toDate);

	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                Map<String, Object> row = new HashMap<>();
	                row.put("vechile_id", rs.getString("vechile_id"));
	                row.put("regtype", rs.getString("regtype"));
	                row.put("Vtype", rs.getString("Vtype"));
	                row.put("vecno", rs.getString("vecno"));
	                row.put("intime", rs.getString("intime"));
	                row.put("outtime", rs.getString("outtime"));
	                row.put("Company_name", rs.getString("Company_name"));
	                row.put("dname", rs.getString("dname"));
	                row.put("dmbno", rs.getString("dmbno"));
	                row.put("username", rs.getString("username"));
	                row.put("outremarks", rs.getString("outremarks"));
	                row.put("invoice_no", rs.getString("invoice_no"));
	                row.put("factory_name", rs.getString("factory_name"));
	                row.put("wtm", rs.getString("wtm"));
	                resultList.add(row);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return resultList;
	    }
	    
	    @GetMapping("/jims/getmaterialinwardRegisterReport")
	    public @ResponseBody List<Map<String, Object>> getmaterialinwardRegisterReport(
	            @RequestParam("registerType") int registerType,
	            @RequestParam("selectType") String selectType,
	            @RequestParam("fromDate") String fromDate,
	            @RequestParam("toDate") String toDate) {

	        List<Map<String, Object>> resultList = new ArrayList<>();

	        // Normalize
	        selectType = selectType.replaceAll("\\s+", "").trim().toLowerCase();

	        // Base Query
	        StringBuilder sql = new StringBuilder(
	            "SELECT ve.vechile_id, vr.regtype, vt.Vtype, ve.vecno, ve.intime, " +
	            "vp.weight, vp.Po_number, vp.Po_date, vp.DC_no, vp.DC_date, vp.Inv_no, vp.Inv_date, " +
	            "vc.Company_name, ve.dname, ve.dmbno, um.username, ve.inremarks, " +
	            "ve.content_desc, fr.factory_name " +
	            "FROM VTS_invoice_exit ve " +
	            "LEFT JOIN VTS_invexitponumber vp ON vp.vechile_id = ve.vechile_id " +
	            "LEFT JOIN VTS_Regtype vr ON vr.tid = ve.regtyp " +
	            "LEFT JOIN VTS_Vechicletype vt ON vt.vid = ve.vtype " +
	            "LEFT JOIN VMS_Companymaster vc ON vc.Company_id = ve.transname " +
	            "LEFT JOIN USERS_MASTER um ON um.id = ve.wtm " +
	            "LEFT JOIN FACTORY_MASTER fr ON fr.id = ve.Factory_id " +
	            "WHERE CAST(ve.Created_date AS date) BETWEEN CAST(? AS date) AND CAST(? AS date) "
	        );

	        // Map names to regtyp values  
	        Map<String, Integer> typeMap = Map.of(
	                "storesmaterial", 3,
	                "rawmaterial", 4,
	                "sitereturn", 5,
	                "materialinward", 2
	        );

	        List<Integer> allowed = new ArrayList<>();

	        // ======================================================
	        // ONLY REGISTER TYPE = 5 IS USED HERE
	        // ======================================================
	        if (registerType == 2) {

	            if (selectType.equals("all")) {
	                allowed = Arrays.asList(2, 3, 4, 5);
	            }
	            else if (typeMap.containsKey(selectType)) { 
	                allowed.add(typeMap.get(selectType));
	            }
	            else {
	                sql.append(" AND 1 = 0 "); // invalid → no data
	            }

	        } 
	        else {
	            // IF registerType is something else → no data
	            sql.append(" AND 1 = 0 ");
	        }

	        // Apply tid IN (...)
	        if (!allowed.isEmpty()) {
	            sql.append(" AND vr.tid IN (")
	                .append(allowed.stream().map(String::valueOf).collect(Collectors.joining(",")))
	                .append(")");
	        }

	        System.out.println("FINAL SQL = " + sql);

	        // Execute Query
	        try (Connection con = jimsDataSource.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql.toString())) {

	            ps.setString(1, fromDate);
	            ps.setString(2, toDate);

	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                Map<String, Object> row = new HashMap<>();
	                row.put("vechile_id", rs.getString("vechile_id"));
	                row.put("regtype", rs.getString("regtype"));
	                row.put("Vtype", rs.getString("Vtype"));
	                row.put("vecno", rs.getString("vecno"));
	                row.put("intime", rs.getString("intime"));
	                row.put("weight", rs.getString("weight"));
	                row.put("Po_number", rs.getString("Po_number"));
	                row.put("Po_date", rs.getString("Po_date"));
	                row.put("DC_no", rs.getString("DC_no"));
	                row.put("DC_date", rs.getString("DC_date"));
	                row.put("Inv_no", rs.getString("Inv_no"));
	                row.put("Inv_date", rs.getString("Inv_date"));
	                row.put("Company_name", rs.getString("Company_name"));
	                row.put("dname", rs.getString("dname"));
	                row.put("dmbno", rs.getString("dmbno"));
	                row.put("username", rs.getString("username"));
	                row.put("inremarks", rs.getString("inremarks"));
	                row.put("content_desc", rs.getString("content_desc"));
	                row.put("factory_name", rs.getString("factory_name"));

	                resultList.add(row);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return resultList;
	    }
	    
	    
		  @Value("${upload.VTSPath}")
		    private String uploadDir; 
		
	    @GetMapping("/viewImage")
	    public void viewImage(@RequestParam("fileName") String fileName,
	                          HttpServletResponse response) {
	        try {
	            if (fileName == null || fileName.trim().isEmpty()) {
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                return;
	            }

	            // ✅ Security: only filename, no path traversal
	            fileName = new File(fileName).getName();

	            File file = new File(uploadDir, fileName);

	            if (!file.exists() || !file.isFile()) {
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                return;
	            }

	            // ✅ Fix — Add Cross-Origin-Resource-Policy
	            // Allows cross-origin requests (port 8080 → port 8004)
	            response.setHeader("Cross-Origin-Resource-Policy", "cross-origin");

	            // ✅ Allow browser to display inline
	            response.setHeader("Content-Disposition",
	                "inline; filename=\"" + file.getName() + "\"");

	            // ✅ Cache for 1 hour
	            response.setHeader("Cache-Control", "max-age=3600");

	            // ✅ Set correct content type
	            String lowerName = fileName.toLowerCase();
	            if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
	                response.setContentType("image/jpeg");
	            } else if (lowerName.endsWith(".png")) {
	                response.setContentType("image/png");
	            } else if (lowerName.endsWith(".gif")) {
	                response.setContentType("image/gif");
	            } else {
	                response.setContentType("application/octet-stream");
	            }

	            response.setContentLengthLong(file.length());

	            try (FileInputStream fis = new FileInputStream(file);
	                 OutputStream os = response.getOutputStream()) {

	                byte[] buffer = new byte[4096];
	                int bytesRead;
	                while ((bytesRead = fis.read(buffer)) != -1) {
	                    os.write(buffer, 0, bytesRead);
	                }
	                os.flush();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }

}


