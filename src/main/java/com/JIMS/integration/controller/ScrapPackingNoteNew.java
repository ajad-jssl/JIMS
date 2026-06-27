package com.JIMS.integration.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.AllScrapingNoteDetails;
import com.JIMS.integration.interfaces.LatestScrapNoteBalanceInterface;
import com.JIMS.integration.interfaces.SaleOrderInterface;
import com.JIMS.integration.interfaces.SaleOrderItemDescLevelInterface;
import com.JIMS.integration.interfaces.SaleOrderItemLevelInterface;
import com.JIMS.integration.interfaces.SaleOrderItemTotalToleranceInfoInterface;
import com.JIMS.integration.interfaces.SalePackingNoteInterface;
import com.JIMS.integration.interfaces.SalePackingNoteItemsInterface;
import com.JIMS.integration.interfaces.ScrapInvoiceInfoInterface;
import com.JIMS.integration.interfaces.ScrapInvoiceItemsoInterface;
import com.JIMS.integration.interfaces.UnverifiedInvoiceListInterface;
import com.JIMS.integration.interfaces.VerifiedPackingNoteInterface;
import com.JIMS.integration.interfaces.VerifiedPackingNoteItemsInterface;
import com.JIMS.integration.interfaces.allSaleOrderEntriesInterface;
import com.JIMS.integration.repository.SaleOrderPackingNoteRepository;
import com.JIMS.integration.repository.ScrapInvoiceRepository;

@RestController
@RequestMapping("/jssl")
@CrossOrigin
public class ScrapPackingNoteNew {

	Logger logger = LogManager.getLogger(StateController.class);

	@Autowired
	private SaleOrderPackingNoteRepository saleOrderPackingNoterepo;

	@Autowired
	private ScrapInvoiceRepository scrapinvoicerepo;

