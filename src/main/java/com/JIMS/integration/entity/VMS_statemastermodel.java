package com.JIMS.integration.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "VMS_Statemaster")
public class VMS_statemastermodel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int State_id;

    @Column(length = 100, nullable = false)
    private String State_name;

    private Integer Created_by;

    private LocalDateTime Created_date;

    private Integer Modified_by;

    private LocalDateTime Modified_date;

	public int getStateId() {
		return State_id;
	}

	public void setStateId(int stateId) {
		this.State_id = stateId;
	}

	public String getStateName() {
		return State_name;
	}

	public void setStateName(String stateName) {
		this.State_name = stateName;
	}

	public Integer getCreatedBy() {
		return Created_by;
	}

	public void setCreatedBy(Integer createdBy) {
		this.Created_by = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return Created_date;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.Created_date = createdDate;
	}

	public Integer getModifiedBy() {
		return Modified_by;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.Modified_by = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return Modified_date;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.Modified_date = modifiedDate;
	}
    
}
