package com.JIMS.integration.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "VTS_Vechicletype")  
public class VTS_vtypemodel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int vid;
	private String Vtype;
	private Integer Created_by;
	private LocalDateTime Created_date;
	private Integer Modified_by;
	private LocalDateTime Modified_date;
	public int getVid() {
		return vid;
	}
	public void setVid(int vid) {
		this.vid = vid;
	}
	public String getVtype() {
		return Vtype;
	}
	public void setVtype(String vtype) {
		Vtype = vtype;
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
