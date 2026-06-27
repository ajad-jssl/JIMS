package com.JIMS.integration.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class gpmscontract {
	
    @Id
    private Long contractId;
    private String jobCode;

    // Getters and Setters
    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }
}

	