package com.JIMS.integration.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.ConsolidatedInvoiceItem;
import com.JIMS.integration.entity.ConsolidatedQSPacking;
import com.JIMS.integration.entity.QSPacking;
import com.JIMS.integration.interfaces.AssignToContract;
import com.JIMS.integration.interfaces.ConsolidateInvoice;
import com.JIMS.integration.interfaces.InvoiceMasterInterface;
import com.JIMS.integration.interfaces.ListAssignMilesonetoContractors;
import com.JIMS.integration.interfaces.QSPACKINGSCRAPTYPELIST_INCTYPE;
import com.JIMS.integration.interfaces.QSPackingInterfaces;
import com.JIMS.integration.interfaces.QSPackingItemsInterfaces;
import com.JIMS.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
import com.JIMS.integration.interfaces.consolidatedQSPacking_Interfaces;
import com.JIMS.integration.interfaces.othersPackingItem_LIST_INTERFACES;
import com.JIMS.integration.repository.QSPackingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.JIMS.integration.repository.ConsolidatedInvoiceItemRepository;
import com.JIMS.integration.repository.ConsolidatedQSPackingRepository;

@CrossOrigin
@Controller
@RequestMapping("/jssl")
public class QSPackingController {

	Logger logger = LogManager.getLogger(QSPackingController.class);
	@Autowired
	QSPackingRepository qsPackingRepository;
	@Autowired
	ConsolidatedQSPackingRepository consolqsPackingRepository;
	@Value("${file.upload-dir}")
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	@SuppressWarnings("null")
	@PostMapping(value = "/qspackingmaster/addnew")
	public @ResponseBody Map<String, Object> createQSPackingMasterNew(@RequestParam("file") MultipartFile file,
			@RequestParam String con_id, @RequestParam String load_id, @RequestParam String lot_no,
			@RequestParam String transport_name, @RequestParam String vechile_no, @RequestParam String freight,
			@RequestParam String milestone_id, @RequestParam String created_by, @RequestParam int factory_id,
			@RequestParam List<String> qty, @RequestParam List<String> per_kgs, @RequestParam List<String> unit_price,
			@RequestParam List<String> total, @RequestParam List<String> UOM_id, @RequestParam List<String> type_id,
			@RequestParam(required = false) List<String> pices, @RequestParam String grand_total) {
		logger.info("EXECUTING METHOD :: createQSPackingMasterNew");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String uniqueFileName = null;
			int valCount = 0;
			int count = 0;
			long maxSize = 10 * 1024 * 1024;
			int size = qty.size();
			if (size != per_kgs.size() || size != unit_price.size() || size != total.size() || size != UOM_id.size()
					|| size != type_id.size()) {
				response.put("message", "All input lists must have the same size!");
				return response;
			}
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().equals("application/pdf")) {
					response.put("message", "Only PDF files are allowed.");
					response.put("status", "no");
					return response;
				}
				if (file.getSize() > maxSize) {
					response.put("message", "File size exceeds the maximum limit of 10MB.");
					response.put("status", "no");
					return response;
				}
				//uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();
				String originalFileName = file.getOriginalFilename();

				// ✅ Check if same file already uploaded for this contract
				int fileExists = qsPackingRepository.checkFileExistsForContract(con_id, originalFileName);

				if (fileExists > 0) {
				    response.put("message", "This file is already uploaded for the selected contract, please choose a different file.");
				    response.put("status", "no");
				    return response;
				}

