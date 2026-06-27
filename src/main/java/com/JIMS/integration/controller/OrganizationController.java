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

import com.JIMS.integration.interfaces.BankMasterInterface;
import com.JIMS.integration.interfaces.BusinessUnitInterface;
import com.JIMS.integration.interfaces.OrganizationMasterInterface;
import com.JIMS.integration.repository.OrganizationRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class OrganizationController {

	Logger logger = LogManager.getLogger(OrganizationController.class);

	@Autowired
	private OrganizationRepository organizationRepository;

	/** ORGANIZATION START **/

	@PostMapping("/organization/addorganization")
	public @ResponseBody Map<String, Object> createOrganizationNew(@RequestParam String org_name,
			@RequestParam String registered_address, @RequestParam String business_address,
			@RequestParam String gst_number, @RequestParam String location, @RequestParam int state_id,
			@RequestParam String created_by, @RequestParam(required=false) String factory_id) {

		logger.info("EXECUTING METHOD :: createOrganization");

		org_name = org_name.toUpperCase();
		registered_address = registered_address.toUpperCase();
		business_address = business_address.toUpperCase();
		gst_number = gst_number.toUpperCase();
		location = location.toUpperCase();
		created_by = created_by.toUpperCase();

		Map<String, Object> response = new HashMap<String, Object>();
		try {

			int checkOrganizationCountIfExists = organizationRepository.checkWhetherOrganizationExistsOrNot(org_name);

			if (checkOrganizationCountIfExists > 0) {
				response.put("action", "AddOrganisation");
				response.put("message", "Organization  Already Exists");
				response.put("status", "no");

				return response;

			} else {

				int count = organizationRepository.addOrganizationNew(org_name, registered_address, business_address,
						gst_number, location, created_by, state_id, factory_id);

				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "AddOrganisation");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createOrganization  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/organization/updateorganization")
	public @ResponseBody Map<String, Object> updateOrganizationNew(@RequestParam String org_name,
			@RequestParam String registered_address, @RequestParam String business_address,
			@RequestParam String gst_number, @RequestParam String location, @RequestParam int state_id,
			@RequestParam String modified_by, @RequestParam(required=false) String factory_id, @RequestParam String org_id) {

		logger.info("EXECUTING METHOD :: updateOrganization");

		org_name = org_name.toUpperCase();
		registered_address = registered_address.toUpperCase();
		business_address = business_address.toUpperCase();
		location = location.toUpperCase();
		modified_by = modified_by.toUpperCase();

		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		try {

			String message = organizationRepository.checkWhetherOrganizationIsinTransactions(org_id);

			if (message != null) {
				response.put("message", message);
				response.put("status", "no");
				response.put("action", "Update_Record_In_OrganizationMaster");

				return response;
			}
			int checkOrganizationCountIfExists = organizationRepository.checkWhetherOrganizationExistsOrNot(org_name);

			if (checkOrganizationCountIfExists > 0) {
				response.put("action", "AddOrganisation");
				response.put("message", "Organization  Already Exists");
				response.put("status", "no");

				return response;
			}
			
			else {

				count = organizationRepository.updateOrganizationNew(org_name, registered_address, business_address,
						gst_number, location, modified_by, state_id, org_id);

				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_OrganizationMaster");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateOrganization  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/organization/deleteorganization")
	public @ResponseBody Map<String, Object> deleteOrganizationNew(@RequestParam String modified_by,
			@RequestParam String org_id) {

		logger.info("EXECUTING METHOD :: deleteOrganizationNew");

		Map<String, Object> deleteOrganizationmap = new HashMap<String, Object>();
		try {

			String message = organizationRepository.checkWhetherOrganizationIsinTransactions(org_id);

			if (message != null) {
				deleteOrganizationmap.put("message", message);
				deleteOrganizationmap.put("status", "no");
				deleteOrganizationmap.put("action", "DeleteOrganization");

				return deleteOrganizationmap;
			} else {
				int count = organizationRepository.deleteOrganizationNew(modified_by, org_id);
				deleteOrganizationmap.put("message", (count > 0) ? "Success" : "failure");
				deleteOrganizationmap.put("status", (count > 0) ? "yes" : "no");
				deleteOrganizationmap.put("action", "DeleteOrganization");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteOrganizationNew  -> " + e.getMessage());

		}
		return deleteOrganizationmap;
	}

	@GetMapping("/organization/listorganization")
	public @ResponseBody Map<String, Object> listOrganizationNew(@RequestParam(required=false) String factory_id) {

		logger.info("EXECUTING METHOD :: listOrganizationNew");

		Map<String, Object> listOrganizationmap = new HashMap<String, Object>();
		List<OrganizationMasterInterface> organizationMasterlist = null;
		try {
			organizationMasterlist = organizationRepository.getOrganizationListNew(factory_id);
			listOrganizationmap.put("message",
					(organizationMasterlist.size() > 0) ? "Success" : "Organization List not available");
			listOrganizationmap.put("status", (organizationMasterlist.size() > 0) ? "yes" : "no");
			listOrganizationmap.put("Data", organizationMasterlist);
			listOrganizationmap.put("action", "AllOrganizationMasterDetails");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: listOrganizationNew  -> " + e.getMessage());

		}
		return listOrganizationmap;
	}

	@GetMapping("/organization/searchorganziation")
	public @ResponseBody Map<String, Object> searchorganziationIdNew(@RequestParam String org_id) {

		logger.info("EXECUTING METHOD :: searchorganziationId");

		Map<String, Object> getOrganizationByIdmap = new HashMap<String, Object>();
		OrganizationMasterInterface organizationMaster = null;
		try {
			organizationMaster = organizationRepository.searchOrganizationByIdNew(org_id);
			getOrganizationByIdmap.put("message", (organizationMaster != null) ? "Success" : "failure");
			getOrganizationByIdmap.put("status", (organizationMaster != null) ? "yes" : "no");
			getOrganizationByIdmap.put("action", "OrganizationInfoBasedOnId");
			getOrganizationByIdmap.put("DATA", organizationMaster);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: searchorganziationId  -> " + e.getMessage());

		}
		return getOrganizationByIdmap;
	}

	/** ORGANIZATION END **/
	/** BANK START **/
	@PostMapping("/bank/addbankaccount")
	public @ResponseBody Map<String, Object> createBankAccountNew(@RequestParam String business_unit_id,
			@RequestParam String bank_name, @RequestParam String account_number, @RequestParam String ifsc_code,
			@RequestParam String branch_address, @RequestParam String state_id, @RequestParam String country_id,
			@RequestParam String city, @RequestParam String swift_code, @RequestParam String branch_code,
			@RequestParam String created_by, @RequestParam(required = false) String factory_id) {

		logger.info("EXECUTING METHOD :: createBankAccountNew");

		bank_name = bank_name.toUpperCase();
		account_number = account_number.toUpperCase();
		ifsc_code = ifsc_code.toUpperCase();
		branch_address = branch_address.toUpperCase();
		city = city.toUpperCase();
		swift_code = swift_code.toUpperCase();
		branch_code = branch_code.toUpperCase();
		created_by = created_by.toUpperCase();

		Map<String, Object> addBankAccountmap = new HashMap<String, Object>();
		try {

			int checkBankCountIfExists = organizationRepository.checkWhetherBankAccountExistsOrNot(account_number);

			if (checkBankCountIfExists > 0) {
				addBankAccountmap.put("action", "AddBankAccount");
				addBankAccountmap.put("message", "Bank Already Exists");
				addBankAccountmap.put("status", "no");

				return addBankAccountmap;

			} else {
				int addBankAccountrecord = organizationRepository.addBankAccount(business_unit_id, bank_name,
						account_number, ifsc_code, branch_address, state_id, country_id, city, swift_code, branch_code,
						created_by, factory_id);

				addBankAccountmap.put("message", (addBankAccountrecord > 0) ? "Success" : "failure");
				addBankAccountmap.put("status", (addBankAccountrecord > 0) ? "yes" : "no");
				addBankAccountmap.put("action", "AddBankAccount");

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createBankAccountNew  -> " + e.getMessage());

		}
		return addBankAccountmap;
	}

	@PostMapping("/bank/updatebankaccount")
	public @ResponseBody Map<String, Object> updateBankAccountNew(@RequestParam String business_unit_id,
			@RequestParam String bank_name, @RequestParam String account_number, @RequestParam String ifsc_code,
			@RequestParam String branch_address, @RequestParam String state_id, @RequestParam String country_id,
			@RequestParam String city, @RequestParam String swift_code, @RequestParam String branch_code,
			@RequestParam String modified_by, @RequestParam String account_id, @RequestParam(required=false) String factory_id,@RequestParam boolean isAccountNumberChanged) {

		logger.info("EXECUTING METHOD :: updateBankAccountNew");

		bank_name = bank_name.toUpperCase();
		// account_number = account_number.toUpperCase();
		ifsc_code = ifsc_code.toUpperCase();
		branch_address = branch_address.toUpperCase();
		city = city.toUpperCase();
		swift_code = swift_code.toUpperCase();
		branch_code = branch_code.toUpperCase();
		modified_by = modified_by.toUpperCase();

		Map<String, Object> updateBankAccountmap = new HashMap<String, Object>();
		int count = 0;
		try {
			
			if (isAccountNumberChanged) {
				int account_count = organizationRepository
						.checkWhetherBankAccountExistsOrNot(account_number);

				if (account_count > 0) {
					updateBankAccountmap.put("status", "no");
					updateBankAccountmap.put("message", "Account number already exists");
					return updateBankAccountmap;
				}
			}

			String message = organizationRepository.checkWhetherBankAccountInvovedInTransactions(account_id);

			if (message != null) {
				updateBankAccountmap.put("action", "UpdateBankMaster");
				updateBankAccountmap.put("message", message);
				updateBankAccountmap.put("status", "no");

				return updateBankAccountmap;
			} else {

				count = organizationRepository.updateBankAccount(business_unit_id, bank_name, account_number, ifsc_code,
						branch_address, state_id, country_id, city, swift_code, branch_code, modified_by, account_id);

				updateBankAccountmap.put("message", (count > 0) ? "Success" : "failure");
				updateBankAccountmap.put("status", (count > 0) ? "yes" : "no");
				updateBankAccountmap.put("action", "UpdateBankMaster");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateBankAccountNew  -> " + e.getMessage());

		}
		return updateBankAccountmap;
	}

	@PostMapping("/bank/deletebankaccount")
	public @ResponseBody Map<String, Object> deleteBankAccountNew(@RequestParam String modified_by,
			@RequestParam String account_id) {

		logger.info("EXECUTING METHOD :: deleteBankAccount");

		Map<String, Object> deleteBankAccountmap = new HashMap<String, Object>();
		try {

			String message = organizationRepository.checkWhetherBankAccountInvovedInTransactions(account_id);

			if (message != null) {
				deleteBankAccountmap.put("action", "DeleteBankAccount");
				deleteBankAccountmap.put("message", message);
				deleteBankAccountmap.put("status", "no");

				return deleteBankAccountmap;
			} else {
				organizationRepository.updateBankAccountHistory(modified_by, account_id);

				int count = organizationRepository.deleteBankAccount(modified_by, account_id);

				deleteBankAccountmap.put("message", (count > 0) ? "Success" : "failure");
				deleteBankAccountmap.put("status", (count > 0) ? "yes" : "no");
				deleteBankAccountmap.put("action", "DeleteBankAccount");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteBankAccount  -> " + e.getMessage());

		}
		return deleteBankAccountmap;
	}

	@GetMapping("/bank/listbankaccount")
	public @ResponseBody Map<String, Object> listBankAccountNew(@RequestParam(required = false) String factory_id) {

		logger.info("EXECUTING METHOD :: listBankAccount");

		Map<String, Object> allBankListmap = new HashMap<String, Object>();
		List<BankMasterInterface> bankMasterInterfaces = null;
		try {
			bankMasterInterfaces = organizationRepository.getBankAccountList(factory_id);
			allBankListmap.put("message", (bankMasterInterfaces.size() > 0) ? "Success" : "failure");
			allBankListmap.put("status", (bankMasterInterfaces.size() > 0) ? "yes" : "no");
			allBankListmap.put("Data", bankMasterInterfaces);
			allBankListmap.put("action", "BankAccountList");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: listBankAccount  -> " + e.getMessage());

		}
		return allBankListmap;
	}

	@GetMapping("/bank/searchbankaccount")
	public @ResponseBody Map<String, Object> searchBankAccountIdNew(@RequestParam String account_id) {

		logger.info("EXECUTING METHOD :: searchBankAccountId");

		Map<String, Object> bankAccountByidmap = new HashMap<String, Object>();
		BankMasterInterface bankMasterInterface = null;
		try {
			bankMasterInterface = organizationRepository.searchBankAccountById(account_id);
			bankAccountByidmap.put("message", (bankMasterInterface != null) ? "Success" : "failure");
			bankAccountByidmap.put("status", (bankMasterInterface != null) ? "yes" : "no");
			bankAccountByidmap.put("action", "Search_Record_In_BankMASTER");
			bankAccountByidmap.put("DATA", bankMasterInterface);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: searchBankAccountId  -> " + e.getMessage());

		}
		return bankAccountByidmap;
	}

	/** BANK END **/

	/** BUSINESS UNIT START **/
	@PostMapping("/businessunits/add")
	private @ResponseBody Map<String, Object> createBussinessUnits(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: createBussinessUnits");
		Map<String, Object> addBusinessUnitsMap = new HashMap<String, Object>();
		int value = 1100;
		try {
			int count = organizationRepository.getCount();
			String org_id = val.get("org_id");
			String business_unit_name = val.get("business_unit_name");
			String gst_number = val.get("gst_number");
			String location = val.get("location");
			int state_id = Integer.parseInt(val.get("state_id"));
			String created_by = val.get("created_by");
			String panNumber = val.get("panNumber");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
			value = value + count;
			int unitscount = organizationRepository.addBusinessUnits(org_id, business_unit_name, gst_number, location,
					state_id, created_by, value, factory_id,panNumber);
			addBusinessUnitsMap.put("action", "AddBusinessUnits");
			addBusinessUnitsMap.put("message", (unitscount > 0) ? "Success" : "Business units details not added");
			addBusinessUnitsMap.put("status", (unitscount > 0) ? "yes" : "no");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createBussinessUnits  -> " + e.getMessage());
			addBusinessUnitsMap.put("message", e.getMessage());
			addBusinessUnitsMap.put("status", "no");
		}
		return addBusinessUnitsMap;
	}

	@PostMapping("/businessunits/addBusinessUnit")
	private @ResponseBody Map<String, Object> createBussinessUnits(@RequestParam String org_id,
			@RequestParam String business_unit_name, @RequestParam String gst_number, @RequestParam String location,
			@RequestParam int state_id, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id,@RequestParam String panNumber) {

		business_unit_name = business_unit_name.toUpperCase();
		location = location.toUpperCase();
		created_by = created_by.toUpperCase();

		logger.info("EXECUTING METHOD :: createBussinessUnits");
		Map<String, Object> addBusinessUnitsNewMap = new HashMap<String, Object>();
		int value = 1100;
		try {
			int count = organizationRepository.getCount();
			value = value + count;
			int checkBusinessCount = organizationRepository.checkWhetherBusinessUnitExists(gst_number);
			if (checkBusinessCount > 0) {
				addBusinessUnitsNewMap.put("action", "AddBusinessUnitsNew");
				addBusinessUnitsNewMap.put("message", " Business units Already Exists");
				addBusinessUnitsNewMap.put("status", "no");
			} else {
				int unitscount = organizationRepository.addBusinessUnits(org_id, business_unit_name, gst_number,
						location, state_id, created_by, value, factory_id,panNumber);
				addBusinessUnitsNewMap.put("action", "AddBusinessUnitsNew");
				addBusinessUnitsNewMap.put("message",
						(unitscount > 0) ? "Success" : "Business units details not added");
				addBusinessUnitsNewMap.put("status", (unitscount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createBussinessUnits  -> " + e.getMessage());
			addBusinessUnitsNewMap.put("message", e.getMessage());
			addBusinessUnitsNewMap.put("status", "no");
		}
		return addBusinessUnitsNewMap;
	}

//	@PostMapping("/businessunits/update")
//	public @ResponseBody Map<String, Object> updateBusinessUnit(@RequestBody Map<String, String> val) {
//
//		logger.info("EXECUTING METHOD :: updateBusinessUnit");
//
//		Map<String, Object> response = new HashMap<>();
//		String gst_number = null;
//		int count = 0;
//		int check = 0;
//		try {
//			String business_unit_id = val.get("business_unit_id");
//			String org_id = val.get("org_id");
//			String business_unit_name = val.get("business_unit_name");
//			gst_number = val.get("gst_number");
//			String location = val.get("location");
//			int state_id = Integer.parseInt(val.get("state_id"));
//			String modified_by = val.get("modified_by");
//			check = organizationRepository.checkBusinessIdPresentInContractMaster(business_unit_id);
//			if (check == 1) {
//				response.put("message", "Invoice Already Generated Not Able to Update");
//				return response;
//			}
//			if (check > 1) {
//				response.put("message", "Please Create New Master and Assign for that contract");
//				return response;
//			}
//			// organizationRepository.insertRecordBusinessUnitHistory(business_unit_id,
//			// modified_by);
//			if (gst_number != null) {
//				count = organizationRepository.updatebusinessUnits(org_id, business_unit_name, gst_number, location,
//						state_id, modified_by, business_unit_id);
//			} else {
//				count = organizationRepository.updatebusinessUnitswithoutGstNumberColumn(org_id, business_unit_name,
//						location, state_id, modified_by, business_unit_id);
//			}
//
//			response.put("message", (count > 0) ? "Success" : "failure");
//			response.put("status", (count > 0) ? "yes" : "no");
//			response.put("action", "UPDATE_Record_In_BankMASTER");
//		} catch (Exception e) {
//			logger.error("ERROR IN THE METHOD :: updateBusinessUnit  -> " + e.getMessage());
//
//			response.put("Message", e.getMessage());
//
//		}
//		return response;
//	}

	@SuppressWarnings("unused")
	@PostMapping("/businessunits/updateBusinessUnit")
	public @ResponseBody Map<String, Object> updateBusinessUnitNew(@RequestParam String org_id,
			@RequestParam String business_unit_name, @RequestParam(required = false) String gst_number,
			@RequestParam String location, @RequestParam int state_id, @RequestParam String modified_by,
			@RequestParam String business_unit_id,@RequestParam Boolean gstChanged,@RequestParam String panNumberUp ) {

		logger.info("EXECUTING METHOD :: updateBusinessUnit");

		business_unit_name = business_unit_name.toUpperCase();
		location = location.toUpperCase();
		modified_by = modified_by.toUpperCase();

		Map<String, Object> updateBusinessUnitmap = new HashMap<>();
		int count = 0;
		try {

		
			String factory_id = organizationRepository.getFactory_id(business_unit_id);
			if(gstChanged) {
			
			int checkBusinessCount = organizationRepository.checkWhetherBusinessUnitExists(gst_number);
			if (checkBusinessCount > 0) {
				updateBusinessUnitmap.put("action", "AddBusinessUnitsNew");
				updateBusinessUnitmap.put("message", " Business units is alread exists");
				updateBusinessUnitmap.put("status", "no");
				return updateBusinessUnitmap;
			}
			}
			

			String message = organizationRepository.checkWhetherBusinessIsInTransactionsBeforeUpdate(business_unit_id);

			if (message != null) {
				updateBusinessUnitmap.put("message", message);
				updateBusinessUnitmap.put("status", "no");
				updateBusinessUnitmap.put("action", "UpdateBusinessUnit");

				return updateBusinessUnitmap;
			} else {
				// organizationRepository.insertRecordBusinessUnitHistory(business_unit_id,
				// modified_by);
				if (gst_number != null) {
					count = organizationRepository.updatebusinessUnits(org_id, business_unit_name, gst_number, location,
							state_id, modified_by, business_unit_id,panNumberUp);
				} else {
					count = organizationRepository.updatebusinessUnitswithoutGstNumberColumn(org_id, business_unit_name,
							location, state_id, modified_by, business_unit_id,panNumberUp);
				}

				updateBusinessUnitmap.put("message", (count > 0) ? "Success" : "failure");
				updateBusinessUnitmap.put("status", (count > 0) ? "yes" : "no");
				updateBusinessUnitmap.put("action", "UpdateBusinessUnit");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateBusinessUnitNew  -> " + e.getMessage());

			updateBusinessUnitmap.put("Message", e.getMessage());

		}
		return updateBusinessUnitmap;
	}

	@GetMapping("/listbusinessunits/{factory_id}")
	private @ResponseBody Map<String, Object> getAllBusinessUnits(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: getAllBusinessUnits");

		Map<String, Object> getAllBusinessUnitsMap = new HashMap<String, Object>();

		try {
			List<BusinessUnitInterface> getAllBusinessUnitslists = organizationRepository
					.getAllBussinessUnits(factory_id);
			getAllBusinessUnitsMap.put("action", "GetAllBusinessUnits");
			getAllBusinessUnitsMap.put("message",
					(getAllBusinessUnitslists.size() > 0) ? "Success" : "No Business units");
			getAllBusinessUnitsMap.put("status", (getAllBusinessUnitslists.size() > 0) ? "yes" : "no");
			getAllBusinessUnitsMap.put("Data", getAllBusinessUnitslists);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllBusinessUnits  -> " + e.getMessage());

		}
		return getAllBusinessUnitsMap;
	}

	@GetMapping("/listbusinessunitsnew")
	private @ResponseBody Map<String, Object> getAllBusinessUnitsNew(
			@RequestParam(required = false) String factory_id) {

		logger.info("EXECUTING METHOD :: getAllBusinessUnits");

		Map<String, Object> getAllBusinessUnitsMap = new HashMap<String, Object>();

		try {
			List<BusinessUnitInterface> getAllBusinessUnitslists = organizationRepository
					.getAllBussinessUnits(factory_id);
			getAllBusinessUnitsMap.put("action", "GetAllBusinessUnits");
			getAllBusinessUnitsMap.put("message",
					(getAllBusinessUnitslists.size() > 0) ? "Success" : "No Business units");
			getAllBusinessUnitsMap.put("status", (getAllBusinessUnitslists.size() > 0) ? "yes" : "no");
			getAllBusinessUnitsMap.put("Data", getAllBusinessUnitslists);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllBusinessUnits  -> " + e.getMessage());

		}
		return getAllBusinessUnitsMap;
	}

	@GetMapping("/searchbusinessunits/{business_unit_id}")
	public @ResponseBody Map<String, Object> findBusinessUnitById(@PathVariable String business_unit_id) {

		logger.info("EXECUTING METHOD :: findBusinessUnitById");

		Map<String, Object> response = new HashMap<>();
		BusinessUnitInterface businessUnitsInterface = null;
		try {
			businessUnitsInterface = organizationRepository.findBusinessUnit(business_unit_id);
			response.put("message", (businessUnitsInterface != null) ? "Success" : "failure");
			response.put("status", (businessUnitsInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_BankMASTER");
			response.put("Data", businessUnitsInterface);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: findBusinessUnitById  -> " + e.getMessage());

		}

		return response;
	}

	@GetMapping("/businessunits/searchbusinessunits")
	public @ResponseBody Map<String, Object> findBusinessUnitByIdNew(@RequestParam String business_unit_id) {

		logger.info("EXECUTING METHOD :: findBusinessUnitById");

		Map<String, Object> response = new HashMap<>();
		BusinessUnitInterface businessUnitsInterface = null;
		try {
			businessUnitsInterface = organizationRepository.findBusinessUnit(business_unit_id);
			response.put("message", (businessUnitsInterface != null) ? "Success" : "failure");
			response.put("status", (businessUnitsInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_BankMASTER");
			response.put("Data", businessUnitsInterface);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: findBusinessUnitById  -> " + e.getMessage());

		}

		return response;
	}

	@PostMapping("/businessunits/delete")
	public @ResponseBody Map<String, Object> deleteBusinessUnit(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: deleteBusinessUnit");

		Map<String, Object> response = new HashMap<>();
		try {
			String modified_by = val.get("modified_by");
			String business_unit_id = val.get("business_unit_id");
			organizationRepository.insertRecordBusinessDeleteUnitHistory(business_unit_id, modified_by);
			int count = organizationRepository.deletebusinessUnits(modified_by, business_unit_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_BankMASTER");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteBusinessUnit  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/businessunits/deleteBusinessUnit")
	public @ResponseBody Map<String, Object> deleteBusinessUnitNew(@RequestParam String modified_by,
			@RequestParam String business_unit_id) {

		logger.info("EXECUTING METHOD :: deleteBusinessUnit");

		Map<String, Object> response = new HashMap<>();
		try {

			String message = organizationRepository.checkWhetherBusinessIsInTransactionsBeforeDelete(business_unit_id);

			if (message != null) {
				response.put("message", message);
				response.put("status", "no");
				response.put("action", "DeleteBusinessUnit");

				return response;
			} else {
				organizationRepository.insertRecordBusinessDeleteUnitHistory(business_unit_id, modified_by);
				int count = organizationRepository.deletebusinessUnits(modified_by, business_unit_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "DeleteBusinessUnit");
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteBusinessUnit  -> " + e.getMessage());

		}
		return response;
	}

	/** BUSINESS UNIT END **/
}
