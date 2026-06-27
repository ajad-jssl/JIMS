package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.JIMS.integration.entity.ScrapInvoice;

public interface ScrapInvoiceRepository extends JpaRepository<ScrapInvoice, Integer> {

	@Transactional
	@Modifying
	@Query(value = "update SCRAP_PACKING_NOTE_ITEMS set hsn_code = :hsn_code_id, service_code = :service_code_id,type_id = :type_id,  modified_by = :created_by, modified_date = GETDATE() where scp_load = :scp_load and scp_pnitems_id = :scp_pnitems_id", nativeQuery = true)
	int updatePackingNoteItemsTable(String hsn_code_id, String service_code_id, String type_id, String scp_load,
			String scp_pnitems_id, String created_by);

	@Query(value = "SELECT MAX(invoice_no) AS highest_invoice_no \r\n"
			+ "FROM (\r\n"
			+ "    SELECT invoice_no FROM INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
			+ "    UNION ALL\r\n"
			+ "    SELECT invoice_no FROM SCRAP_INVOICE_MASTER WHERE invoice_no like %?1% \r\n"
			+ ") AS CombinedInvoices", nativeQuery = true)
	Optional<Long> getMaxInvoiceNumberBasedOnSeriesNumber(String invoiceNumber);

	@Query(value = "select invoice_series from SERIES_MASTER where is_gst =1 and (status is NULL or status ='Open') \r\n"
			+ "and  state_id = (\r\n" + "select top 1 bu.state_id from SCRAP_INVOICE_MASTER scm\r\n"
			+ "inner join SALE_ORDER_ENTRY soe on soe.sale_order_code = scm.load_id\r\n"
			+ "inner join BUSINESS_UNITS bu on bu.business_unit_id = soe.business_unit_id\r\n"
			+ "where scm.load_id =:load_id and id = :id)", nativeQuery = true)
	Optional<Long> getSeriesNumberbasedOnId(String id, String load_id);

	@Query(value = "SELECT MAX(invoice_no) AS max_invoice_no FROM SCRAP_INVOICE_MASTER", nativeQuery = true)
	Optional<Long> getInvoiceNumber();

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * " UPDATE SCRAP_INVOICE_MASTER set verified_status ='VERIFIED', verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE(), is_release = 0, gst_remarks = :gst_remarks where id  = :id"
	 * , nativeQuery = true) int updateScrapInvoiceVerificationDetails(String
	 * non_tax_adv, String tax_adv, String total, String payable_by_customer, String
	 * payable_to_dept, String open_tax_adv, String open_non_tax_adv, String
	 * recovery_amt, String verified_by, String invoice_no, String gst_remarks, int
	 * id);
	 */
	@Transactional
	@Modifying
	@Query(value = "delete from INVOICE_TAXENTRY_DETAILS where invoice_id = :invoice_id", nativeQuery = true)
	void detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String invoice_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_TAXENTRY_DETAILS(tax_id,tax_per,tax_value,adv_tax,tax_payable_by_customer,tax_payable_to_dept,t_adv,invoice_id,created_by,created_date) VALUES (:tax_id,:tax_per,:tax_value,:adv_tax,:tax_payable_by_customer,:tax_payable_to_dept,:t_adv,:invoice_id,:created_by,GETDATE())", nativeQuery = true)
	void insertINVOICE_TAXENTRY_DETAILS(String tax_id, String tax_per, String tax_value, String adv_tax,
			String tax_payable_by_customer, String tax_payable_to_dept, String t_adv, String invoice_id,
			String created_by);

	@Query(value = "select scp_load from SCRAP_INVOICE_MASTER WHERE id = :id", nativeQuery = true)
	String getSaleOrderEntryIdFromInvoiceMaster(int id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE SET is_locked = :locked where scp_load = :scp_load", nativeQuery = true)
	void updateIsLockInScrapPackingNote(String scp_load, String locked);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_PACKING_NOTE_ITEMS SET is_locked = :locked where scp_load = :scp_load", nativeQuery = true)
	void updateIsLockScrapPackingNoteEntry(String scp_load, String locked);

	@Query(value = "Select sale_order_code from SCRAP_PACKING_NOTE where scp_load = :scp_load", nativeQuery = true)
	String getSaleOrderCodeFromScrapPackingNote(String scp_load);

	@Query(value = "Select soe_id,billing_address_id,shipping_address_id,business_unit_id from SALE_ORDER_ENTRY where sale_order_code = :saleOrderCode", nativeQuery = true)
	List<String> getSaleOrderEntryId(String saleOrderCode);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_ENTRY SET is_locked = :locked where saleOrderCode = :saleOrderCode", nativeQuery = true)
	void updateIsLockSaleOrderEntry(String saleOrderCode, String locked);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_ITEM_LEVEL_ENTRY SET is_locked = :locked where soe_id = :soe_id", nativeQuery = true)
	void updateIsLockSaleOrderItemLevelEntry(long soe_id, String locked);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_DESCRIPTION_ENTRY SET is_locked = :locked where soe_id = :soe_id", nativeQuery = true)
	void updateIsLockSaleOrderDescEntry(long soe_id, String locked);

	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER SET is_locked = :locked where id = :billingAddressId", nativeQuery = true)
	void updateIsLockInvoiceConsigneeAddress(String billingAddressId, String locked);

	@Transactional
	@Modifying
	@Query(value = "UPDATE BUSINESS_UNITS SET is_locked = :locked where business_unit_id = :business_unit_id", nativeQuery = true)
	void updateIsLockBusinessUnit(String business_unit_id, String locked);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_INVOICE_MASTER set is_release = 1 where id = :id", nativeQuery = true)
	int UpdateReleaseById(String id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_ENTRY SET is_locked = :locked where scp_load = :scp_load", nativeQuery = true)
	void updateISReleaseInSaleOrderEntry(String scp_load, String locked);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAP_INVOICE_MASTER set verified_status ='VERIFIED', verified_by =:verified_by ,invoice_no = :newSeriesNumber ,verified_date = GETDATE(), is_release = 0, gst_remarks = :gst_remarks where id  = :id", nativeQuery = true)
	int updateScrapInvoiceVerificationDetails(String verified_by, String newSeriesNumber, String gst_remarks,
			String id);

}
