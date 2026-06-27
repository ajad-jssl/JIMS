package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Asset_OwnedBy;
import com.JIMS.integration.repository.Asset_ownedBy;



@Service
public class Asset_OwnedBy_Service {
	
	
	
	 @Autowired
	    private Asset_ownedBy assetOwnedByRepository;


	    // ➤ INSERT (Save or Update)
	    public Asset_OwnedBy insertOwnedBy(Asset_OwnedBy assetOwnedBy) {
	        return assetOwnedByRepository.save(assetOwnedBy);
	    }


	    // ➤ GET ONE by ID
	    public Optional<Asset_OwnedBy> getOneOwnedBy(Integer ob_id) {
	        return assetOwnedByRepository.findById(ob_id);
	    }


	    // ➤ GET ALL
	    public List<Asset_OwnedBy> getAllOwnedBy() {
	        return assetOwnedByRepository.findAll();
	    }


	    // ➤ CHECK DUPLICATE Based on Description
	   
	    
	    
	    public List<Asset_OwnedBy> findByOwnedByDescription(String ob_description) {
	        return assetOwnedByRepository.findByOwnedByDescription(ob_description);
	    }

}
