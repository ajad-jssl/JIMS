package com.JIMS.integration.repository;

import com.JIMS.integration.entity.MaintenanceTicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface MaintenanceTicketHistoryRepository
        extends JpaRepository<MaintenanceTicketHistory, Integer> {

    @Modifying
    @Transactional
    @Query(value =
        "INSERT INTO MAINTENANCE_TICKET_HISTORY " +
        "(TICKET_ID, FACTORY_ID, OLD_STATUS, NEW_STATUS, CHANGED_BY, REMARKS, CHANGED_DATE) " +
        "VALUES (:ticketId, :factoryId, :oldStatus, :newStatus, :changedBy, :remarks, GETDATE())",
        nativeQuery = true)
    void logHistory(
        @Param("ticketId")   Integer ticketId,
        @Param("factoryId")  Integer factoryId,
        @Param("oldStatus")  Integer oldStatus,
        @Param("newStatus")  Integer newStatus,
        @Param("changedBy")  Integer changedBy,
        @Param("remarks")    String  remarks);

    @Query(value =
        """
    		   SELECT H.HISTORY_ID, H.OLD_STATUS, H.NEW_STATUS, H.REMARKS, H.CHANGED_DATE,      
                U.USER_NAME AS CHANGED_BY_NAME      
           FROM   MAINTENANCE_TICKET_HISTORY H      
          LEFT JOIN mis.dbo.Users U  ON H.CHANGED_BY = U.USER_ID      
           WHERE  H.TICKET_ID = :ticketId
           ORDER  BY H.HISTORY_ID

 	
    		""",
        nativeQuery = true)
    List<Map<String, Object>> getHistoryForTicket(
        @Param("ticketId") Integer ticketId);
}