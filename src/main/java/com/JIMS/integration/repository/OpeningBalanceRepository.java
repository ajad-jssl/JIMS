package com.JIMS.integration.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.ContractListFromContractInterfaces;
import com.JIMS.integration.interfaces.OpeningBalanceInterfaces;

import jakarta.transaction.Transactional;

public interface OpeningBalanceRepository extends JpaRepository<User, Integer> {

	@Transactional
	@Modifying
	@Query(value="INSERT INTO OPENING_BALANCE (con_id,load_id,created_by,created_date) Values (:con_id,:load_id,:created_by,GETDATE())", nativeQuery = true)
	int createOpeningBalanceRecord(String con_id, String load_id, String created_by);

//	@Transactional
//	@Modifying
//	@Query(value="INSERT INTO OPENING_BALANCE_ITEM (pn_id,description,total,tax_total,net_total,created_by,created_date,avl_bal,nontax_avl_bal) Values (:pn_id,:description,:total,:tax_total,:net_total,:created_by,GETDATE(),:total,:nontax_avl_bal)", nativeQuery = true)
//	void createOpeningBalanceItemRecord(int pn_id,String description, String total, String tax_total,String net_total, String created_by, String nontax_avl_bal);
	
	
	
	
	
	
	@Modifying
	@Transactional
	@Query(value = """
		     SELECT TOP 1
		        item.total,
		        item.tax_total,
		        item.avl_bal,
		        item.nontax_avl_bal,
				item.pn_Id
		    FROM OPENING_BALANCE_ITEM item
		    INNER JOIN OPENING_BALANCE opens
		        ON item.pn_id = opens.pn_id
		    WHERE opens.con_id =:con_id
		    ORDER BY item.created_date DESC
		""", nativeQuery = true)
		List<Object[]> getLatestBalance(String con_id);
	
	
	@Modifying
	@Transactional
	@Query(value = """
		     SELECT TOP 1
		        item.total,
		        item.tax_total,
		        item.avl_bal,
		        item.nontax_avl_bal,
				item.pn_Id
		    FROM OPENING_BALANCE_ITEM item
		    INNER JOIN OPENING_BALANCE opens
		        ON item.pn_id = opens.pn_id and item.pn_id !=:pn_id
		    WHERE opens.con_id =:con_id
		    ORDER BY item.created_date DESC
		""", nativeQuery = true)
		List<Object[]> getLatestBalancess(String con_id ,String pn_id);

		
		
		
		@Transactional
		@Modifying
		@Query(value = "UPDATE OPENING_BALANCE_ITEM SET latest_flag = 1 WHERE pn_id = :pn_id", nativeQuery = true)
		void updateLatestFlag(@Param("pn_id") int pn_id);


	
	
	@Transactional
	@Modifying
	@Query(value = """
	INSERT INTO OPENING_BALANCE_ITEM
	(pn_id, description, total, tax_total, net_total,
	 created_by, created_date, avl_bal, nontax_avl_bal,
	 utr_reference, proforma_reference, txn_date)
	VALUES
	(:pn_id, :description, :total, :tax_total, :net_total,
	 :created_by, GETDATE(), :avl_bal, :nontax_avl_bal,
	 :utr_reference, :proforma_reference, :txn_date)
	""", nativeQuery = true)
	void createOpeningBalanceItemRecord(
		    int pn_id,
		    String description,
		    String total,
		    String tax_total,
		    String net_total,
		    String created_by,
		    String avl_bal,
		    String nontax_avl_bal,
		    String utr_reference,
		    String proforma_reference,
		    LocalDate txn_date
		);
	
	
	
	
	
	
	


