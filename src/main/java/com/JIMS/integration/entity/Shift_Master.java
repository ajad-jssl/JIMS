package com.JIMS.integration.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="SHIFT_MASTER")
public class Shift_Master {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer shift_id;
	
	@Column(name="CODE")
	private String shiftCode;
	
	@Column(name="SHIFT_DESCRIPTION")
	private String Shift_description ;
	
	
	@Column(name = "CREATED_BY", length = 255)
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;  
    
    @Column(name = "MODIFIED_BY", length = 255)
    private String modifiedBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
    
    @Column(name = "SHIFT_HOURS", precision = 4, scale = 2)
    private BigDecimal shiftHours;
    
    @Column(name="LOCATION_ID")
    private Integer Location_id;
    
    
    
    public Integer getLocation_id() {
		return Location_id;
	}


	public void setLocation_id(Integer location_id) {
		Location_id = location_id;
	}


	public BigDecimal getShiftHours() {
		return shiftHours;
	}


	public void setShiftHours(BigDecimal shiftHours) {
		this.shiftHours = shiftHours;
	}


	@Column(name = "STATUS", nullable = false)
    private Integer status = 0;


	public Integer getShift_id() {
		return shift_id;
	}


	public void setShift_id(Integer shift_id) {
		this.shift_id = shift_id;
	}


	public String getShiftCode() {
		return shiftCode;
	}


	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}


	public String getShift_description() {
		return Shift_description;
	}


	public void setShift_description(String shift_description) {
		Shift_description = shift_description;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public LocalDateTime getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}




	


}
