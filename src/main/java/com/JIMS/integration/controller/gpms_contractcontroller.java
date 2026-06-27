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
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class gpms_contractcontroller {
	@Autowired
	@Qualifier("bellaryDataSource")
	private DataSource bellaryDataSource;

	@Autowired
	@Qualifier("gujaratDataSource")
	private DataSource gujaratDataSource;

    @Autowired
    @Qualifier("misDataSource") // Ensures the correct DataSource is injected
    private DataSource misDataSource;

    @GetMapping("/contracts/allContracts")
    public Map<String, Object> getContracts(@RequestParam("factory") int factory) throws SQLException {

        DataSource selectedDS;

        if (factory == 1) {
            selectedDS = bellaryDataSource;
        } else if (factory == 2) {
            selectedDS = gujaratDataSource;
        } else {
            // Default OR invalid factory handling
            throw new IllegalArgumentException("Invalid factory value. Use 1 for Bellary or 2 for Gujarat.");
        }

        String sql = "SELECT contract_id, JobCode FROM Contracts WHERE JobCode LIKE 'C%'";

        List<Map<String, String>> list = new ArrayList<>();
        try (Connection con = selectedDS.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("contract_id", rs.getString("contract_id"));
                map.put("JobCode", rs.getString("JobCode"));
                list.add(map);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("contracts", list);
        return result;
    }
}
