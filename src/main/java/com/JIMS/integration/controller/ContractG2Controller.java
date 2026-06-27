package com.JIMS.integration.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class ContractG2Controller {

	Logger logger = LogManager.getLogger(ContractG2Controller.class);
	@Autowired
	@Qualifier("bellaryDataSource")
	private DataSource bellaryDataSource;

	@Autowired
	@Qualifier("gujaratDataSource")
	private DataSource gujaratDataSource;
	@Autowired
	@Qualifier("misDataSource")
	private DataSource misDataSource;

	@Autowired
	@Qualifier("misDataSource2")
	private DataSource misDataSource2;

	@Autowired
	@Qualifier("jimsDataSource")
	private DataSource jimsDataSource;

	@GetMapping("/contractfrom/g2/listcontractorsbasedonfactory")
	public @ResponseBody Map<String, Object> getfetchEmployeeDataFrom(
	        @RequestParam("factoryId") String factoryId) {

	    logger.info("EXECUTING METHOD :: getfetchEmployeeDataFrom, factoryId = " + factoryId);

	    Map<String, Object> response = new HashMap<>();

	    String sql = "SELECT contract_id, descr, JobCode FROM Contracts";
	    String assignedContractSQL = "SELECT contract_id FROM CONTRACT_MASTER";

	    List<Map<String, String>> employees = new ArrayList<>();
	    Set<String> assignedContracts = new HashSet<>();

	    try (
	        Connection jimsCon = jimsDataSource.getConnection();
	        PreparedStatement pstAssigned = jimsCon.prepareStatement(assignedContractSQL);
	        ResultSet rsAssigned = pstAssigned.executeQuery();
	    ) {

	        // Step 1: Read assigned contract ids
	        while (rsAssigned.next()) {
	            assignedContracts.add(rsAssigned.getString("contract_id"));
	        }

	        // Step 2: Pick datasource based on factory
	        DataSource selectedDataSource;

	        if ("1".equals(factoryId)) {            // Bellary
	            selectedDataSource = bellaryDataSource;
	        } else if ("2".equals(factoryId)) {     // Gujarat
	            selectedDataSource = gujaratDataSource;
	        } else {
	            response.put("error", "Invalid factory selected");
	            return response;
	        }

	        // Step 3: Fetch ONLY from selected factory MIS
	        fetchContractsFromMIS(selectedDataSource, sql, assignedContracts, employees);

	        response.put("Data", employees);

	    } catch (SQLException e) {
	        logger.error("ERROR in getfetchEmployeeDataFrom", e);
	        response.put("error", "Database error");
	    }

	    logger.info("EXECUTED METHOD :: getfetchEmployeeDataFrom");
	    return response;
	}
	
	private void fetchContractsFromMIS(
	        DataSource ds,
	        String sql,
	        Set<String> assignedContracts,
	        List<Map<String, String>> outputList) throws SQLException {

	    try (
	        Connection con = ds.getConnection();
	        PreparedStatement pst = con.prepareStatement(sql);
	        ResultSet rs = pst.executeQuery();
	    ) {
	        while (rs.next()) {

	            String contractId = rs.getString("contract_id");

	            if (!assignedContracts.contains(contractId)) {

	                Map<String, String> map = new HashMap<>();
	                map.put("contract_id", contractId);

	                String contractName = rs.getString("JobCode") + " - " + rs.getString("descr");
	                map.put("contract_name", contractName);

	                outputList.add(map);
	            }
	        }
	    }
	}

	@SuppressWarnings("null")
	@GetMapping("/contract/g2/listloadforcontractorbasedonfactory")
	public @ResponseBody Map<String, Object> getfetchloaddetailS(@RequestParam int contract_id,
			@RequestParam int milestone_id, @RequestParam int factory_id) {
		String traLoadsSql = "SELECT tload_id, loadno FROM Tra_Loads WHERE contract_id = ?";
		String qsPackingMasterSql = "SELECT load_id, Cancel FROM QSPACKING_MASTER WHERE con_id = ? and factory_id = ?";
		String MSILength = "SELECT TOP 1 'MSI-' + CAST(CAST(RIGHT(lot_no, LEN(lot_no) - 4) AS INT) + 1 AS VARCHAR) AS load_id FROM QSPACKING_MASTER WHERE lot_no LIKE 'MSI-%' ORDER BY CAST(RIGHT(lot_no, LEN(lot_no) - 4) AS INT) DESC";
		String milestoneName = "select milestone_name from MILESTONE_MASTER where milestone_id = ?";
		String count = "MSI-1";
		String mileStoneName = null;
		Map<String, Object> response = new HashMap<>();
		logger.info("EXECUTING METHOD :: getfetchloaddetailS");
		if (factory_id == 1) {
			List<Map<String, String>> employees = new ArrayList<>();
			try (Connection misConnection = misDataSource.getConnection();
					Connection jimsConnection = jimsDataSource.getConnection();
					PreparedStatement traLoadsStmt = misConnection.prepareStatement(traLoadsSql);
					PreparedStatement qsPackingMasterStmt = jimsConnection.prepareStatement(qsPackingMasterSql);
					PreparedStatement msiid = jimsConnection.prepareStatement(MSILength);
					PreparedStatement miname = jimsConnection.prepareStatement(milestoneName);) {
				logger.info("EXECUTING METHOD :: BEFORE adding LOAD MSI LIST FROM QSPACKING FACTORY1");
				ResultSet msValue = msiid.executeQuery();
				while (msValue.next()) {
					String countValue = msValue.getString("load_id");
					if (countValue != null && !countValue.isEmpty()) {
						count = countValue;
					}
				}
				logger.info("EXECUTING METHOD :: AFTER adding LOAD MSI LIST FROM QSPACKING FACTORY1");
				logger.info("EXECUTING METHOD :: BEFORE adding MILESTONE NAME FACTORY1");
				miname.setInt(1, milestone_id);
				ResultSet msname = miname.executeQuery();
				while (msname.next()) {
					mileStoneName = msname.getString("milestone_name");
				}
				logger.info("EXECUTING METHOD :: AFTER adding MILESTONE NAME FACTORY1");
				qsPackingMasterStmt.setInt(1, contract_id);
				qsPackingMasterStmt.setInt(2, factory_id);
				logger.info("EXECUTING METHOD :: BEFORE adding LOAD LIST FROM QSPACKING FACTORY1");
				ResultSet qsResultSet = qsPackingMasterStmt.executeQuery();
				Set<String> existingLoadNos = new HashSet<>();
				while (qsResultSet.next()) {
//					existingLoadNos.add(qsResultSet.getString("load_id"));
					   int cancel = qsResultSet.getInt("Cancel");

					    // Only block load if cancel = 0
					    if (cancel == 0) {
					        existingLoadNos.add(qsResultSet.getString("load_id").trim());
					    }
				}
				logger.info("EXECUTING METHOD :: AFTER adding LOAD LIST FROM QSPACKING FACTORY1");
				traLoadsStmt.setInt(1, contract_id);
				logger.info("EXECUTING METHOD :: BEFORE adding LOAD  LIST FROM G2 FACTORY1");
				ResultSet traResultSet = traLoadsStmt.executeQuery();
				while (traResultSet.next()) {
					String tload_id = traResultSet.getString("tload_id");
					if (!existingLoadNos.contains(tload_id.trim())) { 
						Map<String, String> employeeData = new HashMap<>();
						employeeData.put("load_id", traResultSet.getString("tload_id").trim());
						employeeData.put("lot_no", traResultSet.getString("loadno").trim());
						employees.add(employeeData); 
					}
				}
				logger.info("EXECUTING METHOD :: AFTER adding LOAD  LIST FROM G2 FACTORY1");
				if (!mileStoneName.equalsIgnoreCase("Supply of Material")) {
					List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> ls = new HashMap<String, String>();
					ls.put("lot_no", count);
					ls.put("load_id", count);
					lsv.add(ls);
					response.put("Data", lsv);
					response.put("action", "Load Details");
					response.put("status", "yes");
					return response;
				} else {
					response.put("Data", employees);
					response.put("action", "Load Details");
					if (employees.size() > 0) {
						response.put("status", "yes");
					} else {
						response.put("status", "no");
						response.put("message", "for this Contractor No Load Available ");
					}
					return response;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				response.put("error", "Database error occurred");
				logger.error("ERROR IN THE METHOD FOR getfetchloaddetailS ::   -> " + e.getMessage());
			}

			return response;
		} else if (factory_id == 2) {
			List<Map<String, String>> employees = new ArrayList<>();
			try (Connection misConnection = misDataSource2.getConnection();
					Connection jimsConnection = jimsDataSource.getConnection();
					PreparedStatement traLoadsStmt = misConnection.prepareStatement(traLoadsSql);
					PreparedStatement qsPackingMasterStmt = jimsConnection.prepareStatement(qsPackingMasterSql);
					PreparedStatement msiid = jimsConnection.prepareStatement(MSILength);
					PreparedStatement miname = jimsConnection.prepareStatement(milestoneName);) {
				logger.info("EXECUTING METHOD :: BEFORE adding LOAD MSI LIST FROM QSPACKING FACTORY2");
				ResultSet msValue = msiid.executeQuery();
				while (msValue.next()) {
					String countValue = msValue.getString("load_id");
					if (countValue != null && !countValue.isEmpty()) {
						count = countValue;
					}
				}
				logger.info("EXECUTING METHOD :: AFTER adding LOAD MSI LIST FROM QSPACKING FACTORY2");
				logger.info("EXECUTING METHOD :: BEFORE adding MILESTONE NAME FACTORY2");
				miname.setInt(1, milestone_id);
				ResultSet msname = miname.executeQuery();
				while (msname.next()) {
					mileStoneName = msname.getString("milestone_name");
				}
				logger.info("EXECUTING METHOD :: AFTER adding MILESTONE NAME FACTORY2");
				qsPackingMasterStmt.setInt(1, contract_id);
				qsPackingMasterStmt.setInt(2, factory_id);
				logger.info("EXECUTING METHOD :: BEFORE adding LOAD LIST FROM QSPACKING FACTORY2");
				ResultSet qsResultSet = qsPackingMasterStmt.executeQuery();
				Set<String> existingLoadNos = new HashSet<>();
				while (qsResultSet.next()) {
//					existingLoadNos.add(qsResultSet.getString("load_id"));
					   int cancel = qsResultSet.getInt("Cancel");

					    // Only block load if cancel = 0
					    if (cancel == 0) {
					        existingLoadNos.add(qsResultSet.getString("load_id").trim());
					    }
				}
				logger.info("EXECUTING METHOD :: AFTER adding LOAD LIST FROM QSPACKING FACTORY2");
				traLoadsStmt.setInt(1, contract_id);
				logger.info("EXECUTING METHOD :: BEFORE adding LOAD  LIST FROM G2 FACTORY2");
				ResultSet traResultSet = traLoadsStmt.executeQuery();
				while (traResultSet.next()) {
					String tload_id = traResultSet.getString("tload_id");
					if (!existingLoadNos.contains(tload_id.trim())) { 
						Map<String, String> employeeData = new HashMap<>();
						employeeData.put("load_id", traResultSet.getString("tload_id").trim());
						employeeData.put("lot_no", traResultSet.getString("loadno").trim());
						employees.add(employeeData); 
					}
				}
				logger.info("EXECUTING METHOD :: AFTER adding LOAD  LIST FROM G2 FACTORY2");
				if (!mileStoneName.equalsIgnoreCase("Supply of Material")) {
					List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> ls = new HashMap<String, String>();
					ls.put("lot_no", count);
					ls.put("load_id", count);
					lsv.add(ls);
					response.put("Data", lsv);
					response.put("action", "Load Details");
					response.put("status", "yes");
					return response;
				} else {
					response.put("Data", employees);
					response.put("action", "Load Details");
					if (employees.size() > 0) {
						response.put("status", "yes");
					} else {
						response.put("status", "no");
						response.put("message", "for this Contractor No Load Available ");
					}
					return response;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				response.put("error", "Database error occurred");
				logger.error("ERROR IN THE METHOD FOR getfetchloaddetailS 2 ::   -> " + e.getMessage());
			}

			return response;
		}
		logger.info("EXECUTED METHOD :: getfetchEmployeeDataFrom");
		return response;
	}
	
	
	
	@GetMapping("/invoice/g2/getrasportname")
	public @ResponseBody Map<String, Object> getfetchtransportName(
	        @RequestParam String load_id,
	        @RequestParam int factory_id) {

	    String transportQuery = "SELECT td.trailerref, hr.company " +
	            "FROM Tra_Loads td " +
	            "INNER JOIN Tra_LoadRuns tr ON td.tload_id = tr.tload_id " +
	            "INNER JOIN Hauliers hr ON tr.haulier_id = hr.haulier_id " +
	            "WHERE td.tload_id = ? " +
	            "ORDER BY td.tload_id ASC";

	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, String>> transportList = new ArrayList<>();

	    logger.info("EXECUTING METHOD :: getfetchtransportName");

	    // Choose datasource based on factory_id
	    DataSource selectedDataSource;
	    if (factory_id == 1) {
	        selectedDataSource = misDataSource;
	    } else if (factory_id == 2) {
	        selectedDataSource = misDataSource2;
	    } else {
	        response.put("status", "no");
	        response.put("message", "Invalid factory id");
	        return response;
	    }

	    try (Connection connection = selectedDataSource.getConnection();
	         PreparedStatement stmt = connection.prepareStatement(transportQuery)) {

	        stmt.setString(1, load_id);

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Map<String, String> data = new HashMap<>();
	            data.put("trailerref", rs.getString("trailerref"));
	            data.put("company", rs.getString("company"));
	            transportList.add(data);
	        }

	        response.put("Data", transportList);
	        response.put("action", "Transport Details");

	        if (!transportList.isEmpty()) {
	            response.put("status", "yes");
	        } else {
	            response.put("status", "no");
	            response.put("message", "No transport details found");
	        }

	    } catch (SQLException e) {
	        logger.error("ERROR IN getfetchtransportName :: " + e.getMessage());
	        response.put("status", "error");
	        response.put("message", "Database error occurred");
	    }

	    return response;
	}
	@GetMapping("/invoice/g2/newinvreport")
	public @ResponseBody Map<String, Object> getNewInvoiceReport(
	        @RequestParam String contract_id,
	        @RequestParam String factory,
	        @RequestParam String from,
	        @RequestParam String to) {

	    logger.info("EXECUTING METHOD :: getNewInvoiceReport");

	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, Object>> invoiceList = new ArrayList<>();

	    String spCall = "{call sp_new_inv_report(?, ?, ?, ?)}";

	    try (Connection connection = jimsDataSource.getConnection();
	         CallableStatement stmt = connection.prepareCall(spCall)) {

	        stmt.setString(1, contract_id);  // @contract_id
	        stmt.setString(2, factory);      // @factory_id
	        stmt.setDate(3, java.sql.Date.valueOf(from));
	        stmt.setDate(4, java.sql.Date.valueOf(to));

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {

	            Map<String, Object> row = new HashMap<>();

	            row.put("id", rs.getInt("id"));
	            row.put("inv_type", rs.getString("inv_type"));
	            row.put("invoice_no", rs.getString("incno"));
	            row.put("contract_id", rs.getString("contract_id"));
	            row.put("jobCode", rs.getString("JobCode"));
	            row.put("workorder_no", rs.getString("workorder_no"));
	            row.put("work_date", rs.getString("work_date"));
	            row.put("load_id", rs.getString("load_id"));
	            row.put("loadno", rs.getString("loadno"));
	            row.put("address", rs.getString("add1"));
	            row.put("verified_date", rs.getString("date"));
	            row.put("scrap_type", rs.getString("scrap_type"));
	            row.put("unit_name", rs.getString("unit_name"));

	            row.put("qty", rs.getBigDecimal("qty"));
	            row.put("per_kgs", rs.getBigDecimal("per_kgs"));
	            row.put("unit_price", rs.getBigDecimal("unit_price"));
	            row.put("total", rs.getBigDecimal("total"));

	            row.put("freight", rs.getBigDecimal("freight"));
	            row.put("wb_weight", rs.getBigDecimal("wb_weight"));
	            row.put("transport_name", rs.getString("transport_name"));
	            row.put("vehicle_no", rs.getString("vechile_no"));

	            row.put("status", rs.getString("status"));
	            row.put("remarks", rs.getString("remarks"));

	            row.put("gst_number", rs.getString("gst_number"));
	            row.put("business_unit", rs.getString("badd"));

	            row.put("adv_tax_rec", rs.getBigDecimal("adv_tax_rec"));
	            row.put("adv_nontax_rec", rs.getBigDecimal("adv_nontax_rec"));

	            row.put("IGSTP", rs.getBigDecimal("IGSTP"));
	            row.put("CGSTP", rs.getBigDecimal("CGSTP"));
	            row.put("SGSTP", rs.getBigDecimal("SGSTP"));

	            row.put("IGSTV", rs.getBigDecimal("IGSTV"));
	            row.put("CGSTV", rs.getBigDecimal("CGSTV"));
	            row.put("SGSTV", rs.getBigDecimal("SGSTV"));
	            
	            row.put("IGSTA", rs.getString("IGSTA"));
	            row.put("CGSTA", rs.getString("CGSTA"));
	            row.put("SGSTA", rs.getString("SGSTA"));

	            row.put("ptd", rs.getBigDecimal("ptd"));
	            row.put("ptc", rs.getBigDecimal("ptc"));
	            row.put("total_invoice_value", rs.getBigDecimal("total_invoice_value"));
	            
	            row.put("cgst", rs.getString("cgst"));
	            row.put("cpan", rs.getString("cpan"));

	            row.put("factory", rs.getString("factory"));

	            invoiceList.add(row);
	        }

	        response.put("Data", invoiceList);
	        response.put("status", invoiceList.isEmpty() ? "no" : "yes");

	    } catch (SQLException e) {
	        logger.error("ERROR in getNewInvoiceReport", e);
	        response.put("status", "error");
	        response.put("message", "Database error occurred");
	    }

	    logger.info("EXECUTED METHOD :: getNewInvoiceReport");
	    return response;
	}
	
	@GetMapping("/contracts/byfactory")
	public @ResponseBody Map<String, Object> getContractsByFactory(
	        @RequestParam("factory") String factory) {

	    logger.info("EXECUTING METHOD :: getContractsByFactory, factory = " + factory);

	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, String>> contractList = new ArrayList<>();

	    try {

	        if ("1".equals(factory)) {

	            // Bellary
	            fetchContracts(bellaryDataSource, contractList);

	        } else if ("2".equals(factory)) {

	            // Gujarat
	            fetchContracts(gujaratDataSource, contractList);

	        } else if ("ALL".equalsIgnoreCase(factory)) {

	            // Fetch from both factories
	            fetchContracts(bellaryDataSource, contractList);
	            fetchContracts(gujaratDataSource, contractList);

	        } else {

	            response.put("status", "error");
	            response.put("message", "Invalid factory selected");
	            return response;
	        }

	        response.put("Data", contractList);
	        response.put("status", contractList.isEmpty() ? "no" : "yes");

	    } catch (Exception e) {

	        logger.error("ERROR in getContractsByFactory", e);
	        response.put("status", "error");
	        response.put("message", "Database error occurred");
	    }

	    logger.info("EXECUTED METHOD :: getContractsByFactory");
	    return response;
	}
	
	private void fetchContracts(DataSource dataSource,
            List<Map<String, String>> contractList) throws SQLException {

String sql = "SELECT contract_id, JobCode, descr FROM Contracts";

try (Connection con = dataSource.getConnection();
PreparedStatement pst = con.prepareStatement(sql);
ResultSet rs = pst.executeQuery()) {

while (rs.next()) {

Map<String, String> map = new HashMap<>();

map.put("contract_id", rs.getString("contract_id"));

String contractName =
    rs.getString("JobCode") + " - " + rs.getString("descr");

map.put("contract_name", contractName);

contractList.add(map);
}
}
}
	@GetMapping("/openingbalance/bycontract")
	public @ResponseBody Map<String, Object> getOpeningBalance(
	        @RequestParam String contract_id) {

	    logger.info("EXECUTING METHOD :: getOpeningBalance");

	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, Object>> list = new ArrayList<>();

	    String sql;

	    if ("ALL".equalsIgnoreCase(contract_id)) {

	        sql =
	        "SELECT ob.con_id, " +
	        "CASE WHEN cm.taxable = 'Yes' THEN obi.avl_bal ELSE 0.00 END AS Taxable, " +
	        "CASE WHEN cm.taxable = 'No' THEN obi.avl_bal ELSE 0.00 END AS NonTaxable " +
	        "FROM OPENING_BALANCE ob " +
	        "JOIN OPENING_BALANCE_ITEM obi ON ob.pn_id = obi.pn_id " +
	        "JOIN CONTRACT_MASTER cm ON cm.contract_id = ob.con_id " +
	        "WHERE ob.is_delete = 0 AND obi.latest_flag <> 1";

	    } else {

	        sql =
	        "SELECT ob.con_id, " +
	        "CASE WHEN cm.taxable = 'Yes' THEN obi.avl_bal ELSE 0.00 END AS Taxable, " +
	        "CASE WHEN cm.taxable = 'No' THEN obi.avl_bal ELSE 0.00 END AS NonTaxable " +
	        "FROM OPENING_BALANCE ob " +
	        "JOIN OPENING_BALANCE_ITEM obi ON ob.pn_id = obi.pn_id " +
	        "JOIN CONTRACT_MASTER cm ON cm.contract_id = ob.con_id " +
	        "WHERE ob.con_id = ? AND ob.is_delete = 0 AND obi.latest_flag <> 1";
	    }

	    try (Connection con = jimsDataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        if (!"ALL".equalsIgnoreCase(contract_id)) {
	            pst.setString(1, contract_id);
	        }

	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {

	            Map<String, Object> data = new HashMap<>();

	            data.put("contract_id", rs.getString("con_id"));
	            data.put("taxable", rs.getBigDecimal("Taxable"));
	            data.put("nontaxable", rs.getBigDecimal("NonTaxable"));

	            list.add(data);
	        }

	        response.put("Data", list);
	        response.put("status", list.isEmpty() ? "no" : "yes");

	    } catch (Exception e) {

	        logger.error("ERROR in getOpeningBalance", e);

	        response.put("status", "error");
	        response.put("message", "Database error");
	    }

	    return response;
	}
	
	
	
	@PostMapping("/check-g2-username")
	public @ResponseBody Map<String, Object> checkG2Username(
	        @RequestBody Map<String, String> request) {

	    logger.info("EXECUTING METHOD :: checkG2Username");

	    Map<String, Object> response = new HashMap<>();

	    String username = request.get("username");

	    String sql = "SELECT COUNT(*) FROM Users WHERE UPPER(user_name) = UPPER(?)";

	    try (
	        Connection con = misDataSource.getConnection();
	        PreparedStatement pst = con.prepareStatement(sql)
	    ) {

	        pst.setString(1, username);

	        ResultSet rs = pst.executeQuery();

	        boolean exists = false;

	        if (rs.next()) {
	            exists = rs.getInt(1) > 0;
	        }

	        response.put("exists", exists);

	    } catch (Exception e) {

	        logger.error("ERROR IN checkG2Username :: ", e);

	        response.put("exists", false);
	    }

	    logger.info("EXECUTED METHOD :: checkG2Username");

	    return response;
	}
}

