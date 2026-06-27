package com.JIMS.integration.interfaces;

public interface SaleOrderInterface {

	String getSoe_id();

	String getSale_order_code();

	String getSale_order_type_id();

	String getLocation_type_id();

	String getSale_order_to_id();

	String getAuction_date();

	String getSale_order_duration();

	String getAdvance_payment();

	String getBilling_address_id();

	String getBilling_address();

	String getShipping_address_id();

	String getShipping_address();

	String getBusiness_unit_id();

	String getBuisness_address();

	String getTax1();

	String getTax1_value();

	String getTax1_per();

	String getTax2_per();

	String getTax3_per();

	String getTax2();

	String getTax2_value();

	String getTax3();

	String getTax3_value();

	String getNet_amount();

	String getTotal_tax();

	String getGrand_total();

	String getCreated_by();

	String getCreated_date();

	String getModified_by();

	String getModified_date();

	String getIs_delete();

	String getFactory_id();

	String getScrap_type();

	String getLocation_name();

	String getLocation_code();

	String getAddress();
	
	String gettaxname1();
	
	String gettaxname2();
	
	String gettaxname3();

	String getsaleorderdate();
}
