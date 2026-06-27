package com.JIMS.integration.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "CONTRACT_GATEPASS_ENTRY")  
public class Contract_gatepassmodel {
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cg_id;

    private String Pass_id;

    private Integer company_id;

    private String Aadhar_no;

    private String Fname;

    private String Lname;



	private String Gender;

    private LocalDate pass_issued_dt;

    private LocalDate Dob;

    private LocalDate Doj;
    
    private String Age;

    private LocalDate valid_till;

    private Integer  did;

    private Integer deg;

    private Integer noj;

    private Integer medical;

    private Integer work_loc;

    private Integer BG_id;

    private Integer Bus_id;

    private Integer Vac_id;

    private LocalDate Vac_date;

    private LocalDate Vac2_date;
    
    private String Admin_remarks;

    private Integer status;
    
    private String status_description;

    @Column(name = "photo_path")
    @JsonProperty("photoFilePath")
    private String photo_path;

    
public String getPhoto_path() {
		return photo_path;
	}

	public void setPhoto_path(String photo_path) {
		this.photo_path = photo_path;
	}

public String getStatus_description() {
		return status_description;
	}

	public void setStatus_description(String status_description) {
		this.status_description = status_description;
	}

@Column(name = "Emp_id")
@JsonProperty("emp_id")
private String empId;

    private String filepath;

    private String contactno;

    private String pan_no;

    private String UAN_no;

    private String PF_no;

    private String ESIC_no;

    private String Nominee_name;

    private String NRelationship;

    private LocalDate NDOB;

    private String NAge;

    private String NTotal_depandants;

    private String Bank_name;

    private String bacc;

    private String Branch;

    private String IFSC;

    private String Father_name;

    private String Marital_status;

    private String Spouse_name;

    private String emergency_contact_no;

    private String Relation;

    private String Religion;

    private String Education;

    private String Permnt_add;

    private String dist;

    private String state;

    private String pincode;
    
    private Integer active;

    private String Present_add;

    private String Present_dist;

    private String Present_state;

    private String present_pincode;

    private Integer Emp_status;

    private String previous_exp;

    private String emergency_contact_name;

    private String Vac_status;

    private LocalDate rejoin_date;
    
    private String servicein_JSSL;

    private Integer Created_by;

    private LocalDateTime Created_date;

    private Integer Modified_by;

    private LocalDateTime Modified_date;

    private Integer factory_id;
    private Integer joinee_type;
    
    
    private String aadharFilePath;
    private String medicalFilePath;
    private String other1FilePath;
    

    // Add getters and setters
    public String getAadharFilePath() {
        return aadharFilePath;
    }

    public void setAadharFilePath(String aadharFilePath) {
        this.aadharFilePath = aadharFilePath;
    }

    public String getMedicalFilePath() {
        return medicalFilePath;
    }

    public void setMedicalFilePath(String medicalFilePath) {
        this.medicalFilePath = medicalFilePath;
    }

    public String getOther1FilePath() {
        return other1FilePath;
    }

    public void setOther1FilePath(String other1FilePath) {
        this.other1FilePath = other1FilePath;
    }



	public Integer getJoinee_type() {
		return joinee_type;
	}

	public void setJoinee_type(Integer joinee_type) {
		this.joinee_type = joinee_type;
	}

	public Integer getCg_id() {
		return cg_id;
	}

	public void setCg_id(Integer cg_id) {
		this.cg_id = cg_id;
	}

	public String getPass_id() {
		return Pass_id;
	}

	public void setPass_id(String pass_id) {
		Pass_id = pass_id;
	}

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public String getAadhar_no() {
		return Aadhar_no;
	}

	public void setAadhar_no(String aadhar_no) {
		Aadhar_no = aadhar_no;
	}

	public String getFname() {
		return Fname;
	}

	public void setFname(String fname) {
		Fname = fname;
	}

	public String getLname() {
		return Lname;
	}

