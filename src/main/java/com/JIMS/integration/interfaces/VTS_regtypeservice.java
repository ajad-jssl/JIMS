package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.VTS_Regtypemodel;
import com.JIMS.integration.repository.VTS_Regtypeinterface;

@Service
public class VTS_regtypeservice {

	 @Autowired
	    private VTS_Regtypeinterface repository;


	    // Add a new item or update an existing one
	    public VTS_Regtypemodel saveOrUpdateItem(VTS_Regtypemodel item) {
	        return repository.save(item); // This will handle both insert and update automatically.
	    }

	    // Find item by ID
	    public Optional<VTS_Regtypemodel> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<VTS_Regtypemodel> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }
}
