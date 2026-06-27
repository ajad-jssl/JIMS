package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.modulemodel;


@Repository
public interface moduleinterface extends JpaRepository<modulemodel, Integer> {
	
}
