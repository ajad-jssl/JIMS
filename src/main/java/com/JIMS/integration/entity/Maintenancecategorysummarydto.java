package com.JIMS.integration.entity;

/**
 * Summary DTO for a machine category (MAINTENANCE_MACHINE_DESCRIPTION).
 *
 * STATUS REFERENCE (MAINTENANCE_TICKET.STATUS):
 *   1 = Open
 *   2 = Assigned
 *   3 = In Progress
 *   4 = Fixed  (waiting for closer)
 *   5 = Closed
 *
 *  wipCount         → statuses 2, 3, 4  (Assigned + In Progress + Fixed)
 *  closedCount      → status  5
 *  transferredCount → tickets that have ≥1 MAINTENANCE_TICKET_ASSIGN row
 *                     with a non-null / non-empty TRANSFER_REASON
 *  totalManHours    → SUM(MAINTENANCE_WORK_LOG.WORK_HOURS) for all tickets
 *                     in this category (matching the same filters)
 */
public class Maintenancecategorysummarydto {

    private int    machineDescId;
    private String machineDescription;
    private int    totalCount;
    private int    openCount;
    private int    wipCount;          // Assigned + In Progress + Fixed (statuses 2,3,4)
    private int    closedCount;       // STATUS = 5  (was "resolvedCount" / "stoppedCount")
    private int    transferredCount;  // tickets that were transferred at least once
    private int    escalatedCount;    // reserved / unused — kept for forward-compat
    private double totalDowntimeHours;
    private double totalManHours;     // SUM(WORK_HOURS) from MAINTENANCE_WORK_LOG

    public Maintenancecategorysummarydto() {}

    /**
     * Legacy constructor (pre-manhours). Kept for backward compatibility with
     * any existing callers; totalManHours defaults to 0.0.
     */
    public Maintenancecategorysummarydto(
            int    machineDescId,
            String machineDescription,
            long   totalCount,
            long   openCount,
            long   wipCount,
            long   closedCount,
            long   transferredCount,
            long   escalatedCount,
            double totalDowntimeHours) {

        this(machineDescId, machineDescription, totalCount, openCount, wipCount,
             closedCount, transferredCount, escalatedCount, totalDowntimeHours, 0.0);
    }

    /**
     * Constructor called by MaintenanceDashboardRepository.getSummaryByCategory()
     */
    public Maintenancecategorysummarydto(
            int    machineDescId,
            String machineDescription,
            long   totalCount,
            long   openCount,
            long   wipCount,
            long   closedCount,
            long   transferredCount,
            long   escalatedCount,
            double totalDowntimeHours,
            double totalManHours) {

        this.machineDescId      = machineDescId;
        this.machineDescription = machineDescription;
        this.totalCount         = (int) totalCount;
        this.openCount          = (int) openCount;
        this.wipCount           = (int) wipCount;
        this.closedCount        = (int) closedCount;
        this.transferredCount   = (int) transferredCount;
        this.escalatedCount     = (int) escalatedCount;
        this.totalDowntimeHours = totalDowntimeHours;
        this.totalManHours      = totalManHours;
    }

    // ── Getters & Setters ────────────────────────────────────────

    public int    getMachineDescId()               { return machineDescId; }
    public void   setMachineDescId(int v)           { machineDescId = v; }

    public String getMachineDescription()           { return machineDescription; }
    public void   setMachineDescription(String v)   { machineDescription = v; }

    public int    getTotalCount()                   { return totalCount; }
    public void   setTotalCount(int v)              { totalCount = v; }

    public int    getOpenCount()                    { return openCount; }
    public void   setOpenCount(int v)               { openCount = v; }

    public int    getWipCount()                     { return wipCount; }
    public void   setWipCount(int v)                { wipCount = v; }

    /** STATUS = 5  (Closed) */
    public int    getClosedCount()                  { return closedCount; }
    public void   setClosedCount(int v)             { closedCount = v; }

    /** Tickets transferred at least once (have a TRANSFER_REASON in assign table) */
    public int    getTransferredCount()             { return transferredCount; }
    public void   setTransferredCount(int v)        { transferredCount = v; }

    /** Reserved / unused */
    public int    getEscalatedCount()               { return escalatedCount; }
    public void   setEscalatedCount(int v)          { escalatedCount = v; }

    public double getTotalDowntimeHours()           { return totalDowntimeHours; }
    public void   setTotalDowntimeHours(double v)   { totalDowntimeHours = v; }

    /** SUM(MAINTENANCE_WORK_LOG.WORK_HOURS) for tickets in this category */
    public double getTotalManHours()                { return totalManHours; }
    public void   setTotalManHours(double v)        { totalManHours = v; }
}