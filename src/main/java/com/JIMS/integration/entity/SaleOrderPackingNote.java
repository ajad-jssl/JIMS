package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCRAP_PACKING_NOTE")
public class SaleOrderPackingNote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-increment for primary key
	private int scp_pn_id;

	@Column(name = "sale_order_code")
	private String sale_order_code;

	@Column(name = "scp_load")
	private String scp_load;

	@Column(name = "total_steel_qty")
	private String total_steel_qty;

	@Column(name = "total_non_steel_qty")
	private String total_non_steel_qty;

	@Column(name = "sale_order_validity")

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime sale_order_validity;

	@Column(name = "vendor_id")
	private String vendor_id;

	@Column(name = "weighbridge_no")
	private String weighbridge_no;

	@Column(name = "transportername")
	private String transportername;

	@Column(name = "vehicleno")
	private String vehicleno;

	@Column(name = "scp_pn_date")
	private String scp_pn_date;

	@Column(name = "buyer_ref_no")
	private String buyer_ref_no;

	@Column(name = "created_by")
	private String created_by;

	@Column(name = "created_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime created_date; // Changed to LocalDateTime

	@Column(name = "modified_by")
	private String modified_by;

	@Column(name = "modified_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modified_date; // Changed to LocalDateTime

	@Column(name = "is_delete")
	private boolean is_delete;

	@Column(name = "project_reference")
	private String project_reference;

	@Column(name = "other_reference")
	private String other_reference;

	@Column(name = "central_excise_tarrif_no")
	private String central_excise_tarrif_no;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "credit_reference")
	private String credit_reference;

	@Column(name = "is_verified")
	private String is_verified;

	@Column(name = "verified_by")
	private String verified_by;

	@Column(name = "verified_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime verified_date;

	@Column(name = "factory_id")
	private String factory_id;

	public String getFactory_id() {
		return factory_id;
	}

	public void setFactory_id(String factory_id) {
		this.factory_id = factory_id;
	}

	public String getIs_verified() {
		return is_verified;
	}

	public void setIs_verified(String is_verified) {
		this.is_verified = is_verified;
	}

	public String getVerified_by() {
		return verified_by;
	}

	public void setVerified_by(String verified_by) {
		this.verified_by = verified_by;
	}

	public LocalDateTime getVerified_date() {
		return verified_date;
	}

	public void setVerified_date(LocalDateTime verified_date) {
		this.verified_date = verified_date;
	}

	public String getProject_reference() {
		return project_reference;
	}

	public void setProject_reference(String project_reference) {
		this.project_reference = project_reference;
	}

	public String getOther_reference() {
		return other_reference;
	}

	public void setOther_reference(String other_reference) {
		this.other_reference = other_reference;
	}

	public String getCentral_excise_tarrif_no() {
		return central_excise_tarrif_no;
	}

	public void setCentral_excise_tarrif_no(String central_excise_tarrif_no) {
		this.central_excise_tarrif_no = central_excise_tarrif_no;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCredit_reference() {
		return credit_reference;
	}

	public void setCredit_reference(String credit_reference) {
		this.credit_reference = credit_reference;
	}

	public int getScp_pn_id() {
		return scp_pn_id;
	}

	public void setScp_pn_id(int scp_pn_id) {
		this.scp_pn_id = scp_pn_id;
	}

	public String getSale_order_code() {
		return sale_order_code;
	}

	public void setSale_order_code(String sale_order_code) {
		this.sale_order_code = sale_order_code;
	}

	public String getTotal_steel_qty() {
		return total_steel_qty;
	}

	public void setTotal_steel_qty(String total_steel_qty) {
		this.total_steel_qty = total_steel_qty;
	}

	public String getTotal_non_steel_qty() {
		return total_non_steel_qty;
	}

	public void setTotal_non_steel_qty(String total_non_steel_qty) {
		this.total_non_steel_qty = total_non_steel_qty;
	}

	public LocalDateTime getSale_order_validity() {
		return sale_order_validity;
	}

	public void setSale_order_validity(LocalDateTime sale_order_validity) {
		this.sale_order_validity = sale_order_validity;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getWeighbridge_no() {
		return weighbridge_no;
	}

	public void setWeighbridge_no(String weighbridge_no) {
		this.weighbridge_no = weighbridge_no;
	}

	public String getTransportername() {
		return transportername;
	}

	public void setTransportername(String transportername) {
		this.transportername = transportername;
	}

	public String getVehicleno() {
		return vehicleno;
	}

	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}

	public String getScp_pn_date() {
		return scp_pn_date;
	}

	public void setScp_pn_date(String scp_pn_date) {
		this.scp_pn_date = scp_pn_date;
	}

	public String getBuyer_ref_no() {
		return buyer_ref_no;
	}

	public void setBuyer_ref_no(String buyer_ref_no) {
		this.buyer_ref_no = buyer_ref_no;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
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

	public boolean isIs_delete() {
		return is_delete;
	}

	public void setIs_delete(boolean is_delete) {
		this.is_delete = is_delete;
	}

	public String getScp_load() {
		return scp_load;
	}

	public void setScp_load(String scp_load) {
		this.scp_load = scp_load;
	}

}
