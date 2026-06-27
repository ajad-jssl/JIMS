package com.JIMS.integration.repository;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.ConsolidatedQSPacking;
import com.JIMS.integration.interfaces.ConsolidateInvoice;
import com.JIMS.integration.interfaces.QSPackingInterfaces;
import com.JIMS.integration.interfaces.consolidatedQSPacking_Interfaces;

import jakarta.transaction.Transactional;

	public interface ConsolidatedQSPackingRepository extends JpaRepository<ConsolidatedQSPacking, Integer>{


@Transactional
@Modifying
@Query(value = "INSERT INTO CONSOLIDATED_PACKING_ITEMS(qty,per_kgs,unit_price,total,UOM,created_by,Conpn_id,inc_type,created_date,factory_id) "
		+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM,:created_by, :pn_id, :type_id ,GETDATE(),:factory_id)",nativeQuery = true)
int insertconsolPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM,
		String type_id, String created_by,String pn_id,int factory_id);
		
@Query(value = "select top 1 conpn_id from CONSOLIDATED_PACKING_NOTE order by conpn_id DESC", nativeQuery = true)
String findLastconsolLoad();

@Query(value = "	\r\n"
		+ "    select distinct qm.grand_total,qm.Conpn_id,qm.con_id,qm.load_id,qm.vechile_no,cm.contract_name,   \r\n"
		+ "		   	qim.qty,qim.per_kgs,qim.unit_price,qim.total,qm.transport_name,qim.Conslno,qim.inc_type,qim.UOM,   \r\n"
		+ "		   	sm.scrap_name as scrap_name_type_id,sm.type_id,mm.milestone_name,mm.milestone_id,it.type as inc_type_name    \r\n"
		+ "		   	from CONSOLIDATED_PACKING_NOTE qm     \r\n"
		+ "		   	left join CONSOLIDATED_PACKING_ITEMS qim on qim.Conpn_id = qm.Conpn_id     \r\n"
		+ "		   	inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.inc_type     \r\n"
		+ "		   	inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id     \r\n"
		+ "		   	left join INVOICE_TYPE_MASTER it on it.type_no = qim.inc_type   \r\n"
		+ "		   	inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id    \r\n"
		+ "		   	where qim.Conpn_id = :pn_id and qim.is_delete = 0 and qm.is_delete = 0", nativeQuery = true)
List<consolidatedQSPacking_Interfaces> searchconsolidatedPackingById(String pn_id);

@Query(value="  SELECT \r\n"
		+ "    cii.invid as id,cii.pn_id as pn_id,\r\n"
		+ "    im.invoice_no\r\n"
		+ "FROM consolidated_invoice_items cii\r\n"
		+ "JOIN invoice_master im \r\n"
		+ "    ON cii.invid = im.id\r\n"
		+ "WHERE cii.consol_inv_no =:load_id and is_deleted=0",nativeQuery = true)
List<ConsolidateInvoice> SelectedInvoices(String load_id);






@Query(value ="select qim.load_id from CONSOLIDATED_PACKING_NOTE qim  where  qim.Conpn_id =:pn_id", nativeQuery = true)
String serachcnid(String pn_id);



@Query(value = "select CONCAT(mm.milestone_code ,+' - ',+ mm.milestone_name ) as milestone_name,cm.contract_name,qm.Conpn_id,case when qm.is_locked=0 then 'INOVICE NOT GENERATED' else 'INVOICE GENERTATED' END AS INVGEN\r\n"
		+ "		  from CONSOLIDATED_PACKING_NOTE qm   \r\n"
		+ "		  inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id   \r\n"
		+ "		  inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id  \r\n"
		+ "		  where qm.factory_id = :factory_id AND qm.is_delete = 0 AND (qm.Cancel = 0 OR qm.Cancel IS NULL) order by qm.created_date desc", nativeQuery = true)
List<consolidatedQSPacking_Interfaces> listconsolidatedPAckingMasterRecord(String factory_id);



