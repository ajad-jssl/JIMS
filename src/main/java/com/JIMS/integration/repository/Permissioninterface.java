package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.UserPermissionsmodel;
@Repository

public interface Permissioninterface extends JpaRepository<UserPermissionsmodel, Integer> {
	 
}
