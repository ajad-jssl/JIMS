package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.VTS_vtypemodel;
import com.JIMS.integration.repository.VTS_vtypeinterface;

@Service
public class VTS_vtypeservice {
	 @Autowired
	    private VTS_vtypeinterface repository;


	    // Add a new item or update an existing one
	    public VTS_vtypemodel saveOrUpdateItem(VTS_vtypemodel item) {
	        return repository.save(item); // This will handle both insert and update automatically.
	    }

	    // Find item by ID
	    public Optional<VTS_vtypemodel> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<VTS_vtypemodel> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }
}
