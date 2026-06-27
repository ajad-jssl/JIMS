package com.JIMS.integration.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "MAINTENANCE_WORK_LOG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceWorkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    private Integer logId;

    @Column(name = "TICKET_ID", nullable = false)
    private Integer ticketId;

    @Column(name = "ASSIGN_ID", nullable = false)
    private Integer assignId;

    @Column(name = "FACTORY_ID", nullable = false)
    private Integer factoryId;

    @Column(name = "WORKER_ID", nullable = false)
    private Integer workerId;

    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "WORK_HOURS", precision = 10, scale = 2)
    private BigDecimal workHours;

    @Column(name = "WHAT_FIXED", length = 1000)
    private String whatFixed;

    @Column(name = "REMARKS", length = 500)
    private String remarks;

    // 1=Started 2=Fixed 3=Closed
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