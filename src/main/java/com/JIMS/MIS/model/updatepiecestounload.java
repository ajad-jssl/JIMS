package com.JIMS.MIS.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Piece_Instance")
public class updatepiecestounload {

    @Id
    private int instance_id;

    private int pieces_id;  // Assuming pieces_id is a primary key
    private int tload_id;
    private int load_id;
    private int fab_id;
    private int supplier_id;
    private int locfg_id;
	public int getInstance_id() {
		return instance_id;
	}
	public void setInstance_id(int instance_id) {
		this.instance_id = instance_id;
	}
	public int getPieces_id() {
		return pieces_id;
	}
	public void setPieces_id(int pieces_id) {
		this.pieces_id = pieces_id;
	}
	public int getTload_id() {
		return tload_id;
	}
	public void setTload_id(int tload_id) {
		this.tload_id = tload_id;
	}
	public int getLoad_id() {
		return load_id;
	}
	public void setLoad_id(int load_id) {
		this.load_id = load_id;
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
	public int getLocfg_id() {
		return locfg_id;
	}
	public void setLocfg_id(int locfg_id) {
		this.locfg_id = locfg_id;
	}
    
}
