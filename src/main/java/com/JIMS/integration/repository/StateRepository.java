package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.StateInterface;

import jakarta.transaction.Transactional;

@Repository
public interface StateRepository extends JpaRepository<User, Integer> {

	@Modifying
	@Transactional
	@Query(value = "INSERT into STATE_MASTER(state_code,state_name,state_id,created_by,created_date, factory_id)"
			+ "VALUES(:state_code,:state_name,:state_id,:created_by,GETDATE(), :factory_id)", nativeQuery = true)
	int addStateDetails(String state_code, String state_name, String state_id, String created_by, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT into STATE_MASTER(state_code,state_name,state_id,created_by,created_date, factory_id,country_id)"
			+ "VALUES(:state_code,:state_name,:state_id,:created_by,GETDATE(), :factory_id,:country_id)", nativeQuery = true)
	int addStateDetailsNew(String state_code, String state_name, String state_id, String created_by, String factory_id,
			String country_id);

	@Query(value = "SELECT id, state_code, state_name, state_id, factory_id, country_id FROM STATE_MASTER WHERE is_delete = 0 AND (:factory_id IS NULL OR factory_id = :factory_id)", nativeQuery = true)
	List<StateInterface> getStates(String factory_id);

	@Query(value = "SELECT id,state_code,state_name,state_id, factory_id, country_id FROM STATE_MASTER WHERE is_delete=0  AND id=:id", nativeQuery = true)
	StateInterface getStateById(String id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE STATE_MASTER SET is_delete=1 , modified_by = :user_id, modified_date = GETDATE() WHERE id=:state_id", nativeQuery = true)
	int deleteState(@Param("user_id") String user_id, @Param("state_id") String state_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE STATE_MASTER SET state_code = :state_code, state_name=:state_name, modified_by=:modified_by, state_id = :state_id, modified_date= GETDATE() WHERE id=:id", nativeQuery = true)
	int UpdateState(@Param("state_code") String state_code, @Param("state_name") String name,
			@Param("modified_by") String modified_by, @Param("state_id") String state_id, @Param("id") String id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO STATE_MASTER_HISTORY (state_id, state_code, state_name, deleted_by,\r\n"
			+ "deleted_date, transaction_date, action)\r\n"
			+ "SELECT id, state_code, state_name, :modified_by, GETDATE(),GETDATE(), 'UPDATE'  \r\n"
			+ "FROM STATE_MASTER\r\n" + "WHERE id = :state_id", nativeQuery = true)
	int insertToHistoryTable(String state_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO STATE_MASTER_HISTORY (state_id, state_code, state_name, deleted_by,\r\n"
			+ "deleted_date, transaction_date, action)\r\n"
			+ "SELECT id, state_code, state_name, :parsedUserId, GETDATE(),GETDATE(), 'DELETE'  \r\n"
			+ "FROM STATE_MASTER\r\n" + "WHERE id = :parsedStateId", nativeQuery = true)
	int moveToHistoryBeforeDelete(int parsedUserId, int parsedStateId);

	@Query(value = "SELECT id, state_code, state_name, state_id, factory_id, country_id from STATE_MASTER where country_id = :country_id and is_delete = 0 ", nativeQuery = true)
	List<StateInterface> getStateByCountry(String country_id);

	@Query(value = "select count(id) from STATE_MASTER where factory_id = :factory_id", nativeQuery = true)
	int getStateByCount(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE STATE_MASTER SET state_code = :state_code,state_name=:state_name, state_id = :state_id, modified_by=:modified_by,  modified_date= GETDATE(), country_id = :country_id WHERE id=:id", nativeQuery = true)
	int UpdateStateNew(String state_code, String state_name, String state_id, String modified_by, String country_id,
			String id);

//	@Query(value = "SELECT COUNT(*) from STATE_MASTER where state_name = :state_name or state_code = :state_code or state_id :=state_id", nativeQuery = true)
//	int checkWhetherStateExistsOrNot(String state_name, String state_code, String state_id);
	
	
	@Query(
		    value = """
		        SELECT COUNT(*) 
		        FROM STATE_MASTER 
		        WHERE state_name = :state_name
		           OR state_code = :state_code
		           OR state_id = :state_id
		    """,
		    nativeQuery = true
		)
		int checkWhetherStateExistsOrNot(
		        @Param("state_name") String stateName,
		        @Param("state_code") String stateCode,
		        @Param("state_id") String stateId
		);

	
	@Query(value = "SELECT COUNT(*) from STATE_MASTER where state_name = :state_name",nativeQuery = true)
	int checkWhertherStateNameExistOrNot(@Param("state_name") String stateName);
	
	@Query(value = "SELECT COUNT(*) from STATE_MASTER where state_code = :state_code",nativeQuery = true)
	int checkWhertherStateCodeExistOrNot(@Param("state_code") String stateCode);
	
	
	@Query(value = "SELECT COUNT(*) from STATE_MASTER where state_id = :state_id",nativeQuery = true)
	int checkWhertherStateIdExistOrNot(@Param("state_id") String stateId);
	
	
	
	
	

	@Query(value = "SELECT COUNT(*) AS state_id_count\r\n" + "FROM BUSINESS_UNITS bu\r\n"
			+ "WHERE bu.state_id =:state_id\r\n"
			+ "AND EXISTS (SELECT 1 FROM SALE_ORDER_ENTRY soe WHERE soe.business_unit_id = bu.business_unit_id)", nativeQuery = true)
	int checkWhetherStateInvolvedInTransForDelete(String state_id);

	@Query(value = "select case\r\n" + //
				"WHEN (SELECT COUNT(*) FROM INVOICE_CONSIGNEE_ADDRESS_MASTER where state_id = :id) > 0 \r\n" + //
				"THEN 'The Transcation is already exists for state'\r\n" + //
				"WHEN (SELECT COUNT(*) FROM BUSINESS_UNITS where state_id = :id) > 0\r\n" + //
				"THEN 'The Transcation is already exists for state'\r\n" + //
				"WHEN (SELECT COUNT(*) FROM ORGANIZATION_MASTER where state_id = :id) > 0\r\n" + //
				"THEN 'The Transcation is already exists for state'\r\n" + //
			
			
				"END as message", nativeQuery = true)
	String checkWhetherStateInvolvedInAnyTransactions(String id);

	@Query(value = "select case\r\n"
			+ "WHEN (SELECT COUNT(*) FROM INVOICE_CONSIGNEE_ADDRESS_MASTER where state_id = :state_id) > 0 THEN\r\n"
			+ "'Update failed, State is invoved in Invoice/Consignee Address'\r\n"
			+ "WHEN (SELECT COUNT(*) FROM BUSINESS_UNITS where state_id = :state_id) > 0\r\n"
			+ "THEN 'Update failed, State is invoved in Business Master'\r\n"
			+ "WHEN (SELECT COUNT(*) FROM ORGANIZATION_MASTER where state_id = :state_id) > 0\r\n"
			+ "THEN 'Update failed, State is invoved in Organization Master'\r\n"
			+ "END as message", nativeQuery = true)
	String checkWhetherStateInvolvedInAnyTransactionsBeforeDelete(String state_id);
}
