package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.Deparment;
import com.JIMS.integration.interfaces.Department_Service;


@CrossOrigin
@RestController
@RequestMapping("api/dept")
public class AMS_DepartmentController {
	
	
	@Autowired
	private Department_Service dept_service;
	
	
	@GetMapping("/getAll")
	public ResponseEntity<Object> getAllDept(){
		try {
			List<Deparment> alldepartment = dept_service.getAlldepartment();
			return ResponseEntity.status(HttpStatus.OK).body(alldepartment);
			
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}

}
