package com.JIMS.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="USERS_MASTER")
public class UsersMaster {

	 @Id
	    @Column(name = "id")
	    private Integer id;

	    @Column(name = "user_id")
	    private String userId;

	    @Column(name = "username")
	    private String userName;

	    @Column(name = "Department")
	    private String department;

	    // Getters & Setters
	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public String getUserName() {
	        return userName;
	    }

	    public void setUserName(String userName) {
	        this.userName = userName;
	    }

	    public String getDepartment() {
	        return department;
	    }

	    public void setDepartment(String department) {
	        this.department = department;
	    }
}
