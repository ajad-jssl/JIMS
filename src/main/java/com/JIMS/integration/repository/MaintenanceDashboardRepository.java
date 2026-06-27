package com.JIMS.integration.repository;

import com.JIMS.integration.entity.MaintenanceMachineSummaryDTO;
import com.JIMS.integration.entity.Maintenancecategorysummarydto;
import com.JIMS.integration.entity.TopBrokeCauseDTO;
import com.JIMS.integration.entity.MachineListDTO;
import com.JIMS.integration.entity.MachineTicketDetailDTO;
import com.JIMS.integration.entity.MachinecategoryListDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * MAINTENANCE_TICKET.STATUS:
 *   1 = Open
 *   2 = Assigned
 *   3 = In Progress
 *   4 = Fixed  (waiting for closer)
 *   5 = Closed
 *
 * "Transferred" = ticket has at least one MAINTENANCE_TICKET_ASSIGN row
 *                 with a non-null / non-empty TRANSFER_REASON.
 *
 * SQL Server fix: cannot use a subquery inside SUM().
 * Transfer flag is pre-computed in a CTE (for summary queries) or a
 * derived-table LEFT JOIN (for the detail query), so the outer GROUP BY
 * only ever SUM()s a plain integer column.
 */
@Repository
public class MaintenanceDashboardRepository {

    @PersistenceContext
    private EntityManager em;

