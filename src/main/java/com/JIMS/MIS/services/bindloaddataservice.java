package com.JIMS.MIS.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.bindloaddataInterface;
import com.JIMS.MIS.model.bindloaddatamodel;

@Service
public class bindloaddataservice {

	  private final bindloaddataInterface loadRepository;

	    @Autowired
	    public bindloaddataservice(bindloaddataInterface loadRepository) {
	        this.loadRepository = loadRepository;
	    }
	    // Call the repository method to fetch loads with the updated model
	    public List<bindloaddatamodel> bindloadtosubcon(int contractId, int phase, int supId, int loadId) {
	        return loadRepository.bindLoaddata(contractId, phase, supId, loadId);
	    }
}
