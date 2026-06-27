package com.JIMS.integration.interfaces;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Contract_gatepassmodel;
import com.JIMS.integration.repository.contract_gatepassinterface;

@Service
public class contract_gatepassservice {
	 @Autowired
	    private contract_gatepassinterface repository;


	    // Add a new item or update an existing one
	    public Contract_gatepassmodel saveOrUpdateItem(Contract_gatepassmodel item) {
	        return repository.save(item); // This will handle both insert and update automatically.
	    }

	    // Find item by Id
	    public Optional<Contract_gatepassmodel> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<Contract_gatepassmodel> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }
	    
	    public List<Contract_gatepassmodel> getItemsByFactory(Integer factoryId) {
	        return repository.getByFactoryId(factoryId);
	    }
	    public Optional<Contract_gatepassmodel> checkDuplicateEmpId(String empId) {
	        return repository.findByEmpIdIgnoreCase(empId);
	    }
	    
	    
	    public Integer getDuplicateAdhar(String adharno,Integer cg_id) {
	    	return repository.duplicateadharno(adharno,cg_id);
	    }
}
