package com.JIMS.integration.entity;

import java.util.Date;

/**
 * Detail DTO for a single maintenance ticket, returned by
 * MaintenanceDashboardRepository.getTicketsByMachine()
 *
 * STATUS REFERENCE (MAINTENANCE_TICKET.STATUS):
 *   1 = Open
 *   2 = Assigned
 *   3 = In Progress
 *   4 = Fixed  (waiting for closer)
 *   5 = Closed
 */
public class MachineTicketDetailDTO {

    private String ticketNo;
    private String machineSubcode;
    private String machineDescription;
    private String whatBroke;
    private String problemDesc;
    private String reportedBy;
    private Date   reportedDate;
    private Date   modifiedDate;
    private String status;           // numeric string: "1","2","3","4","5"
    private String empName;
    private String mobileNo;
    private double downtimeHours;
    private int    transferCount;    // number of times this ticket was transferred

    public MachineTicketDetailDTO() {}

    /**
     * Constructor called by MaintenanceDashboardRepository.getTicketsByMachine()
     */
    public MachineTicketDetailDTO(
            String ticketNo,
            String machineSubcode,
            String machineDescription,
            String whatBroke,
            String problemDesc,
            String reportedBy,
            Date   reportedDate,
            Date   modifiedDate,
            String status,
            String empName,
            String mobileNo,
            double downtimeHours,
            int    transferCount) {

        this.ticketNo           = ticketNo;
        this.machineSubcode     = machineSubcode;
        this.machineDescription = machineDescription;
        this.whatBroke          = whatBroke;
        this.problemDesc        = problemDesc;
        this.reportedBy         = reportedBy;
        this.reportedDate       = reportedDate;
        this.modifiedDate       = modifiedDate;
        this.status             = status;
        this.empName            = empName;
        this.mobileNo           = mobileNo;
        this.downtimeHours      = downtimeHours;
        this.transferCount      = transferCount;
    }

    // ── Getters & Setters ────────────────────────────────────────

    public String getTicketNo()                     { return ticketNo; }
    public void   setTicketNo(String v)             { ticketNo = v; }

    public String getMachineSubcode()               { return machineSubcode; }
    public void   setMachineSubcode(String v)       { machineSubcode = v; }

    public String getMachineDescription()           { return machineDescription; }
    public void   setMachineDescription(String v)   { machineDescription = v; }

    public String getWhatBroke()                    { return whatBroke; }
    public void   setWhatBroke(String v)            { whatBroke = v; }

    public String getProblemDesc()                  { return problemDesc; }
    public void   setProblemDesc(String v)          { problemDesc = v; }

    public String getReportedBy()                   { return reportedBy; }
    public void   setReportedBy(String v)           { reportedBy = v; }

    public Date   getReportedDate()                 { return reportedDate; }
    public void   setReportedDate(Date v)           { reportedDate = v; }

    public Date   getModifiedDate()                 { return modifiedDate; }
    public void   setModifiedDate(Date v)           { modifiedDate = v; }

    /** Numeric string matching MAINTENANCE_TICKET.STATUS (1–5) */
    public String getStatus()                       { return status; }
    public void   setStatus(String v)               { status = v; }

    public String getEmpName()                      { return empName; }
    public void   setEmpName(String v)              { empName = v; }

    public String getMobileNo()                     { return mobileNo; }
    public void   setMobileNo(String v)             { mobileNo = v; }

    public double getDowntimeHours()                { return downtimeHours; }
    public void   setDowntimeHours(double v)        { downtimeHours = v; }

    /** Number of times this ticket was transferred (TRANSFER_REASON rows in assign table) */
    public int    getTransferCount()                { return transferCount; }
    public void   setTransferCount(int v)           { transferCount = v; }
}
