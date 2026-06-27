package com.JIMS.MIS.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.ContractRepository;
import com.JIMS.MIS.model.ContractModel;
@Service
public class contractservice {
	 
	@Autowired
	 private  ContractRepository contractRepository ;

	    public List<ContractModel>  getContractsBySupplier(String supplierId) {
	        return contractRepository.findContractsBySupplier(supplierId);
	    }
	    
	
}
