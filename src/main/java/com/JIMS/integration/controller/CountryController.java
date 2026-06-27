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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.CountryInterface;
import com.JIMS.integration.repository.CountryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class CountryController {

	@Autowired
	private CountryRepository countryrepo;

	Logger logger = LogManager.getLogger(CountryController.class);

	@PostMapping("/country/addCountry")
	public @ResponseBody Map<String, Object> addCountry(@RequestBody Map<String, String> country) {

		logger.info("EXECUTING METHOD :: addCountry");

		Map<String, Object> addCountrymap = new HashMap<String, Object>();

		try {
			String country_code = country.get("country_code");
			String country_name = country.get("country_name");
			String user_id = country.get("user_id");
			String factory_id = country.containsKey("factory_id") ? country.get("factory_id") : "0";

			int addCountryRecord = countryrepo.addCountry(country_code, country_name, user_id, factory_id);

			addCountrymap.put("message", (addCountryRecord > 0) ? "Success" : "Country Not Added");
			addCountrymap.put("status", (addCountryRecord > 0) ? "yes" : "no");
			addCountrymap.put("action", "AddCountry");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: addCountry  -> " + e.getMessage());
		}
		return addCountrymap;

	}

	@PostMapping("/country/addCountryNew")
	public @ResponseBody Map<String, Object> addCountryNew(@RequestParam String country_code,
			@RequestParam String country_name, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id) {

		logger.info("EXECUTING METHOD :: addCountry");

		country_code = country_code.toUpperCase();
		country_name = country_name.toUpperCase();
		created_by = created_by.toUpperCase();

		Map<String, Object> addCountryNewmap = new HashMap<String, Object>();

		try {

			int checkCountryExistscount = countryrepo.checkWhetherCountryAlreadyExists(country_name,country_code);

			if (checkCountryExistscount > 0) {
				addCountryNewmap.put("message", "Country Already Exists");
				addCountryNewmap.put("status", "no");
				addCountryNewmap.put("action", "AddCountryNew");

				return addCountryNewmap;
			} else {
				int addCountryRecord = countryrepo.addCountry(country_code, country_name, created_by, factory_id);

				addCountryNewmap.put("message", (addCountryRecord > 0) ? "Success" : "Country Not Added");
				addCountryNewmap.put("status", (addCountryRecord > 0) ? "yes" : "no");
				addCountryNewmap.put("action", "AddCountryNew");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: addCountry  -> " + e.getMessage());
		}
		return addCountryNewmap;

	}

	@PostMapping("/country/updateCountry")
	public @ResponseBody Map<String, Object> updateCountry(@RequestBody Map<String, String> upcountry) {

		logger.info("EXECUTING METHOD :: updateCountry");

		Map<String, Object> updateCountrymap = new HashMap<String, Object>();

		try {

			String country_id = upcountry.get("country_id");
			String country_code = upcountry.get("country_code");
			String country_name = upcountry.get("country_name");
			String user_id = upcountry.get("user_id");

			// int moveExistingToHistoryrecord =
			// countryrepo.moveCountryToHistory(country_id, user_id);
			/*
			 * if (moveExistingToHistoryrecord == 0) { updateCountrymap.put("message",
			 * "Failed to insert existing company into history tale");
			 * updateCountrymap.put("status", "no"); updateCountrymap.put("action",
			 * "UpdateCountry");
			 * 
			 * return updateCountrymap; } else {
			 */

			
			
			
			
			int updateCountryrecord = countryrepo.updateCountry(country_code, country_name, user_id, country_id);

			updateCountrymap.put("message", (updateCountryrecord > 0) ? "Success" : "Country Not Updated");
			updateCountrymap.put("status", (updateCountryrecord > 0) ? "yes" : "no");
			updateCountrymap.put("action", "UpdateCountry");

			/* } */

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateCountry  -> " + e.getMessage());
		}
		return updateCountrymap;

	}

	@PostMapping("/country/updateCountryNew")
	public @ResponseBody Map<String, Object> updateCountryNew(@RequestParam String country_code,
			@RequestParam String country_name, @RequestParam String modified_by, @RequestParam String country_id,@RequestParam Boolean changed_name,@RequestParam Boolean changed_code) {

		logger.info("EXECUTING METHOD :: updateCountryNew");

		country_code = country_code.toUpperCase();
		country_name = country_name.toUpperCase();
		modified_by = modified_by.toUpperCase();

		Map<String, Object> updateCountryNewmap = new HashMap<String, Object>();

		try {
			
			if(changed_name) {
				int country_name_count = countryrepo.checkWhethereCountryAlready(country_name);
				if(country_name_count >0) {
					updateCountryNewmap.put("message", "Country  Already Exists");
					updateCountryNewmap.put("status", "no");
					return updateCountryNewmap;
				}
				
			}
			if(changed_code) {
				int country_code_count = countryrepo.checkWhethereCountryCodeAlready(country_code);
				if(country_code_count>0) {
					updateCountryNewmap.put("message", "Country Code  Already Exists");
					updateCountryNewmap.put("status", "no");
					return updateCountryNewmap;
				}
			}
			
			
			

			String message = countryrepo.checkCountryInTransactions(country_id);
			if (message != null) {

				updateCountryNewmap.put("message", message);
				updateCountryNewmap.put("status", "no");
				updateCountryNewmap.put("action", "UpdateCountryNew");

				return updateCountryNewmap;
			} else {
				int updateCountryrecord = countryrepo.updateCountryNew(country_code, country_name, modified_by,
						country_id);

				updateCountryNewmap.put("message", (updateCountryrecord > 0) ? "Success" : "Country Not Updated");
				updateCountryNewmap.put("status", (updateCountryrecord > 0) ? "yes" : "no");
				updateCountryNewmap.put("action", "UpdateCountryNew");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateCountryNew  -> " + e.getMessage());
		}
		return updateCountryNewmap;

	}

	@PostMapping("/country/deleteCountry")
	public @ResponseBody Map<String, Object> deleteCountry(@RequestBody Map<String, String> delcountry) {

		logger.info("EXECUTING METHOD :: deleteCountry");

		String country_id = delcountry.get("country_id");
		String user_id = delcountry.get("user_id");

		Map<String, Object> deleteCountrymap = new HashMap<String, Object>();

		try {

			String message = countryrepo.checkCountryInTransactions(country_id);
			if (message != null) {

				deleteCountrymap.put("message", message);
				deleteCountrymap.put("status", "no");
				deleteCountrymap.put("action", "DeleteCountry");

				return deleteCountrymap;
			} else {
				int deleteCompanyrecord = countryrepo.deleteCountry(country_id, user_id);
				deleteCountrymap.put("message", (deleteCompanyrecord > 0) ? "Success" : "Country Not Deleted");
				deleteCountrymap.put("status", (deleteCompanyrecord > 0) ? "yes" : "no");
				deleteCountrymap.put("action", "DeleteCountry");

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteCountry  -> " + e.getMessage());
		}
		return deleteCountrymap;
	}

	@PostMapping("/country/deleteCountryNew")
	public @ResponseBody Map<String, Object> deleteCountryNew(@RequestParam String country_id,
			@RequestParam String modified_by) {

		logger.info("EXECUTING METHOD :: deleteCountry");

		Map<String, Object> deleteCountryNewmap = new HashMap<String, Object>();

		try {

			String message = countryrepo.checkCountryInTransactions(country_id);
			if (message != null) {

				deleteCountryNewmap.put("message", message);
				deleteCountryNewmap.put("status", "no");
				deleteCountryNewmap.put("action", "DeleteCountryNew");

				return deleteCountryNewmap;
			} else {
				int deleteCompanyrecord = countryrepo.deleteCountryNew(modified_by, country_id);

				deleteCountryNewmap.put("message", (deleteCompanyrecord > 0) ? "Success" : "Country Not Deleted");
				deleteCountryNewmap.put("status", (deleteCompanyrecord > 0) ? "yes" : "no");
				deleteCountryNewmap.put("action", "DeleteCountryNew");

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteCountry  -> " + e.getMessage());
		}
		return deleteCountryNewmap;
	}

	@GetMapping("/country/getAllCountryList/{factory_id}")
	public @ResponseBody Map<String, Object> getAllCountryList(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: getAllCountryList");

		Map<String, Object> allCountrymap = new HashMap<String, Object>();
		List<CountryInterface> allCountryList = null;

		try {
			allCountryList = countryrepo.getAllCountryDetails(factory_id);

			allCountrymap.put("message", ((allCountryList != null) && (!allCountryList.isEmpty())) ? "Success"
					: "Country Details not available");
			allCountrymap.put("status", ((allCountryList != null) && (!allCountryList.isEmpty())) ? "yes" : "no");
			allCountrymap.put("action", "CountryInfo");

			if ((allCountryList != null) && (!allCountryList.isEmpty())) {
				allCountrymap.put("CountryDetails", allCountryList);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllCountryList  -> " + e.getMessage());
		}
		return allCountrymap;
	}

	@GetMapping("/country/getAllCountryListNew")
	public @ResponseBody Map<String, Object> getAllCountryListNew(@RequestParam(required = false) String factory_id) {

		logger.info("EXECUTING METHOD :: getAllCountryListNew");

		Map<String, Object> allCountryNewmap = new HashMap<String, Object>();
		List<CountryInterface> allCountryNewList = null;

		try {
			allCountryNewList = countryrepo.getAllCountryDetailsNew(factory_id);

			allCountryNewmap.put("message", ((allCountryNewList != null) && (!allCountryNewList.isEmpty())) ? "Success"
					: "Country Details not available");
			allCountryNewmap.put("status",
					((allCountryNewList != null) && (!allCountryNewList.isEmpty())) ? "yes" : "no");
			allCountryNewmap.put("action", "AllCountriesInfoNew");

			if ((allCountryNewList != null) && (!allCountryNewList.isEmpty())) {
				allCountryNewmap.put("AllCountryDetailsNew", allCountryNewList);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllCountryListNew  -> " + e.getMessage());
		}
		return allCountryNewmap;
	}

	@GetMapping("/country/getCountryBasedOnId/{country_id}")
	public @ResponseBody Map<String, Object> getCountryBasedOnId(@PathVariable String country_id) {

		logger.info("EXECUTING METHOD :: getCountryBasedOnId");

		Map<String, Object> countryBasedOnIdmap = new HashMap<String, Object>();
		CountryInterface countryByid = null;
		try {

			countryByid = countryrepo.getCOuntryBasedOnId(country_id);

			countryBasedOnIdmap.put("message", (countryByid != null) ? "Success" : "Country details not available");
			countryBasedOnIdmap.put("status", (countryByid != null) ? "yes" : "no");
			countryBasedOnIdmap.put("action", "CountryBasedOnId");

			if (countryByid != null) {

				ObjectMapper mapper = new ObjectMapper();
				countryBasedOnIdmap.putAll(mapper.convertValue(countryByid, new TypeReference<Map<String, Object>>() {
				}));
				mapper.clearProblemHandlers();
				mapper = null;

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getCountryBasedOnId  -> " + e.getMessage());
		}
		return countryBasedOnIdmap;
	}

	@GetMapping("/country/getCountryBasedOnIdNew")
	public @ResponseBody Map<String, Object> getCountryBasedOnIdNew(@RequestParam String country_id) {

		logger.info("EXECUTING METHOD :: getCountryBasedOnId");

		Map<String, Object> countryBasedOnIdNewmap = new HashMap<String, Object>();
		CountryInterface countryByidNew = null;
		try {

			countryByidNew = countryrepo.getCountryBasedOnIdNew(country_id);

			countryBasedOnIdNewmap.put("message",
					(countryByidNew != null) ? "Success" : "Country details not available");
			countryBasedOnIdNewmap.put("status", (countryByidNew != null) ? "yes" : "no");
			countryBasedOnIdNewmap.put("action", "CountryBasedOnId");

			if (countryByidNew != null) {

				ObjectMapper mapper = new ObjectMapper();
				countryBasedOnIdNewmap
						.putAll(mapper.convertValue(countryByidNew, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;

			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getCountryBasedOnId  -> " + e.getMessage());
		}
		return countryBasedOnIdNewmap;
	}

}
