package com.JIMS.integration.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.QSPacking;
import com.JIMS.integration.interfaces.AssignToContract;
import com.JIMS.integration.interfaces.InvoiceMasterInterface;
import com.JIMS.integration.interfaces.ListAssignMilesonetoContractors;
import com.JIMS.integration.interfaces.QSPACKINGSCRAPTYPELIST_INCTYPE;
import com.JIMS.integration.interfaces.QSPackingInterfaces;
import com.JIMS.integration.interfaces.QSPackingItemsInterfaces;
import com.JIMS.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
import com.JIMS.integration.interfaces.consolidatedQSPacking_Interfaces;
import com.JIMS.integration.interfaces.othersPackingItem_LIST_INTERFACES;

import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;

public interface QSPackingRepository extends JpaRepository<QSPacking, Integer>{
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_MASTER (con_id,load_id,filepath,transport_name,vechile_no,freight,milestone_id,taxexemstatus,taxexemamount,created_by,created_date) "
			+ " VALUES (:con_id,:load_id,:filepath,:transport_name,:vechile_no,:freight,:milestone_id,:taxexemstatus,:taxexemamount,:created_by, GETDATE())", nativeQuery =  true)
	@QueryHints({ @QueryHint(name = "javax.persistence.query.returnGeneratedKeys", value = "true") })
	int insertQSPackingMasterRecord(String con_id, String load_id, String filepath,
			String transport_name, String vechile_no, String freight, String milestone_id, String taxexemstatus,
			String taxexemamount, String created_by);
	
	
	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "UPDATE QSPACKING_MASTER SET con_id = :con_id, load_id = :load_id, filepath = :filepath, transport_name = :transport_name,"
	 * +
	 * " vechile_no = :vechile_no, freight = :freight, milestone_id = :milestone_id, taxexemstatus = :taxexemstatus, taxexemamount = :taxexemamount,"
	 * +
	 * " modified_by = :modified_by, modified_date = GETDATE(), lot_no = :lot_no WHERE pn_id = :pn_id"
	 * , nativeQuery = true) int updateQSPackingMasterRecord(String con_id, String
	 * load_id,String filepath, String transport_name, String vechile_no, String
	 * freight, String milestone_id, String taxexemstatus, String taxexemamount,
	 * String modified_by,String lot_no, String pn_id);
	 */
	
	@Transactional
	@Modifying
	@Query(value = """
	    UPDATE QSPACKING_MASTER
	    SET
	        filepath       = COALESCE(:filepath, filepath),
	        transport_name = :transport_name,
	        vechile_no     = :vechile_no,
	        freight        = :freight,
	        grand_total    = :grand_total,
	        modified_by    = :modified_by,
	        modified_date  = GETDATE()
	    WHERE pn_id = :pn_id
	""", nativeQuery = true)
	int updateQSPackingMasterRecord(
	        String filepath,
	        String transport_name,
	        String vechile_no,
	        int freight,
	        String grand_total,
	        String modified_by,
	        String pn_id
	);

	@Transactional
	@Modifying
	@Query(value =  "update QSPACKING_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteQsPackingMasterRecord(String pn_id, String modified_by);


	@Query(value = "select CONCAT(mm.milestone_code ,+' - ',+ mm.milestone_name ) as milestone_name,cm.contract_name,qm.lot_no,qm.pn_id,case when qm.is_locked=0 then 'INOVICE NOT GENERATED' else 'INVOICE GENERTATED' END AS INVGEN\r\n"
			+ "    from QSPACKING_MASTER qm   \r\n"
			+ "    inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id   \r\n"
			+ "    inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id  \r\n"
			+ "    where qm.factory_id = :factory_id AND qm.is_delete = 0 AND (qm.Cancel = 0 OR qm.Cancel IS NULL) order by qm.created_date desc", nativeQuery = true)
	List<QSPackingInterfaces> listQSPAckingMasterRecord(String factory_id);
	
	
	
	
	
	@Query(
			value =
			"SELECT CONCAT(mm.milestone_code, ' - ', mm.milestone_name) AS milestone_name, " +
			"cm.contract_name, qm.lot_no, qm.pn_id, " +
			"CASE WHEN qm.is_locked = 0 THEN 'INOVICE NOT GENERATED' " +
			"ELSE 'INVOICE GENERTATED' END AS INVGEN " +

			"FROM QSPACKING_MASTER qm " +
			"INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +

			"WHERE qm.factory_id = :factory_id " +
			"AND qm.is_delete = 0 " +
			"AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +

			"AND ( :search IS NULL OR :search = '' OR " +
			"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(qm.lot_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(mm.milestone_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(mm.milestone_code) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(CONCAT(mm.milestone_code,' - ',mm.milestone_name)) LIKE LOWER(CONCAT('%',:search,'%')) " +
			") " +

			"ORDER BY qm.created_date DESC",

			countQuery =
			"SELECT COUNT(*) " +
			"FROM QSPACKING_MASTER qm " +
			"INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +

			"WHERE qm.factory_id = :factory_id " +
			"AND qm.is_delete = 0 " +
			"AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +

			"AND ( :search IS NULL OR :search = '' OR " +
			"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(qm.lot_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(mm.milestone_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(mm.milestone_code) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			"LOWER(CONCAT(mm.milestone_code,' - ',mm.milestone_name)) LIKE LOWER(CONCAT('%',:search,'%')) " +
			")",

			nativeQuery = true
			)
			Page<QSPackingInterfaces> listQSPAckingMasterRecordPaged(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);

		@Query(value = "select distinct qm.grand_total,qm.pn_id,qm.con_id,qm.load_id,qm.filepath,qm.vechile_no,qm.freight,qm.lot_no,cm.contract_name,\r\n"
				+ "qim.qty,qim.per_kgs,qim.unit_price,qim.total,qm.transport_name,qim.pices,qim.slno,qim.inc_type,uom.unit_name as UOM_name,\r\n"
				+ "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.type as inc_type_name \r\n"
				+ "from QSPACKING_MASTER qm  \r\n"
				+ "left join QSPACKING_ITEM_MASTER qim on qim.pn_id = qm.pn_id  \r\n"
				+ "inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
				+ "inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
				+ "inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id  \r\n"
				+ "left join INVOICE_TYPE_MASTER it on it.type_no = qim.inc_type\r\n"
				+ "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n"
				+ "where qim.pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 ", nativeQuery = true)
	List<QSPacking_QSPackingItem_LIST_INTERFACES> searchQSPackingById(String pn_id);
	
	 
//	  @Query(value =
//	  "select distinct qm.grand_total,qm.pn_id,qm.con_id,qm.load_id,qm.filepath,qm.vechile_no,qm.freight,qm.lot_no,cm.contract_name,\r\n"
//	  +
//	  "qim.qty,qim.per_kgs,qim.unit_price,qim.total,qm.transport_name,qim.pices as pice,qim.slno,qim.inc_type,uom.unit_name as UOM_name,\r\n"
//	  +
//	  "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
//	  + "scm.service_code as HCode, sem.service_code as SCode,\r\n" +
//	  "CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),'-', qm.lot_no ) AS con_code_with_lot_no\r\n"
//	  + "from QSPACKING_MASTER qm  \r\n" +
//	  "inner join QSPACKING_ITEM_MASTER qim on qim.pn_id = qm.pn_id  \r\n" +
//	  "left join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n" +
//	  "left join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n" +
//	  "inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id  \r\n" +
//	  "left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n" +
//	  "left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n" +
//	  "left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.inc_type \r\n" +
//	  "left join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id where qim.pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 and qim.factory_id = :factory_id and qm.factory_id = :factory_id"
//	  , nativeQuery = true) List<QSPacking_QSPackingItem_LIST_INTERFACES>
//	  searchQSPackingByIdnew(String pn_id,String factory_id);
	  
	
	//New query changed by ajad 14-02-2026[HSN code has been added]

	@Query(value = "select distinct qm.grand_total,qm.pn_id,qm.con_id,qm.load_id,qm.filepath,qm.vechile_no,qm.freight,qm.lot_no,cm.contract_name,\r\n"
			+ "qim.qty,qim.per_kgs,qim.unit_price,qim.total,qm.transport_name,qim.pices as pice,qim.slno,qim.inc_type,uom.unit_name as UOM_name,\r\n"
			+ "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
			+ "COALESCE(scm.service_code, sem.service_code) AS ServiceCode,\r\n"
			+ "CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),'-', qm.lot_no ) AS con_code_with_lot_no\r\n"
			+ "from QSPACKING_MASTER qm  \r\n" + "inner join QSPACKING_ITEM_MASTER qim on qim.pn_id = qm.pn_id  \r\n"
			+ "left join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
			+ "left join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id  \r\n"
			+ "left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
			+ "left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
			+ "left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.inc_type \r\n"
			+ "left join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id where qim.pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 and qim.factory_id = :factory_id and qm.factory_id = :factory_id", nativeQuery = true)
	List<QSPacking_QSPackingItem_LIST_INTERFACES> searchQSPackingByIdnew(String pn_id, String factory_id);
	
	
	
	//without the hsn/service code

//@Query(value = "select distinct qam.grand_total,qam.pn_id,qam.contract_id,qam.filepath,cm.contract_name,\r\n"
//		+ "qaim.qty,qaim.per_kgs,qaim.unit_price,qaim.total,qaim.pices as pice,qaim.slno,uom.unit_name as UOM_name,\r\n"
//		+ "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,qaim.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
//		+ "scm.service_code as HCode, sem.service_code as SCode, qam.invoice_type_id, \r\n"
//		+ "CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),'-', qam.load_id ) AS con_code_with_lot_no\r\n"
//		+ "from QSCHALLAN_PACKINGNOTE_MASTER qam  \r\n"
//		+ "inner join QSCHALLAN_PACKINGNOTEITEM_MASTER qaim on qaim.pn_id = qam.pn_id  \r\n"
//		+ "left join UOM_MASTER uom on uom.unit_id = qaim.UOM_id  \r\n"
//		+ "left join SCRAPTYPE_MASTER sm on sm.type_id = qaim.type_id  \r\n"
//		+ "inner join CONTRACT_MASTER cm on cm.contract_id = qam.contract_id  \r\n"
//		+ "left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
//		+ "left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
//		+ "left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qam.invoice_type_id  \r\n"
//		+ "left join MILESTONE_MASTER mm on mm.milestone_id = qam.milestone_id where qaim.pn_id = :pn_id and qaim.is_delete = 0 and qam.is_delete = 0 and qaim.factory_id = :factory_id and qam.factory_id = :factory_id", nativeQuery = true)
//List<QSPacking_QSPackingItem_LIST_INTERFACES> searchdlyQSPackingByIdnew(String pn_id,String factory_id);


	//with hsn/serv code
	
	
	@Query(value = "select distinct qam.grand_total,qam.pn_id,qam.contract_id,qam.filepath,cm.contract_name,\r\n"
			+ "qaim.qty,qaim.per_kgs,qam.transport_name,qam.vechile_no,qaim.unit_price,qaim.total,qaim.pices as pice,qaim.slno,uom.unit_name as UOM_name,\r\n"
			+ "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,qaim.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
			+ "COALESCE(scm.service_code, sem.service_code) AS ServiceCode, qam.invoice_type_id, \r\n"
			+ "CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),'-', qam.load_id ) AS con_code_with_lot_no\r\n"
			+ "from QSCHALLAN_PACKINGNOTE_MASTER qam  \r\n"
			+ "inner join QSCHALLAN_PACKINGNOTEITEM_MASTER qaim on qaim.pn_id = qam.pn_id  \r\n"
			+ "left join UOM_MASTER uom on uom.unit_id = qaim.UOM_id  \r\n"
			+ "left join SCRAPTYPE_MASTER sm on sm.type_id = qaim.type_id  \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = qam.contract_id  \r\n"
			+ "left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
			+ "left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
			+ "left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qam.invoice_type_id  \r\n"
			+ "left join MILESTONE_MASTER mm on mm.milestone_id = qam.milestone_id where qaim.pn_id = :pn_id and qaim.is_delete = 0 and qam.is_delete = 0 and qaim.factory_id = :factory_id and qam.factory_id = :factory_id", nativeQuery = true)
	List<QSPacking_QSPackingItem_LIST_INTERFACES> searchdlyQSPackingByIdnew(String pn_id,String factory_id);


// the repository method without hsn /service code


//@Query(value = "select distinct qam.grand_total,qam.pn_id,qam.contract_id,qam.filepath,cm.contract_name,\r\n"
//		+ "qaim.qty,qaim.per_kgs,qaim.unit_price,qaim.total,qaim.pices as pice,qaim.slno,uom.unit_name as UOM_name,\r\n"
//		+ "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
//		+ "scm.service_code as HCode, sem.service_code as SCode, \r\n"
//		+ "CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),'-', qam.load_id ) AS con_code_with_lot_no\r\n"
//		+ "from QSADVANCE_PACKINGNOTE_MASTER qam  \r\n"
//		+ "inner join QSADVANCE_PACKINGNOTEITEM_MASTER qaim on qaim.pn_id = qam.pn_id  \r\n"
//		+ "left join UOM_MASTER uom on uom.unit_id = qaim.UOM_id  \r\n"
//		+ "left join SCRAPTYPE_MASTER sm on sm.type_id = qaim.type_id  \r\n"
//		+ "inner join CONTRACT_MASTER cm on cm.contract_id = qam.contract_id  \r\n"
//		+ "left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
//		+ "left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
//		+ "left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qaim.type_id \r\n"
//		+ "left join MILESTONE_MASTER mm on mm.milestone_id = qam.milestone_id where qaim.pn_id = :pn_id and qaim.is_delete = 0 and qam.is_delete = 0 and qaim.factory_id = :factory_id and qam.factory_id = :factory_id", nativeQuery = true)
//List<QSPacking_QSPackingItem_LIST_INTERFACES> searchQSadvPackingByIdnew(String pn_id,String factory_id);





    //  The Method that iclude hsn /service code

@Query(value = "select distinct qam.grand_total,qam.pn_id,qam.contract_id,qam.filepath,cm.contract_name,\r\n"
		+ "qaim.qty,qaim.per_kgs,qaim.unit_price,qaim.total,qaim.pices as pice,qaim.slno,uom.unit_name as UOM_name,\r\n"
		+ "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
		+ "COALESCE(scm.service_code, sem.service_code) AS ServiceCode, \r\n"
		+ "CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),'-', qam.load_id ) AS con_code_with_lot_no\r\n"
		+ "from QSADVANCE_PACKINGNOTE_MASTER qam  \r\n"
		+ "inner join QSADVANCE_PACKINGNOTEITEM_MASTER qaim on qaim.pn_id = qam.pn_id  \r\n"
		+ "left join UOM_MASTER uom on uom.unit_id = qaim.UOM_id  \r\n"
		+ "left join SCRAPTYPE_MASTER sm on sm.type_id = qaim.type_id  \r\n"
		+ "inner join CONTRACT_MASTER cm on cm.contract_id = qam.contract_id  \r\n"
		+ "left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
		+ "left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
		+ "left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qaim.type_id \r\n"
		+ "left join MILESTONE_MASTER mm on mm.milestone_id = qam.milestone_id where qaim.pn_id = :pn_id and qaim.is_delete = 0 and qam.is_delete = 0 and qaim.factory_id = :factory_id and qam.factory_id = :factory_id", nativeQuery = true)
List<QSPacking_QSPackingItem_LIST_INTERFACES> searchQSadvPackingByIdnew(String pn_id,String factory_id);


@Query(value = "SELECT DISTINCT " +
        "qm.grand_total, qm.pn_id, qm.contract_id, qm.filepath, cm.contract_name, im.load_id, " +  // <-- fixed comma here
        "qim.qty, qim.per_kgs, qim.unit_price, qim.total, qim.pices AS pice, qim.slno, " +
        "uom.unit_name AS UOM_name, uom.unit_id, uom.unit_id AS UOM_id, " +
        "sm.scrap_name AS scrap_name_type_id, sm.type_id, " +
        "mm.milestone_name, mm.milestone_id, " +
        "it.remarks_type AS invoice_remarks_type_name, " +
        "scm.service_code AS HCode, sem.service_code AS SCode " +  // <-- fixed extra comma before FROM
        "FROM QSADVANCE_PACKINGNOTE_MASTER qm " +
        "INNER JOIN QSADVANCE_PACKINGNOTEITEM_MASTER qim ON qim.pn_id = qm.pn_id " +
        "INNER JOIN INVOICE_MASTER im ON qm.contract_id = im.contract_id " +
        "INNER JOIN UOM_MASTER uom ON uom.unit_id = qim.UOM_id " +
        "INNER JOIN SCRAPTYPE_MASTER sm ON sm.type_id = qim.type_id " +
        "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.contract_id " +
        "INNER JOIN SERVICECODE_MASTER scm ON scm.servicecode_id = cm.h_code " +
        "INNER JOIN SERVICECODE_MASTER sem ON sem.servicecode_id = cm.s_code " +
        "LEFT JOIN INVOICE_TYPE_REMARKS_MASTER it ON it.slno = qim.type_id " +
        "INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
        "WHERE qim.pn_id = :pn_id " +
        "AND qim.is_delete = 0 " +
        "AND qm.is_delete = 0 " +
        "AND qim.factory_id = :factory_id " +
        "AND qm.factory_id = :factory_id", 
       nativeQuery = true)
List<QSPacking_QSPackingItem_LIST_INTERFACES> searchQSadvancePackingByIdnew(String pn_id, String factory_id);



	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_MASTER_HISTORY ( pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestoneName,taxexemstatus,taxexemamount,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id,lot_no,is_locked)\r\n"
			+ "select pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestone_id,taxexemstatus,taxexemamount,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),factory_id,lot_no,is_locked from QSPACKING_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int insterQSPackingMasterHistoryRecord(String modified_by, String pn_id);


	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_MASTER_HISTORY (pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestoneName,taxexemstatus,taxexemamount,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,lot_no)\r\n"
			+ "select pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestone_id,taxexemstatus,taxexemamount,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,lot_no from QSPACKING_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int updateIsDeleteQSPackingMasterHistoryRecord(String modified_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date,factory_id) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE(),:factory_id)",nativeQuery = true)
	int insertQSPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id,int factory_id);
	
	@Query(value = "SELECT COUNT(*) FROM QSPACKING_MASTER WHERE con_id = :con_id AND filepath LIKE %:filename% AND is_delete = 0", nativeQuery = true)
	int checkFileExistsForContract(@Param("con_id") String con_id, @Param("filename") String filename);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE())",nativeQuery = true)
	int insertQSPackingItemRecordWIthoutFactory(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id);


	@Transactional
	@Modifying
	@Query(value = "UPDATE QSPACKING_ITEM_MASTER SET qty = :qty,per_kgs = :per_kgs,unit_price = :unit_price ,total = :total,"
			+ " UOM_id = :UOM_id,type_id = :type_id,pices = :pices,modified_by = :modified_by, pn_id = :pn_id, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int updateQsPackingItemsRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,String type_id, String pices, String modified_by,String pn_id, String slno);


	@Transactional
	@Modifying
	@Query(value =  "update QSPACKING_ITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteQsPackingItemMasterRecord(String pn_id, String modified_by);

	@Query(value = "select qim.*, qim.uom_id as unit_id from QSPACKING_ITEM_MASTER qim where is_delete=0   and factory_id =:factory_id", nativeQuery = true)
	List<QSPackingItemsInterfaces> listQSPAckingItemMasterRecord(String factory_id);

	
	@Query(value = "select * from QSPACKING_ITEM_MASTER where is_delete=0 where slno = :slno", nativeQuery = true)
	QSPackingItemsInterfaces searchQSPackingItemById(String slno);



	@Transactional
	@Modifying
	@Query(value =  "update QSPACKING_ITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int delteQsPackingItemRecord(String slno, String modified_by);

	@Query(value="select unit_name from UOM_MASTER where is_delete = 0", nativeQuery = true)
	List<String> listUOM();
	
	@Query(value="select * from SCRAPTYPE_MASTER where is_delete = 0", nativeQuery = true)
	List<QSPACKINGSCRAPTYPELIST_INCTYPE> listScrapType();

	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY ( slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date)\r\n"
			+ "select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int insterQSPackingItenMasterHistoryRecord(String modified_by, String slno);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'UPDATE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int updateQSPackingItemMasterHistoryRecord(String modified_by, String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int IsDeleteQSPackingItemMasterHistoryRecord(String modified_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int IsDeleteQSPackingItemMasterHistoryRecordByItem(String modified_by, String slno);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int updateQSPackingItemMasterHistoryRecordSlno(String modified_by, String slno);
	
	@Query(value ="select TOP 1 pn_id from QSPACKING_ITEM_MASTER where pn_id = :slno", nativeQuery = true)
	Optional<Integer> getPn_id(String slno);

	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_MASTER SET   is_delete = 1 where pn_id = :pn_id", nativeQuery = true)
	int invoiceQSDelete(int pn_id);


	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_ITEM_MASTER SET   is_delete = 1 where pn_id = :pn_id", nativeQuery = true)
	int invoiceQSItemDelete(int pn_id);
	
	
	@Query(value="select TOP 1 pn_id from QSPACKING_MASTER where con_id = :con_id and load_id = :load_id and factory_id = :factory_id order by pn_id desc", nativeQuery =  true)
	String getPnIdBasedOnContractandLoad_id(String con_id, String load_id, String factory_id);
	
	@Query(value="select TOP 1 pn_id from QSCHALLAN_PACKINGNOTE_MASTER where contract_id = :contract_id and load_id = :load_id and factory_id = :factory_id", nativeQuery =  true)
	String getdlyPnIdBasedOnContractandLoad_id(String contract_id, String load_id, String factory_id);
	
	@Query(value="select TOP 1 pn_id from QSADVANCE_PACKINGNOTE_MASTER where contract_id = :contract_id and factory_id = :factory_id", nativeQuery =  true)
	String getadvancePnIdBasedOnContractandLoad_id(String contract_id, String factory_id);
	
	@Query(value="select TOP 1 pn_id from QSPACKING_MASTER where con_id = :con_id and load_id = :load_id and factory_id =:factory_id  order by pn_id desc", nativeQuery =  true)
	String getPnIdBasedOnContract_id(String con_id,  String load_id,String factory_id);
	
	@Query(value="select TOP 1 pn_id from QSPACKING_MASTER where con_id = :con_id and load_id = :load_id and factory_id =:factory_id", nativeQuery =  true)
	String getPnIdBasedOncontractcancel(String con_id,  String load_id,String factory_id);
	
	@Query(value="select TOP 1 pn_id from QSCHALLAN_PACKINGNOTE_MASTER where contract_id = :contract_id and load_id = :load_id order by pn_id desc ", nativeQuery =  true)
	String getdlyPnIdBasedOnContract_id(String contract_id, String load_id);
	
	@Query(value="select TOP 1 pn_id from QSADVANCE_PACKINGNOTE_MASTER where contract_id = :contract_id and load_id = :load_id ", nativeQuery =  true)
	String getadvPnIdBasedOnContract_id(String contract_id,String load_id);


	@Query(value="select filepath from QSPACKING_MASTER where pn_id = :valuePnid", nativeQuery = true)
	String getFilePath(int valuePnid);


	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_MASTER SET   is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinQSPacking(int pn_id, String locked);


	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_ITEM_MASTER SET  is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinQSPackingItem(int pn_id, String locked );
	
	@Transactional
	@Modifying
	@Query(value="UPDATE QSADVANCE_PACKINGNOTE_MASTER SET is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinadvQSPacking(int pn_id, String locked);

	@Transactional
	@Modifying
	@Query(value="UPDATE QSADVANCE_PACKINGNOTEITEM_MASTER SET  is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinadvQSPackingItem(int pn_id, String locked );
	
	@Transactional
	@Modifying
	@Query(value="UPDATE QSCHALLAN_PACKINGNOTE_MASTER SET is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockindlyQSPacking(int pn_id, String locked);

	@Transactional
	@Modifying
	@Query(value="UPDATE QSCHALLAN_PACKINGNOTEITEM_MASTER SET is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockindlyQSPackingItem(int pn_id, String locked );

	@Query(value="select count(*) from QSPACKING_MASTER where pn_id = :pn_id and is_locked = 1", nativeQuery = true)
	int checkIsLocked(String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value="delete from QSPACKING_ITEM_MASTER where pn_id = :pn_id", nativeQuery = true)
	void deleteQSPackingItemBasedOnPnId(String pn_id);
	
	@Transactional
	@Modifying
	@Query(value="delete from QSPACKING_MASTER where pn_id = :pn_id", nativeQuery = true)
	void deleteQSPackingBasedOnPnId(String pn_id);


	@Query(value="select  distinct cm.contract_id, cm.contract_name from MILESTONE_ASSGIN_CONTRACT_MASTER mac\r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = mac.contract_id  WHERE (',' + cm.factory_id + ',' LIKE '%,' + :factoryId + ',%')  ", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistMilestoneAssignedToContractor(String factoryId);
	
	@Query(value="select distinct cm.contract_id, cm.contract_name from QSPACKING_MASTER mac inner join CONTRACT_MASTER cm on cm.contract_id = mac.con_id where mac.factory_id =  :factory_id", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistContractorfromQs(int factory_id);
	
	@Query(value="select distinct cm.contract_id, cm.contract_name from QSADVANCE_PACKINGNOTE_MASTER mavc inner join CONTRACT_MASTER cm on cm.contract_id = mavc.contract_id where mavc.factory_id =  :factory_id", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistContractorfromQsadv(int factory_id);
	
	@Query(value="select distinct cm.contract_id, cm.contract_name from QSCHALLAN_PACKINGNOTE_MASTER qpm inner join CONTRACT_MASTER cm on cm.contract_id = qpm.contract_id where qpm.factory_id =  :factory_id", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistContractorfromQschallan(int factory_id);

	
	/*
	 * @Query(
	 * value="select  distinct mac.load_id,mac.lot_no from QSPACKING_MASTER mac \r\n"
	 * + "inner join CONTRACT_MASTER cm on cm.contract_id = mac.con_id \r\n" +
	 * "left outer join INVOICE_MASTER im on im.load_id = mac.load_id\r\n" +
	 * "where mac.factory_id = :factory_id and mac.con_id = :con_id and  mac.Cancel=0 and \r\n"
	 * +
	 * "mac.load_id not in (select load_id from INVOICE_MASTER where Cancel is null)"
	 * , nativeQuery = true) List<ListAssignMilesonetoContractors>
	 * getlistloadContractorfromQs(int factory_id, String con_id);
	 */
	 
	@Query(
			value="select distinct mac.load_id,mac.lot_no from QSPACKING_MASTER mac \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = mac.con_id \r\n"
			+ "left outer join INVOICE_MASTER im on im.load_id = mac.load_id \r\n"
			+ "where mac.factory_id = :factory_id and mac.con_id = :con_id and mac.Cancel=0 \r\n"
			+ "and mac.load_id not in (select load_id from INVOICE_MASTER \r\n"
			+ "where factory_id = :factory_id and Cancel is null)"
			,nativeQuery = true)
			List<ListAssignMilesonetoContractors> getlistloadContractorfromQs(int factory_id, String con_id);
	
	@Query(value = "select qam.load_id from QSADVANCE_PACKINGNOTE_MASTER qam  \r\n"
	        + "inner join CONTRACT_MASTER cm on cm.contract_id = qam.contract_id  \r\n"
	        + "left outer join INVOICE_MASTER im on im.load_id = qam.load_id\r\n"
	        + "where qam.factory_id = :factory_id and qam.contract_id = :contract_id and  \r\n"
	        + "qam.load_id not in (select load_id from INVOICE_MASTER)", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getadvlistloadContractorfromQs(int factory_id, String contract_id);
	
	@Query(value = "select dcp.load_id from QSCHALLAN_PACKINGNOTE_MASTER dcp   \r\n"
	        + "inner join CONTRACT_MASTER cm on cm.contract_id = dcp.contract_id   \r\n"
	        + "left outer join INVOICE_MASTER im on im.load_id = dcp.load_id\r\n"
	        + "where dcp.factory_id = :factory_id and dcp.contract_id = :contract_id and  \r\n"
	        + "dcp.load_id not in (select load_id from INVOICE_MASTER)", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getdlylistloadContractorfromQs(int factory_id, String contract_id);
	
	@Query(value = "select  qm.totalpn_amount, qim.other_pn_items_id, qm.date, qm.buyer_ref, qm.other_pn_id,cast(qm.con_id as nvarchar(50)) as con_id,qm.filepath,qm.vechile_no,qm.freight,\r\n"
			+ "			qim.quantities,qim.kgs,qim.unit_price,qim.total,qm.Transporter_name,qim.type_id,uom.unit_name as UOM_name,\r\n"
			+ "			uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,it.type as inc_type_name \r\n"
			+ "			from Othertype_packingnote qm  \r\n"
			+ "			left join other_packingnote_items qim on qim.other_pn_id = qm.other_pn_id  \r\n"
			+ "			inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
			+ "			inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
			+ "			left join INVOICE_TYPE_MASTER it on it.type_no = qim.type_id \r\n"
			+ "where qim.other_Pn_no = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 ", nativeQuery = true)
List<othersPackingItem_LIST_INTERFACES> searchothersPackingById(String pn_id);

@Transactional
@Modifying
@Query(value =  "update other_packingnote_items SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where other_pn_items_id = :slno", nativeQuery =  true)
int delteothersPackingItemRecord(String slno, String modified_by);

@Query(value="select count(*) from Othertype_packingnote where other_pn_id = :pn_id and is_locked = 1", nativeQuery = true)
int checkothersIsLocked(String pn_id);


@Transactional
@Modifying
@Query(value = "UPDATE Othertype_packingnote SET filepath = :filepath, Transporter_name = :transport_name,"
		+ " buyer_ref = :buyer_ref, date = :date, vechile_no = :vechile_no, freight = :freight,totalpn_amount = :grand_total,  modified_by = :modified_by, modified_date = GETDATE() WHERE other_pn_id = :pn_id", nativeQuery =  true)
int updateothersPackingMasterRecord(String filepath,String transport_name, String buyer_ref, Date date, String vechile_no, String freight,String grand_total,String modified_by,String pn_id);


@Transactional
@Modifying
@Query(value = "UPDATE other_packingnote_items SET quantities = :qty,kgs = :per_kgs, other_Pn_no = :load_id, unit_price = :unit_price ,total = :total,"
		+ " UOM_id = :UOM_id,type_id = :type_id,remarkstype_id = :remarkstype, modified_by = :modified_by, other_pn_id = :pn_id, modified_date = GETDATE() where other_pn_items_id = :slno", nativeQuery =  true)
int updateothersPackingItemsRecord(String qty, String per_kgs, String load_id, String unit_price, String total, String UOM_id, String type_id, String remarkstype,  String modified_by, String pn_id, String slno);

@Transactional
@Modifying
@Query(value = "INSERT INTO other_packingnote_items(quantities,kgs,other_Pn_no,unit_price,total,UOM_id,type_id,remarkstype_id,created_by,other_pn_id,created_date) "
		+ " VALUES (:qty, :per_kgs, :load_id, :unit_price,:total,:UOM_id,:type_id,:remarkstype,:created_by, :pn_id,GETDATE())",nativeQuery = true)
int insertothersPackingItemRecord(String qty, String per_kgs, String load_id  , String unit_price, String total, String UOM_id,
		String type_id, String remarkstype, String created_by, String pn_id);

@Query(value="select TOP 1 other_pn_id from Othertype_packingnote where other_Pn_no = :load_id and factory_id = :factory_id", nativeQuery =  true)
String getPnIdBasedOnothersLoad_id(String load_id, String factory_id);

@Query(value = "select  qm.totalpn_amount,qm.other_pn_id,qm.con_id,qm.other_Pn_no,qm.filepath,qm.vechile_no,qm.freight,\r\n"
		+ "		qim.quantities,qim.kgs,qim.unit_price,qim.total,SUM(qim.pices) AS total_pices,\r\n"
		+ "			   SUM(qim.quantities) OVER (PARTITION BY qm.other_pn_id) AS total_qty,\r\n"
		+ "    SUM(qim.kgs) OVER (PARTITION BY qm.other_pn_id) AS total_kgs,qm.Transporter_name,qim.pices as pice,qim.other_pn_items_id,qim.type_id,uom.unit_name as UOM_name,\r\n"
		+ "		uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,it.remarks_type as invoice_remarks_type_name\r\n"
		+ "		from Othertype_packingnote qm  \r\n"
		+ "		inner join other_packingnote_items qim on qim.other_pn_id = qm.other_pn_id  \r\n"
		+ "		inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
		+ "		inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
		+ "		left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.type_id \r\n"
		+ "	 where qim.other_pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 and qm.factory_id = :factory_id"
		+ "	 GROUP BY qm.totalpn_amount, qm.other_pn_id, qm.con_id, qm.other_Pn_no, qm.vechile_no, qm.freight, qim.quantities, qim.kgs, qim.unit_price, qim.total, qim.pices, qm.Transporter_name, qim.other_pn_items_id, qim.type_id, \r\n"
		+ "   uom.unit_name, uom.unit_id, sm.scrap_name, sm.type_id, it.remarks_type, qm.filepath", nativeQuery = true)
List<othersPackingItem_LIST_INTERFACES> searchothersPackingByIdnew(String pn_id,String factory_id);

@Query(value="	select TOP 1 other_pn_id from Othertype_packingnote where other_Pn_no = :load_id ", nativeQuery =  true)
String getothersPnIdBasedOnContract_id( String load_id);

@Transactional
@Modifying
@Query(value="UPDATE Othertype_packingnote SET   is_locked = :locked where other_pn_id = :pn_id", nativeQuery = true)
void updateisLockinothersQSPacking(int pn_id, String locked);

@Transactional
@Modifying
@Query(value="UPDATE Othertype_packingnote SET   is_locked = :locked where other_pn_id = :pn_id", nativeQuery = true)
void othersupdateisLockinQSPacking(int pn_id, String locked);

@Transactional
@Modifying
@Query(value="UPDATE other_packingnote_items SET  is_locked = :locked where other_pn_id = :pn_id", nativeQuery = true)
void othersupdateisLockinQSPackingItem(int pn_id, String locked );

@Query(value ="select TOP 1 other_pn_id from other_packingnote_items where other_pn_id = :slno", nativeQuery = true)
Optional<Integer> getothersPn_id(String slno);


@Query(value="select filepath from Othertype_packingnote where other_pn_id = :valuePnid", nativeQuery = true)
String getothersFilePath(int valuePnid);

@Transactional
@Modifying
@Query(
    value = "UPDATE QSPACKING_MASTER " +
            "SET Cancel = 1 " +
            "WHERE pn_id = :pn_id",
    nativeQuery = true
)
int updateQSPackingCancelByPnId( String pn_id);

@Transactional
@Modifying
@Query(
    value = "UPDATE Othertype_packingnote " +
            "SET is_cancel = 1 " +
            "WHERE other_pn_id = :other_pn_id",
    nativeQuery = true
)
int updateothersQSPackingCancelByPnId( String other_pn_id);


@Transactional
@Modifying
@Query(
    value = "UPDATE QSADVANCE_PACKINGNOTE_MASTER " +
            "SET cancel = 1 " +
            "WHERE pn_id = :pn_id",
    nativeQuery = true
)
int updateadvQSPackingCancelByPnId( String pn_id);


@Transactional
@Modifying
@Query(
    value = "UPDATE QSCHALLAN_PACKINGNOTE_MASTER " +
            "SET cancel = 1 " +
            "WHERE pn_id = :pn_id",
    nativeQuery = true
)
int updatechallanQSPackingCancelByPnId( String pn_id);

@Query(value="\r\n"
		+ "		   SELECT im.id, im.invoice_no\r\n"
		+ "FROM INVOICE_MASTER im\r\n"
		+ "WHERE im.contract_id = :con_id\r\n"
		+ "AND im.invoice_type = 'Steel'\r\n"
		+ "AND (im.Cancel = 0 OR im.Cancel IS NULL)\r\n"
		+ "AND im.verified_status = 1 AND im.is_release=0 AND im.factory_id=:factory_id\r\n"
		+ "AND CAST(im.id AS VARCHAR(50)) NOT IN (\r\n"
		+ "    SELECT ci.invid\r\n"
		+ "    FROM consolidated_invoice_items ci\r\n"
		+ "    WHERE ci.conpnid = :con_id\r\n"
		+ "    AND (ci.cancel = 0 OR ci.cancel IS NULL)\r\n"
		+ "    AND ci.is_deleted = 0\r\n"
		+ ")\r\n"
		+ "ORDER BY im.id DESC;", nativeQuery = true)
List<InvoiceMasterInterface> searchconsolidatedinvoice(String con_id,String factory_id);


//@Query(value="select distinct cm.contract_id, cm.contract_name ,mac.load_id from CONSOLIDATED_PACKING_NOTE mac inner join CONTRACT_MASTER cm on cm.contract_id = mac.con_id where mac.factory_id =   :factory_id", nativeQuery = true)
//List<ListAssignMilesonetoContractors> getlistContractorfromconsolidated(int factory_id);




//✅ Query 1 - Distinct contracts only (for dropdown)
@Query(value =
 "SELECT DISTINCT cm.contract_id, cm.contract_name " +
 "FROM CONSOLIDATED_PACKING_NOTE mac " +
 "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = mac.con_id " +
 "WHERE mac.factory_id = :factory_id and mac.is_locked = 0",
 nativeQuery = true)
List<ListAssignMilesonetoContractors> getlistContractorfromconsolidated(
     @Param("factory_id") int factory_id);

//✅ Only loads where invoice NOT yet generated (is_locked = 0)
@Query(value =
"	SELECT mac.load_id, mac.conpn_id        \r\n"
+ "FROM CONSOLIDATED_PACKING_NOTE mac        \r\n"
+ "WHERE mac.factory_id =  :factory_id     \r\n"
+ "AND CAST(mac.con_id AS VARCHAR(50)) = CAST(:con_id AS VARCHAR(50))        \r\n"
+ "AND (mac.is_locked = 0 OR mac.is_locked IS NULL)        \r\n"
+ "AND mac.load_id NOT IN (\r\n"
+ "    SELECT load_id \r\n"
+ "    FROM CONSOLIDATED_INVOICE_MASTER\r\n"
+ "    WHERE load_id IS NOT NULL\r\n"
+ ")\r\n"
+ "ORDER BY mac.conpn_id DESC",
nativeQuery = true)
List<ListAssignMilesonetoContractors> getlistLoadIdsForContractUnlocked(
     @Param("factory_id") int factory_id,
     @Param("con_id") String con_id);                    

@Query(value=" select  distinct mac.load_id from CONSOLIDATED_PACKING_NOTE mac \r\n"
		+ "		where mac.factory_id = :factory_id and mac.con_id = :con_id and  mac.Cancel=0  ", nativeQuery = true)
List<ListAssignMilesonetoContractors> getlistloadContractorfromconsolidated(int factory_id, String con_id);

@Query(value=" select TOP 1 Conpn_id from CONSOLIDATED_PACKING_NOTE where load_id = :load_id and factory_id = :factory_id order by Conpn_id desc", nativeQuery =  true)
String getconsolidatedPnIdBasedOnContractandLoad_id(String load_id, String factory_id);

@Query(value = " select distinct qm.grand_total,qm.Conpn_id,qm.con_id,qm.load_id,qm.vechile_no,cm.contract_name,\r\n"
		+ "			qim.qty,qim.per_kgs,qim.unit_price,qim.total,qm.transport_name,qim.Conslno,qim.inc_type,\r\n"
		+ "			qim.UOM,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
		+ "			COALESCE(scm.service_code, sem.service_code) AS ServiceCode,\r\n"
		+ "			CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),' - ',qm.load_id) AS con_code_with_lot_no\r\n"
		+ "			from CONSOLIDATED_PACKING_NOTE qm   inner join CONSOLIDATED_PACKING_ITEMS qim on qim.Conpn_id = qm.Conpn_id  \r\n"
		+ "			left join SCRAPTYPE_MASTER sm on sm.type_id =qim.inc_type  \r\n"
		+ "			inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id  \r\n"
		+ "			left join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
		+ "			left join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
		+ "			left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.inc_type \r\n"
		+ "			left join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id where qim.Conpn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 and qim.factory_id = :factory_id and qm.factory_id = :factory_id", nativeQuery = true)
List<consolidatedQSPacking_Interfaces> searchconsolidatedPackingByIdnew(String pn_id, String factory_id);


@Transactional
@Modifying
@Query(value="UPDATE CONSOLIDATED_PACKING_NOTE SET   is_locked = :locked where conpn_id = :pn_id", nativeQuery = true)
void updateisLockinconsolidatedPacking(int pn_id, String locked);

@Transactional
@Modifying
@Query(
    value = "UPDATE CONSOLIDATED_PACKING_NOTE " +
            "SET Cancel = 1 " +
            "WHERE conpn_id = :pn_id",
    nativeQuery = true
)
int updateconsolidatedQSPackingCancelByPnId( String pn_id);




@Modifying
@Transactional
@Query(value = "UPDATE consolidated_invoice_items " +
               "SET cancel = 1, cancel_by = :cancelled_by, cancel_date = GETDATE() " +
               "WHERE consol_inv_no = :load_id",
       nativeQuery = true)
int cancelConsolidatedInvoiceItemsByLoadId(
        @Param("load_id")      String load_id,
        @Param("cancelled_by") String cancelled_by
);



}





