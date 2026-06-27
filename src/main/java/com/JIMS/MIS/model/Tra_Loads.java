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
@Table(name = "Tra_loads",schema = "dbo")
public class Tra_Loads {
	@Id
	@Column(name = "tload_id")
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer tload_id;
	@Column(name = "loadno")
	private String loadno;
	@Column(name = "contract_id")
	private Integer contract_id;
	@Column(name = "trailerref")
	private String trailerref;
	@Column(name = "trackingref")
	private String trackingref;
	@Column(name = "created")
	private LocalDateTime 	created;
	@Column (name = "wb_weight")
	private BigDecimal wb_weight;
	@Column(name = "exciseinvoice")
	private String exciseinvoice;
	@Column (name = "descr")
	private  String descr;
	@Column (name = "factory_id")
	private Integer factory_id;
	@Column (name = "supid")
	private Integer supid;
	@Column(name = "createddate")
	private LocalDateTime createddate;
	@Column(name="createdby")
	private String createdby;
	
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public LocalDateTime getCreateddate() {
		return createddate;
	}
	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}
	public Integer getTload_id() {
		return tload_id;
	}
	public void setTload_id(Integer tload_id) {
		this.tload_id = tload_id;
	}
	public String getLoadno() {
		return loadno;
	}
	public void setLoadno(String loadno) {
		this.loadno = loadno;
	}
	public Integer getContract_id() {
		return contract_id;
	}
	public void setContract_id(Integer contract_id) {
		this.contract_id = contract_id;
	}
	public String getTrailerref() {
		return trailerref;
	}
	public void setTrailerref(String trailerref) {
		this.trailerref = trailerref;
	}
	public String getTrackingref() {
		return trackingref;
	}
	public void setTrackingref(String trackingref) {
		this.trackingref = trackingref;
	}
	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	public BigDecimal getWb_weight() {
		return wb_weight;
	}
	public void setWb_weight(BigDecimal wb_weight) {
		this.wb_weight = wb_weight;
	}
	public String getExciseinvoice() {
		return exciseinvoice;
	}
	public void setExciseinvoice(String exciseinvoice) {
		this.exciseinvoice = exciseinvoice;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Integer getFactory_id() {
		return factory_id;
	}
	public void setFactory_id(Integer factory_id) {
		this.factory_id = factory_id;
	}
	public Integer getSupid() {
		return supid;
	}
	public void setSupid(Integer supid) {
		this.supid = supid;
	}
	
	
	

}

	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


