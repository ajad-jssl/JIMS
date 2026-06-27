package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.QSChallanPacking;
import com.JIMS.integration.interfaces.ListAssignMilesonetoContractors;
import com.JIMS.integration.interfaces.QSPACKINGSCRAPTYPELIST_INCTYPE;
import com.JIMS.integration.interfaces.QSPacking_QSChallanInterfaces;
import com.JIMS.integration.interfaces.QSPacking_QSChallanItemsInterfaces;
import com.JIMS.integration.interfaces.QSPacking_QSChallan_LIST_INTERFACES;

import jakarta.persistence.QueryHint; 
import jakarta.transaction.Transactional; 

public interface QSChallanRepository extends JpaRepository<QSChallanPacking, Integer> {

/*	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSCHALLAN_PACKINGNOTEITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date) 	VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE())", nativeQuery = true)
	int insertQSChallanPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String uOM_id,
			String type_id, String pices, String created_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ", nativeQuery = true)
	void insterQSChallanPackingMasterHistoryRecord(String modified_by, String pn_id);*/
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSCHALLAN_PACKINGNOTE_MASTER (contract_id,load_id,filepath,freight,milestone_id,created_by,created_date) "
			+ " VALUES (:contract_id,:load_id,:filepath,:freight,:milestone_id,:created_by, GETDATE())", nativeQuery =  true)
	@QueryHints({ @QueryHint(name = "javax.persistence.query.returnGeneratedKeys", value = "true") })
	int insertQSChallanPackingMasterRecord(String contract_id, String load_id, String filepath,
		 String freight, String milestone_id,String created_by);
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE QSCHALLAN_PACKINGNOTE_MASTER SET filepath = :filepath,"
			+ "transport_name = :transport_name,vechile_no = :vechile_no,grand_total = :grand_total,  modified_by = :modified_by, modified_date = GETDATE() WHERE pn_id = :pn_id", nativeQuery =  true)
	int updateQSChallanPackingMasterRecord(String filepath,String transport_name,String vechile_no,String grand_total,String modified_by,String pn_id);
	

	@Transactional 
	@Modifying
	@Query(value =  "update QSCHALLAN_PACKINGNOTE_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteQSChallanPackingMasterRecord(String pn_id, String modified_by);
	
	 
	@Query(value = "select CONCAT(mm.milestone_code ,+' - ',+ mm.milestone_name ) as milestone_name,cm.contract_name,qm.load_id,qm.pn_id,case WHEN qm.is_locked = 1 THEN 'INVOICE GENERATED' WHEN qm.is_locked = 0 OR qm.is_locked IS NULL then 'INOVICE NOT GENERATED' END AS INVGEN\r\n"
			+ "    from QSCHALLAN_PACKINGNOTE_MASTER qm   \r\n"
			+ "    inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id   \r\n"
			+ "    inner join CONTRACT_MASTER cm on cm.contract_id = qm.contract_id  \r\n"
			+ "    where qm.factory_id = :factory_id AND qm.is_delete = 0 AND (qm.Cancel = 0 OR qm.Cancel IS NULL) order by qm.created_date desc", nativeQuery = true)
	List<QSPacking_QSChallanInterfaces> listQSChallanPackingMasterRecord(String factory_id); 
	
	
	
	
	
	@Query(
			value =
			"SELECT CONCAT(mm.milestone_code, ' - ', mm.milestone_name) AS milestone_name, " +
			"cm.contract_name, qm.load_id, qm.pn_id, " +

			"CASE WHEN qm.is_locked = 1 THEN 'INVOICE GENERATED' " +
			"WHEN qm.is_locked = 0 OR qm.is_locked IS NULL THEN 'INOVICE NOT GENERATED' " +
			"END AS INVGEN " +

			"FROM QSCHALLAN_PACKINGNOTE_MASTER qm " +

			"INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.contract_id " +

			"WHERE qm.factory_id = :factory_id " +
			"AND qm.is_delete = 0 " +
			"AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +

			"AND ( :search IS NULL OR :search = '' OR " +

			"( cm.contract_name + ' ' + " +
			"mm.milestone_code + ' ' + " +
			"mm.milestone_name + ' ' + " +
			"CAST(qm.load_id AS VARCHAR(50)) + ' ' + " +
			"CAST(qm.pn_id AS VARCHAR(50)) + ' ' + " +

			"CASE WHEN qm.is_locked = 1 THEN 'INVOICE GENERATED' " +
			"ELSE 'INOVICE NOT GENERATED' END " +

			") LIKE '%' + :search + '%' " +

			") " +

			"ORDER BY qm.created_date DESC",

			countQuery =
			"SELECT COUNT(*) " +
			"FROM QSCHALLAN_PACKINGNOTE_MASTER qm " +

			"INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
			"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.contract_id " +

			"WHERE qm.factory_id = :factory_id " +
			"AND qm.is_delete = 0 " +
			"AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +

			"AND ( :search IS NULL OR :search = '' OR " +

			"( cm.contract_name + ' ' + " +
			"mm.milestone_code + ' ' + " +
			"mm.milestone_name + ' ' + " +
			"CAST(qm.load_id AS VARCHAR(50)) + ' ' + " +
			"CAST(qm.pn_id AS VARCHAR(50)) + ' ' + " +

			"CASE WHEN qm.is_locked = 1 THEN 'INVOICE GENERATED' " +
			"ELSE 'INOVICE NOT GENERATED' END " +

			") LIKE '%' + :search + '%' " +

			")",

			nativeQuery = true
			)
			Page<QSPacking_QSChallanInterfaces> listQSChallanPackingMasterRecordPaged(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);
	/*
	 * @Query(value =
	 * "select distinct qm.grand_total,qm.pn_id,qm.contract_id,qm.load_id,qm.filepath,qm.transport_name,qm.vechile_no,qm.lot_no,cm.contract_name,\r\n"
	 * +
	 * "qim.qty,qim.per_kgs,qim.unit_price,qim.total,qim.pices,qim.slno,qim.type_id,uom.unit_name as UOM_name,\r\n"
	 * +
	 * "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.type as inc_type_name \r\n"
	 * + "from QSCHALLAN_PACKINGNOTE_MASTER qm  \r\n" +
	 * "left join QSCHALLAN_PACKINGNOTEITEM_MASTER qim on qim.pn_id = qm.pn_id  \r\n"
	 * + "left join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n" +
	 * "left join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n" +
	 * "inner join CONTRACT_MASTER cm on cm.contract_id = qm.contract_id  \r\n" +
	 * "left join INVOICE_TYPE_MASTER it on it.type_no = qim.type_id\r\n" +
	 * "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n" +
	 * "where qim.pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 ",
	 * nativeQuery = true) List<QSPacking_QSChallan_LIST_INTERFACES>
	 * searchQSChallanPackingById(String pn_id);
	 */
	@Query(value = "select distinct qm.grand_total,qm.pn_id,qm.contract_id,qm.load_id,qm.filepath,qm.transport_name,qm.vechile_no,qm.lot_no,cm.contract_name,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN qim.qty END as qty,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN qim.per_kgs END as per_kgs,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN qim.unit_price END as unit_price,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN qim.total END as total,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN qim.pices END as pices,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN qim.slno END as slno,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN qim.type_id END as type_id,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN uom.unit_name END as UOM_name,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN uom.unit_id END as UOM_id,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN sm.scrap_name END as scrap_name_type_id,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN sm.type_id END as scrap_type_id,\r\n"
	        + "mm.milestone_name,mm.milestone_id,\r\n"
	        + "CASE WHEN qim.is_delete = 0 THEN it.type END as inc_type_name \r\n"
	        + "from QSCHALLAN_PACKINGNOTE_MASTER qm  \r\n"
	        + "left join QSCHALLAN_PACKINGNOTEITEM_MASTER qim on qim.pn_id = qm.pn_id  \r\n"
	        + "left join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
	        + "left join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
	        + "inner join CONTRACT_MASTER cm on cm.contract_id = qm.contract_id  \r\n"
	        + "left join INVOICE_TYPE_MASTER it on it.type_no = qim.type_id\r\n"
	        + "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n"
	        + "where qm.pn_id = :pn_id and qm.is_delete = 0 ", 
	        nativeQuery = true)
	List<QSPacking_QSChallan_LIST_INTERFACES> searchQSChallanPackingById(String pn_id);
     
     
 	@Query(value = "select distinct qm.grand_total,qm.pn_id,qm.contract_id,qm.load_id,qm.filepath,,qm.freight,qm.lot_no,cm.contract_name,\r\n"
			+ "qim.qty,qim.per_kgs,qim.unit_price,qim.total,qim.pices as pice,qim.slno,qim.inc_type,uom.unit_name as UOM_name,\r\n"
			+ "uom.unit_id,uom.unit_id as UOM_id,sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.remarks_type as invoice_remarks_type_name, \r\n"
			+ "scm.service_code as HCode, sem.service_code as SCode,\r\n"
			+ "CONCAT(SUBSTRING(CAST(contract_name AS VARCHAR(MAX)), 1, CHARINDEX(' -', CAST(contract_name AS VARCHAR(MAX))) - 1),'-', qm.lot_no ) AS con_code_with_lot_no\r\n"
			+ "from QSCHALLAN_PACKINGNOTE_MASTER qm  \r\n"
			+ "inner join QSCHALLAN_PACKINGNOTEITEM_MASTER qim on qim.pn_id = qm.pn_id  \r\n"
			+ "inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id  \r\n"
			+ "inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id  \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = qm.contract_id  \r\n"
			+ "inner join SERVICECODE_MASTER scm on scm.servicecode_id = cm.h_code\r\n"
			+ "inner join SERVICECODE_MASTER sem on sem.servicecode_id = cm.s_code  \r\n"
			+ "left join INVOICE_TYPE_REMARKS_MASTER it on it.slno = qim.inc_type \r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id where qim.pn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0 and qim.factory_id = :factory_id and qm.factory_id = :factory_id", nativeQuery = true)
     List<QSPacking_QSChallan_LIST_INTERFACES> searchQSChallanPackingByIdnew(String pn_id,String factory_id);
     
     
 	@Transactional
 	@Modifying
 	@Query(value = "INSERT INTO QSCHALLANPACKING_MASTER_HISTORY ( pn_id,contract_id,load_id, filepath,freight,milestoneName,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id,lot_no,is_locked)\r\n"
 			+ "select pn_id,contract_id,load_id, filepath,freight,milestone_id,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),factory_id,lot_no,is_locked from QSCHALLAN_PACKINGNOTE_MASTER where pn_id = :pn_id", nativeQuery =  true)
 	int insterQSChallanPackingMasterHistoryRecord(String modified_by, String pn_id);
 	
 	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSCHALLANPACKING_MASTER_HISTORY (pn_id,contract_id,load_id, filepath,freight,milestoneName,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,lot_no)\r\n"
			+ "select pn_id,contract_id,load_id, filepath,freight,milestone_id,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,lot_no from QSCHALLAN_PACKINGNOTE_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int updateIsDeleteQSChallanPackingMasterHistoryRecord(String modified_by, String pn_id);
	
	 
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSCHALLAN_PACKINGNOTEITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date,factory_id) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE(),:factory_id)",nativeQuery = true)
	int insertQSChallanPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id,int factory_id);
	
	@Query(value = "SELECT COUNT(*) FROM QSCHALLAN_PACKINGNOTE_MASTER WHERE contract_id = :contract_id AND filepath LIKE %:filename%", nativeQuery = true)
	int checkFileExistsForContract(@Param("contract_id") String contract_id,
	                               @Param("filename") String filename);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSCHALLAN_PACKINGNOTEITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE())",nativeQuery = true)
	int insertQSChallanPackingItemRecordWIthoutFactory(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE QSCHALLAN_PACKINGNOTEITEM_MASTER SET qty = :qty,per_kgs = :per_kgs,unit_price = :unit_price ,total = :total,"
			+ " UOM_id = :UOM_id,type_id = :type_id,pices = :pices,modified_by = :modified_by, pn_id = :pn_id, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int updateQSChallanPackingItemsRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,String type_id, String pices, String modified_by,String pn_id, String slno);
	
	
	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "update QSCHALLAN_PACKINGNOTEITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id"
	 * , nativeQuery = true) int delteQSChallanPackingItemMasterRecord(String pn_id,
	 * String modified_by);
	 */
	@Transactional
	@Modifying
	@Query(value = """
	    UPDATE QSCHALLAN_PACKINGNOTEITEM_MASTER
	    SET is_delete = 1,
	        modified_by = :modified_by,
	        modified_date = GETDATE()
	    WHERE slno = :slno
	""", nativeQuery = true)
	int delteQSChallanPackingItemMasterRecord(
	        @Param("slno") String slno,
	        @Param("modified_by") String modified_by
	);

	@Query(value = "select qim.*, qim.uom_id as unit_id from QSCHALLAN_PACKINGNOTEITEM_MASTER qim where is_delete=0   and factory_id =:factory_id", nativeQuery = true)
	List<QSPacking_QSChallanItemsInterfaces> listQSCHALLAN_PACKINGNOTEITEM_MASTERRecord(String factory_id);

	
	@Query(value = "select * from QSCHALLAN_PACKINGNOTEITEM_MASTER where is_delete=0 where slno = :slno", nativeQuery = true)
	QSPacking_QSChallanItemsInterfaces searchQSChallanPackingItemById(String slno);
	
	
	@Transactional 
	@Modifying
	@Query(value =  "update QSCHALLAN_PACKINGNOTEITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int delteQSChallanPackingItemRecord(String slno, String modified_by);

	@Query(value="select unit_name from UOM_MASTER where is_delete = 0", nativeQuery = true)
	List<String> listUOM();
	
	@Query(value="select * from SCRAPTYPE_MASTER where is_delete = 0", nativeQuery = true)
	List<QSPACKINGSCRAPTYPELIST_INCTYPE> listScrapType();
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY ( slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date)\r\n"
			+ "select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int insterQSChallanPackingItenMasterHistoryRecord(String modified_by, String slno);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'UPDATE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int updateQSChallanPackingItemMasterHistoryRecord(String modified_by, String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int IsDeleteQSChallanPackingItemMasterHistoryRecord(String modified_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int IsDeleteQSChallanPackingItemMasterHistoryRecordByItem(String modified_by, String slno);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int updateQSChallanPackingItemMasterHistoryRecordSlno(String modified_by, String slno);
	
	
	@Query(value ="select TOP 1 pn_id from QSCHALLAN_PACKINGNOTEITEM_MASTER where pn_id = :slno", nativeQuery = true)
	Optional<Integer> getPn_id(String slno);
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE QSCHALLAN_PACKINGNOTE_MASTER SET   is_delete = 1 where pn_id = :pn_id", nativeQuery = true)
	int invoiceQSDelete(int pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE QSCHALLAN_PACKINGNOTEITEM_MASTER SET   is_delete = 1 where pn_id = :pn_id", nativeQuery = true)
	int invoiceQSItemDelete(int pn_id);
	
	
	@Query(value="select TOP 1 pn_id from QSCHALLAN_PACKINGNOTE_MASTER where contract_id = :contract_id and load_id = :load_id and factory_id = :factory_id", nativeQuery =  true)
	String getPnIdBasedOnContractandLoad_id(String contract_id, String load_id, String factory_id);
	
	@Query(value="select TOP 1 pn_id from QSCHALLAN_PACKINGNOTE_MASTER where contract_id = :contract_id and load_id = :load_id ", nativeQuery =  true)
	String getPnIdBasedOnContract_id(String contract_id,  String load_id);


	@Query(value="select filepath from QSCHALLAN_PACKINGNOTE_MASTER where pn_id = :valuePnid", nativeQuery = true)
	String getFilePath(int valuePnid);
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE QSCHALLAN_PACKINGNOTE_MASTER SET   is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinQSChallanPacking(int pn_id, String locked);
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE QSCHALLAN_PACKINGNOTEITEM_MASTER SET  is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinQSPackingItem(int pn_id, String locked );
	
	
	@Query(value="select count(*) from QSCHALLAN_PACKINGNOTE_MASTER where pn_id = :pn_id and is_locked = 1", nativeQuery = true)
	int checkIsLocked(String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value="delete from QSCHALLAN_PACKINGNOTEITEM_MASTER where pn_id = :pn_id", nativeQuery = true)
	void deleteQSChallanPackingItemBasedOnPnId(String pn_id);
	
	 
	@Transactional 
	@Modifying
	@Query(value="delete from QSCHALLAN_PACKINGNOTE_MASTER where pn_id = :pn_id", nativeQuery = true)
	void deleteQSChallanPackingBasedOnPnId(String pn_id);
	
	
	@Query(value="select  distinct cm.contract_id, cm.contract_name from MILESTONE_ASSGIN_CONTRACT_MASTER mac\r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = mac.contract_id where mac.factory_id = :factory_id ", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistMilestoneAssignedToContractor(int factory_id);
	
	@Query(value="select distinct cm.contract_id, cm.contract_name from QSPACKING_MASTER mac inner join CONTRACT_MASTER cm on cm.contract_id = mac.contract_id where mac.factory_id =  :factory_id", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistContractorfromQs(int factory_id);

	@Query(value="select  mac.load_id,mac.lot_no from QSPACKING_MASTER mac \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = mac.contract_id \r\n"
			+ "left outer join INVOICE_MASTER im on im.load_id = mac.load_id\r\n"
			+ "where mac.factory_id = :factory_id and mac.contract_id = :contract_id and  \r\n"
			+ "mac.load_id not in (select load_id from INVOICE_MASTER)", nativeQuery = true)
	List<ListAssignMilesonetoContractors> getlistloadContractorfromQs(int factory_id, String contract_id);


	@Query(value = "SELECT TOP 1 load_id FROM QSCHALLAN_PACKINGNOTE_MASTER WHERE load_id LIKE 'DLY-%' ORDER BY pn_id DESC", nativeQuery = true)
	String getLastGlobalLoadId();

}
