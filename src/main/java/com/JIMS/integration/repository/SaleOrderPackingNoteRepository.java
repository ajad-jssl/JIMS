package com.JIMS.integration.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.JIMS.integration.entity.SaleOrderPackingNote;
import com.JIMS.integration.interfaces.AllScrapingNoteDetails;
import com.JIMS.integration.interfaces.LatestScrapNoteBalanceInterface;
import com.JIMS.integration.interfaces.SaleOrderInterface;
import com.JIMS.integration.interfaces.SaleOrderItemDescLevelInterface;
import com.JIMS.integration.interfaces.SaleOrderItemLevelInterface;
import com.JIMS.integration.interfaces.SalePackingNoteInterface;
import com.JIMS.integration.interfaces.SalePackingNoteItemsInterface;
import com.JIMS.integration.interfaces.ScrapInvoiceInfoInterface;
import com.JIMS.integration.interfaces.ScrapInvoiceItemsoInterface;
import com.JIMS.integration.interfaces.UnverifiedInvoiceListInterface;
import com.JIMS.integration.interfaces.VerifiedPackingNoteInterface;
import com.JIMS.integration.interfaces.allSaleOrderEntriesInterface;

public interface SaleOrderPackingNoteRepository extends JpaRepository<SaleOrderPackingNote, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAP_PACKING_NOTE_ITEMS (scp_pn_id, sale_order_code, scp_load, scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, total_ordered, total_sold, balance, created_by, created_date) "
			+ "VALUES (:saleOrder_pn_id, :sale_order_code, :scp_load,  :scrap_type_id,  :scrapitem_id, :uom_id, :quantity, :kg, :unit_price, :total, :total_ordered, :total_sold, :balance, :pn_items_created_by, :pn_items_created_date)", nativeQuery = true)

	int addScrapPackingNoteItems(int saleOrder_pn_id, String sale_order_code, String scp_load, String scrap_type_id,
			String scrapitem_id, String uom_id, String quantity, String kg, String unit_price, String total,
			String total_ordered, String total_sold, String balance, String pn_items_created_by,
			LocalDateTime pn_items_created_date);

	@Query(value = "select spn.* from SCRAP_PACKING_NOTE spn where spn.scp_load = :scp_load and spn.is_delete = 0\r\n"
			+ "", nativeQuery = true)
	SalePackingNoteInterface getScrapPackingNoteDetailsForParticularSaleOrder(String scp_load);

	@Query(value = "select spni.*, stm.scrap_type, sim.scrapitemname, u.unit_name from SCRAP_PACKING_NOTE_ITEMS spni\r\n"
			+ "left outer  join SCRAPSALESCRAPTYPES_MASTER stm on stm.stid = spni.scrap_type_id\r\n"
			+ "left outer join SCRAPSALESSCRAPITEMS_MASTER sim on sim.siid = spni.scp_pnitems_id\r\n"
			+ "left outer join UOM_MASTER u on u.unit_id = spni.uom_id\r\n"
			+ "where spni.scp_load = :scp_load and spni.is_delete = 0", nativeQuery = true)
	SalePackingNoteItemsInterface getScrapPackingNoteItemsDetails(String scp_load);

	@Query(value = "select spni.*, stm.scrap_typename as scrap_type, sim.scrapitemname, u.unit_name from SCRAP_PACKING_NOTE_ITEMS spni\r\n"
			+ "left outer  join SCRAPSALESCRAPTYPES_MASTER stm on stm.stid = spni.scrap_type_id\r\n"
			+ "left outer join SCRAPSALESSCRAPITEMS_MASTER sim on sim.siid = spni.scrapitem_id\r\n"
			+ "left outer join UOM_MASTER u on u.unit_id = spni.uom_id\r\n"
			+ "where spni.scp_load = :scp_load and spni.is_delete = 0", nativeQuery = true)
	List<SalePackingNoteItemsInterface> getScrapPackingNoteItemsDetailsForScp_load(String scp_load);

	@Query(value = "SELECT sale_order_code from SCRAP_PACKING_NOTE where scp_load = :scp_load", nativeQuery = true)
	String getSaleOrderCode(String scp_load);

	/*
	 * @Query(value = "SELECT TOP 1\r\n" + "    soe.sale_order_code, \r\n" +
	 * "    COALESCE(spni.scp_load, NULL) AS scp_load, \r\n" +
	 * "    COALESCE(spni.total_ordered, soei.kg) AS total_ordered, \r\n" +
	 * "    COALESCE(spni.total_sold, 0) AS total_sold, \r\n" +
	 * "    COALESCE(spni.balance, soei.kg - COALESCE(spni.total_sold, 0)) AS balance, \r\n"
	 * +
	 * "    COALESCE(spni.balance, soei.kg - COALESCE(spni.total_sold, 0)) AS dummy_balance, \r\n"
	 * + "    spni.created_date, \r\n" + "    soei.tolerance,  \r\n" +
	 * "    ROUND(\r\n" +
	 * "        (soei.kg + (soei.kg * soei.tolerance / 100.0)) - \r\n" +
	 * "        (SELECT COALESCE(SUM(spni_inner.total_sold), 0) \r\n" +
	 * "         FROM SCRAP_PACKING_NOTE_ITEMS spni_inner\r\n" +
	 * "         WHERE spni_inner.sale_order_code = soe.sale_order_code), \r\n" +
	 * "        2\r\n" + "    ) AS tolerance_value\r\n" +
	 * "FROM SALE_ORDER_ENTRY soe\r\n" +
	 * "INNER JOIN SALE_ORDER_ITEM_LEVEL_ENTRY soei ON soei.soe_id = soe.soe_id\r\n"
	 * +
	 * "LEFT JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.sale_order_code = soe.sale_order_code\r\n"
	 * + "WHERE soe.sale_order_code = :sale_order_code\r\n" +
	 * "ORDER BY scp_load DESC;\r\n" + "", nativeQuery = true)
	 * List<LatestScrapNoteBalanceInterface> getLastestBalanceInfo(String
	 * sale_order_code);
	 */

