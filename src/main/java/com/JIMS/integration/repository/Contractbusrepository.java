package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Contractbusmodel;

@Repository
public interface Contractbusrepository extends JpaRepository<Contractbusmodel, Integer> {
    boolean existsBybusDesc(String busDesc);  // for checking duplicates
    
 
    boolean existsByBusDescIgnoreCaseAndBusIdNot(String busDesc, int busId);
}
