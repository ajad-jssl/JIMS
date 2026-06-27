package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.tra_rates;
import com.JIMS.MIS.services.tra_ratesservice;
@CrossOrigin
@RestController
@RequestMapping("/api/trarates")
public class tra_ratescontroller {
	
   
    private final tra_ratesservice traRatesService;
    
    @Autowired
    public tra_ratescontroller(tra_ratesservice traRatesService) {
        this.traRatesService = traRatesService;
    }
    

    // Get all TraRates
    @GetMapping
    public List<tra_rates> getAllTraRates() {
        return traRatesService.getAllTraRates();
    }
}
