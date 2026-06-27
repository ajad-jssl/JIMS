package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS_MASTER")
public class usermastermodel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String user_id;
    private String password;
    private String username;
    private String email;
    private String role_id;
    private String mobile_number;
    private String created_by;
    private LocalDateTime created_date;
    private String modified_by;
    private LocalDateTime modified_date;
    private LocalDateTime last_login_date;
    private boolean is_admin_locked;
    private boolean is_active;
    private int login_attempt_count;
    private LocalDateTime password_modified_time;
    private boolean is_delete;
    private boolean is_loggedin;
    private String factory_id;
    private String password_otp;
    private LocalDateTime otp_timevalidation;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public LocalDateTime getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	public LocalDateTime getModified_date() {
		return modified_date;
	}
	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}
	public LocalDateTime getLast_login_date() {
		return last_login_date;
	}
	public void setLast_login_date(LocalDateTime last_login_date) {
		this.last_login_date = last_login_date;
	}
	public boolean isIs_admin_locked() {
		return is_admin_locked;
	}
	public void setIs_admin_locked(boolean is_admin_locked) {
		this.is_admin_locked = is_admin_locked;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public int getLogin_attempt_count() {
		return login_attempt_count;
	}
	public void setLogin_attempt_count(int login_attempt_count) {
		this.login_attempt_count = login_attempt_count;
	}
	public LocalDateTime getPassword_modified_time() {
		return password_modified_time;
	}
	public void setPassword_modified_time(LocalDateTime password_modified_time) {
		this.password_modified_time = password_modified_time;
	}
	public boolean isIs_delete() {
		return is_delete;
	}
	public void setIs_delete(boolean is_delete) {
		this.is_delete = is_delete;
	}
	public boolean isIs_loggedin() {
		return is_loggedin;
	}
	public void setIs_loggedin(boolean is_loggedin) {
		this.is_loggedin = is_loggedin;
	}
	public String getFactory_id() {
		return factory_id;
	}
	public void setFactory_id(String factory_id) {
		this.factory_id = factory_id;
	}
	public String getPassword_otp() {
		return password_otp;
	}
	public void setPassword_otp(String password_otp) {
		this.password_otp = password_otp;
	}
	public LocalDateTime getOtp_timevalidation() {
		return otp_timevalidation;
	}
	public void setOtp_timevalidation(LocalDateTime otp_timevalidation) {
		this.otp_timevalidation = otp_timevalidation;
	}
	
}
