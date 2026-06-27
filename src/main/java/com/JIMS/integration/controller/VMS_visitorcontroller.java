package com.JIMS.integration.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.JIMS.integration.entity.VMS_visitors;
import com.JIMS.integration.interfaces.VMS_VisitorService;
import com.JIMS.integration.repository.VMS_visitorinterface;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/api/visitor")
public class VMS_visitorcontroller {

	
	Logger logger = LogManager.getLogger(SeriesMasterNewController.class);
	@Autowired
	private VMS_VisitorService visitorService;

	// Add or update visitor
	@PostMapping
	public ResponseEntity<Object> addOrUpdateVisitor(@RequestBody VMS_visitors visitor) {
		try {
			VMS_visitors savedVisitor = visitorService.saveOrUpdateItem(visitor);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResponseMessage("Success", "Record inserted successfully"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("Error", "Record not inserted. Please try again"));
		}
	}

	// Update visitor by ID with partial update support
	@PutMapping("/{V_id}")
    public ResponseEntity<Object> updateVisitorById(@PathVariable int V_id, @RequestBody VMS_visitors visitor) {
        Optional<VMS_visitors> existingVisitor = visitorService.getItemById(V_id);

        if (existingVisitor.isPresent()) {
        	VMS_visitors updatedVisitor = existingVisitor.get();
        	
                // Check and update only provided fields
                if (visitor.getVisitor_id() != null) {
                    updatedVisitor.setVisitor_id(visitor.getVisitor_id());
                }
                if (visitor.getCompany_id() != null) {
                    updatedVisitor.setCompany_id(visitor.getCompany_id());
                }
                if (visitor.getPass_id() != null) {
                    updatedVisitor.setPass_id(visitor.getPass_id());
                }
                if (visitor.getOutremarks() != null) {
                    updatedVisitor.setOutremarks(visitor.getOutremarks());
                }
                if (visitor.getVecno() != null) {
                    updatedVisitor.setVecno(visitor.getVecno());
                }
                if (visitor.getVisitor_name() != null) {
                    updatedVisitor.setVisitor_name(visitor.getVisitor_name());
                }
                if (visitor.getIntime() != null) {
                    updatedVisitor.setIntime(visitor.getIntime());
                }
                if (visitor.getOuttime() != null) {
                    updatedVisitor.setOuttime(visitor.getOuttime());
                }
                if (visitor.getWtm() != null) {
                    updatedVisitor.setWtm(visitor.getWtm());
                }
                if (visitor.getDepartment() != null) {
                    updatedVisitor.setDepartment(visitor.getDepartment());
                }
                if (visitor.getContact_no() != null) {
                    updatedVisitor.setContact_no(visitor.getContact_no());
                }
                if (visitor.getVec_type_id() != null) {
                    updatedVisitor.setVec_type_id(visitor.getVec_type_id());
                }
                if (visitor.getPurpose() != null) {
                    updatedVisitor.setPurpose(visitor.getPurpose());
                }
                if (visitor.getRemarks() != null) {
                    updatedVisitor.setRemarks(visitor.getRemarks());
                }
                if (visitor.getFactoryId() != null) {
                    updatedVisitor.setFactoryId(visitor.getFactoryId());
                }
                if (visitor.getCreated_by() != null) {
                    updatedVisitor.setCreated_by(visitor.getCreated_by());
                }
                if (visitor.getCreated_date() != null) {
                    updatedVisitor.setCreated_date(visitor.getCreated_date());
                }
                if (visitor.getModified_by() != null) {
                    updatedVisitor.setModified_by(visitor.getModified_by());
                }
                if (visitor.getModified_date() != null) {
                    updatedVisitor.setModified_date(visitor.getModified_date());
                } else {
                    updatedVisitor.setModified_date(visitor.getModified_date());
                }
                logger.info("WTM received: " + visitor.getWtm());
            // Save the updated visitor
            visitorService.saveOrUpdateItem(updatedVisitor);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Success", "Visitor updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage("Error", "Visitor not found"));
        }
    }

	// Get all visitors
