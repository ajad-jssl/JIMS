package com.JIMS.integration.entity;

public class MachineListDTO {
    private String machineSubcode;
    private String machineDescription;

    public MachineListDTO() {}
    public MachineListDTO(String machineSubcode, String machineDescription) {
        this.machineSubcode     = machineSubcode;
        this.machineDescription = machineDescription;
    }
    public String getMachineSubcode()     { return machineSubcode; }
    public void   setMachineSubcode(String v) { machineSubcode = v; }
    public String getMachineDescription() { return machineDescription; }
    public void   setMachineDescription(String v) { machineDescription = v; }
}
