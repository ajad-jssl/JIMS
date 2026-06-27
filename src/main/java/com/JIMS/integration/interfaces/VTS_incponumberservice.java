package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.VTS_incponumbermodel;
import com.JIMS.integration.repository.VTS_incponumberintreface;

@Service

public class VTS_incponumberservice {
	 @Autowired
	    private VTS_incponumberintreface repository;


	    // Add a new item or update an existing one
	    public VTS_incponumbermodel saveOrUpdateItem(VTS_incponumbermodel item) {
	        return repository.save(item); // This will handle both insert and update automatically.
	    }

	    // Find item by ID
	    public Optional<VTS_incponumbermodel> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<VTS_incponumbermodel> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }

		public void deleteItemById(int pono_id) {
			   repository.deleteById(pono_id);
			
		}
}
