package com.JIMS.integration.controller;

import java.io.File;
import java.time.LocalDateTime;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.DcPackingNoteInfo;
import com.JIMS.integration.interfaces.DCScrapPackingNoteBsdOnNoteTypeInterface;
import com.JIMS.integration.interfaces.DCScrapPackingNoteInterface;
import com.JIMS.integration.interfaces.DCScrapPackingNoteItemsInterface;
import com.JIMS.integration.repository.ScrapPackingNoteDCRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/jssl")
public class ScrapPackingNoteDCController {

	@Autowired
	private ScrapPackingNoteDCRepository scrapPackingNoteDCRepo;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/dcPackingNote/addDCPackingNote", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody Map<String, Object> addDebitCreditScrapPackingNote(@RequestParam("file") MultipartFile file,
			@RequestParam("dcPackingDetails") String dcPackingDetails) {

		Map<String, Object> debitCreditScrapPackingNotemap = new HashMap<>();
		String uniqueFileName = null;
		Map<String, String> packingNoteobject = null;
		List<Map<String, String>> packingNoteItemsobject = null;
		int saveDCPackingNoteItemsRecord = 0;

		String newDCLoad = null;

		try {
			// Parse the dcPackingDetails string into a Map
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> dcPackingDetailsMap = objectMapper.readValue(dcPackingDetails, Map.class);

			packingNoteobject = (Map<String, String>) dcPackingDetailsMap.get("dcPackingNoteDetails");
			packingNoteItemsobject = (List<Map<String, String>>) dcPackingDetailsMap.get("dcPackingNoteItemsDetails");

			String fileName = file.getOriginalFilename();
			if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
				debitCreditScrapPackingNotemap.put("message", "Only PDF files are allowed.");
				return debitCreditScrapPackingNotemap;
			}

			String mimeType = file.getContentType();
			if (mimeType == null || !mimeType.equals("application/pdf")) {
				debitCreditScrapPackingNotemap.put("message", "Only PDF files are allowed.");
				return debitCreditScrapPackingNotemap;
			}

			// Create unique file name
			uniqueFileName = file.getOriginalFilename();
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			// Handle file upload

			// Check if the file already exists
			File serverFile = new File(directory, uniqueFileName);
			if (serverFile.exists()) {
				debitCreditScrapPackingNotemap.put("message", "File already exists! Please provide another file.");
				return debitCreditScrapPackingNotemap;
			}

			// Save the file to the server
			file.transferTo(serverFile);

			// Ensure the required details are present
			if (packingNoteobject == null || packingNoteobject.isEmpty() || packingNoteItemsobject == null
					|| packingNoteItemsobject.isEmpty()) {
				debitCreditScrapPackingNotemap.put("message", "Invalid packing details.");
				debitCreditScrapPackingNotemap.put("status", "no");
				return debitCreditScrapPackingNotemap;
			}

			// Determine last DC load and create a new load number
			String note_type = packingNoteobject.get("note_type");
			String lastDcLoad = scrapPackingNoteDCRepo.findLastDcLoad(note_type);

			if (lastDcLoad != null) {
				int numericPart = Integer.parseInt(lastDcLoad.replace(note_type + "-", ""));
				int digitLength = lastDcLoad.split("-")[1].length();
				numericPart++;
				if ("DBT".equals(note_type)) {
					newDCLoad = String.format("DBT-%0" + digitLength + "d", numericPart);
				} else if ("CDT".equals(note_type)) {
					newDCLoad = String.format("CDT-%0" + digitLength + "d", numericPart);
				}
			} else {
				// If no last DC load found, set to default values
				if ("DBT".equals(note_type)) {
					newDCLoad = "DBT-00001"; // Default for DBT
				} else if ("CDT".equals(note_type)) {
					newDCLoad = "CDT-00001"; // Default for CDT
				}
			}

			// Create and populate DcPackingNoteInfo object
			DcPackingNoteInfo dcpnInfo = new DcPackingNoteInfo();
			dcpnInfo.setCon_id(packingNoteobject.get("con_id"));
			dcpnInfo.setMilestone_id(packingNoteobject.get("milestone_id"));
			dcpnInfo.setNote_type(note_type);
			dcpnInfo.setDc_load(newDCLoad);
			dcpnInfo.setFilepath(newDCLoad);
			dcpnInfo.setFreight(packingNoteobject.get("freight"));
			dcpnInfo.setCreated_by(packingNoteobject.get("created_by"));
			dcpnInfo.setFactory_id(packingNoteobject.get("factory_id"));
			dcpnInfo.setCreated_date(LocalDateTime.now());

			LocalDateTime created_date = dcpnInfo.getCreated_date();
			// Save the packing note info to the database
			scrapPackingNoteDCRepo.save(dcpnInfo);

			// Save packing note items to the database
			for (Map<String, String> itemsObject : packingNoteItemsobject) {

				String uom_id = itemsObject.get("uom_id");
				String quantities = itemsObject.get("quantities");
				String kgs = itemsObject.get("kgs");
				String unit_price = itemsObject.get("unit_price");
				String total = itemsObject.get("total");
				String type_id = itemsObject.get("type_id");
				String factory_id = itemsObject.get("factory_id");

				saveDCPackingNoteItemsRecord = scrapPackingNoteDCRepo.saveDCPackingNoteItemsRecord(
						dcpnInfo.getDc_pn_id(), note_type, newDCLoad, uom_id, quantities, kgs, unit_price, total,
						type_id, factory_id, packingNoteobject.get("created_by"), created_date);
			}

			// Prepare Success response
			debitCreditScrapPackingNotemap.put("message",
					(saveDCPackingNoteItemsRecord > 0) ? "Success" : "Debit/Credit Packing note not created");
			debitCreditScrapPackingNotemap.put("status", (saveDCPackingNoteItemsRecord > 0) ? "yes" : "no");
			debitCreditScrapPackingNotemap.put("action", "CreateDebit/CreditPackingNote");

		} catch (Exception e) {
			e.printStackTrace();
			debitCreditScrapPackingNotemap.put("message", "Error: " + e.getMessage());
		}

		return debitCreditScrapPackingNotemap;
	}

	
	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping(value = "/dcPackingNote/updateDCPackingNote", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody Map<String, Object> updateDebitCreditScrapPackingNote(@RequestParam("file") MultipartFile file,
			@RequestParam("dcPackingDetails") String dcPackingDetails) {

		Map<String, Object> debitCreditScrapPackingNotemap = new HashMap<>();
		String uniqueFileName = null;
		Map<String, String> packingNoteobject = null;
		List<Map<String, String>> packingNoteItemsobject = null;
		int saveDCPackingNoteItemsRecord = 0;

		String newDCLoad = null;

		try {

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> dcPackingDetailsMap = objectMapper.readValue(dcPackingDetails, Map.class);

			packingNoteobject = (Map<String, String>) dcPackingDetailsMap.get("dcPackingNoteDetails");
			packingNoteItemsobject = (List<Map<String, String>>) dcPackingDetailsMap.get("dcPackingNoteItemsDetails");

			// Ensure the required details are present
			if (packingNoteobject == null || packingNoteobject.isEmpty() || packingNoteItemsobject == null
					|| packingNoteItemsobject.isEmpty()) {
				debitCreditScrapPackingNotemap.put("message", "Invalid packing details.");
				debitCreditScrapPackingNotemap.put("status", "no");
				return debitCreditScrapPackingNotemap;
			}

			String fileName = file.getOriginalFilename();
			if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
				debitCreditScrapPackingNotemap.put("message", "Only PDF files are allowed.");
				return debitCreditScrapPackingNotemap;
			}

			String mimeType = file.getContentType();
			if (mimeType == null || !mimeType.equals("application/pdf")) {
				debitCreditScrapPackingNotemap.put("message", "Only PDF files are allowed.");
				return debitCreditScrapPackingNotemap;
			}

			// Create unique file name
			uniqueFileName = file.getOriginalFilename();
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			// Handle file upload

			// Check if the file already exists
			File serverFile = new File(directory, uniqueFileName);
			if (serverFile.exists()) {
				debitCreditScrapPackingNotemap.put("message", "File already exists! Please provide another file.");
				return debitCreditScrapPackingNotemap;
			}

			// Save the file to the server
			file.transferTo(serverFile);

			DcPackingNoteInfo dcpnInfo = new DcPackingNoteInfo();
			String filepath = uniqueFileName;

			String note_type = packingNoteobject.get("note_type");
			String modified_by = packingNoteobject.get("modified_by");
			String dc_load = packingNoteobject.get("dc_load");
			dcpnInfo.setModified_date(LocalDateTime.now());

			LocalDateTime modified_date = LocalDateTime.now();

		
			int updateDCPackingNoteDetails = scrapPackingNoteDCRepo.updateDCPackingNoteDetails(filepath, modified_by,
					modified_date, dc_load);

			int dc_pn_id = scrapPackingNoteDCRepo.getDCPackingNoteIdForTheDcLoad(dc_load);
			// Save packing note items to the database
			for (Map<String, String> itemsObject : packingNoteItemsobject) {

				String dc_pn_items_id = itemsObject.get("dc_pn_items_id");
				String uom_id = itemsObject.get("uom_id");
				String quantities = itemsObject.get("quantities");
				String kgs = itemsObject.get("kgs");
				String unit_price = itemsObject.get("unit_price");
				String total = itemsObject.get("total");
				String type_id = itemsObject.get("type_id");
				String factory_id = itemsObject.get("factory_id");

				if (dc_pn_items_id != null && !dc_pn_items_id.isEmpty()) {
					saveDCPackingNoteItemsRecord = scrapPackingNoteDCRepo.updateDCPackingNote(uom_id, quantities, kgs,
							unit_price, total, type_id, modified_by, modified_date, dc_load, dc_pn_items_id);
				} else {
					// Otherwise, insert a new item (dc_pn_items_id will be auto-generated)
					saveDCPackingNoteItemsRecord = scrapPackingNoteDCRepo.insertDCPackingNote(dc_pn_id, note_type,
							dc_load, uom_id, quantities, kgs, unit_price, total, type_id, modified_by, modified_date,
							factory_id);
				}
			}

			// Prepare Success response
			debitCreditScrapPackingNotemap.put("message",
					(saveDCPackingNoteItemsRecord > 0) ? "Success" : "Debit/Credit Packing note not Updated");
			debitCreditScrapPackingNotemap.put("status", (saveDCPackingNoteItemsRecord > 0) ? "yes" : "no");
			debitCreditScrapPackingNotemap.put("action", "UpdateCreateDebit/CreditPackingNote");

		} catch (Exception e) {
			e.printStackTrace();
			debitCreditScrapPackingNotemap.put("message", "Error: " + e.getMessage());
		}

		return debitCreditScrapPackingNotemap;
	}

	@SuppressWarnings("unused")
	@GetMapping("/dcPackingNote/getAllDCScrapPackingNote/{factory_id}")
	public @ResponseBody Map<String, Object> getAllDCScrapNoteDetails(@PathVariable String factory_id) {

		Map<String, Object> allDcScrapPackingNotemap = new HashMap<>();
		try {
			// Fetch the packing note details
			List<DCScrapPackingNoteInterface> dcPackingNoteDetails = scrapPackingNoteDCRepo
					.getDCPackingNoteDetails(factory_id);
			List<DCScrapPackingNoteItemsInterface> itemDetails = null;
			// Check if there are any packing note details
			if (dcPackingNoteDetails == null || dcPackingNoteDetails.isEmpty()) {
				allDcScrapPackingNotemap.put("message", "No scrap packing note details available");
				allDcScrapPackingNotemap.put("status", "no");
				return allDcScrapPackingNotemap;
			}

			// Map to hold dc_load to item details mapping
			Map<String, List<DCScrapPackingNoteItemsInterface>> dcPackingNoteItemsMap = new HashMap<>();

			// Iterate over each packing note to fetch its item details
			for (DCScrapPackingNoteInterface packingNote : dcPackingNoteDetails) {
				String dcLoad = packingNote.getDc_load();
				itemDetails = scrapPackingNoteDCRepo.getDCPackingNoteItemDetails(dcLoad, factory_id);

			}

			// Prepare the response
			allDcScrapPackingNotemap.put("message", "Success");
			allDcScrapPackingNotemap.put("status", "yes");
			allDcScrapPackingNotemap.put("ScrapDCPackingNoteDetails", dcPackingNoteDetails);
			allDcScrapPackingNotemap.put("ScrapDCPackingNoteItemDetails", itemDetails);

		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();

		}

		return allDcScrapPackingNotemap;
	}

	@GetMapping("/dcPackingNote/getDcPackingNoteBasedOnId/{dc_load}")
	public @ResponseBody Map<String, Object> getDCScrapPackingNoteById(@PathVariable String dc_load) {

		Map<String, Object> dcScrapPackingNoteByIdmap = new HashMap<String, Object>();
		DCScrapPackingNoteInterface packingNoteDetails = null;
		List<DCScrapPackingNoteItemsInterface> itemDetails = null;
		try {
			packingNoteDetails = scrapPackingNoteDCRepo.getDCScrapPackingNoteDetailsBasedOnPackingNote(dc_load);

			itemDetails = scrapPackingNoteDCRepo.getDCScrapItemDetails(dc_load);

			dcScrapPackingNoteByIdmap.put("DCScrapPackingNoteItems", itemDetails);
			dcScrapPackingNoteByIdmap.put("DCScrapPackingNote", packingNoteDetails);

			dcScrapPackingNoteByIdmap.put("message",
					(packingNoteDetails != null && itemDetails != null) ? "Success" : "failure");
			dcScrapPackingNoteByIdmap.put("status", (packingNoteDetails != null && itemDetails != null) ? "yes" : "no");
			dcScrapPackingNoteByIdmap.put("action", "DCScrapPackingNoteBasedOnId");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dcScrapPackingNoteByIdmap;
	}

	@GetMapping("/dcPackingNote/getDcPackingNoteBasedOnNoteType/{note_type}")
	public @ResponseBody Map<String, Object> getDCScrapPackingNoteBasedOnDebitAndCredit(
			@PathVariable String note_type) {

		Map<String, Object> dcScrapPackingNoteMasedOnNotetype = new HashMap<String, Object>();

		List<DCScrapPackingNoteBsdOnNoteTypeInterface> pnDetailsBsdOnNotetype = null;
		try {
			pnDetailsBsdOnNotetype = scrapPackingNoteDCRepo.getDCScrapPackingNoteDetailsBasedOnNoteType(note_type);

			dcScrapPackingNoteMasedOnNotetype.put("message",
					(pnDetailsBsdOnNotetype != null && !pnDetailsBsdOnNotetype.isEmpty()) ? "Success" : "failure");
			dcScrapPackingNoteMasedOnNotetype.put("status",
					(pnDetailsBsdOnNotetype != null && !pnDetailsBsdOnNotetype.isEmpty()) ? "yes" : "no");
			dcScrapPackingNoteMasedOnNotetype.put("action", "DCPackingNoteBasedOnNoteType");
			
			
			if ((pnDetailsBsdOnNotetype != null) && (!pnDetailsBsdOnNotetype.isEmpty())) {
				dcScrapPackingNoteMasedOnNotetype.put("DcPnDetails", pnDetailsBsdOnNotetype);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dcScrapPackingNoteMasedOnNotetype;
	}

	/*
	 * public @ResponseBody Map<String, Object> addInvoice(@RequestBody Map<String,
	 * Object> invoicedata){
	 * 
	 * Map<String, Object> addInvoicemap = new HashMap<String, Object>();
	 * 
	 * List<St> try {
	 * 
	 * 
	 * 
	 * } catch (Exception e) { // TODO: handle exception } return addInvoicemap;
	 * 
	 * 
	 * }
	 */

}
