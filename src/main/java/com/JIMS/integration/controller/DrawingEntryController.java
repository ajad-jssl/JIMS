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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/drawingentry")
public class DrawingEntryController {
	
	
	 @Autowired
	    @Qualifier("misDataSource")   
	    private DataSource misDataSource2;

	 
	 
	  @GetMapping("/contractlist")
	    public @ResponseBody Map<String, Object> fetchContractsFromMIS() {

	        Map<String, Object> response = new HashMap<>();
	        List<Map<String, String>> contracts = new ArrayList<>();

	        String sql =
	                "SELECT contract_id, jobCode " +
	                "FROM contracts " +
	                "WHERE jobCode IS NOT NULL " 
	                ;

	        try (Connection con = misDataSource2.getConnection();
	             PreparedStatement pst = con.prepareStatement(sql);
	             ResultSet rs = pst.executeQuery()) {

	            while (rs.next()) {
	                Map<String, String> row = new HashMap<>();
	                row.put("contract_id", rs.getString("contract_id"));
	                row.put("jobCode", rs.getString("jobCode"));
	                contracts.add(row);
	            }

	            response.put("Data", contracts);
	            //response.put("count", contracts.size());

	        } catch (Exception e) {
	            //logger.error("Error fetching contracts from MIS", e);
	            response.put("error", "Database error");
	        }

	        return response;
	    }
	  
	  
	  
	  @GetMapping("/phases")
	    public @ResponseBody Map<String, Object> fetchPhasesByContract(
	            @RequestParam("contractId") String contractId) {

	        Map<String, Object> response = new HashMap<>();
	        List<String> phases = new ArrayList<>();

	        String sql =
	                "SELECT DISTINCT pzone " +
	                "FROM Loads " +
	                "WHERE contract_id = ? " +
	                "ORDER BY pzone ASC";

	        try (Connection con = misDataSource2.getConnection();
	             PreparedStatement pst = con.prepareStatement(sql)) {

	            pst.setString(1, contractId);

	            try (ResultSet rs = pst.executeQuery()) {
	                while (rs.next()) {
	                    phases.add(rs.getString("pzone"));
	                }
	            }

	            response.put("Data", phases);
	            //response.put("contractId", contractId);

	        } catch (Exception e) {
	           // logger.error("Error fetching phases from MIS", e);
	            response.put("error", "Database error");
	        }

	        return response;
	    }
	  
	  
	  
	  
	  @Autowired
	    @Qualifier("jimsDataSource")   // 🔥 Change if needed
	    private DataSource jimsDataSource;
	  
	  
	  
	  
	  @GetMapping("/by-user")
	    public @ResponseBody Map<String, Object> getActivitiesByUser(
	            @RequestParam("userId") Integer userId) {

	        Map<String, Object> response = new HashMap<>();
	        List<Map<String, Object>> activityList = new ArrayList<>();

	        String sql =
	                "SELECT ACM.ACTIVITY_NAME, ACM.ACTIVITY_ID, ACM.TYPE_ID " +
	                "FROM Employee_Type_Assign ETA " +
	                "INNER JOIN ACTIVITY_CATEGORY_MASTER ACM " +
	                "ON ETA.TYPE_ID = ACM.TYPE_ID " +
	                "WHERE ETA.USER_ID = ? and ETA.STATUS= 0";

	        try (Connection con = jimsDataSource.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, userId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Map<String, Object> row = new HashMap<>();
	                    row.put("activity_id", rs.getInt("ACTIVITY_ID"));
	                    row.put("activity_name", rs.getString("ACTIVITY_NAME"));
	                    row.put("type_id", rs.getInt("TYPE_ID"));
	                    activityList.add(row);
	                }
	            }

	            response.put("Data", activityList);
	            //response.put("count", activityList.size());

	        } catch (Exception e) {
	           // logger.error("Error fetching activity for userId=" + userId, e);
	            response.put("error", "Unable to fetch activity data");
	        }

	        return response;
	    }
	  
	  
	  
	  @GetMapping("/shift/by-location")
	  public @ResponseBody Map<String, Object> getShiftsByLocation(
	          @RequestParam("locationId") Integer locationId) {

	      Map<String, Object> response = new HashMap<>();
	      List<Map<String, Object>> shiftList = new ArrayList<>();

	      String sql =
	              "SELECT " +
	              "ID, CODE, SHIFT_HOURS " +
	              "FROM SHIFT_MASTER " +
	              "WHERE LOCATION_ID = ?";

	      try (Connection con = jimsDataSource.getConnection();
	           PreparedStatement ps = con.prepareStatement(sql)) {

	          ps.setInt(1, locationId);

	          try (ResultSet rs = ps.executeQuery()) {
	              while (rs.next()) {
	                  Map<String, Object> row = new HashMap<>();
	                  row.put("shift_id", rs.getInt("ID"));
	                  row.put("shift_code", rs.getString("CODE"));
	            
	                  row.put("shift_hours", rs.getBigDecimal("SHIFT_HOURS"));
	                  shiftList.add(row);
	              }
	          }

	          response.put("Data", shiftList);
	          

	      } catch (Exception e) {
	        
	          response.put("error", "Unable to fetch shift data");
	      }

	      return response;
	  }

	  
	  
}
