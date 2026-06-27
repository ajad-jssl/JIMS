package com.JIMS.MIS.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name="t")
public class Lotnumber {
 @Id
 @Column(name ="tload_id")
 private Integer tload_id;
 
 @Column(name="loadno")
 private String loadno;

public Integer getTload_id() {
	return tload_id;
}

public void setTload_id(Integer tload_id) {
	this.tload_id = tload_id;
}

public String getLoadno() {
	return loadno;
}

public void setLoadno(String loadno) {
	this.loadno = loadno;
}
 
 
}
