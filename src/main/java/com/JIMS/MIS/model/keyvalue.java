package com.JIMS.MIS.model;

public class keyvalue {


    private String keyName;
    private Integer keyValue;

    // Constructors
    public keyvalue() {}

    public keyvalue(String keyName, Integer keyValue) {
        this.keyName = keyName;
        this.keyValue = keyValue;
    }

    // Getters and Setters
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Integer getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(Integer keyValue) {
        this.keyValue = keyValue;
    }
}
