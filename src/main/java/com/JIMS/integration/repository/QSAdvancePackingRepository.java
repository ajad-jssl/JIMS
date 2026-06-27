package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.QSAdavancePackingNote;
import com.JIMS.integration.interfaces.Advance_QSPacking_QSPackingItem_LIST_INTERFACES;
import com.JIMS.integration.interfaces.InvoiceMasterInterface;
import com.JIMS.integration.interfaces.QSAdvancePackingInterfaces;
import com.JIMS.integration.interfaces.QSAdvancePackingItemsInterfaces;
import com.JIMS.integration.interfaces.QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES;

import jakarta.transaction.Transactional;

public interface QSAdvancePackingRepository extends JpaRepository<QSAdavancePackingNote, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSADVANCE_PACKINGNOTEITEM_MASTER (qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date,factory_id) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE(),:factory_id)",nativeQuery = true)
	int insertQSAdvancePackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id,int factory_id);
	
	@Query(value = "SELECT COUNT(*) FROM QSADVANCE_PACKINGNOTE_MASTER WHERE contract_id = :contract_id AND filepath LIKE %:filename%", nativeQuery = true)
	int checkFileExistsForContract(@Param("contract_id") String contract_id,
	                               @Param("filename") String filename);

	@Query(value = "SELECT TOP 1 load_id FROM QSADVANCE_PACKINGNOTE_MASTER WHERE load_id LIKE 'ADV-%' ORDER BY pn_id DESC", nativeQuery = true)
	String getLastGlobalLoadId();

	@Transactional
	@Modifying
	@Query(value = "UPDATE QSADVANCE_PACKINGNOTEITEM_MASTER SET qty = :qty,per_kgs = :per_kgs,unit_price = :unit_price ,total = :total,"
			+ " UOM_id = :UOM_id,type_id = :type_id,pices = :pices,modified_by = :modified_by, pn_id = :pn_id, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int updateQSAdvancePackingItemsRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,String type_id, String pices, String modified_by,String pn_id, String slno);

	
	@Transactional
	@Modifying
	@Query(value = "UPDATE QSADVANCE_PACKINGNOTE_MASTER SET  filepath = COALESCE(:filepath, filepath),"
			+ " grand_total = :grand_total,  modified_by = :modified_by, modified_date = GETDATE() WHERE pn_id = :pn_id", nativeQuery =  true)
	int updateQSAdvancePackingMasterRecord(String filepath,String grand_total,String modified_by,String pn_id);


	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "update QSADVANCE_PACKINGNOTE_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id"
	 * , nativeQuery = true) int delteQSAdvancePackingMasterRecord(String pn_id,
	 * String modified_by);
	 */
	@Transactional
	@Modifying
	@Query(value = """
	    UPDATE QSADVANCE_PACKINGNOTE_MASTER
	    SET is_delete = 1,
	        modified_by = :modified_by,
	        modified_date = GETDATE()
	    WHERE pn_id = :pn_id
	      AND ISNULL(is_delete,0) = 0
	""", nativeQuery = true)
	int deleteQSAdvancePackingMasterRecord(
	        @Param("pn_id") String pn_id,
	        @Param("modified_by") String modified_by
	);

	
	
	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "update QSADVANCE_PACKINGNOTEITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id"
	 * , nativeQuery = true) int delteQSAdvancePackingItemMasterRecord(String pn_id,
	 * String modified_by);
	 */
	
	@Transactional
	@Modifying
	@Query(value = """
	    UPDATE QSADVANCE_PACKINGNOTEITEM_MASTER
	    SET is_delete = 1,
	        modified_by = :modified_by,
	        modified_date = GETDATE()
	    WHERE slno = :slno
	""", nativeQuery = true)
	int deleteQSAdvancePackingItemMasterRecord(
	        @Param("slno") String slno,
	        @Param("modified_by") String modified_by
	);
	
	
	
	
	  @Query(value =
	  "select CONCAT(mm.milestone_code ,+' - ',+ mm.milestone_name ) as milestone_name,cm.contract_name,qm.load_id,qm.pn_id,case WHEN qm.is_locked = 1 THEN 'INVOICE GENERATED' WHEN qm.is_locked = 0 OR qm.is_locked IS NULL then 'INOVICE NOT GENERATED' END AS INVGEN\r\n"
	  + " from QSADVANCE_PACKINGNOTE_MASTER qm \r\n"
	  + "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n"
	  + "inner join CONTRACT_MASTER cm on cm.contract_id = qm.contract_id\r\n" +
	  " where qm.factory_id = :factory_id AND qm.is_delete = 0 AND (qm.Cancel = 0 OR qm.Cancel IS NULL) order by qm.created_date desc"
	  , nativeQuery = true)
	  List<QSAdvancePackingInterfaces>listQSAdvancePAckingMasterRecord(String
	  factory_id);
	  
	  
	  
	  
	  
	  @Query(
			  value =
			  "SELECT CONCAT(mm.milestone_code , ' - ', mm.milestone_name ) AS milestone_name, " +
			  "cm.contract_name, qm.load_id, qm.pn_id, " +
			  "CASE WHEN qm.is_locked = 1 THEN 'INVOICE GENERATED' " +
			  "WHEN qm.is_locked = 0 OR qm.is_locked IS NULL THEN 'INVOICE NOT GENERATED' END AS INVGEN " +
			  "FROM QSADVANCE_PACKINGNOTE_MASTER qm " +
			  "INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
			  "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.contract_id " +
			  "WHERE qm.factory_id = :factory_id " +
			  "AND qm.is_delete = 0 " +
			  "AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +
			  "AND ( :search IS NULL OR :search = '' OR " +
			  "cm.contract_name LIKE %:search% OR " +
			  "CONCAT(mm.milestone_code , ' - ', mm.milestone_name) LIKE %:search% OR " +
			  "qm.load_id LIKE %:search% )",

			  countQuery =
			  "SELECT COUNT(*) " +
			  "FROM QSADVANCE_PACKINGNOTE_MASTER qm " +
			  "INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
			  "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.contract_id " +
			  "WHERE qm.factory_id = :factory_id " +
			  "AND qm.is_delete = 0 " +
			  "AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +
			  "AND ( :search IS NULL OR :search = '' OR " +
			  "cm.contract_name LIKE %:search% OR " +
			  "CONCAT(mm.milestone_code , ' - ', mm.milestone_name) LIKE %:search% OR " +
			  "qm.load_id LIKE %:search% )",

			  nativeQuery = true
			  )
			Page<QSAdvancePackingInterfaces> listQSAdvancePAckingMasterRecordssss(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);
	  
	
	@Query(value = "select qim.*, qim.uom_id as unit_id from QSADVANCE_PACKINGNOTEITEM_MASTER qim where is_delete=0 and factory_id = :factory_id", nativeQuery = true)
	List<QSAdvancePackingItemsInterfaces> listQSAdvancePAckingItemMasterRecord(String factory_id);

	
	
	/*
	 * @Query(value =
	 * "select distinct qm.grand_total,qm.pn_id,qm.contract_id,qm.filepath,cm.contract_name,\r\n"
	 * +
	 * "qim.qty,qim.per_kgs,qim.unit_price,qim.total,qim.pices,qim.slno,uom.unit_name as UOM_name,\r\n"
	 * +
	 * "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.type as inc_type_name \r\n"
	 * + "from QSADVANCE_PACKINGNOTE_MASTER qm  \r\n" +
	 * "left join QSADVANCE_PACKINGNOTEITEM_MASTER qim on qim.pn_id = qm.pn_id  \r\n"
	 * + "inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n" +
	 * "inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n" +
	 * "inner join CONTRACT_MASTER cm on cm.contract_id = qm.contract_id  \r\n" +
	 * "left join INVOICE_TYPE_MASTER it on it.type_no = qim.type_id\r\n" +
	 * "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n" +
	 * "where qim.pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0",
	 * nativeQuery = true)
	 * List<QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES>
	 * searchQSAdvancePackingById(String pn_id);
	 */
	
	@Query(value = "SELECT DISTINCT " +
	        "qm.pn_id, qm.contract_id, qm.filepath, cm.contract_name, " +
             
	        "CASE WHEN qim.is_delete = 0 THEN qm.grand_total END AS grand_total," +
	        "CASE WHEN qim.is_delete = 0 THEN qim.qty END AS qty, " +
	        "CASE WHEN qim.is_delete = 0 THEN qim.per_kgs END AS per_kgs, " +
	        "CASE WHEN qim.is_delete = 0 THEN qim.unit_price END AS unit_price, " +
	        "CASE WHEN qim.is_delete = 0 THEN qim.total END AS total, " +
	        "CASE WHEN qim.is_delete = 0 THEN qim.pices END AS pices, " +
	        "CASE WHEN qim.is_delete = 0 THEN qim.slno END AS slno, " +

	        "CASE WHEN qim.is_delete = 0 THEN uom.unit_name END AS UOM_name, " +
	        "CASE WHEN qim.is_delete = 0 THEN uom.unit_id END AS UOM_id, " +

	        "CASE WHEN qim.is_delete = 0 THEN sm.scrap_name END AS scrap_name_type_id, " +
	        "CASE WHEN qim.is_delete = 0 THEN sm.type_id END AS type_id, " +

	        "mm.milestone_name, mm.milestone_id, " +
	        "CASE WHEN qim.is_delete = 0 THEN it.type END AS inc_type_name " +

	        "FROM QSADVANCE_PACKINGNOTE_MASTER qm " +
	        "LEFT JOIN QSADVANCE_PACKINGNOTEITEM_MASTER qim ON qim.pn_id = qm.pn_id " +
	        "LEFT JOIN UOM_MASTER uom ON uom.unit_id = qim.UOM_id " +
	        "LEFT JOIN SCRAPTYPE_MASTER sm ON sm.type_id = qim.type_id " +
	        "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.contract_id " +
	        "LEFT JOIN INVOICE_TYPE_MASTER it ON it.type_no = qim.type_id " +
	        "INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
	        "WHERE qm.pn_id = :pn_id AND qm.is_delete = 0",
	        nativeQuery = true)
	List<QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES> searchQSAdvancePackingById(
	        @Param("pn_id") String pn_id);


