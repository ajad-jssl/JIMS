package com.JIMS.integration.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.AssignToContract;
import com.JIMS.integration.interfaces.BankMasterInterface;
import com.JIMS.integration.interfaces.BusinessUnitInterface;
import com.JIMS.integration.interfaces.ContractListFromContractInterfaces;
import com.JIMS.integration.interfaces.InvoiceConsigneeAddressInterface;
import com.JIMS.integration.interfaces.MileStoneAssignedContractListInterfaces;
import com.JIMS.integration.interfaces.ServiceCodeMasterInterface;
import com.JIMS.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.JIMS.integration.interfaces.TaxMasterInterface;
import com.JIMS.integration.interfaces.WorkOrderMasterInterface;
import com.JIMS.integration.repository.AssignToContractRepository;
import com.JIMS.integration.repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class AssignToContractController {
	Logger logger = LogManager.getLogger(MasterController.class);

	@SuppressWarnings("unused")
	@Autowired
	private UserRepository userrepo;

	@Autowired
	public AssignToContractRepository assignToContractRepository;
//	@PostMapping("/contract/addnew")
//	public @ResponseBody Map<String, Object> createContractNew(@RequestParam int contract_id, @RequestParam int bid,
//			@RequestParam String invoice_type_calculation, @RequestParam String percentage_value,
//			@RequestParam int invoice_to_id, @RequestParam int consignee_id, @RequestParam int shipment_mode_id,
//			@RequestParam int delivery_condition_id, @RequestParam int bank_details_id, @RequestParam int work_id,
//			@RequestParam int regd_office_id, @RequestParam(required = false) Integer s_code, @RequestParam(required = false) Integer h_code,
//			@RequestParam String contract_name, @RequestParam String product_desc_id, @RequestParam String export,
//			@RequestParam String tax_ex_inc, @RequestParam String taxable, @RequestParam String non_taxable,
//			@RequestParam String tax_payable, @RequestParam String freight_advance_recovery,
//			@RequestParam(required = false) String area_no, @RequestParam(required = false) String lot_no,
//			@RequestParam(required = false) String containter_no, @RequestParam(required = false) String epcgno,
//			@RequestParam String export_title_text, @RequestParam String created_by, @RequestParam String factory_id,
//			@RequestParam List<Integer> taxAdd) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		try {
//
////			  List<RoleAssignModule_SubModule_Actions> listData =  userrepo.getLoginRoleCredtianls(created_by); 
////			  Map<String, String>  permissionMap = new HashMap<>();
////			  for (RoleAssignModule_SubModule_Actions result : listData) {
////				  permissionMap.put(result.getSubmodule_name(), result.getActions_list()); 
////				  }
////			  String str = permissionMap.get("Assign To Contract"); 
////			  if(str == null ||  !str.contains("add")) { response.put("message", "Your not Right Person");
////			  return response; 
////			  }
////			 
//
//			if (taxAdd != null && !taxAdd.isEmpty()) {
//				for (Integer taxValue : taxAdd) {
//					if (taxValue == null || taxValue <= 0) {
//						throw new IllegalArgumentException("Invalid taxValue in taxAdd. Tax value must be positive.");
//					}
//					int count = assignToContractRepository.checkTaxExitForContractor(contract_id, taxValue);
//					if (count > 0) {
//						response.put("message", "Tax Already assigned to the contractor.");
//						return response;
//					} else {
//						assignToContractRepository.insertTaxContract(contract_id, taxValue, factory_id, created_by);
//					}
//
//				}
//			}
//
//	        // ✅ Default values if null (optional; only if your repository cannot accept null)
//			int safeSCode = (s_code != null) ? s_code : 0;
//			int safeHCode = (h_code != null) ? h_code : 0;
//	        
//			int count = assignToContractRepository.createContractInfo(contract_id, bid, invoice_type_calculation,
//					percentage_value, invoice_to_id, consignee_id, shipment_mode_id, delivery_condition_id,
//					product_desc_id, bank_details_id, work_id, regd_office_id, s_code, h_code, export, tax_ex_inc,
//					taxable, non_taxable, tax_payable, freight_advance_recovery, area_no, lot_no, containter_no, epcgno,
//					export_title_text, created_by, contract_name, factory_id);
//			response.put("action", "INSERT_CONTRACT");
//			response.put("message", (count > 0) ? "Success" : "failed");
//			response.put("status", (count > 0) ? "yes" : "no");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
	
		@PostMapping("/contract/addnew")
		public @ResponseBody Map<String, Object> createContractNew(@RequestParam int contract_id, @RequestParam int bid,
				@RequestParam String invoice_type_calculation, @RequestParam String percentage_value,
				@RequestParam int invoice_to_id, @RequestParam int consignee_id, @RequestParam int shipment_mode_id,
				@RequestParam int delivery_condition_id, @RequestParam int bank_details_id, @RequestParam int work_id,
				@RequestParam int regd_office_id, @RequestParam(required = false) Integer s_code, @RequestParam(required = false) Integer h_code,
				@RequestParam String contract_name, @RequestParam String product_desc_id, @RequestParam String export,
				@RequestParam String tax_ex_inc, @RequestParam String taxable, @RequestParam String non_taxable,
				@RequestParam String tax_payable, @RequestParam String freight_advance_recovery,
				@RequestParam(required = false) String area_no, @RequestParam(required = false) String lot_no,
				@RequestParam(required = false) String containter_no, @RequestParam(required = false) String epcgno,
				@RequestParam String export_title_text, @RequestParam String created_by, @RequestParam String factory_id,
				@RequestParam List<Integer> taxAdd) {
			Map<String, Object> response = new HashMap<String, Object>();
			try {
	
	//			  List<RoleAssignModule_SubModule_Actions> listData =  userrepo.getLoginRoleCredtianls(created_by); 
	//			  Map<String, String>  permissionMap = new HashMap<>();
	//			  for (RoleAssignModule_SubModule_Actions result : listData) {
	//				  permissionMap.put(result.getSubmodule_name(), result.getActions_list()); 
	//				  }
	//			  String str = permissionMap.get("Assign To Contract"); 
	//			  if(str == null ||  !str.contains("add")) { response.put("message", "Your not Right Person");
	//			  return response; 
	//			  }
	//			 
			       // 👉 If ALL is selected → expand into multiple factories
				String FactoryId = factory_id.trim().equalsIgnoreCase("all") ? "1,2" : factory_id.trim();

		        	
				if (taxAdd != null && !taxAdd.isEmpty()) {
					for (Integer taxValue : taxAdd) {
						if (taxValue == null || taxValue <= 0) {
							throw new IllegalArgumentException("Invalid taxValue in taxAdd. Tax value must be positive.");
						}
						int count = assignToContractRepository.checkTaxExitForContractor(contract_id, taxValue);

						if (count > 0) {
							response.put("message", "Tax Already assigned to the contractor.");
							return response;
						} else {
							assignToContractRepository.insertTaxContract(contract_id, taxValue, FactoryId, created_by);
						}
	
					}
				}
		
	
		        // ✅ Default values if null (optional; only if your repository cannot accept null)
				int safeSCode = (s_code != null) ? s_code : 0;
				int safeHCode = (h_code != null) ? h_code : 0;
		             
				int count = assignToContractRepository.createContractInfo(contract_id, bid, invoice_type_calculation,
						percentage_value, invoice_to_id, consignee_id, shipment_mode_id, delivery_condition_id,
						product_desc_id, bank_details_id, work_id, regd_office_id, s_code, h_code, export, tax_ex_inc,
						taxable, non_taxable, tax_payable, freight_advance_recovery, area_no, lot_no, containter_no, epcgno,
						export_title_text, created_by, contract_name, FactoryId);
				response.put("action", "INSERT_CONTRACT");
				response.put("message", (count > 0) ? "Success" : "failed");
				response.put("status", (count > 0) ? "yes" : "no");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@PostMapping("/contract/updatenew")
		public @ResponseBody Map<String, Object> updateContractNew(

		        @RequestParam int contract_id,
		        @RequestParam int bid,
		        @RequestParam String invoice_type_calculation,
		        @RequestParam String percentage_value,
		        @RequestParam int invoice_to_id,
		        @RequestParam int consignee_id,
		        @RequestParam int shipment_mode_id,
		        @RequestParam int delivery_condition_id,
		        @RequestParam int bank_details_id,
		        @RequestParam int work_id,
		        @RequestParam int regd_office_id,
		        @RequestParam(required = false) Integer s_code,
		        @RequestParam(required = false) Integer h_code,
		        @RequestParam String contract_name,
		        @RequestParam String product_desc_id,
		        @RequestParam String export,
		        @RequestParam String tax_ex_inc,
		        @RequestParam String taxable,
		        @RequestParam String non_taxable,
		        @RequestParam String tax_payable,
		        @RequestParam String freight_advance_recovery,
		        @RequestParam(required = false) String area_no,
		        @RequestParam(required = false) String lot_no,
		        @RequestParam(required = false) String containter_no,
		        @RequestParam(required = false) String epcgno,
		        @RequestParam String export_title_text,
		        @RequestParam String modified_by,
		        @RequestParam String factory_id,
		        @RequestParam int con_slno,
		        @RequestParam(required = false) List<Integer> taxAdd,
		        @RequestParam(required = false) List<Integer> taxRemoved) {

		    Map<String, Object> response = new HashMap<>();

		    try {

		        /* ================= LOCK CHECK ================= */
		        int locked = assignToContractRepository.checkContractIdisLocked(con_slno);
		        if (locked > 0) {
		            response.put("message", "Invoice Already Generated Not Able to Update");
		            response.put("status", "no");
		            return response;
		        }

		        String FactoryId = factory_id.equalsIgnoreCase("all") ? "1,2" : factory_id;

		        boolean taxChanged = false;

		        /* ================= TAX REMOVE ================= */
		        if (taxRemoved != null && !taxRemoved.isEmpty()) {
		            for (Integer taxId : taxRemoved) {

		                // Only delete if exists (IMPORTANT)
		                int exists = assignToContractRepository.checkTaxExitForContractor(contract_id, taxId);

		                if (exists > 0) {
		                    assignToContractRepository.updateTaxContract(taxId, contract_id, modified_by);
		                    assignToContractRepository.deleteTaxContract(taxId, contract_id);
		                    taxChanged = true;
		                }
		            }
		        }

		        /* ================= TAX ADD ================= */
		        if (taxAdd != null && !taxAdd.isEmpty()) {
		            for (Integer taxId : taxAdd) {

		                int exists = assignToContractRepository.checkTaxExitForContractor(contract_id, taxId);

		                if (exists == 0) {
		                    assignToContractRepository.insertTaxContract(contract_id, taxId, FactoryId, modified_by);
		                    taxChanged = true;
		                }
		            }
		        }

		        int safeSCode = (s_code != null) ? s_code : 0;
		        int safeHCode = (h_code != null) ? h_code : 0;

		        /* ================= MAIN UPDATE ================= */
		        int count = assignToContractRepository.updateContractInfo(
		                contract_id, bid, invoice_type_calculation, percentage_value,
		                invoice_to_id, consignee_id, shipment_mode_id, delivery_condition_id,
		                product_desc_id, bank_details_id, work_id, regd_office_id,
		                safeSCode, safeHCode, export, tax_ex_inc, taxable,
		                non_taxable, tax_payable, freight_advance_recovery,
		                area_no, lot_no, containter_no, epcgno,
		                export_title_text, modified_by, contract_name, FactoryId, con_slno
		        );

		        /* ================= DEBUG LOG (VERY IMPORTANT) ================= */
		        System.out.println("UPDATE COUNT = " + count + " | TAX CHANGED = " + taxChanged);

		        /* ================= FINAL DECISION ================= */
		        if (count == 0 && !taxChanged) {
		            response.put("message", "No fields were changed.");
		            response.put("status", "no");
		            response.put("action", "NO_CHANGE");
		        } else {
		            response.put("message", "Success");
		            response.put("status", "yes");
		            response.put("action", "UPDATE_CONTRACT");
		        }

		    } catch (Exception e) {

		        logger.error("ERROR in updateContractNew", e);

		        response.put("message", "Failed to update record.");
		        response.put("status", "no");
		        response.put("action", "ERROR");
		    }

		    return response;
		}

	@PostMapping("/contract/deletenew")
	public @ResponseBody Map<String, Object> deleteContractNew(@RequestParam String con_slno,
			@RequestParam String modified_by) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int count = assignToContractRepository.updateContractDelete(modified_by, con_slno);
			List<Integer> taxId = assignToContractRepository
					.getTaxListFromContractAssignTax(Integer.parseInt(con_slno));
			for (int value : taxId) {
				assignToContractRepository.deleteTaxContract(value);
			}
			response.put("action", "DELETE_CONTRACT");
			response.put("message", (count > 0) ? "Success" : "failed");
			response.put("status", (count > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listcontractnew")
	public @ResponseBody Map<String, Object> listContractNew() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<AssignToContract> count = assignToContractRepository.getContractList();
			response.put("action", "LIST Contractor");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listcontractfromcontractornew")
	public @ResponseBody Map<String, Object> listContractFromContract() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ContractListFromContractInterfaces> count = assignToContractRepository
					.getContractListFromContract();
			response.put("action", "LIST CONTRACT FROM CONTRACT TABLE");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/searchcontractnew")
	public @ResponseBody Map<String, Object> searContractByIdNew(@RequestParam String con_slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<AssignToContract> assignToContract = null;
		List<Integer> value = null;
		try {
			assignToContract = assignToContractRepository.searchContractById(con_slno);
			int contract_id = assignToContractRepository.getContractorIdforConSlno(con_slno);
			value = assignToContractRepository.getTaxListFromContractAssignTax(contract_id);
			response.put("message", (assignToContract.size() > 0) ? "Success" : "failure");
			response.put("status", (assignToContract.size() > 0) ? "yes" : "no");
			response.put("action", "Search_Record_In_Contract_LIST");
			response.put("tax", value);
			response.put("DATA", assignToContract);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listworkordernew")
	public @ResponseBody Map<String, Object> listWorkOrderNew() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<WorkOrderMasterInterface> count = assignToContractRepository.getWorkOrderList();
			response.put("action", "LIST WORK ORDER");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	
	  @GetMapping("/contract/listbanknamenew") public @ResponseBody Map<String,
	  Object> listbanknameNew() { Map<String,
	  Object> response = new HashMap<String, Object>(); try {
	  List<BankMasterInterface> count =
	  assignToContractRepository.getBankNameList();
	  response.put("action", "LIST BANK NAME"); response.put("message",
	  (count.size() > 0) ? "Success" : "failed"); response.put("status",
	  (count.size() > 0) ? "yes" : "no"); response.put("Data", count); } catch
	  (Exception e) { e.printStackTrace(); } return response; }
	 

	@GetMapping("/contract/listbusinessunitnew")
	public @ResponseBody Map<String, Object> listBusinessUnitNew() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<BusinessUnitInterface> count = assignToContractRepository.getBusinessUnitList();
			response.put("action", "LIST BUSINESS UNIT NAME");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/list_hsn_servicecodenew")
	public @ResponseBody Map<String, Object> listServiceCodeNew() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ServiceCodeMasterInterface> count = assignToContractRepository.getServiceCode();
			response.put("action", "LIST SERVICE HSN CODE");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listinvoiceconsineenew")
	public @ResponseBody Map<String, Object> listInvoiceConsineeNew() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<InvoiceConsigneeAddressInterface> count = assignToContractRepository.getInvoiceConsinee();
			response.put("action", "LIST INVOICE CONSINEE ADDRESS");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listdeliveryshipmentnew")
	public @ResponseBody Map<String, Object> listShipmentDeliveryConditionNew() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ShipmentDeliveryConditionInterfaces> count = assignToContractRepository
					.getShipmentDeliveryCondition();
			response.put("action", "LIST DELIVERY SHIPMENT CONDITION");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listtaxmasternew")
	public @ResponseBody Map<String, Object> listTaxMaster() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<TaxMasterInterface> count = assignToContractRepository.getTaxMaster();
			response.put("action", "LIST TAX MASTER");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/milestoneassigncontractnew")
	public @ResponseBody Map<String, Object> insertMilestoneForContract(@RequestParam String milestone_id,
			@RequestParam String contract_id, @RequestParam String created_by, @RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		String FactoryId = factory_id.trim().equalsIgnoreCase("all") ? "1,2" : factory_id.trim();
		try {
			int value = assignToContractRepository.checkDuplicateMilestone(milestone_id, contract_id);
			if (value == 0) {
				int count = assignToContractRepository.assignMileStoneToContract(milestone_id, contract_id,
						created_by.toUpperCase().trim(), FactoryId);
				response.put("action", "ADD_MILESTONE_FOR_CONTRACT");
				response.put("message", (count > 0) ? "Success" : "failed");
				response.put("status", (count > 0) ? "yes" : "no");
			} else {
				response.put("action", "ADD_MILESTONE_FOR_CONTRACT");
				response.put("message", (value > 0) ? "MileStone Already Exits For That Contractor" : "failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/milestoneassigncontractupdatenew")
	public @ResponseBody Map<String, Object> updateMilestoneForContract(@RequestParam String milestone_id,
			@RequestParam String contract_id, @RequestParam String modified_by, @RequestParam String factory_id,
			@RequestParam String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int value = assignToContractRepository.checkDuplicateMilestone(milestone_id, contract_id);
			if (value > 0) {
				response.put("action", "UPDATE_MILESTONE_FOR_CONTRACT");
				response.put("message", (value > 0) ? "MileStone Already Exits For That Contractor" : "failed");
			} else {
				int checkValue = assignToContractRepository
						.MilestoneUsedinQSPackingCombineOfContractorAndMilestoneId(contract_id, milestone_id);
				if (checkValue > 0) {
					response.put("message", "MileStone Already Used in Packing not able to Update");
				} else {
					int count = assignToContractRepository.updateMileStoneToContract(milestone_id, contract_id,
							modified_by, id);
					response.put("action", "UPDATE_MILESTONE_FOR_CONTRACT");
					response.put("message", (count > 0) ? "Success" : "failed");
					response.put("status", (count > 0) ? "yes" : "no");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/milestoneassigncontractsearchnew")
	public @ResponseBody Map<String, Object> searchMilestoneForContract(@RequestParam String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		MileStoneAssignedContractListInterfaces count = null;
		try {

			count = assignToContractRepository.searchMileStoneToContract(id);
			response.put("action", "Search_MILESTONE_FOR_CONTRACT");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/listassignmilestoneforcontractornew")
	public @ResponseBody Map<String, Object> listContractFromContractMaster(@RequestParam int contract_id
			) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<MileStoneAssignedContractListInterfaces> count = assignToContractRepository
					.listContractMaster(contract_id);
			response.put("action", "LIST_FROM_CONTRACT_MASTER");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("DATA", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
