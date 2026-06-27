package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "spTRA_GetPiecesInLoadSubCon") 
public class lotdetail {
	@Id
    private int pieces_id;
    private int qty;
    private String mark;
    private String descr;
    private Long longest_length;
    private Long tweight;
    private Long tarea;
    private int load_id;
    private String ssize;
    private String dims;
    private int tload_id;
    private Long pzone;
    private Long pload;
    private int fab_id;
    private int supplier_id;
    private String fab;
    private int locfg_id;
    private String finishedgoodslocation;
	public int getPieces_id() {
		return pieces_id;
	}
	public void setPieces_id(int pieces_id) {
		this.pieces_id = pieces_id;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Long getLongest_length() {
		return longest_length;
	}
	public void setLongest_length(Long longest_length) {
		this.longest_length = longest_length;
	}
	public Long getTweight() {
		return tweight;
	}
	public void setTweight(Long tweight) {
		this.tweight = tweight;
	}
	public Long getTarea() {
		return tarea;
	}
	public void setTarea(Long tarea) {
		this.tarea = tarea;
	}
	public int getLoad_id() {
		return load_id;
	}
	public void setLoad_id(int load_id) {
		this.load_id = load_id;
	}
	public String getSsize() {
		return ssize;
	}
	public void setSsize(String ssize) {
		this.ssize = ssize;
	}
	public String getDims() {
		return dims;
	}
	public void setDims(String dims) {
		this.dims = dims;
	}
	public int getTload_id() {
		return tload_id;
	}
	public void setTload_id(int tload_id) {
		this.tload_id = tload_id;
	}
	public Long getPzone() {
		return pzone;
	}
	public void setPzone(Long pzone) {
		this.pzone = pzone;
	}
	public Long getPload() {
		return pload;
	}
	public void setPload(Long pload) {
		this.pload = pload;
	}
	public int getFab_id() {
		return fab_id;
	}
	public void setFab_id(int fab_id) {
		this.fab_id = fab_id;
	}
	public int getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getFab() {
		return fab;
	}
	public void setFab(String fab) {
		this.fab = fab;
	}
	public int getLocfg_id() {
		return locfg_id;
	}
	public void setLocfg_id(int locfg_id) {
		this.locfg_id = locfg_id;
	}
	public String getFinishedgoodslocation() {
		return finishedgoodslocation;
	}
	public void setFinishedgoodslocation(String finishedgoodslocation) {
		this.finishedgoodslocation = finishedgoodslocation;
	}
    
}
