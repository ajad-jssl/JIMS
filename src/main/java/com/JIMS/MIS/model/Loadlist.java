package com.JIMS.MIS.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name = "Tra_Loads")
public class Loadlist {
	 @Id
	 @Column(name = "tload_id")
	    private Integer tloadid;
	 
	  @Column(name = "loadno")
	    private String loadno;
	// Getters and Setters
	  
	  public Integer getloadId(){
		  return tloadid;
	  }
	  
	  public void setloadid(Integer tloadid)
	  {
		  this.tloadid=tloadid;
	  }
	  
	   public String getloadno() {
	        return loadno;
	    }

	    public void setloadno(String loadno) {
	        this.loadno = loadno;
	    }
}