				uniqueFileName = UUID.randomUUID() + "_@$@_" + originalFileName;
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs(); // Create the directory if it doesn't exist
				}
				File serverFile = new File(directory, uniqueFileName);
				if (serverFile.exists()) {
					response.put("message", "File already exists! Please provide another File.");
					return response;
				}
				file.transferTo(serverFile);
			}
			LocalDateTime time = LocalDateTime.now();
			QSPacking qspacking = new QSPacking();
			qspacking.setConId(con_id);
			qspacking.setLoadId(load_id);
			qspacking.setFilepath(uniqueFileName);
			qspacking.setTransport_name(transport_name);
			qspacking.setVechile_no(vechile_no);
			qspacking.setFreight(freight);
			qspacking.setMilestone_id(Integer.parseInt(milestone_id));
			qspacking.setCreatedBy(created_by);
			qspacking.setCreated_date(time);
			qspacking.setLotNo(lot_no);
			qspacking.setFactory_id(factory_id);
			qspacking.setGrand_total(grand_total);
			qspacking.setCancel(0);
			logger.info("EXECUTING METHOD :: BEFORE CREATE QSPACKING");
			QSPacking objQSPacking = qsPackingRepository.save(qspacking);
			logger.info("EXECUTED METHOD :: AFTER CREATE QSPACKING");
			count = objQSPacking.getPnId();
			valCount = 0;
			logger.info("EXECUTING METHOD :: BEFORE CREATE QSPACKING ITEM");
			for (int i = 0; i < size; i++) {
				String pn_id = String.valueOf(count);
				valCount = qsPackingRepository.insertQSPackingItemRecord(qty.get(i), per_kgs.get(i), unit_price.get(i),
						total.get(i), UOM_id.get(i), type_id.get(i), "0", created_by, pn_id, factory_id);
			}
			logger.info("EXECUTED METHOD :: AFTER CREATE QSPACKING ITEM");
			response.put("message", (count > 0 && valCount > 0) ? "Success" : "failure");
			response.put("status", (count > 0 && valCount > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR createQSPackingMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createQSPackingMasterNew");
		return response;
	}

	@SuppressWarnings("null")
	@PostMapping("/qspackingmaster/updatenew")
	public @ResponseBody Map<String, Object> updateQSPackingMasterNew(
			@RequestParam(required = false) MultipartFile file,
			@RequestParam(required = false) String existing_filepath,
			@RequestParam(required = false) String transport_name, @RequestParam(required = false) String vechile_no,
			@RequestParam(required = false) int freight, @RequestParam String grand_total,
			@RequestParam String modified_by, @RequestParam String pn_id, @RequestParam int factory_id,
			@RequestParam List<String> qty, @RequestParam List<String> per_kgs, @RequestParam List<String> unit_price,
			@RequestParam List<String> total, @RequestParam List<String> slno, @RequestParam List<String> UOM_id,
			@RequestParam List<String> type_id, @RequestParam(required = false) List<String> pices) {
		logger.info("EXECUTING METHOD :: updateQSPackingMasterNew");
		Map<String, Object> response = new HashMap<String, Object>();
		int checkLocked = 0;
		try {
			// String uniqueFileName = null;
			String finalFilePath = existing_filepath; // default = old file
			int valCount = 0;
			int count = 0;
			int updateCount = 0;
			int size = qty.size();
			if (size != per_kgs.size() || size != unit_price.size() || size != total.size() || size != UOM_id.size()
					|| size != type_id.size()) {
				response.put("message", "All input lists must have the same size!");
				return response;
			}
			if (file != null && !file.isEmpty()) {

			    if (!file.getContentType().equals("application/pdf")) {
			        response.put("message", "Only PDF files are allowed.");
			        response.put("status", "no");
			        return response;
			    }

			    String originalFileName = file.getOriginalFilename();

			    // ✅ Check if same file already exists
			    if (existing_filepath != null && existing_filepath.contains(originalFileName)) {
			        response.put("message", "This file is already uploaded for the selected contract,please choose different file.");
			        response.put("status", "no");
			        return response;
			    }

			    String uniqueFileName = UUID.randomUUID() + "_@$@_" + originalFileName;

			    File directory = new File(uploadDir);
			    if (!directory.exists()) {
			        directory.mkdirs();
			    }

			    File serverFile = new File(directory, uniqueFileName);

			    file.transferTo(serverFile);

			    // overwrite only when new file uploaded
			    finalFilePath = uniqueFileName;
			}
			logger.info("EXECUTING METHOD :: BEFORE QSPACKING IS LOCKED");
			checkLocked = qsPackingRepository.checkIsLocked(pn_id);
			logger.info("EXECUTED METHOD :: AFTER QSPACKING IS LOCKED");
			if (checkLocked > 0) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			logger.info("EXECUTING METHOD :: BEFORE UPDATE QSPACKING ");
			count = qsPackingRepository.updateQSPackingMasterRecord(finalFilePath, transport_name, vechile_no, freight,
					grand_total, modified_by, pn_id);
			logger.info("EXECUTED METHOD :: AFTER UPDATE QSPACKING ");
			logger.info("EXECUTING METHOD :: BEFORE UPDATE/INSERT QSPACKING ITEM");
			for (int i = 0; i < size; i++) {
				int slnoValue = Integer.parseInt(slno.get(i));
				if (slnoValue != 0) {
					updateCount = qsPackingRepository.updateQsPackingItemsRecord(qty.get(i), per_kgs.get(i),
							unit_price.get(i), total.get(i), UOM_id.get(i), type_id.get(i), "0", modified_by, pn_id,
							slno.get(i));
				} else {
					valCount = qsPackingRepository.insertQSPackingItemRecord(qty.get(i), per_kgs.get(i),
							unit_price.get(i), total.get(i), UOM_id.get(i), type_id.get(i), "0", modified_by, pn_id,
							factory_id);
				}
			}
			logger.info("EXECUTED METHOD :: AFTER UPDATE/INSERT QSPACKING ITEM");
			response.put("message", (count > 0 || (valCount > 0 || updateCount > 0)) ? "Success" : "failure");
			response.put("status", (count > 0 || (valCount > 0 || updateCount > 0)) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {

			String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";

			if (msg.contains("no fields were changed")) {

				logger.info("UPDATE skipped: No fields were changed for pn_id = " + pn_id);

				response.put("message", "No fields were changed.");
				response.put("status", "no");
				response.put("action", "NO_CHANGE");

				return response; // ✅ VERY IMPORTANT
			}

			logger.error("ERROR IN updateQSPackingMasterNew", e);

			response.put("message", "Failed to update record.");
			response.put("status", "no");

			return response;
		}
		logger.info("EXECUTED METHOD :: updateQSPackingMasterNew");
		return response;
	}

	@PostMapping("/qspackingmaster/deletenew")
	public @ResponseBody Map<String, Object> deleteQsPackingRecordNew(@RequestParam String modified_by,
			@RequestParam String pn_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteQsPackingRecordNew");
		try {
			qsPackingRepository.updateIsDeleteQSPackingMasterHistoryRecord(modified_by, pn_id);
			int count = qsPackingRepository.delteQsPackingMasterRecord(pn_id, modified_by);
			qsPackingRepository.updateQSPackingItemMasterHistoryRecord(modified_by, pn_id);
			int count1 = qsPackingRepository.delteQsPackingItemMasterRecord(pn_id, modified_by);
			response.put("message", (count > 0 && count1 > 0) ? "Success" : "failure");
			response.put("status", (count > 0 && count1 > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteQsPackingRecordNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteQsPackingRecordNew");
		return response;
	}

	@PostMapping("/qspackingitemmaster/deletenew")
	public @ResponseBody Map<String, Object> deleteQsPackingItemRecordNew(@RequestParam String modified_by,
			@RequestParam String slno, @RequestParam String pn_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteQsPackingItemRecordNew");
		int checkLocked = 0;
		try {
			checkLocked = qsPackingRepository.checkIsLocked(pn_id);
			logger.info("EXECUTED METHOD :: AFTER othersPACKING IS LOCKED");
			if (checkLocked > 0) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			int count = qsPackingRepository.delteQsPackingItemRecord(slno, modified_by);
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

	@GetMapping("/qspackingmaster/listnew")
	public @ResponseBody Map<String, Object> listQSPackinglistNew(@RequestParam String factory_id) {
		logger.info("EXECUTING METHOD :: listQSPackinglistNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPackingInterfaces> qsPackingInterfaces = null;
		try {
			qsPackingInterfaces = qsPackingRepository.listQSPAckingMasterRecord(factory_id);
			response.put("QSPACKING", qsPackingInterfaces);
			response.put("message", (qsPackingInterfaces.size() > 0) ? "Success" : "failure");
			response.put("status", (qsPackingInterfaces.size() > 0) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listQSPackinglistNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listQSPackinglistNew");
		return response;
	}

	@GetMapping("/qspackingmaster/listnewpaged")
	public @ResponseBody Map<String, Object> listQSPackinglistNewPaged(@RequestParam String factory_id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search) {

		Map<String, Object> response = new HashMap<>();
		logger.info("EXECUTING METHOD :: listQSPackinglistNewPaged");

		try {
			Pageable pageable = PageRequest.of(page, size);

			Page<QSPackingInterfaces> pageResult = qsPackingRepository.listQSPAckingMasterRecordPaged(factory_id,
					search, pageable);

			logger.info("TOTAL RECORDS :: " + pageResult.getTotalElements());

			response.put("QSPACKING", pageResult.getContent());
			response.put("message", pageResult.hasContent() ? "Success" : "failure");
			response.put("status", pageResult.hasContent() ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
			response.put("totalItems", pageResult.getTotalElements());
			response.put("currentPage", pageResult.getNumber());
			response.put("totalPages", pageResult.getTotalPages());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN listQSPackinglistNewPaged :: " + e.getMessage());
			response.put("message", "Error occurred");
			response.put("status", "error");
		}

		logger.info("EXECUTED METHOD :: listQSPackinglistNewPaged");
		return response;
	}

	@GetMapping("/qspackingmastersearchnew")
	public @ResponseBody Map<String, Object> serachQSPackingIdNew(@RequestParam String pn_id) {
		logger.info("EXECUTING METHOD :: qspackingmastersearchnew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		try {
			qsPackingInterfaces = qsPackingRepository.searchQSPackingById(pn_id);
			response.put("message", (qsPackingInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPackingInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSPACKINGMASTER");
			response.put("DATA", qsPackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR qspackingmastersearchnew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: qspackingmastersearchnew");
		return response;
	}

	@GetMapping("/qspackingitemmastersearchnew")
	public @ResponseBody Map<String, Object> serachQSPackingItemIdNew(@RequestParam String slno) {
		logger.info("EXECUTING METHOD :: serachQSPackingItemIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		QSPackingItemsInterfaces qsPackingItemsInterfaces = null;
		try {
			qsPackingItemsInterfaces = qsPackingRepository.searchQSPackingItemById(slno);
			response.put("message", (qsPackingItemsInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPackingItemsInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSPACKINGITEMMASTER");
			response.put("DATA", qsPackingItemsInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachQSPackingItemIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachQSPackingItemIdNew");
		return response;
	}

	@GetMapping("/qspackingmasteruom/listnew")
	public @ResponseBody Map<String, Object> listUOMListNew() {
		logger.info("EXECUTING METHOD :: serachQSPackingItemIdNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = qsPackingRepository.listUOM();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
			response.put("UOM LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR serachQSPackingItemIdNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: serachQSPackingItemIdNew");
		return response;
	}

	@GetMapping("/qspackingmasterscraptype/listnew")
	public @ResponseBody Map<String, Object> listScrapTypeNew() {
		logger.info("EXECUTING METHOD :: listScrapTypeNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPACKINGSCRAPTYPELIST_INCTYPE> value = null;
		try {
			value = qsPackingRepository.listScrapType();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
			response.put("SCRAPTYPELIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listScrapTypeNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listScrapTypeNew");
		return response;
	}

	@GetMapping("/qspackingmaster/viewpdfnew")
	public ResponseEntity<Resource> viewFileNew(@RequestParam int id) {
		logger.info("EXECUTING METHOD :: viewFileNew");
		try {
			Optional<Integer> value = qsPackingRepository.getPn_id(String.valueOf(id));
			if (value.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			int valuePnid = value.get();
			String filepath = qsPackingRepository.getFilePath(valuePnid);
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

	@GetMapping("/getlistassignedmilestonetocontractors")
	public @ResponseBody Map<String, Object> getlistMilestoneAssignedToContractorMaster(
			@RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: getlistMilestoneAssignedToContractorMaster");
		try {
			List<ListAssignMilesonetoContractors> count = qsPackingRepository
					.getlistMilestoneAssignedToContractor(factory_id);
			response.put("action", "List Contractor assigned Milestone");
			response.put("message", (count.size() > 0) ? "Success" : "failed");
			response.put("status", (count.size() > 0) ? "yes" : "no");
			response.put("DATA", count);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"ERROR IN THE METHOD FOR getlistMilestoneAssignedToContractorMaster ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: getlistMilestoneAssignedToContractorMaster");
		return response;
	}

	@GetMapping("/qspackingmasteruom/list")
	public @ResponseBody Map<String, Object> listUOMList() {
		logger.info("EXECUTING METHOD :: listUOMList");
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = qsPackingRepository.listUOM();
			response.put("message", (value != null) ? "Success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
			response.put("UOM LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listUOMList ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listUOMList");
		return response;
	}

	@GetMapping("/otherspackingmastersearchnew")
	public @ResponseBody Map<String, Object> serachothersPackingIdNew(@RequestParam String pn_id) {
		logger.info("EXECUTING METHOD :: otherspackingmastersearchnew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<othersPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		try {
			qsPackingInterfaces = qsPackingRepository.searchothersPackingById(pn_id);
			response.put("message", (qsPackingInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPackingInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_othersPACKINGMASTER");
			response.put("DATA", qsPackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR otherspackingmastersearchnew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: otherspackingmastersearchnew");
		return response;
	}

	@PostMapping("/otherspackingitemmaster/deletenew")
	public @ResponseBody Map<String, Object> deleteothersPackingItemRecordNew(@RequestParam String modified_by,
			@RequestParam String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: deleteothersPackingItemRecordNew");
		try {
			int count = qsPackingRepository.delteothersPackingItemRecord(slno, modified_by);
			response.put("message", (count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_othersPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deleteothersPackingItemRecordNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: deleteothersPackingItemRecordNew");
		return response;
	}

	@SuppressWarnings("unused")
	@PostMapping("/otherspackingmaster/updatenew")
	public @ResponseBody Map<String, Object> updateothersPackingMasterNew(
			@RequestParam(required = false) MultipartFile file, @RequestParam String transport_name,
			@RequestParam String vechile_no, @RequestParam String buyer_ref, @RequestParam Date date,
			@RequestParam String freight, @RequestParam String grand_total, @RequestParam String modified_by,
			@RequestParam String pn_id, @RequestParam int factory_id, @RequestParam List<String> qty,
			@RequestParam List<String> per_kgs, @RequestParam String load_id, @RequestParam List<String> unit_price,
			@RequestParam List<String> total, @RequestParam List<String> slno, @RequestParam List<String> UOM_id,
			@RequestParam List<String> type_id, @RequestParam(required = false) List<String> pices,
			@RequestParam(required = false) List<String> remakrstype) {
		logger.info("EXECUTING METHOD :: updateQSPackingMasterNew");
		Map<String, Object> response = new HashMap<String, Object>();
		int checkLocked = 0;
		try {
			String uniqueFileName = null;
			int count = 0;
			int updateCount = 0;
			int size = qty.size();
			if (size != per_kgs.size() || size != unit_price.size() || size != total.size() || size != UOM_id.size()
					|| size != type_id.size()) {
				response.put("message", "All input lists must have the same size!");
				return response;
			}
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().equals("application/pdf")) {
					response.put("message", "Only PDF files are allowed.");
					response.put("status", "no");
					return response;
				}
				uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				File serverFile = new File(directory, uniqueFileName);
				if (serverFile.exists()) {
					response.put("message", "File already exists! Please provide another File.");
					return response;
				}
				file.transferTo(serverFile);
			}
			logger.info("EXECUTING METHOD :: BEFORE othersPACKING IS LOCKED");
			checkLocked = qsPackingRepository.checkothersIsLocked(pn_id);
			logger.info("EXECUTED METHOD :: AFTER othersPACKING IS LOCKED");
			if (checkLocked > 0) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			logger.info("EXECUTING METHOD :: BEFORE UPDATE othersPACKING ");
			count = qsPackingRepository.updateothersPackingMasterRecord(uniqueFileName, transport_name, buyer_ref, date,
					vechile_no, freight, grand_total, modified_by, pn_id);
			logger.info("EXECUTED METHOD :: AFTER UPDATE othersPACKING ");
			logger.info("EXECUTING METHOD :: BEFORE UPDATE/INSERT othersPACKING ITEM");
			for (int i = 0; i < size; i++) {
				int slnoValue = Integer.parseInt(slno.get(i));
				if (slnoValue != 0) {
					updateCount = qsPackingRepository.updateothersPackingItemsRecord(qty.get(i), per_kgs.get(i),
							load_id, unit_price.get(i), total.get(i), UOM_id.get(i), type_id.get(i), "1", modified_by,
							pn_id, slno.get(i));
				} else {
					int valCount = qsPackingRepository.insertothersPackingItemRecord(qty.get(i), per_kgs.get(i),
							load_id, unit_price.get(i), total.get(i), UOM_id.get(i), type_id.get(i), "1", modified_by,
							pn_id);
				}
			}
			logger.info("EXECUTED METHOD :: AFTER UPDATE/INSERT othersPACKING ITEM");
			response.put("message", (count > 0 && updateCount > 0) ? "Success" : "Other Packing not updated.");
			response.put("status", (updateCount > 0) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR updateothersPackingMasterNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: updateothersPackingMasterNew");
		return response;
	}

	@GetMapping("/otherspackingmaster/viewpdfnew")
	public ResponseEntity<Resource> viewFileNewothers(@RequestParam int id) {
		logger.info("EXECUTING METHOD :: viewFileNew");
		try {
			Optional<Integer> value = qsPackingRepository.getothersPn_id(String.valueOf(id));
			if (value.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			int valuePnid = value.get();
			String filepath = qsPackingRepository.getothersFilePath(valuePnid);
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

	@GetMapping("/searchconsolidatedinvoice")
	public @ResponseBody Map<String, Object> searchconsolidatedinvoice(@RequestParam String con_id,@RequestParam String factory_id) {

		Map<String, Object> response = new HashMap<>();

		try {

			List<InvoiceMasterInterface> invoiceList = qsPackingRepository.searchconsolidatedinvoice(con_id,factory_id);

			response.put("message", "Success");
			response.put("status", "yes");
			response.put("DATA", invoiceList);

		} catch (Exception e) {
			response.put("message", "Failure");
			response.put("status", "no");
			e.printStackTrace();
		}

		return response;
	}

	
	@GetMapping("/searchconsolidatedinvoiceup")
	public @ResponseBody Map<String, Object> searchconsolidatedinvoiceup(@RequestParam String con_id,@RequestParam String factory_id) {

		Map<String, Object> response = new HashMap<>();

		try {

			List<InvoiceMasterInterface> invoiceList = qsPackingRepository.searchconsolidatedinvoice(con_id,factory_id);

			response.put("message", "Success");
			response.put("status", "yes");
			response.put("DATA", invoiceList);

		} catch (Exception e) {
			response.put("message", "Failure");
			response.put("status", "no");
			e.printStackTrace();
		}

		return response;
	}
	
	
	@Autowired
	private ConsolidatedInvoiceItemRepository consolidatedInvoiceItemRepository;
	private List<consolidatedQSPacking_Interfaces> selectedInvoices;

	/*
	 * @SuppressWarnings("null")
	 * 
	 * @PostMapping(value = "/consolpackingmaster/addnew") public @ResponseBody
	 * Map<String, Object> createconsolPackingMasterNew(
	 * 
	 * @RequestParam String con_id,
	 * 
	 * @RequestParam String transport_name, @RequestParam String vechile_no,
	 * 
	 * @RequestParam String milestone_id,@RequestParam String
	 * created_by, @RequestParam int factory_id,
	 * 
	 * @RequestParam String qty, @RequestParam String per_kgs, @RequestParam String
	 * unit_price,
	 * 
	 * @RequestParam String total, @RequestParam String UOM, @RequestParam String
	 * type_id,
	 * 
	 * @RequestParam String grand_total) {
	 * logger.info("EXECUTING METHOD :: createQSPackingMasterNew"); Map<String,
	 * Object> response = new HashMap<String, Object>(); try { int valCount = 0; int
	 * count = 0;
	 * 
	 * String lastconsolload = consolqsPackingRepository.findLastconsolLoad();
	 * 
	 * String lastconsolload = consolqsPackingRepository.findLastconsolLoad();
	 * 
	 * int nextNumber = 1;
	 * 
	 * if (lastconsolload != null && !lastconsolload.trim().isEmpty()) {
	 * 
	 * // Extract only numbers (works even if format changes slightly) String
	 * numericOnly = lastconsolload.replaceAll("\\D+", "");
	 * 
	 * if (!numericOnly.isEmpty()) { nextNumber = Integer.parseInt(numericOnly) + 1;
	 * } }
	 * 
	 * String newLoadId = String.format("CIN-%05d", nextNumber);
	 * 
	 * logger.info("Generated Load ID :: " + newLoadId);
	 * 
	 * LocalDateTime time = LocalDateTime.now(); ConsolidatedQSPacking qspacking =
	 * new ConsolidatedQSPacking(); qspacking.setConId(con_id);
	 * qspacking.setLoadId(newLoadId); qspacking.setTransport_name(transport_name);
	 * qspacking.setVechile_no(vechile_no);
	 * qspacking.setMilestone_id(Integer.parseInt(milestone_id));
	 * qspacking.setCreatedBy(created_by); qspacking.setCreated_date(time);
	 * qspacking.setFactory_id(factory_id); qspacking.setGrand_total(grand_total);
	 * qspacking.setCancel(0);
	 * logger.info("EXECUTING METHOD :: BEFORE CREATE consolQSPACKING");
	 * ConsolidatedQSPacking objQSPacking =
	 * consolqsPackingRepository.save(qspacking);
	 * logger.info("EXECUTED METHOD :: AFTER CREATE consolQSPACKING"); count =
	 * objQSPacking.getPnId(); valCount = 0;
	 * logger.info("EXECUTING METHOD :: BEFORE CREATE consolQSPACKING ITEM"); String
	 * pn_id = String.valueOf(count); valCount =
	 * consolqsPackingRepository.insertconsolPackingItemRecord(qty, per_kgs,
	 * unit_price, total, UOM, type_id, created_by, pn_id, factory_id);
	 * 
	 * logger.info("EXECUTED METHOD :: AFTER CREATE consolQSPACKING ITEM");
	 * response.put("message", (count > 0 && valCount > 0) ? "Success" : "failure");
	 * response.put("status", (count > 0 && valCount > 0) ? "yes" : "no");
	 * response.put("action", "Insert_Record_In_consolQSPACKINGMASTER"); } catch
	 * (Exception e) { e.printStackTrace(); logger.
	 * error("ERROR IN THE METHOD FOR consolcreateQSPackingMasterNew ::   -> " +
	 * e.getMessage()); }
	 * logger.info("EXECUTED METHOD :: createconsolQSPackingMasterNew"); return
	 * response; }
	 */
	
	
	
	
	
	
	@SuppressWarnings("null")
	@PostMapping(value = "/consolpackingmaster/addnew")
	public @ResponseBody Map<String, Object> createconsolPackingMasterNew(
	        @RequestParam String con_id,
	        @RequestParam String transport_name,
	        @RequestParam String vechile_no,
	        @RequestParam String milestone_id,
	        @RequestParam String created_by,
	        @RequestParam int factory_id,
	        @RequestParam String qty,
	        @RequestParam String per_kgs,
	        @RequestParam String unit_price,
	        @RequestParam String total,
	        @RequestParam String UOM,
	        @RequestParam String type_id,
	        @RequestParam String grand_total,
	        @RequestParam(required = false, defaultValue = "[]") String items  //  new param
	) {
	    logger.info("EXECUTING METHOD :: createconsolPackingMasterNew");
	    Map<String, Object> response = new HashMap<>();

	    try {
	        int valCount = 0;
	        int count    = 0;

	        // ✅ Generate load ID
	        String lastconsolload = consolqsPackingRepository.findLastconsolLoad();
	        int nextNumber = 1;
	        if (lastconsolload != null && !lastconsolload.trim().isEmpty()) {
	            String numericOnly = lastconsolload.replaceAll("\\D+", "");
	            if (!numericOnly.isEmpty()) {
	                nextNumber = Integer.parseInt(numericOnly) + 1;
	            }
	        }
	        String newLoadId = String.format("CIN-%05d", nextNumber);
	        logger.info("Generated Load ID :: " + newLoadId);

	        // ✅ Save master record
	        LocalDateTime time = LocalDateTime.now();
	        ConsolidatedQSPacking qspacking = new ConsolidatedQSPacking();
	        qspacking.setConId(con_id);
	        qspacking.setLoadId(newLoadId);
	        qspacking.setTransport_name(transport_name);
	        qspacking.setVechile_no(vechile_no);
	        qspacking.setMilestone_id(Integer.parseInt(milestone_id));
	        qspacking.setCreatedBy(created_by);
	        qspacking.setCreated_date(time);
	        qspacking.setFactory_id(factory_id);
	        qspacking.setGrand_total(grand_total);
	        qspacking.setCancel(0);

	        logger.info("BEFORE SAVE :: ConsolidatedQSPacking");
	        ConsolidatedQSPacking objQSPacking = consolqsPackingRepository.save(qspacking);
	        logger.info("AFTER SAVE :: ConsolidatedQSPacking");

	        count = objQSPacking.getPnId();  // saved master ID

	        //  Save existing single packing item (your original logic)
	        String pn_id = String.valueOf(count);
	        valCount = consolqsPackingRepository.insertconsolPackingItemRecord(
	                qty, per_kgs, unit_price, total, UOM, type_id, created_by, pn_id, factory_id);

	        logger.info("AFTER SAVE :: Packing Item, valCount = " + valCount);

	        //  Parse and save dynamic invoice items into consolidated_invoice_items
	        ObjectMapper mapper = new ObjectMapper();
	        List<Map<String, Object>> itemList =
	                mapper.readValue(items, new TypeReference<List<Map<String, Object>>>() {});

	        logger.info("DYNAMIC ITEMS COUNT :: " + itemList.size());

	        for (int i = 0; i < itemList.size(); i++) {
	            Map<String, Object> item = itemList.get(i);

	            //  Skip empty rows — pn_id null or qty = 0
	            String pnIdStr = item.get("pn_id") != null ? item.get("pn_id").toString() : null;
	            String qtyStr  = item.get("qty")   != null ? item.get("qty").toString()   : "0";
	            String invid   = item.get("invid") != null ? item.get("invid").toString() : "";

	            if (pnIdStr == null || pnIdStr.equals("null") ||
	                invid.isEmpty() || Double.parseDouble(qtyStr) == 0) {
	                logger.info("SKIPPING EMPTY ITEM at index :: " + i);
	                continue;  //skip
	            }

	            ConsolidatedInvoiceItem invoiceItem = new ConsolidatedInvoiceItem();
	            invoiceItem.setSlno(i + 1);
	            invoiceItem.setPnId(Integer.parseInt(pnIdStr));
	            invoiceItem.setQty(Double.parseDouble(qtyStr));
	            invoiceItem.setPerKgs(item.get("per_kgs") != null ?
	                    Double.parseDouble(item.get("per_kgs").toString()) : null);
	            invoiceItem.setUnitPrice(item.get("unit_price") != null ?
	                    Double.parseDouble(item.get("unit_price").toString()) : null);
	            invoiceItem.setTotal(item.get("total") != null ?
	                    Double.parseDouble(item.get("total").toString()) : null);
	            invoiceItem.setUom(item.get("UOM") != null ?
	                    item.get("UOM").toString() : null);
	            invoiceItem.setStype(item.get("stype") != null ?
	                    item.get("stype").toString() : null);
	            invoiceItem.setIncType(item.get("itype") != null ? item.get("itype").toString() : null);
	            invoiceItem.setConpnid(Integer.valueOf(con_id));
	            
	            invoiceItem.setInvid(invid);
	            
//	            invoiceItem.setConsolDate(new java.util.Date());
	            invoiceItem.setConsolInvNo(newLoadId);

	            consolidatedInvoiceItemRepository.save(invoiceItem);
	            logger.info("SAVED INVOICE ITEM :: " + (i + 1));
	        }

	        response.put("message", (count > 0 && valCount > 0) ? "Success" : "failure");
	        response.put("status",  (count > 0 && valCount > 0) ? "yes"     : "no");
	        response.put("action",  "Insert_Record_In_consolQSPACKINGMASTER");

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("ERROR IN createconsolPackingMasterNew :: " + e.getMessage());
	        response.put("message", "Error occurred");
	        response.put("status",  "error");
	    }

	    logger.info("EXECUTED METHOD :: createconsolPackingMasterNew");
	    return response;
	}

	@GetMapping("/consolidatedpackingmaster/listnew")
	public @ResponseBody Map<String, Object> listconsolidatedPackinglistNew(@RequestParam String factory_id) {
		logger.info("EXECUTING METHOD :: listQSPackinglistNew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<consolidatedQSPacking_Interfaces> qsPackingInterfaces = null;
		try {
			qsPackingInterfaces = consolqsPackingRepository.listconsolidatedPAckingMasterRecord(factory_id);
			response.put("QSPACKING", qsPackingInterfaces);
			response.put("message", (qsPackingInterfaces.size() > 0) ? "Success" : "failure");
			response.put("status", (qsPackingInterfaces.size() > 0) ? "yes" : "no");
			response.put("action", "List_Record_In_consolidatedPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR listconsolidatedPackinglistNew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: listconsolidatedPackinglistNew");
		return response;
	}

	@GetMapping("/consolidatedpackingmaster/listnew/page")
	public @ResponseBody Map<String, Object> listconsolidatedPackinglistWithPagination(

			@RequestParam String factory_id, @RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		logger.info("EXECUTING METHOD :: listconsolidatedPackinglistWithPagination");

		Map<String, Object> response = new HashMap<>();

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<consolidatedQSPacking_Interfaces> pageResult = consolqsPackingRepository
					.listconsolidatedPAckingMasterRecordss(factory_id, search, pageable);

			response.put("data", pageResult.getContent());
			response.put("currentPage", pageResult.getNumber());
			response.put("totalItems", pageResult.getTotalElements());
			response.put("totalPages", pageResult.getTotalPages());
			response.put("status", pageResult.getContent().size() > 0 ? "yes" : "no");
			response.put("message", pageResult.getContent().size() > 0 ? "Success" : "No Data Found");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN METHOD listconsolidatedPackinglistWithPagination :: " + e.getMessage());
		}

		logger.info("EXECUTED METHOD :: listconsolidatedPackinglistWithPagination");

		return response;
	}

	@GetMapping("/consolidatedpackingmastersearchnew")
	public @ResponseBody Map<String, Object> serachconsolidatedPackingIdNew(@RequestParam String pn_id) {
		logger.info("EXECUTING METHOD :: consolidatedpackingmastersearchnew");
		Map<String, Object> response = new HashMap<String, Object>();
		List<consolidatedQSPacking_Interfaces> qsPackingInterfaces = null;
		try {
			qsPackingInterfaces = consolqsPackingRepository.searchconsolidatedPackingById(pn_id);
		
			String load_id = consolqsPackingRepository.serachcnid(pn_id);
			System.out.println("load Id"+load_id);
			
			
			
			List<ConsolidateInvoice>selectedInvoices = consolqsPackingRepository.SelectedInvoices(load_id);
			
			
			
		
			response.put("message", (qsPackingInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsPackingInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_consolidatedPACKINGMASTER");
			response.put("invocice data",selectedInvoices);
			response.put("DATA", qsPackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR consolidatedpackingmastersearchnew ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: consolidatedpackingmastersearchnew");
		return response;
	}

//	@PostMapping("/consolidatedupdatePacking")
//	public @ResponseBody Map<String, Object> updatePacking(
//	        @RequestParam String total,
//	        @RequestParam String unit_price,
//	        @RequestParam String type_id,
//	        @RequestParam String transport_name,
//	        @RequestParam String vechile_no,
//	        @RequestParam String pn_id,
//	        @RequestParam String modified_by,
//	        @RequestParam String items,@RequestParam String load_id) {
//
//	    Map<String, Object> response = new HashMap<>();
//
//	    System.out.println("Items JSON: " + items);
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    List<Map<String, Object>> itemList = null;
//
//	    try {
//
//	        // Convert JSON string to List
//	        itemList = mapper.readValue(items, List.class);
//
//	        System.out.println("Parsed Items: " + itemList);
//
//	        for (Map<String, Object> item : itemList) {
//	            System.out.println("Invoice ID: " + item.get("invid"));
//	            System.out.println("Qty: " + item.get("qty"));
//	        }
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        response.put("status", "error");
//	        response.put("message", "Invalid item data");
//	        return response;
//	    }
//	    
//	    
//	    
//	    
//	    List<String> dbInvoices = consolqsPackingRepository.getExistingInvoices(load_id);
//
//	    System.out.println("DB invoices: " + dbInvoices);
//
//	    int rows1 = consolqsPackingRepository.updatePackingItems(total, unit_price, type_id, pn_id, modified_by);
//
//	    int rows2 = consolqsPackingRepository.updatePackingNote(transport_name, vechile_no, total, pn_id, modified_by);
//
//	    if (rows1 > 0 || rows2 > 0) {
//
//	        response.put("status", "success");
//	        response.put("message", "Updated successfully");
//
//	    } 
//	    else if (rows1 == 0 && rows2 == 0) {
//
//	        response.put("status", "nochange");
//	        response.put("message", "No fields were changed.");
//
//	    } 
//	    else {
//
//	        response.put("status", "error");
//	        response.put("message", "Partial update occurred.");
//	    }
//
//	    return response;
//	}
//}

	
	
	
	@PostMapping("/consolidatedupdatePacking")
	public @ResponseBody Map<String, Object> updatePacking(
	        @RequestParam String total,
	        @RequestParam String unit_price,
	        @RequestParam String type_id,
	        @RequestParam String transport_name,
	        @RequestParam String vechile_no,
	        @RequestParam String pn_id,
	        @RequestParam String modified_by,
	        @RequestParam String items,
	        @RequestParam String load_id,@RequestParam String con_id,
	      @RequestParam  String qty,
	        @RequestParam String per_kgs) {

	    Map<String, Object> response = new HashMap<>();

	    ObjectMapper mapper = new ObjectMapper();
	    List<Map<String, Object>> itemList = null;

	    try {

	        itemList = mapper.readValue(items, List.class);

	        System.out.println("Parsed Items: " + itemList);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "error");
	        response.put("message", "Invalid item data");
	        return response;
	    }

	    // STEP 1: Get invoices from DB
	    List<String> dbInvoices = consolqsPackingRepository.getExistingInvoices(load_id);

	    System.out.println("DB invoices: " + dbInvoices);

	    // STEP 2: Extract UI invoices
	    Set<String> uiInvoices = new HashSet<>();

	    for (Map<String, Object> item : itemList) {
	        String invId = String.valueOf(item.get("invid"));
	        uiInvoices.add(invId);
	    }

	    System.out.println("UI invoices: " + uiInvoices);

	    // STEP 3: Mark deleted invoices
	    for (String dbInv : dbInvoices) {

	        if (!uiInvoices.contains(dbInv)) {

	            System.out.println("Marking deleted invoice: " + dbInv);

	            consolqsPackingRepository.markInvoiceDeleted(load_id, dbInv);
	        }
	    }

	    // STEP 4: Insert new invoices
	    for (Map<String, Object> item : itemList) {

	        String invId = String.valueOf(item.get("invid"));

	        if (!dbInvoices.contains(invId)) {

	            System.out.println("Inserting new invoice: " + invId);

	            consolqsPackingRepository.insertInvoiceItem(
	                    load_id,
	                    invId,
	                    String.valueOf(item.get("qty")),
	                    String.valueOf(item.get("per_kgs")),
	                    String.valueOf(item.get("unit_price")),
	                    String.valueOf(item.get("total")),
	                    String.valueOf(item.get("UOM")),
	                    String.valueOf(item.get("slno")),
	                    String.valueOf(item.get("pn_id")),
	                    String.valueOf(item.get("stype")),
	                    String.valueOf(con_id)
	            );
	        }
	    }

	    // STEP 5: Update packing tables
	    int rows1 = consolqsPackingRepository.updatePackingItems(total, unit_price, type_id, pn_id, modified_by,per_kgs,qty);

	    int rows2 = consolqsPackingRepository.updatePackingNote(transport_name, vechile_no, total, pn_id, modified_by);

	    if (rows1 > 0 || rows2 > 0) {

	        response.put("status", "success");
	        response.put("message", "Updated successfully");

	    } else if (rows1 == 0 && rows2 == 0) {

	        response.put("status", "nochange");
	        response.put("message", "Updated successfully");

	    } else {

	        response.put("status", "error");
	        response.put("message", "Partial update occurred");
	    }

	    return response;
	}
}