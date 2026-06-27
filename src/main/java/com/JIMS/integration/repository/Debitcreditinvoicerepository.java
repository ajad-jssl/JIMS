package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.entity.dbtcrdscrapInvoiceMaster;
import com.JIMS.integration.interfaces.Debitcreditinvoiceinterface;
import com.JIMS.integration.interfaces.OtherInvoiceInterface;
import com.JIMS.integration.interfaces.dbtcrdpackingitemsscrap_interface;
import com.JIMS.integration.interfaces.othersPackingItem_LIST_INTERFACES;

import jakarta.transaction.Transactional;

public interface Debitcreditinvoicerepository extends JpaRepository<dbtcrdscrapInvoiceMaster, Integer> {
	@Query(value = "SELECT im.*,im.created_date AS created_onlydate,qm.dc_pn_id, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED'\r\n"
			+ "        ELSE CAST(im.invoice_no AS VARCHAR(100)) \r\n"
			+ "    END AS invoice_number,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN im.Cancel = 1 THEN 'CANCELLED'\r\n"
			+ "        WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED'\r\n"
			+ "        ELSE 'VERIFIED'\r\n"
			+ "    END AS status\r\n"
			+ "FROM DEBIT_CREDIT_INVOICE_MASTER im \r\n"
			+ "INNER JOIN DEBITCREDIT_PACKINGNOTE qm ON qm.dcPn_no = im.load_id \r\n"
			+ "WHERE im.is_delete is null AND (',' + CAST(im.factory_id AS VARCHAR(50)) + ',' LIKE '%,' + CAST(:factory_id AS VARCHAR(10)) + ',%')", nativeQuery = true)
	List<Debitcreditinvoiceinterface> listdbtcrdInvoiceMasterInfo(String factory_id);
	
	
	
	@Query(
		    value =
		    "SELECT im.*, im.created_date AS created_onlydate, qm.dc_pn_id, " +
		    "im.note_type AS note_type, " +          // ✅ ADD THIS LINE

		    "CASE " +
		    "    WHEN ISNUMERIC(CAST(qm.con_id AS VARCHAR)) = 1 THEN cm.contract_name " +
		    "    ELSE CAST(qm.con_id AS VARCHAR) " +
		    "END AS contract_name, " +

		    "CASE " +
		    "    WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED' " +
		    "    ELSE CAST(im.invoice_no AS VARCHAR(100)) " +
		    "END AS invoice_number, " +

		    "CASE " +
		    "    WHEN im.Cancel = 1 THEN 'CANCELLED' " +
		    "    WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED' " +
		    "    ELSE 'VERIFIED' " +
		    "END AS status " +

		    "FROM DEBIT_CREDIT_INVOICE_MASTER im " +
		    "INNER JOIN DEBITCREDIT_PACKINGNOTE qm ON qm.dcPn_no = im.load_id " +
		    "LEFT JOIN CONTRACT_MASTER cm ON cm.contract_id = TRY_CAST(qm.con_id AS INT) " +

		    "WHERE im.is_delete IS NULL " +
		    "AND (',' + CAST(im.factory_id AS VARCHAR(50)) + ',' " +
		    "    LIKE '%,' + CAST(:factory_id AS VARCHAR(50)) + ',%') " +

		    "AND ( :search IS NULL OR :search = '' OR " +
		    "cm.contract_name LIKE '%' + :search + '%' OR " +
		    "qm.note_type LIKE '%' + :search + '%' OR " +        // ✅ already here
		    "CAST(im.load_id AS VARCHAR(50)) LIKE '%' + :search + '%' OR " +
		    "CAST(im.invoice_no AS VARCHAR(50)) LIKE '%' + :search + '%' OR " +
		    "CAST(qm.con_id AS VARCHAR(50)) LIKE '%' + :search + '%' OR " +
		    "CONVERT(VARCHAR(10), im.created_date, 105) LIKE '%' + :search + '%' " +
		    ") " +

		    "ORDER BY im.created_date DESC",

		    countQuery =
		    "SELECT COUNT(*) " +
		    "FROM DEBIT_CREDIT_INVOICE_MASTER im " +
		    "INNER JOIN DEBITCREDIT_PACKINGNOTE qm ON qm.dcPn_no = im.load_id " +
		    "LEFT JOIN CONTRACT_MASTER cm ON cm.contract_id = TRY_CAST(qm.con_id AS INT) " +

		    "WHERE im.is_delete IS NULL " +
		    "AND (',' + CAST(im.factory_id AS VARCHAR(50)) + ',' " +
		    "    LIKE '%,' + CAST(:factory_id AS VARCHAR(50)) + ',%') " +

		    "AND ( :search IS NULL OR :search = '' OR " +
		    "cm.contract_name LIKE '%' + :search + '%' OR " +
		    "qm.note_type LIKE '%' + :search + '%' OR " +
		    "CAST(im.load_id AS VARCHAR(50)) LIKE '%' + :search + '%' OR " +
		    "CAST(im.invoice_no AS VARCHAR(50)) LIKE '%' + :search + '%' OR " +
		    "CAST(qm.con_id AS VARCHAR(50)) LIKE '%' + :search + '%' OR " +
		    "CONVERT(VARCHAR(10), im.created_date, 105) LIKE '%' + :search + '%' " +
		    ")",

		    nativeQuery = true
		)
		Page<Debitcreditinvoiceinterface> listdbtcrdInvoiceMasterInfoPaged(
		        @Param("factory_id") String factory_id,
		        @Param("search") String search,
		        Pageable pageable
		);
	
