package com.JIMS.MIS.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class bindloadmodel {
	 @Id
	 private int load_id;
	 private String descr;  // Added descr field
	 private int pzone;

	 
	 
	public int getLoad_id() {
		return load_id;
	}
	public void setLoad_id(int load_id) {
		this.load_id = load_id;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public int getPzone() {
		return pzone;
	}
	public void setPzone(int pzone) {
		this.pzone = pzone;
	}


}
