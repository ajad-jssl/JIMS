package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Contractbloodgroupmodel;

@Repository
public interface Contractbloodgrouprepository extends JpaRepository<Contractbloodgroupmodel, Integer> {
    boolean existsByblg(String blg);  // for checking duplicates
    boolean existsByBlgIgnoreCaseAndBlIdNot(String blg, int blId);
}
