package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.TargetMonthlyDetails;

@Repository
public interface TargetMonthlyDetailsRepository
        extends JpaRepository<TargetMonthlyDetails, Integer> {

    List<TargetMonthlyDetails> findByTargetId(Integer targetId);

    Optional<TargetMonthlyDetails> findByTargetIdAndMonthNo(
            Integer targetId,
            Integer monthNo);
}