package com.JIMS.integration.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.usermastermodel;
@Repository

public interface usermasterinterface extends JpaRepository<usermastermodel, Integer> {
	
	
	@Query(value = "SELECT * FROM USERS_MASTER WHERE is_delete = 0", nativeQuery = true)
	List<usermastermodel> getActiveUsers();
	
	
	
	
	
}
