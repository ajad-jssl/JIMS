package com.JIMS.integration.interfaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.GPMSreturnableitems;
import com.JIMS.integration.repository.gpmsreturnableitemsinterface;

import java.util.List;
import java.util.Optional;
@Service
public class gpms_returnableitemsservice {
	
	 @Autowired
	    private gpmsreturnableitemsinterface repository;

	    // Add a new item or update an existing one
	    public GPMSreturnableitems saveOrUpdateItem(GPMSreturnableitems item) {
	        return repository.save(item); // This will handle both insert and update automatically.
	    }

	    // Find item by ID
	    public Optional<GPMSreturnableitems> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<GPMSreturnableitems> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }

		public void deleteItemById(int id) {
		     repository.deleteById(id); // This will delete the item by its ID
	    }
}
