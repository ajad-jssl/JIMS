package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USERS_MASTER")
@Data
public class InactiveUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "is_active")
    private Boolean isActive;

    
    @Column(name="Unlock_remarks")
    private String unlockRemarks;

    @Column(name="modified_by")
    private String modifiedBy;

    @Column(name="modified_date")
    private LocalDateTime modifiedDate;

    
    
    @Column(name="status_remarks")
    private String statusRemarks;
    
    
    
    @Column(name="login_attempt_count")
    private Integer LoginAttempt;
    
    
    public Integer getLoginAttempt() {
		return LoginAttempt;
	}

	public void setLoginAttempt(Integer loginAttempt) {
		LoginAttempt = loginAttempt;
	}

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public String getUnlockRemarks() {
		return unlockRemarks;
	}

	public void setUnlockRemarks(String unlockRemarks) {
		this.unlockRemarks = unlockRemarks;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsAdminLocked() {
		return isAdminLocked;
	}

	public void setIsAdminLocked(Boolean isAdminLocked) {
		this.isAdminLocked = isAdminLocked;
	}

	@Column(name = "is_admin_locked")
    private Boolean isAdminLocked;
	
	
	
	
@Column(name="is_delete")	
private Boolean isDeleted;

@Column(name="deleted_by")
private String deletedBy;


@Column(name="deleted_date")
private LocalDateTime deletedDate;



@Column(name="delete_remarks")
private String deleteRemarks;

public Boolean getIsDeleted() {
	return isDeleted;
}

public void setIsDeleted(Boolean isDeleted) {
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

public String getDeleteRemarks() {
	return deleteRemarks;
}

public void setDeleteRemarks(String deleteRemarks) {
	this.deleteRemarks = deleteRemarks;
}
	
	
	
	
}
