package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.LoadModel;
import com.JIMS.MIS.services.Loadservices;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LoadController {
	  @Autowired
	  private Loadservices Loadservices;

	    @GetMapping("/listload") 
	    public List<LoadModel> getLoads(Integer contractId, Integer supId) {
	    return Loadservices.getLoadsByContractAndSupId(contractId, supId);
	    }		 	   
}


