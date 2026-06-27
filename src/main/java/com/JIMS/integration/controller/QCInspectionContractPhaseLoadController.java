package com.JIMS.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;

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

import org.apache.logging.log4j.Logger;

@CrossOrigin
@RestController
@RequestMapping("/jssl/qc/inspection")
public class QCInspectionContractPhaseLoadController {
	
	Logger logger = LogManager.getLogger(QCInspectionContractPhaseLoadController.class);

    @Autowired
    @Qualifier("bellaryDataSource")
    private DataSource bellaryDataSource;

    @Autowired
    @Qualifier("gujaratDataSource")
    private DataSource gujaratDataSource;

    @Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;
    
    @Autowired
    @Qualifier("jdmsDataSource")
    private DataSource jdmsDataSource;
    /**
     * Fetch contracts ONLY from selected factory
     * @param factory BELLARY | GUJARAT
     */
    @GetMapping("/contracts")
    public @ResponseBody Map<String, Object> fetchContracts(
            @RequestParam("factory") Integer  factory) {

        logger.info("QC Inspection :: Fetching contracts for factory :: {}", factory);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> contracts = new ArrayList<>();

        try {
            // 1️⃣ Get assigned contracts
            Set<String> assignedContracts = fetchAssignedContracts();

            // 2️⃣ Resolve factory datasource
            DataSource factoryDataSource = resolveFactoryDataSource(factory);

            // 3️⃣ Fetch contracts from selected factory
            contracts = fetchContractsFromFactory(factoryDataSource, assignedContracts);

            response.put("Data", contracts);
            response.put("factory", factory);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid factory selected", e);
            response.put("error", e.getMessage());

        } catch (Exception e) {
            logger.error("Database error while fetching contracts", e);
            response.put("error", "Database error");
        }

        logger.info("QC Inspection :: Contracts fetch completed");
        return response;
    }

