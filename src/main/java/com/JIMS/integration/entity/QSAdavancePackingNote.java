package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "QSADVANCE_PACKINGNOTE_MASTER")
public class QSAdavancePackingNote {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pn_id")
    private int pnId; // Assuming the primary key column name is "pn_id"
    @Column(name = "contract_id")
    private String conId; // Assuming the column name is "con_id"
    @Column(name = "milestone_id")
    private int milestone_id;
    @Column(name = "factory_id")
    private int factory_id;	
    @Column(name="grand_total")
    private String grand_total;
    @Column(name="load_id")
    private String load_id;
    @Column(name="cancel")
    private int cancel;
    
    
    public String getGrand_total() {
		return grand_total;
	}
	public void setGrand_total(String grand_total) {
		this.grand_total = grand_total;
	}
	public int getFactory_id() {
		return factory_id;
	}
	public void setFactory_id(int factory_id) {
		this.factory_id = factory_id;
	}
	@Column(name = "filepath")
    private String filepath;
	@Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date")
    private LocalDateTime created_date;
    @Column(name = "modified_by")
    private String modified_by;
    @Column(name = "modified_date")
    private LocalDateTime modified_date;
	public int getPnId() {
		return pnId;
	}
	public String getLoad_id() {
	    return load_id;
	}
	public void setLoad_id(String load_id) {
	    this.load_id = load_id;
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
	public int getMilestone_id() {
		return milestone_id;
	}
	public void setMilestone_id(int milestone_id) {
		this.milestone_id = milestone_id;
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
	public LocalDateTime getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	public LocalDateTime getModified_date() {
		return modified_date;
	}
	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}
	public int getcancel() {
		return cancel;
	}
	public void setcancel(int cancel) {
		this.cancel = cancel;
	}
    
}
