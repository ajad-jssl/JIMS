package com.JIMS.integration.repository;

import com.JIMS.integration.entity.MaintenanceTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface MaintenanceTicketRepository
        extends JpaRepository<MaintenanceTicket, Integer> {

    // ── RAISE TICKET ──────────────────────────────────────────────────────
    // whatBroke now holds a comma-separated list (up to 500 chars)
    // empCode, empName, mobileNo are the three new columns
    @Modifying
    @Transactional
    @Query(value =
        "INSERT INTO MAINTENANCE_TICKET " +
        "(TICKET_NO, FACTORY_ID, MACHINE_DESC_ID, MACHINE_ID, " +
        " WHAT_BROKE, PROBLEM_DESC, REPORTED_BY, " +
        " EMP_CODE, EMP_NAME, MOBILE_NO, " +
        " REPORTED_DATE, STATUS, CREATED_DATE) " +
        "VALUES (:ticketNo, :factoryId, :machineDescId, :machineId, " +
        "        :whatBroke, :problemDesc, :reportedBy, " +
        "        :empCode, :empName, :mobileNo, " +
        "        GETDATE(), 1, GETDATE())",
        nativeQuery = true)
    void raiseTicket(
        @Param("ticketNo")      String  ticketNo,
        @Param("factoryId")     Integer factoryId,
        @Param("machineDescId") Integer machineDescId,
        @Param("machineId")     Integer machineId,
        @Param("whatBroke")     String  whatBroke,
        @Param("problemDesc")   String  problemDesc,
        @Param("reportedBy")    Integer reportedBy,
        @Param("empCode")       String  empCode,
        @Param("empName")       String  empName,
        @Param("mobileNo")      String  mobileNo);

    // Returns the TICKET_ID of the last inserted row for this ticketNo
    @Query(value =
        "SELECT TOP 1 TICKET_ID FROM MAINTENANCE_TICKET " +
        "WHERE TICKET_NO = :ticketNo ORDER BY TICKET_ID DESC",
        nativeQuery = true)
    Integer getTicketIdByTicketNo(@Param("ticketNo") String ticketNo);

    // ── LIST TICKETS FOR RAISER (factory-scoped) ──────────────────────────
    @Query(value =
        "SELECT T.TICKET_ID, T.TICKET_NO, T.WHAT_BROKE, T.PROBLEM_DESC, " +
        "       T.REPORTED_DATE, T.STATUS, " +
        "       T.EMP_CODE, T.EMP_NAME, T.MOBILE_NO, " +
        "       MD.MACHINE_DESCRIPTION, ML.MACHINE_SUBCODE, " +
        "       U.USER_NAME AS REPORTER_NAME " +
        "FROM   MAINTENANCE_TICKET T " +
        "LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION MD " +
        "       ON T.MACHINE_DESC_ID = MD.MACHINE_DESC_ID " +
        "LEFT JOIN MAINTENANCE_MACHINE_LIST ML " +
        "       ON T.MACHINE_ID = ML.MACHINE_ID " +
        "LEFT JOIN USERS U ON T.REPORTED_BY = U.USER_ID " +
        "WHERE  T.FACTORY_ID = :factoryId " +
        "ORDER  BY T.TICKET_ID DESC",
        nativeQuery = true)
    List<Map<String, Object>> getTicketsByFactory(
        @Param("factoryId") Integer factoryId);

    // ── LIST TICKETS FOR SPECIFIC RAISER ──────────────────────────────────
    @Query(value =
        "SELECT T.TICKET_ID, T.TICKET_NO, T.WHAT_BROKE, T.PROBLEM_DESC, " +
        "       T.REPORTED_DATE, T.STATUS, " +
        "       T.EMP_CODE, T.EMP_NAME, T.MOBILE_NO, " +
        "       MD.MACHINE_DESCRIPTION, ML.MACHINE_SUBCODE " +
        "FROM   MAINTENANCE_TICKET T " +
        "LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION MD " +
        "       ON T.MACHINE_DESC_ID = MD.MACHINE_DESC_ID " +
        "LEFT JOIN MAINTENANCE_MACHINE_LIST ML " +
        "       ON T.MACHINE_ID = ML.MACHINE_ID " +
        "WHERE  T.FACTORY_ID = :factoryId " +
        "AND    T.REPORTED_BY = :userId " +
        "ORDER  BY T.TICKET_ID DESC",
        nativeQuery = true)
    List<Map<String, Object>> getTicketsByUser(
        @Param("factoryId") Integer factoryId,
        @Param("userId")    Integer userId);

    // ── TICKET DETAIL (for status-tracking popup) ─────────────────────────
    @Query(value = """
        SELECT T.TICKET_ID, T.TICKET_NO, T.WHAT_BROKE, T.PROBLEM_DESC,
               T.REPORTED_DATE, T.STATUS,
               T.EMP_CODE, T.EMP_NAME, T.MOBILE_NO,
               MD.MACHINE_DESCRIPTION, ML.MACHINE_SUBCODE,
               U.user_name AS REPORTER_NAME,
               T.MACHINE_DESC_ID,
               U.user_id   AS REPORTED_BY,
               (SELECT TOP 1 A.ASSIGNED_DATE FROM MAINTENANCE_TICKET_ASSIGN A
                WHERE A.TICKET_ID = T.TICKET_ID ORDER BY A.ASSIGN_ID) AS ASSIGNED_DATE,
               (SELECT TOP 1 WL.START_TIME FROM MAINTENANCE_WORK_LOG WL
                WHERE WL.TICKET_ID = T.TICKET_ID ORDER BY WL.LOG_ID) AS WORK_STARTED,
               (SELECT TOP 1 WL.END_TIME FROM MAINTENANCE_WORK_LOG WL
                WHERE WL.TICKET_ID = T.TICKET_ID ORDER BY WL.LOG_ID DESC) AS WORK_ENDED
        FROM   MAINTENANCE_TICKET T
        LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION MD
               ON T.MACHINE_DESC_ID = MD.MACHINE_DESC_ID
        LEFT JOIN MAINTENANCE_MACHINE_LIST ML
               ON T.MACHINE_ID = ML.MACHINE_ID
        LEFT JOIN mis.dbo.Users U ON T.REPORTED_BY = U.user_id
        WHERE  T.TICKET_ID = :ticketId
        """,
        nativeQuery = true)
    Map<String, Object> getTicketDetail(@Param("ticketId") Integer ticketId);

    // ── UPDATE TICKET STATUS ───────────────────────────────────────────────
    @Modifying
    @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_TICKET " +
        "SET STATUS = :status, MODIFIED_BY = :modifiedBy, MODIFIED_DATE = GETDATE() " +
        "WHERE TICKET_ID = :ticketId",
        nativeQuery = true)
    void updateTicketStatus(
        @Param("ticketId")    Integer ticketId,
        @Param("status")      Integer status,
        @Param("modifiedBy")  Integer modifiedBy);
    
    
    
    @Modifying
    @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_TICKET " +
        "SET STATUS = :status, MODIFIED_BY = :modifiedBy, MODIFIED_DATE = GETDATE(),CLOSE_REMARKS =:closeremarks,   CLOSED_DATE =GETDATE(),CLOSED_BY=:clsBy " +
        "WHERE TICKET_ID = :ticketId",
        nativeQuery = true)
    void updateTicketStatusClosed(
        @Param("ticketId")    Integer ticketId,
        @Param("status")      Integer status,
        @Param("modifiedBy")  Integer modifiedBy,@Param("closeremarks") String closeremarks,@Param("clsBy") String clsBy);

    // ── OPEN TICKETS FOR ASSIGNOR (status 1-4, factory-scoped) ────────────
