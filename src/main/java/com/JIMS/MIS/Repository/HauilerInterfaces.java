package com.JIMS.MIS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.Hauilermodel;

@Repository
public interface HauilerInterfaces extends JpaRepository<Hauilermodel, Integer> {
	@Query(value = " select haulier_id, rtrim(company) as company from hauliers where deleted=0 order by company ",nativeQuery = true)
	List<Hauilermodel> findHauliersWithCompany(); 
}
