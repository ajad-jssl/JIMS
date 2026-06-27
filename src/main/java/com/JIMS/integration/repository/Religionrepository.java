package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Religionmodel;

@Repository
public interface Religionrepository extends JpaRepository<Religionmodel, Integer> {
    boolean existsByreligionDesc(String religionDesc);  // for checking duplicates
    
    boolean existsByReligionDescIgnoreCaseAndReliIdNot(String religionDesc, int reliId);
}