package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MaintenanceWorkerMapping")
public class MaintenanceWorkerMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Worker_EmpCode")
    private String workerEmpCode;

    @Column(name = "Worker_MobileNo")
    private String workerMobileNo;

    @Column(name = "Machine_Category_Id")
    private Integer machineCategoryId;

    @Column(name = "Is_Active")
    private Boolean isActive = true;

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Created_Date")
    private LocalDateTime createdDate;

    @Column(name = "Modified_By")
    private String modifiedBy;

    @Column(name = "Modified_Date")
    private LocalDateTime modifiedDate;
    
    
    @Column(name = "EmpCode")
    private Integer Empcode;
    
   @Column(name="Factory_Id")
    private String factoryid;
   @Column(name = "employee_display_name")
   private String employeeDisplayName;
   
   @Column(name="worker_email")
   private String email;
   
   

    


	public String getEmail() {
	return email;
}

   public void setEmail(String email) {
	this.email = email;
   }

	public String getEmployeeDisplayName() {
	return employeeDisplayName;
}

   public void setEmployeeDisplayName(String employeeDisplayName) {
	this.employeeDisplayName = employeeDisplayName;
   }

	public String getFactoryid() {
	return factoryid;
}

   public void setFactoryid(String factoryid) {
	this.factoryid = factoryid;
   }

	public Integer getEmpcode() {
		return Empcode;
	}

	public void setEmpcode(Integer empcode) {
		Empcode = empcode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkerEmpCode() {
		return workerEmpCode;
	}

	public void setWorkerEmpCode(String workerEmpCode) {
		this.workerEmpCode = workerEmpCode;
	}

	public String getWorkerMobileNo() {
		return workerMobileNo;
	}

	public void setWorkerMobileNo(String workerMobileNo) {
		this.workerMobileNo = workerMobileNo;
	}

	public Integer getMachineCategoryId() {
		return machineCategoryId;
	}

	public void setMachineCategoryId(Integer machineCategoryId) {
		this.machineCategoryId = machineCategoryId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

    
}
