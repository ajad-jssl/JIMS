package com.JIMS.integration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "unit")
public class ItemsModel {  

	 @Id
	 private int uid;
	 private String unit;  // Assuming 'rid' is of type Long
	    
	   
	    public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		// Getters and Setters
	    public String getunit() {
	        return unit;
	    }

	    public void setunit(String unit) {
	        this.unit = unit;
	    }
	   
	    
}
