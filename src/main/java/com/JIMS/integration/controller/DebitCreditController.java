package com.JIMS.integration.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.dbtcrdscrapInvoiceMaster;
import com.JIMS.integration.interfaces.AssignToContract;
import com.JIMS.integration.interfaces.ContractorDetailsInterface;
import com.JIMS.integration.interfaces.DebitCreditPackNoteIteminterface;
import com.JIMS.integration.interfaces.DebitCreditPackNoteinterface;
import com.JIMS.integration.interfaces.Debitcreditinvoiceinterface;
import com.JIMS.integration.interfaces.ListAssignMilesonetoContractors;
import com.JIMS.integration.interfaces.OtherInvoiceInterface;
import com.JIMS.integration.interfaces.PackingNoteInterface;
import com.JIMS.integration.interfaces.ScrapDebitCreditInvoiceInterface;
import com.JIMS.integration.interfaces.SteelPackingNoteItemsInfo;
import com.JIMS.integration.interfaces.TaxMasterInterface;
import com.JIMS.integration.interfaces.dbtcrdpackingitemsscrap_interface;
import com.JIMS.integration.repository.AssignToContractRepository;
import com.JIMS.integration.repository.DebitCreditPackingRepository;
import com.JIMS.integration.repository.Debitcreditinvoicerepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class DebitCreditController {
	Logger logger = LogManager.getLogger(DebitCreditController.class);
	@Autowired
	public Debitcreditinvoicerepository debitCreditinvoicerepo;
	
	@Autowired
	public DebitCreditPackingRepository debitCreditrepo;
	@Autowired
	public AssignToContractRepository assignToContractRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	@PostMapping("/scrapDebitCredit/addDebitCreditPackingNote")
	public @ResponseBody Map<String, Object> addDebitCreditPackingNote(@RequestParam String invoice_type_id,
			@RequestParam String note_type,
			@RequestParam String freight, @RequestParam String file_path, @RequestParam String factory_id,
			@RequestParam List<String> uom_id, @RequestParam List<String> type_id, @RequestParam List<String> quantity, @RequestParam List<String> kgs,
			@RequestParam List<String> unit_price, @RequestParam List<String> total,
			@RequestParam List<String> remarkstype_id, @RequestParam String totalpn_amount,
			@RequestParam("file") MultipartFile file, @RequestParam String created_by) {

		Map<String, Object> addDebitCreditPackingNotemap = new HashMap<String, Object>();
		String uniqueFileName = null;
		int insertPackingNoteItemrec = 0;
		String newDCLoad = null;
		long maxSize = 10 * 1024 * 1024;
		int size = quantity.size();
		if (size != kgs.size() || size != unit_price.size() || size != total.size() || size != uom_id.size()
				|| size != type_id.size()) {
			addDebitCreditPackingNotemap.put("message", "All input lists must have the same size!");
			return addDebitCreditPackingNotemap;
		}
		try {

			String fileName = file.getOriginalFilename();
			if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
				addDebitCreditPackingNotemap.put("message", "Only PDF files are allowed.");
				return addDebitCreditPackingNotemap;
			}

			if (file.getSize() > maxSize) {
				addDebitCreditPackingNotemap.put("message", "File size exceeds the maximum limit of 10MB.");
				addDebitCreditPackingNotemap.put("status", "no");
				return addDebitCreditPackingNotemap;
			}

			// uniqueFileName = file.getOriginalFilename();
			uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File serverFile = new File(directory, uniqueFileName);
			if (serverFile.exists()) {
				addDebitCreditPackingNotemap.put("message", "File already exists! Please provide another file.");
				return addDebitCreditPackingNotemap;
			}

			// Save the file to the server
			file.transferTo(serverFile);

			String lastDcLoad = debitCreditrepo.findLastDcLoad(note_type);

			if (lastDcLoad != null) {
				int numericPart = Integer.parseInt(lastDcLoad.replace(note_type + "-", ""));
				int digitLength = lastDcLoad.split("-")[1].length();
				numericPart++;
				if ("DBT".equals(note_type)) {
					newDCLoad = String.format("DBT-%0" + digitLength + "d", numericPart);
				} else if ("CRD".equals(note_type)) {
					newDCLoad = String.format("CRD-%0" + digitLength + "d", numericPart);
				}
			} else {
				// If no last DC load found, set to default values
				if ("DBT".equals(note_type)) {
					newDCLoad = "DBT-00001"; // Default for DBT
				} else if ("CRD".equals(note_type)) {
					newDCLoad = "CRD-00001"; // Default for CDT
				}
				
			}

			System.out.println("Generated newDCLoad: " + newDCLoad);

			int insertToPackingNoterec = debitCreditrepo.insertIntoDCPackingNote(newDCLoad, invoice_type_id, note_type,
					freight, uniqueFileName, factory_id, created_by, totalpn_amount);

			String dc_pn_id = debitCreditrepo.getDc_pnidFromPackingNote(newDCLoad);

			for (int i = 0; i < size; i++) {

				insertPackingNoteItemrec = debitCreditrepo.insertDCPackingNoteItemDetails(dc_pn_id, note_type,
						newDCLoad, uom_id.get(i), type_id.get(i), quantity.get(i), kgs.get(i), unit_price.get(i), total.get(i),
						"1", created_by);

			}

			addDebitCreditPackingNotemap.put("message",
					(insertToPackingNoterec > 0 && insertPackingNoteItemrec > 0) ? "Success"
							: " Scrap Debit/Credit Packing note not created");
			addDebitCreditPackingNotemap.put("status", (insertPackingNoteItemrec > 0) ? "yes" : "no");
			addDebitCreditPackingNotemap.put("action", "CreateScrapDebit/CreditPackingNote");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return addDebitCreditPackingNotemap;

	}

	@SuppressWarnings("unused")
	@PostMapping("/scrapDebitCredit/updateDebitCreditPackingNote")
	public @ResponseBody Map<String, Object> updateDebitCreditPackingNote(@RequestParam String note_type,
			@RequestParam String freight, @RequestParam String file_path, @RequestParam List<String> uom_id,
			@RequestParam List<String> type_id,@RequestParam List<String> quantity, @RequestParam List<String> kgs, @RequestParam List<String> unit_price,
			@RequestParam List<String> total, @RequestParam List<String> remarkstype_id,
			@RequestParam String totalpn_amount, @RequestParam("file") MultipartFile file,
			@RequestParam String modified_by, @RequestParam String dc_pn_id, @RequestParam List<String> dc_pn_items_id,
			@RequestParam String dcPn_no) {

		Map<String, Object> updateDebitCreditPackingNotemap = new HashMap<String, Object>();
		String uniqueFileName = null;
		int insertPackingNoteItemrec = 0;
		long maxSize = 10 * 1024 * 1024;
		int size = quantity.size();
		if (size != kgs.size() || size != unit_price.size() || size != total.size() || size != uom_id.size()
				|| size != type_id.size()) {
			updateDebitCreditPackingNotemap.put("message", "All input lists must have the same size!");
			return updateDebitCreditPackingNotemap;
		}
		try {

			String fileName = file.getOriginalFilename();
			if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
				updateDebitCreditPackingNotemap.put("message", "Only PDF files are allowed.");
				return updateDebitCreditPackingNotemap;
			}

			if (file.getSize() > maxSize) {
				updateDebitCreditPackingNotemap.put("message", "File size exceeds the maximum limit of 10MB.");
				updateDebitCreditPackingNotemap.put("status", "no");
				return updateDebitCreditPackingNotemap;
			}

			// uniqueFileName = file.getOriginalFilename();
			uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();

			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File serverFile = new File(directory, uniqueFileName);
			if (serverFile.exists()) {
				updateDebitCreditPackingNotemap.put("message", "File already exists! Please provide another file.");
				return updateDebitCreditPackingNotemap;
			}

			// Save the file to the server
			file.transferTo(serverFile);

			int updateToPackingNoterec = debitCreditrepo.updateIntoDCPackingNote(freight, uniqueFileName, modified_by,
					totalpn_amount, dcPn_no);

			for (int i = 0; i < size; i++) {

				int	slnoValue = Integer.parseInt(dc_pn_items_id.get(i));
				if(slnoValue != 0) {

					insertPackingNoteItemrec = debitCreditrepo.updateDCPackingNoteItemDetails(uom_id.get(i),type_id.get(i),
							quantity.get(i), kgs.get(i), unit_price.get(i), total.get(i), "1",
							modified_by, dc_pn_items_id.get(i));

				} else {
					// @SuppressWarnings("unused")
					int insertNewRecord = debitCreditrepo.insertDCPackingNoteItemDetailsDuringUpdate(dc_pn_id,
							note_type, dcPn_no, uom_id.get(i),type_id.get(i), quantity.get(i), kgs.get(i), unit_price.get(i),
							total.get(i), "1", modified_by);
				}
			}
			updateDebitCreditPackingNotemap.put("message",
					(updateToPackingNoterec > 0 && insertPackingNoteItemrec > 0) ? "Success"
							: " Scrap Debit/Credit Packing note not updated");
			updateDebitCreditPackingNotemap.put("status", (insertPackingNoteItemrec > 0) ? "yes" : "no");
			updateDebitCreditPackingNotemap.put("action", "UpdateScrapDebit/CreditPackingNote");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateDebitCreditPackingNotemap;

	}

	@GetMapping("/scrapDebitCredit/getAllScrapPackingNoteDetails")
	public @ResponseBody Map<String, Object> getAllScrapPackingNotes(@RequestParam String factory_id) {

		Map<String, Object> allScrapDebitCreditPackingNotemap = new HashMap<>();

		try {
			List<DebitCreditPackNoteinterface> packingNoteInfoList = debitCreditrepo
					.getAllScrapPackingNoteDetails(factory_id);
			List<String> packingNoteNumbers = debitCreditrepo.getAllPackingNoteNumberForScrap(factory_id);

			Map<String, List<DebitCreditPackNoteIteminterface>> itemMap = new HashMap<>();
			for (String pnNo : packingNoteNumbers) {
				List<DebitCreditPackNoteIteminterface> items = debitCreditrepo.getPackingNoteItemsDetails(pnNo);
				if (items != null && !items.isEmpty()) {
					itemMap.put(pnNo, items);
				}
			}

			List<Map<String, Object>> resultList = new ArrayList<>();
			for (DebitCreditPackNoteinterface note : packingNoteInfoList) {
				Map<String, Object> noteMap = new HashMap<>();
				noteMap.put("con_id", note.getcon_id());
				noteMap.put("dcPn_no", note.getDcPn_no());
				noteMap.put("dc_pn_id", note.getDc_pn_id());
				noteMap.put("note_type", note.getNote_type());
				noteMap.put("freight", note.getFreight());
				noteMap.put("totalpn_amount", note.getTotalpn_amount());
				noteMap.put("invoice_type_id", note.getInvoice_type_id());
				noteMap.put("filepath", note.getFilepath());
				noteMap.put("invgen", note.getinvgen());
				

				List<DebitCreditPackNoteIteminterface> items = itemMap.getOrDefault(note.getDcPn_no(),
						new ArrayList<>());
				noteMap.put("DebitCreditPackingNoteItems", items);

				resultList.add(noteMap);
			}

			allScrapDebitCreditPackingNotemap.put("action", "AllScrapPackingNoteDetails");

			if (!resultList.isEmpty()) {
				allScrapDebitCreditPackingNotemap.put("message", "Success");
				allScrapDebitCreditPackingNotemap.put("status", "yes");
				allScrapDebitCreditPackingNotemap.put("ScrapDebitCreditPackingNoteInfo", resultList);
			} else {
				allScrapDebitCreditPackingNotemap.put("message", "ScrapDebitCreditPackingNoteDetailsNotFound");
				allScrapDebitCreditPackingNotemap.put("status", "no");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return allScrapDebitCreditPackingNotemap;
	}
	
	
	@GetMapping("/scrapDebitCredit/getAllScrapPackingNoteDetailsPaged")
	public @ResponseBody Map<String, Object> getAllScrapPackingNotesPaged(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String search) {
 
	    Map<String, Object> response = new HashMap<>();
 
	    try {
 
	        // ✅ STEP 1 - PAGINATION
	        Pageable pageable = PageRequest.of(page, size);
 
	        Page<DebitCreditPackNoteinterface> pageResult =
	                debitCreditrepo.getAllScrapPackingNoteDetailsPaged(factory_id, search, pageable);
 
	        // ✅ STEP 2 - COLLECT PN NUMBERS
	        List<String> pnNos = pageResult.getContent()
	                .stream()
	                .map(DebitCreditPackNoteinterface::getDcPn_no)
	                .filter(pn -> pn != null && !pn.isEmpty())
	                .collect(Collectors.toList());
 
	        // ✅ STEP 3 - BATCH FETCH ITEMS
	        Map<String, List<DebitCreditPackNoteIteminterface>> itemMap = new HashMap<>();
 
	        if (!pnNos.isEmpty()) {
	            List<DebitCreditPackNoteIteminterface> allItems =
	                    debitCreditrepo.getPackingNoteItemsDetailsBatch(pnNos);
 
	            itemMap = allItems.stream()
	                    .collect(Collectors.groupingBy(
	                            DebitCreditPackNoteIteminterface::getDcPn_no));
	        }
 
	        // ✅ STEP 4 - BUILD RESPONSE
	        List<Map<String, Object>> resultList = new ArrayList<>();
 
	        for (DebitCreditPackNoteinterface note : pageResult.getContent()) {
 
	            Map<String, Object> noteMap = new HashMap<>();
	            noteMap.put("con_id",           note.getcon_id());
	            noteMap.put("dcPn_no",          note.getDcPn_no());
	            noteMap.put("dc_pn_id",         note.getDc_pn_id());
	            noteMap.put("note_type",        note.getNote_type());
	            noteMap.put("freight",          note.getFreight());
	            noteMap.put("totalpn_amount",   note.getTotalpn_amount());
	            noteMap.put("invoice_type_id",  note.getInvoice_type_id());
	            noteMap.put("filepath",         note.getFilepath());
	            noteMap.put("invgen",           note.getinvgen());
 
	            // ✅ NO EXTRA QUERY
	            noteMap.put("DebitCreditPackingNoteItems",
	                    itemMap.getOrDefault(note.getDcPn_no(), new ArrayList<>()));
 
	            resultList.add(noteMap);
	        }
 
	        // ✅ STEP 5 - FINAL RESPONSE
	        if (!resultList.isEmpty()) {
	            response.put("message", "Success");
	            response.put("status", "yes");
	        } else {
	            response.put("message", "ScrapDebitCreditPackingNoteDetailsNotFound");
	            response.put("status", "no");
	        }
 
	        response.put("action", "AllScrapPackingNoteDetails");
	        response.put("ScrapDebitCreditPackingNoteInfo", resultList);
	        response.put("totalItems", pageResult.getTotalElements());
	        response.put("currentPage", pageResult.getNumber());
	        response.put("totalPages", pageResult.getTotalPages());
 
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Error occurred while fetching scrap packing notes");
	        response.put("status", "error");
	    }
 
	    return response;
	} 	

	@GetMapping("/scrapDebitCredit/getDebitCreditPackingNoteByPackingNoteNo")
	public @ResponseBody Map<String, Object> getScrapDebitCreditPackingNoteById(@RequestParam String dcPackNoteno) {

		Map<String, Object> getScrapDebitCreditBasedOnIdmap = new HashMap<String, Object>();
		DebitCreditPackNoteinterface dcpackNoteinfo = null;
		List<DebitCreditPackNoteIteminterface> dcPackingNoteItemslist = null;
		try {

			dcpackNoteinfo = debitCreditrepo.getPackingNotescrapInfo(dcPackNoteno);

			dcPackingNoteItemslist = debitCreditrepo.getPackingNoteItemsDetails(dcPackNoteno);

			getScrapDebitCreditBasedOnIdmap.put("action", "AllStatesInfo");
			getScrapDebitCreditBasedOnIdmap.put("message",
					(dcpackNoteinfo != null && dcPackingNoteItemslist.size() > 0) ? "Success"
							: "DebitCreditPackingNoteDetailsNotFound");
			getScrapDebitCreditBasedOnIdmap.put("status",
					(dcpackNoteinfo != null && dcPackingNoteItemslist.size() > 0) ? "yes" : "no");

			if ((dcpackNoteinfo != null && dcPackingNoteItemslist.size() > 0)) {
				getScrapDebitCreditBasedOnIdmap.put("DebitCreditPackingNoteInfo", dcpackNoteinfo);
				getScrapDebitCreditBasedOnIdmap.put("DebitCreditPackingNoteItemInfo", dcPackingNoteItemslist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getScrapDebitCreditBasedOnIdmap;

	}

	@GetMapping("/scrapDebitCredit/viewpdfnew")
	public ResponseEntity<Resource> viewFileNew(@RequestParam String id) {
		try {
			Optional<Integer> value = debitCreditrepo.getdbtcrdPn_id(String.valueOf(id));
			if (value.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			int valuePnid = value.get();
			String filepath = debitCreditrepo.getdbtcrdFilePath(valuePnid);

			String folderPath = getUploadDir().toString();
			String fileName = filepath;

			// Concatenate folder path and file name to create the full file path
			Path path = Paths.get(folderPath, fileName).normalize();

			// Normalize and validate file path
			// Validate the full file path
			if (!Files.exists(path) || !path.startsWith(Paths.get(folderPath).normalize())) {
				throw new RuntimeException("File not found or unauthorized access.");
			}

			// Encode the file name
			String encodedFileName = URLEncoder.encode(path.getFileName().toString(), StandardCharsets.UTF_8);

			// Load the file as a resource

			Resource resource = new UrlResource(path.toUri());
			if (!resource.exists() || !resource.isReadable()) {
				throw new RuntimeException("File not found or is not readable: " + filepath);
			}

			// Set the Content-Disposition header to inline for viewing in the browser
			String contentType = "application/pdf"; // Assuming it's a PDF file
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\"")
					.body(resource);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@PostMapping("/scrapDebitCreditInvoice/addScrapDebitCreditInvoice")
	public @ResponseBody Map<String, Object> addScrapDebitCreditInvoice(@RequestParam String invoice_type_id,
			@RequestParam String note_type, @RequestParam String load_id,
			@RequestParam String grand_total, @RequestParam String product_desc,
			@RequestParam String remarks, @RequestParam String business_unit_id, @RequestParam String invoice_to,
			@RequestParam String consignee_id,
			@RequestParam String bank_id, @RequestParam String shipmentmode, @RequestParam String delivery_condition,
			@RequestParam String workorder_id,
			@RequestParam String service_code, @RequestParam String hsn_code, @RequestParam String is_export,
			@RequestParam String vendor_id, @RequestParam String created_by, @RequestParam String factory_id) {

		Map<String, Object> addDebitCreditInvoicemap = new HashMap<String, Object>();
		int insertIntoInvoiceRecord = 0;
		try {

			insertIntoInvoiceRecord = debitCreditrepo.addDebitCreditInvoiceForScrap(invoice_type_id, note_type,
					load_id, grand_total, product_desc, remarks, business_unit_id, invoice_to, consignee_id, bank_id,
					shipmentmode, delivery_condition, workorder_id, service_code, hsn_code, is_export, vendor_id,
					created_by, factory_id);

			addDebitCreditInvoicemap.put("message",
					(insertIntoInvoiceRecord > 0) ? "Success" : "Scrap Debit/Credit Invoice not added");
			addDebitCreditInvoicemap.put("status", (insertIntoInvoiceRecord > 0) ? "yes" : "no");
			addDebitCreditInvoicemap.put("action", "AddDebit/CreditInvoice");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return addDebitCreditInvoicemap;

	}

	@GetMapping("/debitCreditInvoice/getInvoiceById")
	public @ResponseBody Map<String, Object> getInvoiceById(@RequestParam String invoice_id) {

		Map<String, Object> getInvoiceByIdmap = new HashMap<String, Object>();
		ScrapDebitCreditInvoiceInterface invoiceInterface = null;
		List<DebitCreditPackNoteIteminterface> debitCreditItemsDetails = null;
		try {
			invoiceInterface = debitCreditrepo.getInvoiceDetails(invoice_id);

			String dcPackNoteno = debitCreditrepo.getPackingNoteNo(invoice_id);

			debitCreditItemsDetails = debitCreditrepo.getPackingNoteItemsDetailsDuringINvoice(dcPackNoteno);

			getInvoiceByIdmap.put("action", "Debit/CreditInvoiceById");
			getInvoiceByIdmap.put("message",
					(invoiceInterface != null && debitCreditItemsDetails.size() > 0) ? "Success"
							: "Debit/Credit Invoice details not found!");
			getInvoiceByIdmap.put("status",
					(invoiceInterface != null && debitCreditItemsDetails.size() > 0) ? "yes" : "no");
			getInvoiceByIdmap.put("InvoiceDetails", invoiceInterface);
			getInvoiceByIdmap.put("InvoiceItemsDetails", debitCreditItemsDetails);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getInvoiceByIdmap;

	}

	@PostMapping("/debitCreditInvoice/updateDebitCreditInvoice")
	public @ResponseBody Map<String, Object> updateDebitCreditInvoice(@RequestParam (required = false) String tax1,
			@RequestParam (required = false) String tax1_per, @RequestParam (required = false) String tax1_value,
			@RequestParam (required = false) String tax2, @RequestParam (required = false) String tax2_per, @RequestParam (required = false) String tax2_value,
			@RequestParam (required = false) String tax3, @RequestParam (required = false) String tax3_per, @RequestParam (required = false) String tax3_value,
			@RequestParam (required = false) String total_tax,
			@RequestParam String totalpn_amount, @RequestParam String product_desc, @RequestParam String remarks,
			@RequestParam String business_unit_id, @RequestParam String invoice_to,
			@RequestParam String consignee_id, @RequestParam String bank_id, @RequestParam String shipmentmode,
			@RequestParam String delivery_condition, @RequestParam String workorder_id,
			@RequestParam String service_code, @RequestParam String hsn_code, @RequestParam String is_export,
			@RequestParam String vendor_id, @RequestParam String modified_by, @RequestParam String invoice_id) {

		Map<String, Object> updateDebitCreditInvoicemap = new HashMap<String, Object>();
		int updateIntoInvoiceRecord = 0;
		try {

			updateIntoInvoiceRecord = debitCreditrepo.updateDebitCreditInvoiceForScrap(tax1, tax1_per, tax1_value, tax2,
					tax2_per, tax2_value, tax3, tax3_per, tax3_value, total_tax, totalpn_amount, product_desc, remarks,
					business_unit_id, invoice_to, consignee_id, bank_id, shipmentmode, delivery_condition, workorder_id,
					service_code, hsn_code, is_export, vendor_id, modified_by, invoice_id);

			updateDebitCreditInvoicemap.put("message",
					(updateIntoInvoiceRecord > 0) ? "Success" : "Scrap Debit/Credit Invoice not Updated");
			updateDebitCreditInvoicemap.put("status", (updateIntoInvoiceRecord > 0) ? "yes" : "no");
			updateDebitCreditInvoicemap.put("action", "UpdateDebit/CreditInvoice");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return updateDebitCreditInvoicemap;

	}

	@GetMapping("/debitCreditInvoice/allDebitCreditInvoices")
	public @ResponseBody Map<String, Object> getAllInvoices(@RequestParam String factory_id) {

		Map<String, Object> getAllDebitCreditInvoicesmap = new HashMap<String, Object>();
		List<ScrapDebitCreditInvoiceInterface> invoiceList = null;

		try {
			invoiceList = debitCreditrepo.getAllInvoiceDetails(factory_id);

			getAllDebitCreditInvoicesmap.put("action", "AllDebitCreditScrapInvoices");
			getAllDebitCreditInvoicesmap.put("message",
					(invoiceList.size() > 0) ? "Success" : "Debit/Credit Invoice details not found!");
			getAllDebitCreditInvoicesmap.put("status", (invoiceList.size() > 0) ? "yes" : "no");

			if ((invoiceList != null) && (!invoiceList.isEmpty())) {

				getAllDebitCreditInvoicesmap.put("Debit/CreditInvoices", invoiceList);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAllDebitCreditInvoicesmap;
	}
	
	@SuppressWarnings("unused")
	@PostMapping("/debitCreditInvoice/verificationnew")
	private @ResponseBody Map<String, Object> debitCreditInvoiceVerficationNew(@RequestParam String id,
			 @RequestParam String verified_by, @RequestParam String gst_remarks) {
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		String newSeriesNumber = null;
		try {
			Optional<Long> optionalSeriesNumber = debitCreditrepo.getSeriesNumberbasedOnId(id);
			long serieNumber = optionalSeriesNumber.orElse((long) 0);
			if (serieNumber < 1) {
				response.put("message", "Please Generate Series Number");
				return response;
			}
			String seriesNumberInString = String.valueOf(serieNumber);
			StringBuilder sbc = new StringBuilder();
			sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
			seriesNumberInString = sbc.toString();
			Optional<Long> optionalInvoiceId = debitCreditrepo
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
				}
			}

			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "VERIFICATION_Record_In_debitCreditInvoice");
			response.put("Invoice_NO", newSeriesNumber);

		} catch (Exception e) {
			//logger.error("ERROR IN THE METHOD :: invoiceVerficationNew  -> " + e.getMessage());

		}
		return response;
	}

	@GetMapping("/scrapDebitCredit/getLoadListForTheContractor")
	public @ResponseBody Map<String, Object> getscraplistloadfromContractorfromQsPacking(@RequestParam int factory_id,
			@RequestParam String con_id, @RequestParam String note_type) {
		//logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ListAssignMilesonetoContractors> count = debitCreditrepo.getscraplistloadContractorfromQs(factory_id,con_id,note_type);
			response.put("action", "List loads assigned contractors");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		//logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}
	// Scrap Ends here .............................

	// Steel Starts from here

	@PostMapping("/steelDebitCredit/addSteelDebitCreditPackingNote")
	public @ResponseBody Map<String, Object> addSteelDebitCreditPackingNote(@RequestParam String invoice_type_id,
			@RequestParam String con_id, @RequestParam String milestone_id, @RequestParam String note_type,
			@RequestParam String freight, @RequestParam String file_path, @RequestParam String factory_id,
			@RequestParam List<String> uom_id, @RequestParam List<String> quantity, @RequestParam List<String> type_id, @RequestParam List<String> kgs,
			@RequestParam List<String> unit_price, @RequestParam List<String> total,
			@RequestParam List<String> remarkstype_id, @RequestParam String totalpn_amount, 
			@RequestParam("file") MultipartFile file, @RequestParam String created_by) {

		Map<String, Object> addSteelDebitCreditPackingNotemap = new HashMap<String, Object>();
		String uniqueFileName = null;
		int insertPackingNoteItemrec = 0;
		String newDCLoad = null;
		long maxSize = 10 * 1024 * 1024;
		int size = quantity.size();
		if (size != kgs.size() || size != unit_price.size() || size != total.size() || size != uom_id.size()
				|| size != type_id.size()) {
			addSteelDebitCreditPackingNotemap.put("message", "All input lists must have the same size!");
			return addSteelDebitCreditPackingNotemap;
		}
		try {

			String fileName = file.getOriginalFilename();
			if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
				addSteelDebitCreditPackingNotemap.put("message", "Only PDF files are allowed.");
				return addSteelDebitCreditPackingNotemap;
			}

			if (file.getSize() > maxSize) {
				addSteelDebitCreditPackingNotemap.put("message", "File size exceeds the maximum limit of 10MB.");
				addSteelDebitCreditPackingNotemap.put("status", "no");
				return addSteelDebitCreditPackingNotemap;
			}
		      String originalFileName = file.getOriginalFilename();

              // 🔴 CHECK DUPLICATE FILE FOR SAME CONTRACT
              int exists = debitCreditinvoicerepo.checkFileExistsForContract(con_id, originalFileName);

              if (exists > 0) {
            	  addSteelDebitCreditPackingNotemap.put("message", "This file is already uploaded for the selected contract,please choose different file.");
            	  addSteelDebitCreditPackingNotemap.put("status", "no");
                  return addSteelDebitCreditPackingNotemap;
              }
              
			// uniqueFileName = file.getOriginalFilename();
			uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File serverFile = new File(directory, uniqueFileName);
			if (serverFile.exists()) {
				addSteelDebitCreditPackingNotemap.put("message", "File already exists! Please provide another file.");
				return addSteelDebitCreditPackingNotemap;
			}

			// Save the file to the server
			file.transferTo(serverFile);

			String lastDcLoad = debitCreditrepo.findLastDcLoad(note_type);

			if (lastDcLoad != null) {
				int numericPart = Integer.parseInt(lastDcLoad.replace(note_type + "-", ""));
				int digitLength = lastDcLoad.split("-")[1].length();
				numericPart++;
				if ("DBT".equals(note_type)) {
					newDCLoad = String.format("DBT-%0" + digitLength + "d", numericPart);
				} else if ("CRD".equals(note_type)) {
					newDCLoad = String.format("CRD-%0" + digitLength + "d", numericPart);
				}
			} else {
				// If no last DC load found, set to default values
				if ("DBT".equals(note_type)) {
					newDCLoad = "DBT-00001"; // Default for DBT
				} else if ("CRD".equals(note_type)) {
					newDCLoad = "CRD-00001"; // Default for CDT
				}
			}

			System.out.println("Generated newDCLoad: " + newDCLoad);

			int insertToPackingNoterec = debitCreditrepo.insertIntoDCPackingNoteSteel(newDCLoad, invoice_type_id,
					con_id,
					milestone_id, note_type, freight, uniqueFileName, factory_id, created_by, totalpn_amount);

			String dc_pn_id = debitCreditrepo.getDc_pnidFromPackingNote(newDCLoad);

			for (int i = 0; i < size; i++) {

				insertPackingNoteItemrec = debitCreditrepo.insertDCPackingNoteItemDetails(dc_pn_id, note_type,
						newDCLoad, uom_id.get(i), type_id.get(i), quantity.get(i), kgs.get(i), unit_price.get(i), total.get(i),
						"1", created_by);

			}

			addSteelDebitCreditPackingNotemap.put("message",
					(insertToPackingNoterec > 0 && insertPackingNoteItemrec > 0) ? "Success"
							: " Scrap Debit/Credit Packing note not created");
			addSteelDebitCreditPackingNotemap.put("status", (insertPackingNoteItemrec > 0) ? "yes" : "no");
			addSteelDebitCreditPackingNotemap.put("action", "CreateScrapDebit/CreditPackingNote");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return addSteelDebitCreditPackingNotemap;

	}

	@SuppressWarnings("unused")
	@PostMapping("/steelDebitCredit/updateSteelDebitCreditPackingNote")
	public @ResponseBody Map<String, Object> updateSteelDebitCreditPackingNote(@RequestParam String note_type,
			@RequestParam String freight, @RequestParam String file_path, @RequestParam List<String> uom_id, @RequestParam List<String> type_id,
			@RequestParam List<String> quantity, @RequestParam List<String> kgs, @RequestParam List<String> unit_price,
			@RequestParam List<String> total, @RequestParam List<String> remarkstype_id,
			@RequestParam String totalpn_amount, @RequestParam("file") MultipartFile file,
			@RequestParam String modified_by, @RequestParam String dc_pn_id, @RequestParam List<String> dc_pn_items_id,
			@RequestParam String dcPn_no) {

		Map<String, Object> updateSteelDebitCreditPackingNotemap = new HashMap<String, Object>();
		String uniqueFileName = null;
		int insertPackingNoteItemrec = 0;
		int size = quantity.size();
		if (size != kgs.size() || size != unit_price.size() || size != total.size() || size != uom_id.size()
				|| size != type_id.size()) {
			updateSteelDebitCreditPackingNotemap.put("message", "All input lists must have the same size!");
			return updateSteelDebitCreditPackingNotemap;
		}
		long maxSize = 10 * 1024 * 1024;
		try {

			String fileName = file.getOriginalFilename();
			if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
				updateSteelDebitCreditPackingNotemap.put("message", "Only PDF files are allowed.");
				return updateSteelDebitCreditPackingNotemap;
			}

			if (file.getSize() > maxSize) {
				updateSteelDebitCreditPackingNotemap.put("message", "File size exceeds the maximum limit of 10MB.");
				updateSteelDebitCreditPackingNotemap.put("status", "no");
				return updateSteelDebitCreditPackingNotemap;
			}

			// uniqueFileName = file.getOriginalFilename();
			uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();

			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File serverFile = new File(directory, uniqueFileName);
			if (serverFile.exists()) {
				updateSteelDebitCreditPackingNotemap.put("message",
						"File already exists! Please provide another file.");
				return updateSteelDebitCreditPackingNotemap;
			}

			// Save the file to the server
			file.transferTo(serverFile);

			int updateToPackingNoterec = debitCreditrepo.updateIntoDCPackingNote(freight, uniqueFileName, modified_by,
					totalpn_amount, dcPn_no);

			for (int i = 0; i < size; i++) {
				int	slnoValue = Integer.parseInt(dc_pn_items_id.get(i));
				if(slnoValue != 0) {

					insertPackingNoteItemrec = debitCreditrepo.updateDCPackingNoteItemDetails(uom_id.get(i),
							type_id.get(i),quantity.get(i), kgs.get(i), unit_price.get(i), total.get(i), "1",
							modified_by, dc_pn_items_id.get(i));

				} else {

					int insertNewRecord = debitCreditrepo.insertDCPackingNoteItemDetailsDuringUpdate(dc_pn_id,
							note_type, dcPn_no, uom_id.get(i), type_id.get(i), quantity.get(i), kgs.get(i), unit_price.get(i),
							total.get(i), "1", modified_by);
				}
			}
			updateSteelDebitCreditPackingNotemap.put("message",
					(updateToPackingNoterec > 0 && insertPackingNoteItemrec > 0) ? "Success"
							: " Scrap Debit/Credit Packing note not updated");
			updateSteelDebitCreditPackingNotemap.put("status", (insertPackingNoteItemrec > 0) ? "yes" : "no");
			updateSteelDebitCreditPackingNotemap.put("action", "UpdateScrapDebit/CreditPackingNote");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateSteelDebitCreditPackingNotemap;

	}

	@GetMapping("/steelDebitCredit/getSteelDebitCreditPackingNoteByPackingNoteNo")
	public @ResponseBody Map<String, Object> getSteelDebitCreditPackingNoteById(@RequestParam String dcPackNoteno) {

		Map<String, Object> getSteelDebitCreditBasedOnIdmap = new HashMap<String, Object>();
		DebitCreditPackNoteinterface dcpackNoteinfo = null;
		List<DebitCreditPackNoteIteminterface> dcPackingNoteItemslist = null;
		try {

			dcpackNoteinfo = debitCreditrepo.getPackingNoteInfo(dcPackNoteno);

			dcPackingNoteItemslist = debitCreditrepo.getPackingNoteItemsDetails(dcPackNoteno);

			getSteelDebitCreditBasedOnIdmap.put("action", "AllStatesInfo");
			getSteelDebitCreditBasedOnIdmap.put("message",
					(dcpackNoteinfo != null && dcPackingNoteItemslist.size() > 0) ? "Success"
							: "DebitCreditPackingNoteDetailsNotFound");
			getSteelDebitCreditBasedOnIdmap.put("status",
					(dcpackNoteinfo != null && dcPackingNoteItemslist.size() > 0) ? "yes" : "no");

			if ((dcpackNoteinfo != null && dcPackingNoteItemslist.size() > 0)) {
				getSteelDebitCreditBasedOnIdmap.put("DebitCreditPackingNoteInfo", dcpackNoteinfo);
				getSteelDebitCreditBasedOnIdmap.put("DebitCreditPackingNoteItemInfo", dcPackingNoteItemslist);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSteelDebitCreditBasedOnIdmap;

	}

	@GetMapping("/steelDebitCredit/getAllSteelPackingNoteDetails")
	public @ResponseBody Map<String, Object> getAllSteelPackingNotes(@RequestParam String factory_id) {

		Map<String, Object> allSteelDebitCreditPackingNotemap = new HashMap<>();

		try {
			List<DebitCreditPackNoteinterface> packingNoteInfoList = debitCreditrepo
					.getAllSteelPackingNoteDetails(factory_id);
			List<String> packingNoteNumbers = debitCreditrepo.getAllPackingNoteNumberForSteel(factory_id);

			Map<String, List<DebitCreditPackNoteIteminterface>> itemMap = new HashMap<>();
			for (String pnNo : packingNoteNumbers) {
				List<DebitCreditPackNoteIteminterface> items = debitCreditrepo.getPackingNoteItemsDetails(pnNo);
				if (items != null && !items.isEmpty()) {
					itemMap.put(pnNo, items);
				}
			}

			List<Map<String, Object>> resultList = new ArrayList<>();
			for (DebitCreditPackNoteinterface note : packingNoteInfoList) {
				Map<String, Object> noteMap = new HashMap<>();
				noteMap.put("con_name", note.getcontract_name());
				noteMap.put("dc_pn_id", note.getDc_pn_id());
				noteMap.put("dcPn_no", note.getDcPn_no());
				noteMap.put("note_type", note.getNote_type());
				noteMap.put("freight", note.getFreight());
				noteMap.put("totalpn_amount", note.getTotalpn_amount());
				noteMap.put("invoice_type_id", note.getInvoice_type_id());
				noteMap.put("filepath", note.getFilepath());
				noteMap.put("invgen", note.getinvgen());
				noteMap.put("con_id", note.getcon_id());

				List<DebitCreditPackNoteIteminterface> items = itemMap.getOrDefault(note.getDcPn_no(),
						new ArrayList<>());
				noteMap.put("DebitCreditPackingNoteItems", items);

				resultList.add(noteMap);
			}

			allSteelDebitCreditPackingNotemap.put("action", "AllSteelPackingNoteDetails");

			if (!resultList.isEmpty()) {
				allSteelDebitCreditPackingNotemap.put("message", "Success");
				allSteelDebitCreditPackingNotemap.put("status", "yes");
				allSteelDebitCreditPackingNotemap.put("SteelDebitCreditPackingNoteInfo", resultList);
			} else {
				allSteelDebitCreditPackingNotemap.put("message", "SteelDebitCreditPackingNoteDetailsNotFound");
				allSteelDebitCreditPackingNotemap.put("status", "no");
			}

		} catch (Exception e) {
			e.printStackTrace();
			allSteelDebitCreditPackingNotemap.put("message", "Error occurred while fetching packing note details.");
			allSteelDebitCreditPackingNotemap.put("status", "error");
		}

		return allSteelDebitCreditPackingNotemap;
	}

	@PostMapping("/steelDebitCredit/addSteelDebitCreditInvoice")
	public @ResponseBody Map<String, Object> addSteelDebitCreditInvoice(@RequestParam String invoice_type_id,
			@RequestParam String con_id, @RequestParam String contract_name, @RequestParam String note_type, @RequestParam String load_id,
			@RequestParam String grand_total, @RequestParam String product_desc, 	@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val,
			@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
			@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
			@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,
			@RequestParam String remarks, @RequestParam String created_by, @RequestParam String factory_id) {

		Map<String, Object> addDebitCreditInvoicemap = new HashMap<String, Object>();
		int insertIntoInvoiceRecord = 0;
		try {

			insertIntoInvoiceRecord = debitCreditrepo.addDebitCreditInvoiceForSteel(invoice_type_id, con_id, contract_name, note_type,
					load_id, grand_total, product_desc, date_of_notification, date_val,
					bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
					date_of_ref, lc_issue_date, s_t_exempted, remarks, created_by, factory_id);
			addDebitCreditInvoicemap.put("message",
					(insertIntoInvoiceRecord > 0) ? "Success" : "Scrap Debit/Credit Invoice not added");
			addDebitCreditInvoicemap.put("status", (insertIntoInvoiceRecord > 0) ? "yes" : "no");
			addDebitCreditInvoicemap.put("action", "AddSteelDebit/CreditInvoice");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return addDebitCreditInvoicemap;

	}
	
	
	
	@GetMapping("/steelDebitCredit/getAllSteelPackingNoteDetailsPaged")
	public @ResponseBody Map<String, Object> getAllSteelPackingNotesPaged(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String search) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: getAllSteelPackingNotesPaged");

	    try {

	        // ✅ Step 1 - Get paginated notes
	        Pageable pageable = PageRequest.of(page, size);
//	        Page<DebitCreditPackNoteinterface> pageResult =
//	                debitCreditrepo.getAllSteelPackingNoteDetailsPaged(factory_id, pageable);
	        Page<DebitCreditPackNoteinterface> pageResult =
	                debitCreditrepo.getAllSteelPackingNoteDetailsPaged(factory_id, search, pageable);

	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());
	        logger.info("CURRENT PAGE  :: " + pageResult.getNumber());

	        // ✅ Step 2 - Extract all dcPn_no from current page
	        List<String> pnNos = pageResult.getContent()
	                .stream()
	                .map(DebitCreditPackNoteinterface::getDcPn_no)
	                .filter(pn -> pn != null && !pn.isEmpty())
	                .collect(Collectors.toList());

	        logger.info("PN NOS IN PAGE :: " + pnNos);

	        // ✅ Step 3 - Single batch query for all items
	        Map<String, List<DebitCreditPackNoteIteminterface>> itemMap = new HashMap<>();
	        if (!pnNos.isEmpty()) {
	            List<DebitCreditPackNoteIteminterface> allItems =
	                    debitCreditrepo.getPackingNoteItemsDetailsBatch(pnNos);

	            logger.info("TOTAL ITEMS FETCHED :: " + allItems.size());

	            // Group items by dcPn_no
	            itemMap = allItems.stream()
	                    .collect(Collectors.groupingBy(
	                            DebitCreditPackNoteIteminterface::getDcPn_no));
	        }

	        // ✅ Step 4 - Build result list
	        List<Map<String, Object>> resultList = new ArrayList<>();

	        for (DebitCreditPackNoteinterface note : pageResult.getContent()) {

	            Map<String, Object> noteMap = new HashMap<>();
	            noteMap.put("con_name",        note.getcontract_name());
	            noteMap.put("dc_pn_id",        note.getDc_pn_id());
	            noteMap.put("dcPn_no",         note.getDcPn_no());
	            noteMap.put("note_type",       note.getNote_type());
	            noteMap.put("freight",         note.getFreight());
	            noteMap.put("totalpn_amount",  note.getTotalpn_amount());
	            noteMap.put("invoice_type_id", note.getInvoice_type_id());
	            noteMap.put("filepath",        note.getFilepath());
	            noteMap.put("invgen",          note.getinvgen());
	            noteMap.put("con_id",          note.getcon_id());

	            // ✅ Get items from map - no extra DB call
	            noteMap.put("DebitCreditPackingNoteItems",
	                    itemMap.getOrDefault(note.getDcPn_no(), new ArrayList<>()));

	            resultList.add(noteMap);
	        }

	        // ✅ Step 5 - Build response
	        if (!resultList.isEmpty()) {
	            response.put("message", "Success");
	            response.put("status",  "yes");
	        } else {
	            response.put("message", "SteelDebitCreditPackingNoteDetailsNotFound");
	            response.put("status",  "no");
	        }

	        response.put("action",                      "AllSteelPackingNoteDetails");
	        response.put("SteelDebitCreditPackingNoteInfo", resultList);
	        response.put("totalItems",                  pageResult.getTotalElements());
	        response.put("currentPage",                 pageResult.getNumber());
	        response.put("totalPages",                  pageResult.getTotalPages());

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR in getAllSteelPackingNotesPaged :: " + e.getMessage());
	        response.put("message", "Error occurred while fetching packing note details.");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: getAllSteelPackingNotesPaged");
	    return response;
	}
	

	  @PostMapping("/steelDebitCredit/addSteelDebitCreditInvoicescrap")
	  public @ResponseBody Map<String, Object> creatdbtcrdInvoiceNewscrap(
	          @RequestParam String con_id,
	          @RequestParam(required = false)  String invoice_no,
	          @RequestParam String load_id,
	          @RequestParam Double grand_total,
	          @RequestParam String product_desc,
	          @RequestParam String invoice_type_id,
	          @RequestParam String contract_name,
	          @RequestParam String note_type,
	          @RequestParam(required = false) String remarks,
	          @RequestParam(required = false) String export,
	          @RequestParam(required = false) String date_of_notification,
	          @RequestParam(required = false) String date_val,
	          @RequestParam(required = false) String bg_type,
	          @RequestParam(required = false) String date_of_issue,
	          @RequestParam(required = false) String reference_no,
	          @RequestParam(required = false) String lc_number,
	          @RequestParam(required = false) String supply_place,
	          @RequestParam(required = false) String s_t_exempted,
	          @RequestParam(required = false) String lr_docketno,
	          @RequestParam(required = false) String bg_no,
	          @RequestParam(required = false) String date_of_expiry,
	          @RequestParam(required = false) String date_of_ref,
	          @RequestParam(required = false) String lc_issue_date,
	          @RequestParam String created_by,
	          @RequestParam(required = false) String created_date,
	          @RequestParam(required = false) String modified_by,
	          @RequestParam(required = false) String modified_date,
	          @RequestParam(required = false) Boolean is_delete,
	          @RequestParam(required = false) Boolean is_release,
	          @RequestParam(required = false) Integer factory_id,
	          @RequestParam(required = false) Boolean is_verified,
	          @RequestParam(required = false) String verified_by,
	          @RequestParam(required = false) String verified_status,
	          @RequestParam(required = false) String payable_to_dept,
	          @RequestParam(required = false) String payable_by_customer,
	          @RequestParam(required = false) String released_by,
	          @RequestParam(required = false) String released_datetime,
	          @RequestParam(required = false) String gst_remarks,
	          @RequestParam(required = false) Double tax1,
	          @RequestParam(required = false) Double tax1_per,
	          @RequestParam(required = false) Double tax1_value,
	          @RequestParam(required = false) Double tax2,
	          @RequestParam(required = false) Double tax2_per,
	          @RequestParam(required = false) Double tax2_value,
	          @RequestParam(required = false) Double tax3,
	          @RequestParam(required = false) Double tax3_per,
	          @RequestParam(required = false) Double tax3_value,
	          @RequestParam(required = false) Double total_tax,
	          @RequestParam(required = false) Double net_amount,
	          @RequestParam(required = false) String invoice_remarks_id,
	          @RequestParam(required = false) Integer business_unit_id,
	          @RequestParam(required = false) Integer invoice_to_id,
	          @RequestParam(required = false) String prod_desc,
	          @RequestParam(required = false) Integer ship_mode_id,
	          @RequestParam(required = false) Integer workorder_id,
	          @RequestParam(required = false) String area_number,
	          @RequestParam(required = false) String lot_number,
	          @RequestParam(required = false) String container_name,
	          @RequestParam(required = false) String epcg_license,
	          @RequestParam(required = false) String export_title_text_id,
	          @RequestParam(required = false) String tax_onpayable_rev,
	          @RequestParam(required = false) Double percentage,
	          @RequestParam(required = false) Integer consignee_id,
	          @RequestParam(required = false) Integer bank_id,
	          @RequestParam(required = false) Integer deliverycondition_id,
	          @RequestParam(required = false) Integer registered_office_id,
	          @RequestParam(required = false) Integer servicecode_id,
	          @RequestParam(required = false) Integer hsncode_id
	       
	  ) {
	      logger.info("EXECUTING METHOD :: createdbtcrdInvoiceNewscrap");
	      Map<String, Object> response = new HashMap<>();
	      dbtcrdscrapInvoiceMaster invoiceObj = null;

	      try {
	    	  dbtcrdscrapInvoiceMaster invoice = new dbtcrdscrapInvoiceMaster();
	    	  invoice.setInvoice_type_id(invoice_type_id);
	    	  invoice.setNote_type(note_type);
	    	  invoice.setContract_name(contract_name);
	    	  invoice.setCon_id(con_id);
	          invoice.setInvoice_no(invoice_no);
	          invoice.setLoad_id(load_id);
	          invoice.setGrand_total(grand_total);
	          invoice.setProduct_desc(product_desc);
	          invoice.setRemarks(remarks);
	          invoice.setDate_of_notification(date_of_notification);
	          invoice.setDate_val(date_val);
	          invoice.setBg_type(bg_type);
	          invoice.setDate_of_issue(date_of_issue);
	          invoice.setReference_no(reference_no);
	          invoice.setLc_number(lc_number);
	          invoice.setSupply_place(supply_place);
	          invoice.setS_t_exempted(s_t_exempted);
	          invoice.setLr_docketno(lr_docketno);
	          invoice.setBg_no(bg_no);
	          invoice.setDate_of_expiry(date_of_expiry);
	          invoice.setDate_of_ref(date_of_ref);
	          invoice.setLc_issue_date(lc_issue_date);
	          invoice.setCreated_by(created_by);
	          invoice.setFactory_id(factory_id);
	          invoice.setGst_remarks(gst_remarks);
	          invoice.setTax1(tax1);
	          invoice.setTax1_per(tax1_per);
	          invoice.setTax1_value(tax1_value);
	          invoice.setTax2(tax2);
	          invoice.setTax2_per(tax2_per);
	          invoice.setTax2_value(tax2_value);
	          invoice.setTax3(tax3);
	          invoice.setTax3_per(tax3_per);
	          invoice.setTax3_value(tax3_value);
	          invoice.setTotal_tax(total_tax);
	          invoice.setNet_amount(net_amount);
	          invoice.setBusiness_unit_id(business_unit_id);
	          invoice.setInvoice_to_id(invoice_to_id);
	          invoice.setProd_desc(prod_desc);
	          invoice.setShip_mode_id(ship_mode_id);
	          invoice.setWorkorder_id(workorder_id);
	          invoice.setArea_number(area_number);
	          invoice.setLot_number(lot_number);
	          invoice.setContainer_name(container_name);
	          invoice.setEpcg_license(epcg_license);
	          invoice.setExport_title_text_id(export_title_text_id);
	          invoice.setTax_onpayable_rev(tax_onpayable_rev);
	          invoice.setPercentage(percentage);
	          invoice.setConsignee_id(consignee_id);
	          invoice.setBank_id(bank_id);
	          invoice.setDeliverycondition_id(deliverycondition_id);
	          invoice.setRegistered_office_id(registered_office_id);
	          invoice.setServicecode_id(servicecode_id);
	          invoice.setHsncode_id(hsncode_id);
	          invoice.setExport(export);
	          invoice.setIs_release(false);
	          invoice.setInvoice_remarks_id(invoice_remarks_id);
	          invoice.setCreated_date(LocalDateTime.now());

	          logger.info("BEFORE saving INVOICE");
			  invoiceObj = debitCreditinvoicerepo.save(invoice); 
	          logger.info("AFTER saving INVOICE");

	          response.put("action", "INVOICE_ADD");
	          response.put("message", (invoiceObj != null) ? "Success" : "Failure");
	          response.put("status", (invoiceObj != null) ? "yes" : "no");

	      } catch (Exception e) {
	          e.printStackTrace();
	          logger.error("ERROR IN THE METHOD createDlyInvoiceNew :: -> " + e.getMessage());
	          response.put("action", "INVOICE_ADD");
	          response.put("message", e.getMessage());
	          response.put("status", "error");
	      }

	      logger.info("EXECUTED METHOD :: createDlyInvoiceNew");
	      return response;
	  }
	  
	
	@PostMapping("/steelDebitCredit/updateSteelDebitCreditInvoice")
	public @ResponseBody Map<String, Object> updateSteelDebitCreditInvoice(@RequestParam (required = false) String tax1,
			@RequestParam (required = false) String tax1_per, @RequestParam (required = false) String tax1_value,
			@RequestParam (required = false) String tax2, @RequestParam (required = false) String tax2_per, @RequestParam (required = false) String tax2_value,
			@RequestParam (required = false) String tax3, @RequestParam (required = false) String tax3_per, @RequestParam (required = false) String tax3_value,
			@RequestParam (required = false) String total_tax, @RequestParam String totalpn_amount, @RequestParam String product_desc,
			@RequestParam String remarks, @RequestParam String business_unit_id, @RequestParam String invoice_to,
			@RequestParam String consignee_id, @RequestParam String bank_id, @RequestParam String shipmentmode,
			@RequestParam String delivery_condition, @RequestParam String workorder_id,
			@RequestParam String service_code, @RequestParam String hsn_code, @RequestParam String is_export,
			@RequestParam String modified_by, @RequestParam String invoice_id) {

		Map<String, Object> updateSteelDebitCreditInvoicemap = new HashMap<String, Object>();
		int updateIntoInvoiceRecord = 0;
		try {

			updateIntoInvoiceRecord = debitCreditrepo.updateDebitCreditInvoiceForSteel(tax1, tax1_per, tax1_value, tax2,
					tax2_per, tax2_value, tax3, tax3_per, tax3_value, total_tax, totalpn_amount, product_desc, remarks,
					business_unit_id, invoice_to, consignee_id, bank_id, shipmentmode, delivery_condition, workorder_id,
					service_code, hsn_code, is_export, modified_by, invoice_id);

			updateSteelDebitCreditInvoicemap.put("message",
					(updateIntoInvoiceRecord > 0) ? "Success" : "Steel Debit/Credit Invoice not Updated");
			updateSteelDebitCreditInvoicemap.put("status", (updateIntoInvoiceRecord > 0) ? "yes" : "no");
			updateSteelDebitCreditInvoicemap.put("action", "UpdateSteelDebit/CreditInvoice");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return updateSteelDebitCreditInvoicemap;

	}
	
	@PostMapping("/scrapDebitCredit/dbtcrdupdateadvnewscrap")
	public @ResponseBody Map<String, Object> dbtcrdupdateadvInvoiceNewscrap(@RequestParam Integer id,
			@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,  @RequestParam Double grand_total,
			@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val, @RequestParam String pn_id,
			@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
			@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
			@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
			@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,@RequestParam(required = false) Double tax1,
	          @RequestParam(required = false) Double tax1_per,
	          @RequestParam(required = false) Double tax1_value,
	          @RequestParam(required = false) Double tax2,
	          @RequestParam(required = false) Double tax2_per,
	          @RequestParam(required = false) Double tax2_value,
	          @RequestParam(required = false) Double tax3,
	          @RequestParam(required = false) Double tax3_per,
	          @RequestParam(required = false) Double tax3_value,
	          @RequestParam(required = false) Double total_tax,
			@RequestParam(required = false) List<String> service_code_id,
			@RequestParam(required = false) List<String> hsn_code_id, 
			@RequestParam(required = false) List<String> slno) {
		logger.info("EXECUTING METHOD :: updateInvoiceNewscrap ");
		Map<String, Object> response = new HashMap<String, Object>();
		int valueCount = 0;
		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE");
			valueCount = debitCreditrepo.dbtcrdupdateInvoiceEntryInfoscrap(product_desc, remarks, date_of_notification, date_val, tax1, tax1_per, tax1_value, tax2, tax2_per, tax2_value, tax3, tax3_per, tax3_value, grand_total, 
					bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
					date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
			logger.info("EXECUTING METHOD :: AFTER UPDATING INVOICE");
			response.put("action", "INVOICE_UPDATE");
			response.put("message", (valueCount > 0) ? "Success" : "No fields changed.");
			response.put("status", (valueCount > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateInvoiceNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateInvoiceNew ");
		return response;
	}

	@GetMapping("/steelDebitCredit/getContractorDetails")
	public @ResponseBody Map<String, Object> getContractorListfromQsPacking(@RequestParam int factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
//		logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		try {
			List<ContractorDetailsInterface> count = debitCreditrepo.getlistContractorfromQs(factory_id);
			response.put("action", "List Contractor assigned QSPACKING");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
	//	logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}

	@GetMapping("/steelDebitCredit/getLoadListForTheContractor")
	public @ResponseBody Map<String, Object> getlistloadfromContractorfromQsPacking(@RequestParam int factory_id,
			@RequestParam String con_id, @RequestParam String note_type) {
		//logger.info("EXECUTING METHOD :: getlistContractorfromQsPacking");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ListAssignMilesonetoContractors> count = debitCreditrepo.getlistloadContractorfromQs(factory_id,con_id,note_type);
			response.put("action", "List loads assigned contractors");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("ERROR IN THE METHOD FOR getlistContractorfromQsPacking ::   -> " + e.getMessage());
		}
		//logger.info("EXECUTIED METHOD :: getlistContractorfromQsPacking");
		return response;
	}

	public @ResponseBody Map<String, Object> getSteelPackingNoteInfoForInvoice(@RequestParam String con_id,
			@RequestParam String load_id, @RequestParam String factory_id) {

		Map<String, Object> getSteelPackingNoteInfoForInvoicemap = new HashMap<String, Object>();
		PackingNoteInterface packingNoteInfo = null;
		List<SteelPackingNoteItemsInfo> qspackingItemslist = null;

		try {




		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	
	@GetMapping("/invoice/dbtcrdlistnew")
	public @ResponseBody Map<String, Object> listdbtcrdInvoiceInformationNew(@RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: listInvoiceInformationNew");
		List<Debitcreditinvoiceinterface> invoiceMasterInterfaces = null;
		try {
			logger.info("EXECUTING METHOD :: BEFORE LISTING INVOICE");
			invoiceMasterInterfaces = debitCreditinvoicerepo.listdbtcrdInvoiceMasterInfo(factory_id);
			logger.info("EXECUTING METHOD :: AFTER LISTING INVOICE");
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Data", invoiceMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listdbtcrdInvoiceInformationNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTIED METHOD :: listdbtcrdInvoiceInformationNew");
		return response;
	}
	
	
	
	@GetMapping("/invoice/dbtcrdlistnewpaged")
	public @ResponseBody Map<String, Object> listdbtcrdInvoiceInformationNewPaged(
	        @RequestParam String factory_id,
	        @RequestParam(required = false) String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: listdbtcrdInvoiceInformationNewPaged");

	    try {
	        Pageable pageable = PageRequest.of(page, size);

	        Page<Debitcreditinvoiceinterface> pageResult =
	                debitCreditinvoicerepo.listdbtcrdInvoiceMasterInfoPaged(factory_id, search, pageable);

	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	        response.put("message",     "Success");
	        response.put("status",      "yes");
	        response.put("action",      "List_Record_IN_INVOICE_MASTER");
	        response.put("Data",        pageResult.getContent());
	        response.put("totalItems",  pageResult.getTotalElements());
	        response.put("currentPage", pageResult.getNumber());
	        response.put("totalPages",  pageResult.getTotalPages());

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR IN listdbtcrdInvoiceInformationNewPaged :: " + e.getMessage());
	        response.put("message", "Error occurred");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: listdbtcrdInvoiceInformationNewPaged");
	    return response;
	}
	
	
	
	
	
	
	@GetMapping("/invoice/searchdbtcrdsidnewscrap")
	public @ResponseBody Map<String, Object> listSearchdbtcrdsByIdNewscrap(@RequestParam String id) {
		logger.info("EXECUTING METHOD :: listSearchByIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<Debitcreditinvoiceinterface> assignToContract = null;
		Debitcreditinvoiceinterface invoiceMasterInterfaces = null;
		List<dbtcrdpackingitemsscrap_interface> qsPackingInterfaces = null;
		List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> ls = new HashMap<String, String>();
		try {
			invoiceMasterInterfaces = debitCreditinvoicerepo.listdbtcrdSearchByIdscrap(id);
			String load_id = invoiceMasterInterfaces.getLoad_id();
			String factory_id = invoiceMasterInterfaces.getFactory_id();
			String pn_id = debitCreditrepo.getdbtcrdPnIdBasedOnContract_idscrap(load_id);
			qsPackingInterfaces = debitCreditrepo.searchdbtcrdPackingByIdnewscrap(pn_id, factory_id);
			assignToContract = debitCreditinvoicerepo.searchdbtcrdContractNewscrap(load_id, factory_id);
		
			lsv.add(ls);
			response.put("Grand_Total_value", lsv);
			response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
			response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_INVOICE_MASTER");
			response.put("Invoice_Data", assignToContract);
			response.put("QsPacking_Data", qsPackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listSearchByIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listSearchByIdNew");
		return response;
	}
	
	
	  @GetMapping("/invoice/getcontractdcpackingdetailsnew") public @ResponseBody
	  Map<String, Object> listGetContractQspackingNew(@RequestParam String
	  contract_id,
	  @RequestParam String DcPn_no, @RequestParam String factory_id) {
	  logger.info("EXECUTING METHOD :: listGetContractQspackingNew"); Map<String,
	  Object> response = new HashMap<String, Object>(); List<AssignToContract>
	  assignToContract = null; List<TaxMasterInterface> range = null;
	  List<DebitCreditPackNoteinterface> qsPackingInterfaces = null;
	  List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
	  HashMap<String, String> ls = new HashMap<String, String>(); try { 
	  debitCreditrepo.getdcPn_noBasedOnContractanddcpno(contract_id, DcPn_no, factory_id);
	  qsPackingInterfaces = debitCreditrepo.searchdcPackingByIdnew(DcPn_no, factory_id);
	  assignToContract = assignToContractRepository.searchContractNew(contract_id,
	  factory_id); range = assignToContractRepository.searchdbtcrdTaxId(contract_id,
	  factory_id, DcPn_no); String grandTotalValue =
	  assignToContractRepository.getdbtcrdTotalValue(contract_id, factory_id, DcPn_no);
	  ls.put("Grand_Total_tax", grandTotalValue); lsv.add(ls);
	  response.put("message", (assignToContract.size() > 0 &&
	  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "Success" : "failure");
	  response.put("status", (assignToContract.size() > 0 &&
	  qsPackingInterfaces.size() > 0 && range.size() > 0) ? "yes" : "no");
	  response.put("action", "List_Record_In_Dbtcrd_INVOICE_MASTER");
	  response.put("Contractor_Data", assignToContract);
	  response.put("QsPacking_Data", qsPackingInterfaces);
	  response.put("Grand_Total_value", lsv);
	  
	  response.put("tax", range); } catch (Exception e) { e.printStackTrace();
	  logger.error("ERROR IN THE METHOD FOR listgetContractdcQspackingNew ::   -> " +
	  e.getMessage()); }
	  logger.info("EXECUTED METHOD :: listGContractdcQspackingNew"); return
	  response; }
	 
	  
	  @GetMapping("/invoice/getdbtcrdspackingdetailsnewscrap") public @ResponseBody
	  Map<String, Object> listGetotherspackingNewscrap( @RequestParam String load_id, @RequestParam String factory_id) {
	  logger.info("EXECUTING METHOD :: listGetContractQspackingNewscrap"); Map<String,
	  Object> response = new HashMap<String, Object>(); 
	  List<DebitCreditPackNoteinterface> qsPackingInterfaces = null;
	  List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
	  HashMap<String, String> ls = new HashMap<String, String>(); try { String
	  pn_id = debitCreditrepo.getPnIdBasedOnothersLoad_idscrap( load_id, factory_id); 
	  qsPackingInterfaces =	 debitCreditrepo.searchothersPackingByIdnewscrap(pn_id, factory_id);
	  response.put("QsPacking_Data", qsPackingInterfaces);
	  response.put("Grand_Total_value", lsv);
	   } catch (Exception e) { e.printStackTrace();
	  logger.error("ERROR IN THE METHOD FOR listGetContractQspackingNewscrap ::   -> " +
	  e.getMessage()); }
	  logger.info("EXECUTED METHOD :: listGetContractQspackingNewscarp"); return
	  response; 
	  }
	  
		
	  @GetMapping("/invoicedbtcrd/searchidnew")
		public @ResponseBody Map<String, Object> listSearchByIdNew(@RequestParam String id) {
			logger.info("EXECUTING METHOD :: listSearchByIdNew");
			Map<String, Object> response = new HashMap<String, Object>();
			Debitcreditinvoiceinterface invoiceMasterInterfaces = null;
			List<AssignToContract> assignToContract = null;
			List<TaxMasterInterface> range = null;
			List<DebitCreditPackNoteinterface> qsPackingInterfaces = null;
			List<HashMap<String, String>> lsv = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> ls = new HashMap<String, String>();
			try {
				invoiceMasterInterfaces = debitCreditinvoicerepo.listSearchById(id);
				String load_id = invoiceMasterInterfaces.getLoad_id();
				String contract_id = invoiceMasterInterfaces.getCon_id();
				String factory_id = invoiceMasterInterfaces.getFactory_id();
				debitCreditrepo.getdcPn_noBasedOnContractanddcpno(contract_id, load_id, factory_id);
				qsPackingInterfaces = debitCreditrepo.searchdcPackingByIdnew(load_id, factory_id);
				assignToContract = assignToContractRepository.searchContractNew(contract_id, factory_id);
				range = assignToContractRepository.searchdbtcrdTaxId(contract_id, factory_id, load_id);		
				List<String> grandTot = assignToContractRepository.getdbtcrdTotalValueList(contract_id, factory_id, load_id);
				String dataVal = grandTot.get(0);
				String[] grandTotal_Value = dataVal.split(",");
				String grandTotalValue = grandTotal_Value[0];
				String tax_Final_total = grandTotal_Value[1];
				String taxable_final_total = grandTotal_Value[2];
				String grandTotalInWords = assignToContractRepository.getgrandTotalInWordsQuery(grandTotalValue);
				float total_tax_adv = 0.0f;
				float total_taxable = 0.0f;
				float optd = 0.0f;
				float ptc = 0.0f;
				for(TaxMasterInterface tx : range) {
					total_tax_adv = total_tax_adv+Float.parseFloat(tx.getAdv_tax());
					total_taxable = Float.parseFloat(tx.getTaxable());
					optd = Float.parseFloat(tx.getOptc());
					ptc = ptc + Float.parseFloat(tx.getPtc());
				}
				total_tax_adv = total_tax_adv + total_taxable;
				ls.put("Grand_Total_tax", grandTotalValue);
				ls.put("GrandTotalInWords", grandTotalInWords);
				ls.put("Tax_Final_total", String.valueOf(ptc));
				ls.put("PBC_Final_total", String.valueOf(ptc+optd));
				ls.put("Tax_with_basic_final_total", taxable_final_total);
				ls.put("Total_adv_tax_Taxable", String.valueOf(total_tax_adv));
				lsv.add(ls);
				response.put("Grand_Total_value", lsv);
				response.put("message", (invoiceMasterInterfaces != null) ? "Success" : "failure");
				response.put("status", (invoiceMasterInterfaces != null) ? "yes" : "no");
				response.put("action", "List_Record_In_INVOICE_MASTER");
				response.put("Invoice_Data", invoiceMasterInterfaces);
				response.put("Contractor_Data", assignToContract);
				response.put("QsPacking_Data", qsPackingInterfaces);
				response.put("tax", range);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR listdbtcrdSearchByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: listdbtcrdSearchByIdNew");
			return response;
		}
	  

		@PostMapping("/invoicedbtcrd/updatenew")
		public @ResponseBody Map<String, Object> updateInvoiceNew(@RequestParam Integer id,
				@RequestParam String modified_by, @RequestParam String product_desc, @RequestParam String remarks,
				@RequestParam(required = false) String date_of_notification, @RequestParam(required = false) String date_val,
				@RequestParam(required = false) String bg_type, @RequestParam(required = false) String date_of_issue, @RequestParam(required = false) String reference_no,
				@RequestParam(required = false) String lc_number, @RequestParam(required = false) String supply_place, @RequestParam(required = false) String s_t_exempted,
				@RequestParam(required = false) String lr_docketno, @RequestParam(required = false) String bg_no, @RequestParam(required = false) String date_of_expiry,
				@RequestParam(required = false) String date_of_ref, @RequestParam(required = false) String lc_issue_date,
				@RequestParam(required = false) List<String> service_code_id,
				@RequestParam(required = false) List<String> hsn_code_id, @RequestParam(required = false) String inc_type,//invoice_type_id for type_remarks
				@RequestParam(required = false) List<String> slno) {
			logger.info("EXECUTING METHOD :: updateInvoiceNew ");
			Map<String, Object> response = new HashMap<String, Object>();
			int valueCount = 0;
			try {
				logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE");
				valueCount = debitCreditinvoicerepo.updateInvoicedbtcrdInfo(product_desc, remarks, date_of_notification, date_val,
						bg_type, date_of_issue, reference_no, lc_number, supply_place, lr_docketno, bg_no, date_of_expiry,
						date_of_ref, lc_issue_date, modified_by, s_t_exempted, id);
				logger.info("EXECUTING METHOD :: AFTER UPDATING INVOICE");
				response.put("action", "INVOICE_UPDATE");
				response.put("message", (valueCount > 0) ? "Success" : "No fields changed.");
				response.put("status", (valueCount > 0) ? "yes" : "no");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR updateInvoiceNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: updateInvoiceNew ");
			return response;
		}

		@PostMapping("/invoicedbtcrd/verificationnew")
		private @ResponseBody Map<String, Object> invoiceVerficationNew(@RequestParam String verified_by,
				@RequestParam String gst_remarks,
				@RequestParam(required = false) String non_tax_adv, @RequestParam(required = false) String tax_adv,
				@RequestParam(required = false) String qs_packing_item_slno, @RequestParam String total,
				@RequestParam String payable_by_customer, @RequestParam String payable_to_dept,
				@RequestParam String open_tax_adv, @RequestParam String open_non_tax_adv,
				@RequestParam(required = false) String recovery_amt, @RequestParam String factory_id, @RequestParam int id,
				@RequestParam int pn_id, @RequestParam List<String> tax_id, @RequestParam List<String> tax_per,
				@RequestParam List<String> tax_value, @RequestParam List<String> adv_tax,
				@RequestParam List<String> tax_payable_by_customer, @RequestParam List<String> tax_payable_to_dept,
				@RequestParam(required = false) List<String> t_adv,@RequestParam String note_type) {
			logger.info("EXECUTING METHOD :: invoiceVerficationNew");
			Map<String, Object> response = new HashMap<String, Object>();
			int count = 0;
			Optional<Float> total_balance;
			float total_bal = 0;
			float avl_bal = 0;
			String newSeriesNumber = null;
			try {
				long contact_id = debitCreditinvoicerepo.getContractIdFromdbtcrdInvoiceMaster(id, factory_id);
				Boolean  isRelease = debitCreditinvoicerepo.getIsReleaseFromdbtcrdInvoiceMaster(id);
				Optional<Long> optionalSeriesNumber = debitCreditinvoicerepo.getSeriesNumberbasedOnId(id,note_type);
				long serieNumber = optionalSeriesNumber.orElse((long) 0);
				if (serieNumber < 1) {
					response.put("message", "Please Generate Series Number");
					return response;
				}
				String seriesNumberInString = String.valueOf(serieNumber);
				StringBuilder sbc = new StringBuilder();
				sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
				seriesNumberInString = sbc.toString();
				Optional<Long> optionalInvoiceId = debitCreditinvoicerepo.getdbtcrdInvoiceNumber(seriesNumberInString);
				long invoiceNumber = optionalInvoiceId.orElse((long) 0);
				if (optionalInvoiceId.isPresent()) {
					// if invoice_no already generate and it's release and coming for approve once again.
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
					//OPENING BALANCE SUBTRACTION 
					String pn_idvalue = debitCreditinvoicerepo.getLastInseretedOpeningBalancePrimaryId(contact_id);
					total_balance = debitCreditinvoicerepo.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
					total_bal = total_balance.orElse((float) 0);
					if( total_bal < 0 ) {
						response.put("message", "Please Create the Opening Balance");
						return response;
					}
					float taxValue = debitCreditinvoicerepo.getTaxPercentageDetailsAssignedToContractor(contact_id);
					float adjustmentAmount;

					if (Float.parseFloat(open_tax_adv) > 0) {

					    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

					    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

					} else {
						
					    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
					}

					if ("Debit".equalsIgnoreCase(note_type)) {

					    if (total_bal >= adjustmentAmount) {

					        avl_bal = total_bal - adjustmentAmount;

					    } else {

					        response.put("message", "Insufficient Balance Please check!");
					        return response;
					    }

					} else {

					    avl_bal = total_bal + adjustmentAmount;

					}
					logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE");
					debitCreditinvoicerepo.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
					logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION ");
					if (isRelease) {
						count = debitCreditinvoicerepo.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
								payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
								id);
						}
						else
						{
							count = debitCreditinvoicerepo.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
									payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
									newSeriesNumber, id);
						}

					logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION ");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING ");
					assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
					logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING ");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING ");
					debitCreditrepo.updateisLockindbtcrdPacking(pn_id, "1");
					logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING ");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING ");
					debitCreditrepo.updateisLockindbtcrdPackingItem(pn_id, "1");
					logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING ITEM MASTER LOCKING ");
					int sizeRange = tax_id.size();
					logger.info("EXECUTING METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
					for (int i = 0; i < sizeRange; i++) {
						debitCreditinvoicerepo.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i), tax_value.get(i),
								adv_tax.get(i), tax_payable_by_customer.get(i), tax_payable_to_dept.get(i),String.valueOf(id), verified_by);
					}
					logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
				} else {
						String num = String.valueOf(serieNumber);
						if (num != null && !num.equals("0")) {
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
							//OPENING BALANCE SUBTRACTION 
							String pn_idvalue = debitCreditinvoicerepo.getLastInseretedOpeningBalancePrimaryId(contact_id);
							total_balance = debitCreditinvoicerepo.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
							total_bal = total_balance.orElse((float) 0);
							if( total_bal < 0 ) {
								response.put("message", "Please Create the Opening Balance");
								return response;
							}
							float taxValue = debitCreditinvoicerepo.getTaxPercentageDetailsAssignedToContractor(contact_id);
							float adjustmentAmount;

							if (Float.parseFloat(open_tax_adv) > 0) {

							    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

							    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

							} else {

							    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
							}

							if ("Debit".equalsIgnoreCase(note_type)) {

							    if (total_bal >= adjustmentAmount) {

							        avl_bal = total_bal - adjustmentAmount;

							    } else {

							        response.put("message", "Insufficient Balance Please check!");
							        return response;
							    }

							} else {

							    avl_bal = total_bal + adjustmentAmount;

							}
							logger.info("EXECUTING METHOD :: BEFORE UPDATING OPENING BALANCE FIRST TIME");
							debitCreditinvoicerepo.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, verified_by);
							logger.info("EXECUTED METHOD :: AFTER UPDATING OPENING BALANCE FIRST TIME");
							logger.info("EXECUTING METHOD :: BEFORE UPDATING INVOICE VERIFICATION FIRST TIME");
							if (isRelease) {
								count = debitCreditinvoicerepo.updateInvoiceVerificationDetails_is_release(non_tax_adv, tax_adv, total,
										payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
										id);
								}
								else
								{
									count = debitCreditinvoicerepo.updateInvoiceVerificationDetails(non_tax_adv, tax_adv, total,
											payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, gst_remarks, verified_by,
											newSeriesNumber, id);
								}
							logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION FIRST TIME");
							logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING FIRST TIME");
							assignToContractRepository.updateISReleaseInContractMaster(contact_id, "1", factory_id);
							logger.info("EXECUTED METHOD :: AFTER UPDATING CONTRACT MASTER LOCKING FIRST TIME");
							logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING MASTER LOCKING FIRST TIME");
							debitCreditrepo.updateisLockindbtcrdPacking(pn_id, "1");
							logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING FIRST TIME");
							logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING FIRST TIME");
							debitCreditrepo.updateisLockindbtcrdPackingItem(pn_id, "1");
							logger.info("EXECUTING METHOD :: AFTER UPDATING QSPACKING MASTER ITEM LOCKING FIRST TIME");
							int sizeRange = tax_id.size();
							logger.info("EXECUTED METHOD :: BEFORE ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
							for (int i = 0; i < sizeRange; i++) {
								debitCreditinvoicerepo.insertINVOICE_TAXENTRY_DETAILSNew(tax_id.get(i), tax_per.get(i),
										tax_value.get(i), adv_tax.get(i), tax_payable_by_customer.get(i),
										tax_payable_to_dept.get(i), String.valueOf(id), verified_by);
							}
							logger.info("EXECUTED METHOD :: AFTER ADDING INVOICE_TAXENTRY_DETAILS FIRST TIME");
						} else {
							response.put("message", "Please Generate Series Number");
							return response;
						}
				}
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
				response.put("Invoice_NO", newSeriesNumber);

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: invoiceVerficationNew");
			return response;
		}
		@PostMapping("/invoicedbtcrd/dbtcrdverificationnewscrap")
		private @ResponseBody Map<String, Object> dbtcrdverificationnewscrap(@RequestParam String verified_by,
				@RequestParam String gst_remarks, @RequestParam(required = false) String non_tax_adv,
				@RequestParam(required = false) String tax_adv,
				@RequestParam(required = false) String qs_packing_item_slno,
				@RequestParam(required = false) String recovery_amt, @RequestParam String factory_id,
				@RequestParam int id, @RequestParam int pn_id, @RequestParam(required = false) List<String> t_adv,
				@RequestParam String note_type, @RequestParam Double grand_total,
				@RequestParam(required = false) String tax1, @RequestParam(required = false) Double tax1_per,
				@RequestParam(required = false) Double tax1_value, @RequestParam(required = false) String tax2,
				@RequestParam(required = false) Double tax2_per, @RequestParam(required = false) Double tax2_value,
				@RequestParam(required = false) String tax3, @RequestParam(required = false) Double tax3_per,
				@RequestParam(required = false) Double tax3_value,@RequestParam(required = false) Double total_tax,@RequestParam(required = false) Double net_amount) {
			logger.info("EXECUTING METHOD :: invoiceVerficationNew");
			logger.info("EXECUTING METHOD :: invoiceVerficationNew | grand_total={} | tax1={} | tax1_per={} | tax1_value={} | tax2={} | tax2_per={} | tax2_value={} | tax3={} | tax3_per={} | tax3_value={} | total_tax={} | net_amount={}",
			        grand_total, tax1, tax1_per, tax1_value, tax2, tax2_per, tax2_value, tax3, tax3_per, tax3_value,total_tax,net_amount);
			Map<String, Object> response = new HashMap<String, Object>();
			int count = 0;
			String newSeriesNumber = null;
			try {
				Boolean  isRelease = debitCreditinvoicerepo.getIsReleaseFromdbtcrdInvoiceMaster(id);
				Optional<Long> optionalSeriesNumber = debitCreditinvoicerepo.getSeriesNumberbasedOnIdscrap(id,note_type);
				long serieNumber = optionalSeriesNumber.orElse((long) 0);
				if (serieNumber < 1) {
					response.put("message", "Please Generate Series Number");
					return response;
				}
				String seriesNumberInString = String.valueOf(serieNumber);
				StringBuilder sbc = new StringBuilder();
				sbc.append(seriesNumberInString.substring(0, Math.min(5, seriesNumberInString.length())));
				seriesNumberInString = sbc.toString();
				Optional<Long> optionalInvoiceId = debitCreditinvoicerepo.getdbtcrdInvoiceNumber(seriesNumberInString);
				long invoiceNumber = optionalInvoiceId.orElse((long) 0);
				if (optionalInvoiceId.isPresent()) {
					// if invoice_no already generate and it's release and coming for approve once again.
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
				
					if (isRelease) {
						count = debitCreditinvoicerepo.updatedbtcrdInvoiceVerificationDetailsscrap_is_release(gst_remarks, verified_by,grand_total,total_tax,net_amount,tax1_value,tax2_value,tax3_value,
								id);
					}
						else
						{
							count = debitCreditinvoicerepo.updatedbtcrdInvoiceVerificationDetailsscrap(gst_remarks, verified_by,
									newSeriesNumber,grand_total,total_tax,net_amount,tax1_value,tax2_value,tax3_value, id);
						}
						
					logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION ");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING CONTRACT MASTER LOCKING ");
					debitCreditrepo.updateisLockindbtcrdQSPackingscrap(pn_id, "1");
					logger.info("EXECUTED METHOD :: AFTER UPDATING QSPACKING MASTER LOCKING ");
					logger.info("EXECUTING METHOD :: BEFORE UPDATING QSPACKING ITEM MASTER LOCKING ");
					
				} else {
						String num = String.valueOf(serieNumber);
						if (num != null && !num.equals("0")) {
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
							if (isRelease) {
								count = debitCreditinvoicerepo.updatedbtcrdInvoiceVerificationDetailsscrap_is_release(gst_remarks, verified_by,grand_total,total_tax,net_amount,tax1_value,tax2_value,tax3_value,
										id);
							}
								else
								{
									count = debitCreditinvoicerepo.updatedbtcrdInvoiceVerificationDetailsscrap(gst_remarks, verified_by,
											newSeriesNumber,grand_total,total_tax,net_amount,tax1_value,tax2_value,tax3_value, id);
								}
							logger.info("EXECUTED METHOD :: AFTER UPDATING INVOICE VERIFICATION FIRST TIME");
							logger.info("EXECUTING METHOD :: BEFORE UPDATING QS MASTER LOCKING FIRST TIME");
							debitCreditrepo.updateisLockindbtcrdQSPackingscrap(pn_id, "1");
							
						} else {
							response.put("message", "Please Generate Series Number");
							return response;
						}
				}
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
				response.put("Invoice_NO", newSeriesNumber);

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: invoiceVerficationNew");
			return response;
		}
		
		@PostMapping("/invoicedbtcrd/releasebyidnew")
		public @ResponseBody Map<String, Object> releaseByIdNew(@RequestParam String id, @RequestParam String factory_id,
				@RequestParam String released_by, @RequestParam int pn_id,@RequestParam String note_type ) {
			logger.info("EXECUTING METHOD :: releaseByIdNew");
			Map<String, Object> response = new HashMap<String, Object>();
			Optional<Float> total_balance;
			Float total_bal = 0.0f;
			float avl_bal = 0.0f;
			try {
				int count = debitCreditinvoicerepo.UpdateReleaseById(id, released_by);
				long contact_id = debitCreditinvoicerepo.getContractIdFromdbtcrdInvoiceMaster(Integer.parseInt(id), factory_id);
				//OPENING BALANCE SUBTRACTION 
				String pn_idvalue = debitCreditinvoicerepo.getLastInseretedOpeningBalancePrimaryId(contact_id);
				total_balance = debitCreditinvoicerepo.getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);
				total_bal = total_balance.orElse((float) 0);
				String open_tax_adv = debitCreditinvoicerepo.getRecoveryAmountfromInvoice(id);
				String open_non_tax_adv = debitCreditinvoicerepo.getOpenNonTaxAdvFromInvoice(id);
				float taxValue = debitCreditinvoicerepo.getTaxPercentageDetailsAssignedToContractor(contact_id);
				float adjustmentAmount;

				if (Float.parseFloat(open_tax_adv) > 0) {

				    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

				    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

				} else {


				    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
				}

				if ("Debit".equalsIgnoreCase(note_type)) {

				    if (total_bal >= adjustmentAmount) {

				        avl_bal = total_bal + adjustmentAmount;

				    } else {

//				        response.put("message", "Insufficient Balance Please check!");
//				        return response;
				    }

				} else {

				    avl_bal = total_bal - adjustmentAmount;

				}
				debitCreditinvoicerepo.reduceOpeningBalanceBasedOnOpenTaxable(pn_idvalue, avl_bal, released_by);
				debitCreditrepo.updateisLockindbtcrdPacking(pn_id, "0");
				debitCreditrepo.updateisLockindbtcrdPackingItem(pn_id, "0");
				assignToContractRepository.updateISReleaseInContractMaster(contact_id, "0", factory_id);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "RELEASED_Record_In_INVOICE_MASTER");
				response.put("action", avl_bal);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: releaseByIdNew");
			return response;
		}
		
		@PostMapping("/invoicedbtcrd/releasebyidnewscrap")
		public @ResponseBody Map<String, Object> releasebyidnewscrap(@RequestParam String id, @RequestParam String factory_id,
				@RequestParam String released_by, @RequestParam int pn_id) {
			logger.info("EXECUTING METHOD :: releaseByIdNew");
			Map<String, Object> response = new HashMap<String, Object>();
			try {
				int count = debitCreditinvoicerepo.dbtcrdUpdateReleaseByIdscrap(id, released_by);
				debitCreditrepo.dbtcrdupdateisLockinQSPackingscrap(pn_id, "0");
			
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "RELEASED_Record_In_INVOICE_MASTER");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR releaseByIdNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: releaseByIdNew");
			return response;
		}

	
		/*
		 * @PostMapping("/debitcreditinvoice/cancelbyid") public @ResponseBody
		 * Map<String, Object> cancelByIdNewScrap(
		 * 
		 * @RequestParam String id,
		 * 
		 * @RequestParam String cancelby,
		 * 
		 * @RequestParam String other_pn_id ) {
		 * 
		 * Map<String, Object> response = new HashMap<>();
		 * 
		 * try {
		 * 
		 * int count = debitCreditinvoicerepo .dbtcrdCancelById(id, cancelby);
		 * 
		 * response.put("message", count > 0 ? "Success" : "Failure");
		 * response.put("status", count > 0 ? "yes" : "no"); response.put("action",
		 * "CANCEL_DEBIT_CREDIT_INVOICE");
		 * 
		 * } catch (Exception e) { e.printStackTrace(); response.put("message",
		 * "Exception occurred"); response.put("status", "no"); }
		 * 
		 * return response; }
		 */
		@PostMapping("/invoicedbtcrd/cancelbyidnew")
		public @ResponseBody Map<String, Object> cancelByIdNew(
		        @RequestParam String id,
		        @RequestParam String factory_id,
		        @RequestParam String cancelled_by,
		        @RequestParam String pn_id,
		        @RequestParam String note_type
		       ) {

		    logger.info("EXECUTING METHOD :: cancelByIdNew");

		    Map<String, Object> response = new HashMap<>();
		    Optional<Float> total_balance;
		    Float total_bal = 0.0f;
		    float avl_bal = 0.0f;

		    try {
		    	debitCreditrepo.updatedbtcrdQSPackingCancelByPnId(pn_id);
		        /* 1️⃣ Mark invoice as CANCELLED */
		        int count = debitCreditinvoicerepo.dbtcrdCancelById(id, cancelled_by);

		        if (count > 0) {

		            long contract_id =
		                    debitCreditinvoicerepo.getContractIdFromdbtcrdInvoiceMaster(
		                            Integer.parseInt(id), factory_id
		                    );

		            /* 2️⃣ Reverse opening balance */
		            String pn_idvalue =
		                    debitCreditinvoicerepo.getLastInseretedOpeningBalancePrimaryId(contract_id);

		            total_balance =
		                    debitCreditinvoicerepo
		                            .getTotalOpeningBalanceFromAdvancePackingNoteItemForContractor(pn_idvalue);

		            total_bal = total_balance.orElse(0.0f);

		            String open_tax_adv =
		                    debitCreditinvoicerepo.getRecoveryAmountfromInvoice(id);
		            
		            String open_non_tax_adv = debitCreditinvoicerepo.getOpenNonTaxAdvFromInvoice(id);

		            float taxValue =
		                    debitCreditinvoicerepo.getTaxPercentageDetailsAssignedToContractor(contract_id);

//		            float tax_amount = open_tax_adv * (taxValue / 100);
//
//		            /* 🔁 Reverse calculation */
//		            avl_bal = total_bal - (open_tax_adv + tax_amount);
		            float adjustmentAmount;

					if (Float.parseFloat(open_tax_adv) > 0) {

					    float value = Float.parseFloat(open_tax_adv) * (taxValue / 100);

					    adjustmentAmount = value + Float.parseFloat(open_tax_adv);

					} else {

					    adjustmentAmount = Float.parseFloat(open_non_tax_adv);
					}

					if ("Debit".equalsIgnoreCase(note_type)) {

					    if (total_bal >= adjustmentAmount) {

					        avl_bal = total_bal + adjustmentAmount;

					    } else {

//					        response.put("message", "Insufficient Balance Please check!");
//					        return response;
					    }

					} else {

					    avl_bal = total_bal - adjustmentAmount;

					}
		            debitCreditinvoicerepo.reduceOpeningBalanceBasedOnOpenTaxable(
		                    pn_idvalue, avl_bal, cancelled_by
		            );

		            /* 3️⃣ Update contract release flag */
		            assignToContractRepository
		                    .updateISReleaseInContractMaster(contract_id, "1", factory_id);
		        }

		        response.put("message", count > 0 ? "Success" : "failure");
		        response.put("status", count > 0 ? "yes" : "no");
		        response.put("action", "CANCEL_Record_In_DBTCRD_INVOICE_MASTER");

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN cancelByIdNew :: " + e.getMessage());
		    }

		    logger.info("EXECUTED METHOD :: cancelByIdNew");
		    return response;
		}


		@PostMapping("/invoicedbtcrd/cancelbyidnewscrap")
		public @ResponseBody Map<String, Object> cancelByIdNewScrap(
		        @RequestParam String id,
		        @RequestParam String factory_id,
		        @RequestParam String cancelled_by,
		        @RequestParam String pn_id
		        ) {

		    logger.info("EXECUTING METHOD :: cancelByIdNewScrap");

		    Map<String, Object> response = new HashMap<>();

		    try {
		    	debitCreditrepo.updatedbtcrdQSPackingCancelByPnId(pn_id);
		        int count =
		                debitCreditinvoicerepo.dbtcrdCancelById(id, cancelled_by);

		        response.put("message", count > 0 ? "Success" : "failure");
		        response.put("status", count > 0 ? "yes" : "no");
		        response.put("action", "CANCEL_Record_In_DBTCRD_INVOICE_MASTER_SCRAP");

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("ERROR IN cancelByIdNewScrap :: " + e.getMessage());
		    }

		    logger.info("EXECUTED METHOD :: cancelByIdNewScrap");
		    return response;
		}
		
		@PostMapping("/dbtcrditemmaster/deletenew")
		public @ResponseBody Map<String, Object> deletedbtcrdItemRecordNew(@RequestParam String modified_by, @RequestParam String slno,@RequestParam String pn_id) {
			Map<String, Object> response = new HashMap<String, Object>();
			logger.info("EXECUTING METHOD :: deleteQsPackingItemRecordNew");
			int checkLocked = 0;
			try {
				checkLocked = debitCreditrepo.checkdbtcrdIsLocked(pn_id);
				logger.info("EXECUTED METHOD :: AFTER othersPACKING IS LOCKED");
				if (checkLocked > 0) {
					response.put("message", "Invoice Already Generated Not Able to Update");
					return response;
				}
				int count = debitCreditrepo.deltedbtcrdItemRecord(slno, modified_by);
				response.put("message", (count > 0) ? "Success" : "failure");
				response.put("status", (count > 0) ? "yes" : "no");
				response.put("action", "DELETE_Record_In_QSPACKINGMASTER");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR IN THE METHOD FOR deleteQsPackingItemRecordNew ::   -> " + e.getMessage());
			}
			logger.info("EXECUTED METHOD :: deleteQsPackingItemRecordNew");
			return response;
		}

}
