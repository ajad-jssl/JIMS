package com.JIMS.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.StateInterface;
import com.JIMS.integration.repository.StateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class StateController {

	@Autowired
	private StateRepository staterepo;

	Logger logger = LogManager.getLogger(StateController.class);

	@PostMapping("/state/createStateNew")
	private @ResponseBody Map<String, Object> createStateNew(@RequestParam String state_code,
			@RequestParam String state_name, @RequestParam String state_id, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id, @RequestParam String country_id) {

		logger.info("EXECUTING METHOD :: createState");

		state_code = state_code.toUpperCase();
		state_name = state_name.toUpperCase();
		created_by = created_by.toUpperCase();

		Map<String, Object> addStateMap = new HashMap<String, Object>();
		int statecount = 0;
		try {

			logger.info("EXECUTING METHOD :: BEFORE adding State");

			int checkStateCountIfExists = staterepo.checkWhetherStateExistsOrNot(state_name, state_code,state_id);

			if (checkStateCountIfExists > 0) {
				addStateMap.put("action", "CreateState");
				addStateMap.put("message", "State Code or State Name or State_Id  Already Exists");
				addStateMap.put("status", "no");

				return addStateMap;
			} else {

				statecount = staterepo.addStateDetailsNew(state_code, state_name, state_id, created_by, factory_id,
						country_id);

				logger.info("EXECUTING METHOD :: AFTER adding State");

				addStateMap.put("action", "CreateState");
				addStateMap.put("message", (statecount > 0) ? "Success" : "State details not added");
				addStateMap.put("status", (statecount > 0) ? "yes" : "no");

			}

		} catch (Exception e) { 
			e.printStackTrace();
		}
		return addStateMap;

	}

	@PostMapping("/state/updateStateNew")
	private @ResponseBody Map<String, Object> updateStateNew(@RequestParam String state_code,
			@RequestParam String state_name, @RequestParam String state_id, @RequestParam String modified_by,
			@RequestParam String country_id, @RequestParam String id,@RequestParam Boolean checkName,@RequestParam Boolean checkCode,@RequestParam Boolean checkStateId ) {

		logger.info("EXECUTING METHOD :: updateState");

		state_code = state_code.toUpperCase();
		state_name = state_name.toUpperCase();
		modified_by = modified_by.toUpperCase();

		Map<String, Object> getStateMap = new HashMap<String, Object>();
		int statecount = 0;

		try {
			if(checkStateId) {
			
			int state_id_count = staterepo.checkWhertherStateIdExistOrNot(state_id);
			if(state_id_count > 0) {
				getStateMap.put("message", "State_id is Already Exists");
				getStateMap.put("status", "no");
				return getStateMap;
			}
			}
			
			if(checkCode) {
			int state_code_count  = staterepo.checkWhertherStateCodeExistOrNot(state_code);
			if(state_code_count > 0) {
				getStateMap.put("message", "state_code is Already Exists");
				getStateMap.put("status", "no");
				return getStateMap;
			}
			}
			if(checkName) {
			int state_name_count = staterepo.checkWhertherStateNameExistOrNot(state_name);
			if(state_name_count > 0) {
				getStateMap.put("message", "state_name is Already Exists");
				getStateMap.put("status", "no");
				return getStateMap;
			}
			}

			String message = staterepo.checkWhetherStateInvolvedInAnyTransactions(
					 id);
			if (message != null) {
				getStateMap.put("action", "UpdateState");
				getStateMap.put("message", message);
				getStateMap.put("status", "no");

				return getStateMap;
			} else {
				statecount = staterepo.UpdateStateNew(state_code, state_name, state_id, modified_by, country_id, id);
				getStateMap.put("action", "UpdateState");
				getStateMap.put("message", (statecount > 0) ? "Success" : "State details not updated!");
				getStateMap.put("status", (statecount > 0) ? "yes" : "no");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getStateMap;
	}

	@PostMapping("/state/deleteState")
	private @ResponseBody Map<String, Object> deleteStatesById(@RequestParam String user_id,
			@RequestParam String state_id) {

		logger.info("EXECUTING METHOD :: deleteStatesById");

		Map<String, Object> deleteStatemap = new HashMap<String, Object>();
		int statecount = 0;
		try {

			String message = staterepo.checkWhetherStateInvolvedInAnyTransactionsBeforeDelete(state_id);
			if (message != null) {
				deleteStatemap.put("action", "DeleteState");
				deleteStatemap.put("message", message);
				deleteStatemap.put("status", "no");

				return deleteStatemap;
			} else {
				statecount = staterepo.deleteState(state_id, user_id);

				deleteStatemap.put("action", "DeleteState");
				deleteStatemap.put("message", (statecount > 0) ? "Success" : "State details not deleted");
				deleteStatemap.put("status", (statecount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteStatesById  -> " + e.getMessage());

		}
		return deleteStatemap;
	}

	@GetMapping("/state/getAllStatesNew")
	private @ResponseBody Map<String, Object> getAllStatesDetailsNew(
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> addAllStateMap = new HashMap<String, Object>();

		logger.info("EXECUTING METHOD :: getAllStatesDetailsNew");

		try {
			int stateCount = staterepo.getStateByCount(factory_id);

			List<StateInterface> allStatesList = staterepo.getStates(factory_id);
			addAllStateMap.put("action", "AllStatesInfoNew");
			addAllStateMap.put("message", (allStatesList.size() > 0) ? "Success" : "State details not found!");
			addAllStateMap.put("status", (allStatesList.size() > 0) ? "yes" : "no");

			if ((allStatesList != null) && (!allStatesList.isEmpty())) {
				addAllStateMap.put("StateCount", stateCount);
				addAllStateMap.put("StateDetailsNew", allStatesList);

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllStatesDetailsNew  -> " + e.getMessage());

		}
		return addAllStateMap;
	}

	@GetMapping("/state/getStatesByIdNew")
	private @ResponseBody Map<String, Object> getStatesByIdNew(@RequestParam String state_id) {

		logger.info("EXECUTING METHOD :: getAllStatesDetails");

		Map<String, Object> getStateMapByid = new HashMap<String, Object>();
		try {
			StateInterface statelist = staterepo.getStateById(state_id);

			getStateMapByid.put("action", "StateDetailsById");
			getStateMapByid.put("message", (statelist != null) ? "Success" : "State details not found!");
			getStateMapByid.put("status", (statelist != null) ? "yes" : "no");
			if (statelist != null) {
				ObjectMapper mapper = new ObjectMapper();
				getStateMapByid.putAll(mapper.convertValue(statelist, new TypeReference<Map<String, Object>>() {
				}));
				mapper.clearProblemHandlers();
				mapper = null;
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getStatesById  -> " + e.getMessage());
		}
		return getStateMapByid;
	}

	@GetMapping("/state/getStatesBsdOnCountry/{country_id}")
	public @ResponseBody Map<String, Object> getStatesBasedOnCountry(@PathVariable String country_id) {

		Map<String, Object> getStatesBasedOncountrymap = new HashMap<String, Object>();
		List<StateInterface> statesBsdOnCountrylist = null;
		try {

			statesBsdOnCountrylist = staterepo.getStateByCountry(country_id);

			getStatesBasedOncountrymap.put("action", "StateDetailsBasedOnCountry");
			getStatesBasedOncountrymap.put("message",
					(statesBsdOnCountrylist.size() > 0) ? "Success" : "State details not found!");
			getStatesBasedOncountrymap.put("status", (statesBsdOnCountrylist.size() > 0) ? "yes" : "no");

			if ((statesBsdOnCountrylist != null) && (!statesBsdOnCountrylist.isEmpty())) {
				getStatesBasedOncountrymap.put("StateDetailsBasedOnCountry", statesBsdOnCountrylist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getStatesBasedOncountrymap;

	}

	@GetMapping("/state/getStatesBsdOnCountry")
	public @ResponseBody Map<String, Object> getStatesBasedOnCountryNew(@RequestParam String country_id) {

		Map<String, Object> getStatesBasedOncountrymap = new HashMap<String, Object>();
		List<StateInterface> statesBsdOnCountrylist = null;
		try {

			statesBsdOnCountrylist = staterepo.getStateByCountry(country_id);

			getStatesBasedOncountrymap.put("action", "StateDetailsBasedOnCountry");
			getStatesBasedOncountrymap.put("message",
					(statesBsdOnCountrylist.size() > 0) ? "Success" : "State details not found!");
			getStatesBasedOncountrymap.put("status", (statesBsdOnCountrylist.size() > 0) ? "yes" : "no");

			if ((statesBsdOnCountrylist != null) && (!statesBsdOnCountrylist.isEmpty())) {
				getStatesBasedOncountrymap.put("StateDetailsBasedOnCountry", statesBsdOnCountrylist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getStatesBasedOncountrymap;

	}
	
	

}
