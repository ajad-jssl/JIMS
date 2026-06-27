package com.JIMS.integration.interfaces;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.UserPermissionsmodel;
import com.JIMS.integration.repository.UserPermissionsinterface;
@Service
public class UserPermissionsservice {
	 @Autowired
	    private UserPermissionsinterface repository;


	    // Add a new item or update an existing one
	    public UserPermissionsmodel saveOrUpdateItem(UserPermissionsmodel permissions) {
	        return repository.save(permissions); // This will handle both insert and update automatically.
	    }

		/*
		 * // Find item by ID public Optional<UserPermissionsmodel> getItemById(int
		 * itemId) { return repository.findById(itemId); }
		 */
	    
	    public List<UserPermissionsmodel> getAllItems() {
	        return repository.findAll(); // Fetches all items from the database
	    }
}