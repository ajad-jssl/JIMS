package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "UserPermissionTabs")
public class UserPermissionTabsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissionTabID")
    @JsonProperty("permissionTabId")
    private int permissionTabId;

    @Column(name = "UserID")
    @JsonProperty("userId")
    private String userId;

    @Column(name = "ModuleID")
    @JsonProperty("moduleId")
    private int moduleId;

    @Column(name = "PageID")
    @JsonProperty("pageId")
    private int pageId;

    @Column(name = "TabId")
    @JsonProperty("tabName")
    private String tabName;

    @Column(name = "CreatedBy")
    @JsonProperty("createdBy")
    private String createdBy;

    @Column(name = "CreatedDate")
    @JsonProperty("createdDate")
    private LocalDateTime createdDate;

    @Column(name = "Is_Delete")
    @JsonProperty("isDelete")
    private Integer isDelete;

    @Column(name = "DeletedBy")
    @JsonProperty("deletedBy")
    private String deletedBy;

    @Column(name = "DeletedDate")
    @JsonProperty("deletedDate")
    private LocalDateTime deletedDate;

    // Getters & Setters
    public int getPermissionTabId() { return permissionTabId; }
    public void setPermissionTabId(int permissionTabId) { this.permissionTabId = permissionTabId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }

    public int getPageId() { return pageId; }
    public void setPageId(int pageId) { this.pageId = pageId; }

    public String getTabName() { return tabName; }
    public void setTabName(String tabName) { this.tabName = tabName; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Integer getIsDelete() { return isDelete; }
    public void setIsDelete(Integer isDelete) { this.isDelete = isDelete; }

    public String getDeletedBy() { return deletedBy; }
    public void setDeletedBy(String deletedBy) { this.deletedBy = deletedBy; }

    public LocalDateTime getDeletedDate() { return deletedDate; }
    public void setDeletedDate(LocalDateTime deletedDate) { this.deletedDate = deletedDate; }
}