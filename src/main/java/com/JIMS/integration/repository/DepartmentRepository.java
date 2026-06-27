package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // No custom query needed for simple GET all
}
