package com.JIMS.integration.controller;

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

import com.JIMS.integration.interfaces.ExportTitleInterface;
import com.JIMS.integration.interfaces.GSTStateMasterInterface;
import com.JIMS.integration.interfaces.InvoiceTypeInterface;
import com.JIMS.integration.interfaces.MileStoneMasterInterface;
import com.JIMS.integration.interfaces.OtherTypeMasterInterface;
import com.JIMS.integration.interfaces.RemarksInvoiceTypeInterfaces;
import com.JIMS.integration.interfaces.ServiceCodeMasterInterface;
import com.JIMS.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.JIMS.integration.interfaces.TypeMasterInterface;
import com.JIMS.integration.interfaces.UOMMasterInterface;
import com.JIMS.integration.interfaces.VendorForScrapMasterInterface;
import com.JIMS.integration.interfaces.WorkOrderMasterInterface;
import com.JIMS.integration.repository.MasterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class MasterController {
	Logger logger = LogManager.getLogger(MasterController.class);

	@Autowired
	private MasterRepository masterRepository;

	/* SHIPMENT DELIVER CONDITION START */
	@PostMapping("/master/addshipmentdeliverynew")
	public @ResponseBody Map<String, Object> createShipmentDeliveryNew(@RequestParam String shipment_mode,
			@RequestParam String delivery_condition, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id) {
		shipment_mode = shipment_mode.toUpperCase();
		delivery_condition = delivery_condition.toUpperCase();
		created_by = created_by.toUpperCase();
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createShipmentDeliveryNew");
		int check = 0;
		try {
			logger.info("EXECUTING METHOD :: BEFORE adding insertShipmentDelivery");
			check = masterRepository.checkDuplicateShipmentDeliveryConditionAndShipmentMode(delivery_condition,
					shipment_mode);
			if (check > 0) {
				response.put("message", "ShipmentMode / DeliveryCondition Already Exists");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_ShipmentDelivery");
				return response;
			}
			int count = masterRepository.insertShipmentDelivery(shipment_mode, delivery_condition, created_by,
					factory_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_ShipmentDelivery");
			logger.info("EXECUTING METHOD :: AFTER adding insertShipmentDelivery");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createShipmentDeliveryNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createShipmentDeliveryNew");
		return response;
	}

	@GetMapping("/master/searchshipmentdeliverynew")
	public @ResponseBody Map<String, Object> serachShipmentDeliveryIdNew(@RequestParam String si_id) {
		logger.info("EXECUTING METHOD :: serachShipmentDeliveryIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		ShipmentDeliveryConditionInterfaces shipmentDeliveryConditionInterfaces = null;
		try {
			shipmentDeliveryConditionInterfaces = masterRepository.searchShipmentDeliveryId(si_id);
			response.put("message", (shipmentDeliveryConditionInterfaces != null) ? "Success" : "failure");
			response.put("status", (shipmentDeliveryConditionInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_ShipmentDelivery");
			response.put("DATA", shipmentDeliveryConditionInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachShipmentDeliveryIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachShipmentDeliveryIdNew");
		return response;
	}

	@PostMapping("/master/updateshipmentdeliverynew")
	public @ResponseBody Map<String, Object> updateShipmentDeliveryNew(@RequestParam String shipment_mode,
			@RequestParam String delivery_condition, @RequestParam String modified_by, @RequestParam String si_id) {
		shipment_mode = shipment_mode.toUpperCase();
		delivery_condition = delivery_condition.toUpperCase();
		modified_by = modified_by.toUpperCase();
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		int check = 0;
		logger.info("EXECUTING METHOD :: updateShipmentDeliveryNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATE SHIPMENTDELIVERY");
			check = masterRepository.checkShipmentOrDeliveryUsed(si_id,si_id);
			if (check > 0) {
				response.put("message", "The Transcation already exists for Shipment/Delivery");
				return response;
			}
			check = masterRepository.checkDuplicateShipmentDeliveryConditionAndShipmentModeWithId(delivery_condition,
					shipment_mode, si_id);
			if (check > 0) {
				response.put("message", "ShipmentMode/DeliveryCondition Already Exists");
				return response;
			}
			count = masterRepository.updateShipmentDeliveryRecord(shipment_mode, delivery_condition, modified_by,
					si_id);
			logger.info("EXECUTED METHOD :: AFTER UPDATE SHIPMENTDELIVERY");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_ShipmentDelivery");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateShipmentDeliveryNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateShipmentDeliveryNew");
		return response;
	}

	@PostMapping("/master/deleteshipmentdeliverynew")
	public @ResponseBody Map<String, Object> deleteShipmentDeliveryNew(@RequestParam String modified_by,
			@RequestParam String si_id) {
		logger.info("EXECUTING METHOD :: deleteShipmentDeliveryNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELTE SHIPMENTDELIVERY");
			int check = masterRepository.checkDeliveryConditionIdPresentInContractMaster(si_id);
			if (check > 0) {
				response.put("message", "Already used in Transactions not able to delete");
				return response;
			}
			int count = masterRepository.deleteshipmentdelivery(modified_by, si_id);
			logger.info("EXECUTING METHOD :: AFTER DELETE SHIPMENTDELIVERY");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Delete_Record_In_ShipmentDelivery");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteShipmentDeliveryNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteShipmentDeliveryNew");
		return response;
	}

	@GetMapping("/master/listshipmentdeliverynew")
	public @ResponseBody Map<String, Object> listShipmentDeliveryNew(
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<ShipmentDeliveryConditionInterfaces> shipmentDeliveryConditionInterfaces = null;
		logger.info("EXECUTING METHOD :: listShipmentDeliveryNew");
		try {
			shipmentDeliveryConditionInterfaces = masterRepository.listShipmentDeliveryRecord(factory_id);
			response.put("message", (shipmentDeliveryConditionInterfaces != null) ? "Success" : "failure");
			response.put("status", (shipmentDeliveryConditionInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_ShipmentDelivery");
			response.put("Data", shipmentDeliveryConditionInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listShipmentDeliveryNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listShipmentDeliveryNew");
		return response;
	}

	/* SHIPMENT DELIVER CONDITION END */
	/* 1. GSTSTATEMASTER( ScreenName = CREATE GST STATE) ----> START */
	@PostMapping("/master/addgstmasternew")
	public @ResponseBody Map<String, Object> createGstStateNew(@RequestParam String sap_bucode,
			@RequestParam String state_code, @RequestParam String state_gstno, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createGstStateNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING GSTMASTER");
			int checkDuplicate = masterRepository.checkDuplicateGSTMasterRecord(sap_bucode, state_gstno);
			if (checkDuplicate > 0) {
				response.put("message", "GST Master Already Exists");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_GSTMASTER");
				return response;
			} else {
				int count = masterRepository.insertGSTMasterRecord(sap_bucode.toUpperCase(), state_code.toUpperCase(),
						state_gstno.toUpperCase(), created_by.toUpperCase(), factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_GSTMASTER");
			}
			logger.info("EXECUTING METHOD :: AFTER ADDING GSTMASTER");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createGstStateNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createGstStateNew");
		return response;
	}

	@GetMapping("/master/searchgstmasternew")
	public @ResponseBody Map<String, Object> serachGstStateByIdNew(@RequestParam String buId) {
		Map<String, Object> response = new HashMap<String, Object>();
		GSTStateMasterInterface gstStateMasterInterface = null;
		logger.info("EXECUTING METHOD :: serachGstStateByIdNew");
		try {
			gstStateMasterInterface = masterRepository.searchGSTById(buId);
			response.put("message", (gstStateMasterInterface != null) ? "Success" : "failure");
			response.put("status", (gstStateMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_GSTMASTER");
			response.put("DATA", gstStateMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachGstStateByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachGstStateByIdNew");
		return response;
	}

	@PostMapping("/master/updategstmasternew")
	public @ResponseBody Map<String, Object> updateGstStateNew(@RequestParam String sap_bucode,
			@RequestParam String state_code, @RequestParam String state_gstno, @RequestParam String modified_by,
			@RequestParam String bu_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updateGstStateNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING GSTMASTER");
			String message = masterRepository.checkGstInTransactions(bu_id);
			if (message != null) {
				response.put("message", message);
				response.put("status", "no");
				response.put("action", "Update_Record_In_GSTMASTER");
				return response;
			} else {
				int checkDuplicate = masterRepository.checkDuplicateGSTMasterRecordWithId(sap_bucode, state_gstno,
						bu_id);
				if (checkDuplicate > 0) {
					response.put("message", "GST Master Already Exists");
					response.put("status", "no");
					response.put("action", "Update_Record_In_GSTMASTER");
					return response;
				}
				int count = masterRepository.updateGSTMasterRecord(sap_bucode.toUpperCase(), state_code.toUpperCase(),
						state_gstno.toUpperCase(), modified_by.toUpperCase(), bu_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_GSTMASTER");
			}
			logger.info("EXECUTING METHOD :: AFTER UPDATING GSTMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateGstStateNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateGstStateNew");
		return response;
	}

	@PostMapping("/master/deletegstmasternew")
	public @ResponseBody Map<String, Object> deleteGstStateNew(@RequestParam String modified_by,
			@RequestParam String bu_id) {
		logger.info("EXECUTING METHOD :: deleteGstStateNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETING GST MASTER");
			String message = masterRepository.checkGstInTransactions(bu_id);
			if (message != null) {
				response.put("message", "Already used in Transactions not able to Delete");
				response.put("status", "no");
				response.put("action", "Delete_Record_In_GSTMASTER");
				return response;
			} else {
				int count = masterRepository.deleteGSTMasterRecord(modified_by, bu_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Delete_Record_In_GSTMASTER");
			}
			logger.info("EXECUTING METHOD :: AFTER DELETING GST MASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteGstStateNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteGstStateNew");
		return response;
	}

	@GetMapping("/master/listgstmasternew")
	public @ResponseBody Map<String, Object> listGstStateNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<GSTStateMasterInterface> gstStateMasterInterface = null;
		logger.info("EXECUTING METHOD :: listGstStateNew");
		try {
			gstStateMasterInterface = masterRepository.listGSTMasterRecord(factory_id);
			response.put("message", (gstStateMasterInterface != null) ? "Success" : "failure");
			response.put("status", (gstStateMasterInterface != null) ? "yes" : "no");
			response.put("action", "List_Record_In_GSTMASTER");
			response.put("Data", gstStateMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listGstStateNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listGstStateNew");
		return response;
	}

	/* 1. GSTSTATEMASTER (ScreenName = CREATE GST STATE)----> END */

	/* 2. MILESTONEMASTER (ScreenName = CREATE MILESTONE)----> START */
	@PostMapping("/master/addmilestonemasternew")
	public @ResponseBody Map<String, Object> createMileStoneNew(@RequestParam String milestone_code,
			@RequestParam String milestone_name, @RequestParam String milestone_desc, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createMileStoneNew");
		milestone_code = milestone_code.toUpperCase();
		milestone_name = milestone_name.toUpperCase();
		milestone_desc = milestone_desc.toUpperCase();
		created_by = created_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING MILESTONE");
			int checkExistCount = masterRepository.checkMilestoneExistsOrNot(milestone_code, milestone_name);
			if (checkExistCount > 0) {
				response.put("message", "Milestone Already Exists");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_MILESTONEMASTER");
				return response;
			} else {
				int count = masterRepository.insertMilestoneRecord(milestone_code, milestone_name, milestone_desc,
						created_by, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_MILESTONEMASTER");
			}
			logger.info("EXECUTING METHOD :: AFTER ADDING MILESTONE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createMileStoneNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createMileStoneNew");
		return response;
	}

	@PostMapping("/master/updatemilestonemasternew")
	public @ResponseBody Map<String, Object> updateMileStoneNew(@RequestParam String milestone_code,
			@RequestParam String milestone_name, @RequestParam String milestone_desc, @RequestParam String modified_by,
			@RequestParam String milestone_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updateMileStoneNew");
		milestone_code = milestone_code.toUpperCase();
		milestone_name = milestone_name.toUpperCase();
		milestone_desc = milestone_desc.toUpperCase();
		modified_by = modified_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING MILESTONE");
			String message = masterRepository.checkTransactionForMilestone(milestone_code, milestone_name,
					milestone_id);
			if (message != null) {
				response.put("message", message);
				response.put("status", "no");
				response.put("action", "Update_Record_In_MILESTONEMASTER");
				return response;
			} else {
				int checkExistCount = masterRepository.checkMilestoneExistsOrNotWithId(milestone_code, milestone_name,
						milestone_id);

				if (checkExistCount > 0) {
					response.put("message", "Milestone Already Exists");
					response.put("status", "no");
					response.put("action", "Update_Record_In_MILESTONEMASTER");
					return response;
				} else {
					int count = masterRepository.updateMilestoneRecord(milestone_code, milestone_name, milestone_desc,
							modified_by, milestone_id);
					response.put("message", (count > 0) ? "Success" : "failure");
					response.put("status", (count > 0) ? "yes" : "no");
					response.put("action", "Update_Record_In_MILESTONEMASTER");
				}
			}
			logger.info("EXECUTING METHOD :: AFTER UPDATING MILESTONE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateMileStoneNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateMileStoneNew");
		return response;
	}

	@PostMapping("/master/deletemilestonemasternew")
	public @ResponseBody Map<String, Object> deleteMileStoneNew(@RequestParam String modified_by,
			@RequestParam String milestone_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteMileStoneNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE  DELETE MILESTONE");
			String message = masterRepository.checkTransactionForMilestoneBeforeDelete(milestone_id);
			if (message != null) {
				response.put("message", "Already used in Transactions not able to Delete");
				response.put("status", "no");
				response.put("action", "Delete_Record_In_MILESTONEMASTER");
				return response;
			} else {
				int count = masterRepository.deleteMilestoneRecord(modified_by, milestone_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Delete_Record_In_MILESTONEMASTER");
			}
			logger.info("EXECUTING METHOD :: AFTER  DELETE MILESTONE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteMileStoneNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteMileStoneNew");
		return response;
	}

	@GetMapping("/master/searchmilestonemasternew")
	public @ResponseBody Map<String, Object> serachmilestonemasterNew(@RequestParam String milestone_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: serachmilestonemasterNew");
		MileStoneMasterInterface mileStoneMasterInterface = null;
		try {
			mileStoneMasterInterface = masterRepository.searchMileStoneById(milestone_id);
			response.put("message", (mileStoneMasterInterface != null) ? "Success" : "failure");
			response.put("status", (mileStoneMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_MILESTONEMASTER");
			response.put("DATA", mileStoneMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachmilestonemasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachmilestonemasterNew");
		return response;
	}

	@GetMapping("/master/listmilestonemasternew")
	public @ResponseBody Map<String, Object> listMileStoneNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<MileStoneMasterInterface> mileStoneMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: listMileStoneNew");
		try {
			mileStoneMasterInterfaces = masterRepository.listMilestoneRecords(factory_id);
			response.put("message", (mileStoneMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (mileStoneMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_MILESTONEMASTER");
			response.put("Data", mileStoneMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listMileStoneNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listMileStoneNew");
		return response;
	}

	/* 2. MILESTONEMASTER (ScreenName = CREATE MILESTONE)----> END */

	/* 3. OTHERTYPEMASTER (ScreenName = OTHERTYPE)----> START */
	@PostMapping("/master/addothertypemasternew")
	public @ResponseBody Map<String, Object> createOtherTypeNew(@RequestParam String type,
			@RequestParam String created_by, @RequestParam(required = false) String factory_id) {
		logger.info("EXECUTING METHOD :: createOtherTypeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		type = type.toUpperCase();
		created_by = created_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING OTHER TYPE");
			int count = masterRepository.insertOtherType(type, created_by, factory_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_OtherType");
			logger.info("EXECUTING METHOD :: AFTER ADDING OTHER TYPE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createOtherTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createOtherTypeNew");
		return response;
	}

	@PostMapping("/master/updateothertypemasternew")
	public @ResponseBody Map<String, Object> updateOtherTypeNew(@RequestParam String type,
			@RequestParam String modified_by, @RequestParam String othertype_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updateothertypemasternew");
		type = type.toUpperCase();
		modified_by = modified_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING OTHER TYPE");
			int count = masterRepository.updateOtherType(type, modified_by, othertype_id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING OTHER TYPE");
			logger.info("EXECUTING METHOD :: createOtherTypeNew");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_OtherType");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateothertypemasternew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateothertypemasternew");
		return response;
	}

	@GetMapping("/master/searchothertypemasternew")
	public @ResponseBody Map<String, Object> serachOtherTypeByIdNew(@RequestParam String otherTypeId) {
		Map<String, Object> response = new HashMap<String, Object>();
		OtherTypeMasterInterface otherTypeMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: serachOtherTypeByIdNew");
		try {
			otherTypeMasterInterfaces = masterRepository.searchOtherTypeById(otherTypeId);
			response.put("message", (otherTypeMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (otherTypeMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_OtherType");
			response.put("DATA", otherTypeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachOtherTypeByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachOtherTypeByIdNew");
		return response;
	}

	@PostMapping("/master/deleteothertypemasternew")
	public @ResponseBody Map<String, Object> deleteOtherTypeNew(@RequestParam String modified_by,
			@RequestParam String othertype_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteOtherTypeNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETE OTHER TYPE");
			int count = masterRepository.deleteOtherType(modified_by, othertype_id);
			logger.info("EXECUTING METHOD :: AFTER DELETE OTHER TYPE");
			logger.info("EXECUTING METHOD :: serachOtherTypeByIdNew");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_OtherType");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteOtherTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteOtherTypeNew");
		return response;
	}

	@GetMapping("/master/listothertypemasternew")
	public @ResponseBody Map<String, Object> listOtherTypeNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<OtherTypeMasterInterface> otherTypeMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: listothertypemasternew");
		try {
			otherTypeMasterInterfaces = masterRepository.listOtherTypes(factory_id);
			response.put("message", (otherTypeMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (otherTypeMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_OtherType");
			response.put("Data", otherTypeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listothertypemasternew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listothertypemasternew");
		return response;
	}
	/* 3. OTHERTYPEMASTER (ScreenName = OTHERTYPE )----> END */

	/* 4. SERVICECODEMASTER (ScreenName = Create ServiceCode) ----> START */
	@PostMapping("/master/addservicecodemasternew")
	public @ResponseBody Map<String, Object> createServiceCodeNew(@RequestParam String service_type,
			@RequestParam String service_description, @RequestParam String service_code,
			@RequestParam String created_by, @RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createServiceCodeNew");
		service_type = service_type.toUpperCase();
		service_description = service_description.toUpperCase();
		service_code = service_code.toUpperCase();
		created_by = created_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING SERVICE CODE");
			int countDuplicate = masterRepository.checkDUplicateServiceCode(service_code);
			if (countDuplicate > 0) {
				response.put("message", "Code Already Exists");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_ServiceCode");
				return response;
			} else {
				int count = masterRepository.insertServiceCodeRecord(service_type, service_description, service_code,
						created_by, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_ServiceCode");
			}
			logger.info("EXECUTING METHOD :: AFTER ADDING SERVICE CODE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createServiceCodeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createServiceCodeNew");
		return response;
	}

	@PostMapping("/master/updateservicecodemasternew")
	public @ResponseBody Map<String, Object> updateServiceCodeNew(@RequestParam String service_type,
			@RequestParam String service_description, @RequestParam String service_code,
			@RequestParam String modified_by, @RequestParam String servicecode_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updateServiceCodeNew");
		service_type = service_type.toUpperCase();
		service_description = service_description.toUpperCase();
		service_code = service_code.toUpperCase();
		modified_by = modified_by.toUpperCase();
		int count = 0;
		int check = 0;
//		int checkservice_count = 0;
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING SERVICE CODE");
			logger.info("what is the servicecode we are sending" + servicecode_id);
			check = masterRepository.checkServiceCodeUsed(servicecode_id);
		
			
			logger.info("see what is the check"+check);
			if (check > 0) {
				response.put("message", "The transaction already exist for Service code");
				return response;
			}
//
//			checkservice_count = masterRepository.checkServiceCodeIdPresentInContractMaster(servicecode_id);
//			if (checkservice_count > 1) {
//				response.put("message", "The transaction already exist for Code");
//				return response;
//			}

			int countDuplicate = masterRepository.checkDUplicateServiceCodeWithId(service_code, servicecode_id);
			if (countDuplicate > 0) {
				response.put("message", "Code Already Exists");
				response.put("status", "no");
				response.put("action", "Update_Record_In_ServiceCode");
				return response;
			} else {
				count = masterRepository.updateServiceCodeRecord(service_type, service_description, service_code,
						modified_by, servicecode_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_ServiceCode");
			}
			logger.info("EXECUTING METHOD :: AFTER UPDATING SERVICE CODE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateServiceCodeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateServiceCodeNew");
		return response;
	}

	@PostMapping("/master/deleteservicecodemasternew")
	public @ResponseBody Map<String, Object> deleteServiceCodeNew(@RequestParam String modified_by,
			@RequestParam String servicecode_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteServiceCodeNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETE SERVICE CODE");
			int check = masterRepository.checkHSNCodeIdPresentInContractMaster(servicecode_id);
			if (check == 1) {
				response.put("message", "Already used in Transactions not able to Delete");
				return response;
			}
			masterRepository.deleteServiceCodeToHistory(servicecode_id, modified_by);
			int count = masterRepository.updateServiceCodeInActiveRecord(modified_by, servicecode_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Delete_Record_In_ServiceCode");
			logger.info("EXECUTING METHOD :: AFTER DELETE SERVICE CODE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteServiceCodeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteServiceCodeNew");
		return response;
	}

	@GetMapping("/master/searchservicecodenew")
	public @ResponseBody Map<String, Object> serachServiceCodeByIdNew(@RequestParam String servicecodeId) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: searchservicecodenew");
		ServiceCodeMasterInterface serviceCodeMasterInterfaces = null;
		try {
			serviceCodeMasterInterfaces = masterRepository.searchServiceById(servicecodeId);
			response.put("message", (serviceCodeMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (serviceCodeMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_ServiceCode");
			response.put("DATA", serviceCodeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR searchservicecodenew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: searchservicecodenew");
		return response;
	}

	@GetMapping("/master/listservicecodemasternew")
	public @ResponseBody Map<String, Object> listServiceCodeNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listservicecodemasternew");
		List<ServiceCodeMasterInterface> serviceCodeMasterInterfaces = null;
		try {
			serviceCodeMasterInterfaces = masterRepository.listServiceCodeRecordsNew(factory_id);
			response.put("message", (serviceCodeMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (serviceCodeMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_ServiceCode");
			response.put("Data", serviceCodeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listservicecodemasternew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listservicecodemasternew");
		return response;
	}

	@GetMapping("/master/listHsncodemasternew")
	public @ResponseBody Map<String, Object> listHSNCodeNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> responsehsnmap = new HashMap<String, Object>();
		List<ServiceCodeMasterInterface> hsnCodeMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: listHSNCodeNew");
		try {
			hsnCodeMasterInterfaces = masterRepository.listHSNCodeRecords(factory_id);
			responsehsnmap.put("message", (hsnCodeMasterInterfaces != null) ? "Success" : "failure");
			responsehsnmap.put("status", (hsnCodeMasterInterfaces != null) ? "yes" : "no");
			responsehsnmap.put("action", "List_Record_In_HSNCode");
			responsehsnmap.put("Data", hsnCodeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listHSNCodeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listHSNCodeNew");
		return responsehsnmap;
	}

	@GetMapping("/master/listbothhsnservicecodemasternew")
	public @ResponseBody Map<String, Object> listHSNServiceCodeNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> responsehsnmap = new HashMap<String, Object>();
		List<ServiceCodeMasterInterface> hsnCodeMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: listHSNCodeNew");
		try {
			hsnCodeMasterInterfaces = masterRepository.listServiceAndHSNCodes(factory_id);
			responsehsnmap.put("message", (hsnCodeMasterInterfaces != null) ? "Success" : "failure");
			responsehsnmap.put("status", (hsnCodeMasterInterfaces != null) ? "yes" : "no");
			responsehsnmap.put("action", "List_Record_In_HSNCode");
			responsehsnmap.put("Data", hsnCodeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listHSNCodeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listHSNCodeNew");
		return responsehsnmap;
	}

	/* 4. SERVICECODEMASTER (ScreenName = Create ServiceCode) ----> END */

	/* 5. TYPEMASTER (ScreenName = Type Master) ----> START */
	@PostMapping("/master/addtypemasternew")
	public @ResponseBody Map<String, Object> createTypeMasterNew(@RequestParam String scrap_type,
			@RequestParam String scrap_name, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createTypeMasterNew");
		scrap_type = scrap_type.toUpperCase();
		scrap_name = scrap_name.toUpperCase();
		created_by = created_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING TYPE MASTER");
			int duplicateCount = masterRepository.checkForDuplicateScrapType(scrap_name);
			if (duplicateCount > 0) {
				response.put("message", "Type already exist");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_TypeMaster");

				return response;
			} else {
				int count = masterRepository.insertScrapTypeRecord(scrap_type, scrap_name, created_by, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_TypeMaster");
			}
			logger.info("EXECUTING METHOD :: AFTER ADDING TYPE MASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createTypeMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createTypeMasterNew");
		return response;
	}

	@PostMapping("/master/updatetypemasternew")
	public @ResponseBody Map<String, Object> updateTypeMasterNew(@RequestParam String scrap_type,
			@RequestParam String scrap_name, @RequestParam String modified_by, @RequestParam String type_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updatetypemasternew");
		scrap_type = scrap_type.toUpperCase();
		scrap_name = scrap_name.toUpperCase();
		modified_by = modified_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING TYPE MASTER");
			int check = masterRepository.checkUsedInQSPackingItemMaster(type_id);
			if (check > 0) {
				response.put("message", "The transaction already exist for Type Master");
				return response;
			}
			int duplicateCount = masterRepository.checkForDuplicateScrapTypeWithId(scrap_name, type_id);
			if (duplicateCount > 0) {
				response.put("message", "Type already exist");
				response.put("status", "no");
				response.put("action", "Update_Record_In_TypeMaster");
				return response;
			} else {
				int count = masterRepository.updateScrapTypeRecord(scrap_type, scrap_name, modified_by, type_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_TypeMaster");
			}
			logger.info("EXECUTING METHOD ::  AFTER UPDATING TYPE MASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updatetypemasternew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updatetypemasternew");
		return response;
	}

	@PostMapping("/master/deletetypemasternew")
	public @ResponseBody Map<String, Object> deleteTypeMasterNew(@RequestParam String modified_by,
			@RequestParam String type_id) {
		logger.info("EXECUTING METHOD :: deleteTypeMasterNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETING TYPE MASTER");
			int count = masterRepository.deleteScrapTypeRecord(modified_by, type_id);
			logger.info("EXECUTING METHOD :: updatetypemasternew");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_TypeMaster");
			logger.info("EXECUTING METHOD :: AFTER DELETING TYPE MASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteTypeMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteTypeMasterNew");
		return response;
	}

	@GetMapping("/master/searchtypemasternew")
	public @ResponseBody Map<String, Object> serachTypeMasterByIdNew(@RequestParam String type_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		TypeMasterInterface typeMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: serachTypeMasterByIdNew");
		try {
			typeMasterInterfaces = masterRepository.searchTypeMasterId(type_id);
			response.put("message", (typeMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (typeMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_TypeMaster");
			response.put("DATA", typeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachTypeMasterByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachTypeMasterByIdNew");
		return response;
	}

	@GetMapping("/master/listtypemasternew")
	public @ResponseBody Map<String, Object> listTypeMasterNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listTypeMasterNew");
		List<TypeMasterInterface> typeMasterInterfaces = null;
		try {
			typeMasterInterfaces = masterRepository.listScrapTypeRecords(factory_id);
			response.put("message", (typeMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (typeMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_TypeMaster");
			response.put("Data", typeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listTypeMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listTypeMasterNew");
		return response;
	}
	/* 5. TypeEMASTER (ScreenName = TypeMaster)----> END */

	/* 6. UNITMEASUREMENTMASTER (ScreenName = Unit Measurement)-----> START */
	@PostMapping("/master/addunitmeasurementmasternew")
	public @ResponseBody Map<String, Object> createUnitMeasurementMasterNew(@RequestParam String unit_name,
			@RequestParam String created_by, @RequestParam(required = false) String factory_id,
			@RequestParam String description) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: addunitmeasurementmasternew");
		unit_name = unit_name.toUpperCase();
		created_by = created_by.toUpperCase();
		description = description.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING UOM");
			int existingCount = masterRepository.checkWhetheUomExists(unit_name, description);
			if (existingCount > 0) {
				response.put("message", "UOM Already Exists");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_UnitMeasurementMaster");
				return response;
			} else {
				int count = masterRepository.insertInvoiceUnitRecord(unit_name, description, created_by, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_UnitMeasurementMaster");
			}
			logger.info("EXECUTING METHOD ::  AFTER ADDING UOM");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR addunitmeasurementmasternew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: addunitmeasurementmasternew");
		return response;
	}

	@PostMapping("/master/updateunitmeasurementmasternew")
	public @ResponseBody Map<String, Object> updateUnitMeasurementMaster(@RequestParam String unit_name,
			@RequestParam String modified_by, @RequestParam String unit_id, @RequestParam String description) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updateUnitMeasurementMaster");
		unit_name = unit_name.toUpperCase();
		modified_by = modified_by.toUpperCase();
		description = description.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING UOM");
//			String message = masterRepository.checkWhetherUOMIsUsedInTransactionsBeforeUpdate(unit_name, description,
//					unit_id);
//			if (message != null) {
//				response.put("message", message);
//				response.put("status", "no");
//				response.put("action", "Update_Record_In_UnitMeasurementMaster");
//				return response;
//			}
			logger.info(unit_id);
			int check = masterRepository.checkUomUsedInTransactions(unit_id);
			if (check > 0) {
				response.put("message", "The transaction already exist for  Uom");
				response.put("status", "no");
				response.put("action", "Update_Record_In_UnitMeasurementMaster");
				return response;
			} else {
				int existingCount = masterRepository.checkWhetheUomExist(unit_name, description, unit_id);
				if (existingCount > 0) {
					response.put("message", "UOM already exits");
					response.put("status", "no");
					response.put("action", "Update_Record_In_UnitMeasurementMaster");
					return response;
				} else {
					int count = masterRepository.updateInvoiceUnitRecord(unit_name, description, modified_by, unit_id);
					response.put("message", (count > 0) ? "Success" : "failure");
					response.put("status", (count > 0) ? "yes" : "no");
					response.put("action", "Update_Record_In_UnitMeasurementMaster");
				}
			}
			logger.info("EXECUTING METHOD :: AFTER UPDATING UOM ");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateUnitMeasurementMaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateUnitMeasurementMaster");
		return response;
	}

	@PostMapping("/master/deleteunitmeasurementmasternew")
	public @ResponseBody Map<String, Object> deleteUnitMeasurementMasterNew(@RequestParam String modified_by,
			@RequestParam String unit_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteUnitMeasurementMasterNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETEING UOM");
			String message = masterRepository.checkWhetherUOMIsUsedInTransactionsBeforeDelete(unit_id);
			if (message != null) {
				response.put("message", message);
				response.put("status", "no");
				response.put("action", "Delete_Record_In_UnitMeasurementMaster");
				return response;
			} else {
				int count = masterRepository.deleteInvoiceUnitRecord(modified_by, unit_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Delete_Record_In_UnitMeasurementMaster");
			}
			logger.info("EXECUTING METHOD ::  AFTER DELETEING UOM");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteUnitMeasurementMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteUnitMeasurementMasterNew");
		return response;
	}

	@GetMapping("/master/listunitmeasurementmasternew")
	public @ResponseBody Map<String, Object> listUnitMeasurementMasterNew(
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listUnitMeasurementMasterNew");
		List<UOMMasterInterface> uomMasterInterfaces = null;
		try {
			uomMasterInterfaces = masterRepository.listInvoiceUnitRecords(factory_id);
			response.put("message", (uomMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (uomMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_UnitMeasurementMaster");
			response.put("Data", uomMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listUnitMeasurementMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listUnitMeasurementMasterNew");
		return response;
	}

	@GetMapping("/master/searchunitmasternew")
	public @ResponseBody Map<String, Object> serachUnitMeasurementMasterByIdNew(@RequestParam String unitId) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: serachUnitMeasurementMasterByIdNew");
		UOMMasterInterface uomMasterInterfaces = null;
		try {
			uomMasterInterfaces = masterRepository.searchUOMMasterId(unitId);
			response.put("message", (uomMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (uomMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_UnitMeasurementMaste");
			response.put("DATA", uomMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachUnitMeasurementMasterByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachUnitMeasurementMasterByIdNew");
		return response;
	}

	/* UNITMEASUREMENTMASTER (ScreenName = Unit Measurement)-----> END */

	/* 7. VENDORFORSCRAPMASTER (ScreenName = Vendor for scrap)-----> START */
	@PostMapping("/master/addvendorforscrapmaster")
	public @ResponseBody Map<String, Object> createVendorForScrapMaster(@RequestParam String vendor_name,
			@RequestParam String vendor_state_id, @RequestParam String vendor_city, @RequestParam String vendor_desc,
			@RequestParam String created_by, @RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createVendorForScrapMaster");
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING VENDOR");
			int val = masterRepository.checkUniqueNameScrapVendor(vendor_name);
			if (val > 0) {
				response.put("message", "Vendor Name Should Be Unique");
				return response;
			}
			int count = masterRepository.insertScrapVendorRecord(vendor_name.toUpperCase(), vendor_state_id,
					vendor_city.toUpperCase(), vendor_desc.toUpperCase(), created_by.toUpperCase(), factory_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_VendorForScrapMaster");
			logger.info("EXECUTING METHOD ::  AFTER ADDING VENDOR");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createVendorForScrapMaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createVendorForScrapMaster");
		return response;
	}

	@PostMapping("/master/updatevendorforscrapmaster")
	public @ResponseBody Map<String, Object> updateVendorForScrapMaster(@RequestParam String vendor_name,
			@RequestParam String vendor_state_id, @RequestParam String vendor_city, @RequestParam String vendor_desc,
			@RequestParam String modified_by, @RequestParam String ven_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updateVendorForScrapMaster");
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING VENDOR");
			int val = masterRepository.checkUniqueNameScrapVendorWithId(vendor_name, ven_id);
			if (val > 0) {
				response.put("message", "Vendor Name Should Be Unique");
				return response;
			} else {
				int count = masterRepository.updateScrapVendorRecord(vendor_name.toUpperCase(), vendor_state_id,
						vendor_city.toUpperCase(), vendor_desc.toUpperCase(), modified_by.toUpperCase(), ven_id);
				logger.info("EXECUTING METHOD ::  BEFORE UPDATING VENDOR");
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_VendorForScrapMaster");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateVendorForScrapMaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateVendorForScrapMaster");
		return response;
	}

	@GetMapping("/master/listvendorforscrapmaster")
	public @ResponseBody Map<String, Object> listVendorForScrapMaster(
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<VendorForScrapMasterInterface> vendorForScrapMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: listVendorForScrapMaster");
		try {
			vendorForScrapMasterInterfaces = masterRepository.listScrapVendorRecord(factory_id);
			response.put("message", (vendorForScrapMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (vendorForScrapMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_VendorForScrapMaster");
			response.put("Data", vendorForScrapMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listVendorForScrapMaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listVendorForScrapMaster");
		return response;
	}

	@GetMapping("/master/searchvendorforscrapmaster")
	public @ResponseBody Map<String, Object> searchScrapVendorRecord(@RequestParam String ven_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		VendorForScrapMasterInterface vendorForScrapMasterInterface = null;
		logger.info("EXECUTING METHOD :: searchvendorforscrapmaster");
		try {
			vendorForScrapMasterInterface = masterRepository.searchScrapVendorRecordById(ven_id);
			response.put("message", (vendorForScrapMasterInterface != null) ? "Success" : "failure");
			response.put("status", (vendorForScrapMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_UnitMeasurementMaste");
			response.put("data", vendorForScrapMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR searchvendorforscrapmaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: searchvendorforscrapmaster");
		return response;
	}

	@PostMapping("/master/deletevendorforscrapmaster")
	public @ResponseBody Map<String, Object> deletevendorforscrapmaster(@RequestParam String modified_by,
			@RequestParam String ven_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deletevendorforscrapmaster");
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETE VENDOR");
			int count = masterRepository.deleteScrapVendorRecord(modified_by, ven_id);
			logger.info("EXECUTING METHOD :: AFTER DELETE VENDOR");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_UnitMeasurementMaster");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deletevendorforscrapmaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deletevendorforscrapmaster");
		return response;
	}

	/* 7. VENDORFORSCRAPMASTER (ScreenName = vendor for scrap)-----> END */
	/* 8. WORKORDERMASTER (ScreenName = work order)-----> START */
	@PostMapping("/master/addworkordermasternew")
	public @ResponseBody Map<String, Object> createWorkOrderMasterNew(@RequestParam String workorder_no,
			@RequestParam String work_date, @RequestParam String created_by,
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createWorkOrderMasterNew");
		workorder_no = workorder_no.toUpperCase();
		created_by = created_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING WORKORDER");
			int duplicateWorkOrderCount = masterRepository.checkForDuplicate(workorder_no);
			if (duplicateWorkOrderCount > 0) {
				response.put("message", "Work Order already exists");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_WorkOrderMaster");
				return response;
			} else {
				int count = masterRepository.insertWorkOrderRecord(workorder_no, work_date, created_by, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_WorkOrderMaster");
			}
			logger.info("EXECUTING METHOD :: AFTER ADDING WORKORDER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createWorkOrderMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createWorkOrderMasterNew");
		return response;
	}

	@PostMapping("/master/updateworkordermasternew")
	public @ResponseBody Map<String, Object> updateWorkOrderMasterNew(@RequestParam String workorder_no,
			@RequestParam String work_date, @RequestParam String modified_by, @RequestParam String work_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: updateWorkOrderMasterNew");
		int count = 0;
		workorder_no = workorder_no.toUpperCase();
		modified_by = modified_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING WORKORDER");
			String message = masterRepository.checkWorkOrderInTransactions(work_id);
			if (message != null) {
				response.put("message", message);
				response.put("status", "no");
				response.put("action", "Update_Record_In_WorkOrderMaster");
				return response;
			} else {
				int duplicateWorkOrderCount = masterRepository.checkForDuplicateWithId(workorder_no, work_id);
				if (duplicateWorkOrderCount > 0) {
					response.put("message", "WorkOrder already exists");
					response.put("status", "no");
					response.put("action", "Update_Record_In_WorkOrderMaster");
					return response;
				} else {
					count = masterRepository.updateWorkOrderRecord(workorder_no, work_date, modified_by, work_id);
					response.put("message", (count > 0) ? "Success" : "failure");
					response.put("status", (count > 0) ? "yes" : "no");
					response.put("action", "Update_Record_In_WorkOrderMaster");
				}
			}
			logger.info("EXECUTING METHOD :: AFTER UPDATING WORKORDER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateWorkOrderMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateWorkOrderMasterNew");
		return response;
	}

	@GetMapping("/master/listworkordermasternew")
	public @ResponseBody Map<String, Object> listWorkOrderMasterNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<WorkOrderMasterInterface> workOrderMasterInterfaces = null;
		logger.info("EXECUTING METHOD :: listWorkOrderMasterNew");
		try {
			workOrderMasterInterfaces = masterRepository.listWorkOrderRecords(factory_id);
			response.put("message", (workOrderMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (workOrderMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_WorkOrderMaster");
			response.put("Data", workOrderMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listWorkOrderMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listWorkOrderMasterNew");
		return response;
	}

	@GetMapping("/master/searchworkordermasternew")
	public @ResponseBody Map<String, Object> searchworkorderRecordNew(@RequestParam String work_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: searchworkorderRecordNew");
		WorkOrderMasterInterface workOrderMasterInterface = null;
		try {
			workOrderMasterInterface = masterRepository.searchWorkOrderById(work_id);
			response.put("message", (workOrderMasterInterface != null) ? "Success" : "failure");
			response.put("status", (workOrderMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_WorkOrderMaster");
			response.put("DATA", workOrderMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR searchworkorderRecordNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: searchworkorderRecordNew");
		return response;
	}

	@PostMapping("/master/deleteworkordermasternew")
	public @ResponseBody Map<String, Object> deleteWorkOrderMasterNew(@RequestParam String modified_by,
			@RequestParam String work_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteWorkOrderMasterNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETE WORKORDER");
			String message = masterRepository.checkWorkOrderInTransactionsDuringDelete(work_id);
			if (message != null) {
				response.put("message", message);
				response.put("status", "no");
				response.put("action", "Delete_Record_In_WorkOrderMaster");
				return response;
			} else {
				int count = masterRepository.deleteWorkOrderRecord(modified_by, work_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Delete_Record_In_WorkOrderMaster");
			}
			logger.info("EXECUTING METHOD :: AFTER DELETE WORKORDER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteWorkOrderMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteWorkOrderMasterNew");
		return response;
	}
	/* WORKORDERMASTER ----> END */
	/* REMARK INVOICE TYPE CRUD start ***/

	@GetMapping("/master/listinvoicetyperemarksnew") // INVOICE_TYPE table 10 hard code value
	public @ResponseBody Map<String, Object> listInvoiceTypeRemarksNew(
			@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<RemarksInvoiceTypeInterfaces> value = null;
		logger.info("EXECUTING METHOD :: listInvoiceTypeRemarksNew");
		try {
			value = masterRepository.listInvoiceRemarksType(factory_id);
			response.put("message", (value.size() > 0) ? "Success" : "failure");
			response.put("status", (value.size() > 0) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_TYPE_LIST");
			response.put("INVOICE_TYPE_LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listInvoiceTypeRemarksNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listInvoiceTypeRemarksNew");
		return response;
	}

	@PostMapping("/master/addinvoicetyperemarksnew")
	public @ResponseBody Map<String, Object> createInvoiceTypeRemarksNew(@RequestParam String remarks_type,
			@RequestParam String created_by, @RequestParam(required = false) String factory_id) {
		logger.info("EXECUTING METHOD :: createInvoiceTypeRemarksNew");
		Map<String, Object> response = new HashMap<String, Object>();
		remarks_type = remarks_type.toUpperCase();
		created_by = created_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING TYPEREMARKS");
			int duplicateCount = masterRepository.checkForDuplicateRemarksType(remarks_type);
			if (duplicateCount > 0) {
				response.put("message", "Invoice Remarks  Already Exists");
				response.put("status", "no");
				response.put("action", "Insert_Record_In_RemarkInvoiceType_Master");
				return response;
			} else {
				int count = masterRepository.insertRemarksType(remarks_type, created_by, factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_RemarkInvoiceType_Master");
			}
			logger.info("EXECUTING METHOD ::  AFTER ADDING TYPEREMARKS");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createInvoiceTypeRemarksNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createInvoiceTypeRemarksNew");
		return response;
	}

	@PostMapping("/master/updateinvoicetyperemarksnew")
	public @ResponseBody Map<String, Object> updateInvoiceTypeRemarksnewNew(@RequestParam String remarks_type,
			@RequestParam String modified_by, @RequestParam String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		int check = 0;
		logger.info("EXECUTING METHOD :: updateInvoiceTypeRemarksnewNew");
		remarks_type = remarks_type.toUpperCase();
		modified_by = modified_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING TYPEREMARKS");
			check = masterRepository.checkremarksIdPresentInQspackingItemMaster(slno);
			int checktranscationindelverychallan = masterRepository.checkinvoiceremarkInDeliveryChallan(slno);
			if (check > 0 || checktranscationindelverychallan >0) {
				response.put("message", "The transaction already exist for Invoice Remark");
				return response;
			}
			
			int duplicateCount = masterRepository.checkForDuplicateRemarksTypeWithId(remarks_type, slno);
			if (duplicateCount > 0) {
				response.put("message", "Invoice Remarks is Already exists");
				response.put("status", "no");
				response.put("action", "Update_Record_In_RemarkInvoiceType_Master");
				return response;
			} else {
				count = masterRepository.updateInvoiceTypeRemarks(remarks_type, modified_by, slno);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Update_Record_In_RemarkInvoiceType_Master");
			}
			logger.info("EXECUTING METHOD ::  AFTER UPDATING TYPEREMARKS");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateInvoiceTypeRemarksnewNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateInvoiceTypeRemarksnewNew");
		return response;
	}

	@GetMapping("/master/searchinvoicetyperemarksnew")
	public @ResponseBody Map<String, Object> searchInvoiceTypeRemarksNew(@RequestParam String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: searchInvoiceTypeRemarksNew");
		RemarksInvoiceTypeInterfaces workOrderMasterInterface = null;
		try {
			workOrderMasterInterface = masterRepository.searchInvoiceRemarksById(slno);
			response.put("message", (workOrderMasterInterface != null) ? "Success" : "failure");
			response.put("status", (workOrderMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_RemarkInvoiceType_Master");
			response.put("DATA", workOrderMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR searchInvoiceTypeRemarksNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: searchInvoiceTypeRemarksNew");
		return response;
	}

	@PostMapping("/master/deleteinvoicetyperemarksnew")
	public @ResponseBody Map<String, Object> deleteInvoiceTypeRemarksNew(@RequestParam String modified_by,
			@RequestParam String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteInvoiceTypeRemarksNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETEING TYPEREMARKS");
			int check = masterRepository.checkremarksIdPresentInQspackingItemMaster(slno);
			if (check > 0) {
				response.put("message", "Already used in Transactions not able to Delete");
				return response;
			}
			int count = masterRepository.deleteInvoiceRemarksById(modified_by, slno);
			logger.info("EXECUTING METHOD ::  AFTER DELETEING TYPEREMARKS");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_RemarkInvoiceType_Master");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteInvoiceTypeRemarksNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteInvoiceTypeRemarksNew");
		return response;
	}
	/* REMARK INVOICE TYPE CRUD end ***/

	/* INVOICE TYPE CRUD start ***/

	@GetMapping("/master/listinvoicetypenew")
	public @ResponseBody Map<String, Object> listInvoiceTypeNew(@RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceTypeInterface> value = null;
		logger.info("EXECUTING METHOD :: listInvoiceTypeNew");
		try {
			value = masterRepository.listInvoiceType(factory_id);
			response.put("message", (value.size() > 0) ? "Success" : "failure");
			response.put("status", (value.size() > 0) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_TYPE_LIST");
			response.put("INVOICE_TYPE_LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listInvoiceTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listInvoiceTypeNew");
		return response;
	}

	@PostMapping("/master/addinvoicetypenew")
	public @ResponseBody Map<String, Object> createInvoiceTypeNew(@RequestParam String type,
			@RequestParam String created_by, @RequestParam(required = false) String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: createInvoiceTypeNew");
		type = type.toUpperCase();
		created_by = created_by.toUpperCase();
		try {
			logger.info("Check duplicate for the Invoice type");

			int check_count = masterRepository.checkDuplicationInvoiceType(type);

			if (check_count > 0) {
				response.put("message", "Invoice Type is Already Exists");
				return response;
			}

			logger.info("EXECUTING METHOD :: BEFORE ADDING INVOICETYPE");
			int count = masterRepository.insertInvoiceType(type, created_by, factory_id);
			logger.info("EXECUTING METHOD :: AFTER ADDING INVOICETYPE");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_RemarkInvoiceType_Master");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createInvoiceTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createInvoiceTypeNew");
		return response;
	}

	@PostMapping("/master/updateinvoicetypenew")
	public @ResponseBody Map<String, Object> updateInvoiceTypenewNew(@RequestParam String type,
			@RequestParam String modified_by, @RequestParam String type_no) {
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		type = type.toUpperCase();
		modified_by = modified_by.toUpperCase();
		logger.info("EXECUTING METHOD :: updateInvoiceTypenewNew");
		try {

			int check_count = masterRepository.checkDuplicationInvoiceType(type);

			if (check_count > 0) {
				response.put("message", "Invoice Type is Already Exits");
				return response;
			}

			int transcation_count = masterRepository.checkinvoicetypetranscation(type_no);
			if (transcation_count > 0) {
				response.put("message", "The transaction already exist for Invoice type");
				return response;
			}

			logger.info("EXECUTING METHOD :: BEFORE UPDATE INVOICETYPE");
			count = masterRepository.updateInvoiceType(type, modified_by, type_no);
			logger.info("EXECUTING METHOD :: AFTER UPDATE INVOICETYPE");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_RemarkInvoiceType_Master");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateInvoiceTypenewNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateInvoiceTypenewNew");
		return response;
	}

	@GetMapping("/master/searchinvoicetypenew")
	public @ResponseBody Map<String, Object> searchInvoiceTypeNew(@RequestParam String type_no) {
		Map<String, Object> response = new HashMap<String, Object>();
		InvoiceTypeInterface workOrderMasterInterface = null;
		logger.info("EXECUTING METHOD :: searchInvoiceTypeNew");
		try {
			workOrderMasterInterface = masterRepository.searchInvoiceTypeById(type_no);
			response.put("message", (workOrderMasterInterface != null) ? "Success" : "failure");
			response.put("status", (workOrderMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_InvoiceType_Master");
			response.put("DATA", workOrderMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR searchInvoiceTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: searchInvoiceTypeNew");
		return response;
	}

	@PostMapping("/master/deleteinvoicetypenew")
	public @ResponseBody Map<String, Object> deleteInvoiceTypeNew(@RequestParam String modified_by,
			@RequestParam String type_no) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteInvoiceTypeNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETE INVOICETYPE");
			int count = masterRepository.deleteInvoiceTypeById(modified_by, type_no);
			logger.info("EXECUTING METHOD :: AFTER DELETE INVOICETYPE");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_InvoiceType_Master");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteInvoiceTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteInvoiceTypeNew");
		return response;
	}
	/* INVOICE TYPE CRUD end ***/

	/* BG TYPE MASTER START */

	@GetMapping("/master/listbgtypenew")
	public @ResponseBody Map<String, Object> listbgtypeNew() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<RemarksInvoiceTypeInterfaces> value = null;
		logger.info("EXECUTING METHOD :: listbgtypenew");
		try {
			value = masterRepository.listBGType();
			response.put("message", (value.size() > 0) ? "Success" : "failure");
			response.put("status", (value.size() > 0) ? "yes" : "no");
			response.put("action", "List_Record_In_BGTYPE_LIST");
			response.put("BGTYPE", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listbgtypenew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listbgtypenew");
		return response;
	}

	@PostMapping("/master/addbgtypenew")
	public @ResponseBody Map<String, Object> createBGTypeNew(@RequestParam String description,
			@RequestParam String created_by) {
		Map<String, Object> response = new HashMap<String, Object>();
		description = description.toUpperCase();
		created_by = created_by.toUpperCase();
		logger.info("EXECUTING METHOD :: createBGTypeNew");
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING BGTYPE");
			int val = masterRepository.checkDuplicateBgTYpeDesc(description);
			if (val > 0) {
				response.put("message", "BG Type Already Exits");
			} else {
				int count = masterRepository.insertBGType(description, created_by);
				logger.info("EXECUTING METHOD ::  AFTER ADDING BGTYPE");
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "Insert_Record_In_BGType_Master");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createBGTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createBGTypeNew");
		return response;
	}

	@PostMapping("/master/updatebgtypenew")
	public @ResponseBody Map<String, Object> updateBGTypeNew(@RequestParam String description,
			@RequestParam String modified_by, @RequestParam String bgid) {
		logger.info("EXECUTING METHOD :: updateBGTypeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		description = description.toUpperCase();
		modified_by = modified_by.toUpperCase();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATE BGTYPE");
//			String check = masterRepository.checkBGTypePresentInContractMaster(bgid);
//			if (check != null) {
//				response.put("message", check);
//				response.put("status", "no");
//				response.put("action", "Update_Record_In_BGType_Master");
//				return response;
//			}
			logger.info("the bgNo is"+bgid);
			int check  = masterRepository.checkBgNoUsed(bgid);
			if(check>0) {
				response.put("message", "The transaction already exist for BgType");
				response.put("status", "no");
				response.put("action", "Update_Record_In_BGType_Master");
				return response;
			}
			else {
				int val = masterRepository.checkDuplicateBgTYpeDescWithId(description, bgid);
				if (val > 0) {
					response.put("message", "BG Type Already Exists");
				} else {
					count = masterRepository.updateBGType(description, modified_by, bgid);
					response.put("message", (count > 0) ? "Success" : "failure");
					response.put("status", (count > 0) ? "yes" : "no");
					response.put("action", "Update_Record_In_BGType_Master");
				}

			}
			logger.info("EXECUTING METHOD :: BEFORE UPDATE BGTYPE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateBGTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateBGTypeNew");
		return response;
	}

	@GetMapping("/master/searchbgtypenew")
	public @ResponseBody Map<String, Object> searchBGTypeNew(@RequestParam String bgid) {
		Map<String, Object> response = new HashMap<String, Object>();
		RemarksInvoiceTypeInterfaces workOrderMasterInterface = null;
		logger.info("EXECUTING METHOD :: searchBGTypeNew");
		try {
			workOrderMasterInterface = masterRepository.searchBGTypeByBgId(bgid);
			response.put("message", (workOrderMasterInterface != null) ? "Success" : "failure");
			response.put("status", (workOrderMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_BGType_Master");
			response.put("DATA", workOrderMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR searchBGTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: searchBGTypeNew");
		return response;
	}

	@PostMapping("/master/deletebgtypenew")
	public @ResponseBody Map<String, Object> deleteBGTypeNew(@RequestParam String modified_by,
			@RequestParam String bgid) {
		logger.info("EXECUTING METHOD :: deleteBGTypeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETE BGTYPE");
			String check = masterRepository.checkBGTypePresentInContractMaster(bgid);
			if (check != null) {
				response.put("message", check);
				response.put("status", "no");
				response.put("action", "DELETE_Record_In_BGType_Master");
				return response;
			} else {
				int count = masterRepository.deleteBGTypebyBGId(modified_by, bgid);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "DELETE_Record_In_BGType_Master");

			}
			logger.info("EXECUTING METHOD :: AFTER DELETE BGTYPE");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteBGTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteBGTypeNew");
		return response;
	}

	/* BG TYPE MASTER END */

	@PostMapping("/master/addExportTitle")
	public @ResponseBody Map<String, Object> addExportTitle(@RequestParam String export_title,
			@RequestParam String created_by) {
		logger.info("EXECUTING METHOD :: addExportTitle");
		Map<String, Object> addExportTitlemap = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD :: BEFORE ADDING addExportTitle");
			int count = masterRepository.checkDuplicatesExportTitle(export_title);
			if (count > 0) {
				addExportTitlemap.put("message", "ExportTitle details  Already Exists");
				addExportTitlemap.put("status", "no");
				addExportTitlemap.put("action", "AddExportTitle");
				return addExportTitlemap;
			} else {
				export_title = export_title.toUpperCase();
				created_by = created_by.toUpperCase();
				int addExportTitleRecord = masterRepository.addExportTitle(export_title, created_by);
				addExportTitlemap.put("message", (addExportTitleRecord > 0) ? "Success" : "failure");
				addExportTitlemap.put("status", (addExportTitleRecord > 0) ? "yes" : "no");
				addExportTitlemap.put("action", "AddExportTitle");
			}
			logger.info("EXECUTING METHOD :: AFTER ADDING addExportTitle");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR addExportTitle ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: addExportTitle");
		return addExportTitlemap;
	}

	@PostMapping("/master/updateExportTitle")
	public @ResponseBody Map<String, Object> UpdateExportTitle(@RequestParam String export_title,
			@RequestParam String modified_by, @RequestParam String id) {
		logger.info("EXECUTING METHOD :: addExportTitle");
		Map<String, Object> addExportTitlemap = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATE UpdateExportTitle");
			String message = masterRepository.checkExportTitleinContactorMaster(id);
			if (message != null) {
				addExportTitlemap.put("message", message);
				addExportTitlemap.put("status", "no");
				addExportTitlemap.put("action", "UpdateExportTitle");
				return addExportTitlemap;
			} else {
				int count = masterRepository.checkDuplicatesExportTitle(export_title);
				if (count > 0) {
					addExportTitlemap.put("message", "ExportTitle details  Already Exists");
					addExportTitlemap.put("status", "no");
					addExportTitlemap.put("action", "AddExportTitle");
					return addExportTitlemap;
				} else {
					export_title = export_title.toUpperCase();
					modified_by = modified_by.toUpperCase();
					int addExportTitleRecord = masterRepository.updateExportTitle(export_title, modified_by, id);
					addExportTitlemap.put("message", (addExportTitleRecord > 0) ? "Success" : "failure");
					addExportTitlemap.put("status", (addExportTitleRecord > 0) ? "yes" : "no");
					addExportTitlemap.put("action", "UpdateExportTitle");
				}
			}
			logger.info("EXECUTING METHOD :: AFTER UPDATE UpdateExportTitle");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR UpdateExportTitle ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: UpdateExportTitle");
		return addExportTitlemap;

	}

	@PostMapping("/master/deleteExportTitle")
	public @ResponseBody Map<String, Object> deletExporteTitle(@RequestParam String modified_by,
			@RequestParam String id) {
		logger.info("EXECUTING METHOD :: deletExporteTitle");
		Map<String, Object> addExportTitlemap = new HashMap<String, Object>();
		try {
			logger.info("EXECUTING METHOD :: BEFORE DELETE deletExporteTitle");
			String message = masterRepository.checkExportTitleinContactorMaster(id);
			if (message != null) {
				addExportTitlemap.put("message", message);
				addExportTitlemap.put("status", "no");
				addExportTitlemap.put("action", "DeleteExportTitle");
				return addExportTitlemap;
			} else {
				int addExportTitleRecord = masterRepository.deleteExportTitle(modified_by, id);
				addExportTitlemap.put("message", (addExportTitleRecord > 0) ? "Success" : "failure");
				addExportTitlemap.put("status", (addExportTitleRecord > 0) ? "yes" : "no");
				addExportTitlemap.put("action", "DeleteExportTitle");
			}
			logger.info("EXECUTING METHOD :: AFTER DELETE deletExporteTitle");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deletExporteTitle ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deletExporteTitle");
		return addExportTitlemap;
	}

	@GetMapping("/master/allExportTitleList")
	public @ResponseBody Map<String, Object> getAllExportTitleList() {
		logger.info("EXECUTING METHOD :: getAllExportTitleList");
		Map<String, Object> getAllExportTitlemap = new HashMap<String, Object>();
		List<ExportTitleInterface> allExportTitlelist = null;
		try {
			allExportTitlelist = masterRepository.getAllExportTitle();
			getAllExportTitlemap.put("action", "ExportTitleList");
			getAllExportTitlemap.put("message",
					(allExportTitlelist.size() > 0) ? "Success" : "ExportTitle List not Available!");
			getAllExportTitlemap.put("status", (allExportTitlelist.size() > 0) ? "yes" : "no");
			if ((allExportTitlelist != null) && (!allExportTitlelist.isEmpty())) {
				getAllExportTitlemap.put("ExportTitleDetails", allExportTitlelist);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getAllExportTitleList ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getAllExportTitleList");
		return getAllExportTitlemap;
	}

	@GetMapping("/master/exportTitleListBasedOnId")
	public @ResponseBody Map<String, Object> getExportTitleListBasedOnId(@RequestParam String id) {
		logger.info("EXECUTING METHOD :: getExportTitleListBasedOnId");
		Map<String, Object> getAllExportTitlemap = new HashMap<String, Object>();
		ExportTitleInterface exportTitleBsdOnId = null;
		try {
			exportTitleBsdOnId = masterRepository.getAllExportTitleBasedOnId(id);
			getAllExportTitlemap.put("action", "ExportTitleInfoBsdOnId");
			getAllExportTitlemap.put("message",
					(exportTitleBsdOnId != null) ? "Success" : "ExportTitleListBsdOnId not Available");
			getAllExportTitlemap.put("status", (exportTitleBsdOnId != null) ? "yes" : "no");
			if (exportTitleBsdOnId != null) {
				ObjectMapper mapper = new ObjectMapper();
				getAllExportTitlemap
						.putAll(mapper.convertValue(exportTitleBsdOnId, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getExportTitleListBasedOnId ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getExportTitleListBasedOnId");
		return getAllExportTitlemap;
	}

}