//@Query(
//
//value =
//"SELECT CONCAT(mm.milestone_code,' - ', mm.milestone_name) AS milestone_name, " +
//"cm.contract_name, qm.Conpn_id, " +
//
//"CASE WHEN qm.is_locked = 0 THEN 'INVOICE NOT GENERATED' " +
//"ELSE 'INVOICE GENERATED' END AS INVGEN " +
//
//"FROM CONSOLIDATED_PACKING_NOTE qm " +
//
//"INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
//"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +
//
//"WHERE qm.factory_id = :factory_id " +
//"AND qm.is_delete = 0 " +
//"AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +
//
//"AND ( :search IS NULL OR :search = '' OR " +
//
//"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//"LOWER(mm.milestone_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//"LOWER(mm.milestone_code) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//"LOWER(qm.Conpn_id) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//
//"CONVERT(VARCHAR, qm.created_date, 105) LIKE CONCAT('%',:search,'%') " +
//
//") " +
//
//"ORDER BY qm.created_date DESC",
//
//countQuery =
//"SELECT COUNT(*) " +
//
//"FROM CONSOLIDATED_PACKING_NOTE qm " +
//"INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id " +
//"INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +
//
//"WHERE qm.factory_id = :factory_id " +
//"AND qm.is_delete = 0 " +
//"AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +
//
//"AND ( :search IS NULL OR :search = '' OR " +
//
//"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//"LOWER(mm.milestone_name) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//"LOWER(mm.milestone_code) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//"LOWER(qm.Conpn_id) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
//
//"CONVERT(VARCHAR, qm.created_date, 105) LIKE CONCAT('%',:search,'%') " +
//
//")",
//
//nativeQuery = true
//)
//Page<consolidatedQSPacking_Interfaces> listconsolidatedPAckingMasterRecordss(
//        @Param("factory_id") String factory_id,
//        @Param("search") String search,
//        Pageable pageable
//);






@Query(value = "SELECT CONCAT(mm.milestone_code,' - ', mm.milestone_name) AS milestone_name, "
		+ "cm.contract_name, qm.Conpn_id, qm.load_id, " + //  added load_id

		"CASE WHEN qm.is_locked = 0 THEN 'INVOICE NOT GENERATED' " + "ELSE 'INVOICE GENERATED' END AS INVGEN " +

		"FROM CONSOLIDATED_PACKING_NOTE qm " +

		"INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id "
		+ "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +

		"WHERE qm.factory_id = :factory_id " + "AND qm.is_delete = 0 " + "AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +

		"AND ( :search IS NULL OR :search = '' OR " +

		"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR "
		+ "LOWER(mm.milestone_name) LIKE LOWER(CONCAT('%',:search,'%')) OR "
		+ "LOWER(mm.milestone_code) LIKE LOWER(CONCAT('%',:search,'%')) OR "
		+ "LOWER(CAST(qm.Conpn_id AS VARCHAR)) LIKE LOWER(CONCAT('%',:search,'%')) OR "
		+ "LOWER(qm.load_id) LIKE LOWER(CONCAT('%',:search,'%')) OR " + //  search by load_id

		"CONVERT(VARCHAR, qm.created_date, 105) LIKE CONCAT('%',:search,'%') " +

		") " +

		"ORDER BY qm.created_date DESC",

		countQuery = "SELECT COUNT(*) " +

				"FROM CONSOLIDATED_PACKING_NOTE qm "
				+ "INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = qm.milestone_id "
				+ "INNER JOIN CONTRACT_MASTER cm ON cm.contract_id = qm.con_id " +

				"WHERE qm.factory_id = :factory_id " + "AND qm.is_delete = 0 "
				+ "AND (qm.Cancel = 0 OR qm.Cancel IS NULL) " +

				"AND ( :search IS NULL OR :search = '' OR " +

				"LOWER(cm.contract_name) LIKE LOWER(CONCAT('%',:search,'%')) OR "
				+ "LOWER(mm.milestone_name) LIKE LOWER(CONCAT('%',:search,'%')) OR "
				+ "LOWER(mm.milestone_code) LIKE LOWER(CONCAT('%',:search,'%')) OR "
				+ "LOWER(CAST(qm.Conpn_id AS VARCHAR)) LIKE LOWER(CONCAT('%',:search,'%')) OR "
				+ "LOWER(qm.load_id) LIKE LOWER(CONCAT('%',:search,'%')) OR " + //  search by load_id

				"CONVERT(VARCHAR, qm.created_date, 105) LIKE CONCAT('%',:search,'%') " +

				")",

		nativeQuery = true)
