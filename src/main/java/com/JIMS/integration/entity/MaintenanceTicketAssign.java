package com.JIMS.integration.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "MAINTENANCE_TICKET_ASSIGN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceTicketAssign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASSIGN_ID")
    private Integer assignId;

    @Column(name = "TICKET_ID", nullable = false)
    private Integer ticketId;

    @Column(name = "FACTORY_ID", nullable = false)
    private Integer factoryId;

    @Column(name = "ASSIGNED_TO", nullable = false)
    private Integer assignedTo;

    @Column(name = "ASSIGNED_BY", nullable = false)
    private Integer assignedBy;

    @Column(name = "ASSIGNED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;

    @Column(name = "ASSIGN_NOTE", length = 500)
    private String assignNote;

    // 1=Assigned 2=Started 3=Fixed 4=Closed
    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "MODIFIED_BY")
    private Integer modifiedBy;

    @Column(name = "MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
}