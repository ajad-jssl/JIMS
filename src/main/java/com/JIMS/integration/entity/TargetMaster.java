package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TARGET_MASTER")
public class TargetMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TARGET_ID")
    private Integer targetId;

    @Column(name = "FINANCIAL_YEAR")
    private String financialYear;

    @Column(name = "TARGET_TYPE")
    private String targetType;

    @Column(name = "CONTRACT_ID")
    private Integer contractId;

    @Column(name = "TARGET_PRODUCTION_IH")
    private Double targetProductionIh;

    @Column(name = "TARGET_PRODUCTION_SC")
    private Double targetProductionSc;

    @Column(name = "TARGET_DISPATCH_IH")
    private Double targetDispatchIh;

    @Column(name = "TARGET_DISPATCH_SC")
    private Double targetDispatchSc;

    @Column(name = "TARGET_ERECTION_IH")
    private Double targetErectionIh;

    @Column(name = "TARGET_ERECTION_SC")
    private Double targetErectionSc;

    @Column(name = "TARGET_MANHOURS")
    private Double targetManhours;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_BY")
    private Integer modifiedBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Double getTargetProductionIh() {
		return targetProductionIh;
	}

	public void setTargetProductionIh(Double targetProductionIh) {
		this.targetProductionIh = targetProductionIh;
	}

	public Double getTargetProductionSc() {
		return targetProductionSc;
	}

	public void setTargetProductionSc(Double targetProductionSc) {
		this.targetProductionSc = targetProductionSc;
	}

	public Double getTargetDispatchIh() {
		return targetDispatchIh;
	}

	public void setTargetDispatchIh(Double targetDispatchIh) {
		this.targetDispatchIh = targetDispatchIh;
	}

	public Double getTargetDispatchSc() {
		return targetDispatchSc;
	}

	public void setTargetDispatchSc(Double targetDispatchSc) {
		this.targetDispatchSc = targetDispatchSc;
	}

	public Double getTargetErectionIh() {
		return targetErectionIh;
	}

	public void setTargetErectionIh(Double targetErectionIh) {
		this.targetErectionIh = targetErectionIh;
	}

	public Double getTargetErectionSc() {
		return targetErectionSc;
	}

	public void setTargetErectionSc(Double targetErectionSc) {
		this.targetErectionSc = targetErectionSc;
	}

	public Double getTargetManhours() {
		return targetManhours;
	}

	public void setTargetManhours(Double targetManhours) {
		this.targetManhours = targetManhours;
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
