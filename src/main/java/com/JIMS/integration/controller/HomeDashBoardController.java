package com.JIMS.integration.controller;

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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequestMapping("/jssl/HomeDashBoard")
public class HomeDashBoardController {

	Logger logger = LogManager.getLogger(ContractG2Controller.class);

	@Autowired
	@Qualifier("misDataSource")
	private DataSource misDataSource;

	@Autowired
	@Qualifier("misDataSource2")
	private DataSource misDataSource2;
	
	
	
	
	@Autowired
	@Qualifier("webDataSource")
	private DataSource misDataSource3;

	@GetMapping("/contracts/byfactory")
	public @ResponseBody Map<String, Object> getContractsByFactory(@RequestParam("factory") String factory) {

		logger.info("EXECUTING METHOD :: getContractsByFactory, factory = " + factory);

		Map<String, Object> response = new HashMap<>();

		List<Map<String, String>> contractList = new ArrayList<>();

		// FOR UNIQUE CONTRACTS
		Set<String> uniqueContracts = new HashSet<>();

		try {

			if ("1".equals(factory)) {

				// Bellary
				fetchContracts(misDataSource, contractList, uniqueContracts);

			} else if ("2".equals(factory)) {

				// Gujarat
				fetchContracts(misDataSource2, contractList, uniqueContracts);

			} else if ("ALL".equalsIgnoreCase(factory)) {

				// BOTH FACTORIES
				fetchContracts(misDataSource, contractList, uniqueContracts);
				fetchContracts(misDataSource2, contractList, uniqueContracts);

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

	private void fetchContracts(DataSource dataSource, List<Map<String, String>> contractList,
			Set<String> uniqueContracts) throws SQLException {

		String sql = "SELECT contract, JobCode " + "FROM Contracts " + "WHERE contract IS NOT NULL "
				+ "AND contract <> ''";

		try (Connection con = dataSource.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {

				String contract = rs.getString("contract");

				// SKIP DUPLICATES
				if (uniqueContracts.contains(contract)) {
					continue;
				}

				uniqueContracts.add(contract);

				Map<String, String> map = new HashMap<>();

				map.put("contract_id", contract);

				map.put("jobcode", rs.getString("JobCode"));

				contractList.add(map);
			}
		}
	}

//	@GetMapping("/shipment")
//	public @ResponseBody Map<String, Object> getShipmentData(
//	        @RequestParam("factory") String factory,
//	        @RequestParam("contract") String contract) {
//
//	    logger.info("EXECUTING METHOD :: getShipmentData");
//
//	    Map<String, Object> response = new HashMap<>();
//
//	    List<Map<String, Object>> shipmentList = new ArrayList<>();
//
//	    try {
//
//	        // SINGLE FACTORY
//	        if ("1".equals(factory)) {
//
//	            fetchFGStockData(misDataSource, shipmentList, contract);
//
//	            // SORT
//	            shipmentList.sort((a, b) -> Double.compare(
//	                    Double.parseDouble(String.valueOf(b.get("weight"))),
//	                    Double.parseDouble(String.valueOf(a.get("weight")))
//	            ));
//
//	            // TOP 10
//	            if (shipmentList.size() > 10) {
//
//	                shipmentList =
//	                        new ArrayList<>(shipmentList.subList(0, 10));
//	            }
//
//	        } else if ("2".equals(factory)) {
//
//	            fetchFGStockData(misDataSource2, shipmentList, contract);
//
//	            // SORT
//	            shipmentList.sort((a, b) -> Double.compare(
//	                    Double.parseDouble(String.valueOf(b.get("weight"))),
//	                    Double.parseDouble(String.valueOf(a.get("weight")))
//	            ));
//
//	            // TOP 10
//	            if (shipmentList.size() > 10) {
//
//	                shipmentList =
//	                        new ArrayList<>(shipmentList.subList(0, 10));
//	            }
//
//	        }
//
//	        // BOTH FACTORIES
//	        else if ("ALL".equalsIgnoreCase(factory)) {
//
//	            List<Map<String, Object>> tempList = new ArrayList<>();
//
//	            // FETCH BOTH FACTORIES
//	            fetchFGStockDataAll(misDataSource, tempList, contract);
//
//	            fetchFGStockDataAll(misDataSource2, tempList, contract);
//
//	            // MERGE SAME CONTRACTS
//	            Map<String, Double> mergedMap = new HashMap<>();
//
//	            for (Map<String, Object> row : tempList) {
//
//	                String jobCode =
//	                        String.valueOf(row.get("job"));
//
//	                Double weight =
//	                        Double.parseDouble(
//	                                String.valueOf(row.get("weight"))
//	                        );
//
//	                mergedMap.put(
//	                        jobCode,
//	                        mergedMap.getOrDefault(jobCode, 0.0) + weight
//	                );
//	            }
//
//	            // CONVERT BACK TO LIST
//	            for (Map.Entry<String, Double> entry : mergedMap.entrySet()) {
//
//	                Map<String, Object> map = new HashMap<>();
//
//	                map.put("job", entry.getKey());
//
//	                map.put("weight", entry.getValue());
//
//	                shipmentList.add(map);
//	            }
//
//	            // SORT DESC
//	            shipmentList.sort((a, b) -> Double.compare(
//	                    Double.parseDouble(String.valueOf(b.get("weight"))),
//	                    Double.parseDouble(String.valueOf(a.get("weight")))
//	            ));
//
//	            // TOP 10
//	            if (shipmentList.size() > 10) {
//
//	                shipmentList =
//	                        new ArrayList<>(shipmentList.subList(0, 10));
//	            }
//
//	        } else {
//
//	            response.put("status", "error");
//
//	            response.put("message", "Invalid factory selected");
//
//	            return response;
//	        }
//
//	        response.put("Data", shipmentList);
//
//	        response.put("status",
//	                shipmentList.isEmpty() ? "no" : "yes");
//
//	    } catch (Exception e) {
//
//	        logger.error("ERROR in getShipmentData", e);
//
//	        response.put("status", "error");
//
//	        response.put("message", "Database error occurred");
//	    }
//
//	    logger.info("EXECUTED METHOD :: getShipmentData");
//
//	    return response;
//	}
//
//
//	private void fetchFGStockDataAll(
//	        DataSource dataSource,
//	        List<Map<String, Object>> shipmentList,
//	        String contract) throws SQLException {
//
//	    String sql =
//
//	            "WITH ContractWeights AS ( " +
//
//	            "    SELECT " +
//
//	            "        Contracts.JobCode, " +
//
//	            "        ISNULL(SUM(vw_JSSLItemsInDespatchYard2.weight), 0) AS WEIGHT " +
//
//	            "    FROM vw_JSSLItemsInDespatchYard2 " +
//
//	            "    LEFT JOIN Loads " +
//	            "        ON vw_JSSLItemsInDespatchYard2.load_id = Loads.load_id " +
//
//	            "    LEFT JOIN Contracts " +
//	            "        ON Loads.contract_id = Contracts.contract_id " +
//
//	            "    WHERE scrap = 0 " +
//
//	            "      AND ( ? = 'ALL' OR CAST(Contracts.contract AS VARCHAR) = ? ) " +
//
//	            "    GROUP BY Contracts.JobCode " +
//
//	            ") " +
//
//	            "SELECT " +
//	            "    JobCode, " +
//	            "    WEIGHT " +
//
//	            "FROM ContractWeights";
//
//	    try (Connection con = dataSource.getConnection();
//	         PreparedStatement pst = con.prepareStatement(sql)) {
//
//	        pst.setString(1, contract.toUpperCase());
//
//	        pst.setString(2, contract);
//
//	        try (ResultSet rs = pst.executeQuery()) {
//
//	            while (rs.next()) {
//
//	                Map<String, Object> map = new HashMap<>();
//
//	                map.put("job", rs.getString("JobCode"));
//
//	                map.put("weight", rs.getDouble("WEIGHT"));
//
//	                shipmentList.add(map);
//	            }
//	        }
//	    }
//	}
//
//	private void fetchFGStockData(DataSource dataSource, List<Map<String, Object>> shipmentList, String contract)
//			throws SQLException {
//
//		String sql = "WITH ContractWeights AS ( " + "    SELECT " + "        Contracts.JobCode, "
//				+ "        ISNULL(SUM(vw_JSSLItemsInDespatchYard2.weight), 0) AS WEIGHT " +
//
//				"    FROM vw_JSSLItemsInDespatchYard2 " +
//
//				"    LEFT JOIN Loads " + "        ON vw_JSSLItemsInDespatchYard2.load_id = Loads.load_id " +
//
//				"    LEFT JOIN Contracts " + "        ON Loads.contract_id = Contracts.contract_id " +
//
//				"    WHERE scrap = 0 " + "      AND ( ? = 'ALL' OR CAST(Contracts.contract AS VARCHAR) = ? ) " +
//
//				"    GROUP BY Contracts.JobCode " + ") " +
//
//				"SELECT * FROM ( " +
//
//				"    SELECT TOP 10 " + "        JobCode, " + "        WEIGHT, " + "        1 AS SortOrder " +
//
//				"    FROM ContractWeights " + "    ORDER BY WEIGHT DESC " +
//
//				") A " +
//
//				"ORDER BY SortOrder, WEIGHT DESC";
//
//		try (Connection con = dataSource.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
//
//			pst.setString(1, contract.toUpperCase());
//			pst.setString(2, contract);
//
//			try (ResultSet rs = pst.executeQuery()) {
//
//				while (rs.next()) {
//
//					Map<String, Object> map = new HashMap<>();
//
//					map.put("job", rs.getString("JobCode"));
//					map.put("weight", rs.getDouble("WEIGHT"));
//
//					shipmentList.add(map);
//				}
//			}
//		}
//	}

	@GetMapping("/shipment")
	public @ResponseBody Map<String, Object> getShipmentData(
	        @RequestParam("factory") String factory,
	        @RequestParam("contract") String contract) {

	    logger.info("EXECUTING METHOD :: getShipmentData");

	    Map<String, Object> response = new HashMap<>();

	    try {
	        List<Map<String, Object>> shipmentList;
	        double total;  // ✅ NEW

	        if ("1".equals(factory)) {
	            shipmentList = fetchTop10FGStock(misDataSource, contract);
	            total = fetchTotalWeight(misDataSource);  // ✅ NEW

	        } else if ("2".equals(factory)) {
	            shipmentList = fetchTop10FGStock(misDataSource2, contract);
	            total = fetchTotalWeight(misDataSource2);  // ✅ NEW

	        } else if ("ALL".equalsIgnoreCase(factory)) {

	            // BOTH FACTORIES IN PARALLEL
	            CompletableFuture<List<Map<String, Object>>> future1 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        return fetchAllFGStock(misDataSource, contract);
	                    } catch (SQLException e) {
	                        throw new RuntimeException("Factory 1 DB error", e);
	                    }
	                });

	            CompletableFuture<List<Map<String, Object>>> future2 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        return fetchAllFGStock(misDataSource2, contract);
	                    } catch (SQLException e) {
	                        throw new RuntimeException("Factory 2 DB error", e);
	                    }
	                });

