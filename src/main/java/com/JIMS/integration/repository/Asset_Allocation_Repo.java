package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.ErAllocation;




@Repository
public interface Asset_Allocation_Repo extends JpaRepository<ErAllocation, Integer>{

	@Query("FROM ErAllocation e WHERE e.contractId = :contractId")
	List<ErAllocation> findByContractId(@Param("contractId") Integer contractId);

	
	@Query("FROM ErAllocation WHERE allocatedTo = :allocatedTo")
	List<ErAllocation> findByAllocatedTo(@Param("allocatedTo") Integer allocatedTo);
	
	

	@Query("FROM ErAllocation WHERE assetId = :assetId")
	List<ErAllocation> findByAssetId(@Param("assetId") Integer assetId);
	
	@Query("SELECT a FROM ErAllocation a WHERE a.allocatedReturnDate IS NULL")
	List<ErAllocation> getActiveAllocation();
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END " +
            "FROM Er_Allocation " +
            "WHERE Asseten_id = :id AND Allocatedrtn_date IS NULL",
    nativeQuery = true)
boolean existsAllocated(@Param("id") Integer id);

	}