	@Query(value = "select im.* from DEBIT_CREDIT_INVOICE_MASTER im\r\n"
			+ " WHERE id = :id", nativeQuery = true)
	Debitcreditinvoiceinterface listSearchById(String id);
	
	@Transactional
	@Modifying
	@Query(value="update DEBIT_CREDIT_INVOICE_MASTER set verified_by =:verified_by,verified_status=1, invoice_no =:newSeriesNumber,gst_remarks =:gst_remarks where id = :id", nativeQuery = true)
	int updatedbtcrdInvoiceVerificationDetails(String verified_by, String newSeriesNumber, String gst_remarks,
			String id);
	@Transactional
	@Modifying
	@Query(value="update DEBIT_CREDIT_INVOICE_MASTER set verified_by =:verified_by,verified_status=1,gst_remarks =:gst_remarks where id = :id", nativeQuery = true)
	int updatedbtcrdInvoiceVerificationDetailsrelease(String verified_by, String gst_remarks,
			String id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE DEBIT_CREDIT_INVOICE_MASTER SET product_desc = :product_desc, remarks = :remarks, date_of_notification = :date_of_notification,"
	        + " date_val = :date_val, bg_type = :bg_type, date_of_issue = :date_of_issue, reference_no = :reference_no,"
	        + " lc_number = :lc_number, supply_place = :supply_place, lr_docketno = :lr_docketno, bg_no = :bg_no,"
	        + " date_of_expiry = :date_of_expiry, date_of_ref = :date_of_ref, lc_issue_date = :lc_issue_date,"
	        + " modified_by = :modified_by, modified_date = GETDATE(), s_t_exempted = :s_t_exempted "
	        + " WHERE id = :id AND ("
	        + " COALESCE(product_desc, '') <> COALESCE(:product_desc, '')"
	        + " OR COALESCE(remarks, '') <> COALESCE(:remarks, '')"
	        + " OR COALESCE(date_of_notification, '') <> COALESCE(:date_of_notification, '')"
	        + " OR COALESCE(date_val, '') <> COALESCE(:date_val, '')"
	        + " OR COALESCE(bg_type, '') <> COALESCE(:bg_type, '')"
	        + " OR COALESCE(date_of_issue, '') <> COALESCE(:date_of_issue, '')"
	        + " OR COALESCE(reference_no, '') <> COALESCE(:reference_no, '')"
	        + " OR COALESCE(lc_number, '') <> COALESCE(:lc_number, '')"
	        + " OR COALESCE(supply_place, '') <> COALESCE(:supply_place, '')"
	        + " OR COALESCE(lr_docketno, '') <> COALESCE(:lr_docketno, '')"
	        + " OR COALESCE(bg_no, '') <> COALESCE(:bg_no, '')"
	        + " OR COALESCE(date_of_expiry, '') <> COALESCE(:date_of_expiry, '')"
	        + " OR COALESCE(date_of_ref, '') <> COALESCE(:date_of_ref, '')"
	        + " OR COALESCE(lc_issue_date, '') <> COALESCE(:lc_issue_date, '')"
	        + " OR COALESCE(s_t_exempted, '') <> COALESCE(:s_t_exempted, '') )",  nativeQuery = true)
	int updateInvoicedbtcrdInfo(String product_desc, String remarks, String date_of_notification,
			String date_val, String bg_type, String date_of_issue, String reference_no, String lc_number,
			String supply_place, String lr_docketno, String bg_no, String date_of_expiry, String date_of_ref,
			String lc_issue_date, String modified_by,String s_t_exempted, int id);
	
