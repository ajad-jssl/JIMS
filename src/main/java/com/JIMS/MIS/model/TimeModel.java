package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name = "Loads")
public class TimeModel {
	
	 @Id 	 
	  @Column(name = "weight")
	    private String weight; 
	// Getters and Setters
	  private Integer load_id; 
	  
	   public String getweight() { 
	        return weight;
	    }

	    public void setweight(String weight) {
	        this.weight = weight;
	    }
	    
	    public Integer getload_id() { 
	        return load_id; 
	    }

	    public void setload_id(Integer load_id) {
	        this.load_id = load_id;
	    }

}
