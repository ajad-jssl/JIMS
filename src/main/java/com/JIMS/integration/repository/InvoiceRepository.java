package com.JIMS.integration.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.Invoice;
import com.JIMS.integration.interfaces.InvoiceMasterInterface;
import com.JIMS.integration.interfaces.InvoiceTypeInterfaces;
import com.JIMS.integration.interfaces.Listotherloadsinterface;
import com.JIMS.integration.interfaces.OtherInvoiceInterface;
import com.JIMS.integration.interfaces.ServiceCodeMasterInterface;
import com.JIMS.integration.interfaces.othersPackNoteIteminterface;
import com.JIMS.integration.interfaces.othersPackNoteinterface;

import jakarta.transaction.Transactional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

	@Query(value="select type from INVOICE_TYPE_MASTER where is_delete = 0 ", nativeQuery = true)
	List<String> listInvoiceMaster();


	
	@Query(value="select servicecode_id, service_type from SERVICECODE_MASTER where service_type = 'SERVICE CODE' and factory_id =:factory_id", nativeQuery = true)
	List<ServiceCodeMasterInterface> listServiceCode(String factory_id);

	@Query(value="select servicecode_id, service_type from SERVICECODE_MASTER where service_type = 'HSN CODE' and factory_id =:factory_id", nativeQuery = true)
	List<ServiceCodeMasterInterface> listHSNCode(String factory_id);
	
	
	@Transactional
	@Modifying
	@Query(value ="update QSPACKING_ITEM_MASTER SET SType1 = :stype, HType = :hcode  modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery = true)
	int updateQSPAckingItem(String stype, String hcode, String pn_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value ="INSERT INTO INVOICE_TAXENTRY_DETAILS(tax_id,tax_per,invoice_id,advtax,baltax,recovery,tadv,tax_value,created_by,created_date)"
			+ " VALUES (:tax_id,:tax_per,:invoice_id,:advtax,:baltax,:recovery,:tadv,:tax_value,:created_by,GETDATE())", nativeQuery = true)
	void addTaxInvoiceEntry(String tax_id, String tax_per, String invoice_id, String advtax, String baltax,
			String recovery, String tadv, String tax_value,String created_by);

	
	@Transactional
	@Modifying
	@Query(value ="UPDATE INVOICE_TAXENTRY_DETAILS set tax_id = :tax_id,tax_per = :tax_per,invoice_id = :invoice_id,advtax = :advtax,baltax = :baltax,recovery = :recovery,tadv = :tadv,tax_value = :tax_value,modified_by = :modified_by,GETDATE() where slno = :slno)", nativeQuery = true)
	void updateTaxInvoiceEntry(String tax_id, float tax_per, String invoice_id, String advtax, String baltax,
			String recovery, String tadv, float tax_value,String modified_by, String slno);
	@Transactional
	@Modifying
	@Query(value ="update QSPACKING_ITEM_MASTER set SCode = :service_code_id, HCode =:hsn_code_id, inc_type = :type_id, modified_by = :created_by, modified_date =  GETDATE() where slno = :slno", nativeQuery = true)
	void addPackingNoteItems(String service_code_id, String hsn_code_id, String type_id, String created_by,String slno);

	@Query(value="select * from INVOICE_TYPE", nativeQuery = true)
	List<InvoiceTypeInterfaces> listInvoiceType();

	
	@Query(value = "select top 1 other_Pn_no from Othertype_packingnote order by other_Pn_no DESC", nativeQuery = true)
	String findLastotherLoad();
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO Othertype_packingnote (other_Pn_no, con_id, freight, buyer_ref, otherstype_id, Transporter_name, vechile_no, date, filepath, factory_id, created_by, created_date, totalpn_amount, is_locked) VALUES "
			+ "(:newothersLoad, 'OTHERS', :freight, :buyer_ref, :otherstype_id, :Transporter_name, :vechile_no, :date,:uniqueFileName, :factory_id, :created_by, GETDATE(), :totalpn_amount, '0')", nativeQuery = true)
int insertIntothersPackingNote(
  String newothersLoad, String freight, String buyer_ref,
  String otherstype_id, String Transporter_name, String vechile_no, Date date,
  String uniqueFileName, String factory_id, String created_by,
  String totalpn_amount
);

	
	@Query(value = "SELECT other_pn_id from Othertype_packingnote where other_Pn_no= :newothersLoad", nativeQuery = true)
	String getothers_pnidFromPackingNote(String newothersLoad);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO other_packingnote_items (other_pn_id, other_Pn_no, uom_id, type_id, quantities, kgs, unit_price, total, remarkstype_id, created_by, created_date) "
			+ "values (:other_pn_id,:newothersLoad ,:uom_id, :type_id, :quantity, :kgs, :unit_price, :total, :remarkstype_id, :created_by, GETDATE())", nativeQuery = true)
	int insertothersPackingNoteItemDetails(String other_pn_id, String newothersLoad, String uom_id,
			String quantity, String type_id, String kgs, String unit_price, String total, String remarkstype_id, String created_by);
	
