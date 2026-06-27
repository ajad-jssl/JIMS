package com.JIMS.MIS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "tra_rates")
public class tra_rates {
    @Id
    @Column(name = "trate_id")
    private Integer trateid;

  
    @Column(name = "country")
    private String country;
    
  
    @Column(name = "county")
    private String county;
    

	public Integer getTrateid() {
		return trateid;
	}

	public void setTrateid(Integer trateid) {
		this.trateid = trateid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}
	
}
