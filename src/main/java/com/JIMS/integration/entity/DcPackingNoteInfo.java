package com.JIMS.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PACKING_NOTE_DC")
public class DcPackingNoteInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dc_pn_id")
	private int dc_pn_id;

	@Column(name = "con_id")
	private String con_id;

	@Column(name = "filepath")
	private String filepath;

	@Column(name = "milestone_id")
	private String milestone_id;

	@Column(name = "note_type")
	private String note_type;

	@Column(name = "dc_load")
	private String dc_load;

	@Column(name = "freight")
	private String freight;

	@Column(name = "is_delete")
	private boolean is_delete;

	@Column(name = "created_by")
	private String created_by;

	@Column(name = "modified_by")
	private String modified_by;

	@Column(name = "created_date")
	private LocalDateTime created_date;

	@Column(name = "modified_date")
	private LocalDateTime modified_date;

	@Column(name = "factory_id")
	private String factory_id;

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int getDc_pn_id() {
		return dc_pn_id;
	}

	public void setDc_pn_id(int dc_pn_id) {
		this.dc_pn_id = dc_pn_id;
	}

	public String getCon_id() {
		return con_id;
	}

	public void setCon_id(String con_id) {
		this.con_id = con_id;
	}

	public String getMilestone_id() {
		return milestone_id;
	}

	public void setMilestone_id(String milestone_id) {
		this.milestone_id = milestone_id;
	}

	public String getNote_type() {
		return note_type;
	}

	public void setNote_type(String note_type) {
		this.note_type = note_type;
	}

	public String getDc_load() {
		return dc_load;
	}

	public void setDc_load(String dc_load) {
		this.dc_load = dc_load;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public boolean isIs_delete() {
		return is_delete;
	}

	public void setIs_delete(boolean is_delete) {
		this.is_delete = is_delete;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getModified_by() {
		return modified_by;
	}

	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public LocalDateTime getModified_date() {
		return modified_date;
	}

	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}

	public String getFactory_id() {
		return factory_id;
	}

	public void setFactory_id(String factory_id) {
		this.factory_id = factory_id;
	}

}
