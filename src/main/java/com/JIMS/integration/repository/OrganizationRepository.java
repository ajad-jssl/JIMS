package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.BankMasterInterface;
import com.JIMS.integration.interfaces.BusinessUnitInterface;
import com.JIMS.integration.interfaces.OrganizationMasterInterface;

import jakarta.transaction.Transactional;

public interface OrganizationRepository extends JpaRepository<User, Integer> {

	/* ORGANIZATION START */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ORGANIZATION_MASTER (org_name,registered_address,business_address,gst_number,location,created_by,created_date,state_id, factory_id)"
			+ " VALUES (:org_name,:registered_address,:business_address, :gst_number,:location,:created_by, GETDATE(), :state_id, :factory_id)", nativeQuery = true)
	int addOrganization(String org_name, String registered_address, String business_address, String gst_number,
			String location, String created_by, int state_id, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ORGANIZATION_MASTER (org_name,registered_address,business_address,gst_number,location,created_by,created_date,state_id, factory_id)"
			+ " VALUES (:org_name,:registered_address,:business_address, :gst_number,:location,:created_by, GETDATE(), :state_id, :factory_id)", nativeQuery = true)
	int addOrganizationNew(String org_name, String registered_address, String business_address, String gst_number,
			String location, String created_by, int state_id, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ORGANIZATION_MASTER SET org_name= :org_name, registered_address= :registered_address, business_address= :business_address,"
			+ "gst_number = :gst_number, location= :location, modified_by = :modified_by,modified_date = GETDATE(), state_id = :state_id WHERE org_id = :org_id", nativeQuery = true)
	int updateOrganization(String org_name, String registered_address, String business_address, String gst_number,
			String location, String modified_by, int state_id, String org_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ORGANIZATION_MASTER SET org_name= :org_name, registered_address= :registered_address, business_address= :business_address,"
			+ "gst_number = :gst_number, location= :location, modified_by = :modified_by,modified_date = GETDATE(), state_id = :state_id WHERE org_id = :org_id", nativeQuery = true)
	int updateOrganizationNew(String org_name, String registered_address, String business_address, String gst_number,
			String location, String modified_by, int state_id, String org_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ORGANIZATION_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE  org_id = :org_id", nativeQuery = true)
	int deleteOrganization(String modified_by, String org_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ORGANIZATION_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE  org_id = :org_id", nativeQuery = true)
	int deleteOrganizationNew(String modified_by, String org_id);

	@Query(value = "SELECT * FROM ORGANIZATION_MASTER WHERE is_delete = 0 and factory_id = :factory_id ", nativeQuery = true)
	List<OrganizationMasterInterface> getOrganizationList(String factory_id);

	@Query(value = "SELECT * FROM ORGANIZATION_MASTER WHERE is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id) "
			+ "order by org_id asc", nativeQuery = true)
	List<OrganizationMasterInterface> getOrganizationListNew(String factory_id);

	@Query(value = "SELECT * FROM ORGANIZATION_MASTER WHERE org_id = :org_id ", nativeQuery = true)
	OrganizationMasterInterface searchOrganizationById(String org_id);

	@Query(value = "SELECT * FROM ORGANIZATION_MASTER WHERE org_id = :org_id ", nativeQuery = true)
	OrganizationMasterInterface searchOrganizationByIdNew(String org_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ORGANIZATION_MASTER_HISTORY (org_id,org_name,registered_address,business_address,gst_number,location,created_by,created_date,modified_by,"
			+ "modified_date,action,transaction_date,deleted_date, deleted_by)  select org_id,org_name,registered_address,business_address,gst_number,location,created_by,created_date,modified_by,"
			+ " modified_date, 'DELETE', GETDATE(),GETDATE(),:modified_by FROM ORGANIZATION_MASTER where org_id = :org_id", nativeQuery = true)
	int deleteOrganizationHistory(String modified_by, String org_id);

	/* ORGANIZATION END */
	/* BANK START */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BANK_MASTER (business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date, factory_id) "
			+ "values (:business_unit_id,:bank_name,:account_number,:ifsc_code,:branch_address,:state_id,:country_id,:city,:swift_code,:branch_code,:created_by,GETDATE(),:factory_id)", nativeQuery = true)
	int addBankAccount(String business_unit_id, String bank_name, String account_number, String ifsc_code,
			String branch_address, String state_id, String country_id, String city, String swift_code,
			String branch_code, String created_by, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE BANK_MASTER SET business_unit_id = :business_unit_id, bank_name = :bank_name, account_number=:account_number,"
			+ "ifsc_code = :ifsc_code, branch_address =:branch_address,state_id=:state_id,country_id = :country_id, city =:city,"
			+ "swift_code = :swift_code,branch_code=:branch_code,modified_by=:modified_by, modified_date = GETDATE() WHERE account_id = :account_id", nativeQuery = true)
	int updateBankAccount(String business_unit_id, String bank_name, String account_number, String ifsc_code,
			String branch_address, String state_id, String country_id, String city, String swift_code,
			String branch_code, String modified_by, String account_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE BANK_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE  account_id = :account_id", nativeQuery = true)
	int deleteBankAccount(String modified_by, String account_id);

	@Query(value = "SELECT bm.*, s.state_name, c.country_name FROM BANK_MASTER bm JOIN STATE_MASTER s on bm.state_id = s.id "
			+ " join COUNTRY_MASTER as c on bm.country_id = c.id WHERE bm.is_delete = 0 AND (:factory_id IS NULL OR bm.factory_id = :factory_id)", nativeQuery = true)
	List<BankMasterInterface> getBankAccountList(String factory_id);

	@Query(value = "SELECT bm.*, s.state_name, c.country_name FROM BANK_MASTER bm JOIN STATE_MASTER s on bm.state_id = s.id join COUNTRY_MASTER as c on bm.country_id = c.id WHERE bm.account_id = :account_id ", nativeQuery = true)
	BankMasterInterface searchBankAccountById(String account_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BANK_HISTORY_MASTER (account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,modified_by,modified_date,action,transaction_date) select account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from BANK_MASTER where account_id = :account_id", nativeQuery = true)
	int InsterBankAccountHistory(String modified_by, String account_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BANK_HISTORY_MASTER (account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,modified_by,modified_date,action,transaction_date,deletd_by,deleted_date) select account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,modified_by,modified_date,'delete',GETDATE(),:modified_by,GETDATE() from BANK_MASTER where account_id = :account_id", nativeQuery = true)
	int updateBankAccountHistory(String modified_by, String account_id);

	/* BANK END */
	/* BUSINESS UINT START */
	@Transactional
	@Modifying
	@Query(value = "INSERT into business_units(org_id,business_unit_name,gst_number,location, state_id,created_by,created_date,bu_code, factory_id,pan_number)"
			+ "VALUES(:org_id,:business_unit_name,:gst_number,:location,:state_id,:created_by,GETDATE(),:value, :factory_id,:panNumber)", nativeQuery = true)
	int addBusinessUnits(String org_id, String business_unit_name, String gst_number, String location, int state_id,
			String created_by, int value, String factory_id,String panNumber);

	@Query(value = "select bu.*,sm.state_name from business_units bu inner join STATE_MASTER sm on sm.id = bu.state_id where (:factory_id IS NULL OR bu.factory_id = :factory_id) and bu.is_delete= 0 order by business_unit_id ASC", nativeQuery = true)
	List<BusinessUnitInterface> getAllBussinessUnits(String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE business_units  SET org_id=:org_id ,business_unit_name = :business_unit_name, gst_number = :gst_number, location = :location,state_id =:state_id, modified_by = :modified_by, modified_date= GETDATE(),pan_number=:panNumber WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int updatebusinessUnits(String org_id, String business_unit_name, String gst_number, String location, int state_id,
			String modified_by, String business_unit_id,String panNumber);

	@Modifying
	@Transactional
	@Query(value = "UPDATE business_units  SET org_id=:org_id ,business_unit_name = :business_unit_name, location = :location,state_id =:state_id, modified_by = :modified_by, modified_date= GETDATE(),pan_number:panNumber WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int updatebusinessUnitswithoutGstNumberColumn(String org_id, String business_unit_name, String location,
			int state_id, String modified_by, String business_unit_id,String panNumber);

	@Modifying
	@Transactional
	@Query(value = "UPDATE business_units  SET  modified_by=:modified_by, modified_date=GETDATE() , is_delete = 1 WHERE business_unit_id=:business_unit_id", nativeQuery = true)
	int deletebusinessUnits(String modified_by, String business_unit_id);

	@Query(value = "SELECT b.business_unit_id, b.business_unit_name,b.gst_number, b.location,b.org_id,b.state_id, b.factory_id,b.pan_number FROM business_units b  WHERE b.business_unit_id = :business_unit_id", nativeQuery = true)
	BusinessUnitInterface findBusinessUnit(String business_unit_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BUSINESS_UNITS_HISTORY (business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date, modified_by, modified_date, action, transaction_date) "
			+ "SELECT business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE() "
			+ "FROM business_units WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int insertRecordBusinessUnitHistory(String business_unit_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BUSINESS_UNITS_HISTORY (business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date,modified_by,modified_date, deleted_by, deleted_date, action, transaction_date) "
			+ "SELECT business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date, modified_by,  modified_date ,:modified_by, GETDATE(), 'DELETE', GETDATE() "
			+ "FROM business_units WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int insertRecordBusinessDeleteUnitHistory(String business_unit_id, String modified_by);

	@Query(value = "select Count(*) from BUSINESS_UNITS", nativeQuery = true)
	int getCount();

	@Query(value = "select count(*) from CONTRACT_MASTER where regd_office_id = :org_id and is_locked = 1", nativeQuery = true)
	int checkOrgIdPresentInContractMaster(String org_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where bid = :bid and is_locked = 1", nativeQuery = true)
	int checkBusinessIdPresentInContractMaster(String bid);

	@Query(value = "select count(*) from CONTRACT_MASTER where bank_details_id = :bank_details_id and is_locked = 1", nativeQuery = true)
	int checkBankIdPresentInContractMaster(String bank_details_id);

	@Query(value = "SELECT COUNT(*) FROM BANK_MASTER where account_number = :account_number and is_delete = 0", nativeQuery = true)
	int checkWhetherBankAccountExistsOrNot(String account_number);

	@Query(value = "SELECT count(*) from ORGANIZATION_MASTER where org_name = :org_name ", nativeQuery = true)
	int checkWhetherOrganizationExistsOrNot(String org_name);

	@Query(value = "select case\r\n"
			+ "when (SELECT COUNT(*) FROM SALE_ORDER_ENTRY where business_unit_id = :business_unit_id) > 0\r\n"
			+ "THEN 'The Transcation is already exists for Business Units'\r\n"
			+ "WHEN (SELECT COUNT(*) FROM CONTRACT_MASTER where bid = :business_unit_id) > 0\r\n"
			+ "THEN 'The Transcation is already exists for Business Units'\r\n"
			+ "WHEN (select count(*) from Others_invoice_master where Business_Unit_id = :business_unit_id) > 0\r\n"
			+ "THEN 'The Transcation is already exists for Business Units'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWhetherBusinessIsInTransactionsBeforeUpdate(String business_unit_id);

	@Query(value = "select case\r\n"
			+ "when (SELECT COUNT(*) FROM SALE_ORDER_ENTRY where business_unit_id = :business_unit_id) > 0\r\n"
			+ "THEN 'Delete failed, Business Unit is involed in SCRAP SALE ORDER transaction'\r\n"
			+ "WHEN (SELECT COUNT(*) FROM CONTRACT_MASTER where bid = :business_unit_id) > 0\r\n"
			+ "THEN 'Delete failed, Business Unit is involed in CONTRACT MASTER'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWhetherBusinessIsInTransactionsBeforeDelete(String business_unit_id);

	@Query(value = "select case\r\n"
			+ "WHEN (SELECT COUNT(*) FROM CONTRACT_MASTER where regd_office_id = :org_id) > 0\r\n"
			+ "THEN 'The Transcation is Exists for Organization'\r\n"
			+ "WHEN ( select count(*) from Others_invoice_master where registered_office_id = :org_id) > 0\r\n"
			+ "THEN 'The Transcation is Exists for Organization'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWhetherOrganizationIsinTransactions(String org_id);

	@Query(value = "SELECT factory_id from BUSINESS_UNITS where business_unit_id = :business_unit_id", nativeQuery = true)
	String getFactory_id(String business_unit_id);

	@Query(value = "select case\r\n"
			+ "WHEN (SELECT COUNT(*) FROM SCRAP_INVOICE_MASTER where bank_id  = :account_id) > 0\r\n"
			+ "THEN 'The Transcation is Already Exists for Bank'\r\n"
			+ "WHEN (SELECT COUNT(*) FROM CONTRACT_MASTER where bank_details_id  = :account_id) > 0\r\n"
			+ "THEN 'The Transcation is Already Exists for Bank'\r\n"
			+ "WHEN (select count(*) from Others_invoice_master where bank_id = :account_id) > 0\r\n"
			+ "THEN 'The Transcation is Already Exists for Bank'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWhetherBankAccountInvovedInTransactions(String account_id);

	@Query(value = "SELECT count(*) from BUSINESS_UNITS where gst_number = :gst_number", nativeQuery = true)
	int checkWhetherBusinessUnitExists(String gst_number);
	
	@Query(value = """
		    SELECT COUNT(*) 
		    FROM BUSINESS_UNITS 
		    WHERE gst_number = :gst
		    AND business_unit_id <> :businessUnitId
		""", nativeQuery = true)
		int checkWhetherBusinessUnitExistsForUpdate(
		        String gst,
		         String businessUnitId
		);

}