	@Transactional
	@Modifying
	@Query(value="UPDATE OPENING_BALANCE SET con_id = :con_id, modified_by = :modified_by, modified_date = GETDATE() WHERE pn_id = :pn_id   ", nativeQuery = true)
	int updateOpeningBalanceRecord(String con_id,  String modified_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value=" UPDATE OPENING_BALANCE_ITEM SET pn_id = :pn_id,description =:description,total= :total,tax_total=:tax_total,net_total=:net_total, modified_by = :modified_by, modified_date = GETDATE() WHERE slno = :slno", nativeQuery = true)
	void updateOpeningBalanceItemRecord(String pn_id, String description, String total, String tax_total,String net_total, String modified_by, String slno);


	@Query(value = "select \r\n"
			+ "    apni.description,\r\n"
			+ "    apni.utr_reference,\r\n"
			+ "    apni.txn_date,\r\n"
			+ "    apni.proforma_reference,\r\n"
			+ "    apni.total,\r\n"
			+ "    apni.net_total as net_total,\r\n"
			+ "    apnb.nontax_avl_bal,\r\n"
			+ "    apni.tax_total,\r\n"
			+ "    apni.avl_bal,\r\n"
			+ "    apn.con_id,\r\n"
			+ "    apni.created_by,\r\n"
			+ "    apni.created_date,\r\n"
			+ "    LEFT(cm.contract_name, CHARINDEX('-', cm.contract_name) - 1) AS contract_name,\r\n"
			+ "    apni.factory_id,\r\n"
			+ "    apn.pn_id\r\n"
			+ "from OPENING_BALANCE_ITEM apni\r\n"
			+ "inner join OPENING_BALANCE apn on apn.pn_id = apni.pn_id\r\n"
			+ "inner join OPENING_BALANCE_ITEM apnb on apnb.pn_id = apni.pn_id\r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = apn.con_id\r\n"
			+ "where apni.is_delete = 0\r\n"
			+ "order by apni.created_date desc;", nativeQuery = true)
	List<OpeningBalanceInterfaces> getOpeningBalanceList();
	
	
	@Query(value ="select apni.description,apni.slno, apni.total, apni.net_total, apni.tax_total, apn.con_id  from OPENING_BALANCE_ITEM apni\r\n"
			+ "inner join OPENING_BALANCE apn on apn.pn_id = apni.pn_id where apni.is_delete = 0 and apni.slno = :slno", nativeQuery = true)
	OpeningBalanceInterfaces searchOpeningBalanceById(String slno);
	
	@Query(value = """
		    SELECT 
		        oe.pn_id,
		        ctm.contract_name,
		        oi.total,
		        oi.avl_bal,
		        oi.tax_total,
		        oi.nontax_avl_bal,
		        ctm.contract_id,
		        oi.utr_reference,
		        oi.proforma_reference,
		        oi.txn_date,
		        oi.description
		    FROM OPENING_BALANCE oe
		    INNER JOIN CONTRACT_MASTER ctm 
		        ON ctm.contract_id = oe.con_id
		    INNER JOIN OPENING_BALANCE_ITEM oi 
		        ON oi.pn_id = oe.pn_id
		    WHERE oe.pn_id = :pnId
		""", nativeQuery = true)
    OpeningBalanceInterfaces getOpeningBalanceById(String pnId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE OPENING_BALANCE_ITEM " +
	        "SET total = ?, tax_total = ?, avl_bal = ?, nontax_avl_bal = ?, " +
	        "utr_reference = ?, proforma_reference = ?, txn_date = ?, description = ?, " +
	        "ob_modified_by = ?, ob_modified_date = GETDATE() " +  //  added
	        "WHERE pn_id = ?", nativeQuery = true)
	int updateOpeningBalanceItem(
	        String total,
	        String tax_total,
	        String avl_bal,
	        String nontax_avl_bal,
	        String utr_reference,
	        String proforma_reference,
	        LocalDate txn_date,
	        String description,
	        String ob_modified_by,  //  renamed from modified_by
	        String pn_id            //  pn_id must be LAST (matches WHERE ?)
	);

