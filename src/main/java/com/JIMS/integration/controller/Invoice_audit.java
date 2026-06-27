package com.JIMS.integration.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
public class Invoice_audit {
	   @Autowired
	    @Qualifier("jimsDataSource")
	    private DataSource jimsDataSource;
	   
	   @GetMapping("/history/invoice")
	   public @ResponseBody List<Map<String, Object>> getInvoiceHistory() {

	       String sql = "SELECT         ICAH.id, (ICAH.name_of_add+''+ICAH.add1+''+ICAH.add2) as address, ICAH.city, ICAH.district, ICAH.state_id, ICAH.country_id, ICAH.created_by, ICAH.created_date, ICAH.modified_by, ICAH.modified_date, ICAH.is_delete, ICAH.is_invoice, \r\n"
	       		+ "                         ICAH.is_consignee, ICAH.pin_no, ICAH.gst_no, ICAH.pan_no, ICAH.code, ICAH.factory_id, SM.state_name, ICAH.deleted_by, ICAH.deleted_date, ICAH.transaction_date, ICAH.action, CM.country_name, \r\n"
	       		+ "                         USERS_MASTER_1.username as createdby, dbo.USERS_MASTER.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	       		+ "FROM            dbo.USERS_MASTER AS USERS_MASTER_1 RIGHT OUTER JOIN\r\n"
	       		+ "                         dbo.USERS_MASTER RIGHT OUTER JOIN\r\n"
	       		+ "                         dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER_HISTORY AS ICAH LEFT OUTER JOIN\r\n"
	       		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 ON ICAH.deleted_by = USERS_MASTER_2.id ON dbo.USERS_MASTER.id = ICAH.modified_by ON USERS_MASTER_1.id = ICAH.created_by LEFT OUTER JOIN\r\n"
	       		+ "                         dbo.COUNTRY_MASTER AS CM ON ICAH.country_id = CM.id LEFT OUTER JOIN\r\n"
	       		+ "                         dbo.STATE_MASTER AS SM ON SM.id = ICAH.state_id\r\n"
	       		+ "WHERE        (ICAH.is_invoice = '1')\r\n"
	       		+ "ORDER BY ICAH.transaction_date";

	       List<Map<String, Object>> list = new ArrayList<>();

	       try (Connection con = jimsDataSource.getConnection();
	            PreparedStatement ps = con.prepareStatement(sql);
	            ResultSet rs = ps.executeQuery()) {

	           while (rs.next()) {
	               Map<String, Object> map = new HashMap<>();
	               map.put("id", rs.getInt("id"));
	               map.put("address", rs.getString("address"));
	               map.put("city", rs.getString("city"));
	               map.put("district", rs.getString("district"));
	               map.put("state_id", rs.getInt("state_id"));
	               map.put("country_id", rs.getInt("country_id"));
	               map.put("createdby", rs.getString("createdby"));
	               map.put("created_date", rs.getString("created_date"));
	               map.put("modifiedby", rs.getString("modifiedby"));
	               map.put("modified_date", rs.getString("modified_date"));
	               map.put("is_delete", rs.getString("is_delete"));
	               map.put("is_invoice", rs.getString("is_invoice"));
	               map.put("is_consignee", rs.getString("is_consignee"));
	               map.put("pin_no", rs.getString("pin_no"));
	               map.put("gst_no", rs.getString("gst_no"));
	               map.put("pan_no", rs.getString("pan_no"));
	               map.put("code", rs.getString("code"));
	               map.put("factory_id", rs.getInt("factory_id"));
	               map.put("state_name", rs.getString("state_name"));
	               map.put("deletedby", rs.getString("deletedby"));
	               map.put("deleted_date", rs.getString("deleted_date"));
	               map.put("transaction_date", rs.getString("transaction_date"));
	               map.put("action", rs.getString("action"));
	               map.put("country_name", rs.getString("country_name"));
	               list.add(map);
	           }

	       } catch (SQLException e) { e.printStackTrace(); }

	       return list;
	   }

	   
	@GetMapping("/history/consignee")
	public @ResponseBody List<Map<String, Object>> getConsigneeHistory() {

	    String sql = "SELECT       ICAH.id, (ICAH.name_of_add+''+ICAH.add1+''+ICAH.add2) as address, ICAH.city, ICAH.district, ICAH.state_id, ICAH.country_id, ICAH.created_by, ICAH.created_date, ICAH.modified_by, ICAH.modified_date, ICAH.is_delete, ICAH.is_invoice, \r\n"
	    		+ "                         ICAH.is_consignee, ICAH.pin_no, ICAH.gst_no, ICAH.pan_no, ICAH.code, ICAH.factory_id, ICAH.deleted_by, ICAH.deleted_date, ICAH.transaction_date, ICAH.action, SM.state_name, CM.country_name, \r\n"
	    		+ "                         dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER_HISTORY AS ICAH LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 ON ICAH.deleted_by = USERS_MASTER_2.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_1 ON ICAH.modified_by = USERS_MASTER_1.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER ON ICAH.created_by = dbo.USERS_MASTER.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.COUNTRY_MASTER AS CM ON ICAH.country_id = CM.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.STATE_MASTER AS SM ON SM.id = ICAH.state_id\r\n"
	    		+ "WHERE        (ICAH.is_consignee = '1')\r\n"
	    		+ "ORDER BY ICAH.transaction_date";

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                map.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(map);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}
	
