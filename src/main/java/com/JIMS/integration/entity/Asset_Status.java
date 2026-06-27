package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Er_Status")
public class Asset_Status {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Staus_id")
	private int status_id;
	
	
	@Column(name="st_Description")
	private String assetStatus;
	
	
	
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

	    
	
	  public int getStatus_id() {
		return status_id;
	}

	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}

	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
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