    // ---------------------------------------------------------------
    // Summary by machine subcode
    // ---------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public List<MaintenanceMachineSummaryDTO> getSummaryByMachine(
            Integer factoryId, String machineSubcode, String dateFrom, String dateTo) {

        StringBuilder sql = new StringBuilder(
            "WITH cte_transfer AS ( " +
            "    SELECT TICKET_ID, " +
            "           CAST(CASE WHEN COUNT(CASE WHEN TRANSFER_REASON IS NOT NULL " +
            "                                      AND TRANSFER_REASON <> '' " +
            "                               THEN 1 END) > 0 " +
            "                THEN 1 ELSE 0 END AS INT) AS IS_TRANSFERRED " +
            "    FROM MAINTENANCE_TICKET_ASSIGN " +
            "    GROUP BY TICKET_ID " +
            ") " +
            "SELECT ml.MACHINE_SUBCODE, " +
            "       md.MACHINE_DESCRIPTION, " +
            "       COUNT(DISTINCT mt.TICKET_ID)                                            AS TOTAL_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS = 1      THEN 1 ELSE 0 END)                     AS OPEN_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS = 2      THEN 1 ELSE 0 END)                     AS ASSIGNED_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS IN (3,4) THEN 1 ELSE 0 END)                     AS WIP_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS = 5      THEN 1 ELSE 0 END)                     AS CLOSED_COUNT, " +
            "       SUM(ISNULL(tr.IS_TRANSFERRED, 0))                                       AS TRANSFERRED_COUNT, " +
            "       ISNULL(SUM(CASE WHEN mt.STATUS = 1 THEN " +
            "           CASE WHEN mt.MODIFIED_DATE IS NOT NULL " +
            "                THEN ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,mt.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "                ELSE ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,GETDATE()) AS FLOAT)/60,2) " +
            "           END ELSE 0 END), 0.0)                                               AS OPEN_DOWNTIME_HOURS, " +
            "       ISNULL(SUM(CASE WHEN mt.STATUS = 2 THEN " +
            "           CASE WHEN mt.MODIFIED_DATE IS NOT NULL " +
            "                THEN ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,mt.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "                ELSE ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,GETDATE()) AS FLOAT)/60,2) " +
            "           END ELSE 0 END), 0.0)                                               AS ASSIGNED_DOWNTIME_HOURS, " +
            "       ISNULL(SUM(CASE WHEN mt.STATUS IN (3,4) THEN " +
            "           CASE WHEN mt.MODIFIED_DATE IS NOT NULL " +
            "                THEN ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,mt.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "                ELSE ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,GETDATE()) AS FLOAT)/60,2) " +
            "           END ELSE 0 END), 0.0)                                               AS WIP_DOWNTIME_HOURS, " +
            "       ISNULL(SUM(CASE WHEN mt.STATUS = 5 THEN " +
            "           CASE WHEN mt.MODIFIED_DATE IS NOT NULL " +
            "                THEN ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,mt.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "                ELSE ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,GETDATE()) AS FLOAT)/60,2) " +
            "           END ELSE 0 END), 0.0)                                               AS CLOSED_DOWNTIME_HOURS, " +
            "       ISNULL(SUM( " +
            "           CASE WHEN mt.MODIFIED_DATE IS NOT NULL " +
            "                THEN ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,mt.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "                ELSE ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,GETDATE())        AS FLOAT)/60,2) " +
            "           END), 0.0)                                                           AS TOTAL_DOWNTIME_HOURS " +
            "FROM MAINTENANCE_TICKET mt " +
            "LEFT JOIN MAINTENANCE_MACHINE_LIST ml        ON ml.MACHINE_ID      = mt.MACHINE_ID " +
            "LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION md ON md.MACHINE_DESC_ID = mt.MACHINE_DESC_ID " +
            "LEFT JOIN cte_transfer tr                    ON tr.TICKET_ID       = mt.TICKET_ID " +
            "WHERE 1=1 "
        );
        appendFilters(sql, factoryId, machineSubcode, dateFrom, dateTo);
        sql.append(" GROUP BY ml.MACHINE_SUBCODE, md.MACHINE_DESCRIPTION ORDER BY TOTAL_COUNT DESC ");

        var query = em.createNativeQuery(sql.toString());
        bindFilters(query, factoryId, machineSubcode, dateFrom, dateTo);

        List<Object[]> rows = query.getResultList();
        List<MaintenanceMachineSummaryDTO> result = new ArrayList<>();
        for (Object[] r : rows) {
            result.add(new MaintenanceMachineSummaryDTO(
                str(r[0]),       // machineSubcode
                str(r[1]),       // machineDescription
                toLong(r[2]),    // totalCount
                toLong(r[3]),    // openCount
                toLong(r[4]),    // assignedCount
                toLong(r[5]),    // wipCount
                toLong(r[6]),    // closedCount
                toLong(r[7]),    // transferredCount
                0L,              // escalatedCount
                toDouble(r[8]),  // openDowntimeHours
                toDouble(r[9]),  // assignedDowntimeHours
                toDouble(r[10]), // wipDowntimeHours
                toDouble(r[11]), // closedDowntimeHours
                toDouble(r[12])  // totalDowntimeHours
            ));
        }
        return result;
    }

    // ---------------------------------------------------------------
    // Grand total
    // ---------------------------------------------------------------
    public MaintenanceMachineSummaryDTO getGrandTotal(
            Integer factoryId, String machineSubcode, String dateFrom, String dateTo) {

        StringBuilder sql = new StringBuilder(
            "WITH cte_transfer AS ( " +
            "    SELECT TICKET_ID, " +
            "           CAST(CASE WHEN COUNT(CASE WHEN TRANSFER_REASON IS NOT NULL " +
            "                                      AND TRANSFER_REASON <> '' " +
            "                               THEN 1 END) > 0 " +
            "                THEN 1 ELSE 0 END AS INT) AS IS_TRANSFERRED " +
            "    FROM MAINTENANCE_TICKET_ASSIGN " +
            "    GROUP BY TICKET_ID " +
            ") " +
            "SELECT COUNT(DISTINCT mt.TICKET_ID)                                           AS TOTAL_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS = 1        THEN 1 ELSE 0 END)                  AS OPEN_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS IN (2,3,4) THEN 1 ELSE 0 END)                  AS WIP_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS = 5        THEN 1 ELSE 0 END)                  AS CLOSED_COUNT, " +
            "       SUM(ISNULL(tr.IS_TRANSFERRED, 0))                                      AS TRANSFERRED_COUNT, " +
            "       ISNULL(SUM( " +
            "           CASE WHEN mt.MODIFIED_DATE IS NOT NULL " +
            "                THEN ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,mt.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "                ELSE ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,GETDATE())        AS FLOAT)/60,2) " +
            "           END), 0.0)                                                          AS TOTAL_DOWNTIME_HOURS " +
            "FROM MAINTENANCE_TICKET mt " +
            "LEFT JOIN MAINTENANCE_MACHINE_LIST ml ON ml.MACHINE_ID = mt.MACHINE_ID " +
            "LEFT JOIN cte_transfer tr              ON tr.TICKET_ID = mt.TICKET_ID " +
            "WHERE 1=1 "
        );
        appendFilters(sql, factoryId, machineSubcode, dateFrom, dateTo);

        var query = em.createNativeQuery(sql.toString());
        bindFilters(query, factoryId, machineSubcode, dateFrom, dateTo);

        Object[] r = (Object[]) query.getSingleResult();
        return new MaintenanceMachineSummaryDTO(
            toLong(r[0]),    // totalCount
            toLong(r[1]),    // openCount
            toLong(r[2]),    // wipCount
            toLong(r[3]),    // closedCount
            toLong(r[4]),    // transferredCount
            0L,              // escalatedCount
            toDouble(r[5])   // totalDowntimeHours
        );
    }

    // ---------------------------------------------------------------
    // Summary by category
    // ---------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public List<Maintenancecategorysummarydto> getSummaryByCategory(
            Integer factoryId, String machineSubcode, String dateFrom, String dateTo) {

        StringBuilder sql = new StringBuilder(
            "WITH cte_transfer AS ( " +
            "    SELECT TICKET_ID, " +
            "           CAST(CASE WHEN COUNT(CASE WHEN TRANSFER_REASON IS NOT NULL " +
            "                                      AND TRANSFER_REASON <> '' " +
            "                               THEN 1 END) > 0 " +
            "                THEN 1 ELSE 0 END AS INT) AS IS_TRANSFERRED " +
            "    FROM MAINTENANCE_TICKET_ASSIGN " +
            "    GROUP BY TICKET_ID " +
            "), " +
            "cte_worklog AS ( " +
            "    SELECT TICKET_ID, " +
            "           SUM(ISNULL(WORK_HOURS,0)) AS TOTAL_WORK_HOURS " +
            "    FROM MAINTENANCE_WORK_LOG " +
            "    GROUP BY TICKET_ID " +
            ") " +
            "SELECT md.MACHINE_DESC_ID, " +
            "       md.MACHINE_DESCRIPTION, " +
            "       COUNT(DISTINCT mt.TICKET_ID)                                           AS TOTAL_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS = 1        THEN 1 ELSE 0 END)                  AS OPEN_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS IN (2,3,4) THEN 1 ELSE 0 END)                  AS WIP_COUNT, " +
            "       SUM(CASE WHEN mt.STATUS = 5        THEN 1 ELSE 0 END)                  AS CLOSED_COUNT, " +
            "       SUM(ISNULL(tr.IS_TRANSFERRED, 0))                                      AS TRANSFERRED_COUNT, " +
            "       ISNULL(SUM( " +
            "           CASE WHEN mt.MODIFIED_DATE IS NOT NULL " +
            "                THEN ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,mt.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "                ELSE ROUND(CAST(DATEDIFF(MINUTE,mt.REPORTED_DATE,GETDATE())        AS FLOAT)/60,2) " +
            "           END), 0.0)                                                          AS TOTAL_DOWNTIME_HOURS, " +
            "       ISNULL(SUM(ISNULL(wl.TOTAL_WORK_HOURS, 0.0)), 0.0)                      AS TOTAL_MANHOURS " +
            "FROM MAINTENANCE_TICKET mt " +
            "LEFT JOIN MAINTENANCE_MACHINE_LIST ml        ON ml.MACHINE_ID      = mt.MACHINE_ID " +
            "LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION md ON md.MACHINE_DESC_ID = mt.MACHINE_DESC_ID " +
            "LEFT JOIN cte_transfer tr                    ON tr.TICKET_ID       = mt.TICKET_ID " +
            "LEFT JOIN cte_worklog wl                     ON wl.TICKET_ID       = mt.TICKET_ID " +
            "WHERE 1=1 "
        );
        appendFilters(sql, factoryId, machineSubcode, dateFrom, dateTo);
        sql.append(" GROUP BY md.MACHINE_DESC_ID, md.MACHINE_DESCRIPTION ORDER BY TOTAL_COUNT DESC ");

        var query = em.createNativeQuery(sql.toString());
        bindFilters(query, factoryId, machineSubcode, dateFrom, dateTo);

        List<Object[]> rows = query.getResultList();
        List<Maintenancecategorysummarydto> result = new ArrayList<>();
        for (Object[] r : rows) {
            result.add(new Maintenancecategorysummarydto(
                toInt(r[0]),     // machineDescId
                str(r[1]),       // machineDescription
                toLong(r[2]),    // totalCount
                toLong(r[3]),    // openCount
                toLong(r[4]),    // wipCount
                toLong(r[5]),    // closedCount
                toLong(r[6]),    // transferredCount
                0L,              // escalatedCount
                toDouble(r[7]),  // totalDowntimeHours
                toDouble(r[8])   // totalManHours  <-- NEW
            ));
        }
        return result;
    }
    // ---------------------------------------------------------------
    // Machine list for dropdown
    // ---------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public List<MachineListDTO> getMachineList(Integer factoryId, String category) {
        StringBuilder sql = new StringBuilder(
            "SELECT ml.MACHINE_SUBCODE, md.MACHINE_DESCRIPTION " +
            "FROM MAINTENANCE_MACHINE_LIST ml " +
            "LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION md ON md.MACHINE_DESC_ID = ml.MACHINE_DESCRIPTION " +
            "WHERE 1=1 "
        );
        if (factoryId != null)
            sql.append(" AND ml.FACTORY_ID = :factoryId ");
        if (category != null && !category.isEmpty())
            sql.append(" AND ml.MACHINE_DESCRIPTION = :category ");
        sql.append(" ORDER BY ml.MACHINE_SUBCODE ");

        var query = em.createNativeQuery(sql.toString());
        if (factoryId != null)
            query.setParameter("factoryId", factoryId);
        if (category != null && !category.isEmpty())
            query.setParameter("category", Integer.parseInt(category));

        List<Object[]> rows = query.getResultList();
        List<MachineListDTO> result = new ArrayList<>();
        for (Object[] r : rows) {
            result.add(new MachineListDTO(str(r[0]), str(r[1])));
        }
        return result;
    }

    // ---------------------------------------------------------------
    // Category list for dropdown
    // ---------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public List<MachinecategoryListDTO> getMachinecategoryList(Integer factoryId) {

        String sql =
            "SELECT MACHINE_DESC_ID, MACHINE_DESCRIPTION, STATUS " +
            "FROM MAINTENANCE_MACHINE_DESCRIPTION " +
            "ORDER BY MACHINE_DESCRIPTION";

        var query = em.createNativeQuery(sql);

        List<Object[]> rows = query.getResultList();
        List<MachinecategoryListDTO> result = new ArrayList<>();

        for (Object[] r : rows) {
            result.add(new MachinecategoryListDTO(
                Integer.parseInt(str(r[0])),
                str(r[1]),
                "1".equals(str(r[2])) || "true".equalsIgnoreCase(str(r[2]))
            ));
        }

        return result;
    }

    // ---------------------------------------------------------------
    // Tickets by machine — detail panel
    // ---------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public List<MachineTicketDetailDTO> getTicketsByMachine(
            String machineSubcode, Integer factoryId, String dateFrom, String dateTo) {

        StringBuilder sql = new StringBuilder(
            "SELECT t.TICKET_NO, ml.MACHINE_SUBCODE, md.MACHINE_DESCRIPTION, " +
            "    t.WHAT_BROKE, t.PROBLEM_DESC, t.REPORTED_BY, " +
            "    t.REPORTED_DATE, t.MODIFIED_DATE, t.STATUS, " +
            "    t.EMP_NAME, t.MOBILE_NO, " +
            "    CASE WHEN t.MODIFIED_DATE IS NOT NULL " +
            "         THEN ROUND(CAST(DATEDIFF(MINUTE,t.REPORTED_DATE,t.MODIFIED_DATE) AS FLOAT)/60,2) " +
            "         ELSE ROUND(CAST(DATEDIFF(MINUTE,t.REPORTED_DATE,GETDATE())        AS FLOAT)/60,2) " +
            "    END AS DOWNTIME_HOURS, " +
            "    ISNULL(tc.TRANSFER_COUNT, 0) AS TRANSFER_COUNT " +
            "FROM MAINTENANCE_TICKET t " +
            "JOIN MAINTENANCE_MACHINE_LIST ml        ON ml.MACHINE_ID      = t.MACHINE_ID " +
            "JOIN MAINTENANCE_MACHINE_DESCRIPTION md ON md.MACHINE_DESC_ID = t.MACHINE_DESC_ID " +
            // Pre-aggregated derived table — no subquery inside aggregate
            "LEFT JOIN ( " +
            "    SELECT TICKET_ID, COUNT(*) AS TRANSFER_COUNT " +
            "    FROM   MAINTENANCE_TICKET_ASSIGN " +
            "    WHERE  TRANSFER_REASON IS NOT NULL AND TRANSFER_REASON <> '' " +
            "    GROUP BY TICKET_ID " +
            ") tc ON tc.TICKET_ID = t.TICKET_ID " +
            "WHERE 1=1 "
        );

        if (machineSubcode != null && !machineSubcode.isEmpty())
            sql.append(" AND ml.MACHINE_SUBCODE = :machineSubcode ");
        if (factoryId != null)
            sql.append(" AND t.FACTORY_ID = :factoryId ");
        if (dateFrom != null && !dateFrom.isEmpty())
            sql.append(" AND CAST(t.REPORTED_DATE AS DATE) >= :dateFrom ");
        if (dateTo != null && !dateTo.isEmpty())
            sql.append(" AND CAST(t.REPORTED_DATE AS DATE) <= :dateTo ");

        sql.append(" ORDER BY t.REPORTED_DATE DESC ");

        var query = em.createNativeQuery(sql.toString());
        if (machineSubcode != null && !machineSubcode.isEmpty())
            query.setParameter("machineSubcode", machineSubcode);
        if (factoryId != null)
            query.setParameter("factoryId", factoryId);
        if (dateFrom != null && !dateFrom.isEmpty())
            query.setParameter("dateFrom", dateFrom);
        if (dateTo != null && !dateTo.isEmpty())
            query.setParameter("dateTo", dateTo);

        List<Object[]> rows = query.getResultList();
        List<MachineTicketDetailDTO> result = new ArrayList<>();
        for (Object[] r : rows) {
            result.add(new MachineTicketDetailDTO(
                str(r[0]),                                             // ticketNo
                str(r[1]),                                             // machineSubcode
                str(r[2]),                                             // machineDescription
                str(r[3]),                                             // whatBroke
                str(r[4]),                                             // problemDesc
                str(r[5]),                                             // reportedBy
                (java.util.Date) r[6],                                 // reportedDate
                (java.util.Date) r[7],                                 // modifiedDate
                str(r[8]),                                             // status "1"–"5"
                str(r[9]),                                             // empName
                str(r[10]),                                            // mobileNo
                r[11] != null ? ((Number) r[11]).doubleValue() : 0.0,  // downtimeHours
                r[12] != null ? ((Number) r[12]).intValue()    : 0     // transferCount
            ));
        }
        return result;
    }

 // ---------------------------------------------------------------
 // Top broke causes — splits WHAT_BROKE comma-separated field,
 // counts occurrences, classifies into elec/mech/hydr,
 // returns top 5 per category
 // ---------------------------------------------------------------
 @SuppressWarnings("unchecked")
 public java.util.Map<String, List<TopBrokeCauseDTO>> getTopBrokeCauses(
         Integer factoryId, String dateFrom, String dateTo) {

     // All known items mapped to their category
     java.util.Map<String, String> categoryMap = new java.util.LinkedHashMap<>();
     // Electrical
     categoryMap.put("panel / wiring",   "elec");
     categoryMap.put("sensor",           "elec");
     categoryMap.put("motor",            "elec");
     categoryMap.put("plc / control",    "elec");
     categoryMap.put("vfd / drive",      "elec");
     categoryMap.put("breaker / fuse",   "elec");
     categoryMap.put("wiring fault",     "elec");
     categoryMap.put("other (elec)",     "elec");
     // Mechanical
     categoryMap.put("belt / chain",     "mech");
     categoryMap.put("gear / bearing",   "mech");
     categoryMap.put("cylinder",         "mech");
     categoryMap.put("tool / spindle",   "mech");
     categoryMap.put("coupling / shaft", "mech");
     categoryMap.put("frame / structure","mech");
     categoryMap.put("lubrication",      "mech");
     categoryMap.put("other (mech)",     "mech");
     // Hydraulics
     categoryMap.put("pump",             "hydr");
     categoryMap.put("valve",            "hydr");
     categoryMap.put("hose / pipe",      "hydr");
     categoryMap.put("coolant system",   "hydr");
     categoryMap.put("hyd. cylinder",    "hydr");
     categoryMap.put("seal / gasket",    "hydr");
     categoryMap.put("accumulator",      "hydr");
     categoryMap.put("other (hydr)",     "hydr");

     StringBuilder sql = new StringBuilder(
         "SELECT LTRIM(RTRIM(s.value)) AS ITEM, COUNT(*) AS CNT " +
         "FROM MAINTENANCE_TICKET mt " +
         "CROSS APPLY STRING_SPLIT(mt.WHAT_BROKE, ',') s " +
         "WHERE mt.WHAT_BROKE IS NOT NULL " +
         "  AND LTRIM(RTRIM(s.value)) <> '' "
     );
     if (factoryId != null)
         sql.append(" AND mt.FACTORY_ID = :factoryId ");
     if (dateFrom != null && !dateFrom.isEmpty())
         sql.append(" AND CAST(mt.REPORTED_DATE AS DATE) >= :dateFrom ");
     if (dateTo != null && !dateTo.isEmpty())
         sql.append(" AND CAST(mt.REPORTED_DATE AS DATE) <= :dateTo ");
     sql.append(" GROUP BY LTRIM(RTRIM(s.value)) ORDER BY CNT DESC ");

     var query = em.createNativeQuery(sql.toString());
     if (factoryId != null)
         query.setParameter("factoryId", factoryId);
     if (dateFrom != null && !dateFrom.isEmpty())
         query.setParameter("dateFrom", dateFrom);
     if (dateTo != null && !dateTo.isEmpty())
         query.setParameter("dateTo", dateTo);

     List<Object[]> rows = query.getResultList();

     // Group into categories, top 5 each
     java.util.Map<String, List<TopBrokeCauseDTO>> result = new java.util.LinkedHashMap<>();
     result.put("elec", new ArrayList<>());
     result.put("mech", new ArrayList<>());
     result.put("hydr", new ArrayList<>());

     for (Object[] r : rows) {
         String itemName = str(r[0]);
         int    cnt      = ((Number) r[1]).intValue();
         String cat      = categoryMap.get(itemName.toLowerCase());
         if (cat == null) continue;
         List<TopBrokeCauseDTO> list = result.get(cat);
         if (list.size() < 5) {
             list.add(new TopBrokeCauseDTO(cat, itemName, cnt));
         }
     }
     return result;
 }
    
    // ---------------------------------------------------------------
    // Shared filter helpers
    // ---------------------------------------------------------------
    private void appendFilters(StringBuilder sql, Integer factoryId,
            String machineSubcode, String dateFrom, String dateTo) {
        if (factoryId != null)
            sql.append(" AND mt.FACTORY_ID = :factoryId ");
        if (machineSubcode != null && !machineSubcode.isEmpty())
            sql.append(" AND ml.MACHINE_SUBCODE = :machineSubcode ");
        if (dateFrom != null && !dateFrom.isEmpty())
            sql.append(" AND CAST(mt.REPORTED_DATE AS DATE) >= :dateFrom ");
        if (dateTo != null && !dateTo.isEmpty())
            sql.append(" AND CAST(mt.REPORTED_DATE AS DATE) <= :dateTo ");
    }

    private void bindFilters(jakarta.persistence.Query query, Integer factoryId,
            String machineSubcode, String dateFrom, String dateTo) {
        if (factoryId != null)
            query.setParameter("factoryId", factoryId);
        if (machineSubcode != null && !machineSubcode.isEmpty())
            query.setParameter("machineSubcode", machineSubcode);
        if (dateFrom != null && !dateFrom.isEmpty())
            query.setParameter("dateFrom", dateFrom);
        if (dateTo != null && !dateTo.isEmpty())
            query.setParameter("dateTo", dateTo);
    }

    // ---------------------------------------------------------------
    // Type helpers
    // ---------------------------------------------------------------
    private String str(Object o)      { return o != null ? o.toString().trim() : ""; }
    private long   toLong(Object o)   { return o != null ? ((Number) o).longValue()   : 0L;  }
    private int    toInt(Object o)    { return o != null ? ((Number) o).intValue()    : 0;   }
    private double toDouble(Object o) { return o != null ? ((Number) o).doubleValue() : 0.0; }
}