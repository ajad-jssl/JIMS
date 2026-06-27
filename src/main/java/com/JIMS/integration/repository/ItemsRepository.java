package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.ItemsModel;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<ItemsModel, Integer> { 

	// Fetch all records from Register_Type
    @Query(value = "SELECT unit ,uid FROM GPMS_UNITMASTER", nativeQuery = true)   
    List<ItemsModel> findunit();   
}
