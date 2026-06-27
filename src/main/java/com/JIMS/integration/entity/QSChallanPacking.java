package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="QSCHALLAN_PACKINGNOTE_MASTER")
public class QSChallanPacking {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pn_id")
    private int pnId; 
    @Column(name = "contract_id")
    private String conId; 
    @Column(name = "load_id")
    private String loadid; 
    @Column(name = "invoice_type_id")
    private int itypeID; 
	@Column(name = "created_by")
    private String createdBy; 
    @Column(name = "filepath")
    private String filepath;
    @Column(name = "transport_name")
    private String transportname;
    @Column(name = "vechile_no")
    private String vechileno;
    @Column(name = "milestone_id")
    private int milestone_id;
    @Column(name = "created_date")
    private LocalDateTime created_date;
    @Column(name = "factory_id")
    private int factory_id;
    @Column(name = "grand_total")
    private String grand_total;
    @Column(name = "cancel")
    private int cancel;
   
	public int getPnId() {
		return pnId;
	}
	public void setPnId(int pnId) {
		this.pnId = pnId;
	}
	public String getConId() {
		return conId;
	}
	public void setConId(String conId) {
		this.conId = conId;
	}
	public String getloadid() {
		return loadid;
	}
	public void setloadid(String loadid) {
		this.loadid = loadid;
	}
	public int getitypeID() {
		return itypeID;
	}
	public void setitypeID(int itypeID) {
		this.itypeID = itypeID;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String gettransportname() {
		return transportname;
	}
	public void settransportname(String transportname) {
		this.transportname = transportname;
	}
	public String getvechileno() {
		return vechileno;
	}
	public void setvechileno(String vechileno) {
		this.vechileno = vechileno;
	}
	public int getMilestone_id() {
		return milestone_id;
	}
	public void setMilestone_id(int milestone_id) {
		this.milestone_id = milestone_id;
	}
	public LocalDateTime getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}
    
	public int getFactory_id() {
	    return factory_id;
	}

	public void setFactory_id(int factory_id) {
	    this.factory_id = factory_id;
	}

	public String getGrand_total() {
	    return grand_total;
	}

	public void setGrand_total(String grand_total) {
	    this.grand_total = grand_total;
	}
	
	public int getcancel() {
		return cancel;
	}
	public void setcancel(int cancel) {
		this.cancel = cancel;
	}

}
