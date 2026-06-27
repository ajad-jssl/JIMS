package com.JIMS.MIS.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tra_LoadRuns",schema = "dbo")
public class Tra_LoadRuns {
@Id
@Column(name = "run_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int run_id;
@Column(name="created")
private LocalDateTime created;
@Column(name = "tload_id")
private int tload_id;
@Column(name = "haulier_id")
private int haulier_id;
@Column(name="destination_id")
private int destination_id;
@Column(name = "reqddate")
private LocalDateTime reqddate;
@Column(name = "reqdtime")
private String reqdtime;
@Column(name = "notes")
private String notes;
@Column(name="trate_id")
private int trate_id;
@Column(name = "loadtype_id")
private int loadtype_id;
@Column(name="tablerate")
private BigDecimal tablerate;
@Column(name = "adjustment")
private BigDecimal adjustment;
@Column(name = "fuelpc")
private BigDecimal fuelpc;
@Column(name = "fuelcost")
private BigDecimal fuelcost;
@Column(name = "totalcost")
private BigDecimal totalcost;
@Column(name = "invoiceno")
private String invoiceno;
@Column(name = "costnotes")
private String costnotes;
@Column(name = "despatchdate")
private LocalDateTime despatchdate;
@Column(name="arrivaldate")
private LocalDateTime arrivaldate;
@Column(name="despatchtime")
private String despatchtime;
@Column(name="arrivaltime")
private String arrivaltime;
@Column(name="delaycost")
private BigDecimal delaycost;
@Column(name="length")
private String length;
@Column(name = "width")
private String width;
@Column(name="runstatus_id")
private int runstatus_id;
@Column(name="leftdestdate")
private LocalDateTime leftdestdate;
@Column(name = "leftdesttime")
private String leftdesttime;
@Column(name="createdby")
private int createdby;
@Column(name="createddate")
private LocalDateTime createddate;
@Column(name ="updatedby")
private String updatedby;
@Column(name = "updateddate")
private LocalDateTime updateddate;


public int getRun_id() {
	return run_id;
}
public void setRun_id(int run_id) {
	this.run_id = run_id;
}
public LocalDateTime getCreated() {
	return created;
}
public void setCreated(LocalDateTime created) {
	this.created = created;
}
public int getTload_id() {
	return tload_id;
}
public void setTload_id(int tload_id) {
	this.tload_id = tload_id;
}
public int getHaulier_id() {
	return haulier_id;
}
public void setHaulier_id(int haulier_id) {
	this.haulier_id = haulier_id;
}
public int getDestination_id() {
	return destination_id;
}
public void setDestination_id(int destination_id) {
	this.destination_id = destination_id;
}
public LocalDateTime getReqddate() {
	return reqddate;
}
public void setReqddate(LocalDateTime reqddate) {
	this.reqddate = reqddate;
}
public String getReqdtime() {
	return reqdtime;
}
public void setReqdtime(String reqdtime) {
	this.reqdtime = reqdtime;
}
public String getNotes() {
	return notes;
}
public void setNotes(String notes) {
	this.notes = notes;
}
public int getTrate_id() {
	return trate_id;
}
public void setTrate_id(int trate_id) {
	this.trate_id = trate_id;
}

public int getLoadtype_id() {
	return loadtype_id;
}
public void setLoadtype_id(int loadtype_id) {
	this.loadtype_id = loadtype_id;
}
public BigDecimal getTablerate() {
	return tablerate;
}
public void setTablerate(BigDecimal tablerate) {
	this.tablerate = tablerate;
}
public BigDecimal getAdjustment() {
	return adjustment;
}
public void setAdjustment(BigDecimal adjustment) {
	this.adjustment = adjustment;
}
public BigDecimal getFuelpc() {
	return fuelpc;
}
public void setFuelpc(BigDecimal fuelpc) {
	this.fuelpc = fuelpc;
}
public BigDecimal getFuelcost() {
	return fuelcost;
}
public void setFuelcost(BigDecimal fuelcost) {
	this.fuelcost = fuelcost;
}
public BigDecimal getTotalcost() {
	return totalcost;
}
public void setTotalcost(BigDecimal totalcost) {
	this.totalcost = totalcost;
}
public String getInvoiceno() {
	return invoiceno;
}
public void setInvoiceno(String invoiceno) {
	this.invoiceno = invoiceno;
}
public String getCostnotes() {
	return costnotes;
}
public void setCostnotes(String costnotes) {
	this.costnotes = costnotes;
}
public LocalDateTime getDespatchdate() {
	return despatchdate;
}
public void setDespatchdate(LocalDateTime despatchdate) {
	this.despatchdate = despatchdate;
}
public LocalDateTime getArrivaldate() {
	return arrivaldate;
}
public void setArrivaldate(LocalDateTime arrivaldate) {
	this.arrivaldate = arrivaldate;
}
public String getDespatchtime() {
	return despatchtime;
}
public void setDespatchtime(String despatchtime) {
	this.despatchtime = despatchtime;
}
public String getArrivaltime() {
	return arrivaltime;
}
public void setArrivaltime(String arrivaltime) {
	this.arrivaltime = arrivaltime;
}
public BigDecimal getDelaycost() {
	return delaycost;
}
public void setDelaycost(BigDecimal delaycost) {
	this.delaycost = delaycost;
}
public String getLength() {
	return length;
}
public void setLength(String length) {
	this.length = length;
}
public String getWidth() {
	return width;
}
public void setWidth(String width) {
	this.width = width;
}
public int getRunstatus_id() {
	return runstatus_id;
}
public void setRunstatus_id(int runstatus_id) {
	this.runstatus_id = runstatus_id;
}
public LocalDateTime getLeftdestdate() {
	return leftdestdate;
}
public void setLeftdestdate(LocalDateTime leftdestdate) {
	this.leftdestdate = leftdestdate;
}
public String getLeftdesttime() {
	return leftdesttime;
}
public void setLeftdesttime(String leftdesttime) {
	this.leftdesttime = leftdesttime;
}
public int getCreatedby() {
	return createdby;
}
public void setCreatedby(int createdby) {
	this.createdby = createdby;
}
public LocalDateTime getCreateddate() {
	return createddate;
}
public void setCreateddate(LocalDateTime createddate) {
	this.createddate = createddate;
}
public String getUpdatedby() {
	return updatedby;
}
public void setUpdatedby(String updatedby) {
	this.updatedby = updatedby;
}
public LocalDateTime getUpdateddate() {
	return updateddate;
}
public void setUpdateddate(LocalDateTime updateddate) {
	this.updateddate = updateddate;
}	


}
