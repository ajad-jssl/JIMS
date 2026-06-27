package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DC_QSPACKING_NOTE")
public class QSDCPackingNote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "con_id")
	private int contract_id;

	@Column(name = "milestone_id")
	private int milestone_id;

	@Column(name = "dc_type")
	private String dc_type;

	@Column(name = "invoice_type")
	private String invoice_type;

	@Column(name = "factory_id")
	private int factory_id;

	@Column(name = "filepath")
	private String filepath;

	@Column(name = "freight")
	private String freight;

	@Column(name = "created_date")
	private LocalDateTime created_date;

	@Column(name = "created_by")
	private String created_by;
	
	@Column(name = "tax_exempted")
	private String tax_exempted;
	
	@Column(name = "modified_date")
	private LocalDateTime modified_date;

	@Column(name = "modified_by")
	private String modified_by;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getContract_id() {
		return contract_id;
	}

	public void setContract_id(int contract_id) {
		this.contract_id = contract_id;
	}

	public int getMilestone_id() {
		return milestone_id;
	}

	public void setMilestone_id(int milestone_id) {
		this.milestone_id = milestone_id;
	}

	public String getDc_type() {
		return dc_type;
	}

	public void setDc_type(String dc_type) {
		this.dc_type = dc_type;
	}

	public String getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	public int getFactory_id() {
		return factory_id;
	}

	public void setFactory_id(int factory_id) {
		this.factory_id = factory_id;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getTax_exempted() {
		return tax_exempted;
	}

	public void setTax_exempted(String tax_exempted) {
		this.tax_exempted = tax_exempted;
	}

	public LocalDateTime getModified_date() {
		return modified_date;
	}

	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}

	public String getModified_by() {
		return modified_by;
	}

	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	
	
	

}
