package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "VW_JSSL_SubconContractLoad")
public class ContractModel {
	  @Id
	    @Column(name = "cid") // Ensure this matches the exact column name in the DB
	    private Integer cid;
 
	    @Column(name = "cname") 
	    private String cname;

	    // Getters and Setters
	    public Integer getcontractId() {
	        return cid;
	    }

	    public void setcontractId(Integer cid) {
	        this.cid = cid;
	    }

	    public String getcontractName() {
	        return cname;
	    }

	    public void setContractName(String cname) {
	        this.cname = cname;
	    }
}
