package com.JIMS.integration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.repository.MaintanceReportRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/report")
public class MaintenanceReport {

	@Autowired
	private MaintanceReportRepo repo;
	
	
	
	
	@GetMapping("/machines")
	public ResponseEntity<?> getMachines(
	        @RequestParam Integer factoryId,
	        @RequestParam Integer machineDescId) {

	    return ResponseEntity.ok(
	    		repo.getMachines(factoryId, machineDescId)
	    );
	}
	
	
	
	   @GetMapping("/details")
	    public ResponseEntity<?> getDetailsReport(
	            @RequestParam Integer factoryId,
	            @RequestParam Integer machineDescId,
	            @RequestParam Integer machineId,
	            @RequestParam String fromDate,
	            @RequestParam String toDate) {

	        return ResponseEntity.ok(
	                repo.getMaintenanceDetailReport(
	                        factoryId,
	                        machineDescId,
	                        machineId,
	                        fromDate,
	                        toDate));
	    }
	    
	    
	    
	    
}
