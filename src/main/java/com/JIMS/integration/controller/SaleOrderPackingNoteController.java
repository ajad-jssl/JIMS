package com.JIMS.integration.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

import com.JIMS.integration.entity.SaleOrderPackingNote;
import com.JIMS.integration.entity.ScrapInvoice;
import com.JIMS.integration.interfaces.AllScrapingNoteDetails;
import com.JIMS.integration.interfaces.LatestScrapNoteBalanceInterface;
import com.JIMS.integration.interfaces.SalePackingNoteInterface;
import com.JIMS.integration.interfaces.SalePackingNoteItemsInterface;
import com.JIMS.integration.repository.SaleOrderPackingNoteRepository;
import com.JIMS.integration.repository.ScrapInvoiceRepository;

@RestController
@RequestMapping("/jssl")
@CrossOrigin
public class SaleOrderPackingNoteController {

	@Autowired
	private SaleOrderPackingNoteRepository saleOrderPackingNoterepo;

	@Autowired
	private ScrapInvoiceRepository scrapinvoicerepo;

	Logger logger = LogManager.getLogger(SaleOrderPackingNoteController.class);

	@SuppressWarnings({ "unused", "unchecked" })
	@PostMapping("/saleOrderPackingNote/addSaleOrderPackingNote")
	public @ResponseBody Map<String, Object> addSaleOrderPackingNote(
			@RequestBody Map<String, Object> saleOrderPackingNote) {

		logger.info("EXECUTING METHOD :: addSaleOrderPackingNote");

		Map<String, Object> createScrapPackingNotemap = new HashMap<String, Object>();
		Map<String, String> scrapPackingNoteInfo = null;
		Map<String, String> scrapPackingNoteItems = null;
		int saveScrapPackingNoteItems = 0 ;
		Integer rowCount = 0;

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
			scrapPackingNoteInfo = (Map<String, String>) saleOrderPackingNote.get("scrap_packing_note_info");
			scrapPackingNoteItems = (Map<String, String>) saleOrderPackingNote.get("scrap_packing_note_items");

			SaleOrderPackingNote salepackingnote = new SaleOrderPackingNote();
			salepackingnote.setSale_order_code(scrapPackingNoteInfo.get("sale_order_code"));
			salepackingnote.setScp_load(newScpLoad);
			salepackingnote.setTotal_steel_qty(scrapPackingNoteInfo.get("total_steel_qty"));
			salepackingnote.setTotal_non_steel_qty(scrapPackingNoteInfo.get("total_non_steel_qty"));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			salepackingnote
					.setSale_order_validity(LocalDateTime.parse(scrapPackingNoteInfo.get("sale_order_validity")));
			salepackingnote.setVendor_id(scrapPackingNoteInfo.get("vendor_id"));
			salepackingnote.setWeighbridge_no(scrapPackingNoteInfo.get("weighbridge_no"));
			salepackingnote.setTransportername(scrapPackingNoteInfo.get("transportername"));
			salepackingnote.setVehicleno(scrapPackingNoteInfo.get("vehicle_no"));
			salepackingnote.setScp_pn_date(scrapPackingNoteInfo.get("scp_pn_date"));

			String saleOrderCode = scrapPackingNoteInfo.get("sale_order_code");
			salepackingnote.setBuyer_ref_no(saleOrderCode);

			salepackingnote.setCreated_by(scrapPackingNoteInfo.get("created_by"));
			salepackingnote.setCreated_date(LocalDateTime.now());
			salepackingnote.setFactory_id(scrapPackingNoteInfo.get("factory_id"));

			logger.info("EXECUTING METHOD :: BEFORE saving scrap packing note");

			saleOrderPackingNoterepo.save(salepackingnote);

			logger.info("EXECUTING METHOD :: AFTER saving scrap packing note");

			String scrap_type_id = scrapPackingNoteItems.get("scrap_type_id");
			String scrapitem_id = scrapPackingNoteItems.get("scrapitem_id");
			String uom_id = scrapPackingNoteItems.get("uom_id");
			String quantity = scrapPackingNoteItems.get("quantity");
			String kg = scrapPackingNoteItems.get("kg");
			String unit_price = scrapPackingNoteItems.get("unit_price");
			String total = scrapPackingNoteItems.get("total");
			String total_ordered = scrapPackingNoteItems.get("total_ordered");
			String total_sold = scrapPackingNoteItems.get("total_sold");
			String balance = scrapPackingNoteItems.get("balance");
			String pn_items_created_by = salepackingnote.getCreated_by();
			LocalDateTime pn_items_created_date = salepackingnote.getCreated_date();

			int saleOrder_pn_id = salepackingnote.getScp_pn_id();
			String sale_order_code = salepackingnote.getSale_order_code();
			String scp_load = salepackingnote.getScp_load();

			logger.info(
					"EXECUTING METHOD :: BEFORE saving scrap packing note item details => addScrapPackingNoteItems ");

			saveScrapPackingNoteItems = saleOrderPackingNoterepo.addScrapPackingNoteItems(saleOrder_pn_id,
					sale_order_code, scp_load, scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total,
					total_ordered, total_sold, balance, pn_items_created_by, pn_items_created_date);

			logger.info(
					"EXECUTING METHOD :: AFTER saving scrap packing note item details => addScrapPackingNoteItems ");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: addSaleOrderPackingNote  -> " + e.getMessage());
		}
		saleOrderPackingNote.put("message", (saveScrapPackingNoteItems > 0) ? "Success" : "ScrapPackingNote Not Added");
		saleOrderPackingNote.put("status", (saveScrapPackingNoteItems > 0) ? "yes" : "no");
		saleOrderPackingNote.put("action", "AddScrapPackingNote");

