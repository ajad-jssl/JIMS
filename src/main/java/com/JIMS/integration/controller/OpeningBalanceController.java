package com.JIMS.integration.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.AdvancePackingNote;
import com.JIMS.integration.interfaces.ContractListFromContractInterfaces;
import com.JIMS.integration.interfaces.OpeningBalanceInterfaces;
import com.JIMS.integration.repository.AdvancePackingNoteRepository;
import com.JIMS.integration.repository.OpeningBalanceRepository;



@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class OpeningBalanceController {

	Logger logger = LogManager.getLogger(OpeningBalanceController.class);

	@Autowired
	public OpeningBalanceRepository openingBalanceRepository;

	@Autowired
	public AdvancePackingNoteRepository advancePackingNoteRepository;

	
	@Transactional(transactionManager = "integrationTransactionManager",
            rollbackFor = Exception.class)
	@PostMapping("/openingbalance/addnew")
	public @ResponseBody Map<String, Object> createOpeningBalanceMasterNew(@RequestParam String con_id,
			@RequestParam String created_by, @RequestParam String description, @RequestParam String total,
			@RequestParam String tax_total, @RequestParam String net_total,
			@RequestParam(required = false) String nontax_avl_bal, @RequestParam(required = false) String utr_reference,
			@RequestParam(required = false) String proforma_reference,
			@RequestParam(required = false) String txn_date) {
		logger.info("EXECUTING METHOD :: createOpeningBalanceMasterNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			LocalDate parsedDate = null;
			if (txn_date != null && !txn_date.isEmpty()) {
			    parsedDate = LocalDate.parse(txn_date);
			}

			
			created_by = created_by.toUpperCase();
			description = description.toUpperCase();
			AdvancePackingNote advancePackingNote = new AdvancePackingNote();
			advancePackingNote.setConId(con_id);
			advancePackingNote.setCreatedBy(created_by);
			advancePackingNote.setCreatedDate(LocalDateTime.now());
			/* advancePackingNote.setFactory_id(factory_id); */
			logger.info("EXECUTING METHOD :: BEFORE adding OPENING BALANCE");
			AdvancePackingNote advancePackingNote1 = advancePackingNoteRepository.save(advancePackingNote);
			logger.info("EXECUTING METHOD :: AFTER adding OPENING BALANCE ");
			int pn_id = advancePackingNote1.getPnId();
			logger.info("EXECUTING METHOD :: BEFORE adding  OPENING BALANCE ITEM");
//			openingBalanceRepository.createOpeningBalanceItemRecord(pn_id, description, total, tax_total, net_total,
//					created_by, nontax_avl_bal);
			
			
			
			List<Object[]> latestList = openingBalanceRepository.getLatestBalance(con_id);

			double newTotal;
			double newTax;
			double newNonTax;
			double newAvl;

			// incoming
			double inputTotal = Double.parseDouble(total);
			double inputTax = Double.parseDouble(tax_total);
			double inputNonTax = (nontax_avl_bal != null && !nontax_avl_bal.isEmpty())
			        ? Double.parseDouble(nontax_avl_bal)
			        : 0;

			// CASE 1: previous exists → cumulative
			if (!latestList.isEmpty()) {

			    Object[] latest = latestList.get(0);

			    double prevTotal = latest[0] != null ? Double.parseDouble(latest[0].toString()) : 0;
			    double prevTax = latest[1] != null ? Double.parseDouble(latest[1].toString()) : 0;
			    double prevAvl = latest[2] != null ? Double.parseDouble(latest[2].toString()) : 0;
			    double prevNonTax = latest[3] != null ? Double.parseDouble(latest[3].toString()) : 0;
			    
			    Integer pnID = latest[4] != null
			            ? Integer.parseInt(latest[4].toString())
			            : null;
			    
			    if(pnID != null) {
			    	
			    	openingBalanceRepository.updateLatestFlag(pnID);
			    	logger.info("the pnId is successfully updated...!");
			    	
			    }

			    newTotal = prevTotal + inputTotal;
			    newTax =  inputTax;
			    newNonTax = inputNonTax;
			    newAvl = prevAvl + inputTotal;
			}
			// CASE 2: first record
			else {
			    newTotal = inputTotal;
			    newTax = inputTax;
			    newNonTax = inputNonTax;
			    newAvl = inputTotal;
			}
			
		


			openingBalanceRepository.createOpeningBalanceItemRecord(
			        pn_id,
			        description,
			        String.valueOf(newTotal),
			        String.valueOf(newTax),
			        net_total,
			        created_by,
			        String.valueOf(newAvl),
			        String.valueOf(newNonTax),
			        utr_reference,
			        proforma_reference,
			        parsedDate
			);

			logger.info("EXECUTING METHOD :: AFTER adding  OPENING BALANCE ITEM");
			response.put("message", (advancePackingNote1 != null) ? "Success" : "failure");
			response.put("status", (advancePackingNote1 != null) ? "yes" : "no");
			response.put("action", "Insert_Record_In_OPeningBalanceMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createOpeningBalanceMasterNew ::   -> " + e.getMessage());

			response.put("message", "Error occurred: " + e.getMessage());
			response.put("status", "no");
			response.put("action", "Insert_Record_In_OPeningBalanceMASTER");
			return response;
		}
		logger.info("EXECUTED METHOD :: createOpeningBalanceMasterNew");
		return response;
	}

	@PostMapping("/openingbalance/updatenew")
	public @ResponseBody Map<String, Object> updateOpeningBalanceMasternew(@RequestParam String con_id,
			@RequestParam String modified_by, @RequestParam String description, @RequestParam String slno,
			@RequestParam String total, @RequestParam String tax_total, @RequestParam String net_total,
			@RequestParam String pn_id) {
		logger.info("EXECUTING METHOD :: updateOpeningBalanceMasternew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			modified_by = modified_by.toUpperCase();
			description = description.toUpperCase();
			logger.info("EXECUTING METHOD :: BEFORE UPDATING  OPENING BALANCE ");
			int count = openingBalanceRepository.updateOpeningBalanceRecord(con_id, modified_by, pn_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING  OPENING BALANCE ");
			logger.info("EXECUTING METHOD :: BEFORE UPDATING  OPENING BALANCE ITEM");
			openingBalanceRepository.updateOpeningBalanceItemRecord(pn_id, description, total, tax_total, net_total,
					modified_by, slno);
			logger.info("EXECUTING METHOD :: AFTER UPDATING  OPENING BALANCE ITEM");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_OPeningBalanceMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR  updateOpeningBalanceMasternew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateOpeningBalanceMasternew ");
		return response;
	}

	@GetMapping("/listopeningbalancenew")
	public @ResponseBody Map<String, Object> listOpeningBalanceNew() {
		logger.info("EXECUTING METHOD :: listOpeningBalanceNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<OpeningBalanceInterfaces> openingBalanceInterfaces = null;
		try {
			logger.info("EXECUTING METHOD :: BEFORE LISTING  OPENING BALANCE ");
			openingBalanceInterfaces = openingBalanceRepository.getOpeningBalanceList();
			logger.info("EXECUTING METHOD :: AFTER LISTING  OPENING BALANCE ");
			response.put("message", (openingBalanceInterfaces != null) ? "Success" : "failure");
			response.put("status", (openingBalanceInterfaces != null) ? "yes" : "no");
			response.put("Data", openingBalanceInterfaces);
			response.put("action", "List_Record_In_OpeningBalanceMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR  listOpeningBalanceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listOpeningBalanceNew ");
		return response;
	}

	@GetMapping("/searchopeningbalancenew")
	public @ResponseBody Map<String, Object> searchorganziationIdNew(@RequestParam String slno) {
		logger.info("EXECUTING METHOD :: searchorganziationIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		OpeningBalanceInterfaces openingBalanceInterfaces = null;
		try {
			logger.info("EXECUTING METHOD :: BEFORE SEARCHING  OPENING BALANCE ");
			openingBalanceInterfaces = openingBalanceRepository.searchOpeningBalanceById(slno);
			logger.info("EXECUTING METHOD :: AFTER SEARCHING  OPENING BALANCE ");
			response.put("message", (openingBalanceInterfaces != null) ? "Success" : "failure");
			response.put("status", (openingBalanceInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_OPeningBalanceMASTER");
			response.put("DATA", openingBalanceInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR  searchorganziationIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: searchorganziationIdNew ");
		return response;
	}

	@GetMapping("/listcontractorfromopeningbalancenew")
	public @ResponseBody Map<String, Object> listOpeningBalanceInfoNew(@RequestParam String factory_id) {
		logger.info("EXECUTING METHOD :: listOpeningBalanceInfoNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<ContractListFromContractInterfaces> openingBalanceInterfaces = null;
		try {
			logger.info("EXECUTING METHOD :: BEFORE LIST CONTRACTOR  OPENING BALANCE ");
			openingBalanceInterfaces = openingBalanceRepository.getContractListFromContractInfo();
			logger.info("EXECUTING METHOD :: AFTER LIST CONTRACTOR  OPENING BALANCE ");
			response.put("message", (openingBalanceInterfaces != null) ? "Success" : "failure");
			response.put("status", (openingBalanceInterfaces != null) ? "yes" : "no");
			response.put("Data", openingBalanceInterfaces);
			response.put("action", "List_Record_In_OPeningBalanceCONTRACTORMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR  listOpeningBalanceInfoNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listOpeningBalanceInfoNew ");
		return response;
	}

	@GetMapping("/getOpeningBalanceByPnId")
	public @ResponseBody Map<String, Object> getOpeningBalanceByPnId(@RequestParam("pn_id") String pnId) {
		logger.info("EXECUTING METHOD :: getOpeningBalanceByPnId with pn_id = {}", pnId);

		Map<String, Object> response = new HashMap<>();

		try {
			// Call repository method to fetch opening balance details
			OpeningBalanceInterfaces openingBalanceDetails = openingBalanceRepository.getOpeningBalanceById(pnId);

			response.put("message", (openingBalanceDetails != null) ? "Success" : "Failure");
			response.put("status", (openingBalanceDetails != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_OpeningBalance");
			response.put("DATA", openingBalanceDetails);

			logger.info("EXECUTED METHOD :: getOpeningBalanceByPnId Successfully");
		} catch (Exception e) {
			logger.error("ERROR IN METHOD getOpeningBalanceByPnId :: {}", e.getMessage());
			response.put("message", "Error: " + e.getMessage());
			response.put("status", "no");
			response.put("action", "Search_Record_In_OpeningBalance");
		}

		return response;
	}

//	@PostMapping("/openingbalance/updateItem")
//	public @ResponseBody Map<String, Object> updateOpeningBalanceItem(@RequestParam String pn_id,
//			@RequestParam String total, @RequestParam String tax_total, @RequestParam String nontax_avl_bal,
//			@RequestParam String avl_bal, @RequestParam String modified_by, @RequestParam String contract_name) {
//		Map<String, Object> response = new HashMap<>();
//
//		try {
//			Map<String, Object> taxinfo = openingBalanceRepository.getContractDetails(contract_name);
//
//			String taxable = String.valueOf(taxinfo.get("taxable")); // Yes / No
//			String nonTaxable = String.valueOf(taxinfo.get("non_taxable")); // Yes / No
//
//			// --- Convert numeric values for validation ---
//			double availableBalance = Double.parseDouble(avl_bal);
//			double taxValue = Double.parseDouble(tax_total);
//			double nonTaxValue = Double.parseDouble(nontax_avl_bal);
//
//			// --- Validation Logic ---
//
//			// Case 1: Only taxable editable
////			if (taxable.equalsIgnoreCase("Yes") && nonTaxable.equalsIgnoreCase("No")) {
////				if (nonTaxValue != 0) {
////					response.put("status", "no");
////					response.put("message", "Non-taxable balance cannot be updated for taxable contract!");
////					return response;
////				}
////				if (taxValue > availableBalance) {
////					response.put("status", "no");
////					response.put("message", "Taxable amount cannot exceed available opening balance!");
////					return response;
////				}
////			}
////
////			// Case 2: Only non-taxable editable
////			if (nonTaxable.equalsIgnoreCase("Yes") && taxable.equalsIgnoreCase("No")) {
////				if (taxValue != 0) {
////					response.put("status", "no");
////					response.put("message", "Taxable value cannot be updated for non-taxable contract!");
////					return response;
////				}
////				if (nonTaxValue > availableBalance) {
////					response.put("status", "no");
////					response.put("message", "Non-taxable amount cannot exceed available opening balance!");
////					return response;
////				}
////			}
////
////			// Case 3: Both taxable and non-taxable editable (Yes/Yes)
////			if (taxable.equalsIgnoreCase("Yes") && nonTaxable.equalsIgnoreCase("Yes")) {
////				if (taxValue > availableBalance) {
////					response.put("status", "no");
////					response.put("message", "Taxable amount cannot exceed available opening balance!");
////					return response;
////				}
////				if (nonTaxValue > availableBalance) {
////					response.put("status", "no");
////					response.put("message", "Non-taxable amount cannot exceed available opening balance!");
////					return response;
////				}
////			}
//
//			// -------- UPDATE TABLE 1 --------
//			int updateItem = openingBalanceRepository.updateOpeningBalanceItem(total, tax_total, avl_bal,
//					nontax_avl_bal, pn_id);
//
//			// -------- UPDATE TABLE 2 --------
//			int updateMain = openingBalanceRepository.updateOpeningBalanceMain(modified_by, pn_id);
//
//			if (updateItem > 0 && updateMain > 0) {
//				response.put("message", "Success");
//				response.put("status", "yes");
//			} else {
//				response.put("message", "No record found to update!");
//				response.put("status", "no");
//			}
//
//		} catch (Exception e) {
//
//			/* ✅ HANDLE TRIGGER MESSAGE */
//			if (e.getMessage() != null && e.getMessage().contains("No fields were changed")) {
//				response.put("status", "nochange");
//				response.put("message", "No fields were changed.");
//			} else {
//				response.put("status", "no");
//				response.put("message", "Failed to update opening balance.");
//			}
//		}
//		return response;
//	}
	
	
	
	
	
	
	
	
//	 Newly Added the Update Controller for The Opening Balance
	
	
	

	@PostMapping("/openingbalance/updateItem")
	public @ResponseBody Map<String, Object> updateOpeningBalanceItem(
	        @RequestParam String pn_id,
	        @RequestParam String total,
	        @RequestParam String tax_total,
	        @RequestParam String nontax_avl_bal,
	        @RequestParam String avl_bal,
	        @RequestParam String modified_by,
	        @RequestParam String contract_name,
	        @RequestParam(required = false) String utr_reference,
	        @RequestParam(required = false) String proforma_reference,
	        @RequestParam(required = false) String txn_date,
	        @RequestParam(required = false) String description
	) {

		Map<String, Object> response = new HashMap<>();

		try {
			
			logger.info("Checking for the Old Opening Balance");
			
			int checkOld = openingBalanceRepository.countforOldopeningBalance(pn_id);
			
			logger.info("try to check the Old value and checkold value",+checkOld);
			
			if(checkOld >0) {
				  response.put("message", "You cannot modify an old opening balance. Please update the most recent opening balance instead.");
				    response.put("status", "No");
				    return response;
			}
			
			
			
			
			
			logger.info("this is  checking for element that are already used");
			
			
			//int checkTranscationUsed= openingBalanceRepository.countUnusedOpeningBalance(contract_name);
			
			int checkTranscationUsed = openingBalanceRepository
				    .countUnusedOpeningBalance(contract_name, pn_id);
			logger.info("See  the checkTrancscation value"+checkTranscationUsed);
			if (checkTranscationUsed > 0) {
			    logger.info("Opening balance already used, blocking update");

			    response.put("message", "The Transcation is Already Exists for Opening Balance");
			    response.put("status", "No");
			    return response;
			}
			LocalDate parsedDate = null;
			if (txn_date != null && !txn_date.isEmpty()) {
			    parsedDate = LocalDate.parse(txn_date);
			}

			
			
			
			List<Object[]> latestList = openingBalanceRepository.getLatestBalancess(contract_name,pn_id);

			double newTotal;
			double newTax;
			double newNonTax;
			double newAvl;

			// incoming
			double inputTotal = Double.parseDouble(total);
			double inputTax = Double.parseDouble(tax_total);
			double inputNonTax = (nontax_avl_bal != null && !nontax_avl_bal.isEmpty())
			        ? Double.parseDouble(nontax_avl_bal)
			        : 0;

			// CASE 1: previous exists → cumulative
			if (!latestList.isEmpty()) {

			    Object[] latest = latestList.get(0);

			    double prevTotal = latest[0] != null ? Double.parseDouble(latest[0].toString()) : 0;
			    double prevTax = latest[1] != null ? Double.parseDouble(latest[1].toString()) : 0;
			    double prevAvl = latest[2] != null ? Double.parseDouble(latest[2].toString()) : 0;
			    double prevNonTax = latest[3] != null ? Double.parseDouble(latest[3].toString()) : 0;
			    
			    Integer pnID = latest[4] != null
			            ? Integer.parseInt(latest[4].toString())
			            : null;
			    
			    if(pnID != null) {
			    	
			    	openingBalanceRepository.updateLatestFlag(pnID);
			    	logger.info("the pnId is successfully updated...!");
			    	
			    }

			    newTotal = prevTotal + inputTotal;
			    newTax =  inputTax;
			    newNonTax = inputNonTax;
			    newAvl = prevAvl + inputTotal;
			}
			// CASE 2: first record
			else {
			    newTotal = inputTotal;
			    newTax = inputTax;
			    newNonTax = inputNonTax;
			    newAvl = inputTotal;
			}
			
		
			

			// -------- UPDATE TABLE 1 --------
			int updateItem = openingBalanceRepository.updateOpeningBalanceItem(String.valueOf(newTotal), String.valueOf(newTax), String.valueOf(newAvl),
					String.valueOf(newNonTax),utr_reference,proforma_reference,parsedDate,description,modified_by,pn_id);

			// -------- UPDATE TABLE 2 --------
			int updateMain = openingBalanceRepository.updateOpeningBalanceMain(modified_by, pn_id);

			if (updateItem > 0 && updateMain > 0) {
				response.put("message", "Success");
				response.put("status", "yes");
			} else {
				response.put("message", "No record found to update!");
				response.put("status", "no");
			}

		} catch (Exception e) {

			/*  HANDLE TRIGGER MESSAGE */
			if (e.getMessage() != null && e.getMessage().contains("No fields were changed")) {
				response.put("status", "nochange");
				response.put("message", "No fields were changed.");
			} else {
				response.put("status", "no");
				response.put("message", "Failed to update opening balance.");
			}
		}
		return response;
	}

	@GetMapping("/checkTaxType")
	public @ResponseBody Map<String, Object> getContractTaxType(@RequestParam String contract_name) {

		Map<String, Object> response = new HashMap<>();

		logger.info("the contract name:" + contract_name);

		try {
			Map<String, Object> data = openingBalanceRepository.getContractDetails(contract_name);

			if (data == null) {
				response.put("status", "no");
				response.put("message", "Contract not found");
				return response;
			}

			response.put("status", "yes");
			response.put("taxable", data.get("taxable"));
			response.put("non_taxable", data.get("non_taxable"));

		} catch (Exception e) {
			response.put("status", "no");
			response.put("message", e.getMessage());
		}

		return response;
	}

	@GetMapping("/openingbalance/availableContracts")
	public @ResponseBody List<Map<String, Object>> getAvailableContracts() {
		return openingBalanceRepository.findAvailableContracts();
	}
}

