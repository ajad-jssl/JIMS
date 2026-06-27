package com.JIMS.MIS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class bindloaddatamodel {
@Id
private int pieces_id;
private int load_id;
private String descr;	
private int pqty;
private double weight;
private int max_piecestages_id;
private int pieceRework;

public int getPieces_id() {
	return pieces_id;
}
public void setPieces_id(int pieces_id) {
	this.pieces_id = pieces_id;
}
public String getDescr() {
	return descr;
}
public void setDescr(String descr) {
	this.descr = descr;
}
public int getLoad_id() {
	return load_id;
}
public void setLoad_id(int load_id) {
	this.load_id = load_id;
}
public int getPqty() {
	return pqty;
}
public void setPqty(int pqty) {
	this.pqty = pqty;
}
public double getWeight() {
	return weight;
}
public void setWeight(double weight) {
	this.weight = weight;
}
public int getMaxPieceStagesId() {
	return max_piecestages_id;
}
public void setMaxPieceStagesId(int maxPieceStagesId) {
	this.max_piecestages_id = maxPieceStagesId;
}
public Integer getPieceRework() {
	return pieceRework;
}
public void setPieceRework(Integer pieceRework) {
	this.pieceRework = pieceRework;
}
public int getMax_piecestages_id() {
	return max_piecestages_id;
}
public void setMax_piecestages_id(int max_piecestages_id) {
	this.max_piecestages_id = max_piecestages_id;
}


}
