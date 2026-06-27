package com.JIMS.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.UserSupplier;



@RestController
@RequestMapping("/api/usersupplier")
public class UserSupplierController {

	@Autowired
	@Qualifier("misDataSource")
	private DataSource misDataSource;

	@Autowired
	@Qualifier("misDataSource2")
	private DataSource misDataSource2;
	
	@Autowired
	@Qualifier("jimsDataSource")
	private DataSource jimsDataSource;

	@PostMapping("/assign")
	public ResponseEntity<String> assignUser(@RequestBody UserSupplier userSupplier, @RequestParam String factory) {
		userSupplier.setEdt(LocalDateTime.now());
		String sql = "INSERT INTO usersupplier (uid, supid, euid, edt,factory) VALUES (?, ?, ?, ?,?)";

		// Insert into Factory 1
		if (factory.equalsIgnoreCase("1") || factory.equalsIgnoreCase("all")) {
			insertIntoDB(misDataSource, sql, userSupplier, factory);
		}

		// Insert into Factory 2
		if (factory.equalsIgnoreCase("2") || factory.equalsIgnoreCase("all")) {
			insertIntoDB(misDataSource2, sql, userSupplier, factory);
		}

		return ResponseEntity.ok("Inserted Successfully into " + factory);
	}

	private void insertIntoDB(DataSource ds, String sql, UserSupplier us, String factory) {
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, us.getUid());
			ps.setLong(2, us.getSupid());
			ps.setString(3, us.getEuid());
			ps.setObject(4, us.getEdt());
			ps.setString(5, factory);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/supplierid")
	public @ResponseBody List<Map<String, Object>> getSupplierIdByUser(@RequestParam("uid") int uid,
			@RequestParam("factory_id") int factoryId) {

		String sql = "SELECT supid FROM usersupplier WHERE uid = ?";
		List<Map<String, Object>> resultList = new ArrayList<>();

		DataSource selectedDataSource;

		if (factoryId == 1) {
			selectedDataSource = misDataSource;
		} else {
			selectedDataSource = misDataSource2;
		}

		try (Connection connection = selectedDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setInt(1, uid);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("supid", resultSet.getInt("supid"));
				resultList.add(resultMap);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	public String getFactoryByUserId(String uid) {

		String sql = "SELECT TOP 1 factory FROM usersupplier WHERE uid = ?";
		String factory = null;

		try (Connection connection = misDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, uid);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				factory = rs.getString("factory");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return factory;
	}
	
	
	
	
	public String getLocation(String uid) {

		String sql = "select Location From JIMS.dbo.UserFactoryMappingJIMS where User_Id =?";
		String factory = null;

		try (Connection connection = misDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, uid);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				factory = rs.getString("Location");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return factory;
	}
	  
	  
		
		
	public String getEmpCode(String uid) {

		String sql = "select emp_id From JIMS.dbo.UserFactoryMappingJIMS where User_Id =?";
		String factory = null;

		try (Connection connection = misDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, uid);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				factory = rs.getString("emp_id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return factory;
	}
	
	
	
	public String getFactoryByG2UserId(String uid) {
		
		System.out.println("This is what UserId i am getting :"+uid);

		String sql = "SELECT TOP 1 FactoryId FROM UserFactoryMappingJIMS WHERE User_Id = ?";
		String factory = null;

		try (Connection connection = jimsDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, uid);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				factory = rs.getString("FactoryId");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Before sending the factory we should Print :"+factory);

		return factory;
	}
	
	
	public boolean isSCUser(String userId) {

	    String sql = "SELECT COUNT(*) " +
	                 "FROM Users " +
	                 "WHERE user_id = ? " +
	                 "AND user_menus = ',SC'";

	    boolean isSC = false;

	    try (
	        Connection connection = misDataSource.getConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement(sql)
	    ) {

	        preparedStatement.setString(1, userId);

	        ResultSet rs = preparedStatement.executeQuery();

	        if (rs.next()) {

	            int count = rs.getInt(1);

	            isSC = count > 0;
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();
	    }

	    return isSC;
	}
	
	

	@GetMapping("/all-users-distinct")
	public @ResponseBody List<Map<String, Object>> getAllUsersDistinct() {

		String sql = "SELECT  us.uid, us.supid, us.factory, u.USER_NAME " + "FROM usersupplier us "
				+ "LEFT JOIN Users u ON us.uid = u.user_id order by us.edt desc";

		// Use LinkedHashMap → keeps order  fast lookup
		Map<Long, Map<String, Object>> uniqueUsers = new LinkedHashMap<>(8000);

		// Parallel fetch (IMPORTANT )
		Thread t1 = new Thread(() -> fetchAndMerge(misDataSource, sql, uniqueUsers));
		Thread t2 = new Thread(() -> fetchAndMerge(misDataSource2, sql, uniqueUsers));

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return new ArrayList<>(uniqueUsers.values());
	}
	
	
	@GetMapping("/user-by-id")
	public ResponseEntity<Map<String, Object>> getUserFromTwoDB(
	        @RequestParam("uid") int uid) {

	    String sql = "SELECT us.uid, us.supid, us.factory, u.USER_NAME " +
	                 "FROM usersupplier us " +
	                 "INNER JOIN Users u ON us.uid = u.user_id " +
	                 "WHERE us.uid = ?";

	    // 1️⃣ Check FIRST DB
	    Map<String, Object> result = fetchSingleRecord(misDataSource, sql, uid);

	    if (result != null) {
	        return ResponseEntity.ok(result); // ✅ RETURN immediately
	    }

	    // 2️⃣ Check SECOND DB only if first failed
	    result = fetchSingleRecord(misDataSource2, sql, uid);

	    if (result != null) {
	        return ResponseEntity.ok(result);
	    }

	    // 3️⃣ If not found anywhere
	    return ResponseEntity.ok(null);
	}
	private Map<String, Object> fetchSingleRecord(DataSource ds, String sql, int uid) {

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, uid);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("uid", rs.getInt("uid"));
	            map.put("supid", rs.getInt("supid"));
	            map.put("factory", rs.getString("factory"));
	            map.put("user_name", rs.getString("USER_NAME"));
	            return map;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null; // ❌ no data
	}

	private void fetchAndMerge(DataSource ds, String sql, Map<Long, Map<String, Object>> uniqueUsers) {

		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Long uid = rs.getLong("uid");

				Long supid = rs.getLong("supid");
				String factory = rs.getString("factory");
				String userName = rs.getString("USER_NAME");

				uniqueUsers.putIfAbsent(uid, createRow(uid, supid, factory, userName));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> createRow(Long uid, Long supid, String factory, String userName) {
	    Map<String, Object> row = new HashMap<>();
	    row.put("uid", uid);
	    row.put("supid", supid);
	    row.put("factory", factory);
	    row.put("user_name", userName);
	    return row;
	}
	
	
	@GetMapping("/suppliers-distinct")
	public ResponseEntity<List<Map<String, Object>>> getSuppliersFromBothDB() {

	    String sql = "SELECT supplier_id, code, (code + ' ' + name) as name, suppcat_id " +
	                 "FROM Suppliers WHERE suppcat_id = 21";

	    List<Map<String, Object>> list1 = fetchSuppliers(misDataSource, sql);
	    List<Map<String, Object>> list2 = fetchSuppliers(misDataSource2, sql);

	    //  Merge + Distinct by CODE
	    Map<String, Map<String, Object>> uniqueMap = new LinkedHashMap<>();

	    for (Map<String, Object> row : list1) {
	        String code = (String) row.get("code");
	        uniqueMap.put(code, row);
	    }

	    for (Map<String, Object> row : list2) {
	        String code = (String) row.get("code");

	        // Only add if not already present
	        uniqueMap.putIfAbsent(code, row);
	    }

	    return ResponseEntity.ok(new ArrayList<>(uniqueMap.values()));
	}
	
	private List<Map<String, Object>> fetchSuppliers(DataSource ds, String sql) {

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            Map<String, Object> map = new HashMap<>();
	            map.put("supplier_id", rs.getInt("supplier_id"));
	            map.put("code", rs.getString("code").trim());
	            map.put("name", rs.getString("name").trim());
	            map.put("suppcat_id", rs.getInt("suppcat_id"));

	            list.add(map);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	
	@PostMapping("/update-factory")
	public ResponseEntity<Map<String, Object>> updateFactory(
	        @RequestParam String uid,
	        @RequestParam String factory) {

	    String sql = "UPDATE usersupplier SET factory = ? WHERE uid = ?";

	    boolean updatedDb1 = updateFactoryInDB(misDataSource, sql, uid, factory);
	    boolean updatedDb2 = updateFactoryInDB(misDataSource2, sql, uid, factory);

	    Map<String, Object> response = new HashMap<>();

	    // ✅ SUCCESS if ANY DB updated
	    if (updatedDb1 || updatedDb2) {
	        response.put("status", "SUCCESS");
	        response.put("message", "Factory updated in both DBs (where applicable)");
	    } else {
	        response.put("status", "FAILED");
	        response.put("message", "No record found in both DBs");
	    }

	    response.put("db1Updated", updatedDb1);
	    response.put("db2Updated", updatedDb2);

	    return ResponseEntity.ok(response);
	}
	
	private boolean updateFactoryInDB(DataSource ds, String sql, String uid, String factory) {
	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, factory);
	        ps.setString(2, uid);

	        int rows = ps.executeUpdate();

	        return rows > 0; // ✅ true if updated

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	
	// G2 User FActory Assgin For Jims 
	
	
	@PostMapping("/assignG2User")
	public ResponseEntity<String> assignG2User(
	        @RequestBody Map<String, String> requestData) {

	    try {

	        String user_id = requestData.get("user_id");
	        String username = requestData.get("username");
	        String factory = requestData.get("factory");
	        String created_by = requestData.get("created_by");
	        String location = requestData.get("location");
	        String  EmpCode = requestData.get("EmployeeCode");
	        
	        Integer Department =Integer.valueOf(requestData.get("department")) ;

	        String sql = "INSERT INTO UserFactoryMappingJIMS "
	                + "(User_Id, UserName, FactoryId,Location,emp_id,Department_Id, Created_By, Created_Date) "
	                + "VALUES (?, ?, ?,?,?, ?,?, GETDATE())";

	        JdbcTemplate jdbcTemplate = new JdbcTemplate(jimsDataSource);

	        jdbcTemplate.update(
	                sql,
	                user_id,
	                username,
	                factory,
	                location,
	                EmpCode,
	                Department,
	                created_by
	        );

	        return ResponseEntity.ok("User Assigned Successfully");

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to Assign User");
	    }
	}

	
	@GetMapping("/getG2UserMappings")
	public ResponseEntity<List<Map<String, Object>>> getG2UserMappings() {

	    try {

	        String sql = "SELECT User_Id, UserName, FactoryId,Location  "
	                   + "FROM UserFactoryMappingJIMS";

	        JdbcTemplate jdbcTemplate = new JdbcTemplate(jimsDataSource);

	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

	        return ResponseEntity.ok(result);

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.emptyList());
	    }
	}
	
	@GetMapping("/getG2UserById")
	public ResponseEntity<?> getG2UserById(@RequestParam String user_id) {

	    try {

	        String sql = "SELECT UserName, FactoryId,Location,Department_Id,emp_id   " +
	                     "FROM UserFactoryMappingJIMS " +
	                     "WHERE User_Id = ?";

	        JdbcTemplate jdbcTemplate = new JdbcTemplate(jimsDataSource);

	        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, user_id);

	        if (result.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("User not found");
	        }

	        return ResponseEntity.ok(result.get(0));

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error fetching user");

	    }
	}
	
	
	@PostMapping("/updateG2Factory")
	public ResponseEntity<?> updateG2Factory(
	        @RequestParam String user_id,
	        @RequestParam String factory_id,
	        @RequestParam String modified_by,
	        @RequestParam String Location,
	        @RequestParam Integer department,@RequestParam String Empcode) {

	    try {

	        String sql = "UPDATE UserFactoryMappingJIMS " +
	                     "SET FactoryId = ?, Location = ?, Department_Id =? , emp_id =? ,  " +
	                     "Modified_By = ?, " +
	                     "Modified_Date = GETDATE() " +
	                     "WHERE User_Id = ?";

	        JdbcTemplate jdbcTemplate = new JdbcTemplate(jimsDataSource);

	        int rows = jdbcTemplate.update(
	                sql,
	                factory_id,
	                Location,
	                department,
	                Empcode,
	                modified_by,
	                user_id
	        );

	        if (rows > 0) {

	            return ResponseEntity.ok(Map.of(
	                    "status", "SUCCESS",
	                    "message", "Updated Successfully"
	            ));

	        } else {

	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(Map.of(
	                            "status", "FAILED",
	                            "message", "User Not Found"
	                    ));
	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of(
	                        "status", "ERROR",
	                        "message", "Update Failed"
	                ));
	    }
	}
}
