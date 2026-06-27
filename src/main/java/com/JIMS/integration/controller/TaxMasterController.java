package com.JIMS.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.TaxMasterInterface;
import com.JIMS.integration.repository.TaxMasterRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class TaxMasterController {

	Logger logger = LogManager.getLogger(SeriesMasterNewController.class);
	@Autowired
	private TaxMasterRepository taxMastersRepository;

	@PostMapping("/taxmasters/addTaxMaster")
	public @ResponseBody Map<String, Object> createTaxMastersNew(@RequestParam String tax_name,
			@RequestParam String tax_per, @RequestParam String startdate, @RequestParam String enddate,
			@RequestParam String created_by) {
		logger.info("EXECUTING METHOD :: createTaxMastersNew");
		Map<String, Object> addtaxMastersMap = new HashMap<String, Object>();
		tax_name = tax_name.toUpperCase();
		created_by = created_by.toUpperCase();
		if (enddate == null || enddate.trim().isEmpty()) {
			enddate = null;
		}
		int taxcount = 0;
		try {
			String tax_desc = tax_name + "/" + tax_per + "%";
			logger.info("EXECUTING METHOD :: BEFORE ADDING createTaxMastersNew");
			int checkTaxCount = taxMastersRepository.checkTaxExistOrNot(tax_name, tax_per);
			if (checkTaxCount > 0) {
				addtaxMastersMap.put("action", "AddTaxMasters");
				addtaxMastersMap.put("message", "Tax details Already Exists");
				addtaxMastersMap.put("status", "no");
				return addtaxMastersMap;
			} else {
				taxcount = taxMastersRepository.addTaxMastersNew(tax_name, tax_per, startdate, enddate, created_by,
						tax_desc);
				addtaxMastersMap.put("action", "AddTaxMasters");
				addtaxMastersMap.put("message", (taxcount > 0) ? "Success" : "TaxMasters details not added");
				addtaxMastersMap.put("status", (taxcount > 0) ? "yes" : "no");
			}
			logger.info("EXECUTING METHOD :: AFTER ADDING createTaxMastersNew");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createTaxMastersNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createTaxMastersNew");
		return addtaxMastersMap;
	}

	@GetMapping("/taxmasters/list")
	private @ResponseBody Map<String, Object> getAllTaxMasters() {
		Map<String, Object> getAllTaxMastersMap = new HashMap<String, Object>();
		List<TaxMasterInterface> getAllTaxMasterslists = null;
		logger.info("EXECUTING METHOD :: getAllTaxMasters");
		try {
			getAllTaxMasterslists = taxMastersRepository.getAllTaxMasters();
			getAllTaxMastersMap.put("action", "GetAllBusinessUnits");
			getAllTaxMastersMap.put("message", (getAllTaxMasterslists.size() > 0) ? "Success" : "No TaxMasters");
			getAllTaxMastersMap.put("status", (getAllTaxMasterslists.size() > 0) ? "yes" : "no");
			getAllTaxMastersMap.put("Data", getAllTaxMasterslists);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getAllTaxMasters ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getAllTaxMasters");
		return getAllTaxMastersMap;
	}

	@GetMapping("/taxmasters/searchtaxmasters")
	private @ResponseBody Map<String, Object> getTaxMastersById(@RequestParam String id) {
		Map<String, Object> getTaxMastersByIdMap = new HashMap<String, Object>();
		TaxMasterInterface getTaxMastersByIdlists = null;
		logger.info("EXECUTING METHOD :: getTaxMastersById");
		try {
			getTaxMastersByIdlists = taxMastersRepository.getTaxMastersById(id);
			getTaxMastersByIdMap.put("action", "GetTaxMastersById");
			getTaxMastersByIdMap.put("message", (getTaxMastersByIdlists != null) ? "Success" : "No TaxMasters");
			getTaxMastersByIdMap.put("status", (getTaxMastersByIdlists != null) ? "yes" : "no");
			getTaxMastersByIdMap.put("Data", getTaxMastersByIdlists);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getTaxMastersById ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getTaxMastersById");
		return getTaxMastersByIdMap;
	}

	@PostMapping("/taxmasters/delete")
	private @ResponseBody Map<String, Object> deleteBusinessUnitsById(@RequestBody Map<String, String> val) {
		Map<String, Object> gettaxmastMap = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteBusinessUnitsById");
		try {
			String tax_id = val.get("tax_id");
			String modified_by = val.get("modified_by");
			taxMastersRepository.updateTaxMasterHistory(modified_by, tax_id);
			int taxcount = taxMastersRepository.deleteTaxMastersById(tax_id, modified_by);
			gettaxmastMap.put("action", "DeleteTaxMastersById");
			gettaxmastMap.put("message", (taxcount > 0) ? "Success" : "TaxMasters  not delete!");
			gettaxmastMap.put("status", (taxcount > 0) ? "yes" : "no");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteBusinessUnitsById ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteBusinessUnitsById");
		return gettaxmastMap;
	}

	@PostMapping("/taxmasters/deleteNew")
	private @ResponseBody Map<String, Object> deleteBusinessUnitsById(@RequestParam String tax_id,
			@RequestParam String modified_by) {
		logger.info("EXECUTING METHOD :: deleteBusinessUnitsById");
		Map<String, Object> deleteTaxmastermap = new HashMap<String, Object>();
		try {
			taxMastersRepository.updateTaxMasterHistory(modified_by, tax_id);
			String message = taxMastersRepository.checkWhetherTaxIsUsedInTransactionsBeforeDelete(tax_id);
			if (message != null) {
				deleteTaxmastermap.put("action", "DeleteTaxMastersById");
				deleteTaxmastermap.put("message", message);
				deleteTaxmastermap.put("status", "no");
				return deleteTaxmastermap;
			} else {
				int taxcount = taxMastersRepository.deleteTaxMastersById(tax_id, modified_by);
				deleteTaxmastermap.put("action", "DeleteTaxMastersById");
				deleteTaxmastermap.put("message", (taxcount > 0) ? "Success" : "TaxMasters  not delete!");
				deleteTaxmastermap.put("status", (taxcount > 0) ? "yes" : "no");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteBusinessUnitsById ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteBusinessUnitsById");
		return deleteTaxmastermap;
	}

	@PostMapping("/taxmasters/updateTaxmaster")
	public @ResponseBody Map<String, Object> UpdateTaxMastersNew(@RequestParam String tax_name,
			@RequestParam String tax_per, @RequestParam String startdate, @RequestParam String enddate,
			@RequestParam String tax_desc, @RequestParam String modified_by, @RequestParam String tax_id) {
		Map<String, Object> updatetaxMastersMap = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: UpdateTaxMastersNew");
		tax_name = tax_name.toUpperCase();
		modified_by = modified_by.toUpperCase();
		if (enddate == null || enddate.trim().isEmpty()) {
			enddate = null;
		}
		try {
			String message = taxMastersRepository.checkWhetherTaxIsUsedInTransactions(tax_id);
			int check = taxMastersRepository.checkWhetherTaxUsed(tax_name, tax_per, tax_id);
			logger.info("the check method",+check);
			if (message != null) {
				updatetaxMastersMap.put("action", "UpdateTaxMasters");
				updatetaxMastersMap.put("message", message);
				updatetaxMastersMap.put("status", "no");
				return updatetaxMastersMap;
			}
	
			else if(check >0) {
				updatetaxMastersMap.put("action", "UpdateTaxMasters");
				updatetaxMastersMap.put("message", "Tax details Already Exist");
				updatetaxMastersMap.put("status", "no");
				return updatetaxMastersMap;
			}
			else {
				int taxcount = taxMastersRepository.UpdateTaxMasters(tax_name, tax_per, startdate, enddate, modified_by,
						tax_id, tax_desc);
				updatetaxMastersMap.put("action", "UpdateTaxMasters");
				updatetaxMastersMap.put("message", (taxcount > 0) ? "Success" : "TaxMasters details not updated");
				updatetaxMastersMap.put("status", (taxcount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR UpdateTaxMastersNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: UpdateTaxMastersNew");
		return updatetaxMastersMap;
	}
	
	@PostMapping("/taxmasters/updateTaxEndDateOnly")
	public @ResponseBody Map<String,Object> updateTaxMasterEndNew(@RequestParam String tax_name,
			@RequestParam String tax_per, @RequestParam String startdate, @RequestParam String enddate,
			@RequestParam String tax_desc, @RequestParam String modified_by, @RequestParam String tax_id){
		Map<String, Object> updatetaxMastersMap = new HashMap<String, Object>();
		
		
		try {
		int taxcount = taxMastersRepository.UpdateTaxMasters(tax_name, tax_per, startdate, enddate, modified_by,
				tax_id, tax_desc);
		updatetaxMastersMap.put("action", "UpdateTaxMasters");
		updatetaxMastersMap.put("message", (taxcount > 0) ? "Success" : "TaxMasters details not updated");
		updatetaxMastersMap.put("status", (taxcount > 0) ? "yes" : "no");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR UpdateTaxMastersNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: UpdateTaxMastersNew");
		return updatetaxMastersMap;
	}
	
	
	
	
//	@PostMapping("/taxmasters/updateTaxmaster")
//	public @ResponseBody Map<String, Object> UpdateTaxMastersNew(
//	        @RequestParam String tax_name,
//	        @RequestParam String tax_per,
//	        @RequestParam String startdate,
//	        @RequestParam String enddate,
//	        @RequestParam String tax_desc,
//	        @RequestParam String modified_by,
//	        @RequestParam String tax_id) {
//
//	    Map<String, Object> response = new HashMap<>();
//	    logger.info("EXECUTING METHOD :: UpdateTaxMastersNew");
//
//	    tax_name = tax_name.toUpperCase();
//	    modified_by = modified_by.toUpperCase();
//
//	    if (enddate == null || enddate.trim().isEmpty()) {
//	        enddate = null;
//	    }
//
//	    try {
//	        // 1️ Fetch existing data
//	        Map<String, Object> oldData = taxMastersRepository.getTaxById(tax_id);
//
//	        boolean isOnlyEndDateChanged =
//	                enddate != null && !enddate.equals(oldData.get("enddate")) &&
//	                tax_name.equals(oldData.get("tax_name")) &&
//	                tax_per.equals(oldData.get("tax_per")) &&
//	                startdate.equals(oldData.get("startdate")) &&
//	                tax_desc.equals(oldData.get("tax_desc"));
//
//	        // 2️ Check usage
//	        String usageMessage =
//	                taxMastersRepository.checkWhetherTaxIsUsedInTransactions(tax_name, tax_per, tax_id);
//
//	        // 3️ If used & NOT only end date → BLOCK
//	        if (usageMessage != null && !isOnlyEndDateChanged) {
//	            response.put("action", "UpdateTaxMasters");
//	            response.put("message", "This tax is used in transactions. Only End Date can be updated.");
//	            response.put("status", "no");
//	            return response;
//	        }
//
//	        // 4️ Allow update
//	        int count = taxMastersRepository.UpdateTaxMasters(
//	                tax_name, tax_per, startdate, enddate, modified_by, tax_id, tax_desc
//	        );
//
//	        response.put("action", "UpdateTaxMasters");
//	        response.put("message", count > 0 ? "Success" : "TaxMasters details not updated");
//	        response.put("status", count > 0 ? "yes" : "no");
//
//	    } catch (Exception e) {
//	        logger.error("ERROR :: UpdateTaxMastersNew", e);
//	        response.put("status", "no");
//	        response.put("message", "Internal error");
//	    }
//
//	    logger.info("EXECUTED METHOD :: UpdateTaxMastersNew");
//	    return response;
//	}

}
