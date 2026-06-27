package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Educationmodel;

@Repository
public interface Educationrepository extends JpaRepository<Educationmodel, Integer> {
    boolean existsByeduDesc(String eduDesc);  // for checking duplicates
    
    

    boolean existsByEduDescIgnoreCaseAndEduIdNot(String eduDesc, int eduId);
}