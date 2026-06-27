package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Asset_type;
import com.JIMS.integration.repository.Asset_typeinterface;




@Service
public class Asset_type_service {
	
	@Autowired
	private Asset_typeinterface asset_type_inter;
	
	public Asset_type save(Asset_type asset_type) {
		return asset_type_inter.save(asset_type);
	}
	
	public List<Asset_type> getall() {
		return  asset_type_inter.findAll();
	}
	
	public Optional<Asset_type> get(Integer type_id) {
		return asset_type_inter.findById(type_id);
	}
	
//	public boolean existsByAssetType(String assetType) {
//	    return asset_type_inter.existsByAssetType(assetType);
//	}
	
	
	public List<Asset_type> findByAssetType(String asset_type) {
	    return asset_type_inter.findByAssetType(asset_type);
	}

}
