package com.JIMS.integration.repository;

import com.JIMS.integration.entity.MaintenanceTicketAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface MaintenanceTicketAssignRepository
        extends JpaRepository<MaintenanceTicketAssign, Integer> {

    // ── ASSIGN A TICKET TO A WORKER ───────────────────────────────────────
//    @Modifying
//    @Transactional
//    @Query(value =
//        "INSERT INTO MAINTENANCE_TICKET_ASSIGN " +
//        "(TICKET_ID, FACTORY_ID, ASSIGNED_TO, ASSIGNED_BY, " +
//        " ASSIGNED_DATE, ASSIGN_NOTE, STATUS, CREATED_DATE) " +
//        "VALUES (:ticketId, :factoryId, :assignedTo, :assignedBy, " +
//        "        GETDATE(), :assignNote, 1, GETDATE())",
//        nativeQuery = true)
//    void assignTicket(
//        @Param("ticketId")   Integer ticketId,
//        @Param("factoryId")  Integer factoryId,
//        @Param("assignedTo") Integer assignedTo,
//        @Param("assignedBy") Integer assignedBy,
//        @Param("assignNote") String  assignNote);
	
	
	
	
	   @Modifying
	    @Transactional
	    @Query(value =
	        "INSERT INTO MAINTENANCE_TICKET_ASSIGN " +
	        "(TICKET_ID, FACTORY_ID, ASSIGNED_TO, ASSIGNED_BY, " +
	        " ASSIGNED_DATE, ASSIGN_NOTE, STATUS, CREATED_DATE) " +
	        "OUTPUT INSERTED.ASSIGN_ID " +     // ← THIS LINE IS THE ONLY REAL CHANGE
	        "VALUES (:ticketId, :factoryId, :assignedTo, :assignedBy, " +
	        "        GETDATE(), :assignNote, 1, GETDATE())",
	        nativeQuery = true)
	    Integer assignTicket(    // ← changed from void to Integer
	        @Param("ticketId")   Integer ticketId,
	        @Param("factoryId")  Integer factoryId,
	        @Param("assignedTo") Integer assignedTo,
	        @Param("assignedBy") Integer assignedBy,
	        @Param("assignNote") String  assignNote);
    
    
    

    // ── GET ASSIGNMENTS FOR A TICKET ─────────────────────────────────────
    @Query(value =
        """
    	
		  SELECT A.ASSIGN_ID, A.TICKET_ID, A.ASSIGNED_DATE, A.ASSIGN_NOTE, A.STATUS,        
                  U.employee_display_name AS WORKER_NAME,        
                  UB.USER_NAME AS ASSIGNED_BY_NAME        
           FROM   MAINTENANCE_TICKET_ASSIGN A        
         LEFT JOIN MaintenanceWorkerMapping  U  ON A.ASSIGNED_TO = U.Worker_EmpCode        
           LEFT JOIN mis.dbo.Users  UB ON A.ASSIGNED_BY = UB.USER_ID        
           WHERE  A.TICKET_ID = :ticketId and a.STATUS !=6
           ORDER  BY A.ASSIGN_ID 
    		
    		""",
        nativeQuery = true)
    List<Map<String, Object>> getAssignmentsForTicket(
        @Param("ticketId") Integer ticketId);
    
    
    @Query(value =
            """
        	
    		  SELECT A.ASSIGN_ID, A.TICKET_ID, A.ASSIGNED_DATE, A.ASSIGN_NOTE, A.STATUS,        
                      U.employee_display_name AS WORKER_NAME,        
                      UB.USER_NAME AS ASSIGNED_BY_NAME        
               FROM   MAINTENANCE_TICKET_ASSIGN A        
             LEFT JOIN MaintenanceWorkerMapping  U  ON A.ASSIGNED_TO = U.Worker_EmpCode        
               LEFT JOIN mis.dbo.Users  UB ON A.ASSIGNED_BY = UB.USER_ID        
               WHERE  A.TICKET_ID = :ticketId and a.STATUS !=6 and u.Machine_Category_Id =:machineDescId
               ORDER  BY A.ASSIGN_ID 
        		
        		""",
            nativeQuery = true)
        List<Map<String, Object>> getAssignmentsForTicketgetdetail(
            @Param("ticketId") Integer ticketId, @Param("machineDescId") Integer machineDescId);

    
    @Query(value =
            """
        	
    		  SELECT A.ASSIGN_ID, A.TICKET_ID, A.ASSIGNED_DATE, A.ASSIGN_NOTE, A.STATUS,        
                      U.employee_display_name AS WORKER_NAME,        
                      UB.USER_NAME AS ASSIGNED_BY_NAME        
               FROM   MAINTENANCE_TICKET_ASSIGN A        
             LEFT JOIN MaintenanceWorkerMapping  U  ON A.ASSIGNED_TO = U.Worker_EmpCode        
               LEFT JOIN mis.dbo.Users  UB ON A.ASSIGNED_BY = UB.USER_ID        
               WHERE  A.TICKET_ID = :ticketId and  U.Machine_Category_Id =:machinedes 
               ORDER  BY A.ASSIGN_ID
        		
        		""",
            nativeQuery = true)
        List<Map<String, Object>> getAssignmentsForTicketshow(
            @Param("ticketId") Integer ticketId,@Param("machinedes") Integer machinedes);
    
    
    
    // ── GET TICKETS ASSIGNED TO A WORKER (worker screen) ─────────────────
    @Query(value =
    	    "SELECT A.ASSIGN_ID, A.TICKET_ID, A.ASSIGN_NOTE, A.STATUS AS ASSIGN_STATUS, " +
    	    "       A.ASSIGNED_DATE, " +
    	    "       T.TICKET_NO, T.WHAT_BROKE, T.PROBLEM_DESC, T.STATUS AS TICKET_STATUS, " +
    	    "       MD.MACHINE_DESCRIPTION, ML.MACHINE_SUBCODE, " +
    	    "       UB.USER_NAME AS ASSIGNED_BY_NAME, " +
    	    "       T.REPORTED_BY, A.ASSIGNED_BY, " +

    	    // ── ADD THESE TWO ─────────────────────────────────────────────────────
    	    "       (SELECT TOP 1 WL.STATUS " +
    	    "        FROM MAINTENANCE_WORK_LOG WL " +
    	    "        WHERE WL.ASSIGN_ID = A.ASSIGN_ID " +
    	    "        AND WL.STATUS IN (1, 5) " +
    	    "        ORDER BY WL.LOG_ID DESC) AS LOG_STATUS, " +

    	    "       (SELECT TOP 1 WL.LOG_ID " +
    	    "        FROM MAINTENANCE_WORK_LOG WL " +
    	    "        WHERE WL.ASSIGN_ID = A.ASSIGN_ID " +
    	    "        AND WL.STATUS IN (1, 5) " +
    	    "        ORDER BY WL.LOG_ID DESC) AS ACTIVE_LOG_ID " +
    	    // ─────────────────────────────────────────────────────────────────────

    	    "FROM   MAINTENANCE_TICKET_ASSIGN A " +
    	    "JOIN   MAINTENANCE_TICKET T  ON A.TICKET_ID = T.TICKET_ID " +
    	    "LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION MD " +
    	    "       ON T.MACHINE_DESC_ID = MD.MACHINE_DESC_ID " +
    	    "LEFT JOIN MAINTENANCE_MACHINE_LIST ML " +
    	    "       ON T.MACHINE_ID = ML.MACHINE_ID " +
    	    "LEFT JOIN mis.dbo.USERS UB ON A.ASSIGNED_BY = UB.USER_ID " +
    	    "WHERE  A.ASSIGNED_TO = :workerId " +
    	    "AND    A.FACTORY_ID  = :factoryId " +
    	    "AND    A.STATUS IN (1, 2, 3) " +
    	    "ORDER  BY A.ASSIGN_ID DESC",
    	    nativeQuery = true)
    	List<Map<String, Object>> getTicketsForWorker(
    	    @Param("workerId")  Integer workerId,
    	    @Param("factoryId") Integer factoryId);
    
    
    
    @Query(value =
    	    "SELECT A.TICKET_ID, A.STATUS, U.USER_NAME AS WORKER_NAME " +
    	    "FROM MAINTENANCE_TICKET_ASSIGN A " +
    	    "LEFT JOIN mis.dbo.USERS U ON A.ASSIGNED_TO = U.USER_ID " +
    	    "WHERE A.TICKET_ID IN (:ticketIds) " +
    	    "AND   A.FACTORY_ID = :factoryId",
    	    nativeQuery = true)
    	List<Map<String, Object>> getAssignsForTickets(
    	    @Param("ticketIds") List<Integer> ticketIds,
    	    @Param("factoryId") Integer factoryId);

    // ── UPDATE ASSIGN STATUS ──────────────────────────────────────────────
    @Modifying
    @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_TICKET_ASSIGN " +
        "SET STATUS = :status, MODIFIED_BY = :modifiedBy, MODIFIED_DATE = GETDATE() " +
        "WHERE ASSIGN_ID = :assignId",
        nativeQuery = true)
    void updateAssignStatus(
        @Param("assignId")   Integer assignId,
        @Param("status")     Integer status,
        @Param("modifiedBy") Integer modifiedBy);

    // ── CHECK IF TICKET HAS ACTIVE ASSIGNMENT ────────────────────────────
    @Query(value =
        "SELECT COUNT(*) FROM MAINTENANCE_TICKET_ASSIGN " +
        "WHERE TICKET_ID = :ticketId AND STATUS IN (1,2)",
        nativeQuery = true)
    int countActiveAssignments(@Param("ticketId") Integer ticketId);

    // ── CHECK ALL ASSIGNMENTS FIXED FOR A TICKET ─────────────────────────
    @Query(value =
        "SELECT COUNT(*) FROM MAINTENANCE_TICKET_ASSIGN " +
        "WHERE TICKET_ID = :ticketId AND STATUS NOT IN (3, 4)",
        nativeQuery = true)
    int countNotFixedAssignments(@Param("ticketId") Integer ticketId);
    
    
    @Transactional
    @Modifying
    @Query(value =
           "UPDATE MAINTENANCE_TICKET_ASSIGN " +
           "SET STATUS = 6, " +
           "    TRANSFER_REASON = :reason, " +
           "    MODIFIED_DATE = GETDATE() " +
           "WHERE ASSIGN_ID = :assignId",
           nativeQuery = true)
    void transferAssignment(
            @Param("assignId") Integer assignId,
            @Param("reason") String reason);
    
    
}