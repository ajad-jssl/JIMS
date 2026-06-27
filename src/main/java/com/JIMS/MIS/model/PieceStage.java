package com.JIMS.MIS.model;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 	
@Table(name="PieceStage",schema ="dbo")
public class PieceStage { 
  
	@Id
	@Column(name="piecestage_id")
	@GeneratedValue (strategy =GenerationType.IDENTITY)
	private Integer piecestage_id;
	@Column(name="pieces_id")
	private Integer pieces_id;
	@Column(name="pieceinstance_id")
	private Integer pieceinstance_id;
	@Column(name="piecestages_id")
	private Integer piecestages_id;
	@Column(name="stagedatetime")
	private LocalDateTime stagedatetime;
	@Column(name="user_id")
	private Integer user_id;
	@Column(name="created")
	private LocalDateTime created;
	@Column(name="workby")
	private String workby;
	@Column(name="revision")
	private String revision;
	@Column(name="line_id")
	private Integer line_id;
	@Column(name="piecestagenotes_id")
	private Integer piecestagenotes_id;
	@Column(name="batch_id")
	private Integer batch_id;
	@Column(name="dft")
	private String dft;
	@Column(name="fabsupplier_id")
	private Integer fabsupplier_id;
	@Column(name="userid")
	private Integer userid;
	@Column(name="udtm")
	private LocalDateTime udtm;
	
	
	
	public Integer getPiecestage_id() {
		return piecestage_id;
	}
	public void setPiecestage_id(Integer piecestage_id) {
		this.piecestage_id = piecestage_id;
	}
	public Integer getPieces_id() {
		return pieces_id; 
	}
	public void setPieces_id(Integer pieces_id) {
		this.pieces_id = pieces_id;
	}
	public Integer getPieceinstance_id() {
		return pieceinstance_id;
	}
	public void setPieceinstance_id(Integer pieceinstance_id) {
		this.pieceinstance_id = pieceinstance_id;
	}
	public Integer getPiecestages_id() {
		return piecestages_id;
	}
	public void setPiecestages_id(Integer piecestages_id) {
		this.piecestages_id = piecestages_id;
	}
	public LocalDateTime getStagedatetime() {
		return stagedatetime;
	}
	public void setStagedatetime(LocalDateTime stagedatetime) {
		this.stagedatetime = stagedatetime;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	public String getWorkby() {
		return workby;
	}
	public void setWorkby(String workby) {
		this.workby = workby;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public Integer getLine_id() {
		return line_id;
	}
	public void setLine_id(Integer line_id) {
		this.line_id = line_id;
	}
	public Integer getPiecestagenotes_id() {
		return piecestagenotes_id;
	}
	public void setPiecestagenotes_id(Integer piecestagenotes_id) {
		this.piecestagenotes_id = piecestagenotes_id;
	}
	public Integer getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(Integer batch_id) {
		this.batch_id = batch_id;
	}
	public String getDft() {
		return dft;
	}
	public void setDft(String dft) {
		this.dft = dft;
	}
	public Integer getFabsupplier_id() {
		return fabsupplier_id;
	}
	public void setFabsupplier_id(Integer fabsupplier_id) {
		this.fabsupplier_id = fabsupplier_id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public LocalDateTime getUdtm() {
		return udtm;
	}
	public void setUdtm(LocalDateTime udtm) {
		this.udtm = udtm;
	}
		
}
