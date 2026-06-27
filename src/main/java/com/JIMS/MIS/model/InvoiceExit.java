package com.JIMS.MIS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_exit", schema = "dbo")
public class InvoiceExit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ine_id;

    private String vechile_id;
    
    @Column(name = "invoice_id")
    private Integer invoice_id;
    
    @Column(name = "ddate")
    private LocalDateTime ddate;
    
    @Column(name = "vdate")
    private LocalDateTime vdate;
    
    @Column(name = "dmbno")
    private String dmbno;
    
    @Column(name = "rdate",nullable = true)
    private LocalDateTime rdate;
    
    @Column(name = "reportedto")
    private String reportedto;
    
    @Column(name = "udate",nullable = true)
    private LocalDateTime udate;
    
    @Column(name = "reason")
    private String reason;
    
    @Column(name = "action")
    private String action;
    
    @Column(name = "dname")
    private String dname;
    
    @Column(name = "days")
    private String days;
    
    @Column(name = "vecno")
    private String vecno;
    
    @Column(name = "transname")
    private String transname;
    
    @Column(name = "regtyp")
    private Integer regtyp;
    
    @Column(name = "dl")
    private String dl;
    
    @Column(name = "vtype")
    private Integer vtype;
    
    @Column(name = "grn")
    private String grn;
    
    @Column(name = "matdec")
    private String matdec;
    
    @Column(name = "qty")
    private String qty;
    
    @Column(name = "intime")
    private String intime;
    
    @Column(name = "outtime")
    private String outtime;
    
    @Column(name = "delvchaln")
    private String delvchaln;
    
    @Column(name = "helper")
    private String helper;
    
    @Column(name = "uto")
    private String uto;
    
    @Column(name = "grndate",nullable = true)
    private String grndate;
    
    @Column(name = "dlvchndate",nullable = true)
    private String dlvchndate;
    
    @Column(name = "vuser")
    private String vuser;
    
    @Column(name = "inremarks")
    private String inremarks;
    
    @Column(name = "outremarks")
    private String outremarks;
    
    @Column(name = "wtm")
    private Integer wtm;
    
    @Column(name = "purpose")
    private String purpose;
    
    @Column(name = "invoicedate",nullable = true)
    private String invoicedate;
    
    @Column(name = "vendorid")
    private Integer vendorid;
    
    @Column(name = "invoice_no")
    private String invoice_no;
    
    @Column(name = "lrNo")
    private String lrNo;
    
    @Column(name = "rcflag")
    private String rcflag;
    
    @Column(name = "rcValidThrough",nullable = true)
    private LocalDateTime rcValidThrough;
    
    @Column(name = "insuflag")
    private String insuflag;
    
    @Column(name = "insuValidThrough",nullable = true)
    private LocalDateTime insuValidThrough;
    
    @Column(name = "fitflag")
    private String fitflag;
    
    @Column(name = "fitValidThrough",nullable = true)
    private LocalDateTime fitValidThrough;
    
    @Column(name = "pucflag")
    private String pucflag;
    
    @Column(name = "pucValidThrough",nullable = true)
    private LocalDateTime pucValidThrough;
    
    @Column(name = "selfVehicle")
    private String selfVehicle;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "fname")
    private String fname;
    
    @Column(name = "fpath")
    private String fpath;

    // Getters and setters
    public Integer getIneId() {
        return ine_id;
    }

    public void setIneId(Integer ineId) {
        this.ine_id = ineId;
    }

    public String getVechileId() {
        return vechile_id;
    }

    public void setVechileId(String vechileId) {
        this.vechile_id = vechileId;
    }

    public Integer getInvoiceId() {
        return invoice_id;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoice_id = invoiceId;
    }

    public LocalDateTime getDdate() {
        return ddate;
    }

    public void setDdate(LocalDateTime ddate) {
        this.ddate = ddate;
    }

    public LocalDateTime getVdate() {
        return vdate;
    }

    public void setVdate(LocalDateTime vdate) {
        this.vdate = vdate;
    }

    public String getDmbno() {
        return dmbno;
    }

    public void setDmbno(String dmbno) {
        this.dmbno = dmbno;
    }

    public LocalDateTime getRdate() {
        return rdate;
    }

    public void setRdate(LocalDateTime rdate) {
        this.rdate = rdate;
    }

    public String getReportedto() {
        return reportedto;
    }

    public void setReportedto(String reportedto) {
        this.reportedto = reportedto;
    }

    public LocalDateTime getUdate() {
        return udate;
    }

    public void setUdate(LocalDateTime udate) {
        this.udate = udate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getVecno() {
        return vecno;
    }

    public void setVecno(String vecno) {
        this.vecno = vecno;
    }

    public String getTransname() {
        return transname;
    }

    public void setTransname(String transname) {
        this.transname = transname;
    }

    public Integer getRegtyp() {
        return regtyp;
    }

    public void setRegtyp(Integer regtyp) {
        this.regtyp = regtyp;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public Integer getVtype() {
        return vtype;
    }

    public void setVtype(Integer vtype) {
        this.vtype = vtype;
    }

    public String getGrn() {
        return grn;
    }

    public void setGrn(String grn) {
        this.grn = grn;
    }

    public String getMatdec() {
        return matdec;
    }

    public void setMatdec(String matdec) {
        this.matdec = matdec;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getDelvchaln() {
        return delvchaln;
    }

    public void setDelvchaln(String delvchaln) {
        this.delvchaln = delvchaln;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public String getUto() {
        return uto;
    }

    public void setUto(String uto) {
        this.uto = uto;
    }

    public String getGrndate() {
        return grndate;
    }

    public void setGrndate(String grndate) {
        this.grndate = grndate;
    }

    public String getDlvchndate() {
        return dlvchndate;
    }

    public void setDlvchndate(String dlvchndate) {
        this.dlvchndate = dlvchndate;
    }

    public String getVuser() {
        return vuser;
    }

    public void setVuser(String vuser) {
        this.vuser = vuser;
    }

    public String getInremarks() {
        return inremarks;
    }

    public void setInremarks(String inremarks) {
        this.inremarks = inremarks;
    }

    public String getOutremarks() {
        return outremarks;
    }

    public void setOutremarks(String outremarks) {
        this.outremarks = outremarks;
    }

    public Integer getWtm() {
        return wtm;
    }

    public void setWtm(Integer wtm) {
        this.wtm = wtm;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public Integer getVendorid() {
        return vendorid;
    }

    public void setVendorid(Integer vendorid) {
        this.vendorid = vendorid;
    }

    public String getInvoiceNo() {
        return invoice_no;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoice_no = invoiceNo;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getRcflag() {
        return rcflag;
    }

    public void setRcflag(String rcflag) {
        this.rcflag = rcflag;
    }

    public LocalDateTime getRcValidThrough() {
        return rcValidThrough;
    }

    public void setRcValidThrough(LocalDateTime rcValidThrough) {
        this.rcValidThrough = rcValidThrough;
    }

    public String getInsuflag() {
        return insuflag;
    }

    public void setInsuflag(String insuflag) {
        this.insuflag = insuflag;
    }

    public LocalDateTime getInsuValidThrough() {
        return insuValidThrough;
    }

    public void setInsuValidThrough(LocalDateTime insuValidThrough) {
        this.insuValidThrough = insuValidThrough;
    }

    public String getFitflag() {
        return fitflag;
    }

    public void setFitflag(String fitflag) {
        this.fitflag = fitflag;
    }

    public LocalDateTime getFitValidThrough() {
        return fitValidThrough;
    }

    public void setFitValidThrough(LocalDateTime fitValidThrough) {
        this.fitValidThrough = fitValidThrough;
    }

    public String getPucflag() {
        return pucflag;
    }

    public void setPucflag(String pucflag) {
        this.pucflag = pucflag;
    }

    public LocalDateTime getPucValidThrough() {
        return pucValidThrough;
    }

    public void setPucValidThrough(LocalDateTime pucValidThrough) {
        this.pucValidThrough = pucValidThrough;
    }

    public String getSelfVehicle() {
        return selfVehicle;
    }

    public void setSelfVehicle(String selfVehicle) {
        this.selfVehicle = selfVehicle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFpath() {
        return fpath;
    }

    public void setFpath(String fpath) {
        this.fpath = fpath;
    }
}


