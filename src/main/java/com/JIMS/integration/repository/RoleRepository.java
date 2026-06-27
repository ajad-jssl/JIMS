package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.AllRolesInterface;

import jakarta.transaction.Transactional;

public interface RoleRepository extends JpaRepository<User, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from ROLE_MASTER where role_id = :role_id", nativeQuery = true)
	int deleteRole(String role_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ROLE_MASTER set is_delete = 1 where id in (:role_id)", nativeQuery = true)
	int deleteRoleInBulk(List<String> role_id);

	@Query(value = "SELECT * from ROLE_MASTER", nativeQuery = true)
	List<AllRolesInterface> getAllRoles();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ROLE_MASTER_HISTORY (role_id,role_name, role_code,created_by,created_date,modified_by, modified_date, transaction_date, action)\r\n"
			+ "SELECT role_id,role_name, role_code, created_by,created_date,:user_id,GETDATE(),GETDATE(), 'UPDATE' FROM ROLE_MASTER WHERE role_id = :role_id;", nativeQuery = true)
	int moveRoleToHistoryTable(String role_id, String user_id);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ROLE_MASTER_HISTORY (role_id,role_name, role_code, created_by,created_date,modified_by, modified_date, transaction_date, action)\r\n"
			+ "SELECT role_id,role_name, role_code,created_by,created_date,:user_id,GETDATE(),GETDATE(), 'DELETE' FROM ROLE_MASTER WHERE role_id = :role_id;", nativeQuery = true)
	int deleteRoleToHistoryTable(String role_id, String user_id);


	@Transactional
	@Modifying
	@Query(value = "INSERT INTO role_master (role_name, role_code, created_by,created_date) VALUES (:role_name, :role_code, :created_by, GETDATE())", nativeQuery = true)
	int addRole(
	    @Param("role_name") String role_name,
	    @Param("role_code") String role_code,
	    @Param("created_by") String created_by
	);
	
	

	@Transactional
	@Modifying
	@Query(value = "update ROLE_MASTER SET role_name = :role_name, role_code=:role_code,modified_by=:modified_by,modified_date = GETDATE() where role_id = :role_id", nativeQuery = true)
	int updateRole(String role_name, String role_code, String modified_by, String role_id);

	@Query(value="select * from MODULE_MASTER where is_delete = 0", nativeQuery = true)
	List<AllRolesInterface> getAllModules();

	@Query(value = "select am.id as action_id, am.action_desc from Action_Master am", nativeQuery = true)
	List<AllRolesInterface> getAllActions();

}
