package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Asset_Model;




@Repository
public interface Asset_modelinterface  extends JpaRepository<Asset_Model, Integer>{

	
	boolean existsByAssetModel(String assetModel);
	
	 List<Asset_Model> findByAssetModel(String assetModel);
}