@Query(value = "select * from QSADVANCE_PACKINGNOTEITEM_MASTER where is_delete=0 where slno = :slno", nativeQuery = true)
QSAdvancePackingItemsInterfaces searchQSAdvancePackingItemById(String slno);


@Modifying
@Transactional
@Query(value=" INSERT INTO ADVANCEINVOICE_MASTER(contract_id, load_id, contract_slno , product_desc, remarks, date_of_notification, date_val, bg_type,	date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,contract_name,pn_id,created_by,created_date)"
		+ "VALUES (:contract_id, :load_id, :contract_slno , :product_desc, :remarks, :date_of_notification, :date_val, :bg_type,	:date_of_issue, :reference_no, :lc_number,:supply_place, :s_t_exempted, :lr_docketno, :bg_no, :date_of_expiry, :date_of_ref, :lc_issue_date, :contract_name, :pn_id, :created_by, GETDATE())", nativeQuery = true)
int insertAdvanceInvoice(int contract_id, String load_id, int contract_slno , String product_desc, String remarks,
		String date_of_notification, String date_val, String bg_type, String date_of_issue, String reference_no,
		String lc_number, String supply_place, String s_t_exempted, String lr_docketno, String bg_no,
		String date_of_expiry, String date_of_ref, String lc_issue_date, String contract_name, String pn_id,
		String created_by);


