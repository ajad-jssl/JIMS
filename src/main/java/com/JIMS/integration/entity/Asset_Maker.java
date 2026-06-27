package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Er_make") // Maps to your SQL table
public class Asset_Maker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Make_id") // Primary key
    private int makeId;

    @Column(name = "Make_Description") // Description column
    private String makeDescription;

    @Column(name = "Created_by")
    private Integer createdBy;

    @Column(name = "Created_date")
    private LocalDateTime createdDate;

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

    

    // Getters and Setters
    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public String getMakeDescription() {
        return makeDescription;
    }

    public void setMakeDescription(String makeDescription) {
        this.makeDescription = makeDescription;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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