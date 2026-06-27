package com.JIMS.integration.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VMS_companymodel;
@Repository
public interface VMS_companyinterface extends JpaRepository<VMS_companymodel, Integer> {

    @Query("SELECT c FROM VMS_companymodel c WHERE LOWER(c.Company_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<VMS_companymodel> searchByCompanyName(@Param("name") String name, Pageable pageable);
	
}
