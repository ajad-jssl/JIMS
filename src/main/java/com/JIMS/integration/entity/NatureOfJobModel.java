package com.JIMS.integration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONTRACT_GATEPASS_NATURE_OF_JOB_MASTER")
public class NatureOfJobModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noj_id")
    private int nojId;

    @Column(name = "Nojdesc", length = 50, nullable = false)
    private String nojDesc;

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
    public int getNoj_id() {
        return nojId;
    }

    public void setNoj_id(int noj_id) {
        this.nojId = noj_id;
    }

    public String getNojDesc() {
        return nojDesc;
    }

    public void setNojDesc(String nojDesc) {
        this.nojDesc = nojDesc;
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
