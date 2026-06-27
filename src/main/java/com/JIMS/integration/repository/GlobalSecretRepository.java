package com.JIMS.integration.repository;


import com.JIMS.integration.entity.GlobalTable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlobalSecretRepository extends JpaRepository<GlobalTable, Long> {
    Optional<GlobalTable> findBySecretKey(String secretKey);  // find by key
}