	            // ✅ NEW — total futures run in parallel alongside existing futures
	            CompletableFuture<Double> totalFuture1 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        return fetchTotalWeight(misDataSource);
	                    } catch (SQLException e) {
	                        throw new RuntimeException("Factory 1 total error", e);
	                    }
	                });

	            CompletableFuture<Double> totalFuture2 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        return fetchTotalWeight(misDataSource2);
	                    } catch (SQLException e) {
	                        throw new RuntimeException("Factory 2 total error", e);
	                    }
	                });

	            List<Map<String, Object>> list1 = future1.get();
	            List<Map<String, Object>> list2 = future2.get();
	            total = totalFuture1.get() + totalFuture2.get();  // ✅ NEW

	            // MERGE SAME JOB CODES FROM BOTH FACTORIES
	            Map<String, Double> mergedMap = new HashMap<>();

	            for (Map<String, Object> row : list1) {
	                mergedMap.merge(
	                    String.valueOf(row.get("job")),
	                    (Double) row.get("weight"),
	                    Double::sum
	                );
	            }
	            for (Map<String, Object> row : list2) {
	                mergedMap.merge(
	                    String.valueOf(row.get("job")),
	                    (Double) row.get("weight"),
	                    Double::sum
	                );
	            }

	            // SORT DESC + TOP 10 IN ONE STREAM
	            shipmentList = mergedMap.entrySet().stream()
	                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
	                .limit(10)
	                .map(e -> {
	                    Map<String, Object> m = new HashMap<>();
	                    m.put("job", e.getKey());
	                    m.put("weight", e.getValue());
	                    return m;
	                })
	                .collect(Collectors.toList());

	        } else {
	            response.put("status", "error");
	            response.put("message", "Invalid factory selected");
	            return response;
	        }

	        response.put("Data", shipmentList);
	        response.put("total", total);  // ✅ NEW
	        response.put("status", shipmentList.isEmpty() ? "no" : "yes");

	    } catch (Exception e) {
	        logger.error("ERROR in getShipmentData", e);
	        response.put("status", "error");
	        response.put("message", "Database error occurred");
	    }

	    logger.info("EXECUTED METHOD :: getShipmentData");
	    return response;
	}

	//  Used for Factory 1 / Factory 2 — TOP 10 done in DB directly
	private List<Map<String, Object>> fetchTop10FGStock(
	        DataSource dataSource,
	        String contract) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    // ✅ Two different SQLs — avoids OR condition & CAST overhead in DB
	    String sql = isAll
	        ?
	        // CONTRACT = ALL → no filter needed
	        "SELECT TOP 10 " +
	        "    Contracts.JobCode, " +
	        "    ISNULL(SUM(vw_JSSLItemsInDespatchYard2.weight), 0) AS WEIGHT " +
	        "FROM vw_JSSLItemsInDespatchYard2 " +
	        "LEFT JOIN Loads " +
	        "    ON vw_JSSLItemsInDespatchYard2.load_id = Loads.load_id " +
	        "LEFT JOIN Contracts " +
	        "    ON Loads.contract_id = Contracts.contract_id " +
	        "WHERE scrap = 0 " +
	        "GROUP BY Contracts.JobCode " +
	        "ORDER BY WEIGHT DESC"
	        :
	        // CONTRACT = specific ID → filter applied
	        "SELECT TOP 10 " +
	        "    Contracts.JobCode, " +
	        "    ISNULL(SUM(vw_JSSLItemsInDespatchYard2.weight), 0) AS WEIGHT " +
	        "FROM vw_JSSLItemsInDespatchYard2 " +
	        "LEFT JOIN Loads " +
	        "    ON vw_JSSLItemsInDespatchYard2.load_id = Loads.load_id " +
	        "LEFT JOIN Contracts " +
	        "    ON Loads.contract_id = Contracts.contract_id " +
	        "WHERE scrap = 0 " +
	        "  AND CAST(Contracts.contract AS VARCHAR) = ? " +
	        "GROUP BY Contracts.JobCode " +
	        "ORDER BY WEIGHT DESC";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        // Only bind param if specific contract
	        if (!isAll) {
	            pst.setString(1, contract);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("job", rs.getString("JobCode"));
	                map.put("weight", rs.getDouble("WEIGHT"));
	                result.add(map);
	            }
	        }
	    }
	    return result;
	}


	// ✅ Used for ALL factory — fetches ALL rows for Java-side merge
	private List<Map<String, Object>> fetchAllFGStock(
	        DataSource dataSource,
	        String contract) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    String sql = isAll
	        ?
	        // CONTRACT = ALL → no filter
	        "SELECT " +
	        "    Contracts.JobCode, " +
	        "    ISNULL(SUM(vw_JSSLItemsInDespatchYard2.weight), 0) AS WEIGHT " +
	        "FROM vw_JSSLItemsInDespatchYard2 " +
	        "LEFT JOIN Loads " +
	        "    ON vw_JSSLItemsInDespatchYard2.load_id = Loads.load_id " +
	        "LEFT JOIN Contracts " +
	        "    ON Loads.contract_id = Contracts.contract_id " +
	        "WHERE scrap = 0 " +
	        "GROUP BY Contracts.JobCode"
	        :
	        // CONTRACT = specific ID → filter applied
	        "SELECT " +
	        "    Contracts.JobCode, " +
	        "    ISNULL(SUM(vw_JSSLItemsInDespatchYard2.weight), 0) AS WEIGHT " +
	        "FROM vw_JSSLItemsInDespatchYard2 " +
	        "LEFT JOIN Loads " +
	        "    ON vw_JSSLItemsInDespatchYard2.load_id = Loads.load_id " +
	        "LEFT JOIN Contracts " +
	        "    ON Loads.contract_id = Contracts.contract_id " +
	        "WHERE scrap = 0 " +
	        "  AND CAST(Contracts.contract AS VARCHAR) = ? " +
	        "GROUP BY Contracts.JobCode";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        if (!isAll) {
	            pst.setString(1, contract);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("job", rs.getString("JobCode"));
	                map.put("weight", rs.getDouble("WEIGHT"));
	                result.add(map);
	            }
	        }
	    }
	    return result;
	}
	
	
	private double fetchTotalWeight(DataSource dataSource) throws SQLException {
	    String sql =
	        "SELECT ISNULL(SUM(weight), 0) AS TOTAL_WEIGHT " +
	        "FROM vw_JSSLItemsInDespatchYard2 " +
	        "WHERE scrap = 0";

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql);
	         ResultSet rs = pst.executeQuery()) {
	        if (rs.next()) {
	            return rs.getDouble("TOTAL_WEIGHT");
	        }
	    }
	    return 0.0;
	}


	
	
	@GetMapping("/rawmaterial")
	public @ResponseBody Map<String, Object> getRawMaterialData(
	        @RequestParam("factory") String factory,
	        @RequestParam("contract") String contract) {

	    logger.info("EXECUTING METHOD :: getRawMaterialData");

	    Map<String, Object> response = new HashMap<>();
	    List<Map<String, Object>> rawMaterialList = new ArrayList<>();

	    try {
	        double total;

	        if ("1".equals(factory)) {
	            fetchRawMaterialData(misDataSource, rawMaterialList, contract);
	            total = fetchRawMaterialTotal(misDataSource);  // ✅ NEW

	        } else if ("2".equals(factory)) {
	            fetchRawMaterialData(misDataSource2, rawMaterialList, contract);
	            total = fetchRawMaterialTotal(misDataSource2);  // ✅ NEW

	        } else if ("ALL".equalsIgnoreCase(factory)) {

	            // ✅ ALL 3 DB calls in true parallel
	            CompletableFuture<List<Map<String, Object>>> f1 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        List<Map<String, Object>> l = new ArrayList<>();
	                        fetchRawMaterialDataAll(misDataSource, l, contract);
	                        return l;
	                    } catch (SQLException e) { throw new RuntimeException("RM Factory 1 error", e); }
	                });

	            CompletableFuture<List<Map<String, Object>>> f2 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        List<Map<String, Object>> l = new ArrayList<>();
	                        fetchRawMaterialDataAll(misDataSource2, l, contract);
	                        return l;
	                    } catch (SQLException e) { throw new RuntimeException("RM Factory 2 error", e); }
	                });

	            CompletableFuture<Double> totalF1 =
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchRawMaterialTotal(misDataSource); }
	                    catch (SQLException e) { throw new RuntimeException("RM total 1 error", e); }
	                });

	            CompletableFuture<Double> totalF2 =
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchRawMaterialTotal(misDataSource2); }
	                    catch (SQLException e) { throw new RuntimeException("RM total 2 error", e); }
	                });

	            // ✅ Wait for all 4 at once — true parallel
	            CompletableFuture.allOf(f1, f2, totalF1, totalF2).get();

	            List<Map<String, Object>> tempList = new ArrayList<>();
	            tempList.addAll(f1.get());
	            tempList.addAll(f2.get());
	            total = totalF1.get() + totalF2.get();

	            // Merge same jobCode
	            Map<String, Double> mergedMap = new HashMap<>();
	            for (Map<String, Object> row : tempList) {
	                String jobCode = String.valueOf(row.get("jobCode"));
	                Double weight  = Double.parseDouble(String.valueOf(row.get("weight")));
	                mergedMap.put(jobCode, mergedMap.getOrDefault(jobCode, 0.0) + weight);
	            }

	            // Convert + sort + top 10
	            for (Map.Entry<String, Double> entry : mergedMap.entrySet()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("jobCode", entry.getKey());
	                map.put("weight",  entry.getValue());
	                rawMaterialList.add(map);
	            }

	            rawMaterialList.sort((a, b) -> Double.compare(
	                Double.parseDouble(String.valueOf(b.get("weight"))),
	                Double.parseDouble(String.valueOf(a.get("weight")))
	            ));

	            if (rawMaterialList.size() > 10) {
	                rawMaterialList = new ArrayList<>(rawMaterialList.subList(0, 10));
	            }

	        } else {
	            response.put("status", "error");
	            response.put("message", "Invalid factory selected");
	            return response;
	        }

	        response.put("Data", rawMaterialList);
	        response.put("total", total);  // ✅ NEW
	        response.put("status", rawMaterialList.isEmpty() ? "no" : "yes");

	    } catch (Exception e) {
	        logger.error("ERROR in getRawMaterialData", e);
	        response.put("status", "error");
	        response.put("message", "Database error occurred");
	    }

	    logger.info("EXECUTED METHOD :: getRawMaterialData");
	    return response;
	}
	private void fetchRawMaterialData(DataSource dataSource, List<Map<String, Object>> rawMaterialList, String contract)
			throws SQLException {
		String sql = "SELECT TOP 10 " + " c.jobcode, " + " ISNULL(SUM(ss.sweight),0) AS TOTAL_WEIGHT "
				+ "FROM MIS.dbo.SteelStock ss " + "INNER JOIN MIS.dbo.SectionSizes sz " + " ON ss.sect_id = sz.sect_id "
				+ "INNER JOIN MIS.dbo.SectionGrades sg " + " ON ss.grade_id = sg.grade_id "
				+ "LEFT JOIN MIS.dbo.Purchase_Orders po " + " ON ss.porder_id = po.porder_id "
				+ "LEFT JOIN MIS.dbo.Contracts c " + " ON po.contract_id = c.contract_id " + "WHERE "
				+ " ( ? = 'ALL' OR CAST(c.contract AS VARCHAR) = ? ) " + " AND ss.status = 0   AND ss.instock = 1 "
				+ " AND (ss.shop_id IS NULL OR ss.shop_id = '') " + "GROUP BY c.jobcode "
				+ "ORDER BY TOTAL_WEIGHT DESC";
		try (Connection con = dataSource.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, contract.toUpperCase());
			pst.setString(2, contract);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<>();
					map.put("jobCode", rs.getString("jobcode"));
					map.put("weight", rs.getDouble("TOTAL_WEIGHT"));
					rawMaterialList.add(map);
				}
			}
		}
	}

	private void fetchRawMaterialDataAll(DataSource dataSource, List<Map<String, Object>> rawMaterialList,
			String contract) throws SQLException {

		String sql = "SELECT " + "    c.jobcode, " + "    ISNULL(SUM(ss.sweight),0) AS TOTAL_WEIGHT " +

				"FROM MIS.dbo.SteelStock ss " +

				"INNER JOIN MIS.dbo.SectionSizes sz " + "    ON ss.sect_id = sz.sect_id " +

				"INNER JOIN MIS.dbo.SectionGrades sg " + "    ON ss.grade_id = sg.grade_id " +

				"LEFT JOIN MIS.dbo.Purchase_Orders po " + "    ON ss.porder_id = po.porder_id " +

				"LEFT JOIN MIS.dbo.Contracts c " + "    ON po.contract_id = c.contract_id " +

				"WHERE " + "    ( ? = 'ALL' OR CAST(c.contract AS VARCHAR) = ? ) " + "    AND ss.status = 0   AND ss.instock = 1 "
				+ "    AND (ss.shop_id IS NULL OR ss.shop_id = '') " +

				"GROUP BY c.jobcode ";

		// ONLY SINGLE FACTORY SORT + TOP 10
		if (!"ALL".equalsIgnoreCase(contract)) {

			sql += " ORDER BY TOTAL_WEIGHT DESC";
		}

		try (Connection con = dataSource.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, contract.toUpperCase());
			pst.setString(2, contract);

			try (ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {

					Map<String, Object> map = new HashMap<>();

					map.put("jobCode", rs.getString("jobcode"));
					map.put("weight", rs.getDouble("TOTAL_WEIGHT"));

					rawMaterialList.add(map);
				}
			}
		}
	}
	
	
	private double fetchRawMaterialTotal(DataSource dataSource) throws SQLException {
	    String sql =
	        "SELECT ISNULL(SUM(ss.sweight), 0) AS TOTAL_WEIGHT " +
	        "FROM MIS.dbo.SteelStock ss " +
	        "INNER JOIN MIS.dbo.SectionSizes sz  ON ss.sect_id  = sz.sect_id " +
	        "INNER JOIN MIS.dbo.SectionGrades sg ON ss.grade_id = sg.grade_id " +
	        "LEFT JOIN  MIS.dbo.Purchase_Orders po ON ss.porder_id = po.porder_id " +
	        "LEFT JOIN  MIS.dbo.Contracts c        ON po.contract_id = c.contract_id " +
	        "WHERE ss.status = 0 " +
	        "  AND ss.instock = 1 " +
	        "  AND (ss.shop_id IS NULL OR ss.shop_id = '')";

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql);
	         ResultSet rs = pst.executeQuery()) {
	        if (rs.next()) {
	            return rs.getDouble("TOTAL_WEIGHT");
	        }
	    }
	    return 0.0;
	}


	
	
	 @GetMapping("/production")
	    public @ResponseBody Map<String, Object> getProductionData(
	            @RequestParam("factory") String factory,
	            @RequestParam("contract") String contract,
	            @RequestParam("fromDate") String fromDate,
	            @RequestParam("toDate")   String toDate) {
	 
	        logger.info("EXECUTING METHOD :: getProductionData");
	 
	        Map<String, Object> response = new HashMap<>();
	 
	        try {
	            List<Map<String, Object>> productionList;
	            Map<String, Double> total;
	 
	            if ("1".equals(factory)) {
	                productionList = fetchTop10Production(misDataSource, contract, fromDate, toDate);
	                total          = fetchProductionTotal(misDataSource, fromDate, toDate);
	 
	            } else if ("2".equals(factory)) {
	                productionList = fetchTop10Production(misDataSource2, contract, fromDate, toDate);
	                total          = fetchProductionTotal(misDataSource2, fromDate, toDate);
	 
	            } else if ("ALL".equalsIgnoreCase(factory)) {
	 
	                CompletableFuture<List<Map<String, Object>>> future1 =
	                    CompletableFuture.supplyAsync(() -> {
	                        try { return fetchAllProduction(misDataSource, contract, fromDate, toDate); }
	                        catch (SQLException e) { throw new RuntimeException("Factory 1 DB error", e); }
	                    });
	 
	                CompletableFuture<List<Map<String, Object>>> future2 =
	                    CompletableFuture.supplyAsync(() -> {
	                        try { return fetchAllProduction(misDataSource2, contract, fromDate, toDate); }
	                        catch (SQLException e) { throw new RuntimeException("Factory 2 DB error", e); }
	                    });
	 
	                CompletableFuture<Map<String, Double>> totalFuture1 =
	                    CompletableFuture.supplyAsync(() -> {
	                        try { return fetchProductionTotal(misDataSource, fromDate, toDate); }
	                        catch (SQLException e) { throw new RuntimeException("Factory 1 total error", e); }
	                    });
	 
	                CompletableFuture<Map<String, Double>> totalFuture2 =
	                    CompletableFuture.supplyAsync(() -> {
	                        try { return fetchProductionTotal(misDataSource2, fromDate, toDate); }
	                        catch (SQLException e) { throw new RuntimeException("Factory 2 total error", e); }
	                    });
	 
	                CompletableFuture.allOf(future1, future2, totalFuture1, totalFuture2).get();
	 
	                List<Map<String, Object>> list1 = future1.get();
	                List<Map<String, Object>> list2 = future2.get();
	 
	                Map<String, Double> t1 = totalFuture1.get();
	                Map<String, Double> t2 = totalFuture2.get();
	 
	                total = new HashMap<>();
	                total.put("assembled", t1.get("assembled") + t2.get("assembled"));
	                total.put("welded",    t1.get("welded")    + t2.get("welded"));
	                total.put("treated",   t1.get("treated")   + t2.get("treated"));
	 
	                Map<String, double[]> mergedMap = new HashMap<>();
	                for (Map<String, Object> row : list1) { mergeProductionRow(mergedMap, row); }
	                for (Map<String, Object> row : list2) { mergeProductionRow(mergedMap, row); }
	 
	                productionList = mergedMap.entrySet().stream()
	                    .sorted((a, b) -> Double.compare(b.getValue()[0], a.getValue()[0]))
	                    .limit(10)
	                    .map(e -> {
	                        Map<String, Object> m = new HashMap<>();
	                        m.put("label",     e.getKey());
	                        m.put("assembled", e.getValue()[0]);
	                        m.put("welded",    e.getValue()[1]);
	                        m.put("treated",   e.getValue()[2]);
	                        return m;
	                    })
	                    .collect(Collectors.toList());
	 
	            } else {
	                response.put("status",  "error");
	                response.put("message", "Invalid factory selected");
	                return response;
	            }
	 
	            response.put("Data",   productionList);
	            response.put("total",  total);
	            response.put("status", productionList.isEmpty() ? "no" : "yes");
	 
	        } catch (Exception e) {
	            logger.error("ERROR in getProductionData", e);
	            response.put("status",  "error");
	            response.put("message", "Database error occurred");
	        }
	 
	        logger.info("EXECUTED METHOD :: getProductionData");
	        return response;
	    }

	
	private void mergeProductionRow(
	        Map<String, double[]> mergedMap,
	        Map<String, Object> row) {

	    String label     = String.valueOf(row.get("label"));
	    double assembled = (Double) row.get("assembled");
	    double welded    = (Double) row.get("welded");
	    double treated   = (Double) row.get("treated");

	    mergedMap.compute(label, (k, existing) -> {
	        if (existing == null) {
	            return new double[]{ assembled, welded, treated };
	        }
	        existing[0] += assembled;
	        existing[1] += welded;
	        existing[2] += treated;
	        return existing;
	    });
	}


	// ✅ Factory 1 / 2 — TOP 10 fully in DB, split SQL for ALL vs specific contract
	private List<Map<String, Object>> fetchTop10Production(
	        DataSource dataSource,
	        String contract,
	        String fromDate,
	        String toDate) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    String contractFilter = isAll
	        ? ""
	        : "AND CAST(Contracts.contract AS VARCHAR) = ? ";

	    String sql =
	        "SELECT TOP 10 " +
	        "    Contracts.JobCode, " +
	        "    SUM(CASE WHEN PieceStage.piecestages_id = 1 THEN Pieces.weight ELSE 0 END) AS AssembledWeight, " +
	        "    SUM(CASE WHEN PieceStage.piecestages_id = 2 THEN Pieces.weight ELSE 0 END) AS WeldedWeight, " +
	        "    SUM(CASE WHEN PieceStage.piecestages_id = 3 THEN Pieces.weight ELSE 0 END) AS TreatedWeight " +
	        "FROM PieceStage " +
	        "INNER JOIN Piece_Instance " +
	        "    ON PieceStage.pieceinstance_id = Piece_Instance.instance_id " +
	        "INNER JOIN Pieces " +
	        "    ON Piece_Instance.pieces_id = Pieces.pieces_id " +
	        "INNER JOIN Loads " +
	        "    ON Piece_Instance.load_id = Loads.load_id " +
	        "INNER JOIN Contracts " +
	        "    ON Pieces.contract_id = Contracts.contract_id " +
	        "WHERE Contracts.contract_id > 0 " +
	        "  AND Piece_Instance.fab_id = 0 " +
	        "  AND TRY_CONVERT(DATETIME, PieceStage.created) " +
	        "      BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        contractFilter +
	        "GROUP BY Contracts.JobCode " +
	        "ORDER BY AssembledWeight DESC";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setString(1, fromDate + " 00:00:00");
	        pst.setString(2, toDate   + " 23:59:59");

	        if (!isAll) {
	            pst.setString(3, contract);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                result.add(mapProductionRow(rs));
	            }
	        }
	    }
	    return result;
	}


	
	private List<Map<String, Object>> fetchAllProduction(
	        DataSource dataSource,
	        String contract,
	        String fromDate,
	        String toDate) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    String contractFilter = isAll
	        ? ""
	        : "AND CAST(Contracts.contract AS VARCHAR) = ? ";

	    String sql =
	        "SELECT " +
	        "    Contracts.JobCode, " +
	        "    SUM(CASE WHEN PieceStage.piecestages_id = 1 THEN Pieces.weight ELSE 0 END) AS AssembledWeight, " +
	        "    SUM(CASE WHEN PieceStage.piecestages_id = 2 THEN Pieces.weight ELSE 0 END) AS WeldedWeight, " +
	        "    SUM(CASE WHEN PieceStage.piecestages_id = 3 THEN Pieces.weight ELSE 0 END) AS TreatedWeight " +
	        "FROM PieceStage " +
	        "INNER JOIN Piece_Instance " +
	        "    ON PieceStage.pieceinstance_id = Piece_Instance.instance_id " +
	        "INNER JOIN Pieces " +
	        "    ON Piece_Instance.pieces_id = Pieces.pieces_id " +
	        "INNER JOIN Loads " +
	        "    ON Piece_Instance.load_id = Loads.load_id " +
	        "INNER JOIN Contracts " +
	        "    ON Pieces.contract_id = Contracts.contract_id " +
	        "WHERE Contracts.contract_id > 0 " +
	        "  AND Piece_Instance.fab_id = 0 " +
	        "  AND TRY_CONVERT(DATETIME, PieceStage.created) " +
	        "      BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        contractFilter +
	        "GROUP BY Contracts.JobCode";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setString(1, fromDate + " 00:00:00");
	        pst.setString(2, toDate   + " 23:59:59");

	        if (!isAll) {
	            pst.setString(3, contract);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                result.add(mapProductionRow(rs));
	            }
	        }
	    }
	    return result;
	}



	private Map<String, Object> mapProductionRow(ResultSet rs) throws SQLException {
	    Map<String, Object> map = new HashMap<>();
	    map.put("label",     rs.getString("JobCode"));
	    map.put("assembled", rs.getDouble("AssembledWeight"));
	    map.put("welded",    rs.getDouble("WeldedWeight"));
	    map.put("treated",   rs.getDouble("TreatedWeight"));
	    return map;
	}
	
	
	private Map<String, Double> fetchProductionTotal(
	        DataSource dataSource,
	        String fromDate,
	        String toDate) throws SQLException {   // ✅ removed contract param entirely

	    String sql =
	        "SELECT " +
	        "    ISNULL(SUM(CASE WHEN PieceStage.piecestages_id = 1 THEN Pieces.weight ELSE 0 END), 0) AS TotalAssembled, " +
	        "    ISNULL(SUM(CASE WHEN PieceStage.piecestages_id = 2 THEN Pieces.weight ELSE 0 END), 0) AS TotalWelded, " +
	        "    ISNULL(SUM(CASE WHEN PieceStage.piecestages_id = 3 THEN Pieces.weight ELSE 0 END), 0) AS TotalTreated " +
	        "FROM PieceStage " +
	        "INNER JOIN Piece_Instance ON PieceStage.pieceinstance_id = Piece_Instance.instance_id " +
	        "INNER JOIN Pieces         ON Piece_Instance.pieces_id    = Pieces.pieces_id " +
	        "INNER JOIN Loads          ON Piece_Instance.load_id      = Loads.load_id " +
	        "INNER JOIN Contracts      ON Pieces.contract_id          = Contracts.contract_id " +
	        "WHERE Contracts.contract_id > 0 " +
	        "  AND Piece_Instance.fab_id = 0 " +
	        "  AND TRY_CONVERT(DATETIME, PieceStage.created) " +
	        "      BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120)";
	        // ✅ no contract filter — date range only

	    Map<String, Double> totals = new HashMap<>();
	    totals.put("assembled", 0.0);
	    totals.put("welded",    0.0);
	    totals.put("treated",   0.0);

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setString(1, fromDate + " 00:00:00");
	        pst.setString(2, toDate   + " 23:59:59");
	        // ✅ no contract param binding

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                totals.put("assembled", rs.getDouble("TotalAssembled"));
	                totals.put("welded",    rs.getDouble("TotalWelded"));
	                totals.put("treated",   rs.getDouble("TotalTreated"));
	            }
	        }
	    }
	    return totals;
	}
	

	
	
	@GetMapping("/dispatch")
	public @ResponseBody Map<String, Object> getDispatchData(
	        @RequestParam("factory") String factory,
	        @RequestParam("contract") String contract,
	        @RequestParam("fromDate") String fromDate,
	        @RequestParam("toDate") String toDate) {

	    logger.info("EXECUTING METHOD :: getDispatchData");

	    Map<String, Object> response = new HashMap<>();

	    try {
	        List<Map<String, Object>> dispatchList;
	        Map<String, Double> total;

	        if ("1".equals(factory)) {
	            dispatchList = fetchTop10Dispatch(misDataSource, contract, fromDate, toDate);
	            total        = fetchDispatchTotal(misDataSource, fromDate, toDate);

	        } else if ("2".equals(factory)) {
	            dispatchList = fetchTop10Dispatch(misDataSource2, contract, fromDate, toDate);
	            total        = fetchDispatchTotal(misDataSource2, fromDate, toDate);

	        } else if ("ALL".equalsIgnoreCase(factory)) {

	            CompletableFuture<List<Map<String, Object>>> future1 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        return fetchAllDispatch(misDataSource, contract, fromDate, toDate);
	                    } catch (SQLException e) {
	                        throw new RuntimeException("Factory 1 DB error", e);
	                    }
	                });

	            CompletableFuture<List<Map<String, Object>>> future2 =
	                CompletableFuture.supplyAsync(() -> {
	                    try {
	                        return fetchAllDispatch(misDataSource2, contract, fromDate, toDate);
	                    } catch (SQLException e) {
	                        throw new RuntimeException("Factory 2 DB error", e);
	                    }
	                });

	            CompletableFuture<Map<String, Double>> totalFuture1 =
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchDispatchTotal(misDataSource, fromDate, toDate); }
	                    catch (SQLException e) { throw new RuntimeException("Factory 1 total error", e); }
	                });

	            CompletableFuture<Map<String, Double>> totalFuture2 =
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchDispatchTotal(misDataSource2, fromDate, toDate); }
	                    catch (SQLException e) { throw new RuntimeException("Factory 2 total error", e); }
	                });

	            CompletableFuture.allOf(future1, future2, totalFuture1, totalFuture2).get();

	            List<Map<String, Object>> list1 = future1.get();
	            List<Map<String, Object>> list2 = future2.get();

	            Map<String, Double> t1 = totalFuture1.get();
	            Map<String, Double> t2 = totalFuture2.get();

	            total = new HashMap<>();
	            total.put("dispatched", t1.get("dispatched") + t2.get("dispatched"));

	            // MERGE BY JOB CODE
	            Map<String, Double> mergedMap = new HashMap<>();
	            for (Map<String, Object> row : list1) {
	                mergedMap.merge(
	                    String.valueOf(row.get("job")),
	                    (Double) row.get("weight"),
	                    Double::sum
	                );
	            }
	            for (Map<String, Object> row : list2) {
	                mergedMap.merge(
	                    String.valueOf(row.get("job")),
	                    (Double) row.get("weight"),
	                    Double::sum
	                );
	            }

	            // SORT DESC + TOP 10
	            dispatchList = mergedMap.entrySet().stream()
	                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
	                .limit(10)
	                .map(e -> {
	                    Map<String, Object> m = new HashMap<>();
	                    m.put("job", e.getKey());
	                    m.put("weight", e.getValue());
	                    return m;
	                })
	                .collect(Collectors.toList());

	        } else {
	            response.put("status",  "error");
	            response.put("message", "Invalid factory selected");
	            return response;
	        }

	        response.put("Data",   dispatchList);
	        response.put("total",  total);
	        response.put("status", dispatchList.isEmpty() ? "no" : "yes");

	    } catch (Exception e) {
	        logger.error("ERROR in getDispatchData", e);
	        response.put("status",  "error");
	        response.put("message", "Database error occurred");
	    }

	    logger.info("EXECUTED METHOD :: getDispatchData");
	    return response;
	}


	private List<Map<String, Object>> fetchTop10Dispatch(
	        DataSource dataSource,
	        String contract,
	        String fromDate,
	        String toDate) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    String contractFilter = isAll
	    	    ? ""
	    	    : "AND LTRIM(RTRIM(Contracts.contract)) = LTRIM(RTRIM(?)) ";

	    String sql =
	        "SELECT TOP 10 " +
	        "    x.JobCode, " +
	        "    x.contract, " +
	        "    SUM(x.SHIPWHT) AS TOTAL_SHIPMENT_WEIGHT " +
	        "FROM ( " +

	        "    SELECT " +
	        "        Contracts.JobCode, " +
	        "        Contracts.contract, " +
	        "        SUM(Tra_LoadAdditionalItems.weight) AS SHIPWHT " +
	        "    FROM Tra_LoadAdditionalItems " +
	        "    LEFT JOIN Tra_Loads " +
	        "        ON Tra_LoadAdditionalItems.tload_id = Tra_Loads.tload_id " +
	        "    LEFT JOIN Contracts " +
	        "        ON Tra_Loads.contract_id = Contracts.contract_id " +
	        "    LEFT JOIN JDMS.dbo.Invoice_No " +
	        "        ON Tra_Loads.tload_id = Invoice_No.load_id " +
	        "    WHERE Invoice_No.load_id NOT LIKE '%[^0-9]%' " +
	        "      AND JDMS.dbo.Invoice_No.release = 0 " +
	        "      AND JDMS.dbo.Invoice_No.status IS NULL " +
	        "      AND Invoice_No.verification_date BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        contractFilter +
	        "    GROUP BY Contracts.JobCode, Contracts.contract " +

	        "    UNION ALL " +

	        "    SELECT " +
	        "        Contracts.JobCode, " +
	        "        Contracts.contract, " +
	        "        SUM(Pieces.weight) AS SHIPWHT " +
	        "    FROM Piece_Instance " +
	        "    LEFT JOIN Tra_Loads " +
	        "        ON Piece_Instance.tload_id = Tra_Loads.tload_id " +
	        "    LEFT JOIN Pieces " +
	        "        ON Piece_Instance.pieces_id = Pieces.pieces_id " +
	        "    LEFT JOIN Contracts " +
	        "        ON Tra_Loads.contract_id = Contracts.contract_id " +
	        "    LEFT JOIN JDMS.dbo.Invoice_No " +
	        "        ON Tra_Loads.tload_id = Invoice_No.load_id " +
	        "    WHERE Invoice_No.load_id NOT LIKE '%[^0-9]%' " +
	        "      AND JDMS.dbo.Invoice_No.release = 0 " +
	        "      AND JDMS.dbo.Invoice_No.status IS NULL " +
	        "      AND Invoice_No.verification_date BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        contractFilter +
	        "    GROUP BY Contracts.JobCode, Contracts.contract " +

	        ") x " +
	        "GROUP BY x.JobCode, x.contract " +
	        "ORDER BY TOTAL_SHIPMENT_WEIGHT DESC";

	    String from = fromDate + " 00:00:00";
	    String to   = toDate   + " 23:59:59";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        if (isAll) {
	            pst.setString(1, from);
	            pst.setString(2, to);
	            pst.setString(3, from);
	            pst.setString(4, to);
	        } else {
	            pst.setString(1, from);
	            pst.setString(2, to);
	            pst.setString(3, contract);
	            pst.setString(4, from);
	            pst.setString(5, to);
	            pst.setString(6, contract);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("job",    rs.getString("JobCode"));
	                map.put("weight", rs.getDouble("TOTAL_SHIPMENT_WEIGHT"));
	                result.add(map);
	            }
	        }
	    }
	    return result;
	}


	private List<Map<String, Object>> fetchAllDispatch(
	        DataSource dataSource,
	        String contract,
	        String fromDate,
	        String toDate) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    String contractFilter = isAll
	    	    ? ""
	    	    : "AND LTRIM(RTRIM(Contracts.contract)) = LTRIM(RTRIM(?)) ";
	    String sql =
	        "SELECT " +
	        "    x.JobCode, " +
	        "    x.contract, " +
	        "    SUM(x.SHIPWHT) AS TOTAL_SHIPMENT_WEIGHT " +
	        "FROM ( " +

	        "    SELECT " +
	        "        Contracts.JobCode, " +
	        "        Contracts.contract, " +
	        "        SUM(Tra_LoadAdditionalItems.weight) AS SHIPWHT " +
	        "    FROM Tra_LoadAdditionalItems " +
	        "    LEFT JOIN Tra_Loads " +
	        "        ON Tra_LoadAdditionalItems.tload_id = Tra_Loads.tload_id " +
	        "    LEFT JOIN Contracts " +
	        "        ON Tra_Loads.contract_id = Contracts.contract_id " +
	        "    LEFT JOIN JDMS.dbo.Invoice_No " +
	        "        ON Tra_Loads.tload_id = Invoice_No.load_id " +
	        "    WHERE Invoice_No.load_id NOT LIKE '%[^0-9]%' " +
	        "      AND JDMS.dbo.Invoice_No.release = 0 " +
	        "      AND JDMS.dbo.Invoice_No.status IS NULL " +
	        "      AND Invoice_No.verification_date BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        contractFilter +
	        "    GROUP BY Contracts.JobCode, Contracts.contract " +

	        "    UNION ALL " +

	        "    SELECT " +
	        "        Contracts.JobCode, " +
	        "        Contracts.contract, " +
	        "        SUM(Pieces.weight) AS SHIPWHT " +
	        "    FROM Piece_Instance " +
	        "    LEFT JOIN Tra_Loads " +
	        "        ON Piece_Instance.tload_id = Tra_Loads.tload_id " +
	        "    LEFT JOIN Pieces " +
	        "        ON Piece_Instance.pieces_id = Pieces.pieces_id " +
	        "    LEFT JOIN Contracts " +
	        "        ON Tra_Loads.contract_id = Contracts.contract_id " +
	        "    LEFT JOIN JDMS.dbo.Invoice_No " +
	        "        ON Tra_Loads.tload_id = Invoice_No.load_id " +
	        "    WHERE Invoice_No.load_id NOT LIKE '%[^0-9]%' " +
	        "      AND JDMS.dbo.Invoice_No.release = 0 " +
	        "      AND JDMS.dbo.Invoice_No.status IS NULL " +
	        "      AND Invoice_No.verification_date BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        contractFilter +
	        "    GROUP BY Contracts.JobCode, Contracts.contract " +

	        ") x " +
	        "GROUP BY x.JobCode, x.contract";

	    String from = fromDate + " 00:00:00";
	    String to   = toDate   + " 23:59:59";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        if (isAll) {
	            pst.setString(1, from);
	            pst.setString(2, to);
	            pst.setString(3, from);
	            pst.setString(4, to);
	        } else {
	            pst.setString(1, from);
	            pst.setString(2, to);
	            pst.setString(3, contract);
	            pst.setString(4, from);
	            pst.setString(5, to);
	            pst.setString(6, contract);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("job",    rs.getString("JobCode"));
	                map.put("weight", rs.getDouble("TOTAL_SHIPMENT_WEIGHT"));
	                result.add(map);
	            }
	        }
	    }
	    return result;
	}


	private Map<String, Double> fetchDispatchTotal(
	        DataSource dataSource,
	        String fromDate,
	        String toDate) throws SQLException {

	    String sql =
	        "SELECT ISNULL(SUM(x.SHIPWHT), 0) AS TOTAL_DISPATCH_WEIGHT " +
	        "FROM ( " +

	        "    SELECT SUM(Tra_LoadAdditionalItems.weight) AS SHIPWHT " +
	        "    FROM Tra_LoadAdditionalItems " +
	        "    LEFT JOIN Tra_Loads " +
	        "        ON Tra_LoadAdditionalItems.tload_id = Tra_Loads.tload_id " +
	        "    LEFT JOIN Contracts " +
	        "        ON Tra_Loads.contract_id = Contracts.contract_id " +
	        "    LEFT JOIN JDMS.dbo.Invoice_No " +
	        "        ON Tra_Loads.tload_id = Invoice_No.load_id " +
	        "    WHERE Invoice_No.load_id NOT LIKE '%[^0-9]%' " +
	        "      AND JDMS.dbo.Invoice_No.release = 0 " +
	        "      AND JDMS.dbo.Invoice_No.status IS NULL " +
	        "      AND Invoice_No.verification_date " +
	        "          BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +

	        "    UNION ALL " +

	        "    SELECT SUM(Pieces.weight) AS SHIPWHT " +
	        "    FROM Piece_Instance " +
	        "    LEFT JOIN Tra_Loads " +
	        "        ON Piece_Instance.tload_id = Tra_Loads.tload_id " +
	        "    LEFT JOIN Pieces " +
	        "        ON Piece_Instance.pieces_id = Pieces.pieces_id " +
	        "    LEFT JOIN Contracts " +
	        "        ON Tra_Loads.contract_id = Contracts.contract_id " +
	        "    LEFT JOIN JDMS.dbo.Invoice_No " +
	        "        ON Tra_Loads.tload_id = Invoice_No.load_id " +
	        "    WHERE Invoice_No.load_id NOT LIKE '%[^0-9]%' " +
	        "      AND JDMS.dbo.Invoice_No.release = 0 " +
	        "      AND JDMS.dbo.Invoice_No.status IS NULL " +
	        "      AND Invoice_No.verification_date " +
	        "          BETWEEN CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +

	        ") x";

	    Map<String, Double> totals = new HashMap<>();
	    totals.put("dispatched", 0.0);

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setString(1, fromDate + " 00:00:00");
	        pst.setString(2, toDate   + " 23:59:59");
	        pst.setString(3, fromDate + " 00:00:00");
	        pst.setString(4, toDate   + " 23:59:59");

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                totals.put("dispatched", rs.getDouble("TOTAL_DISPATCH_WEIGHT"));
	            }
	        }
	    }
	    return totals;
	}
	
	
	@GetMapping("/invoice")
	public @ResponseBody Map<String, Object> getInvoiceData(
	        @RequestParam("factory") String factory,
	        @RequestParam("contract") String contract,
	        @RequestParam("fromDate") String fromDate,
	        @RequestParam("toDate") String toDate) {

	    logger.info("EXECUTING METHOD :: getInvoiceData");

	    Map<String, Object> response = new HashMap<>();

	    try {
	        List<Map<String, Object>> invoiceList;
	        Map<String, Double> total;                          // ← added

	        if ("1".equals(factory)) {
	            invoiceList = fetchTop10Invoice(misDataSource, contract, fromDate, toDate);
	            total       = fetchInvoiceTotal(misDataSource, fromDate, toDate);   // ← added

	        } else if ("2".equals(factory)) {
	            invoiceList = fetchTop10Invoice(misDataSource2, contract, fromDate, toDate);
	            total       = fetchInvoiceTotal(misDataSource2, fromDate, toDate);  // ← added

	        } else if ("ALL".equalsIgnoreCase(factory)) {

	            CompletableFuture<List<Map<String, Object>>> future1 =
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchAllInvoice(misDataSource, contract, fromDate, toDate); }
	                    catch (SQLException e) { throw new RuntimeException("Factory 1 DB error", e); }
	                });

	            CompletableFuture<List<Map<String, Object>>> future2 =
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchAllInvoice(misDataSource2, contract, fromDate, toDate); }
	                    catch (SQLException e) { throw new RuntimeException("Factory 2 DB error", e); }
	                });

	            CompletableFuture<Map<String, Double>> totalFuture1 =        // ← added
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchInvoiceTotal(misDataSource, fromDate, toDate); }
	                    catch (SQLException e) { throw new RuntimeException("Factory 1 total error", e); }
	                });

	            CompletableFuture<Map<String, Double>> totalFuture2 =        // ← added
	                CompletableFuture.supplyAsync(() -> {
	                    try { return fetchInvoiceTotal(misDataSource2, fromDate, toDate); }
	                    catch (SQLException e) { throw new RuntimeException("Factory 2 total error", e); }
	                });

	            CompletableFuture.allOf(future1, future2, totalFuture1, totalFuture2).get();

	            List<Map<String, Object>> list1 = future1.get();
	            List<Map<String, Object>> list2 = future2.get();

	            Map<String, Double> t1 = totalFuture1.get();                 // ← added
	            Map<String, Double> t2 = totalFuture2.get();                 // ← added

	            // SUM BOTH FACTORY TOTALS
	            total = new HashMap<>();
	            total.put("invoiceValue", t1.get("invoiceValue") + t2.get("invoiceValue"));

	            // MERGE BY JOB CODE
	            Map<String, long[]> mergedMap = new HashMap<>();
	            for (Map<String, Object> row : list1) {
	                String job = String.valueOf(row.get("job"));
	                mergedMap.computeIfAbsent(job, k -> new long[]{0, Double.doubleToLongBits(0.0)});
	                long[] arr = mergedMap.get(job);
	                arr[0] += ((Number) row.get("count")).longValue();
	                arr[1]  = Double.doubleToLongBits(
	                              Double.longBitsToDouble(arr[1]) + ((Number) row.get("value")).doubleValue());
	            }
	            for (Map<String, Object> row : list2) {
	                String job = String.valueOf(row.get("job"));
	                mergedMap.computeIfAbsent(job, k -> new long[]{0, Double.doubleToLongBits(0.0)});
	                long[] arr = mergedMap.get(job);
	                arr[0] += ((Number) row.get("count")).longValue();
	                arr[1]  = Double.doubleToLongBits(
	                              Double.longBitsToDouble(arr[1]) + ((Number) row.get("value")).doubleValue());
	            }

	            // SORT DESC by value + TOP 10
	            invoiceList = mergedMap.entrySet().stream()
	                .sorted((a, b) -> Double.compare(
	                    Double.longBitsToDouble(b.getValue()[1]),
	                    Double.longBitsToDouble(a.getValue()[1])
	                ))
	                .limit(10)
	                .map(e -> {
	                    Map<String, Object> m = new HashMap<>();
	                    m.put("job",   e.getKey());
	                    m.put("count", e.getValue()[0]);
	                    m.put("value", Double.longBitsToDouble(e.getValue()[1]));
	                    return m;
	                })
	                .collect(Collectors.toList());

	        } else {
	            response.put("status",  "error");
	            response.put("message", "Invalid factory selected");
	            return response;
	        }

	        response.put("Data",   invoiceList);
	        response.put("total",  total);                     // ← added
	        response.put("status", invoiceList.isEmpty() ? "no" : "yes");

	    } catch (Exception e) {
	        logger.error("ERROR in getInvoiceData", e);
	        response.put("status",  "error");
	        response.put("message", "Database error occurred");
	    }

	    logger.info("EXECUTED METHOD :: getInvoiceData");
	    return response;
	}
	
	
	
	

	private List<Map<String, Object>> fetchTop10Invoice(
	        DataSource dataSource,
	        String contract,
	        String fromDate,
	        String toDate) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    String contractFilter = isAll
	    	    ? "? = 'ALL' "
	    	    : "( ? = 'ALL' OR LTRIM(RTRIM(Contracts.contract)) = LTRIM(RTRIM(?)) ) ";
	    String sql =
	        "SELECT TOP 10 " +
	        "    Contracts.JobCode, " +
	        "    Contracts.contract_id, " +
	        "    Contracts.contract, " +
	        "    COUNT(Invoice_No.invoice_id) AS TOTAL_INVOICE, " +
	        "    SUM( " +
	        "        ( " +
	        "            TRY_CAST(REPLACE(Invoice_No.bal,  ',', '') AS DECIMAL(18,2)) " +
	        "          + TRY_CAST(REPLACE(Invoice_No.nadv, ',', '') AS DECIMAL(18,2)) " +
	        "        ) * (1 + te.tax_per / 100.0) " +
	        "    ) AS TOTAL_VALUE " +
	        "FROM JDMS.dbo.Invoice_No " +
	        "LEFT JOIN MIS.dbo.Contracts " +
	        "    ON Invoice_No.contract_id = Contracts.contract_id " +
	        "LEFT JOIN JDMS.dbo.I_tax_entry te " +
	        "    ON te.invoice_id = Invoice_No.invoice_id " +
	        "WHERE " +
	        "    " + contractFilter +
	        "AND Invoice_No.contract_id NOT LIKE '%[^0-9]%' " +
	        "AND Invoice_No.verification = 1 " +
	        "AND Invoice_No.status IS NULL " +
	        "AND Invoice_No.verification_date BETWEEN " +
	        "    CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        "GROUP BY " +
	        "    Contracts.JobCode, Contracts.contract_id, Contracts.contract " +
	        " ORDER BY TOTAL_VALUE DESC";

	    String from = fromDate + " 00:00:00";
	    String to   = toDate   + " 23:59:59";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        if (isAll) {
	            pst.setString(1, "ALL");   // satisfies ? = 'ALL'
	            pst.setString(2, from);
	            pst.setString(3, to);
	        } else {
	            pst.setString(1, "NOT_ALL");  // forces OR branch
	            pst.setString(2, contract);
	            pst.setString(3, from);
	            pst.setString(4, to);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("job",   rs.getString("JobCode"));
	                map.put("count", rs.getLong("TOTAL_INVOICE"));
	                map.put("value", rs.getDouble("TOTAL_VALUE"));
	                result.add(map);
	            }
	        }
	    }
	    return result;
	}


	private List<Map<String, Object>> fetchAllInvoice(
	        DataSource dataSource,
	        String contract,
	        String fromDate,
	        String toDate) throws SQLException {

	    boolean isAll = "ALL".equalsIgnoreCase(contract);

	    String contractFilter = isAll
	    	    ? "? = 'ALL' "
	    	    : "( ? = 'ALL' OR LTRIM(RTRIM(Contracts.contract)) = LTRIM(RTRIM(?)) ) ";

	    String sql =
	        "SELECT " +
	        "    Contracts.JobCode, " +
	        "    Contracts.contract_id, " +
	        "    Contracts.contract, " +
	        "    COUNT(Invoice_No.invoice_id) AS TOTAL_INVOICE, " +
	        "    SUM( " +
	        "        ( " +
	        "            TRY_CAST(REPLACE(Invoice_No.bal,  ',', '') AS DECIMAL(18,2)) " +
	        "          + TRY_CAST(REPLACE(Invoice_No.nadv, ',', '') AS DECIMAL(18,2)) " +
	        "        ) * (1 + te.tax_per / 100.0) " +
	        "    ) AS TOTAL_VALUE " +
	        "FROM JDMS.dbo.Invoice_No " +
	        "LEFT JOIN MIS.dbo.Contracts " +
	        "    ON Invoice_No.contract_id = Contracts.contract_id " +
	        "LEFT JOIN JDMS.dbo.I_tax_entry te " +
	        "    ON te.invoice_id = Invoice_No.invoice_id " +
	        "WHERE " +
	        "    " + contractFilter +
	        "AND Invoice_No.contract_id NOT LIKE '%[^0-9]%' " +
	        "AND Invoice_No.verification = 1 " +
	        "AND Invoice_No.status IS NULL " +
	        "AND Invoice_No.verification_date BETWEEN " +
	        "    CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120) " +
	        "GROUP BY " +
	        "    Contracts.JobCode, Contracts.contract_id, Contracts.contract";

	    String from = fromDate + " 00:00:00";
	    String to   = toDate   + " 23:59:59";

	    List<Map<String, Object>> result = new ArrayList<>();

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        if (isAll) {
	            pst.setString(1, "ALL");
	            pst.setString(2, from);
	            pst.setString(3, to);
	        } else {
	            pst.setString(1, "NOT_ALL");
	            pst.setString(2, contract);
	            pst.setString(3, from);
	            pst.setString(4, to);
	        }

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("job",   rs.getString("JobCode"));
	                map.put("count", rs.getLong("TOTAL_INVOICE"));
	                map.put("value", rs.getDouble("TOTAL_VALUE"));
	                result.add(map);
	            }
	        }
	    }
	    return result;
	}
	
	private Map<String, Double> fetchInvoiceTotal(
	        DataSource dataSource,
	        String fromDate,
	        String toDate) throws SQLException {

	    String sql =
	        "SELECT ISNULL(SUM( " +
	        "    ( " +
	        "        TRY_CAST(REPLACE(Invoice_No.bal,  ',', '') AS DECIMAL(18,2)) " +
	        "      + TRY_CAST(REPLACE(Invoice_No.nadv, ',', '') AS DECIMAL(18,2)) " +
	        "    ) * (1 + te.tax_per / 100.0) " +
	        "), 0) AS TOTAL_INVOICE_VALUE " +
	        "FROM JDMS.dbo.Invoice_No " +
	        "LEFT JOIN MIS.dbo.Contracts " +
	        "    ON Invoice_No.contract_id = Contracts.contract_id " +
	        "LEFT JOIN JDMS.dbo.I_tax_entry te " +
	        "    ON te.invoice_id = Invoice_No.invoice_id " +
	        "WHERE " +
	        "    Invoice_No.contract_id NOT LIKE '%[^0-9]%' " +
	        "AND Invoice_No.verification = 1 " +
	        "AND Invoice_No.status IS NULL " +
	        "AND Invoice_No.verification_date BETWEEN " +
	        "    CONVERT(DATETIME, ?, 120) AND CONVERT(DATETIME, ?, 120)";

	    Map<String, Double> totals = new HashMap<>();
	    totals.put("invoiceValue", 0.0);

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setString(1, fromDate + " 00:00:00");
	        pst.setString(2, toDate   + " 23:59:59");

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                totals.put("invoiceValue", rs.getDouble("TOTAL_INVOICE_VALUE"));
	            }
	        }
	    }
	    return totals;
	}
}
