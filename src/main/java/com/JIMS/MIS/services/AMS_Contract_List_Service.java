package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.AMS_Contract_List_Repo;
import com.JIMS.MIS.model.AMS_Contracts;


@Service
public class AMS_Contract_List_Service {
	
	@Autowired
private AMS_Contract_List_Repo contract_list_repo;	
	
public List<AMS_Contracts> getAllContracts(){
	return contract_list_repo.findAll();
}
	
	
}
