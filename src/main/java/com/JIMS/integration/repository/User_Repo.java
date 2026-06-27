package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.UsersMaster;



@Repository
public interface User_Repo extends JpaRepository<UsersMaster,Integer> {

}