Page<consolidatedQSPacking_Interfaces> listconsolidatedPAckingMasterRecordss(@Param("factory_id") String factory_id,
		@Param("search") String search, Pageable pageable);




@Query(value = """
 select invid from consolidated_invoice_items   WHERE consol_inv_no =:load_id  AND is_deleted = 0 
AND cancel IS NULL
""", nativeQuery = true)
List<String> getExistingInvoices(@Param("load_id") String load_id);

//@Modifying
//@Transactional
//@Query(value = "UPDATE CONSOLIDATED_PACKING_ITEMS SET " +
//        "total = :total, " +
//        "unit_price = :unit_price, " +
//        "modified_by = :modified_by, " +
//        "modified_date = getdate(), " +
//        "inc_type = :type_id"  + 
//        "WHERE Conpn_id = :pn_id " +
//        "AND (total <> :total " +
//        "OR unit_price <> :unit_price " +
//        "OR inc_type <> :type_id)",
//        nativeQuery = true)
//int updatePackingItems(
//         String total,
//         String unit_price,
//        String type_id,
//        String pn_id,
//        String modified_by
//);

@Modifying
@Transactional
@Query(value = "UPDATE CONSOLIDATED_PACKING_ITEMS SET " +
        "total = :total, " +
        "unit_price = :unit_price, " +
        "modified_by = :modified_by, " +
        "modified_date = getdate(), " +
        "inc_type = :type_id, " +
        "per_kgs = :per_kgs, " +
        "qty = :qty " +
        "WHERE Conpn_id = :pn_id " +
        "AND (total <> :total " +
        "OR unit_price <> :unit_price " +
        "OR inc_type <> :type_id " +
        "OR per_kgs <> :per_kgs " +
        "OR qty <> :qty)",
        nativeQuery = true)
int updatePackingItems(
        String total,
        String unit_price,
        String type_id,
        String pn_id,
        String modified_by,
        String per_kgs,
        String qty
);
@Modifying
@Transactional
@Query(value = "UPDATE CONSOLIDATED_PACKING_NOTE SET " +
        "transport_name = :transport_name, " +
        "vechile_no = :vechile_no, " +
        "modified_by = :modified_by, " +
        "modified_date = getdate(), " +
        "grand_total = :total " +
        "WHERE Conpn_id = :pn_id " +
        "AND (transport_name <> :transport_name " +
        "OR vechile_no <> :vechile_no " +
        "OR grand_total <> :total)",
        nativeQuery = true)
int updatePackingNote(
        String transport_name,
        String vechile_no,
        String total,
         String pn_id,
         String modified_by
);




@Modifying
@Transactional
@Query(value = """
UPDATE consolidated_invoice_items
SET is_deleted = 1
WHERE consol_inv_no = :loadId
AND invid = :invId
""", nativeQuery = true)
void markInvoiceDeleted(@Param("loadId") String loadId,
                        @Param("invId") String invId);



@Modifying
@Transactional
@Query(value = """
INSERT INTO consolidated_invoice_items
(consol_inv_no, invid, qty, per_kgs, unit_price, total, UOM, is_deleted,slno,pn_id,stype,conpnid)
VALUES (:loadId,:invId,:qty,:perKgs,:unitPrice,:total,:uom,0,:slno,:pn_id,:stype,:con_id)
""", nativeQuery = true)
void insertInvoiceItem(String loadId,String invId,String qty,String perKgs,String unitPrice,String total,String uom,String slno,String pn_id,String stype,String con_id);


}
