package com.JIMS.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/websmartuser")
public class WebSmartUserController {

	
	
	@Autowired
	@Qualifier("webDataSource")
	private DataSource webSmartDataSource;
	
	
	
	@GetMapping("getAll")
	public @ResponseBody Map<String,Object> fetchLocationWebSmart(){
		Map<String,Object> response = new HashMap<>();
		List<Map<String,String>> location = new ArrayList<>();
		
		String sql =
			    " SELECT \r\n"
			    + "    emp.EmpId AS id,\r\n"
			    + "    emp.Empcode,\r\n"
			    + "    emp.Name,\r\n"
			    + "    emp.Deptid,\r\n"
			    + "    dep.department,\r\n"
			    + "\r\n"
			    + "    -- Combined display\r\n"
			    + "    emp.Empcode + ' - ' +  emp.Name  AS EmployeeDisplay\r\n"
			    + "\r\n"
			    + "FROM Employee emp\r\n"
			    + "\r\n"
			    + "INNER JOIN Department dep \r\n"
			    + "    ON dep.deptid = emp.Deptid\r\n"
			    + "\r\n"
			    + "WHERE emp.LeftDate IS NULL  and emp.Locid!=2";
		try(Connection con = webSmartDataSource.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery())
		
		
		{
				while(rs.next()) {
					Map<String,String> row = new HashMap<>();
					row.put("id", rs.getString("id"));
					row.put("username",rs.getString("EmployeeDisplay"));
					row.put("department",rs.getString("department"));
					
					location.add(row);
				}
				response.put("Data",location);
		}
		catch(Exception ex) {
			response.put("error","Database Error");
			/* System.out.println(ex); */
		}
				
		return response;
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<?> getUserDepartment(@RequestParam("id") String id) {

	    Map<String, Object> response = new HashMap<>();
	    Map<String, Object> data = new HashMap<>();

	    String sql = "  SELECT \r\n"
	    		+ "    emp.EmpId AS id,\r\n"
	    		+ "    \r\n"
	    		+ "    emp.Name,\r\n"
	    		+ "    emp.Deptid as dept1 ,\r\n"
	    		+ "    dep.department as department\r\n"
	    		+ "\r\n"
	    		+ "   \r\n"
	    		+ "FROM Employee emp\r\n"
	    		+ "\r\n"
	    		+ "INNER JOIN Department dep \r\n"
	    		+ "    ON dep.deptid = emp.Deptid\r\n"
	    		+ "\r\n"
	    		+ "WHERE emp.LeftDate IS NULL and emp.EmpId=?";

	    try (
	            Connection connection = webSmartDataSource.getConnection();
	            PreparedStatement ps = connection.prepareStatement(sql)
	    ) {

	        ps.setString(1, id);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            data.put("department", rs.getString("dept1"));
	            data.put("dept1", rs.getString("department"));

	            response.put("Status", "SUCCESS");
	            response.put("Data", data);

	            return ResponseEntity.ok(response);

	        } else {

	            response.put("Status", "FAILED");
	            response.put("Message", "User not found");

	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	        response.put("Status", "FAILED");
	        response.put("Message", e.getMessage());

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	
	@GetMapping("/getAllDepartment")
	public ResponseEntity<?> getAllDepartment() {

	    List<Map<String, Object>> dataList = new ArrayList<>();

	    String sql =
	            "select deptid as dept_id, department as department_name from Department";

	    try (
	            Connection connection = webSmartDataSource.getConnection();
	            PreparedStatement ps = connection.prepareStatement(sql)
	    ) {

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            Map<String, Object> data = new HashMap<>();

	            data.put("department_name", rs.getString("department_name"));
	            data.put("dept_id", rs.getInt("dept_id"));

	            dataList.add(data);
	        }

	        return ResponseEntity.ok(dataList);

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error: " + e.getMessage());
	    }
	}
	
	@GetMapping("/getAllEmpCode")
	public ResponseEntity<?> getAllEmpCode() {

	    List<Map<String, Object>> dataList = new ArrayList<>();

	    String sql =
	            "  SELECT \r\n"
	            + "    emp.EmpId,\r\n"
	            + "    emp.EmpCode\r\n"
	            + "   \r\n"
	            + "FROM Employee emp\r\n"
	            + "INNER JOIN Department dp \r\n"
	            + "    ON emp.Deptid = dp.deptid\r\n"
	            + "WHERE \r\n"
	            + "    emp.LeftDate IS NULL\r\n"
	            + "\r\n"
	            + "    AND (emp.Locid = 2 OR dp.Locid = 3);";

	    try (
	            Connection connection = webSmartDataSource.getConnection();
	            PreparedStatement ps = connection.prepareStatement(sql)
	    ) {

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            Map<String, Object> data = new HashMap<>();

	            data.put("EmpId", rs.getInt("EmpId"));
	            data.put("EmpCode", rs.getString("EmpCode"));

	            dataList.add(data);
	        }

	        return ResponseEntity.ok(dataList);

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error: " + e.getMessage());
	    }
	}
	
	@GetMapping("/getEmployeesByLocation")
	public ResponseEntity<?> getEmployeesByLocation(
	        @RequestParam("locid") Integer locid) {

	    List<Map<String, Object>> dataList = new ArrayList<>();

	    String sql =
	            "SELECT " +
	            "    EmpId, " +
	            "    EmpCode, " +
	            "    EmpCode + ' - ' + Name AS EmployeeDisplay , pertel as telphone  " +
	            "FROM Employee " +
	            "WHERE Deptid = 50 " +
	            "  AND LeftDate IS NULL " +
	            "  AND Locid = ?";

	    try (
	            Connection connection = webSmartDataSource.getConnection();
	            PreparedStatement ps = connection.prepareStatement(sql)
	    ) {

	        ps.setInt(1, locid);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            Map<String, Object> data = new HashMap<>();

	            data.put("EmpId", rs.getInt("EmpId"));
	            data.put("EmpCode", rs.getString("EmpCode"));
	            data.put("telephone", rs.getString("telphone"));
	            data.put("EmployeeDisplay", rs.getString("EmployeeDisplay"));

	            dataList.add(data);
	        }

	        return ResponseEntity.ok(dataList);

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error: " + e.getMessage());
	    }
	}
	
	
	
