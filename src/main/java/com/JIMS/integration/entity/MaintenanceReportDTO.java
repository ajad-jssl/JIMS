package com.JIMS.integration.entity;

public class MaintenanceReportDTO {

    private Long ticketId;
    private String ticketNo;
    private String machineDescription;
    private String machineCode;
    private String machineSubcode;
    private String whatBroke;
    private String problemDesc;
    private String reportedBy;
    private String reportedDate;
    private String empName;
    private String mobileNo;
    private String assignedTo;
    private String assignedBy;
    private String assignedDate;
    private String assignNote;

    public MaintenanceReportDTO() {}

    public MaintenanceReportDTO(Long ticketId, String ticketNo, String machineDescription,
                                 String machineCode, String machineSubcode, String whatBroke,
                                 String problemDesc, String reportedBy, String reportedDate,
                                 String empName, String mobileNo, String assignedTo,
                                 String assignedBy, String assignedDate, String assignNote) {
        this.ticketId = ticketId;
        this.ticketNo = ticketNo;
        this.machineDescription = machineDescription;
        this.machineCode = machineCode;
        this.machineSubcode = machineSubcode;
        this.whatBroke = whatBroke;
        this.problemDesc = problemDesc;
        this.reportedBy = reportedBy;
        this.reportedDate = reportedDate;
        this.empName = empName;
        this.mobileNo = mobileNo;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.assignedDate = assignedDate;
        this.assignNote = assignNote;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getTicketNo() { return ticketNo; }
    public void setTicketNo(String ticketNo) { this.ticketNo = ticketNo; }

    public String getMachineDescription() { return machineDescription; }
    public void setMachineDescription(String machineDescription) { this.machineDescription = machineDescription; }

    public String getMachineCode() { return machineCode; }
    public void setMachineCode(String machineCode) { this.machineCode = machineCode; }

    public String getMachineSubcode() { return machineSubcode; }
    public void setMachineSubcode(String machineSubcode) { this.machineSubcode = machineSubcode; }

    public String getWhatBroke() { return whatBroke; }
    public void setWhatBroke(String whatBroke) { this.whatBroke = whatBroke; }

    public String getProblemDesc() { return problemDesc; }
    public void setProblemDesc(String problemDesc) { this.problemDesc = problemDesc; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public String getReportedDate() { return reportedDate; }
    public void setReportedDate(String reportedDate) { this.reportedDate = reportedDate; }

    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }

    public String getAssignedDate() { return assignedDate; }
    public void setAssignedDate(String assignedDate) { this.assignedDate = assignedDate; }

    public String getAssignNote() { return assignNote; }
    public void setAssignNote(String assignNote) { this.assignNote = assignNote; }
}
