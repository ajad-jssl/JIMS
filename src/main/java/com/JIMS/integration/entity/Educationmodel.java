package com.JIMS.integration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONTRACT_GATEPASS_EDUCATION_MASTER")
public class Educationmodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edu_Id")
    private int eduId;

    @Column(name = "edudesc", length = 50, nullable = false)
    private String eduDesc;

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
    public int geteduId() {
        return eduId;
    }

    public void seteduId(int eduId) {
        this.eduId = eduId;
    }

    public String geteduDesc() {
        return eduDesc;
    }

    public void seteduDesc(String eduDesc) {
        this.eduDesc = eduDesc;
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