	 @Modifying
	    @Transactional
	    @Query(value = "UPDATE OPENING_BALANCE " +
	                   "SET modified_by = ?, modified_date = GETDATE() " +
	                   "WHERE pn_id = ?", nativeQuery = true)
	    int updateOpeningBalanceMain(String modified_by, String pn_id);
	 
	 @Query(value = "SELECT taxable, non_taxable FROM CONTRACT_MASTER WHERE contract_id = :contract_name",
		       nativeQuery = true)
		Map<String, Object> getContractDetails(String contract_name);
	 
	 
	 
	 
	 @Query(value="select count(*) from OPENING_BALANCE_ITEM where latest_flag = 1 and pn_id =:pn_id",nativeQuery = true)
	 int countforOldopeningBalance(String pn_id);
	 
	 
//	 
//	 @Query(value = """
//			    SELECT COUNT(*)
//			    FROM OPENING_BALANCE_ITEM item
//			    INNER JOIN OPENING_BALANCE opens
//			        ON item.pn_id = opens.pn_id
//			    WHERE opens.con_id = :con_id
//			      AND item.latest_flag = 0
//			      AND item.is_used = 0
//			""", nativeQuery = true)
//			int countUnusedOpeningBalance(@Param("con_id") String conId);
	 
//	 @Query(value = """
//			    SELECT
//			        COALESCE(
//			            (
//			                SELECT SUM(CASE WHEN verified_status = 1 THEN 1 ELSE 0 END)
//			                FROM INVOICE_MASTER
//			                WHERE contract_id = :con_id
//			                  AND Cancel IS NULL
//			                  AND is_release <> 1
//			            ), 0
//			        )
//			        +
//			        COALESCE(
//			            (
//			                SELECT SUM(CASE WHEN verified_status = 1 THEN 1 ELSE 0 END)
//			                FROM DEBIT_CREDIT_INVOICE_MASTER
//			                WHERE con_id = :con_id
//			                  AND Cancel IS NULL
//			                  AND is_release <> 1
//			            ), 0
//			        )
//			    """, nativeQuery = true)
//			int countUnusedOpeningBalance(@Param("con_id") String conId);
	 
	 
	 
	 
	 @Query(value = """
		        SELECT
		            CASE
		                WHEN (
		                    SELECT modified_date 
		                    FROM OPENING_BALANCE_ITEM 
		                    WHERE pn_id = :pn_id
		                ) IS NULL 
		                THEN 0
		                ELSE
		                    COALESCE(
		                        (
		                            SELECT SUM(CASE WHEN verified_status = 1 THEN 1 ELSE 0 END)
		                            FROM INVOICE_MASTER
		                            WHERE contract_id = :con_id
		                              AND Cancel IS NULL
		                              AND is_release <> 1
		                        ), 0
		                    )
		                    +
		                    COALESCE(
		                        (
		                            SELECT SUM(CASE WHEN verified_status = 1 THEN 1 ELSE 0 END)
		                            FROM DEBIT_CREDIT_INVOICE_MASTER
		                            WHERE con_id = :con_id
		                              AND Cancel IS NULL
		                              AND is_release <> 1
		                        ), 0
		                    )
		            END
		        """, nativeQuery = true)
		int countUnusedOpeningBalance(
		    @Param("con_id") String conId,
		    @Param("pn_id") String pnId
		);
	 @Query(value = "SELECT c.contract_id, c.contract_name FROM CONTRACT_MASTER c  " +
             "WHERE NOT EXISTS (" +
             "   SELECT 1 FROM OPENING_BALANCE ob " +
             "   WHERE ob.con_id = c.contract_id AND ob.is_delete = 0" +
             ")",
     nativeQuery = true)
List<Map<String, Object>> findAvailableContracts();
	
	@Query(value =  "select contract_id, contract_name from  CONTRACT_MASTER where is_delete = 0", nativeQuery = true)
	List<ContractListFromContractInterfaces> getContractListFromContractInfo();
}