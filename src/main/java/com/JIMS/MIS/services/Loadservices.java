package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.Loadrepository;
import com.JIMS.MIS.model.LoadModel;

@Service
public class Loadservices {
	 	
	    @Autowired
	    private Loadrepository Loadrepository;

	    public List<LoadModel> getLoadsByContractAndSupId(Integer contractId, Integer supId) {
	        return Loadrepository.findLoadsByContractAndSupId(contractId, supId); 
	    }
	    
	  
}
