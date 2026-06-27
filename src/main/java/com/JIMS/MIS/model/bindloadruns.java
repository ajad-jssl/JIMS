package com.JIMS.MIS.model;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class bindloadruns {
    @Id
    private int run_Id;
    private int contract_Id;
    private int tload_Id;
    private String fcontract;
    private String loadno;
    private String haulier;
    private String destination;
    private Date reqdDate;
    private String reqdTime;
    private String notes;
    private String country;
    private String county;
    private String loadType;
    private BigDecimal tableRate;
    private BigDecimal adjustment;
    private BigDecimal fuelPC;
    private BigDecimal fuelCost;
    private BigDecimal totalCost;
    private String invoiceNo;
    private String costNotes;
    private Date despatchDate;
    private Date arrivalDate;
    private String status;
    private String despatchTime;
    private String arrivalTime;
    private BigDecimal delayCost;
    private String length;
    private String width;
    private Date leftDestDate;
    private String leftDestTime;
    private int haulier_Id;
    private int destination_Id;
    private int trate_Id;
	public int getRun_Id() {
		return run_Id;
	}
	public void setRun_Id(int run_Id) {
		this.run_Id = run_Id;
	}
	public int getContract_Id() {
		return contract_Id;
	}
	public void setContract_Id(int contract_Id) {
		this.contract_Id = contract_Id;
	}
	public int getTload_Id() {
		return tload_Id;
	}
	public void setTload_Id(int tload_Id) {
		this.tload_Id = tload_Id;
	}
	public String getFcontract() {
		return fcontract;
	}
	public void setFcontract(String fcontract) {
		this.fcontract = fcontract;
	}
	public String getLoadno() {
		return loadno;
	}
	public void setLoadno(String loadno) {
		this.loadno = loadno;
	}
	public String getHaulier() {
		return haulier;
	}
	public void setHaulier(String haulier) {
		this.haulier = haulier;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Date getReqdDate() {
		return reqdDate;
	}
	public void setReqdDate(Date reqdDate) {
		this.reqdDate = reqdDate;
	}
	public String getReqdTime() {
		return reqdTime;
	}
	public void setReqdTime(String reqdTime) {
		this.reqdTime = reqdTime;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getLoadType() {
		return loadType;
	}
	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
	public BigDecimal getTableRate() {
		return tableRate;
	}
	public void setTableRate(BigDecimal tableRate) {
		this.tableRate = tableRate;
	}
	public BigDecimal getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(BigDecimal adjustment) {
		this.adjustment = adjustment;
	}
	public BigDecimal getFuelPC() {
		return fuelPC;
	}
	public void setFuelPC(BigDecimal fuelPC) {
		this.fuelPC = fuelPC;
	}
	public BigDecimal getFuelCost() {
		return fuelCost;
	}
	public void setFuelCost(BigDecimal fuelCost) {
		this.fuelCost = fuelCost;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getCostNotes() {
		return costNotes;
	}
	public void setCostNotes(String costNotes) {
		this.costNotes = costNotes;
	}
	public Date getDespatchDate() {
		return despatchDate;
	}
	public void setDespatchDate(Date despatchDate) {
		this.despatchDate = despatchDate;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDespatchTime() {
		return despatchTime;
	}
	public void setDespatchTime(String despatchTime) {
		this.despatchTime = despatchTime;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public BigDecimal getDelayCost() {
		return delayCost;
	}
	public void setDelayCost(BigDecimal delayCost) {
		this.delayCost = delayCost;
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
	public Date getLeftDestDate() {
		return leftDestDate;
	}
	public void setLeftDestDate(Date leftDestDate) {
		this.leftDestDate = leftDestDate;
	}
	public String getLeftDestTime() {
		return leftDestTime;
	}
	public void setLeftDestTime(String leftDestTime) {
		this.leftDestTime = leftDestTime;
	}
	public int getDestination_Id() {
		return destination_Id;
	}
	public void setDestination_Id(int destination_Id) {
		this.destination_Id = destination_Id;
	}
	public int getTrate_Id() {
		return trate_Id;
	}
	public void setTrate_Id(int trate_Id) {
		this.trate_Id = trate_Id;
	}
	public int getHaulier_Id() {
		return haulier_Id;
	}
	public void setHaulier_Id(int haulier_Id) {
		this.haulier_Id = haulier_Id;
	}
    


}

