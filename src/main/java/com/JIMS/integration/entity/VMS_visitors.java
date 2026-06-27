package com.JIMS.integration.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VMS_Visitors")  
public class VMS_visitors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer V_id;
    
    private String Visitor_id;
    private Integer Company_id;
    private Integer Pass_id;
    private String visitor_name;
    private String outremarks;
    private String Vecno;
    private LocalTime Intime;
    private LocalTime Outtime;
    @JsonProperty("whom_to_meet")
    @Column(name = "Wtm")
    private String Wtm;

   // private String Wtm;
    private String department;
    private String Contact_no;
    private Integer Vec_type_id;
    private String Purpose;
    private String Remarks;
    @Column(name = "Factory_id")
    private Integer factoryId;
   // private Integer Factory_id;
    private String Created_by;
    private LocalDateTime Created_date;
    private String Modified_by;
    private LocalDateTime Modified_date;
    private String capture;

    // No-args constructor for JPA
    public VMS_visitors() {
    }

    // Getters and Setters
    public Integer getV_id() {
        return V_id;
    }
    public void setV_id(Integer v_id) {
        V_id = v_id;
    }

    public String getVisitor_id() {
        return Visitor_id;
    }
    public void setVisitor_id(String visitor_id) {
        Visitor_id = visitor_id;
    }

    public Integer getCompany_id() {
        return Company_id;
    }
    public void setCompany_id(Integer company_id) {
        Company_id = company_id;
    }

    public Integer getPass_id() {
        return Pass_id;
    }
    public void setPass_id(Integer pass_id) {
        Pass_id = pass_id;
    }

    public String getVisitor_name() {
        return visitor_name;
    }
    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getOutremarks() {
        return outremarks;
    }
    public void setOutremarks(String outremarks) {
        this.outremarks = outremarks;
    }

    public String getVecno() {
        return Vecno;
    }
    public void setVecno(String vecno) {
        Vecno = vecno;
    }

    public LocalTime getIntime() {
        return Intime;
    }
    public void setIntime(LocalTime intime) {
        Intime = intime;
    }

    public LocalTime getOuttime() {
        return Outtime;
    }
    public void setOuttime(LocalTime outtime) {
        Outtime = outtime;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContact_no() {
        return Contact_no;
    }
    public void setContact_no(String contact_no) {
        Contact_no = contact_no;
    }

    public Integer getVec_type_id() {
        return Vec_type_id;
    }
    public void setVec_type_id(Integer vec_type_id) {
        Vec_type_id = vec_type_id;
    }

    public String getPurpose() {
        return Purpose;
    }
    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getRemarks() {
        return Remarks;
    }
    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public String getCreated_by() {
        return Created_by;
    }
    public void setCreated_by(String created_by) {
        Created_by = created_by;
    }

    public LocalDateTime getCreated_date() {
        return Created_date;
    }
    public void setCreated_date(LocalDateTime created_date) {
        Created_date = created_date;
    }

    public String getModified_by() {
        return Modified_by;
    }
    public void setModified_by(String modified_by) {
        Modified_by = modified_by;
    }

    public LocalDateTime getModified_date() {
        return Modified_date;
    }
    public void setModified_date(LocalDateTime modified_date) {
        Modified_date = modified_date;
    }

	public String getWtm() {
		return Wtm;
	}

	public void setWtm(String wtm) {
		this.Wtm=wtm;
	}

	public String getCapture() {
		return capture;
	}

	public void setCapture(String capture) {
		this.capture = capture;
	}
	
	
}
