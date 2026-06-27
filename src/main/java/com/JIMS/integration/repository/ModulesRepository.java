package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.integration.entity.Modules;
import com.JIMS.integration.interfaces.ModuleSubModulesList;
import com.JIMS.integration.interfaces.RoleAssignModule_SubModule_Actions;

import jakarta.transaction.Transactional;

public interface ModulesRepository extends JpaRepository<Modules, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SUBMODULE_MASTER (submodule_name,module_id,created_by,created_date,add_action,update_action,delete_action,view_action,verify_action,release_action,print_action) VALUES (:submodule_name, :moduleId, :createdBy, GETDATE(),:add, :update,:delete,:view, :verify, :release, :print)", nativeQuery = true)
	int createSubModules(String submodule_name, int moduleId, String createdBy, String add, String update,
			String delete, String view, String verify, String release, String print);

	@Query(value = "select mm.module_name, mm.module_id, sm.submodule_name, sm.id as submodule_id,sm.add_action,sm.update_action,sm.view_action,sm.delete_action,sm.verify_action,sm.release_action,sm.print_action, "
			+ "CONCAT(  COALESCE(sm.add_action, ''), ', ',  COALESCE(sm.update_action, ''), ', ', COALESCE(sm.view_action, ''), ', ', COALESCE(sm.delete_action, ''), ', ', COALESCE(sm.verify_action, ''), ', ', COALESCE(sm.release_action, ''), ', ', COALESCE(sm.print_action, '') ) AS actions_value"
			+ " from MODULE_MASTER mm inner join SUBMODULE_MASTER sm on sm.module_id = mm.module_id", nativeQuery = true)
	List<ModuleSubModulesList> getAllModulesAndSubModules();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ROLEASSIGN_MASTER (role_id,module_id,submodule_id,action_create,action_update,action_delete,action_read,action_verify,action_release,action_reject,action_print,created_by,created_date,all_actions)"
			+ "VALUES (:role_id,:module_id,:submodule_id,:action_create,:action_update,:action_delete,:action_read,:action_verify,:action_release,:action_reject,:action_print,:created_by,GETDATE(),:sb)", nativeQuery = true)
	int AssignModulesForRoles(String role_id, String module_id, String submodule_id, String action_create,
			String action_update, String action_delete, String action_read, String action_verify, String action_release,
			String action_reject, String action_print, String created_by,String sb);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ROLEASSIGN_MASTER SET role_id =:role_id,module_id = :module_id,submodule_id = :submodule_id,action_create = :action_create,action_update = :action_update,action_delete = :action_delete,action_read = :action_read,action_verify = :action_verify,action_release  = :action_release ,action_reject = :action_reject,modified_by = :modified_by,modified_date =  GETDATE() where id = :id", nativeQuery = true)
	int updateModulesForRoles(String role_id, String module_id, String submodule_id, String action_create,
			String action_update, String action_delete, String action_read, String action_verify, String action_release,
			String action_reject, String modified_by, String id);

	@Query(value = "select RM.role_name,MM.module_name,SM.submodule_name,RAM.id,RAM.module_id,RAM.submodule_id,  \r\n"
			+ "CONCAT(  COALESCE(RAM.action_create, ''), ', ',  COALESCE(RAM.action_update, ''), ', ', COALESCE(RAM.action_read, ''), ', ', COALESCE(RAM.action_delete, ''), ', ', COALESCE(RAM.action_verify, ''), ', ', COALESCE(RAM.action_release, ''), ', ', COALESCE(RAM.action_print, '') ) AS actions_value  from ROLEASSIGN_MASTER RAM\r\n"
			+ "INNER JOIN ROLE_MASTER RM ON RM.role_id = RAM.role_id\r\n"
			+ "INNER JOIN MODULE_MASTER MM ON MM.module_id = RAM.module_id\r\n"
			+ "INNER JOIN SUBMODULE_MASTER SM ON SM.id = RAM.submodule_id \r\n"
			+ "WHERE RAM.is_delete = 0", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> getListModuleandSubModuleActions();

	@Query(value = "select distinct rm.role_name, mm.module_name, sm.submodule_name,ram.* from ROLEASSIGN_MASTER ram\r\n"
			+ "inner join ROLE_MASTER rm on rm.role_id = ram.role_id\r\n"
			+ "inner join MODULE_MASTER mm on mm.module_id = ram.module_id\r\n"
			+ "inner join SUBMODULE_MASTER sm on sm.module_id = mm.module_id where ram.id = :id", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> serachModuleSubModulesActionsListById(String id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SUBMODULE_MASTER SET submodule_name = :submodule_name,module_id = :module_id, modified_by =:modified_by, modified_date = GETDATE() where id =:id  ", nativeQuery = true)
	int updateSubModules(String submodule_name, int module_id, String modified_by, int id);

	@Query(value = "select mm.module_name,mm.module_id,sm.submodule_name from MODULE_MASTER mm inner join SUBMODULE_MASTER sm on sm.module_id = mm.module_id where mm.module_id = :module_id", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> serachModuleSubModules(String module_id);

	@Query(value = "select sm.submodule_name,sm.id as submodule_id,CONCAT('-', sm.id) AS sumodule_I_FANID, CONCAT(  COALESCE(sm.add_action, ''), ', ',  COALESCE(sm.update_action, ''), ', ', COALESCE(sm.view_action, ''), ', ', COALESCE(sm.delete_action, ''), ', ', COALESCE(sm.verify_action, ''), ', ', COALESCE(sm.release_action, ''), ', ', COALESCE(sm.print_action, '') ) AS actions_value  from SUBMODULE_MASTER sm where sm.module_id IN (:id)", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> getSubModuleassignedToModule(List<String> id);

	/*
	 * @Query(value =
	 * "select sm.submodule_name,sm.id as submodule_id,sm.module_id, mm.module_name, CONCAT(  COALESCE(sm.add_action, ''), ', ',  \r\n"
	 * +
	 * "COALESCE(sm.update_action, ''), ', ', COALESCE(sm.view_action, ''), ', ', \r\n"
	 * +
	 * "COALESCE(sm.delete_action, ''), ', ', COALESCE(sm.verify_action, ''), ', ', \r\n"
	 * +
	 * "COALESCE(sm.release_action, ''), ', ', COALESCE(sm.print_action, '') ) AS actions_value  \r\n"
	 * + "from SUBMODULE_MASTER sm \r\n" +
	 * "inner join MODULE_MASTER mm on mm.module_id = sm.module_id\r\n" +
	 * "where sm.module_id IN (:module_id) and sm.id IN (:submodule_id)",
	 * nativeQuery = true) List<RoleAssignModule_SubModule_Actions>
	 * getSubModuleActions(List<String> module_id, List<String> submodule_id);
	 */
	
	@Query(value = "SELECT\r\n"
			+ "    sm.submodule_name,\r\n"
			+ "    sm.id AS submodule_id,\r\n"
			+ "    sm.module_id,\r\n"
			+ "    mm.module_name,\r\n"
			+ "    CONCAT('-', sm.id) AS sumodule_I_FANID,\r\n"
			+ "    \r\n"
			+ "    -- Combining all actions with a comma (including empty strings)\r\n"
			+ "    CONCAT(\r\n"
			+ "        COALESCE(sm.add_action, ''), ', ',\r\n"
			+ "        COALESCE(sm.update_action, ''), ', ',\r\n"
			+ "        COALESCE(sm.view_action, ''), ', ',\r\n"
			+ "        COALESCE(sm.delete_action, ''), ', ',\r\n"
			+ "        COALESCE(sm.verify_action, ''), ', ',\r\n"
			+ "        COALESCE(sm.release_action, ''), ', ',\r\n"
			+ "        COALESCE(sm.print_action, '')\r\n"
			+ "    ) AS actions_value,\r\n"
			+ "\r\n"
			+ "    -- Remove empty strings and construct actions list properly\r\n"
			+ "    STUFF(\r\n"
			+ "        (SELECT ', ' + action_value\r\n"
			+ "         FROM (VALUES\r\n"
			+ "            (NULLIF(sm.add_action, '')),\r\n"
			+ "            (NULLIF(sm.update_action, '')),\r\n"
			+ "            (NULLIF(sm.view_action, '')),\r\n"
			+ "            (NULLIF(sm.delete_action, '')),\r\n"
			+ "            (NULLIF(sm.verify_action, '')),\r\n"
			+ "            (NULLIF(sm.release_action, '')),\r\n"
			+ "            (NULLIF(sm.print_action, ''))\r\n"
			+ "         ) AS ActionList(action_value)\r\n"
			+ "         WHERE action_value IS NOT NULL\r\n"
			+ "         FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), \r\n"
			+ "        1, 2, ''  -- Removes the leading ', '\r\n"
			+ "    ) AS actions_value_without_EmptyString,\r\n"
			+ "\r\n"
			+ "    -- New column: action_values prefixed with '-'\r\n"
			+ "    STUFF(\r\n"
			+ "        (SELECT ' -' + action_value  -- Prefix each action with \"-\"\r\n"
			+ "         FROM (VALUES\r\n"
			+ "            (NULLIF(sm.add_action, '')),\r\n"
			+ "            (NULLIF(sm.update_action, '')),\r\n"
			+ "            (NULLIF(sm.view_action, '')),\r\n"
			+ "            (NULLIF(sm.delete_action, '')),\r\n"
			+ "            (NULLIF(sm.verify_action, '')),\r\n"
			+ "            (NULLIF(sm.release_action, '')),\r\n"
			+ "            (NULLIF(sm.print_action, ''))\r\n"
			+ "         ) AS ActionList(action_value)\r\n"
			+ "         WHERE action_value IS NOT NULL\r\n"
			+ "         FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), \r\n"
			+ "        1, 2, ''  -- Removes the leading ', '\r\n"
			+ "    ) AS action_values_adding_Ifan  \r\n"
			+ "\r\n"
			+ "FROM\r\n"
			+ "    SUBMODULE_MASTER sm   \r\n"
			+ "INNER JOIN\r\n"
			+ "    MODULE_MASTER mm \r\n"
			+ "    ON mm.module_id = sm.module_id  where sm.module_id IN (:module_id) and sm.id IN (:submodule_id)", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> getSubModuleActions(List<String> module_id, List<String> submodule_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SUBMODULE_MASTER ( module_id, submodule_name, action_id, created_by, created_date) VALUES (:module_id,:submodule_name, :action_id, :created_by, GETDATE())", nativeQuery = true)
	int addSubModules(int module_id, String submodule_name, String action_id, String created_by);

	@Query(value="select module_id from SUBMODULE_MASTER where id = :al", nativeQuery = true)
	int getModuleId(String al);

}
