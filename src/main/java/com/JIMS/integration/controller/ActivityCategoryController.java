package com.JIMS.integration.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.ActivityCategory;
import com.JIMS.integration.interfaces.Activity_Category_Service;

@CrossOrigin
@RestController
@RequestMapping("/api/activity_category")
public class ActivityCategoryController {

	@Autowired
	private Activity_Category_Service activity_service;
	
	
	  @PostMapping
	    public ResponseEntity<Object> addActivity(@RequestBody ActivityCategory activityCategory) {
	        activity_service.addActivity(activityCategory);
	        return ResponseEntity.status(HttpStatus.CREATED).body("New Activity Category Added Successfully");
	    }
	
	@GetMapping("/getAll")
	public ResponseEntity<Object> getAllcon(){
		List<ActivityCategory> allActivity = activity_service.getAllActivity();
		return ResponseEntity.status(HttpStatus.OK).body(allActivity);
	}
	
	@GetMapping("/get/{id}")
    public ResponseEntity<Object> getActivityById(@PathVariable Integer id) {
        try {
            Optional<ActivityCategory> activityById = activity_service.getActivityById(id);
            if (activityById.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(activityById.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity Category not found");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @GetMapping("check/activity")
    public ResponseEntity<Object> checkDuplicate(@RequestParam String activityName) {
        List<ActivityCategory> duplicate = activity_service.checkDuplicate(activityName);

        if (!duplicate.isEmpty()) {
            return ResponseEntity.ok(Map.of("exists", true, "activityId", duplicate.get(0).getActivityId()));
        } else {
            return ResponseEntity.ok(Map.of("exists", false));
        }
    }

    @PutMapping("modified/{id}")
    public ResponseEntity<Object> modifyActivity(
            @PathVariable Integer id,
            @RequestBody ActivityCategory activityCategory) {

        Optional<ActivityCategory> activityById = activity_service.getActivityById(id);
        
        Integer count = activity_service.checkActivityUsed(id);
        
        if(count>0) {
        	 return ResponseEntity
	 	                .status(HttpStatus.CONFLICT)
	 	                .body("The Transcation is Already Exist for  this Activity");
        }

        if (activityById.isPresent()) {

            ActivityCategory existingActivity = activityById.get();

            if (activityCategory.getTypeId() != null) {
                existingActivity.setTypeId(activityCategory.getTypeId());
            }
            if (activityCategory.getActivityName() != null) {
                existingActivity.setActivityName(activityCategory.getActivityName());
            }
            if (activityCategory.getCreatedBy() != null) {
                existingActivity.setCreatedBy(activityCategory.getCreatedBy());
            }
            if (activityCategory.getCreatedDate() != null) {
                existingActivity.setCreatedDate(activityCategory.getCreatedDate());
            }
            if (activityCategory.getModifiedBy() != null) {
                existingActivity.setModifiedBy(activityCategory.getModifiedBy());
            }
            if (activityCategory.getModifiedDate() != null) {
                existingActivity.setModifiedDate(activityCategory.getModifiedDate());
            }
            if (activityCategory.getStatus() != null) {
                existingActivity.setStatus(activityCategory.getStatus());
            }

            activity_service.addActivity(existingActivity);

            return ResponseEntity.ok(existingActivity);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Activity Category not found with ID: " + id);
    }

    @GetMapping("/mapping")
    public ResponseEntity<Object> getActivityIdAndNameOnly() {
        try {
            List<Object[]> list = activity_service.getActivityIdAndNameOnly();
            List<Map<String, Object>> result = new ArrayList<>();

            for (Object[] row : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("activityId", ((Number) row[0]).intValue());
                map.put("activityName", (String) row[1]);
                result.add(map);
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
	
    
    @GetMapping("/count")
    public ResponseEntity<Long> getActivityCounts() {
        return ResponseEntity.ok(activity_service.getActivityCount());
    }

	
}
