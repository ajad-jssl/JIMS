package com.JIMS.integration.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VTS_invoiceexitmodel;


@Repository
public interface VTS_invoiceexitinterface extends JpaRepository<VTS_invoiceexitmodel, Integer> {
	@Query(value = "SELECT * FROM VTS_invoice_exit WHERE factory_id = :factoryId ORDER BY ine_id DESC", 
	           nativeQuery = true)
	    Page<VTS_invoiceexitmodel> getAllItems(@Param("factoryId") String factoryId, Pageable pageable);
	  
	    @Query(value = "SELECT * FROM VTS_invoice_exit " +
                "WHERE factory_id = :factoryId " +
                "AND (vecno LIKE CONCAT('%', :search, '%') " +
                "OR dname LIKE CONCAT('%', :search, '%') " +
                "OR dmbno LIKE CONCAT('%', :search, '%') " +
                "OR dl LIKE CONCAT('%', :search, '%') " +
                "OR helper LIKE CONCAT('%', :search, '%') " +
                "OR inremarks LIKE CONCAT('%', :search, '%')) " +
                "ORDER BY ine_id DESC",
        nativeQuery = true)
 Page<VTS_invoiceexitmodel> searchItems(@Param("factoryId") String factoryId, 
                                        @Param("search") String search, 
                                        Pageable pageable);
	 
	   
	   
	 
	   
	/*
	 * @Query(value =
	 * "SELECT * FROM VTS_invoice_exit WHERE vechile_id = :vehicleId AND factory_id = :factoryId"
	 * , nativeQuery = true) VTS_invoiceexitmodel
	 * findByVehicleId(@Param("vehicleId") String vehicleId, @Param("factoryId")
	 * String factoryId);
	 */
}
