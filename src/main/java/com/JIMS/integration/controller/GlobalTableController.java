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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/global")
public class GlobalTableController {

    @Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;

    @GetMapping("/shift-rules")
    public @ResponseBody Map<String, Object> getShiftRules(@RequestParam String modal_id) {

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> rules = new ArrayList<>();

        String sql =
            "\r\n"
            + "SELECT LOCATION_ID, CONFIG_KEY, CONFIG_VALUE\r\n"
            + "            FROM GLOBAL_TABLE\r\n"
            + "            WHERE MODAL_ID = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // ✅ correct parameter setting
            ps.setString(1, modal_id);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> row = new HashMap<>();

                    row.put("location_id", rs.getInt("LOCATION_ID"));
                    row.put("config_key", rs.getString("CONFIG_KEY"));
                    row.put("config_value", rs.getString("CONFIG_VALUE"));

                    rules.add(row);
                }
            }

            response.put("Data", rules);

        } catch (Exception e) {

            response.put("error", "Unable to fetch shift rules");

        }

        return response;
    }
    
    @GetMapping("/config-value")
    public @ResponseBody Map<String, Object> getConfigValue(@RequestParam String config_key) {

        Map<String, Object> response = new HashMap<>();

        String sql = "SELECT CONFIG_VALUE FROM GLOBAL_TABLE WHERE CONFIG_KEY = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, config_key);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    response.put("config_value", rs.getString("CONFIG_VALUE"));
                } else {
                    response.put("config_value", null);
                }

            }

        } catch (Exception e) {

            response.put("error", "Unable to fetch config value");

        }

        return response;
    }
}