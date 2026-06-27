package com.JIMS.integration.interfaces;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Employee_Assign;
import com.JIMS.integration.repository.Employee_Assign_Repo;


@Service
public class Employee_Assign_Service {
	
	
	@Autowired
	private Employee_Assign_Repo employee_assign_repo;
	
	
	public Employee_Assign insertIntoDat(Employee_Assign employee_assign) {
		return employee_assign_repo.save(employee_assign);
	}
	
	public List<Employee_Assign> getAllAssign(){
		return employee_assign_repo.findAll();
	}

	public void deletetAssign(Integer ass_id) {
	 employee_assign_repo.deleteById(ass_id);
	}
	
	
}
