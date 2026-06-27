package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Er_Model") // ✅ updated to match your new table name
public class Asset_Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Model_id") // ✅ maps to table column
    private int model_id;

    @Column(name = "Model_Description") // ✅ matches SQL column
    private String assetModel;

    @Column(name = "Created_by") // ✅ matches SQL column
    private Integer createdBy;

    @Column(name = "Created_date") // ✅ matches SQL column
    private LocalDateTime created_date;

    @Column(name = "Modified_by") // ✅ matches SQL column
    private Integer ModifiedBy;

    @Column(name = "Modified_date") // ✅ matches SQL column
    private LocalDateTime ModifiedDate;
    
    @Column(name = "Code",insertable = false, updatable = false)
    private String code;
    
    
    public String getCode() {
    	return code;
    }
    public void setCdoe(String code) {
    	this.code = code;
    }

    // Getters & Setters (unchanged)
    public int getModel_id() {
        return model_id;
    }

    public void setType_id(int model_id) {
        this.model_id = model_id;
    }

    public String getAssetModel() {
        return assetModel;
    }

    public void setAssetModel(String assetModel) {
        this.assetModel = assetModel;
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
        return ModifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}
