package com.JIMS.integration.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.QSDCPackingNote;
import com.JIMS.integration.interfaces.QSPackingInterfaces;
import com.JIMS.integration.interfaces.QSPackingItemsInterfaces;
import com.JIMS.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
import com.JIMS.integration.repository.DCQSPackingRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin
@Controller
@RequestMapping("/jssl")
public class QSDebitCreditPackingController {
	Logger logger = LogManager.getLogger(QSDebitCreditPackingController.class);
	@Autowired
	DCQSPackingRepository dcQSPackingRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	@SuppressWarnings({ "unused", "null" })
	@PostMapping(value = "/qspackingmasterdc/add")
	public @ResponseBody Map<String, Object> createQSPackingMaster(@RequestParam("file") MultipartFile file,
			@RequestParam("val") String valueDataJson, @RequestParam("obj") String objDataJson) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> val = null;
		List<Map<String, String>> obj = null;

		try {
			// Convert the 'val' and 'obj' JSON strings into Java objects
			ObjectMapper objectMapper = new ObjectMapper();
			String uniqueFileName = null;
			val = objectMapper.readValue(valueDataJson, new TypeReference<Map<String, String>>() {
			});
			obj = objectMapper.readValue(objDataJson, new TypeReference<List<Map<String, String>>>() {
			});
			int valCount = 0;
			int count = 0;
			String uploadedFilePath = null;
			long maxSize = 10 * 1024 * 1024;
			// Extract the 'obj' part, which is a list of maps
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().equals("application/pdf")) {
					response.put("message", "Only PDF files are allowed.");
					response.put("status", "no");
					return response;
				}
				// Check the file size manually
				if (file.getSize() > maxSize) {
					response.put("message", "File size exceeds the maximum limit of 10MB.");
					response.put("status", "no");
					return response;
				}
				// Generate a unique file name
				uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs(); // Create the directory if it doesn't exist
				}

				// Save the file
				File serverFile = new File(directory, uniqueFileName);
				if (serverFile.exists()) {
					response.put("message", "File already exists! Please provide another File.");
					return response;
				}
				file.transferTo(serverFile);

				// Set the uploaded file path
				uploadedFilePath = serverFile.getAbsolutePath();
			}
			if ((val != null && !val.isEmpty()) && (obj != null && !obj.isEmpty())) {
				String invoice_type = val.get("invoice_type");
				String con_id = val.get("con_id");
				String milestone_id = val.get("milestone_id");
				String dc_type = val.get("dc_type");
				String freight = val.get("freight");
				String tax_exempted = val.get("tax_exempted");
				String filepath = uniqueFileName;
				String created_by = val.get("created_by");
				int factory_id = val.containsKey("factory_id") ? Integer.parseInt(val.get("factory_id")) : 0;
				LocalDateTime time = LocalDateTime.now();
				QSDCPackingNote qsDC = new QSDCPackingNote();
				qsDC.setInvoice_type(invoice_type);
				qsDC.setContract_id(Integer.parseInt(con_id));
				qsDC.setMilestone_id(Integer.parseInt(milestone_id));
				qsDC.setDc_type(dc_type);
				qsDC.setFreight(freight);
				qsDC.setFilepath(filepath);
				qsDC.setFactory_id(factory_id);
				qsDC.setCreated_by(created_by);
				qsDC.setTax_exempted(tax_exempted);
				qsDC.setCreated_date(time);
				QSDCPackingNote qsDCPacking = dcQSPackingRepository.save(qsDC);
				int id = qsDCPacking.getId();
				if(id > 0)
					count = 1;
				valCount = 0;
				for (Map<String, String> item : obj) {
					String UOM_id = item.get("unit_id");
					String qty = item.get("qty");
					String per_kgs = item.get("per_kgs");
					String unit_price = item.get("unit_price");
					String total = item.get("total");
					valCount = dcQSPackingRepository.insertDCQSPackingItemRecord(qty, per_kgs, unit_price, total,UOM_id, created_by, id);
				}
			}
			response.put("message", (count > 0 && valCount > 0) ? "Success" : "failure");
			response.put("status", (count > 0 && valCount > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_QSDCPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@SuppressWarnings({ "unused", "null" })
	@PostMapping("/qspackingmasterdc/update") // before delete move to history table and delete
	public @ResponseBody Map<String, Object> updateDCQSPackingMaster(@RequestParam("file") MultipartFile file,
			   @RequestParam("val") String valueDataJson, @RequestParam("obj") String objDataJson) {
	    Map<String, Object> response = new HashMap<String, Object>();
	    Map<String, String> val = null;
	    List<Map<String, String>> obj = null;
		   List<Map<String, String>> obj1 = null;
	    int checkLocked = 0;
	    
	    try {
	        // Convert the 'val' and 'obj' JSON strings into Java objects
	        ObjectMapper objectMapper = new ObjectMapper();
	        String uniqueFileName = null;
	        val = objectMapper.readValue(valueDataJson, new TypeReference<Map<String, String>>() {});
	  	  obj = objectMapper.readValue(objDataJson, new TypeReference<List<Map<String, String>>>() {}); 
			int valCount = 0;
			int count = 0;
			int updateCount = 0;
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().equals("application/pdf")) {
					response.put("message", "Only PDF files are allowed.");
					response.put("status", "no");
					return response;
				}
				  // Generate a unique file name
             uniqueFileName = UUID.randomUUID() + "_@ALTER@_" + file.getOriginalFilename();
             File directory = new File(uploadDir);
             if (!directory.exists()) {
                 directory.mkdirs(); // Create the directory if it doesn't exist
             }

             // Save the file
             File serverFile = new File(directory, uniqueFileName);
             if (serverFile.exists()) {
             	response.put("message", "File already exists! Please provide another File.");
                 return response;
             }
             file.transferTo(serverFile);
			}
			if ((val != null && !val.isEmpty()) && (obj != null && !obj.isEmpty())) {
				String invoice_type = val.get("invoice_type");
				String id = val.get("id");
				String con_id = val.get("con_id");
				String milestone_id = val.get("milestone_id");
				String dc_type = val.get("dc_type");
				String freight = val.get("freight");
				String tax_exempted = val.get("tax_exempted");
				String filepath = uniqueFileName;
				String modified_by = val.get("modified_by");
				int factory_id = val.containsKey("factory_id") ? Integer.parseInt(val.get("factory_id")) : 0;
				LocalDateTime time = LocalDateTime.now();
				QSDCPackingNote qsDCPacking = null;
				dcQSPackingRepository.InsertHistoryTableBeforeUpdate(id,modified_by);
				Optional<QSDCPackingNote> qsDCObj =dcQSPackingRepository.findById(Integer.parseInt(id));
				if(qsDCObj.isPresent()) {
					 QSDCPackingNote qsDC = qsDCObj.get();
					qsDC.setInvoice_type(invoice_type);
					qsDC.setContract_id(Integer.parseInt(con_id));
					qsDC.setMilestone_id(Integer.parseInt(milestone_id));
					qsDC.setDc_type(dc_type);
					qsDC.setFreight(freight);
					qsDC.setFilepath(filepath);
					qsDC.setFactory_id(factory_id);
					qsDC.setModified_by(modified_by);
					qsDC.setModified_date(time);
					qsDC.setTax_exempted(tax_exempted);
					qsDC.setId(Integer.parseInt(id));
					qsDCPacking = dcQSPackingRepository.save(qsDC);		
				}
			 //>>>>>>> before save take into history
				if(qsDCPacking != null)
					count = 1;
//				checkLocked  = qsPackingRepository.checkIsLocked(pn_id);
//				if(checkLocked > 0) {
//					response.put("message", "Invoice Already Generated Not Able to Update");
//					return response;
//				}
//				qsPackingRepository.insterQSPackingMasterHistoryRecord(modified_by, pn_id);
//				count = qsPackingRepository.updateQSPackingMasterRecord(con_id, load_id, filepath, transport_name,
//							vechile_no, freight, milestone_id, taxexemstatus, taxexemamount, modified_by, lot_no, pn_id);
				for (Map<String, String> item : obj) {
					String UOM_id = item.get("unit_id");
					String qty = item.get("qty");
					String per_kgs = item.get("per_kgs");
					String unit_price = item.get("unit_price");
					String total = item.get("total");
					String isChanged = item.get("isChanged");
					String slno = item.get("slno");
					if(slno.equalsIgnoreCase("no")) { // if slno is no assume that new row going to insert
						valCount =	dcQSPackingRepository.insertDCQSPackingItemRecord(qty, per_kgs, unit_price, total,UOM_id, modified_by, Integer.parseInt(id));
					}
					if(isChanged.equalsIgnoreCase("yes")) { // only that packing note changed then only move to history and update
					//	qsPackingRepository.updateQSPackingItemMasterHistoryRecordSlno(modified_by,slno);
						updateCount =	dcQSPackingRepository.updateDCQSPackingItemRecord(qty, per_kgs, unit_price, total, UOM_id, modified_by, Integer.parseInt(id), slno);
					}					
				}
			}
			response.put("message",  (count > 0 && (valCount > 0 || updateCount >0)) ? "Success" : "failure");
			response.put("status",  (count > 0 && (valCount > 0 || updateCount >0)) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_DCQSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	

	@PostMapping("/qspackingmasterdc/delete")
	public @ResponseBody Map<String, Object> deleteDCQsPackingRecord(@RequestBody Map<String, String> val) {

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String id = val.get("id");
		//	qsPackingRepository.updateQSPackingMasterHistoryRecord(modified_by, pn_id);
			int count = dcQSPackingRepository.delteDCQsPackingNoteMasterRecord(id, modified_by);
		//	qsPackingRepository.updateQSPackingItemMasterHistoryRecord(modified_by, pn_id);
			int count1 = dcQSPackingRepository.delteDCQsPackingItemMasterRecord(id, modified_by);
			response.put("message", (count > 0 && count1 > 0) ? "Success" : "failure");
			response.put("status", (count > 0 && count1 > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_DCQSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/qspackingitemmasterdc/delete")
	public @ResponseBody Map<String, Object> deleteQsPackingItemRecord(@RequestBody Map<String, String> val) {

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String slno = val.get("slno");
			int count = dcQSPackingRepository.delteDCQsPackingItemRecord(slno, modified_by);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_DCQSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingmasterdc/list/{factory_id}")
	public @ResponseBody Map<String, Object> listQSPackinglist(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPackingInterfaces> qsPackingInterfaces = null;
		List<QSPackingItemsInterfaces> qsPackingItemsInterfaces = null;
		try {
			qsPackingInterfaces = dcQSPackingRepository.listDCQSPAckingMasterRecord(factory_id);
			qsPackingItemsInterfaces = dcQSPackingRepository.listDCQSPAckingItemMasterRecord(factory_id);
			response.put("QSPACKING", qsPackingInterfaces);
			response.put("QSPACKING_ITEM", qsPackingItemsInterfaces);
			response.put("message",
					(qsPackingInterfaces != null && qsPackingItemsInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPackingInterfaces != null && qsPackingItemsInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingmastersearchdc/{pn_id}")
	public @ResponseBody Map<String, Object> serachQSPackingId(@PathVariable String pn_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		try {
			//qsPackingInterfaces = qsPackingRepository.searchQSPackingById(pn_id);
			response.put("message", (qsPackingInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPackingInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSPACKINGMASTER");
			response.put("DATA", qsPackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingitemmastersearchdc/{slno}")
	public @ResponseBody Map<String, Object> serachQSPackingItemId(@PathVariable String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		QSPackingItemsInterfaces qsPackingItemsInterfaces = null;
		try {
			qsPackingItemsInterfaces = dcQSPackingRepository.searchDCQSPackingItemById(slno);
			response.put("message", (qsPackingItemsInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPackingItemsInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_DCQSPACKINGITEMMASTER");
			response.put("DATA", qsPackingItemsInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

//	@GetMapping("/qspackingmasteruom/list")
//	public @ResponseBody Map<String, Object> listUOMList() {
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<String> value = null;
//		try {
//			value = qsPackingRepository.listUOM();
//			response.put("message", (value != null) ? "Success" : "failure");
//			response.put("status", (value != null) ? "yes" : "no");
//			response.put("action", "List_Record_In_QSPACKINGMASTER");
//			response.put("UOM LIST", value);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@GetMapping("/qspackingmasterscraptype/list/{factory_id}")
//	public @ResponseBody Map<String, Object> listScrapType(@PathVariable String factory_id) {
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<String> value = null;
//		try {
//			value = qsPackingRepository.listScrapType(factory_id);
//			response.put("message", (value != null) ? "Success" : "failure");
//			response.put("status", (value != null) ? "yes" : "no");
//			response.put("action", "List_Record_In_QSPACKINGMASTER");
//			response.put("UOM LIST", value);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//	
	 @GetMapping("/qspackingmasterdc/viewpdf/{id}")
	    public ResponseEntity<Resource> viewFile(@PathVariable int id) {
	        try {
	        	Optional<Integer> value = dcQSPackingRepository.getDCPn_id(String.valueOf(id));
	        	 if (value.isEmpty()) {
	                 return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                         .body(null);
	             }
	             int valuePnid = value.get();
	             String filepath = dcQSPackingRepository.getDCFilePath(valuePnid);
	             String folderPath = getUploadDir().toString();
	             String fileName = filepath; // This is the file name from the database

System.out.println("FILEPATH = "+filepath);
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
	             return ResponseEntity.ok()
	                     .contentType(MediaType.parseMediaType(contentType))
	                     .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\"")
	                     .body(resource);

	         } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(null);
	        }
	    }
	 
	 @GetMapping("/qspackingmasterdc/viewpdfnew")
		public ResponseEntity<Resource> viewFileNew(@RequestParam int id) {
			logger.info("EXECUTING METHOD :: viewFileNew");
			try {
				Optional<Integer> value = dcQSPackingRepository.getDCPn_id(String.valueOf(id));
				if (value.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
				int valuePnid = value.get();
				String filepath = dcQSPackingRepository.getDCFilePath(valuePnid);
				String folderPath = getUploadDir().toString();
				String fileName = filepath;
				Path path = Paths.get(folderPath, fileName).normalize();
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
				String contentType = "application/pdf"; 
				logger.info("EXECUTED METHOD :: viewFileNew");
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
						.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\"")
						.body(resource);

			} catch (Exception e) {
				logger.error("ERROR IN THE METHOD FOR viewFileNew ::   -> " + e.getMessage());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		}
	
}
