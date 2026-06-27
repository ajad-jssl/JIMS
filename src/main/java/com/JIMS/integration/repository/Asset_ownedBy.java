package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Asset_OwnedBy;




@Repository
public interface Asset_ownedBy extends JpaRepository<Asset_OwnedBy, Integer> {

	
	List<Asset_OwnedBy> findByOwnedByDescription(String ownedByDescription);
	
}