//	@Transactional
//	@Modifying
//	@Query(value ="INSERT INTO INVOICE_NUMBER(contract_id, load_id, pd1, transporter, vehicle_no, LRDock_no, LRDock_date, st_exempt, notification_date, verification, verified_emp, verification_date, status, remarks, status_emp, status_date, pn_id, release, invoice_type, Rpd1, taxinex, nadv, adv, bal, recoveryamt, recovery1, tadv1, tnadv, bgtype, bgnumber, referenceno, doi, doe, gstrmk, dor, LCReferenceno, LCIssuedate, frightinex, vendorid, supplyofplace, invoicetype, vendorname, vendorinvno, vendorqty, vendorinvdate, rdate, created_by, created_date"
//			+ " VALUES (:contract_id, :load_id, :pd1, :transporter, :vehicle_no, :LRDock_no, :LRDock_date, :st_exempt, :notification_date, :verification, :verified_emp, :verification_date, :status, :remarks, :status_emp, :status_date, :pn_id, :release, :invoice_type, :Rpd1, :taxinex, :nadv, :adv, :bal, :recoveryamt, :recovery1, :tadv1, :tnadv, :bgtype, :bgnumber, :referenceno, :doi, :doe, :gstrmk, :dor, :LCReferenceno, :LCIssuedate, :frightinex, :vendorid, :supplyofplace, :invoicetype, :vendorname, :vendorinvno, :vendorqty, :vendorinvdate, :rdate, :created_by, GETDATE())", nativeQuery = true)
//	void addInvoiceInfo(String contract_id, String load_id, String pd1, String transporter, String vehicle_no,String LRDock_no, String LRDock_date, String st_exempt, String notification_date, String verification,String verified_emp, String verification_date, String status, String remarks, String status_emp,String status_date, String pn_id, String release, String invoice_type, String Rpd1, String taxinex,	String nadv, String adv, String bal, String recoveryamt, String recovery1, String tadv1, String tnadv,
//			String bgtype, String bgnumber, String referenceno, String doi, String doe, String gstrmk, String dor,
//			String lCReferenceno, String lCIssuedate, String frightinex, String vendorid, String supplyofplace,
//			String invoicetype, String vendorname, String vendorinvno, String vendorqty, String vendorinvdate,
//			String rdate, String created_by);

	@Transactional
	@Modifying
	@Query(value ="INSERT INTO INVOICE_NUMBER(con_slno,slno,product_desc,remarks,date_of_notification,date_val,bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,created_date ) "
			+ " VALUES (:con_slno,:slno,:product_desc,:remarks,:date_of_notification,:date_val,:bg_type,:date_of_issue,:reference_no,:lc_number,:supply_place,:s_t_exempted,:lr_docketno,:bg_no,:date_of_expiry,:date_of_ref,:lc_issue_date,:created_by,GETDATE())", nativeQuery = true)
	int addInvoiceNumber(String con_slno, String slno, String product_desc, String remarks,
			String date_of_notification, String date_val, String bg_type, String date_of_issue, String reference_no,
			String lc_number, String supply_place, String s_t_exempted, String lr_docketno, String bg_no,
			String date_of_expiry, String date_of_ref, String lc_issue_date,String created_by);



	@Query(value = "SELECT distinct im.*,im.created_date AS created_onlydate, qm.lot_no, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED'\r\n"
			+ "        ELSE CAST(im.invoice_no AS VARCHAR(100)) \r\n"
			+ "    END AS invoice_number,\r\n"
			+ "   CASE\r\n"
			+ "        WHEN im.Cancel = 1 THEN 'CANCELLED'\r\n"
			+ "        WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED'\r\n"
			+ "        ELSE 'VERIFIED'\r\n"
			+ "    END AS status \r\n"
			+ "FROM INVOICE_MASTER im  \r\n"
			+ "INNER JOIN QSPACKING_MASTER qm ON qm.load_id = im.load_id   \r\n"
			+ "WHERE im.is_delete = 0 AND im.factory_id = :factory_id", nativeQuery = true)
	List<InvoiceMasterInterface> listInvoiceMasterInfo(String factory_id);
	
	
	
	@Query(

			value =
			"SELECT DISTINCT im.*, im.created_date AS created_onlydate, qm.lot_no, cm.contract_name, " +

			"CASE WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED' " +
			"ELSE CAST(im.invoice_no AS VARCHAR(100)) END AS invoice_number, " +

			"CASE " +
			"WHEN im.Cancel = 1 THEN 'CANCELLED' " +
			"WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED' " +
			"ELSE 'VERIFIED' END AS status " +

			"FROM INVOICE_MASTER im " +

			"INNER JOIN QSPACKING_MASTER qm ON qm.load_id = im.load_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +

			"WHERE im.is_delete = 0 " +
			"AND im.factory_id = :factory_id " +

			"AND ( :search IS NULL OR :search = '' OR " +

			"LOWER(qm.lot_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(im.invoice_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +

			"CONVERT(VARCHAR, im.created_date, 105) LIKE CONCAT('%',:search,'%') OR " +  // dd-mm-yyyy
			"CONVERT(VARCHAR, im.created_date, 23) LIKE CONCAT('%',:search,'%') " +      // yyyy-mm-dd

			") " +

			"ORDER BY im.created_date DESC",

			countQuery =
			"SELECT COUNT(DISTINCT im.id) " +

			"FROM INVOICE_MASTER im " +
			"INNER JOIN QSPACKING_MASTER qm ON qm.load_id = im.load_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +

			"WHERE im.is_delete = 0 " +
			"AND im.factory_id = :factory_id " +

			"AND ( :search IS NULL OR :search = '' OR " +

			"LOWER(qm.lot_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(im.invoice_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +

			"CONVERT(VARCHAR, im.created_date, 105) LIKE CONCAT('%',:search,'%') OR " +
			"CONVERT(VARCHAR, im.created_date, 23) LIKE CONCAT('%',:search,'%') " +

			")",

			nativeQuery = true
			)
	Page<InvoiceMasterInterface> listInvoiceMasterInfoPaged(
	        @Param("factory_id") String factory_id,
	        @Param("search") String search,
	        Pageable pageable
	);
	
	
	@Query(value = "SELECT im.*,im.created_date AS created_onlydate, qmm.lot_no, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED'\r\n"
			+ "        ELSE CAST(im.invoice_no AS VARCHAR(100)) \r\n"
			+ "    END AS invoice_number,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN im.Cancel = 1 THEN 'CANCELLED'\r\n"
			+ "        WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED'\r\n"
			+ "        ELSE 'VERIFIED'\r\n"
			+ "    END AS status \r\n"
			+ "FROM INVOICE_MASTER im  \r\n"
			+ "INNER JOIN QSCHALLAN_PACKINGNOTE_MASTER qmm ON qmm.load_id = im.load_id   \r\n"
			+ "WHERE im.is_delete = 0 AND im.factory_id = :factory_id", nativeQuery = true)
	List<InvoiceMasterInterface> listdlyInvoiceMasterInfo(String factory_id);
	
	
	
	
	@Query(
			value =
			"SELECT im.*, im.created_date AS created_onlydate, qmm.lot_no, cm.contract_name, " +

			"CASE WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED' " +
			"ELSE CAST(im.invoice_no AS VARCHAR(100)) END AS invoice_number, " +

			"CASE " +
			"WHEN im.Cancel = 1 THEN 'CANCELLED' " +
			"WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED' " +
			"ELSE 'VERIFIED' END AS status " +

			"FROM INVOICE_MASTER im " +

			"INNER JOIN QSCHALLAN_PACKINGNOTE_MASTER qmm ON qmm.load_id = im.load_id " +

			"LEFT JOIN CONTRACT_MASTER cm ON cm.contract_id = qmm.contract_id " +

			"WHERE im.is_delete = 0 " +
			"AND im.factory_id = :factory_id " +

			"AND ( " +
			":search IS NULL OR :search = '' OR " +

			"cm.contract_name LIKE CONCAT('%',:search,'%') OR " +

			"im.load_id LIKE CONCAT('%',:search,'%') OR " +

			"im.invoice_no LIKE CONCAT('%',:search,'%') OR " +

			"qmm.lot_no LIKE CONCAT('%',:search,'%') OR " +

			"CONVERT(VARCHAR(10), im.created_date, 105) LIKE CONCAT('%',:search,'%') " +

			") " +

			"ORDER BY im.created_date DESC",

			countQuery =
			"SELECT COUNT(*) " +
			"FROM INVOICE_MASTER im " +
			"INNER JOIN QSCHALLAN_PACKINGNOTE_MASTER qmm ON qmm.load_id = im.load_id " +
			"LEFT JOIN CONTRACT_MASTER cm ON cm.contract_id = qmm.contract_id " +

			"WHERE im.is_delete = 0 " +
			"AND im.factory_id = :factory_id " +

			"AND ( " +
			":search IS NULL OR :search = '' OR " +

			"cm.contract_name LIKE CONCAT('%',:search,'%') OR " +

			"im.load_id LIKE CONCAT('%',:search,'%') OR " +

			"im.invoice_no LIKE CONCAT('%',:search,'%') OR " +

			"qmm.lot_no LIKE CONCAT('%',:search,'%') OR " +

			"CONVERT(VARCHAR(10), im.created_date, 105) LIKE CONCAT('%',:search,'%') " +

			")",

			nativeQuery = true
			)
			Page<InvoiceMasterInterface> listdlyInvoiceMasterInfoPaged(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);
	
	@Query(value = "SELECT im.*,im.created_date AS created_onlydate, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED'\r\n"
			+ "        ELSE CAST(im.invoice_no AS VARCHAR(100)) \r\n"
			+ "    END AS invoice_number,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN im.Cancel = 1 THEN 'CANCELLED'\r\n"
			+ "        WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED'\r\n"
			+ "        ELSE 'VERIFIED'\r\n"
			+ "    END AS status\r\n"
			+ "FROM INVOICE_MASTER im  \r\n"
			+ "INNER JOIN QSADVANCE_PACKINGNOTE_MASTER qam ON qam.load_id = im.load_id    \r\n"
			+ "WHERE im.is_delete = 0 AND im.factory_id = :factory_id", nativeQuery = true)
	List<InvoiceMasterInterface> listadvInvoiceMasterInfo(String factory_id);
	
	
	
	@Query(
			value =
			"SELECT DISTINCT im.*, im.created_date AS created_onlydate, " +
			"CASE WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED' " +
			"ELSE CAST(im.invoice_no AS VARCHAR(100)) END AS invoice_number, " +
			"CASE " +
			"WHEN im.Cancel = 1 THEN 'CANCELLED' " +
			"WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED' " +
			"ELSE 'VERIFIED' END AS status " +

			"FROM INVOICE_MASTER im " +

			"INNER JOIN QSADVANCE_PACKINGNOTE_MASTER qam ON qam.load_id = im.load_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qam.contract_id " +

			"WHERE im.is_delete = 0 " +
			"AND im.factory_id = :factory_id " +

			"AND ( :search IS NULL OR :search = '' OR " +
			"im.load_id LIKE CONCAT('%',:search,'%') OR " +
			"im.invoice_no LIKE CONCAT('%',:search,'%') OR " +
			"cm.contract_name LIKE CONCAT('%',:search,'%') OR " +
			"FORMAT(im.created_date,'dd-MM-yyyy') LIKE CONCAT('%',:search,'%') )",

			countQuery =
			"SELECT COUNT(DISTINCT im.id) " +

			"FROM INVOICE_MASTER im " +

			"INNER JOIN QSADVANCE_PACKINGNOTE_MASTER qam ON qam.load_id = im.load_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qam.contract_id " +

			"WHERE im.is_delete = 0 " +
			"AND im.factory_id = :factory_id " +

			"AND ( :search IS NULL OR :search = '' OR " +
			"im.load_id LIKE CONCAT('%',:search,'%') OR " +
			"im.invoice_no LIKE CONCAT('%',:search,'%') OR " +
			"cm.contract_name LIKE CONCAT('%',:search,'%') OR " +
			"FORMAT(im.created_date,'dd-MM-yyyy') LIKE CONCAT('%',:search,'%') )",

			nativeQuery = true
			)

			Page<InvoiceMasterInterface> listadvInvoiceMasterInfoss(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);
	
	
	

	@Query(value = "select im.*,bg.description as bgDescription from INVOICE_MASTER im\r\n"
			+ "left join BGTYPE_MASTER bg on bg.bgid = im.bg_type\r\n"
			+ " WHERE id = :id", nativeQuery = true)
	InvoiceMasterInterface listSearchById(String id);

	@Transactional
	@Modifying
	@Query(value = "update INVOICE_MASTER SET is_delete = 1 where id = :id", nativeQuery = true)
	void listDeleteById(String id);

	@Transactional
	@Modifying
	@Query(value = " UPDATE INVOICE_MASTER set rejected_by =:rejected_by , rejected_date = GETDATE(), is_delete = 1 where id  = :id", nativeQuery = true)
	void updateRejectedById(String rejected_by, String id);

	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO INVOICE_MASTER_HISTORY (id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,pn_id,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,action,transcation,factory_id) "
			+ "select id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,pn_id,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,:modified_by,GETDATE(),verified_by,verified_date,verified_status,rejected_by,rejected_date,'UPDATE', GETDATE(),factory_id from INVOICE_MASTER where id = :id"
			+ "", nativeQuery =  true)
	void insertInvoiceHistory(String modified_by, int id);
	
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO INVOICE_MASTER_HISTORY (id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,pn_id,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,action,transcation,factory_id,non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt) "
			+ "select id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,pn_id,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,:verified_by,GETDATE(),verified_status,rejected_by,rejected_date,'VERIFICATION', GETDATE(),factory_id,non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt from INVOICE_MASTER where id = :id"
			+ "", nativeQuery =  true)
	void verificationInvoiceHistory(String verified_by, int id);
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO INVOICE_MASTER_HISTORY (id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,pn_id,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,action,transcation,deleted_by,deleted_date) "
			+ "select id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,pn_id,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,'REJECTED', GETDATE(),:deleted_by, GETDATE() from INVOICE_MASTER where id = :id"
			+ "", nativeQuery =  true)
	void rejectInvoiceHistory(String deleted_by, int id);

	@Query(value = "select load_id, lot_no from QSPACKING_MASTER where con_id = :con_id and factory_id = :factory_id", nativeQuery = true)
	List<Map<Integer,String>> getLoadIdQsPacking(int con_id, int factory_id);

	@Query(value = "select invoice_series " +
	        "from SERIES_MASTER " +
	        "where is_gst = 1 " +
	        "and (status is NULL or status = 'Open') " +
	        "and state_id = ( " +
	        "    select bu.state_id " +
	        "    from INVOICE_MASTER im " +
	        "    inner join CONTRACT_MASTER cm on cm.con_slno = im.contract_slno " +
	        "    inner join business_units bu on bu.business_unit_id = cm.bid " +
	        "    where im.id = :id " +
	        ")", nativeQuery = true)
	Optional<Long> getSeriesNumberbasedOnId(@Param("id") int id);
	
	/*
	 * @Query(value = " SELECT invoice_series\r\n" + "FROM SERIES_MASTER\r\n" +
	 * "WHERE is_gst = 1\r\n" + "  AND (status IS NULL OR status = 'Open')\r\n" +
	 * "  AND state_id IN (\r\n" + "\r\n" + "    -- From INVOICE_MASTER\r\n" +
	 * "    SELECT bu.state_id\r\n" + "    FROM INVOICE_MASTER im\r\n" +
	 * "    INNER JOIN CONTRACT_MASTER cm ON cm.con_slno = im.contract_slno\r\n" +
	 * "    INNER JOIN business_units bu ON bu.business_unit_id = cm.bid\r\n" +
	 * "    WHERE im.id = :id\r\n" + "\r\n" + "    UNION\r\n" + "\r\n" +
	 * "    -- From OTHERS_INVOICE_MASTER (FIXED)\r\n" +
	 * "    SELECT bu.state_id\r\n" + "    FROM OTHERS_INVOICE_MASTER oim\r\n" +
	 * "    INNER JOIN business_units bu ON bu.business_unit_id = oim.factory_id\r\n"
	 * + "    WHERE oim.id = :id\r\n" + ");", nativeQuery = true) Optional<Long>
	 * getSeriesNumberbasedOnIdothers(int id);
	 */
	@Query(value = "SELECT invoice_series " +
            "FROM SERIES_MASTER " +
            "WHERE is_gst = 1 " +
            "  AND (status IS NULL OR status = 'Open') " +
            "  AND state_id = ( " +
            "      SELECT TOP 1 bu.state_id " +
            "      FROM OTHERS_INVOICE_MASTER oim " +
            "      INNER JOIN business_units bu ON bu.business_unit_id = oim.factory_id " +
            "      WHERE oim.id = :id " +
            "  )", nativeQuery = true)
