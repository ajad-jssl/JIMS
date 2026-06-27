package com.JIMS.integration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONTRACT_GATEPASS_RELIGION_MASTER")
public class Religionmodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reli_Id")
    private int reliId;

    @Column(name = "religiondesc", length = 50, nullable = false)
    private String religionDesc;

    @Column(name = "createdby")
    private String createdBy;

    @Column(name = "createddt")
    private LocalDateTime createdDate;

    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

	/*
	 * @Column(name = "factory_id") private int factoryId;
	 */

    // Getters and Setters
    public int getreliId() {
        return reliId;
    }

    public void setreliId(int reliId) {
        this.reliId = reliId;
    }

    public String getreligionDesc() {
        return religionDesc;
    }

    public void setreligionDesc(String religionDesc) {
        this.religionDesc = religionDesc;
    }

    public String getCreatedby() {
        return createdBy;
    }

    public void setCreatedby(String createdby) {
        this.createdBy = createdby;
    }

    public LocalDateTime getCreateddt() {
        return createdDate;
    }

    public void setCreateddt(LocalDateTime createddt) {
        this.createdDate = createddt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

	/*
	 * public int getFactory_id() { return factoryId; }
	 * 
	 * public void setFactory_id(int factory_id) { this.factoryId = factory_id; }
	 */
}
