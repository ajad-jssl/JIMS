package com.JIMS.integration.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "MAINTENANCE_TICKET_NOTIFICATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceTicketNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIF_ID")
    private Integer notifId;

    @Column(name = "TICKET_ID", nullable = false)
    private Integer ticketId;

    @Column(name = "FACTORY_ID", nullable = false)
    private Integer factoryId;

    @Column(name = "NOTIF_TO", nullable = false)
    private Integer notifTo;

    // 'RAISED','ASSIGNED','STARTED','FIXED','CLOSED'
    @Column(name = "NOTIF_TYPE", length = 100)
    private String notifType;

    @Column(name = "NOTIF_MESSAGE", length = 500)
    private String notifMessage;

    @Column(name = "IS_READ")
    private Boolean isRead;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}