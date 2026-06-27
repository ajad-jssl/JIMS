package com.JIMS.integration.entity;

public class MachinecategoryListDTO {

    private int machineDescId;
    private String machineDescription;
    private Boolean status;

    public MachinecategoryListDTO(String string, String string2) {}

    public MachinecategoryListDTO(int machineDescId, String machineDescription, Boolean status) {
        this.machineDescId      = machineDescId;
        this.machineDescription = machineDescription;
        this.status             = status;
    }

    public int     getMachineDescId()               { return machineDescId; }
    public void    setMachineDescId(int v)           { machineDescId = v; }

    public String  getMachineDescription()           { return machineDescription; }
    public void    setMachineDescription(String v)   { machineDescription = v; }

    public Boolean getStatus()                       { return status; }
    public void    setStatus(Boolean v)              { status = v; }
}