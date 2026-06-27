package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.UsersMaster;
import com.JIMS.integration.interfaces.User_Service;




@RestController
@RequestMapping("api/user")
public class AMS_UserController {
	
	
	
	@Autowired
	private User_Service user_service;
	
	@GetMapping("/getAll")
	public ResponseEntity<Object> getAllUser(){
		try {
		List<UsersMaster> allUser = user_service.getAllUser();
		return ResponseEntity.status(HttpStatus.OK).body(allUser);
		}
		catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.OK).body(ex);
		}
	}

}
