package com.JIMS.integration.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.TimeModel;
import com.JIMS.MIS.services.TimeServices;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class TimeController {

	 @Autowired
	  private TimeServices TimeServices;

	    @GetMapping("/Weight")  
	    public List<TimeModel> getweight(Integer load_id) {
	    return TimeServices.getweightByload_id(load_id); 
	    }
} 


