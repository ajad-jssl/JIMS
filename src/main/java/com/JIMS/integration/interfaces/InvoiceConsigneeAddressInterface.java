package com.JIMS.integration.interfaces;

public interface InvoiceConsigneeAddressInterface {

	String getAddress();

	String getCity();

	String getDistrict();

	String getState_id();

	String getState_name();

	String getPin_no();

	String getCountry_id();

	String getCountry_name();

	String getId();

	String getPan_no();

	String getGst_no();

	String getFactory_id();
	
	Integer getIs_verified();
}
