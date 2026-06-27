package com.JIMS.integration.entity;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserActionPermissions")
public class useractionpermissionmodel {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "Action_permission")
	    @JsonProperty("ActionpermissionId")
	private int ActionpermissionId;
	  
    @Column(name = "user_id")
    private String userId;

    @Column(name = "module_id")
    private int moduleId;

    @Column(name = "action")
    private String action;
    
    
    
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

	public int getActionpermissionId() {
		return ActionpermissionId;
	}

	public void setActionpermissionId(int actionpermissionId) {
		ActionpermissionId = actionpermissionId;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}