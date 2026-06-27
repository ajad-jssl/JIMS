package com.JIMS.integration.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Asset_Status;
import com.JIMS.integration.repository.Asset_statusinter;

import java.util.*;
@Service
public class Asset_Status_Service {
	
	
	@Autowired
private Asset_statusinter asset_status_inter;
	
public Asset_Status insetIntoAssetStaus(Asset_Status asset_status) {
	return asset_status_inter.save(asset_status);
}


public Optional<Asset_Status> getOneStatus(Integer st_id) {
	return asset_status_inter.findById(st_id);
}



public List<Asset_Status> getAllStatus() {
	return asset_status_inter.findAll();
}



public List<Asset_Status> findByAssetStatus(String assetModel) {
    return asset_status_inter.findByAssetStatus(assetModel);
}



}