@Modifying
@Transactional
@Query(value="update ADVANCEINVOICE_MASTER SET product_desc = :product_desc,remarks = :remarks,date_of_notification = :date_of_notification,\r\n"
		+ "date_val = :date_val,bg_type = :bg_type,date_of_issue = :date_of_issue,reference_no =  :reference_no,lc_number = :lc_number,supply_place = :supply_place,\r\n"
		+ "s_t_exempted = :s_t_exempted,lr_docketno = :lr_docketno,bg_no =  :bg_no,date_of_expiry = :date_of_expiry,date_of_ref = :date_of_ref,lc_issue_date = :lc_issue_date,\r\n"
		+ "pn_id = :pn_id, modified_by = :modified_by, modified_date = GETDATE() where id = :id", nativeQuery = true)
int updateAdvanceInvoice(String product_desc, String remarks,
		String date_of_notification, String date_val, String bg_type, String date_of_issue, String reference_no,
		String lc_number, String supply_place, String s_t_exempted, String lr_docketno, String bg_no,
		String date_of_expiry, String date_of_ref, String lc_issue_date,String pn_id,
		String modified_by, int id);


@Query(value = "select * from ADVANCEINVOICE_MASTER WHERE factory_id = :factory_id and is_delete = 0", nativeQuery = true)
List<InvoiceMasterInterface> listAdvanceInvoiceMasterInfo(int factory_id);

