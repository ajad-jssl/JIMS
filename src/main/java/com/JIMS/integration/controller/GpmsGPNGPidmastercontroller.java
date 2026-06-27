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
public class GpmsGPNGPidmastercontroller {

    @Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;

    // Get GP number based on GP_type and order by GP_no DESC
    @GetMapping("/contracts/jims/gpNo")
    public @ResponseBody int getGpNoData(@RequestParam("gp_type") int gpType) {

        // SQL query to get the top GP_no based on GP_type
        String sql = "SELECT TOP 1 count(gp_no) as gp_no FROM [GPMS_tabRetGatePass] WHERE GP_type = ? ORDER BY gp_no DESC";
        int count = 0;

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the GP_type parameter for SQL query
            preparedStatement.setInt(1, gpType);

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

    // Get GP number based on GP_type and order by GP_no DESC, for records where security is null
    @GetMapping("/returnable/jims/gpno_nonsecurity")
    public @ResponseBody List<Map<String, Object>> getGpNosecurityNotcheck(
            @RequestParam("GP_type") int gpType, 
            @RequestParam("factory_id") int factoryId) {

        // SQL query to get the GP_no based on security being null and factory_id
        String sql = "SELECT Distinct gp_no, MIN(gpid) AS gpid FROM GPMS_tabRetGatePass WHERE security IS NULL " +
                     "AND GP_type=? AND factory_id=? " +
                     "AND (cancelstatus != 1 OR cancelstatus IS NULL) GROUP BY gp_no ORDER BY gp_no DESC";
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, gpType);
            preparedStatement.setInt(2, factoryId); // Set the factory_id parameter

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the results and add them to the resultList
            while (resultSet.next()) {
                Map<String, Object> resultMap = new HashMap<>();
                String gpNoString = resultSet.getString("gp_no");
                String gpidString = resultSet.getString("gpid");

                // Return gp_no as String and gpid as String
                resultMap.put("gp_no", gpNoString);
                resultMap.put("gpid", gpidString);

                resultList.add(resultMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the list of maps containing gp_no and gpid
        return resultList;
    }

    // Get item details for a given GP_no
    @GetMapping("/contracts/jims/gpnoitemdetails")
    public @ResponseBody List<Map<String, Object>> getGpItemDetails(@RequestParam("gp_no") String gpNo) {

        String sql = "SELECT DISTINCT d.gpdid, m.ItemNo, m.UM, m.itmDescription1, m.itmDescription2, "
                + "d.qty, d.itmPrice, d.itmRemarks, d.gp_no, d.item, "
                + "CASE WHEN SUM(s.Return_qty) IS NULL THEN d.qty ELSE d.qty - SUM(s.Return_qty) END AS remaining "
                + "FROM dbo.GPMS_tabRetGatePassdetail d "
                + "INNER JOIN dbo.GPMS_ITEMMASTER m ON CAST(m.ItemNo AS VARCHAR(50)) = CAST(d.item AS VARCHAR(50)) "
                + "LEFT OUTER JOIN dbo.GPMS_tabretgatepassshipdetail s "
                + "ON d.gp_no = s.gp_no AND CAST(d.item AS VARCHAR(50)) = CAST(s.itemno AS VARCHAR(50)) "
                + "WHERE d.gp_no = ? "
                + "AND LTRIM(RTRIM(CAST(d.item AS VARCHAR(50)))) <> '' "
                + "GROUP BY d.gpdid, m.ItemNo, m.UM, m.itmDescription1, m.itmDescription2, "
                + "d.qty, d.itmPrice, d.itmRemarks, d.gp_no, d.item";

        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, gpNo);  // only ONE ? in SQL

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("gpdid",           resultSet.getString("gpdid"));
                resultMap.put("ItemNo",          resultSet.getString("ItemNo"));
                resultMap.put("UM",              resultSet.getString("UM"));
                resultMap.put("itmDescription1", resultSet.getString("itmDescription1"));
                resultMap.put("itmDescription2", resultSet.getString("itmDescription2"));
                resultMap.put("gp_no",           resultSet.getString("gp_no"));
                resultMap.put("item",            resultSet.getString("item"));
                resultMap.put("itmRemarks",      resultSet.getString("itmRemarks"));
                resultMap.put("remaining",       resultSet.getString("remaining"));

                // Safe parsing — itmPrice and qty are nvarchar and may be empty
                String qtyStr   = resultSet.getString("qty");
                String priceStr = resultSet.getString("itmPrice");
                resultMap.put("qty",      (qtyStr   != null && !qtyStr.trim().isEmpty())   ? Double.parseDouble(qtyStr.trim())   : 0);
                resultMap.put("itmPrice", (priceStr != null && !priceStr.trim().isEmpty()) ? Double.parseDouble(priceStr.trim()) : 0);

                resultList.add(resultMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
    @GetMapping("/contracts/jims/gpitemrecieve")
    	public @ResponseBody int getitemrecieve(@RequestParam("gp_type") int gpType) {

        // SQL query to get item details for a given GP_no
        String sql = " select top 1 count(gps_entry) as gps_entry  from GPMS_tabretgatepassshipdetail where gp_type = ? order by gps_entry desc";

        int count = 0;

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the GP_type parameter for SQL query
            preparedStatement.setInt(1, gpType);

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
    
    @GetMapping("/contracts/jims/gpstroereceived")
    public @ResponseBody List<Map<String, Object>> getgpstroereceived(@RequestParam("gp_type") int gpType, @RequestParam("security_received") int security,@RequestParam("Return_complete") int rtncomplete) {

        // SQL query to get item details for a given GP_no
    	String sql =
    	        "SELECT \r\n"
    	      + "    s.gp_no, \r\n"
    	      + "    s.gps_entry, \r\n"
    	      + "    s.itemno AS itemId, \r\n"
    	      + "    s.Return_qty, \r\n"
    	      + "    s.Dc_ref, \r\n"
    	      + "    s.Created_date, \r\n"
    	      + "    s.security_remarks, \r\n"
    	      + "    s.itemno AS itemno, \r\n"
    	      + "    d.actualqty, \r\n"
    	      + "    s.gpsdid \r\n"
    	      + "FROM dbo.GPMS_Tabretgatepassshipdetail s \r\n"
    	      + "LEFT JOIN ( \r\n"
    	      + "    SELECT \r\n"
    	      + "        gp_no, \r\n"
    	      + "        item, \r\n"
    	      + "        SUM(qty) AS actualqty \r\n"
    	      + "    FROM dbo.GPMS_tabRetGatePassdetail \r\n"
    	      + "    GROUP BY gp_no, item \r\n"
    	      + ") d \r\n"
    	      + "    ON s.gp_no = d.gp_no \r\n"
    	      + "   AND s.itemno = d.item \r\n"
    	      + "LEFT JOIN dbo.GPMS_ITEMMASTER im \r\n"
    	      + "    ON d.item = im.itemid \r\n"
    	      + "WHERE s.security_received = ? \r\n"
    	      + "  AND s.gp_type = ? \r\n"
    	      + "  AND s.Return_complete = ? ";
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the gp_no parameter for SQL query
        	preparedStatement.setInt(1, security);
        	preparedStatement.setInt(2, gpType);
        	preparedStatement.setInt(3, rtncomplete);
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the results and map them to resultList
            while (resultSet.next()) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("gp_no", resultSet.getString("gp_no"));
                resultMap.put("gps_entry", resultSet.getString("gps_entry"));
                resultMap.put("itemId", resultSet.getInt("itemId"));
                resultMap.put("Dc_ref", resultSet.getString("Dc_ref"));
                resultMap.put("Created_date", resultSet.getString("Created_date"));
                resultMap.put("security_remarks", resultSet.getString("security_remarks"));
                resultMap.put("itemno", resultSet.getString("itemno"));
                resultMap.put("actualqty", resultSet.getInt("actualqty"));
                resultMap.put("Return_qty", resultSet.getInt("Return_qty"));
                resultMap.put("gpsdid", resultSet.getInt("gpsdid"));
                resultList.add(resultMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the result list containing item details
        return resultList;
    }
    
 
    @GetMapping("/contracts/jims/gptype_updatetables")
    public @ResponseBody List<Map<String, Object>> getGpTypeUpdateTables(@RequestParam("gp_type") int gpType, @RequestParam("factory_id") int factoryId) {
        // SQL query to get item details for a given GP_type
        String sql = "SELECT * ,  CAST(returnDate AS DATE) AS return_date FROM GPMS_tabRetGatePass WHERE GP_type = ? AND Factory_id = ?";
        
        // List to store result rows as maps (for converting to JSON)
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the gp_type parameter for the SQL query
            preparedStatement.setInt(1, gpType);
            preparedStatement.setInt(2, factoryId);
            
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Process the result set
            while (resultSet.next()) {
                // For each row, create a map to represent the column name-value pairs
                Map<String, Object> row = new HashMap<>();
                
                // Get all columns dynamically and add them to the map
                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                
                // Add the row map to the result list
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return the list of rows as the response, will be automatically converted to JSON
        return resultList;
    }
    
    @GetMapping("/contracts/jims/getgpno")
    public @ResponseBody List<String> getAllGpNos(@RequestParam("gp_type") int gpType) {
        String sql = "SELECT Distinct gp_no FROM GPMS_tabRetGatePass WHERE GP_type = ? ORDER BY gp_no DESC";
        List<String> gpNos = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, gpType);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gpNos.add(resultSet.getString("gp_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gpNos; 
    }
    
    @GetMapping("/contracts/jims/getngpno")
    public @ResponseBody List<String> getAllnGpNos(@RequestParam("gp_type") int gpType) {
        String sql = "SELECT Distinct gp_no FROM GPMS_tabRetGatePass WHERE GP_type = ? ORDER BY gp_no DESC";
        List<String> gpNos = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, gpType);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gpNos.add(resultSet.getString("gp_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gpNos; 
    }
    
    @GetMapping("/contracts/jims/getGatePassDetails")
    public @ResponseBody List<Map<String, Object>> getGatePassDetails(@RequestParam("gp_no") String gpno) {

    	String sql = "SELECT DISTINCT d.gpdid, i.ItemNo, i.UM AS UM, i.itmDescription1, i.itmDescription2, "
    	           + "ISNULL(TRY_CAST(d.qty AS INT), 0) AS qty, "
    	           + "ISNULL(TRY_CAST(d.itmPrice AS DECIMAL(18,2)), 0.0) AS itmPrice, "
    	           + "d.itmRemarks, d.gp_no, d.item, "
    	           + "CASE WHEN SUM(s.Return_qty) IS NULL THEN TRY_CAST(d.qty AS INT) "
    	           + "     ELSE TRY_CAST(d.qty AS INT) - SUM(s.Return_qty) END AS remaining, "
    	           + "g.sentThrough, CONVERT(VARCHAR(10), g.returnDate, 105) AS returnDate, g.note, "
    	           + "g.encl, c.JobCode AS project, g.remarks, g.dept, "
    	           + "(a.name_of_add + '-' + a.add1 + '-' + a.add2) AS address, "
    	           + "g.requestedby, CONVERT(VARCHAR(10), g.createddate, 105) AS date, fa.address AS factoryaddress "
    	           + "FROM dbo.GPMS_tabRetGatePass g "
    	           + "LEFT JOIN dbo.GPMS_tabRetGatePassdetail d       ON g.gp_no = d.gp_no "
    	           + "LEFT JOIN dbo.GPMS_ITEMMASTER i                 ON d.item = i.ItemNo "
    	           + "LEFT JOIN dbo.GPMS_tabretgatepassshipdetail s   ON d.gp_no = s.gp_no AND d.item = s.itemno "
    	           + "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER a    ON a.id = g.sentToClient "
    	           + "LEFT JOIN USERS_MASTER usr                      ON usr.id = g.requestedby "
    	           + "LEFT JOIN MIS.dbo.Contracts c                   ON CAST(c.contract_id AS VARCHAR(50)) = g.project "
    	           + "LEFT JOIN FACTORY_MASTER fa                     ON fa.id = g.factory_id "
    	           + "GROUP BY d.gpdid, i.ItemNo, i.UM, i.itmDescription1, i.itmDescription2, "
    	           + "a.name_of_add, a.add1, a.add2, usr.username, g.sentThrough, g.returnDate, "
    	           + "g.note, g.encl, g.project, g.remarks, g.dept, c.JobCode, g.createddate, "
    	           + "d.qty, d.itmPrice, d.itmRemarks,g.requestedby, d.gp_no, d.item, fa.address "
    	           + "HAVING d.gp_no = ?";

        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, gpno);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("ItemNo",          resultSet.getString("ItemNo"));
                    resultMap.put("UM",              resultSet.getString("UM"));
                    resultMap.put("itmDescription1", resultSet.getString("itmDescription1"));
                    resultMap.put("itmDescription2", resultSet.getString("itmDescription2"));
                    resultMap.put("itmRemarks",      resultSet.getString("itmRemarks"));
                    resultMap.put("gp_no",           resultSet.getString("gp_no"));
                    resultMap.put("item",            resultSet.getString("item"));
                    resultMap.put("remaining",       resultSet.getString("remaining"));
                    resultMap.put("sentThrough",     resultSet.getString("sentThrough"));
                    resultMap.put("returnDate",      resultSet.getString("returnDate"));
                    resultMap.put("note",            resultSet.getString("note"));
                    resultMap.put("encl",            resultSet.getString("encl"));
                    resultMap.put("project",         resultSet.getString("project"));
                    resultMap.put("remarks",         resultSet.getString("remarks"));
                    resultMap.put("dept",            resultSet.getString("dept"));
                    resultMap.put("address",         resultSet.getString("address"));
                    resultMap.put("username",        resultSet.getString("requestedby"));
                    resultMap.put("date",            resultSet.getString("date"));
                    resultMap.put("factoryaddress",  resultSet.getString("factoryaddress"));
                    resultMap.put("qty",      resultSet.getInt("qty"));        // safe now — SQL returns 0 if null
                    resultMap.put("itmPrice", resultSet.getDouble("itmPrice")); // safe now — SQL returns 0.0 if null
                    resultMap.put("gpdid",    safeGetInt(resultSet, "gpdid"));
                    resultList.add(resultMap);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
    
    private Integer safeGetInt(ResultSet rs, String column) throws SQLException {
        String value = rs.getString(column);
        if (value == null || value.trim().isEmpty()) return null;
        try { return Integer.parseInt(value.trim()); } 
        catch (NumberFormatException e) { return null; }
    }
    
    @GetMapping("/contracts/jims/getGatePassByDate")
    public @ResponseBody List<Map<String, Object>> getGatePassByDateRange(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate) {

        // SQL query with date range filter (only compares date part)
        String sql = 
            "SELECT DISTINCT d.gpdid, i.ItemNo, u.unit AS UM, i.itmDescription1, i.itmDescription2, " +
            "d.qty, d.itmPrice, d.itmRemarks, d.gp_no, s.gps_entry, s.Dc_ref,s.Return_qty as security_received_qty,g.securityoutdt, " +
            "g.createddate AS transcdate, s.receive_qty, d.item, " +
            "CASE WHEN SUM(s.Return_qty) IS NULL THEN d.qty ELSE d.qty - SUM(s.Return_qty) END AS remaining, " +
            "g.sentThrough, g.returnDate, g.note, g.encl, c.JobCode AS project, " +
            "g.remarks, g.dept, (a.name_of_add + '-' + a.add1 + '-' + a.add2) AS address, usr.username, g.UpdatedDate, " +
            "g.UpdateReason, g.cancelreason, fm.factory_name " +
            "FROM dbo.GPMS_tabRetGatePass g " +
            "LEFT JOIN dbo.GPMS_tabRetGatePassdetail d ON g.gp_no = d.gp_no " +
            "LEFT JOIN dbo.GPMS_ITEMMASTER i ON d.item = i.itemid " +
            "LEFT JOIN dbo.GPMS_tabretgatepassshipdetail s ON d.gp_no = s.gp_no AND d.item = s.itemno " +
            "LEFT JOIN GPMS_UNITMASTER u ON u.uid = i.UM " +
            "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER a ON a.id = g.sentToClient " +
            "LEFT JOIN USERS_MASTER usr ON usr.id = g.requestedby " +
            "LEFT JOIN MIS.dbo.Contracts c ON CAST(c.contract_id AS VARCHAR(50)) = g.project" +
            "LEFT JOIN FACTORY_MASTER fm on fm.id=g.factory_id " +
            "WHERE CAST(g.createddate AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) " +
            "GROUP BY d.gpdid, i.ItemNo, i.UM, i.itmDescription1, i.itmDescription2, " +
            "u.unit, a.name_of_add, a.add1, a.add2, usr.username, g.sentThrough, g.returnDate, " +
            "g.note, g.encl, g.project, g.remarks, g.dept, c.JobCode, " +
            "g.UpdatedDate, g.UpdateReason, g.cancelreason, d.qty, d.itmPrice, s.Return_qty, g.securityoutdt, " +
            "d.itmRemarks, d.gp_no, d.item, s.gps_entry, s.Dc_ref, g.createddate, s.receive_qty, fm.factory_name " +
            "ORDER BY g.createddate DESC";

        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, fromDate);
            preparedStatement.setString(2, toDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("gpdid", resultSet.getInt("gpdid"));
                map.put("ItemNo", resultSet.getString("ItemNo"));
                map.put("UM", resultSet.getString("UM"));
                map.put("itmDescription1", resultSet.getString("itmDescription1"));
                map.put("itmDescription2", resultSet.getString("itmDescription2"));
                map.put("qty", resultSet.getInt("qty"));
                map.put("itmPrice", resultSet.getBigDecimal("itmPrice"));
                map.put("itmRemarks", resultSet.getString("itmRemarks"));
                map.put("gp_no", resultSet.getString("gp_no"));
                map.put("item", resultSet.getString("item"));
                map.put("remaining", resultSet.getInt("remaining"));
                map.put("sentThrough", resultSet.getString("sentThrough"));
                map.put("returnDate", resultSet.getString("returnDate"));
                map.put("note", resultSet.getString("note"));
                map.put("encl", resultSet.getString("encl"));
                map.put("project", resultSet.getString("project"));
                map.put("remarks", resultSet.getString("remarks"));
                map.put("dept", resultSet.getString("dept"));
                map.put("address", resultSet.getString("address"));
                map.put("username", resultSet.getString("username"));
                map.put("transcdate", resultSet.getString("transcdate"));
                map.put("gps_entry", resultSet.getString("gps_entry"));
                map.put("Dc_ref", resultSet.getString("Dc_ref"));
                map.put("receive_qty", resultSet.getString("receive_qty"));
                map.put("UpdatedDate", resultSet.getString("UpdatedDate"));
                map.put("UpdateReason", resultSet.getString("UpdateReason"));
                map.put("cancelreason", resultSet.getString("cancelreason"));
                map.put("security_received_qty", resultSet.getString("security_received_qty"));
                map.put("securityoutdt", resultSet.getString("securityoutdt"));
                map.put("factory_name", resultSet.getString("factory_name"));
                resultList.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
    
    @GetMapping("/contracts/jims/getitemsdue")
    public @ResponseBody List<Map<String, Object>> getitemsdue() {

        String sql =
            "SELECT d.gpdid, " +
            "ISNULL(i.ItemNo, d.item) AS ItemNo, " +
            "ISNULL(i.UM, '') AS UM, " +
            "ISNULL(i.itmDescription1, '') AS itmDescription1, " +
            "ISNULL(i.itmDescription2, '') AS itmDescription2, " +
            "d.qty, d.itmPrice, d.itmRemarks, d.gp_no, s.gps_entry, s.Dc_ref, " +
            "g.createddate AS transcdate, s.receive_qty, d.item, " +
            "CASE WHEN SUM(s.Return_qty) IS NULL THEN d.qty ELSE d.qty - SUM(s.Return_qty) END AS remaining, " +
            "g.sentThrough, g.returnDate, g.note, g.encl, " +
            "c.JobCode AS project, g.remarks, g.dept, " +
            "(ISNULL(a.name_of_add,'') + '-' + ISNULL(a.add1,'') + '-' + ISNULL(a.add2,'')) AS address, " +
            "g.requestedby, g.UpdatedDate, g.UpdateReason, g.cancelreason, f.factory_name " +
            "FROM dbo.GPMS_tabRetGatePass g " +
            "LEFT JOIN dbo.GPMS_tabRetGatePassdetail d ON g.gp_no = d.gp_no " +
            "LEFT JOIN dbo.GPMS_ITEMMASTER i ON d.item = i.ItemNo " +
            "LEFT JOIN dbo.GPMS_tabretgatepassshipdetail s ON d.gp_no = s.gp_no " +
            "AND d.item = CAST(s.itemno AS NVARCHAR(200)) " +
            "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER a ON a.id = g.sentToClient " +
            "LEFT JOIN USERS_MASTER usr ON usr.id = g.requestedby " +
            "LEFT JOIN MIS.dbo.Contracts c ON CAST(c.contract_id AS NVARCHAR(50)) = CAST(g.project AS NVARCHAR(50)) " +
            "LEFT JOIN FACTORY_MASTER f ON f.id = g.factory_id " +
            "WHERE (s.receive_qty IS NULL OR TRY_CAST(d.qty AS DECIMAL(18,2)) > TRY_CAST(s.receive_qty AS DECIMAL(18,2))) " +
            "GROUP BY d.gpdid, i.ItemNo, i.UM, i.itmDescription1, i.itmDescription2, " +
            "a.name_of_add, a.add1, a.add2, usr.username, g.sentThrough, g.returnDate, " +
            "g.note, g.encl, g.project, g.remarks, g.dept, c.JobCode, g.UpdatedDate, " +
            "g.UpdateReason, g.cancelreason, d.qty, d.itmPrice, d.itmRemarks, " +
            "d.gp_no, d.item, s.gps_entry, s.Dc_ref,g.requestedby, g.createddate, s.receive_qty, f.factory_name " +
            "ORDER BY g.createddate DESC";

        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("gpdid", rs.getString("gpdid"));
                map.put("ItemNo", rs.getString("ItemNo"));
                map.put("UM", rs.getString("UM"));
                map.put("itmDescription1", rs.getString("itmDescription1"));
                map.put("itmDescription2", rs.getString("itmDescription2"));
                map.put("qty", rs.getString("qty"));
                map.put("itmPrice", rs.getString("itmPrice"));
                map.put("itmRemarks", rs.getString("itmRemarks"));
                map.put("gp_no", rs.getString("gp_no"));
                map.put("item", rs.getString("item"));
                map.put("remaining", rs.getString("remaining"));
                map.put("sentThrough", rs.getString("sentThrough"));
                map.put("returnDate", rs.getString("returnDate"));
                map.put("note", rs.getString("note"));
                map.put("encl", rs.getString("encl"));
                map.put("project", rs.getString("project"));
                map.put("remarks", rs.getString("remarks"));
                map.put("dept", rs.getString("dept"));
                map.put("address", rs.getString("address"));
                map.put("username", rs.getString("requestedby"));
                map.put("transcdate", rs.getString("transcdate"));
                map.put("gps_entry", rs.getString("gps_entry"));
                map.put("Dc_ref", rs.getString("Dc_ref"));
                map.put("receive_qty", rs.getString("receive_qty"));
                map.put("UpdatedDate", rs.getString("UpdatedDate"));
                map.put("UpdateReason", rs.getString("UpdateReason"));
                map.put("cancelreason", rs.getString("cancelreason"));
                map.put("factory_name", rs.getString("factory_name"));

                resultList.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in getitemsdue(): " + e.getMessage());
        }

        return resultList;
    }
}

