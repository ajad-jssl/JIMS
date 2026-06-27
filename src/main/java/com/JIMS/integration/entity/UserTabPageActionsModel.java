package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserTabPageActions")
public class UserTabPageActionsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ActionPermissionID")
    private int actionPermissionId;

    @Column(name = "UserID")
    private String userId;

    @Column(name = "TabID")
    private int tabId;

    @Column(name = "TabPageID")
    private int tabPageId;

    @Column(name = "ActionName")
    private String actionName;

    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @Column(name = "Is_Delete")
    private Integer isDelete;

    @Column(name = "DeletedBy")
    private String deletedBy;

    @Column(name = "DeletedDate")
    private LocalDateTime deletedDate;

	public int getActionPermissionId() {
		return actionPermissionId;
	}

	public void setActionPermissionId(int actionPermissionId) {
		this.actionPermissionId = actionPermissionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTabId() {
		return tabId;
	}

	public void setTabId(int tabId) {
		this.tabId = tabId;
	}

	public int getTabPageId() {
		return tabPageId;
	}

	public void setTabPageId(int tabPageId) {
		this.tabPageId = tabPageId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
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
    
    

}