package com.JIMS.integration.repository;

import com.JIMS.integration.entity.MaintenanceTicketNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface MaintenanceNotificationRepository
        extends JpaRepository<MaintenanceTicketNotification, Integer> {

    @Modifying
    @Transactional
    @Query(value =
        "INSERT INTO MAINTENANCE_TICKET_NOTIFICATION " +
        "(TICKET_ID, FACTORY_ID, NOTIF_TO, NOTIF_TYPE, NOTIF_MESSAGE, IS_READ, CREATED_DATE) " +
        "VALUES (:ticketId, :factoryId, :notifTo, :notifType, :notifMessage, 0, GETDATE())",
        nativeQuery = true)
    void sendNotification(
        @Param("ticketId")     Integer ticketId,
        @Param("factoryId")    Integer factoryId,
        @Param("notifTo")      Integer notifTo,
        @Param("notifType")    String  notifType,
        @Param("notifMessage") String  notifMessage);

    @Query(value =
        "SELECT N.NOTIF_ID, N.TICKET_ID, N.NOTIF_TYPE, N.NOTIF_MESSAGE, " +
        "       N.IS_READ, N.CREATED_DATE, T.TICKET_NO " +
        "FROM   MAINTENANCE_TICKET_NOTIFICATION N " +
        "JOIN   MAINTENANCE_TICKET T ON N.TICKET_ID = T.TICKET_ID " +
        "WHERE  N.NOTIF_TO = :userId AND N.FACTORY_ID = :factoryId " +
        "ORDER  BY N.NOTIF_ID DESC",
        nativeQuery = true)
    List<Map<String, Object>> getNotificationsForUser(
        @Param("userId")    Integer userId,
        @Param("factoryId") Integer factoryId);

    @Query(value =
        "SELECT COUNT(*) FROM MAINTENANCE_TICKET_NOTIFICATION " +
        "WHERE NOTIF_TO = :userId AND IS_READ = 0 AND FACTORY_ID = :factoryId",
        nativeQuery = true)
    int countUnread(
        @Param("userId")    Integer userId,
        @Param("factoryId") Integer factoryId);

    @Modifying
    @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_TICKET_NOTIFICATION " +
        "SET IS_READ = 1 WHERE NOTIF_ID = :notifId",
        nativeQuery = true)
    void markRead(@Param("notifId") Integer notifId);

    @Modifying
    @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_TICKET_NOTIFICATION " +
        "SET IS_READ = 1 " +
        "WHERE NOTIF_TO = :userId AND FACTORY_ID = :factoryId",
        nativeQuery = true)
    void markAllRead(
        @Param("userId")    Integer userId,
        @Param("factoryId") Integer factoryId);
}