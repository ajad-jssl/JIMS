package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Er_type")
public class Asset_type {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "type_id")
private	int type_id;
	
	@Column(name = "asset_type")
private	String assetType;
	
	@Column(name="created_by")
private	Integer createdBy;
	
	@Column(name="created_date")
private	LocalDateTime created_date;
	
	@Column(name="Modified_by")
private	Integer ModifiedBy;
	
	
	@Column(name="Modified_date")
private	LocalDateTime ModifiedDate;

	
	
	
    @Column(name = "Code",insertable = false, updatable = false)
    private String code;
    
    
    public String getCode() {
    	return code;
    }
    public void setCdoe(String code) {
    	this.code = code;
    }

	

	public int getType_id() {
		return type_id;
	}


	public void setType_id(int type_id) {
		this.type_id = type_id;
	}


	public String getAssetType() {
		return assetType;
	}


	public void setAssetType(String assetType) {
		this.assetType = assetType;
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
