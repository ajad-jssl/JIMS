package com.JIMS.integration.interfaces;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Deparment;
import com.JIMS.integration.repository.Department;


@Service
public class Department_Service  {

	@Autowired
private	Department department_repo;


	
	public List<Deparment> getAlldepartment(){
		return department_repo.findAll();
	}

}
