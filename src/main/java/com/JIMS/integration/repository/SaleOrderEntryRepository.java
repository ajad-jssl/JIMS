package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.JIMS.integration.entity.SaleOrderEntry;
import com.JIMS.integration.interfaces.SaleOrderInterface;
import com.JIMS.integration.interfaces.SaleOrderItemDescLevelInterface;
import com.JIMS.integration.interfaces.SaleOrderItemLevelInterface;
import com.JIMS.integration.interfaces.allSaleOrderEntriesInterface;

@Repository
public interface SaleOrderEntryRepository extends JpaRepository<SaleOrderEntry, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SALE_ORDER_ITEM_LEVEL_ENTRY (soe_id, auction_no, scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, tolerance, created_by, created_date)\r\n"
			+ "VALUES (:soeid, :auction_no, :scrap_type_id, :scrapitem_id, :uom_id, :quantity, :kg, :unit_price, :total, :tolerance, :created_by, GETDATE())", nativeQuery = true)
	int insertIntoSaleOrderItemLevelEntry(int soeid, String auction_no, String scrap_type_id, String scrapitem_id,
			String uom_id, String quantity, String kg, String unit_price, String total, String tolerance,
			String created_by);

	@Transactional
	@Modifying
	@Query(value = "insert into SALE_ORDER_DESCRIPTION_ENTRY (soe_id, sl_no, terms_conditions, created_by, created_date) VALUES (:soeid, :sl_no,  :terms_conditions, :created_by, GETDATE()) ", nativeQuery = true)
	int insertIntoSaleOrderDescriptionLevelEntry(int soeid, int sl_no, String terms_conditions, String created_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_ITEM_LEVEL_ENTRY " + "SET scrap_type_id = :scrap_type_id, "
			+ "scrapitem_id = :scrapitem_id, " + "uom_id = :uom_id, " + "quantity = :quantity, " + "kg = :kg, "
			+ "unit_price = :unit_price, " + "total = :total, " + "tolerance = :tolerance, "
			+ "modified_by = :modified_by, " + "modified_date = GETDATE() "
			+ "WHERE soe_id = :soe_id AND auction_no = :auction_no", nativeQuery = true)
	int updateSaleOrderItemLevel(Integer auction_no, Integer scrap_type_id, Integer scrapitem_id, Integer uom_id,
			Integer quantity, Integer kg, Float unit_price, Float total, Integer tolerance, String modified_by,
			String soe_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_DESCRIPTION_ENTRY set terms_conditions = :terms_conditions, modified_by = :modified_by WHERE soe_id = :soe_id and sl_no = :sl_no", nativeQuery = true)
	int updateDescriptionLevelSaleOrderEntry(String soe_id, String sl_no, String terms_conditions, String modified_by);

	@Query(value = "SELECT soe_id, sale_order_type_id,sale_order_code, auction_date, stm.scrap_type, location_type_id,ssl.location_name, ssl.location_code , sale_order_to_id,icam.address ,sale_order_duration, advance_payment, \r\n"
			+ "billing_address_id, shipping_address_id, business_unit_id, tax1, tax1_value, tax2, tax2_value,\r\n"
			+ "tax3, tax3_value, net_amount, total_tax, grand_total from SALE_ORDER_ENTRY soe\r\n"
			+ "left join SCRAP_TYPE_MASTER stm on stm.scrap_type_id = soe.sale_order_type_id\r\n"
			+ "left join SCRAPSALELOCATION ssl on ssl.slid = soe.location_type_id\r\n"
			+ "left join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = soe.sale_order_to_id\r\n"
			+ " where soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderInterface> getAllSaleOrderDetailsBAsedOnId(String soe_id);

	@Query(value = "SELECT soe_id, auction_no, scrap_type_id, scrapitem_id, ssm.scrap_typename, spm.scrapitemname, uom_id, um.unit_name, quantity, kg, unit_price, total, tolerance " +
            "FROM SALE_ORDER_ITEM_LEVEL_ENTRY " +
            "LEFT JOIN SCRAPSALESCRAPTYPES_MASTER ssm ON ssm.stid = SALE_ORDER_ITEM_LEVEL_ENTRY.soe_id " +
            "LEFT JOIN SCRAPSALESSCRAPITEMS_MASTER spm ON spm.siid = SALE_ORDER_ITEM_LEVEL_ENTRY.scrapitem_id " +
            "LEFT JOIN UOM_MASTER um ON um.unit_id = SALE_ORDER_ITEM_LEVEL_ENTRY.uom_id " +
            "WHERE soe_id = :soe_id", nativeQuery = true)
List<SaleOrderItemLevelInterface> getSaleOrderItemLevelDetails(String soe_id);

	@Query(value = "SELECT soe_id, sl_no, terms_conditions from SALE_ORDER_DESCRIPTION_ENTRY where soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderItemDescLevelInterface> getSaleOrderDescriptionLevelDetails(String soe_id);

	@Query(value = " select count(*) from SALE_ORDER_ENTRY where soe_id = :id and is_locked = 1", nativeQuery = true)
	int checkisLocked(String id);

	@Query(value = "SELECT soe.factory_id AS factory_id, \r\n" 
	       + "soe.sale_order_code AS sale_order_code, \r\n" 
	       + "soe.auction_date AS auction_date, \r\n"
	       + "(icam.name_of_add+''+icam.add1+''+icam.add2) AS address, \r\n"
	       + "ssl.location_name AS location_name, \r\n" 
	       + "sot.saleorder_typename AS saleorder_typename \r\n" 
	       + "FROM SALE_ORDER_ENTRY soe \r\n" 
	       + "LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam ON icam.id = soe.sale_order_to_id \r\n" 
	       + "LEFT JOIN SCRAPSALELOCATION ssl ON ssl.slid = soe.location_type_id \r\n" 
	       + "LEFT JOIN SCRAPSALESORDERTYPE_MASTER sot ON sot.saleorder_id = soe.sale_order_type_id \r\n" 
	       + "WHERE soe.factory_id = :factory_id AND soe.is_delete = 0", nativeQuery = true)
	List<allSaleOrderEntriesInterface> getAllSaleOrderDetailsBasedOnFactory(String factory_id);
	
	
	
	
	@Query(
			value =
			"SELECT soe.factory_id AS factory_id, " +
			"soe.sale_order_code AS sale_order_code, " +
			"soe.auction_date AS auction_date, " +
			"(icam.name_of_add+''+icam.add1+''+icam.add2) AS address, " +
			"ssl.location_name AS location_name, " +
			"sot.saleorder_typename AS saleorder_typename " +

			"FROM SALE_ORDER_ENTRY soe " +

			"LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam ON icam.id = soe.sale_order_to_id " +
			"LEFT JOIN SCRAPSALELOCATION ssl ON ssl.slid = soe.location_type_id " +
			"LEFT JOIN SCRAPSALESORDERTYPE_MASTER sot ON sot.saleorder_id = soe.sale_order_type_id " +

			"WHERE soe.factory_id = :factory_id " +
			"AND soe.is_delete = 0 " +

			"AND ( :search IS NULL OR :search = '' OR " +

			"soe.sale_order_code LIKE CONCAT('%',:search,'%') OR " +
			"(icam.name_of_add+''+icam.add1+''+icam.add2) LIKE CONCAT('%',:search,'%') OR " +
			"ssl.location_name LIKE CONCAT('%',:search,'%') OR " +
			"sot.saleorder_typename LIKE CONCAT('%',:search,'%') OR " +

			"CONVERT(VARCHAR(10), soe.auction_date, 105) LIKE CONCAT('%',:search,'%') " +

			") " +

			"ORDER BY soe.auction_date DESC",

			countQuery =
			"SELECT COUNT(*) " +
			"FROM SALE_ORDER_ENTRY soe " +

			"LEFT JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam ON icam.id = soe.sale_order_to_id " +
			"LEFT JOIN SCRAPSALELOCATION ssl ON ssl.slid = soe.location_type_id " +
			"LEFT JOIN SCRAPSALESORDERTYPE_MASTER sot ON sot.saleorder_id = soe.sale_order_type_id " +

			"WHERE soe.factory_id = :factory_id " +
			"AND soe.is_delete = 0 " +

			"AND ( :search IS NULL OR :search = '' OR " +

			"soe.sale_order_code LIKE CONCAT('%',:search,'%') OR " +
			"(icam.name_of_add+''+icam.add1+''+icam.add2) LIKE CONCAT('%',:search,'%') OR " +
			"ssl.location_name LIKE CONCAT('%',:search,'%') OR " +
			"sot.saleorder_typename LIKE CONCAT('%',:search,'%') OR " +

			"CONVERT(VARCHAR(10), soe.auction_date, 105) LIKE CONCAT('%',:search,'%') " +

			")",

			nativeQuery = true
			)
			Page<allSaleOrderEntriesInterface> getAllSaleOrderDetailsBasedOnFactoryPaged(
			        @Param("factory_id") String factory_id,
			        @Param("search") String search,
			        Pageable pageable
			);
	
	@Query(value = "SELECT soe_id from SALE_ORDER_ENTRY where sale_order_code = :sale_order_code",nativeQuery = true)
	String getSoeIdBasedOnSaleOrderCode(String sale_order_code);

}
