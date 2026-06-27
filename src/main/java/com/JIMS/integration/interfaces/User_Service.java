package com.JIMS.integration.interfaces;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.UsersMaster;
import com.JIMS.integration.repository.User_Repo;


@Service
public class User_Service {
	
	@Autowired
	private User_Repo user_repo;
	
	public List<UsersMaster> getAllUser(){
		return user_repo.findAll();
	}

}
