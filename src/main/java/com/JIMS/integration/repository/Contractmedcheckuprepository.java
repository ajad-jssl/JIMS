package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Contractmedcheckupmodel;

@Repository
public interface Contractmedcheckuprepository extends JpaRepository<Contractmedcheckupmodel, Integer> {
    boolean existsBymcDesc(String mcDesc);  // for checking duplicates
    
    boolean existsByMcDescIgnoreCaseAndMcidNot(String mcDesc, int mcid);
}
