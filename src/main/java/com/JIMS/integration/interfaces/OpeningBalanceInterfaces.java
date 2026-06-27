
package com.JIMS.integration.interfaces;

import java.time.LocalDate;

public interface OpeningBalanceInterfaces {
	
	String getCon_id();
	String getTotal();
	String getNet_total();
	String getTax_total();
	String getDescription();
	String getCreated_by();
	String getCreated_date();
	String getContract_name();
	String getFactory_id();
	String getPn_id();
	String getnontax_avl_bal();
	String getAvl_bal();
	String getContract_id();
	String getutr_reference();
	String getproforma_reference();
	LocalDate  gettxn_date();
	
	
	
}