Optional<Long> getSeriesNumberbasedOnIdothers(int id);
	
	/*
	 * @Query(value =
	 * "select invoice_series from SERIES_MASTER where is_deliveryChallan = 1 and (status is NULL or status = 'Open') and \r\n"
	 * + "state_id = (select bu.state_id from INVOICE_MASTER im\r\n" +
	 * "inner join CONTRACT_MASTER cm on cm.con_slno = im.contract_slno\r\n" +
	 * "inner join business_units bu on bu.business_unit_id = cm.bid\r\n" +
	 * "where im.id = :id)", nativeQuery = true) Optional<Long>
	 * getdlySeriesNumberbasedOnId(int id);
	 */
	@Query(value = "SELECT TOP 1 sm.invoice_series " +
            "FROM SERIES_MASTER sm " +
            "WHERE sm.is_deliveryChallan = 1 " +
            "AND (sm.status IS NULL OR sm.status = 'Open') " +
            "AND sm.state_id = (SELECT bu.state_id FROM INVOICE_MASTER im " +
            "                   INNER JOIN CONTRACT_MASTER cm ON cm.con_slno = im.contract_slno " +
            "                   INNER JOIN business_units bu ON bu.business_unit_id = cm.bid " +
            "                   WHERE im.id = :id) " +
            "AND sm.bu_code = (SELECT bu.bu_code FROM INVOICE_MASTER im " +
            "                  INNER JOIN CONTRACT_MASTER cm ON cm.con_slno = im.contract_slno " +
            "                  INNER JOIN business_units bu ON bu.business_unit_id = cm.bid " +
            "                  WHERE im.id = :id) " +
            "ORDER BY sm.series_no ASC",
            nativeQuery = true)
Optional<Long> getdlySeriesNumberbasedOnId(int id);

	@Query(value = "select invoice_series from SERIES_MASTER where is_advance = 1 and (status is NULL or status = 'Open') and \r\n"
	        + "state_id = (select bu.state_id from INVOICE_MASTER im\r\n"
	        + "inner join CONTRACT_MASTER cm on cm.con_slno = im.contract_slno\r\n"
	        + "inner join business_units bu on bu.business_unit_id = cm.bid\r\n"
	        + "where im.id = :id)", nativeQuery = true)
	Optional<Long> getadvSeriesNumberbasedOnId(int id);
	
	@Query(value = "SELECT MAX(invoice_no) AS max_invoice_no FROM INVOICE_MASTER", nativeQuery = true)
	Optional<Long> getInvoiceNumber();
	//?1 mark it will take first parameter
