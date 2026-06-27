package com.JIMS.integration.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consolidated_g2reports {


    private String contract;
    private String descr;
    private Integer contract_id;
    private String jobCode;
    private Integer loadtype;
    private String pzone;

    // Load Weight
    private Double weight;

    // JSSL Weight
    private Double jsslwght;

    // Sublet Weight
    private Double sublet;

    // Assembly Weights
    private Double asswght;
    private Double asubletwght;
    private Double totalasw;

    // Weld Weights
    private Double weldwght;
    private Double fsubletwght;
    private Double totalwsw;

    // Treatment Weights
    private Double treatwght;
    private Double tsubletwght;
    private Double totaltwght;
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Integer getContract_id() {
		return contract_id;
	}
	public void setContract_id(Integer contract_id) {
		this.contract_id = contract_id;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public Integer getLoadtype() {
		return loadtype;
	}
	public void setLoadtype(Integer loadtype) {
		this.loadtype = loadtype;
	}
	public String getPzone() {
		return pzone;
	}
	public void setPzone(String pzone) {
		this.pzone = pzone;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getJsslwght() {
		return jsslwght;
	}
	public void setJsslwght(Double jsslwght) {
		this.jsslwght = jsslwght;
	}
	public Double getSublet() {
		return sublet;
	}
	public void setSublet(Double sublet) {
		this.sublet = sublet;
	}
	public Double getAsswght() {
		return asswght;
	}
	public void setAsswght(Double asswght) {
		this.asswght = asswght;
	}
	public Double getAsubletwght() {
		return asubletwght;
	}
	public void setAsubletwght(Double asubletwght) {
		this.asubletwght = asubletwght;
	}
	public Double getTotalasw() {
		return totalasw;
	}
	public void setTotalasw(Double totalasw) {
		this.totalasw = totalasw;
	}
	public Double getWeldwght() {
		return weldwght;
	}
	public void setWeldwght(Double weldwght) {
		this.weldwght = weldwght;
	}
	public Double getFsubletwght() {
		return fsubletwght;
	}
	public void setFsubletwght(Double fsubletwght) {
		this.fsubletwght = fsubletwght;
	}
	public Double getTotalwsw() {
		return totalwsw;
	}
	public void setTotalwsw(Double totalwsw) {
		this.totalwsw = totalwsw;
	}
	public Double getTreatwght() {
		return treatwght;
	}
	public void setTreatwght(Double treatwght) {
		this.treatwght = treatwght;
	}
	public Double getTsubletwght() {
		return tsubletwght;
	}
	public void setTsubletwght(Double tsubletwght) {
		this.tsubletwght = tsubletwght;
	}
	public Double getTotaltwght() {
		return totaltwght;
	}
	public void setTotaltwght(Double totaltwght) {
		this.totaltwght = totaltwght;
	}
    
}