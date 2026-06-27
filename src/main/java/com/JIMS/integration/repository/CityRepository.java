package com.JIMS.integration.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

	List<City> findByStateId(int stateId);
    // You get all methods like findAll(), findById(), save(), deleteById() etc.
	
	Page<City> findByCityNameContainingIgnoreCase(String cityName, Pageable pageable);
}


