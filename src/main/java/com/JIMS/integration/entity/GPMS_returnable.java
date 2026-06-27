package com.JIMS.integration.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="GPMS_tabRetGatePass",uniqueConstraints = @UniqueConstraint(columnNames = {"gp_no"}))
public class GPMS_returnable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int gpid;
	private String gp_no;
	private Integer GP_type;
	private String requestedby;
	private String dept;
	private Integer sent_type;
	private Integer sentToClient;
	private Integer sentToemployee;
	private String sentThrough;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
	private String note;
	private LocalDateTime returnedOn;
	private String receiver;
	private String remarks;
	private String encl;
	private Integer security;
	private String securityout;
	private LocalDateTime securityoutdt;
	private Integer createdby;
	private LocalDateTime createddate;
	private String project;
	private String securityremark;
	

	private String exitweight;
	private  String gstDCNo;
	private Integer UpdatedBy;
	private LocalDateTime UpdatedDate;
	private	String UpdateReason;
	private	String cancelstatus;
	private	String cancelreason;
	private Integer Factory_id;
	
	public int getGpid() {
		return gpid;
	}
	public void setGpid(int gpid) {
		this.gpid = gpid;
	}
	public String getGp_no() {
		return gp_no;
	}
	public void setGp_no(String gp_no) {
		this.gp_no = gp_no;
	}
	public Integer getGP_type() {
		return GP_type;
	}
	public void setGP_type(Integer gP_type) {
		GP_type = gP_type;
	}

	public String getRequestedby() {
		return requestedby;
	}
	public void setRequestedby(String requestedby) {
		this.requestedby = requestedby;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public Integer getsent_type() {
		return sent_type;
	}
	public void setsent_type(Integer sent_type) {
		this.sent_type = sent_type;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getEncl() {
		return encl;
	}
	public void setEncl(String encl) {
		this.encl = encl;
	}
	public Integer getSecurity() {
		return security;
	}
	public void setSecurity(Integer security) {
		this.security = security;
	}
	public String getSecurityout() {
		return securityout;
	}
	public void setSecurityout(String securityout) {
		this.securityout = securityout;
	}

	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getSecurityremark() {
		return securityremark;
	}
	public void setSecurityremark(String securityremark) {
		this.securityremark = securityremark;
	}
	public String getExitweight() {
		return exitweight;
	}
	public void setExitweight(String exitweight) {
		this.exitweight = exitweight;
	}
	public String getGstDCNo() {
		return gstDCNo;
	}
	public void setGstDCNo(String gstDCNo) {
		this.gstDCNo = gstDCNo;
	}
	public Integer getUpdatedBy() {
		return UpdatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		UpdatedBy = updatedBy;
	}

	public String getUpdateReason() {
		return UpdateReason;
	}
	public void setUpdateReason(String updateReason) {
		UpdateReason = updateReason;
	}
	public String getCancelstatus() {
		return cancelstatus;
	}
	public void setCancelstatus(String cancelstatus) {
		this.cancelstatus = cancelstatus;
	}
	public String getCancelreason() {
		return cancelreason;
	}
	public void setCancelreason(String cancelreason) {
		this.cancelreason = cancelreason;
	}
	public LocalDateTime getReturnedOn() {
		return returnedOn;
	}
	public void setReturnedOn(LocalDateTime returnedOn) {
		this.returnedOn = returnedOn;
	}
	public LocalDateTime getSecurityoutdt() {
		return securityoutdt;
	}
	public void setSecurityoutdt(LocalDateTime securityoutdt) {
		this.securityoutdt = securityoutdt;
	}
	public LocalDateTime getCreateddate() {
		return createddate;
	}
	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}
	public LocalDateTime getUpdatedDate() {
		return UpdatedDate;
	}
	public void setUpdatedDate(LocalDateTime updatedDate) {
		UpdatedDate = updatedDate;
	}
	public Integer getSentToClient() {
		return sentToClient;
	}
	public void setSentToClient(Integer sentToClient) {
		this.sentToClient = sentToClient;
	}
	public Integer getsentToemployee() {
		return sentToemployee;
	}
	public void setsentToemployee(Integer sentToemployee) {
		this.sentToemployee = sentToemployee;
	}
	public String getSentThrough() {
		return sentThrough;
	}
	public void setSentThrough(String sentThrough) {
		this.sentThrough = sentThrough;
	}
	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	public Integer getFactory_id() {
        return Factory_id;
}
    public void setFactory_id(Integer factory_id) {
        this.Factory_id = factory_id;
}
	
}
