package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name = "Loads")
public class LoadModel {

	 @Id 
	 @Column(name = "load_id")
	    private Integer load_id;
	 
	  @Column(name = "pload")
	    private String pload;
	// Getters and Setters
	  
	  public Integer getloadId(){
		  return load_id;
	  }
	  
	  public void setloadId(Integer load_id)
	  {
		  this.load_id=load_id;
	  } 
	  
	   public String getpload() { 
	        return pload;
	    }

	    public void setpload(String pload) {
	        this.pload = pload;
	    }
}