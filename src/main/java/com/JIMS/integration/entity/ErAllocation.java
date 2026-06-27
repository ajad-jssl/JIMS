package com.JIMS.integration.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Er_Allocation")
public class ErAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Allocation_id")
    private Integer allocationId;

    @Column(name = "Type_id")
    private Integer typeId;

    @Column(name = "Asseten_id")
    private Integer assetId;

    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "Make_id")
    private Integer makeId;

    @Column(name = "Status_id")
    private String statusId;

    @Column(name = "Allocated_qty")
    private Double allocatedQty;

    @Column(name = "Asset_srno")
    private String assetSerialNo;

    @Column(name = "Contract_id")
    private Integer contractId;

    @Column(name = "Allocated_to")
    private String allocatedTo;

    @Column(name = "Department")
    private String department;

    // Pure date (no time)
    @Column(name = "Purchase_date")
    private LocalDate purchaseDate;

    // Date + time
    @Column(name = "Allocated_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime allocatedDate;

    // Date + time
    @Column(name = "exp_return_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expectedReturnDate;

    @Column(name = "Allocatedrtn_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime allocatedReturnDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "Return_remark")
    private String returnRemark;

    @Column(name = "Created_by")
    private String createdBy;

    @Column(name = "Created_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "Modified_by")
    private String modifiedBy;

    @Column(name = "Modified_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedDate;

    // ===========================
    //        GETTERS & SETTERS
    // ===========================

    public Integer getAllocationId() { return allocationId; }
    public void setAllocationId(Integer allocationId) { this.allocationId = allocationId; }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    public Integer getAssetId() { return assetId; }
    public void setAssetId(Integer assetId) { this.assetId = assetId; }

    public Integer getModelId() { return modelId; }
    public void setModelId(Integer modelId) { this.modelId = modelId; }

    public Integer getMakeId() { return makeId; }
    public void setMakeId(Integer makeId) { this.makeId = makeId; }

    public String getStatusId() { return statusId; }
    public void setStatusId(String statusId) { this.statusId = statusId; }

    public Double getAllocatedQty() { return allocatedQty; }
    public void setAllocatedQty(Double allocatedQty) { this.allocatedQty = allocatedQty; }

    public String getAssetSerialNo() { return assetSerialNo; }
    public void setAssetSerialNo(String assetSerialNo) { this.assetSerialNo = assetSerialNo; }

    public Integer getContractId() { return contractId; }
    public void setContractId(Integer contractId) { this.contractId = contractId; }

    public String getAllocatedTo() { return allocatedTo; }
    public void setAllocatedTo(String allocatedTo) { this.allocatedTo = allocatedTo; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public LocalDateTime getAllocatedDate() { return allocatedDate; }
    public void setAllocatedDate(LocalDateTime allocatedDate) { this.allocatedDate = allocatedDate; }

    public LocalDateTime getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public LocalDateTime getAllocatedReturnDate() { return allocatedReturnDate; }
    public void setAllocatedReturnDate(LocalDateTime allocatedReturnDate) { this.allocatedReturnDate = allocatedReturnDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getReturnRemark() { return returnRemark; }
    public void setReturnRemark(String returnRemark) { this.returnRemark = returnRemark; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
}
