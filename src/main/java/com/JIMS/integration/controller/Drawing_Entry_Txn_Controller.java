package com.JIMS.integration.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.Drawing_Entry_Entity;
import com.JIMS.integration.interfaces.DrawingEntryDTO;
import com.JIMS.integration.interfaces.DrawingReportDTO;
import com.JIMS.integration.interfaces.MonthlyTotalDTO;
import com.JIMS.integration.repository.DrawingEntryRepository;

import jakarta.transaction.Transactional;


@RestController
@RequestMapping("api/Drawing_txn")
@CrossOrigin
public class Drawing_Entry_Txn_Controller {

	
	@Autowired
	private DrawingEntryRepository drawing_repo;
	
	
	@GetMapping("/getAll")
	public ResponseEntity<Object> getALlTxn(){
		List<Drawing_Entry_Entity> all = drawing_repo.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(all);
	}
	

	
	
	
	


	@PostMapping
	public ResponseEntity<Object> saveUpdateAndDelete(
	        @RequestBody DrawingEntrySaveRequestDTO request) {

	    // 1️⃣ SOFT DELETE
	    if (request.getDeleteIds() != null && !request.getDeleteIds().isEmpty()) {

	        for (Integer id : request.getDeleteIds()) {

	            Drawing_Entry_Entity existing = drawing_repo.findById(id).orElse(null);

	            if (existing != null) {
	                existing.setIsDeleted(1); // mark as deleted
	                existing.setDeletedBy(existing.getCreatedBy()); // taking createdBy
	                existing.setDeletedDate(LocalDateTime.now());

	                drawing_repo.save(existing);
	            }
	        }
	    }

	    // 2️⃣ SAVE / UPDATE
	    if (request.getSaveOrUpdateList() != null) {

	        for (Drawing_Entry_Entity entry : request.getSaveOrUpdateList()) {

	            if (entry.getId() != null) {

	                Drawing_Entry_Entity existing =
	                        drawing_repo.findById(entry.getId()).orElse(null);

	                if (existing != null) {

	                    existing.setShiftId(entry.getShiftId());
	                    existing.setContractId(entry.getContractId());
	                    existing.setPhaseId(entry.getPhaseId());
	                    existing.setTypeId(entry.getTypeId());
	                    existing.setActivityCategoryId(entry.getActivityCategoryId());
	                    existing.setDhHours(entry.getDhHours());
	                    existing.setIhHours(entry.getIhHours());
	                    existing.setRhHours(entry.getRhHours());
	                    existing.setRework(entry.getRework());
	                    existing.setReasonId(entry.getReasonId());
	                    existing.setEcNo(entry.getEcNo());
	                    existing.setTotalHours(entry.getTotalHours());
	                    existing.setGrandTotal(entry.getGrandTotal());
	                    existing.setModifiedBy(entry.getCreatedBy());
	                    existing.setMonth(entry.getMonth());
	                    existing.setModifiedDate(LocalDateTime.now());
	                    

	                    drawing_repo.save(existing);
	                }

	            } else {

	                entry.setCreatedDate(LocalDateTime.now());
	                drawing_repo.save(entry);
	            }
	        }
	    }

	    return ResponseEntity.ok("Data saved, updated and deleted successfully");
	}
	
	
	 @GetMapping("/by-user")
	    public Map<String, Object> getDrawingReportByUser(
	            @RequestParam("year") Integer year,
	            @RequestParam("month") Integer month,
	            @RequestParam("userId") Integer userId) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            List<DrawingReportDTO> reportData = drawing_repo.getDrawingReportByUser(year, month, userId);