    /**
     * Fetch already assigned contract IDs from JIMS
     */
    private Set<String> fetchAssignedContracts() throws SQLException {

        String sql = "SELECT contract_id FROM CONTRACT_MASTER";
        Set<String> assignedContracts = new HashSet<>();

        try (Connection con = (jimsDataSource).getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                assignedContracts.add(rs.getString("contract_id"));
            }
        }
        return assignedContracts;
    }

    /**
     * Fetch contracts from factory MIS database
     */
    private List<Map<String, String>> fetchContractsFromFactory(
            DataSource dataSource,
            Set<String> assignedContracts) throws SQLException {

        List<Map<String, String>> list = new ArrayList<>();

        String sql =
                "SELECT contract_id, " +
                "(fcontract + ' - ' + descr) AS JobCode " +
                "FROM Contracts " +
                "WHERE contract_id <> '1' " +
                "AND fcontract IS NOT NULL " +
                "ORDER BY JobCode";

        try (Connection con = (dataSource).getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                String contractId = rs.getString("contract_id");

                if (!assignedContracts.contains(contractId)) {

                    Map<String, String> map = new HashMap<>();
                    map.put("contract_id", contractId);
                    map.put("contract_name", rs.getString("JobCode"));

                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * Resolve datasource based on selected factory
     */
    private DataSource resolveFactoryDataSource(Integer factoryId) {

        switch (factoryId) {

            case 1:
                return bellaryDataSource;

            case 2:
                return gujaratDataSource;

            default:
                throw new IllegalArgumentException(
                        "Invalid factory id. Allowed values: 1 (Bellary), 2 (Gujarat)");
        }
    }
    
    @GetMapping("/phases")
    public @ResponseBody Map<String, Object> fetchPhasesByContract(
            @RequestParam("factory") Integer factoryId,
            @RequestParam("contractId") String contractId) {

        logger.info("Fetching phases for contractId :: {} | factoryId :: {}",
                contractId, factoryId);

        Map<String, Object> response = new HashMap<>();
        List<String> phases = new ArrayList<>();

        try {
            DataSource factoryDataSource = resolveFactoryDataSource(factoryId);

            String sql =
                    "SELECT DISTINCT pzone " +
                    "FROM Loads " +
                    "WHERE contract_id = ? " +
                    "AND pzone IS NOT NULL " +
                    "ORDER BY pzone";

            try (Connection con = factoryDataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, contractId);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        phases.add(rs.getString("pzone"));
                    }
                }
            }

            response.put("Data", phases);

        } catch (Exception e) {
            logger.error("Error fetching phases", e);
            response.put("error", "Database error");
        }

        return response;
    }
    
    @GetMapping("/loads")
    public @ResponseBody Map<String, Object> fetchLoadsByPhase(
            @RequestParam("factory") Integer factoryId,
            @RequestParam("contractId") String contractId,
            @RequestParam("phase") String phase) {

        logger.info("Fetching loads for contractId :: {} | phase :: {} | factoryId :: {}",
                contractId, phase, factoryId);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> loads = new ArrayList<>();

        try {
            DataSource factoryDataSource = resolveFactoryDataSource(factoryId);

            String sql =
                    "SELECT DISTINCT pload, load_id " +
                    "FROM Loads " +
                    "WHERE contract_id = ? " +
                    "AND pzone = ? " +
                    "AND pload IS NOT NULL " +
                    "ORDER BY pload";

            try (Connection con = factoryDataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, contractId);
                pst.setString(2, phase);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> map = new HashMap<>();
                        map.put("load_id", rs.getString("load_id"));
                        map.put("pload", rs.getString("pload"));
                        loads.add(map);
                    }
                }
            }

            response.put("Data", loads);

        } catch (Exception e) {
            logger.error("Error fetching loads", e);
            response.put("error", "Database error");
        }

        return response;
    }
    
    @GetMapping("/fitup-data")
    public @ResponseBody Map<String, Object> fetchFitUpInspectionData(
            @RequestParam("factory") Integer factoryId,
            @RequestParam("contractId") String contractId,
            @RequestParam("phase") String phase,
            @RequestParam("loadId") String loadId) {

        logger.info("Fetching FIT-UP inspection data | contractId={} | phase={} | loadId={} | factory={}",
                contractId, phase, loadId, factoryId);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();

        try {
            DataSource factoryDataSource = resolveFactoryDataSource(factoryId);

            String sql =
            	    "WITH BaseData AS ( " +
            	    " SELECT " +
            	    "   c.contract, " +
            	    "   c.descr AS contract_descr, " +
            	    "   l.pload, " +
            	    "   c.JobCode, " +
            	    "   l.pzone, " +
            	    "   l.loadtype, " +

            	    "   vp.mark, " +
            	    "   vp.descr AS piece_descr, " +

            	    "   SUM(p.pqty) OVER (PARTITION BY vp.mark) AS pqty, " +

            	    "   p.pieces_id, " +
            	    "   l.load_id, " +
            	    "   p.instance_id, " +
            	    "   c.contract_id, " +
            	    "   u.user_fullname AS fullname, " +
            	    "   vp.dims1, " +
            	    "   vp.tweight AS awght, " +
            	    "   sd.ass_dt " +

            	    " FROM VW_JSSLsumLoadPieceProgressForQC p " +
            	    " INNER JOIN Loads l ON l.load_id = p.load_id " +
            	    " INNER JOIN Contracts c ON c.contract_id = l.contract_id " +
            	    " INNER JOIN vw_JSSLsumPieceStageDate sd " +
            	    "   ON sd.pieces_id = p.pieces_id " +
            	    "  AND sd.pieceinstance_id = p.instance_id " +
            	    " LEFT JOIN vw_PiecesinLoad vp " +
            	    "   ON vp.pieces_id = p.pieces_id " +
            	    "  AND vp.load_id = l.load_id " +
            	    " LEFT JOIN Users u ON u.user_id = p.user_id " +

            	    " WHERE c.contract_id = ? " +
            	    "   AND l.loadtype = 0 " +
            	    "   AND l.pzone = ? " +
            	    "   AND l.load_id = ? " +
            	    "   AND p.ass <> '0' " +

            	    "   AND vp.descr NOT LIKE '%Angle%' " +
            	    "   AND vp.descr NOT LIKE '%Stay Angle%' " +
            	    "   AND vp.descr NOT LIKE '%Deck Angle%' " +
            	    "   AND vp.descr NOT LIKE '%HBrace%' " +
            	    "   AND vp.descr NOT LIKE '%VBrace%' " +
            	    "   AND vp.descr NOT LIKE '%Knee Brace%' " +
            	    "   AND vp.descr NOT LIKE '%Template%' " +
            	    "   AND vp.descr NOT LIKE '%Embedded Plate%' " +
            	    "   AND vp.descr NOT LIKE '%Fin Plate%' " +
            	    "   AND vp.descr NOT LIKE '%Batten Plate%' " +
            	    "   AND vp.descr NOT LIKE '%Splice Plate%' " +
            	    "   AND vp.descr NOT LIKE '%Loose Plate%' " +
            	    "   AND vp.descr NOT LIKE '%FLANGE BRACE%' " +
            	    "   AND vp.descr NOT LIKE '%Shim Plate%' " +
            	    "), " +

            	    "LatestFitUp AS ( " +
            	    " SELECT * FROM ( " +
            	    "   SELECT qi.*, " +
            	    "     ROW_NUMBER() OVER ( " +
            	    "       PARTITION BY qi.instance_id " +
            	    "       ORDER BY qi.created_date DESC, qi.insp_id DESC " +
            	    "     ) AS rn " +
            	    "   FROM JIMS.dbo.tab_qc_inspection qi " +
            	    " ) x WHERE rn = 1 " +
            	    "), " +

            	    "MarkWise AS ( " +
            	    " SELECT " +
            	    "   bd.*, " +

            	    "   qi.fitup_inspected_qty, " +
            	    "   qi.fitup_balanced_qty, " +
            	    "   qi.fab_code, " +
            	    "   mlt.Qc_camber as camber, " +
            	    "   qi.result, " +
            	    "   qi.status, " +
            	    "   qi.buckling, " +
            	    "   qi.length, " +
            	    "   qi.fitup_balance, " +
            	    "   qi.remarks, " +
            	    "   qi.rework, " +
            	    "   qi.revision, " +

            	    "   CASE " +
            	    "     WHEN qi.modified_by IS NULL THEN " +
            	    "       'Entered by: ' + CAST(qi.created_by AS NVARCHAR) " +
            	    "     WHEN qi.modified_by = '971203' THEN " +
            	    "       'Entered by: ' + CAST(qi.created_by AS NVARCHAR) + " +
            	    "       ' | modified by: ' + CAST(qi.name AS NVARCHAR) " +
            	    "     ELSE " +
            	    "       'Entered by: ' + CAST(qi.created_by AS NVARCHAR) + " +
            	    "       ' | modified by: ' + CAST(qi.modified_by AS NVARCHAR) " +
            	    "   END AS name, " +

            	    "   CASE " +
            	    "     WHEN qi.modified_by IS NULL THEN " +
            	    "       'Entered date: ' + CONVERT(NVARCHAR, qi.created_date, 105) + ' ' + " +
            	    "       CONVERT(NVARCHAR, qi.created_date, 108) " +
            	    "     WHEN qi.modified_by = '971203' THEN " +
            	    "       'Entered date: ' + CONVERT(NVARCHAR, qi.created_date, 105) + ' ' + " +
            	    "       CONVERT(NVARCHAR, qi.created_date, 108) + " +
            	    "       ' | modified date: ' + CONVERT(NVARCHAR, qi.date, 105) + ' ' + " +
            	    "       CONVERT(NVARCHAR, qi.date, 108) " +
            	    "     ELSE " +
            	    "       'Entered date: ' + CONVERT(NVARCHAR, qi.created_date, 105) + ' ' + " +
            	    "       CONVERT(NVARCHAR, qi.created_date, 108) + " +
            	    "       ' | modified date: ' + CONVERT(NVARCHAR, qi.modified_date, 105) + ' ' + " +
            	    "       CONVERT(NVARCHAR, qi.modified_date, 108) " +
            	    "   END AS insp_date, " +

            	    "   qi.date, " +

            	    "   ROW_NUMBER() OVER (PARTITION BY bd.mark ORDER BY bd.pieces_id) AS rn " +
            	    " FROM BaseData bd " +
            	    " LEFT JOIN LatestFitUp qi ON qi.instance_id = bd.instance_id " +
            		" LEFT JOIN MLT mlt ON mlt.pieces_id = bd.pieces_id " +
            	    ") " +

            	    "SELECT * FROM MarkWise WHERE rn = 1 ORDER BY mark";

            try (Connection con = factoryDataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, contractId);
                pst.setString(2, phase);
                pst.setString(3, loadId);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {

                        Map<String, Object> row = new HashMap<>();

                        row.put("contract", rs.getString("contract"));
                        row.put("contract_descr", rs.getString("contract_descr"));
                        row.put("jobCode", rs.getString("JobCode"));

                        row.put("phase", rs.getString("pzone"));
                        row.put("load", rs.getString("pload"));
                        row.put("load_id", rs.getString("load_id"));

                        row.put("mark", rs.getString("mark"));
                        row.put("piece_descr", rs.getString("piece_descr"));
                        row.put("qty", rs.getInt("pqty"));

                        row.put("pieces_id", rs.getString("pieces_id"));
                        row.put("instance_id", rs.getString("instance_id"));

                        row.put("fullname", rs.getString("fullname"));
                        row.put("dims1", rs.getString("dims1"));
                        row.put("awght", rs.getBigDecimal("awght"));
                        row.put("ass_dt", rs.getTimestamp("ass_dt"));

                        row.put("fitup_inspected_qty", rs.getInt("fitup_inspected_qty"));
                        row.put("fitup_balanced_qty", rs.getInt("fitup_balanced_qty"));

                        row.put("fab_code", rs.getString("fab_code"));
                        row.put("camber", rs.getString("camber"));
                        row.put("buckling", rs.getString("buckling"));
                        row.put("length", rs.getString("length"));
                        row.put("fitup_balance", rs.getString("fitup_balance"));

                        row.put("remarks", rs.getString("remarks"));
                        row.put("rework", rs.getString("rework"));
                        row.put("revision", rs.getString("revision"));
                        row.put("result", rs.getString("result"));
                        row.put("status", rs.getString("status"));

                        row.put("name", rs.getString("name"));
                        row.put("insp_date", rs.getString("insp_date"));
                        row.put("date", rs.getTimestamp("date"));

                        data.add(row);
                    }
                }
            }

            response.put("Data", data);

        } catch (Exception e) {
            logger.error("Error fetching FIT-UP inspection data", e);
            response.put("error", "Database error");
        }

        return response;
    }
    
    private int getInt(Object value) {
        if (value == null) return 0;
        return Integer.parseInt(value.toString());
    }
    
  
    @PostMapping("/create")
    public @ResponseBody Map<String, Object> createInspection(
            @RequestBody Map<String, Object> payload) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Parse factory safely
            Integer factoryId = getInt(payload.get("factory_id"));
            DataSource ds = resolveFactoryDataSource(factoryId);

            String contractId = (String) payload.get("contract_id");
            String phase      = (String) payload.get("phase");
            String loadId     = (String) payload.get("load_id");
            String mark       = (String) payload.get("mark");

            // Fetch correct piece and instance (UNCHANGED)
            Map<String, String> pieceData =
                    fetchPieceAndInstance(ds, contractId, phase, loadId, mark);

            if (pieceData == null) {
                response.put("error", "No piece found for this mark");
                return response;
            }

            String piecesId   = pieceData.get("pieces_id");
            String instanceId = pieceData.get("instance_id");

            int fitupInspectedQty = getInt(payload.get("fitup_inspected_qty"));
            int fitupBalancedQty  = getInt(payload.get("fitup_balanced_qty"));

            Integer existingInspId = null;

            // 🔍 CHECK EXISTING INSPECTION (LATEST FOR INSTANCE)
            String checkSql =
                    "SELECT TOP 1 insp_id " +
                    "FROM JIMS.dbo.tab_qc_inspection " +
                    "WHERE instance_id = ? " +
                    "ORDER BY ISNULL(modified_date, created_date) DESC";

            try (Connection con = ds.getConnection();
                 PreparedStatement pst = con.prepareStatement(checkSql)) {

                pst.setString(1, instanceId);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        existingInspId = rs.getInt("insp_id");
                    }
                }
            }

            // =====================================================
            // 🔁 UPDATE IF EXISTS
            // =====================================================
            if (existingInspId != null) {

                String updateSql =
                        "UPDATE JIMS.dbo.tab_qc_inspection SET " +
                        "fitup_inspected_qty = ?, " +
                        "fitup_balanced_qty  = ?, " +
                        "fab_code = ?, camber = ?, buckling = ?, length = ?, fitup_balance = ?, " +
                        "remarks = ?, rework = ?, revision = ?, result = ?, status = ?, " +
                        "modified_by = ?, modified_date = ? " +
                        "WHERE insp_id = ?";

                try (Connection con = ds.getConnection();
                     PreparedStatement pst = con.prepareStatement(updateSql)) {

                    pst.setInt(1, fitupInspectedQty);
                    pst.setInt(2, fitupBalancedQty);

                    pst.setString(3, (String) payload.get("fab_code"));
                    pst.setString(4, (String) payload.get("camber"));
                    pst.setString(5, (String) payload.get("buckling"));
                    pst.setString(6, (String) payload.get("length"));
                    pst.setString(7, (String) payload.get("fitup_balance"));
                    pst.setString(8, (String) payload.get("remarks"));
                    pst.setString(9, (String) payload.get("rework"));
                    pst.setString(10, (String) payload.get("revision"));
                    pst.setString(11, (String) payload.get("result"));
                    pst.setString(12, (String) payload.get("status"));

                    pst.setString(13, (String) payload.getOrDefault("created_by", "system"));
                    pst.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
                    pst.setInt(15, existingInspId);

                    int rows = pst.executeUpdate();
                    response.put("success", rows > 0);
                    response.put("message",
                            rows > 0 ? "Inspection record updated" : "Failed to update record");
                    response.put("operation", "update"); 
                }

            }
            // =====================================================
            // ➕ INSERT IF NOT EXISTS
            // =====================================================
            else {

                String insertSql =
                        "INSERT INTO JIMS.dbo.tab_qc_inspection (" +
                        "contract_id, pieces_id, instance_id, " +
                        "fitup_inspected_qty, fitup_balanced_qty, " +
                        "fab_code, camber, buckling, length, fitup_balance, " +
                        "remarks, rework, revision, result, status, " +
                        "created_by, created_date, factory_id" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection con = ds.getConnection();
                     PreparedStatement pst = con.prepareStatement(insertSql)) {

                    pst.setString(1, contractId);
                    pst.setString(2, piecesId);
                    pst.setString(3, instanceId);

                    pst.setInt(4, fitupInspectedQty);
                    pst.setInt(5, fitupBalancedQty);

                    pst.setString(6, (String) payload.get("fab_code"));
                    pst.setString(7, (String) payload.get("camber"));
                    pst.setString(8, (String) payload.get("buckling"));
                    pst.setString(9, (String) payload.get("length"));
                    pst.setString(10, (String) payload.get("fitup_balance"));
                    pst.setString(11, (String) payload.get("remarks"));
                    pst.setString(12, (String) payload.get("rework"));
                    pst.setString(13, (String) payload.get("revision"));
                    pst.setString(14, (String) payload.get("result"));
                    pst.setString(15, (String) payload.get("status"));

                    pst.setString(16, (String) payload.getOrDefault("created_by", "system"));
                    pst.setTimestamp(17, new Timestamp(System.currentTimeMillis()));
                    pst.setInt(18, factoryId);

                    int rows = pst.executeUpdate();
                    response.put("success", rows > 0);
                    response.put("message",
                            rows > 0 ? "Inspection record created" : "Failed to create record");
                    response.put("operation", "insert");
                }
            }

        } catch (Exception e) {
            logger.error("Error creating QC Inspection record", e);
            response.put("error", e.getMessage());
        }

        return response;
    }
    
    private Map<String, String> fetchPieceAndInstance(
            DataSource factoryDataSource,
            String contractId,
            String phase,
            String loadId,
            String mark) throws SQLException {

        String sql = "SELECT TOP 1 p.pieces_id, p.instance_id " +
                     "FROM VW_JSSLsumLoadPieceProgressForQC p " +
                     "INNER JOIN Loads l ON l.load_id = p.load_id " +
                     "INNER JOIN vw_PiecesinLoad vp ON vp.pieces_id = p.pieces_id AND vp.load_id = l.load_id " +
                     "WHERE l.contract_id = ? " +
                     "AND l.pzone = ? " +
                     "AND l.load_id = ? " +
                     "AND vp.mark = ? " +
                     "AND p.ass <> '0' " +
                     "ORDER BY p.pieces_id";

        try (Connection con = factoryDataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, contractId);
            pst.setString(2, phase);
            pst.setString(3, loadId);
            pst.setString(4, mark);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("pieces_id", rs.getString("pieces_id"));
                    map.put("instance_id", rs.getString("instance_id"));
                    return map;
                } else {
                    return null; // no piece found
                }
            }
        }
    }
    
    @GetMapping("/weld-data")
    public @ResponseBody Map<String, Object> fetchWeldInspectionData(
            @RequestParam("factory") Integer factoryId,
            @RequestParam("contractId") String contractId,
            @RequestParam("phase") String phase,
            @RequestParam("loadId") String loadId) {

        logger.info("Fetching WELD inspection data | contractId={} | phase={} | loadId={} | factory={}",
                contractId, phase, loadId, factoryId);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();

        try {
            DataSource factoryDataSource = resolveFactoryDataSource(factoryId);

            String sql =
            	    "WITH LatestFitUpCompleted AS ( " +
            	    "    SELECT * FROM ( " +
            	    "        SELECT qi.*, " +
            	    "               ROW_NUMBER() OVER (PARTITION BY qi.instance_id ORDER BY qi.created_date DESC, qi.insp_id DESC) AS rn " +
            	    "        FROM JIMS.dbo.tab_qc_inspection qi " +
            	    "        WHERE qi.status = 'Completed' " +
            	    "    ) t WHERE rn = 1 " +
            	    "), " +

            	    "BaseData AS ( " +
            	    "    SELECT " +
            	    "        c.contract, " +
            	    "        c.descr AS contract_descr, " +
            	    "        l.pload, " +
            	    "        c.JobCode, " +
            	    "        l.pzone, " +
            	    "        l.loadtype, " +
            	    "        vp.mark, " +
            	    "        vp.descr AS piece_descr, " +
            	    "        SUM(p.pqty) OVER (PARTITION BY vp.mark) AS pqty, " +
            	    "        p.pieces_id, " +
            	    "        l.load_id, " +
            	    "        p.instance_id, " +
            	    "        c.contract_id, " +
            	    "        u.user_fullname AS fullname, " +
            	    "        vp.dims1, " +
            	    "        vp.tweight AS awght, " +
            	    "        sd.ass_dt, " +
            	    "        CASE " +
            	    "            WHEN ISNUMERIC(qi.remarks) = 1 THEN " +
            	    "                CASE qi.remarks " +
            	    "                    WHEN '0' THEN 'None' " +
            	    "                    WHEN '1' THEN 'Shorten length' " +
            	    "                    WHEN '2' THEN 'Hole out' " +
            	    "                    WHEN '3' THEN 'After UT fit up' " +
            	    "                    ELSE '' " +
            	    "                END " +
            	    "            ELSE qi.remarks " +
            	    "        END AS remarks " +
            	    "    FROM dbo.VW_JSSLsumLoadPieceProgressForQC p " +
            	    "    INNER JOIN dbo.Loads l ON l.load_id = p.load_id " +
            	    "    INNER JOIN dbo.Contracts c ON c.contract_id = l.contract_id " +
            	    "    INNER JOIN dbo.vw_JSSLsumPieceStageDate sd ON sd.pieces_id = p.pieces_id AND sd.pieceinstance_id = p.instance_id " +
            	    "    LEFT JOIN dbo.vw_PiecesinLoad vp ON vp.pieces_id = p.pieces_id AND vp.load_id = l.load_id " +
            	    "    LEFT JOIN LatestFitUpCompleted qi ON qi.instance_id = p.instance_id " +
            	    "    LEFT JOIN dbo.Users u ON u.user_id = p.user_id " +
            	    "    WHERE c.contract_id = ? " +
            	    "      AND l.loadtype = 0 " +
            	    "      AND l.pzone = ? " +
            	    "      AND l.load_id = ? " +
            	    "      AND p.ass <> '0' " +
            	    "      AND vp.descr NOT LIKE '%Angle%' " +
            	    "      AND vp.descr NOT LIKE '%Stay Angle%' " +
            	    "      AND vp.descr NOT LIKE '%Deck Angle%' " +
            	    "      AND vp.descr NOT LIKE '%HBrace%' " +
            	    "      AND vp.descr NOT LIKE '%VBrace%' " +
            	    "      AND vp.descr NOT LIKE '%Knee Brace%' " +
            	    "      AND vp.descr NOT LIKE '%Template%' " +
            	    "      AND vp.descr NOT LIKE '%Embedded Plate%' " +
            	    "      AND vp.descr NOT LIKE '%Fin Plate%' " +
            	    "      AND vp.descr NOT LIKE '%Batten Plate%' " +
            	    "      AND vp.descr NOT LIKE '%Splice Plate%' " +
            	    "      AND vp.descr NOT LIKE '%Loose Plate%' " +
            	    "      AND vp.descr NOT LIKE '%FLANGE BRACE%' " +
            	    "      AND vp.descr NOT LIKE '%Shim Plate%' " +
            	    "), " +

            	    "LatestWeld AS ( " +
            	    "    SELECT * FROM ( " +
            	    "        SELECT w.*, " +
            	    "               ROW_NUMBER() OVER (PARTITION BY w.instance_id ORDER BY w.created_date DESC, w.insp_id DESC) AS rn " +
            	    "        FROM JIMS.dbo.tab_qc_weld_inspection w " +
            	    "    ) t WHERE rn = 1 " +
            	    "), " +

            	    "MarkWise AS ( " +
            	    "    SELECT " +
            	    "        bd.*, " +
            	    "        lw.welder_ID, " +
            	    "        lw.weld_inspected_qty, " +
            	    "        lw.weld_balanced_qty, " +
            	    "        lw.weld_buckling, " +
            	    "        mlt.Qc_camber as weld_camber, " +
            	    "        lw.sweep, " +
            	    "        lw.result AS weld_result, " +
            	    "        lw.status AS weld_status, " +
            	    "        CASE " +
            	    "            WHEN lw.modified_by IS NULL THEN 'Entered by: ' + CAST(lw.created_by AS NVARCHAR) " +
            	    "            WHEN lw.modified_by = '971203' THEN 'Entered by: ' + CAST(lw.created_by AS NVARCHAR) + ' | modified by: ' + CAST(lw.weld_inspected_by AS NVARCHAR) " +
            	    "            ELSE 'Entered by: ' + CAST(lw.created_by AS NVARCHAR) + ' | modified by: ' + CAST(lw.modified_by AS NVARCHAR) " +
            	    "        END AS weld_inspected_by, " +
            	    "        CASE " +
            	    "            WHEN lw.modified_by IS NULL THEN CONVERT(NVARCHAR, lw.created_date, 105) + ' ' + CONVERT(NVARCHAR, lw.created_date, 108) " +
            	    "            ELSE CONVERT(NVARCHAR, lw.created_date, 105) + ' ' + CONVERT(NVARCHAR, lw.created_date, 108) + ' | modified: ' + CONVERT(NVARCHAR, ISNULL(lw.modified_date, lw.weld_date), 105) + ' ' + CONVERT(NVARCHAR, ISNULL(lw.modified_date, lw.weld_date), 108) " +
            	    "        END AS weld_date, " +
            	    "        CASE " +
            	    "            WHEN lw.insp_id IS NOT NULL THEN " +
            	    "                CASE " +
            	    "                    WHEN ISNUMERIC(lw.ut) = 1 THEN " +
            	    "                        CASE lw.ut WHEN 0 THEN 'None' WHEN 1 THEN 'OK' WHEN 2 THEN 'NA' ELSE '' END " +
            	    "                    ELSE lw.ut " +
            	    "                END " +
            	    "        END AS ut, " +
            	    "        CASE " +
            	    "            WHEN lw.insp_id IS NOT NULL THEN " +
            	    "                CASE " +
            	    "                    WHEN ISNUMERIC(lw.weld_visual) = 1 AND lw.weld_visual = 1 THEN 'Satisfactory' ELSE lw.weld_visual " +
            	    "                END " +
            	    "        END AS weld_visual, " +
            	    "        CASE " +
            	    "            WHEN lw.insp_id IS NOT NULL THEN " +
            	    "                CASE " +
            	    "                    WHEN ISNUMERIC(lw.rework) = 1 THEN " +
            	    "                        CASE lw.rework WHEN 0 THEN 'None' WHEN 1 THEN 'Rework req' WHEN 2 THEN 'Rework clrd' ELSE '' END " +
            	    "                    ELSE lw.rework " +
            	    "                END " +
            	    "        END AS rework, " +
            	    "        ROW_NUMBER() OVER (PARTITION BY bd.mark ORDER BY bd.pieces_id) AS rn " +
            	    "    FROM BaseData bd " +
            	    "    LEFT JOIN LatestWeld lw ON lw.instance_id = bd.instance_id " +
            		"    LEFT JOIN MLT mlt ON mlt.pieces_id = bd.pieces_id " +
            	    ") " +

            	    "SELECT * FROM MarkWise WHERE rn = 1 ORDER BY mark";

            try (Connection con = factoryDataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, contractId);
                pst.setString(2, phase);
                pst.setString(3, loadId);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {

                        Map<String, Object> row = new HashMap<>();

                        row.put("contract", rs.getString("contract"));
                        row.put("contract_descr", rs.getString("contract_descr"));
                        row.put("jobCode", rs.getString("JobCode"));
                        row.put("phase", rs.getString("pzone"));
                        row.put("load", rs.getString("pload"));
                        row.put("load_id", rs.getString("load_id"));

                        row.put("mark", rs.getString("mark"));
                        row.put("piece_descr", rs.getString("piece_descr"));
                        row.put("qty", rs.getInt("pqty"));

                        row.put("pieces_id", rs.getString("pieces_id"));
                        row.put("instance_id", rs.getString("instance_id"));

                        row.put("fullname", rs.getString("fullname"));
                        row.put("dims1", rs.getString("dims1"));
                        row.put("awght", rs.getBigDecimal("awght"));
                        row.put("ass_dt", rs.getTimestamp("ass_dt"));

                        row.put("remarks", rs.getString("remarks"));

                        row.put("welder_ID", rs.getString("welder_ID"));
                        row.put("weld_inspected_qty", rs.getString("weld_inspected_qty"));
                        row.put("weld_balanced_qty", rs.getString("weld_balanced_qty"));
                        row.put("weld_buckling", rs.getString("weld_buckling"));
                        row.put("weld_camber", rs.getString("weld_camber"));
                        row.put("sweep", rs.getString("sweep"));
                        row.put("result", rs.getString("weld_result"));
                        row.put("status", rs.getString("weld_status"));

                        row.put("ut", rs.getString("ut"));
                        row.put("weld_visual", rs.getString("weld_visual"));
                        row.put("rework", rs.getString("rework"));

                        row.put("weld_inspected_by", rs.getString("weld_inspected_by"));
                        row.put("weld_date", rs.getString("weld_date"));

                        data.add(row);
                    }
                }
            }

            response.put("Data", data);

        } catch (Exception e) {
            logger.error("Error fetching WELD inspection data", e);
            response.put("error", "Database error");
        }

        return response;
    }
    
    
    @PostMapping("/weld/create")
    public @ResponseBody Map<String, Object> createWeldInspection(
            @RequestBody Map<String, Object> payload) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer factoryId = safeParseInt(payload.get("factory_id"));
            DataSource ds = resolveFactoryDataSource(factoryId);

            String contractId = asString(payload.get("contract_id"));
            String phase      = asString(payload.get("phase"));
            String loadId     = asString(payload.get("load_id"));
            String mark       = asString(payload.get("mark"));

            if (contractId.isEmpty() || phase.isEmpty() || loadId.isEmpty() || mark.isEmpty()) {
                response.put("error", "Contract, Phase, Load and Mark are required");
                return response;
            }

            Map<String, String> pieceData = fetchPieceAndInstance(ds, contractId, phase, loadId, mark);
            if (pieceData == null) {
                response.put("error", "No piece found for selected mark");
                return response;
            }

            String piecesId   = pieceData.get("pieces_id");
            String instanceId = pieceData.get("instance_id");

            // Validate Fit-Up completion (existing logic)
            String fitupCheckSql =
                    "SELECT TOP 1 status, fitup_balanced_qty " +
                    "FROM JIMS.dbo.tab_qc_inspection " +
                    "WHERE instance_id = ? " +
                    "ORDER BY ISNULL(modified_date, created_date) DESC";

            try (Connection con = ds.getConnection();
                 PreparedStatement pst = con.prepareStatement(fitupCheckSql)) {

                pst.setString(1, instanceId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        response.put("error", "Please complete Fit-Up entry before Welding");
                        return response;
                    }

                    String fitupStatus = rs.getString("status");
                    int balanceQty = rs.getInt("fitup_balanced_qty");

                    if (!"Completed".equalsIgnoreCase(fitupStatus) || balanceQty > 0) {
                        response.put("error", "Fit-Up is not completed. Welding is not allowed.");
                        return response;
                    }
                }
            }

            // Welding quantities
            int weldInspectedQty = safeParseInt(payload.get("weld_inspected_qty"));
            int weldBalancedQty  = safeParseInt(payload.get("weld_balanced_qty"));

            if (weldInspectedQty <= 0) {
                response.put("error", "Weld inspected quantity must be greater than 0");
                return response;
            }

            Integer existingWeldId = null;

            // ✅ Check for existing welding inspection
            String checkSql = "SELECT TOP 1 insp_id FROM JIMS.dbo.tab_qc_weld_inspection " +
                              "WHERE instance_id = ? ORDER BY ISNULL(modified_date, created_date) DESC";

            try (Connection con = ds.getConnection();
                 PreparedStatement pst = con.prepareStatement(checkSql)) {

                pst.setString(1, instanceId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        existingWeldId = rs.getInt("insp_id");
                    }
                }
            }

            // =====================================================
            // 🔁 UPDATE IF EXISTS
            // =====================================================
            if (existingWeldId != null) {

                String updateSql =
                        "UPDATE JIMS.dbo.tab_qc_weld_inspection SET " +
                        "weld_inspected_qty = ?, weld_balanced_qty = ?, " +
                        "welder_ID = ?, weld_camber = ?, weld_buckling = ?, sweep = ?, ut = ?, weld_visual = ?, " +
                        "remarks = ?, rework = ?, result = ?, status = ?, " +
                        "modified_by = ?, modified_date = ? " +
                        "WHERE insp_id = ?";

                try (Connection con = ds.getConnection();
                     PreparedStatement pst = con.prepareStatement(updateSql)) {

                    pst.setInt(1, weldInspectedQty);
                    pst.setInt(2, weldBalancedQty);

                    pst.setString(3, asString(payload.get("welder_ID")));
                    pst.setString(4, asString(payload.get("weld_camber")));
                    pst.setString(5, asString(payload.get("weld_buckling")));
                    pst.setString(6, asString(payload.get("sweep")));
                    pst.setString(7, asString(payload.get("ut")));
                    pst.setString(8, asString(payload.get("weld_visual")));
                    pst.setString(9, asString(payload.get("remarks")));
                    pst.setString(10, asString(payload.get("rework")));
                    pst.setString(11, asString(payload.get("result")));
                    pst.setString(12, asString(payload.get("status")));
                    pst.setString(13, asString(payload.getOrDefault("created_by", "system")));
                    pst.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
                    pst.setInt(15, existingWeldId);

                    int rows = pst.executeUpdate();
                    response.put("success", rows > 0);
                    response.put("message",
                            rows > 0 ? "Welding inspection updated successfully" : "Failed to update welding inspection");
                    response.put("operation", "update");
                }

            } 
            // =====================================================
            // ➕ INSERT IF NOT EXISTS
            // =====================================================
            else {

                String insertSql =
                        "INSERT INTO JIMS.dbo.tab_qc_weld_inspection (" +
                                "contract_id, pieces_id, instance_id, " +
                                "weld_inspected_qty, weld_balanced_qty, " +
                                "welder_ID, weld_camber, weld_buckling, sweep, ut, weld_visual, " +
                                "remarks, rework, result, status, " +
                                "created_by, created_date, factory_id" +
                                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection con = ds.getConnection();
                     PreparedStatement pst = con.prepareStatement(insertSql)) {

                    pst.setString(1, contractId);
                    pst.setString(2, piecesId);
                    pst.setString(3, instanceId);
                    pst.setInt(4, weldInspectedQty);
                    pst.setInt(5, weldBalancedQty);

                    pst.setString(6, asString(payload.get("welder_ID")));
                    pst.setString(7, asString(payload.get("weld_camber")));
                    pst.setString(8, asString(payload.get("weld_buckling")));
                    pst.setString(9, asString(payload.get("sweep")));
                    pst.setString(10, asString(payload.get("ut")));
                    pst.setString(11, asString(payload.get("weld_visual")));
                    pst.setString(12, asString(payload.get("remarks")));
                    pst.setString(13, asString(payload.get("rework")));
                    pst.setString(14, asString(payload.get("result")));
                    pst.setString(15, asString(payload.get("status")));
                    pst.setString(16, asString(payload.getOrDefault("created_by", "system")));
                    pst.setTimestamp(17, new Timestamp(System.currentTimeMillis()));
                    pst.setInt(18, factoryId);

                    int rows = pst.executeUpdate();
                    response.put("success", rows > 0);
                    response.put("message",
                            rows > 0 ? "Welding inspection created successfully" : "Failed to create welding inspection");
                    response.put("operation", "insert");
                }
            }

        } catch (Exception e) {
            logger.error("Error creating Welding inspection", e);
            response.put("error", e.getMessage());
        }

        return response;
    }

    // ✅ Utility functions
    private int safeParseInt(Object value) {
        if (value == null) return 0;
        String str = value.toString().trim();
        if (str.isEmpty()) return 0;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String asString(Object value) {
        if (value == null) return null;
        String str = value.toString().trim();
        return str.isEmpty() ? null : str;
    }
    
    @PostMapping("/validate-fitup-on-mark")
    @ResponseBody
    public Map<String, Object> validateFitUpOnMark(
            @RequestBody Map<String, Object> payload) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer factoryId = safeParseInt(payload.get("factory_id"));
            DataSource ds = resolveFactoryDataSource(factoryId);

            String contractId = asString(payload.get("contract_id"));
            String phase      = asString(payload.get("phase"));
            String loadId     = asString(payload.get("load_id"));
            String mark       = asString(payload.get("mark"));

            // 🔹 Resolve piece + instance (YOUR METHOD)
            Map<String, String> pieceData =
                    fetchPieceAndInstance(ds, contractId, phase, loadId, mark);

            if (pieceData == null) {
                response.put("error", "Please complete Fit-Up entry before Welding");
                return response;
            }

            String instanceId = pieceData.get("instance_id");

            // 🔹 Check Fit-Up status
            String sql =
                "SELECT TOP 1 status, ISNULL(fitup_balanced_qty,0) AS bal " +
                "FROM JIMS.dbo.tab_qc_inspection " +
                "WHERE instance_id = ? " +
                "ORDER BY ISNULL(modified_date, created_date) DESC";

            try (Connection con = ds.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, instanceId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()
                    || !"Completed".equalsIgnoreCase(rs.getString("status"))
                    || rs.getInt("bal") > 0) {

                    response.put("error",
                            "Please complete Fit-Up entry before Welding");
                    return response;
                }
            }

            response.put("allowed", true);

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }
    
    @GetMapping("/loose-items-data")
    public @ResponseBody Map<String, Object> fetchLooseItemInspectionData(
            @RequestParam("factory") Integer factoryId,
            @RequestParam("contractId") String contractId,
            @RequestParam("phase") String phase,
            @RequestParam("loadId") String loadId) {

        logger.info("Fetching LOOSE ITEM inspection data | contractId={} | phase={} | loadId={} | factory={}",
                contractId, phase, loadId, factoryId);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();

        try {
            DataSource factoryDataSource = resolveFactoryDataSource(factoryId);

            String sql =
                "SELECT DISTINCT " +
                " Contracts.contract, " +
                " Contracts.descr, " +
                " Loads.pload, " +
                " Contracts.JobCode, " +
                " Loads.pzone, " +
                " Loads.loadtype, " +
                " vw_PiecesinLoad.mark, " +
                " dbo.VW_JSSL_FittingmarkforQA.fmark, " +
                " COUNT(VW_JSSLsumLoadPieceProgressForQC.pqty) AS pqty, " +
                " MAX(VW_JSSLsumLoadPieceProgressForQC.pieces_id) AS pieces_id, " +
                " MAX(Loads.load_id) AS load_id, " +
                " MAX(VW_JSSLsumLoadPieceProgressForQC.instance_id) AS instance_id, " +
                " Contracts.contract_id, " +
                " vw_PiecesinLoad.descr AS description, " +
                " vw_PiecesinLoad.ssize, " +
                " dbo.Users.user_fullname, " +
                " vw_PiecesinLoad.dims1, " +
                " vw_PiecesinLoad.tweight AS awght, " +
                " dbo.vw_JSSLsumPieceStageDate.ass_dt, " +
                " JIMS.dbo.tab_qc_non_weld_inspection.inspected_qty, " +
                " JIMS.dbo.tab_qc_non_weld_inspection.balanced_qty, " +
                " JIMS.dbo.tab_qc_non_weld_inspection.result, " +
                " JIMS.dbo.tab_qc_non_weld_inspection.status, " +

                " CASE " +
                "   WHEN JIMS.dbo.tab_qc_non_weld_inspection.modified_by IS NULL THEN " +
                "     'Entered by: ' + CAST(JIMS.dbo.tab_qc_non_weld_inspection.created_by AS NVARCHAR) " +
                "   WHEN JIMS.dbo.tab_qc_non_weld_inspection.modified_by = '971203' THEN " +
                "     'Entered by: ' + CAST(JIMS.dbo.tab_qc_non_weld_inspection.created_by AS NVARCHAR) " +
                "     + ' | modified by: ' + CAST(JIMS.dbo.tab_qc_non_weld_inspection.weld_inspected_by AS NVARCHAR) " +
                "   ELSE " +
                "     'Entered by: ' + CAST(JIMS.dbo.tab_qc_non_weld_inspection.created_by AS NVARCHAR) " +
                "     + ' | modified by: ' + CAST(JIMS.dbo.tab_qc_non_weld_inspection.modified_by AS NVARCHAR) " +
                " END AS weld_inspected_by, " +

                " CASE " +
                "   WHEN JIMS.dbo.tab_qc_non_weld_inspection.modified_by IS NULL THEN " +
                "     'Entered date: ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.created_date, 105) " +
                "     + ' ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.created_date, 108) " +
                "   WHEN JIMS.dbo.tab_qc_non_weld_inspection.modified_by = '971203' THEN " +
                "     'Entered date: ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.created_date, 105) " +
                "     + ' ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.created_date, 108) " +
                "     + ' | modified date: ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.weld_date, 105) " +
                "     + ' ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.weld_date, 108) " +
                "   ELSE " +
                "     'Entered date: ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.created_date, 105) " +
                "     + ' ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.created_date, 108) " +
                "     + ' | modified date: ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.modified_date, 105) " +
                "     + ' ' + CONVERT(NVARCHAR, JIMS.dbo.tab_qc_non_weld_inspection.modified_date, 108) " +
                " END AS weld_date, " +

                " CASE " +
                "   WHEN ISNUMERIC(tab_qc_non_weld_inspection.dimensional_insp) = 1 THEN " +
                "     CASE " +
                "       WHEN tab_qc_non_weld_inspection.dimensional_insp = 0 THEN 'None' " +
                "       WHEN tab_qc_non_weld_inspection.dimensional_insp = 1 THEN 'OK' " +
                "       WHEN tab_qc_non_weld_inspection.dimensional_insp = 2 THEN 'NA' " +
                "       ELSE '' " +
                "     END " +
                "     WHEN tab_qc_non_weld_inspection.dimensional_insp IN ('OK','NA','None') THEN " +
                "     tab_qc_non_weld_inspection.dimensional_insp " +
                "     WHEN tab_qc_non_weld_inspection.dimensional_insp = 'Select' THEN 'Select' " +
                "     ELSE '' " +
                " END AS dimensional_insp, " +

                " CASE " +
                "   WHEN ISNUMERIC(tab_qc_non_weld_inspection.weld_visual) = 1 THEN " +
                "     CASE WHEN tab_qc_non_weld_inspection.weld_visual = 1 THEN 'Satisfactory' ELSE '' END " +
                "   WHEN tab_qc_non_weld_inspection.weld_visual = 'Satisfactory' THEN " +
                "   tab_qc_non_weld_inspection.weld_visual " +
                "   WHEN tab_qc_non_weld_inspection.weld_visual = 'Select' THEN 'Select' " +
                "   ELSE '' " +
                " END AS weld_visual " +

                "FROM dbo.VW_JSSLsumLoadPieceProgressForQC " +
                "INNER JOIN dbo.Loads ON VW_JSSLsumLoadPieceProgressForQC.load_id = Loads.load_id " +
                "INNER JOIN dbo.Contracts ON Loads.contract_id = Contracts.contract_id " +
                "INNER JOIN dbo.vw_JSSLsumPieceStageDate " +
                " ON VW_JSSLsumLoadPieceProgressForQC.pieces_id = vw_JSSLsumPieceStageDate.pieces_id " +
                " AND VW_JSSLsumLoadPieceProgressForQC.instance_id = vw_JSSLsumPieceStageDate.pieceinstance_id " +
                "LEFT JOIN dbo.VW_JSSL_FittingmarkforQA " +
                " ON VW_JSSLsumLoadPieceProgressForQC.pieces_id = VW_JSSL_FittingmarkforQA.pieces_id " +
                "LEFT JOIN dbo.vw_PiecesinLoad " +
                " ON VW_JSSLsumLoadPieceProgressForQC.pieces_id = vw_PiecesinLoad.pieces_id " +
                " AND Loads.load_id = vw_PiecesinLoad.load_id " +
                "FULL OUTER JOIN dbo.Users " +
                " ON VW_JSSLsumLoadPieceProgressForQC.user_id = Users.user_id " +
                "LEFT JOIN JIMS.dbo.tab_qc_non_weld_inspection " +
                " ON VW_JSSLsumLoadPieceProgressForQC.load_id = tab_qc_non_weld_inspection.load_id " +
                " AND VW_JSSLsumLoadPieceProgressForQC.pieces_id = tab_qc_non_weld_inspection.pieces_id " +

                "WHERE Contracts.contract_id = ? " +
                " AND Loads.loadtype = 0 " +
                " AND Loads.pzone = ? " +
                " AND Loads.load_id = ? " +
                " AND VW_JSSLsumLoadPieceProgressForQC.ass <> '0' " +
                " AND vw_PiecesinLoad.descr NOT LIKE '%BEAM%' " +

                "GROUP BY Contracts.contract, Contracts.descr, Loads.pload, Contracts.JobCode, " +
                " Loads.pzone, Loads.loadtype, vw_PiecesinLoad.mark, VW_JSSL_FittingmarkforQA.fmark, " +
                " Contracts.contract_id, vw_PiecesinLoad.descr, vw_PiecesinLoad.ssize, Users.user_fullname, " +
                " vw_PiecesinLoad.dims1, vw_PiecesinLoad.tweight, vw_JSSLsumPieceStageDate.ass_dt, " +
                " tab_qc_non_weld_inspection.result, tab_qc_non_weld_inspection.status, " +
                " tab_qc_non_weld_inspection.modified_by, tab_qc_non_weld_inspection.created_by, " +
                " tab_qc_non_weld_inspection.weld_inspected_by, tab_qc_non_weld_inspection.created_date, " +
                " tab_qc_non_weld_inspection.weld_date, tab_qc_non_weld_inspection.modified_date, " +
                " tab_qc_non_weld_inspection.dimensional_insp, tab_qc_non_weld_inspection.weld_visual, " +
                " tab_qc_non_weld_inspection.inspected_qty, tab_qc_non_weld_inspection.balanced_qty " +
                "ORDER BY Contracts.contract, Loads.pzone, Loads.pload";

            try (Connection con = factoryDataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, contractId);
                pst.setString(2, phase);
                pst.setString(3, loadId);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("contract", rs.getString("contract"));
                        row.put("contract_descr", rs.getString("descr"));
                        row.put("jobCode", rs.getString("JobCode"));
                        row.put("phase", rs.getString("pzone"));
                        row.put("load", rs.getString("pload"));
                        row.put("mark", rs.getString("mark"));
                        row.put("fmark", rs.getString("fmark"));
                        row.put("qty", rs.getInt("pqty"));
                        row.put("pieces_id", rs.getString("pieces_id"));
                        row.put("instance_id", rs.getString("instance_id"));
                        row.put("description", rs.getString("description"));
                        row.put("ssize", rs.getString("ssize"));
                        row.put("fullname", rs.getString("user_fullname"));
                        row.put("dims1", rs.getString("dims1"));
                        row.put("awght", rs.getBigDecimal("awght"));
                        row.put("ass_dt", rs.getTimestamp("ass_dt"));
                        row.put("inspected_qty", rs.getInt("inspected_qty"));
                        row.put("balanced_qty", rs.getInt("balanced_qty"));
                        row.put("result", rs.getString("result"));
                        row.put("status", rs.getString("status"));
                        row.put("weld_inspected_by", rs.getString("weld_inspected_by"));
                        row.put("weld_date", rs.getString("weld_date"));
                        row.put("dimensional_insp", rs.getString("dimensional_insp"));
                        row.put("weld_visual", rs.getString("weld_visual"));
                        data.add(row);
                    }
                }
            }

            response.put("Data", data);

        } catch (Exception e) {
            logger.error("Error fetching Loose Item inspection data", e);
            response.put("error", "Database error");
        }

        return response;
    }
    
    @PostMapping("/create-loose-item")
    public @ResponseBody Map<String, Object> createLooseItemInspection(
            @RequestBody Map<String, Object> payload) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1️⃣ Factory
            Integer factoryId = getInt(payload.get("factory_id"));
            DataSource ds = resolveFactoryDataSource(factoryId);

            // 2️⃣ Basic identifiers
            String contractId = (String) payload.get("contract_id");
            String phase      = (String) payload.get("phase");
            String loadId     = (String) payload.get("load_id");
            String mark       = (String) payload.get("mark");

            // 3️⃣ Fetch piece & instance (SAME AS FIT-UP)
            Map<String, String> pieceData =
                    fetchPieceAndInstance(ds, contractId, phase, loadId, mark);

            if (pieceData == null) {
                response.put("error", "No piece found for this mark");
                return response;
            }

            String piecesId   = pieceData.get("pieces_id");
            String instanceId = pieceData.get("instance_id");

            // 4️⃣ Quantities
            int inspectedQty = getInt(payload.get("inspected_qty"));
            int balancedQty  = getInt(payload.get("balanced_qty"));

            // 5️⃣ INSERT (Loose Item table)
            String sql =
                "INSERT INTO JIMS.dbo.tab_qc_non_weld_inspection ( " +
                " contract_id, pieces_id, instance_id, load_id, " +
                " inspected_qty, balanced_qty, " +
                " dimensional_insp, weld_visual, result, status, " +
                " weld_inspected_by, weld_date, " +
                " created_by, created_date, factory_id " +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = ds.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, contractId);
                pst.setString(2, piecesId);
                pst.setString(3, instanceId);
                pst.setString(4, loadId);

                pst.setInt(5, inspectedQty);
                pst.setInt(6, balancedQty);

                pst.setString(7, (String) payload.get("dimensional_insp")); // OK / NA / None
                pst.setString(8, (String) payload.get("weld_visual"));      // Satisfactory
                pst.setString(9, (String) payload.get("result"));
                pst.setString(10, (String) payload.get("status"));

                pst.setString(11, (String) payload.getOrDefault("created_by", "system"));
                pst.setTimestamp(12, new Timestamp(System.currentTimeMillis()));

                pst.setString(13, (String) payload.getOrDefault("created_by", "system"));
                pst.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
                pst.setInt(15, factoryId);

                int rows = pst.executeUpdate();

                response.put("success", rows > 0);
                response.put("message",
                        rows > 0 ? "Loose Item inspection created" : "Failed to create record");
            }

        } catch (Exception e) {
            logger.error("Error creating Loose Item Inspection record", e);
            response.put("error", e.getMessage());
        }

        return response;
    }
    
    @GetMapping("/validate-contract")
    public @ResponseBody Map<String, Object> validateContract(
            @RequestParam("factory") Integer factoryId,
            @RequestParam("contractId") String contractId) {

        Map<String, Object> response = new HashMap<String, Object>();

        try {

            String sql =
                "SELECT " +
                "    COUNT(*) AS total, " +
                "    SUM(CASE WHEN LTRIM(RTRIM(ISNULL(status,''))) = 'Completed' THEN 1 ELSE 0 END) AS completed_cnt, " +
                "    SUM(CASE WHEN LTRIM(RTRIM(ISNULL(status,''))) = 'Incomplete' THEN 1 ELSE 0 END) AS incomplete_cnt " +
                "FROM dbo.tab_qc_inspection " +
                "WHERE contract_id = ?";

            try (Connection con = jdmsDataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, contractId);

                try (ResultSet rs = pst.executeQuery()) {

                	if (rs.next()) {

                	    int total = rs.getInt("total");
                	    int completedCnt = rs.getInt("completed_cnt");
                	    int incompleteCnt = rs.getInt("incomplete_cnt");

                	    logger.info("total=" + total);
                	    logger.info("completedCnt=" + completedCnt);
                	    logger.info("incompleteCnt=" + incompleteCnt);

                	    response.put("debug_total", total);
                	    response.put("debug_completedCnt", completedCnt);
                	    response.put("debug_incompleteCnt", incompleteCnt);

                	    if (total == 0) {

                	        response.put("allowed", true);

                	    } else if (completedCnt > 0) {

                	        response.put("allowed", false);
                	        response.put("message",
                	                "Transaction already completed for this Contract in JDMS. Entry not allowed.");

                	    } else if (incompleteCnt > 0) {

                	        response.put("allowed", true);
                	        response.put("message",
                	                "Incomplete record found. You may continue entry.");

                	    } else {

                	        response.put("allowed", true);
                	    }
                	}
                }
            }

        } catch (Exception e) {

            logger.error(
                "Error validating contract in JDMS for ContractId: " + contractId,
                e
            );

            response.put("allowed", false);
            response.put("message", "Validation failed.");
        }

        return response;
    }
 }
