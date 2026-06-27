package com.JIMS.integration.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "MAINTENANCE_TICKET_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceTicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Integer historyId;

    @Column(name = "TICKET_ID", nullable = false)
    private Integer ticketId;

    @Column(name = "FACTORY_ID", nullable = false)
    private Integer factoryId;

    @Column(name = "OLD_STATUS")
    private Integer oldStatus;

    @Column(name = "NEW_STATUS")
    private Integer newStatus;

    @Column(name = "CHANGED_BY")
    private Integer changedBy;

    @Column(name = "REMARKS", length = 500)
    private String remarks;

    @Column(name = "CHANGED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changedDate;
}