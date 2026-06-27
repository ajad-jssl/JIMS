package com.JIMS.MIS.model;

import jakarta.persistence.*;

@Entity 
@Table (name="Loads", schema = "dbo")
public class Loads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long load_id; // Renamed for standard naming convention

    private String pload;
  
    // Getters and Setters
    public long getLoadId() {
        return load_id;
    }

    public void setLoadId(long loadId) {
        this.load_id = loadId;
    }

    public String getPload() { // Corrected method naming
        return pload;
    }

    public void setPload(String pload) {
        this.pload = pload;
    }
}
