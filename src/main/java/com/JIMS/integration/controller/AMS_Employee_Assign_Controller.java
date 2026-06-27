package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.Employee_Assign;
import com.JIMS.integration.entity.ResponseMessage;
import com.JIMS.integration.interfaces.Employee_Assign_Service;



@CrossOrigin
@RestController
@RequestMapping("api/assign")
public class AMS_Employee_Assign_Controller {
	
	@Autowired
	private Employee_Assign_Service employee_assign_service;
	
	
	@PostMapping
	public ResponseEntity<Object> insertintoDatabase(@RequestBody Employee_Assign employee_assign){
		
		try {
			
			Employee_Assign insertIntoDat = employee_assign_service.insertIntoDat(employee_assign);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Success", "Employee_Assign is successfully"));
			
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
		
	}
	
	
	
	@GetMapping("/getAll")
	public ResponseEntity<Object> getAllAssign(){
		try {
			List<Employee_Assign> allAssign = employee_assign_service.getAllAssign();
			return ResponseEntity.status(HttpStatus.OK).body(allAssign);
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}
	
	
@DeleteMapping("/{a_id}")
public ResponseEntity<Object> deleteAssing(@PathVariable Integer a_id){
	try {
		
		employee_assign_service.deletetAssign(a_id);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Successs","The Assign is Delete"));
		
	}catch(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	}
}

}
