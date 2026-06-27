package com.JIMS.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/qc/inspection/report")
public class QCInspectionReportController {

    @Autowired
    @Qualifier("bellaryDataSource")
    private DataSource bellaryDataSource;

    @Autowired
    @Qualifier("gujaratDataSource")
    private DataSource gujaratDataSource;

    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource;

    private JdbcTemplate bellaryJdbcTemplate;
    private JdbcTemplate gujaratJdbcTemplate;
    @PostConstruct
    public void init() {

        bellaryJdbcTemplate = new JdbcTemplate(bellaryDataSource);
        gujaratJdbcTemplate = new JdbcTemplate(gujaratDataSource);
        new JdbcTemplate(misDataSource);
    }

    /**
     * Utility method to select factory DB
     */
    private JdbcTemplate getFactoryJdbc(String factory) {

        if ("1".equals(factory)) {   // Bellary
            return bellaryJdbcTemplate;
        } 
        else if ("2".equals(factory)) {  // Gujarat
            return gujaratJdbcTemplate;
        }

        throw new RuntimeException("Invalid factory selected");
    }

    /**
     * 1️⃣ Fetch completed welding contracts
     */
    @GetMapping("/contracts")
    public List<Map<String, Object>> getCompletedContracts(@RequestParam String factory) {

        String sql =
            "SELECT DISTINCT c.contract_id, c.fcontract + ' - ' + c.descr AS JobCode " +
            "FROM MIS.dbo.Contracts c " +
            "INNER JOIN JIMS.dbo.tab_qc_inspection qi ON c.contract_id = qi.contract_id " +
            "INNER JOIN JIMS.dbo.tab_qc_weld_inspection qw ON c.contract_id = qw.contract_id " +
            "WHERE c.contract_id <> '1' AND qw.status='Completed' " +
            "ORDER BY JobCode";

        if ("ALL".equalsIgnoreCase(factory)) {

            List<Map<String,Object>> bellary = bellaryJdbcTemplate.queryForList(sql);
            List<Map<String,Object>> gujarat = gujaratJdbcTemplate.queryForList(sql);

            bellary.addAll(gujarat);

            return bellary;
        }

        JdbcTemplate jdbc = getFactoryJdbc(factory);

        return jdbc.queryForList(sql);
    }

    /**
     * 2️⃣ Fetch pzone based on contract_id
     */
    @GetMapping("/pzone")
    public List<Map<String, Object>> getPzoneByContract(
            @RequestParam int contractId,
            @RequestParam String factory) {

        String sql =
                "SELECT DISTINCT l.pzone " +
                "FROM MIS.dbo.Loads l " +
                "INNER JOIN MIS.dbo.VW_JSSL_QC_Inspection_JIMS v ON l.load_id = v.load_id " +
                "WHERE v.contract_id = ? " +
                "ORDER BY l.pzone";

        if ("ALL".equalsIgnoreCase(factory)) {

            List<Map<String,Object>> bellary =
                    bellaryJdbcTemplate.queryForList(sql, contractId);

            List<Map<String,Object>> gujarat =
                    gujaratJdbcTemplate.queryForList(sql, contractId);

            bellary.addAll(gujarat);

            return bellary;
        }

        JdbcTemplate factoryJdbc = getFactoryJdbc(factory);

        return factoryJdbc.queryForList(sql, contractId);
    }

    /**
     * 3️⃣ Fetch load numbers
     */
    @GetMapping("/load/list")
    public List<Map<String, Object>> getLoads(
            @RequestParam int contractId,
            @RequestParam int pzone,
            @RequestParam String factory) {

        String sql =
                "SELECT DISTINCT load_id, pload AS load_no " +
                "FROM MIS.dbo.VW_JSSL_QC_Inspection_JIMS " +
                "WHERE contract_id = ? AND pzone = ? " +
                "ORDER BY load_no";

        if ("ALL".equalsIgnoreCase(factory)) {

            List<Map<String,Object>> bellary =
                    bellaryJdbcTemplate.queryForList(sql, contractId, pzone);

            List<Map<String,Object>> gujarat =
                    gujaratJdbcTemplate.queryForList(sql, contractId, pzone);

            bellary.addAll(gujarat);

            return bellary;
        }

        JdbcTemplate factoryJdbc = getFactoryJdbc(factory);

        return factoryJdbc.queryForList(sql, contractId, pzone);
    }

    /**
     * 4️⃣ Fetch QC inspection details
     */
    @GetMapping("/details")
    public List<Map<String, Object>> getInspectionDetails(
            @RequestParam int contractId,
            @RequestParam int pzone,
            @RequestParam int loadId,
            @RequestParam String factory) {

        String sql =
                "SELECT * " +
                "FROM MIS.dbo.VW_JSSL_QC_Inspection_JIMS " +
                "WHERE contract_id = ? " +
                "AND pzone = ? " +
                "AND load_id = ? " +
                "ORDER BY mark";

        if ("ALL".equalsIgnoreCase(factory)) {

            List<Map<String,Object>> bellary =
                    bellaryJdbcTemplate.queryForList(sql, contractId, pzone, loadId);

            List<Map<String,Object>> gujarat =
                    gujaratJdbcTemplate.queryForList(sql, contractId, pzone, loadId);

            bellary.addAll(gujarat);

            return bellary;
        }

        JdbcTemplate factoryJdbc = getFactoryJdbc(factory);

        return factoryJdbc.queryForList(sql, contractId, pzone, loadId);
    }
}