//	@GetMapping
//	public ResponseEntity<List<VMS_visitors>> getAllVisitors(
//	        @RequestParam Integer factory_id) {
//
//	    List<VMS_visitors> visitors =
//	            visitorService.getVisitorsByFactoryId(factory_id);
//
//	    if (visitors.isEmpty()) {
//	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	    }
//
//	    return new ResponseEntity<>(visitors, HttpStatus.OK);
//	}
	
	@Autowired
	private VMS_visitorinterface vm_repo;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllVisitors(
	        @RequestParam Integer factory_id,
	        @RequestParam(defaultValue = "") String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    String searchParam =
	            (search == null || search.trim().isEmpty())
	                    ? null
	                    : search.trim();

	    Page<VMS_visitors> pageData =
	            visitorService.getVisitorsByFactoryId(
	                    factory_id,
	                    searchParam,
	                    pageable);

	    long totalWithoutFilter =
	    		vm_repo.countByFactoryId(factory_id);

	    Map<String, Object> response = new HashMap<>();

	    response.put("data", pageData.getContent());
	    response.put("currentPage", pageData.getNumber());
	    response.put("totalItems", pageData.getTotalElements());
	    response.put("totalRecords", totalWithoutFilter);
	    response.put("totalPages", pageData.getTotalPages());

	    return ResponseEntity.ok(response);
	}

	// Get visitor by ID
	@GetMapping("/{id}")
	public ResponseEntity<VMS_visitors> getVisitorById(@PathVariable int id) {
		Optional<VMS_visitors> visitor = visitorService.getItemById(id);
		if (visitor.isPresent()) {
			return new ResponseEntity<>(visitor.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

	@Autowired
	@Qualifier("jimsDataSource")
	private DataSource jimsDataSource;

	// Get GP number based on GP_type and order by GP_no DESC
//	@GetMapping("/jims/autovisitorno")
//	public @ResponseBody int getvechicleData() {
//
//		// SQL query to get the top GP_no based on GP_type
//		String sql = "  SELECT TOP 1 count(Visitor_id) as visitor_id FROM [VMS_Visitors]  ORDER BY Visitor_id DESC";
//		int count = 0;
//
//		try (Connection connection = jimsDataSource.getConnection();
//				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//			// Execute the query
//			ResultSet resultSet = preparedStatement.executeQuery();
//
//			// Check if there are results
//			if (resultSet.next()) {
//				count = resultSet.getInt(1);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		// Return the result containing the GP_no
//		return count;
//
//	}
	
	// Get GP number based on GP_type and order by GP_no DESC
	@GetMapping("/jims/autovisitorno")
	public @ResponseBody int getVisitorData() {

	    int count = 0;

	    try (Connection connection = jimsDataSource.getConnection()) {

	        // Check whether any records exist
	        String countSql = "SELECT COUNT(*) FROM VMS_Visitors";

	        try (PreparedStatement ps = connection.prepareStatement(countSql);
	             ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                count = rs.getInt(1);
	            }
	        }

	        // If no records exist, return 0
	        if (count == 0) {
	            return 0;
	        }

	        // Otherwise, return the latest identity value
	        String sql = "SELECT MAX(V_id) FROM VMS_Visitors";

	        try (PreparedStatement ps = connection.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}

	@GetMapping("/jims/passtype")
	public @ResponseBody List<Map<String, String>> getpassdata() {
		String sql = "SELECT pass_id, pass_name FROM VMS_pass_type";
		List<Map<String, String>> passTypeList = new ArrayList<>();

		try (Connection connection = jimsDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery()) {

			while (rs.next()) {
				Map<String, String> passTypeMap = new HashMap<>();
				passTypeMap.put("pass_id", rs.getString("pass_id"));
				passTypeMap.put("pass_name", rs.getString("pass_name"));
				passTypeList.add(passTypeMap);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return passTypeList;
	}

	@GetMapping("/jims/visitorid")
	public @ResponseBody List<Map<String, String>> getvechtype(
	        @RequestParam("factoryId") Integer factoryId) {

	    String sql = """
	        SELECT visitor_id, v_id
	        FROM vms_visitors
	        WHERE outtime IS NULL
	          AND outremarks IS NULL
	          AND Factory_id = ?
	    """;

	    List<Map<String, String>> passTypeList = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        preparedStatement.setInt(1, factoryId);

	        try (ResultSet rs = preparedStatement.executeQuery()) {
	            while (rs.next()) {
	                Map<String, String> passTypeMap = new HashMap<>();
	                passTypeMap.put("visitor_id", rs.getString("visitor_id"));
	                passTypeMap.put("v_id", rs.getString("v_id"));
	                passTypeList.add(passTypeMap);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return passTypeList;
	}

	
	@GetMapping("/jims/vechtype")
	public @ResponseBody List<Map<String, String>> getvisitorid() {
		String sql = " select Vtype,vid from VTS_Vechicletype where vid  in (14,15,16,17)";
		List<Map<String, String>> passTypeList = new ArrayList<>();

		try (Connection connection = jimsDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery()) {

			while (rs.next()) {
				Map<String, String> passTypeMap = new HashMap<>();
				passTypeMap.put("vid", rs.getString("vid"));
				passTypeMap.put("Vtype", rs.getString("Vtype"));
				passTypeList.add(passTypeMap);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return passTypeList;
	}
	
    
    @GetMapping("/jims/visitoroutfetch")
    public @ResponseBody List<Map<String, String>> getVehicleData(@RequestParam("Visitor_id") String visitorId) {
        String sql = "SELECT        dbo.VMS_Visitors.Visitor_id,dbo.VMS_Visitors.Company_id,dbo.VMS_Visitors.Pass_id as pass_id,dbo.VMS_Visitors.Vec_type_id, dbo.VMS_Visitors.visitor_name, dbo.VMS_Companymaster.Company_name, dbo.VMS_Companymaster.Address, dbo.VMS_Citymaster.City_name, dbo.VMS_Pass_type.Pass_name, "
        		+ "                         dbo.VTS_Vechicletype.Vtype,VMS_Visitors.Wtm as wtm,VMS_Visitors.department as department,dbo.VMS_Visitors.Vecno, dbo.VMS_Visitors.Intime, dbo.VMS_Visitors.Contact_no, dbo.VMS_Visitors.Purpose, dbo.VMS_Visitors.Remarks, dbo.VMS_Visitors.Created_date "
        		+ "FROM            dbo.VMS_Citymaster RIGHT OUTER JOIN"
        		+ "                         dbo.VMS_Companymaster ON dbo.VMS_Citymaster.City_id = dbo.VMS_Companymaster.City_id RIGHT OUTER JOIN"
        		+ "                         dbo.VMS_Visitors LEFT OUTER JOIN"
        		+ "                         dbo.VTS_Vechicletype ON dbo.VMS_Visitors.Vec_type_id = dbo.VTS_Vechicletype.vid LEFT OUTER JOIN"
        		+ "                         dbo.VMS_Pass_type ON dbo.VMS_Visitors.Pass_id = dbo.VMS_Pass_type.Pass_id ON dbo.VMS_Companymaster.Company_id = dbo.VMS_Visitors.Company_id"
        		+ "						 where  dbo.VMS_Visitors.Visitor_id=?";

        List<Map<String, String>> vehicleList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, visitorId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> vehicleMap = new HashMap<>();
                    vehicleMap.put("Visitor_id", rs.getString("Visitor_id"));
                    vehicleMap.put("visitor_name", rs.getString("visitor_name"));
                    vehicleMap.put("Company_name", rs.getString("Company_name"));
                    vehicleMap.put("Address", rs.getString("Address"));
                    vehicleMap.put("City_name", rs.getString("City_name"));
                    vehicleMap.put("Pass_name", rs.getString("Pass_name"));
                    vehicleMap.put("Vtype", rs.getString("Vtype"));
                    vehicleMap.put("wtm", rs.getString("wtm"));
                    vehicleMap.put("department", rs.getString("department"));
                    vehicleMap.put("Vecno", rs.getString("Vecno"));
                    vehicleMap.put("Intime", rs.getString("Intime"));
                    vehicleMap.put("Contact_no", rs.getString("Contact_no"));
                    vehicleMap.put("Purpose", rs.getString("Purpose"));
                    vehicleMap.put("Remarks", rs.getString("Remarks"));
                    vehicleMap.put("Company_id", rs.getString("Company_id"));
                    vehicleMap.put("pass_id", rs.getString("pass_id"));
                    vehicleMap.put("Vec_type_id", rs.getString("Vec_type_id"));
                    vehicleMap.put("Created_date", rs.getString("Created_date"));
                    vehicleList.add(vehicleMap);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ideally use logger here
        }

        return vehicleList;
    }
    
    @GetMapping("/jims/visitorsearch")
    public @ResponseBody List<Map<String, String>> getvisitorsearch(
            @RequestParam("factoryId") Integer factoryId) {

        String sql =
            "WITH RankedVisitors AS ( " +
            "    SELECT " +
            "        CONVERT(nvarchar(50), contact_no) AS contact_no, " +
            "        visitor_id, " +
            "        created_date, " +
            "        ROW_NUMBER() OVER (PARTITION BY contact_no ORDER BY created_date DESC) AS rn " +
            "    FROM VMS_Visitors " +
            "    WHERE contact_no IS NOT NULL " +
            "      AND ISNUMERIC(contact_no) = 1 " +
            "      AND TRY_CAST(contact_no AS bigint) <> 0 " +
            "      AND Factory_id = ? " +
            ") " +
            "SELECT TOP 5000 " +
            "    contact_no, " +
            "    visitor_id, " +
            "    created_date " +
            "FROM RankedVisitors " +
            "WHERE rn = 1 " +
            "ORDER BY created_date DESC";

        List<Map<String, String>> passTypeList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // ✅ SET PARAMETER HERE
            preparedStatement.setInt(1, factoryId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> passTypeMap = new HashMap<>();
                    passTypeMap.put("contact_no", rs.getString("contact_no"));
                    passTypeMap.put("visitor_id", rs.getString("visitor_id"));
                    passTypeList.add(passTypeMap);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return passTypeList;
    }

	
	@GetMapping("/jims/visitorgatepass")
	public @ResponseBody List<String> getAllVisitorIds( @RequestParam("factoryId") Integer factoryId) {
	    String sql = "SELECT Visitor_id FROM VMS_Visitors where Factory_id = ? and  Outtime is null ORDER BY Visitor_id DESC";
	    List<String> visitorIds = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	    	 preparedStatement.setInt(1, factoryId);
	        // No need to set any parameters if you don't have any placeholders in the query
	        ResultSet resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {
	            // Add the Visitor_id from the result set to the list
	            visitorIds.add(resultSet.getString("Visitor_id"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return visitorIds; 
	}
	
	@GetMapping("/jims/getVisitorDetails")
	public @ResponseBody List<Map<String, Object>> getVisitorDetails(@RequestParam("visitor_id") String visitorId) {

	    // SQL query to fetch details based on the Visitor ID (visitor_id)
	    String sql = "SELECT DISTINCT vs.V_id, vs.Visitor_id, vs.visitor_name, vc.Company_name, vs.Contact_no, vs.Created_date, um.username, vs.department,vs.Capture,vs.Vecno,vs.Wtm "
	            + "FROM VMS_Visitors vs "
	            + "LEFT JOIN VMS_Companymaster vc ON vc.Company_id = vs.Company_id "
	            + "LEFT JOIN USERS_MASTER um ON um.id = vs.Wtm "
	            + "GROUP BY vs.Visitor_id, vs.V_id, vs.visitor_name, vc.Company_name, vs.Contact_no, vs.Created_date, um.username, vs.department,vs.Capture,vs.Vecno,vs.Wtm "
	            + "HAVING vs.Visitor_id = ?";

	    List<Map<String, Object>> resultList = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        // Set the visitor_id parameter for SQL query
	        preparedStatement.setString(1, visitorId);

	        // Execute the query
	        ResultSet resultSet = preparedStatement.executeQuery();

	        // Iterate through the results and map them to resultList
	        while (resultSet.next()) {
	            Map<String, Object> resultMap = new HashMap<>();
	            resultMap.put("V_id", resultSet.getString("V_id"));
	            resultMap.put("Visitor_id", resultSet.getString("Visitor_id"));
	            resultMap.put("visitor_name", resultSet.getString("visitor_name"));
	            resultMap.put("Company_name", resultSet.getString("Company_name"));
	            resultMap.put("Contact_no", resultSet.getString("Contact_no"));
	            resultMap.put("Created_date", resultSet.getString("Created_date"));
	            resultMap.put("username", resultSet.getString("username"));
	            resultMap.put("department", resultSet.getString("department"));
	            resultMap.put("vehicle_No", resultSet.getString("Vecno"));
	            resultMap.put("wtm", resultSet.getString("Wtm"));
	            //resultMap.put("photo_url", resultSet.getString("Capture"));
	            
	            
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
	
	@GetMapping("/jims/getVisitorDetailsByDate")
	public @ResponseBody List<Map<String, Object>> getVisitorDetailsByDateRange(
	        @RequestParam("fromDate") String fromDate,
	        @RequestParam("toDate") String toDate) {

	    String sql =
	        "SELECT vs.V_id, vs.Visitor_id, vs.visitor_name, vc.Company_name, vc.Address, " +
	        "vs.Vecno, vs.Wtm,vs.Contact_no, CONVERT(VARCHAR(10), vs.Created_date, 23) \r\n"
	        + "        + ' ' + CONVERT(VARCHAR(8), vs.Intime, 108) AS VisitorDate_InTime, CASE \r\n"
	        + "        WHEN vs.Outtime IS NULL THEN NULL\r\n"
	        + "        ELSE CONVERT(VARCHAR(10), vs.modified_date, 23) \r\n"
	        + "             + ' ' + CONVERT(VARCHAR(8), vs.Outtime, 108)\r\n"
	        + "    END AS VisitorDate_OutTime, " +
	        "um.username AS username, vs.Remarks, vs.outremarks, vs.department, fr.factory_name " +
	        "FROM VMS_Visitors vs " +
	        "LEFT JOIN VMS_Companymaster vc ON vc.Company_id = vs.Company_id " +
	        "LEFT JOIN USERS_MASTER um ON um.id = vs.Wtm " +
	        "LEFT JOIN FACTORY_MASTER fr on fr.id=vs.Factory_id " +
	        "WHERE CAST(vs.Created_date AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) " +
	        "ORDER BY vs.Created_date DESC";

	    List<Map<String, Object>> resultList = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        preparedStatement.setString(1, fromDate);
	        preparedStatement.setString(2, toDate);

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("V_id", resultSet.getInt("V_id"));
	            map.put("Visitor_id", resultSet.getString("Visitor_id"));
	            map.put("visitor_name", resultSet.getString("visitor_name"));
	            map.put("Company_name", resultSet.getString("Company_name"));
	            map.put("Address", resultSet.getString("Address"));
	            map.put("Vecno", resultSet.getString("Vecno"));
	            map.put("Contact_no", resultSet.getString("Contact_no"));
	            map.put("VisitorDate_InTime", resultSet.getString("VisitorDate_InTime"));
	            map.put("VisitorDate_OutTime", resultSet.getString("VisitorDate_OutTime"));
	            map.put("username", resultSet.getString("username"));
	            map.put("department", resultSet.getString("department"));
	            map.put("Remarks", resultSet.getString("Remarks"));
	            map.put("outremarks", resultSet.getString("outremarks"));
	            map.put("factory_name", resultSet.getString("factory_name"));
	            map.put("wtm",resultSet.getString("Wtm"));
	            resultList.add(map);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return resultList;
	}
	
	@GetMapping("/jims/companies")
	public @ResponseBody List<Map<String, Object>> getCompanyNames() {
	    String sql = "SELECT Company_id, Company_name FROM VMS_Companymaster";
	    List<Map<String, Object>> companies = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("id", resultSet.getInt("Company_id"));
	            map.put("name", resultSet.getString("Company_name"));
	            companies.add(map);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return companies;
	}
	
	@GetMapping("/jims/departments")
	public @ResponseBody List<Map<String, Object>> getDepartments() {

	    String sql = "SELECT dept_id, Department_name FROM TAB_DEPARTMENTS";
	    List<Map<String, Object>> departments = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Map<String, Object> dept = new HashMap<>();
	            dept.put("dept_id", rs.getInt("dept_id"));
	            dept.put("department_name", rs.getString("Department_name"));
	            departments.add(dept);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return departments;
	}

	
	@GetMapping("/jims/getVisitorDetailsByFilter")
	public @ResponseBody List<Map<String, Object>> getVisitorDetailsByFilter(
	        @RequestParam(value = "company", required = false) String company,
	        @RequestParam(value = "department", required = false) String department) {

	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT DISTINCT ")
	       .append("vs.V_id, vs.Visitor_id, vs.visitor_name, vc.Company_name, vc.Address,vs.Wtm,")

	       // Created Date + Intime → VisitorDate_InTime
	       .append("(CONVERT(VARCHAR(10), vs.Created_date, 23) + ' ' + ")
	       .append(" CONVERT(VARCHAR(8), vs.Intime, 108)) AS VisitorDate_InTime, ")

	       // Modified Date + Outtime → VisitorDate_OutTime
	       .append("CASE WHEN vs.Outtime IS NULL THEN NULL ")
	       .append("     ELSE (CONVERT(VARCHAR(10), vs.modified_date, 23) + ' ' + ")
	       .append("           CONVERT(VARCHAR(8), vs.Outtime, 108)) ")
	       .append("END AS VisitorDate_OutTime, ")

	       .append("um.username AS username, vs.department, fr.factory_name ")
	       .append("FROM VMS_Visitors vs ")
	       .append("LEFT JOIN VMS_Companymaster vc ON vc.Company_id = vs.Company_id ")
	       .append("LEFT JOIN USERS_MASTER um ON um.id = vs.Wtm ")
	       .append("LEFT JOIN FACTORY_MASTER fr ON fr.id = vs.Factory_id ")
	       .append("WHERE 1=1 ");

	    // Apply filters
	    if (company != null && !company.isEmpty()) {
	        sql.append("AND vs.Company_id = ? ");
	    }

	    if (department != null && !department.isEmpty()) {
	        sql.append("AND vs.department = ? ");
	    }

	    // Final ordering
	    sql.append("ORDER BY \r\n"
	    		+ "    CONVERT(VARCHAR(10), vs.Created_date, 23) + ' ' + CONVERT(VARCHAR(8), vs.Intime, 108) DESC\r\n"
	    		+ "");

	    List<Map<String, Object>> resultList = new ArrayList<>();

	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

	        int index = 1;

	        if (company != null && !company.isEmpty()) {
	            preparedStatement.setString(index++, company);
	        }

	        if (department != null && !department.isEmpty()) {
	            preparedStatement.setString(index++, department);
	        }

	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("V_id", rs.getInt("V_id"));
	            map.put("Visitor_id", rs.getString("Visitor_id"));
	            map.put("visitor_name", rs.getString("visitor_name"));
	            map.put("Company_name", rs.getString("Company_name"));
	            map.put("Address", rs.getString("Address"));
	            map.put("VisitorDate_InTime", rs.getString("VisitorDate_InTime"));
	            map.put("VisitorDate_OutTime", rs.getString("VisitorDate_OutTime"));
	            map.put("Wtm", rs.getString("username"));
	            map.put("department", rs.getString("department"));
	            map.put("factory_name", rs.getString("factory_name"));
	            map.put("wtm", rs.getString("Wtm"));

	            resultList.add(map);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return resultList;
	}
	
	  @Value("${upload.VMSPath}")
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