	@GetMapping("/history/bank")
	public @ResponseBody List<Map<String, Object>> getBankHistory() {

	    String sql = "SELECT        BMH.account_id, BMH.business_unit_id, BMH.bank_name, BMH.account_number, BMH.ifsc_code, BMH.branch_address, BMH.state_id, BMH.country_id, BMH.city, BMH.swift_code, BMH.branch_code, BMH.created_by, \r\n"
	    		+ "                         BMH.created_date, BMH.modified_by, BMH.modified_date, BMH.is_delete, BMH.factory_id, BMH.deleted_by, BMH.deleted_date, BMH.transaction_date, BMH.action, SM.state_name, CM.country_name, \r\n"
	    		+ "                         dbo.BUSINESS_UNITS.business_unit_name, dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.BANK_MASTER_HISTORY AS BMH LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_1 ON BMH.modified_by = USERS_MASTER_1.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 ON BMH.deleted_by = USERS_MASTER_2.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER ON BMH.created_by = dbo.USERS_MASTER.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.BUSINESS_UNITS ON BMH.business_unit_id = dbo.BUSINESS_UNITS.business_unit_id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.STATE_MASTER AS SM ON BMH.state_id = SM.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.COUNTRY_MASTER AS CM ON BMH.country_id = CM.id";

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                map.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(map);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}
	
	@GetMapping("/history/tax")
	public @ResponseBody List<Map<String, Object>> getTaxHistory() {

	    String sql = "SELECT    dbo.TAX_MASTER_HISTORY.tax_id, dbo.TAX_MASTER_HISTORY.tax_name, dbo.TAX_MASTER_HISTORY.tax_per, dbo.TAX_MASTER_HISTORY.startdate, dbo.TAX_MASTER_HISTORY.enddate, \r\n"
	    		+ "                         dbo.TAX_MASTER_HISTORY.created_by, dbo.TAX_MASTER_HISTORY.modified_by, dbo.TAX_MASTER_HISTORY.created_date, dbo.TAX_MASTER_HISTORY.modified_date, dbo.TAX_MASTER_HISTORY.is_delete, \r\n"
	    		+ "                         dbo.TAX_MASTER_HISTORY.tax_desc, dbo.TAX_MASTER_HISTORY.deleted_by, dbo.TAX_MASTER_HISTORY.deleted_date, dbo.TAX_MASTER_HISTORY.transaction_date, dbo.TAX_MASTER_HISTORY.action, \r\n"
	    		+ "                         dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.USERS_MASTER AS USERS_MASTER_1 RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.TAX_MASTER_HISTORY LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 ON dbo.TAX_MASTER_HISTORY.deleted_by = USERS_MASTER_2.id ON dbo.USERS_MASTER.id = dbo.TAX_MASTER_HISTORY.created_by ON \r\n"
	    		+ "                         USERS_MASTER_1.id = dbo.TAX_MASTER_HISTORY.modified_by\r\n"
	    		+ "ORDER BY dbo.TAX_MASTER_HISTORY.transaction_date";
	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                map.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(map);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}

	@GetMapping("/history/shipment-delivery")
	public @ResponseBody List<Map<String, Object>> getShipmentDeliveryHistory() {

	    String sql = "SELECT   dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.si_id, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.shipment_mode, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.delivery_condition, \r\n"
	    		+ "                         dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.created_by, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.created_date, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.modified_by, \r\n"
	    		+ "                         dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.modified_date, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.is_delete, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.factory_id, \r\n"
	    		+ "                         dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.deleted_by, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.deleted_date, dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.transaction_date, \r\n"
	    		+ "                         dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.action, dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.USERS_MASTER AS USERS_MASTER_1 RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY ON USERS_MASTER_1.id = dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.modified_by LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 ON dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.deleted_by = USERS_MASTER_2.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER ON dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.created_by = dbo.USERS_MASTER.id\r\n"
	    		+ "ORDER BY dbo.SHIPMENT_DELIVERY_CONDITION_HISTORY.transaction_date";
	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}

	@GetMapping("/history/workorder")
	public @ResponseBody List<Map<String, Object>> getWorkorderHistory() {

	    String sql = "SELECT      dbo.WORKORDER_MASTER_HISTORY.work_id, dbo.WORKORDER_MASTER_HISTORY.workorder_no, dbo.WORKORDER_MASTER_HISTORY.work_date, dbo.WORKORDER_MASTER_HISTORY.created_by, \r\n"
	    		+ "                         dbo.WORKORDER_MASTER_HISTORY.created_date, dbo.WORKORDER_MASTER_HISTORY.modified_by, dbo.WORKORDER_MASTER_HISTORY.modified_date, dbo.WORKORDER_MASTER_HISTORY.is_delete, \r\n"
	    		+ "                         dbo.WORKORDER_MASTER_HISTORY.factory_id, dbo.WORKORDER_MASTER_HISTORY.deleted_by, dbo.WORKORDER_MASTER_HISTORY.deleted_date, dbo.WORKORDER_MASTER_HISTORY.transaction_date, \r\n"
	    		+ "                         dbo.WORKORDER_MASTER_HISTORY.action, dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.USERS_MASTER RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.WORKORDER_MASTER_HISTORY LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_1 ON dbo.WORKORDER_MASTER_HISTORY.modified_by = USERS_MASTER_1.id ON USERS_MASTER_2.id = dbo.WORKORDER_MASTER_HISTORY.deleted_by ON \r\n"
	    		+ "                         dbo.USERS_MASTER.id = dbo.WORKORDER_MASTER_HISTORY.created_by\r\n"
	    		+ "ORDER BY dbo.WORKORDER_MASTER_HISTORY.transaction_date";
	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}

