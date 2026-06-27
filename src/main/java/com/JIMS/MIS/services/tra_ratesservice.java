package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.tra_ratesInterfaces;
import com.JIMS.MIS.model.tra_rates;

@Service

public class tra_ratesservice {
	
  
    private final  tra_ratesInterfaces traRatesRepository;

    @Autowired
    public tra_ratesservice(tra_ratesInterfaces traRatesRepository) {
        this.traRatesRepository = traRatesRepository;
    }
    
    // Get all TraRates
    public List<tra_rates> getAllTraRates() {
        return traRatesRepository.findAll();
    }
}
