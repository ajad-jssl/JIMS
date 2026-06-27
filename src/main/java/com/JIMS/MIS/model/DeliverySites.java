package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name = "Delivery_Sites")
public class DeliverySites {
	 @Id
	 @Column(name = "delsite_id")
	    private Integer delsiteid;
	 
	  @Column(name = "shortname")
	    private String shortname;
	  
	  @Column(name = "del1")
	    private String del1;
	  
	  @Column(name = "del2")
	    private String del2;
	  
	  @Column(name = "del3")
	    private String del3;
	  
	  @Column(name = "del4")
	    private String del4;

	public Integer getDelsiteid() {
		return delsiteid;
	}

	public void setDelsiteid(Integer delsiteid) {
		this.delsiteid = delsiteid;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getDel1() {
		return del1;
	}

	public void setDel1(String del1) {
		this.del1 = del1;
	}

	public String getDel2() {
		return del2;
	}

	public void setDel2(String del2) {
		this.del2 = del2;
	}

	public String getDel3() {
		return del3;
	}

	public void setDel3(String del3) {
		this.del3 = del3;
	}

	public String getDel4() {
		return del4;
	}

	public void setDel4(String del4) {
		this.del4 = del4;
	}
	
	
}




