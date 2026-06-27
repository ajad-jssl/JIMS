package com.JIMS.integration.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VMS_visitors;
@Repository
public interface VMS_visitorinterface
extends JpaRepository<VMS_visitors, Integer> {

//List<VMS_visitors> findByFactoryId(Integer factoryId);
	
	@Query(value =
		       "SELECT * FROM VMS_Visitors " +
		       "WHERE Factory_id = :factoryId " +
		       "AND (:search IS NULL OR :search = '' OR " +
		       "     Visitor_id  LIKE CONCAT('%', :search, '%') OR " +
		       "     visitor_name LIKE CONCAT('%', :search, '%') OR " +
		       "     Contact_no LIKE CONCAT('%', :search, '%') OR " +
		       "     Vecno LIKE CONCAT('%', :search, '%') OR " +
		       "     Purpose LIKE CONCAT('%', :search, '%')) " +
		       "ORDER BY V_id DESC",

		       countQuery =
		       "SELECT COUNT(*) FROM VMS_Visitors " +
		       "WHERE Factory_id = :factoryId " +
		       "AND (:search IS NULL OR :search = '' OR " +
		       "     Visitor_id  LIKE CONCAT('%', :search, '%') OR " +
		       "     visitor_name LIKE CONCAT('%', :search, '%') OR " +
		       "     Contact_no LIKE CONCAT('%', :search, '%') OR " +
		       "     Vecno LIKE CONCAT('%', :search, '%') OR " +
		       "     Purpose LIKE CONCAT('%', :search, '%'))",

		       nativeQuery = true)
		Page<VMS_visitors> getVisitorsByFactoryId(
		        @Param("factoryId") Integer factoryId,
		        @Param("search") String search,
		        Pageable pageable
		);

		
		
		long countByFactoryId(Integer factoryId);
}