@Query(value=" select count(*) from ADVANCEINVOICE_MASTER ", nativeQuery =  true)
int getRowCount();

@Transactional
@Modifying
@Query(value=" update  ADVANCEINVOICE_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where id = :id", nativeQuery =  true)
int deleteAdvanceInvoice(int id, String modified_by);


@Query(value=" select pn_id from ADVANCEINVOICE_MASTER where id = :id", nativeQuery =  true)
int getPnId(int id);


@Query(value = "select * from ADVANCEINVOICE_MASTER where id = :id", nativeQuery =  true)
InvoiceMasterInterface getInfo(int id);


@Query(value = "select qm.*,qpm.slno,qpm.UOM_id,qpm.qty,qpm.per_kgs,qpm.unit_price,qpm.total,qpm.type_id,qpm.pices from QSADVANCE_PACKINGNOTE_MASTER qm\r\n"
		+ "inner join QSADVANCE_PACKINGNOTEITEM_MASTER qpm on qpm.pn_id = qm.pn_id where qm.pn_id = 1", nativeQuery = true)
List<Advance_QSPacking_QSPackingItem_LIST_INTERFACES> getAdvancePackingAndItemNote(String pn_id);


@Query(value = "select invoice_series from SERIES_MASTER where is_advance =1 and (status is NULL or status ='Open') and \r\n"
		+ "state_id = (select bu.state_id from ADVANCEINVOICE_MASTER im\r\n"
		+ "inner join CONTRACT_MASTER cm on cm.con_slno = im.contract_slno\r\n"
		+ "inner join business_units bu on bu.business_unit_id = cm.regd_office_id\r\n"
		+ "inner join STATE_MASTER sm on sm.id = bu.state_id where im.id = :id)", nativeQuery = true)
Optional<Long> getAdvanceSeriesNumberbasedOnId(int id);

@Query(value = "SELECT MAX(invoice_no) AS highest_invoice_no FROM ( \r\n"
		+ "    SELECT invoice_no FROM INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
		+ "    UNION ALL\r\n"
		+ "    SELECT invoice_no FROM SCRAP_INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
		+ "	UNION ALL\r\n"
		+ "	SELECT invoice_no FROM ADVANCEINVOICE_MASTER WHERE invoice_no like %?1%\r\n"
		+ ") AS CombinedInvoices", nativeQuery = true)
Optional<Long> getInvoiceNumber();


@Transactional
@Modifying
@Query(value = " UPDATE ADVANCEINVOICE_MASTER set verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE() where id  = :id", nativeQuery = true)
int updateAdvanceInvoiceVerification(String verified_by, String invoice_no, int id);

@Query(value="select filepath from QSADVANCE_PACKINGNOTE_MASTER where pn_id = :valuePnid", nativeQuery = true)
String getadvFilePath(int valuePnid);

@Query(value ="select TOP 1 pn_id from QSADVANCE_PACKINGNOTEITEM_MASTER where pn_id = :slno", nativeQuery = true)
Optional<Integer> getadvPn_id(String slno);

}