	@GetMapping("/history/gst")
	public @ResponseBody List<Map<String, Object>> getGstHistory() {

	    String sql = "SELECT     dbo.GST_MASTER_HISTORY.bu_id, dbo.GST_MASTER_HISTORY.sap_bucode, dbo.GST_MASTER_HISTORY.state_code, dbo.GST_MASTER_HISTORY.state_gstno, dbo.GST_MASTER_HISTORY.created_by, \r\n"
	    		+ "                         dbo.GST_MASTER_HISTORY.created_date, dbo.GST_MASTER_HISTORY.modified_by, dbo.GST_MASTER_HISTORY.modified_date, dbo.GST_MASTER_HISTORY.action, dbo.GST_MASTER_HISTORY.transaction_date, \r\n"
	    		+ "                         dbo.GST_MASTER_HISTORY.deleted_date, dbo.GST_MASTER_HISTORY.deleted_by, dbo.GST_MASTER_HISTORY.factory_id, dbo.GST_MASTER_HISTORY.is_delete, dbo.USERS_MASTER.username AS createdby, \r\n"
	    		+ "                         USERS_MASTER_1.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.USERS_MASTER RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.GST_MASTER_HISTORY ON dbo.USERS_MASTER.id = dbo.GST_MASTER_HISTORY.created_by LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 ON dbo.GST_MASTER_HISTORY.deleted_by = USERS_MASTER_2.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_1 ON dbo.GST_MASTER_HISTORY.modified_by = USERS_MASTER_1.id\r\n"
	    		+ "ORDER BY dbo.GST_MASTER_HISTORY.transaction_date";
	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}

	@GetMapping("/history/invoice-type")
	public @ResponseBody List<Map<String, Object>> getInvoiceTypeHistory() {

	    String sql = "SELECT        dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.slno, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.remarks_type, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.created_by, \r\n"
	    		+ "                         dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.created_date, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.modified_by, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.modified_date, \r\n"
	    		+ "                         dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.is_delete, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.factory_id, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.transaction_date, \r\n"
	    		+ "                         dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.action, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.deleted_date, dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.deleted_by, \r\n"
	    		+ "                         dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.USERS_MASTER AS USERS_MASTER_2 RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY ON USERS_MASTER_2.id = dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.deleted_by LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_1 ON dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.modified_by = USERS_MASTER_1.id LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER ON dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.created_by = dbo.USERS_MASTER.id\r\n"
	    		+ "ORDER BY dbo.INVOICE_TYPE_REMARKS_MASTER_HISTORY.transaction_date";
	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}

	@GetMapping("/history/milestone-contract")
	public @ResponseBody List<Map<String, Object>> getMilestoneContractHistory() {

	    String sql = "SELECT         MACH.id, MACH.milestone_id, MACH.contract_id, MACH.created_by, MACH.created_date, MACH.factory_id, MACH.modified_by, MACH.modified_date, MACH.action, MACH.transaction_date, \r\n"
	    		+ "                         MACH.deleted_date, MACH.deleted_by, MM.milestone_name, CM.contract_name, MM.milestone_code, dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, \r\n"
	    		+ "                         USERS_MASTER_2.username AS deletedby\r\n"
	    		+ "FROM            dbo.MILESTONE_MASTER AS MM RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.CONTRACT_MASTER AS CM RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_2 RIGHT OUTER JOIN\r\n"
	    		+ "                         dbo.MILESTONE_ASSGIN_CONTRACT_MASTER_HISTORY AS MACH LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER AS USERS_MASTER_1 ON MACH.modified_by = USERS_MASTER_1.id ON USERS_MASTER_2.id = MACH.deleted_by LEFT OUTER JOIN\r\n"
	    		+ "                         dbo.USERS_MASTER ON MACH.created_by = dbo.USERS_MASTER.id ON CM.contract_id = MACH.contract_id ON MM.milestone_id = MACH.milestone_id\r\n"
	    		+ "ORDER BY MACH.transaction_date";

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Map<String, Object> map = new HashMap<>();
	            map.put("id", rs.getInt("id"));
	            map.put("milestone_id", rs.getInt("milestone_id"));
	            map.put("contract_id", rs.getInt("contract_id"));
	            map.put("created_by", rs.getString("created_by"));
	            map.put("created_date", rs.getString("created_date"));
	            map.put("factory_id", rs.getInt("factory_id"));
	            map.put("modified_by", rs.getString("modified_by"));
	            map.put("modified_date", rs.getString("modified_date"));
	            map.put("action", rs.getString("action"));
	            map.put("transaction_date", rs.getString("transaction_date"));
	            map.put("deleted_date", rs.getString("deleted_date"));
	            map.put("deleted_by", rs.getString("deleted_by"));
	            map.put("milestone_name", rs.getString("milestone_name"));
	            map.put("contract_name", rs.getString("contract_name"));
	            map.put("milestone_code", rs.getString("milestone_code"));
	            list.add(map);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}