//	@Query(value = "SELECT TOP 1 soe.sale_order_code, \r\n" + "       COALESCE(spni.scp_load, NULL) AS scp_load, \r\n"
//			+ "       COALESCE(spni.total_ordered, \r\n" + "                CASE \r\n"
//			+ "                    WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg \r\n"
//			+ "                    ELSE soei.non_steel_kgs \r\n" + "                END) AS total_ordered, \r\n"
//			+ "       COALESCE(spni.total_sold, 0) AS total_sold, \r\n" + "       COALESCE(spni.balance, \r\n"
//			+ "                CASE \r\n"
//			+ "                    WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg \r\n"
//			+ "                    ELSE soei.non_steel_kgs \r\n"
//			+ "                END - COALESCE(spni.total_sold, 0)) AS balance, \r\n"
//			+ "       COALESCE(spni.balance, \r\n" + "                CASE \r\n"
//			+ "                    WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg \r\n"
//			+ "                    ELSE soei.non_steel_kgs \r\n"
//			+ "                END - COALESCE(spni.total_sold, 0)) AS dummy_balance, \r\n"
//			+ "       spni.created_date, soei.tolerance,  \r\n" + "       ROUND((\r\n" + "             CASE \r\n"
//			+ "                 WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN (soei.kg + (soei.kg * soei.tolerance / 100.0)) \r\n"
//			+ "                 ELSE (soei.non_steel_kgs + (soei.non_steel_kgs * soei.tolerance / 100.0)) \r\n"
//			+ "             END\r\n" + "             ) - \r\n"
//			+ "             (SELECT COALESCE(SUM(spni_inner.total_sold), 0) \r\n"
//			+ "              FROM SCRAP_PACKING_NOTE_ITEMS spni_inner \r\n"
//			+ "              WHERE spni_inner.sale_order_code = soe.sale_order_code), 2) AS tolerance_value  \r\n"
//			+ "FROM SALE_ORDER_ENTRY soe \r\n"
//			+ "LEFT JOIN SALE_ORDER_ITEM_LEVEL_ENTRY soei ON soei.soe_id = soe.soe_id \r\n"
//			+ "LEFT JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.sale_order_code = soe.sale_order_code \r\n"
//			+ "WHERE soe.sale_order_code = :sale_order_code\r\n" + "ORDER BY scp_load DESC", nativeQuery = true)
//	List<LatestScrapNoteBalanceInterface> getLastestBalanceInfo(String sale_order_code);

	@Query(value = "\r\n"
			+ "\r\n"
			+ "			SELECT TOP 1\r\n"
			+ "    soe.sale_order_code,\r\n"
			+ "    spni.scp_load,\r\n"
			+ "\r\n"
			+ "    CASE\r\n"
			+ "        WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg\r\n"
			+ "        ELSE soei.non_steel_kgs\r\n"
			+ "    END AS total_ordered,\r\n"
			+ "\r\n"
			+ "COALESCE(sales.total_sold,0) AS total_sold,\r\n"
			+ "\r\n"
			+ "CASE\r\n"
			+ "   WHEN COALESCE(sales.active_cnt,0)=0\r\n"
			+ "        THEN\r\n"
			+ "            CASE\r\n"
			+ "                WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg\r\n"
			+ "                ELSE soei.non_steel_kgs\r\n"
			+ "            END\r\n"
			+ "   ELSE\r\n"
			+ "        CASE\r\n"
			+ "            WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg\r\n"
			+ "            ELSE soei.non_steel_kgs\r\n"
			+ "        END - COALESCE(sales.total_sold,0)\r\n"
			+ "END AS balance,\r\n"
			+ "CASE\r\n"
			+ "   WHEN COALESCE(sales.active_cnt,0)=0\r\n"
			+ "        THEN\r\n"
			+ "            CASE\r\n"
			+ "                WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg\r\n"
			+ "                ELSE soei.non_steel_kgs\r\n"
			+ "            END\r\n"
			+ "   ELSE\r\n"
			+ "        CASE\r\n"
			+ "            WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg\r\n"
			+ "            ELSE soei.non_steel_kgs\r\n"
			+ "        END - COALESCE(sales.total_sold,0)\r\n"
			+ "END AS dummy_balance,\r\n"
			+ "\r\n"
			+ "      spni.created_date,soei.tolerance,\r\n"
			+ "\r\n"
			+ "ROUND(\r\n"
			+ "   (\r\n"
			+ "      CASE\r\n"
			+ "         WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg\r\n"
			+ "         ELSE soei.non_steel_kgs\r\n"
			+ "      END\r\n"
			+ "      *\r\n"
			+ "      (1 + soei.tolerance/100.0)\r\n"
			+ "   )\r\n"
			+ "   -\r\n"
			+ "   CASE\r\n"
			+ "      WHEN COALESCE(sales.active_cnt,0)=0 THEN 0\r\n"
			+ "      ELSE COALESCE(sales.total_sold,0)\r\n"
			+ "   END\r\n"
			+ ",2) AS tolerance_value\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "FROM SALE_ORDER_ENTRY soe\r\n"
			+ "\r\n"
			+ "LEFT JOIN SALE_ORDER_ITEM_LEVEL_ENTRY soei\r\n"
			+ "    ON soei.soe_id = soe.soe_id\r\n"
			+ "\r\n"
			+ "LEFT JOIN\r\n"
			+ "(\r\n"
			+ "    SELECT\r\n"
			+ "        spni.sale_order_code,\r\n"
			+ "\r\n"
			+ "        -- sold only from ACTIVE PN\r\n"
			+ "        SUM(CASE WHEN ISNULL(spn.Cancel,0)=0 THEN spni.total_sold ELSE 0 END) AS total_sold,\r\n"
			+ "\r\n"
			+ "        -- count active rows\r\n"
			+ "        COUNT(CASE WHEN ISNULL(spn.Cancel,0)=0 THEN 1 END) AS active_cnt\r\n"
			+ "\r\n"
			+ "    FROM SCRAP_PACKING_NOTE_ITEMS spni\r\n"
			+ "    INNER JOIN SCRAP_PACKING_NOTE spn\r\n"
			+ "        ON spn.scp_pn_id = spni.scp_pn_id\r\n"
			+ "    GROUP BY spni.sale_order_code\r\n"
			+ ") sales\r\n"
			+ "ON sales.sale_order_code = soe.sale_order_code\r\n"
			+ "\r\n"
			+ "LEFT JOIN SCRAP_PACKING_NOTE_ITEMS spni\r\n"
			+ "    ON spni.sale_order_code = soe.sale_order_code\r\n"
			+ "\r\n"
			+ "WHERE soe.sale_order_code = :sale_order_code\r\n"
			+ "\r\n"
			+ "ORDER BY spni.scp_load DESC;", nativeQuery = true)
	List<LatestScrapNoteBalanceInterface> getLastestBalanceInfo(String sale_order_code);
	
	/*
	 * @Query(value =
	 * "SELECT total_ordered, total_sold, balance from SCRAP_PACKING_NOTE_ITEMS where scp_load = :scp_load"
	 * , nativeQuery = true) LatestScrapNoteBalanceInterface
	 * getLastestBalanceInfoForScpload(String scp_load);
	 * 
	 */
	@Query(value = "SELECT TOP 1 soe.sale_order_code, \r\n" + "       COALESCE(spni.scp_load, NULL) AS scp_load, \r\n"
			+ "       COALESCE(spni.total_ordered, \r\n" + "                CASE \r\n"
			+ "                    WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg \r\n"
			+ "                    ELSE soei.non_steel_kgs \r\n" + "                END) AS total_ordered, \r\n"
			+ "       COALESCE(spni.total_sold, 0) AS total_sold, \r\n" + "       COALESCE(spni.balance, \r\n"
			+ "                CASE \r\n"
			+ "                    WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg \r\n"
			+ "                    ELSE soei.non_steel_kgs \r\n"
			+ "                END - COALESCE(spni.total_sold, 0)) AS balance, \r\n"
			+ "       COALESCE(spni.balance, \r\n" + "                CASE \r\n"
			+ "                    WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg \r\n"
			+ "                    ELSE soei.non_steel_kgs \r\n"
			+ "                END - COALESCE(spni.total_sold, 0)) AS dummy_balance, \r\n"
			+ "       spni.created_date, soei.tolerance,  \r\n" + "       ROUND((\r\n" + "             CASE \r\n"
			+ "                 WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN (soei.kg + (soei.kg * soei.tolerance / 100.0)) \r\n"
			+ "                 ELSE (soei.non_steel_kgs + (soei.non_steel_kgs * soei.tolerance / 100.0)) \r\n"
			+ "             END\r\n" + "             ) - \r\n"
			+ "             (SELECT COALESCE(SUM(spni_inner.total_sold), 0) \r\n"
			+ "              FROM SCRAP_PACKING_NOTE_ITEMS spni_inner \r\n"
			+ "              WHERE spni_inner.sale_order_code = soe.sale_order_code), 2) AS tolerance_value  \r\n"
			+ "FROM SALE_ORDER_ENTRY soe \r\n"
			+ "LEFT JOIN SALE_ORDER_ITEM_LEVEL_ENTRY soei ON soei.soe_id = soe.soe_id \r\n"
			+ "LEFT JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.sale_order_code = soe.sale_order_code \r\n"
			+ "WHERE soe.sale_order_code = :sale_order_code\r\n" + "ORDER BY scp_load DESC;", nativeQuery = true)
	LatestScrapNoteBalanceInterface getLastestBalanceInfoForScpload(String sale_order_code);

	@Query(value = "SELECT TOP 1 spni.sale_order_code, spni.scp_load, spni.total_ordered, spni.total_sold, spni.balance, \r\n"
			+ "spni.created_date, soei.tolerance,  ROUND((spni.total_ordered + (spni.total_ordered * soei.tolerance / 100.0)), 2) AS tolerance_value FROM SCRAP_PACKING_NOTE_ITEMS spni\r\n"
			+ "inner join SALE_ORDER_ENTRY soe on soe.sale_order_code  = spni.sale_order_code\r\n"
			+ "inner join SALE_ORDER_ITEM_LEVEL_ENTRY soei on soei.soe_id = soe.soe_id\r\n"
			+ "WHERE spni.sale_order_code = :sale_order_code ORDER BY created_date DESC;", nativeQuery = true)
	LatestScrapNoteBalanceInterface getLInitialBalanceInfo(String sale_order_code);

	@Transactional
	@Modifying
	@Query(value = "update SCRAP_PACKING_NOTE_ITEMS set quantity = :quantity, kg = :kg, unit_price = :unit_price , total= :total, \r\n"
			+ " total_ordered = :total_ordered, total_sold = :total_sold, balance = :balance, modified_by = :modified_by, "
			+ "modified_date = :modified_date where scp_load = :scp_load ", nativeQuery = true)
	int updatePackingNoteItemsInfo(String quantity, String kg, String unit_price, String total, String total_ordered,
			String total_sold, String balance, String modified_by, LocalDateTime modified_date, String scp_load);

	@Query(value = "Select * from SCRAP_PACKING_NOTE where scp_load = :scp_load", nativeQuery = true)
	SaleOrderPackingNote findScp_loadIfExists(String scp_load);

	@Query(value = "select spn.sale_order_code ,spn.scp_load, spn.weighbridge_no, spn.vehicleno, spn.transportername, spn.buyer_ref_no,\r\n"
			+ "spni.scrap_type_id, stm.scrap_type, sssi.scrapitemname, sssi.scrapitemname, uo.unit_name\r\n"
			+ "from SCRAP_PACKING_NOTE spn\r\n"
			+ "inner join SCRAP_PACKING_NOTE_ITEMS spni on spni.scp_load = spn.scp_load\r\n"
			+ "inner join SCRAPSALESSCRAPITEMS_MASTER sssi on sssi.siid = spni.scrapitem_id\r\n"
			+ "inner join SCRAPSALESCRAPTYPES_MASTER stm on stm.stid = spni.scrap_type_id\r\n"
			+ "inner join UOM_MASTER uo on uo.unit_id = spni.uom_id\r\n"
			+ "where spn.is_delete = 0 and spni.is_delete = 0", nativeQuery = true)
	List<AllScrapingNoteDetails> getAllScrapPackingnoteDetails();

	@Query(value = "select spn.sale_order_code ,spn.scp_load, spn.weighbridge_no, spn.vehicleno, spn.transportername, spn.buyer_ref_no,\r\n"
			+ "spni.scrap_type_id, stm.scrap_typename as scrap_type, sssi.scrapitemname, sssi.scrapitemname, uo.unit_name, spn.factory_id,CASE \r\n"
			+ "WHEN spn.is_verified = 0 THEN 'Not Verified' \r\n" + "ELSE 'Verified' \r\n"
			+ "END AS verification_status, spni.total_ordered,spni.total_sold, spni.balance\r\n"
			+ "from SCRAP_PACKING_NOTE spn\r\n"
			+ "inner join SCRAP_PACKING_NOTE_ITEMS spni on spni.scp_load = spn.scp_load\r\n"
			+ "inner join SCRAPSALESSCRAPITEMS_MASTER sssi on sssi.siid = spni.scrapitem_id\r\n"
			+ "inner join SCRAPSALESCRAPTYPES_MASTER stm on stm.stid = spni.scrap_type_id\r\n"
			+ "inner join UOM_MASTER uo on uo.unit_id = spni.uom_id\r\n"
			+ "where spn.factory_id = :factory_id and spn.is_delete = 0 and spni.is_delete = 0 and (spn.Cancel=0 or spn.Cancel is null) ", nativeQuery = true)
	List<AllScrapingNoteDetails> getAllScrapPackingnoteDetailsNew(String factory_id);
	
	
	
	@Query(
			value = "SELECT spn.sale_order_code, spn.scp_load, spn.weighbridge_no, spn.vehicleno, " +
			        "spn.transportername, spn.buyer_ref_no, " +
			        "spni.scrap_type_id, stm.scrap_typename AS scrap_type, " +
			        "sssi.scrapitemname, uo.unit_name, spn.factory_id, " +
			        "CASE WHEN spn.is_verified = 0 THEN 'Not Verified' ELSE 'Verified' END AS verification_status, " +
			        "spni.total_ordered, spni.total_sold, spni.balance " +
			        "FROM SCRAP_PACKING_NOTE spn " +
			        "INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load " +
			        "INNER JOIN SCRAPSALESSCRAPITEMS_MASTER sssi ON sssi.siid = spni.scrapitem_id " +
			        "INNER JOIN SCRAPSALESCRAPTYPES_MASTER stm ON stm.stid = spni.scrap_type_id " +
			        "INNER JOIN UOM_MASTER uo ON uo.unit_id = spni.uom_id " +
			        "WHERE spn.factory_id = :factory_id " +
			        "AND spn.is_delete = 0 " +
			        "AND spni.is_delete = 0 " +
			        "AND (spn.Cancel = 0 OR spn.Cancel IS NULL) " +

			        "AND ( :search IS NULL OR :search = '' OR " +
			        "LOWER(spn.sale_order_code) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(spn.scp_load) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(sssi.scrapitemname) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(stm.scrap_typename) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(spn.vehicleno) LIKE LOWER(CONCAT('%',:search,'%')) ) " +

			        "ORDER BY spn.scp_load DESC",

			countQuery = "SELECT COUNT(*) FROM SCRAP_PACKING_NOTE spn " +
			        "INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load " +
			        "INNER JOIN SCRAPSALESSCRAPITEMS_MASTER sssi ON sssi.siid = spni.scrapitem_id " +
			        "INNER JOIN SCRAPSALESCRAPTYPES_MASTER stm ON stm.stid = spni.scrap_type_id " +
			        "WHERE spn.factory_id = :factory_id " +
			        "AND spn.is_delete = 0 " +
			        "AND spni.is_delete = 0 " +
			        "AND (spn.Cancel = 0 OR spn.Cancel IS NULL) " +

			        "AND ( :search IS NULL OR :search = '' OR " +
			        "LOWER(spn.sale_order_code) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(spn.scp_load) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(sssi.scrapitemname) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(stm.scrap_typename) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(spn.vehicleno) LIKE LOWER(CONCAT('%',:search,'%')) )",

			nativeQuery = true
			)
	Page<AllScrapingNoteDetails> getAllScrapPackingnoteDetailsNewPaged(
	        @Param("factory_id") String factory_id,
	        @Param("search") String search,
	        Pageable pageable
	);
	

	@Query(value = "select count(*) from SCRAP_PACKING_NOTE where scp_load = :scp_load and is_locked = 1", nativeQuery = true)
	int checkIsLocked(String scp_load);

	@Query(value = "select count(*) from SCRAP_PACKING_NOTE where scp_load = :scp_load and is_verified = 1", nativeQuery = true)
	int checkIsVerified(String scp_load);

	/*
	 * @Query(value =
	 * "SELECT ISNULL((SELECT 1 FROM SCRAP_PACKING_NOTE WHERE scp_load = 'SCP-00001' AND is_verified = 1), 0);"
	 * , nativeQuery = true) int checkIsVerified(String scp_load);
	 */

	/*
	 * @Query(value =
	 * "SELECT ISNULL((SELECT TOP 1 scp_pn_id FROM SCRAP_PACKING_NOTE ORDER BY scp_pn_id DESC), 0) AS scp_pn_id;"
	 * , nativeQuery = true) int chekcRowCount();
	 */

	@Query(value = "SELECT TOP 1 scp_load FROM SCRAP_PACKING_NOTE ORDER BY scp_pn_id DESC", nativeQuery = true)
	String findLastScpLoad();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAP_PACKING_NOTE_HISTORY (scp_pn_id, sale_order_code, scp_load, total_steel_qty, total_non_steel_qty, \r\n"
			+ "sale_order_validity, vendor_id, weighbridge_no, transportername, vehicleno, scp_pn_date, buyer_ref_no, \r\n"
			+ "created_by, created_date,  project_reference, other_reference, \r\n"
			+ "central_excise_tarrif_no, remarks, credit_reference, is_verified, verified_by, verified_date, factory_id,action,transaction_date,modified_by, modified_date)\r\n"
			+ " SELECT scp_pn_id, sale_order_code, scp_load, total_steel_qty, total_non_steel_qty, \r\n"
			+ "sale_order_validity, vendor_id, weighbridge_no, transportername, vehicleno, scp_pn_date, buyer_ref_no, \r\n"
			+ "created_by, created_date,  project_reference, other_reference, \r\n"
			+ "central_excise_tarrif_no, remarks, credit_reference, is_verified, verified_by, verified_date, factory_id,'UPDATE', GETDATE() ,:modified_by, GETDATE() from SCRAP_PACKING_NOTE where scp_load = :scp_load", nativeQuery = true)
	int updateScrapPackingNoteIntoHistoryTable(String scp_load, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAP_PACKING_NOTE_ITEMS_HISTORY ( scp_pnitems_id, scp_pn_id, sale_order_code, scp_load, scrap_type_id, \r\n"
			+ "scrapitem_id, uom_id, quantity, kg, unit_price, total, total_ordered, total_sold, balance, created_by, \r\n"
			+ "created_date, modified_by, modified_date,  factory_id, hsn_code, service_code, type_id, \r\n"
			+ "is_locked, action, transaction_date) select scp_pnitems_id, scp_pn_id, \r\n"
			+ "sale_order_code, scp_load, scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, \r\n"
			+ "total_ordered, total_sold, balance, created_by, created_date, :modified_by, :modified_date, factory_id, \r\n"
			+ "hsn_code, service_code, type_id, is_locked,'UPDATE', :modified_date from SCRAP_PACKING_NOTE_ITEMS where scp_load = :scp_load and scp_pnitems_id = :scp_pnitems_id", nativeQuery = true)
	int insertPackingNoteDetailsToHistory(String scp_load, String modified_by, LocalDateTime modified_date,
			String scp_pnitems_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAP_PACKING_NOTE (sale_order_code, scp_load,  total_steel_qty, total_non_steel_qty, sale_order_validity, vendor_id, weighbridge_no, transportername, vehicleno, scp_pn_date,buyer_ref_no, created_by, created_date, factory_id,location_type, sale_order_date) \r\n"
			+ "VALUES (:sale_order_code, :newScpLoad, :total_steel_qty, :total_non_steel_qty, :sale_order_validity, :vendor_id, :weighbridge_no, :transportername, :vehicle_no, :scp_pn_date, :sale_order_code, :created_by, GETDATE(), :factory_id,:location_type_id,:sale_order_date )", nativeQuery = true)
	int insertIntoScrapPackingNoteTable(String sale_order_code, String newScpLoad, String total_steel_qty,
			String total_non_steel_qty, String sale_order_validity, String vendor_id, String weighbridge_no,
			String transportername, String vehicle_no, String scp_pn_date, String created_by, String factory_id,
			String location_type_id, String sale_order_date);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAP_PACKING_NOTE_ITEMS (scp_pn_id,sale_order_code, scp_load, scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, total_ordered, total_sold, balance) \r\n"
			+ "VALUES (:scp_pn_id, :sale_order_code, :newScpLoad, :scrap_type_id, :scrapitem_id, :uom_id, :quantity, :kg, :unit_price, :total, :total_ordered, :total_sold, :balance)", nativeQuery = true)
	int insertIntoScrapPackingNoteItems(int scp_pn_id, String sale_order_code, String newScpLoad, String scrap_type_id,
			String scrapitem_id, String uom_id, String quantity, String kg, String unit_price, String total,
			String total_ordered, String total_sold, String balance);

	@Query(value = "SELECT TOP 1 scp_pn_id from SCRAP_PACKING_NOTE where sale_order_code = :sale_order_code\r\n"
			+ "order by scp_pn_id desc", nativeQuery = true)
	int getScp_pn_id(String sale_order_code);

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "UPDATE SCRAP_PACKING_NOTE_ITEMS SET scrap_type_id = :scrap_type_id, scrapitem_id = :scrapitem_id, "
	 * +
	 * "uom_id = :uom_id, quantity = :quantity, kg = :kg, unit_price = :unit_price, total = :total, "
	 * +
	 * "total_ordered = :total_ordered, total_sold = :total_sold, balance = :balance, modified_by, "
	 * + "modified_date\r\n" +
	 * "WHERE scp_pn_id = :scp_pn_id AND  scp_pnitems_id = :scp_pnitems_id;",
	 * nativeQuery = true) int updateScrapPackingNoteItems(String scp_pn_id, String
	 * string, String string2, String string3, String string4, String string5,
	 * String string6, String string7, String total_ordered, String total_sold,
	 * String balance, String scp_pnitems_id);
	 */

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE \r\n"
			+ "SET total_steel_qty = :total_steel_qty, total_non_steel_qty = :total_non_steel_qty, sale_order_validity = :sale_order_validity, \r\n"
			+ "vendor_id = :vendor_id, weighbridge_no = :weighbridge_no, transportername = :transportername, vehicleno = :vehicle_no,\r\n"
			+ "scp_pn_date = :scp_pn_date, project_reference = :project_reference, central_excise_tarrif_no = :central_excise_tarrif_no, other_reference = :other_reference,credit_reference =:credit_reference,\r\n"
			+ "modified_by = :modified_by, location_type = :location_type, sale_order_date = :sale_order_date, modified_date = GETDATE() \r\n"
			+ "WHERE  scp_load = :scp_load", nativeQuery = true)
	int updateScrapPackingNoteDetails(String total_steel_qty, String total_non_steel_qty, String sale_order_validity,
			String vendor_id, String weighbridge_no, String transportername, String vehicle_no, String scp_pn_date,
			String project_reference, String central_excise_tarrif_no, String other_reference, String credit_reference,
			String modified_by, String location_type, String sale_order_date, String scp_load);

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value = "UPDATE SCRAP_PACKING_NOTE \r\n" +
	 * "SET total_steel_qty = :total_steel_qty, \r\n" +
	 * "total_non_steel_qty = :total_non_steel_qty, \r\n" +
	 * "sale_order_validity = :sale_order_validity, \r\n" +
	 * "vendor_id = :vendor_id, \r\n" + "weighbridge_no = :weighbridge_no, \r\n" +
	 * "transportername = :transportername, \r\n" + "vehicleno = :vehicle_no, \r\n"
	 * + "scp_pn_date = :scp_pn_date, \r\n" +
	 * "project_reference = COALESCE(:project_reference, project_reference), \r\n" +
	 * "central_excise_tarrif_no = COALESCE(:central_excise_tarrif_no, central_excise_tarrif_no), \r\n"
	 * + "remarks = COALESCE(:remarks, remarks), \r\n" +
	 * "other_reference = COALESCE(:other_reference, other_reference), \r\n" +
	 * "credit_reference = COALESCE(:credit_reference, credit_reference), \r\n" +
	 * "modified_by = :modified_by, \r\n" + "location_type = :location_type, \r\n" +
	 * "sale_order_date = :sale_order_date, \r\n" + "modified_date = GETDATE() \r\n"
	 * + "WHERE scp_load = :scp_load", nativeQuery = true) int
	 * updateScrapPackingNoteDetails(String total_steel_qty, String
	 * total_non_steel_qty, String sale_order_validity, String vendor_id, String
	 * weighbridge_no, String transportername, String vehicle_no, String
	 * scp_pn_date, String project_reference, String central_excise_tarrif_no,
	 * String remarks, String other_reference, String credit_reference, String
	 * modified_by, String location_type, String sale_order_date, String scp_load);
	 */

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE_ITEMS SET scrap_type_id = :scrap_type_id, scrapitem_id = :scrapitem_id, uom_id = :uom_id, quantity = :quantity, kg = :kg, unit_price = :unit_price, total = :total, total_ordered = :total_ordered, total_sold = :total_sold, balance = :balance \r\n"
			+ "WHERE scp_pn_id = :scp_pn_id AND scp_pnitems_id = :scp_pnitems_id", nativeQuery = true)
	int updateScrapPackingNoteItems(String scrap_type_id, String scrapitem_id, String uom_id, String quantity,
			String kg, String unit_price, String total, String total_ordered, String total_sold, String balance,
			String scp_pn_id, String scp_pnitems_id);

	@Query(value = "SELECT TOP 1 sale_order_code from SALE_ORDER_ENTRY order by sale_order_code DESC", nativeQuery = true)
	String getLastIncrementFromDatabase();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SALE_ORDER_ENTRY (sale_order_code,sale_order_type_id, location_type_id, "
			+ "sale_order_to_id, auction_date, sale_order_duration, advance_payment, billing_address_id, "
			+ "shipping_address_id, business_unit_id, tax1, tax1_value, tax2, tax2_value, tax3, tax3_value, "
			+ "net_amount, total_tax, grand_total, created_by, factory_id, created_date) "
			+ "VALUES (:sale_order_type_id, :sale_order_code, :location_type_id, :sale_order_to_id, :auction_date, "
			+ ":sale_order_duration, :advance_payment, :billing_address_id, :shipping_address_id, :business_unit_id, "
			+ ":tax1, :tax1_value, :tax2, :tax2_value, :tax3, :tax3_value, :net_amount, :total_tax, :grand_total, :created_by,:factory_id, GETDATE())", nativeQuery = true)
	int insertIntoSaleOrderEntryTable(String sale_order_code, String sale_order_type_id, String location_type_id,
			String sale_order_to_id, String auction_date, String sale_order_duration, String advance_payment,
			String billing_address_id, String shipping_address_id, String business_unit_id, String tax1,
			String tax1_value, String tax2, String tax2_value, String tax3, String tax3_value, String net_amount,
			String total_tax, String grand_total, String created_by, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SALE_ORDER_ITEM_LEVEL_ENTRY \r\n"
			+ "    (soe_id, auction_no, scrap_type_id, scrapitem_id, uom_id, quantity, kg, non_steel_kgs, unit_price, total, tolerance, created_by, created_date)\r\n"
			+ "VALUES \r\n" + "    (:soe_id, :auction_no, :scrap_type_id, :scrapitem_id, :uom_id, :quantity, \r\n"
			+ "        CASE WHEN :scrap_type_id = 1 THEN :kg ELSE 0 END, \r\n"
			+ "        CASE WHEN :scrap_type_id = 2 THEN :kg ELSE 0 END, \r\n"
			+ "        :unit_price, :total, :tolerance, :created_by, GETDATE())", nativeQuery = true)
	int insertIntoSaleOrderItemTable(int soe_id, String auction_no, String scrap_type_id, String scrapitem_id,
			String uom_id, String quantity, String kg, String unit_price, String total, String tolerance,
			String created_by);

	@Query(value = "select soe_id from SALE_ORDER_ENTRY where sale_order_code = :sale_order_code", nativeQuery = true)
	int getSoe_idFromTable(String sale_order_code);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SALE_ORDER_DESCRIPTION_ENTRY (soe_id, sl_no,terms_conditions, created_by, created_date) VALUES (:soe_id, :sl_no,:terms_conditions, :created_by, GETDATE())", nativeQuery = true)
	int insertIntoSaleOrderDescriptiontable(int soe_id, int sl_no, String terms_conditions, String created_by);

	@Transactional

	@Modifying

	@Query(value = "INSERT INTO SALE_ORDER_DESCRIPTION_ENTRY (soe_id, sl_no, terms_conditions, modified_by, modified_date) VALUES (:soe_id, :sl_no, :terms_conditions, :modified_by, GETDATE())", nativeQuery = true)
	int insertIntoSaleOrderDescriptiontableDuringUpdate(String soe_id, int sl_no, String terms_conditions,
			String modified_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_ENTRY SET sale_order_type_id = :sale_order_type_id, "
			+ "location_type_id = :location_type_id, sale_order_to_id = :sale_order_to_id, "
			+ "auction_date = :auction_date, sale_order_duration = :sale_order_duration, "
			+ "advance_payment = :advance_payment, billing_address_id = :billing_address_id, "
			+ "shipping_address_id = :shipping_address_id, business_unit_id = :business_unit_id, "
			+ "tax1 = :tax1, tax1_value = :tax1_value, tax2 = :tax2, tax2_value = :tax2_value, "
			+ "tax3 = :tax3, tax3_value = :tax3_value, net_amount = :net_amount, total_tax = :total_tax, "
			+ "grand_total = :grand_total, modified_by = :modified_by, modified_date = GETDATE(), sale_order_code = :sale_order_code "
			+ "WHERE soe_id =:soe_id ", nativeQuery = true)
	int updateSaleOrderEntryTable(String sale_order_type_id, String location_type_id, String sale_order_to_id,
			String auction_date, String sale_order_duration, String advance_payment, String billing_address_id,
			String shipping_address_id, String business_unit_id, String tax1, String tax1_value, String tax2,
			String tax2_value, String tax3, String tax3_value, String net_amount, String total_tax, String grand_total,
			String modified_by, String sale_order_code, String soe_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_ITEM_LEVEL_ENTRY SET auction_no = :auction_no, scrap_type_id = :scrap_type_id, "
			+ "scrapitem_id = :scrapitem_id, uom_id = :uom_id, quantity = :quantity, kg = :kg, "
			+ "unit_price = :unit_price, total = :total, tolerance = :tolerance, modified_by = :modified_by, modified_date = GETDATE() "
			+ "WHERE soe_id = :soe_id AND soei_id = :soei_id", nativeQuery = true)
	int updateSaleOrderItemsTable(String auction_no, String scrap_type_id, String scrapitem_id, String uom_id,
			String quantity, String kg, String unit_price, String total, String tolerance, String modified_by,
			String soe_id, String soei_id);

	@Query(value = "select soe.sale_order_code, soei.auction_no, soe.auction_date, (icam.name_of_add+''+icam.add1+''+icam.add2) as address ,ssl.location_name,sot.saleorder_typename ,soe.factory_id from SALE_ORDER_ENTRY soe\r\n"
			+ "inner join SALE_ORDER_ITEM_LEVEL_ENTRY soei on soei.soe_id = soe.soe_id\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id= soe.sale_order_to_id\r\n"
			+ "inner join SCRAPSALELOCATION ssl on ssl.slid = soe.location_type_id\r\n"
			+ "inner join SCRAPSALESORDERTYPE_MASTER sot on sot.saleorder_id = soe.sale_order_type_id\r\n"
			+ "where soe.factory_id = :factory_id and soe.is_delete=0 ", nativeQuery = true)
	List<allSaleOrderEntriesInterface> getAllSaleOrderDetailsBasedOnFactory(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAP_INVOICE_MASTER (contract_id, load_id, invoice_type, scp_load, product_desc, remarks, created_by, created_date,verified_status, factory_id,tax1_ptd,tax1_pbc,tax2_ptd,tax2_pbc,tax3_ptd,tax3_pbc, shipment_mode, delivery_condition, bank_id, is_invoice) "
			+ "VALUES (:contract_id, :load_id, :invoice_type, :scp_load, :product_desc, :remarks,  :created_by, GETDATE(), 'NOT VERIFIED', :factory_id,:tax1_ptd,:tax1_pbc,:tax2_ptd,:tax2_pbc,:tax3_ptd,:tax3_pbc, :shipment_mode, :delivery_condition, :bank_id, 1)", nativeQuery = true)
	int insertIntoScrapInvoiceTable(String contract_id, String load_id, String invoice_type, String scp_load,
			String product_desc, String remarks, String created_by, String factory_id, BigDecimal tax1_ptd, BigDecimal tax1_pbc,
			BigDecimal tax2_ptd, BigDecimal tax2_pbc, BigDecimal tax3_ptd, BigDecimal tax3_pbc, String shipment_mode,
			String delivery_condition, String bank_id);

	@Transactional
	@Modifying
	@Query(value = "update SCRAP_PACKING_NOTE_ITEMS set hsn_code = :hsn_code_id, service_code = :service_code_id,type_id = :type_id,  modified_by = :created_by, modified_date = GETDATE() where scp_load = :scp_load and scp_pnitems_id = :scp_pnitems_id", nativeQuery = true)
	int updatePackingNoteItemsTableDuringInvoice(String hsn_code_id, String service_code_id, String type_id,
			String created_by, String scp_load, String scp_pnitems_id);

	@Query(value = "SELECT soe.soe_id, soe.sale_order_code,soe.sale_order_type_id, ssot.saleorder_typename, soe.location_type_id,ssl.location_name, ssl.location_code , soe.sale_order_to_id,(icam.name_of_add+''+icam.add1+''+icam.add2) AS address,soe.sale_order_duration, soe.auction_date, soe.advance_payment,\r\n"
			+ "soe.billing_address_id, (ica.name_of_add+''+ica.add1+''+ica.add2) as billing_address , soe.shipping_address_id, (isa.name_of_add+''+isa.add1+''+isa.add2) as shipping_address,  soe.business_unit_id,bu.business_unit_name, soe.tax1, tm1.tax_per as tax1_per ,soe.tax1_value, soe.tax2, tm2.tax_per as tax2_per,soe.tax2_value,\r\n"
			+ "soe.tax3, tm3.tax_per as tax3_per ,soe.tax3_value,tm1.tax_name as taxname1,tm2.tax_name as taxname2,tm3.tax_name as taxname3, soe.net_amount, soe.total_tax, soe.grand_total, cast(soe.created_date as date) as saleorderdate  from SALE_ORDER_ENTRY soe\r\n"
			+ "left join SCRAPSALESORDERTYPE_MASTER ssot on ssot.saleorder_id = soe.sale_order_type_id\r\n"
			+ "left join SCRAPSALELOCATION ssl on ssl.slid = soe.location_type_id\r\n"
			+ "left join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = soe.sale_order_to_id\r\n"
			+ "left join INVOICE_CONSIGNEE_ADDRESS_MASTER ica on ica.id = soe.billing_address_id\r\n"
			+ "left join BUSINESS_UNITS bu on bu.business_unit_id = soe.business_unit_id\r\n"
			+ "left join INVOICE_CONSIGNEE_ADDRESS_MASTER isa on isa.id = soe.shipping_address_id\r\n"
			+ "left join TAX_MASTER tm1 on tm1.tax_id = soe.tax1\r\n"
			+ "left join TAX_MASTER tm2 on tm2.tax_id = soe.tax2\r\n"
			+ "left join TAX_MASTER tm3 on tm3.tax_id = soe.tax3\r\n" + " where soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderInterface> getAllSaleOrderDetailsBAsedOnId(String soe_id);

	@Query(value = " SELECT soei_id, soe_id, auction_no, soei.scrap_type_id, stm.scrap_typename AS scrap_type,  \r\n"
			+ "       scrapitem_id, sssi.scrapitemname, uom_id,  uom.unit_name, quantity,  \r\n"
			+ "       CASE WHEN kg = 0 THEN non_steel_kgs ELSE kg END AS kg, unit_price, total, tolerance  \r\n"
			+ "FROM SALE_ORDER_ITEM_LEVEL_ENTRY soei  \r\n"
			+ "INNER JOIN SCRAPSALESSCRAPITEMS_MASTER sssi ON sssi.siid = soei.scrapitem_id  \r\n"
			+ "INNER JOIN SCRAPSALESCRAPTYPES_MASTER stm ON stm.stid = soei.scrap_type_id  \r\n"
			+ "INNER JOIN UOM_MASTER uom ON uom.unit_id = soei.uom_id  \r\n"
			+ "WHERE soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderItemLevelInterface> getSaleOrderItemLevelDetails(String soe_id);

	@Query(value = "SELECT soe_id, sl_no, terms_conditions from SALE_ORDER_DESCRIPTION_ENTRY where soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderItemDescLevelInterface> getSaleOrderDescriptionLevelDetails(String soe_id);

	@Query(value = "SELECT soe_id from SALE_ORDER_ENTRY where sale_order_code = :sale_order_code", nativeQuery = true)
	String getSoeIdBasedOnSaleOrderCode(String sale_order_code);

	@Query(value = "SELECT sale_order_code from SALE_ORDER_ENTRY where factory_id = :factory_id", nativeQuery = true)
	List<String> getAllSaleOrderCodes(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE_ITEMS \r\n"
			+ "SET service_code= :service_code, hsn_code= :hsn_code, modified_by = :modified_by, type_id = :invoice_type\r\n"
			+ "WHERE scp_pnitems_id = :scp_pnitems_id;", nativeQuery = true)
	int updateScrapPackingNoteDuringInvoiceUpdate(String service_code, String hsn_code, String modified_by,
			String invoice_type, String scp_pnitems_id);

	@Query(value = "select spn.*, (icm.name_of_add+''+icm.add1+''+icm.add2) as vendor_name, ssl.location_code, ssl.location_name, \r\n" + "CASE \r\n"
			+ "        WHEN spn.is_verified = 0 THEN 'Not Verified' \r\n" + "        ELSE 'Verified' \r\n"
			+ "    END AS verification_status   from SCRAP_PACKING_NOTE spn\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icm on icm.id = spn.vendor_id\r\n"
			+ "inner join SCRAPSALELOCATION ssl on ssl.slid = spn.location_type\r\n"
			+ "where spn.scp_load =:scp_load and spn.is_delete = 0", nativeQuery = true)
	SalePackingNoteInterface getInfoFromScrapPackingNoteTable(String scp_load);

	@Query(value = "SELECT spn.*, (icm.name_of_add+''+icm.add1+''+icm.add2) AS vendor_name, "
            + "soe.sale_order_type_id, ssot.saleorder_typename, soe.location_type_id, ssl.location_code, "
            + "ssl.location_name, soe.sale_order_to_id, soe.auction_date, soe.sale_order_duration, "
            + "soe.billing_address_id, (ica.name_of_add+''+ica.add1+''+ica.add2) AS billing_address, "
            + "soe.shipping_address_id, (isa.name_of_add+''+isa.add1+''+isa.add2) AS shipping_address, "
            + "soe.business_unit_id, bu.business_unit_name, soe.tax1 AS tax1_id, tm1.tax_per AS tax1, tm1.tax_name AS tax1_name, "
            + "soe.tax2 AS tax2_id, tm2.tax_per AS tax2, tm2.tax_name AS tax2_name, soe.tax3 AS tax3_id, "
            + "tm3.tax_per AS tax3, tm3.tax_name AS tax3_name, "
            + "soe.total_tax, soe.net_amount, CONCAT(tm1.tax_name, '/', tm1.tax_per, '%') AS tax1_concat, "
            + "CONCAT(tm2.tax_name, '/', tm2.tax_per, '%') AS tax2_concat, CONCAT(tm3.tax_name, '/', tm3.tax_per, '%') AS tax3_concat, "
            + "tm1.tax_desc AS tax1_desc, tm2.tax_desc AS tax2_desc, tm3.tax_desc AS tax3_desc, spni.total, "
            + "ROUND(spni.total * TRY_CAST(tm1.tax_per AS FLOAT) / 100, 2) AS tax1_value, "
            + "ROUND(spni.total * TRY_CAST(tm2.tax_per AS FLOAT) / 100, 2) AS tax2_value, "
            + "ROUND((TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) "
            + "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0) / 100,2) "
            + "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0) / 100,2)) "
            + "* COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0) / 100,2) AS tax3_value, "
            + "ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) "
            + "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0) / 100,2) "
            + "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0) / 100,2) "
            + "+ ROUND((TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) "
            + "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0) / 100,2) "
            + "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0) / 100,2)) "
            + "* COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0) / 100,2), "
            + "2) AS grand_total "
            + "FROM SCRAP_PACKING_NOTE spn "
            + "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icm ON icm.id = spn.vendor_id "
            + "LEFT JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code = spn.sale_order_code "
            + "LEFT JOIN SCRAPSALESORDERTYPE_MASTER ssot ON ssot.saleorder_id = soe.sale_order_type_id "
            + "LEFT JOIN SCRAPSALELOCATION ssl ON ssl.slid = soe.location_type_id "
            + "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam ON icam.id = soe.sale_order_to_id "
            + "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER ica ON ica.id = soe.billing_address_id "
            + "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER isa ON isa.id = soe.shipping_address_id "
            + "LEFT JOIN BUSINESS_UNITS bu ON bu.business_unit_id = soe.business_unit_id "
            + "LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id = soe.tax1 "
            + "LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id = soe.tax2 "
            + "LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id = soe.tax3 "
            + "LEFT JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load = spn.scp_load "
            + "WHERE spn.scp_load = :scp_load AND spn.is_delete = 0", nativeQuery = true)
	VerifiedPackingNoteInterface getInfoFromVerifiedPackingNoteTable(String scp_load);

	@Query(value = "SELECT sale_order_code from SCRAP_PACKING_NOTE where scp_load = :scp_load", nativeQuery = true)
	String getSaleOrderCodeForScp_load(String scp_load);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE SET weighbridge_no = :weighbridge_no, transportername = :transportername,"
			+ " vehicleno = :vehicle_no, scp_pn_date = :scp_pn_date, is_verified = 1, verified_by = :verified_by, verified_date = GETDATE(), remarks = :remarks WHERE scp_load = :scp_load", nativeQuery = true)
	int verifyScrapPAckingNoteInfo(String weighbridge_no, String transportername, String vehicle_no, String scp_pn_date,
			String verified_by, String remarks, String scp_load);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE_ITEMS set quantity = :quantity, kg = :kg,unit_price = :unit_price, total = :total, total_ordered = :total_ordered, total_sold = :total_sold, balance = :balance, is_verified = 1, verified_by = :verified_by where scp_load = :scp_load and scp_pnitems_id = :scp_pnitems_id ", nativeQuery = true)
	int updateScrapPackingNoteItemsDuringVerification(String quantity, String kg, String unit_price, String total,
			String total_ordered, String total_sold, String balance, String verified_by, String scp_load,
			String scp_pnitems_id);

	@Query(value = "SELECT spn.scp_load from SCRAP_PACKING_NOTE spn where spn.factory_id = :factory_id AND spn.is_verified = 1 and spn.is_invoice = 0", nativeQuery = true)
	List<AllScrapingNoteDetails> getAllVerifiedPackingNoteList(String factory_id);

	@Query(value = "SELECT id, CASE WHEN invoice_no IS NULL OR LTRIM(RTRIM(invoice_no)) = '' THEN 'YET TO GENERATE' ELSE "
			+ "invoice_no END AS invoice_no, load_id, scp_load, CASE\r\n"
			+ "        WHEN Cancel = 1 THEN 'CANCELLED'\r\n"
			+ "        ELSE verified_status\r\n"
			+ "    END AS verified_status,   CAST(created_date AS DATE) as invoice_generated_date FROM SCRAP_INVOICE_MASTER WHERE "
			+ "factory_id = :factory_id", nativeQuery = true)
	List<UnverifiedInvoiceListInterface> getUnverifiedScrapInvoDetails(String factory_id);
	
	
	
	
	
	
	
	@Query(
			value = "SELECT id, " +
			        "CASE WHEN invoice_no IS NULL OR LTRIM(RTRIM(invoice_no)) = '' " +
			        "THEN 'YET TO GENERATE' ELSE invoice_no END AS invoice_no, " +
			        "load_id, scp_load, " +
			        "CASE WHEN Cancel = 1 THEN 'CANCELLED' ELSE verified_status END AS verified_status, " +
			        "CAST(created_date AS DATE) AS invoice_generated_date " +
			        "FROM SCRAP_INVOICE_MASTER " +
			        "WHERE factory_id = :factory_id " +

			        "AND ( :search IS NULL OR :search = '' OR " +

			        "LOWER(invoice_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(load_id) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(scp_load) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(verified_status) LIKE LOWER(CONCAT('%',:search,'%')) OR " +

			        /* DATE SEARCH */
			        "CONVERT(VARCHAR(10), created_date, 105) LIKE CONCAT('%',:search,'%') " +

			        ") " +

			        "ORDER BY created_date DESC",

			countQuery = "SELECT COUNT(*) FROM SCRAP_INVOICE_MASTER " +
			        "WHERE factory_id = :factory_id " +

			        "AND ( :search IS NULL OR :search = '' OR " +

			        "LOWER(invoice_no) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(load_id) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(scp_load) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
			        "LOWER(verified_status) LIKE LOWER(CONCAT('%',:search,'%')) OR " +

			        /* DATE SEARCH */
			        "CONVERT(VARCHAR(10), created_date, 105) LIKE CONCAT('%',:search,'%') " +

			        ")",

			nativeQuery = true
			)
			Page<UnverifiedInvoiceListInterface> getUnverifiedScrapInvoDetailsPaged(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);

	
	

//	@Query(value = "select spn.*, sale_order_type_id, ssot.saleorder_typename, soe.location_type_id,ssl.location_name, ssl.location_code , soe.sale_order_to_id,icam.address ,soe.sale_order_duration, soe.auction_date, soe.advance_payment,\r\n"
//			+ "soe.billing_address_id, ica.address as billing_address , soe.shipping_address_id, isa.address as shipping_address,  soe.business_unit_id,bu.business_unit_name, soe.tax1, soe.tax2, \r\n"
//			+ "soe.tax3,soe.net_amount, \r\n"
//			+ "tm1.tax_desc as tax1_desc, tm2.tax_desc as tax2_desc, tm3.tax_desc as tax3_desc,scppnnote.buyer_ref_no,          \r\n"
//			+ "scppnnote.sale_order_validity, scppnnote.weighbridge_no, scppnnote.transportername, scppnnote.central_excise_tarrif_no,\r\n"
//			+ "spn.gst_remarks, tm1.tax_per as tax1_per, tm2.tax_per as tax2_per, tm3.tax_per as tax3_per, spn.shipment_mode,       \r\n"
//			+ "spn.delivery_condition, spn.bank_id,bm.bank_name, scppnnote.vehicleno ,spn.gst_remarks, concat('Scrap', '-',spn.scp_load) as contract_no,       \r\n"
//			+ "sm.id as shipping_state_id  ,sm.state_code as shipping_state_code, sm.state_name as shipping_state_name,\r\n"
//			+ "ica.state_id as billing_state_id, smb.state_name as billing_state_name,   \r\n"
//			+ "smb.state_code as billing_state_code, tm1.tax_name as tax1_name,tm2.tax_name as tax2_name,   \r\n"
//			+ "tm3.tax_name as tax3_name, bm.account_number, bm.ifsc_code, bm.branch_address,   \r\n"
//			+ "bm.branch_code, bm.swift_code ,bm.city, smbank.state_name,bubank.business_unit_name as account_name,\r\n"
//			+ "ROUND(spni.total * TRY_CAST(tm1.tax_per AS FLOAT) / 100, 2) AS tax1_value,\r\n"
//			+ "ROUND(spni.total * TRY_CAST(tm2.tax_per AS FLOAT) / 100, 2) AS tax2_value,\r\n"
//			+ "ROUND(spni.total * TRY_CAST(tm3.tax_per AS FLOAT) / 100, 2) AS tax3_value,\r\n" + "ROUND(\r\n"
//			+ "    ROUND(spni.total * TRY_CAST(tm1.tax_per AS FLOAT) / 100, 2) +\r\n"
//			+ "    ROUND(spni.total * TRY_CAST(tm2.tax_per AS FLOAT) / 100, 2) +\r\n"
//			+ "    ROUND(spni.total * TRY_CAST(tm3.tax_per AS FLOAT) / 100, 2), \r\n" + "2) AS total_tax,\r\n"
//			+ "ROUND(spni.total +  \r\n" + "      ROUND(spni.total * CAST(tm1.tax_per AS FLOAT) / 100, 2) +  \r\n"
//			+ "      ROUND(spni.total * CAST(tm2.tax_per AS FLOAT) / 100, 2) +  \r\n"
//			+ "      ROUND(spni.total * CAST(tm3.tax_per AS FLOAT) / 100, 2), 2) AS grand_total,\r\n"
//			+ "dbo.NumberToRupeesWords(ROUND(spni.total * TRY_CAST(tm1.tax_per AS FLOAT) / 100, 2)) as tax1_words,\r\n"
//			+ "dbo.NumberToRupeesWords(ROUND(spni.total * TRY_CAST(tm2.tax_per AS FLOAT) / 100, 2)) as tax2_words,\r\n"
//			+ "dbo.NumberToRupeesWords(ROUND(spni.total * TRY_CAST(tm3.tax_per AS FLOAT) / 100, 2)) as tax3_words,\r\n"
//			+ "dbo.NumberToRupeesWords(ROUND(spni.total +  \r\n"
//			+ "      ROUND(spni.total * CAST(tm1.tax_per AS FLOAT) / 100, 2) +  \r\n"
//			+ "      ROUND(spni.total * CAST(tm2.tax_per AS FLOAT) / 100, 2) +  \r\n"
//			+ "      ROUND(spni.total * CAST(tm3.tax_per AS FLOAT) / 100, 2), 2)) as grand_total_words   \r\n"
//			+ "from SCRAP_INVOICE_MASTER spn      \r\n"
//			+ "inner join SALE_ORDER_ENTRY soe on soe.sale_order_code = spn.load_id        \r\n"
//			+ "inner join SCRAP_PACKING_NOTE scppnnote on scppnnote.scp_load = spn.scp_load\r\n"
//			+ "inner join SCRAP_PACKING_NOTE_ITEMS spni on spni.scp_load = spn.scp_load\r\n"
//			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icm on icm.id = scppnnote.vendor_id         \r\n"
//			+ "inner join SCRAPSALESORDERTYPE_MASTER ssot on ssot.saleorder_id = soe.sale_order_type_id        \r\n"
//			+ "inner join SCRAPSALELOCATION ssl on ssl.slid = soe.location_type_id        \r\n"
//			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = soe.sale_order_to_id        \r\n"
//			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER ica on ica.id = soe.billing_address_id        \r\n"
//			+ "inner join BUSINESS_UNITS bu on bu.business_unit_id = soe.business_unit_id        \r\n"
//			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER isa on isa.id = soe.shipping_address_id        \r\n"
//			+ "inner join TAX_MASTER tm1 on tm1.tax_id = soe.tax1         \r\n"
//			+ "inner join TAX_MASTER tm2 on tm2.tax_id = soe.tax2        \r\n"
//			+ "inner join TAX_MASTER tm3 on tm3.tax_id = soe.tax3        \r\n"
//			+ "inner join BANK_MASTER bm on bm.account_id = spn.bank_id       \r\n"
//			+ "inner join STATE_MASTER sm on sm.id = isa.state_id       \r\n"
//			+ "inner join STATE_MASTER smb on smb.id =ica.state_id    \r\n"
//			+ "inner join STATE_MASTER smbank on smbank.id =bm.state_id  \r\n"
//			+ "inner join BUSINESS_UNITS bubank on bubank.business_unit_id = bm.business_unit_id  \r\n"
//			+ "where spn.id = :scp_invoice_id ", nativeQuery = true)
//	ScrapInvoiceInfoInterface getScrapInvoDetails(String scp_invoice_id);
	
//	@Query(value = "SELECT spn.*,soe.sale_order_type_id,ssot.saleorder_typename,soe.location_type_id,ssl.location_name,ssl.location_code,\r\n"
//			+ "soe.sale_order_to_id,icam.address,soe.sale_order_duration,soe.auction_date,soe.advance_payment,\r\n"
//			+ "soe.billing_address_id,ica.address AS billing_address,soe.shipping_address_id,isa.address AS shipping_address,\r\n"
//			+ "soe.business_unit_id,bu.business_unit_name,soe.tax1,soe.tax2,soe.tax3,soe.net_amount,\r\n"
//			+ "tm1.tax_desc AS tax1_desc,tm2.tax_desc AS tax2_desc,tm3.tax_desc AS tax3_desc,\r\n"
//			+ "scppnnote.buyer_ref_no,scppnnote.sale_order_validity,scppnnote.weighbridge_no,\r\n"
//			+ "scppnnote.transportername,scppnnote.central_excise_tarrif_no,spn.gst_remarks,\r\n"
//			+ "COALESCE(tm1.tax_per,0) AS tax1_per,COALESCE(tm2.tax_per,0) AS tax2_per,COALESCE(tm3.tax_per,0) AS tax3_per,\r\n"
//			+ "spn.shipment_mode,spn.delivery_condition,spn.bank_id,bm.bank_name,scppnnote.vehicleno,\r\n"
//			+ "CONCAT('Scrap-',spn.scp_load) AS contract_no,\r\n"
//			+ "sm.id AS shipping_state_id,sm.state_code AS shipping_state_code,sm.state_name AS shipping_state_name,\r\n"
//			+ "ica.state_id AS billing_state_id,smb.state_name AS billing_state_name,smb.state_code AS billing_state_code,\r\n"
//			+ "tm1.tax_name AS tax1_name,tm2.tax_name AS tax2_name,tm3.tax_name AS tax3_name,\r\n"
//			+ "bm.account_number,bm.ifsc_code,bm.branch_address,bm.branch_code,bm.swift_code,bm.city,\r\n"
//			+ "smbank.state_name AS bank_state_name,bubank.business_unit_name AS account_name,\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2) AS tax1_value,\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2) AS tax2_value,\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2) AS tax3_value,\r\n"
//			+ "ROUND(\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2)+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2)+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2),2) AS total_tax,\r\n"
//			+ "ROUND(spni.total+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2)+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2)+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2),2) AS grand_total,\r\n"
//			+ "dbo.NumberToRupeesWords(ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2)) AS tax1_words,\r\n"
//			+ "dbo.NumberToRupeesWords(ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2)) AS tax2_words,\r\n"
//			+ "dbo.NumberToRupeesWords(ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2)) AS tax3_words,\r\n"
//			+ "dbo.NumberToRupeesWords(\r\n"
//			+ "ROUND(spni.total+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2)+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2)+\r\n"
//			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2),2)\r\n"
//			+ ") AS grand_total_words\r\n"
//			+ "FROM SCRAP_INVOICE_MASTER spn\r\n"
//			+ "INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code=spn.load_id\r\n"
//			+ "INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load=spn.scp_load\r\n"
//			+ "INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load=spn.scp_load\r\n"
//			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icm ON icm.id=scppnnote.vendor_id\r\n"
//			+ "INNER JOIN SCRAPSALESORDERTYPE_MASTER ssot ON ssot.saleorder_id=soe.sale_order_type_id\r\n"
//			+ "INNER JOIN SCRAPSALELOCATION ssl ON ssl.slid=soe.location_type_id\r\n"
//			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam ON icam.id=soe.sale_order_to_id\r\n"
//			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER ica ON ica.id=soe.billing_address_id\r\n"
//			+ "INNER JOIN BUSINESS_UNITS bu ON bu.business_unit_id=soe.business_unit_id\r\n"
//			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER isa ON isa.id=soe.shipping_address_id\r\n"
//			+ "LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id=soe.tax1\r\n"
//			+ "LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id=soe.tax2\r\n"
//			+ "LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id=soe.tax3\r\n"
//			+ "INNER JOIN BANK_MASTER bm ON bm.account_id=spn.bank_id\r\n"
//			+ "INNER JOIN STATE_MASTER sm ON sm.id=isa.state_id\r\n"
//			+ "INNER JOIN STATE_MASTER smb ON smb.id=ica.state_id\r\n"
//			+ "INNER JOIN STATE_MASTER smbank ON smbank.id=bm.state_id\r\n"
//			+ "INNER JOIN BUSINESS_UNITS bubank ON bubank.business_unit_id=bm.business_unit_id\r\n"
//			+ "WHERE spn.id= :scp_invoice_id ", nativeQuery = true)
//	ScrapInvoiceInfoInterface getScrapInvoDetails(String scp_invoice_id);
	
	
	
	
	
	
	@Query(value = "SELECT spn.*,soe.sale_order_type_id,ssot.saleorder_typename,spn.factory_id,soe.location_type_id,ssl.location_name,ssl.location_code,\r\n"
			+ "soe.sale_order_to_id,(icam.name_of_add+''+icam.add1+''+icam.add2) AS address,icam.gst_no as GstNo,icam.pan_no as PanNo,soe.sale_order_duration,soe.auction_date,soe.advance_payment,\r\n"
			+ "soe.billing_address_id,(ica.name_of_add+''+ica.add1+''+ica.add2) AS billing_address,isa.gst_no  as shippingtogst,isa.pan_no  as shippingPanNo,soe.shipping_address_id,(isa.name_of_add+''+isa.add1+''+isa.add2) AS shipping_address,\r\n"
			+ "soe.business_unit_id,bu.business_unit_name,soe.tax1,soe.tax2,soe.tax3,soe.net_amount,\r\n"
			+ "tm1.tax_desc AS tax1_desc,tm2.tax_desc AS tax2_desc,tm3.tax_desc AS tax3_desc,\r\n"
			+ "scppnnote.buyer_ref_no,scppnnote.sale_order_validity,scppnnote.weighbridge_no,\r\n"
			+ "scppnnote.transportername,scppnnote.central_excise_tarrif_no,spn.gst_remarks,\r\n"
			+ "COALESCE(tm1.tax_per,0) AS tax1_per,COALESCE(tm2.tax_per,0) AS tax2_per,COALESCE(tm3.tax_per,0) AS tax3_per,\r\n"
			+ "spn.shipment_mode,spn.delivery_condition,spn.bank_id,bm.bank_name,scppnnote.vehicleno,\r\n"
			+ "CONCAT('Scrap-',spn.scp_load) AS contract_no,\r\n"
			+ "sm.id AS shipping_state_id,sm.state_code AS shipping_state_code,sm.state_name AS shipping_state_name,\r\n"
			+ "ica.state_id AS billing_state_id,smb.state_name AS billing_state_name,smb.state_code AS billing_state_code,bu.state_id as bu_state_code,\r\n"
			+ "tm1.tax_name AS tax1_name,tm2.tax_name AS tax2_name,tm3.tax_name AS tax3_name,\r\n"
			+ "bm.account_number,bm.ifsc_code,bm.branch_address,bm.branch_code,bm.swift_code,bm.city,\r\n"
			+ "smbank.state_name AS bank_state_name,org.org_name AS account_name,\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2) AS tax1_value,\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2) AS tax2_value,\r\n"
			+ "ROUND((TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) "
			+ "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0) / 100,2) "
			+ "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0) / 100,2)) "
			+ "* COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0) / 100,2) AS tax3_value, "
			+ "ROUND(\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2)+\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2)+\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2),2) AS total_tax,\r\n"
			+ "ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) "
			+ "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0) / 100,2) "
			+ "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0) / 100,2) "
			+ "+ ROUND((TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) "
			+ "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0) / 100,2) "
			+ "+ ROUND(TRY_CAST(COALESCE(spni.total,'0') AS FLOAT) * COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0) / 100,2)) "
			+ "* COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0) / 100,2), "
			+ "2) AS grand_total, "
			+ "dbo.NumberToRupeesWords(ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2)) AS tax1_words,\r\n"
			+ "dbo.NumberToRupeesWords(ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2)) AS tax2_words,\r\n"
			+ "dbo.NumberToRupeesWords(ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2)) AS tax3_words,\r\n"
			+ "dbo.NumberToRupeesWords(\r\n"
			+ "ROUND(spni.total+\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm1.tax_per AS FLOAT),0)/100,2)+\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm2.tax_per AS FLOAT),0)/100,2)+\r\n"
			+ "ROUND(spni.total*COALESCE(TRY_CAST(tm3.tax_per AS FLOAT),0)/100,2),2)\r\n"
			+ ") AS grand_total_words\r\n"
			+ "FROM SCRAP_INVOICE_MASTER spn\r\n"
			+ "INNER JOIN SALE_ORDER_ENTRY soe ON soe.sale_order_code=spn.load_id\r\n"
			+ "INNER JOIN SCRAP_PACKING_NOTE scppnnote ON scppnnote.scp_load=spn.scp_load\r\n"
			+ "INNER JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.scp_load=spn.scp_load\r\n"
			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icm ON icm.id=scppnnote.vendor_id\r\n"
			+ "INNER JOIN SCRAPSALESORDERTYPE_MASTER ssot ON ssot.saleorder_id=soe.sale_order_type_id\r\n"
			+ "INNER JOIN SCRAPSALELOCATION ssl ON ssl.slid=soe.location_type_id\r\n"
			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam ON icam.id=soe.shipping_address_id\r\n"
			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER ica ON ica.id=soe.billing_address_id\r\n"
			+ "INNER JOIN BUSINESS_UNITS bu ON bu.business_unit_id=soe.business_unit_id\r\n"
			+ "INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER isa ON isa.id=soe.shipping_address_id\r\n"
			+ "LEFT JOIN TAX_MASTER tm1 ON tm1.tax_id=soe.tax1\r\n"
			+ "LEFT JOIN TAX_MASTER tm2 ON tm2.tax_id=soe.tax2\r\n"
			+ "LEFT JOIN TAX_MASTER tm3 ON tm3.tax_id=soe.tax3\r\n"
			+ "INNER JOIN BANK_MASTER bm ON bm.account_id=spn.bank_id\r\n"
			+ "INNER JOIN STATE_MASTER sm ON sm.id=isa.state_id\r\n"
			+ "INNER JOIN STATE_MASTER smb ON smb.id=ica.state_id\r\n"
			+ "INNER JOIN STATE_MASTER smbank ON smbank.id=bm.state_id\r\n"
			+ "INNER JOIN BUSINESS_UNITS bubank ON bubank.business_unit_id=bm.business_unit_id\r\n"
			+" INNER JOIN  ORGANIZATION_MASTER as org on  org.org_id = bubank.org_id\r\n"
			+ "WHERE spn.id= :scp_invoice_id ", nativeQuery = true)
	ScrapInvoiceInfoInterface getScrapInvoDetails(String scp_invoice_id);

	@Query(value = "select scp_load from SCRAP_INVOICE_MASTER where id = :scp_invoice_id", nativeQuery = true)
	String getScpLoadFromInvoiceTable(String scp_invoice_id);

//	@Query(value = "SELECT        spni.scp_pnitems_id, spni.scp_pn_id, spni.sale_order_code, spni.scp_load, spni.scrap_type_id, spni.scrapitem_id, spni.uom_id, spni.quantity, spni.kg, spni.unit_price, spni.total, spni.total_ordered, spni.total_sold, \r\n"
//			+ "                         spni.balance, spni.created_by, spni.created_date, spni.modified_by, spni.modified_date, spni.is_delete, spni.factory_id, spni.hsn_code, spni.service_code, spni.type_id, spni.is_locked, spni.is_verified, spni.verified_by, \r\n"
//			+ "                         stm.scrap_typename, sssi.scrapitemname, uom.unit_name, itm.remarks_type AS invoice_remarks_type, SERVICECODE_MASTER_1.service_code AS servicecodename, \r\n"
//			+ "                         dbo.SERVICECODE_MASTER.service_code AS hsncodename\r\n"
//			+ "FROM            dbo.SCRAP_PACKING_NOTE_ITEMS AS spni INNER JOIN\r\n"
//			+ "                         dbo.UOM_MASTER AS uom ON uom.unit_id = spni.uom_id INNER JOIN\r\n"
//			+ "                         dbo.SCRAPSALESCRAPTYPES_MASTER AS stm ON stm.stid = spni.scrap_type_id INNER JOIN\r\n"
//			+ "                         dbo.SCRAPSALESSCRAPITEMS_MASTER AS sssi ON sssi.siid = spni.scrapitem_id INNER JOIN\r\n"
//			+ "                         dbo.INVOICE_TYPE_REMARKS_MASTER AS itm ON itm.slno = spni.type_id LEFT OUTER JOIN\r\n"
//			+ "                         dbo.SERVICECODE_MASTER AS SERVICECODE_MASTER_1 ON spni.service_code = SERVICECODE_MASTER_1.servicecode_id LEFT OUTER JOIN\r\n"
//			+ "                         dbo.SERVICECODE_MASTER ON spni.hsn_code = dbo.SERVICECODE_MASTER.servicecode_id\r\n"
//			+ "WHERE        (spni.scp_load = :scp_load)", nativeQuery = true)
//	ScrapInvoiceItemsoInterface getScrapInvoiceItemsDetails(String scp_load);
	
	
	
	
	@Query(value = "SELECT        spni.scp_pnitems_id, spni.scp_pn_id, spni.sale_order_code, spni.scp_load, spni.scrap_type_id, spni.scrapitem_id, spni.uom_id, spni.quantity, spni.kg, spni.unit_price, spni.total, spni.total_ordered, spni.total_sold, \r\n"
			+ "                         spni.balance, spni.created_by, spni.created_date, spni.modified_by, spni.modified_date, spni.is_delete, spni.factory_id, spni.hsn_code, spni.service_code, spni.type_id, spni.is_locked, spni.is_verified, spni.verified_by, \r\n"
			+ "                         stm.scrap_typename, sssi.scrapitemname, uom.unit_name, itm.remarks_type AS invoice_remarks_type, SERVICECODE_MASTER_1.service_code AS servicecodename, \r\n"
			+ "                         dbo.SERVICECODE_MASTER.service_code AS hsncodename\r\n"
			+ "FROM            dbo.SCRAP_PACKING_NOTE_ITEMS AS spni INNER JOIN\r\n"
			+ "                         dbo.UOM_MASTER AS uom ON uom.unit_id = spni.uom_id INNER JOIN\r\n"
			+ "                         dbo.SCRAPSALESCRAPTYPES_MASTER AS stm ON stm.stid = spni.scrap_type_id INNER JOIN\r\n"
			+ "                         dbo.SCRAPSALESSCRAPITEMS_MASTER AS sssi ON sssi.siid = spni.scrapitem_id INNER JOIN\r\n"
			+ "                         dbo.INVOICE_TYPE_REMARKS_MASTER AS itm ON itm.slno = spni.type_id LEFT OUTER JOIN\r\n"
			+ "                         dbo.SERVICECODE_MASTER AS SERVICECODE_MASTER_1 ON spni.service_code = SERVICECODE_MASTER_1.servicecode_id LEFT OUTER JOIN\r\n"
			+ "                         dbo.SERVICECODE_MASTER ON spni.hsn_code = dbo.SERVICECODE_MASTER.servicecode_id\r\n"
			+ "WHERE        (spni.scp_load = :scp_load)", nativeQuery = true)
	ScrapInvoiceItemsoInterface getScrapInvoiceItemsDetails(String scp_load);

	@Query(value = " SELECT COUNT(scp_load) from SCRAP_INVOICE_MASTER where scp_load = :scp_load", nativeQuery = true)
	int checkWhetherInvoiceIsCreatedForThisPackingNote(String scp_load);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE set is_invoice = 1 where scp_load = :scp_load", nativeQuery = true)
	int updateScrapPackingNotetableForDetails(String scp_load);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SALE_ORDER_DESCRIPTION_ENTRY (soe_id, sl_no, terms_conditions, modified_by, modified_date) "
			+ "VALUES (:soe_id, :sl_no, :terms_conditions, :modified_by, GETDATE())", nativeQuery = true)
	int insertSaleOrderDescription(String soe_id, String sl_no, String terms_conditions, String modified_by);

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM SALE_ORDER_DESCRIPTION_ENTRY WHERE soe_id = :soe_id AND sl_no = :sl_no", nativeQuery = true)
	Integer existsBySoeIdAndSlNo(String soe_id, String sl_no);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_DESCRIPTION_ENTRY SET terms_conditions = :terms_conditions, modified_by = :modified_by, modified_date = GETDATE() WHERE soe_id = :soe_id AND sl_no = :sl_no", nativeQuery = true)
	int updateSaleOrderDescription(String terms_conditions, String modified_by, String soe_id, String sl_no);

	@Query(value = "SELECT TOP 1 sl_no from SALE_ORDER_DESCRIPTION_ENTRY where soe_id = :soe_id order by sl_no desc ", nativeQuery = true)
	int getMaxSlNo(String soe_id);

	@Query(value = "SELECT count(*) from  SCRAP_PACKING_NOTE_ITEMS where sale_order_code = :sale_order_code", nativeQuery = true)
	int getCountOfSaleOrderCode(String sale_order_code);

	@Query(value = "SELECT TOP 1 soe.sale_order_code, \r\n" + "       COALESCE(spni.scp_load, NULL) AS scp_load, \r\n"
			+ "       COALESCE(spni.total_ordered, \r\n" + "                CASE \r\n"
			+ "                    WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg \r\n"
			+ "                    ELSE soei.non_steel_kgs \r\n" + "                END) AS total_ordered, \r\n"
			+ "       COALESCE(spni.total_sold, 0) AS total_sold, \r\n" + "       COALESCE(spni.balance, \r\n"
			+ "                CASE \r\n"
			+ "                    WHEN (SELECT COUNT(*) FROM SALE_ORDER_ENTRY WHERE sale_order_code = soe.sale_order_code) = 1 \r\n"
			+ "                    THEN CASE WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg ELSE soei.non_steel_kgs END\r\n"
			+ "                    ELSE CASE WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg ELSE soei.non_steel_kgs END - COALESCE(spni.total_sold, 0) \r\n"
			+ "                END) AS balance, \r\n" + "       COALESCE(spni.balance, \r\n"
			+ "                CASE \r\n"
			+ "                    WHEN (SELECT COUNT(*) FROM SALE_ORDER_ENTRY WHERE sale_order_code = soe.sale_order_code) = 1 \r\n"
			+ "                    THEN CASE WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg ELSE soei.non_steel_kgs END\r\n"
			+ "                    ELSE CASE WHEN soei.kg IS NOT NULL AND soei.kg > 0 THEN soei.kg ELSE soei.non_steel_kgs END - COALESCE(spni.total_sold, 0) \r\n"
			+ "                END) AS dummy_balance, \r\n" + "       spni.created_date, soei.tolerance,  \r\n"
			+ "       ROUND((\r\n" + "             CASE \r\n"
			+ "                 WHEN soei.kg IS NOT NULL AND soei.kg > 0 \r\n"
			+ "                 THEN (soei.kg + (soei.kg * soei.tolerance / 100.0)) \r\n"
			+ "                 ELSE (soei.non_steel_kgs + (soei.non_steel_kgs * soei.tolerance / 100.0)) \r\n"
			+ "             END\r\n" + "             ) - \r\n" + "             CASE \r\n"
			+ "                 WHEN (SELECT COUNT(*) FROM SALE_ORDER_ENTRY WHERE sale_order_code = soe.sale_order_code) = 1 \r\n"
			+ "                 THEN 0 \r\n"
			+ "                 ELSE (SELECT COALESCE(SUM(spni_inner.total_sold), 0) \r\n"
			+ "                       FROM SCRAP_PACKING_NOTE_ITEMS spni_inner \r\n"
			+ "                       WHERE spni_inner.sale_order_code = soe.sale_order_code) \r\n"
			+ "             END, 2) AS tolerance_value  \r\n" + "FROM SALE_ORDER_ENTRY soe \r\n"
			+ "LEFT JOIN SALE_ORDER_ITEM_LEVEL_ENTRY soei ON soei.soe_id = soe.soe_id \r\n"
			+ "LEFT JOIN SCRAP_PACKING_NOTE_ITEMS spni ON spni.sale_order_code = soe.sale_order_code \r\n"
			+ "WHERE soe.sale_order_code = :sale_order_code\r\n" + "ORDER BY scp_load DESC;\r\n"
			+ "", nativeQuery = true)
	LatestScrapNoteBalanceInterface getBalanceDuringFirstUpdate(String sale_order_code);

	@Query(value = "SELECT sale_order_code from SCRAP_PACKING_NOTE where scp_load = :scp_load", nativeQuery = true)
	String getSale_order_code(String scp_load);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_INVOICE_MASTER SET invoice_type = :invoice_type, shipment_mode = :shipment_mode, "
			+ "delivery_condition = :delivery_condition, bank_id = :bank_id, product_desc = :product_desc, "
			+ "remarks = :remarks, modified_by = :modified_by, modified_date = GETDATE() WHERE id = :scp_invoice_id", nativeQuery = true)
	int updateScrapInvoiceTable(String invoice_type, String shipment_mode, String delivery_condition, String bank_id,
			String product_desc, String remarks, String modified_by, String scp_invoice_id);
	
	@Transactional
	@Modifying
	@Query(value =
	    "UPDATE SCRAP_INVOICE_MASTER " +
	    "SET Cancel = 1, Cancel_by = :cancelled_by, Cancel_date = GETDATE() " +
	    "WHERE id = :id",
	    nativeQuery = true)
	int updatescrapInvoiceCancelById( String id,
	                      String cancelled_by);


	@Transactional
	@Modifying
	@Query(
	    value = "UPDATE SCRAP_PACKING_NOTE " +
	            "SET Cancel = 1 " +
	            "WHERE scp_load = :scp_load",
	    nativeQuery = true
	)
	int updatedbtcrdQSPackingCancelByPnId( String scp_load);
	
	@Query(value=" select concat(dbo.NumberToRupeesWords(:grandTotalValue),' Only')", nativeQuery = true)
	String getgrandTotalInWordsQuery(String grandTotalValue);
}