//	@Query(value = "SELECT MAX(invoice_no) AS highest_invoice_no FROM (\r\n"
//			+ "    SELECT invoice_no FROM INVOICE_MASTER WHERE invoice_no like %?1%\r\n"
//			+ "    UNION ALL\r\n"
//			+ "    SELECT invoice_no FROM SCRAP_INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
//			+ "	UNION ALL\r\n"
//			+ "	SELECT invoice_no FROM DEBIT_CREDIT_INVOICE_MASTER WHERE invoice_no like %?1%  UNION ALL\r\n"
//			+ "	SELECT invoice_no FROM Others_invoice_master WHERE invoice_no like %?1% \r\n"
//			+ ") AS CombinedInvoices", nativeQuery = true)
//	Optional<Long> getInvoiceNumber(String serieNumber);
	
	@Query(value = "SELECT MAX(invoice_no) AS highest_invoice_no\r\n"
			+ "FROM (\r\n"
			+ "    SELECT invoice_no \r\n"
			+ "    FROM INVOICE_MASTER \r\n"
			+ "    WHERE invoice_no LIKE CONCAT('%', ?1, '%')\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    SELECT invoice_no \r\n"
			+ "    FROM SCRAP_INVOICE_MASTER \r\n"
			+ "    WHERE invoice_no LIKE CONCAT('%', ?1, '%')\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    SELECT invoice_no \r\n"
			+ "    FROM DEBIT_CREDIT_INVOICE_MASTER \r\n"
			+ "    WHERE invoice_no LIKE CONCAT('%', ?1, '%')\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    SELECT invoice_no \r\n"
			+ "    FROM OTHERS_INVOICE_MASTER \r\n"
			+ "    WHERE invoice_no LIKE CONCAT('%', ?1, '%')\r\n"
			+ "\r\n"
			+ "    UNION ALL\r\n"
			+ "\r\n"
			+ "    SELECT invoice_no \r\n"
			+ "    FROM CONSOLIDATED_INVOICE_MASTER \r\n"
			+ "    WHERE invoice_no LIKE CONCAT('%', ?1, '%')\r\n"
			+ "\r\n"
			+ ") AS CombinedInvoices;", nativeQuery = true)
	Optional<Long> getInvoiceNumber(String serieNumber);
	
	@Query(value = "select * from INVOICE_MASTER WHERE is_delete = 0 and invoice_no is NULL", nativeQuery = true)
	List<InvoiceMasterInterface> listInvoiceVerificationInfo();

	
	@Query(value="select bgid, description  from BGTYPE_MASTER where is_delete = 0", nativeQuery = true)
	List<Map<String,String>> bgTypeList();

	@Transactional
	@Modifying
	@Query(value = " UPDATE INVOICE_MASTER SET product_desc = :product_desc, remarks = :remarks, date_of_notification = :date_of_notification,"
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
	int updateInvoiceEntryInfo(String product_desc, String remarks, String date_of_notification,
			String date_val, String bg_type, String date_of_issue, String reference_no, String lc_number,
			String supply_place, String lr_docketno, String bg_no, String date_of_expiry, String date_of_ref,
			String lc_issue_date, String modified_by,String s_t_exempted, int id);

	
	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * " UPDATE INVOICE_MASTER SET invoice_type = :invoice_type,product_desc = :product_desc,remarks = :remarks,date_of_notification = :date_of_notification,\r\n"
	 * +
	 * "date_val = :date_val,bg_type = :bg_type,date_of_issue = :date_of_issue,reference_no = :reference_no,\r\n"
	 * +
	 * "lc_number = :lc_number,supply_place = :supply_place,lr_docketno = :lr_docketno,bg_no = :bg_no,date_of_expiry = :date_of_expiry,\r\n"
	 * +
	 * "date_of_ref = :date_of_ref,lc_issue_date = :lc_issue_date,modified_by = :modified_by, modified_date = GETDATE(),load_id = :load_id,s_t_exempted = :s_t_exempted where id = :id "
	 * , nativeQuery = true) int updateInvoiceEntryInfo(String invoice_type, String
	 * product_desc, String remarks, String date_of_notification, String date_val,
	 * String bg_type, String date_of_issue, String reference_no, String lc_number,
	 * String supply_place, String lr_docketno, String bg_no, String date_of_expiry,
	 * String date_of_ref, String lc_issue_date, String modified_by,String
	 * load_id,String s_t_exempted, int id);
	 */
	@Transactional
	@Modifying
	@Query(value = " UPDATE INVOICE_MASTER set non_tax_adv = :non_tax_adv, tax_adv = :tax_adv, total =:total,payable_by_customer = :payable_by_customer, payable_to_dept = :payable_to_dept,open_tax_adv =:open_tax_adv, open_non_tax_adv = :open_non_tax_adv, gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateInvoiceVerificationDetails(String non_tax_adv, String tax_adv, String total, String payable_by_customer,
			String payable_to_dept, String open_tax_adv, String open_non_tax_adv, String gst_remarks,
			String verified_by, String invoice_no, int id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE INVOICE_MASTER set non_tax_adv = :non_tax_adv, tax_adv = :tax_adv, total =:total,payable_by_customer = :payable_by_customer, payable_to_dept = :payable_to_dept,open_tax_adv =:open_tax_adv, open_non_tax_adv = :open_non_tax_adv, gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateInvoiceVerificationDetails_is_release(String non_tax_adv, String tax_adv, String total, String payable_by_customer,
			String payable_to_dept, String open_tax_adv, String open_non_tax_adv, String gst_remarks,
			String verified_by, int id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_TAXENTRY_DETAILS(tax_id,tax_per,tax_value,adv_tax,tax_payable_by_customer,tax_payable_to_dept,t_adv,invoice_id,created_by,created_date) VALUES (:tax_id,:tax_per,:tax_value,:adv_tax,:tax_payable_by_customer,:tax_payable_to_dept,:t_adv,:invoice_id,:created_by,GETDATE())", nativeQuery = true)
	void insertINVOICE_TAXENTRY_DETAILS(String tax_id, String tax_per, String tax_value, String adv_tax,
			String tax_payable_by_customer, String tax_payable_to_dept, String t_adv, String invoice_id,String created_by);
	
	
	  @Transactional
	  
	  @Modifying
	  
	  @Query(value =
	  "INSERT INTO INVOICE_TAXENTRY_DETAILS(tax_id,tax_per,tax_value,adv_tax,tax_payable_by_customer,tax_payable_to_dept,invoice_id,created_by,created_date) VALUES (:tax_id,:tax_per,:tax_value,:adv_tax,:tax_payable_by_customer,:tax_payable_to_dept,:invoice_id,:created_by,GETDATE())"
	  , nativeQuery = true) void insertINVOICE_TAXENTRY_DETAILSNew(String tax_id,
	  String tax_per, String tax_value, String adv_tax, String
	  tax_payable_by_customer, String tax_payable_to_dept,String invoice_id,String
	  created_by);
	 
	
	@Transactional
	@Modifying
	@Query(value = "delete from INVOICE_TAXENTRY_DETAILS where invoice_id = :invoice_id", nativeQuery = true)
	void detele_INVOICE_TAXENTRY_DETAILS(String invoice_id);



	@Transactional
	@Modifying
	@Query(value ="UPDATE INVOICE_MASTER set is_release = 1,released_by =:released_by,released_datetime = GETDATE() where id = :id", nativeQuery = true)
	int UpdateReleaseById(String id, String released_by);

	@Query(value="select invoice_no from INVOICE_MASTER WHERE id = :id", nativeQuery = true)
	Optional<Long> getInvoiceValue(int id);

	@Query(value="select TOP 1 contract_id from INVOICE_MASTER WHERE id = :id and factory_id = :factory_id", nativeQuery = true)
	long getContractIdFromInvoiceMaster(int id, String factory_id);
	
	@Query(value="select is_release from INVOICE_MASTER WHERE id = :id and factory_id = :factory_id", nativeQuery = true)
	Boolean  getIsReleaseFromInvoiceMaster(int id, String factory_id);
	
	@Query(value="select is_release from Others_invoice_master WHERE id = :id and factory_id = :factory_id", nativeQuery = true)
	Boolean  getIsReleaseFromInvoiceMasterothers(int id, String factory_id);

	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, :modified_by, GETDATE(), verified_by, verified_date, verified_status, rejected_by,   rejected_date,'UPDATE',  GETDATE(),  factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistory(String id, String modified_by);

	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date,'RELEASED',GETDATE(), factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, :released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistoryisReleased(String id, String released_by);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation,deleted_by,deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date,'DELETED',GETDATE(),:deleted_by,GETDATE(),    factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release,  released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistoryisDelete(String id, String deleted_by);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation, deleted_by, deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, pn_id, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, :verified_by, GETDATE(), verified_status, rejected_by, rejected_date,'VERIFIED',GETDATE(),   deleted_by, deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistoryisVerified(String id, String verified_by);
	//List<InvoiceMasterInterface> listSearchById(String id); 2910800066

	@Transactional
	@Modifying
	@Query(value = "delete from INVOICE_MASTER WHERE id = :id", nativeQuery = true)
	int deleteInvoiceMasterBasedOnId(String id);


	@Transactional
	@Modifying
	@Query(value ="update QSPACKING_ITEM_MASTER set inc_type = :inc_type, modified_by = :created_by, modified_date =  GETDATE() where pn_id = :pn_id", nativeQuery = true)
	void addPackingNoteItemInsertTypeIdbasedonPnId(String inc_type, String created_by, String pn_id);
	
	@Transactional
	@Modifying
	@Query(value ="update QSPACKING_MASTER set Cancel = 0, modified_by = :created_by, modified_date =  GETDATE() where pn_id = :pn_id", nativeQuery = true)
	void addPackingNoteInsertTypeIdbasedonPnId(String created_by, String pn_id);
	
	@Transactional
	@Modifying
	@Query(value ="update QSCHALLAN_PACKINGNOTE_MASTER set invoice_type_id = :type_id, modified_by = :created_by, modified_date =  GETDATE() where pn_id = :pn_id", nativeQuery = true)
	void adddlyPackingNoteInsertTypeIdbasedonPnId(String type_id, String created_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value ="update QSADVANCE_PACKINGNOTEITEM_MASTER set modified_by = :created_by, modified_date =  GETDATE() where pn_id = :pn_id", nativeQuery = true)
	void addadvPackingNoteItemInsertTypeIdbasedonPnId(String created_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value ="update OPENING_BALANCE_ITEM set avl_bal = :avl_bal, modified_by =:verified_by, modified_date = GETDATE()  where pn_id = :pn_id", nativeQuery = true)
	void reduceOpeningBalanceBasedOnOpenTaxable(String pn_id, float avl_bal, String verified_by);
//	@Query(value ="update OPENING_BALANCE_ITEM set avl_bal = :avl_bal, modified_by =:verified_by, modified_date = GETDATE()  where pn_id = :pn_id and factory_id = :factory_id", nativeQuery = true)
//	void reduceOpeningBalanceBasedOnOpenTaxable(String pn_id, float avl_bal, String verified_by,String factory_id);

	@Query(value ="select TOP 1 pn_id from OPENING_BALANCE where con_id = :contact_id order by created_date desc", nativeQuery = true)
	String getLastInseretedOpeningBalancePrimaryId(long contact_id);


	@Query(value ="select avl_bal from OPENING_BALANCE_ITEM where pn_id = :pn_idvalue order by created_date desc", nativeQuery = true)
	Optional<Float> getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(String pn_idvalue);



	@Query(value ="select state_id from BUSINESS_UNITS where business_unit_id =  (select bid from CONTRACT_MASTER Where contact_id = :contact_id and factory_id = :factory_id) ", nativeQuery = true)
	int getStateIdofTheBusinessUnits(long contact_id, String factory_id);


	@Query(value ="select open_tax_adv from INVOICE_MASTER where id = :id", nativeQuery = true)
	String getRecoveryAmountfromInvoice(String id);

	@Query(value ="select open_non_tax_adv from INVOICE_MASTER where id = :id", nativeQuery = true)
	String getOpenNonTaxAdvFromInvoice(String id);

	@Query(value = "select sum(tm.tax_per) as taxper from TAX_MASTER tm\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.tax_id = tm.tax_id\r\n"
			+ "where cat.contract_id = :contact_id", nativeQuery = true)
	float getTaxPercentageDetailsAssignedToContractor(long contact_id);

	
	@Query(value = "select dcpn.* ,case when dcpn.is_locked=0 then 'INOVICE NOT GENERATED' else 'INVOICE GENERTATED' END AS INVGEN from Othertype_packingnote dcpn\r\n"
			+ "where dcpn.factory_id= ? and is_cancel is null", nativeQuery = true)
	List<othersPackNoteinterface> getAllothersPackingNoteDetails(String factory_id);
	
	@Query(value = "SELECT other_Pn_no from Othertype_packingnote where factory_id= :factory_id and is_cancel is null", nativeQuery = true)
	List<String> getAllPackingNoteNumberForothers(String factory_id);
	
	@Query(value = "select dcpni.*, um.unit_name, itrm.remarks_type,scm.scrap_name as scrap_name_type_id from other_packingnote_items dcpni\r\n"
			+ "inner join UOM_MASTER um on um.unit_id =  dcpni.uom_id\r\n"
			+ "inner join INVOICE_TYPE_REMARKS_MASTER itrm on itrm.slno = dcpni.remarkstype_id\r\n"
			+ "inner join SCRAPTYPE_MASTER scm on dcpni.type_id=scm.type_id\r\n"
			+ "where dcpni.other_Pn_no = :PnNo", nativeQuery = true)
	List<othersPackNoteIteminterface> getothersPackingNoteItemsDetails(String PnNo);
	
	
	
	
	
	
	
	
	
	// ✅ Query 1 - Paginated main query
	@Query(
		    value = "SELECT op.other_Pn_id, op.other_Pn_no, op.freight, op.filepath, " +
		            "op.totalpn_amount, " +
		            "op.Transporter_name, op.con_id, " +
		            "CASE WHEN op.is_locked = 1 THEN 'INVOICE GENERATED' " +
		            "ELSE 'INOVICE NOT GENERATED' END AS invgen, " +
		            "CASE " +
		            "    WHEN ISNUMERIC(CAST(op.con_id AS VARCHAR)) = 1 THEN cm.contract_name " +
		            "    ELSE CAST(op.con_id AS VARCHAR) " +
		            "END AS con_name " +
		            "FROM Othertype_packingnote op " +
		            "LEFT JOIN CONTRACT_MASTER cm ON cm.contract_id = TRY_CAST(op.con_id AS INT) " +
		            "WHERE op.factory_id = :factory_id " +
		            "AND op.is_cancel IS NULL " +

		            // ✅ Search
		            "AND ( :search = '' " +
		            "OR op.other_Pn_no LIKE '%' + :search + '%' " +                         // load/pn number
		            "OR op.Transporter_name LIKE '%' + :search + '%' " +                     // transporter
		            "OR CAST(op.con_id AS VARCHAR) LIKE '%' + :search + '%' " +              // con_id (e.g. OTHERS)
		            "OR cm.contract_name LIKE '%' + :search + '%' " +                        // contract name
		            "OR CASE WHEN op.is_locked = 1 THEN 'INVOICE GENERATED' " +
		            "   ELSE 'INOVICE NOT GENERATED' END LIKE '%' + :search + '%' " +        // status
		            ") " +

		            "ORDER BY op.other_Pn_no DESC",

		    countQuery = "SELECT COUNT(*) FROM Othertype_packingnote op " +
		                 "LEFT JOIN CONTRACT_MASTER cm ON cm.contract_id = TRY_CAST(op.con_id AS INT) " +
		                 "WHERE op.factory_id = :factory_id " +
		                 "AND op.is_cancel IS NULL " +

		                 "AND ( :search = '' " +
		                 "OR op.other_Pn_no LIKE '%' + :search + '%' " +
		                 "OR op.Transporter_name LIKE '%' + :search + '%' " +
		                 "OR CAST(op.con_id AS VARCHAR) LIKE '%' + :search + '%' " +
		                 "OR cm.contract_name LIKE '%' + :search + '%' " +
		                 "OR CASE WHEN op.is_locked = 1 THEN 'INVOICE GENERATED' " +
		                 "   ELSE 'INOVICE NOT GENERATED' END LIKE '%' + :search + '%' " +
		                 ")",

		    nativeQuery = true
		)
		Page<othersPackNoteinterface> getAllothersPackingNoteDetailsPaged(
		        @Param("factory_id") String factory_id,
		        @Param("search") String search,          // ✅ add search
		        Pageable pageable
		);

	// ✅ Query 2 - Batch items query
	@Query(
	    value = "SELECT dcpni.*, um.unit_name, itrm.remarks_type, " +
	            "scm.scrap_name AS scrap_name_type_id " +
	            "FROM other_packingnote_items dcpni " +
	            "INNER JOIN UOM_MASTER um ON um.unit_id = dcpni.uom_id " +
	            "INNER JOIN INVOICE_TYPE_REMARKS_MASTER itrm ON itrm.slno = dcpni.remarkstype_id " +
	            "INNER JOIN SCRAPTYPE_MASTER scm ON dcpni.type_id = scm.type_id " +
	            "WHERE dcpni.other_Pn_no IN (:pnNos)",
	    nativeQuery = true
	)
	List<othersPackNoteIteminterface> getothersPackingNoteItemsDetailsBatch(
	        @Param("pnNos") List<String> pnNos
	);
	
	
	
	
	
	
	@Query(value = " SELECT        mac.other_Pn_no\r\n"
			+ "			FROM            dbo.Othertype_packingnote AS mac LEFT OUTER JOIN\r\n"
			+ "			                         dbo.Others_invoice_master ON mac.other_Pn_no = dbo.Others_invoice_master.load_id    \r\n"
			+ "			where mac.factory_id = :factory_id  and  mac.other_Pn_no not in (select load_id from Others_invoice_master)", nativeQuery = true)
	List<Listotherloadsinterface> getothersloadContractorfromQs(int factory_id);
	
	@Query(value = "SELECT im.*,im.created_date AS created_onlydate,qm.other_pn_id, \r\n"
			+ "			    CASE \r\n"
			+ "			        WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED'\r\n"
			+ "			        ELSE CAST(im.invoice_no AS VARCHAR(100)) \r\n"
			+ "			    END AS invoice_number,\r\n"
			+ "			    CASE\r\n"
			+ "        WHEN im.Cancel = 1 THEN 'CANCELLED'\r\n"
			+ "        WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED'\r\n"
			+ "        ELSE 'VERIFIED'\r\n"
			+ "    END AS status\r\n"
			+ "			FROM Others_invoice_master im \r\n"
			+ "			INNER JOIN Othertype_packingnote qm ON qm.other_Pn_no = im.load_id \r\n"
			+ "			WHERE im.is_delete is null AND im.factory_id = :factory_id", nativeQuery = true)
	List<OtherInvoiceInterface> listothersInvoiceMasterInfo(String factory_id);
	
	
	
	
	
	
	
	
	
	
	
	
	@Query(
		    value = "SELECT DISTINCT im.*, " +
		            "CONVERT(VARCHAR, im.created_date, 105) AS created_onlydate, " + // dd-MM-yyyy for frontend
		            "qm.other_Pn_id, " +
		            "CASE " +
		            "    WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED' " +
		            "    ELSE CAST(im.invoice_no AS VARCHAR(100)) " +
		            "END AS invoice_number, " +
		            "CASE " +
		            "    WHEN im.Cancel = 1 THEN 'CANCELLED' " +
		            "    WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED' " +
		            "    ELSE 'VERIFIED' " +
		            "END AS status " +
		            "FROM Others_invoice_master im " +
		            "INNER JOIN Othertype_packingnote qm ON qm.other_Pn_no = im.load_id " +
		            "WHERE im.is_delete IS NULL " +
		            "AND im.factory_id = :factory_id " +
		            "AND ( :search IS NULL OR :search = '' OR " +
		            "LOWER(im.invoice_no) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		            "LOWER(im.load_id) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		            "LOWER(qm.other_Pn_no) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		            "CONVERT(VARCHAR, im.created_date, 105) LIKE CONCAT('%', :search, '%') OR " + // dd-MM-yyyy
		            "CONVERT(VARCHAR, im.created_date, 23) LIKE CONCAT('%', :search, '%') " +     // yyyy-MM-dd
		            ") " +
		            "ORDER BY im.created_date DESC",
		    countQuery = "SELECT COUNT(DISTINCT im.id) " +
		                 "FROM Others_invoice_master im " +
		                 "INNER JOIN Othertype_packingnote qm ON qm.other_Pn_no = im.load_id " +
		                 "WHERE im.is_delete IS NULL " +
		                 "AND im.factory_id = :factory_id " +
		                 "AND ( :search IS NULL OR :search = '' OR " +
		                 "LOWER(im.invoice_no) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		                 "LOWER(im.load_id) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		                 "LOWER(qm.other_Pn_no) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		                 "CONVERT(VARCHAR, im.created_date, 105) LIKE CONCAT('%', :search, '%') OR " +
		                 "CONVERT(VARCHAR, im.created_date, 23) LIKE CONCAT('%', :search, '%') " +
		                 ")",
		    nativeQuery = true
		)
		Page<OtherInvoiceInterface> listOthersInvoiceMasterInfoPaged(
		    @Param("factory_id") String factory_id,
		    @Param("search") String search,
		    Pageable pageable
		);
	
	
	
//	@Query(value = "select distinct cm.*,icam.address as invoice_to_id_value,icam1.address as consignee_id_value,shd.shipment_mode as shipment_mode_id_value,\r\n"
//			+ "			shdd.delivery_condition as delivery_condition_id_value,bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value,om.org_name as reg_office_id_value,\r\n"
//			+ "			sm.service_code as s_code_value,sms.service_code as h_code_value,bu.business_unit_name,etm.export_title,\r\n"
//			+ "			icam.gst_no as consigneeGstNo, icam.pan_no as consigneePanNo,icam.district as consigneeDistrict,icam.pin_no as consigneePinNo,\r\n"
//			+ "			stm.state_code as consigneestatecode,stm.state_name as consigneestateName,cum.country_name as consigneeCountryName ,icam1.gst_no as invoiceGstNo, icam1.pan_no as invoicePanNo,stm1.state_code as invoiceStateCode,icam1.pin_no as invoicePinNo,icam1.district as invoiceDistrict,\r\n"
//			+ "			stm1.state_name as invoiceStateName, cum1.country_name as invoiceCountryName,bm.city as bankcityname,stmbm.state_code as bankstatecode, stmbm.state_name as bankstatename, cmbm.country_code as bankcountrycode,\r\n"
//			+ "			cmbm.country_name as bankcountryname,bm.bank_name,bm.account_number, bm.branch_address, bm.branch_code, bm.ifsc_code, bm.swift_code, TAX_MASTER_1.tax_name AS tax_1, dbo.TAX_MASTER.tax_name AS tax_2, TAX_MASTER_2.tax_name AS tax_3 FROM  dbo.EXPORT_TITLE_MASTER AS etm RIGHT OUTER JOIN\r\n"
//			+ "                         dbo.TAX_MASTER RIGHT OUTER JOIN\r\n"
//			+ "                         dbo.Others_invoice_master AS cm INNER JOIN\r\n"
//			+ "                         dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS icam ON icam.id = cm.consignee_id INNER JOIN\r\n"
//			+ "                         dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS icam1 ON icam1.id = cm.Invoice_to_id INNER JOIN\r\n"
//			+ "                         dbo.SHIPMENT_DELIVERY_CONDITION AS shd ON shd.si_id = cm.ship_mode_id INNER JOIN\r\n"
//			+ "                         dbo.SHIPMENT_DELIVERY_CONDITION AS shdd ON shdd.si_id = cm.deliverycondition_id INNER JOIN\r\n"
//			+ "                         dbo.WORKORDER_MASTER AS wm ON wm.work_id = cm.workorder_id INNER JOIN\r\n"
//			+ "                         dbo.BANK_MASTER AS bm ON bm.account_id = cm.bank_id INNER JOIN\r\n"
//			+ "                         dbo.SERVICECODE_MASTER AS sms ON sms.servicecode_id = cm.HSNcode_id INNER JOIN\r\n"
//			+ "                         dbo.SERVICECODE_MASTER AS sm ON sm.servicecode_id = cm.servicecode_id INNER JOIN\r\n"
//			+ "                         dbo.ORGANIZATION_MASTER AS om ON om.org_id = cm.registered_office_id INNER JOIN\r\n"
//			+ "                         dbo.BUSINESS_UNITS AS bu ON bu.business_unit_id = cm.Business_Unit_id INNER JOIN\r\n"
//			+ "                         dbo.STATE_MASTER AS stm ON stm.id = icam.state_id INNER JOIN\r\n"
//			+ "                         dbo.STATE_MASTER AS stm1 ON stm1.id = icam1.state_id INNER JOIN\r\n"
//			+ "                         dbo.STATE_MASTER AS stmbm ON stmbm.id = bm.state_id INNER JOIN\r\n"
//			+ "                         dbo.COUNTRY_MASTER AS cmbm ON cmbm.id = bm.country_id INNER JOIN\r\n"
//			+ "                         dbo.COUNTRY_MASTER AS cum ON cum.id = icam.country_id INNER JOIN\r\n"
//			+ "                         dbo.COUNTRY_MASTER AS cum1 ON cum1.id = icam1.country_id LEFT OUTER JOIN\r\n"
//			+ "                         dbo.TAX_MASTER AS TAX_MASTER_1 ON cm.tax1 = TAX_MASTER_1.tax_id ON dbo.TAX_MASTER.tax_id = cm.tax2 LEFT OUTER JOIN\r\n"
//			+ "                         dbo.TAX_MASTER AS TAX_MASTER_2 ON cm.tax3 = TAX_MASTER_2.tax_id ON etm.id = cm.Export_title_text_id where cm.load_id =  :load_id and cm.factory_id =:factory_id", nativeQuery = true)
//	List<OtherInvoiceInterface> searchothersContractNew(String load_id, String factory_id);
	    
	    
	    
		@Query(value = "select distinct cm.*,(icam.name_of_add+''+icam.add1+''+icam.add2) as invoice_to_id_value,(icam1.name_of_add+''+icam1.add1+''+icam1.add2) as consignee_id_value,shd.shipment_mode as shipment_mode_id_value,\r\n"
				+ "			shdd.delivery_condition as delivery_condition_id_value,bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value,om.org_name as reg_office_id_value,\r\n"
				+ "			sm.service_code as s_code_value,sms.service_code as h_code_value,bu.business_unit_name,org.org_name,etm.export_title,\r\n"
				+ "			icam.gst_no as consigneeGstNo, icam.pan_no as consigneePanNo,icam.district as consigneeDistrict,icam.pin_no as consigneePinNo,\r\n"
				+ "			stm.state_id as consigneestatecode,stm.state_name as consigneestateName,cum.country_name as consigneeCountryName ,icam1.gst_no as invoiceGstNo, icam1.pan_no as invoicePanNo,stm1.state_id as invoiceStateCode,icam1.pin_no as invoicePinNo,icam1.district as invoiceDistrict,\r\n"
				+ "			stm1.state_name as invoiceStateName, cum1.country_name as invoiceCountryName,bm.city as bankcityname,stmbm.state_code as bankstatecode, stmbm.state_name as bankstatename, cmbm.country_code as bankcountrycode,\r\n"
				+ "			cmbm.country_name as bankcountryname,bm.bank_name,bm.account_number, bm.branch_address, bm.branch_code, bm.ifsc_code, bm.swift_code, TAX_MASTER_1.tax_name AS tax_1, dbo.TAX_MASTER.tax_name AS tax_2, TAX_MASTER_2.tax_name AS tax_3 FROM  dbo.EXPORT_TITLE_MASTER AS etm RIGHT OUTER JOIN\r\n"
				+ "                         dbo.TAX_MASTER RIGHT OUTER JOIN\r\n"
				+ "                         dbo.Others_invoice_master AS cm INNER JOIN\r\n"
				+ "                         dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS icam ON icam.id = cm.consignee_id INNER JOIN\r\n"
				+ "                         dbo.INVOICE_CONSIGNEE_ADDRESS_MASTER AS icam1 ON icam1.id = cm.Invoice_to_id INNER JOIN\r\n"
				+ "                         dbo.SHIPMENT_DELIVERY_CONDITION AS shd ON shd.si_id = cm.ship_mode_id INNER JOIN\r\n"
				+ "                         dbo.SHIPMENT_DELIVERY_CONDITION AS shdd ON shdd.si_id = cm.deliverycondition_id INNER JOIN\r\n"
				+ "                         dbo.WORKORDER_MASTER AS wm ON wm.work_id = cm.workorder_id INNER JOIN\r\n"
				+ "                         dbo.BANK_MASTER AS bm ON bm.account_id = cm.bank_id INNER JOIN\r\n"
				+ "                         dbo.SERVICECODE_MASTER AS sms ON sms.servicecode_id = cm.HSNcode_id INNER JOIN\r\n"
				+ "                         dbo.SERVICECODE_MASTER AS sm ON sm.servicecode_id = cm.servicecode_id INNER JOIN\r\n"
				+ "                         dbo.ORGANIZATION_MASTER AS om ON om.org_id = cm.registered_office_id INNER JOIN\r\n"
				+ "                         dbo.BUSINESS_UNITS AS bu ON bu.business_unit_id = cm.Business_Unit_id INNER JOIN\r\n"
				+ "                         dbo.STATE_MASTER AS stm ON stm.id = icam.state_id INNER JOIN\r\n"
				+ "                         dbo.STATE_MASTER AS stm1 ON stm1.id = icam1.state_id INNER JOIN\r\n"
				+ "                         dbo.STATE_MASTER AS stmbm ON stmbm.id = bm.state_id   inner join ORGANIZATION_MASTER org on org.org_id = bu.org_id INNER JOIN\r\n"
				+ "                         dbo.COUNTRY_MASTER AS cmbm ON cmbm.id = bm.country_id INNER JOIN\r\n"
				+ "                         dbo.COUNTRY_MASTER AS cum ON cum.id = icam.country_id INNER JOIN\r\n"
				+ "                         dbo.COUNTRY_MASTER AS cum1 ON cum1.id = icam1.country_id LEFT OUTER JOIN\r\n"
				+ "                         dbo.TAX_MASTER AS TAX_MASTER_1 ON cm.tax1 = TAX_MASTER_1.tax_id ON dbo.TAX_MASTER.tax_id = cm.tax2 LEFT OUTER JOIN\r\n"
				+ "                         dbo.TAX_MASTER AS TAX_MASTER_2 ON cm.tax3 = TAX_MASTER_2.tax_id ON etm.id = cm.Export_title_text_id where cm.load_id =  :load_id and cm.factory_id =:factory_id", nativeQuery = true)
		List<OtherInvoiceInterface> searchothersContractNew(String load_id, String factory_id);

	
	@Query(value = "select im.*,bg.description as bgDescription from Others_invoice_master im\r\n"
			+ "			left join BGTYPE_MASTER bg on bg.bgid = im.bg_type\r\n"
			+ " WHERE id = :id", nativeQuery = true)
	OtherInvoiceInterface listothersSearchById(String id);
	
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE Others_invoice_master \r\n"
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
	int otherupdateInvoiceEntryInfo(String product_desc, String remarks, String date_of_notification,
			String date_val, Double tax1, Double tax1_per, Double tax1_value, Double tax2, Double tax2_per, Double tax2_value, Double tax3, Double tax3_per, Double tax3_value, Double grand_total ,String bg_type, String date_of_issue, String reference_no, String lc_number,
			String supply_place, String lr_docketno, String bg_no, String date_of_expiry, String date_of_ref,
			String lc_issue_date, String modified_by,String s_t_exempted, int id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE Others_invoice_master set gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateothersInvoiceVerificationDetails( String gst_remarks,
			String verified_by, String invoice_no, int id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE Others_invoice_master set gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateothersInvoiceVerificationDetails_is_release( String gst_remarks,
			String verified_by, int id);

	@Transactional
	@Modifying
	@Query(value ="UPDATE Others_invoice_master set is_release = 1,released_by =:released_by,released_datetime = GETDATE() where id = :id", nativeQuery = true)
	int othersUpdateReleaseById(String id, String released_by);

	@Transactional
	@Modifying
	@Query(
		    value = "UPDATE OTHERS_INVOICE_MASTER " +
		            "SET Cancel = 1, " +
		            "    Cancel_by = :cancelled_by, " +
		            "    Cancel_date = GETDATE() " +
		            "WHERE id = :id",
		    nativeQuery = true
		)
	int updateOthersInvoiceCancelById(
	         String id,
	         String cancelled_by
	);

	@Transactional
	@Modifying
	@Query(value =
	    "UPDATE INVOICE_MASTER " +
	    "SET Cancel = 1, Cancel_by = :cancelled_by, Cancel_date = GETDATE() " +
	    "WHERE id = :id",
	    nativeQuery = true)
	int updateCancelById( String id,
	                      String cancelled_by);
	
	
	@Transactional
	@Modifying
	@Query(value ="update CONSOLIDATED_PACKING_ITEMS set inc_type = :inc_type, modified_by = :created_by, modified_date =  GETDATE() where Conpn_id = :pn_id", nativeQuery = true)
	void addconsolidatedPackingNoteItemInsertTypeIdbasedonPnId(String inc_type, String created_by, String pn_id);
	
	@Transactional
	@Modifying
	@Query(value ="update CONSOLIDATED_PACKING_NOTE set Cancel = 0, modified_by = :created_by, modified_date =  GETDATE() where Conpn_id = :pn_id", nativeQuery = true)
	void addconsolidatedPackingNoteInsertTypeIdbasedonPnId(String created_by, String pn_id);
	
	@Query(value = "SELECT distinct im.*,im.created_date AS created_onlydate, qm.load_id, \r\n"
			+ "    CASE \r\n"
			+ "        WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED'\r\n"
			+ "        ELSE CAST(im.invoice_no AS VARCHAR(100)) \r\n"
			+ "    END AS invoice_number,\r\n"
			+ "   CASE\r\n"
			+ "        WHEN im.Cancel = 1 THEN 'CANCELLED'\r\n"
			+ "        WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED'\r\n"
			+ "        ELSE 'VERIFIED'\r\n"
			+ "    END AS status \r\n"
			+ "FROM CONSOLIDATED_INVOICE_MASTER im  \r\n"
			+ "INNER JOIN CONSOLIDATED_PACKING_NOTE qm ON qm.load_id = im.load_id   \r\n"
			+ "WHERE im.is_delete = 0 AND im.factory_id = :factory_id", nativeQuery = true)
	List<InvoiceMasterInterface> listInvoiceMasterconsolidatedInfo(String factory_id);
	
	
	
	
	
	@Query(
		    value = "SELECT DISTINCT im.*, im.created_date AS created_onlydate, qm.load_id, " +
		            "CASE " +
		            "    WHEN im.invoice_no IS NULL THEN 'YET TO BE GENERATED' " +
		            "    ELSE CAST(im.invoice_no AS VARCHAR(100)) " +
		            "END AS invoice_number, " +
		            "CASE " +
		            "    WHEN im.Cancel = 1 THEN 'CANCELLED' " +
		            "    WHEN im.invoice_no IS NULL OR im.is_release = 1 THEN 'NOT VERIFIED' " +
		            "    ELSE 'VERIFIED' " +
		            "END AS status " +
		            "FROM CONSOLIDATED_INVOICE_MASTER im " +
		            "INNER JOIN CONSOLIDATED_PACKING_NOTE qm ON qm.load_id = im.load_id " +
		            "WHERE im.is_delete = 0 " +
		            "AND im.factory_id = :factory_id " +

		            "AND ( :search = '' " +
		            "OR CAST(im.invoice_no AS VARCHAR(100)) LIKE '%' + :search + '%' " +   // ✅ invoice number
		            "OR CAST(im.load_id AS VARCHAR(100)) LIKE '%' + :search + '%' " +      // ✅ load_id
		            "OR CONVERT(VARCHAR(10), im.created_date, 105) LIKE '%' + :search + '%' " + // ✅ date (created_date - no invoice_date column)
		            "OR im.contract_name LIKE '%' + :search + '%' " +                       // ✅ contract_name exists
		            ") " +

		            "ORDER BY im.created_date DESC",

		    countQuery = "SELECT COUNT(DISTINCT im.id) " +
		                 "FROM CONSOLIDATED_INVOICE_MASTER im " +
		                 "INNER JOIN CONSOLIDATED_PACKING_NOTE qm ON qm.load_id = im.load_id " +
		                 "WHERE im.is_delete = 0 " +
		                 "AND im.factory_id = :factory_id " +

		                 "AND ( :search = '' " +
		                 "OR CAST(im.invoice_no AS VARCHAR(100)) LIKE '%' + :search + '%' " +
		                 "OR CAST(im.load_id AS VARCHAR(100)) LIKE '%' + :search + '%' " +
		                 "OR CONVERT(VARCHAR(10), im.created_date, 105) LIKE '%' + :search + '%' " +
		                 "OR im.contract_name LIKE '%' + :search + '%' " +
		                 ")",

		    nativeQuery = true
		)
		Page<InvoiceMasterInterface> listInvoiceMasterConsolidatedInfoPaged(
		        @Param("factory_id") String factory_id,
		        @Param("search") String search,
		        Pageable pageable
		);
	
	
	
	@Query(value = "select im.*,bg.description as bgDescription from CONSOLIDATED_INVOICE_MASTER im\r\n"
			+ "left join BGTYPE_MASTER bg on bg.bgid = im.bg_type\r\n"
			+ " WHERE id = :id", nativeQuery = true)
	InvoiceMasterInterface listconsolidatedSearchById(String id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE CONSOLIDATED_INVOICE_MASTER SET product_desc = :product_desc, remarks = :remarks, date_of_notification = :date_of_notification,"
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
	int updateconsolidatedInvoiceEntryInfo(String product_desc, String remarks, String date_of_notification,
			String date_val, String bg_type, String date_of_issue, String reference_no, String lc_number,
			String supply_place, String lr_docketno, String bg_no, String date_of_expiry, String date_of_ref,
			String lc_issue_date, String modified_by,String s_t_exempted, int id);
	

	@Transactional
	@Modifying
	@Query(value ="update CONSOLIDATED_PACKING_ITEMS set inc_type = :inc_type, modified_by = :created_by, modified_date =  GETDATE() where Conpn_id = :pn_id", nativeQuery = true)
	void updatePackingNoteItemconsolidated(String inc_type, String created_by, String pn_id);
	
	@Query(value="select TOP 1 contract_id from CONSOLIDATED_INVOICE_MASTER WHERE id = :id and factory_id = :factory_id", nativeQuery = true)
	long getconsolidatedContractIdFromInvoiceMaster(int id, String factory_id);
	
	@Query(value="select is_release from CONSOLIDATED_INVOICE_MASTER WHERE id = :id and factory_id = :factory_id", nativeQuery = true)
	Boolean  getIsReleaseconsolidatedFromInvoiceMaster(int id, String factory_id);
	
	@Query(value = "	 SELECT invoice_series   \r\n"
			+ "			   FROM SERIES_MASTER   \r\n"
			+ "			   WHERE is_gst = 1   \r\n"
			+ "			     AND (status IS NULL OR status = 'Open')   \r\n"
			+ "			     AND state_id IN (   \r\n"
			+ "			      \r\n"
			+ "			\r\n"
			+ "			       SELECT bu.state_id   \r\n"
			+ "			       FROM CONSOLIDATED_INVOICE_MASTER cim   \r\n"
			+ "			       INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = cim.contract_id   \r\n"
			+ "			       INNER JOIN business_units bu ON bu.business_unit_id = cm.bid   \r\n"
			+ "			       WHERE cim.id = :id  \r\n"
			+ "			     );  ", nativeQuery = true)
	Optional<Long> getconsolidatedSeriesNumberbasedOnId(int id); 
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE CONSOLIDATED_INVOICE_MASTER set non_tax_adv = :non_tax_adv, tax_adv = :tax_adv, total =:total,payable_by_customer = :payable_by_customer, payable_to_dept = :payable_to_dept,open_tax_adv =:open_tax_adv, open_non_tax_adv = :open_non_tax_adv, gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateconsolidatedInvoiceVerificationDetails_is_release(String non_tax_adv, String tax_adv, String total, String payable_by_customer,
			String payable_to_dept, String open_tax_adv, String open_non_tax_adv, String gst_remarks,
			String verified_by, int id);
	
	@Transactional
	@Modifying
	@Query(value = " UPDATE CONSOLIDATED_INVOICE_MASTER set non_tax_adv = :non_tax_adv, tax_adv = :tax_adv, total =:total,payable_by_customer = :payable_by_customer, payable_to_dept = :payable_to_dept,open_tax_adv =:open_tax_adv, open_non_tax_adv = :open_non_tax_adv, gst_remarks =:gst_remarks, verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateconsolidatedInvoiceVerificationDetails(String non_tax_adv, String tax_adv, String total, String payable_by_customer,
			String payable_to_dept, String open_tax_adv, String open_non_tax_adv, String gst_remarks,
			String verified_by, String invoice_no, int id);

	@Transactional
	@Modifying
	@Query(value ="UPDATE CONSOLIDATED_INVOICE_MASTER set is_release = 1,released_by =:released_by,released_datetime = GETDATE(),verified_status=0 where id = :id", nativeQuery = true)
	int consolidatedUpdateReleaseById(String id, String released_by);
	
	@Query(value ="select open_tax_adv from CONSOLIDATED_INVOICE_MASTER where id = :id", nativeQuery = true)
	float getconsolidatedRecoveryAmountfromInvoice(String id);
	
	@Transactional
	@Modifying
	@Query(value =
	    "UPDATE CONSOLIDATED_INVOICE_MASTER " +
	    "SET Cancel = 1, Cancel_by = :cancelled_by, Cancel_date = GETDATE() " +
	    "WHERE id = :id",
	    nativeQuery = true)
	int updateconsolidatedCancelById( String id,
	                      String cancelled_by);
	
}
