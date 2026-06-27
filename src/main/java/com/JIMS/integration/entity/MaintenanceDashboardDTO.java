package com.JIMS.integration.entity;

public class MaintenanceDashboardDTO {

    private String machineSubcode;
    private String machineDescription;

    private int totalCount;
    private int openCount;
    private int wipCount;
    private int resolvedCount;
    private int closedCount;

    // Default Constructor
    public MaintenanceDashboardDTO() {
    }

    // Machine-wise Summary Constructor
    public MaintenanceDashboardDTO(
            String machineSubcode,
            String machineDescription,
            long totalCount,
            long openCount,
            long wipCount,
            long resolvedCount,
            long closedCount) {

        this.machineSubcode = machineSubcode;
        this.machineDescription = machineDescription;
        this.totalCount = (int) totalCount;
        this.openCount = (int) openCount;
        this.wipCount = (int) wipCount;
        this.resolvedCount = (int) resolvedCount;
        this.closedCount = (int) closedCount;
    }

    // Grand Total Constructor
    public MaintenanceDashboardDTO(
            long totalCount,
            long openCount,
            long wipCount,
            long resolvedCount,
            long closedCount) {

        this.totalCount = (int) totalCount;
        this.openCount = (int) openCount;
        this.wipCount = (int) wipCount;
        this.resolvedCount = (int) resolvedCount;
        this.closedCount = (int) closedCount;
    }

    public String getMachineSubcode() {
        return machineSubcode;
    }

    public void setMachineSubcode(String machineSubcode) {
        this.machineSubcode = machineSubcode;
    }

    public String getMachineDescription() {
        return machineDescription;
    }

    public void setMachineDescription(String machineDescription) {
        this.machineDescription = machineDescription;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getOpenCount() {
        return openCount;
    }

    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }

    public int getWipCount() {
        return wipCount;
    }

    public void setWipCount(int wipCount) {
        this.wipCount = wipCount;
    }

    public int getResolvedCount() {
        return resolvedCount;
    }

    public void setResolvedCount(int resolvedCount) {
        this.resolvedCount = resolvedCount;
    }

    public int getClosedCount() {
        return closedCount;
    }

    public void setClosedCount(int closedCount) {
        this.closedCount = closedCount;
    }
}