package com.JIMS.integration.interfaces;
import com.JIMS.integration.entity.GPMS_itemmaster;
import com.JIMS.integration.repository.GPMS_itemmasterinterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class gpms_itemmasterservice {

    @Autowired
    private GPMS_itemmasterinterface repository;

    // Add a new item or update an existing one
    public GPMS_itemmaster saveOrUpdateItem(GPMS_itemmaster item) {
        return repository.save(item); // This will handle both insert and update automatically.
    }

    // Find item by ID
    public Optional<GPMS_itemmaster> getItemById(int itemId) {
        return repository.findById(itemId);
    }
    
    public List<GPMS_itemmaster> getAllItems() {
        return repository.findAll(); // Fetches all items from the database
    }
}
