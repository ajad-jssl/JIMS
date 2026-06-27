package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.SalesOrderTandCInterfaces;
import com.JIMS.integration.interfaces.ScrapItemInterfaces;
import com.JIMS.integration.interfaces.ScrapSalesLocationInterfaces;
import com.JIMS.integration.interfaces.ScrapSalesOrderInterfaces;
import com.JIMS.integration.interfaces.ScrapTypeInterfaces;

import jakarta.transaction.Transactional;

public interface ScrapMasterRepository extends JpaRepository<User, Integer> {

	/* SCRAPTYPE START */
	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESCRAPTYPES_MASTER(scrap_typecode,scrap_typename,created_by,created_date)"
			+ "VALUES(:scrap_typecode,:scrap_typename,:created_by,GETDATE())", nativeQuery = true)
	int addScrapSaleScrapTypes(String scrap_typecode, String scrap_typename, String created_by);

	@Query(value = "SELECT stid,scrap_typecode,scrap_typename,factory_id FROM SCRAPSALESCRAPTYPES_MASTER WHERE is_delete=0", nativeQuery = true)
	List<ScrapTypeInterfaces> getAllScrapSaleScrapTypes();

	@Query(value = "SELECT stid,scrap_typecode,scrap_typename FROM SCRAPSALESCRAPTYPES_MASTER WHERE is_delete=0 AND stid=:stid", nativeQuery = true)
	ScrapTypeInterfaces getScrapSaleScrapTypesById(String stid);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAPSALESCRAPTYPES_MASTER SET is_delete=1, modified_by =:modified_by, modified_date = GETDATE() WHERE stid=:stid", nativeQuery = true)
	int deleteScrapSaleScrapTypesById(String stid, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESCRAPTYPES_MASTER SET scrap_typecode=:scrap_typecode,scrap_typename=:scrap_typename,modified_by=:modified_by,modified_date=GETDATE() WHERE stid=:stid", nativeQuery = true)
	int UpdateScrapSaleScrapTypes(String stid, String scrap_typecode, String scrap_typename, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT into SCRAPSALESCRAPTYPES_MASTER_HISTORY(stid,scrap_typecode,scrap_typename,created_by,created_date,modified_by,modified_date,action,transaction_date)"
			+ " select stid,scrap_typecode,scrap_typename,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from SCRAPSALESCRAPTYPES_MASTER where stid=:stid", nativeQuery = true)
	void insertScrapSalesTypeHistoryRecord(String modified_by, String stid);

	@Modifying
	@Transactional
	@Query(value = "INSERT into SCRAPSALESCRAPTYPES_MASTER_HISTORY(stid,scrap_typecode,scrap_typename,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date)"
			+ " select stid,scrap_typecode,scrap_typename,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE() from SCRAPSALESCRAPTYPES_MASTER where stid=:stid", nativeQuery = true)
	void UpdateScrapSaleScrapTypesHistory(String stid, String modified_by);
	/* SCRAPTYPE END */

	/* SCRAPITEM START */
	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESSCRAPITEMS_MASTER(scraptypecode,scrapitemcode,scrapitemname,created_by,created_date)"
			+ "VALUES(:scraptypecode,:scrapitemcode,:scrapitemname,:created_by,GETDATE())", nativeQuery = true)
	int addScrapSaleScrapItems(String scraptypecode, String scrapitemcode, String scrapitemname, String created_by);

	@Query(value = "SELECT siid,scraptypecode,scrapitemcode,scrapitemname,sst.stid , sst.scrap_typename, sssm.factory_id FROM SCRAPSALESSCRAPITEMS_MASTER sssm\r\n"
			+ "inner join SCRAPSALESCRAPTYPES_MASTER sst on sst.scrap_typecode = sssm.scraptypecode\r\n"
			+ "WHERE sssm.is_delete=0", nativeQuery = true)
	List<ScrapItemInterfaces> getAllScrapSaleScrapItems();

	@Query(value = "SELECT sssm.siid,sssm.scraptypecode,sssm.scrapitemcode,sssm.scrapitemname,sst.stid, sst.scrap_typename FROM SCRAPSALESSCRAPITEMS_MASTER sssm\r\n"
			+ "inner join SCRAPSALESCRAPTYPES_MASTER sst on sst.stid = sssm.scraptypecode\r\n"
			+ "WHERE sssm.is_delete=0 AND siid=:siid", nativeQuery = true)
	ScrapItemInterfaces getScrapItemsById(String siid);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESSCRAPITEMS_MASTER SET is_delete=1, modified_by =:modified_by, modified_date = GETDATE() WHERE siid=:siid", nativeQuery = true)
	int deleteScrapItemsById(String siid, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESSCRAPITEMS_MASTER SET scraptypecode=:scraptypecode,scrapitemcode=:scrapitemcode,scrapitemname=:scrapitemname,modified_by=:modified_by,modified_date=GETDATE() WHERE siid=:siid", nativeQuery = true)
	int UpdateScrapSaleScrapItems(String siid, String scraptypecode, String scrapitemcode, String scrapitemname,
			String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESSCRAPITEMS_MASTER_HISTORY(siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,modified_by,modified_date,action,transaction_date)"
			+ " select siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from SCRAPSALESSCRAPITEMS_MASTER where siid =:siid", nativeQuery = true)
	void insertScrapItemHistory(String siid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESSCRAPITEMS_MASTER_HISTORY(siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date)"
			+ " select siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE() from SCRAPSALESSCRAPITEMS_MASTER where siid =:siid", nativeQuery = true)
	void updateScrapItemHistory(String siid, String modified_by);
	/* SCRAPITEM END */

	/* SCRAP SALES ORDER START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPSALESORDERTYPE_MASTER (saleorder_typecode, saleorder_typename,created_by, created_date) VALUES (:saleorder_typecode, :saleorder_typename, :created_by, GETDATE())", nativeQuery = true)
	int addSalesOrder(String saleorder_typecode, String saleorder_typename, String created_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESORDERTYPE_MASTER  SET saleorder_typecode = :saleorder_typecode, saleorder_typename = :saleorder_typename, modified_by = :modified_by, modified_date=GETDATE() WHERE saleorder_id= :saleorder_id", nativeQuery = true)
	int updateSalesorder(String saleorder_typecode, String saleorder_typename, String modified_by, String saleorder_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAPSALESORDERTYPE_MASTER_HISTORY (saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, modified_by, modified_date, action, transaction_date) "
			+ "SELECT saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE() "
			+ "FROM SCRAPSALESORDERTYPE_MASTER WHERE saleorder_id = :saleorder_id", nativeQuery = true)
	int insertSalesOrderHistory(String saleorder_id, String modified_by);

	@Transactional
	@Query(value = "SELECT saleorder_id, saleorder_typecode, saleorder_typename FROM SCRAPSALESORDERTYPE_MASTER WHERE saleorder_id = :saleorder_id", nativeQuery = true)

	ScrapSalesOrderInterfaces findSalesOrder(String saleorder_id);

	@Transactional
	@Query(value = "SELECT saleorder_id, saleorder_typecode, saleorder_typename, factory_id FROM SCRAPSALESORDERTYPE_MASTER WHERE is_delete = 0", nativeQuery = true)
	List<ScrapSalesOrderInterfaces> getAllSalesOrders();

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAPSALESORDERTYPE_MASTER SET is_delete=1 , modified_by = :modified_by, modified_date = GETDATE() where saleorder_id = :saleorder_id ", nativeQuery = true)
	int insertDeletedRecord(String saleorder_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAPSALESORDERTYPE_MASTER_HISTORY (saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, modified_by, modified_date, action, transaction_date,deleted_by,deleted_date) "
			+ "SELECT saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, modified_by, modified_date, 'DELETE', GETDATE(),:modified_by,GETDATE() "
			+ "FROM SCRAPSALESORDERTYPE_MASTER WHERE saleorder_id = :saleorder_id", nativeQuery = true)
	int updateSalesOrderHistory(String saleorder_id, String modified_by);

	/* SCRAP SALES ORDER END */

	/* SALES LOCATION START */

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALELOCATION(location_code,location_name,created_by,created_date)"
			+ "VALUES(:location_code,:location_name,:created_by,GETDATE())", nativeQuery = true)
	int addLocationTrack(String location_code, String location_name, String created_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALELOCATION SET is_delete=1 , modified_by=:modified_by, modified_date = GETDATE() WHERE slid=:slid", nativeQuery = true)
	int deleteLocationTrackById(String slid, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALELOCATION SET location_code=:location_code,location_name=:location_name,modified_by=:modified_by,modified_date=GETDATE() WHERE is_delete=0 AND slid=:slid", nativeQuery = true)
	int UpdateLocationTrack(String slid, String location_code, String location_name, String modified_by);

	@Query(value = "SELECT slid,location_code,location_name,created_by, factory_id FROM SCRAPSALELOCATION WHERE is_delete=0", nativeQuery = true)
	List<ScrapSalesLocationInterfaces> getAllLocationTrack();

	@Query(value = "SELECT slid,location_code,location_name, factory_id FROM SCRAPSALELOCATION WHERE is_delete=0 AND slid=:slid", nativeQuery = true)
	ScrapSalesLocationInterfaces getLocationTrackById(String slid);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALELOCATION_HISTORY(slid,location_code,location_name,created_by,created_date,modified_by,modified_date,action,transaction_date)"
			+ " select slid,location_code,location_name,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from SCRAPSALELOCATION where slid = :slid", nativeQuery = true)
	void insertLocationHistory(String slid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALELOCATION_HISTORY(slid,location_code,location_name,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by, deleted_date)"
			+ " select slid,location_code,location_name,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE() from SCRAPSALELOCATION where slid = :slid", nativeQuery = true)
	void updateLocationHistory(String slid, String modified_by);

	/* SALES LOCATION END */

	/* SALES T & C START */

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SaleOrderTandC (description, created_by, created_date) \r\n"
			+ "VALUES ( :description,  :created_by, GETDATE())", nativeQuery = true)
	int addsalesTncDetailsDetails(String description, String created_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SaleOrderTandC SET description=:description, modified_by=:modified_by,modified_date=GETDATE() WHERE tcid=:tc_id", nativeQuery = true)
	int updateSalesTncDetails(String description, String modified_by, String tc_id);

	@Query(value = "SELECT tcid, description FROM SaleOrderTandC WHERE tcid=:tcid", nativeQuery = true)
	SalesOrderTandCInterfaces findSalesOrderTnCById(String tcid);

	@Query(value = "SELECT tcid, description FROM SaleOrderTandC WHERE is_delete=0", nativeQuery = true)
	List<SalesOrderTandCInterfaces> getAllSalesOrderTnc();

	@Transactional
	@Modifying
	@Query(value = "UPDATE SaleOrderTandC SET is_delete=1, modified_by =:modified_by, modified_date = GETDATE() WHERE tcid=:tcid", nativeQuery = true)
	int deleteSalesTncDetails(String tcid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SaleOrderTandC_HISTORY (tcid,sale_ordercode, slno, description, value, status, created_by, created_date, modified_by, modified_date,action, transaction_date ) \r\n"
			+ " select tcid,sale_ordercode, slno, description, value, status, created_by, created_date, :modified_by, GETDATE(),'UPDATE' , GETDATE() from SaleOrderTandC where tcid= :tcid ", nativeQuery = true)
	void insertsalesTandCHistory(String tcid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SaleOrderTandC_HISTORY (tcid,sale_ordercode, slno, description, value, status, created_by, created_date, modified_by, modified_date,action, transaction_date,deleted_by, deleted_date ) \r\n"
			+ " select tcid,sale_ordercode, slno, description, value, status, created_by, created_date, modified_by, modified_date,'DELETE',:modified_by , GETDATE() from SaleOrderTandC where tcid= :tcid", nativeQuery = true)
	void updatesalesTandCHistory(String tcid, String modified_by);

	@Query(value = "SELECT COUNT(*) from SCRAPSALESORDERTYPE_MASTER where saleorder_typename = :saleorder_typename ", nativeQuery = true)
	int checkOrderTypeExist(String saleorder_typename);

	@Query(value = "select case\r\n" + //
				"WHEN (SELECT COUNT(*) from SALE_ORDER_ENTRY where sale_order_type_id = :saleorder_id) > 0\r\n" + //
				"THEN 'The transaction already exist for Sale Order '\r\n" + //
				
				"END as message", nativeQuery = true)
	String checkSaleOrderInTransaction(String saleorder_id);
	
	@Query(value = "select case\r\n"
			+ "WHEN (SELECT COUNT(*) from SALE_ORDER_ENTRY where sale_order_type_id = :saleorder_id) > 0\r\n"
			+ "THEN 'Update Failed, Scrap Order type is used in Sale Order Transactions'\r\n"
			+ "END as message", nativeQuery = true)
	String checkSaleOrderInTransactionDuringDelete(String saleorder_id);

	@Query(value = "SELECT COUNT(*) FROM SCRAPSALESSCRAPITEMS_MASTER where scrapitemname = :scrapitemname", nativeQuery = true)
	int checkDuplicateItemCodeName(String scrapitemname);

	@Query(value = "SELECT case \r\n" + //
				"WHEN (SELECT COUNT(*) FROM SALE_ORDER_ITEM_LEVEL_ENTRY where scrapitem_id = :siid) > 0\r\n" + //
				"THEN ' The Transcation already exist for Scrap Item'\r\n" + //
				"END as message", nativeQuery = true)
	String checkScrapItemTransactions(String siid);

	@Query(value = "SELECT case \r\n"
			+ "WHEN (SELECT COUNT(*) FROM SALE_ORDER_ITEM_LEVEL_ENTRY where scrapitem_id = :siid) > 0\r\n"
			+ "THEN 'Update Failed, Scrap Item is involved in Sale Order Transactions'\r\n"
			+ "END as message", nativeQuery = true)
	String checkScrapItemTransactionsBeforeDelete(String siid);

	@Query(value = "SELECT COUNT(*) FROM SCRAPSALELOCATION where location_name = :location_name", nativeQuery = true)
	int checkForDuplicate(String location_name);

	@Query(value = "select case\r\n" + //
				"WHEN (SELECT count(*) from SALE_ORDER_ENTRY where location_type_id = :slid) > 0 \r\n" + //
				"THEN 'The Transcation already exists for Location'\r\n" + //
				
				
				"END as message", nativeQuery = true)
	String checkLocationInTransactions(String slid);

	@Query(value = "select case\r\n"
			+ "WHEN (SELECT count(*) from SALE_ORDER_ENTRY where location_type_id = :slid) > 0 \r\n"
			+ "THEN 'Update Failed, Scrap location is used in Sale Order Transactions'\r\n"
			+ "END as message", nativeQuery = true)
	String checkLocationInTransactionsDuringDelete(String slid);

	@Query(value = "SELECT COUNT(*) FROM SCRAPSALESCRAPTYPES_MASTER  where  scrap_typename = :scrap_typename", nativeQuery = true)
	int checkForDuplicates(String scrap_typename);

	@Query(value = "SELECT CASE\r\n"
			+ "WHEN (SELECT count(*) from SALE_ORDER_ITEM_LEVEL_ENTRY where scrap_type_id = :stid) > 0\r\n"
			+ "THEN 'The Transcation already exists for Scrap Type'\r\n"
			
			
			+ "END as message", nativeQuery = true)
	String checkScrapTypeInTransactions(String stid);

	@Query(value = "SELECT CASE\r\n"
			+ "WHEN (SELECT count(*) from SALE_ORDER_ITEM_LEVEL_ENTRY where scrap_type_id = :stid) > 0\r\n"
			+ "THEN 'Update Failed, ScrapType is already used in Sale Order Transactions'\r\n"
			+ "END as message", nativeQuery = true)
	String checkScrapTypeInTransactionsDuringDelete(String stid);

	@Query(value = "SELECT TOP 1 scrapitemcode  from SCRAPSALESSCRAPITEMS_MASTER order by scrapitemcode DESC", nativeQuery = true)
    String getLastScrapItemFromDatabase();

	@Query(value = "SELECT TOP 1 saleorder_typecode from SCRAPSALESORDERTYPE_MASTER order by\r\n" + //
				"saleorder_typecode DESC", nativeQuery = true)
	String getLastScrapSaleOrderTypeCode();

	@Query(value = "SELECT TOP 1 scrap_typecode from SCRAPSALESCRAPTYPES_MASTER order by\r\n" + //
				"scrap_typecode DESC", nativeQuery = true)
	String getLastScrapTypeFromDatabase();

	@Query(value = "SELECT TOP 1 location_code from  SCRAPSALELOCATION order by location_code DESC", nativeQuery = true)
	String getLastScrapLocationCodeFromDatabase();

	/* SALES T&C END */
}
