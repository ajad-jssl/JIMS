package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Deparment;



@Repository 
public interface Department extends JpaRepository<Deparment, Integer>{

}
