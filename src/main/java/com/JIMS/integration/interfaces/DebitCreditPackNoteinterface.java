package com.JIMS.integration.interfaces;

public interface DebitCreditPackNoteinterface {

	String getDcPn_no();

	String getInvoice_type_id();

	String getNote_type();

	String getFreight();

	String getFilepath();

	String getTotalpn_amount();

	String getDc_pn_id();
	
	String getcontract_name(); 

	String getcon_id(); 
	
	String gettype();
	
    String getmilestone_id();
    
	String getmilestone();
	
	String getmilestone_name();
	
	String getquantities();
	String getkgs();
	String getUnit_price();
	String getTotal();
	String getUOM_id();
	String gettype_id();
	String getdc_pn_items_id();
	String getUnit_id();
	String getUOM_name();
	String getscrap_name_type_id();
	String getInvoice_remarks_type_name();
	String getinvgen();
}
