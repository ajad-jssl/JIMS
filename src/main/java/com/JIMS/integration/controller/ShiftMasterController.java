package com.JIMS.integration.controller;

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

import com.JIMS.integration.entity.Shift_Master;
import com.JIMS.integration.interfaces.Shift_Master_Service;
import com.JIMS.integration.repository.Shift_Master_Repistory;

@CrossOrigin
@RestController
@RequestMapping("api/shift")
public class ShiftMasterController {

	@Autowired
	private Shift_Master_Service shift_service;
	
	
	@Autowired
	private Shift_Master_Repistory shft_repo;

	@PostMapping
	public ResponseEntity<Object> addShift(@RequestBody Shift_Master shift_master) {
		
		long count =0;
		
	count = shft_repo.CheckforDuplicationBasedLocation(shift_master.getShiftCode(), shift_master.getLocation_id());
		
		if(count > 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("The shift code is already Exist for This Location");
		}
		
		
		
		
		shift_service.addShift(shift_master);
		return ResponseEntity.status(HttpStatus.OK).body("Service Method is successfully added");
	}

	@GetMapping("getAll")
	public ResponseEntity<Object> getAllshifts() {
		try {
			List<Shift_Master> allShift = shift_service.getAllShift();

			return ResponseEntity.status(HttpStatus.OK).body(allShift);

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}

	@GetMapping("get/{id}")
	public ResponseEntity<Object> getById(@PathVariable Integer id) {
		try {

			Optional<Shift_Master> shiftById = shift_service.getShiftById(id);
			if (shiftById.isPresent()) {
				Shift_Master shift_Master = shiftById.get();
				return ResponseEntity.status(HttpStatus.OK).body(shift_Master);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
			}

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}

//	@GetMapping("check/shift")
//	public ResponseEntity<Object> checkDuplicate(@RequestParam String code) {
//		List<Shift_Master> duplicate = shift_service.findDuplicate(code);
//
//		if (!duplicate.isEmpty()) {
//			return ResponseEntity.ok(Map.of("exists", true, "Mid", duplicate.get(0).getShiftCode()));
//
//		} else {
//			return ResponseEntity.ok(Map.of("exists", false));
//		}
//
//	}

	
	@PostMapping("/check-duplicate")
	public ResponseEntity<Object> checkShiftDuplicate(
	        @RequestBody Shift_Master shiftMaster) {

	System.out.println("the starting of the controller");
		
		
		
	    long count = shft_repo.CheckforDuplicationBasedLocation(
	            shiftMaster.getShiftCode(),
	            shiftMaster.getLocation_id()
	    );
	    System.out.println(count);

	    if (count > 0) {
	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body("The shift code already exists for this location");
	    }

	    System.out.println("ending of the controller");
	    return ResponseEntity
	            .status(HttpStatus.OK)
	            .body("No duplicate found");
	}
	
	
	
	@PutMapping("modified/{id}")
	public ResponseEntity<Object> modifyShift(@PathVariable Integer id, @RequestBody Shift_Master shift_master) {
		Optional<Shift_Master> shiftById = shift_service.getShiftById(id);
		
		

	Integer checkUsedshift = shift_service.checkUsedshift(id);
		
	if(checkUsedshift >0) {
		 return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("The transaction already exist for This Shift");
	}

		if (shiftById.isPresent()) {
			Shift_Master existing_shift = shiftById.get();

			if (shift_master.getShiftCode() != null) {
				existing_shift.setShiftCode(shift_master.getShiftCode());
			}
			if (shift_master.getShift_description() != null) {
				existing_shift.setShift_description(shift_master.getShift_description());
			}
			if (shift_master.getCreatedBy() != null) {
				existing_shift.setCreatedBy(shift_master.getCreatedBy());
			}
			if (shift_master.getCreatedDate() != null) {
				existing_shift.setCreatedDate(shift_master.getCreatedDate());
			}
			if (shift_master.getModifiedBy() != null) {
				existing_shift.setModifiedBy(shift_master.getModifiedBy());
			}
			if (shift_master.getModifiedDate() != null) {
				existing_shift.setModifiedDate(shift_master.getModifiedDate());
			}
			if (shift_master.getStatus() != null) {
				existing_shift.setStatus(shift_master.getStatus());
			}
			if (shift_master.getShiftHours() != null) {
				existing_shift.setShiftHours(shift_master.getShiftHours());
			}
			if(shift_master.getLocation_id() !=null) {
				existing_shift.setLocation_id(shift_master.getLocation_id());
			}

			shift_service.addShift(existing_shift);

			return ResponseEntity.ok(existing_shift);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shift not found with ID: " + id);
	}
	
	@GetMapping("/count")
	public ResponseEntity<Long> getAllShiftCount() {
	    Long shiftCount = shift_service.shiftCount();
	    return ResponseEntity.ok(shiftCount);
	}


}
