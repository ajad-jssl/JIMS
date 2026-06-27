package com.JIMS.integration.interfaces;

import com.JIMS.integration.entity.Consolidated_g2reports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class Consolidated_g2reportsService {

    @Autowired
    @Qualifier("misDataSource")
    private DataSource misDataSource;

    @Autowired
    @Qualifier("misDataSource2")
    private DataSource misDataSource2;

    // ── Query when a SPECIFIC contract is selected ──
    private static final String QUERY_BY_CONTRACT = """
            SELECT
                sub.contract,
                sub.descr,
                sub.contract_id,
                sub.JobCode,
                sub.loadtype,
                sub.pzone,
                SUM(sub.weight)                                           AS weight,
                SUM(sub.shaft_weight + sub.fittings_weight)               AS jsslwght,
                SUM(sub.shaft_subcon_weight + sub.fittings_subcon_weight) AS sublet,
                SUM(sub.asswght)                                          AS asswght,
                SUM(sub.asubletwght)                                      AS asubletwght,
                SUM(sub.asswght + sub.asubletwght)                        AS totalasw,
                SUM(sub.weldwght)                                         AS weldwght,
                SUM(sub.fsubletwght)                                      AS fsubletwght,
                SUM(sub.weldwght + sub.fsubletwght)                       AS totalwsw,
                SUM(sub.treatwght)                                        AS treatwght,
                SUM(sub.tsubletwght)                                      AS tsubletwght,
                SUM(sub.treatwght + sub.tsubletwght)                      AS totaltwght
            FROM (
                SELECT
                    v.treatwght, v.tsubletwght, v.asswght, v.weldwght,
                    v.asubletwght, v.fsubletwght,
                    c.contract, l.loadtype, c.descr,
                    l.shaft_subcon_weight, l.fittings_subcon_weight,
                    c.contract_id, l.pzone, l.weight, c.JobCode,
                    l.shaft_weight, l.fittings_weight
                FROM MIS.dbo.vw_JSSLsumLoadProgressWithSubletnewforrepot v
                INNER JOIN MIS.dbo.Loads l ON v.load_id = l.load_id
                INNER JOIN MIS.dbo.Contracts c ON l.contract_id = c.contract_id
                WHERE c.JobCode = ?        
                AND l.loadtype = ?
            ) sub
            GROUP BY
                sub.pzone, sub.contract, sub.descr,
                sub.contract_id, sub.JobCode, sub.loadtype
            ORDER BY
                sub.contract_id, sub.pzone
            """;

    // ── Query when ALL contracts are selected (no contract filter) ──
    private static final String QUERY_ALL_CONTRACTS = """
            SELECT
                sub.contract,
                sub.descr,
                sub.contract_id,
                sub.JobCode,
                sub.loadtype,
                sub.pzone,
                SUM(sub.weight)                                           AS weight,
                SUM(sub.shaft_weight + sub.fittings_weight)               AS jsslwght,
                SUM(sub.shaft_subcon_weight + sub.fittings_subcon_weight) AS sublet,
                SUM(sub.asswght)                                          AS asswght,
                SUM(sub.asubletwght)                                      AS asubletwght,
                SUM(sub.asswght + sub.asubletwght)                        AS totalasw,
                SUM(sub.weldwght)                                         AS weldwght,
                SUM(sub.fsubletwght)                                      AS fsubletwght,
                SUM(sub.weldwght + sub.fsubletwght)                       AS totalwsw,
                SUM(sub.treatwght)                                        AS treatwght,
                SUM(sub.tsubletwght)                                      AS tsubletwght,
                SUM(sub.treatwght + sub.tsubletwght)                      AS totaltwght
            FROM (
                SELECT
                    v.treatwght, v.tsubletwght, v.asswght, v.weldwght,
                    v.asubletwght, v.fsubletwght,
                    c.contract, l.loadtype, c.descr,
                    l.shaft_subcon_weight, l.fittings_subcon_weight,
                    c.contract_id, l.pzone, l.weight, c.JobCode,
                    l.shaft_weight, l.fittings_weight
                FROM MIS.dbo.vw_JSSLsumLoadProgressWithSubletnewforrepot v
                INNER JOIN MIS.dbo.Loads l ON v.load_id = l.load_id
                INNER JOIN MIS.dbo.Contracts c ON l.contract_id = c.contract_id
                WHERE l.loadtype = ?
            ) sub
            GROUP BY
                sub.pzone, sub.contract, sub.descr,
                sub.contract_id, sub.JobCode, sub.loadtype
            ORDER BY
                sub.contract_id, sub.pzone
            """;

    public List<Consolidated_g2reports> getReport(String jobCode, Integer loadtype, Integer factory) {

        DataSource dataSource = resolveDataSource(factory);
        boolean isAllContracts = "ALL".equalsIgnoreCase(jobCode);

        List<Consolidated_g2reports> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     isAllContracts ? QUERY_ALL_CONTRACTS : QUERY_BY_CONTRACT)) {

            if (isAllContracts) {
                ps.setInt(1, loadtype);                        // ALL: only loadtype
            } else {
            	ps.setString(1, jobCode);      // ✅ set as INT (contract_id)
                ps.setInt(2, loadtype);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing consolidated G2 report query: " + e.getMessage(), e);
        }

        return results;
    }

    private DataSource resolveDataSource(Integer factory) {
        return switch (factory) {
            case 1 -> misDataSource;
            case 2 -> misDataSource2;
            default -> throw new IllegalArgumentException(
                    "Invalid factory value: " + factory + ". Allowed values are 1 or 2.");
        };
    }

    private Consolidated_g2reports mapRow(ResultSet rs) throws SQLException {
        Consolidated_g2reports report = new Consolidated_g2reports();
        report.setContract(rs.getString("contract"));
        report.setDescr(rs.getString("descr"));
        report.setContract_id(rs.getInt("contract_id"));
        report.setJobCode(rs.getString("JobCode"));
        report.setLoadtype(rs.getInt("loadtype"));
        report.setPzone(rs.getString("pzone"));
        report.setWeight(rs.getDouble("weight"));
        report.setJsslwght(rs.getDouble("jsslwght"));
        report.setSublet(rs.getDouble("sublet"));
        report.setAsswght(rs.getDouble("asswght"));
        report.setAsubletwght(rs.getDouble("asubletwght"));
        report.setTotalasw(rs.getDouble("totalasw"));
        report.setWeldwght(rs.getDouble("weldwght"));
        report.setFsubletwght(rs.getDouble("fsubletwght"));
        report.setTotalwsw(rs.getDouble("totalwsw"));
        report.setTreatwght(rs.getDouble("treatwght"));
        report.setTsubletwght(rs.getDouble("tsubletwght"));
        report.setTotaltwght(rs.getDouble("totaltwght"));
        return report;
    }
}