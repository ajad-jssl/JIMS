package com.JIMS.MIS.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
@Entity
@Table(name = "VW_JSSL_SubconContractLoad")
public class Contracts {
	 @Id
	 @Column(name = "contract_id")
	    private Integer contractid;
	 
	  @Column(name = "cname")
	    private String cname;
	// Getters and Setters
	  
	  public Integer getcontractId(){
		  return contractid;
	  }
	  
	  public void setcontractid(Integer contractid)
	  {
		  this.contractid=contractid;
	  }
	  
	   public String getContractName() {
	        return cname;
	    }

	    public void setContractName(String cname) {
	        this.cname = cname;
	    }
}




