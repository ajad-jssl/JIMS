package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Employee_Assign;



@Repository
public interface Employee_Assign_Repo extends JpaRepository<Employee_Assign, Integer>{

}
