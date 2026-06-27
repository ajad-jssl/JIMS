package com.JIMS.integration.entity;

public class MaintenanceMachineSummaryDTO {

    private String machineSubcode;
    private String machineDescription;

    private int totalCount;
    private int openCount;
    private int assignedCount;
    private int wipCount;
    private int closedCount;
    private int transferredCount;
    private int escalatedCount;

    private double totalDowntimeHours;
    private double openDowntimeHours;
    private double assignedDowntimeHours;
    private double wipDowntimeHours;
    private double closedDowntimeHours;

    // --------------------------------------------------
    // Default Constructor
    // --------------------------------------------------
    public MaintenanceMachineSummaryDTO() {}

    // --------------------------------------------------
    // Machine-wise Summary Constructor (with per-status downtime)
    // --------------------------------------------------
    public MaintenanceMachineSummaryDTO(
            String machineSubcode,
            String machineDescription,
            long totalCount,
            long openCount,
            long assignedCount,
            long wipCount,
            long closedCount,
            long transferredCount,
            long escalatedCount,
            double openDowntimeHours,
            double assignedDowntimeHours,
            double wipDowntimeHours,
            double closedDowntimeHours,
            double totalDowntimeHours) {

        this.machineSubcode        = machineSubcode;
        this.machineDescription    = machineDescription;
        this.totalCount            = (int) totalCount;
        this.openCount             = (int) openCount;
        this.assignedCount         = (int) assignedCount;
        this.wipCount              = (int) wipCount;
        this.closedCount           = (int) closedCount;
        this.transferredCount      = (int) transferredCount;
        this.escalatedCount        = (int) escalatedCount;
        this.openDowntimeHours     = openDowntimeHours;
        this.assignedDowntimeHours = assignedDowntimeHours;
        this.wipDowntimeHours      = wipDowntimeHours;
        this.closedDowntimeHours   = closedDowntimeHours;
        this.totalDowntimeHours    = totalDowntimeHours;
    }

    // --------------------------------------------------
    // Grand Total Constructor
    // --------------------------------------------------
    public MaintenanceMachineSummaryDTO(
            long totalCount,
            long openCount,
            long wipCount,
            long closedCount,
            long transferredCount,
            long escalatedCount,
            Double totalDowntimeHours) {

        this.totalCount         = (int) totalCount;
        this.openCount          = (int) openCount;
        this.wipCount           = (int) wipCount;
        this.closedCount        = (int) closedCount;
        this.transferredCount   = (int) transferredCount;
        this.escalatedCount     = (int) escalatedCount;
        this.totalDowntimeHours = totalDowntimeHours != null ? totalDowntimeHours : 0.0;
    }

    // --------------------------------------------------
    // Getters & Setters
    // --------------------------------------------------

    public String getMachineSubcode() { return machineSubcode; }
    public void setMachineSubcode(String machineSubcode) { this.machineSubcode = machineSubcode; }

    public String getMachineDescription() { return machineDescription; }
    public void setMachineDescription(String machineDescription) { this.machineDescription = machineDescription; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getOpenCount() { return openCount; }
    public void setOpenCount(int openCount) { this.openCount = openCount; }

    public int getAssignedCount() { return assignedCount; }
    public void setAssignedCount(int assignedCount) { this.assignedCount = assignedCount; }

    public int getWipCount() { return wipCount; }
    public void setWipCount(int wipCount) { this.wipCount = wipCount; }

    public int getClosedCount() { return closedCount; }
    public void setClosedCount(int closedCount) { this.closedCount = closedCount; }

    public int getTransferredCount() { return transferredCount; }
    public void setTransferredCount(int transferredCount) { this.transferredCount = transferredCount; }

    public int getEscalatedCount() { return escalatedCount; }
    public void setEscalatedCount(int escalatedCount) { this.escalatedCount = escalatedCount; }

    public double getTotalDowntimeHours() { return totalDowntimeHours; }
    public void setTotalDowntimeHours(double totalDowntimeHours) { this.totalDowntimeHours = totalDowntimeHours; }

    public double getOpenDowntimeHours() { return openDowntimeHours; }
    public void setOpenDowntimeHours(double openDowntimeHours) { this.openDowntimeHours = openDowntimeHours; }

    public double getAssignedDowntimeHours() { return assignedDowntimeHours; }
    public void setAssignedDowntimeHours(double assignedDowntimeHours) { this.assignedDowntimeHours = assignedDowntimeHours; }

    public double getWipDowntimeHours() { return wipDowntimeHours; }
    public void setWipDowntimeHours(double wipDowntimeHours) { this.wipDowntimeHours = wipDowntimeHours; }

    public double getClosedDowntimeHours() { return closedDowntimeHours; }
    public void setClosedDowntimeHours(double closedDowntimeHours) { this.closedDowntimeHours = closedDowntimeHours; }
}