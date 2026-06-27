package com.JIMS.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "consolidated_invoice_items")
public class ConsolidatedInvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpn_id")
    private Integer cpnId;

    @Column(name = "slno")
    private Integer slno;

    @Column(name = "pn_id")
    private Integer pnId;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "per_kgs")
    private Double perKgs;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total")
    private Double total;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "stype")
    private String stype;

    @Column(name = "R_item_code")
    private String rItemCode;

    @Column(name = "inc_type")
    private String incType;

    @Column(name = "pices")
    private String pices;

    @Column(name = "taxexmamount")
    private Double taxexmamount;

    @Column(name = "invid")
    private String invid;

    @Column(name = "conpnid")
    private Integer conpnid;

    @Column(name = "itype")
    private Integer itype;

    @Column(name = "consol_inv_no")
    private String consolInvNo;
    
    @Column(name="cancel")
    private Integer Cancel;
    
    @Column(name="cancel_by")
    private String Cancelby;
    

    public String getCancelby() {
		return Cancelby;
	}
	public void setCancelby(String cancelby) {
		Cancelby = cancelby;
	}
	public Integer getCancel() {
		return Cancel;
	}
	public void setCancel(Integer cancel) {
		Cancel = cancel;
	}
	@Column(name = "cancel_date")
    private java.util.Date consolDate;

    // ✅ Getters and Setters
    public Integer getCpnId() { return cpnId; }
    public void setCpnId(Integer cpnId) { this.cpnId = cpnId; }

    public Integer getSlno() { return slno; }
    public void setSlno(Integer slno) { this.slno = slno; }

    public Integer getPnId() { return pnId; }
    public void setPnId(Integer pnId) { this.pnId = pnId; }

    public Double getQty() { return qty; }
    public void setQty(Double qty) { this.qty = qty; }

    public Double getPerKgs() { return perKgs; }
    public void setPerKgs(Double perKgs) { this.perKgs = perKgs; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getUom() { return uom; }
    public void setUom(String uom) { this.uom = uom; }

    public String getStype() { return stype; }
    public void setStype(String stype) { this.stype = stype; }

    public String getRItemCode() { return rItemCode; }
    public void setRItemCode(String rItemCode) { this.rItemCode = rItemCode; }

    public String getIncType() { return incType; }
    public void setIncType(String incType) { this.incType = incType; }

    public String getPices() { return pices; }
    public void setPices(String pices) { this.pices = pices; }

    public Double getTaxexmamount() { return taxexmamount; }
    public void setTaxexmamount(Double taxexmamount) { this.taxexmamount = taxexmamount; }

    public String getInvid() { return invid; }
    public void setInvid(String invid) { this.invid = invid; }

    public Integer getConpnid() { return conpnid; }
    public void setConpnid(Integer conpnid) { this.conpnid = conpnid; }

    public Integer getItype() { return itype; }
    public void setItype(Integer itype) { this.itype = itype; }

    public String getConsolInvNo() { return consolInvNo; }
    public void setConsolInvNo(String consolInvNo) { this.consolInvNo = consolInvNo; }

    public java.util.Date getConsolDate() { return consolDate; }
    public void setConsolDate(java.util.Date consolDate) { this.consolDate = consolDate; }
}
