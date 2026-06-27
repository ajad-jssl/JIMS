package com.JIMS.integration.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Jc_Drawntxn")
public class Drawing_Entry_Entity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "EMP_ID")
	private String empId;

	@Column(name = "SHIFT_ID")
	private Integer shiftId;

	@Column(name = "CONTRACT_ID")
	private Integer contractId;

	@Column(name = "PHASE_ID")
	private Integer phaseId;

	@Column(name = "TXT_DATE")
	private String txtDate;

	@Column(name = "YEAR")
	private Integer year;

	@JsonProperty("Month")
	@Column(name = "MONTH")
	private Integer month;
	
	@Column(name="is_deleted")
	private Integer isDeleted;

	@Column(name="deleted_by")
	private String deletedBy;

	@Column(name="deleted_date")
	private LocalDateTime deletedDate;

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public LocalDateTime getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(LocalDateTime deletedDate) {
		this.deletedDate = deletedDate;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Column(name = "WEEK")
	private Integer week;

	@Column(name = "TYPE_ID")
	private Integer typeId;

	@Column(name = "ACTIVITY_CATEGORY_ID")
	private Integer activityCategoryId;

	@Column(name = "DH_HOURS")
	private BigDecimal dhHours;

	@Column(name = "IH_HOURS")
	private BigDecimal ihHours;

	@Column(name = "EH_HOURS")
	private BigDecimal ehHours;

	@Column(name = "REWORK")
	private String rework;

	@Column(name = "RH_HOURS")
	private BigDecimal rhHours;

	@Column(name = "TOTAL_HOURS")
	private BigDecimal totalHours;

	@Column(name = "GRAND_TOTAL")
	private BigDecimal grandTotal;

	@Column(name = "REASON_ID")
	private Integer reasonId;

	@Column(name = "EC_NO")
	private Integer ecNo;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;

	// -------- getters & setters --------

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Integer phaseId) {
		this.phaseId = phaseId;
	}

	public String getTxtDate() {
		return txtDate;
	}

	public void setTxtDate(String txtDate) {
		this.txtDate = txtDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getActivityCategoryId() {
		return activityCategoryId;
	}

	public void setActivityCategoryId(Integer activityCategoryId) {
		this.activityCategoryId = activityCategoryId;
	}

	public BigDecimal getDhHours() {
		return dhHours;
	}

	public void setDhHours(BigDecimal dhHours) {
		this.dhHours = dhHours;
	}

	public BigDecimal getIhHours() {
		return ihHours;
	}

	public void setIhHours(BigDecimal ihHours) {
		this.ihHours = ihHours;
	}

	public BigDecimal getEhHours() {
		return ehHours;
	}

	public void setEhHours(BigDecimal ehHours) {
		this.ehHours = ehHours;
	}

	public String getRework() {
		return rework;
	}

	public void setRework(String rework) {
		this.rework = rework;
	}

	public BigDecimal getRhHours() {
		return rhHours;
	}

	public void setRhHours(BigDecimal rhHours) {
		this.rhHours = rhHours;
	}

	public BigDecimal getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(BigDecimal totalHours) {
		this.totalHours = totalHours;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getEcNo() {
		return ecNo;
	}

	public void setEcNo(Integer ecNo) {
		this.ecNo = ecNo;
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