	@GetMapping("/history/contract")
	public @ResponseBody List<Map<String, Object>> getContractHistory() {

		String sql = "SELECT CMH.con_slno, CMH.bid, CMH.contract_id, CMH.invoice_type_calculation, CMH.percentage_value, CMH.invoice_to_id, CMH.consignee_id, CMH.shipment_mode_id, CMH.delivery_condition_id, "
			    + "CMH.product_desc_id, CMH.bank_details_id, CMH.work_id, CMH.regd_office_id, CMH.s_code, CMH.h_code, CMH.export, CMH.tax_ex_inc, CMH.taxable, CMH.non_taxable, CMH.tax_payable, CMH.freight_advance_recovery, "
			    + "CMH.area_no, CMH.lot_no, CMH.containter_no, CMH.epcgno, CMH.export_title_text, CMH.created_by, CMH.created_date, CMH.modified_by, CMH.modified_date, CMH.is_delete, CMH.contract_name, CMH.is_locked, "
			    + "CMH.factory_id, STUFF((SELECT ', ' + CASE LTRIM(RTRIM(value)) WHEN '1' THEN 'Bellary' WHEN '2' THEN 'Gujarat' ELSE LTRIM(RTRIM(value)) END FROM STRING_SPLIT(CAST(CMH.factory_id AS NVARCHAR(100)), ',') FOR XML PATH('')), 1, 2, '') AS factory, "
			    + "BU.business_unit_name, (ICAM1.name_of_add+''+ICAM1.add1+''+ICAM1.add2) AS invoice_address, (ICAM2.name_of_add+''+ICAM2.add1+''+ICAM2.add2) AS consignee_address, SDC.shipment_mode, SDC1.delivery_condition, BM.bank_name, WM.workorder_no, "
			    + "OM.registered_address, SC1.service_code AS s_code_value, SC2.service_code AS hsn_code, dbo.USERS_MASTER.username AS createdby, USERS_MASTER_1.username AS modifiedby, "
			    + "USERS_MASTER_2.username AS deletedby, CMH.transaction_date, CMH.action "
			    + "FROM dbo.CONTRACT_MASTER_HISTORY AS CMH "
			    + "LEFT OUTER JOIN dbo.USERS_MASTER AS USERS_MASTER_2 ON CMH.deleted_by = USERS_MASTER_2.id "
			    + "LEFT OUTER JOIN dbo.USERS_MASTER AS USERS_MASTER_1 ON CMH.modified_by = USERS_MASTER_1.id "
			    + "LEFT OUTER JOIN dbo.USERS_MASTER ON CMH.created_by = dbo.USERS_MASTER.id "
			    + "LEFT OUTER JOIN dbo.BUSINESS_UNITS AS BU ON CMH.bid = BU.business_unit_id "
			    + "LEFT OUTER JOIN dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS ICAM1 ON CMH.invoice_to_id = ICAM1.id "
			    + "LEFT OUTER JOIN dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS ICAM2 ON CMH.consignee_id = ICAM2.id "
			    + "LEFT OUTER JOIN dbo.SHIPMENT_DELIVERY_CONDITION AS SDC ON CMH.shipment_mode_id = SDC.si_id "
			    + "LEFT OUTER JOIN dbo.SHIPMENT_DELIVERY_CONDITION AS SDC1 ON CMH.delivery_condition_id = SDC1.si_id "
			    + "LEFT OUTER JOIN dbo.BANK_MASTER AS BM ON CMH.bank_details_id = BM.account_id "
			    + "LEFT OUTER JOIN dbo.WORKORDER_MASTER AS WM ON CMH.work_id = WM.work_id "
			    + "LEFT OUTER JOIN dbo.ORGANIZATION_MASTER AS OM ON CMH.regd_office_id = OM.org_id "
			    + "LEFT OUTER JOIN dbo.SERVICECODE_MASTER AS SC1 ON CMH.s_code = SC1.servicecode_id "
			    + "LEFT OUTER JOIN dbo.SERVICECODE_MASTER AS SC2 ON CMH.h_code = SC2.servicecode_id "
			    + "ORDER BY CMH.contract_id";

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (SQLException e) { e.printStackTrace(); }

	    return list;
	}

