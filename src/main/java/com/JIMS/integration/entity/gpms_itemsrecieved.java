package com.JIMS.integration.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="GPMS_tabretgatepassshipdetail",schema="dbo")

public class gpms_itemsrecieved {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int gpsdid;
	private String gp_no;
	private String gps_entry;
	private double Return_qty;
	private int gp_type;
	private int itemno;
	private double receive_qty	;
	private String Dc_ref;
	private int Security_received;
	private String security_remarks;
	private int Return_complete;
	private int Created_by;
	private LocalDateTime Created_date;	
	private int Modified_by;
	private LocalDateTime Modified_date;
	private Integer Factory_id;
	public int getGpsdid() {
		return gpsdid;
	}
	public void setGpsdid(int gpsdid) {
		this.gpsdid = gpsdid;
	}
	public String getGp_no() {
		return gp_no;
	}
	public void setGp_no(String gp_no) {
		this.gp_no = gp_no;
	}
	public String getGps_entry() {
		return gps_entry;
	}
	public void setGps_entry(String gps_entry) {
		this.gps_entry = gps_entry;
	}
	public double getReturn_qty() {
		return Return_qty;
	}
	public void setReturn_qty(double return_qty) {
		Return_qty = return_qty;
	}
	public double getReceive_qty() {
		return receive_qty;
	}
	public void setReceive_qty(double receive_qty) {
		this.receive_qty = receive_qty;
	}
	public String getDc_ref() {
		return Dc_ref;
	}
	public void setDc_ref(String dc_ref) {
		Dc_ref = dc_ref;
	}
	public int getSecurity_received() {
		return Security_received;
	}
	public void setSecurity_received(int security_received) {
		Security_received = security_received;
	}
	
	public String getSecurity_remarks() {
		return security_remarks;
	}
	public void setSecurity_remarks(String security_remarks) {
		this.security_remarks = security_remarks;
	}
	public int getReturn_complete() {
		return Return_complete;
	}
	public void setReturn_complete(int return_complete) {
		Return_complete = return_complete;
	}
	public int getCreated_by() {
		return Created_by;
	}
	public void setCreated_by(int created_by) {
		Created_by = created_by;
	}
	public LocalDateTime getCreated_date() {
		return Created_date;
	}
	public void setCreated_date(LocalDateTime created_date) {
		Created_date = created_date;
	}
	public int getModified_by() {
		return Modified_by;
	}
	public void setModified_by(int modified_by) {
		Modified_by = modified_by;
	}
	public LocalDateTime getModified_date() {
		return Modified_date;
	}
	public void setModified_date(LocalDateTime modified_date) {
		Modified_date = modified_date;
	}
	public int getGp_type() {
		return gp_type;
	}
	public void setGp_type(int gp_type) {
		this.gp_type = gp_type;
	}
	public int getItemno() {
		return itemno;
	}
	public void setItemno(int itemno) {
		this.itemno = itemno;
	}
	public Integer getFactory_id() {
        return Factory_id;
}
    public void setFactory_id(Integer factory_id) {
        this.Factory_id = factory_id;
}
}
