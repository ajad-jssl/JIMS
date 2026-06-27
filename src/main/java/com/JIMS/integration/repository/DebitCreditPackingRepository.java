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
import com.JIMS.integration.interfaces.ContractorDetailsInterface;
import com.JIMS.integration.interfaces.DebitCreditPackNoteIteminterface;
import com.JIMS.integration.interfaces.DebitCreditPackNoteinterface;
import com.JIMS.integration.interfaces.ListAssignMilesonetoContractors;
import com.JIMS.integration.interfaces.ScrapDebitCreditInvoiceInterface;
import com.JIMS.integration.interfaces.dbtcrdpackingitemsscrap_interface;

import jakarta.transaction.Transactional;

public interface DebitCreditPackingRepository extends JpaRepository<User, Integer> {

	@Query(value = "select top 1 dcPn_no from DEBITCREDIT_PACKINGNOTE where note_type like :note_type order by dcPn_no DESC", nativeQuery = true)
	String findLastDcLoad(String note_type);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DEBITCREDIT_PACKINGNOTE (dcPn_no, invoice_type_id, con_id, milestone_id, note_type, freight, filepath, factory_id, created_by, created_date, totalpn_amount, is_locked) VALUES "
			+ "(:newDCLoad, :invoice_type_id, 'SCRAP', 'SCRAP', :note_type, :freight, :uniqueFileName, :factory_id, :created_by, GETDATE(), :totalpn_amount,0)", nativeQuery = true)
	int insertIntoDCPackingNote(String newDCLoad, String invoice_type_id,
			String note_type, String freight, String uniqueFileName, String factory_id, String created_by,
			String totalpn_amount);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DEBITCREDIT_PACKINGNOTE (dcPn_no, invoice_type_id, con_id, milestone_id, note_type, freight, filepath, factory_id, created_by, created_date, totalpn_amount, is_locked) VALUES "
			+ "(:newDCLoad, :invoice_type_id, :con_id, :milestone_id, :note_type, :freight, :uniqueFileName, :factory_id, :created_by, GETDATE(), :totalpn_amount,0)", nativeQuery = true)
	int insertIntoDCPackingNoteSteel(String newDCLoad, String invoice_type_id, String con_id, String milestone_id,
			String note_type, String freight, String uniqueFileName, String factory_id, String created_by,
			String totalpn_amount);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DEBITCREDIT_PACKINGNOTE_ITEMS (dc_pn_id, note_type, dcPn_no, uom_id, type_id, quantities, kgs, unit_price, total, remarkstype_id, created_by, created_date) "
			+ "values (:dc_pn_id, :note_type,:newDCLoad ,:uom_id, :type_id, :quantity, :kgs, :unit_price, :total, :remarkstype_id, :created_by, GETDATE())", nativeQuery = true)
	int insertDCPackingNoteItemDetails(String dc_pn_id, String note_type, String newDCLoad, String uom_id, String type_id,
			String quantity, String kgs, String unit_price, String total, String remarkstype_id, String created_by);

	@Query(value = "SELECT dc_pn_id from DEBITCREDIT_PACKINGNOTE where dcPn_no= :newDCLoad", nativeQuery = true)
	String getDc_pnidFromPackingNote(String newDCLoad);

	@Transactional
	@Modifying
	@Query(value = "UPDATE DEBITCREDIT_PACKINGNOTE set freight = :freight, filepath = :uniqueFileName,modified_by = :modified_by, totalpn_amount = :totalpn_amount, \r\n"
			+ "modified_date = GETDATE() WHERE dcPn_no = :dcPn_no", nativeQuery = true)
	int updateIntoDCPackingNote(String freight, String uniqueFileName, String modified_by, String totalpn_amount,
			String dcPn_no);

	@Transactional
	@Modifying
	@Query(value = "UPDATE DEBITCREDIT_PACKINGNOTE_ITEMS SET uom_id = :uom_id, type_id = :type_id, quantities = :quantity, kgs = :kgs, "
			+ "unit_price = :unit_price, total = :total, remarkstype_id = :remarkstype_id, modified_by = :modified_by, "
			+ "modified_date = GETDATE() WHERE dc_pn_items_id = :dc_pn_items_id", nativeQuery = true)
	int updateDCPackingNoteItemDetails(String uom_id, String type_id, String quantity, String kgs, String unit_price, String total,
			String remarkstype_id, String modified_by, String dc_pn_items_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DEBITCREDIT_PACKINGNOTE_ITEMS (dc_pn_id, note_type, dcPn_no, uom_id, type_id, quantities, kgs, unit_price, total, remarkstype_id, modified_by, modified_date) "
			+ "values (:dc_pn_id, :note_type,:dcPn_no ,:uom_id, :type_id, :quantity, :kgs, :unit_price, :total, :remarkstype_id, :modified_by, GETDATE())", nativeQuery = true)
	int insertDCPackingNoteItemDetailsDuringUpdate(String dc_pn_id, String note_type, String dcPn_no, String uom_id,String type_id,
			String quantity, String kgs, String unit_price, String total, String remarkstype_id, String modified_by);

	@Query(value = "select dcpn.* , cm.* ,itm.type as type,(milestone_code +'-'+milestone_desc) as milestone from DEBITCREDIT_PACKINGNOTE dcpn\r\n"
			+ "			left join CONTRACT_MASTER cm on cm.contract_id = dcpn.con_id\r\n"
			+ "inner join INVOICE_TYPE_MASTER itm on itm.type_no = dcpn.invoice_type_id\r\n"
			+ "inner join MILESTONE_MASTER mm on dcpn.milestone_id=mm.milestone_id\r\n"
			+ "where dcpn.dcPn_no = :dcPackNoteno", nativeQuery = true)
	DebitCreditPackNoteinterface getPackingNoteInfo(String dcPackNoteno);
	
	@Query(value = "select dcpn.*,itm.type as type from DEBITCREDIT_PACKINGNOTE dcpn\r\n"
			+ "inner join INVOICE_TYPE_MASTER itm on itm.type_no = dcpn.invoice_type_id\r\n"
			+ "where dcpn.dcPn_no = :dcPackNoteno and dcpn.con_id='SCRAP'", nativeQuery = true)
	DebitCreditPackNoteinterface getPackingNotescrapInfo(String dcPackNoteno);

	@Query(value = "select dcpn.*,case when dcpn.is_locked=0 then 'INVOICE NOT GENERATED' else 'INVOICE GENERTATED' END AS INVGEN from DEBITCREDIT_PACKINGNOTE dcpn\r\n" + //
			"inner join INVOICE_TYPE_MASTER itm on itm.type_no = dcpn.invoice_type_id\r\n" + //
			"where dcpn.con_id = 'SCRAP' AND  dcpn.factory_id= :factory_id and (dcpn.Cancel=0 or dcpn.Cancel is null)", nativeQuery = true)
	List<DebitCreditPackNoteinterface> getAllScrapPackingNoteDetails(String factory_id);

	@Query(value = "select dcpn.*, cm.* ,case when dcpn.is_locked=0 then 'INOVICE NOT GENERATED' else 'INVOICE GENERTATED' END AS INVGEN from DEBITCREDIT_PACKINGNOTE dcpn\r\n"
			+ "			left join CONTRACT_MASTER cm on  CAST(cm.contract_id AS NVARCHAR) = dcpn.con_id\r\n"
			+ "			inner join INVOICE_TYPE_MASTER itm on itm.type_no = dcpn.invoice_type_id\r\n"
			+ "where CAST(dcpn.con_id AS VARCHAR) NOT IN ('SCRAP') AND  dcpn.factory_id= ? and (dcpn.cancel = 0 OR dcpn.cancel IS NULL)", nativeQuery = true)
	List<DebitCreditPackNoteinterface> getAllSteelPackingNoteDetails(String factory_id);

	@Query(value = "select dcpni.*, um.unit_name, itrm.remarks_type,scm.scrap_name as scrap_name_type_id from DEBITCREDIT_PACKINGNOTE_ITEMS dcpni\r\n"
			+ "inner join UOM_MASTER um on um.unit_id =  dcpni.uom_id\r\n"
			+ "inner join INVOICE_TYPE_REMARKS_MASTER itrm on itrm.slno = dcpni.remarkstype_id\r\n"
			+ "inner join SCRAPTYPE_MASTER scm on dcpni.type_id=scm.type_id\r\n"
			+ "where dcpni.dcPn_no = :PnNo and dcpni.is_delete=0", nativeQuery = true)
	List<DebitCreditPackNoteIteminterface> getPackingNoteItemsDetails(String PnNo);
	
	
	
	
	// ✅ Query 1 - Paginated main query
		@Query(
				value = "SELECT dcpn.dc_pn_id, dcpn.dcPn_no, dcpn.note_type, dcpn.freight, " +
				        "dcpn.totalpn_amount, dcpn.invoice_type_id, dcpn.filepath, dcpn.con_id, " +
				        "cm.contract_name, " +
				        "CASE WHEN dcpn.is_locked = 1 THEN 'INVOICE GENERATED' " +
				        "ELSE 'INOVICE NOT GENERATED' END AS invgen " +

				        "FROM DEBITCREDIT_PACKINGNOTE dcpn " +
				        "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = dcpn.con_id " +

				        "WHERE CAST(dcpn.con_id AS VARCHAR) NOT IN ('SCRAP') " +
				        "AND dcpn.factory_id = :factory_id " +

				        "AND ( :search IS NULL OR :search = '' OR " +
				        "dcpn.dcPn_no LIKE CONCAT('%',:search,'%') OR " +
				        "cm.contract_name LIKE CONCAT('%',:search,'%') OR " +
				        "dcpn.note_type LIKE CONCAT('%',:search,'%') OR " +
				        "CAST(dcpn.freight AS VARCHAR) LIKE CONCAT('%',:search,'%') ) " +

				        "ORDER BY dcpn.dcPn_no DESC",

				countQuery = "SELECT COUNT(*) FROM DEBITCREDIT_PACKINGNOTE dcpn " +
				             "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = dcpn.con_id " +

				             "WHERE CAST(dcpn.con_id AS VARCHAR) NOT IN ('SCRAP') " +
				             "AND dcpn.factory_id = :factory_id " +

				             "AND ( :search IS NULL OR :search = '' OR " +
				             "dcpn.dcPn_no LIKE CONCAT('%',:search,'%') OR " +
				             "cm.contract_name LIKE CONCAT('%',:search,'%') OR " +
				             "dcpn.note_type LIKE CONCAT('%',:search,'%') OR " +
				             "CAST(dcpn.freight AS VARCHAR) LIKE CONCAT('%',:search,'%') )",

				nativeQuery = true
				)
				Page<DebitCreditPackNoteinterface> getAllSteelPackingNoteDetailsPaged(
				        @Param("factory_id") String factory_id,
				        @Param("search") String search,
				        Pageable pageable
				);

		// ✅ Query 2 - Batch items query
		@Query(
		    value = "SELECT dcpni.*, um.unit_name, itrm.remarks_type, " +
		            "scm.scrap_name AS scrap_name_type_id " +
		            "FROM DEBITCREDIT_PACKINGNOTE_ITEMS dcpni " +
		            "INNER JOIN UOM_MASTER um ON um.unit_id = dcpni.uom_id " +
		            "INNER JOIN INVOICE_TYPE_REMARKS_MASTER itrm ON itrm.slno = dcpni.remarkstype_id " +
		            "INNER JOIN SCRAPTYPE_MASTER scm ON dcpni.type_id = scm.type_id " +
		            "WHERE dcpni.dcPn_no IN (:pnNos) " +
		            "AND dcpni.is_delete = 0",
		    nativeQuery = true
		)
		List<DebitCreditPackNoteIteminterface> getPackingNoteItemsDetailsBatch(
		        @Param("pnNos") List<String> pnNos
		);
		
	
		
		@Query(
			    value = "SELECT dcpn.dc_pn_id, dcpn.dcPn_no, dcpn.note_type, dcpn.freight, " +
			            "dcpn.totalpn_amount, dcpn.invoice_type_id, dcpn.filepath, dcpn.con_id, " +
			            "CASE WHEN dcpn.is_locked = 1 THEN 'INVOICE GENERATED' " +
			            "ELSE 'INOVICE NOT GENERATED' END AS invgen " +

			            "FROM DEBITCREDIT_PACKINGNOTE dcpn " +

			            "WHERE dcpn.con_id = 'SCRAP' " +
			            "AND dcpn.factory_id = :factory_id " +
			            "AND (dcpn.Cancel = 0 OR dcpn.Cancel IS NULL) " +

			            "AND ( :search IS NULL OR :search = '' OR " +
			            "dcpn.dcPn_no LIKE CONCAT('%',:search,'%') OR " +
			            "dcpn.note_type LIKE CONCAT('%',:search,'%') OR " +
			            "CAST(dcpn.freight AS VARCHAR) LIKE CONCAT('%',:search,'%') ) " +

			            "ORDER BY dcpn.dcPn_no DESC",

			    countQuery = "SELECT COUNT(*) FROM DEBITCREDIT_PACKINGNOTE dcpn " +
			                 "WHERE dcpn.con_id = 'SCRAP' " +
			                 "AND dcpn.factory_id = :factory_id " +
			                 "AND (dcpn.Cancel = 0 OR dcpn.Cancel IS NULL) " +

			                 "AND ( :search IS NULL OR :search = '' OR " +
			                 "dcpn.dcPn_no LIKE CONCAT('%',:search,'%') OR " +
			                 "dcpn.note_type LIKE CONCAT('%',:search,'%') OR " +
			                 "CAST(dcpn.freight AS VARCHAR) LIKE CONCAT('%',:search,'%') )",

			    nativeQuery = true
			)
			Page<DebitCreditPackNoteinterface> getAllScrapPackingNoteDetailsPaged(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);

	@Query(value = "select dcpni.*, um.unit_name, itrm.remarks_type from DEBITCREDIT_PACKINGNOTE_ITEMS dcpni\r\n"
			+ "inner join UOM_MASTER um on um.unit_id =  dcpni.uom_id\r\n"
			+ "inner join INVOICE_TYPE_REMARKS_MASTER itrm on itrm.slno = dcpni.remarkstype_id\r\n"
			+ "where dcpni.dcPn_no = :dcPackNoteno", nativeQuery = true)
	List<DebitCreditPackNoteIteminterface> getPackingNoteItemsDetailsDuringINvoice(String dcPackNoteno);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DEBIT_CREDIT_INVOICE_MASTER (invoice_type_id, note_type, load_id, grand_total, product_desc, remarks, business_unit_id, invoice_to, consignee_id, bank_id, shipmentmode, delivery_condition, workorder_id, service_code, hsn_code, is_export, vendor_id, created_by, created_date, factory_id, verified_status, is_release) VALUES (:invoice_type_id, :note_type, :load_id, :tax1, :tax1_per, :tax1_value, :tax2, :tax2_per, :tax2_value, :tax3, :tax3_per, :tax3_value, :total_tax, :grand_total, :product_desc, :remarks, :business_unit_id, :invoice_to, :consignee_id, :bank_id, :shipmentmode, :delivery_condition, :workorder_id, :service_code, :hsn_code, :is_export, :vendor_id, :created_by, GETDATE(), :factory_id, 'NOT VERIFIED', '0');\r\n"
			+ //
			"", nativeQuery = true)
	int addDebitCreditInvoiceForScrap(String invoice_type_id, String note_type, String load_id, String grand_total, String product_desc,
			String remarks,
			String business_unit_id, String invoice_to, String consignee_id, String bank_id, String shipmentmode,
			String delivery_condition, String workorder_id, String service_code, String hsn_code, String is_export,
			String vendor_id, String created_by, String factory_id);

	@Transactional
	@Modifying
	@Query(value = """
    INSERT INTO DEBIT_CREDIT_INVOICE_MASTER (
        invoice_type_id, con_id, contract_name, note_type, load_id, grand_total, product_desc, 
        date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number,
        supply_place, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, 
        s_t_exempted, remarks, created_by, created_date, factory_id, verified_status
    ) 
    VALUES (
        :invoice_type_id, :con_id, :contract_name, :note_type, :load_id, :grand_total, :product_desc,
        :date_of_notification, :date_val, :bg_type, :date_of_issue, :reference_no, :lc_number,
        :supply_place, :lr_docketno, :bg_no, :date_of_expiry, :date_of_ref, :lc_issue_date,
        :s_t_exempted, :remarks, :created_by, GETDATE(), :factory_id, 'NOT VERIFIED'
    )
""", nativeQuery = true)
	int addDebitCreditInvoiceForSteel(String invoice_type_id, String con_id, String contract_name ,String note_type, String load_id,
			String grand_total, String product_desc, String date_of_notification,
			String date_val, String bg_type, String date_of_issue, String reference_no, String lc_number,
			String supply_place, String lr_docketno, String bg_no, String date_of_expiry, String date_of_ref,
			String lc_issue_date,String s_t_exempted,
			String remarks,String created_by, String factory_id);

	@Query(value = "SELECT filepath from DEBITCREDIT_PACKINGNOTE where dc_pn_id = :pn_id", nativeQuery = true)
	String getFilePath(String pn_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE DEBIT_CREDIT_INVOICE_MASTER SET tax1 = :tax1, tax1_per = :tax1_per, tax1_value = :tax1_value, tax2 = :tax2, tax2_per = :tax2_per, tax2_value = :tax2_value, tax3 = :tax3, tax3_per = :tax3_per, tax3_value = :tax3_value, total_tax = :total_tax, grand_total = :grand_total, product_desc = :product_desc, remarks = :remarks, business_unit_id = :business_unit_id, invoice_to = :invoice_to, consignee_id = :consignee_id, bank_id = :bank_id, shipmentmode = :shipmentmode, delivery_condition = :delivery_condition, workorder_id = :workorder_id, service_code = :service_code, hsn_code = :hsn_code, is_export = :is_export, vendor_id = :vendor_id, modified_by = :modified_by, modified_date = GETDATE() WHERE id = :invoice_id", nativeQuery = true)
	int updateDebitCreditInvoiceForScrap(String tax1, String tax1_per, String tax1_value, String tax2, String tax2_per,
			String tax2_value, String tax3, String tax3_per, String tax3_value, String total_tax, String grand_total,
			String product_desc,
			String remarks, String business_unit_id, String invoice_to, String consignee_id, String bank_id,
			String shipmentmode, String delivery_condition, String workorder_id, String service_code, String hsn_code,
			String is_export, String vendor_id, String modified_by, String invoice_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE DEBIT_CREDIT_INVOICE_MASTER SET tax1 = :tax1, tax1_per = :tax1_per, tax1_value = :tax1_value, tax2 = :tax2, tax2_per = :tax2_per, tax2_value = :tax2_value, tax3 = :tax3, tax3_per = :tax3_per, tax3_value = :tax3_value, total_tax = :total_tax, grand_total = :grand_total, product_desc = :product_desc, remarks = :remarks, business_unit_id = :business_unit_id, invoice_to = :invoice_to, consignee_id = :consignee_id, bank_id = :bank_id, shipmentmode = :shipmentmode, delivery_condition = :delivery_condition, workorder_id = :workorder_id, service_code = :service_code, hsn_code = :hsn_code, is_export = :is_export, modified_by = :modified_by, modified_date = GETDATE() WHERE id = :invoice_id", nativeQuery = true)
	int updateDebitCreditInvoiceForSteel(String tax1, String tax1_per, String tax1_value, String tax2, String tax2_per,
			String tax2_value, String tax3, String tax3_per, String tax3_value, String total_tax, String grand_total,
			String product_desc,
			String remarks, String business_unit_id, String invoice_to, String consignee_id, String bank_id,
			String shipmentmode, String delivery_condition, String workorder_id, String service_code, String hsn_code,
			String is_export, String modified_by, String invoice_id);

	@Query(value = "SELECT dcim.id, dcim.invoice_type_id, dcim.note_type, dcim.invoice_no,  dcim.load_id, dcim.tax1, dcim.tax1_per, \r\n"
			+ //
			"dcim.tax1_value, dcim.tax2, dcim.tax2_per, dcim.tax2_value, dcim.tax3, dcim.tax3_per, dcim.tax3_value, \r\n"
			+ //
			"dcim.grand_total, dcim.product_desc, dcim.remarks, dcim.business_unit_id, dcim.invoice_to, \r\n" + //
			"dcim.consignee_id, dcim.bank_id, dcim.shipmentmode, dcim.delivery_condition, dcim.workorder_id, \r\n" + //
			"dcim.service_code, dcim.hsn_code, dcim.is_export, dcim.vendor_id, dcim.created_by, dcim.created_date, \r\n"
			+ //
			"dcim.modified_by, dcim.modified_date, dcim.is_delete, dcim.factory_id, bu.business_unit_name, \r\n" + //
			"bm.account_number, bm.bank_name, bm.branch_address, bm.ifsc_code, bm.branch_code, bm.state_id, \r\n" + //
			"bm.city, bm.swift_code, conadd.address AS consignee_address, invadd.address AS invoice_address, \r\n" + //
			"sm.state_name AS bank_state_name, \r\n" + //
			"SUM(CAST(dcim.tax1_value AS DECIMAL(18, 2)) + CAST(dcim.tax2_value AS DECIMAL(18, 2)) + CAST(dcim.tax3_value AS DECIMAL(18, 2))) AS total_tax \r\n"
			+ //
			"FROM DEBIT_CREDIT_INVOICE_MASTER dcim \r\n" + //
			"INNER JOIN BUSINESS_UNITS bu ON bu.business_unit_id = dcim.business_unit_id \r\n" + //
			"INNER JOIN BANK_MASTER bm ON bm.account_id = dcim.bank_id \r\n" + //
			"INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER conadd ON conadd.id = dcim.consignee_id \r\n" + //
			"INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER invadd ON invadd.id = dcim.invoice_to \r\n" + //
			"INNER JOIN WORKORDER_MASTER wom ON wom.work_id = dcim.workorder_id \r\n" + //
			"INNER JOIN SCRAPVENDOR_MASTER svm ON svm.ven_id = dcim.vendor_id \r\n" + //
			"LEFT JOIN STATE_MASTER sm ON sm.state_id = bm.state_id WHERE dcim.id = 2 \r\n" + //
			"GROUP BY dcim.id, dcim.invoice_type_id, dcim.note_type,dcim.invoice_no, dcim.load_id, dcim.tax1, dcim.tax1_per, \r\n"
			+ //
			"dcim.tax1_value, dcim.tax2, dcim.tax2_per, dcim.tax2_value, dcim.tax3, dcim.tax3_per, dcim.tax3_value, \r\n"
			+ //
			"dcim.grand_total, dcim.product_desc, dcim.remarks, dcim.business_unit_id, dcim.invoice_to, dcim.consignee_id, \r\n"
			+ //
			"dcim.bank_id, dcim.shipmentmode, dcim.delivery_condition, dcim.workorder_id, dcim.service_code, \r\n" + //
			"dcim.hsn_code, dcim.is_export, dcim.vendor_id, dcim.created_by, dcim.created_date, dcim.modified_by, \r\n"
			+ //
			"dcim.modified_date, dcim.is_delete, dcim.factory_id, bu.business_unit_name, bm.account_number, \r\n" + //
			"bm.bank_name, bm.branch_address, bm.ifsc_code, bm.branch_code, bm.state_id, bm.city, bm.swift_code, \r\n" + //
			"conadd.address, invadd.address, sm.state_name", nativeQuery = true)

	ScrapDebitCreditInvoiceInterface getInvoiceDetails(String invoice_id);

	@Query(value = "SELECT load_id from DEBIT_CREDIT_INVOICE_MASTER where id = :invoice_id", nativeQuery = true)
	String getPackingNoteNo(String invoice_id);

	@Query(value = "SELECT id, CASE WHEN invoice_no IS NULL OR LTRIM(RTRIM(invoice_no)) = '' THEN 'YET TO GENERATE' ELSE \r\n"
			+ //
			"invoice_no END AS invoice_no,  load_id, verified_status,   CAST(created_date AS DATE) as invoice_generated_date FROM DEBIT_CREDIT_INVOICE_MASTER WHERE\r\n"
			+ //
			"factory_id = :factory_id", nativeQuery = true)
	List<ScrapDebitCreditInvoiceInterface> getAllInvoiceDetails(String factory_id);

	@Query(value = "SELECT dcPn_no from DEBITCREDIT_PACKINGNOTE where con_id = 'SCRAP' AND factory_id = :factory_id", nativeQuery = true)
	List<String> getAllPackingNoteNumberForScrap(String factory_id);

	@Query(value = "SELECT dcPn_no from DEBITCREDIT_PACKINGNOTE where CAST(con_id AS VARCHAR) NOT IN ('SCRAP') AND factory_id= :factory_id", nativeQuery = true)
	List<String> getAllPackingNoteNumberForSteel(String factory_id);

	@Query(value = "select distinct cm.contract_name, cm.contract_id from DEBITCREDIT_PACKINGNOTE dcpn \r\n" + //
			"inner join CONTRACT_MASTER cm on cm.contract_id = dcpn.con_id \r\n" + //
			"where CAST(dcpn.con_id AS VARCHAR) NOT IN ('SCRAP') AND dcpn.factory_id = :factory_id", nativeQuery = true)
	List<ContractorDetailsInterface> getlistContractorfromQs(int factory_id);

	@Query(value =  " SELECT        mac.dcPn_no\r\n"
			+ "FROM            dbo.DEBITCREDIT_PACKINGNOTE AS mac INNER JOIN\r\n"
			+ "                         dbo.CONTRACT_MASTER AS cm ON cm.contract_id = mac.con_id LEFT OUTER JOIN\r\n"
			+ "                         dbo.DEBIT_CREDIT_INVOICE_MASTER ON mac.dcPn_no = dbo.DEBIT_CREDIT_INVOICE_MASTER.load_id\r\n"
			+" where mac.factory_id = :factory_id and mac.con_id = :con_id and mac.note_type= :note_type and mac.con_id != 'SCRAP' and mac.dcPn_no not in (select load_id from DEBIT_CREDIT_INVOICE_MASTER)", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistloadContractorfromQs(int factory_id, String con_id, String note_type);
	
	@Query(value = " SELECT        mac.dcPn_no\r\n"
			+ "FROM            dbo.DEBITCREDIT_PACKINGNOTE AS mac LEFT OUTER JOIN\r\n"
			+ "                         dbo.DEBIT_CREDIT_INVOICE_MASTER ON mac.dcPn_no = dbo.DEBIT_CREDIT_INVOICE_MASTER.load_id    \r\n" + //
			"where mac.factory_id = :factory_id and mac.con_id = :con_id and mac.note_type= :note_type and  mac.dcPn_no not in (select load_id from DEBIT_CREDIT_INVOICE_MASTER) ", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getscraplistloadContractorfromQs(int factory_id, String con_id, String note_type);

	@Query(value = "SELECT MAX(invoice_no) AS highest_invoice_no FROM (\r\n"
			+ "    SELECT invoice_no FROM INVOICE_MASTER WHERE invoice_no like %?1%\r\n"
			+ "    UNION ALL\r\n"
			+ "    SELECT invoice_no FROM SCRAP_INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
			+ "	UNION ALL\r\n"
			+ "	SELECT invoice_no FROM DEBIT_CREDIT_INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
			+ ") AS CombinedInvoices", nativeQuery = true)
	Optional<Long> getMaxInvoiceNumberBasedOnSeriesNumber(String seriesNumberInString);

	@Query(value="select invoice_series from SERIES_MASTER where is_gst =1 and (status is NULL or status ='Open') and \r\n"
			+ "state_id = (select bu.state_id from DEBIT_CREDIT_INVOICE_MASTER im\r\n"
			+ "inner join business_units bu on bu.business_unit_id = im.business_unit_id\r\n"
			+ "inner join STATE_MASTER sm on sm.id = bu.state_id where im.id = :id)", nativeQuery = true)
	Optional<Long> getSeriesNumberbasedOnId(String id);
	
	@Query(value="select TOP 1 dcPn_no from DEBITCREDIT_PACKINGNOTE where con_id = :con_id and dcPn_no = :DcPn_no and factory_id = :factory_id", nativeQuery =  true)
	String getdcPn_noBasedOnContractanddcpno(String con_id, String DcPn_no, String factory_id);
	
	
	@Query(value = " select  qm.totalpn_amount,\r\n"
			+ "		qm.dc_pn_id,qm.con_id,qm.dcPn_no,qm.filepath,qm.freight,case when qm.con_id = 'SCRAP' THEN 'SCRAP' else cm.contract_name end as contract_name,\r\n"
			+ "			qim.quantities,qim.kgs,qim.unit_price,qim.total,qim.dc_pn_items_id,uom.unit_name as UOM_name,\r\n"
			+ "			uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,case when qm.con_id = 'SCRAP' THEN 'SCRAP' else mm.milestone_name end as milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name\r\n"
			+ "			from DEBITCREDIT_PACKINGNOTE qm  \r\n"
			+ "			inner join DEBITCREDIT_PACKINGNOTE_ITEMS qim on qim.dc_pn_id = qm.dc_pn_id  \r\n"
			+ "			inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
			+ "			inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
			+ "			left join CONTRACT_MASTER cm on cm.contract_id = TRY_CAST(qm.con_id AS INT)  \r\n"
			+ "			left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
			+ "			left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
			+ "			left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.remarkstype_id \r\n"
			+ "			left join MILESTONE_MASTER mm on mm.milestone_id = TRY_CAST(qm.milestone_id AS INT) where qim.dcPn_no = :DcPn_no and qim.is_delete = 0 and qm.is_delete = 0 and qm.factory_id = :factory_id ", nativeQuery = true)
List<DebitCreditPackNoteinterface> searchdcPackingByIdnew(String DcPn_no,String factory_id);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE DEBITCREDIT_PACKINGNOTE SET   is_locked = :locked where dc_pn_id = :pn_id", nativeQuery = true)
	void updateisLockindbtcrdPacking(int pn_id, String locked);
	
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE DEBITCREDIT_PACKINGNOTE_ITEMS SET  is_locked = :locked where dc_pn_id = :pn_id", nativeQuery = true)
	void updateisLockindbtcrdPackingItem(int pn_id, String locked );

	@Query(value="select TOP 1 dc_pn_id from DEBITCREDIT_PACKINGNOTE where dcPn_no = :load_id and factory_id = :factory_id", nativeQuery =  true)
	String getPnIdBasedOnothersLoad_idscrap(String load_id, String factory_id);
	
	
	@Query(value = "select  qm.totalpn_amount,qm.dc_pn_id,qm.con_id,qm.dcPn_no,qm.filepath,qm.freight,\r\n"
			+ "				qim.quantities,qim.kgs,qim.unit_price,qim.total,\r\n"
			+ "					   SUM(qim.quantities) AS total_qty,\r\n"
			+ "					SUM(qim.kgs) As total_kgs,qim.dc_pn_items_id,qim.type_id,uom.unit_name as UOM_name,\r\n"
			+ "				uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id\r\n"
			+ "				from DEBITCREDIT_PACKINGNOTE qm  \r\n"
			+ "				inner join DEBITCREDIT_PACKINGNOTE_ITEMS qim on qim.dc_pn_id = qm.dc_pn_id  \r\n"
			+ "				inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
			+ "				inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
			+ "				left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.type_id \r\n"
			+ "			 where qim.dc_pn_id = :pn_id and qim.is_delete = 0 and qm.con_id='SCRAP' and  qm.is_delete = 0 and qm.factory_id = :factory_id\r\n"
			+ "			 GROUP BY qm.totalpn_amount, qm.dc_pn_id, qm.con_id, qm.dcPn_no, qm.freight, qim.quantities, qim.kgs, qim.unit_price, qim.total, qim.dc_pn_items_id, qim.type_id, \r\n"
			+ "			   uom.unit_name, uom.unit_id, sm.scrap_name, sm.type_id, qm.filepath", nativeQuery = true)
	List<DebitCreditPackNoteinterface> searchothersPackingByIdnewscrap(String pn_id,String factory_id);

	@Query(value= "select TOP 1 dc_pn_id from DEBITCREDIT_PACKINGNOTE where dcPn_no = :load_id ", nativeQuery =  true)
	String getdbtcrdPnIdBasedOnContract_idscrap( String load_id);
	
	@Query(value = "select  qm.totalpn_amount,qm.dc_pn_id,qm.con_id,qm.dcPn_no,qm.filepath,qm.freight,\r\n"
			+ "			qim.quantities,qim.kgs,qim.unit_price,qim.total,\r\n"
			+ "				  SUM(qim.quantities) OVER (PARTITION BY qm.dc_pn_id) AS total_qty,\r\n"
			+ "    SUM(qim.kgs) OVER (PARTITION BY qm.dc_pn_id) AS total_kgs,qim.dc_pn_items_id,qim.type_id,uom.unit_name as UOM_name,\r\n"
			+ "			uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,it.remarks_type as invoice_remarks_type_name\r\n"
			+ "			from DEBITCREDIT_PACKINGNOTE qm \r\n"
			+ "			inner join DEBITCREDIT_PACKINGNOTE_ITEMS qim on qim.dc_pn_id = qm.dc_pn_id \r\n"
			+ "			inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id \r\n"
			+ "			inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id \r\n"
			+ "			left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.type_id\r\n"
			+ "		 where qim.dc_pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 and qm.factory_id = :factory_id\r\n"
			+ "		 GROUP BY qm.totalpn_amount, qm.dc_pn_id, qm.con_id, qm.dcPn_no, qm.freight, qim.quantities, qim.kgs, qim.unit_price, qim.total, qim.dc_pn_items_id, qim.type_id,\r\n"
			+ "		  uom.unit_name, uom.unit_id, sm.scrap_name, sm.type_id, it.remarks_type, qm.filepath", nativeQuery = true)
	List<dbtcrdpackingitemsscrap_interface> searchdbtcrdPackingByIdnewscrap(String pn_id,String factory_id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE DEBIT_CREDIT_INVOICE_MASTER \r\n"
			+ "SET product_desc = :product_desc, \r\n"
			+ "    remarks = :remarks, \r\n"
			+ "    date_of_notification = :date_of_notification,\r\n"
			+ "    date_val = :date_val, \r\n"
			+ "    bg_type = :bg_type, \r\n"
			+ "    date_of_issue = :date_of_issue, \r\n"
			+ "    reference_no = :reference_no,\r\n"
			+ "    lc_number = :lc_number, \r\n"
			+ "    supply_place = :supply_place, \r\n"
			+ "    lr_docketno = :lr_docketno, \r\n"
			+ "    bg_no = :bg_no,\r\n"
			+ "    date_of_expiry = :date_of_expiry, \r\n"
			+ "    date_of_ref = :date_of_ref, \r\n"
			+ "    lc_issue_date = :lc_issue_date,\r\n"
			+ "    modified_by = :modified_by, \r\n"
			+ "    modified_date = GETDATE(), \r\n"
			+ "    s_t_exempted = :s_t_exempted,\r\n"
			+ "    tax1 = :tax1,\r\n"
			+ "    tax1_per = :tax1_per,\r\n"
			+ "    tax1_value = :tax1_value,\r\n"
			+ "    tax2 = :tax2,\r\n"
			+ "    tax2_per = :tax2_per,\r\n"
			+ "    tax2_value = :tax2_value,\r\n"
			+ "    tax3 = :tax3,\r\n"
			+ "    tax3_per = :tax3_per,\r\n"
			+ "    tax3_value = :tax3_value,\r\n"
			+ "    grand_total = :grand_total\r\n"
			+ "WHERE id = :id \r\n"
			+ "  AND (\r\n"
			+ "    COALESCE(product_desc, '') <> COALESCE(:product_desc, '') OR\r\n"
			+ "    COALESCE(remarks, '') <> COALESCE(:remarks, '') OR\r\n"
			+ "    COALESCE(date_of_notification, '') <> COALESCE(:date_of_notification, '') OR\r\n"
			+ "    COALESCE(date_val, '') <> COALESCE(:date_val, '') OR\r\n"
			+ "    COALESCE(bg_type, '') <> COALESCE(:bg_type, '') OR\r\n"
			+ "    COALESCE(date_of_issue, '') <> COALESCE(:date_of_issue, '') OR\r\n"
			+ "    COALESCE(reference_no, '') <> COALESCE(:reference_no, '') OR\r\n"
			+ "    COALESCE(lc_number, '') <> COALESCE(:lc_number, '') OR\r\n"
			+ "    COALESCE(supply_place, '') <> COALESCE(:supply_place, '') OR\r\n"
			+ "    COALESCE(lr_docketno, '') <> COALESCE(:lr_docketno, '') OR\r\n"
			+ "    COALESCE(bg_no, '') <> COALESCE(:bg_no, '') OR\r\n"
			+ "    COALESCE(date_of_expiry, '') <> COALESCE(:date_of_expiry, '') OR\r\n"
			+ "    COALESCE(date_of_ref, '') <> COALESCE(:date_of_ref, '') OR\r\n"
			+ "    COALESCE(lc_issue_date, '') <> COALESCE(:lc_issue_date, '') OR\r\n"
			+ "    COALESCE(s_t_exempted, '') <> COALESCE(:s_t_exempted, '') OR\r\n"
			+ "    COALESCE(tax1, 0) <> COALESCE(:tax1, 0) OR\r\n"
			+ "    COALESCE(tax1_per, 0) <> COALESCE(:tax1_per, 0) OR\r\n"
			+ "    COALESCE(tax1_value, 0) <> COALESCE(:tax1_value, 0) OR\r\n"
			+ "    COALESCE(tax2, 0) <> COALESCE(:tax2, 0) OR\r\n"
			+ "    COALESCE(tax2_per, 0) <> COALESCE(:tax2_per, 0) OR\r\n"
			+ "    COALESCE(tax2_value, 0) <> COALESCE(:tax2_value, 0) OR\r\n"
			+ "    COALESCE(tax3, 0) <> COALESCE(:tax3, 0) OR\r\n"
			+ "    COALESCE(tax3_per, 0) <> COALESCE(:tax3_per, 0) OR\r\n"
			+ "    COALESCE(tax3_value, 0) <> COALESCE(:tax3_value, 0) OR\r\n"
			+ "    COALESCE(grand_total, 0) <> COALESCE(:grand_total, 0)\r\n"
			+ "  )\r\n"
			+ "",  nativeQuery = true)
	int dbtcrdupdateInvoiceEntryInfoscrap(String product_desc, String remarks, String date_of_notification,
			String date_val, Double tax1, Double tax1_per, Double tax1_value, Double tax2, Double tax2_per, Double tax2_value, Double tax3, Double tax3_per, Double tax3_value, Double grand_total ,String bg_type, String date_of_issue, String reference_no, String lc_number,
			String supply_place, String lr_docketno, String bg_no, String date_of_expiry, String date_of_ref,
			String lc_issue_date, String modified_by,String s_t_exempted, int id);

	
	@Transactional
	@Modifying
	@Query(value="UPDATE DEBITCREDIT_PACKINGNOTE SET   is_locked = :locked where dc_pn_id = :pn_id", nativeQuery = true)
	void updateisLockindbtcrdQSPackingscrap(int pn_id, String locked);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE DEBITCREDIT_PACKINGNOTE SET   is_locked = :locked where dc_pn_id = :pn_id", nativeQuery = true)
	void dbtcrdupdateisLockinQSPackingscrap(int pn_id, String locked);

	@Query(value="select filepath from DEBITCREDIT_PACKINGNOTE where dc_pn_id = :valuePnid", nativeQuery = true)
	String getdbtcrdFilePath(int valuePnid);

	@Query(value ="select TOP 1 dc_pn_items_id from DEBITCREDIT_PACKINGNOTE_ITEMS where dc_pn_id = :slno", nativeQuery = true)
	Optional<Integer> getdbtcrdPn_id(String slno);
	
	@Transactional
	@Modifying
	@Query(value =  "update DEBITCREDIT_PACKINGNOTE_ITEMS SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where dc_pn_items_id = :slno", nativeQuery =  true)
	int deltedbtcrdItemRecord(String slno, String modified_by);
	
	@Query(value="select count(*) from DEBITCREDIT_PACKINGNOTE where dc_pn_id = :pn_id and is_locked = 1", nativeQuery = true)
	int checkdbtcrdIsLocked(String pn_id);

	
	@Transactional
	@Modifying
	@Query(
	    value = "UPDATE DEBITCREDIT_PACKINGNOTE " +
	            "SET cancel = 1 " +
	            "WHERE dc_pn_id = :pn_id",
	    nativeQuery = true
	)
	int updatedbtcrdQSPackingCancelByPnId( String pn_id);

}