	@Query(value="select TOP 1 con_id,is_release from DEBIT_CREDIT_INVOICE_MASTER WHERE id = :id and factory_id = :factory_id", nativeQuery = true)
	long getContractIdFromdbtcrdInvoiceMaster(int id, String factory_id);
	
	@Query(value = "select invoice_series from SERIES_MASTER where debitcredit_type= :note_type and is_debitCredit =1 and (status is NULL or status ='Open') and \r\n"
			+ "state_id = (select bu.state_id from DEBIT_CREDIT_INVOICE_MASTER im\r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = im.con_id\r\n"
			+ "inner join business_units bu on bu.business_unit_id = cm.bid\r\n"
			+ "inner join STATE_MASTER sm on sm.id = bu.state_id where im.id = :id)", nativeQuery = true)
	Optional<Long> getSeriesNumberbasedOnId(int id, String note_type);
	
	/*
	 * @Query(value =
	 * "	select invoice_series from SERIES_MASTER where debitcredit_type= :note_type and is_debitCredit =1 and (status is NULL or status ='Open') and \r\n"
	 * +
	 * "			state_id = (select im.Invoice_to_id from DEBIT_CREDIT_INVOICE_MASTER im\r\n"
	 * +
	 * "			inner join INVOICE_CONSIGNEE_ADDRESS_MASTER cm on cm.id = im.Invoice_to_id\r\n"
	 * +
	 * "			inner join STATE_MASTER sm on sm.state_id = cm.state_id where im.id = :id)"
	 * , nativeQuery = true) Optional<Long> getSeriesNumberbasedOnIdscrap(int id,
	 * String note_type);
	 */
	@Query(value = "select invoice_series from SERIES_MASTER where debitcredit_type= :note_type and is_debitCredit =1 and (status is NULL or status ='Open') and \r\n"
	        + "    state_id = (select cm.state_id from DEBIT_CREDIT_INVOICE_MASTER im\r\n"
	        + "    inner join INVOICE_CONSIGNEE_ADDRESS_MASTER cm on cm.id = im.Invoice_to_id\r\n"
	        + "    where im.id = :id)", nativeQuery = true)
	Optional<Long> getSeriesNumberbasedOnIdscrap(int id, String note_type);
	
	@Query(value = "SELECT MAX(invoice_no) AS highest_invoice_no FROM (\r\n"
			+ "    SELECT invoice_no FROM DEBIT_CREDIT_INVOICE_MASTER WHERE invoice_no like %?1%\r\n"
			+ "    UNION ALL\r\n"
			+ "    SELECT invoice_no FROM SCRAP_INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
			+ "	UNION ALL\r\n"
			+ "	SELECT invoice_no FROM DEBIT_CREDIT_INVOICE_MASTER WHERE invoice_no like %?1% UNION ALL\r\n"
			+ "	SELECT invoice_no FROM Others_invoice_master WHERE invoice_no like %?1% \r\n"
			+ ") AS CombinedInvoices", nativeQuery = true)
	Optional<Long> getdbtcrdInvoiceNumber(String serieNumber);
	
