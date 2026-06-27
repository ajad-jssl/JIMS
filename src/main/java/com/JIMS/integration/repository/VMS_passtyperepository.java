package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VMS_companymodel;
import com.JIMS.integration.entity.VMS_passtypemodel;

@Repository
public interface VMS_passtyperepository extends JpaRepository<VMS_passtypemodel, Integer> {
    
}


