package com.JIMS.integration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Er_asseten")
public class Asset_Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Asseten_id")
    private Integer assetenId;

    @Column(name = "Type_id")
    private Integer typeId;

    @Column(name = "Contract_id")
    private Integer contractId;

    @Column(name = "Make_id")
    private Integer makeId;

    @Column(name = "Asset_srno", length = 25)
    private String assetSrNo;

    @Column(name = "Purchase_date")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    @Column(name = "Purchase_order", length = 255)
    private String purchaseOrder;

    @Column(name = "Cost")
    private Float cost;

    @Column(name = "Vendor_id", length = 50)
    private String vendorId;

    @Column(name = "Wstart_date")
    @Temporal(TemporalType.DATE)
    private Date wstartDate;

    @Column(name = "Wend_date")
    @Temporal(TemporalType.DATE)
    private Date wendDate;

    @Column(name = "Status_id")
    private Integer statusId;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "Assertor_id")
    private Integer assertorId;

    @Column(name = "Remarks", length = 500)
    private String remarks;

    @Column(name = "Asset_no", length = 255,insertable = false, updatable = false)
    private String assetNo;

    @Column(name = "Model_id")
    private Integer modelId;

    @Column(name = "Created_by", length = 255)
    private String createdBy;

    @Column(name = "Created_date")
    private LocalDateTime createdDate;  // ✅ Changed from Date to LocalDateTime

    @Column(name = "Modified_by", length = 255)
    private String modifiedBy;

    @Column(name = "Modified_date")
    private LocalDateTime modifiedDate;  // ✅ Changed from Date to LocalDateTime

    @Column(name = "status")
    private Integer status;

    @Column(name = "AssetAM_AT")
    private Integer assetAmAt;

    // Getters and Setters
    public Integer getAssetenId() { return assetenId; }
    public void setAssetenId(Integer assetenId) { this.assetenId = assetenId; }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }

    public Integer getMakeId() { return makeId; }
    public void setMakeId(Integer makeId) { this.makeId = makeId; }

    public String getAssetSrNo() { return assetSrNo; }
    public void setAssetSrNo(String assetSrNo) { this.assetSrNo = assetSrNo; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public String getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(String purchaseOrder) { this.purchaseOrder = purchaseOrder; }

    public Float getCost() { return cost; }
    public void setCost(Float cost) { this.cost = cost; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public Date getWstartDate() { return wstartDate; }
    public void setWstartDate(Date wstartDate) { this.wstartDate = wstartDate; }

    public Date getWendDate() { return wendDate; }
    public void setWendDate(Date wendDate) { this.wendDate = wendDate; }

    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Integer getAssertorId() { return assertorId; }
    public void setAssertorId(Integer assertorId) { this.assertorId = assertorId; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getAssetNo() { return assetNo; }
    public void setAssetNo(String assetNo) { this.assetNo = assetNo; }

    public Integer getModelId() { return modelId; }
    public void setModelId(Integer modelId) { this.modelId = modelId; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getAssetAmAt() { return assetAmAt; }
    public void setAssetAmAt(Integer assetAmAt) { this.assetAmAt = assetAmAt; }
}