	@Query(value ="select TOP 1 pn_id from OPENING_BALANCE where con_id = :contact_id order by created_date desc", nativeQuery = true)
	String getLastInseretedOpeningBalancePrimaryId(long contact_id);
	
	@Query(value ="select avl_bal from OPENING_BALANCE_ITEM where pn_id = :pn_idvalue order by created_date desc", nativeQuery = true)
	Optional<Float> getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(String pn_idvalue);
	
	@Query(value = "select sum(tm.tax_per) as taxper from TAX_MASTER tm\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.tax_id = tm.tax_id\r\n"
			+ "where cat.contract_id = :contact_id", nativeQuery = true)
	float getTaxPercentageDetailsAssignedToContractor(long contact_id);
	
	
	@Transactional
	@Modifying
	@Query(value ="update OPENING_BALANCE_ITEM set avl_bal = :avl_bal, modified_by =:verified_by, modified_date = GETDATE()  where pn_id = :pn_id ", nativeQuery = true)
	void reduceOpeningBalanceBasedOnOpenTaxable(String pn_id, float avl_bal, String verified_by);
	
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE DEBIT_CREDIT_INVOICE_MASTER set non_tax_adv = :non_tax_adv, tax_adv = :tax_adv, grand_total =:total,payable_by_customer = :payable_by_customer, payable_to_dept = :payable_to_dept,open_tax_adv =:open_tax_adv, open_non_tax_adv = :open_non_tax_adv, gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateInvoiceVerificationDetails(String non_tax_adv, String tax_adv, String total, String payable_by_customer,
			String payable_to_dept, String open_tax_adv, String open_non_tax_adv, String gst_remarks,
			String verified_by, String invoice_no, int id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE DEBIT_CREDIT_INVOICE_MASTER set non_tax_adv = :non_tax_adv, tax_adv = :tax_adv, grand_total =:total,payable_by_customer = :payable_by_customer, payable_to_dept = :payable_to_dept,open_tax_adv =:open_tax_adv, open_non_tax_adv = :open_non_tax_adv, gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateInvoiceVerificationDetails_is_release(String non_tax_adv, String tax_adv, String total, String payable_by_customer,
			String payable_to_dept, String open_tax_adv, String open_non_tax_adv, String gst_remarks,
			String verified_by, int id);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_TAXENTRY_DETAILS(tax_id,tax_per,tax_value,adv_tax,tax_payable_by_customer,tax_payable_to_dept,invoice_id,created_by,created_date) VALUES (:tax_id,:tax_per,:tax_value,:adv_tax,:tax_payable_by_customer,:tax_payable_to_dept,:invoice_id,:created_by,GETDATE())", nativeQuery = true)
	void insertINVOICE_TAXENTRY_DETAILSNew(String tax_id, String tax_per, String tax_value, String adv_tax,
			String tax_payable_by_customer, String tax_payable_to_dept,String invoice_id,String created_by);
	
	@Transactional
	@Modifying
	@Query(value ="UPDATE DEBIT_CREDIT_INVOICE_MASTER set is_release = 1,released_by =:released_by,released_datetime = GETDATE() where id = :id", nativeQuery = true)
	int UpdateReleaseById(String id, String released_by);
	
	
	
	@Query(value ="select open_tax_adv from DEBIT_CREDIT_INVOICE_MASTER where id = :id", nativeQuery = true)
	String getRecoveryAmountfromInvoice(String id);

	@Query(value ="select open_non_tax_adv from DEBIT_CREDIT_INVOICE_MASTER where id = :id", nativeQuery = true)
	String getOpenNonTaxAdvFromInvoice(String id);
	
	@Query(value = "select im.*,bg.description as bgDescription from DEBIT_CREDIT_INVOICE_MASTER im\r\n"
			+ "						left join BGTYPE_MASTER bg on bg.bgid = im.bg_type\r\n"
			+ "			 WHERE id = :id", nativeQuery = true)
	Debitcreditinvoiceinterface listdbtcrdSearchByIdscrap(String id);
	
	
	@Query(value = "select distinct cm.*,concat(dbo.NumberToRupeesWords(cm.grand_total),' Only') as grand_total_word,concat(dbo.NumberToRupeesWords(cm.tax1_value),' Only') as tax1_value_word,concat(dbo.NumberToRupeesWords(cm.tax2_value),' Only') as tax2_value_word,concat(dbo.NumberToRupeesWords(cm.tax3_value),' Only') as tax3_value_word,icam.address as invoice_to_id_value,icam1.address as consignee_id_value,shd.shipment_mode as shipment_mode_id_value,\r\n"
			+ "					shdd.delivery_condition as delivery_condition_id_value,bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value,om.org_name as reg_office_id_value,\r\n"
			+ "					sm.service_code as s_code_value,sms.service_code as h_code_value,bu.business_unit_name,org.org_name,etm.export_title,\r\n"
			+ "					icam.gst_no as consigneeGstNo, icam.pan_no as consigneePanNo,icam.district as consigneeDistrict,icam.pin_no as consigneePinNo,\r\n"
			+ "					stm.state_id as consigneestatecode,stm.state_name as consigneestateName,cum.country_name as consigneeCountryName ,icam1.gst_no as invoiceGstNo, icam1.pan_no as invoicePanNo,stm1.state_id as invoiceStateCode,icam1.pin_no as invoicePinNo,icam1.district as invoiceDistrict,\r\n"
			+ "					stm1.state_name as invoiceStateName, cum1.country_name as invoiceCountryName,bm.city as bankcityname,stmbm.state_code as bankstatecode, stmbm.state_name as bankstatename, cmbm.country_code as bankcountrycode,\r\n"
			+ "					cmbm.country_name as bankcountryname,bm.bank_name,bm.account_number, bm.branch_address, bm.branch_code, bm.ifsc_code, bm.swift_code, TAX_MASTER_1.tax_name AS tax_1, dbo.TAX_MASTER.tax_name AS tax_2, TAX_MASTER_2.tax_name AS tax_3 FROM  dbo.EXPORT_TITLE_MASTER AS etm RIGHT OUTER JOIN\r\n"
			+ "			                       dbo.TAX_MASTER RIGHT OUTER JOIN\r\n"
			+ "			                       dbo.DEBIT_CREDIT_INVOICE_MASTER AS cm INNER JOIN\r\n"
			+ "			                       dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS icam ON icam.id = cm.consignee_id INNER JOIN\r\n"
			+ "			                       dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS icam1 ON icam1.id = cm.Invoice_to_id INNER JOIN\r\n"
			+ "			                       dbo.SHIPMENT_DELIVERY_CONDITION AS shd ON shd.si_id = cm.ship_mode_id INNER JOIN\r\n"
			+ "			                       dbo.SHIPMENT_DELIVERY_CONDITION AS shdd ON shdd.si_id = cm.deliverycondition_id INNER JOIN\r\n"
			+ "			                       dbo.WORKORDER_MASTER AS wm ON wm.work_id = cm.workorder_id INNER JOIN\r\n"
			+ "			                       dbo.BANK_MASTER AS bm ON bm.account_id = cm.bank_id INNER JOIN\r\n"
			+ "			                       dbo.SERVICECODE_MASTER AS sms ON sms.servicecode_id = cm.HSNcode_id INNER JOIN\r\n"
			+ "			                       dbo.SERVICECODE_MASTER AS sm ON sm.servicecode_id = cm.servicecode_id INNER JOIN\r\n"
			+ "			                       dbo.ORGANIZATION_MASTER AS om ON om.org_id = cm.registered_office_id INNER JOIN\r\n"
			+ "			                       dbo.BUSINESS_UNITS AS bu ON bu.business_unit_id = cm.Business_Unit_id INNER JOIN\r\n"
			+ "			                       dbo.STATE_MASTER AS stm ON stm.id = icam.state_id INNER JOIN\r\n"
			+ "			                       dbo.STATE_MASTER AS stm1 ON stm1.id = icam1.state_id INNER JOIN\r\n"
			+ "			                       dbo.STATE_MASTER AS stmbm ON stmbm.id = bm.state_id INNER JOIN\r\n"
			+ "			                       dbo.COUNTRY_MASTER AS cmbm ON cmbm.id = bm.country_id INNER JOIN\r\n"
			+ "			                       dbo.COUNTRY_MASTER AS cum ON cum.id = icam.country_id INNER JOIN\r\n"
			+ "			                       dbo.COUNTRY_MASTER AS cum1 ON cum1.id = icam1.country_id"
			+ "                                                                                   INNER JOIN \r\n"
			+ "										   ORGANIZATION_MASTER as org on  org.org_id = bu.org_id LEFT OUTER JOIN\r\n"
			+ "			                       dbo.TAX_MASTER AS TAX_MASTER_1 ON cm.tax1 = TAX_MASTER_1.tax_id ON dbo.TAX_MASTER.tax_id = cm.tax2 LEFT OUTER JOIN\r\n"
			+ "			                       dbo.TAX_MASTER AS TAX_MASTER_2 ON cm.tax3 = TAX_MASTER_2.tax_id ON etm.id = cm.Export_title_text_id where cm.load_id =  :load_id and cm.factory_id =:factory_id", nativeQuery = true)
	List<Debitcreditinvoiceinterface> searchdbtcrdContractNewscrap(String load_id, String factory_id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE DEBIT_CREDIT_INVOICE_MASTER set gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE(), is_release = 0 ,grand_total =:grand_total ,Total_tax=:total_tax,net_amount=:net_amount,tax1_value=:tax1_value,tax2_value=:tax2_value,tax3_value=:tax3_value where id  = :id", nativeQuery = true)
	int updatedbtcrdInvoiceVerificationDetailsscrap( String gst_remarks,
			String verified_by, String invoice_no ,Double grand_total,Double total_tax,Double net_amount,Double tax1_value,Double tax2_value,Double tax3_value, int id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE DEBIT_CREDIT_INVOICE_MASTER set gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,verified_date = GETDATE(), is_release = 0 ,grand_total =:grand_total ,Total_tax=:total_tax,net_amount=:net_amount,tax1_value=:tax1_value,tax2_value=:tax2_value,tax3_value=:tax3_value where id  = :id", nativeQuery = true)
	int updatedbtcrdInvoiceVerificationDetailsscrap_is_release( String gst_remarks,
			String verified_by,Double grand_total,Double total_tax,Double net_amount,Double tax1_value,Double tax2_value,Double tax3_value, int id);
	
	@Transactional
	@Modifying
	@Query(value ="UPDATE DEBIT_CREDIT_INVOICE_MASTER set is_release = 1,released_by =:released_by,released_datetime = GETDATE() where id = :id", nativeQuery = true)
	int dbtcrdUpdateReleaseByIdscrap(String id, String released_by);
	
	@Transactional
	@Modifying
	@Query(
	    value = "UPDATE DEBIT_CREDIT_INVOICE_MASTER " +
	            "SET Cancel = 1, " +
	            "    Cancel_by = :cancelled_by, " +
	            "    Cancel_date = GETDATE() " +
	            "WHERE id = :id",
	    nativeQuery = true
	)
	int dbtcrdCancelById(
	         String id,
	         String cancelled_by
	);

	@Query(value="select TOP 1 is_release from DEBIT_CREDIT_INVOICE_MASTER WHERE id = :id ", nativeQuery = true)
	long getContractIdFromInvoiceMaster(String id);
	
	@Query(value="select is_release from DEBIT_CREDIT_INVOICE_MASTER WHERE id = :id", nativeQuery = true)
	Boolean  getIsReleaseFromdbtcrdInvoiceMaster(int id);
	
	@Query(value = "SELECT COUNT(*) FROM DEBITCREDIT_PACKINGNOTE WHERE con_id = :contract_id AND filepath LIKE %:filename%", nativeQuery = true)
	int checkFileExistsForContract(@Param("contract_id") String contract_id,
	                               @Param("filename") String filename);
}
