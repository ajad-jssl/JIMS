package com.JIMS.integration.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "VTS_invexitponumber")  
public class VTS_incponumbermodel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	   private int Pono_id;
	   private String vechile_id;
	   private Integer Company_id;
	   private String Po_number;
	   private LocalDate Po_date;
	   private String Inv_no;
	   private LocalDate Inv_date;
	   private String DC_no;
	   private LocalDate DC_date;
	   private double qty;
	   private double weight;
	   private String Descp;
	   private Integer 	Reject;
	   private Integer Created_by;
	   private LocalDateTime Created_date;
	   private Integer Modified_by;
	   private LocalDateTime Modified_date;
	   private String Factory_id;
	public int getPono_id() {
		return Pono_id;
	}
	public void setPono_id(int pono_id) {
		Pono_id = pono_id;
	}
	public String getVechile_id() {
		return vechile_id;
	}
	public void setVechile_id(String vechile_id) {
		this.vechile_id = vechile_id;
	}
	public Integer getCompany_id() {
		return Company_id;
	}
	public void setCompany_id(Integer company_id) {
		Company_id = company_id;
	}
	public String getPo_number() {
		return Po_number;
	}
	public void setPo_number(String po_number) {
		Po_number = po_number;
	}
	public LocalDate getPo_date() {
		return Po_date;
	}
	public void setPo_date(LocalDate po_date) {
		Po_date = po_date;
	}
	public String getInv_no() {
		return Inv_no;
	}
	public void setInv_no(String inv_no) {
		Inv_no = inv_no;
	}
	public LocalDate getInv_date() {
		return Inv_date;
	}
	public void setInv_date(LocalDate inv_date) {
		Inv_date = inv_date;
	}
	public String getDC_no() {
		return DC_no;
	}
	public void setDC_no(String dC_no) {
		DC_no = dC_no;
	}
	public LocalDate getDC_date() {
		return DC_date;
	}
	public void setDC_date(LocalDate dC_date) {
		DC_date = dC_date;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getDescp() {
		return Descp;
	}
	public void setDescp(String descp) {
		Descp = descp;
	}
	
	public Integer getReject() {
		return Reject;
	}
	public void setReject(Integer reject) {
		Reject = reject;
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
	public String getFactory_id() {
		return Factory_id;
	}
	public void setFactory_id(String factory_id) {
		Factory_id = factory_id;
	}
	   
	   
	   
}
