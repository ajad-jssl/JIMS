package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.NatureOfJobModel;

@Repository
public interface NatureOfJobRepository extends JpaRepository<NatureOfJobModel, Integer> {
    boolean existsByNojDesc(String nojDesc); 
    boolean existsByNojDescIgnoreCaseAndNojIdNot(String nojDesc, int nojId);// for checking duplicates
}
