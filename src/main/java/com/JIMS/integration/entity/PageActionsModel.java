package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PageActions")
public class PageActionsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private Integer PageID;

    private Integer ActionID;

    private Integer STATUS;

    @Column(name = "CREATED_BY")
    private String created_by;

    private LocalDateTime CREATED_DATE;

    @Column(name = "MODIFIED_BY")
    private String modified_by;

    private LocalDateTime MODIFIED_DATE;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Integer getPageID() {
		return PageID;
	}

	public void setPageID(Integer pageID) {
		PageID = pageID;
	}

	public Integer getActionID() {
		return ActionID;
	}

	public void setActionID(Integer actionID) {
		ActionID = actionID;
	}

	public Integer getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(Integer sTATUS) {
		STATUS = sTATUS;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public LocalDateTime getCREATED_DATE() {
		return CREATED_DATE;
	}

	public void setCREATED_DATE(LocalDateTime cREATED_DATE) {
		CREATED_DATE = cREATED_DATE;
	}

	public String getModified_by() {
		return modified_by;
	}

	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}

	public LocalDateTime getMODIFIED_DATE() {
		return MODIFIED_DATE;
	}

	public void setMODIFIED_DATE(LocalDateTime mODIFIED_DATE) {
		MODIFIED_DATE = mODIFIED_DATE;
	}

    
    
}