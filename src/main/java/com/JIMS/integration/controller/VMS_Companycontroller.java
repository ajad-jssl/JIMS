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

import com.JIMS.integration.entity.VMS_companymodel;
import com.JIMS.integration.interfaces.VMS_CompanyService;

@CrossOrigin
@RestController
@RequestMapping("/api/VMScompany")
public class VMS_Companycontroller {
	
	@Autowired
	private VMS_CompanyService companyservice;
	@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody VMS_companymodel item) {
        try {
        	VMS_companymodel savedItem = companyservice.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
    }
	
	@PutMapping("/{company_id}")
	public ResponseEntity<Object> updateCompanyById(@PathVariable int company_id, @RequestBody VMS_companymodel company) {
	    Optional<VMS_companymodel> existingCompany = companyservice.getItemById(company_id);
	    
	    if (existingCompany.isPresent()) {
	        VMS_companymodel updatedCompany = existingCompany.get();
	        
	        // Check and update only the provided fields
	        if (company.getCompany_name() != null) {
	            updatedCompany.setCompany_name(company.getCompany_name()); 
	        }
	        if (company.getAddress() != null) {
	            updatedCompany.setAddress(company.getAddress());
	        }
	        if (company.getState_id() != 0) {
	            updatedCompany.setState_id(company.getState_id());
	        }
	        if (company.getCity_id() != 0) {
	            updatedCompany.setCity_id(company.getCity_id());
	        }
	        if (company.getCreated_by() != null) {
	            updatedCompany.setCreated_by(company.getCreated_by());
	        }
	        if (company.getCreated_date() != null) {
	            updatedCompany.setCreated_date(company.getCreated_date());
	        }
	        if (company.getModified_by() != null) {
	            updatedCompany.setModified_by(company.getModified_by());
	        }
	        if (company.getModified_date() != null) {
	            updatedCompany.setModified_date(company.getModified_date());
	        }
	        
	        // Save the updated company
	        companyservice.saveOrUpdateItem(updatedCompany);

	        return ResponseEntity.status(HttpStatus.OK)
	                             .body(new ResponseMessage("Success", "Company updated successfully"));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(new ResponseMessage("Error", "Company not found"));
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
	public ResponseEntity<List<VMS_companymodel>> getAllItems() {
	    List<VMS_companymodel> items = companyservice.getAllItems();
	    if (items.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
	    }
	    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
	}

	@GetMapping("/paging")
	public Page<VMS_companymodel> listCompaniesPaged(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "") String search
	) {
	    Pageable pageable = PageRequest.of(page, size);
	    if (search == null || search.trim().isEmpty()) {
	        return companyservice.findAll(pageable);  // add this in service too
	    }
	    return companyservice.searchByCompanyName(search.trim(), pageable);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<VMS_companymodel> getItemById(@PathVariable int id) {
	    Optional<VMS_companymodel> item = companyservice.getItemById(id);
	    if (item.isPresent()) {
	        return new ResponseEntity<>(item.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	@Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;
	@GetMapping("/company/jims/details")
	public @ResponseBody List<Map<String, Object>> getCompanyDetails(@RequestParam("company_id") int companyId) {

	    // SQL query to get Company details with City name based on Company_id
	    String sql = "SELECT dbo.VMS_Companymaster.Company_id, " +
	                 "dbo.VMS_Companymaster.Company_name, " +
	                 "dbo.VMS_Companymaster.Address, " +
	                 "dbo.VMS_Citymaster.City_name " +
	                 "FROM dbo.VMS_Companymaster " +
	                 "LEFT OUTER JOIN dbo.VMS_Citymaster ON dbo.VMS_Companymaster.City_id = dbo.VMS_Citymaster.City_id " +
	                 "WHERE dbo.VMS_Companymaster.Company_id = ?";

	    List<Map<String, Object>> resultList = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        preparedStatement.setInt(1, companyId);  // Set the company_id parameter

	        // Execute the query
	        ResultSet resultSet = preparedStatement.executeQuery();

	        // Iterate through the results and add them to the resultList
	        while (resultSet.next()) {
	            Map<String, Object> resultMap = new HashMap<>();
	            int companyIdResult = resultSet.getInt("Company_id");
	            String companyName = resultSet.getString("Company_name");
	            String address = resultSet.getString("Address");
	            String cityName = resultSet.getString("City_name");

	            // Put the values into the result map
	            resultMap.put("Company_id", companyIdResult);
	            resultMap.put("Company_name", companyName);
	            resultMap.put("Address", address);
	            resultMap.put("City_name", cityName);

	            resultList.add(resultMap);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    // Return the list of maps containing company details and city name
	    return resultList;
	}
	@GetMapping("/checkcompany")
	public ResponseEntity<Map<String, Object>> checkcompanyExists(@RequestParam("Company_name") String Company_name) {
	    String sql = "SELECT COUNT(*) FROM VMS_Companymaster WHERE Company_name = ?";
	    Map<String, Object> response = new HashMap<>();
	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, Company_name);
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
	@GetMapping("/city/is-used/{cityId}")
	public ResponseEntity<Map<String, Boolean>> isCityUsed(
	        @PathVariable int cityId) {

	    String sql = "SELECT COUNT(*) FROM VMS_Companymaster WHERE City_id = ?";

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, cityId);
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

	@GetMapping("/transporter/is-used/{transname}")
	public ResponseEntity<Map<String, Boolean>> isTransporterUsed(
	        @PathVariable String transname) {

	    String sql = "SELECT COUNT(*) FROM VTS_invoice_exit WHERE transname = ? AND outtime is null";

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, transname.trim());

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

	
	
	@GetMapping("/pass/is-used/{passId}")
	public ResponseEntity<Map<String, Boolean>> isPassUsed(
	        @PathVariable int passId) {

	    String sql = """
	        SELECT COUNT(*) 
	        FROM VMS_Visitors 
	        WHERE Pass_id = ? 
	        AND Outtime IS NULL
	    """;

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, passId);
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
