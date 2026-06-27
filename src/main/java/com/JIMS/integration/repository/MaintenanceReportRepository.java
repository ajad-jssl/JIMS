package com.JIMS.integration.repository;

import com.JIMS.integration.entity.MaintenanceReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MaintenanceReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String REPORT_QUERY =
        "SELECT " +
        "    dbo.MAINTENANCE_TICKET.TICKET_ID, " +
        "    dbo.MAINTENANCE_TICKET.TICKET_NO, " +
        "    dbo.MAINTENANCE_MACHINE_DESCRIPTION.MACHINE_DESCRIPTION, " +
        "    dbo.MAINTENANCE_MACHINE_LIST.MACHINE_CODE, " +
        "    dbo.MAINTENANCE_MACHINE_LIST.MACHINE_SUBCODE, " +
        "    dbo.MAINTENANCE_TICKET.WHAT_BROKE, " +
        "    dbo.MAINTENANCE_TICKET.PROBLEM_DESC, " +
        "    Users_1.user_name AS [reported by], " +
        "    dbo.MAINTENANCE_TICKET.REPORTED_DATE, " +
        "    dbo.MAINTENANCE_TICKET.EMP_NAME, " +
        "    dbo.MAINTENANCE_TICKET.MOBILE_NO, " +
        "    dbo.MAINTENANCE_TICKET_ASSIGN.ASSIGNED_TO, " +
        "    MIS.dbo.Users.user_name AS assignedby, " +
        "    dbo.MAINTENANCE_TICKET_ASSIGN.ASSIGNED_DATE, " +
        "    dbo.MAINTENANCE_TICKET_ASSIGN.ASSIGN_NOTE " +
        "FROM MIS.dbo.Users " +
        "RIGHT OUTER JOIN dbo.MAINTENANCE_TICKET_ASSIGN " +
        "    ON MIS.dbo.Users.user_id = dbo.MAINTENANCE_TICKET_ASSIGN.ASSIGNED_BY " +
        "LEFT OUTER JOIN MIS.dbo.Users AS Users_1 " +
        "LEFT OUTER JOIN dbo.MAINTENANCE_TICKET " +
        "    ON Users_1.user_id = dbo.MAINTENANCE_TICKET.REPORTED_BY " +
        "    ON dbo.MAINTENANCE_TICKET_ASSIGN.TICKET_ID = dbo.MAINTENANCE_TICKET.TICKET_ID " +
        "LEFT OUTER JOIN dbo.MAINTENANCE_MACHINE_LIST " +
        "    ON dbo.MAINTENANCE_TICKET.MACHINE_ID = dbo.MAINTENANCE_MACHINE_LIST.MACHINE_ID " +
        "LEFT OUTER JOIN dbo.MAINTENANCE_MACHINE_DESCRIPTION " +
        "    ON dbo.MAINTENANCE_TICKET.MACHINE_DESC_ID = dbo.MAINTENANCE_MACHINE_DESCRIPTION.MACHINE_DESC_ID";

    /**
     * Fetch all maintenance report records.
     */
    public List<MaintenanceReportDTO> fetchAllReports() {
        return jdbcTemplate.query(REPORT_QUERY, new MaintenanceReportRowMapper());
    }

    /**
     * Fetch reports filtered by a date range on REPORTED_DATE.
     * CAST to DATE strips the time portion so same-day queries work correctly
     * even when the column stores a full datetime (e.g. 2026-06-09 15:21:35.137).
     */
    public List<MaintenanceReportDTO> fetchReportsByDateRange(String fromDate, String toDate) {
        String sql = REPORT_QUERY +
            " WHERE CAST(dbo.MAINTENANCE_TICKET.REPORTED_DATE AS DATE)" +
            "       BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
        return jdbcTemplate.query(sql, new MaintenanceReportRowMapper(), fromDate, toDate);
    }

    // ── RowMapper ──────────────────────────────────────────────────────────────

    private static class MaintenanceReportRowMapper implements RowMapper<MaintenanceReportDTO> {
        @Override
        public MaintenanceReportDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            MaintenanceReportDTO dto = new MaintenanceReportDTO();
            dto.setTicketId(rs.getLong("TICKET_ID"));
            dto.setTicketNo(rs.getString("TICKET_NO"));
            dto.setMachineDescription(rs.getString("MACHINE_DESCRIPTION"));
            dto.setMachineCode(rs.getString("MACHINE_CODE"));
            dto.setMachineSubcode(rs.getString("MACHINE_SUBCODE"));
            dto.setWhatBroke(rs.getString("WHAT_BROKE"));
            dto.setProblemDesc(rs.getString("PROBLEM_DESC"));
            dto.setReportedBy(rs.getString("reported by"));
            dto.setReportedDate(rs.getString("REPORTED_DATE"));
            dto.setEmpName(rs.getString("EMP_NAME"));
            dto.setMobileNo(rs.getString("MOBILE_NO"));
            dto.setAssignedTo(rs.getString("ASSIGNED_TO"));
            dto.setAssignedBy(rs.getString("assignedby"));
            dto.setAssignedDate(rs.getString("ASSIGNED_DATE"));
            dto.setAssignNote(rs.getString("ASSIGN_NOTE"));
            return dto;
        }
    }
}