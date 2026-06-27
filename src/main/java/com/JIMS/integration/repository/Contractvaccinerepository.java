package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Contractvaccinemodel;

@Repository
public interface Contractvaccinerepository extends JpaRepository<Contractvaccinemodel, Integer> {
    boolean existsByvaccineDesc(String vaccineDesc); // case-sensitive match to the field
    
    boolean existsByVaccineDescIgnoreCaseAndVacidNot(String vaccineDesc, int vacid);
    
    
}

