package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.GPMS_returnable;
import com.JIMS.integration.repository.gpms_returnableinterface;

@Service
public class gpmsreturnableservice {
	

    @Autowired
    private gpms_returnableinterface repository;

    // Method to fetch records where GP_type = '1' and security = '1'
    public List<GPMS_returnable> getReturnablesByGpTypeAndSecurityAndFactoryId(Integer factoryId) {
        return repository.findByGpTypeAndSecurityAndFactoryId(factoryId);
    }
    // Add a new item or update an existing one
    public GPMS_returnable saveOrUpdateItem(GPMS_returnable item) {
        return repository.save(item); // This will handle both insert and update automatically.
    }

    // Find item by ID
    public Optional<GPMS_returnable> getItemById(int itemId) {
        return repository.findById(itemId);
    }
    
    public List<GPMS_returnable> getAllItems() {
        return repository.findAll(); // Fetches all items from the database
    }

}

