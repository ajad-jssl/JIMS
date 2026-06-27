package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.InactiveUser;

import jakarta.transaction.Transactional;



public interface InactiveUserRepository extends JpaRepository<InactiveUser, Integer> {
	
	
	@Query("SELECT u FROM InactiveUser u WHERE u.isActive = false")
	List<InactiveUser> findInactiveUsers();
    
    
    @Query("SELECT u from InactiveUser u WHERE u.isAdminLocked = true")
    List<InactiveUser> findInactiveUserloked();
    

   
}
