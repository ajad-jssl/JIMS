package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VMS_Companymaster")  
public class VMS_companymodel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Company_id;
	private String Company_name;
	private String Address;
	private int State_id;
	private int City_id;
	private Integer Created_by;
	private LocalDateTime Created_date;
	private Integer Modified_by;
	private LocalDateTime Modified_date;
	public int getCompany_id() {
		return Company_id;
	}
	public void setCompany_id(int company_id) {
		Company_id = company_id;
	}
	
	public String getCompany_name() {
		return Company_name;
	}
	public void setCompany_name(String company_name) {
		Company_name = company_name;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public int getState_id() {
		return State_id;
	}
	public void setState_id(int state_id) {
		State_id = state_id;
	}
	public int getCity_id() {
		return City_id;
	}
	public void setCity_id(int city_id) {
		City_id = city_id;
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
