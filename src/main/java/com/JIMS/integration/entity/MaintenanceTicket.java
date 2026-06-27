package com.JIMS.integration.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "MAINTENANCE_TICKET")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    private Integer ticketId;

    @Column(name = "TICKET_NO", nullable = false, length = 50)
    private String ticketNo;

    @Column(name = "FACTORY_ID", nullable = false)
    private Integer factoryId;

    @Column(name = "MACHINE_DESC_ID", nullable = false)
    private Integer machineDescId;

    @Column(name = "MACHINE_ID", nullable = false)
    private Integer machineId;

    /** Comma-separated list – e.g. "Motor, Sensor, Pump" */
    @Column(name = "WHAT_BROKE", nullable = false, length = 500)
    private String whatBroke;

    @Column(name = "PROBLEM_DESC", nullable = false, length = 1000)
    private String problemDesc;

    @Column(name = "REPORTED_BY", nullable = false)
    private Integer reportedBy;

    /** Employee code from HR system (e.g. "EMP-001") */
    @Column(name = "EMP_CODE", length = 50)
    private String empCode;

    /** Full display name – EmpCode + Name (e.g. "EMP-001 - John Smith") */
    @Column(name = "EMP_NAME", length = 200)
    private String empName;

    /** Mobile number – stored as entered; may be empty */
    @Column(name = "MOBILE_NO", length = 20)
    private String mobileNo;

    @Column(name = "REPORTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportedDate;

    /** 1=Open  2=Assigned  3=InProgress  4=Fixed(pending close)  5=Closed */
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