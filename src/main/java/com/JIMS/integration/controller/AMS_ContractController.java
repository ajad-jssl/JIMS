package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.AMS_Contracts;
import com.JIMS.MIS.services.AMS_Contract_List_Service;


@CrossOrigin
@RestController
@RequestMapping("api/contract")
public class AMS_ContractController {

	@Autowired
	private AMS_Contract_List_Service contract_list_service;
	private List<AMS_Contracts> allContracts;
	
	@GetMapping("/getAll")
public ResponseEntity<Object> getAllContracts(){
		 try
		 {
			 
			 allContracts = contract_list_service.getAllContracts();
			 return ResponseEntity.status(HttpStatus.OK).body(allContracts);
			 
		 }catch(Exception ex) {
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		 }
	}
}