//    @Query(value = """
//        SELECT T.TICKET_ID, T.TICKET_NO, T.WHAT_BROKE, T.PROBLEM_DESC,
//               T.REPORTED_DATE, T.STATUS,
//               T.EMP_CODE, T.EMP_NAME, T.MOBILE_NO,T.MACHINE_DESC_ID,
//               MD.MACHINE_DESCRIPTION, ML.MACHINE_SUBCODE,
//                T.EMP_NAME AS REPORTER_NAME,
//              T.EMP_CODE   AS REPORTED_BY
//        FROM   MAINTENANCE_TICKET T
//        LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION MD
//               ON T.MACHINE_DESC_ID = MD.MACHINE_DESC_ID
//        LEFT JOIN MAINTENANCE_MACHINE_LIST ML
//               ON T.MACHINE_ID = ML.MACHINE_ID
//        LEFT JOIN mis.dbo.Users U ON T.REPORTED_BY = U.USER_ID
//        WHERE  T.FACTORY_ID = :factoryId
//        AND    T.STATUS IN (1, 2, 3, 4,5)
//        ORDER  BY T.TICKET_ID DESC
//        """,
//        nativeQuery = true)
//    List<Map<String, Object>> getOpenTicketsForAssignor(
//        @Param("factoryId") Integer factoryId);
    
    
    
    
    @Query(value = """
    	    SELECT T.TICKET_ID, T.TICKET_NO, T.WHAT_BROKE, T.PROBLEM_DESC,
    	           T.REPORTED_DATE, T.STATUS,T.CLOSED_DATE,
    	           T.EMP_CODE, T.EMP_NAME, T.MOBILE_NO, T.MACHINE_DESC_ID,
    	           MD.MACHINE_DESCRIPTION, ML.MACHINE_SUBCODE,
    	           T.EMP_NAME AS REPORTER_NAME,
    	           T.EMP_CODE AS REPORTED_BY
    	    FROM   MAINTENANCE_TICKET T
    	    LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION MD ON T.MACHINE_DESC_ID = MD.MACHINE_DESC_ID
    	    LEFT JOIN MAINTENANCE_MACHINE_LIST ML        ON T.MACHINE_ID = ML.MACHINE_ID
    	    LEFT JOIN mis.dbo.Users U                    ON T.REPORTED_BY = U.USER_ID
    	    WHERE  T.FACTORY_ID = :factoryId
    	    AND    T.STATUS IN (1, 2, 3, 4, 5)
    	    ORDER  BY T.TICKET_ID DESC
    	    OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY
    	    """,
    	    nativeQuery = true)
    	List<Map<String, Object>> getOpenTicketsForAssignorPaged(
    	    @Param("factoryId") Integer factoryId,
    	    @Param("offset")    Integer offset,
    	    @Param("pageSize")  Integer pageSize);

    	@Query(value = """
    	    SELECT COUNT(*) FROM MAINTENANCE_TICKET T
    	    WHERE T.FACTORY_ID = :factoryId
    	    AND   T.STATUS IN (1, 2, 3, 4, 5)
    	    """,
    	    nativeQuery = true)
    	Integer countTicketsForAssignor(@Param("factoryId") Integer factoryId);
    


        @Query(value =
        	    """
        	    SELECT
        	        mwm.Worker_EmpCode,
        	        CASE
        	            WHEN ISNULL(
        	                STUFF(
        	                    (
        	                        SELECT ',' + md2.DESIGNATION_CODE
        	                        FROM MAINTENANCE_WORKER_DESIGNATION mwd2
        	                        INNER JOIN MAINTENANCE_DESIGNATION md2
        	                            ON md2.DESIGNATON_ID = mwd2.DESIGNATION_ID
        	                        WHERE mwd2.WORKER_MAPPING_ID = mwm.ID
        	                          AND mwd2.STATUS = 1
        	                        FOR XML PATH(''), TYPE
        	                    ).value('.', 'NVARCHAR(MAX)')
        	                ,1,1,'')
        	            ,'') = ''
        	            THEN mwm.employee_display_name
        	            ELSE mwm.employee_display_name + ' (' +
        	                STUFF(
        	                    (
        	                        SELECT ',' + md2.DESIGNATION_CODE
        	                        FROM MAINTENANCE_WORKER_DESIGNATION mwd2
        	                        INNER JOIN MAINTENANCE_DESIGNATION md2
        	                            ON md2.DESIGNATON_ID = mwd2.DESIGNATION_ID
        	                        WHERE mwd2.WORKER_MAPPING_ID = mwm.ID
        	                          AND mwd2.STATUS = 1
        	                        FOR XML PATH(''), TYPE
        	                    ).value('.', 'NVARCHAR(MAX)')
        	                ,1,1,'') + ')'
        	        END AS employee_display_name
        	    FROM MaintenanceWorkerMapping mwm
        	    WHERE mwm.Factory_Id = :factoryId
        	      AND mwm.Machine_Category_Id = :machineCategoryId
        	      AND mwm.Is_Active = 1
        	      AND NOT EXISTS (
        	            SELECT 1
        	            FROM MAINTENANCE_TICKET_ASSIGN mta
        	            WHERE mta.TICKET_ID = :ticketid
        	              AND mta.ASSIGNED_TO = mwm.Worker_EmpCode
        	             AND ISNULL(mta.STATUS, 0) NOT IN (5, 6)
        	      )
        	    ORDER BY mwm.employee_display_name
        	    """,
        	    nativeQuery = true)
        	List<Map<String, Object>> getWorkersByFactory(
        	    @Param("factoryId") String factoryId,
        	    @Param("machineCategoryId") String machineCategoryId,
        	    @Param("ticketid") String ticket_id);
    
    
    
    
    
    @Query(value =
           """
    		SELECT worker_email, employee_display_name    
               FROM   MaintenanceWorkerMapping    
      
                    where   Worker_EmpCode =:userId
    		"""

            ,nativeQuery = true)
        Map<String, Object> getWorkerEmailByUserId(@Param("userId") Integer userId);
}