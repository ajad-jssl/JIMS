package com.JIMS.integration.repository;



import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.AssetFullHistory;

@Repository
public interface AssetFullHistoryRepository extends JpaRepository<AssetFullHistory, Long> {
	 List<AssetFullHistory> findAllByOrderByTransactionDateDesc();
	
	 @Query(value = "SELECT * FROM [JIMS].[dbo].[vw_Full_Asset_Timeline5] ORDER BY [Operation_Time] ASC", 
	           nativeQuery = true)
	    List<AssetFullHistory> findAllOrderByTransactionDateAsc();
	 
	 List<AssetFullHistory> findByTransactionDateGreaterThanEqualAndTransactionDateLessThanOrderByTransactionDateAsc(
	            Date startDate, Date endDate);

	    // 2️⃣ Date >= Start
	 List<AssetFullHistory> findByTransactionDateGreaterThanEqualOrderByTransactionDateAsc(Date startDate);


	    // 3️⃣ Date <= End
	 List<AssetFullHistory> findByTransactionDateLessThanOrderByTransactionDateAsc(Date endDate);


	 
	 
}
