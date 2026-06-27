package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.QSDCPackingNote;
import com.JIMS.integration.interfaces.QSPackingInterfaces;
import com.JIMS.integration.interfaces.QSPackingItemsInterfaces;

import jakarta.transaction.Transactional;

public interface DCQSPackingRepository extends JpaRepository<QSDCPackingNote, Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DC_QSPACKING_ITEM(qty,per_kgs,unit_price,total,UOM_id,created_by,pn_id,created_date)  VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:created_by, :pn_id,GETDATE())",nativeQuery = true)
	int insertDCQSPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id, String created_by,int pn_id);

	

	@Transactional
	@Modifying
	@Query(value = "UPDATE DC_QSPACKING_ITEM SET qty = :qty, per_kgs = :per_kgs, unit_price = :unit_price, total = :total,UOM_id = :UOM_id, modified_by = :modified_by, modified_date = GETDATE(), pn_id = :id where slno = :slno",nativeQuery = true)
	int updateDCQSPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id, String modified_by,int id, String slno);
	
	@Transactional
	@Modifying
	@Query(value =  "update DC_QSPACKING_NOTE SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where id = :id", nativeQuery =  true)
	int delteDCQsPackingNoteMasterRecord(String id, String modified_by);
	
	
	@Transactional
	@Modifying
	@Query(value =  "update DC_QSPACKING_ITEM SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteDCQsPackingItemMasterRecord(String pn_id, String modified_by);
	
	@Transactional
	@Modifying
	@Query(value =  "update DC_QSPACKING_ITEM SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int delteDCQsPackingItemRecord(String slno, String modified_by);

	
	@Query(value = "select qm.*,mm.milestone_name,cm.contract_name,mm.milestone_code from DC_QSPACKING_NOTE qm \r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id\r\n"
			+ "where qm.is_delete=0 and qm.factory_id =:factory_id", nativeQuery = true)
	List<QSPackingInterfaces> listDCQSPAckingMasterRecord(String factory_id);
	
	
	@Query(value = "select qim.*, qim.uom_id as unit_id from DC_QSPACKING_ITEM qim where is_delete=0   and factory_id =:factory_id", nativeQuery = true)
	List<QSPackingItemsInterfaces> listDCQSPAckingItemMasterRecord(String factory_id);
	
	
	@Query(value ="select TOP 1 pn_id from DC_QSPACKING_ITEM where slno = :slno", nativeQuery = true)
	Optional<Integer> getDCPn_id(String slno);
	
	
	@Query(value="select filepath from DC_QSPACKING_NOTE where id = :valuePnid", nativeQuery = true)
	String getDCFilePath(int valuePnid);

	@Query(value = "select * from DC_QSPACKING_ITEM where is_delete=0 where slno = :slno", nativeQuery = true)
	QSPackingItemsInterfaces searchDCQSPackingItemById(String slno);



	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DC_QSPACKING_NOTE_HISTORY (id,invoice_type,con_id,milestone_id,dc_type,freight,tax_exempted,filepath,created_by,created_date,modified_by,modified_date,action,transaction_date,is_locked )"
			+ " select id,invoice_type,con_id,milestone_id,dc_type,freight,tax_exempted,filepath,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),is_locked from DC_QSPACKING_NOTE where id = :id", nativeQuery = true)
	void InsertHistoryTableBeforeUpdate(String id, String modified_by);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DC_QSPACKING_NOTE_HISTORY (id,invoice_type,con_id,milestone_id,dc_type,freight,tax_exempted,filepath,created_by,created_date,modified_by,modified_date,action,transaction_date,is_locked,deletd_by,deleted_date )"
			+ " select id,invoice_type,con_id,milestone_id,dc_type,freight,tax_exempted,filepath,created_by,created_date,:modified_by,modified_date,'DELETE',GETDATE(),is_locked,:modified_by,GETDATE() from DC_QSPACKING_NOTE where id = :id", nativeQuery = true)
	void updateHistoryTableBeforeisDelete(String id, String modified_by);

	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DC_QSPACKING_NOTE_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked from DC_QSPACKING_ITEM where pn_id = :pn_id", nativeQuery =  true)
	int updateQSPackingItemMasterHistoryRecord(String modified_by, String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DC_QSPACKING_NOTE_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked from DC_QSPACKING_ITEM where slno = :slno", nativeQuery =  true)
	int IsDeleteQSPackingItemMasterHistoryRecord(String modified_by, String slno);
	
	




}
