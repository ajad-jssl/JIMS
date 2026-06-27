package com.JIMS.integration.interfaces;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.modulemodel;
import com.JIMS.integration.repository.moduleinterface;
@Service

public class moduleservice {

	 @Autowired
	    private moduleinterface repository;


	    // Add a new item or update an existing one
	    public modulemodel saveOrUpdateItem(modulemodel item) {
	        return repository.save(item); // This will handle both insert and update automatically.
	    }

	    // Find item by ID
	    public Optional<modulemodel> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<modulemodel> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }
}