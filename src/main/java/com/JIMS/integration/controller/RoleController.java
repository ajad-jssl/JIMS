package com.JIMS.integration.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.Modules;
import com.JIMS.integration.interfaces.AllRolesInterface;
import com.JIMS.integration.interfaces.ModuleSubModulesList;
import com.JIMS.integration.interfaces.RoleAssignModule_SubModule_Actions;
import com.JIMS.integration.repository.ModulesRepository;
import com.JIMS.integration.repository.RoleRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class RoleController {
	Logger logger = LogManager.getLogger(RoleController.class);
	@Autowired
	private RoleRepository rolerepo;

	@Autowired
	private ModulesRepository modulesRepo;

	@PostMapping("/role/addRole")
	public @ResponseBody Map<String, Object> addRole(@RequestParam String role_code, @RequestParam String role_name,
			@RequestParam String created_by) {
		Map<String, Object> responseMap = new HashMap<>();
		logger.info("EXECUTING METHOD :: addRole");
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING ROLE");
			int addRolerecord = rolerepo.addRole(role_code.toUpperCase(), role_name.toUpperCase(),
					created_by.toUpperCase());
			logger.info("EXECUTING METHOD :: AFTER ADDING ROLE");
			responseMap.put("message", (addRolerecord > 0) ? "Success" : "Role Not Added");
			responseMap.put("status", (addRolerecord > 0) ? "yes" : "no");
			responseMap.put("action", "AddRole");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR addRole ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: addRole");
		return responseMap;
	}

	@PostMapping("/role/updateRole")
	public @ResponseBody Map<String, Object> updateRole(@RequestParam String role_code, @RequestParam String role_id,
			@RequestParam String role_name, @RequestParam String modified_by) {
		Map<String, Object> responseMap = new HashMap<>();
		logger.info("EXECUTING METHOD :: updateRole");
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATE ROLE");
			//rolerepo.moveRoleToHistoryTable(role_id, modified_by);
			int updateRolerecord = rolerepo.updateRole(role_name.toUpperCase(), role_code.toUpperCase(),
					modified_by.toUpperCase(), role_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATE ROLE");
			responseMap.put("message", (updateRolerecord > 0) ? "Success" : "Role Not Updated");
			responseMap.put("status", (updateRolerecord > 0) ? "yes" : "no");
			responseMap.put("action", "UpdateRole");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateRole ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateRole");
		return responseMap;
	}

	@DeleteMapping("/role/deleteRole")
	public @ResponseBody Map<String, Object> deleteRole(@RequestParam String role_id, @RequestParam String user_id) {
		Map<String, Object> deleteRolemap = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteRole");
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATE ROLE");
			//rolerepo.deleteRoleToHistoryTable(role_id, user_id);
			int deleteRolerecord = rolerepo.deleteRole(role_id);
			logger.info("EXECUTING METHOD :: BEFORE UPDATE ROLE");
			deleteRolemap.put("message", (deleteRolerecord > 0) ? "Success" : "Role Not Deleted");
			deleteRolemap.put("status", (deleteRolerecord > 0) ? "yes" : "no");
			deleteRolemap.put("action", "DeleteRole");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteRole ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteRole");
		return deleteRolemap;
	}

	@DeleteMapping("/role/deleteRoleInBulk")
	public @ResponseBody Map<String, Object> deleteRoleInBulk(@RequestParam List<String> role_id) {
		Map<String, Object> deleteRoleInBulkmap = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteRoleInBulk");
		try {
			int deleteRoleInBulkrecord = rolerepo.deleteRoleInBulk(role_id);
			deleteRoleInBulkmap.put("message", (deleteRoleInBulkrecord > 0) ? "Success" : "Role Not Deleted");
			deleteRoleInBulkmap.put("status", (deleteRoleInBulkrecord > 0) ? "yes" : "no");
			deleteRoleInBulkmap.put("action", "DeleteRole");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteRoleInBulk ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteRoleInBulk");
		return deleteRoleInBulkmap;
	}

	@GetMapping("/role/getAllRoles")
	public @ResponseBody Map<String, Object> getAllRoles() {
		Map<String, Object> allRolesmap = new HashMap<String, Object>();
		List<AllRolesInterface> allroleslist = null;
		logger.info("EXECUTING METHOD :: getAllRoles");
		try {
			allroleslist = rolerepo.getAllRoles();
			allRolesmap.put("message", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "Success" : "Failure");
			allRolesmap.put("status", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "yes" : "no");
			allRolesmap.put("action", "GetAllRoles");
			if ((allroleslist != null) && (!allroleslist.isEmpty())) {
				allRolesmap.put("DistrictDetails", allroleslist);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getAllRoles ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getAllRoles");
		return allRolesmap;
	}

	@GetMapping("/listactions")
	public @ResponseBody Map<String, Object> getListActions() {
		Map<String, Object> allRolesmap = new HashMap<String, Object>();
		List<AllRolesInterface> allroleslist = null;
		logger.info("EXECUTING METHOD :: getListActions");
		try {
			allroleslist = rolerepo.getAllActions();
			allRolesmap.put("message", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "Success" : "Failure");
			allRolesmap.put("status", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "yes" : "no");
			allRolesmap.put("action", "Action List");
			if ((allroleslist != null) && (!allroleslist.isEmpty())) {
				allRolesmap.put("DistrictDetails", allroleslist);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD FOR getListActions ::   -> " + e.getMessage());
			e.printStackTrace();
		}
		logger.info("EXECUTED METHOD :: getListActions");
		return allRolesmap;
	}

	@PostMapping("/addModule")
	public @ResponseBody Map<String, Object> addModule(@RequestParam String moduleName,
			@RequestParam String created_by) {
		logger.info("EXECUTING METHOD :: addModule");
		Map<String, Object> createModule = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD ::BEFORE ADDING  addModule");
			Modules module = new Modules();
			module.setModuleName(moduleName.toUpperCase());
			module.setCreatedBy(created_by.toUpperCase());
			Modules value = modulesRepo.save(module);
			logger.info("EXECUTING METHOD :: AFTER ADDING addModule");
			createModule.put("action", "Create_Module");
			createModule.put("message", value != null ? "Success" : "Failure");
			createModule.put("status", value != null ? "YES" : "NO");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR addModule ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: addModule");
		return createModule;
	}

	@GetMapping("/module/getAllmodules")
	public @ResponseBody Map<String, Object> getAllModules() {
		Map<String, Object> allRolesmap = new HashMap<String, Object>();
		List<AllRolesInterface> allroleslist = null;
		logger.info("EXECUTING METHOD :: getAllModules");
		try {
			allroleslist = rolerepo.getAllModules();
			allRolesmap.put("message", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "Success" : "");
			allRolesmap.put("status", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "yes" : "no");
			allRolesmap.put("action", "module_list");
			if ((allroleslist != null) && (!allroleslist.isEmpty())) {
				allRolesmap.put("DistrictDetails", allroleslist);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getAllModules ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getAllModules");
		return allRolesmap;
	}

	@PostMapping("/subModule")
	public @ResponseBody Map<String, Object> addsubModule(@RequestParam int module_id, @RequestParam String subModule,
			@RequestParam List<String> actions, @RequestParam String createdBy) {
		logger.info("EXECUTING METHOD :: addsubModule");
		int count = 0;
		String add = "", update = "", delete = "", view = "", verify = "", release = "", print = "";
		Map<String, Object> createModule = new HashMap<String, Object>();
		try {
			for (String actionValue : actions) {
				if (actionValue.equalsIgnoreCase("add")) {
					add = "add";
				} else if (actionValue.equalsIgnoreCase("update")) {
					update = "update";
				} else if (actionValue.equalsIgnoreCase("delete")) {
					delete = "delete";
				} else if (actionValue.equalsIgnoreCase("view")) {
					view = "view";
				} else if (actionValue.equalsIgnoreCase("verify")) {
					verify = "verify";
				} else if (actionValue.equalsIgnoreCase("Release")) {
					release = "release";
				} else if (actionValue.equalsIgnoreCase("print")) {
					print = "print";
				}
			}
			logger.info("EXECUTING METHOD :: BEFORE ADDING createSubModules");
			count = modulesRepo.createSubModules(subModule.toUpperCase(), module_id, createdBy.toUpperCase(),
					add.toUpperCase(), update.toUpperCase(), delete.toUpperCase(), view.toUpperCase(),
					verify.toUpperCase(), release.toUpperCase(), print.toUpperCase());
			logger.info("EXECUTING METHOD :: AFTER ADDING createSubModules");
			createModule.put("action", "Create_Sub_Module");
			createModule.put("message", count > 0 ? "Success" : "Failure");
			createModule.put("status", count > 0 ? "YES" : "NO");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD FOR addsubModule ::   -> " + e.getMessage());
			e.printStackTrace();
		}
		logger.info("EXECUTED METHOD :: addsubModule");
		return createModule;
	}

	/*
	 * for update clarification is required 1. only we are going to add or remove
	 * the submodules 2. if we want to add submodule for that module is fine 3. if
	 * we want to remove that if it's assigned to any other roles that time? 4. We
	 * want to give is_delete option bcz once we assign to that role's number of
	 * users how?
	 */
//	@PostMapping("/updateModule")
//	public @ResponseBody Map<String, Object> updateModule(@RequestParam String moduleName, @RequestParam List<Map<String,String>> subModule, @RequestParam String modifiedBy,@RequestParam int id) {
//		int count =0;
//		Map<String, Object> createModule = new HashMap<String, Object>();
//		try {
//			Modules module = new Modules();
//			module.setModuleName(moduleName);
//			module.setModifiedBy(modifiedBy);
//			module.setModifiedDate(LocalDateTime.now());
//			module.setId(id);
//			Modules value =  modulesRepo.save(module);
//			int moduleId = value.getId();
//			for(Map<String,String> submodule_name : subModule) {
//				String name = submodule_name.getOrDefault("submodule_name", "NO");
//				String submodule_id = submodule_name.getOrDefault("submodule_id", "NO");
//				String remove = submodule_name.getOrDefault("removed", "NO");
//				if(submodule_id.equalsIgnoreCase("NO")) {
//					modulesRepo.createSubModules(name,moduleId, modifiedBy);
//				}
//				if(remove.equalsIgnoreCase("YES")) {
//					
//				}
////				else {
////					int ids = Integer.valueOf(submodule_id); 
////					count  = modulesRepo.updateSubModules(name,moduleId, modifiedBy,ids);
////				}
//				
//			}
//			createModule.put("action", "Update_Module");
//			createModule.put("message", count > 0 ? "Success" : "Failure");
//			createModule.put("message", count > 0 ? "YES" : "NO");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return  createModule;
//	}
	@GetMapping("/modules/getAllmodulesandsubmodules")
	public @ResponseBody Map<String, Object> getAllModulesandSubmodules() {
		logger.info("EXECUTING METHOD :: getAllModulesandSubmodules");
		Map<String, Object> allRolesmap = new HashMap<String, Object>();
		List<ModuleSubModulesList> allroleslist = null;
		try {
			allroleslist = modulesRepo.getAllModulesAndSubModules();
			allRolesmap.put("message", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "Success" : "Failure");
			allRolesmap.put("status", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "yes" : "no");
			allRolesmap.put("action", "ModuleList");
			if ((allroleslist != null) && (!allroleslist.isEmpty())) {
				allRolesmap.put("DistrictDetails", allroleslist);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getAllModulesandSubmodules ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getAllModulesandSubmodules");
		return allRolesmap;
	}

	@GetMapping("/modules/search")
	public @ResponseBody Map<String, Object> userSearch(@RequestParam String id) {
		logger.info("EXECUTING METHOD :: userSearch");
		Map<String, Object> response = new HashMap<String, Object>();
		List<RoleAssignModule_SubModule_Actions> userInterface = null;
		try {
			userInterface = modulesRepo.serachModuleSubModules(id);
			response.put("Data", userInterface);
			response.put("message", (userInterface != null) ? "Search Module & Submodule List Success"
					: "Search Module & Submodule List Failed");
			response.put("status", (userInterface != null) ? "yes" : "no");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR userSearch ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: userSearch");
		return response;
	}

	@GetMapping("/getsubmodulesassignedtomodule")
	public @ResponseBody Map<String, Object> usergetSubMoModule(@RequestParam List<String> id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: usergetSubMoModule");
		List<RoleAssignModule_SubModule_Actions> userInterface = null;
		try {
			userInterface = modulesRepo.getSubModuleassignedToModule(id);
			response.put("DataModule", userInterface);
			response.put("message", (userInterface != null) ? "Get SubModules Asscoiated with module"
					: "Search Module & Submodule List Failed");
			response.put("status", (userInterface != null) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR usergetSubMoModule ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: usergetSubMoModule");
		return response;
	}

	@GetMapping("/getactionsbasedonmodule_idandsubmodule_id")
	public @ResponseBody Map<String, Object> usergetActionsSubMoModule(@RequestParam List<String> module_id,
			@RequestParam List<String> submodule_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: usergetActionsSubMoModule");
		List<RoleAssignModule_SubModule_Actions> userInterface = null;
		try {
			userInterface = modulesRepo.getSubModuleActions(module_id, submodule_id);
			response.put("DataActions", userInterface);
			response.put("message", (userInterface != null) ? "Get Actions SubModules Asscoiated with module"
					: "Search Module & Submodule List Failed");
			response.put("status", (userInterface != null) ? "yes" : "no");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR usergetActionsSubMoModule ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: usergetActionsSubMoModule");
		return response;
	}

	

	@SuppressWarnings("unused")
	@PostMapping("/assignModuleToRole/add")
	public @ResponseBody Map<String, Object> assignModulesToRole(@RequestParam String role_id,
			@RequestParam List<String> module_id, @RequestParam String submodule_id, 
			@RequestParam String actions,@RequestParam String created_by) {
		logger.info("EXECUTING METHOD :: assignModulesToRole");
		Map<String, Object> response = new HashMap<>();
		int count = 0;
		String add = "", update = "", delete = "", view = "", verify = "", release = "", print = "", reject = "";
		List<List<String>> submoduleList = Arrays.stream(submodule_id.split(","))
				.map(sub -> Arrays.asList(sub.split("-"))).collect(Collectors.toList());
		List<List<String>> actionsList = Arrays.stream(actions.split(",")).map(act -> Arrays.asList(act.split("-")))
				.collect(Collectors.toList());
		try {
			int loopingValue = 0;
			logger.info("EXECUTING METHOD :: BEFORE ADDING assignModulesToRole");
			for (int i = 0; i < module_id.size(); i++) {
				String module = module_id.get(i);
				for (int j = i; j <= i; j++) {
					List<String> submodules = submoduleList.get(j);
					for (String submodule : submodules) {
						for (int k = loopingValue; k < actionsList.size(); loopingValue++) {
							List<String> actionsForModule = actionsList.get(k);
							StringBuilder sb = new StringBuilder();
							for (String action : actionsForModule) {
								sb = sb.append(action);
								switch (action.toLowerCase()) {
								case "add":
									add = "ADD";
									break;
								case "update":
									update = "UPDATE";
									break;
								case "delete":
									delete = "DELETE";
									break;
								case "view":
									view = "VIEW";
									break;
								case "verify":
									verify = "VERIFY";
									break;
								case "release":
									release = "RELEASE";
									break;
								case "print":
									print = "PRINT";
									break;
								case "reject":
									reject = "REJECT";
									break;
								}
								sb.append(", ");
							}
							sb = sb.deleteCharAt(sb.length() - 1);
							count = modulesRepo.AssignModulesForRoles(role_id, module, submodule, add, update, delete,
									view, verify, release, reject, print, created_by.toUpperCase(), sb.toString());
							add = ""; update = ""; delete = ""; view = ""; verify = "";	release = ""; print = ""; reject = "";
							loopingValue = loopingValue + 1;
							break;
						}
					}
				}

			}
			logger.info("EXECUTING METHOD :: AFTER ADDING assignModulesToRole");
			response.put("action", "AssignModuleToRole");
			response.put("message", count > 0 ? "Success" : "Failure");
			response.put("status", count > 0 ? "YES" : "NO");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR assignModulesToRole ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: assignModulesToRole");
		return response;
	}

	
	@SuppressWarnings("unused")
	@PostMapping("/assignModuleToRole/update")
	public @ResponseBody Map<String, Object> assignUpdateModulesToRole(@RequestParam String role_id,
			@RequestParam List<String> module_id, @RequestParam List<String[]> submodule_id,
			@RequestParam List<String[]> action_create, @RequestParam List<String[]> action_update,
			@RequestParam List<String[]> action_delete, @RequestParam List<String[]> action_read,
			@RequestParam List<String[]> action_verify, @RequestParam List<String[]> action_release,
			@RequestParam List<String[]> action_reject, @RequestParam List<String[]> action_print,
			@RequestParam String modified_by, @RequestParam String id) {
		int count = 0;
		logger.info("EXECUTING METHOD :: assignUpdateModulesToRole");
		Map<String, Object> createModule = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: BEFORE UPDATING assignUpdateModulesToRole");
		try {
			for (String module : module_id) {
				for (int i = 0; i < submodule_id.size(); i++) {
					String[] subModule = submodule_id.get(i);
					String[] actionCreate = action_create.get(i);
					String[] actionUpdate = action_update.get(i);
					String[] actionDelete = action_delete.get(i);
					String[] actionRead = action_read.get(i);
					String[] actionVerifiy = action_verify.get(i);
					String[] actionRelease = action_release.get(i);
					String[] actionReject = action_reject.get(i);
					String[] actionPrint = action_print.get(i);
					for (int j = 0; j < subModule.length; j++) {
						// modulesRepo.updateModulesForRoles(role_id, module,
						// subModule[j],actionCreate[j],actionUpdate[j],actionDelete[j],actionRead[j],actionVerifiy[j],actionRelease[j],actionReject[j],actionPrint[j],modified_by,id);
					}
				}
			}
			logger.info("EXECUTING METHOD :: AFTER UPDATING assignUpdateModulesToRole");
			createModule.put("action", "AssignModuleToRole");
			createModule.put("message", count > 0 ? "Success" : "Failure");
			createModule.put("status", count > 0 ? "YES" : "NO");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR assignUpdateModulesToRole ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: assignUpdateModulesToRole");
		return createModule;
	}

	@GetMapping("/assignModuleToRole/list")
	public @ResponseBody Map<String, Object> listModulesToRole() {
		Map<String, Object> createModule = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listModulesToRole");
		try { // for each submodules UI have to send same way
			List<RoleAssignModule_SubModule_Actions> ls = modulesRepo.getListModuleandSubModuleActions();
			createModule.put("action", "ListAssignModuleToRole");
			createModule.put("message", ls != null ? "Success" : "Failure");
			createModule.put("status", ls != null ? "YES" : "NO");
			createModule.put("data", ls != null ? ls : "NULL");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listModulesToRole ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listModulesToRole");
		return createModule;
	}

	@GetMapping("/assignModuleToRole/search")
	public @ResponseBody Map<String, Object> moduleSearchById(@RequestParam String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<RoleAssignModule_SubModule_Actions> userInterface = null;
		logger.info("EXECUTING METHOD :: moduleSearchById");
		try {
			userInterface = modulesRepo.serachModuleSubModulesActionsListById(id);
			response.put("Data", userInterface);
			response.put("message",
					(userInterface != null) ? "Search AssignModuleToRole Success" : "Search AssignModuleToRole Failed");
			response.put("status", (userInterface != null) ? "yes" : "no");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR moduleSearchById ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: moduleSearchById");
		return response;
	}

}
