package com.JIMS.MIS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Contracts", schema = "dbo")
public class AMS_Contracts {

    @Id
    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "JobCode")
    private String jobCode;

    @Column(name = "cname")
    private String cname;
    
    @Column(name="descr")
    private String description;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    // --- Getters and Setters ---

    
}
