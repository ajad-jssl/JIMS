package com.JIMS.integration.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "VTS_invoice_exit")  
public class VTS_invoiceexitmodel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ine_id;
	   private String Vechile_id;
	    private String Invoice_id;
	    private LocalDateTime Ddate;
	    private LocalDateTime Vdate;
	    private String Dmbno;
	    private String Dname;
	    private String Vecno;
	    private String Transname;
	    private Integer Regtyp;
	    private String Dl;
	    private Integer Vtype;
	    private String Grn;
	    private LocalTime Intime;
	    private LocalTime Outtime;
	    private String Delvchaln;
	    private String Helper;
	    private String Grndate;
	    private String Dlvchndate;
	    private String Vuser;
	    private String Inremarks;
	    private String Outremarks;
	    private Integer Wtm;
	    private String Invoicedate;
	    private Integer Vendorid;
	    private String Invoice_no;
	    private String Rcflag;
	    private LocalDate RcValidThrough;
	    private String Insuflag;
	    private LocalDate InsuValidThrough;
	    private String Fitflag;
	    private LocalDate FitValidThrough;
	    private String Pucflag;
	    private LocalDate PucValidThrough;
	    private String Description;
	    private String Fpath;
	    private String Lr_no;
	    private Integer Reject;
	    private String Reject_remarks;
	    private String Securityreject_remarks;
	    private Integer Created_by;
	    private LocalDateTime Created_date;
	    private Integer Modified_by;
	    private LocalDateTime Modified_date;
	    private String Factory_id;
	    private String capture;
		public int getIne_id() {
			return ine_id;
		}
		public void setIne_id(int ine_id) {
			this.ine_id = ine_id;
		}
		public String getVechile_id() {
			return Vechile_id;
		}
		public void setVechile_id(String vechile_id) {
			Vechile_id = vechile_id;
		}
		public String getInvoice_id() {
			return Invoice_id;
		}
		public void setInvoice_id(String invoice_id) {
			Invoice_id = invoice_id;
		}
		public LocalDateTime getDdate() {
			return Ddate;
		}
		public void setDdate(LocalDateTime ddate) {
			Ddate = ddate;
		}
		public LocalDateTime getVdate() {
			return Vdate;
		}
		public void setVdate(LocalDateTime vdate) {
			Vdate = vdate;
		}
		public String getDmbno() {
			return Dmbno;
		}
		public void setDmbno(String dmbno) {
			Dmbno = dmbno;
		}
		public String getDname() {
			return Dname;
		}
		public void setDname(String dname) {
			Dname = dname;
		}
		public String getVecno() {
			return Vecno;
		}
		public void setVecno(String vecno) {
			Vecno = vecno;
		}
		public String getTransname() {
			return Transname;
		}
		public void setTransname(String transname) {
			Transname = transname;
		}
		public Integer getRegtyp() {
			return Regtyp;
		}
		public void setRegtyp(Integer regtyp) {
			Regtyp = regtyp;
		}
		public String getDl() {
			return Dl;
		}
		public void setDl(String dl) {
			Dl = dl;
		}
		public Integer getVtype() {
			return Vtype;
		}
		public void setVtype(Integer vtype) {
			Vtype = vtype;
		}
		public String getGrn() {
			return Grn;
		}
		public void setGrn(String grn) {
			Grn = grn;
		}
		public LocalTime getIntime() {
			return Intime;
		}
		public void setIntime(LocalTime intime) {
			Intime = intime;
		}
		public LocalTime getOuttime() {
			return Outtime;
		}
		public void setOuttime(LocalTime outtime) {
			Outtime = outtime;
		}
		public String getDelvchaln() {
			return Delvchaln;
		}
		public void setDelvchaln(String delvchaln) {
			Delvchaln = delvchaln;
		}
		public String getHelper() {
			return Helper;
		}
		public void setHelper(String helper) {
			Helper = helper;
		}
		public String getGrndate() {
			return Grndate;
		}
		public void setGrndate(String grndate) {
			Grndate = grndate;
		}
		public String getDlvchndate() {
			return Dlvchndate;
		}
		public void setDlvchndate(String dlvchndate) {
			Dlvchndate = dlvchndate;
		}
		public String getVuser() {
			return Vuser;
		}
		public void setVuser(String vuser) {
			Vuser = vuser;
		}
		public String getInremarks() {
			return Inremarks;
		}
		public void setInremarks(String inremarks) {
			Inremarks = inremarks;
		}
		public String getOutremarks() {
			return Outremarks;
		}
		public void setOutremarks(String outremarks) {
			Outremarks = outremarks;
		}
		public Integer getWtm() {
			return Wtm;
		}
		public void setWtm(Integer wtm) {
			Wtm = wtm;
		}
		public String getInvoicedate() {
			return Invoicedate;
		}
		public void setInvoicedate(String invoicedate) {
			Invoicedate = invoicedate;
		}
		public Integer getVendorid() {
			return Vendorid;
		}
		public void setVendorid(Integer vendorid) {
			Vendorid = vendorid;
		}
		public String getInvoice_no() {
			return Invoice_no;
		}
		public void setInvoice_no(String invoice_no) {
			Invoice_no = invoice_no;
		}
		public String getRcflag() {
			return Rcflag;
		}
		public void setRcflag(String rcflag) {
			Rcflag = rcflag;
		}
		public LocalDate getRcValidThrough() {
			return RcValidThrough;
		}
		public void setRcValidThrough(LocalDate rcValidThrough) {
			RcValidThrough = rcValidThrough;
		}
		public String getInsuflag() {
			return Insuflag;
		}
		public void setInsuflag(String insuflag) {
			Insuflag = insuflag;
		}
		public LocalDate getInsuValidThrough() {
			return InsuValidThrough;
		}
		public void setInsuValidThrough(LocalDate insuValidThrough) {
			InsuValidThrough = insuValidThrough;
		}
		public String getFitflag() {
			return Fitflag;
		}
		public void setFitflag(String fitflag) {
			Fitflag = fitflag;
		}
		public LocalDate getFitValidThrough() {
			return FitValidThrough;
		}
		public void setFitValidThrough(LocalDate fitValidThrough) {
			FitValidThrough = fitValidThrough;
		}
		public String getPucflag() {
			return Pucflag;
		}
		public void setPucflag(String pucflag) {
			Pucflag = pucflag;
		}
		public LocalDate getPucValidThrough() {
			return PucValidThrough;
		}
		public void setPucValidThrough(LocalDate pucValidThrough) {
			PucValidThrough = pucValidThrough;
		}
		public String getDescription() {
			return Description;
		}
		public void setDescription(String description) {
			Description = description;
		}
		public String getFpath() {
			return Fpath;
		}
		public void setFpath(String fpath) {
			Fpath = fpath;
		}
		public String getLr_no() {
			return Lr_no;
		}
		public void setLr_no(String lr_no) {
			Lr_no = lr_no;
		}
		
		public Integer getReject() {
			return Reject;
		}
		public void setReject(Integer reject) {
			Reject = reject;
		}
		public String getReject_remarks() {
			return Reject_remarks;
		}
		
		public String getSecurityreject_remarks() {
			return Securityreject_remarks;
		}
		public void setSecurityreject_remarks(String securityreject_remarks) {
			Securityreject_remarks = securityreject_remarks;
		}
		public void setReject_remarks(String reject_remarks) {
			Reject_remarks = reject_remarks;
		}
		public Integer getCreated_by() {
			return Created_by;
		}
		public void setCreated_by(Integer created_by) {
			Created_by = created_by;
		}
		public LocalDateTime getCreated_date() {
			return Created_date;
		}
		public void setCreated_date(LocalDateTime created_date) {
			Created_date = created_date;
		}
		public Integer getModified_by() {
			return Modified_by;
		}
		public void setModified_by(Integer modified_by) {
			Modified_by = modified_by;
		}
		public LocalDateTime getModified_date() {
			return Modified_date;
		}
		public void setModified_date(LocalDateTime modified_date) {
			Modified_date = modified_date;
		}
		public String getFactory_id() {
			return Factory_id;
		}
		public void setFactory_id(String factory_id) {
			Factory_id = factory_id;
		}
		public String getCapture() {
			return capture;
		}
		public void setCapture(String capture) {
			this.capture = capture;
		}
	
	    
		

}
