package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Asset_Entry;




@Repository
public interface Asset_Entryinter extends JpaRepository<Asset_Entry, Integer> {
	
	boolean existsByTypeId(Integer typeId);
	
	boolean existsByModelId(Integer modelId);
	
	boolean existsByStatusId(Integer statusId);
	
	boolean existsByAssertorId(Integer ownerId);
	
	boolean existsByMakeId(Integer makeId);
	
	boolean existsByAssetSrNoIgnoreCase(String assetSrNo);
	
	
	Optional<Asset_Entry> findByAssetSrNoIgnoreCase(String assetSrNo);
	
	List<Asset_Entry> findAllByOrderByAssetenIdDesc();
	
	
	@Query(value = """
			SELECT * 
			FROM Er_asseten A
			WHERE NOT EXISTS (
			    SELECT 1 
			    FROM Er_Allocation B
			    WHERE B.Asseten_id = A.Asseten_id
			      AND B.Allocatedrtn_date IS NULL
			)
		""", nativeQuery = true)
		List<Asset_Entry> getAvailableAssets();
		
		
	    @Query(value = """
	            SELECT TOP (2)
	                e.Type_id AS typeId,
	                CASE WHEN e.Type_id = 1 THEN 'AM' ELSE 'AT' END AS assetType,

	                COUNT(e.Asset_no) AS totalAsset,
	                COUNT(er.Allocation_id) AS allocated,
	                COUNT(e.Asset_no) - COUNT(er.Allocation_id) AS stock,

	                SUM(CASE WHEN e.Status_id = 1 THEN 1 ELSE 0 END) AS working,
	                SUM(CASE WHEN e.Status_id = 2 THEN 1 ELSE 0 END) AS notWorking,
	                SUM(CASE WHEN e.Status_id = 3 THEN 1 ELSE 0 END) AS scrap,
	                SUM(CASE WHEN e.Status_id = 4 THEN 1 ELSE 0 END) AS repair,
	                SUM(CASE WHEN e.Status_id > 4 THEN 1 ELSE 0 END) AS others,
	                COUNT(e.Status_id) AS totalStatus

	            FROM Er_asseten e 
	            LEFT JOIN vw_Allocationdate er 
	                ON er.Asseten_id = e.Asseten_id

	            GROUP BY e.Type_id
	            """,
	            nativeQuery = true)
	        List<Object[]> getAssetSummary();		
		
		
}
