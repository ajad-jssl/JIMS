package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONSOLIDATED_INVOICE_MASTER")
public class Consolidated_invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "invoice_no")
    private Long invoiceNo;

    @Column(name = "contract_id")
    private int contract_id;
    
    @Column(name = "load_id")
    private String load_id;
    
   
	@Column(name = "invoice_type")
    private String invoice_type;
    
    @Column(name = "conpn_id")
    private int conpn_id;
    
    @Column(name = "contract_slno")
    private String contract_slno;

   
    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "date_of_notification")
    private String dateOfNotification;

    @Column(name = "date_val")
    private String dateVal;

    @Column(name = "bg_type")
    private String bgType;

    @Column(name = "date_of_issue")
    private String dateOfIssue;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "lc_number")
    private String lcNumber;

    @Column(name = "supply_place")
    private String supplyPlace;

    @Column(name = "s_t_exempted")
    private String stExempted;

    @Column(name = "lr_docketno")
    private String lrDocketNo;

    @Column(name = "bg_no")
    private String bgNo;

    @Column(name = "date_of_expiry")
    private String dateOfExpiry;

    @Column(name = "date_of_ref")
    private String dateOfRef;

    @Column(name = "lc_issue_date")
    private String lcIssueDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    
    @Column(name = "contract_name")
    private String contract_name;
    
    @Column(name = "verified_status")
    private int verified_status;
    
    @Column(name = "factory_id")
    private int factory_id;
    
    public int getFactory_id() {
		return factory_id;
	}

	public void setFactory_id(int factory_id) {
		this.factory_id = factory_id;
	}

	public int getVerified_status() {
		return verified_status;
	}

	public void setVerified_status(int verified_status) {
		this.verified_status = verified_status;
	}

	public String getVerified_by() {
		return verified_by;
	}

	public void setVerified_by(String verified_by) {
		this.verified_by = verified_by;
	}

	public LocalDateTime getVerified_date() {
		return verified_date;
	}

	public void setVerified_date(LocalDateTime verified_date) {
		this.verified_date = verified_date;
	}

	@Column(name = "verified_by")
    private String verified_by;
    
    @Column(name = "verified_date")
    private LocalDateTime verified_date;
    
    public String getContract_name() {
		return contract_name;
	}

	public void setContract_name(String contract_name) {
		this.contract_name = contract_name;
	}

	// Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(Long invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getContract_slno() {
		return contract_slno;
	}

	public void setContract_slno(String contract_slno) {
		this.contract_slno = contract_slno;
	}

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDateOfNotification() {
        return dateOfNotification;
    }

    public void setDateOfNotification(String dateOfNotification) {
        this.dateOfNotification = dateOfNotification;
    }

    public String getDateVal() {
        return dateVal;
    }

    public void setDateVal(String dateVal) {
        this.dateVal = dateVal;
    }

    public String getBgType() {
        return bgType;
    }

    public void setBgType(String bgType) {
        this.bgType = bgType;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getLcNumber() {
        return lcNumber;
    }

    public void setLcNumber(String lcNumber) {
        this.lcNumber = lcNumber;
    }

    public String getSupplyPlace() {
        return supplyPlace;
    }

    public void setSupplyPlace(String supplyPlace) {
        this.supplyPlace = supplyPlace;
    }

    public String getStExempted() {
        return stExempted;
    }

    public void setStExempted(String stExempted) {
        this.stExempted = stExempted;
    }

    public String getLrDocketNo() {
        return lrDocketNo;
    }

    public void setLrDocketNo(String lrDocketNo) {
        this.lrDocketNo = lrDocketNo;
    }

    public String getBgNo() {
        return bgNo;
    }

    public void setBgNo(String bgNo) {
        this.bgNo = bgNo;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getDateOfRef() {
        return dateOfRef;
    }

    public void setDateOfRef(String dateOfRef) {
        this.dateOfRef = dateOfRef;
    }

    public String getLcIssueDate() {
        return lcIssueDate;
    }

    public void setLcIssueDate(String lcIssueDate) {
        this.lcIssueDate = lcIssueDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    public String getLoad_id() {
		return load_id;
	}

	public void setLoad_id(String load_id) {
		this.load_id = load_id;
	}

	public int getContract_id() {
		return contract_id;
	}

	public void setContract_id(int contract_id) {
		this.contract_id = contract_id;
	}

	

	public String getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	public int getPn_id() {
		return conpn_id;
	}

	public void setPn_id(int pn_id) {
		this.conpn_id = pn_id;
	}

	

}
