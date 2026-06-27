package com.JIMS.integration.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.SalesOrderTandCInterfaces;
import com.JIMS.integration.interfaces.ScrapItemInterfaces;
import com.JIMS.integration.interfaces.ScrapSalesLocationInterfaces;
import com.JIMS.integration.interfaces.ScrapSalesOrderInterfaces;
import com.JIMS.integration.interfaces.ScrapTypeInterfaces;
import com.JIMS.integration.repository.ScrapMasterRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class ScrapMasterController {

	@Autowired
	public ScrapMasterRepository scrapMasterRepository;

	/* SCRAPTYPE START */
	@GetMapping("/scraptypes/getScrapTypeCode")
	public @ResponseBody Map<String, Object> getNewUpdatedScrapTypeCode() {

		Map<String, Object> getNewSaleOrderCodemap = new HashMap<String, Object>();

		String startWord = "STN";
		int currentYear = LocalDate.now().getYear();
		String saleOrderCodePrefix = startWord + "/" + currentYear + "/"; 
		String sale_order_code = null;

		// Fetch the last increment value from the database
		String lastIncrement = scrapMasterRepository.getLastScrapTypeFromDatabase();

		System.out.println("Last increment is: " + lastIncrement);

		// If lastIncrement is null or empty, initialize the sale_order_code with the
		// first value
		if (lastIncrement == null || lastIncrement.isEmpty()) {
			sale_order_code = saleOrderCodePrefix + "000001";
		} else {
			// If lastIncrement exists, split it to get the increment part and increment it
			String[] parts = lastIncrement.split("/");
			int incrementNumber = Integer.parseInt(parts[2]);

			// Increment the number
			incrementNumber++;

			// Format the sale_order_code with the updated increment
			sale_order_code = saleOrderCodePrefix + String.format("%06d", incrementNumber); // Ensure 6-digit padding
		}

		// Prepare response map with the generated sale order code
		getNewSaleOrderCodemap.put("message",
				(sale_order_code != null && !sale_order_code.isEmpty()) ? "Success" : "SaleOrderCode Not Added");
		getNewSaleOrderCodemap.put("status", (sale_order_code != null && !sale_order_code.isEmpty()) ? "yes" : "no");
		getNewSaleOrderCodemap.put("action", "GetUpdatedSaleOrderCode");
		getNewSaleOrderCodemap.put("Sale_order_code", sale_order_code);

		return getNewSaleOrderCodemap;
	}


	@PostMapping("/scraptypes/add")
	public @ResponseBody Map<String, Object> createScrapTypes(@RequestParam String scrap_typecode,
			@RequestParam String scrap_typename, @RequestParam String created_by) {
		Map<String, Object> addScrapSaleScrapTypesMap = new HashMap<String, Object>();
		scrap_typecode = scrap_typecode.toUpperCase();
		scrap_typename = scrap_typename.toUpperCase();
		created_by = created_by.toUpperCase();

		try {

			int count = scrapMasterRepository.checkForDuplicates(scrap_typename);
			if (count > 0) {
				addScrapSaleScrapTypesMap.put("action", "AddScrapSaleScrapTypes");
				addScrapSaleScrapTypesMap.put("message", "Scrap type  Already Exists");
				addScrapSaleScrapTypesMap.put("status", "no");

				return addScrapSaleScrapTypesMap;
			} else {
				int scraptypescount = scrapMasterRepository.addScrapSaleScrapTypes(scrap_typecode, scrap_typename,
						created_by);
				addScrapSaleScrapTypesMap.put("action", "AddScrapSaleScrapTypes");
				addScrapSaleScrapTypesMap.put("message", (scraptypescount > 0) ? "Success" : "Failure");
				addScrapSaleScrapTypesMap.put("status", (scraptypescount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return addScrapSaleScrapTypesMap;
	}

	@PostMapping("/scraptypes/update")
	public @ResponseBody Map<String, Object> UpdateScrapSaleScrapTypes(@RequestParam String scrap_typecode,
			@RequestParam String scrap_typename, @RequestParam String modified_by, @RequestParam String stid) {
		Map<String, Object> addScrapSaleScrapTypesMap = new HashMap<String, Object>();

		scrap_typecode = scrap_typecode.toUpperCase();
		scrap_typename = scrap_typename.toUpperCase();
		modified_by = modified_by.toUpperCase();

		try {

			int count = scrapMasterRepository.checkForDuplicates(scrap_typename);
			if (count > 0) {
				addScrapSaleScrapTypesMap.put("action", "AddScrapSaleScrapTypes");
				addScrapSaleScrapTypesMap.put("message", "Scrap Type  Already Exists");
				addScrapSaleScrapTypesMap.put("status", "no");

				return addScrapSaleScrapTypesMap;
			
			}
			
			
			String message = scrapMasterRepository.checkScrapTypeInTransactions(stid);
			if (message != null) {
				addScrapSaleScrapTypesMap.put("action", "UpdateScrapSaleScrapTypes");
				addScrapSaleScrapTypesMap.put("message", message);
				addScrapSaleScrapTypesMap.put("status", "no");

				return addScrapSaleScrapTypesMap;

			} else {
				int scraptypescount = scrapMasterRepository.UpdateScrapSaleScrapTypes(stid, scrap_typecode,
						scrap_typename, modified_by);
				addScrapSaleScrapTypesMap.put("action", "UpdateScrapSaleScrapTypes");
				addScrapSaleScrapTypesMap.put("message", (scraptypescount > 0) ? "Success" : "Failure");
				addScrapSaleScrapTypesMap.put("status", (scraptypescount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return addScrapSaleScrapTypesMap;
	}

	@GetMapping("/scraptypes/list")
	public @ResponseBody Map<String, Object> listSaleScrapTypes() {
		Map<String, Object> getAllScrapSaleScrapTypesMap = new HashMap<String, Object>();
		List<ScrapTypeInterfaces> getAllScrapSaleScraplists = null;
		try {
			getAllScrapSaleScraplists = scrapMasterRepository.getAllScrapSaleScrapTypes();

			getAllScrapSaleScrapTypesMap.put("action", "GetAllScrapSaleScrapTypes");
			getAllScrapSaleScrapTypesMap.put("message", (getAllScrapSaleScraplists != null) ? "Success" : "Failure");
			getAllScrapSaleScrapTypesMap.put("status", (getAllScrapSaleScraplists != null) ? "yes" : "no");
			getAllScrapSaleScrapTypesMap.put("Lists", getAllScrapSaleScraplists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAllScrapSaleScrapTypesMap;
	}

	@GetMapping("/scraptypes/search")
	public @ResponseBody Map<String, Object> searchScrapTypesByid(@RequestParam String id) {
		Map<String, Object> getScrapSaleScrapTypesMap = new HashMap<String, Object>();
		ScrapTypeInterfaces getScrapSaleScrap = null;
		try {
			getScrapSaleScrap = scrapMasterRepository.getScrapSaleScrapTypesById(id);
			getScrapSaleScrapTypesMap.put("action", "GetScrapSaleScrapTypesById");
			getScrapSaleScrapTypesMap.put("message", (getScrapSaleScrap != null) ? "Success" : "Failure");
			getScrapSaleScrapTypesMap.put("status", (getScrapSaleScrap != null) ? "yes" : "no");
			getScrapSaleScrapTypesMap.put("ScrapSaleScrapType", getScrapSaleScrap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getScrapSaleScrapTypesMap;
	}

	@PostMapping("/scraptypes/delete")
	public @ResponseBody Map<String, Object> deleteScrapSaleScrapTypesByid(@RequestParam String modified_by,
			@RequestParam String stid) {
		Map<String, Object> deleteScrapSaleScrapTypesMap = new HashMap<String, Object>();
		try {

			String message = scrapMasterRepository.checkScrapTypeInTransactionsDuringDelete(stid);
			if (message != null) {
				deleteScrapSaleScrapTypesMap.put("action", "DeleteScrapSaleScrapTypesById");
				deleteScrapSaleScrapTypesMap.put("message", message);
				deleteScrapSaleScrapTypesMap.put("status", "no");

				return deleteScrapSaleScrapTypesMap;

			} else {
				int scraptypescount = scrapMasterRepository.deleteScrapSaleScrapTypesById(stid, modified_by);
				deleteScrapSaleScrapTypesMap.put("action", "DeleteScrapSaleScrapTypesById");
				deleteScrapSaleScrapTypesMap.put("message", (scraptypescount > 0) ? "Success" : "Failure");
				deleteScrapSaleScrapTypesMap.put("status", (scraptypescount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleteScrapSaleScrapTypesMap;
	}

	/* SCRAPTYPE END */

	/* SCRAPITEM START */


	@GetMapping("/scrapsalescrapitems/ScrapItemCode")
	public @ResponseBody Map<String, Object> getNewUpdatedScrapItemCode() {

		Map<String, Object> getNewSaleOrderCodemap = new HashMap<String, Object>();

		String startWord = "STIN";
		int currentYear = LocalDate.now().getYear();
		String saleOrderCodePrefix = startWord + "/" + currentYear + "/"; 
		String sale_order_code = null;

		// Fetch the last increment value from the database
		String lastIncrement = scrapMasterRepository.getLastScrapItemFromDatabase();

		System.out.println("Last increment is: " + lastIncrement);

		// If lastIncrement is null or empty, initialize the sale_order_code with the
		// first value
		if (lastIncrement == null || lastIncrement.isEmpty()) {
			sale_order_code = saleOrderCodePrefix + "000001";
		} else {
			// If lastIncrement exists, split it to get the increment part and increment it
			String[] parts = lastIncrement.split("/");
			int incrementNumber = Integer.parseInt(parts[2]);

			// Increment the number
			incrementNumber++;

			// Format the sale_order_code with the updated increment
			sale_order_code = saleOrderCodePrefix + String.format("%06d", incrementNumber); // Ensure 6-digit padding
		}

		// Prepare response map with the generated sale order code
		getNewSaleOrderCodemap.put("message",
				(sale_order_code != null && !sale_order_code.isEmpty()) ? "Success" : "SaleOrderCode Not Added");
		getNewSaleOrderCodemap.put("status", (sale_order_code != null && !sale_order_code.isEmpty()) ? "yes" : "no");
		getNewSaleOrderCodemap.put("action", "GetUpdatedSaleOrderCode");
		getNewSaleOrderCodemap.put("Sale_order_code", sale_order_code);

		return getNewSaleOrderCodemap;
	}



	@PostMapping("/scrapsalescrapitems/add")
	public @ResponseBody Map<String, Object> createScrapSaleScrapItems(@RequestParam String scraptypecode,
			@RequestParam String scrapitemcode, @RequestParam String scrapitemname, @RequestParam String created_by) {
		Map<String, Object> addscrapitemcount = new HashMap<String, Object>();
		int scrapitemcount = 0;

		scrapitemcode = scrapitemcode.toUpperCase();
		scrapitemname = scrapitemname.toUpperCase();
		created_by = created_by.toUpperCase();

		try {

			int duplicateCount = scrapMasterRepository.checkDuplicateItemCodeName(scrapitemname);
			if (duplicateCount > 0) {
				addscrapitemcount.put("action", "Addscrapitem");
				addscrapitemcount.put("message", "Scrap Item Already Exists");
				addscrapitemcount.put("status", "no");

				return addscrapitemcount;
			} else {
				scrapitemcount = scrapMasterRepository.addScrapSaleScrapItems(scraptypecode, scrapitemcode,
						scrapitemname, created_by);
				addscrapitemcount.put("action", "Addscrapitem");
				addscrapitemcount.put("message", (scrapitemcount > 0) ? "Success" : "Failure");
				addscrapitemcount.put("status", (scrapitemcount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return addscrapitemcount;
	}

	@PostMapping("/scrapsalescrapitems/update")
	public @ResponseBody Map<String, Object> UpdateScrapSaleScrapItems(@RequestParam String siid,
			@RequestParam String scraptypecode, @RequestParam String scrapitemcode, @RequestParam String scrapitemname,
			@RequestParam String modified_by) {
		Map<String, Object> updatescrapsalescrapitemsMap = new HashMap<String, Object>();

		scrapitemcode = scrapitemcode.toUpperCase();
		scrapitemname = scrapitemname.toUpperCase();
		modified_by = modified_by.toUpperCase();

		try {
			
			int duplicateCount = scrapMasterRepository.checkDuplicateItemCodeName(scrapitemname);
			if (duplicateCount > 0) {
				updatescrapsalescrapitemsMap.put("action", "Addscrapitem");
				updatescrapsalescrapitemsMap.put("message", "Scrap Item  Already Exists");
				updatescrapsalescrapitemsMap.put("status", "no");

				return updatescrapsalescrapitemsMap;
			}

			String message = scrapMasterRepository.checkScrapItemTransactions(
					 siid);
			if (message != null) {
				updatescrapsalescrapitemsMap.put("action", "UpdateScrapSaleScrapItems");
				updatescrapsalescrapitemsMap.put("message", message);
				updatescrapsalescrapitemsMap.put("status", "no");

				return updatescrapsalescrapitemsMap;
			} else {
				int scrapitemcount = scrapMasterRepository.UpdateScrapSaleScrapItems(siid, scraptypecode, scrapitemcode,
						scrapitemname, modified_by);
				updatescrapsalescrapitemsMap.put("action", "UpdateScrapSaleScrapItems");
				updatescrapsalescrapitemsMap.put("message", (scrapitemcount > 0) ? "Success" : "Failure");
				updatescrapsalescrapitemsMap.put("status", (scrapitemcount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return updatescrapsalescrapitemsMap;
	}

	@PostMapping("/scrapsalescrapitems/delete")
	private @ResponseBody Map<String, Object> deleteScrapItemsById(@RequestParam String modified_by,
			@RequestParam String siid) {
		Map<String, Object> getScrapSaleScrapItemsMap = new HashMap<String, Object>();

		try {
			String message = scrapMasterRepository.checkScrapItemTransactionsBeforeDelete(siid);
			if (message != null) {
				getScrapSaleScrapItemsMap.put("action", "DeleteScrapItemsById");
				getScrapSaleScrapItemsMap.put("message", message);
				getScrapSaleScrapItemsMap.put("status", "no");

				return getScrapSaleScrapItemsMap;
			} else {
				int taxcount = scrapMasterRepository.deleteScrapItemsById(siid, modified_by);
				getScrapSaleScrapItemsMap.put("action", "DeleteScrapItemsById");
				getScrapSaleScrapItemsMap.put("message", (taxcount > 0) ? "Success" : "Failure");
				getScrapSaleScrapItemsMap.put("status", (taxcount > 0) ? "yes" : "no");

			}

		} catch (Exception e) {

		}
		return getScrapSaleScrapItemsMap;
	}

	@GetMapping("scrapsalescrapitems/list")
	public @ResponseBody Map<String, Object> listScrapSaleScrapItems() {
		Map<String, Object> getAllScrapSaleScrapItemsMap = new HashMap<String, Object>();
		List<ScrapItemInterfaces> getAllScrapSaleScrapItemsLists = null;
		try {
			getAllScrapSaleScrapItemsLists = scrapMasterRepository.getAllScrapSaleScrapItems();
			getAllScrapSaleScrapItemsMap.put("action", "GetAllScrapSaleScrapItems");
			getAllScrapSaleScrapItemsMap.put("message",
					(getAllScrapSaleScrapItemsLists.size() > 0) ? "Success" : "Failure");
			getAllScrapSaleScrapItemsMap.put("status", (getAllScrapSaleScrapItemsLists.size() > 0) ? "yes" : "no");
			getAllScrapSaleScrapItemsMap.put("ListScrapSaleScrapItems", getAllScrapSaleScrapItemsLists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAllScrapSaleScrapItemsMap;
	}

	@GetMapping("/scrapsalesrapitems/search")
	private @ResponseBody Map<String, Object> getScrapItemsById(@RequestParam String id) {
		Map<String, Object> getScrapItemsByIdMap = new HashMap<String, Object>();
		try {
			ScrapItemInterfaces getScrapItemsByIdlists = scrapMasterRepository.getScrapItemsById(id);
			getScrapItemsByIdMap.put("action", "GetScrapItemsById");
			getScrapItemsByIdMap.put("message", (getScrapItemsByIdlists != null) ? "Success" : "Failure");
			getScrapItemsByIdMap.put("status", (getScrapItemsByIdlists != null) ? "yes" : "no");
			getScrapItemsByIdMap.put("scrapsalescrapitems", getScrapItemsByIdlists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getScrapItemsByIdMap;
	}
	/* SCRAPITEM END */

	/* SALSE ORDER TYPE START */

	@GetMapping("/salesordertype/getSaleOrderTypeCode")
	public @ResponseBody Map<String, Object> getNewUpdatedScrapOrderType() {

		Map<String, Object> getNewSaleOrderCodemap = new HashMap<String, Object>();

		String startWord = "SOT";
		int currentYear = LocalDate.now().getYear();
		String saleOrderCodePrefix = startWord + "/" + currentYear + "/"; 
		String sale_order_code = null;

		// Fetch the last increment value from the database
		String lastIncrement = scrapMasterRepository.getLastScrapSaleOrderTypeCode();

		System.out.println("Last increment is: " + lastIncrement);

		// If lastIncrement is null or empty, initialize the sale_order_code with the
		// first value
		if (lastIncrement == null || lastIncrement.isEmpty()) {
			sale_order_code = saleOrderCodePrefix + "000001";
		} else {
			// If lastIncrement exists, split it to get the increment part and increment it
			String[] parts = lastIncrement.split("/");
			int incrementNumber = Integer.parseInt(parts[2]);

			// Increment the number
			incrementNumber++;

			// Format the sale_order_code with the updated increment
			sale_order_code = saleOrderCodePrefix + String.format("%06d", incrementNumber); // Ensure 6-digit padding
		}

		// Prepare response map with the generated sale order code
		getNewSaleOrderCodemap.put("message",
				(sale_order_code != null && !sale_order_code.isEmpty()) ? "Success" : "SaleOrderCode Not Added");
		getNewSaleOrderCodemap.put("status", (sale_order_code != null && !sale_order_code.isEmpty()) ? "yes" : "no");
		getNewSaleOrderCodemap.put("action", "GetUpdatedSaleOrderCode");
		getNewSaleOrderCodemap.put("Sale_order_code", sale_order_code);

		return getNewSaleOrderCodemap;
	}

	@PostMapping("/salesordertype/add")
	private @ResponseBody Map<String, Object> createScrapOrderType(
	        @RequestParam String saleorder_typecode,
	        @RequestParam String saleorder_typename,
	        @RequestParam String created_by) {

	    Map<String, Object> response = new HashMap<>();

	    saleorder_typecode = saleorder_typecode.toUpperCase();
	    saleorder_typename = saleorder_typename.toUpperCase();
	    created_by = created_by.toUpperCase();

	    try {
	        int count = scrapMasterRepository.checkOrderTypeExist(saleorder_typename);
	        if (count > 0) {
	            response.put("action", "AddScrapSaleScrapTypes");
	            response.put("message", " Scrap Type  Already Exists");
	            response.put("status", "no");
	        } else {
	            int unitCount = scrapMasterRepository.addSalesOrder(saleorder_typecode, saleorder_typename, created_by);
	            response.put("message", (unitCount > 0) ? "Success" : "Failure");
	            response.put("status", (unitCount > 0) ? "yes" : "no");
	            response.put("action", "Add salesOrderDetails");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Error occurred while processing the request.");
	        response.put("status", "error");
	        response.put("action", "Add salesOrderDetails");
	    }

	    return response;
	}

	@PostMapping("/salesordertype/update")
	public @ResponseBody Map<String, Object> updateScrapOrderType(@RequestParam String saleorder_typecode,
			@RequestParam String saleorder_typename, @RequestParam String modified_by,
			@RequestParam String saleorder_id) {
		Map<String, Object> response = new HashMap<>();

		saleorder_typecode = saleorder_typecode.toUpperCase();
		saleorder_typename = saleorder_typename.toUpperCase();
		modified_by = modified_by.toUpperCase();

		try {

			
			 int count_type = scrapMasterRepository.checkOrderTypeExist(saleorder_typename);
		        if (count_type > 0) {
		            response.put("action", "AddScrapSaleScrapTypes");
		            response.put("message", " Scrap Type Already Exits");
		            response.put("status", "no");
		            return response;
		        }
			
			
			String message = scrapMasterRepository.checkSaleOrderInTransaction(
					saleorder_id);
			if (message != null) {

				response.put("message", message);
				response.put("status", "no");
				response.put("action", "UPDATE_Record_In_SalesMAster");

				return response;
			} else {
				int count = scrapMasterRepository.updateSalesorder(saleorder_typecode, saleorder_typename, modified_by,
						saleorder_id);
				response.put("message", (count > 0) ? "Success" : "Failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "UPDATE_Record_In_SalesMAster");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/salesordertype/delete")
	public @ResponseBody Map<String, Object> deleteScrapOrderType(@RequestParam String modified_by,
			@RequestParam String saleorder_id) {
		Map<String, Object> response = new HashMap<>();
		try {

			String message = scrapMasterRepository.checkSaleOrderInTransactionDuringDelete(saleorder_id);
			if (message != null) {

				response.put("message", message);
				response.put("status", "no");
				response.put("action", "DELETE_Record_In_SalesMASTER");

				return response;
			} else {
				int count = scrapMasterRepository.insertDeletedRecord(saleorder_id, modified_by);
				response.put("message", (count > 0) ? "Success" : "Failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "DELETE_Record_In_SalesMASTER");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/salesordertype/search")
	public @ResponseBody Map<String, Object> findSalesOrderById(@RequestParam String saleorder_id) {
		Map<String, Object> response = new HashMap<>();
		ScrapSalesOrderInterfaces salesOrderInterface = null;
		try {
			salesOrderInterface = scrapMasterRepository.findSalesOrder(saleorder_id);
			response.put("message", (salesOrderInterface != null) ? "Success" : "Failure");
			response.put("status", (salesOrderInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesMASTER");
			response.put("SalesOrderTypeInfo", salesOrderInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/salesordertype/list")
	public @ResponseBody Map<String, Object> findAllSalesOrder() {
		Map<String, Object> response = new HashMap<>();
		List<ScrapSalesOrderInterfaces> listSalesOrder = null;
		try {
			listSalesOrder = scrapMasterRepository.getAllSalesOrders();
			response.put("message", (listSalesOrder != null) ? "Success" : "Failure");
			response.put("status", (listSalesOrder != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesMASTER");
			response.put("SalesOrderTypeDetails", listSalesOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/* SALES ORDER ITEM END */
	/* SALES LOCATION START */

	@GetMapping("/scrapsalelocation/getScrapLocationCode")
	public @ResponseBody Map<String, Object> getNewUpdatedScrapLocationCode() {

		Map<String, Object> getNewSaleOrderCodemap = new HashMap<String, Object>();

		String startWord = "SCP";
		int currentYear = LocalDate.now().getYear();
		String saleOrderCodePrefix = startWord + "/" + currentYear + "/"; 
		String sale_order_code = null;

		// Fetch the last increment value from the database
		String lastIncrement = scrapMasterRepository.getLastScrapLocationCodeFromDatabase();

		System.out.println("Last increment is: " + lastIncrement);

		// If lastIncrement is null or empty, initialize the sale_order_code with the
		// first value
		if (lastIncrement == null || lastIncrement.isEmpty()) {
			sale_order_code = saleOrderCodePrefix + "000001";
		} else {
			// If lastIncrement exists, split it to get the increment part and increment it
			String[] parts = lastIncrement.split("/");
			int incrementNumber = Integer.parseInt(parts[2]);

			// Increment the number
			incrementNumber++;

			// Format the sale_order_code with the updated increment
			sale_order_code = saleOrderCodePrefix + String.format("%06d", incrementNumber); // Ensure 6-digit padding
		}

		// Prepare response map with the generated sale order code
		getNewSaleOrderCodemap.put("message",
				(sale_order_code != null && !sale_order_code.isEmpty()) ? "Success" : "SaleOrderCode Not Added");
		getNewSaleOrderCodemap.put("status", (sale_order_code != null && !sale_order_code.isEmpty()) ? "yes" : "no");
		getNewSaleOrderCodemap.put("action", "GetUpdatedSaleOrderCode");
		getNewSaleOrderCodemap.put("Sale_order_code", sale_order_code);

		return getNewSaleOrderCodemap;
	}

	@PostMapping("/scrapsalelocation/add")
	public @ResponseBody Map<String, Object> createLocationTrack(@RequestParam String location_code,
			@RequestParam String location_name, @RequestParam String created_by) {
		Map<String, Object> addlocationcount = new HashMap<String, Object>();
		int locationcount = 0;

		location_code = location_code.toUpperCase();
		location_name = location_name.toUpperCase();
		created_by = created_by.toUpperCase();

		try {

			int checkDuplicateRecord = scrapMasterRepository.checkForDuplicate(location_name);
			if (checkDuplicateRecord > 0) {
				addlocationcount.put("action", "Addlocationcount");
				addlocationcount.put("message", "Scrap location  Already Exists");
				addlocationcount.put("status", "no");

				return addlocationcount;
			} else {

				locationcount = scrapMasterRepository.addLocationTrack(location_code, location_name, created_by);
				addlocationcount.put("action", "Addlocationcount");
				addlocationcount.put("message", (locationcount > 0) ? "Success" : "Failure");
				addlocationcount.put("status", (locationcount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return addlocationcount;
	}

	@GetMapping("scrapsalelocation/list")
	public @ResponseBody Map<String, Object> getAllLocationTrack() {
		Map<String, Object> getAllLocationTrackMap = new HashMap<String, Object>();
		List<ScrapSalesLocationInterfaces> getAllLocationTrackLists = null;
		try {
			getAllLocationTrackLists = scrapMasterRepository.getAllLocationTrack();
			getAllLocationTrackMap.put("action", "GetAll");
			getAllLocationTrackMap.put("message", (getAllLocationTrackLists.size() > 0) ? "Success" : "Failure");
			getAllLocationTrackMap.put("status", (getAllLocationTrackLists.size() > 0) ? "yes" : "no");
			getAllLocationTrackMap.put("ListLocationTrack", getAllLocationTrackLists);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return getAllLocationTrackMap;
	}

	@GetMapping("/scrapsalelocation/search")
	private @ResponseBody Map<String, Object> getLocationTrackById(@RequestParam String slid) {
		Map<String, Object> getLocationTrackByIdMap = new HashMap<String, Object>();
		ScrapSalesLocationInterfaces getLocationTrackByIdLists = null;
		try {
			getLocationTrackByIdLists = scrapMasterRepository.getLocationTrackById(slid);
			getLocationTrackByIdMap.put("action", "GetLocationTrackById");
			getLocationTrackByIdMap.put("message", (getLocationTrackByIdLists != null) ? "Success" : "Failure");
			getLocationTrackByIdMap.put("status", (getLocationTrackByIdLists != null) ? "yes" : "no");
			getLocationTrackByIdMap.put("locationtrack", getLocationTrackByIdLists);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return getLocationTrackByIdMap;

	}

	@PostMapping("/scrapsalelocation/delete")
	private @ResponseBody Map<String, Object> deleteLocationTrackById(@RequestParam String modified_by,
			@RequestParam String slid) {
		Map<String, Object> getLocationTrackMap = new HashMap<String, Object>();

		try {

			String message = scrapMasterRepository.checkLocationInTransactionsDuringDelete(slid);
			if (message != null) {
				getLocationTrackMap.put("action", "DeleteLocationTrackById");
				getLocationTrackMap.put("message", message);
				getLocationTrackMap.put("status", "no");
			} else {
				// scrapMasterRepository.updateLocationHistory(slid, modified_by);
				int locationcount = scrapMasterRepository.deleteLocationTrackById(slid, modified_by);
				getLocationTrackMap.put("action", "DeleteLocationTrackById");
				getLocationTrackMap.put("message", (locationcount > 0) ? "Success" : "Failure");
				getLocationTrackMap.put("status", (locationcount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {

		}
		return getLocationTrackMap;
	}

	@PostMapping("/scrapsalelocation/update")
	public @ResponseBody Map<String, Object> UpdateLocationTrack(@RequestParam String location_code,
			@RequestParam String location_name, @RequestParam String modified_by, @RequestParam String slid) {
		Map<String, Object> updatelocationtrackMap = new HashMap<String, Object>();

		location_code = location_code.toUpperCase();
		location_name = location_name.toUpperCase();
		modified_by = modified_by.toUpperCase();

		try {
			
			int checkDuplicateRecord = scrapMasterRepository.checkForDuplicate(location_name);
			if (checkDuplicateRecord > 0) {
				updatelocationtrackMap.put("action", "Addlocationcount");
				updatelocationtrackMap.put("message", "Scrap location  Already Exists");
				updatelocationtrackMap.put("status", "no");

				return updatelocationtrackMap;
			} 
			
			String message = scrapMasterRepository.checkLocationInTransactions(slid);
			if (message != null) {
				updatelocationtrackMap.put("action", "UpdateLocationTrack");
				updatelocationtrackMap.put("message", message);
				updatelocationtrackMap.put("status", "no");
			} else {
				int locationcount = scrapMasterRepository.UpdateLocationTrack(slid, location_code, location_name,
						modified_by);
				updatelocationtrackMap.put("action", "UpdateLocationTrack");
				updatelocationtrackMap.put("message", (locationcount > 0) ? "Success" : "Failure");
				updatelocationtrackMap.put("status", (locationcount > 0) ? "yes" : "no");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return updatelocationtrackMap;
	}
	/* SALES LOCATION END */

	/* SALES T&C START */
	@PostMapping("/salesordertandc/add")

	private @ResponseBody Map<String, Object> createSalesTnC(@RequestParam String description,
			@RequestParam String created_by) {
		Map<String, Object> response = new HashMap<String, Object>();

		try {

			int count = scrapMasterRepository.addsalesTncDetailsDetails(description, created_by);

			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_salesordertandc");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return response;

	}

	@PostMapping("/salesordertandc/update")
	private @ResponseBody Map<String, Object> updateSalesTnC(@RequestParam String description,
			@RequestParam String modified_by, @RequestParam String tc_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {

			int count = scrapMasterRepository.updateSalesTncDetails(description, modified_by, tc_id);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_salesordertandc");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return response;

	}

	@GetMapping("/salesordertandc/search")
	public @ResponseBody Map<String, Object> findSalesOrderTnCById(@RequestParam String tcid) {
		Map<String, Object> response = new HashMap<>();
		SalesOrderTandCInterfaces salesOrderTandCInterface = null;
		try {
			salesOrderTandCInterface = scrapMasterRepository.findSalesOrderTnCById(tcid);
			response.put("message", (salesOrderTandCInterface != null) ? "Success" : "failure");
			response.put("status", (salesOrderTandCInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesTnCMASTER");
			response.put("SalesTnCDetails", salesOrderTandCInterface);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("action", "findSalesOrderTnCById");
			response.put("message", "An error occurred while retrieving the business unit.");
			response.put("status", "error");
		}

		return response;
	}

	@GetMapping("/salesordertandc/list")
	public @ResponseBody Map<String, Object> findAllSalesTandCOrder() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<SalesOrderTandCInterfaces> listSalesOrder = scrapMasterRepository.getAllSalesOrderTnc();
			response.put("message", (listSalesOrder != null) ? "Success" : "failure");
			response.put("status", (listSalesOrder != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesMASTER");
			response.put("SalesTnCDetailsList", listSalesOrder);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("action", "findAllSalesOrders");
			response.put("message", "An error occurred while retrieving the business unit.");
			response.put("status", "error");
		}
		return response;
	}

	@PostMapping("/salesordertandc/delete")
	public @ResponseBody Map<String, Object> deleteSalesTandCorder(@RequestParam String tcid,
			@RequestParam String modified_by) {
		Map<String, Object> response = new HashMap<>();
		try {

			int count = scrapMasterRepository.deleteSalesTncDetails(tcid, modified_by);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_SalesOrderTnCMaster");

		} catch (Exception e) {
			e.printStackTrace();

		}

		return response;
	}

	/* SALES T&C END */

}
