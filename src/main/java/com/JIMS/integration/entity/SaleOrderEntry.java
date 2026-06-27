package com.JIMS.integration.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SALE_ORDER_ENTRY")
public class SaleOrderEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-increment for primary key
	private int soe_id;

	@Column(name = "sale_order_code")
	private String sale_order_code;

	@Column(name = "sale_order_type_id")
	private int sale_order_type_id;

	@Column(name = "location_type_id")
	private int location_type_id;

	@Column(name = "sale_order_to_id")
	private int sale_order_to_id;

	@Column(name = "auction_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate auction_date;

	@Column(name = "sale_order_duration")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDate sale_order_duration; // Changed to LocalDateTime

	@Column(name = "advance_payment")
	private int advance_payment;

	@Column(name = "billing_address_id")
	private int billing_address_id;

	@Column(name = "shipping_address_id")
	private int shipping_address_id;

	@Column(name = "business_unit_id")
	private int business_unit_id;

	@Column(name = "tax1")
	private float tax1;

	@Column(name = "tax1_value")
	private float tax1_value;

	@Column(name = "tax2")
	private float tax2;

	@Column(name = "tax2_value")
	private float tax2_value;

	@Column(name = "tax3")
	private float tax3;

	@Column(name = "tax3_value")
	private float tax3_value;

	@Column(name = "net_amount")
	private float net_amount;

	@Column(name = "total_tax")
	private float total_tax;

	@Column(name = "grand_total")
	private float grand_total;

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

	@Column(name = "factory_id")
	private String factory_id;

	// Getters and Setters

	public int getSoe_id() {
		return soe_id;
	}

	public String getFactory_id() {
		return factory_id;
	}

	public void setFactory_id(String factory_id) {
		this.factory_id = factory_id;
	}

	public void setSoe_id(int soe_id) {
		this.soe_id = soe_id;
	}

	public int getSale_order_type_id() {
		return sale_order_type_id;
	}

	public void setSale_order_type_id(int sale_order_type_id) {
		this.sale_order_type_id = sale_order_type_id;
	}

	public int getLocation_type_id() {
		return location_type_id;
	}

	public void setLocation_type_id(int location_type_id) {
		this.location_type_id = location_type_id;
	}

	public int getSale_order_to_id() {
		return sale_order_to_id;
	}

	public void setSale_order_to_id(int sale_order_to_id) {
		this.sale_order_to_id = sale_order_to_id;
	}

	public LocalDate getAuction_date() {
		return auction_date;
	}

	public void setAuction_date(LocalDate auction_date) {
		this.auction_date = auction_date;
	}

	public LocalDate getSale_order_duration() {
		return sale_order_duration;
	}

	public void setSale_order_duration(LocalDate sale_order_duration) {
		this.sale_order_duration = sale_order_duration;
	}

	public int getAdvance_payment() {
		return advance_payment;
	}

	public void setAdvance_payment(int advance_payment) {
		this.advance_payment = advance_payment;
	}

	public int getBilling_address_id() {
		return billing_address_id;
	}

	public void setBilling_address_id(int billing_address_id) {
		this.billing_address_id = billing_address_id;
	}

	public int getShipping_address_id() {
		return shipping_address_id;
	}

	public void setShipping_address_id(int shipping_address_id) {
		this.shipping_address_id = shipping_address_id;
	}

	public int getBusiness_unit_id() {
		return business_unit_id;
	}

	public void setBusiness_unit_id(int business_unit_id) {
		this.business_unit_id = business_unit_id;
	}

	public float getTax1() {
		return tax1;
	}

	public void setTax1(float tax1) {
		this.tax1 = tax1;
	}

	public float getTax1_value() {
		return tax1_value;
	}

	public void setTax1_value(float tax1_value) {
		this.tax1_value = tax1_value;
	}

	public float getTax2() {
		return tax2;
	}

	public void setTax2(float tax2) {
		this.tax2 = tax2;
	}

	public float getTax2_value() {
		return tax2_value;
	}

	public void setTax2_value(float tax2_value) {
		this.tax2_value = tax2_value;
	}

	public float getTax3() {
		return tax3;
	}

	public void setTax3(float tax3) {
		this.tax3 = tax3;
	}

	public float getTax3_value() {
		return tax3_value;
	}

	public void setTax3_value(float tax3_value) {
		this.tax3_value = tax3_value;
	}

	public float getNet_amount() {
		return net_amount;
	}

	public void setNet_amount(float net_amount) {
		this.net_amount = net_amount;
	}

	public float getTotal_tax() {
		return total_tax;
	}

	public void setTotal_tax(float total_tax) {
		this.total_tax = total_tax;
	}

	public float getGrand_total() {
		return grand_total;
	}

	public void setGrand_total(float grand_total) {
		this.grand_total = grand_total;
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

	public String getSale_order_code() {
		return sale_order_code;
	}

	public void setSale_order_code(String sale_order_code) {
		this.sale_order_code = sale_order_code;
	}

}
