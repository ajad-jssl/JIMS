package com.JIMS.integration.repository;
 


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Asset_type;



@Repository
public interface Asset_typeinterface extends JpaRepository<Asset_type, Integer>{

	
	List<Asset_type> findByAssetType(String asset_type);

	
}