		return saleOrderPackingNote;

	}

	@SuppressWarnings("unused")
	@GetMapping("/saleOrderPackingNote/getPackingNoteById")
	public @ResponseBody Map<String, Object> getPackingNoteBasedOnScrapNote(@RequestParam String scp_load) {

		logger.info("EXECUTING METHOD :: getPackingNoteBasedOnScrapNote");

		Map<String, Object> getScrapPackingnotemap = new HashMap<String, Object>();
		SalePackingNoteInterface salepackingnoteinterface = null;
		SalePackingNoteItemsInterface salepackingnoteitemsinterface = null;
		String pnsale_order_code = null;
		LatestScrapNoteBalanceInterface scrapnotebalance = null;
		try {

			logger.info(
					"EXECUTING METHOD :: getPackingNoteBasedOnScrapNote :: getScrapPackingNoteDetailsForParticularSaleOrder ");

			salepackingnoteinterface = saleOrderPackingNoterepo
					.getScrapPackingNoteDetailsForParticularSaleOrder(scp_load);

			logger.info("EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before executing getSaleOrderCode ");

			pnsale_order_code = saleOrderPackingNoterepo.getSaleOrderCode(scp_load);

			logger.info("EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before executing getLastestBalanceInfo ");

			//scrapnotebalance = saleOrderPackingNoterepo.getLastestBalanceInfo(pnsale_order_code);

			logger.info(
					"EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before executing getScrapPackingNoteItemsDetails ");

			salepackingnoteitemsinterface = saleOrderPackingNoterepo.getScrapPackingNoteItemsDetails(scp_load);

			getScrapPackingnotemap.put("action", "SaleOrderScrapPackingNoteDetails");
			getScrapPackingnotemap.put("message", (salepackingnoteinterface != null) ? "Success"
					: "Sale Order Scrap Packing Note details not found!");
			getScrapPackingnotemap.put("status", (salepackingnoteinterface != null) ? "yes" : "no");
			getScrapPackingnotemap.put("PackingNoteDetails", salepackingnoteinterface);
			getScrapPackingnotemap.put("PackingNoteItemDetails", salepackingnoteitemsinterface);
			getScrapPackingnotemap.put("PackingNoteDetails", salepackingnoteinterface);
			getScrapPackingnotemap.put("BalanceInfo", scrapnotebalance);

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getPackingNoteBasedOnScrapNote  -> " + e.getMessage());
		}
		return getScrapPackingnotemap;

	}

	/*
	 * @PostMapping("/saleOrderPackingNote/updatePackingNoteDetails")
	 * public @ResponseBody Map<String, Object> updateScrapPackingNote(
	 * 
	 * @RequestBody Map<String, Object> updateScrapPackingNote) {
	 * 
	 * logger.info("EXECUTING METHOD :: updateScrapPackingNote");
	 * 
	 * Map<String, Object> updateScrapPackingNotemap = new HashMap<String,
	 * Object>(); Map<String, String> scrapPackingNoteInfo = null; Map<String,
	 * String> scrapPackingNoteItems = null;
	 * 
	 * try { scrapPackingNoteInfo = (Map<String, String>)
	 * updateScrapPackingNote.get("scrap_packing_note_info"); scrapPackingNoteItems
	 * = (Map<String, String>)
	 * updateScrapPackingNote.get("scrap_packing_note_items");
	 * 
	 * SaleOrderPackingNote updatesalepackingnote = new SaleOrderPackingNote();
	 * DateTimeFormatter formatter =
	 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	 * 
	 * updatesalepackingnote.setScp_load(scrapPackingNoteInfo.get("scp_pn_id"));
	 * updatesalepackingnote.setScp_load(scrapPackingNoteInfo.get("scp_load"));
	 * updatesalepackingnote.setVendor_id(scrapPackingNoteInfo.get("vendor_id"));
	 * updatesalepackingnote.setWeighbridge_no(scrapPackingNoteInfo.get(
	 * "weighbridge_no"));
	 * updatesalepackingnote.setTransportername(scrapPackingNoteInfo.get(
	 * "transportername"));
	 * updatesalepackingnote.setVehicleno(scrapPackingNoteInfo.get("vehicle_no"));
	 * updatesalepackingnote.setScp_pn_date(scrapPackingNoteInfo.get("scp_pn_date"))
	 * ;
	 * 
	 * updatesalepackingnote.setCredit_reference(scrapPackingNoteInfo.get(
	 * "credit_reference"));
	 * updatesalepackingnote.setOther_reference(scrapPackingNoteInfo.get(
	 * "other_reference"));
	 * updatesalepackingnote.setProject_reference(scrapPackingNoteInfo.get(
	 * "project_reference"));
	 * updatesalepackingnote.setCentral_excise_tarrif_no(scrapPackingNoteInfo.get(
	 * "central_excise_tarrif_no"));
	 * updatesalepackingnote.setRemarks(scrapPackingNoteInfo.get("remarks"));
	 * updatesalepackingnote.setModified_by(scrapPackingNoteInfo.get("modified_by"))
	 * ; updatesalepackingnote.setModified_date(LocalDateTime.now());
	 * 
	 * String project_reference = scrapPackingNoteInfo.get("project_reference");
	 * 
	 * String scp_load = updatesalepackingnote.getScp_load(); String modified_by =
	 * updatesalepackingnote.getModified_by(); LocalDateTime modified_date =
	 * updatesalepackingnote.getModified_date();
	 * 
	 * int verifiedValue = saleOrderPackingNoterepo.checkIsVerified(scp_load); if
	 * (verifiedValue == 1) { updateScrapPackingNotemap.put("message",
	 * "QS Packing Note Already Verified, unable to Update"); return
	 * updateScrapPackingNotemap; }
	 * 
	 * int value = saleOrderPackingNoterepo.checkIsLocked(scp_load); if (value > 0)
	 * { updateScrapPackingNotemap.put("message",
	 * "Invoice Already Generated Not Able to Update"); return
	 * updateScrapPackingNotemap; }
	 * 
	 * logger.
	 * info("EXECUTING METHOD :: updateScrapPackingNote :: findScp_loadIfExists ");
	 * int moveScPackingNoteToHistoryTable = saleOrderPackingNoterepo
	 * .updateScrapPackingNoteIntoHistoryTable(scp_load, modified_by);
	 * 
	 * SaleOrderPackingNote existingRecord =
	 * saleOrderPackingNoterepo.findScp_loadIfExists(scp_load); if (existingRecord
	 * != null) {
	 * 
	 * existingRecord.setWeighbridge_no(updatesalepackingnote.getWeighbridge_no());
	 * existingRecord.setTransportername(updatesalepackingnote.getTransportername())
	 * ; existingRecord.setVehicleno(updatesalepackingnote.getVehicleno());
	 * existingRecord.setScp_pn_date(updatesalepackingnote.getScp_pn_date());
	 * existingRecord.setModified_by(updatesalepackingnote.getModified_by());
	 * existingRecord.setModified_date(updatesalepackingnote.getModified_date());
	 * existingRecord.setProject_reference(updatesalepackingnote.
	 * getProject_reference());
	 * existingRecord.setCentral_excise_tarrif_no(updatesalepackingnote.
	 * getCentral_excise_tarrif_no());
	 * existingRecord.setOther_reference(updatesalepackingnote.getOther_reference())
	 * ;
	 * existingRecord.setCredit_reference(updatesalepackingnote.getCredit_reference(
	 * )); existingRecord.setRemarks(updatesalepackingnote.getRemarks()); //
	 * existingRecord.setScp_pn_id(updatesalepackingnote.getScp_pn_id());
	 * 
	 * logger.
	 * info("EXECUTING METHOD :: updateScrapPackingNote ::BEFORE saving ScrapPackingNoteDetails"
	 * );
	 * 
	 * saleOrderPackingNoterepo.save(existingRecord);
	 * 
	 * String scp_pnitems_id = scrapPackingNoteItems.get("scp_pnitems_id"); String
	 * quantity = scrapPackingNoteItems.get("quantity"); String kg =
	 * scrapPackingNoteItems.get("kg"); String unit_price =
	 * scrapPackingNoteItems.get("unit_price"); String total =
	 * scrapPackingNoteItems.get("total"); String total_ordered =
	 * scrapPackingNoteItems.get("total_ordered"); String total_sold =
	 * scrapPackingNoteItems.get("total_sold"); String balance =
	 * scrapPackingNoteItems.get("balance");
	 * 
	 * logger.info(
	 * "EXECUTING METHOD :: updateScrapPackingNote ::BEFORE saving ScrapPackingNoteItemDetailsDetails"
	 * );
	 * 
	 * int movePackingNoteItemDetailsToHistoryBeforeUpdate =
	 * saleOrderPackingNoterepo .insertPackingNoteDetailsToHistory(scp_load,
	 * modified_by, modified_date, scp_pnitems_id);
	 * 
	 * int updatePackingNoteItemsrecord =
	 * saleOrderPackingNoterepo.updatePackingNoteItemsInfo(quantity, kg, unit_price,
	 * total, total_ordered, total_sold, balance, modified_by, modified_date,
	 * scp_load);
	 * 
	 * updateScrapPackingNotemap.put("message", (updatePackingNoteItemsrecord > 0) ?
	 * "Success" : "Scrap Packing Note not updated");
	 * updateScrapPackingNotemap.put("status", (updatePackingNoteItemsrecord > 0) ?
	 * "yes" : "no"); updateScrapPackingNotemap.put("action",
	 * "UpdateScrapPackingNoteDetails");
	 * 
	 * }
	 * 
	 * } catch (Exception e) {
	 * logger.error("ERROR IN THE METHOD :: updateScrapPackingNote  -> " +
	 * e.getMessage()); } return updateScrapPackingNotemap;
	 * 
	 * }
	 */

	/*
	 * @PostMapping("/saleOrderPackingNote/verifyPackingNoteDetails")
	 * public @ResponseBody Map<String, Object> verifyScrapPackingNote(
	 * 
	 * @RequestBody Map<String, Object> verifyScrapPackingNote) {
	 * 
	 * logger.info("EXECUTING METHOD :: verifyScrapPackingNote");
	 * 
	 * Map<String, Object> verifyScrapPackingNotemap = new HashMap<String,
	 * Object>(); Map<String, String> scrapPackingNoteInfo = null; Map<String,
	 * String> scrapPackingNoteItems = null;
	 * 
	 * try { scrapPackingNoteInfo = (Map<String, String>)
	 * verifyScrapPackingNote.get("scrap_packing_note_info"); scrapPackingNoteItems
	 * = (Map<String, String>)
	 * verifyScrapPackingNote.get("scrap_packing_note_items");
	 * 
	 * SaleOrderPackingNote verifysalepackingnote = new SaleOrderPackingNote();
	 * DateTimeFormatter formatter =
	 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	 * 
	 * verifysalepackingnote.setScp_load(scrapPackingNoteInfo.get("scp_load")); //
	 * verifysalepackingnote.setVendor_id(scrapPackingNoteInfo.get("vendor_id"));
	 * verifysalepackingnote.setWeighbridge_no(scrapPackingNoteInfo.get(
	 * "weighbridge_no"));
	 * verifysalepackingnote.setTransportername(scrapPackingNoteInfo.get(
	 * "transportername"));
	 * verifysalepackingnote.setVehicleno(scrapPackingNoteInfo.get("vehicle_no"));
	 * verifysalepackingnote.setScp_pn_date(scrapPackingNoteInfo.get("scp_pn_date"))
	 * ;
	 * verifysalepackingnote.setIs_verified(scrapPackingNoteInfo.get("is_verified"))
	 * ;
	 * verifysalepackingnote.setVerified_by(scrapPackingNoteInfo.get("verified_by"))
	 * ; verifysalepackingnote.setVerified_date(LocalDateTime.now());
	 * verifysalepackingnote.setRemarks(scrapPackingNoteInfo.get("remarks"));
	 * 
	 * String scp_load = verifysalepackingnote.getScp_load();
	 * 
	 * SaleOrderPackingNote verifyExistingRecord =
	 * saleOrderPackingNoterepo.findScp_loadIfExists(scp_load); if
	 * (verifyExistingRecord != null) {
	 * 
	 * verifyExistingRecord.setWeighbridge_no(verifysalepackingnote.
	 * getWeighbridge_no());
	 * verifyExistingRecord.setTransportername(verifysalepackingnote.
	 * getTransportername());
	 * verifyExistingRecord.setVehicleno(verifysalepackingnote.getVehicleno());
	 * verifyExistingRecord.setScp_pn_date(verifysalepackingnote.getScp_pn_date());
	 * verifyExistingRecord.setIs_verified(verifysalepackingnote.getIs_verified());
	 * verifyExistingRecord.setVerified_by(verifysalepackingnote.getVerified_by());
	 * verifyExistingRecord.setVerified_date(verifysalepackingnote.getVerified_date(
	 * )); verifyExistingRecord.setRemarks(verifysalepackingnote.getRemarks());
	 * 
	 * logger.info(
	 * "EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before saving/verifying ScrapPackingNoteDetails "
	 * );
	 * 
	 * saleOrderPackingNoterepo.save(verifyExistingRecord);
	 * 
	 * String quantity = scrapPackingNoteItems.get("quantity"); String kg =
	 * scrapPackingNoteItems.get("kg"); String unit_price =
	 * scrapPackingNoteItems.get("unit_price"); String total =
	 * scrapPackingNoteItems.get("total"); String total_ordered =
	 * scrapPackingNoteItems.get("total_ordered"); String total_sold =
	 * scrapPackingNoteItems.get("total_sold"); String balance =
	 * scrapPackingNoteItems.get("balance"); String modified_by =
	 * verifysalepackingnote.getVerified_by(); LocalDateTime modified_date =
	 * verifysalepackingnote.getVerified_date();
	 * 
	 * logger.info(
	 * "EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before saving/verifying ScrapPackingNoteItemDetails "
	 * );
	 * 
	 * int updatePackingNoteItemsDuringVerificationrecord = saleOrderPackingNoterepo
	 * .updatePackingNoteItemsInfo(quantity, kg, unit_price, total, total_ordered,
	 * total_sold, balance, modified_by, modified_date, scp_load);
	 * 
	 * verifyScrapPackingNotemap.put("message",
	 * (updatePackingNoteItemsDuringVerificationrecord > 0) ? "Success" :
	 * "Scrap Packing Note not verified"); verifyScrapPackingNotemap.put("status",
	 * (updatePackingNoteItemsDuringVerificationrecord > 0) ? "yes" : "no");
	 * verifyScrapPackingNotemap.put("action", "VerifyScrapPackingNoteDetails");
	 * 
	 * }
	 * 
	 * } catch (Exception e) {
	 * logger.error("ERROR IN THE METHOD :: verifyScrapPackingNote  -> " +
	 * e.getMessage()); } return verifyScrapPackingNotemap;
	 * 
	 * }
	 */

	@GetMapping("/saleOrderPackingNote/allScrapPackingNoteDetails")
	public @ResponseBody Map<String, Object> getAllScrapPackingNoteDetails() {

		logger.info("EXECUTING METHOD :: getAllScrapPackingNoteDetails");

		Map<String, Object> allScrapPackingNoteDetailsmap = new HashMap<String, Object>();
		List<AllScrapingNoteDetails> allScPackingNoteDetails = null;

		try {

			allScPackingNoteDetails = saleOrderPackingNoterepo.getAllScrapPackingnoteDetails();

			allScrapPackingNoteDetailsmap.put("message",
					((allScPackingNoteDetails != null) && (!allScPackingNoteDetails.isEmpty())) ? "Success"
							: "AllScrapPackingNoteDetails Not Available");
			allScrapPackingNoteDetailsmap.put("status",
					((allScPackingNoteDetails != null) && (!allScPackingNoteDetails.isEmpty())) ? "yes" : "no");
			allScrapPackingNoteDetailsmap.put("action", "DistrictInfo");

			if ((allScPackingNoteDetails != null) && (!allScPackingNoteDetails.isEmpty())) {
				allScrapPackingNoteDetailsmap.put("AllScraPackingNoteDetails", allScPackingNoteDetails);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllScrapPackingNoteDetails  -> " + e.getMessage());
		}
		return allScrapPackingNoteDetailsmap;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping("/scrapInvoice/add")
	public @ResponseBody Map<String, Object> createScrapInvoice(@RequestBody Map<String, Object> data) {

		logger.info("EXECUTING METHOD :: createInvoice");

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> val = null;
		List<Map<String, String>> obj = null;
		try {
			val = (Map<String, String>) data.get("val");
			int valCount = 0;
			int count = 0;
			
			// Extract the 'obj' part, which is a list of maps
			obj = (List<Map<String, String>>) data.get("obj");
			ScrapInvoice invoice = new ScrapInvoice();
			@SuppressWarnings("unused")
			int contract_id = Integer.parseInt(val.get("contract_id")); //
			String load_id = val.get("scp_load");//
			String invoice_type = val.get("invoice_type");//

			String scp_pnitems_id = val.get("scp_pnitems_id");
			String scp_load = val.get("scp_load");
			String created_by = val.get("created_by");
			String product_desc = val.get("product_desc");
			String remarks = val.get("remarks");
			String date_of_notification = val.get("date_of_notification");
			String date_val = val.get("date_val");
			String bg_type = val.get("bg_type");
			String date_of_issue = val.get("date_of_issue");
			String reference_no = val.get("reference_no");
			String lc_number = val.get("lc_number");
			String supply_place = val.get("supply_place");
			String s_t_exempted = val.get("s_t_exempted");
			String lr_docketno = val.get("lr_docketno");
			String bg_no = val.get("bg_no");
			String date_of_expiry = val.get("date_of_expiry");
			String date_of_ref = val.get("date_of_ref");
			String lc_issue_date = val.get("lc_issue_date");

			invoice.setLoad_id(load_id);
			invoice.setInvoice_type(invoice_type);
			// invoice.setQs_packing_item_slno(qs_packing_item_slno);
			invoice.setProductDesc(product_desc);
			invoice.setRemarks(remarks);
			invoice.setDateOfNotification(date_of_notification);
			invoice.setDateVal(date_val);
			invoice.setBgType(bg_type);
			invoice.setDateOfIssue(date_of_issue);
			invoice.setReferenceNo(reference_no);
			invoice.setLcNumber(lc_number);
			invoice.setSupplyPlace(supply_place);
			invoice.setStExempted(s_t_exempted);
			invoice.setLrDocketNo(lr_docketno);
			invoice.setBgNo(bg_no);
			invoice.setDateOfExpiry(date_of_expiry);
			invoice.setDateOfRef(date_of_ref);
			invoice.setLcIssueDate(lc_issue_date);
			invoice.setCreatedBy(created_by);
			// invoice.setContract_name(contract_name);
			LocalDateTime time = LocalDateTime.now();
			invoice.setCreatedDate(time);
			invoice.setScp_load(scp_load);
			invoice.setScp_pnitems_id(scp_pnitems_id);

			ScrapInvoice invoiceObj = scrapinvoicerepo.save(invoice);
			for (Map<String, String> item : obj) {
				String service_code_id = item.get("service_code_id");
				String hsn_code_id = item.get("hsn_code_id");
				String type_id = item.get("type_id");

				logger.info("EXECUTING METHOD :: createInvoice :: BEFORE addPackingNoteItems");

				int updateScrapPackingNoteRecord = scrapinvoicerepo.updatePackingNoteItemsTable(service_code_id,
						hsn_code_id, type_id, scp_load, scp_pnitems_id, created_by);

				logger.info("EXECUTING METHOD :: createInvoice :: AFTER addPackingNoteItems");

			}
			response.put("action", "SCRAPINVOICE_ADD");
			response.put("message", (invoiceObj != null) ? "Success" : "failure");
			response.put("status", (invoiceObj != null) ? "yes" : "no");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createInvoice  -> " + e.getMessage());

		}
		return response;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping("/scrapInvoice/update")
	public @ResponseBody Map<String, Object> updateScrapInvoice(@RequestBody Map<String, Object> data) {

		logger.info("EXECUTING METHOD :: updateScrapInvoice");

		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> val = null;
		List<Map<String, String>> obj = null;
		try {
			val = (Map<String, String>) data.get("val");
			int valCount = 0;
			int count = 0;
			// Extract the 'obj' part, which is a list of maps
			obj = (List<Map<String, String>>) data.get("obj");
			ScrapInvoice invoice = new ScrapInvoice();
			int contract_id = Integer.parseInt(val.get("contract_id")); //
			String load_id = val.get("scp_load");//
			String invoice_type = val.get("invoice_type");//
			String id = val.get("id");
			String scp_pnitems_id = val.get("scp_pnitems_id");
			String scp_load = val.get("scp_load");
			String created_by = val.get("created_by");
			String product_desc = val.get("product_desc");
			String remarks = val.get("remarks");
			String date_of_notification = val.get("date_of_notification");
			String date_val = val.get("date_val");
			String bg_type = val.get("bg_type");
			String date_of_issue = val.get("date_of_issue");
			String reference_no = val.get("reference_no");
			String lc_number = val.get("lc_number");
			String supply_place = val.get("supply_place");
			String s_t_exempted = val.get("s_t_exempted");
			String lr_docketno = val.get("lr_docketno");
			String bg_no = val.get("bg_no");
			String date_of_expiry = val.get("date_of_expiry");
			String date_of_ref = val.get("date_of_ref");
			String lc_issue_date = val.get("lc_issue_date");

			invoice.setLoad_id(load_id);
			invoice.setInvoice_type(invoice_type);
			// invoice.setQs_packing_item_slno(qs_packing_item_slno);
			invoice.setProductDesc(product_desc);
			invoice.setRemarks(remarks);
			invoice.setDateOfNotification(date_of_notification);
			invoice.setDateVal(date_val);
			invoice.setBgType(bg_type);
			invoice.setDateOfIssue(date_of_issue);
			invoice.setReferenceNo(reference_no);
			invoice.setLcNumber(lc_number);
			invoice.setSupplyPlace(supply_place);
			invoice.setStExempted(s_t_exempted);
			invoice.setLrDocketNo(lr_docketno);
			invoice.setBgNo(bg_no);
			invoice.setDateOfExpiry(date_of_expiry);
			invoice.setDateOfRef(date_of_ref);
			invoice.setLcIssueDate(lc_issue_date);
			invoice.setCreatedBy(created_by);
			// invoice.setContract_name(contract_name);
			LocalDateTime time = LocalDateTime.now();
			invoice.setCreatedDate(time);
			invoice.setScp_load(scp_load);
			invoice.setScp_pnitems_id(scp_pnitems_id);
			invoice.setId(Integer.parseInt(id));
			ScrapInvoice invoiceObj = scrapinvoicerepo.save(invoice);
			for (Map<String, String> item : obj) {
				String service_code_id = item.get("service_code_id");
				String hsn_code_id = item.get("hsn_code_id");
				String type_id = item.get("type_id");
				logger.info("EXECUTING METHOD :: updateScrapInvoice :: BEFORE updatePackingNoteItems");
				int updateScrapPackingNoteRecord = scrapinvoicerepo.updatePackingNoteItemsTable(service_code_id,
						hsn_code_id, type_id, scp_load, scp_pnitems_id, created_by);
				logger.info("EXECUTING METHOD :: updateScrapInvoice :: AFTER updatePackingNoteItems");
			}
			response.put("action", "UPDATE SCRAP INVOICE");
			response.put("message", (invoiceObj != null) ? "Success" : "failure");
			response.put("status", (invoiceObj != null) ? "yes" : "no");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateScrapInvoice  -> " + e.getMessage());

		}
		return response;
	}

	/*
	 * @PostMapping("/scrapinvoice/verification") private @ResponseBody Map<String,
	 * Object> invoiceVerfication(@RequestBody Map<String, Object> data) {
	 * Map<String, Object> response = new HashMap<String, Object>(); int count = 0;
	 * String newSeriesNumber = null; Map<String, String> val = null;
	 * List<Map<String, String>> obj = null;
	 * 
	 * String soe_id = ""; String billingAddressId = ""; String shippingAddressId =
	 * ""; String business_unit_id = ""; try { val = (Map<String, String>)
	 * data.get("val"); obj = (List<Map<String, String>>) data.get("obj"); String
	 * verified_by = val.get("verified_by"); String non_tax_adv =
	 * val.get("non_tax_adv"); String tax_adv = val.get("tax_adv"); String total =
	 * val.get("total"); String payable_by_customer =
	 * val.get("payable_by_customer"); String payable_to_dept =
	 * val.get("payable_to_dept"); String open_tax_adv = val.get("open_tax_adv");
	 * String open_non_tax_adv = val.get("open_non_tax_adv"); String recovery_amt =
	 * val.get("recovery_amt"); int id = Integer.parseInt(val.get("id"));
	 * Optional<Long> optionalInvoiceId = scrapinvoicerepo.getInvoiceValue(id);
	 * Optional<Long> optionalSeriesNumber =
	 * scrapinvoicerepo.getSeriesNumberbasedOnId(id,""); Optional<Long>
	 * optionalInvoiceNumber = scrapinvoicerepo.getInvoiceNumber(); long
	 * invoiceNumber = optionalInvoiceNumber.orElse((long) 0); // read from
	 * i=INVOICE_MASTER last invoice no long serieNumber =
	 * optionalSeriesNumber.orElse((long) 0); if (optionalInvoiceId.isPresent()) {
	 * // if invoice_no already generate and it's release and coming for // approve
	 * once again. if (invoiceNumber == 0) { String num =
	 * String.valueOf(serieNumber); if (num != null && !num.equals("0")) { int size
	 * = num.lastIndexOf("00000"); String partAfterZero = null; if (size != -1) {
	 * partAfterZero = num.substring(size + 5);// total length from start to till
	 * 5(00000) int a = Integer.parseInt(partAfterZero); a = a + 1; partAfterZero =
	 * String.valueOf(a); } Long incrementedValue = Long.parseLong(partAfterZero);
	 * newSeriesNumber = num.substring(0, size + 5) + incrementedValue; count =
	 * scrapinvoicerepo.updateScrapInvoiceVerificationDetails(non_tax_adv, tax_adv,
	 * total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv,
	 * recovery_amt, verified_by, newSeriesNumber, id); String scp_load =
	 * scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(id); String
	 * saleOrderCode =
	 * scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load); List<String>
	 * result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode); for (int i = 0;
	 * i < result.size(); i++) { soe_id = result.get(0); billingAddressId =
	 * result.get(1); shippingAddressId = result.get(2); business_unit_id =
	 * result.get(3);
	 * 
	 * }
	 * 
	 * scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "1"); //
	 * scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"1");
	 * scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderItemLevelEntry(soe_id,"1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderDescEntry(soe_id,"1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "1");
	 * scrapinvoicerepo.detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
	 * for (Map<String, String> item : obj) { String tax_id = item.get("tax_id");
	 * String tax_per = item.get("tax_per"); String tax_value =
	 * item.get("tax_value"); String adv_tax = item.get("adv_tax"); String
	 * tax_payable_by_customer = item.get("tax_payable_by_customer"); String
	 * tax_payable_to_dept = item.get("tax_payable_to_dept"); String t_adv =
	 * item.get("t_adv"); String invoice_id = String.valueOf(id);
	 * scrapinvoicerepo.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value,
	 * adv_tax, tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id,
	 * verified_by);
	 * 
	 * } } else { response.put("Generate_Series_Number",
	 * "Please Generate Series Number"); return response; }
	 * 
	 * } else { if (serieNumber > 0) { String num = String.valueOf(invoiceNumber);
	 * int size = num.lastIndexOf("00000"); String partAfterZero = null; if (size !=
	 * -1) { partAfterZero = num.substring(size + 5);// total length from start to
	 * till 5(00000) (+5 // means number of Zeros) int a =
	 * Integer.parseInt(partAfterZero); a = a + 1; partAfterZero =
	 * String.valueOf(a); } Long incrementedValue = Long.parseLong(partAfterZero);
	 * newSeriesNumber = num.substring(0, size + 5) + incrementedValue; count =
	 * scrapinvoicerepo.updateScrapInvoiceVerificationDetails(non_tax_adv, tax_adv,
	 * total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv,
	 * recovery_amt, verified_by, newSeriesNumber, id); String scp_load =
	 * scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(id); String
	 * saleOrderCode =
	 * scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load); List<String>
	 * result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode); for (int i = 0;
	 * i < result.size(); i++) { soe_id = result.get(0); billingAddressId =
	 * result.get(1); shippingAddressId = result.get(2); business_unit_id =
	 * result.get(3);
	 * 
	 * } scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "1"); //
	 * scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"1");
	 * scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderItemLevelEntry(soe_id,"1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderDescEntry(soe_id,"1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "1");
	 * scrapinvoicerepo.detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
	 * for (Map<String, String> item : obj) { String tax_id = item.get("tax_id");
	 * String tax_per = item.get("tax_per"); String tax_value =
	 * item.get("tax_value"); String adv_tax = item.get("adv_tax"); String
	 * tax_payable_by_customer = item.get("tax_payable_by_customer"); String
	 * tax_payable_to_dept = item.get("tax_payable_to_dept"); String t_adv =
	 * item.get("t_adv"); String invoice_id = String.valueOf(id);
	 * scrapinvoicerepo.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value,
	 * adv_tax, tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id,
	 * verified_by); } } else { response.put("Generate_Series_Number",
	 * "Please Generate Series Number"); return response; } } } else { if
	 * (invoiceNumber == 0) { String num = String.valueOf(serieNumber); if (num !=
	 * null && !num.equals("0")) { int size = num.lastIndexOf("00000"); String
	 * partAfterZero = null; if (size != -1) { partAfterZero = num.substring(size +
	 * 5);// total length from start to till 5(00000) int a =
	 * Integer.parseInt(partAfterZero); a = a + 1; partAfterZero =
	 * String.valueOf(a); } Long incrementedValue = Long.parseLong(partAfterZero);
	 * newSeriesNumber = num.substring(0, size + 5) + incrementedValue; count =
	 * scrapinvoicerepo.updateScrapInvoiceVerificationDetails(non_tax_adv, tax_adv,
	 * total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv,
	 * recovery_amt, verified_by, newSeriesNumber, id); String scp_load =
	 * scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(id); String
	 * saleOrderCode =
	 * scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load); List<String>
	 * result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode); for (int i = 0;
	 * i < result.size(); i++) { soe_id = result.get(0); billingAddressId =
	 * result.get(1); shippingAddressId = result.get(2); business_unit_id =
	 * result.get(3);
	 * 
	 * } scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "1"); //
	 * scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"1");
	 * scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderItemLevelEntry(soe_id,"1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderDescEntry(soe_id,"1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "1");
	 * scrapinvoicerepo.detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
	 * for (Map<String, String> item : obj) { String tax_id = item.get("tax_id");
	 * String tax_per = item.get("tax_per"); String tax_value =
	 * item.get("tax_value"); String adv_tax = item.get("adv_tax"); String
	 * tax_payable_by_customer = item.get("tax_payable_by_customer"); String
	 * tax_payable_to_dept = item.get("tax_payable_to_dept"); String t_adv =
	 * item.get("t_adv"); String invoice_id = String.valueOf(id);
	 * scrapinvoicerepo.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value,
	 * adv_tax, tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id,
	 * verified_by);
	 * 
	 * } } else { response.put("Generate_Series_Number",
	 * "Please Generate Series Number"); return response; }
	 * 
	 * } else { if (serieNumber > 0) { String num = String.valueOf(invoiceNumber);
	 * int size = num.lastIndexOf("00000"); String partAfterZero = null; if (size !=
	 * -1) { partAfterZero = num.substring(size + 5);// total length from start to
	 * till 5(00000) (+5 // means number of Zeros) int a =
	 * Integer.parseInt(partAfterZero); a = a + 1; partAfterZero =
	 * String.valueOf(a); } Long incrementedValue = Long.parseLong(partAfterZero);
	 * newSeriesNumber = num.substring(0, size + 5) + incrementedValue; count =
	 * scrapinvoicerepo.updateScrapInvoiceVerificationDetails(non_tax_adv, tax_adv,
	 * total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv,
	 * recovery_amt, verified_by, newSeriesNumber, id); String scp_load =
	 * scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(id); String
	 * saleOrderCode =
	 * scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load); List<String>
	 * result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode); for (int i = 0;
	 * i < result.size(); i++) { soe_id = result.get(0); billingAddressId =
	 * result.get(1); shippingAddressId = result.get(2); business_unit_id =
	 * result.get(3);
	 * 
	 * } scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "1"); //
	 * scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"1");
	 * scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderItemLevelEntry(soe_id,"1"); //
	 * scrapinvoicerepo.updateIsLockSaleOrderDescEntry(soe_id,"1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "1");
	 * scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "1");
	 * scrapinvoicerepo.detele_SCRAP_INVOICE_TAXENTRY_DETAILS(String.valueOf(id));
	 * for (Map<String, String> item : obj) { String tax_id = item.get("tax_id");
	 * String tax_per = item.get("tax_per"); String tax_value =
	 * item.get("tax_value"); String adv_tax = item.get("adv_tax"); String
	 * tax_payable_by_customer = item.get("tax_payable_by_customer"); String
	 * tax_payable_to_dept = item.get("tax_payable_to_dept"); String t_adv =
	 * item.get("t_adv"); String invoice_id = String.valueOf(id);
	 * scrapinvoicerepo.insertINVOICE_TAXENTRY_DETAILS(tax_id, tax_per, tax_value,
	 * adv_tax, tax_payable_by_customer, tax_payable_to_dept, t_adv, invoice_id,
	 * verified_by); } } else { response.put("Generate_Series_Number",
	 * "Please Generate Series Number"); return response; } } }
	 * 
	 * response.put("message", (count > 0) ? "Success" : "failure");
	 * response.put("status", (count > 0) ? "yes" : "no"); response.put("action",
	 * "VERIFICATION_Record_In_INVOICE_MASTER"); response.put("Invoice_NO",
	 * newSeriesNumber);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return response; }
	 */

	@SuppressWarnings("unused")
	@PostMapping("/Scrapinvoice/releasebyid")
	public @ResponseBody Map<String, Object> releaseById(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		String soe_id = "";
		String billingAddressId = "";
		String shippingAddressId = "";
		String business_unit_id = "";
		try {
			String id = val.get("id");
			int count = scrapinvoicerepo.UpdateReleaseById(id);
			String scp_load = scrapinvoicerepo.getSaleOrderEntryIdFromInvoiceMaster(Integer.parseInt(id));
			String saleOrderCode = scrapinvoicerepo.getSaleOrderCodeFromScrapPackingNote(scp_load);
			List<String> result = scrapinvoicerepo.getSaleOrderEntryId(saleOrderCode);
			for (int i = 0; i < result.size(); i++) {
				soe_id = result.get(0);
				billingAddressId = result.get(1);
				shippingAddressId = result.get(2);
				business_unit_id = result.get(3);

			}
			scrapinvoicerepo.updateIsLockInScrapPackingNote(scp_load, "0");
			// scrapinvoicerepo.updateIsLockScrapPackingNoteEntry(scp_load,"0");
			scrapinvoicerepo.updateIsLockSaleOrderEntry(saleOrderCode, "0");
			scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(billingAddressId, "0");
			scrapinvoicerepo.updateIsLockInvoiceConsigneeAddress(shippingAddressId, "0");
			scrapinvoicerepo.updateIsLockBusinessUnit(business_unit_id, "0");
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "RELEASE_Record_In_SCRAP_INVOICE_MASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