	@GetMapping("/check-inpunch")
	public ResponseEntity<?> checkInPunch(
	        @RequestParam(value = "empId", defaultValue = "11136") String empId,
	        @RequestParam("date") String date,
	        @RequestParam("year") String year
	) {
	    Map<String, Object> response = new HashMap<>();

	    // Parse dd/MM from frontend + year → build full date string "YYYY-MM-DD"
	    String fullDate;
	    try {
	        String[] parts = date.split("/");
	        if (parts.length != 2) {
	            response.put("allowed", false);
	            response.put("message", "Invalid date format. Expected dd/MM.");
	            return ResponseEntity.badRequest().body(response);
	        }

	        String day   = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
	        String month = parts[1].length() == 1 ? "0" + parts[1] : parts[1];

	        fullDate = year + "-" + month + "-" + day; // "2026-03-06"

	    } catch (Exception e) {
	        response.put("allowed", false);
	        response.put("message", "Date parsing error: " + e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }

	    // Dynamic table name = year (e.g. [2026])
	    String tableName = "[" + year + "]";

	    // Fetch both InTime and Leave in a single query
	    String sql = "SELECT TOP 1 InTime, Leave FROM " + tableName
	               + " WHERE EmpId = ? AND CAST(Date AS DATE) = CAST(? AS DATE)";

	    try (
	        Connection con = webSmartDataSource.getConnection();
	        PreparedStatement pst = con.prepareStatement(sql)
	    ) {
	        pst.setString(1, empId);
	        pst.setString(2, fullDate);

	        ResultSet rs = pst.executeQuery();

	        if (rs.next()) {
	            String inTime   = rs.getString("InTime");
	            String leaveVal = rs.getString("Leave");

	            if (inTime != null && !inTime.trim().isEmpty()) {
	                // In-punch exists → allow entry
	                response.put("allowed", true);
	                response.put("message", "In-punch found. You may proceed.");

	            } else {
	                // No in-punch → check Leave type before sending message

	                if (leaveVal != null && leaveVal.trim().equalsIgnoreCase("WO")) {
	                    // Week Off
	                    response.put("allowed", false);
	                    response.put("message", "You cannot enter data for a Week Off (WO) day.");

	                } else {
	                    // No in-punch and not a WO (could be absent, HL, etc.)
	                    response.put("allowed", false);
	                    response.put("message", "You do not have an in-punch on the selected date. Drawing entry is not allowed.");
	                }
	            }

	        } else {
	            // No attendance record at all for this date
	            response.put("allowed", false);
	            response.put("message", "No attendance record found for the selected date. Drawing entry is not allowed.");
	        }

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("allowed", false);
	        response.put("message", "Database error: " + e.getMessage());
	        return ResponseEntity.internalServerError().body(response);
	    }
	}
	
	
	
	
}
