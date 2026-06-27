package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Contractworklocationmodel;


@Repository
public interface Contractworklocationrepository extends JpaRepository<Contractworklocationmodel, Integer> {
    boolean existsBywlDesc(String wlDesc); 
    
    boolean existsByWlDescIgnoreCaseAndWlidNot(String wlDesc, int wlid);
}