package com.JIMS.integration.interfaces;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.usermastermodel;
import com.JIMS.integration.repository.usermasterinterface;
@Service

public class usermasterservice {

	 @Autowired
	    private usermasterinterface repository;


	    // Add a new item or update an existing one
	    public usermastermodel saveOrUpdateItem(usermastermodel item) {
	        return repository.save(item); // This will handle both insert and update automatically.
	    }

	    // Find item by ID
	    public Optional<usermastermodel> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<usermastermodel> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }
}