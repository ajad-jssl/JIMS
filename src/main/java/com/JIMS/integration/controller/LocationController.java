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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/location")
public class LocationController {
	
	
	@Autowired
	@Qualifier("webDataSource")
	private DataSource webSmartDataSource;
	
	
	
	
	@GetMapping("getAll")
	public @ResponseBody Map<String,Object> fetchLocationWebSmart(){
		Map<String,Object> response = new HashMap<>();
		List<Map<String,String>> location = new ArrayList<>();
		
		String sql =
			    "SELECT LocId, Loccode, Location " +
			    "FROM WebSmart.dbo.Location";
		try(Connection con = webSmartDataSource.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery())
		
		
		{
				while(rs.next()) {
					Map<String,String> row = new HashMap<>();
					row.put("Loc_id", rs.getString("LocId"));
					row.put("Loccode",rs.getString("Loccode"));
					row.put("Location", rs.getString("Location"));
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
	

}
