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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.QSChallanPacking;
import com.JIMS.integration.interfaces.ListAssignMilesonetoContractors;
import com.JIMS.integration.interfaces.QSPACKINGSCRAPTYPELIST_INCTYPE;
import com.JIMS.integration.interfaces.QSPacking_QSChallanInterfaces;
import com.JIMS.integration.interfaces.QSPacking_QSChallanItemsInterfaces;
import com.JIMS.integration.interfaces.QSPacking_QSChallan_LIST_INTERFACES;
import com.JIMS.integration.repository.QSChallanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin
@RestController
@RequestMapping("/jssl") 
public class QSChallanController { 
	
	
	Logger logger = LogManager.getLogger(QSChallanController.class);
	@Autowired
	QSChallanRepository qsChallanRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}
		
	
	@SuppressWarnings("null")
	@PostMapping(value = "/QSCHALLAN_PACKINGNOTE_MASTER/addnew")
	public @ResponseBody Map<String, Object> createQSChallanPackingMasterNew(
	        @RequestParam("filepath") MultipartFile filepath,
	        @RequestParam String contract_id,
	        @RequestParam int invoice_type_id,
	        @RequestParam String transport_name,
	        @RequestParam String vechile_no,
	        @RequestParam String milestone_id,
	        @RequestParam String created_by,
	        @RequestParam int factory_id,
	        @RequestParam List<String> qty,
	        @RequestParam List<String> per_kgs,
	        @RequestParam List<String> unit_price,
	        @RequestParam List<String> total,
	        @RequestParam List<String> UOM_id,
	        @RequestParam List<String> type_id,
	        @RequestParam(required = false) List<String> pices,
	        @RequestParam String grand_total) {

	    logger.info("EXECUTING METHOD :: createQSChallanPackingMasterNew");
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        String uniqueFileName = null;
	        int valCount = 0;
	        int count = 0;
	        long maxSize = 10 * 1024 * 1024;
	        int size = qty.size();
	        
	        if (size != per_kgs.size() || size != unit_price.size() || size != total.size() || size != UOM_id.size() || size != type_id.size()) {
	            response.put("message", "All input lists must have the same size!");
	            return response;
	        }

	        // File upload validation
			/*
			 * if (filepath != null && !filepath.isEmpty()) { if
			 * (!filepath.getContentType().equals("application/pdf")) {
			 * response.put("message", "Only PDF files are allowed.");
			 * response.put("status", "no"); return response; } if (filepath.getSize() >
			 * maxSize) { response.put("message",
			 * "File size exceeds the maximum limit of 10MB."); response.put("status",
			 * "no"); return response; }
			 * 
			 * uniqueFileName = UUID.randomUUID() + "_@$@_" +
			 * filepath.getOriginalFilename(); File directory = new File(uploadDir); if
			 * (!directory.exists()) { directory.mkdirs(); // Create the directory if it
			 * doesn't exist } File serverFile = new File(directory, uniqueFileName); if
			 * (serverFile.exists()) { response.put("message",
			 * "File already exists! Please provide another File."); return response; }
			 * filepath.transferTo(serverFile); }
			 */
	        
	     // File upload validation
	        if (filepath != null && !filepath.isEmpty()) {

	            if (!filepath.getContentType().equals("application/pdf")) {
	                response.put("message", "Only PDF files are allowed.");
	                response.put("status", "no");
	                return response;
	            }

	            if (filepath.getSize() > maxSize) {
	                response.put("message", "File size exceeds the maximum limit of 10MB.");
	                response.put("status", "no");
	                return response;
	            }

	            String originalFileName = filepath.getOriginalFilename();

	            // 🔴 Check if same file already uploaded for this contract
	            int fileExists = qsChallanRepository.checkFileExistsForContract(contract_id, originalFileName);

	            if (fileExists > 0) {
	                response.put("message", "This file is already uploaded for the selected contract, please choose different file.");
	                response.put("status", "no");
	                return response;
	            }

	            uniqueFileName = UUID.randomUUID() + "_@$@_" + originalFileName;

	            File directory = new File(uploadDir);
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            File serverFile = new File(directory, uniqueFileName);
	            filepath.transferTo(serverFile);
	        }

	        // 1. Generate next load_id like DLY-1, DLY-2, ...
	        String lastLoadId = qsChallanRepository.getLastGlobalLoadId(); // Add this method in repository to get last "DLY-" ID
	        int nextLoadNumber = 1;
	        if (lastLoadId != null && lastLoadId.startsWith("DLY-")) {
	            try {
	                nextLoadNumber = Integer.parseInt(lastLoadId.substring(4)) + 1;
	            } catch (NumberFormatException e) {
	                nextLoadNumber = 1;
	            }
	        }
	        String newLoadId = "DLY-" + nextLoadNumber;

	        // 2. Save master record with the generated DLY- ID
	        LocalDateTime time = LocalDateTime.now();
	        QSChallanPacking qschallanpacking = new QSChallanPacking();
	        qschallanpacking.setConId(contract_id);
	        qschallanpacking.setitypeID(invoice_type_id);
	        qschallanpacking.setFilepath(uniqueFileName);
	        qschallanpacking.settransportname(transport_name);
	        qschallanpacking.setvechileno(vechile_no);
	        qschallanpacking.setMilestone_id(Integer.parseInt(milestone_id));
	        qschallanpacking.setCreatedBy(created_by);
	        qschallanpacking.setCreated_date(time);
	        qschallanpacking.setFactory_id(factory_id);
	        qschallanpacking.setGrand_total(grand_total);
	        qschallanpacking.setloadid(newLoadId); // Set the new load ID
	        qschallanpacking.setcancel(0);

	        logger.info("EXECUTING METHOD :: BEFORE CREATE QSCHALLANPACKING");
	        QSChallanPacking objQSchallanPacking = qsChallanRepository.save(qschallanpacking);
	        logger.info("EXECUTED METHOD :: AFTER CREATE QSCHALLANPACKING");
	        
	        count = objQSchallanPacking.getPnId();
	        
	        // 3. Save item records
	        valCount = 0;
	        logger.info("EXECUTING METHOD :: BEFORE CREATE QSCHALLANPACKING ITEM");
	        for (int i = 0; i < size; i++) {
	            String pn_id = String.valueOf(count);
	            valCount = qsChallanRepository.insertQSChallanPackingItemRecord(
	                    qty.get(i), per_kgs.get(i), unit_price.get(i),
	                    total.get(i), UOM_id.get(i), type_id.get(i),
	                    "0", created_by, pn_id, factory_id);
	        }
	        logger.info("EXECUTED METHOD :: AFTER CREATE QSCHALLANPACKING ITEM");

	        response.put("message", (count > 0 && valCount > 0) ? "Success" : "failure");
	        response.put("status", (count > 0 && valCount > 0) ? "yes" : "no");
	        response.put("action", "Insert_Record_In_QSCHALLANPACKINGMASTER");
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR IN THE METHOD FOR createQSChallanPackingMasterNew ::   -> " + e.getMessage());
	    }
	    logger.info("EXECUTED METHOD :: createQSChallanPackingMasterNew");
	    return response;
	}
	
	
	@SuppressWarnings("null")
	@PostMapping("/qschallanpackingmaster/updatenew")
	public @ResponseBody Map<String, Object> updateQSChallanPackingMasterNew(
	        @RequestParam(required = false) MultipartFile filepath,
	        @RequestParam(required = false) String existing_filepath,
	        @RequestParam String transport_name,
	        @RequestParam String vechile_no,
	        @RequestParam String grand_total,
	        @RequestParam String modified_by,
	        @RequestParam String pn_id,
	        @RequestParam int factory_id,
	        @RequestParam(required = false) List<String> qty,
	        @RequestParam(required = false) List<String> per_kgs,
	        @RequestParam(required = false) List<String> unit_price,
	        @RequestParam(required = false) List<String> total,
	        @RequestParam(required = false) List<String> UOM_id,
	        @RequestParam(required = false) List<String> type_id,
	        @RequestParam(required = false) List<String> slno,
	        @RequestParam(required = false) String data
	) {
	    Map<String, Object> response = new HashMap<>();
	    String finalFilePath = existing_filepath; // default
	    String pendingUploadFileName = null;

	    try {
	        /* ---------- 1. Optional JSON payload ---------- */
	        if (data != null && !data.isEmpty()) {
	            new ObjectMapper().readValue(data, Map.class);
	        }

	        /* ---------- 2. Null-safe lists ---------- */
	        if (qty == null) qty = new ArrayList<>();
	        if (per_kgs == null) per_kgs = new ArrayList<>();
	        if (unit_price == null) unit_price = new ArrayList<>();
	        if (total == null) total = new ArrayList<>();
	        if (UOM_id == null) UOM_id = new ArrayList<>();
	        if (type_id == null) type_id = new ArrayList<>();
	        if (slno == null) slno = new ArrayList<>();

	        int size = qty.size();

	        // Validate lists all same size
	        if (size != per_kgs.size() || size != unit_price.size()
	                || size != total.size() || size != UOM_id.size()
	                || size != type_id.size()) {

	            response.put("message", "All input lists must have the same size.");
	            response.put("status", "no");
	            return response;
	        }

	        /* ---------- 3. Validate file ONLY ---------- */
	        if (filepath != null && !filepath.isEmpty()) {
	            if (!"application/pdf".equalsIgnoreCase(filepath.getContentType())) {
	                response.put("message", "Only PDF files are allowed.");
	                response.put("status", "no");
	                return response;
	            }
	            pendingUploadFileName = UUID.randomUUID() + "_@$@_" + filepath.getOriginalFilename();
	            finalFilePath = pendingUploadFileName;
	        }

	        /* ---------- 4. Check if record is locked ---------- */
	        int checkLocked = qsChallanRepository.checkIsLocked(pn_id);
	        if (checkLocked > 0) {
	            response.put("message", "Invoice Already Generated. Cannot Update.");
	            response.put("status", "no");
	            return response;
	        }

	        /* ---------- 5. Update MASTER record ---------- */
	        int masterCount = qsChallanRepository.updateQSChallanPackingMasterRecord(
	                finalFilePath, transport_name, vechile_no, grand_total, modified_by, pn_id
	        );

	        /* ---------- 6. Insert / update ITEM rows ---------- */
	        int insertCount = 0;
	        int updateCount = 0;

	        for (int i = 0; i < size; i++) {

	            // If slno exists and not empty, treat as existing, else insert new
	            boolean isExisting = (i < slno.size() && slno.get(i) != null && !slno.get(i).isEmpty() && !slno.get(i).equals("0"));

	            if (isExisting) {
	                updateCount += qsChallanRepository.updateQSChallanPackingItemsRecord(
	                        qty.get(i), per_kgs.get(i), unit_price.get(i), total.get(i),
	                        UOM_id.get(i), type_id.get(i), "0",
	                        modified_by, pn_id, slno.get(i)
	                );
	            } else {
	                insertCount += qsChallanRepository.insertQSChallanPackingItemRecord(
	                        qty.get(i), per_kgs.get(i), unit_price.get(i), total.get(i),
	                        UOM_id.get(i), type_id.get(i), "0",
	                        modified_by, pn_id, factory_id
	                );
	            }
	        }

	        /* ---------- 7. Upload file ONLY if DB updated ---------- */
	        boolean updated = masterCount > 0 || insertCount > 0 || updateCount > 0;
	        if (updated && pendingUploadFileName != null) {
	            File directory = new File(uploadDir);
	            if (!directory.exists()) directory.mkdirs();
	            File serverFile = new File(directory, pendingUploadFileName);
	            filepath.transferTo(serverFile);
	        }

	        /* ---------- 8. Response ---------- */
	        if (updated) {
	            response.put("message", "Updated successfully.");
	            response.put("status", "yes");
	            response.put("action", "UPDATE");
	        } else {
	            response.put("message", "No fields were changed.");
	            response.put("status", "no");
	            response.put("action", "NO_CHANGE");
	        }

	    } catch (Exception e) {
	        logger.error("ERROR IN updateQSChallanPackingMasterNew", e);
	        response.put("message", "Failed to update record.");
	        response.put("status", "no");
	        response.put("action", "ERROR");
	    }

	    return response;
	}
	
	@PostMapping("/qschallanpackingmaster/deletenew")
	public @ResponseBody Map<String, Object> deleteQsChallanPackingRecordNew(@RequestParam String modified_by, @RequestParam String pn_id) {
		Map<String, Object> response = new HashMap<String, Object>(); 
		logger.info("EXECUTING METHOD :: deleteQSChallanPackingMasterRecordNew"); 
		try {
			qsChallanRepository.updateIsDeleteQSChallanPackingMasterHistoryRecord(modified_by, pn_id);
			int count = qsChallanRepository.delteQSChallanPackingMasterRecord(pn_id, modified_by);
			qsChallanRepository.updateQSChallanPackingItemMasterHistoryRecord(modified_by, pn_id);
			int count1 = qsChallanRepository.delteQSChallanPackingItemMasterRecord(pn_id, modified_by);
			response.put("message", (count > 0 && count1 > 0) ? "Success" : "failure");
			response.put("status", (count > 0 && count1 > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_QSCHALLANPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteQsChallanPackingRecordNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteQSChallanPackingRecordNew");
		return response;
	}
	
	@PostMapping("/qschallanpackingitemmaster/deletenew")
	public @ResponseBody Map<String, Object> deleteQSChallanPackingItemRecordNew(@RequestParam String modified_by, @RequestParam String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteQSChallanPackingItemRecordNew");
		try {
			int count = qsChallanRepository.delteQSChallanPackingItemMasterRecord(slno, modified_by);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_QSCHALLANPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteQSChallanPackingItemRecordNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteQSChallanPackingItemRecordNew");
		return response;
	}
	
	@GetMapping("/qschallanpackingmaster/listnew")
	public @ResponseBody Map<String, Object> listQSChallanPackinglistNew(@RequestParam String factory_id) {
		logger.info("EXECUTING METHOD :: listQSChallanPackinglistNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPacking_QSChallanInterfaces> qsPacking_QSChallanInterfaces = null;
		try {
			qsPacking_QSChallanInterfaces= qsChallanRepository.listQSChallanPackingMasterRecord(factory_id); 
			response.put("QSPACKING", qsPacking_QSChallanInterfaces);
			response.put("message",(qsPacking_QSChallanInterfaces.size() > 0 ) ? "Success" : "failure");
			response.put("status", (qsPacking_QSChallanInterfaces.size() > 0 ) ? "yes" : "no");
			response.put("action", "List_Record_In_QSCHALLANPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listQSChallanPackinglistNew ::   -> " + e.getMessage());
		} 
		logger.info("EXECUTED METHOD :: listQSChallanPackinglistNew");
		return response;
	}
	
	
	
	@GetMapping("/qschallanpackingmaster/listnewpaged")
	public @ResponseBody Map<String, Object> listQSChallanPackinglistNewPaged(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "") String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Map<String, Object> response = new HashMap<>();
	    logger.info("EXECUTING METHOD :: listQSChallanPackinglistNewPaged");

	    try {
	        PageRequest pageable = PageRequest.of(page, size);

	        Page<QSPacking_QSChallanInterfaces> pageResult =
	                qsChallanRepository.listQSChallanPackingMasterRecordPaged(factory_id, search, pageable);

	        logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

	        response.put("QSPACKING",    pageResult.getContent());
	        response.put("message",      pageResult.hasContent() ? "Success" : "failure");
	        response.put("status",       pageResult.hasContent() ? "yes" : "no");
	        response.put("action",       "List_Record_In_QSCHALLANPACKINGMASTER");
	        response.put("totalItems",   pageResult.getTotalElements());
	        response.put("currentPage",  pageResult.getNumber());
	        response.put("totalPages",   pageResult.getTotalPages());

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR IN listQSChallanPackinglistNewPaged :: " + e.getMessage());
	        response.put("message", "Error occurred");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: listQSChallanPackinglistNewPaged");
	    return response;
	}
	 
	@GetMapping("/qschallanpackingmastersearchnew")
	public @ResponseBody Map<String, Object> serachQSChallanPackingIdNew(@RequestParam String pn_id) {
		logger.info("EXECUTING METHOD :: qschallanpackingmastersearchnew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPacking_QSChallan_LIST_INTERFACES> qsPacking_QSChallanInterfaces = null;
		try {
			qsPacking_QSChallanInterfaces = qsChallanRepository.searchQSChallanPackingById(pn_id);
			response.put("message", (qsPacking_QSChallanInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPacking_QSChallanInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSCHALLANPACKINGMASTER");
			response.put("DATA", qsPacking_QSChallanInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR qschallanpackingmastersearchnew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: qschallanpackingmastersearchnew");
		return response;
	}
	
	
	@GetMapping("/qschallanpackingitemmastersearchnew")
	public @ResponseBody Map<String, Object> serachQSChallanPackingItemIdNew(@RequestParam String slno) {
		logger.info("EXECUTING METHOD :: serachQSChallanPackingItemIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		QSPacking_QSChallanItemsInterfaces qsPacking_QSChallanItemsInterfaces = null;
		try {
			qsPacking_QSChallanItemsInterfaces = qsChallanRepository.searchQSChallanPackingItemById(slno);
			response.put("message", (qsPacking_QSChallanItemsInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPacking_QSChallanItemsInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSCHALLANPACKINGITEMMASTER");
			response.put("DATA", qsPacking_QSChallanItemsInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachQSChallanPackingItemIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachQSChallanPackingItemIdNew");
		return response;
	}
	
	
	@GetMapping("/qschallanpackingmasteruom/listnew")
	public @ResponseBody Map<String, Object> listUOMListNew() {
		logger.info("EXECUTING METHOD :: serachQSChallanPackingItemIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = qsChallanRepository.listUOM();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSCHALLANPACKINGMASTER");
			response.put("UOM LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachQSChallanPackingItemIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachQSChallanPackingItemIdNew");
		return response;
	}
	
	
	@GetMapping("/qschallanpackingmasterscraptype/listnew")
	public @ResponseBody Map<String, Object> listScrapTypeNew() {
		logger.info("EXECUTING METHOD :: listScrapTypeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPACKINGSCRAPTYPELIST_INCTYPE> value = null;
		try {
			value = qsChallanRepository.listScrapType();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSCHALLANPACKINGMASTER");
			response.put("SCRAPTYPELIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listScrapTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listScrapTypeNew");
		return response;
	}
	
	
	@GetMapping("/qschallanpackingmaster/viewpdfnew")
	public ResponseEntity<Resource> viewFileNew(@RequestParam int id) {
		logger.info("EXECUTING METHOD :: viewFileNew");
		try {
			Optional<Integer> value = qsChallanRepository.getPn_id(String.valueOf(id));
			if (value.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			int valuePnid = value.get();
			String filepath = qsChallanRepository.getFilePath(valuePnid);
			String folderPath = getUploadDir().toString();
			String fileName = filepath;
			
			Path path = Paths.get(folderPath,fileName).normalize();
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
	
	
	@GetMapping("/listassignedmilestonetocontractors")
	public @ResponseBody Map<String, Object> getlistMilestoneAssignedToContractorMaster(@RequestParam int factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: getlistMilestoneAssignedToContractorMaster");
		try {
			List<ListAssignMilesonetoContractors> count = qsChallanRepository.getlistMilestoneAssignedToContractor(factory_id);
			response.put("action", "List Contractor assigned Milestone");
			response.put("message", (count.size()> 0) ? "Success" : "failed");
			response.put("status", (count.size()> 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR getlistMilestoneAssignedToContractorMaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getlistMilestoneAssignedToContractorMaster");
		return response;
	}
	
	
	@GetMapping("/qschallanpackingmasteruom/list")
	public @ResponseBody Map<String, Object> listUOMList() {
		logger.info("EXECUTING METHOD :: listUOMList");
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = qsChallanRepository.listUOM();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSCHALLANPACKINGMASTER");
			response.put("UOM LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listUOMList ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listUOMList");
		return response; 
	}
	
}
