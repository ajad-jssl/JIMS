package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Er_assetob")
public class Asset_OwnedBy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Asserob_id")
    private int asserob_id;

    @Column(name = "ob_Description")
    private String ownedByDescription;

    @Column(name = "Created_by")
    private Integer createdBy;

    @Column(name = "Created_date")
    private LocalDateTime created_date;

    @Column(name = "Modified_by")
    private Integer modifiedBy;

    @Column(name = "Modified_date")
    private LocalDateTime modifiedDate;

    
    
    
    
    @Column(name = "Code",insertable = false, updatable = false)
    private String code;
    
    
    public String getCode() {
    	return code;
    }
    public void setCdoe(String code) {
    	this.code = code;
    }

    
    
    
    
    // -------- GETTERS & SETTERS --------

    public int getAsserob_id() {
        return asserob_id;
    }

    public void setAsserob_id(int asserob_id) {
        this.asserob_id = asserob_id;
    }

    public String getOwnedByDescription() {
        return ownedByDescription;
    }

    public void setOwnedByDescription(String ownedByDescription) {
        this.ownedByDescription = ownedByDescription;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDateTime created_date) {
        this.created_date = created_date;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
