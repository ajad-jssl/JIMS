package com.JIMS.integration.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GPMS_clientmaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;

    private String client_number;
    private String name;
    private String contactname1;
    private String contactname2;
    private String contactnumber;
    private String contactemail;
    private String address1;
    private String city;
    private String district;
    private String state;
    private String pincode;
    private String remarks;
    private String gst;

    private int Created_by;                  // keeps int — always set to 20, never null
    private LocalDateTime Created_date;

    private Integer Modified_by;             // ✅ Changed int → Integer (allows null)
    private LocalDateTime Modified_date;     // already nullable (object type)

    public int getCid() { return cid; }
    public void setCid(int cid) { this.cid = cid; }

    public String getClient_number() { return client_number; }
    public void setClient_number(String client_number) { this.client_number = client_number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactname1() { return contactname1; }
    public void setContactname1(String contactname1) { this.contactname1 = contactname1; }

    public String getContactname2() { return contactname2; }
    public void setContactname2(String contactname2) { this.contactname2 = contactname2; }

    public String getContactnumber() { return contactnumber; }
    public void setContactnumber(String contactnumber) { this.contactnumber = contactnumber; }

    public String getContactemail() { return contactemail; }
    public void setContactemail(String contactemail) { this.contactemail = contactemail; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getGst() { return gst; }
    public void setGst(String gst) { this.gst = gst; }

    public int getCreated_by() { return Created_by; }
    public void setCreated_by(int created_by) { Created_by = created_by; }

    public LocalDateTime getCreated_date() { return Created_date; }
    public void setCreated_date(LocalDateTime created_date) { Created_date = created_date; }

    public Integer getModified_by() { return Modified_by; }        // ✅ Integer
    public void setModified_by(Integer modified_by) { Modified_by = modified_by; }  // ✅ Integer

    public LocalDateTime getModified_date() { return Modified_date; }
    public void setModified_date(LocalDateTime modified_date) { Modified_date = modified_date; }
}