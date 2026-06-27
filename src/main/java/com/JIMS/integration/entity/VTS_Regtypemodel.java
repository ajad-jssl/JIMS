package com.JIMS.integration.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "VTS_Regtype")  
public class VTS_Regtypemodel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tid;
	private String regtype;
	private Integer Created_by;
	private LocalDateTime Created_date;
	private Integer Modified_by;
	private LocalDateTime Modified_date;
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getRegtype() {
		return regtype;
	}
	public void setRegtype(String regtype) {
		this.regtype = regtype;
	}
	public Integer getCreated_by() {
		return Created_by;
	}
	public void setCreated_by(Integer created_by) {
		Created_by = created_by;
	}
	public LocalDateTime getCreated_date() {
		return Created_date;
	}
	public void setCreated_date(LocalDateTime created_date) {
		Created_date = created_date;
	}
	public Integer getModified_by() {
		return Modified_by;
	}
	public void setModified_by(Integer modified_by) {
		Modified_by = modified_by;
	}
	public LocalDateTime getModified_date() {
		return Modified_date;
	}
	public void setModified_date(LocalDateTime modified_date) {
		Modified_date = modified_date;
	}
}
