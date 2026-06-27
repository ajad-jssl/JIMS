package com.JIMS.integration.interfaces;

import java.util.List;

public class MaintenanceWorkerMappingRequest {
	

	private Long id;
	
	private String factoryId;
	private String employeeDisplayName;
	
	private String email;
	
	
	private List<Integer> designationIds;

	public List<Integer> getDesignationIds() {
	    return designationIds;
	}
	public void setDesignationIds(List<Integer> designationIds) {
	    this.designationIds = designationIds;
	}
	
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
	public String getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String workerEmpCode;
    private String workerMobileNo;
    private Integer machineCategoryId;
    private String createdBy;

    private String modifiedBy;
    public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	private Integer empId;
    
    
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

    // getters & setters
}
