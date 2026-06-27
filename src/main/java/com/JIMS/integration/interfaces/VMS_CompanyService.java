package com.JIMS.integration.interfaces;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.VMS_companymodel;
import com.JIMS.integration.repository.VMS_companyinterface;
@Service
public class VMS_CompanyService {

    @Autowired
    private VMS_companyinterface repository;


    // Add a new item or update an existing one
    public VMS_companymodel saveOrUpdateItem(VMS_companymodel item) {
        return repository.save(item); // This will handle both insert and update automatically.
    }

    // Find item by ID
    public Optional<VMS_companymodel> getItemById(int itemId) {
        return repository.findById(itemId);
    }
    
    public List<VMS_companymodel> getAllItems() {
        return repository.findAll(); // Fetches all items from the database
    }
    
    
    public Page<VMS_companymodel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<VMS_companymodel> searchByCompanyName(String name, Pageable pageable) {
        return repository.searchByCompanyName(name, pageable);
    }

}
