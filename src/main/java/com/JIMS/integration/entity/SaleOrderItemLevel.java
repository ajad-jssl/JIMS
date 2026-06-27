package com.JIMS.integration.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SALE_ORDER_ITEM_LEVEL_ENTRY")
public class SaleOrderItemLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-increment for primary key
	private int id;

	@Column(name = "soe_id")
	private int soe_id;

	@Column(name = "auction_no")
	private String auction_no;

	@Column(name = "scrap_type_id")
	private String scrap_type_id;

	@Column(name = "scrapitem_id")
	private String scrapitem_id;

	@Column(name = "uom_id")
	private String uom_id;

	@Column(name = "quantity")
	private String quantity;

	@Column(name = "kg")
	private String kg;

	@Column(name = "unit_price")
	private String unit_price;

	@Column(name = "total")
	private String total;

	@Column(name = "tolerance")
	private String tolerance;

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSoe_id() {
		return soe_id;
	}

	public void setSoe_id(int soe_id) {
		this.soe_id = soe_id;
	}

	public String getAuction_no() {
		return auction_no;
	}

	public void setAuction_no(String auction_no) {
		this.auction_no = auction_no;
	}

	public String getScrap_type_id() {
		return scrap_type_id;
	}

	public void setScrap_type_id(String scrap_type_id) {
		this.scrap_type_id = scrap_type_id;
	}

	public String getScrapitem_id() {
		return scrapitem_id;
	}

	public void setScrapitem_id(String scrapitem_id) {
		this.scrapitem_id = scrapitem_id;
	}

	public String getUom_id() {
		return uom_id;
	}

	public void setUom_id(String uom_id) {
		this.uom_id = uom_id;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getKg() {
		return kg;
	}

	public void setKg(String kg) {
		this.kg = kg;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTolerance() {
		return tolerance;
	}

	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}
}
