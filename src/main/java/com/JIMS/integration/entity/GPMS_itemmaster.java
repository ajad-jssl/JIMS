package com.JIMS.integration.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "GPMS_ITEMMASTER")
public class GPMS_itemmaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemid;

    private String ItemNo;
    private String um;
    private String itmDescription1;
    private String itmDescription2;
    private String itmRemark;

    private int Created_by;                  // ✅ int is fine — always set to 20

    private LocalDateTime Created_date;

    @Column(nullable = true)
    private Integer Modified_by;            // ✅ Changed int → Integer (nullable)

    @Column(nullable = true)
    private LocalDateTime Modified_date;    // ✅ Already object type, nullable

    public int getItemid() {
        return itemid;
    }
    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getItemNo() {
        return ItemNo;
    }
    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getUm() {
        return um;
    }
    public void setUm(String um) {
        this.um = um;
    }

    public String getItmDescription1() {
        return itmDescription1;
    }
    public void setItmDescription1(String itmDescription1) {
        this.itmDescription1 = itmDescription1;
    }

    public String getItmDescription2() {
        return itmDescription2;
    }
    public void setItmDescription2(String itmDescription2) {
        this.itmDescription2 = itmDescription2;
    }

    public String getItmRemark() {
        return itmRemark;
    }
    public void setItmRemark(String itmRemark) {
        this.itmRemark = itmRemark;
    }

    public int getCreated_by() {
        return Created_by;
    }
    public void setCreated_by(int created_by) {
        Created_by = created_by;
    }

    public LocalDateTime getCreated_date() {
        return Created_date;
    }
    public void setCreated_date(LocalDateTime created_date) {
        Created_date = created_date;
    }

    public Integer getModified_by() {       // ✅ Return type Integer, not int
        return Modified_by;
    }
    public void setModified_by(Integer modified_by) {  // ✅ Parameter type Integer
        Modified_by = modified_by;
    }

    public LocalDateTime getModified_date() {
        return Modified_date;
    }
    public void setModified_date(LocalDateTime modified_date) {
        Modified_date = modified_date;
    }
}