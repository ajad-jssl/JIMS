package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "spDTL_AssemblyStagesForLoadSubcon")
public class WeightModel {

	  @Id 
	   
	    @Column(name = "pieces_id")
	    private String pieces_id;

	    @Column(name = "qty")
	    private String Qty;

	    @Column(name = "assembledqty")
	    private int Assembled;
 
	    @Column(name = "weldedqty")
	    private String Welded; 

	    @Column(name = "treatedqty")
	    private String Treated;

	    @Column(name = "reworkreqdqty")
	    private String Reworkreqd;

	    @Column(name = "reworkdoneqty")
	    private String Reworkdone;

	    @Column(name = "mark")
	    private String Mark;

	    @Column(name = "descr")
	    private String Descr;

	    @Column(name = "ssize")
	    private String Ssize;

	    @Column(name = "dims")
	    private String Dims;

	    @Column(name = "reworktodo")
	    private String Reworktodo;

	    // Getters and setters
	
	    public String getpieces_id() {
	        return pieces_id;
	    }

	    public void setpieces_id(String pieces_id) {
	        this.pieces_id = pieces_id;
	    } 

	    public String getQty() {
	        return Qty;
	    }

	    public void setQty(String Qty) {
	        this.Qty = Qty;
	    } 

	    public int getAssembled() {
	        return Assembled;
	    }

	    public void setAssembled(int Assembled) {
	        this.Assembled = Assembled;
	    }

	    public String getWelded() {
	        return Welded;
	    }

	    public void setWelded(String Welded) {
	        this.Welded = Welded;
	    }

	    public String getTreated() {
	        return Treated;
	    }

	    public void setTreated(String Treated) {
	        this.Treated = Treated;
	    }

	    public String getReworkreqd() {
	        return Reworkreqd;
	    }

	    public void setReworkreqd(String Reworkreqd) {
	        this.Reworkreqd = Reworkreqd; 
	    } 

	    public String getReworkdone() {
	        return Reworkdone;
	    }

	    public void setReworkdone(String Reworkdone) {
	        this.Reworkdone = Reworkdone;
	    }

	    public String getMark() {
	        return Mark;
	    }

	    public void setMark(String Mark) {
	        this.Mark = Mark;
	    }

	    public String getDescr() {
	        return Descr;
	    }

	    public void setDescr(String Descr) {
	        this.Descr = Descr;
	    }

	    public String getSsize() {
	        return Ssize;
	    }

	    public void setSsize(String Ssize) {
	        this.Ssize = Ssize;
	    }

	    public String getDims() {
	        return Dims;
	    }

	    public void setDims(String Dims) {
	        this.Dims = Dims;
	    }

	    public String getReworktodo() {
	        return Reworktodo;
	    }

	    public void setReworktodo(String Reworktodo) {
	        this.Reworktodo = Reworktodo;
	    }

	    @Override
	    public String toString() {
	        return "Lotlist{" +
	               
	                ", pieces_id='" + pieces_id + '\'' +
	                ", Qty='" + Qty + '\'' +
	                ", Assembled=" + Assembled +
	                ", Welded='" + Welded + '\'' +
	                ", Treated='" + Treated + '\'' +
	                ", Reworkreqd='" + Reworkreqd + '\'' +
	                ", Reworkdone=" + Reworkdone +
	                ", Mark=" + Mark +
	                ", Descr=" + Descr +
	                ", Ssize=" + Ssize +
	                ", Dims='" + Dims + '\'' +
	                ", Reworktodo='" + Reworktodo + '\'' +	               
	                '}';
	    }
}