	            response.put("Data", reportData);
	            response.put("count", reportData.size());
	            response.put("success", true);

	        } catch (Exception e) {
	            response.put("error", "Failed to fetch drawing report for user");
	            response.put("message", e.getMessage());
	            response.put("success", false);
	        }

	        return response;
	    }
	 
	 
	 @GetMapping("/by-user/by-year")
	    public Map<String, Object> getDrawingReportByUseryear(
	            @RequestParam("year") Integer year,
	            
	            @RequestParam("userId") Integer userId) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            List<DrawingReportDTO> reportData = drawing_repo.getDrawingReportByUserByyear(year, userId);

	            response.put("Data", reportData);
	            response.put("count", reportData.size());
	            response.put("success", true);

	        } catch (Exception e) {
	            response.put("error", "Failed to fetch drawing report for user");
	            response.put("message", e.getMessage());
	            response.put("success", false);
	        }

	        return response;
	    }
	
	 @GetMapping("/test-drawing-report")
	 public Map<String, Object> getTestDrawingReport() {

	     Map<String, Object> response = new HashMap<>();
	     List<Map<String, Object>> dataList = new ArrayList<>();

	     for (int i = 1; i <= 2000; i++) {

	         Map<String, Object> row = new HashMap<>();

	         row.put("typeName", "Editor");
	         row.put("year", 2026);
	         row.put("activityName", "Drawing Editor");
	         row.put("jobCode", "C015");
	         row.put("cname", "JSW");
	         row.put("monthName", "March");

	         int day = (i % 28) + 1;
	         row.put("txtDate", String.format("%02d/03", day));

	         row.put("shiftCode", (i % 2 == 0) ? "A+G" : "A");
	         row.put("phaseId", String.valueOf(10 + (i % 10)));

	         double dh = 4 + (i % 4);
	         double ih = 0;
	         double rh = 0;

	         row.put("dhHours", dh);
	         row.put("ihHours", ih);
	         row.put("rhHours", rh);
	         row.put("totalHours", dh + ih + rh);

	         dataList.add(row);
	     }

	     response.put("success", true);
	     response.put("count", dataList.size());
	     response.put("Data", dataList);

	     return response;
	 }
	
	
    @GetMapping("/by-date")
    public ResponseEntity<?> getByDate(
            @RequestParam Integer userId,
            @RequestParam Integer year,
            @RequestParam String txtDate) {

        // Directly call the repository
        List<DrawingEntryDTO> data = drawing_repo.findByUserIdAndTxtDateAndYear(userId, txtDate, year);

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("exists", !data.isEmpty());
        response.put("Data", data);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

	
    
    @GetMapping("/total-grand-total")
    public ResponseEntity<Map<String, Object> > getTotalGrandTotal(
            @RequestParam Integer userId,
            @RequestParam Integer year,
            @RequestParam String txtDate) {

        BigDecimal total = drawing_repo
                .getTotalGrandTotalNative(txtDate, year, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("year", year);
        response.put("txtDate", txtDate);
        response.put("totalGrandTotal", total);

        return ResponseEntity.ok(response);
    }
    
    

    
    
    @GetMapping("/monthly/{year}/{userId}")
    public Map<String, Object> getSummary(
            @PathVariable int year,
            @PathVariable int userId) {
        return drawing_repo.getMonthlySummary(year, userId);
    }
    
    
    
    @GetMapping("/getHours")
    public ResponseEntity<?> getHours(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer contractId,
            @RequestParam(required = false) String  fromDate,   // "2026-01-01"
            @RequestParam(required = false) String  toDate) {   // "2026-03-16"

        try {
            List<Object[]> rows = drawing_repo.getHours(userId, contractId, fromDate, toDate);

            List<Map<String, Object>> result = new ArrayList<>();
            for (Object[] row : rows) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("jobCode",     row[0]);
                map.put("userId",      row[1]);
                map.put("username",    row[2]);
                map.put("location",    row[3]);
                map.put("finalTotal",  row[4]);
                result.add(map);
            }

            return ResponseEntity.ok(Map.of("status","yes", "data", result));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                   .body(Map.of("status","no", "message", e.getMessage()));
        }
    }
    
    
    
	
}
