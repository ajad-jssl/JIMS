package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Asset_Maker;



@Repository
public interface Asset_Makerinter  extends JpaRepository<Asset_Maker, Integer>{

	List<Asset_Maker> findByMakeDescription(String asset_maker);
	
}
