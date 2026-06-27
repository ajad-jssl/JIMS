package com.JIMS.integration.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Er_Assign_Con_Master")
public class Employee_Assign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "atid")
    private Long atid;

    @Column(name = "conid")
    @JsonProperty("conid")
    private Integer conId;

    @Column(name = "did")
    @JsonProperty("did")
    private Integer did;

    @Column(name = "employee_code")
    @JsonProperty("employee_code")
    private String employeeCode;

    @Column(name = "created_by")
    @JsonProperty("created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    @JsonProperty("modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    @JsonProperty("modified_date")
    private LocalDateTime modifiedDate;

    // 📌 Getter and Setter methods
    public Long getAtid() {
        return atid;
    }
    public void setAtid(Long atid) {
        this.atid = atid;
    }

    public Integer getConId() {
        return conId;
    }
    public void setConId(Integer conId) {
        this.conId = conId;
    }

    public Integer getDid() {
        return did;
    }
    public void setDid(Integer did) {
        this.did = did;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
