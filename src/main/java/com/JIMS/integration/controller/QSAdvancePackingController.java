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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.QSAdavancePackingNote;
import com.JIMS.integration.interfaces.QSAdvancePackingInterfaces;
import com.JIMS.integration.interfaces.QSAdvancePackingItemsInterfaces;
import com.JIMS.integration.interfaces.QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES;
import com.JIMS.integration.repository.QSAdvancePackingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@Controller
@RequestMapping("/jssl")
public class QSAdvancePackingController {
	Logger logger = LogManager.getLogger(QSAdvancePackingController.class);
	@Autowired
	QSAdvancePackingRepository qsAdvancePackingRepository;
	@Value("${file.upload-dir}")
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	
    @SuppressWarnings("null")
	/* @Transactional(rollbackFor = Exception.class) */
    @PostMapping("/qsadvancepackingmaster/add")
    public @ResponseBody Map<String, Object> createQSAdvancePackingMaster(@RequestParam("file") MultipartFile file,
            @RequestParam String contract_id, @RequestParam String milestone_id, @RequestParam String created_by,
            @RequestParam int factory_id, @RequestParam List<String> qty, @RequestParam List<String> per_kgs,
            @RequestParam List<String> unit_price, @RequestParam List<String> total, @RequestParam List<String> UOM_id,
            @RequestParam List<String> type_id, @RequestParam List<String> pices, @RequestParam String grand_total) {

        Map<String, Object> response = new HashMap<>();
        try {
            int valCount = 0;
            int count = 0;
            int size = qty.size();

            if (size != per_kgs.size() || size != unit_price.size() || size != total.size()
                    || size != UOM_id.size() || size != type_id.size() || size != pices.size()) {
                response.put("message", "All input lists must have the same size!");
                return response;
            }

            String uniqueFileName = null;
            long maxSize = 10 * 1024 * 1024;

			/*
			 * if (file != null && !file.isEmpty()) { if
			 * (!file.getContentType().equals("application/pdf")) { response.put("message",
			 * "Only PDF files are allowed."); response.put("status", "no"); return
			 * response; } if (file.getSize() > maxSize) { response.put("message",
			 * "File size exceeds the maximum limit of 10MB."); response.put("status",
			 * "no"); return response; }
			 * 
			 * uniqueFileName = UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();
			 * File directory = new File(uploadDir); if (!directory.exists()) {
			 * directory.mkdirs(); }
			 * 
			 * File serverFile = new File(directory, uniqueFileName); if
			 * (serverFile.exists()) { response.put("message",
			 * "File already exists! Please provide another File."); return response; }
			 * file.transferTo(serverFile); }
			 */

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

                String originalFileName = file.getOriginalFilename();

                // 🔴 CHECK DUPLICATE FILE FOR SAME CONTRACT
                int exists = qsAdvancePackingRepository.checkFileExistsForContract(contract_id, originalFileName);

                if (exists > 0) {
                    response.put("message", "This file is already uploaded for the selected contract,please choose different file.");
                    response.put("status", "no");
                    return response;
                }

                uniqueFileName = UUID.randomUUID() + "_@$@_" + originalFileName;

                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File serverFile = new File(directory, uniqueFileName);
                file.transferTo(serverFile);
            }
            
            // 1. Generate next load_id like ADV-1, ADV-2, ...
            String lastLoadId = qsAdvancePackingRepository.getLastGlobalLoadId(); // Add this method in repository
            int nextLoadNumber = 1;
            if (lastLoadId != null && lastLoadId.startsWith("ADV-")) {
                try {
                    nextLoadNumber = Integer.parseInt(lastLoadId.substring(4)) + 1;
                } catch (NumberFormatException e) {
                    nextLoadNumber = 1;
                }
            }
            String newLoadId = "ADV-" + nextLoadNumber;

            // 2. Save master record
            LocalDateTime time = LocalDateTime.now();
            QSAdavancePackingNote qsAdvancePacking = new QSAdavancePackingNote();
            qsAdvancePacking.setConId(contract_id);
            qsAdvancePacking.setFilepath(uniqueFileName);
            qsAdvancePacking.setMilestone_id(Integer.parseInt(milestone_id));
            qsAdvancePacking.setCreatedBy(created_by);
            qsAdvancePacking.setCreated_date(time);
            qsAdvancePacking.setFactory_id(factory_id);
            qsAdvancePacking.setGrand_total(grand_total);
            qsAdvancePacking.setLoad_id(newLoadId); // <--- NEW LINE
            qsAdvancePacking.setcancel(0);

            QSAdavancePackingNote objQSAdvancePacking = qsAdvancePackingRepository.save(qsAdvancePacking);
            count = objQSAdvancePacking.getPnId();

            // 3. Save item records
            valCount = 0;
            for (int i = 0; i < size; i++) {
                String pn_id = String.valueOf(count);
                valCount = qsAdvancePackingRepository.insertQSAdvancePackingItemRecord(qty.get(i), per_kgs.get(i),
                        unit_price.get(i), total.get(i), UOM_id.get(i), type_id.get(i), pices.get(i),
                        created_by, pn_id, factory_id);
            }

            response.put("message", (count > 0 && valCount > 0) ? "Success" : "failure");
            response.put("status", (count > 0 && valCount > 0) ? "yes" : "no");
            response.put("action", "Insert_Record_In_QSADVANCEPACKINGMASTER");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Exception occurred: " + e.getMessage());
            response.put("status", "no");
        }
        return response;
    }
    
    @SuppressWarnings("null")
    @PostMapping("/qsadvancepackingmaster/update")
    public @ResponseBody Map<String, Object> updateQSAdvancePackingMaster(
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String existing_filepath,
            @RequestParam String grand_total,
            @RequestParam String modified_by,
            @RequestParam String pn_id,
            @RequestParam int factory_id,
            @RequestParam List<String> qty,
            @RequestParam List<String> per_kgs,
            @RequestParam List<String> unit_price,
            @RequestParam List<String> total,
            @RequestParam List<String> slno,
            @RequestParam List<String> UOM_id,
            @RequestParam List<String> type_id,
            @RequestParam List<String> pices,
            @RequestParam(required = false) String data
    ) {

        Map<String, Object> response = new HashMap<>();

        String finalFilePath = existing_filepath;   // default
        String pendingUploadFileName = null;

        try {
            /* ---------- 1. Optional JSON payload ---------- */
            if (data != null && !data.isEmpty()) {
                new ObjectMapper().readValue(data, Map.class);
            }

            /* ---------- 2. List size validation ---------- */
            int size = qty.size();
            if (size != per_kgs.size() || size != unit_price.size()
                    || size != total.size() || size != UOM_id.size()
                    || size != type_id.size() || size != pices.size()) {

                response.put("message", "All input lists must have the same size.");
                response.put("status", "no");
                return response;
            }

            /* ---------- 3. Validate file ONLY (do not upload yet) ---------- */
            if (file != null && !file.isEmpty()) {

                if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
                    response.put("message", "Only PDF files are allowed.");
                    response.put("status", "no");
                    return response;
                }

                pendingUploadFileName =
                        UUID.randomUUID() + "_@$@_" + file.getOriginalFilename();

                finalFilePath = pendingUploadFileName; // will persist only if DB updates
            }

            /* ---------- 4. Update MASTER (trigger decides NO CHANGE) ---------- */
            int masterCount =
                    qsAdvancePackingRepository.updateQSAdvancePackingMasterRecord(
                            finalFilePath, grand_total, modified_by, pn_id
                    );

            /* ---------- 5. Insert / update ITEM rows ---------- */
            int insertCount = 0;
            int updateCount = 0;

            for (int i = 0; i < size; i++) {

                boolean isExisting = Integer.parseInt(slno.get(i)) != 0;

                if (isExisting) {
                    updateCount += qsAdvancePackingRepository
                            .updateQSAdvancePackingItemsRecord(
                                    qty.get(i), per_kgs.get(i), unit_price.get(i), total.get(i),
                                    UOM_id.get(i), type_id.get(i), pices.get(i),
                                    modified_by, pn_id, slno.get(i));
                } else {
                    insertCount += qsAdvancePackingRepository
                            .insertQSAdvancePackingItemRecord(
                                    qty.get(i), per_kgs.get(i), unit_price.get(i), total.get(i),
                                    UOM_id.get(i), type_id.get(i), pices.get(i),
                                    modified_by, pn_id, factory_id);
                }
            }

            /* ---------- 6. Decide outcome (trigger-safe) ---------- */
            boolean updated =
                    masterCount > 0 || insertCount > 0 || updateCount > 0;

            /* ---------- 7. Upload file ONLY if DB updated ---------- */
            if (updated && pendingUploadFileName != null) {

                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File serverFile = new File(directory, pendingUploadFileName);
                file.transferTo(serverFile);
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

            logger.error("ERROR IN updateQSAdvancePackingMaster", e);

            response.put("message", "Failed to update record.");
            response.put("status", "no");
            response.put("action", "ERROR");
        }

        return response;
    }

    @PostMapping("/qsadvancepackingmaster/delete")
    public @ResponseBody Map<String, Object> deleteQSAdvancePackingRecord(
            @RequestParam String modified_by,
            @RequestParam String pn_id) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1️⃣ Delete child records FIRST
            qsAdvancePackingRepository
                    .deleteQSAdvancePackingMasterRecord(pn_id, modified_by);

            // 2️⃣ Delete master record
            int count = qsAdvancePackingRepository
                    .deleteQSAdvancePackingMasterRecord(pn_id, modified_by);

            if (count > 0) {
                response.put("message", "Success");
                response.put("status", "yes");
            } else {
                response.put("message", "Already deleted or not found");
                response.put("status", "no");
            }

            response.put("action", "DELETE_Record_In_QSADVANCEPACKINGMASTER");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", e.getMessage());
            response.put("status", "error");
        }
        return response;
    }

	@PostMapping("/qsadvancepackingitemmaster/delete")
	public @ResponseBody Map<String, Object> deleteQSAdvancePackingItemRecord(
	        @RequestParam String modified_by,
	        @RequestParam String slno) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        int count = qsAdvancePackingRepository
	                .deleteQSAdvancePackingItemMasterRecord(slno, modified_by);

	        response.put("message", count > 0 ? "Success" : "failure");
	        response.put("status", count > 0 ? "yes" : "no");
	        response.put("action", "DELETE_Record_In_QSADVANCEPACKINGITEM_MASTER");

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", e.getMessage());
	        response.put("status", "error");
	    }
	    return response;
	}


	@GetMapping("/qsadvancepackingnotemaster/list") 
	public @ResponseBody Map<String, Object> listQSAdvancePackinglist(@RequestParam String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSAdvancePackingInterfaces> qsAdvancePackingInterfaces = null;
		//List<QSAdvancePackingItemsInterfaces> qsAdvancePackingItemsInterfaces = null;
		try {
			qsAdvancePackingInterfaces = qsAdvancePackingRepository.listQSAdvancePAckingMasterRecord(factory_id);
			//qsAdvancePackingItemsInterfaces = qsAdvancePackingRepository
					//.listQSAdvancePAckingItemMasterRecord(factory_id);
			response.put("QSADVANCEPACKING", qsAdvancePackingInterfaces);
			//response.put("QSADVANCEPACKING_ITEM", qsAdvancePackingItemsInterfaces);
			response.put("message",
					(qsAdvancePackingInterfaces != null && qsAdvancePackingInterfaces != null) ? "Success" : "failure");
			/*
			 * response.put("status", (qsAdvancePackingItemsInterfaces != null &&
			 * qsAdvancePackingItemsInterfaces != null) ? "yes" : "no");
			 */
			response.put("action", "List_Record_In_QSADVANCEPACKINGNOTEMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	@GetMapping("/qsadvancepackingnotemaster/listss")
	public @ResponseBody Map<String, Object> listQSAdvancePackinglistss(
	        @RequestParam String factory_id,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String search) {

	    Map<String, Object> response = new HashMap<>();

	    try {

	        Pageable pageable = PageRequest.of(page, size,
	                Sort.by("created_date").descending());

	        Page<QSAdvancePackingInterfaces> pageResult =
	                qsAdvancePackingRepository
	                        .listQSAdvancePAckingMasterRecordssss(factory_id, search, pageable);

	        response.put("data", pageResult.getContent());
	        response.put("currentPage", pageResult.getNumber());
	        response.put("totalItems", pageResult.getTotalElements());
	        response.put("totalPages", pageResult.getTotalPages());
	        response.put("message", "Success");

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Failure");
	    }

	    return response;
	}

	@GetMapping("/qsadvancepackingnotemastersearch")
	public @ResponseBody Map<String, Object> serachQSAdvancePackingId(@RequestParam String pn_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES> qsAdvancePackingInterfaces = null;
		try {
			qsAdvancePackingInterfaces = qsAdvancePackingRepository.searchQSAdvancePackingById(pn_id);
			response.put("message", (qsAdvancePackingInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsAdvancePackingInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSADVANCEPACKINGNOTEITEMMASTER");
			response.put("DATA", qsAdvancePackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qsadvancepackingnoteitemmastersearch")
	public @ResponseBody Map<String, Object> serachQSAdvancePackingItemId(@RequestParam String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		QSAdvancePackingItemsInterfaces qsAdvancePackingItemsInterfaces = null;
		try {
			qsAdvancePackingItemsInterfaces = qsAdvancePackingRepository.searchQSAdvancePackingItemById(slno);
			response.put("message", (qsAdvancePackingItemsInterfaces != null) ? "Success" : "failure");
			response.put("status", (qsAdvancePackingItemsInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSADVANCEPACKINGITEMMASTER");
			response.put("DATA", qsAdvancePackingItemsInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	 @GetMapping("/qsadvancepackingmaster/viewpdfnew")
		public ResponseEntity<Resource> viewFileNew(@RequestParam int id) {
			logger.info("EXECUTING METHOD :: viewFileNew");
			try {
				Optional<Integer> value = qsAdvancePackingRepository.getadvPn_id(String.valueOf(id));
				if (value.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
				int valuePnid = value.get();
				String filepath = qsAdvancePackingRepository.getadvFilePath(valuePnid);
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
	/*
	 * @GetMapping("/qsadvancepackingnotemastersearch/{pn_id}") public @ResponseBody
	 * Map<String,Object> serachQSAdvancePackingId(@PathVariable String pn_id){
	 * Map<String, Object> response = new HashMap<String, Object>();
	 * List<QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES>
	 * qsAdvancePackingInterfaces = null; try { qsAdvancePackingInterfaces =
	 * qsAdvancePackingRepository.searchQSAdvancePackingById(pn_id);
	 * response.put("message", (qsAdvancePackingInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (qsAdvancePackingInterfaces != null)?"yes":"no"); response.put("action",
	 * "Search_Record_In_QSADVANCEPACKINGNOTEITEMMASTER"); response.put("DATA",
	 * qsAdvancePackingInterfaces); } catch (Exception e) { e.printStackTrace(); }
	 * return response; }
	 * 
	 * @GetMapping("/qsadvancepackingnoteitemmastersearch/{slno}")
	 * public @ResponseBody Map<String,Object>
	 * serachQSAdvancePackingItemId(@PathVariable String slno){ Map<String, Object>
	 * response = new HashMap<String, Object>(); QSAdvancePackingItemsInterfaces
	 * qsAdvancePackingItemsInterfaces = null; try { qsAdvancePackingItemsInterfaces
	 * = qsAdvancePackingRepository.searchQSAdvancePackingItemById(slno);
	 * response.put("message", (qsAdvancePackingItemsInterfaces !=
	 * null)?"Success":"failure"); response.put("status",
	 * (qsAdvancePackingItemsInterfaces != null)?"yes":"no"); response.put("action",
	 * "Search_Record_In_QSADVANCEPACKINGITEMMASTER"); response.put("DATA",
	 * qsAdvancePackingItemsInterfaces); } catch (Exception e) {
	 * e.printStackTrace(); } return response; }
	 */

	/*
	 * @PostMapping("/qsadvancepackingitemmaster/delete") // history is pending
	 * public @ResponseBody Map<String,Object>
	 * deleteQSAdvancePackingItemRecord(@RequestBody Map<String,String> val){
	 * 
	 * Map<String, Object> response = new HashMap<String, Object>(); try { String
	 * modified_by = val.get("modified_by"); String slno = val.get("slno"); int
	 * count =
	 * qsAdvancePackingRepository.delteQSAdvancePackingItemMasterRecord(slno,
	 * modified_by); response.put("message", (count > 0)?"Success":"failure");
	 * response.put("status", (count > 0)?"yes":"no"); response.put("action",
	 * "DELETE_Record_In_QSADVANCEPACKINGMASTER"); } catch (Exception e) {
	 * e.printStackTrace(); } return response; }
	 */

	/*
	 * @GetMapping("/qsadvancepackingnotemaster/list/{factory_id}")
	 * public @ResponseBody Map<String,Object>
	 * listQSAdvancePackinglist(@PathVariable String factory_id){ Map<String,
	 * Object> response = new HashMap<String, Object>();
	 * List<QSAdvancePackingInterfaces> qsAdvancePackingInterfaces = null;
	 * List<QSAdvancePackingItemsInterfaces> qsAdvancePackingItemsInterfaces = null;
	 * try { qsAdvancePackingInterfaces =
	 * qsAdvancePackingRepository.listQSAdvancePAckingMasterRecord(factory_id);
	 * qsAdvancePackingItemsInterfaces =
	 * qsAdvancePackingRepository.listQSAdvancePAckingItemMasterRecord(factory_id);
	 * response.put("QSADVANCEPACKING", qsAdvancePackingInterfaces);
	 * response.put("QSADVANCEPACKING_ITEM", qsAdvancePackingItemsInterfaces);
	 * response.put("message", (qsAdvancePackingInterfaces != null &&
	 * qsAdvancePackingInterfaces != null)?"Success":"failure");
	 * response.put("status", (qsAdvancePackingItemsInterfaces != null &&
	 * qsAdvancePackingItemsInterfaces != null)?"yes":"no"); response.put("action",
	 * "List_Record_In_QSADVANCEPACKINGNOTEMASTER"); } catch (Exception e) {
	 * e.printStackTrace(); } return response; }
	 */

	/*
	 * @PostMapping("/qsadvancepackingmaster/update") public @ResponseBody
	 * Map<String,Object> updateQSAdvancePackingMaster(@RequestBody Map<String,
	 * Object> data){ Map<String, Object> response = new HashMap<String, Object>();
	 * Map<String, String> val = null; List<Map<String, String>> obj = null; try {
	 * // Extract the 'val' part from the data val = (Map<String, String>)
	 * data.get("val"); int valCount = 0; QSAdavancePackingNote objQSAdvancePacking
	 * = null; // Extract the 'obj' part, which is a list of maps obj =
	 * (List<Map<String, String>>) data.get("obj"); if((val != null &&
	 * !val.isEmpty()) && (obj != null && !obj.isEmpty())) { String con_id =
	 * val.get("con_id"); String filepath = val.get("filepath"); String milestone_id
	 * = val.get("milestone_id"); String modified_by = val.get("modified_by");
	 * String pn_id = val.get("pn_id"); LocalDateTime time = LocalDateTime.now();
	 * QSAdavancePackingNote qsAdvancePacking = new QSAdavancePackingNote();
	 * qsAdvancePacking.setConId(con_id); qsAdvancePacking.setFilepath(filepath);
	 * qsAdvancePacking.setMilestone_id(Integer.parseInt(milestone_id));
	 * qsAdvancePacking.setModified_by(modified_by);
	 * qsAdvancePacking.setModified_date(time);
	 * qsAdvancePacking.setPnId(Integer.parseInt(pn_id)); objQSAdvancePacking =
	 * qsAdvancePackingRepository.save(qsAdvancePacking);
	 * //qsPackingRepository.insterQSPackingMasterHistoryRecord(modified_by, pn_id);
	 * for (Map<String, String> item : obj) { String qty = item.get("qty"); String
	 * per_kgs = item.get("per_kgs"); String unit_price = item.get("unit_price");
	 * String total = item.get("total"); String UOM_id = item.get("unit_id"); String
	 * type_id = item.get("type_id"); String pices = item.get("pices"); String slno
	 * = item.get("slno"); //
	 * qsPackingRepository.insterQSPackingItenMasterHistoryRecord(modified_by,
	 * slno); valCount =
	 * qsAdvancePackingRepository.updateQSAdvancePackingItemsRecord(qty,per_kgs,
	 * unit_price,total,UOM_id,type_id,pices,modified_by,pn_id,slno); } }
	 * response.put("message", (objQSAdvancePacking != null && valCount > 0)?
	 * "Success":"failure"); response.put("status", (objQSAdvancePacking != null &&
	 * valCount > 0) ? "yes":"no"); response.put("action",
	 * "UPDATE_Record_In_QSADVANCEPACKINGMASTER"); } catch (Exception e) {
	 * e.printStackTrace(); } return response; }
	 */

	/*
	 * @PostMapping("/qsadvancepackingmaster/delete") // history is pending
	 * public @ResponseBody Map<String,Object>
	 * deleteQSAdvancePackingRecord(@RequestBody Map<String,String> val){
	 * 
	 * Map<String, Object> response = new HashMap<String, Object>(); try { String
	 * modified_by = val.get("modified_by"); String pn_id = val.get("pn_id"); //
	 * qsAdvancePackingRepository.updateQSPackingMasterHistoryRecord(modified_by,
	 * pn_id); int count =
	 * qsAdvancePackingRepository.delteQSAdvancePackingMasterRecord(pn_id,
	 * modified_by); //
	 * qsAdvancePackingRepository.updateQSPackingItemMasterHistoryRecord(
	 * modified_by, pn_id); int count1 =
	 * qsAdvancePackingRepository.delteQSAdvancePackingItemMasterRecord(pn_id,
	 * modified_by); response.put("message", (count > 0 && count1 >
	 * 0)?"Success":"failure"); response.put("status", (count > 0 && count1 >
	 * 0)?"yes":"no"); response.put("action",
	 * "DELETE_Record_In_QSADVANCEPACKINGMASTER"); } catch (Exception e) {
	 * e.printStackTrace(); } return response; }
	 */

	/*
	 * @PostMapping("/qsadvancepackingmaster/add") public @ResponseBody
	 * Map<String,Object> createQSAdvancePackingMaster(@RequestBody Map<String,
	 * Object> data){ Map<String, Object> response = new HashMap<String, Object>();
	 * Map<String, String> val = null; List<Map<String, String>> obj = null; try {
	 * // Extract the 'val' part from the data val = (Map<String, String>)
	 * data.get("val"); int valCount = 0; int count = 0; // Extract the 'obj' part,
	 * which is a list of maps obj = (List<Map<String, String>>) data.get("obj");
	 * if((val != null && !val.isEmpty()) && (obj != null && !obj.isEmpty())) {
	 * String con_id = val.get("con_id"); String filepath = val.get("filepath");
	 * String milestone_id = val.get("milestone_id"); String created_by =
	 * val.get("created_by"); int factory_id = val.containsKey("factory_id") ?
	 * Integer.parseInt(val.get("factory_id")) : 0; LocalDateTime time =
	 * LocalDateTime.now(); QSAdavancePackingNote qsAdvancePacking = new
	 * QSAdavancePackingNote(); qsAdvancePacking.setConId(con_id);
	 * qsAdvancePacking.setFilepath(filepath);
	 * qsAdvancePacking.setMilestone_id(Integer.parseInt(milestone_id));
	 * qsAdvancePacking.setCreatedBy(created_by);
	 * qsAdvancePacking.setCreated_date(time);
	 * qsAdvancePacking.setFactory_id(factory_id); QSAdavancePackingNote
	 * objQSAdvancePacking = qsAdvancePackingRepository.save(qsAdvancePacking);
	 * count = objQSAdvancePacking.getPnId(); valCount = 0; for (Map<String, String>
	 * item : obj) { String qty = item.get("qty"); String per_kgs =
	 * item.get("per_kgs"); String unit_price = item.get("unit_price"); String total
	 * = item.get("total"); String UOM_id = item.get("unit_id"); String type_id =
	 * item.get("type_id"); String pices = item.get("pices"); String pn_id =
	 * String.valueOf(count); valCount =
	 * qsAdvancePackingRepository.insertQSAdvancePackingItemRecord(qty,per_kgs,
	 * unit_price,total,UOM_id,type_id,pices,created_by,pn_id); } }
	 * response.put("message", (count > 0 && valCount > 0 )?"Success":"failure");
	 * response.put("status", (count > 0 && valCount > 0)?"yes":"no");
	 * response.put("action", "Insert_Record_In_QSADVANCEPACKINGMASTER"); } catch
	 * (Exception e) { e.printStackTrace(); } return response; }
	 */
}
