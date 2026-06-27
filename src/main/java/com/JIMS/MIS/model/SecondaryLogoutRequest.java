package com.JIMS.MIS.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SecondaryLogoutRequest {

    private String SourceSystem;              // CAPITAL S
    private List<String> targetFactories;
    private int userId;
    private Boolean userLoggedIn;
    private String modifiedByUserId;           // STRING

    public String getSourceSystem() {
        return SourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        SourceSystem = sourceSystem;
    }

    public List<String> getTargetFactories() {
        return targetFactories;
    }

    public void setTargetFactories(List<String> targetFactories) {
        this.targetFactories = targetFactories;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(Boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public String getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(String modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }
}
