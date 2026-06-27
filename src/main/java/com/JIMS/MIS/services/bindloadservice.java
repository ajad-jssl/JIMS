package com.JIMS.MIS.services;
import com.JIMS.MIS.Repository.bindloadInterfaces;
import com.JIMS.MIS.model.bindloadmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bindloadservice {
	
    private final bindloadInterfaces loadRepository;

    @Autowired
    public bindloadservice(bindloadInterfaces loadRepository) {
        this.loadRepository = loadRepository;
    }

    // Call the repository method to fetch loads with the updated model
    public List<bindloadmodel> getLoadsToLoadSubcon(int contractId, int phase, int supid) {
        return loadRepository.getLoadsToLoadSubcon(contractId, phase, supid);
    }
}
