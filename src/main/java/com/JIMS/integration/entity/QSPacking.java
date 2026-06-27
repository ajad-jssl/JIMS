package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "QSPACKING_MASTER")
public class QSPacking {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pn_id")
    private int pnId; // Assuming the primary key column name is "pn_id"
    @Column(name = "con_id")
    private String conId; // Assuming the column name is "con_id"
    @Column(name = "load_id")
    private String loadId; // Assuming the column name is "load_id"
    @Column(name = "lot_no")
    private String lotNo;
	@Column(name = "created_by")
    private String createdBy; // Assuming the column name is "created_by"
    @Column(name = "filepath")
    private String filepath;
    @Column(name = "transport_name")
    private String transport_name;
    @Column(name = "vechile_no")
    private String vechile_no;
    @Column(name = "freight")
    private String freight;
    @Column(name = "milestone_id")
    private int milestone_id;
    @Column(name = "created_date")
    private LocalDateTime created_date;
    @Column(name = "factory_id")
    private int factory_id;
    @Column(name = "grand_total")
    private String grand_total;
    @Column(name = "Cancel")
    private int cancel;
    public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
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
	public String getLoadId() {
		return loadId;
	}
	public void setLoadId(String loadId) {
		this.loadId = loadId;
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
	public String getTransport_name() {
		return transport_name;
	}
	public void setTransport_name(String transport_name) {
		this.transport_name = transport_name;
	}
	public String getVechile_no() {
		return vechile_no;
	}
	public void setVechile_no(String vechile_no) {
		this.vechile_no = vechile_no;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
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
	public int getCancel() {
		return cancel;
	}
	public void setCancel(int cancel) {
		this.cancel = cancel;
	}

    
    

}
