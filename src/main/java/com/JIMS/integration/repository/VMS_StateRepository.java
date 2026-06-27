package com.JIMS.integration.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VMS_statemastermodel;

@Repository
public interface VMS_StateRepository extends JpaRepository<VMS_statemastermodel, Integer> {

	 @Query("SELECT s FROM VMS_statemastermodel s WHERE LOWER(s.State_name) LIKE LOWER(CONCAT('%', :name, '%'))")
	    Page<VMS_statemastermodel> searchByStateName(@Param("name") String name, Pageable pageable);
	    

}



