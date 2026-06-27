package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VTS_Regtypemodel;

@Repository
public interface VTS_Regtypeinterface extends JpaRepository<VTS_Regtypemodel, Integer> {
	
}
