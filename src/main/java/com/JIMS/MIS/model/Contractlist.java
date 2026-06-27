package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "VW_JSSL_SubconContractLoad")
public class Contractlist {
    @Id
    @Column(name = "contract_id") // Ensure this matches the exact column name in the DB
    private Integer contractid;

    @Column(name = "cname") 
    private String cname;

    // Getters and Setters
    public Integer getContractId() {
        return contractid;
    }

    public void setContractId(Integer contractid) {
        this.contractid = contractid;
    }

    public String getContractName() {
        return cname;
    }

    public void setContractName(String cname) {
        this.cname = cname;
    }
}

