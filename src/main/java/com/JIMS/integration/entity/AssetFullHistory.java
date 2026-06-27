package com.JIMS.integration.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import java.util.Date;

@Entity
@Immutable
@Table(name = "vw_Full_Asset_Timeline5")
public class AssetFullHistory {

    @Id
    @Column(name = "Id")
    private Long id;

    @Column(name = "HistoryId")
    private Integer historyId;

    @Column(name = "AssetId")
    private Integer assetId;

    @Column(name = "Type_id")
    private Integer typeId;

    @Column(name = "Model_id")
    private Integer modelId;

    @Column(name = "Make_id")
    private Integer makeId;

    @Column(name = "Status_id")
    private Integer statusId;

    @Column(name = "Asset_srno")
    private String assetSrNo;

    @Column(name = "Contract_id")
    private Integer contractId;

    @Column(name = "Allocation_id")
    private Long allocationId;

    @Column(name = "Allocated_qty")
    private Integer allocatedQty;

    @Column(name = "Allocated_to")
    private String allocatedTo;

    @Column(name = "Department")
    private String department;

    @Column(name = "Allocated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;

    @Column(name = "Exp_return_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedReturnDate;

    @Column(name = "Allocatedrtn_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    @Column(name = "Return_remark")
    private String returnRemark;

    @Column(name = "Purchase_date")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    @Column(name = "Purchase_order")
    private String purchaseOrder;

    @Column(name = "Cost")
    private Double cost;

    @Column(name = "Wstart_date")
    @Temporal(TemporalType.DATE)
    private Date warrantyStart;

    @Column(name = "Wend_date")
    @Temporal(TemporalType.DATE)
    private Date warrantyEnd;

    @Column(name = "Created_by")
    private Integer createdBy;

    @Column(name = "Created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "Modified_by")
    private Integer modifiedBy;

    @Column(name = "Modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(name = "Operation_Type")
    private String operationType;

    @Column(name = "Operation_Time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "Remarks")
    private String remarks;

    @Column(name = "Asset_no")
    private String assetNo;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "Vendor_id")
    private Integer vendorId;

    @Column(name = "ExtraStatus")
    private Integer extraStatus;

    @Column(name = "AssetAM_AT")
    private String assetAmAt;

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getHistoryId() { return historyId; }
    public void setHistoryId(Integer historyId) { this.historyId = historyId; }

    public Integer getAssetId() { return assetId; }
    public void setAssetId(Integer assetId) { this.assetId = assetId; }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    public Integer getModelId() { return modelId; }
    public void setModelId(Integer modelId) { this.modelId = modelId; }

    public Integer getMakeId() { return makeId; }
    public void setMakeId(Integer makeId) { this.makeId = makeId; }

    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }

    public String getAssetSrNo() { return assetSrNo; }
    public void setAssetSrNo(String assetSrNo) { this.assetSrNo = assetSrNo; }

    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }

    public Long getAllocationId() { return allocationId; }
    public void setAllocationId(Long allocationId) { this.allocationId = allocationId; }

    public Integer getAllocatedQty() { return allocatedQty; }
    public void setAllocatedQty(Integer allocatedQty) { this.allocatedQty = allocatedQty; }

    public String getAllocatedTo() { return allocatedTo; }
    public void setAllocatedTo(String allocatedTo) { this.allocatedTo = allocatedTo; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Date getAssignedDate() { return assignedDate; }
    public void setAssignedDate(Date assignedDate) { this.assignedDate = assignedDate; }

    public Date getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(Date expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public String getReturnRemark() { return returnRemark; }
    public void setReturnRemark(String returnRemark) { this.returnRemark = returnRemark; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public String getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(String purchaseOrder) { this.purchaseOrder = purchaseOrder; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public Date getWarrantyStart() { return warrantyStart; }
    public void setWarrantyStart(Date warrantyStart) { this.warrantyStart = warrantyStart; }

    public Date getWarrantyEnd() { return warrantyEnd; }
    public void setWarrantyEnd(Date warrantyEnd) { this.warrantyEnd = warrantyEnd; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Integer getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(Integer modifiedBy) { this.modifiedBy = modifiedBy; }

    public Date getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getAssetNo() { return assetNo; }
    public void setAssetNo(String assetNo) { this.assetNo = assetNo; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }

    public Integer getExtraStatus() { return extraStatus; }
    public void setExtraStatus(Integer extraStatus) { this.extraStatus = extraStatus; }

    public String getAssetAmAt() { return assetAmAt; }
    public void setAssetAmAt(String assetAmAt) { this.assetAmAt = assetAmAt; }

}
