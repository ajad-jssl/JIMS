package com.JIMS.integration.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="GPMS_tabRetGatePassdetail",schema="dbo")
public class GPMSreturnableitems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int gpdid;
	private String gp_no;
	private String item;
	private String itmPrice;
	private String itmRemarks;
	private double qty;
	private Integer Created_by;
	private LocalDateTime Created_date;
	private Integer Modified_by;
	private LocalDateTime Modified_date;
	private Integer Factory_id;
	
	public int getGpdid() {
		return gpdid;
	}
	public void setGpdid(int gpdid) {
		this.gpdid = gpdid;
	}

	public String getGp_no() {
		return gp_no;
	}
	public void setGp_no(String gp_no) {
		this.gp_no = gp_no;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getItmPrice() {
		return itmPrice;
	}
	public void setItmPrice(String itmPrice) {
		this.itmPrice = itmPrice;
	}
	public String getItmRemarks() {
		return itmRemarks;
	}
	public void setItmRemarks(String itmRemarks) {
		this.itmRemarks = itmRemarks;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
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
	 public Integer getFactory_id() {
	        return Factory_id;
	}
	    public void setFactory_id(Integer factory_id) {
	        this.Factory_id = factory_id;
	}
	
	
}