	@GetMapping("/contracts/by-factory")   
	public @ResponseBody List<Map<String, Object>> getContractsByFactory(
	        @RequestParam(value = "factoryId") String factoryId) {

	    StringBuilder sqlBuilder = new StringBuilder(
	        "SELECT contract_id, contract_name FROM dbo.CONTRACT_MASTER WHERE is_delete = 0 "
	    );

	    boolean filterByFactory = factoryId != null 
	                           && !factoryId.trim().isEmpty() 
	                           && !factoryId.equalsIgnoreCase("ALL");

	    if (filterByFactory) {
	        sqlBuilder.append(
	            "AND EXISTS ( " +
	            "  SELECT 1 FROM STRING_SPLIT(CAST(factory_id AS NVARCHAR(100)), ',') " +
	            "  WHERE LTRIM(RTRIM(value)) = ? " +
	            ") "
	        );
	    }

	    sqlBuilder.append("ORDER BY contract_name");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sqlBuilder.toString())) {

	        if (filterByFactory) {
	            ps.setString(1, factoryId.trim());
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("contract_id", rs.getInt("contract_id"));
	                map.put("contract_name", rs.getString("contract_name"));
	                list.add(map);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/contracts/by-factory-packing")
	public @ResponseBody List<Map<String, Object>> getContractsByfactory(
	        @RequestParam(value = "factoryId") String factoryId) {

	    List<Map<String, Object>> list = new ArrayList<>();

	    StringBuilder sqlBuilder = new StringBuilder();
	    sqlBuilder.append(
	        "SELECT DISTINCT im.contract_id,im.contract_name " +
	        "FROM dbo.INVOICE_MASTER im  " +
	        "WHERE 1=1 "
	    );

	    // 🔍 Check if factory filter should be applied
	    boolean filterByFactory = factoryId != null
	            && !factoryId.trim().isEmpty()
	            && !factoryId.equalsIgnoreCase("ALL");

	    if (filterByFactory) {
	        sqlBuilder.append("AND im.factory_id = ? ");
	    }

	    // ✅ Better ordering for UI
	    sqlBuilder.append("ORDER BY im.contract_name");

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sqlBuilder.toString())) {

	        // ✅ Set parameter only if needed
	        if (filterByFactory) {
	            ps.setString(1, factoryId.trim());
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();

	                // ✅ Correct datatype handling
	                map.put("contract_id", rs.getInt("contract_id"));
	                map.put("contract_name", rs.getString("contract_name"));

	                list.add(map);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/lots/by-contract")
	public @ResponseBody List<Map<String, Object>> getLotsByContract(
	        @RequestParam("contractId") int contractId) {

	    String sql = "SELECT DISTINCT load_id " +
	                 "FROM dbo.INVOICE_MASTER " +
	                 "WHERE contract_id = ? AND CAST(load_id AS VARCHAR(50)) LIKE 'ADV%' " +
	                 "ORDER BY load_id";

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, contractId);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("load_id", rs.getString("load_id"));
	                list.add(map);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/lots/by-factory")
	public @ResponseBody List<Map<String, Object>> getLotsByFactory(@RequestParam String factoryId) {
	    StringBuilder sql = new StringBuilder(
	        "SELECT DISTINCT load_id FROM INVOICE_MASTER_HISTORY WHERE load_id LIKE '%ADV%'"
	    );

	    List<Object> params = new ArrayList<>();

	    if (!"all".equalsIgnoreCase(factoryId)) {
	        sql.append(" AND factory_id = ?");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append(" ORDER BY load_id");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            row.put("load_id", rs.getString("load_id"));
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/invoice/adv")
	public @ResponseBody List<Map<String, Object>> getInvoiceHistoryADV(
	        @RequestParam String factoryId,
	        @RequestParam String contractId,   // ← Changed from int to String
	        @RequestParam String loadId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT cmh.contract_name, imh.load_id, imh.invoice_no, imh.product_desc, " +
	        "(ICAM1.name_of_add+''+ICAM1.add1+''+ICAM1.add2) AS invoice_to, (ICAM2.name_of_add+''+ICAM2.add1+''+ICAM2.add2) AS consignee_add, " +
	        "SDC.shipment_mode, SDC1.delivery_condition, WM.workorder_no, BM.bank_name, " +
	        "SC1.service_code AS s_code_value, SC2.service_code AS hsn_code, " +
	        "imh.reference_no, cmh.export, " +
	        "U1.username AS createdby, U2.username AS modifiedby, " +
	        "imh.transaction_date, imh.action, " +

	        "STUFF((SELECT ', ' + CASE LTRIM(RTRIM(value)) " +
	        "WHEN '1' THEN 'Bellary' WHEN '2' THEN 'Gujarat' " +
	        "ELSE LTRIM(RTRIM(value)) END " +
	        "FROM STRING_SPLIT(CAST(imh.factory_id AS NVARCHAR(100)), ',') " +
	        "FOR XML PATH('')), 1, 2, '') AS factory " +

	        "FROM INVOICE_MASTER_HISTORY imh " +

	        "OUTER APPLY ( " +
	        "   SELECT TOP 1 * FROM CONTRACT_MASTER_HISTORY cmh " +
	        "   WHERE cmh.contract_id = imh.contract_id " +
	        "   ORDER BY cmh.transaction_date DESC " +
	        ") cmh " +

	        "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER ICAM1 ON cmh.invoice_to_id = ICAM1.id " +
	        "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER ICAM2 ON cmh.consignee_id = ICAM2.id " +
	        "LEFT JOIN SHIPMENT_DELIVERY_CONDITION SDC ON cmh.shipment_mode_id = SDC.si_id " +
	        "LEFT JOIN SHIPMENT_DELIVERY_CONDITION SDC1 ON cmh.delivery_condition_id = SDC1.si_id " +
	        "LEFT JOIN BANK_MASTER BM ON cmh.bank_details_id = BM.account_id " +
	        "LEFT JOIN WORKORDER_MASTER WM ON cmh.work_id = WM.work_id " +
	        "LEFT JOIN SERVICECODE_MASTER SC1 ON cmh.s_code = SC1.servicecode_id " +
	        "LEFT JOIN SERVICECODE_MASTER SC2 ON cmh.h_code = SC2.servicecode_id " +
	        "LEFT JOIN USERS_MASTER U1 ON imh.created_by = U1.id " +
	        "LEFT JOIN USERS_MASTER U2 ON imh.modified_by = U2.id " +

	        "WHERE imh.load_id LIKE '%ADV%' "
	    );

	    List<Object> params = new ArrayList<>();

	    // Factory filter — skip if "all"
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append(" AND imh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    // Contract filter — skip if "all", otherwise parse safely to int
	    if (contractId != null && !contractId.isEmpty() && !"all".equalsIgnoreCase(contractId)) {
	        sql.append(" AND imh.contract_id = ? ");
	        params.add(Integer.parseInt(contractId));
	    }

	    // Load filter — skip if "all"
	    if (loadId != null && !loadId.isEmpty() && !"all".equalsIgnoreCase(loadId)) {
	        sql.append(" AND imh.load_id = ? ");
	        params.add(loadId);
	    }

	    sql.append(" ORDER BY imh.transaction_date ");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/contracts/by-factory-qs")
	public @ResponseBody List<Map<String, Object>> getContractsByFactoryQS(
	        @RequestParam(value = "factoryId") String factoryId) {

	    List<Map<String, Object>> list = new ArrayList<>();

	    StringBuilder sqlBuilder = new StringBuilder();
	    sqlBuilder.append(
	        "SELECT DISTINCT qm.con_id, cm.contract_name " +
	        "FROM dbo.QSPACKING_MASTER qm " +          // ← alias qm added here
	        "LEFT OUTER JOIN dbo.CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +
	        "WHERE 1=1 "
	    );

	    boolean filterByFactory = factoryId != null
	            && !factoryId.trim().isEmpty()
	            && !factoryId.equalsIgnoreCase("ALL");

	    if (filterByFactory) {
	        sqlBuilder.append("AND qm.factory_id = ? ");
	    }

	    sqlBuilder.append("ORDER BY cm.contract_name");

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sqlBuilder.toString())) {

	        if (filterByFactory) {
	            ps.setString(1, factoryId.trim());
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {                         // ← fixed rs.next()
	                Map<String, Object> map = new HashMap<>();
	                map.put("con_id", rs.getInt("con_id"));
	                map.put("contract_name", rs.getString("contract_name"));
	                list.add(map);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}

	@GetMapping("/lots/by-factory-qs")
	public @ResponseBody List<Map<String, Object>> getLotsByFactoryQS(
	        @RequestParam String factoryId,
	        @RequestParam String contractId) {

	    List<Map<String, Object>> list = new ArrayList<>();

	    StringBuilder sql = new StringBuilder(
	        "SELECT DISTINCT lot_no FROM dbo.QSPACKING_MASTER WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    boolean filterByFactory = factoryId != null
	            && !factoryId.trim().isEmpty()
	            && !"all".equalsIgnoreCase(factoryId);

	    boolean filterByContract = contractId != null
	            && !contractId.trim().isEmpty()
	            && !"all".equalsIgnoreCase(contractId);

	    if (filterByFactory) {
	        sql.append("AND factory_id = ? ");
	        params.add(Integer.parseInt(factoryId.trim()));
	    }

	    if (filterByContract) {
	        sql.append("AND con_id = ? ");
	        params.add(Integer.parseInt(contractId.trim()));
	    }

	    sql.append("ORDER BY lot_no");

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("lot_no", rs.getString("lot_no"));
	                list.add(map);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/qs-packing")
	public @ResponseBody List<Map<String, Object>> getQSPackingHistory(
	        @RequestParam String factoryId,
	        @RequestParam String contractId,
	        @RequestParam String lotNo) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qmh.pn_id, qmh.con_id, qmh.filepath, qmh.load_id, qmh.lot_no, qmh.transport_name, qmh.vechile_no, " +
	        "    qmh.freight, qmh.grand_total, qmh.is_locked, qmh.is_delete, qmh.Cancel, " +
	        "    qmh.factory_id, qmh.created_date, qmh.modified_date, qmh.deleted_date, " +
	        "    qmh.transaction_date, qmh.action, " +
	        "    cm.contract_name, " +
	        "    mm.milestone_code+'-'+ mm.milestone_name as milestone_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qmh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qmh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.QSPACKING_MASTER_HISTORY qmh " +
	        "OUTER APPLY ( " +
	        "    SELECT TOP 1 contract_name FROM dbo.CONTRACT_MASTER_HISTORY cmh " +
	        "    WHERE cmh.contract_id = qmh.con_id " +
	        "    ORDER BY cmh.transaction_date DESC " +
	        ") cm " +
	        "LEFT JOIN dbo.MILESTONE_MASTER mm ON qmh.milestone_id = mm.milestone_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qmh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qmh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qmh.deleted_by = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qmh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    if (contractId != null && !contractId.isEmpty() && !"all".equalsIgnoreCase(contractId)) {
	        sql.append("AND qmh.con_id = ? ");
	        params.add(Integer.parseInt(contractId));
	    }

	    if (lotNo != null && !lotNo.isEmpty() && !"all".equalsIgnoreCase(lotNo)) {
	        sql.append("AND qmh.lot_no = ? ");
	        params.add(lotNo);
	    }

	    sql.append("ORDER BY qmh.transaction_date");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/qs-packing-items")
	public @ResponseBody List<Map<String, Object>> getQSPackingItemHistory(
	        @RequestParam String factoryId,
	        @RequestParam String contractId,
	        @RequestParam String lotNo) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qimh.slno, qimh.pn_id, qimh.qty, qimh.per_kgs, qimh.unit_price, qimh.total, " +
	        "    qimh.pices, qimh.inc_type, qimh.is_delete, qimh.is_locked, " +
	        "    qimh.factory_id, qimh.created_date, qimh.modified_date, qimh.deleted_date, " +
	        "    qimh.transaction_date, qimh.action, " +
	        "    uom.unit_name, " +
	        "    tp.scrap_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qimh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qimh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.QSPACKING_ITEM_MASTER_HISTORY qimh " +
	        "LEFT JOIN dbo.UOM_MASTER uom ON qimh.UOM_id = uom.unit_id " +
	        "LEFT JOIN dbo.SCRAPTYPE_MASTER tp ON qimh.type_id = tp.type_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qimh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qimh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qimh.deleted_by = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qimh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    // Filter by contractId and lotNo via pn_id subquery
	    if ((contractId != null && !contractId.isEmpty() && !"all".equalsIgnoreCase(contractId)) ||
	        (lotNo != null && !lotNo.isEmpty() && !"all".equalsIgnoreCase(lotNo))) {

	        sql.append(
	            "AND qimh.pn_id IN ( " +
	            "    SELECT pn_id FROM dbo.QSPACKING_MASTER_HISTORY qmh " +
	            "    WHERE 1=1 "
	        );

	        if (contractId != null && !contractId.isEmpty() && !"all".equalsIgnoreCase(contractId)) {
	            sql.append("    AND qmh.con_id = ? ");
	            params.add(Integer.parseInt(contractId));
	        }

	        if (lotNo != null && !lotNo.isEmpty() && !"all".equalsIgnoreCase(lotNo)) {
	            sql.append("    AND qmh.lot_no = ? ");
	            params.add(lotNo);
	        }

	        sql.append(") ");
	    }

	    sql.append("ORDER BY qimh.transaction_date");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/scrap-packing")
	public @ResponseBody List<Map<String, Object>> getScrapPackingHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qmh.scp_pn_id, sm.contract_id, qmh.scp_load, qmh.transportername, qmh.vehicleno, " +
	        "    qmh.weighbridge_no, qmh.buyer_ref_no, qmh.is_locked, qmh.is_delete, qmh.Cancel, " +
	        "    qmh.factory_id, qmh.created_date, qmh.modified_date, qmh.deleted_date, " +
	        "    qmh.transaction_date, qmh.action, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qmh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qmh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.SCRAP_PACKING_NOTE_HISTORY qmh " +
	        "LEFT JOIN dbo.SCRAP_INVOICE_MASTER sm ON sm.scp_load = qmh.scp_load " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qmh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qmh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qmh.deleted_by = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qmh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qmh.transaction_date");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/scrap-packing-items")
	public @ResponseBody List<Map<String, Object>> getScrapPackingItemHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT DISTINCT" +
	        "    qimh.scp_pn_id, qimh.quantity, qimh.kg, qimh.unit_price, qimh.total, " +
	        "    qimh.is_delete, qimh.is_locked, " +
	        "    qimh.factory_id, qimh.created_date, qimh.modified_date, qimh.deleted_date, " +
	        "    qimh.transaction_date, qimh.action, " +
	        "    uom.unit_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE shm.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qimh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.SCRAP_PACKING_NOTE_ITEMS_HISTORY qimh " +
	        "LEFT JOIN dbo.SCRAP_PACKING_NOTE_HISTORY shm on shm.scp_load=qimh.scp_load " +
	        "LEFT JOIN dbo.UOM_MASTER uom ON qimh.UOM_id = uom.unit_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON shm.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qimh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qimh.deleted_by = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND shm.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qimh.transaction_date");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/other-packing")
	public @ResponseBody List<Map<String, Object>> getOtherPackingHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qmh.other_pn_id, qmh.con_id, qmh.other_Pn_no, qmh.Transporter_name, qmh.vechile_no, " +
	        "    qmh.freight, qmh.buyer_ref, qmh.is_locked, qmh.is_delete, qmh.is_cancel, " +
	        "    qmh.factory_id, qmh.created_date, qmh.modified_date, " +
	        "    qmh.action_date, qmh.action_type, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qmh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qmh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.Othertype_packingnote_history qmh " +
	        "LEFT JOIN dbo.SCRAP_INVOICE_MASTER sm ON sm.scp_load = qmh.other_Pn_no " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qmh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qmh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qmh.is_delete = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qmh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qmh.action_date");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/other-packing-items")
	public @ResponseBody List<Map<String, Object>> getOtherPackingItemHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qimh.other_Pn_no, qimh.quantities, qimh.kgs, qimh.unit_price, qimh.total, " +
	        "    qimh.is_delete, qimh.created_date, qimh.modified_date, qimh.deleted_date, " +
	        "    qimh.transaction_date, qimh.action, " +
	        "    uom.unit_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE oth.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(oth.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.other_packingnote_items_history qimh " +
	        "LEFT JOIN dbo.UOM_MASTER uom ON qimh.UOM_id = uom.unit_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qimh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qimh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qimh.deleted_by = U3.id " +
	        "LEFT JOIN dbo.Othertype_packingnote_history oth ON oth.other_pn_id = qimh.other_pn_id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER (comes from oth table)
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND oth.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qimh.transaction_date");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/qs-challan-packing")
	public @ResponseBody List<Map<String, Object>> getQSChallanPackingHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qmh.pn_id, qmh.contract_id, qmh.filepath, qmh.load_id, qmh.transport_name, qmh.vechile_no, " +
	        "    qmh.grand_total, qmh.is_locked, qmh.is_delete, qmh.Cancel, " +
	        "    qmh.factory_id, qmh.created_date, qmh.modified_date, " +
	        "    qmh.operation_timestamp, qmh.operation_type, " +
	        "    cm.contract_name, " +
	        "    mm.milestone_code+'-'+ mm.milestone_name as milestone_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qmh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qmh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.QSCHALLAN_PACKINGNOTE_MASTER_HISTORY qmh " +
	        "OUTER APPLY ( " +
	        "    SELECT TOP 1 contract_name FROM dbo.CONTRACT_MASTER_HISTORY cmh " +
	        "    WHERE cmh.contract_id = qmh.contract_id " +
	        "    ORDER BY cmh.transaction_date DESC " +
	        ") cm " +
	        "LEFT JOIN dbo.MILESTONE_MASTER mm ON qmh.milestone_id = mm.milestone_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qmh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qmh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qmh.is_delete = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qmh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qmh.operation_timestamp");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/qs-challan-packing-items")
	public @ResponseBody List<Map<String, Object>> getQSChallanPackingItemHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qimh.slno, qimh.pn_id, qimh.qty, qimh.per_kgs, qimh.unit_price, qimh.total, " +
	        "    qimh.pices, qimh.type_id, qimh.is_delete, qimh.is_locked, " +
	        "    qimh.factory_id, qimh.created_date, qimh.modified_date, " +
	        "    qimh.operation_timestamp, qimh.operation_type, " +
	        "    uom.unit_name, " +
	        "    tp.scrap_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qimh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qimh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.QSCHALLAN_PACKINGNOTEITEM_MASTER_HISTORY qimh " +
	        "LEFT JOIN dbo.UOM_MASTER uom ON qimh.UOM_id = uom.unit_id " +
	        "LEFT JOIN dbo.SCRAPTYPE_MASTER tp ON qimh.type_id = tp.type_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qimh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qimh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qimh.is_delete = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qimh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qimh.operation_timestamp");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/debit-credit-packing")
	public @ResponseBody List<Map<String, Object>> getDebitCreditPackingHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qmh.dc_pn_id, " +
	        "    qmh.con_id, " +
	        "    qmh.filepath, " +
	        "    qmh.dcPn_no, " +
	        "    qmh.is_locked, " +
	        "    qmh.is_delete, " +
	        "    qmh.Cancel, " +
	        "    qmh.factory_id, " +
	        "    qmh.created_date, " +
	        "    qmh.modified_date, " +
	        "    qmh.operation_timestamp, " +
	        "    qmh.operation_type, " +

	        // CONTRACT NAME (SAFE)
	        "    CASE " +
	        "        WHEN TRY_CAST(qmh.con_id AS INT) IS NOT NULL " +
	        "            THEN cm.contract_name " +
	        "        ELSE qmh.con_id " +
	        "    END AS contract_name, " +

	        // MILESTONE NAME (SAFE)
	        "    CASE " +
	        "        WHEN TRY_CAST(qmh.milestone_id AS INT) IS NOT NULL " +
	        "            THEN mm.milestone_code + '-' + mm.milestone_name " +
	        "        ELSE qmh.milestone_id " +
	        "    END AS milestone_name, " +

	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +

	        "    CASE qmh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qmh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +

	        "FROM dbo.DEBITCREDIT_PACKINGNOTE_HISTORY qmh " +

	        // SAFE CONTRACT JOIN
	        "OUTER APPLY ( " +
	        "    SELECT TOP 1 contract_name " +
	        "    FROM dbo.CONTRACT_MASTER_HISTORY cmh " +
	        "    WHERE cmh.contract_id = TRY_CAST(qmh.con_id AS INT) " +
	        "    ORDER BY cmh.transaction_date DESC " +
	        ") cm " +

	        // SAFE MILESTONE JOIN
	        "LEFT JOIN dbo.MILESTONE_MASTER mm " +
	        "    ON mm.milestone_id = TRY_CAST(qmh.milestone_id AS INT) " +

	        "LEFT JOIN dbo.USERS_MASTER U1 ON qmh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qmh.modified_by = U2.id " +

	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qmh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qmh.operation_timestamp");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/debit-credit-packing-items")
	public @ResponseBody List<Map<String, Object>> getDebitCreditPackingItemHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT DISTINCT " +
	        "    qimh.history_id, qimh.dc_pn_id, qimh.quantities, qimh.kgs, qimh.unit_price, qimh.total, " +
	        "    qimh.type_id, qimh.is_delete, qimh.is_locked, " +
	        "    qhp.factory_id, qimh.created_date, qimh.modified_date, " +
	        "    qimh.operation_timestamp, qimh.operation_type, " +
	        "    uom.unit_name, " +
	        "    tp.scrap_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qhp.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qhp.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.DEBITCREDIT_PACKINGNOTE_ITEMS_HISTORY qimh " +
	        "LEFT JOIN dbo.DEBITCREDIT_PACKINGNOTE_HISTORY qhp ON qhp.dc_pn_id = qimh.dc_pn_id " +
	        "LEFT JOIN dbo.UOM_MASTER uom ON qimh.UOM_id = uom.unit_id " +
	        "LEFT JOIN dbo.SCRAPTYPE_MASTER tp ON qimh.type_id = tp.type_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qimh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qimh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qimh.is_delete = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER (comes from header table)
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qhp.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qimh.operation_timestamp");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/qs-advance-packing")
	public @ResponseBody List<Map<String, Object>> getQSAdvancePackingHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT " +
	        "    qmh.pn_id, qmh.contract_id, qmh.filepath, qmh.load_id, " +
	        "    qmh.grand_total, qmh.is_locked, qmh.is_delete, qmh.Cancel, " +
	        "    qmh.factory_id, qmh.created_date, qmh.modified_date, " +
	        "    qmh.operation_timestamp, qmh.operation_type, " +
	        "    cm.contract_name, " +
	        "    mm.milestone_code+'-'+ mm.milestone_name as milestone_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qmh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qmh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.QSADVANCE_PACKINGNOTE_MASTER_HISTORY qmh " +
	        "OUTER APPLY ( " +
	        "    SELECT TOP 1 contract_name FROM dbo.CONTRACT_MASTER_HISTORY cmh " +
	        "    WHERE cmh.contract_id = qmh.contract_id " +
	        "    ORDER BY cmh.transaction_date DESC " +
	        ") cm " +
	        "LEFT JOIN dbo.MILESTONE_MASTER mm ON qmh.milestone_id = mm.milestone_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qmh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qmh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qmh.is_delete = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qmh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qmh.operation_timestamp");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@GetMapping("/history/qs-advance-packing-items")
	public @ResponseBody List<Map<String, Object>> getQSAdvancePackingItemHistory(
	        @RequestParam String factoryId) {

	    StringBuilder sql = new StringBuilder();

	    sql.append(
	        "SELECT DISTINCT " +
	        "    qimh.history_id, qimh.pn_id, qimh.qty, qimh.per_kgs, qimh.unit_price, qimh.total, " +
	        "    qimh.type_id, qimh.is_delete, qimh.is_locked, " +
	        "    qimh.factory_id, qimh.created_date, qimh.modified_date, " +
	        "    qimh.operation_timestamp, qimh.operation_type, " +
	        "    uom.unit_name, " +
	        "    tp.scrap_name, " +
	        "    U1.username AS createdby, " +
	        "    U2.username AS modifiedby, " +
	        "    U3.username AS deletedby, " +
	        "    CASE qimh.factory_id " +
	        "        WHEN 1 THEN 'Bellary' " +
	        "        WHEN 2 THEN 'Gujarat' " +
	        "        ELSE CAST(qimh.factory_id AS NVARCHAR(50)) " +
	        "    END AS factory " +
	        "FROM dbo.QSADVANCE_PACKINGNOTEITEM_MASTER_HISTORY qimh " +
	        "LEFT JOIN dbo.UOM_MASTER uom ON qimh.UOM_id = uom.unit_id " +
	        "LEFT JOIN dbo.SCRAPTYPE_MASTER tp ON qimh.type_id = tp.type_id " +
	        "LEFT JOIN dbo.USERS_MASTER U1 ON qimh.created_by = U1.id " +
	        "LEFT JOIN dbo.USERS_MASTER U2 ON qimh.modified_by = U2.id " +
	        "LEFT JOIN dbo.USERS_MASTER U3 ON qimh.is_delete = U3.id " +
	        "WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    // ONLY FACTORY FILTER
	    if (factoryId != null && !factoryId.isEmpty() && !"all".equalsIgnoreCase(factoryId)) {
	        sql.append("AND qimh.factory_id = ? ");
	        params.add(Integer.parseInt(factoryId));
	    }

	    sql.append("ORDER BY qimh.operation_timestamp");

	    List<Map<String, Object>> list = new ArrayList<>();

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();
	        ResultSetMetaData md = rs.getMetaData();
	        int cols = md.getColumnCount();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            for (int i = 1; i <= cols; i++) {
	                row.put(md.getColumnLabel(i), rs.getObject(i));
	            }
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
}
