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

import com.JIMS.integration.interfaces.FactoryInterface;
import com.JIMS.integration.repository.FactoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class FactoryController {

	@Autowired
	private FactoryRepository factoryrepo;

	Logger logger = LogManager.getLogger(CountryController.class);

	@PostMapping("/factory/addFactory")
	public @ResponseBody Map<String, Object> addFactory(@RequestParam String factory_name) {

		Map<String, Object> addFactorymap = new HashMap<String, Object>();

		try {
			int checkIfFactoryExists = factoryrepo.checkIfFactoryNameExists(factory_name);

			if (checkIfFactoryExists > 0) {
				addFactorymap.put("message", "Factory Name Already exists");
				addFactorymap.put("status", "no");
				addFactorymap.put("action", "AddFactory");
			}

			int addFactoryRecord = factoryrepo.addFactory(factory_name);

			addFactorymap.put("message", (addFactoryRecord > 0) ? "Success" : "Factory Not Added");
			addFactorymap.put("status", (addFactoryRecord > 0) ? "yes" : "no");
			addFactorymap.put("action", "AddFactory");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addFactorymap;
	}

	@GetMapping("/factory/getAllFactoryList")
	public @ResponseBody Map<String, Object> getAllFactoryList() {

		logger.info("EXECUTING METHOD :: getAllFactoryList");

		Map<String, Object> allFactorymap = new HashMap<String, Object>();
		List<FactoryInterface> allFactoryList = null;

		try {
			allFactoryList = factoryrepo.getAllFactoryDetails();

			allFactorymap.put("message", ((allFactoryList != null) && (!allFactoryList.isEmpty())) ? "Success"
					: "Factory Details not available");
			allFactorymap.put("status", ((allFactoryList != null) && (!allFactoryList.isEmpty())) ? "yes" : "no");
			allFactorymap.put("action", "FactoryInfo");

			if ((allFactoryList != null) && (!allFactoryList.isEmpty())) {
				allFactorymap.put("FactoryDetails", allFactoryList);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllFactoryList  -> " + e.getMessage());
		}
		return allFactorymap;
	}

	
	
	
	
	@GetMapping("/factory/getAllFactoryListSS")
	public @ResponseBody Map<String, Object> getAllFactoryLists() {

		logger.info("EXECUTING METHOD :: getAllFactoryList");

		Map<String, Object> allFactorymap = new HashMap<String, Object>();
		List<FactoryInterface> allFactoryList = null;

		try {
			allFactoryList = factoryrepo.getAllFactoryDetailss();

			allFactorymap.put("message", ((allFactoryList != null) && (!allFactoryList.isEmpty())) ? "Success"
					: "Factory Details not available");
			allFactorymap.put("status", ((allFactoryList != null) && (!allFactoryList.isEmpty())) ? "yes" : "no");
			allFactorymap.put("action", "FactoryInfo");

			if ((allFactoryList != null) && (!allFactoryList.isEmpty())) {
				allFactorymap.put("FactoryDetails", allFactoryList);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllFactoryList  -> " + e.getMessage());
		}
		return allFactorymap;
	}
	
	
	@GetMapping("/factory/getFactoryListById/{factory_id}")
	public @ResponseBody Map<String, Object> getFactoryListById(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: getFactoryListById");

		Map<String, Object> factoryListByIdmap = new HashMap<String, Object>();
		FactoryInterface factoryListById = null;

		try {
			factoryListById = factoryrepo.getFactoryDetailsById(factory_id);

			factoryListByIdmap.put("message",
					(factoryListById != null) ? "Success" : "Factory details Based On Id not available");
			factoryListByIdmap.put("status", (factoryListById != null) ? "yes" : "no");
			factoryListByIdmap.put("action", "FactoryInfoBasedOnId");

			if (factoryListById != null) {

				ObjectMapper mapper = new ObjectMapper();
				factoryListByIdmap
						.putAll(mapper.convertValue(factoryListById, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getFactoryListById  -> " + e.getMessage());
		}
		return factoryListByIdmap;
	}

}
