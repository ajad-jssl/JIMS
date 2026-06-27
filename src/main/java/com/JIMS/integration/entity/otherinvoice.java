package com.JIMS.integration.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Others_invoice_master")
public class otherinvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String con_id;
    private String invoice_no;
    private String load_id;
    private Double grand_total;
    private String product_desc;
    private String remarks;
    private String export;
    private String date_of_notification;
    private String date_val;
    private String bg_type;
    private String date_of_issue;
    private String reference_no;
    private String lc_number;
    private String supply_place;
    private String s_t_exempted;
    private String lr_docketno;
    private String bg_no;
    private String date_of_expiry;
    private String date_of_ref;
    private String lc_issue_date;

    private String created_by;
    private LocalDateTime created_date;

    private String modified_by;
    private LocalDateTime modified_date;

    private Boolean is_delete;
    private Boolean is_release;
    private Integer factory_id;
    private Boolean is_verified;
    private String verified_by;
    private String verified_status;

    private String payable_to_dept;
    private String payable_by_customer;
    private String released_by;
    private String released_datetime;

    private String gst_remarks;

    private Double tax1;
    private Double tax1_per;
    private Double tax1_value;

    private Double tax2;
    private Double tax2_per;
    private Double tax2_value;

    private Double tax3;
    private Double tax3_per;
    private Double tax3_value;

    private Double Total_tax;
    private Double net_amount;

    private String invoice_remarks_id;
    private Integer Business_Unit_id;
    private Integer Invoice_to_id;
    private String prod_desc;

    private Integer ship_mode_id;
    private Integer workorder_id;
    private String Area_number;
    private String lot_number;
    private String container_name;
    private String EPCG_license;
    private String Export_title_text_id;

    private String tax_onpayable_rev;
    private Double percentage;

    private Integer consignee_id;
    private Integer bank_id;
    private Integer deliverycondition_id;
    private Integer registered_office_id;
    private Integer servicecode_id;
    private Integer HSNcode_id;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    
    
	public String getLot_number() {
		return lot_number;
	}
	public void setLot_number(String lot_number) {
		this.lot_number = lot_number;
	}
	public String getContainer_name() {
		return container_name;
	}
	public void setContainer_name(String container_name) {
		this.container_name = container_name;
	}
	public String getEPCG_license() {
		return EPCG_license;
	}
	public void setEPCG_license(String ePCG_license) {
		EPCG_license = ePCG_license;
	}
	public String getCon_id() {
		return con_id;
	}
	public void setCon_id(String con_id) {
		this.con_id = con_id;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public String getLoad_id() {
		return load_id;
	}
	public void setLoad_id(String load_id) {
		this.load_id = load_id;
	}
	public Double getGrand_total() {
		return grand_total;
	}
	public void setGrand_total(Double grand_total) {
		this.grand_total = grand_total;
	}
	public String getProduct_desc() {
		return product_desc;
	}
	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDate_of_notification() {
		return date_of_notification;
	}
	public void setDate_of_notification(String date_of_notification) {
		this.date_of_notification = date_of_notification;
	}
	public String getDate_val() {
		return date_val;
	}
	public void setDate_val(String date_val) {
		this.date_val = date_val;
	}
	public String getDate_of_issue() {
		return date_of_issue;
	}
	public void setDate_of_issue(String date_of_issue) {
		this.date_of_issue = date_of_issue;
	}
	public String getReference_no() {
		return reference_no;
	}
	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}
	public String getLc_number() {
		return lc_number;
	}
	public void setLc_number(String lc_number) {
		this.lc_number = lc_number;
	}
	public String getSupply_place() {
		return supply_place;
	}
	public void setSupply_place(String supply_place) {
		this.supply_place = supply_place;
	}
	public String getS_t_exempted() {
		return s_t_exempted;
	}
	public void setS_t_exempted(String s_t_exempted) {
		this.s_t_exempted = s_t_exempted;
	}
	public String getLr_docketno() {
		return lr_docketno;
	}
	public void setLr_docketno(String lr_docketno) {
		this.lr_docketno = lr_docketno;
	}
	public String getDate_of_expiry() {
		return date_of_expiry;
	}
	public void setDate_of_expiry(String date_of_expiry) {
		this.date_of_expiry = date_of_expiry;
	}
	public String getDate_of_ref() {
		return date_of_ref;
	}
	public void setDate_of_ref(String date_of_ref) {
		this.date_of_ref = date_of_ref;
	}
	public String getLc_issue_date() {
		return lc_issue_date;
	}
	public void setLc_issue_date(String lc_issue_date) {
		this.lc_issue_date = lc_issue_date;
	}
	public LocalDateTime getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	public LocalDateTime getModified_date() {
		return modified_date;
	}
	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}
	public Boolean getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(Boolean is_delete) {
		this.is_delete = is_delete;
	}
	public Boolean getIs_release() {
		return is_release;
	}
	public void setIs_release(Boolean is_release) {
		this.is_release = is_release;
	}
	public Integer getFactory_id() {
		return factory_id;
	}
	public void setFactory_id(Integer factory_id) {
		this.factory_id = factory_id;
	}
	public Boolean getIs_verified() {
		return is_verified;
	}
	public void setIs_verified(Boolean is_verified) {
		this.is_verified = is_verified;
	}
	public String getVerified_by() {
		return verified_by;
	}
	public void setVerified_by(String verified_by) {
		this.verified_by = verified_by;
	}
	public String getVerified_status() {
		return verified_status;
	}
	public void setVerified_status(String verified_status) {
		this.verified_status = verified_status;
	}
	public String getPayable_to_dept() {
		return payable_to_dept;
	}
	public void setPayable_to_dept(String payable_to_dept) {
		this.payable_to_dept = payable_to_dept;
	}
	public String getPayable_by_customer() {
		return payable_by_customer;
	}
	public void setPayable_by_customer(String payable_by_customer) {
		this.payable_by_customer = payable_by_customer;
	}
	public String getReleased_by() {
		return released_by;
	}
	public void setReleased_by(String released_by) {
		this.released_by = released_by;
	}
	public String getReleased_datetime() {
		return released_datetime;
	}
	public void setReleased_datetime(String released_datetime) {
		this.released_datetime = released_datetime;
	}
	public String getGst_remarks() {
		return gst_remarks;
	}
	public void setGst_remarks(String gst_remarks) {
		this.gst_remarks = gst_remarks;
	}
	public Double getTax1() {
		return tax1;
	}
	public void setTax1(Double tax1) {
		this.tax1 = tax1;
	}
	public Double getTax1_per() {
		return tax1_per;
	}
	public void setTax1_per(Double tax1_per) {
		this.tax1_per = tax1_per;
	}
	public Double getTax1_value() {
		return tax1_value;
	}
	public void setTax1_value(Double tax1_value) {
		this.tax1_value = tax1_value;
	}
	public Double getTax2() {
		return tax2;
	}
	public void setTax2(Double tax2) {
		this.tax2 = tax2;
	}
	public Double getTax2_per() {
		return tax2_per;
	}
	public void setTax2_per(Double tax2_per) {
		this.tax2_per = tax2_per;
	}
	public Double getTax2_value() {
		return tax2_value;
	}
	public void setTax2_value(Double tax2_value) {
		this.tax2_value = tax2_value;
	}
	public Double getTax3() {
		return tax3;
	}
	public void setTax3(Double tax3) {
		this.tax3 = tax3;
	}
	public Double getTax3_per() {
		return tax3_per;
	}
	public void setTax3_per(Double tax3_per) {
		this.tax3_per = tax3_per;
	}
	public Double getTax3_value() {
		return tax3_value;
	}
	public void setTax3_value(Double tax3_value) {
		this.tax3_value = tax3_value;
	}
	public Double getTotal_tax() {
		return Total_tax;
	}
	public void setTotal_tax(Double total_tax) {
		Total_tax = total_tax;
	}
	public Double getNet_amount() {
		return net_amount;
	}
	public void setNet_amount(Double net_amount) {
		this.net_amount = net_amount;
	}
	public String getInvoice_remarks_id() {
		return invoice_remarks_id;
	}
	public void setInvoice_remarks_id(String invoice_remarks_id) {
		this.invoice_remarks_id = invoice_remarks_id;
	}
	public String getExport() {
		return export;
	}
	public void setExport(String export) {
		this.export = export;
	}
	public String getBg_type() {
		return bg_type;
	}
	public void setBg_type(String bg_type) {
		this.bg_type = bg_type;
	}
	public String getBg_no() {
		return bg_no;
	}
	public void setBg_no(String bg_no) {
		this.bg_no = bg_no;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public Integer getBusiness_Unit_id() {
		return Business_Unit_id;
	}
	public void setBusiness_Unit_id(Integer business_Unit_id) {
		Business_Unit_id = business_Unit_id;
	}
	public Integer getInvoice_to_id() {
		return Invoice_to_id;
	}
	public void setInvoice_to_id(Integer invoice_to_id) {
		Invoice_to_id = invoice_to_id;
	}
	public String getProdDesc() {
		return prod_desc;
	}
	public void setProdDesc(String prodDesc) {
		this.prod_desc = prodDesc;
	}
	public Integer getShip_mode_id() {
		return ship_mode_id;
	}
	public void setShip_mode_id(Integer ship_mode_id) {
		this.ship_mode_id = ship_mode_id;
	}
	public Integer getWorkorder_id() {
		return workorder_id;
	}
	public void setWorkorder_id(Integer workorder_id) {
		this.workorder_id = workorder_id;
	}
	public String getArea_number() {
		return Area_number;
	}
	public void setArea_number(String area_number) {
		Area_number = area_number;
	}
	public String getExport_title_text_id() {
		return Export_title_text_id;
	}
	public void setExport_title_text_id(String export_title_text_id) {
		Export_title_text_id = export_title_text_id;
	}
	public String getTaxOnpayableRev() {
		return tax_onpayable_rev;
	}
	public void setTaxOnpayableRev(String taxOnpayableRev) {
		this.tax_onpayable_rev = taxOnpayableRev;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public Integer getConsignee_id() {
		return consignee_id;
	}
	public void setConsignee_id(Integer consignee_id) {
		this.consignee_id = consignee_id;
	}
	public Integer getBank_id() {
		return bank_id;
	}
	public void setBank_id(Integer bank_id) {
		this.bank_id = bank_id;
	}
	public Integer getDeliverycondition_id() {
		return deliverycondition_id;
	}
	public void setDeliverycondition_id(Integer deliverycondition_id) {
		this.deliverycondition_id = deliverycondition_id;
	}
	public Integer getRegistered_office_id() {
		return registered_office_id;
	}
	public void setRegistered_office_id(Integer registered_office_id) {
		this.registered_office_id = registered_office_id;
	}
	public Integer getServicecode_id() {
		return servicecode_id;
	}
	public void setServicecode_id(Integer servicecode_id) {
		this.servicecode_id = servicecode_id;
	}
	public Integer getHSNcode_id() {
		return HSNcode_id;
	}
	public void setHSNcode_id(Integer hSNcode_id) {
		HSNcode_id = hSNcode_id;
	}
	
	
    
}
