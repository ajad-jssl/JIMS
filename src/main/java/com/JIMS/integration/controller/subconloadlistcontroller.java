package com.JIMS.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class subconloadlistcontroller {
	@Autowired
	@Qualifier("misDataSource")
	private DataSource misDataSource;

	@Autowired
	@Qualifier("misDataSource2")
	private DataSource misDataSource2;

	@GetMapping("/loads/by-contract-supplier")
	public @ResponseBody List<Map<String, Object>> getTransportLoads(@RequestParam("contractId") int contractId,
			@RequestParam("supId") int supId, @RequestParam("factory_id") int factoryId) {

		String sql = "SELECT DISTINCT l.tload_id, l.loadno " + "FROM Tra_Loads l "
				+ "WHERE l.contract_id = ? AND l.supid = ? " + "ORDER BY l.tload_id";

		List<Map<String, Object>> resultList = new ArrayList<>();

		DataSource selectedDataSource = (factoryId == 1) ? misDataSource : misDataSource2;

		try (Connection con = selectedDataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, contractId);
			ps.setInt(2, supId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				row.put("tload_id", rs.getInt("tload_id"));
				row.put("loadno", rs.getString("loadno"));
				resultList.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@GetMapping("/loads/by-contract-supplier/summary")
	public @ResponseBody List<Map<String, Object>> getTransportLoadsummary(
			@RequestParam("contractId") String contractId, @RequestParam("supId") int supId,
			@RequestParam("factory_id") int factoryId) {

		List<Map<String, Object>> resultList = new ArrayList<>();

		DataSource selectedDataSource = (factoryId == 1) ? misDataSource : misDataSource2;

		try (Connection con = selectedDataSource.getConnection()) {

			PreparedStatement ps;

			// FIXED STRING CHECK
			if ("ALL".equalsIgnoreCase(contractId)) {

				String sql = "SELECT DISTINCT l.tload_id, l.loadno " + "FROM Tra_Loads l " + "WHERE l.supid = ? "
						+ "ORDER BY l.tload_id";

				ps = con.prepareStatement(sql);
				ps.setInt(1, supId); // correct index

			} else {

				String sql = "SELECT DISTINCT l.tload_id, l.loadno " + "FROM Tra_Loads l "
						+ "WHERE l.contract_id = ? AND l.supid = ? " + "ORDER BY l.tload_id";

				ps = con.prepareStatement(sql);

				ps.setString(1, contractId); // ✅ convert to int
				ps.setInt(2, supId);
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				row.put("tload_id", rs.getInt("tload_id"));
				row.put("loadno", rs.getString("loadno"));
				resultList.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;
	}


	
	
	
	@GetMapping("/loads/by-contract-supplier/summarys")
	public @ResponseBody List<Map<String, Object>> getTransportLoadSummaryDetails(
	        @RequestParam int supId,
	        @RequestParam String sd,
	        @RequestParam String ed,
	        @RequestParam int factory_id,
	        @RequestParam(required = false) String contractId,
	        @RequestParam(required = false) String tloadId) throws Exception {

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    Date startDate = sdf.parse(sd);
	    Date endDate = sdf.parse(ed);

	    return fetchTransportLoadSummaryContractandloadall(
	            supId,
	            startDate,
	            endDate,
	            contractId,
	            tloadId,
	            factory_id
	    );
	}
	
	
	
	
	@GetMapping("/loads/by-contract-supplier/details")
	public @ResponseBody List<Map<String, Object>> getTransportLoadDetails(
	        @RequestParam int supId,
	        @RequestParam String sd,
	        @RequestParam String ed,
	        @RequestParam int factory_id,
	        @RequestParam(required = false) String contractId,
	        @RequestParam(required = false) String tloadId) throws Exception {

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	    Date startDate = sdf.parse(sd);
	    Date endDate = sdf.parse(ed);

	    return fetchTransportLoadDetailsContractandloadall(
	            supId,
	            startDate,
	            endDate,
	            contractId,
	            tloadId,
	            factory_id
	    );
	}
	
	

	
	
	
	public List<Map<String, Object>> fetchTransportLoadDetailsContractandloadall(
	        int supId,
	        Date startDate,
	        Date endDate,
	        String contractId,
	        String tloadId,
	        int factoryId) {

	    List<Map<String, Object>> resultList = new ArrayList<>();

	    DataSource selectedDataSource =
	            (factoryId == 1) ? misDataSource : misDataSource2;

	    StringBuilder sql = new StringBuilder("""
	        
	    ;WITH FILTERED_PIECES AS (
	        SELECT DISTINCT i.pieces_id, i.revision
	        FROM Piece_Instance i
	        INNER JOIN Tra_Loads tl 
	            ON tl.tload_id = i.tload_id
	        WHERE (i.load_id <> 0)
	            AND (i.supplier_id = ?)
	            AND CONVERT(DATE, tl.created) BETWEEN CONVERT(DATE, ?) AND CONVERT(DATE, ?)
	    ),

	    MLT_DATA AS (
	        SELECT 
	            c2.pieces_id,
	            c2.revision,

	            RTRIM(s2.scode) + ' ' + RTRIM(s2.ssize) AS ssize,

	            LTRIM(
	                RIGHT(
	                    SPACE(11) + 
	                    CAST(c2.length AS VARCHAR(5)) + 
	                    CASE 
	                        WHEN c2.width = 0 THEN ''
	                        ELSE '*' + CAST(c2.width AS VARCHAR(5))
	                    END,
	                11)
	            ) AS dims,

	            ROW_NUMBER() OVER (
	                PARTITION BY c2.pieces_id, c2.revision
	                ORDER BY c2.mainpart DESC, c2.shaft, c2.length DESC, c2.width DESC
	            ) AS rn

	        FROM mlt c2
	        INNER JOIN FILTERED_PIECES fp
	            ON fp.pieces_id = c2.pieces_id
	            AND fp.revision = c2.revision
	        INNER JOIN sectionsizes s2 
	            ON s2.sect_id = c2.sect_id
	    )

	    SELECT 
	        COUNT(i.instance_id) AS qty,
	        p.mark,
	        p.descr,
	        p.longest_length,
	        SUM(p.weight) AS tweight,
	        SUM(p.area) AS tarea,
	        p.pieces_id,
	        i.load_id,
	        c.jobcode,
	        tl.loadno,
	        tl.tload_id,
	        tl.descr,
	        tl.contract_id,
	        tl.trailerref,
	        tl.trackingref,
	        tl.created,

	        ISNULL(x.ssize, 'Fittings') AS ssize,
	        ISNULL(x.dims, '') AS dims,

	        i.tload_id,
	        i.pzone,
	        l.pload,
	        i.fab_id,
	        i.supplier_id,

	        CASE 
	            WHEN i.supplier_id = 0 THEN f.descr
	            ELSE RTRIM(s.code) + ' - ' + RTRIM(s.NAME)
	        END AS fab,

	        i.locfg_id,
	        ISNULL(lfg.code, '') AS finishedgoodslocation

	    FROM Piece_Instance i
	    INNER JOIN Tra_Loads tl ON tl.tload_id = i.tload_id
	    INNER JOIN Contracts c ON tl.contract_id = c.contract_id
	    INNER JOIN Pieces p ON i.pieces_id = p.pieces_id AND i.revision = p.revision
	    INNER JOIN Loads l ON i.load_id = l.load_id
	    INNER JOIN fabrication f ON i.fab_id = f.subcon_id
	    LEFT JOIN Suppliers s ON i.supplier_id = s.supplier_id
	    LEFT JOIN LocationFinishedGoods lfg ON i.locfg_id = lfg.locfg_id

	    LEFT JOIN MLT_DATA x 
	        ON x.pieces_id = p.pieces_id 
	        AND x.revision = p.revision
	        AND x.rn = 1

	    WHERE (i.load_id <> 0)
	        AND (p.comp_id = 0)
	        AND (i.supplier_id = ?)
	        AND CONVERT(DATE, tl.created) BETWEEN CONVERT(DATE, ?) AND CONVERT(DATE, ?)
	    """);

	    // ✅ Dynamic filters (same style as your other method)
	    if (contractId != null && !"All".equalsIgnoreCase(contractId)) {
	        sql.append(" AND tl.contract_id = ? ");
	    }

	    if (tloadId != null && !"All".equalsIgnoreCase(tloadId)) {
	        sql.append(" AND tl.tload_id = ? ");
	    }

	    sql.append("""
	        GROUP BY 
	            p.mark, p.descr, p.longest_length, p.pieces_id,
	            i.load_id, p.revision, i.tload_id, i.pzone,
	            l.pload, i.locfg_id, lfg.code, c.JobCode,
	            i.supplier_id, f.descr, s.code, s.NAME,
	            i.fab_id, tl.tload_id, tl.loadno, tl.descr,
	            tl.contract_id, tl.trailerref, tl.trackingref,
	            tl.created, x.ssize, x.dims

	        ORDER BY 
	            i.pzone,
	            l.pload,
	            RIGHT('00000000000000000000' + RTRIM(p.mark), 20)
	    """);

	    try (Connection con = selectedDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        int index = 1;

	        // FILTERED_PIECES
	        ps.setInt(index++, supId);
	        ps.setDate(index++, new java.sql.Date(startDate.getTime()));
	        ps.setDate(index++, new java.sql.Date(endDate.getTime()));

	        // MAIN WHERE
	        ps.setInt(index++, supId);
	        ps.setDate(index++, new java.sql.Date(startDate.getTime()));
	        ps.setDate(index++, new java.sql.Date(endDate.getTime()));

	        // Dynamic params
	        if (contractId != null && !"All".equalsIgnoreCase(contractId)) {
	            ps.setString(index++, contractId);
	        }

	        if (tloadId != null && !"All".equalsIgnoreCase(tloadId)) {
	            ps.setString(index++, tloadId);
	        }

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();

	            row.put("qty", rs.getInt("qty"));
	            row.put("mark", rs.getString("mark"));
	            row.put("phases",rs.getString("pzone") );
	            row.put("descr", rs.getString("descr"));
	            row.put("longest_length", rs.getObject("longest_length"));
	            row.put("tweight", rs.getDouble("tweight"));
	            row.put("tarea", rs.getDouble("tarea"));
	            row.put("pieces_id", rs.getInt("pieces_id"));
	            row.put("load_id", rs.getInt("load_id"));
	            row.put("jobcode", rs.getString("jobcode"));
	            row.put("loadno", rs.getString("loadno"));
	            row.put("tload_id", rs.getInt("tload_id"));
	            row.put("contract_id", rs.getInt("contract_id"));
	            row.put("created", rs.getString("created"));
row.put("pload",rs.getString("pload"));
row.put("vehicleref", rs.getString("trailerref"));
row.put("waybridgref", rs.getString("trackingref"));
	            row.put("ssize", rs.getString("ssize"));
	            row.put("dims", rs.getString("dims"));
	            row.put("fab", rs.getString("fab"));
	            row.put("finishedgoodslocation", rs.getString("finishedgoodslocation"));

	            resultList.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return resultList;
	}
	

	
	
	public List<Map<String, Object>> fetchTransportLoadSummaryContractandloadall(
	        int supId,
	        Date startDate,
	        Date endDate,
	        String contractId,   // optional filter
	        String tloadId,      // optional filter
	        int factoryId) {

	    List<Map<String, Object>> resultList = new ArrayList<>();

	    DataSource selectedDataSource =
	            (factoryId == 1) ? misDataSource : misDataSource2;

	    StringBuilder sql = new StringBuilder("""
	        SELECT
	            c.JobCode,
	            tl.tload_id,
	            tl.loadno,
	            tl.descr,
	            tl.contract_id,
	            tl.trailerref,
	            tl.trackingref,
	            tl.created,

	            ISNULL(pw.tweight, 0) AS pieceweight,
	            ISNULL(aw.tweight, 0) AS otherweight,
	            ISNULL(pw.tweight, 0) + ISNULL(aw.tweight, 0) AS tweight,

	            tl.wb_weight,
	            tl.exciseinvoice,
	            lrs.descr AS status,

	            ISNULL((
	                SELECT STUFF((
	                    SELECT ', ' + RTRIM(x.pload)
	                    FROM (
	                        SELECT l.fload, l.pload
	                        FROM Piece_Instance pin
	                        INNER JOIN pieces p ON pin.pieces_id = p.pieces_id
	                        INNER JOIN loads l ON pin.load_id = l.load_id
	                        WHERE pin.tload_id = tl.tload_id
	                        GROUP BY l.fload, l.pload
	                    ) x
	                    ORDER BY x.fload
	                    FOR XML PATH(''), TYPE
	                ).value('.', 'NVARCHAR(MAX)'), 1, 2, '')
	            ), '') AS fabloads,

	            tl.factory_id

	        FROM tra_loads tl
	        LEFT JOIN Contracts c ON tl.contract_id = c.contract_id
	        LEFT JOIN vwTra_SumPieceWeight pw ON tl.tload_id = pw.tload_id
	        LEFT JOIN vwTra_SumAdditionalItemsWeight aw ON tl.tload_id = aw.tload_id
	        LEFT JOIN vwTra_GetMaxRunStatus ms ON tl.tload_id = ms.tload_id
	        LEFT JOIN loadrunstatuses lrs ON ms.maxrunstatus_id = lrs.runstatus_id

	        WHERE tl.supid = ?
	        AND CONVERT(DATE, tl.created) BETWEEN CONVERT(DATE, ?) AND CONVERT(DATE, ?)
	    """);

	    // ✅ Dynamic filters
	    if (contractId != null && !"All".equalsIgnoreCase(contractId)) {
	        sql.append(" AND tl.contract_id = ? ");
	    }

	    // ✅ Load filter (only if NOT "All")
	    if (tloadId != null && !"All".equalsIgnoreCase(tloadId)) {
	        sql.append(" AND tl.tload_id = ? ");
	    }

	    sql.append("""
	        ORDER BY RIGHT('0000000000' + RTRIM(tl.loadno), 10)
	    """);

	    try (Connection con = selectedDataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        int index = 1;

	        ps.setInt(index++, supId);
	        ps.setDate(index++, new java.sql.Date(startDate.getTime()));
	        ps.setDate(index++, new java.sql.Date(endDate.getTime()));

	        if (contractId != null && !"All".equalsIgnoreCase(contractId)) {
	            ps.setString(index++, contractId);   // ✅ STRING
	        }

	        if (tloadId != null && !"All".equalsIgnoreCase(tloadId)) {
	            ps.setString(index++, tloadId);      // ✅ STRING
	        }

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();

	            row.put("JobCode", rs.getString("JobCode"));
	            row.put("tload_id", rs.getInt("tload_id"));
	            row.put("loadno", rs.getString("loadno"));
	            row.put("descr", rs.getString("descr"));
	            row.put("contract_id", rs.getInt("contract_id"));
	            row.put("trailerref", rs.getString("trailerref"));
	            row.put("trackingref", rs.getString("trackingref"));
	            row.put("created", rs.getString("created"));

	            row.put("pieceweight", rs.getDouble("pieceweight"));
	            row.put("otherweight", rs.getDouble("otherweight"));
	            row.put("tweight", rs.getDouble("tweight"));

	            row.put("wb_weight", rs.getObject("wb_weight"));
	            row.put("exciseinvoice", rs.getString("exciseinvoice"));
	            row.put("status", rs.getString("status"));

	            String fab = rs.getString("fabloads");
	            row.put("fabloads", fab != null ? fab.trim() : "");

	            row.put("factory_id", rs.getInt("factory_id"));

	            resultList.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return resultList;
	}

}
