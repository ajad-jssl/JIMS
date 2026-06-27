package com.JIMS.integration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONTRACT_GATEPASS_CONTRACTVACCINE_MASTER")
public class Contractvaccinemodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vac_id")
    private int vacid;

    @Column(name = "Vaccine_desc", length = 50, nullable = false)
    private String vaccineDesc;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "Created_dt")
    private LocalDateTime createdDate;

    @Column(name = "UpdatedBy")
    private String updatedBy;

    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

	/*
	 * @Column(name = "factory_id") private int factoryId;
	 */

    // Getters and Setters
    public int getvac_id() {
        return vacid;
    }

    public void setvac_id(int vac_id) {
        this.vacid = vac_id;
    }

    public String getvaccineDesc() {
        return vaccineDesc;
    }

    public void setvaccineDesc(String Vaccine_desc) {
        this.vaccineDesc = Vaccine_desc;
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
