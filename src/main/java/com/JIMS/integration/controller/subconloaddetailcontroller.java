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
@RequestMapping("/api")
public class subconloaddetailcontroller {
    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource;

    @Autowired
    @Qualifier("misDataSource2")
    private DataSource misDataSource2;

    @GetMapping("/traloads/summary-subcon")
    public @ResponseBody List<Map<String, Object>> getLoadSummarySubcon(
            @RequestParam("contractId") int contractId,
            @RequestParam("loadno") String loadno,
            @RequestParam("supid") int supid,
            @RequestParam("factory_id") int factoryId) {

        String sql = "EXEC dbo.spTRA_GetLoadSummarySubcon ?, ?, ?";

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contractId);
            ps.setString(2, loadno);
            ps.setInt(3, supid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // 🔹 CAMEL CASE MAPPING (FRONTEND FRIENDLY)
                row.put("tloadId", rs.getInt("tload_id"));
                row.put("contractId", rs.getInt("contract_id"));
                row.put("loadno", rs.getString("loadno"));
                row.put("description", rs.getString("descr"));
                row.put("trailerRef", rs.getString("trailerref"));
                row.put("trackingRef", rs.getString("trackingref"));
                row.put("createdDate", rs.getTimestamp("created"));
                row.put("pieceWeight", rs.getBigDecimal("pieceweight"));
                row.put("totalWeight", rs.getBigDecimal("tweight"));
                row.put("wbWeight", rs.getBigDecimal("wb_weight"));
                row.put("otherWeight", rs.getBigDecimal("otherweight"));
                row.put("fabLoads", rs.getString("fabloads"));
                row.put("status", rs.getString("status"));
                row.put("factoryId", rs.getInt("factory_id"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
    
    @GetMapping("/traloads/latest")
    public @ResponseBody List<Map<String, Object>> getLatestTraLoadByContract(
            @RequestParam("contractId") int contractId,
            @RequestParam("factory_id") int factoryId) {

        String sql = """
            SELECT TOP 1 *
            FROM Tra_Loads
            WHERE contract_id = ?
            ORDER BY tload_id DESC
            """;

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contractId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // 🔹 EXACT COLUMN → RESPONSE MAPPING
                row.put("tloadId", rs.getInt("tload_id"));
                row.put("contractId", rs.getInt("contract_id"));
                row.put("loadno", rs.getString("loadno"));
                row.put("description", rs.getString("descr"));
                row.put("trailerRef", rs.getString("trailerref"));
                row.put("trackingRef", rs.getString("trackingref"));
                row.put("created", rs.getTimestamp("created"));
                row.put("createdBy", rs.getObject("createdby"));
                row.put("createdDate", rs.getObject("createddate"));
                row.put("exciseInvoice", rs.getString("exciseinvoice"));
                row.put("wbWeight", rs.getBigDecimal("wb_weight"));
                row.put("factoryId", rs.getInt("factory_id"));
                row.put("supid", rs.getInt("supid"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching latest Tra_Load", e);
        }

        return resultList;
    }

    @GetMapping("/traloads/runs-subcon")
    public @ResponseBody List<Map<String, Object>> getLoadRunsSubcon(
            @RequestParam("tloadId") int tloadId,
            @RequestParam("factory_id") int factoryId) {

        String sql = "EXEC dbo.spTra_GetLoadRunsSubcon ?";

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tloadId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // 🔹 EXACT COLUMN → JSON MAPPING
                row.put("runId", rs.getInt("run_Id"));
                row.put("tloadId", rs.getInt("tload_Id"));
                row.put("contractId", rs.getInt("contract_Id"));
                row.put("loadno", rs.getString("loadno"));
                row.put("loadType", rs.getString("loadType"));
                row.put("status", rs.getString("status"));

                row.put("haulierId", rs.getInt("haulier_Id"));
                row.put("haulier", rs.getString("haulier"));

                row.put("destinationId", rs.getInt("destination_Id"));
                row.put("destination", rs.getString("destination"));

                row.put("country", rs.getString("country"));
                row.put("county", rs.getString("county"));

                row.put("arrivalDate", rs.getTimestamp("arrivalDate"));
                row.put("arrivalTime", rs.getString("arrivalTime"));

                row.put("despatchDate", rs.getTimestamp("despatchDate"));
                row.put("despatchTime", rs.getString("despatchTime"));

                row.put("leftDestDate", rs.getTimestamp("leftDestDate"));
                row.put("leftDestTime", rs.getString("leftDestTime"));

                row.put("reqdDate", rs.getTimestamp("reqdDate"));
                row.put("reqdTime", rs.getString("reqdTime"));

                row.put("invoiceNo", rs.getString("invoiceNo"));
                row.put("notes", rs.getString("notes"));
                row.put("costNotes", rs.getString("costNotes"));

                row.put("fuelCost", rs.getBigDecimal("fuelCost"));
                row.put("fuelPC", rs.getBigDecimal("fuelPC"));
                row.put("delayCost", rs.getBigDecimal("delayCost"));
                row.put("tableRate", rs.getBigDecimal("tableRate"));
                row.put("totalCost", rs.getBigDecimal("totalCost"));

                row.put("trateId", rs.getInt("trate_Id"));
                row.put("fcontract", rs.getString("fcontract"));

                row.put("length", rs.getString("length"));
                row.put("width", rs.getString("width"));

                row.put("adjustment", rs.getBigDecimal("adjustment"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing spTra_GetLoadRunsSubcon", e);
        }

        return resultList;
    }

    @GetMapping("/contract/zones-subcon")
    public @ResponseBody List<Map<String, Object>> getContractZonesForSubcon(
            @RequestParam("contractId") int contractId,
            @RequestParam("supplierId") int supplierId,
            @RequestParam("factory_id") int factoryId) {

        String sql = """
            SELECT DISTINCT
                z.czone_id,
                z.contract_zone,
                z.zone_descr,
                CONCAT(
                    'Phase ',
                    CAST(z.contract_zone AS VARCHAR),
                    ' - ',
                    RTRIM(z.zone_descr)
                ) AS descr,
                supplier_id
            FROM contract_zones z
            INNER JOIN Loads l
                ON l.pzone = z.contract_zone
               AND z.contract_id = l.contract_id
            INNER JOIN Piece_Instance i
                ON i.load_id = l.load_id
            WHERE z.contract_id = ?
              AND supplier_id = ?
            """;

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contractId);
            ps.setInt(2, supplierId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

           
                row.put("czone_id", rs.getInt("czone_id"));
                row.put("contract_Zone", rs.getInt("contract_zone"));
                row.put("zone_descr", rs.getString("zone_descr"));
                row.put("descr", rs.getString("descr"));
                row.put("supplier_id", rs.getInt("supplier_id"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching contract zones for subcontractor", e);
        }

        return resultList;
    }
    
    @GetMapping("/loads/loads-to-load-subcon")
    public @ResponseBody List<Map<String, Object>> getLoadsToLoadSubcon(
            @RequestParam("contractId") int contractId,
            @RequestParam("phase") int phase,
            @RequestParam("supid") int supid,
            @RequestParam("factory_id") int factoryId) {

        String sql = "EXEC spTRA_GetLoadsToLoadSubcon ?, ?, '1', ?";

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contractId);
            ps.setInt(2, phase);
            ps.setInt(3, supid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // EXACT SP OUTPUT COLUMNS
                row.put("descr", rs.getString("descr"));
                row.put("load_id", rs.getInt("load_id"));
                row.put("pzone", rs.getInt("pzone"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing spTRA_GetLoadsToLoadSubcon", e);
        }

        return resultList;
    }
    
    @GetMapping("/loads/pieces-to-load-subcon")
    public @ResponseBody List<Map<String, Object>> getPiecesToLoadWithProgressSubcon(
            @RequestParam("contractId") int contractId,
            @RequestParam("phase") int phase,
            @RequestParam("supid") int supid,
            @RequestParam("loadid") int loadid,
            @RequestParam("factory_id") int factoryId) {

        String sql = "EXEC spTRA_GetPiecesToLoadWithPieceProgressSubcon ?, ?, 1, ?, ?";

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contractId);
            ps.setInt(2, phase);
            ps.setInt(3, supid);
            ps.setInt(4, loadid);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // ✅ EXACT COLUMN NAMES AS SP OUTPUT
                row.put("descr", rs.getString("descr"));
                row.put("load_id", rs.getInt("load_id"));
                row.put("pieces_id", rs.getInt("pieces_id"));
                row.put("pqty", rs.getInt("pqty"));
                row.put("weight", rs.getBigDecimal("weight"));
                row.put("pieceRework", rs.getInt("pieceRework"));
                row.put("max_piecestages_id", rs.getInt("max_piecestages_id"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error executing spTRA_GetPiecesToLoadWithPieceProgressSubcon", e
            );
        }

        return resultList;
    }

    @GetMapping("/loads/distinct-ploads-subcon")
    public @ResponseBody List<Map<String, Object>> getDistinctPLoadsSubcon(
            @RequestParam("contractId") String contractId,
            @RequestParam("supplierId") int supplierId,
            @RequestParam("pzone") String pzone,
            @RequestParam("factory_id") int factoryId) {

        String sql = """
            SELECT DISTINCT l.pload, l.load_id
            FROM MIS.dbo.Loads l
            INNER JOIN MIS.dbo.VW_JSSL_SubconContractLoad s
                ON s.contract_id = l.contract_id
               AND s.load_id = l.load_id
            WHERE l.contract_id = ?
              AND s.supplier_id = ?
              AND l.pzone = ?
            ORDER BY l.pload
            """;

        List<Map<String, Object>> resultList = new ArrayList<>();

        // ✅ Factory-based DB selection
        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, contractId);
            ps.setInt(2, supplierId);
            ps.setString(3, pzone);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // ✅ Same column names as query
                row.put("pload", rs.getString ("pload"));
                row.put("load_id", rs.getInt("load_id"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching distinct ploads (subcon)", e);
        }

        return resultList;
    }
    
    @GetMapping("/loads/print-headers")
    public @ResponseBody List<Map<String, Object>> getLoadPrintDetails(
            @RequestParam("contractId") int contractId,
            @RequestParam("loadId") int loadId,
            @RequestParam("factory_id") int factoryId) {

        String sql = """
            SELECT 
                Contracts.descr,                      -- 0
                Tra_Loads.loadno,                     -- 1
                ISNULL(Tra_Loads.wb_weight * 1000,0), -- 2
                vw.descr,                             -- 3
                vw.mark,                              -- 4
                vw.ssize,                             -- 5
                vw.qty,                               -- 6
                vw.source,                            -- 7
                Contracts.fcontract,                  -- 8
                Tra_Loads.trailerref,                 -- 9
                Delivery_Sites.del1,                  -- 10
                Delivery_Sites.del2,                  -- 11
                Delivery_Sites.del3,                  -- 12
                Delivery_Sites.del4,                  -- 13
                CASE 
                    WHEN CONVERT(date, Tra_LoadRuns.despatchdate, 103) = '1900-01-01'
                    THEN Tra_Loads.created
                    ELSE Tra_LoadRuns.despatchdate
                END AS despatchdate,                  -- 14
                Hauliers.company,                     -- 15
                Tra_LoadRuns.notes,                   -- 16
                Tra_LoadRuns.invoiceno,               -- 17
                Contracts.contract,                   -- 18
                Delivery_Sites.phone,                 -- 19
                Delivery_Sites.contact,               -- 20
                Tra_Loads.trackingref,                -- 21
                Contracts.lc_ref,                     -- 22
                Contracts.client_po                   -- 23
            FROM MIS.dbo.vw_JSSLunionTraItemsAndExtras vw
            INNER JOIN MIS.dbo.vw_JSSLtraFirstRun fr ON fr.tload_id = vw.tload_id
            INNER JOIN MIS.dbo.Tra_Loads Tra_Loads ON fr.tload_id = Tra_Loads.tload_id
            INNER JOIN MIS.dbo.Tra_LoadRuns Tra_LoadRuns ON fr.run_id = Tra_LoadRuns.run_id
            INNER JOIN MIS.dbo.Delivery_Sites Delivery_Sites 
                ON Tra_LoadRuns.destination_id = Delivery_Sites.delsite_id
            INNER JOIN MIS.dbo.Hauliers Hauliers 
                ON Tra_LoadRuns.haulier_id = Hauliers.haulier_id
            INNER JOIN MIS.dbo.Contracts Contracts 
                ON Tra_Loads.contract_id = Contracts.contract_id
            WHERE Contracts.contract_id = ?
              AND Tra_Loads.tload_id = ?
            ORDER BY vw.source, vw.descr, vw.mark, Tra_LoadRuns.createddate DESC
            """;

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contractId);
            ps.setInt(2, loadId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();

                map.put("descr", rs.getString(1));
                map.put("loadNo", rs.getString(2));
                map.put("weight", rs.getBigDecimal(3));
                map.put("itemDescr", rs.getString(4));
                map.put("mark", rs.getString(5));
                map.put("ssize", rs.getString(6));
                map.put("qty", rs.getInt(7));
                map.put("source", rs.getString(8));
                map.put("fcontract", rs.getString(9));
                map.put("vehicle", rs.getString(10));
                map.put("del1", rs.getString(11));
                map.put("del2", rs.getString(12));
                map.put("del3", rs.getString(13));
                map.put("del4", rs.getString(14));
                map.put("despatchDate", rs.getTimestamp(15));
                map.put("haulier", rs.getString(16));
                map.put("notes", rs.getString(17));
                map.put("invoiceNo", rs.getString(18));
                map.put("contract", rs.getString(19));
                map.put("phone", rs.getString(20));
                map.put("contact", rs.getString(21));
                map.put("trackingRef", rs.getString(22));
                map.put("lcRef", rs.getString(23));
                map.put("clientPO", rs.getString(24));

                resultList.add(map);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching load print headers", e);
        }

        return resultList;
    }

    
    @GetMapping("/loads/items")
    public @ResponseBody List<Map<String, Object>> getLoadItemWeights(
            @RequestParam("contractId") int contractId,
            @RequestParam("loadId") int loadId,
            @RequestParam("factory_id") int factoryId) {

        String sql = """
            SELECT DISTINCT 
                c.descr, 
                t.loadno, 
                t.wb_weight,
                v.descr AS pdescr, 
                v.mark, 
                v.ssize, 
                v.qty,
                (v.tweight / v.qty) * 1000 AS iawght, 
                v.pload
            FROM MIS.dbo.vw_JSSLunionTraItemsAndExtras v
            INNER JOIN MIS.dbo.vw_JSSLtraFirstRun f 
                ON f.tload_id = v.tload_id
            INNER JOIN MIS.dbo.Tra_Loads t 
                ON f.tload_id = t.tload_id
            INNER JOIN MIS.dbo.Contracts c 
                ON t.contract_id = c.contract_id
            WHERE c.contract_id = ?
              AND t.tload_id = ?
            ORDER BY v.descr, v.mark
            """;

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contractId);
            ps.setInt(2, loadId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                row.put("contractDescr", rs.getString(1));
                row.put("loadNo", rs.getString(2));
                row.put("wbWeight", rs.getBigDecimal(3));
                row.put("productDescr", rs.getString(4));
                row.put("mark", rs.getString(5));
                row.put("ssize", rs.getString(6));
                row.put("qty", rs.getInt(7));
                row.put("itemAvgWeight", rs.getBigDecimal(8));
                row.put("pload", rs.getString(9));

                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching load item weights", e);
        }

        return resultList;
    }
    
    @GetMapping("/loads/details")
    public @ResponseBody List<Map<String, Object>> getLoadPieceSummarySubcon(
            @RequestParam("contractId") String contractId,
            @RequestParam("pzone") String pzone,
            @RequestParam("loadId") int loadId,
            @RequestParam("supplierId") int supplierId,
            @RequestParam("factory_id") int factoryId
    ) {
        String sql = """
            SELECT c.contract, c.JobCode, c.descr,
                   l.pzone, l.pload, p.mark, p.descr AS pdescr,
                   p.weight, p.area, vw.pqty,
                   CAST(ROUND((vw.pqty * p.weight), 2) AS numeric(36,2)) AS tweight,
                   CAST(ROUND((vw.pqty * p.area), 2) AS numeric(36,2)) AS tarea
            FROM MIS.dbo.Contracts c
            INNER JOIN MIS.dbo.Loads l ON l.contract_id = c.contract_id
            INNER JOIN MIS.dbo.vw_JSSLsumLoadPieceProgress vw ON vw.load_id = l.load_id
            INNER JOIN MIS.dbo.Pieces p ON l.contract_id = p.contract_id AND vw.pieces_id = p.pieces_id
            INNER JOIN MIS.dbo.VW_JSSL_SubconContractLoad s
                ON s.load_id = l.load_id AND s.contract_id = c.contract_id AND s.pzone = l.pzone
            WHERE c.contract_id = ?
              AND l.pzone = ?
              AND l.load_id = ?
              AND s.supplier_id = ?
            ORDER BY c.contract, l.pzone, l.pload
            """;

        List<Map<String, Object>> resultList = new ArrayList<>();

        // ✅ Select DataSource based on factory
        DataSource selectedDataSource = (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, contractId);
            ps.setString(2, pzone);
            ps.setInt(3, loadId);
            ps.setInt(4, supplierId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("contract", rs.getString("contract"));
                    row.put("JobCode", rs.getString("JobCode"));
                    row.put("descr", rs.getString("descr"));
                    row.put("pzone", rs.getString("pzone"));
                    row.put("pload", rs.getInt("pload"));
                    row.put("mark", rs.getString("mark"));
                    row.put("pdescr", rs.getString("pdescr"));
                    row.put("weight", rs.getBigDecimal("weight"));
                    row.put("area", rs.getBigDecimal("area"));
                    row.put("pqty", rs.getInt("pqty"));
                    row.put("tweight", rs.getBigDecimal("tweight"));
                    row.put("tarea", rs.getBigDecimal("tarea"));
                    resultList.add(row);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching load piece summary (subcon)", e);
        }

        return resultList;
    }

}
