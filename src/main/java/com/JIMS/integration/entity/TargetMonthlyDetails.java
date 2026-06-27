package com.JIMS.integration.entity;


import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "TARGET_MONTHLY_DETAILS")
public class TargetMonthlyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TARGET_MONTHLY_ID")
    private Integer targetMonthlyId;

    @Column(name = "TARGET_ID")
    private Integer targetId;

    @Column(name = "MONTH_NO")
    private Integer monthNo;

    @Column(name = "PROD_IH")
    private Double prodIh;

    @Column(name = "PROD_SC")
    private Double prodSc;

    @Column(name = "DISP_IH")
    private Double dispIh;

    @Column(name = "DISP_SC")
    private Double dispSc;

    @Column(name = "EREC_IH")
    private Double erecIh;

    @Column(name = "EREC_SC")
    private Double erecSc;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

	public Integer getTargetMonthlyId() {
		return targetMonthlyId;
	}

	public void setTargetMonthlyId(Integer targetMonthlyId) {
		this.targetMonthlyId = targetMonthlyId;
	}

	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public Integer getMonthNo() {
		return monthNo;
	}

	public void setMonthNo(Integer monthNo) {
		this.monthNo = monthNo;
	}

	public Double getProdIh() {
		return prodIh;
	}

	public void setProdIh(Double prodIh) {
		this.prodIh = prodIh;
	}

	public Double getProdSc() {
		return prodSc;
	}

	public void setProdSc(Double prodSc) {
		this.prodSc = prodSc;
	}

	public Double getDispIh() {
		return dispIh;
	}

	public void setDispIh(Double dispIh) {
		this.dispIh = dispIh;
	}

	public Double getDispSc() {
		return dispSc;
	}

	public void setDispSc(Double dispSc) {
		this.dispSc = dispSc;
	}

	public Double getErecIh() {
		return erecIh;
	}

	public void setErecIh(Double erecIh) {
		this.erecIh = erecIh;
	}

	public Double getErecSc() {
		return erecSc;
	}

	public void setErecSc(Double erecSc) {
		this.erecSc = erecSc;
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

    // Getters & Setters
    
    
}
