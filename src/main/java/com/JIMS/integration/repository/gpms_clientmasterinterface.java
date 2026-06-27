package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.GPMS_clientmaster;

@Repository
public interface  gpms_clientmasterinterface extends JpaRepository<GPMS_clientmaster, Integer> {
	  GPMS_clientmaster findTopByOrderByCidDesc();
}
