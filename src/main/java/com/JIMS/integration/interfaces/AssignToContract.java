package com.JIMS.integration.interfaces;

import java.util.List;

public interface AssignToContract {
	
	String getCon_slno();
	List<String> getTaxId();
	String getBid();
    String getContract_id();
    String getInvoice_type_calculation();
    String getPercentage_value();
	String getInvoice_to_id();
    String getConsignee_id();
    String getShipment_mode_id();
    String getDelivery_condition_id();
    String getProduct_desc_id();
    String getBank_details_id();
	String getWork_id();
	String getRegd_office_id();
    String getS_code();
	String getH_code();
	String getExport();
    String getTax_ex_inc();
    String getTaxable();
    String getNon_taxable();
    String getTax_payable();
    String getFreight_advance_recovery();
	String getArea_no();
    String getLot_no();
    String getContainter_no();
    String getEpcgno();
    String getExport_title_text();
    String getTax_id();
    String getTax_name();
    String getTax_per();
    String getInvoice_to_id_value();
    String getConsignee_id_value();
    String getShipment_mode_id_value();
    String getDelivery_condition_id_value();
    String getBank_details_id_value();
	String getWork_id_value();
	String getReg_office_id_value();
    String getS_code_value();
	String getH_code_value();
	String getContract_name();
//    String getMilestone_name();
	String getExport_title();
    String getBusiness_unit_name();
  //  String getMilestone_code();
    String getBu_code();
  //  String getMilestone_id();
    
    String getTaxable_amount();
    String getNontaxable_amount();
    String getTotal();
    //businessUnit
    String getBusinessLocation();
    String getBusinessStateName();
    String getBusinessStateCode();
    String getBusinessStateId();
    String getBusinessBuCode();
    String getBusinessgstNumber();
    String getBusinesspanNumber();
    String getBusinessUnitName();
    
    // INVOICE ADDRESS
    
    String getInvoiceCountryName();
    String getInvoiceStateCode();
    String getInvoiceStateName();
    String getInvoiceGstNo();
    String getInvoicePanNo();
    String getInvoicePinNo();
    String getInvoiceDistrict();
    String getInvoiceAddress();
    
    // Consignee Address
    
    String getConsigneeCountryName();
    String getConsigneeStateCode();
    String getConsigneeStateName();
    String getConsigneeGstNo();
    String getConsigneePanNo();
    String getConsigneePinNo();
    String getConsigneeDistrict();
    String getConsigneeAddress();
    
    String getFactory_id();
    
    // BANK
    String getBankName();
    String getBankstatecode();
    String getBankstatename();
    String getBankcountrycode();
    String getBankcountryname();
    String getAccount_number();
    String getBranch_address();
    String getBranch_code();
    String getIfsc_code();
    String getSwift_code();
    String getBankcityname();
    String getwork_date();
    String getsupply_place();
    
    
    String getorg_name();
}
