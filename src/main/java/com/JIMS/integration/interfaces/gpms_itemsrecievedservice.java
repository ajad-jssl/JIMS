package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.gpms_itemsrecieved;
import com.JIMS.integration.repository.gpms_itemsrecievedeinterface;

@Service
public class gpms_itemsrecievedservice {

    @Autowired
    private gpms_itemsrecievedeinterface repository;

    // Add a new item or update an existing one
    public gpms_itemsrecieved saveOrUpdateItem(gpms_itemsrecieved item) {
        return repository.save(item); // This will handle both insert and update automatically.
    }

    // Find item by ID
    public Optional<gpms_itemsrecieved> getItemById(int itemId) {
        return repository.findById(itemId);
    }
    
    public List<gpms_itemsrecieved> getAllItems() {
        return repository.findAll(); // Fetches all items from the database
    }
}
