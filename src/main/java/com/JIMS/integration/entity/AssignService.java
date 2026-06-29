package com.JIMS.integration.entity;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class AssignService {

    @PersistenceContext
    @Qualifier("integrationEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional("integrationTransactionManager")  
    public Integer assignTicket(Integer ticketId, Integer factoryId,
                                 Integer assignedTo, Integer assignedBy, String assignNote) {

        String sql =
            "INSERT INTO MAINTENANCE_TICKET_ASSIGN " +
            "(TICKET_ID, FACTORY_ID, ASSIGNED_TO, ASSIGNED_BY, " +
            " ASSIGNED_DATE, ASSIGN_NOTE, STATUS, CREATED_DATE) " +
            "OUTPUT INSERTED.ASSIGN_ID " +
            "VALUES (:ticketId, :factoryId, :assignedTo, :assignedBy, " +
            "        GETDATE(), :assignNote, 1, GETDATE())";

        Object result = entityManager.createNativeQuery(sql)
                .setParameter("ticketId", ticketId)
                .setParameter("factoryId", factoryId)
                .setParameter("assignedTo", assignedTo)
                .setParameter("assignedBy", assignedBy)
                .setParameter("assignNote", assignNote)
                .getSingleResult();   // ← treats it as a SELECT-style result, not executeUpdate

        return ((Number) result).intValue();
    }
}