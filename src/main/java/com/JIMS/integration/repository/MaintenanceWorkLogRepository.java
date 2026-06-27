package com.JIMS.integration.repository;

import com.JIMS.integration.entity.MaintenanceWorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface MaintenanceWorkLogRepository
        extends JpaRepository<MaintenanceWorkLog, Integer> {

    // ── START WORK ────────────────────────────────────────────────────────
    @Modifying @Transactional
    @Query(value =
        "INSERT INTO MAINTENANCE_WORK_LOG " +
        "(TICKET_ID, ASSIGN_ID, FACTORY_ID, WORKER_ID, " +
        " START_TIME, STATUS, PAUSED_MINUTES, LAST_HEARTBEAT, CREATED_DATE) " +
        "VALUES (:ticketId, :assignId, :factoryId, :workerId, " +
        "        GETDATE(), 1, 0, GETDATE(), GETDATE())",
        nativeQuery = true)
    void startWork(
        @Param("ticketId")  Integer ticketId,
        @Param("assignId")  Integer assignId,
        @Param("factoryId") Integer factoryId,
        @Param("workerId")  String  workerId);

    @Query(value =
        "SELECT TOP 1 LOG_ID FROM MAINTENANCE_WORK_LOG " +
        "WHERE ASSIGN_ID = :assignId AND WORKER_ID = :workerId " +
        "ORDER BY LOG_ID DESC",
        nativeQuery = true)
    Integer getLatestLogId(
        @Param("assignId") Integer assignId,
        @Param("workerId") String  workerId);

    // ── PAUSE WORK ────────────────────────────────────────────────────────
    // KEY FIX: immediately calculate and store net WORK_HOURS at pause time.
    // Formula: total elapsed seconds from START_TIME - already-paused seconds
    // This way any device can just read WORK_HOURS from DB for display.
    @Modifying @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORK_LOG " +
        "SET STATUS         = 5, " +
        "    PAUSE_TIME     = GETDATE(), " +
        "    LAST_HEARTBEAT = GETDATE(), " +
        // Update WORK_HOURS immediately = net work seconds so far / 3600
        "    WORK_HOURS     = CAST(" +
        "        CASE WHEN DATEDIFF(SECOND, START_TIME, GETDATE()) - (ISNULL(PAUSED_MINUTES,0) * 60) < 0 " +
        "             THEN 0 " +
        "             ELSE DATEDIFF(SECOND, START_TIME, GETDATE()) - (ISNULL(PAUSED_MINUTES,0) * 60) " +
        "        END AS DECIMAL(10,2)) / 3600.0, " +
        "    MODIFIED_BY    = :workerId, " +
        "    MODIFIED_DATE  = GETDATE() " +
        "WHERE LOG_ID = :logId AND STATUS = 1",
        nativeQuery = true)
    void pauseWork(
        @Param("logId")    Integer logId,
        @Param("workerId") String  workerId);

    // ── RESUME WORK ───────────────────────────────────────────────────────
    // KEY FIX: accumulate PAUSED_MINUTES = existing paused + minutes just spent on break.
    // After resume: WORK_HOURS stays as-is (snapshot at pause).
    // PAUSED_MINUTES is now correct cumulative total.
    // Any device: elapsed = DATEDIFF(SECOND, START_TIME, NOW) - PAUSED_MINUTES*60
    @Modifying @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORK_LOG " +
        "SET STATUS         = 1, " +
        "    PAUSED_MINUTES = ISNULL(PAUSED_MINUTES, 0) + DATEDIFF(MINUTE, PAUSE_TIME, GETDATE()), " +
        "    PAUSE_TIME     = NULL, " +
        "    LAST_HEARTBEAT = GETDATE(), " +
        "    MODIFIED_BY    = :workerId, " +
        "    MODIFIED_DATE  = GETDATE() " +
        "WHERE LOG_ID = :logId AND STATUS = 5",
        nativeQuery = true)
    void resumeWork(
        @Param("logId")    Integer logId,
        @Param("workerId") String  workerId);

    // ── MARK FIXED ────────────────────────────────────────────────────────
    // Final WORK_HOURS = net elapsed minutes / 60 (in decimal hours)
    @Modifying @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORK_LOG " +
        "SET END_TIME      = GETDATE(), " +
        "    WORK_HOURS    = CAST(" +
        "        CASE WHEN DATEDIFF(MINUTE, START_TIME, GETDATE()) - ISNULL(PAUSED_MINUTES,0) < 0 " +
        "             THEN 0 " +
        "             ELSE DATEDIFF(MINUTE, START_TIME, GETDATE()) - ISNULL(PAUSED_MINUTES,0) " +
        "        END AS DECIMAL(10,2)) / 60.0, " +
        "    WHAT_FIXED    = :whatFixed, " +
        "    REMARKS       = :remarks, " +
        "    STATUS        = 2, " +
        "    LAST_HEARTBEAT = GETDATE(), " +
        "    MODIFIED_BY   = :workerId, " +
        "    MODIFIED_DATE = GETDATE() " +
        "WHERE LOG_ID = :logId",
        nativeQuery = true)
    void markFixed(
        @Param("logId")     Integer logId,
        @Param("whatFixed") String  whatFixed,
        @Param("remarks")   String  remarks,
        @Param("workerId")  String  workerId);

    // ── CLOSE LOG ─────────────────────────────────────────────────────────
    @Modifying @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORK_LOG SET STATUS=3, " +
        "MODIFIED_BY=:closedBy, MODIFIED_DATE=GETDATE() WHERE LOG_ID=:logId",
        nativeQuery = true)
    void closeLog(@Param("logId") Integer logId, @Param("closedBy") Integer closedBy);

    // ── HEARTBEAT ─────────────────────────────────────────────────────────
    @Modifying @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORK_LOG SET LAST_HEARTBEAT=GETDATE() " +
        "WHERE WORKER_ID=:workerId AND STATUS IN(1,4) AND FACTORY_ID=:factoryId",
        nativeQuery = true)
    void updateHeartbeat(@Param("workerId") String workerId, @Param("factoryId") Integer factoryId);

    // ── GET ALL ACTIVE LOGS FOR WORKER ────────────────────────────────────
    // NET_ELAPSED_SECONDS: this formula works correctly on ALL devices because
    // it reads PAUSED_MINUTES (accumulated break time) directly from DB.
    // Device A paused at 10:00, resumed at 10:30 → PAUSED_MINUTES=30
    // Device B reads at 11:00 → elapsed = (11:00-9:00)*60 - 30*60 = 5400s = 1.5hrs ✓
    @Query(value =
        "SELECT WL.LOG_ID, WL.ASSIGN_ID, WL.TICKET_ID, " +
        "    WL.START_TIME, WL.STATUS AS LOG_STATUS, " +
        "    WL.PAUSED_MINUTES, WL.PAUSE_TIME, WL.LAST_HEARTBEAT, WL.WORK_HOURS, " +
        // NET_ELAPSED_SECONDS for running logs
        "    CASE WHEN WL.STATUS = 1 " +
        "         THEN DATEDIFF(SECOND, WL.START_TIME, GETDATE()) - (ISNULL(WL.PAUSED_MINUTES,0) * 60) " +
        // For paused logs, use snapshot WORK_HOURS stored at pause time
        "         ELSE CAST(ISNULL(WL.WORK_HOURS,0) * 3600 AS INT) " +
        "    END AS NET_ELAPSED_SECONDS, " +
        "    T.TICKET_NO, T.WHAT_BROKE, T.REPORTED_BY, T.STATUS AS TICKET_STATUS, " +
        "    A.ASSIGNED_BY, A.ASSIGN_ID AS A_ASSIGN_ID " +
        "FROM MAINTENANCE_WORK_LOG WL " +
        "JOIN MAINTENANCE_TICKET T        ON WL.TICKET_ID = T.TICKET_ID " +
        "JOIN MAINTENANCE_TICKET_ASSIGN A ON WL.ASSIGN_ID = A.ASSIGN_ID " +
        "WHERE WL.WORKER_ID=:workerId AND WL.FACTORY_ID=:factoryId AND WL.STATUS IN(1,5) " +
        "ORDER BY WL.LOG_ID ASC",
        nativeQuery = true)
    List<Map<String, Object>> getActiveLogsForWorker(
        @Param("workerId")  String  workerId,
        @Param("factoryId") Integer factoryId);

    // ── GET LOGS FOR TICKET ───────────────────────────────────────────────
    @Query(value =
        """
    		    SELECT WL.LOG_ID, WL.ASSIGN_ID, WL.WORKER_ID,       
               WL.START_TIME, WL.END_TIME, WL.WORK_HOURS, WL.PAUSED_MINUTES,       
               WL.WHAT_FIXED, WL.REMARKS, WL.STATUS,       
               M.employee_display_name AS WORKER_NAME       
           FROM MAINTENANCE_WORK_LOG WL       
           LEFT JOIN MaintenanceWorkerMapping M ON WL.WORKER_ID = M.Worker_EmpCode       
           WHERE WL.TICKET_ID=:ticketId 
		   ORDER BY WL.LOG_ID
    		""",
        nativeQuery = true)
    List<Map<String, Object>> getLogsForTicket(@Param("ticketId") Integer ticketId);
    
    @Query(value =
            """
SELECT 
    WL.LOG_ID, 
    WL.ASSIGN_ID, 
    WL.WORKER_ID,        
    WL.START_TIME, 
    WL.END_TIME, 
    WL.WORK_HOURS, 
    WL.PAUSED_MINUTES,        
    WL.WHAT_FIXED, 
    WL.REMARKS, 
    WL.STATUS,        
    M.employee_display_name AS WORKER_NAME,

    -- Current live hours for active logs (END_TIME is null = still working)
    CASE 
        WHEN WL.END_TIME IS NULL AND WL.START_TIME IS NOT NULL 
        THEN CAST(
            CASE 
                WHEN DATEDIFF(MINUTE, WL.START_TIME, GETDATE()) - ISNULL(WL.PAUSED_MINUTES, 0) < 0 
                THEN 0 
                ELSE DATEDIFF(MINUTE, WL.START_TIME, GETDATE()) - ISNULL(WL.PAUSED_MINUTES, 0)
            END AS DECIMAL(10,2)) / 60.0
        ELSE NULL
    END AS CURRENT_HOURS

FROM MAINTENANCE_WORK_LOG WL        
LEFT JOIN MaintenanceWorkerMapping M ON WL.WORKER_ID = M.Worker_EmpCode        
WHERE WL.TICKET_ID = :ticketId 
AND M.Machine_Category_Id = :machineDes    
ORDER BY WL.LOG_ID
        		""",
            nativeQuery = true)
        List<Map<String, Object>> getLogsForTicketgetDetils(@Param("ticketId") Integer ticketId,@Param("machineDes") Integer machineDes);

    // ── TOTAL HOURS FOR TICKET ────────────────────────────────────────────
    @Query(value =
        "SELECT ISNULL(SUM(WORK_HOURS),0) FROM MAINTENANCE_WORK_LOG " +
        "WHERE TICKET_ID=:ticketId AND STATUS IN(2,3)",
        nativeQuery = true)
    Object getTotalWorkHours(@Param("ticketId") Integer ticketId);

    // ── WORKER HOURS BREAKDOWN ────────────────────────────────────────────
    @Query(value =
        "SELECT WL.WORKER_ID, ISNULL(M.employee_display_name, WL.WORKER_ID) AS WORKER_NAME, " +
        "    SUM(WL.WORK_HOURS) AS TOTAL_HOURS, " +
        "    SUM(WL.PAUSED_MINUTES) AS TOTAL_PAUSED_MINUTES " +
        "FROM MAINTENANCE_WORK_LOG WL " +
        "LEFT JOIN MaintenanceWorkerMapping M ON WL.WORKER_ID = M.Worker_EmpCode " +
        "WHERE WL.TICKET_ID=:ticketId AND WL.STATUS IN(2,3) " +
        "GROUP BY WL.WORKER_ID, M.employee_display_name",
        nativeQuery = true)
    List<Map<String, Object>> getWorkerHoursForTicket(@Param("ticketId") Integer ticketId);

    // ── VERIFY WORKER ─────────────────────────────────────────────────────
    @Query(value =
        "SELECT Worker_EmpCode, employee_display_name " +
        "FROM MaintenanceWorkerMapping WHERE Worker_EmpCode=:empCode AND Is_Active=1 and Factory_Id=:factoryId",
        nativeQuery = true)
    List<Object[]> verifyWorker(@Param("empCode") String empCode,@Param("factoryId") String factoryId);

    // ── SNAPSHOT WORK HOURS AT TRANSFER (called before transfer completes) ─
    @Query(value =
        "SELECT ISNULL(WORK_HOURS,0) FROM MAINTENANCE_WORK_LOG WHERE LOG_ID=:logId",
        nativeQuery = true)
    Object getWorkHoursSnapshot(@Param("logId") Integer logId);

    // ── MARK LOG AS TRANSFERRED ───────────────────────────────────────────
    @Modifying @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORK_LOG SET STATUS=5, " +  // 5=Transferred
        "MODIFIED_BY=:workerId, MODIFIED_DATE=GETDATE() WHERE LOG_ID=:logId",
        nativeQuery = true)
    void markTransferred(@Param("logId") Integer logId, @Param("workerId") String workerId);
    
    @Transactional
    @Modifying
    @Query(value =
        "UPDATE MAINTENANCE_WORK_LOG " +
        "SET END_TIME      = GETDATE(), " +
        "    WORK_HOURS    = CAST(" +
        "        CASE WHEN DATEDIFF(MINUTE, START_TIME, GETDATE()) - ISNULL(PAUSED_MINUTES,0) < 0 " +
        "             THEN 0 " +
        "             ELSE DATEDIFF(MINUTE, START_TIME, GETDATE()) - ISNULL(PAUSED_MINUTES,0) " +
        "        END AS DECIMAL(10,2)) / 60.0, " +
        "    STATUS        = 4, " +
        "    LAST_HEARTBEAT = GETDATE(), " +
        "    MODIFIED_DATE = GETDATE() " +
        "WHERE LOG_ID = :logId",
        nativeQuery = true)
    void transferWorkLog(
            @Param("logId") Integer logId);
    
    @Query(
    	    value = "SELECT MACHINE_ID " +
    	            "FROM MAINTENANCE_TICKET " +
    	            "WHERE TICKET_ID = :ticketId",
    	    nativeQuery = true
    	)
    	Integer getMachineIdByTicketId(@Param("ticketId") Integer ticketId);
    
    

    @Query(
    	    value = "SELECT gim.itemid, " +
    	            "       gim.itmDescription1, " +
    	            "       gim.UM ,gim.ItemNo  " +
    	            "FROM MAINTENANCE_MACHINE_ITEMS mit " +
    	            "INNER JOIN GPMS_ITEMMASTER gim " +
    	            "    ON mit.id = gim.itemid " +
    	            "WHERE mit.status = 1 " +
    	            "  AND mit.machine_id = :machineId",
    	    nativeQuery = true
    	)
    	List<Map<String, Object>> getMachineItemsByMachineId(
    	        @Param("machineId") Integer machineId);


    @Modifying
    @Transactional
    @Query(value =
        "INSERT INTO MAINTENANCE_TICKET_PARTS_USED " +
        "(TICKET_ID, LOG_ID, ASSIGN_ID, WORKER_ID, FACTORY_ID, " +
        " ITEM_ID, ITEM_NAME, QUANTITY_USED, CREATED_DATE) " +
        "VALUES " +
        "(:ticketId, :logId, :assignId, :workerId, :factoryId, " +
        " :itemId, :itemName, :qty, GETDATE())",
        nativeQuery = true)
    void savePartsUsed(
            @Param("ticketId") Integer ticketId,
            @Param("logId") Integer logId,
            @Param("assignId") Integer assignId,
            @Param("workerId") String workerId,
            @Param("factoryId") Integer factoryId,
            @Param("itemId") String itemId,
            @Param("itemName") String itemName,
            @Param("qty") Double qty);
    
    
    
}