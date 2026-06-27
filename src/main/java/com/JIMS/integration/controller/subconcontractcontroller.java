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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class subconcontractcontroller {
	
    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource;

    @Autowired
    @Qualifier("misDataSource2")
    private DataSource misDataSource2;

    @GetMapping("/subconlistcontracts")
    public ResponseEntity<List<Map<String, Object>>> getContractsBySupplier(
            @RequestParam String supplierId,
            @RequestParam("factory_id") int factoryId) {

        DataSource selectedDataSource =
                (factoryId == 1) ? misDataSource : misDataSource2;

        String sql = """
            SELECT DISTINCT contract_id, cname
            FROM VW_JSSL_SubconContractLoad
            WHERE supplier_id = ?
            ORDER BY contract_id
        """;

        List<Map<String, Object>> result = new ArrayList<>();

        try (Connection con = selectedDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, supplierId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("contractId", rs.getInt("contract_id"));
                row.put("contractName", rs.getString("cname"));
                result.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(result);
    }
}
