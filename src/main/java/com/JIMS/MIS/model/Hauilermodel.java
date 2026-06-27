package com.JIMS.MIS.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name = "hauliers")
public class Hauilermodel {
	 @Id
	 @Column(name = "haulier_id")
	    private Integer haulierid;
	 
	  @Column(name = "company")
	    private String company;

	public Integer getHaulierid() {
		return haulierid;
	}

	public void setHaulierid(Integer haulierid) {
		this.haulierid = haulierid;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
