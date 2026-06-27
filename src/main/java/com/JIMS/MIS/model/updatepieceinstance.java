package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Piece_Instance")
public class updatepieceinstance {

    @Id
    private Long instance_id;

    private String tload_id;
    private int load_id;
    private int pieces_id;
    private Long revision;
    private int comp_id;
    private int fab_id;
    
	public Long getInstance_id() {
		return instance_id;
	}
	public void setInstance_id(Long instance_id) {
		this.instance_id = instance_id;
	}
	public String getTload_id() {
		return tload_id;
	}
	public void setTload_id(String tload_id) {
		this.tload_id = tload_id;
	}
	public int getLoad_id() {
		return load_id;
	}
	public void setLoad_id(int load_id) {
		this.load_id = load_id;
	}
	public int getPieces_id() {
		return pieces_id;
	}
	public void setPieces_id(int pieces_id) {
		this.pieces_id = pieces_id;
	}
	public Long getRevision() {
		return revision;
	}
	public void setRevision(Long revision) {
		this.revision = revision;
	}
	public int getComp_id() {
		return comp_id;
	}
	public void setComp_id(int comp_id) {
		this.comp_id = comp_id;
	}
	public int getFab_id() {
		return fab_id;
	}
	public void setFab_id(int fab_id) {
		this.fab_id = fab_id;
	}
    
    
}
