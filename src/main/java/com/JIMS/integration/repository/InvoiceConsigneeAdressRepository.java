
package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.InvoiceConsigneeAddressInterface;

import jakarta.transaction.Transactional;

public interface InvoiceConsigneeAdressRepository extends JpaRepository<User, Integer> {

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "INSERT into INVOICE_CONSIGNEE_ADDRESS_MASTER (address, city, district, state_id, country_id, pin_no, created_by, created_date, is_invoice, is_consignee,code,gst_no,pan_no, factory_id) VALUES (:address, :city, :district, :state_id, :country_id, :pin_no, :created_by, GETDATE(),:is_invoice, :is_consignee, :count,:gst_no,:pan_no, :factory_id)"
	 * , nativeQuery = true) int addInvoiceAddress(String address, String city,
	 * String district, String state_id, String country_id, String pin_no, String
	 * created_by, String is_invoice, String is_consignee, int count, String gst_no,
	 * String pan_no, String factory_id);
	 */
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_CONSIGNEE_ADDRESS_MASTER " +
	    "(name_of_add, add1, add2, city, district, state_id, country_id, pin_no, " +
	    "created_by, created_date, is_invoice, is_consignee, code, gst_no, pan_no, factory_id) " +
	    "VALUES (:name_of_add, :add1, :add2, :city, :district, :state_id, :country_id, :pin_no, " +
	    ":created_by, GETDATE(), :is_invoice, :is_consignee, :count, :gst_no, :pan_no, :factory_id)",
	    nativeQuery = true)
	int addInvoiceAddress(
	    @Param("name_of_add") String name_of_add,
	    @Param("add1") String add1,
	    @Param("add2") String add2,
	    @Param("city") String city,
	    @Param("district") String district,
	    @Param("state_id") String state_id,
	    @Param("country_id") String country_id,
	    @Param("pin_no") String pin_no,
	    @Param("created_by") String created_by,
	    @Param("is_invoice") String is_invoice,
	    @Param("is_consignee") String is_consignee,
	    @Param("count") int count,
	    @Param("gst_no") String gst_no,
	    @Param("pan_no") String pan_no,
	    @Param("factory_id") String factory_id
	);

	// ✅ UPDATE - changed 'address' to name_of_add, add1, add2
	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER " +
	    "SET name_of_add = :name_of_add, add1 = :add1, add2 = :add2, " +
	    "city = :city, district = :district, state_id = :state_id, " +
	    "country_id = :country_id, modified_by = :modified_by, " +
	    "modified_date = GETDATE(), gst_no = :gst_no, pan_no = :pan_no, " +
	    "pin_no = :pin_no WHERE id = :address_id", nativeQuery = true)
	int updateAddressRecord(
	    @Param("name_of_add") String name_of_add,
	    @Param("add1") String add1,
	    @Param("add2") String add2,
	    @Param("city") String city,
	    @Param("district") String district,
	    @Param("state_id") String state_id,
	    @Param("country_id") String country_id,
	    @Param("modified_by") String modified_by,
	    @Param("gst_no") String gst_no,
	    @Param("pan_no") String pan_no,
	    @Param("pin_no") String pin_no,
	    @Param("address_id") String address_id
	);


	// ✅ HISTORY INSERT on UPDATE - changed 'address' to name_of_add, add1, add2
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_CONSIGNEE_ADDRESS_MASTER_HISTORY " +
	    "(address_id, name_of_add, add1, add2, city, district, " +
	    "state_id, country_id, is_invoice, is_consignee, " +
	    "modified_by, modified_date, transaction_date, action) " +
	    "SELECT id, name_of_add, add1, add2, city, district, " +
	    "state_id, country_id, is_invoice, is_consignee, " +
	    ":modified_by, GETDATE(), GETDATE(), 'UPDATE' " +
	    "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER " +
	    "WHERE id = :address_id", nativeQuery = true)
	int moveToHistoryTable(
	    @Param("modified_by") String modified_by,
	    @Param("address_id") String address_id
	);


	// ✅ SOFT DELETE - no address column used, this one is fine as-is
	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER " +
	    "SET is_delete = 1, modified_by = :user_id, " +
	    "modified_date = GETDATE() WHERE id = :address_id", nativeQuery = true)
	int deleteInvConsigneeAddress(
	    @Param("address_id") String address_id,
	    @Param("user_id") String user_id
	);


	// ✅ HISTORY INSERT on DELETE - changed 'address' to name_of_add, add1, add2
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_CONSIGNEE_ADDRESS_MASTER_HISTORY " +
	    "(address_id, name_of_add, add1, add2, city, district, " +
	    "state_id, country_id, deleted_by, deleted_date, transaction_date, action) " +
	    "SELECT id, name_of_add, add1, add2, city, district, " +
	    "state_id, country_id, :user_id, GETDATE(), GETDATE(), 'DELETE' " +
	    "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER " +
	    "WHERE id = :address_id", nativeQuery = true)
	int moveToHistoryTableBeforeDelete(
	    @Param("address_id") String address_id,
	    @Param("user_id") String user_id
	);

	@Query(value = " SELECT " +
	        "am.id, " +

	        "ISNULL(am.name_of_add,'') + " +
	        "CASE " +
	        "WHEN ISNULL(am.add1,'') <> '' " +
	        "THEN CHAR(13) + CHAR(10) + am.add1 " +
	        "ELSE '' " +
	        "END + " +
	        "CASE " +
	        "WHEN ISNULL(am.add2,'') <> '' " +
	        "THEN CHAR(13) + CHAR(10) + am.add2 " +
	        "ELSE '' " +
	        "END AS address, " +

	        "am.city, " +
	        "am.district, " +
	        "am.state_id, " +
	        "sm.state_name, " +
	        "am.country_id, " +
	        "cm.country_name, " +
	        "am.gst_no, " +
	        "am.pan_no, " +
	        "am.pin_no, " +
	        "am.factory_id, " +
	        "am.is_verified, " +
	        "am.verified_by, " +
	        "am.verified_date " +

	        "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER am " +

	        "LEFT JOIN STATE_MASTER sm ON sm.id = am.state_id " +
	        "LEFT JOIN COUNTRY_MASTER cm ON cm.id = am.country_id " +

	        "WHERE am.is_delete = 0 " +
	        "AND am.is_invoice = 1 " +
	        "AND (:factory_id IS NULL OR am.factory_id = :factory_id)",

	        nativeQuery = true)
	List<InvoiceConsigneeAddressInterface> getInvoiceAddressDetails(String factory_id); //modified by anusha on 25-05-2026

	@Query(value = " SELECT " +
	        "am.id, " +

	        "ISNULL(am.name_of_add,'') + " +
	        "CASE " +
	        "WHEN ISNULL(am.add1,'') <> '' " +
	        "THEN CHAR(13) + CHAR(10) + am.add1 " +
	        "ELSE '' " +
	        "END + " +
	        "CASE " +
	        "WHEN ISNULL(am.add2,'') <> '' " +
	        "THEN CHAR(13) + CHAR(10) + am.add2 " +
	        "ELSE '' " +
	        "END AS address, " +

	        "am.city, " +
	        "am.district, " +
	        "am.state_id, " +
	        "sm.state_name, " +
	        "am.country_id, " +
	        "cm.country_name, " +
	        "am.gst_no, " +
	        "am.pan_no, " +
	        "am.pin_no, " +
	        "am.factory_id, " +
	        "am.is_verified, " +
	        "am.verified_by, " +
	        "am.verified_date, " +
	        "am.is_release, " +
	        "am.released_by, " +
	        "am.released_date " +

	        "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER am " +

	        "LEFT JOIN STATE_MASTER sm ON sm.id = am.state_id " +
	        "LEFT JOIN COUNTRY_MASTER cm ON cm.id = am.country_id " +

	        "WHERE am.is_delete = 0 " +
	        "AND am.is_consignee = 1 " +
	        "AND (:factory_id IS NULL OR am.factory_id = :factory_id)",

	        nativeQuery = true)
	List<InvoiceConsigneeAddressInterface> getConsigneeAddressDetails(String factory_id); //modified by anusha on 25-05-2026

	@Query(value = " select count(*) from INVOICE_CONSIGNEE_ADDRESS_MASTER", nativeQuery = true)
	int countNumberOfRows();

	@Query(value = "select " +
	        "icam.id, " +

	        "ISNULL(icam.name_of_add,'') + " +
	        "CASE " +
	        "WHEN ISNULL(icam.add1,'') <> '' " +
	        "THEN CHAR(13) + CHAR(10) + icam.add1 " +
	        "ELSE '' " +
	        "END + " +
	        "CASE " +
	        "WHEN ISNULL(icam.add2,'') <> '' " +
	        "THEN CHAR(13) + CHAR(10) + icam.add2 " +
	        "ELSE '' " +
	        "END AS address, " +

	        "icam.city, " +
	        "icam.district, " +

	        "sm.id as state_id, " +
	        "cm.id as country_id, " +

	        "sm.state_code, " +
	        "sm.state_name, " +
	        "cm.country_code, " +
	        "cm.country_name, " +

	        "icam.pin_no, " +
	        "icam.pin_no as pin_code, " +
	        "icam.gst_no, " +
	        "icam.pan_no, " +
	        "icam.is_consignee, " +
	        "icam.factory_id, " +
	        "icam.is_invoice " +

	        "from INVOICE_CONSIGNEE_ADDRESS_MASTER icam " +

	        "LEFT JOIN STATE_MASTER sm on sm.id = icam.state_id " +
	        "LEFT JOIN COUNTRY_MASTER cm on cm.id = icam.country_id " +

	        "where icam.id = :address_id and icam.is_delete = 0",

	        nativeQuery = true)
	InvoiceConsigneeAddressInterface getConsigneeAddressDetailsById(String address_id); //modified by anusha on 12-05-2026

	@Query(value = "select count(*) from CONTRACT_MASTER where consignee_id = :address_id and is_locked = 1", nativeQuery = true)
	int checkConsigneeAddressIdPresentInContractMaster(String address_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where invoice_to_id = :address_id and is_locked = 1", nativeQuery = true)
	int checkInvoiceAddressIdPresentInContractMaster(String address_id);

	/*
	 * @Query(value = "SELECT CASE " +
	 * "WHEN (SELECT COUNT(*) FROM INVOICE_CONSIGNEE_ADDRESS_MASTER " +
	 * "      WHERE UPPER(LTRIM(RTRIM(address))) = UPPER(LTRIM(RTRIM(:address))) " +
	 * "        AND is_consignee = :is_consignee " +
	 * "        AND ((factory_id IS NULL AND :factory_id IS NULL) OR factory_id = :factory_id)) > 0 "
	 * + "THEN 'Consigneed  Address is already exist \r\n" + "' " +
	 * "WHEN (SELECT COUNT(*) FROM INVOICE_CONSIGNEE_ADDRESS_MASTER " +
	 * "      WHERE UPPER(LTRIM(RTRIM(address))) = UPPER(LTRIM(RTRIM(:address))) " +
	 * "        AND is_invoice = :is_invoice " +
	 * "        AND ((factory_id IS NULL AND :factory_id IS NULL) OR factory_id = :factory_id)) > 0 "
	 * + "THEN 'Invoice Address is already exist' " + "ELSE 'NEW' END AS message",
	 * nativeQuery = true) String checkWhetherAddressExistsOrNot(String address,
	 * String is_consignee, String is_invoice, String factory_id);
	 */
	
	@Query(value = "SELECT CASE " +
		    "WHEN (SELECT COUNT(*) FROM INVOICE_CONSIGNEE_ADDRESS_MASTER " +
		    "WHERE UPPER(LTRIM(RTRIM(name_of_add))) = UPPER(LTRIM(RTRIM(:name_of_add))) " +
		    "AND is_consignee = :is_consignee " +
		    "AND ((factory_id IS NULL AND :factory_id IS NULL) OR factory_id = :factory_id)) > 0 " +
		    "THEN 'Consignee Address already exists' " +
		    "WHEN (SELECT COUNT(*) FROM INVOICE_CONSIGNEE_ADDRESS_MASTER " +
		    "WHERE UPPER(LTRIM(RTRIM(name_of_add))) = UPPER(LTRIM(RTRIM(:name_of_add))) " +
		    "AND is_invoice = :is_invoice " +
		    "AND ((factory_id IS NULL AND :factory_id IS NULL) OR factory_id = :factory_id)) > 0 " +
		    "THEN 'Invoice Address already exists' " +
		    "ELSE 'NEW' END AS message", nativeQuery = true)
		String checkWhetherAddressExistsOrNot(
		    @Param("name_of_add") String name_of_add,
		    @Param("is_consignee") String is_consignee,
		    @Param("is_invoice") String is_invoice,
		    @Param("factory_id") String factory_id
		);

	@Query(value = "select case\r\n"
			+ "WHEN (select count(*) from SALE_ORDER_ENTRY where (billing_address_id = :address_id or shipping_address_id = :address_id)) > 0 \r\n"
			+ "THEN 'The transaction already exist for Address'\r\n"
			+ "WHEN (select COUNT(*) from CONTRACT_MASTER where (invoice_to_id = :address_id or consignee_id = :address_id)) > 0\r\n"
			+ "THEN 'The transaction already exist for Address'\r\n"
			+ "WHEN (select COUNT(*) from Others_invoice_master where (Invoice_to_id = :address_id or consignee_id = :address_id))  > 0\r\n"
			+ "THEN 'The transaction already exist for Address'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWhetherAddressInvolvedInTransactionsOrNot(String address_id);

	@Query(value = "UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER SET is_verified = 1, verified_by = :verified_by, verified_date = GETDATE() WHERE id = :id", nativeQuery = true)
	@Modifying
	@Transactional
	int verifyAddressRecord(@Param("id") String id, @Param("verified_by") String verified_by);


	@Query(value = "SELECT CASE WHEN is_verified = 1 THEN 'yes' ELSE NULL END " +
	               "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER WHERE id = :id", nativeQuery = true)
	String checkIfAlreadyVerified(@Param("id") String id);
	
	@Query(value =
		    "UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER " +
		    "SET is_release = CASE WHEN ISNULL(is_release, 0) = 0 THEN 1 ELSE 0 END, " + // ✅ Toggle
		    "    is_verified = 0, " +
		    "    released_by = :released_by, " +
		    "    released_date = GETDATE() " +
		    "WHERE id = :id",
		    nativeQuery = true)
		@Modifying
		@Transactional
		int releaseAddressRecord(
		        @Param("id") Integer id,
		        @Param("released_by") Integer released_by);


	@Query(value =
		    "SELECT CASE WHEN is_release = 0 THEN 'yes' ELSE NULL END " + // ✅ Changed from 1 → 0
		    "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER WHERE id = :id",
		    nativeQuery = true)
		String checkIfAlreadyReleased(@Param("id") String id);

@Query(value =
"UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER " +
"SET is_verified = 1, " +
"verified_by = :verified_by, " +
"verified_date = GETDATE() " +
"WHERE id = :id " +
"AND is_consignee = 1",
nativeQuery = true)
@Modifying
@Transactional
int verifyConsigneeAddressRecord(
@Param("id") String id,
@Param("verified_by") String verified_by
);

@Query(value =
"SELECT CASE " +
"WHEN is_verified = 1 THEN 'yes' " +
"ELSE NULL END " +
"FROM INVOICE_CONSIGNEE_ADDRESS_MASTER " +
"WHERE id = :id " +
"AND is_consignee = 1",
nativeQuery = true)
String checkIfAlreadyVerifiedConsignee(
@Param("id") String id
);

@Query(value =
"UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER " +
"SET is_release = CASE WHEN ISNULL(is_release, 0) = 0 THEN 1 ELSE 0 END, " + // ✅ Toggle
"    is_verified = 0, " +
"    released_by = :released_by, " +
"    released_date = GETDATE() " +
"WHERE id = :id " +
"AND is_consignee = 1",
nativeQuery = true)
@Modifying
@Transactional
int releaseConsigneeAddressRecord(
    @Param("id") Integer id,
    @Param("released_by") Integer released_by);
}