	@GetMapping("/saleOrderEntry/getSaleOrderCode")
	public @ResponseBody Map<String, Object> getNewUpdatedSaleOrderCode() {

		logger.info("EXECUTING METHOD :: getNewUpdatedSaleOrderCode");

		Map<String, Object> getNewSaleOrderCodemap = new HashMap<String, Object>();

		int currentYear = LocalDate.now().getYear();
		String saleOrderCodePrefix = currentYear + "/";
		String sale_order_code = null;

		// Fetch the last increment value from the database
		String lastIncrement = saleOrderPackingNoterepo.getLastIncrementFromDatabase();

		System.out.println("Last increment is: " + lastIncrement);

		// If lastIncrement is null or empty, initialize the sale_order_code with the
		// first value
		if (lastIncrement == null || lastIncrement.isEmpty()) {
			sale_order_code = saleOrderCodePrefix + "000001";
		} else {
			// If lastIncrement exists, split it to get the increment part and increment it
			String[] parts = lastIncrement.split("/");
			int incrementNumber = Integer.parseInt(parts[1]);

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

		logger.info("FINISHING METHOD :: getNewUpdatedSaleOrderCode");

		return getNewSaleOrderCodemap;
	}

	@SuppressWarnings("unused")
	@PostMapping("/saleOrderEntry/addSaleOrder")
	public @ResponseBody Map<String, Object> addScrapSaleOrder(@RequestParam String sale_order_type_id,
			@RequestParam String sale_order_code, @RequestParam String location_type_id,
			@RequestParam String sale_order_to_id, @RequestParam String auction_date,
			@RequestParam String sale_order_duration, @RequestParam String advance_payment,
			@RequestParam String billing_address_id, @RequestParam String shipping_address_id,
			@RequestParam String business_unit_id, @RequestParam(required = false) String tax1,
			@RequestParam(required = false) String tax1_value,
			@RequestParam(required = false) String tax2, @RequestParam(required = false) String tax2_value,
			@RequestParam(required = false) String tax3,
			@RequestParam(required = false) String tax3_value, @RequestParam String net_amount,
			@RequestParam String total_tax,
			@RequestParam String grand_total, @RequestParam String created_by, @RequestParam String factory_id,
			@RequestParam List<String> auction_no, @RequestParam List<String> scrap_type_id,
			@RequestParam List<String> scrapitem_id, @RequestParam List<String> uom_id,
			@RequestParam List<String> quantity, @RequestParam List<String> kg, @RequestParam List<String> unit_price,
			@RequestParam List<String> total, @RequestParam List<String> tolerance,
			@RequestParam List<String> terms_conditions) {

		logger.info("EXECUTING METHOD :: addScrapSaleOrder");

		Map<String, Object> addScrapSaleOrdermap = new HashMap<String, Object>();
		int soe_id = 0;
		int insertIntoSaleOrderItemLevelTable = 0;
		int insertToSaleOrderDescriptiontable = 0;
		try {

			int insertToSaleOrderEntryTable = saleOrderPackingNoterepo.insertIntoSaleOrderEntryTable(sale_order_type_id,
					sale_order_code, location_type_id, sale_order_to_id, auction_date, sale_order_duration,
					advance_payment, billing_address_id, shipping_address_id, business_unit_id, tax1, tax1_value, tax2,
					tax2_value, tax3, tax3_value, net_amount, total_tax, grand_total, created_by, factory_id);

			soe_id = saleOrderPackingNoterepo.getSoe_idFromTable(sale_order_code);

			for (int i = 0; i < scrapitem_id.size(); i++) {

				insertIntoSaleOrderItemLevelTable = saleOrderPackingNoterepo.insertIntoSaleOrderItemTable(soe_id,
						auction_no.get(i), scrap_type_id.get(i), scrapitem_id.get(i), uom_id.get(i), quantity.get(i),
						kg.get(i), unit_price.get(i), total.get(i), tolerance.get(i), created_by);

			}

			int sl_no = 1;
			for (int i = 0; i < terms_conditions.size(); i++) {

				insertToSaleOrderDescriptiontable = saleOrderPackingNoterepo.insertIntoSaleOrderDescriptiontable(soe_id,
						sl_no, terms_conditions.get(i), created_by);

				sl_no++;

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: addScrapSaleOrder  -> " + e.getMessage());
		}

		addScrapSaleOrdermap.put("message",
				(insertIntoSaleOrderItemLevelTable > 0 && insertToSaleOrderDescriptiontable > 0) ? "Success"
						: "SaleOrderEntry Not Added");
		addScrapSaleOrdermap.put("status",
				(insertIntoSaleOrderItemLevelTable > 0 && insertToSaleOrderDescriptiontable > 0) ? "yes" : "no");
		addScrapSaleOrdermap.put("action", "AddSaleOrderEntry");

		return addScrapSaleOrdermap;

	}

	@SuppressWarnings("unused")
	@PostMapping("/saleOrderEntry/updateSaleOrderNew")
	public @ResponseBody Map<String, Object> updateScrapSaleOrder(@RequestParam String sale_order_type_id,
			@RequestParam String location_type_id, @RequestParam String sale_order_to_id,
			@RequestParam String auction_date, @RequestParam String sale_order_duration,
			@RequestParam String advance_payment, @RequestParam String billing_address_id,
			@RequestParam String shipping_address_id, @RequestParam String business_unit_id, @RequestParam String tax1,
			@RequestParam String tax1_value, @RequestParam String tax2, @RequestParam String tax2_value,
			@RequestParam String tax3, @RequestParam String tax3_value, @RequestParam String net_amount,
			@RequestParam String total_tax, @RequestParam String grand_total, @RequestParam String modified_by,
			@RequestParam String auction_no, @RequestParam String scrap_type_id, @RequestParam String scrapitem_id,
			@RequestParam String uom_id, @RequestParam String quantity, @RequestParam String kg,
			@RequestParam String unit_price, @RequestParam String total, @RequestParam String tolerance,
			@RequestParam List<String> terms_conditions, @RequestParam String sale_order_code,
			@RequestParam String soe_id, @RequestParam String soei_id, @RequestParam List<String> sl_no) {

		logger.info("EXECUTING METHOD :: updateScrapSaleOrder");

		Map<String, Object> updateScrapSaleOrdermap = new HashMap<String, Object>();
		// int soe_id = 0;

		int updateSaleOrderDescriptionEntry = 0;
		try {

			int insertToSaleOrderEntryTable = saleOrderPackingNoterepo.updateSaleOrderEntryTable(sale_order_type_id,
					location_type_id, sale_order_to_id, auction_date, sale_order_duration, advance_payment,
					billing_address_id, shipping_address_id, business_unit_id, tax1, tax1_value, tax2, tax2_value, tax3,
					tax3_value, net_amount, total_tax, grand_total, modified_by, sale_order_code, soe_id);

			int updateSaleOrderEntryTable = saleOrderPackingNoterepo.updateSaleOrderItemsTable(auction_no,
					scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, tolerance, modified_by,
					soe_id, soei_id);

			int getMaxSlforSoe_id = saleOrderPackingNoterepo.getMaxSlNo(soe_id);

			for (int i = 0; i < terms_conditions.size(); i++) {

				String slNum = sl_no.get(i);

				boolean exists = saleOrderPackingNoterepo.existsBySoeIdAndSlNo(soe_id, sl_no.get(i)) > 0;

				if (exists) {
					// Update existing record
					updateSaleOrderDescriptionEntry = saleOrderPackingNoterepo
							.updateSaleOrderDescription(terms_conditions.get(i), modified_by, soe_id, sl_no.get(i));
				} else {
					// Insert new record
					int insertToSaleOrderDescriptionTable = saleOrderPackingNoterepo.insertSaleOrderDescription(soe_id,
							sl_no.get(i), terms_conditions.get(i), modified_by);
				}
			}

			updateScrapSaleOrdermap.put("status", "Success");
			updateScrapSaleOrdermap.put("message",
					(updateSaleOrderDescriptionEntry > 0) ? "Sale order updated successfully" : "Failure");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateScrapSaleOrder  -> " + e.getMessage());

		}

		return updateScrapSaleOrdermap;

	}

	@GetMapping("/saleOrderEntry/getSaleOrderBasedOnFactoryId")
	public @ResponseBody Map<String, Object> getAllSaleOrderEntries(@RequestParam String factory_id) {

		logger.info("EXECUTING METHOD :: getAllSaleOrderEntries");

		Map<String, Object> getAllSaleOrderEntriesMap = new HashMap<String, Object>();
		List<allSaleOrderEntriesInterface> allSaleOrderList = null;

		try {
			allSaleOrderList = saleOrderPackingNoterepo.getAllSaleOrderDetailsBasedOnFactory(factory_id);

			getAllSaleOrderEntriesMap.put("action", "AllSaleOrderDetails");
			getAllSaleOrderEntriesMap.put("message",
					(allSaleOrderList.size() > 0) ? "Success" : "Sale Order details not found!");
			getAllSaleOrderEntriesMap.put("status", (allSaleOrderList.size() > 0) ? "yes" : "no");

			if ((allSaleOrderList != null) && (!allSaleOrderList.isEmpty())) {
				getAllSaleOrderEntriesMap.put("SaleOrderDetails", allSaleOrderList);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllSaleOrderEntries  -> " + e.getMessage());
		}
		return getAllSaleOrderEntriesMap;

	}

	@SuppressWarnings("unused")
	@GetMapping("/saleOrderEntry/getSaleOrderBasedOnIdNew")
	public @ResponseBody Map<String, Object> getSaleOrderDetailsBasedOnIdNew(@RequestParam String sale_order_code) {

		logger.info("EXECUTING METHOD :: getSaleOrderDetailsBasedOnIdNew");

		Map<String, Object> saleOrderByIdmap = new HashMap<String, Object>();
		List<SaleOrderInterface> saleOrderlist = new ArrayList<SaleOrderInterface>();
		List<SaleOrderItemLevelInterface> saleOrderItemLevellist = new ArrayList<SaleOrderItemLevelInterface>();
		List<SaleOrderItemDescLevelInterface> saleOrderDescLevellist = new ArrayList<SaleOrderItemDescLevelInterface>();
		List<LatestScrapNoteBalanceInterface> scrapnotebalance = null;
		LatestScrapNoteBalanceInterface initialBalancedata = null;
		SaleOrderItemTotalToleranceInfoInterface saleOrderNoteItemSaleableInfo = null;
		try {
			   String grandTotalValue = null;
			String soe_id = saleOrderPackingNoterepo.getSoeIdBasedOnSaleOrderCode(sale_order_code);

			// int countOfSaleOrderCode =
			// saleOrderPackingNoterepo.getCountOfSaleOrderCode(sale_order_code);
			saleOrderlist = saleOrderPackingNoterepo.getAllSaleOrderDetailsBAsedOnId(soe_id);
		    SaleOrderInterface saleOrder = saleOrderlist.get(0);   // ✅ get first object

	        grandTotalValue = saleOrder.getGrand_total();
			saleOrderItemLevellist = saleOrderPackingNoterepo.getSaleOrderItemLevelDetails(soe_id);
			saleOrderDescLevellist = saleOrderPackingNoterepo.getSaleOrderDescriptionLevelDetails(soe_id);
			scrapnotebalance = saleOrderPackingNoterepo.getLastestBalanceInfo(sale_order_code);
			String grandTotalInWords = saleOrderPackingNoterepo.getgrandTotalInWordsQuery(grandTotalValue);

			saleOrderByIdmap.put("orderLevel", saleOrderlist);
			saleOrderByIdmap.put("orderItemLevel", saleOrderItemLevellist);
			saleOrderByIdmap.put("orderDescriptionLevel", saleOrderDescLevellist);
			saleOrderByIdmap.put("balanceInfo", scrapnotebalance);
			saleOrderByIdmap.put("Grand_Total_value", grandTotalInWords);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getSaleOrderDetailsBasedOnIdNew  -> " + e.getMessage());
		}
		return saleOrderByIdmap;

	}

	@GetMapping("/saleOrderEntry/getAllSaleOrderWithDetails")
	public @ResponseBody Map<String, Object> getAllSaleOrderDetails(@RequestParam String factory_id) {

		logger.info("EXECUTING METHOD :: getAllSaleOrderDetails");

		Map<String, Object> allSaleOrderDetailsmap = new HashMap<String, Object>();
		List<SaleOrderInterface> saleOrderlist = new ArrayList<SaleOrderInterface>();
		List<SaleOrderItemLevelInterface> saleOrderItemLevellist = new ArrayList<SaleOrderItemLevelInterface>();
		List<SaleOrderItemDescLevelInterface> saleOrderDescLevellist = new ArrayList<SaleOrderItemDescLevelInterface>();
		List<String> sale_order_codes = null;
		try {

			sale_order_codes = saleOrderPackingNoterepo.getAllSaleOrderCodes(factory_id);

			for (String saleOrderCode : sale_order_codes) {
				String soe_id = saleOrderPackingNoterepo.getSoeIdBasedOnSaleOrderCode(saleOrderCode);

				saleOrderlist = saleOrderPackingNoterepo.getAllSaleOrderDetailsBAsedOnId(soe_id);
				saleOrderItemLevellist = saleOrderPackingNoterepo.getSaleOrderItemLevelDetails(soe_id);
				saleOrderDescLevellist = saleOrderPackingNoterepo.getSaleOrderDescriptionLevelDetails(soe_id);

				Map<String, Object> saleOrderDetails = new HashMap<>();

				saleOrderDetails.put("orderLevel", saleOrderlist);
				saleOrderDetails.put("orderItemLevel", saleOrderItemLevellist);
				saleOrderDetails.put("orderDescriptionLevel", saleOrderDescLevellist);
				allSaleOrderDetailsmap.put(saleOrderCode, saleOrderDetails);
				allSaleOrderDetailsmap.put("All Sale Order Details", saleOrderDetails);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllSaleOrderDetails  -> " + e.getMessage());

		}
		return allSaleOrderDetailsmap;

	}

	@SuppressWarnings("unused")
	@PostMapping("/saleOrderPackingNote/addSaleOrderPackingNoteNew")
	public @ResponseBody Map<String, Object> addScrapPackingNote(@RequestParam String sale_order_code,
			@RequestParam String total_steel_qty, @RequestParam String total_non_steel_qty,
			@RequestParam String sale_order_validity, @RequestParam String vendor_id,
			@RequestParam String weighbridge_no, @RequestParam String transportername, @RequestParam String vehicle_no,
			@RequestParam String scp_pn_date, @RequestParam String created_by, @RequestParam String factory_id,
			@RequestParam String location_type_id, @RequestParam String sale_order_date,
			@RequestParam List<String> scrap_type_id, @RequestParam List<String> scrapitem_id,
			@RequestParam List<String> uom_id, @RequestParam List<String> quantity, @RequestParam List<String> kg,
			@RequestParam List<String> unit_price, @RequestParam List<String> total, @RequestParam String total_ordered,
			@RequestParam String total_sold, @RequestParam String balance) {

		logger.info("EXECUTING METHOD :: addScrapPackingNote");

		Map<String, Object> addScrapPackingNotemap = new HashMap<String, Object>();
		int insertIntoScrapPackingNoterec = 0;
		int insertIntoPackingNoteItemsrec = 0;
		String newScpLoad = "SCP-00001";
		try {

			// rowCount = saleOrderPackingNoterepo.chekcRowCount();

			String lastScpLoad = saleOrderPackingNoterepo.findLastScpLoad(); // Implement this query

			if (lastScpLoad != null) {
				int numericPart = Integer.parseInt(lastScpLoad.replace("SCP-", ""));

				System.out.println("numeric part : " + numericPart);
				numericPart++;

				int digitLength = lastScpLoad.split("-")[1].length();

				newScpLoad = String.format("SCP-%0" + digitLength + "d", numericPart);
			}

			insertIntoScrapPackingNoterec = saleOrderPackingNoterepo.insertIntoScrapPackingNoteTable(sale_order_code,
					newScpLoad, total_steel_qty, total_non_steel_qty, sale_order_validity, vendor_id, weighbridge_no,
					transportername, vehicle_no, scp_pn_date, created_by, factory_id, location_type_id,
					sale_order_date);

			int scp_pn_id = saleOrderPackingNoterepo.getScp_pn_id(sale_order_code);

			for (int i = 0; i < scrapitem_id.size(); i++) {
				insertIntoPackingNoteItemsrec = saleOrderPackingNoterepo.insertIntoScrapPackingNoteItems(scp_pn_id,
						sale_order_code, newScpLoad, scrap_type_id.get(i), scrapitem_id.get(i), uom_id.get(i),
						quantity.get(i), kg.get(i), unit_price.get(i), total.get(i), total_ordered, total_sold,
						balance);
			}

			addScrapPackingNotemap.put("message",
					(insertIntoScrapPackingNoterec > 0) ? "Success" : "Country Not Added");
			addScrapPackingNotemap.put("status", (insertIntoScrapPackingNoterec > 0) ? "yes" : "no");
			addScrapPackingNotemap.put("action", "AddScrapPackingNoteAndItems");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: addScrapPackingNote  -> " + e.getMessage());

		}

		return addScrapPackingNotemap;

	}

	@SuppressWarnings("unused")
	@PostMapping("/saleOrderPackingNote/updateSaleOrderPackingNoteNew")
	public @ResponseBody Map<String, Object> updateScrapPackingNoteDetails(@RequestParam String sale_order_code,
			@RequestParam String scp_load, @RequestParam String total_steel_qty,
			@RequestParam String total_non_steel_qty, @RequestParam String sale_order_validity,
			@RequestParam String vendor_id, @RequestParam String weighbridge_no, @RequestParam String transportername,
			@RequestParam String vehicle_no, @RequestParam String scp_pn_date, @RequestParam String project_reference,
			@RequestParam String central_excise_tarrif_no, @RequestParam String other_reference,
			@RequestParam String credit_reference, @RequestParam String modified_by, @RequestParam String location_type,
			@RequestParam String sale_order_date, @RequestParam List<String> scrap_type_id,
			@RequestParam List<String> scrapitem_id, @RequestParam List<String> uom_id,
			@RequestParam List<String> quantity, @RequestParam List<String> kg, @RequestParam List<String> unit_price,
			@RequestParam List<String> total, @RequestParam String total_ordered, @RequestParam String total_sold,
			@RequestParam String balance, @RequestParam String scp_pn_id, @RequestParam List<String> scp_pnitems_id) {

		logger.info("EXECUTING METHOD :: updateScrapPackingNoteDetails");

		Map<String, Object> updateScrapPackingNotemap = new HashMap<String, Object>();
		int updateScrapPackingNoteDetails = 0;
		int updateScrapPackingNoteItemDetails = 0;
		try {

			updateScrapPackingNoteDetails = saleOrderPackingNoterepo.updateScrapPackingNoteDetails(total_steel_qty,
					total_non_steel_qty, sale_order_validity, vendor_id, weighbridge_no, transportername, vehicle_no,
					scp_pn_date, project_reference, central_excise_tarrif_no, other_reference, credit_reference,
					modified_by, location_type, sale_order_date, scp_load);

			for (int i = 0; i < scrapitem_id.size(); i++) {

				int updateScrapPackingNoteItemDetailsrec = saleOrderPackingNoterepo.updateScrapPackingNoteItems(
						scrap_type_id.get(i), scrapitem_id.get(i), uom_id.get(i), quantity.get(i), kg.get(i),
						unit_price.get(i), total.get(i), total_ordered, total_sold, balance, scp_pn_id,
						scp_pnitems_id.get(i));

			}

			updateScrapPackingNotemap.put("message",
					(updateScrapPackingNoteDetails > 0) ? "Success" : "Scrap Packing Note not updated");
			updateScrapPackingNotemap.put("status", (updateScrapPackingNoteDetails > 0) ? "yes" : "no");
			updateScrapPackingNotemap.put("action", "UpdateScrapPackingNoteAndItems");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateScrapPackingNoteDetails  -> " + e.getMessage());

		}

		return updateScrapPackingNotemap;

	}

	@GetMapping("/saleOrderPackingNote/packingNoteInfoForView")
	public @ResponseBody Map<String, Object> getScrapPackigNoteForView(@RequestParam String scp_load) {

		logger.info("EXECUTING METHOD :: getScrapPackigNoteForView");

		Map<String, Object> getScrapPackingNoteForViewmap = new HashMap<String, Object>();
		VerifiedPackingNoteInterface viewPackingNoteInterface = null;
		try {

			viewPackingNoteInterface = saleOrderPackingNoterepo.getInfoFromVerifiedPackingNoteTable(scp_load);

			getScrapPackingNoteForViewmap.put("message",
					(viewPackingNoteInterface != null) ? "Success" : "PackingNoteDetailsForView Not Available");
			getScrapPackingNoteForViewmap.put("status", (viewPackingNoteInterface != null) ? "yes" : "no");
			getScrapPackingNoteForViewmap.put("action", "PackingNoteInfoForView");
			getScrapPackingNoteForViewmap.put("PackiingNoteForView", viewPackingNoteInterface);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getScrapPackigNoteForView  -> " + e.getMessage());
		}
		return getScrapPackingNoteForViewmap;
	}

	@SuppressWarnings("unused")
	@GetMapping("/saleOrderPackingNote/getPackingNoteScpLoad")
	public @ResponseBody Map<String, Object> getScrapPackingNoteByIdMap(@RequestParam String scp_load) {

		logger.info("EXECUTING METHOD :: getScrapPackingNoteByIdMap");

		Map<String, Object> getScrapPackingNoteByScpLoadmap = new HashMap<String, Object>();
		SalePackingNoteInterface salepackingnoteinterface = null;
		SalePackingNoteItemsInterface salepackingnoteitemsinterface = null;
		String sale_order_code = null;
		LatestScrapNoteBalanceInterface scrapnotebalance = null;
		List<SalePackingNoteItemsInterface> salepackingnoteitemslist = null;

		try {

			sale_order_code = saleOrderPackingNoterepo.getSale_order_code(scp_load);

			int countOfSaleOrderCode = saleOrderPackingNoterepo.getCountOfSaleOrderCode(sale_order_code);

			if (countOfSaleOrderCode == 1) {
				scrapnotebalance = saleOrderPackingNoterepo.getBalanceDuringFirstUpdate(sale_order_code);
			} else {
				scrapnotebalance = saleOrderPackingNoterepo.getLastestBalanceInfoForScpload(sale_order_code);

			}

			salepackingnoteinterface = saleOrderPackingNoterepo.getInfoFromScrapPackingNoteTable(scp_load);
			sale_order_code = saleOrderPackingNoterepo.getSaleOrderCodeForScp_load(scp_load);
			salepackingnoteitemslist = saleOrderPackingNoterepo.getScrapPackingNoteItemsDetailsForScp_load(scp_load);

			// Add status, action, and message before the data
			getScrapPackingNoteByScpLoadmap.put("action", "ScrapPackingNoteDetailsBasdOnLoad");
			getScrapPackingNoteByScpLoadmap.put("message", (salepackingnoteinterface != null) ? "Success"
					: "Sale Order Scrap Packing Note details Based On Load not found!");
			getScrapPackingNoteByScpLoadmap.put("status", (salepackingnoteinterface != null) ? "yes" : "no");

			// Add the list or interface details after
			getScrapPackingNoteByScpLoadmap.put("PackingNoteDetails", salepackingnoteinterface);
			getScrapPackingNoteByScpLoadmap.put("PackingNoteItemDetails", salepackingnoteitemslist);
			getScrapPackingNoteByScpLoadmap.put("BalanceInfo", scrapnotebalance);

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getScrapPackingNoteByIdMap  -> " + e.getMessage());

		}

		return getScrapPackingNoteByScpLoadmap;
	}

	@PostMapping("/saleOrderPackingNote/verifyScrapPackingNoteNew")
	public @ResponseBody Map<String, Object> verifyScrapPackingNote(@RequestParam String scp_load,
			@RequestParam String vendor_id, @RequestParam String weighbridge_no, @RequestParam String transportername,
			@RequestParam String vehicle_no, @RequestParam String scp_pn_date, @RequestParam String verified_by,
			@RequestParam String remarks, @RequestParam List<String> quantity, @RequestParam List<String> kg,
			@RequestParam List<String> unit_price, @RequestParam List<String> total, @RequestParam String total_ordered,
			@RequestParam String total_sold, @RequestParam String balance, @RequestParam String scp_pnitems_id) {

		logger.info("EXECUTING METHOD :: verifyScrapPackingNote");

		Map<String, Object> verifyScrapPackingNotemap = new HashMap<String, Object>();
		int updateScrapPackingNoteItemsDuringVerification = 0;
		int updatePackingNoteWithVerificationInforec = 0;
		try {

			updatePackingNoteWithVerificationInforec = saleOrderPackingNoterepo.verifyScrapPAckingNoteInfo(
					weighbridge_no, transportername, vehicle_no, scp_pn_date, verified_by, remarks, scp_load);

			for (int i = 0; i < quantity.size(); i++) {
				updateScrapPackingNoteItemsDuringVerification = saleOrderPackingNoterepo
						.updateScrapPackingNoteItemsDuringVerification(quantity.get(i), kg.get(i), unit_price.get(i),
								total.get(i), total_ordered, total_sold, balance, verified_by, scp_load,
								scp_pnitems_id);
			}

			verifyScrapPackingNotemap.put("action", "VerifyScrapPackingNOte");
			verifyScrapPackingNotemap.put("message",
					(updateScrapPackingNoteItemsDuringVerification > 0) ? "Success" : "PackingNote not verified");
			verifyScrapPackingNotemap.put("status", (updatePackingNoteWithVerificationInforec > 0) ? "yes" : "no");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: verifyScrapPackingNote  -> " + e.getMessage());

		}

		return verifyScrapPackingNotemap;

	}

	@GetMapping("/saleOrderPackingNote/allVerifiedScrapingNotes")
	public @ResponseBody Map<String, Object> verifiedPackingNotesList(@RequestParam String factory_id) {

		logger.info("EXECUTING METHOD :: verifiedPackingNotesList");

		Map<String, Object> verifiedPackingNotelistmap = new HashMap<String, Object>();
		List<AllScrapingNoteDetails> verifiedPackingNotelist = null;
		try {

			verifiedPackingNotelist = saleOrderPackingNoterepo.getAllVerifiedPackingNoteList(factory_id);

			verifiedPackingNotelistmap.put("action", "VerifiedPackingNotesInfo");
			verifiedPackingNotelistmap.put("message",
					(verifiedPackingNotelist.size() > 0) ? "Success" : "Verified Packing Note Details Not Available");
			verifiedPackingNotelistmap.put("status", (verifiedPackingNotelist.size() > 0) ? "yes" : "no");

			if ((verifiedPackingNotelist != null) && (!verifiedPackingNotelist.isEmpty())) {

				verifiedPackingNotelistmap.put("VerifiedPackingNotesList", verifiedPackingNotelist);

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: verifiedPackingNotesList  -> " + e.getMessage());

		}
		return verifiedPackingNotelistmap;
	}

	@SuppressWarnings("unused")
	@GetMapping("/saleOrderPackingNote/verifiedScrapPackingNoteForInvoice")
	public @ResponseBody Map<String, Object> getVerifiedPackingNoteDetailsForInvoice(@RequestParam String scp_load) {

		logger.info("EXECUTING METHOD :: getVerifiedPackingNoteDetailsForInvoice");

		Map<String, Object> getVerifiedPackingNoteByScpLoadmap = new HashMap<String, Object>();
		VerifiedPackingNoteInterface verifiedpackingnoteinterface = null;
		VerifiedPackingNoteItemsInterface verifiedpackingnoteitemsinterface = null;
		String sale_order_code = null;
		LatestScrapNoteBalanceInterface scrapnotebalance = null;
		List<SalePackingNoteItemsInterface> verifiedpackingnoteitemslist = null;

		try {

			verifiedpackingnoteinterface = saleOrderPackingNoterepo.getInfoFromVerifiedPackingNoteTable(scp_load);

			verifiedpackingnoteitemslist = saleOrderPackingNoterepo
					.getScrapPackingNoteItemsDetailsForScp_load(scp_load);

			// Add status, action, and message before the data
			getVerifiedPackingNoteByScpLoadmap.put("action", "ScrapPackingNoteDetailsBasdOnLoad");
			getVerifiedPackingNoteByScpLoadmap.put("message", (verifiedpackingnoteinterface != null) ? "Success"
					: "Verified Scrap Packing Note details Based On Load not found!");
			getVerifiedPackingNoteByScpLoadmap.put("status", (verifiedpackingnoteinterface != null) ? "yes" : "no");

			// Add the list or interface details after
			getVerifiedPackingNoteByScpLoadmap.put("PackingNoteDetails", verifiedpackingnoteinterface);
			getVerifiedPackingNoteByScpLoadmap.put("PackingNoteItemDetails", verifiedpackingnoteitemslist);
			getVerifiedPackingNoteByScpLoadmap.put("BalanceInfo", scrapnotebalance);

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getVerifiedPackingNoteDetailsForInvoice  -> " + e.getMessage());

		}

		return getVerifiedPackingNoteByScpLoadmap;

	}

	@SuppressWarnings("unused")
	@PostMapping("/scrapInvoice/addScrapInvoice")
	public @ResponseBody Map<String, Object> addInvoice(
	        @RequestParam String contract_id,
	        @RequestParam String load_id,
	        @RequestParam String invoice_type,
	        @RequestParam List<String> scp_pnitems_id,
	        @RequestParam String scp_load,
	        @RequestParam String product_desc,
	        @RequestParam String remarks,
	        @RequestParam(required = false) List<String> service_code_id,
	        @RequestParam(required = false) List<String> hsn_code_id,
	        @RequestParam List<String> type_id,
	        @RequestParam String created_by,
	        @RequestParam String factory_id,
	        @RequestParam(required = false) BigDecimal tax1_ptd,
	        @RequestParam(required = false) BigDecimal tax1_pbc,
	        @RequestParam(required = false) BigDecimal tax2_ptd,
	        @RequestParam(required = false) BigDecimal tax2_pbc,
	        @RequestParam(required = false) BigDecimal tax3_ptd,
	        @RequestParam(required = false) BigDecimal tax3_pbc,
	        @RequestParam String shipment_mode,
	        @RequestParam String delivery_condition,
	        @RequestParam String bank_id) {

	    logger.info("EXECUTING METHOD :: addInvoice");

	    Map<String, Object> addScrapInvoicemap = new HashMap<>();
	    int insertIntoScrapInvoiceTable = 0;
	    int updatePackingNoteItemTables = 0;

	    try {

	        // Insert into Scrap Invoice table
	        insertIntoScrapInvoiceTable = saleOrderPackingNoterepo.insertIntoScrapInvoiceTable(
	                contract_id, load_id, invoice_type, scp_load, product_desc, remarks,
	                created_by, factory_id, tax1_ptd, tax1_pbc, tax2_ptd, tax2_pbc,
	                tax3_ptd, tax3_pbc, shipment_mode, delivery_condition, bank_id);

	        // Update packing note item table
	        for (int i = 0; i < scp_pnitems_id.size(); i++) {

	            String serviceCode = (service_code_id != null && service_code_id.size() > i)
	                    ? service_code_id.get(i)
	                    : null;

	            String hsnCode = (hsn_code_id != null && hsn_code_id.size() > i)
	                    ? hsn_code_id.get(i)
	                    : null;

	            String type = (type_id != null && type_id.size() > i)
	                    ? type_id.get(i)
	                    : null;

	            updatePackingNoteItemTables = saleOrderPackingNoterepo
	                    .updatePackingNoteItemsTableDuringInvoice(
	                            hsnCode,
	                            serviceCode,
	                            type,
	                            created_by,
	                            scp_load,
	                            scp_pnitems_id.get(i));
	        }

	        // Update scrap packing note table
	        int updateScrapPackingNoteTable =
	                saleOrderPackingNoterepo.updateScrapPackingNotetableForDetails(scp_load);

	    } catch (Exception e) {

	        logger.error("ERROR IN THE METHOD :: addInvoice -> " + e.getMessage());
	    }

	    addScrapInvoicemap.put("message",
	            (insertIntoScrapInvoiceTable > 0) ? "Success" : "Invoice Add");

	    addScrapInvoicemap.put("status",
	            (insertIntoScrapInvoiceTable > 0) ? "yes" : "no");

	    addScrapInvoicemap.put("action", "AddScrapInvoice");

	    return addScrapInvoicemap;
	}

	@SuppressWarnings("unused")
	@PostMapping("/scrapInvoice/updateScrapInvoiceNew")
	public @ResponseBody Map<String, Object> updateScrapInvoice(
	        @RequestParam String invoice_type,
	        @RequestParam String shipment_mode,
	        @RequestParam String delivery_condition,
	        @RequestParam String bank_id,
	        @RequestParam String product_desc,
	        @RequestParam String remarks,
	        @RequestParam String modified_by,
	        @RequestParam String scp_invoice_id,
	        @RequestParam(required = false) List<String> service_code,
	        @RequestParam(required = false) List<String> hsn_code,
	        @RequestParam List<String> scp_pnitems_id) {

	    logger.info("EXECUTING METHOD :: updateScrapInvoice");

	    Map<String, Object> updateScrapInvoiceNewmap = new HashMap<>();
	    int updateScrapInvoiceRecord = 0;

	    try {

	        // Update main invoice table
	        updateScrapInvoiceRecord = saleOrderPackingNoterepo.updateScrapInvoiceTable(
	                invoice_type,
	                shipment_mode,
	                delivery_condition,
	                bank_id,
	                product_desc,
	                remarks,
	                modified_by,
	                scp_invoice_id);

	        // Update packing note items
	        for (int i = 0; i < scp_pnitems_id.size(); i++) {

	            String serviceCode = (service_code != null && service_code.size() > i)
	                    ? service_code.get(i)
	                    : null;

	            String hsnCode = (hsn_code != null && hsn_code.size() > i)
	                    ? hsn_code.get(i)
	                    : null;

	            int updateScrapPackingNoteForInvoice =
	                    saleOrderPackingNoterepo.updateScrapPackingNoteDuringInvoiceUpdate(
	                            serviceCode,
	                            hsnCode,
	                            modified_by,
	                            invoice_type,
	                            scp_pnitems_id.get(i));
	        }

	    } catch (Exception e) {

	        logger.error("ERROR IN THE METHOD :: updateScrapInvoice -> " + e.getMessage());
	    }

	    updateScrapInvoiceNewmap.put("message",
	            (updateScrapInvoiceRecord > 0) ? "Success" : "Invoice Not updated");

	    updateScrapInvoiceNewmap.put("status",
	            (updateScrapInvoiceRecord > 0) ? "yes" : "no");

	    updateScrapInvoiceNewmap.put("action", "UpdateScrapInvoice");

	    return updateScrapInvoiceNewmap;
	}

	@GetMapping("/saleOrderPackingNote/allScrapPackingNoteDetailsNew")
	public @ResponseBody Map<String, Object> getAllScrapPackingNoteDetailsNew(@RequestParam String factory_id) {

		logger.info("EXECUTING METHOD :: getAllScrapPackingNoteDetailsNew");

		Map<String, Object> allScrapPackingNoteDetailsmap = new HashMap<String, Object>();
		List<AllScrapingNoteDetails> allScPackingNoteDetails = null;

		try {

			allScPackingNoteDetails = saleOrderPackingNoterepo.getAllScrapPackingnoteDetailsNew(factory_id);

			allScrapPackingNoteDetailsmap.put("message",
					((allScPackingNoteDetails != null) && (!allScPackingNoteDetails.isEmpty())) ? "Success"
							: "AllScrapPackingNoteDetails Not Available");
			allScrapPackingNoteDetailsmap.put("status",
					((allScPackingNoteDetails != null) && (!allScPackingNoteDetails.isEmpty())) ? "yes" : "no");
			allScrapPackingNoteDetailsmap.put("action", "ScrapPackingNoteList");

			if ((allScPackingNoteDetails != null) && (!allScPackingNoteDetails.isEmpty())) {
				allScrapPackingNoteDetailsmap.put("AllScraPackingNoteDetailsNew", allScPackingNoteDetails);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllScrapPackingNoteDetailsNew  -> " + e.getMessage());

		}
		return allScrapPackingNoteDetailsmap;

	}
	
	
	
	@GetMapping("/saleOrderPackingNote/allScrapPackingNoteDetailsNewPaged")
	public @ResponseBody Map<String, Object> getAllScrapPackingNoteDetailsNewPaged(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String search) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: getAllScrapPackingNoteDetailsNewPaged");

	    try {
	        Pageable pageable = PageRequest.of(page, size);

	        Page<AllScrapingNoteDetails> pageResult =
	                saleOrderPackingNoterepo.getAllScrapPackingnoteDetailsNewPaged(factory_id, search, pageable);

	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	        response.put("message",  pageResult.hasContent() ? "Success" : "AllScrapPackingNoteDetails Not Available");
	        response.put("status",   pageResult.hasContent() ? "yes" : "no");
	        response.put("action",   "ScrapPackingNoteList");
	        response.put("AllScraPackingNoteDetailsNew", pageResult.getContent());
	        response.put("totalItems",   pageResult.getTotalElements());
	        response.put("currentPage",  pageResult.getNumber());
	        response.put("totalPages",   pageResult.getTotalPages());

	    } catch (Exception e) {
	        logger.error("ERROR IN getAllScrapPackingNoteDetailsNewPaged :: " + e.getMessage());
	        response.put("message", "Error occurred");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: getAllScrapPackingNoteDetailsNewPaged");
	    return response;
	}
	
	
	

	@SuppressWarnings("unused")
	@PostMapping("/scrapinvoice/verificationnew")
	private @ResponseBody Map<String, Object> invoiceVerficationNew(@RequestParam String id,
			@RequestParam String load_id, @RequestParam String verified_by, @RequestParam String gst_remarks) {

		logger.info("EXECUTING METHOD :: invoiceVerficationNew");

		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		String newSeriesNumber = null;

		String soe_id = "";
		String billingAddressId = "";
		String shippingAddressId = "";
		String business_unit_id = "";
		String sale_order_type_id = "";
		String location_type_id = "";
		String sale_order_to_id = "";

		try {
			Optional<Long> optionalSeriesNumber = scrapinvoicerepo.getSeriesNumberbasedOnId(id, load_id);
			long serieNumber = optionalSeriesNumber.orElse((long) 0);
			if (serieNumber < 1) {
				// pre-validation when series number date is expired
				response.put("message", "Please Generate Series Number");
				return response;
			}
			String seriesNumberInString = String.valueOf(serieNumber);
			StringBuilder sbc = new StringBuilder();
			sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
			seriesNumberInString = sbc.toString();
			Optional<Long> optionalInvoiceId = scrapinvoicerepo
					.getMaxInvoiceNumberBasedOnSeriesNumber(seriesNumberInString);
			long invoiceNumber = optionalInvoiceId.orElse((long) 0);
			if (optionalInvoiceId.isPresent()) { // if invoice_no already generate and it's release and coming for
				String num = String.valueOf(invoiceNumber);
				int size = num.lastIndexOf("0000");
				String partAfterZero = null;
				if (size != -1) {
					partAfterZero = num.substring(size + 4);
					int a = Integer.parseInt(partAfterZero);
					a = a + 1;
					partAfterZero = String.valueOf(a);
				}
				Long incrementedValue = Long.parseLong(partAfterZero);
				newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
				count = scrapinvoicerepo.updateScrapInvoiceVerificationDetails(verified_by, newSeriesNumber,
						gst_remarks, id);
				// String scp_load = scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(id);
				// String saleOrderCode =
				// scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load);
				// List<String> result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode);
				// String valueData = result.get(0);
				// String[] parts = valueData.split(",");
				// if(parts.length >= 6) {
				// sale_order_type_id = result.get(0);
				// location_type_id = result.get(1);
				// // sale_order_to_id = result.get(2);
				// soe_id = result.get(2);
				// billingAddressId = result.get(3);
				// shippingAddressId = result.get(4);
				// business_unit_id = result.get(5);
				//
				// }
				// scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "1");
				// // scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"1");
				// scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "1");
				//// scrapinvoicerepo.updateIsLockSaleOrderItemLevelEntry(soe_id,"1");
				//// scrapinvoicerepo.updateIsLockSaleOrderDescEntry(soe_id,"1");
				// scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "1");
				// scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "1");
				// scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "1");
				// scrapinvoicerepo.updateIsLockSaleOrderTypeId(sale_order_type_id,"1");
				// scrapinvoicerepo.updateIsLockLocationTypeId(location_type_id,"1");
				// scrapinvoicerepo.detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
				// int sizeRange = tax_id.size();
				// for (int i = 0; i < sizeRange; i++) {
				// scrapinvoicerepo.insertINVOICE_TAXENTRY_DETAILS(tax_id.get(i),
				// tax_per.get(i),
				// tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
				// tax_payable_to_dept.get(i), t_adv.get(i), String.valueOf(id), verified_by);
				// }

			} else {
				if (invoiceNumber == 0) {
					String num = String.valueOf(serieNumber);
					int size = num.lastIndexOf("0000");
					String partAfterZero = null;
					if (size != -1) {
						partAfterZero = num.substring(size + 4);
						int a = Integer.parseInt(partAfterZero);
						a = a + 1;
						partAfterZero = String.valueOf(a);
					}
					Long incrementedValue = Long.parseLong(partAfterZero);
					newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
					count = scrapinvoicerepo.updateScrapInvoiceVerificationDetails(verified_by, newSeriesNumber,
							gst_remarks, id);
					// String scp_load = scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(id);
					// String saleOrderCode =
					// scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load);
					// List<String> result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode);
					// for (int i = 0; i < result.size(); i++) {
					// soe_id = result.get(0);
					// billingAddressId = result.get(1);
					// shippingAddressId = result.get(2);
					// business_unit_id = result.get(3);
					//
					// }
					// scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "1");
					// // scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"1");
					// scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "1");
					//// scrapinvoicerepo.updateIsLockSaleOrderItemLevelEntry(soe_id,"1");
					//// scrapinvoicerepo.updateIsLockSaleOrderDescEntry(soe_id,"1");
					// scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "1");
					// scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "1");
					// scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "1");
					// scrapinvoicerepo.detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
					// int sizeRange = tax_id.size();
					// for (int i = 0; i < sizeRange; i++) {
					// scrapinvoicerepo.insertINVOICE_TAXENTRY_DETAILS(tax_id.get(i),
					// tax_per.get(i),
					// tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
					// tax_payable_to_dept.get(i), t_adv.get(i), String.valueOf(id), verified_by);
					// }

				} else {
					String num = String.valueOf(invoiceNumber);
					int size = num.lastIndexOf("0000");
					String partAfterZero = null;
					if (size != -1) {
						partAfterZero = num.substring(size + 4);
						int a = Integer.parseInt(partAfterZero);
						a = a + 1;
						partAfterZero = String.valueOf(a);
					}
					Long incrementedValue = Long.parseLong(partAfterZero);
					newSeriesNumber = num.substring(0, size + 4) + incrementedValue;
					count = scrapinvoicerepo.updateScrapInvoiceVerificationDetails(verified_by, newSeriesNumber,
							gst_remarks, id);
					// String scp_load = scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(id);
					// String saleOrderCode =
					// scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load);
					// List<String> result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode);
					// if (result != null && result.size() >= 4) {
					// soe_id = result.get(0);
					// billingAddressId = result.get(1);
					// shippingAddressId = result.get(2);
					// business_unit_id = result.get(3);
					// }
					// scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "1");
					// // scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"1");
					// scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "1");
					//// scrapinvoicerepo.updateIsLockSaleOrderItemLevelEntry(soe_id,"1");
					//// scrapinvoicerepo.updateIsLockSaleOrderDescEntry(soe_id,"1");
					// scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "1");
					// scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "1");
					// scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "1");
					// scrapinvoicerepo.detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
					// int sizeRange = tax_id.size();
					// for (int i = 0; i < sizeRange; i++) {
					// scrapinvoicerepo.insertINVOICE_TAXENTRY_DETAILS(tax_id.get(i),
					// tax_per.get(i),
					// tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
					// tax_payable_to_dept.get(i), t_adv.get(i), String.valueOf(id), verified_by);
					// }

				}
			}

			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
			response.put("Invoice_NO", newSeriesNumber);

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: invoiceVerficationNew  -> " + e.getMessage());

		}
		return response;
	}

	@GetMapping("/scrapinvoice/scrapInvoiceDetails")
	public @ResponseBody Map<String, Object> getUnverifiedInvoiceDetails(@RequestParam String factory_id) {

		logger.info("EXECUTING METHOD :: getUnverifiedInvoiceDetails");

		Map<String, Object> unverifiedInvoiceDetailsmap = new HashMap<String, Object>();

		List<UnverifiedInvoiceListInterface> unverifiedInvoiceList = null;

		try {

			unverifiedInvoiceList = saleOrderPackingNoterepo.getUnverifiedScrapInvoDetails(factory_id);

			unverifiedInvoiceDetailsmap.put("action", "ScrapInvoiceDetails");
			unverifiedInvoiceDetailsmap.put("message",
					(unverifiedInvoiceList.size() > 0) ? "Success" : "ScrapInvoiceDetails not found!");
			unverifiedInvoiceDetailsmap.put("status", (unverifiedInvoiceList.size() > 0) ? "yes" : "no");

			if ((unverifiedInvoiceList != null) && (!unverifiedInvoiceList.isEmpty())) {

				unverifiedInvoiceDetailsmap.put("ScrapInvoiceInfo", unverifiedInvoiceList);

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getUnverifiedInvoiceDetails  -> " + e.getMessage());
		}
		return unverifiedInvoiceDetailsmap;
	}
	
	
	
	
	@GetMapping("/scrapinvoice/scrapInvoiceDetailsPaged")
	public @ResponseBody Map<String, Object> getUnverifiedInvoiceDetailsPaged(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String search) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: getUnverifiedInvoiceDetailsPaged");

	    try {
	        Pageable pageable = PageRequest.of(page, size);

	        Page<UnverifiedInvoiceListInterface> pageResult =
	                saleOrderPackingNoterepo.getUnverifiedScrapInvoDetailsPaged(factory_id, search, pageable);

	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	        response.put("action",       "ScrapInvoiceDetails");
	        response.put("message",      pageResult.hasContent() ? "Success" : "ScrapInvoiceDetails not found!");
	        response.put("status",       pageResult.hasContent() ? "yes" : "no");
	        response.put("ScrapInvoiceInfo", pageResult.getContent());
	        response.put("totalItems",   pageResult.getTotalElements());
	        response.put("currentPage",  pageResult.getNumber());
	        response.put("totalPages",   pageResult.getTotalPages());

	    } catch (Exception e) {
	        logger.error("ERROR IN getUnverifiedInvoiceDetailsPaged :: " + e.getMessage());
	        response.put("message", "Error occurred");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: getUnverifiedInvoiceDetailsPaged");
	    return response;
	}
	
	

	@GetMapping("/scrapinvoice/scrapInvoiceDetailsBsdOnId")
	public @ResponseBody Map<String, Object> getScrapInvoiceDetailsForVerification(
			@RequestParam String scp_invoice_id) {

		logger.info("EXECUTING METHOD :: getScrapInvoiceDetailsForVerification");

		Map<String, Object> scrapDetailsBsdOnInvoiceIdmap = new HashMap<String, Object>();

		ScrapInvoiceInfoInterface scpInvoiceInfo = null;
		ScrapInvoiceItemsoInterface scpInvoiceItemsInfo = null;
		String scp_load = "";
		try {

			scp_load = saleOrderPackingNoterepo.getScpLoadFromInvoiceTable(scp_invoice_id);

			
			
			
			
			logger.info(scp_invoice_id);
			scpInvoiceInfo = saleOrderPackingNoterepo.getScrapInvoDetails(scp_invoice_id);

			
			logger.info(scp_load);
			
			scpInvoiceItemsInfo = saleOrderPackingNoterepo.getScrapInvoiceItemsDetails(scp_load);

			scrapDetailsBsdOnInvoiceIdmap.put("action", "ScrapInvoiceDetails");
			scrapDetailsBsdOnInvoiceIdmap.put("message",
					(scpInvoiceInfo != null) ? "Success" : "ScrapInvoiceDetails not found!");
			scrapDetailsBsdOnInvoiceIdmap.put("status", (scpInvoiceInfo != null) ? "yes" : "no");

			if ((scpInvoiceInfo != null)) {

				scrapDetailsBsdOnInvoiceIdmap.put("ScrapInvoiceInfo", scpInvoiceInfo);
				scrapDetailsBsdOnInvoiceIdmap.put("ScrapInvoiceItemInfo", scpInvoiceItemsInfo);

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getScrapInvoiceDetailsForVerification  -> " + e.getMessage());

		}
		return scrapDetailsBsdOnInvoiceIdmap;
	}

	@PostMapping("/scrapinvoice/cancelbyidnew")
	public @ResponseBody Map<String, Object> cancelscrapInvoiceById(
	        @RequestParam String id,
	        @RequestParam String cancelled_by,@RequestParam String scp_load
	   ) {

	    Map<String, Object> response = new HashMap<>();

	    int count = saleOrderPackingNoterepo.updatescrapInvoiceCancelById(id, cancelled_by);
	    saleOrderPackingNoterepo.updatedbtcrdQSPackingCancelByPnId(scp_load);
	    response.put("message", count > 0 ? "Success" : "Failure");
	    response.put("status", count > 0 ? "yes" : "no");
	    response.put("action", "CANCEL_OTHERS_INVOICE");

	    return response;
	}
}
