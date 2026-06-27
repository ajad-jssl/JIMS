package com.JIMS.integration.controller;
import com.JIMS.MIS.model.lotdetail;
import com.JIMS.MIS.services.lotdetailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

@RestController
@RequestMapping("/api/lotdetail")
@CrossOrigin
public class lotdetailcontroller {

    @Autowired
    private lotdetailservice pieceInLoadService;

    @GetMapping("/load/{tLoadId}")
    public List<lotdetail> getPiecesInLoad(@PathVariable int tLoadId) {
        return pieceInLoadService.getPiecesInLoad(tLoadId);
    }
    
    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource;

    @Autowired
    @Qualifier("misDataSource2")
    private DataSource misDataSource2;

    @GetMapping("/load/pieces-in-load")
    public @ResponseBody List<Map<String, Object>> getPiecesInLoad(
            @RequestParam("tLoadId") int tLoadId,
            @RequestParam("factory_id") int factoryId
    ) {

        String sql = "EXEC dbo.spTRA_GetPiecesInLoad ?";

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tLoadId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // 🔹 SAME MAPPING STYLE AS EXAMPLE
                row.put("tload_id", rs.getInt("tload_id"));
                row.put("load_id", rs.getInt("load_id"));
                row.put("pieces_id", rs.getInt("pieces_id"));
                row.put("qty", rs.getInt("qty"));
                row.put("mark", rs.getString("mark"));
                row.put("descr", rs.getString("descr"));
                row.put("dims", rs.getString("dims"));
                row.put("ssize", rs.getString("ssize"));
                row.put("longestLength", rs.getInt("longest_length"));
                row.put("pzone", rs.getInt("pzone"));
                row.put("tarea", rs.getInt("tarea"));
                row.put("pload", rs.getInt("pload"));
                row.put("tweight", rs.getBigDecimal("tweight"));
                row.put("fab", rs.getString("fab"));
                row.put("fabId", rs.getInt("fab_id"));
                row.put("supplierId", rs.getInt("supplier_id"));
                row.put("locfg_id", rs.getInt("locfg_id"));
                row.put("finishedGoodsLocation", rs.getString("finishedgoodslocation"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
    
    
    
    @GetMapping("/load/pieces-in-loadss")
    public @ResponseBody List<Map<String, Object>> getPiecesInLoadss(
            @RequestParam("tLoadId") String tLoadId,
            @RequestParam("factory_id") int factoryId
    ) {

        String sql = "EXEC dbo.spTRA_GetPiecesInLoad ?";

        List<Map<String, Object>> resultList = new ArrayList<>();

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tLoadId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                // 🔹 SAME MAPPING STYLE AS EXAMPLE
                row.put("tload_id", rs.getInt("tload_id"));
                row.put("load_id", rs.getInt("load_id"));
                row.put("pieces_id", rs.getInt("pieces_id"));
                row.put("qty", rs.getInt("qty"));
                row.put("mark", rs.getString("mark"));
                row.put("descr", rs.getString("descr"));
                row.put("dims", rs.getString("dims"));
                row.put("ssize", rs.getString("ssize"));
                row.put("longestLength", rs.getInt("longest_length"));
                row.put("pzone", rs.getInt("pzone"));
                row.put("tarea", rs.getInt("tarea"));
                row.put("pload", rs.getString("pload"));
                row.put("tweight", rs.getBigDecimal("tweight"));
                row.put("fab", rs.getString("fab"));
                row.put("fabId", rs.getInt("fab_id"));
                row.put("supplierId", rs.getInt("supplier_id"));
                row.put("locfg_id", rs.getInt("locfg_id"));
                row.put("finishedGoodsLocation", rs.getString("finishedgoodslocation"));

                resultList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
