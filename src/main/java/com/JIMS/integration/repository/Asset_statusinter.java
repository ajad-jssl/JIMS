package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Asset_Status;



@Repository
public interface Asset_statusinter extends  JpaRepository<Asset_Status, Integer> {
	
	 List<Asset_Status> findByAssetStatus(String assetModel);

}
