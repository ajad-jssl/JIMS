package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Pagesmodel;
@Repository
public interface pageinterface extends JpaRepository<Pagesmodel, Integer> {
	  @Query("SELECT p FROM Pagesmodel p WHERE p.ModuleID = ?1")
	    List<Pagesmodel> findByModuleId(int ModuleID);
	}
