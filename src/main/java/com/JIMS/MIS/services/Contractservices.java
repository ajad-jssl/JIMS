package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.ContractRepository;
import com.JIMS.MIS.model.ContractModel; 
@Service
public class Contractservices {
	 
	@Autowired
	 private  ContractRepository Contractrepository;

	    public List<ContractModel>getContractsBySupplier(String supplierId) {
	        return Contractrepository.findContractsBySupplier(supplierId);  
	    }
	    
}
	      
	  
