package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.webSmart_Location;

@Repository
public interface Location_Repo extends JpaRepository<webSmart_Location,Integer> {

}
