package com.JIMS.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@RequestMapping("/jssl")
public class verficationcontroller {

    @Autowired
    @Qualifier("jdmsDataSource")
    private DataSource jdmsDataSource; // JDMS DataSource

    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource; 
    @Autowired
    @Qualifier("gujaratDataSource")
    private DataSource gujaratDataSource; 
    
    // Get verification based on contract_id and load_id
    @GetMapping("/contracts/jdms/verification")
    public @ResponseBody Map<String, Object> getVerificationData(
            @RequestParam("contract_id") String contractId,
            @RequestParam("load_id") String loadId) {

        String sql = "SELECT verification FROM Invoice_No WHERE contract_id = ? AND load_id = ?";
        Map<String, Object> verificationMap = new HashMap<>();
        List<Map<String, String>> verificationList = new ArrayList<>();

        try (Connection connection = jdmsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters for SQL query
            preparedStatement.setString(1, contractId);
            preparedStatement.setString(2, loadId);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Ensure the resultSet has rows
            while (resultSet.next()) {
                String verification = resultSet.getString("verification");

                // Create a map to store the verification
                Map<String, String> verificationData = new HashMap<>();
                verificationData.put("verification", verification);

                // Add the map to the list
                verificationList.add(verificationData);
            }

            // Add the list of verification data to the map
            verificationMap.put("verification", verificationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the map with the list of verification data
        return verificationMap;
    }
    
    @GetMapping("/load/dlyload_id")
    public @ResponseBody Map<String, Object> getdlyload_id(
            @RequestParam("contract_id") String contractId,
            @RequestParam("factory") String factory) {

        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, String>> finalList = new ArrayList<>();

        List<String> jimsLotIds = new ArrayList<>();

        try {
            // ✅ Step 1: Get all lot_id from JIMS (DLY invoices) using misDataSource
            String jimsSql = "SELECT lot_id FROM JIMS.dbo.invoice_master WHERE invoice_type = 'DLY'";

            try (Connection con = jdmsDataSource.getConnection();
                 PreparedStatement ps = con.prepareStatement(jimsSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    jimsLotIds.add(rs.getString("lot_id"));
                }
            }

            // ✅ Step 2: Get loads from selected datasource
            DataSource dataSource = "1".equals(factory) ? misDataSource : gujaratDataSource;

            String loadSql = "SELECT loadno, tload_id FROM MIS.dbo.Tra_Loads WHERE contract_id = ?";

            try (Connection con = dataSource.getConnection();
                 PreparedStatement ps = con.prepareStatement(loadSql)) {

                ps.setString(1, contractId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String tloadId = rs.getString("tload_id");

                    // ✅ Step 3: Filter (NOT present in JIMS)
                    if (!jimsLotIds.contains(tloadId)) {
                        Map<String, String> data = new HashMap<>();
                        data.put("loadno", rs.getString("loadno"));
                        data.put("tload_id", tloadId);

                        finalList.add(data);
                    }
                }
            }

            responseMap.put("data", finalList);

        } catch (SQLException e) {
            e.printStackTrace();
            responseMap.put("error", "Database error occurred");
        }

        return responseMap;
    }
    
    
    @GetMapping("/load/dlyload_idUp")
    public @ResponseBody Map<String, Object> getdlyload_idUp(
            @RequestParam("contract_id") String contractId,
            @RequestParam("factory") String factory) {

        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, String>> finalList = new ArrayList<>();

        List<String> jimsLotIds = new ArrayList<>();

        try {
            // ✅ Step 1: Get all lot_id from JIMS (DLY invoices) using misDataSource
            String jimsSql = "SELECT lot_id FROM JIMS.dbo.invoice_master WHERE invoice_type = 'DLY'";

            try (Connection con = jdmsDataSource.getConnection();
                 PreparedStatement ps = con.prepareStatement(jimsSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    jimsLotIds.add(rs.getString("lot_id"));
                }
            }

            // ✅ Step 2: Get loads from selected datasource
            DataSource dataSource = "1".equals(factory) ? misDataSource : gujaratDataSource;

            String loadSql = "SELECT loadno, tload_id FROM MIS.dbo.Tra_Loads WHERE contract_id = ?";

            try (Connection con = dataSource.getConnection();
                 PreparedStatement ps = con.prepareStatement(loadSql)) {

                ps.setString(1, contractId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String tloadId = rs.getString("tload_id");

                    // ✅ Step 3: Filter (NOT present in JIMS)
                    Map<String, String> data = new HashMap<>();
                    data.put("loadno", rs.getString("loadno"));
                    data.put("tload_id", tloadId);

                    finalList.add(data);
                }
            }

            responseMap.put("data", finalList);

        } catch (SQLException e) {
            e.printStackTrace();
            responseMap.put("error", "Database error occurred");
        }

        return responseMap;
    }
}
