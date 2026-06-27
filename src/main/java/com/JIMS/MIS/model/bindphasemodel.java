package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "contract_zones")
public class bindphasemodel {
	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int czone_Id;
    private String contract_Zone;
    private String zone_Descr;
    private String descr;
    private long supplier_id;
	public int getCzone_Id() {
		return czone_Id;
	}
	public void setCzone_Id(int czone_Id) {
		this.czone_Id = czone_Id;
	}
	public String getContract_Zone() {
		return contract_Zone;
	}
	public void setContract_Zone(String contract_Zone) {
		this.contract_Zone = contract_Zone;
	}
	public String getZone_Descr() {
		return zone_Descr;
	}
	public void setZone_Descr(String zone_Descr) {
		this.zone_Descr = zone_Descr;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public long getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(long supplier_id) {
		this.supplier_id = supplier_id;
	}
	


}
