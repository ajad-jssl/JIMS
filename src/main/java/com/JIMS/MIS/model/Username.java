package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
@Entity
@Table(name = "Users")
public class Username {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_fullname")
    private String userFullName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "deleted")
    private Integer deleted;
    
    
    @Column(name ="locked")
    private Integer locked;
    
    @Column(name = "inactive")
    private Integer inactive;

    @Column(name ="remarks")
    private String remarks;
    
    
   @Column(name = "password_attempts")
    private Integer passwordAttempts;
    
    
    
    

	public Integer getPasswordAttempts() {
	return passwordAttempts;
}

   public void setPasswordAttempts(Integer passwordAttempts) {
	this.passwordAttempts = passwordAttempts;
   }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	// Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public Integer getInactive() {
		return inactive;
	}

	public void setInactive(Integer inactive) {
		this.inactive = inactive;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