	public void setLname(String lname) {
		Lname = lname;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public LocalDate getPass_issued_dt() {
		return pass_issued_dt;
	}

	public void setPass_issued_dt(LocalDate pass_issued_dt) {
		this.pass_issued_dt = pass_issued_dt;
	}

	public LocalDate getDob() {
		return Dob;
	}

	public void setDob(LocalDate dob) {
		Dob = dob;
	}

	public LocalDate getDoj() {
		return Doj;
	}

	public void setDoj(LocalDate doj) {
		Doj = doj;
	}
	
	public String getAge() {
		return Age;
	}

	public void setAge(String age) {
		Age = age;
	}

	public LocalDate getValid_till() {
		return valid_till;
	}

	public void setValid_till(LocalDate valid_till) {
		this.valid_till = valid_till;
	}

	public Integer  getDid() {
		return did;
	}

	public void setDid(Integer  did) {
		this.did = did;
	}

	public Integer getDeg() {
		return deg;
	}

	public void setDeg(Integer deg) {
		this.deg = deg;
	}

	public Integer getNoj() {
		return noj;
	}

	public void setNoj(Integer noj) {
		this.noj = noj;
	}

	public Integer getMedical() {
		return medical;
	}

	public void setMedical(Integer medical) {
		this.medical = medical;
	}

	public Integer getWork_loc() {
		return work_loc;
	}

	public void setWork_loc(Integer work_loc) {
		this.work_loc = work_loc;
	}

	public Integer getBG_id() {
		return BG_id;
	}

	public void setBG_id(Integer bG_id) {
		BG_id = bG_id;
	}

	public Integer getBus_id() {
		return Bus_id;
	}

	public void setBus_id(Integer bus_id) {
		Bus_id = bus_id;
	}

	public Integer getVac_id() {
		return Vac_id;
	}

	public void setVac_id(Integer vac_id) {
		Vac_id = vac_id;
	}

	public LocalDate getVac_date() {
		return Vac_date;
	}

	public void setVac_date(LocalDate vac_date) {
		Vac_date = vac_date;
	}

	public LocalDate getVac2_date() {
		return Vac2_date;
	}

	public void setVac2_date(LocalDate vac2_date) {
		Vac2_date = vac2_date;
	}

	
	public String getAdmin_remarks() {
		return Admin_remarks;
	}

	public void setAdmin_remarks(String admin_remarks) {
		Admin_remarks = admin_remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getPan_no() {
		return pan_no;
	}

	public void setPan_no(String pan_no) {
		this.pan_no = pan_no;
	}

	public String getUAN_no() {
		return UAN_no;
	}

	public void setUAN_no(String uAN_no) {
		UAN_no = uAN_no;
	}

	public String getPF_no() {
		return PF_no;
	}

	public void setPF_no(String pF_no) {
		PF_no = pF_no;
	}

	public String getESIC_no() {
		return ESIC_no;
	}

	public void setESIC_no(String eSIC_no) {
		ESIC_no = eSIC_no;
	}

	public String getNominee_name() {
		return Nominee_name;
	}

	public void setNominee_name(String nominee_name) {
		Nominee_name = nominee_name;
	}

	public String getNRelationship() {
		return NRelationship;
	}

	public void setNRelationship(String nRelationship) {
		NRelationship = nRelationship;
	}

	public LocalDate getNDOB() {
		return NDOB;
	}

	public void setNDOB(LocalDate nDOB) {
		NDOB = nDOB;
	}

	public String getNAge() {
		return NAge;
	}

	public void setNAge(String nAge) {
		NAge = nAge;
	}

	public String getNTotal_depandants() {
		return NTotal_depandants;
	}

	public void setNTotal_depandants(String nTotal_depandants) {
		NTotal_depandants = nTotal_depandants;
	}

	public String getBank_name() {
		return Bank_name;
	}

	public void setBank_name(String bank_name) {
		Bank_name = bank_name;
	}

	public String getBacc() {
		return bacc;
	}

	public void setBacc(String bacc) {
		this.bacc = bacc;
	}

	public String getBranch() {
		return Branch;
	}

	public void setBranch(String branch) {
		Branch = branch;
	}

	public String getIFSC() {
		return IFSC;
	}

	public void setIFSC(String iFSC) {
		IFSC = iFSC;
	}

	public String getFather_name() {
		return Father_name;
	}

	public void setFather_name(String father_name) {
		Father_name = father_name;
	}

	public String getMarital_status() {
		return Marital_status;
	}

	public void setMarital_status(String marital_status) {
		Marital_status = marital_status;
	}

	public String getSpouse_name() {
		return Spouse_name;
	}

	public void setSpouse_name(String spouse_name) {
		Spouse_name = spouse_name;
	}

	public String getEmergency_contact_no() {
		return emergency_contact_no;
	}

	public void setEmergency_contact_no(String emergency_contact_no) {
		this.emergency_contact_no = emergency_contact_no;
	}

	public String getRelation() {
		return Relation;
	}

	public void setRelation(String relation) {
		Relation = relation;
	}

	public String getReligion() {
		return Religion;
	}

	public void setReligion(String religion) {
		Religion = religion;
	}

	public String getEducation() {
		return Education;
	}

	public void setEducation(String education) {
		Education = education;
	}

	public String getPermnt_add() {
		return Permnt_add;
	}

	public void setPermnt_add(String permnt_add) {
		Permnt_add = permnt_add;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public Integer getActive() {
	    return active;
	}

	public void setActive(Integer active) {
	    this.active = active;
	}

	public String getPresent_add() {
		return Present_add;
	}

	public void setPresent_add(String present_add) {
		Present_add = present_add;
	}

	public String getPresent_dist() {
		return Present_dist;
	}

	public void setPresent_dist(String present_dist) {
		Present_dist = present_dist;
	}

	public String getPresent_state() {
		return Present_state;
	}

	public void setPresent_state(String present_state) {
		Present_state = present_state;
	}

	public String getPresent_pincode() {
		return present_pincode;
	}

	public void setPresent_pincode(String present_pincode) {
		this.present_pincode = present_pincode;
	}

	public Integer getEmp_status() {
		return Emp_status;
	}

	public void setEmp_status(Integer emp_status) {
		Emp_status = emp_status;
	}

	public String getPrevious_exp() {
		return previous_exp;
	}

	public void setPrevious_exp(String previous_exp) {
		this.previous_exp = previous_exp;
	}

	public String getEmergency_contact_name() {
		return emergency_contact_name;
	}

	public void setEmergency_contact_name(String emergency_contact_name) {
		this.emergency_contact_name = emergency_contact_name;
	}

	public String getVac_status() {
		return Vac_status;
	}

	public void setVac_status(String vac_status) {
		Vac_status = vac_status;
	}

	public LocalDate getRejoin_date() {
		return rejoin_date;
	}

	public void setRejoin_date(LocalDate rejoin_date) {
		this.rejoin_date = rejoin_date;
	}
	
	

	public String getServicein_JSSL() {
		return servicein_JSSL;
	}

	public void setServicein_JSSL(String servicein_JSSL) {
		this.servicein_JSSL = servicein_JSSL;
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

	public Integer getFactory_id() {
		return factory_id;
	}

	public void setFactory_id(Integer factory_id) {
		this.factory_id = factory_id;
	}
    
}