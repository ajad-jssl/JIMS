package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Asset_Model;
import com.JIMS.integration.repository.Asset_modelinterface;


@Service
public class Asset_Model_Service {
	
	
	
	@Autowired
private Asset_modelinterface asset_model_inter;
	
	
	
//	Method to Fetch the All Model
	public List<Asset_Model> getAllModel() {
		return asset_model_inter.findAll();
	}
	

//	Method to Fetch the Single Model
	
	public  Optional<Asset_Model> getOneModel(int Mid) {
		return asset_model_inter.findById(Mid);
	}
	
	
	
	
//	Method to Insert the new Model in DataBase 
	
	public Asset_Model saveIntoModel(Asset_Model asset_model) {
		return asset_model_inter.save(asset_model);
	}
	
	
	
//	This Method for Checking the Duplicates ,Before Inserting the data into DataBase
	
	
//	public boolean exitByAssetModel(String asset_model) {
//		return asset_model_inter.existsByAssetModel(asset_model);
//	}

	
	
	public List<Asset_Model> findByAssetModel(String assetModel) {
	    return asset_model_inter.findByAssetModel(assetModel);
	}
}
