package com.JIMS.integration.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.TaxMasterInterface;

import jakarta.transaction.Transactional;

public interface TaxMasterRepository extends JpaRepository<User, Integer> {
	@Transactional
	@Modifying
	@Query(value = "INSERT into TAX_MASTER(tax_name,tax_per,startdate,enddate,created_by,created_date,tax_desc)"
			+ "VALUES(:tax_name,:tax_per,:startdate,:enddate,:created_by,GETDATE(),:tax_desc)", nativeQuery = true)
	int addTaxMasters(String tax_name, String tax_per, String startdate, String enddate, String created_by,
			String tax_desc);

	@Transactional
	@Modifying
	@Query(value = "INSERT into TAX_MASTER(tax_name,tax_per,startdate,enddate,created_by,created_date,tax_desc)"
			+ "VALUES(:tax_name,:tax_per,:startdate,:enddate,:created_by,GETDATE(),:tax_desc)", nativeQuery = true)
	int addTaxMastersNew(String tax_name, String tax_per, String startdate, String enddate, String created_by,
			String tax_desc);

	@Query(value = "SELECT * FROM TAX_MASTER WHERE is_delete=0 AND (enddate IS NULL OR enddate >= CAST(GETDATE() AS DATE))", nativeQuery = true)
	List<TaxMasterInterface> getAllTaxMasters();

	@Query(value = "SELECT tax_id,tax_name,tax_per,startdate,enddate,tax_desc FROM TAX_MASTER WHERE is_delete=0 AND tax_id=:tax_id", nativeQuery = true)
	TaxMasterInterface getTaxMastersById(String tax_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE TAX_MASTER SET is_delete=1 , modified_by =:modified_by, modified_date = GETDATE() WHERE tax_id=:tax_id", nativeQuery = true)
	int deleteTaxMastersById(String tax_id, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE TAX_MASTER SET tax_name=:tax_name,tax_per=:tax_per,startdate=:startdate,enddate=:enddate,modified_by=:modified_by,modified_date=GETDATE(),tax_desc=:tax_desc WHERE tax_id=:tax_id", nativeQuery = true)
	int UpdateTaxMasters(String tax_name, String tax_per, String startdate, String enddate, String modified_by,
			String tax_id, String tax_desc);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO TAX_MASTER_HISTORY (tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,modified_by,modified_date,action,transaction_date) select tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from TAX_MASTER WHERE tax_id=:tax_id", nativeQuery = true)
	int insertTaxMasterHistory(String modified_by, String tax_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO TAX_MASTER_HISTORY (tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,modified_by,modified_date,action,transaction_date,deletd_by,deleted_date) select tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,modified_by,modified_date,'UPDATE',GETDATE(),:modified_by,GETDATE() from TAX_MASTER WHERE tax_id=:tax_id", nativeQuery = true)
	int updateTaxMasterHistory(String modified_by, String tax_id);

//	@Query(value = "select case\r\n" + //
//				"WHEN (SELECT COUNT(*) FROM SALE_ORDER_ENTRY where (tax1 = :tax_id or tax2  = :tax_id or tax3 = :tax_id)) > 0\r\n" + //
//				"THEN 'TAX is  involed in SCRAP SALE ORDER transaction'\r\n" + //
//				"WHEN (select count(*) from CONTRACT_ASSIGN_TAX where tax_id=:tax_id)>0\r\n" + //
//				"Then 'TAX is  involed in Contract Assign  transaction'\\r\\n" + //
//				"WHEN (SELECT COUNT(*) FROM TAX_MASTER where tax_name = :tax_name AND tax_per = :tax_per and tax_id <>:tax_id) > 0\r\n" + //
//				"THEN ' Tax details Already Exists'\r\n" + //
//				"END as message", nativeQuery = true)
//	String checkWhetherTaxIsUsedInTransactions(String tax_name, String tax_per, String tax_id);
	
	
	
	
	@Query(value =
	        "SELECT CASE " +
	        "WHEN (SELECT COUNT(*) FROM SALE_ORDER_ENTRY " +
	        "      WHERE (tax1 = :tax_id OR tax2 = :tax_id OR tax3 = :tax_id)) > 0 " +
	        "THEN 'TAX is involed in SCRAP SALE ORDER transaction' " +

	        "WHEN (SELECT COUNT(*) FROM CONTRACT_ASSIGN_TAX " +
	        "      WHERE tax_id = :tax_id) > 0 " +
	        "THEN 'TAX is involed in Contract Assign transaction' " +

	    

	        "ELSE NULL END AS message",
	        nativeQuery = true)
	String checkWhetherTaxIsUsedInTransactions(

	        @Param("tax_id") String tax_id
	);
	
	
	@Query(value ="	SELECT COUNT(*) FROM TAX_MASTER  WHERE tax_name = :tax_name AND tax_per = :tax_per AND tax_id <> :tax_id",   
	        nativeQuery = true)
	int checkWhetherTaxUsed(
	        @Param("tax_name") String tax_name,
	        @Param("tax_per") String tax_per,
	        @Param("tax_id") String tax_id
	);

	


	@Query(value = "select case\r\n"
			+ "WHEN (SELECT COUNT(*) FROM SALE_ORDER_ENTRY where (tax1 = :tax_id or tax2  = :tax_id or tax3 = :tax_id)) > 0 \r\n"
			+ "THEN 'Update failed, TAX is  involed in SCRAP SALE ORDER transaction'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWhetherTaxIsUsedInTransactionsBeforeDelete(String tax_id);

	@Query(value = "SELECT count(*) from TAX_MASTER where tax_name = :tax_name AND tax_per = :tax_per", nativeQuery = true)
	int checkTaxExistOrNot(String tax_name, String tax_per);
	
	@Query(value = "SELECT tax_name, tax_per, startdate, enddate, tax_desc FROM TAX_MASTER WHERE tax_id = :tax_id", nativeQuery = true)
	Map<String, Object> getTaxById(String tax_id);
	

}
