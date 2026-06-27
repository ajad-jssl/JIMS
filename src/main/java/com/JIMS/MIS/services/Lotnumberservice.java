package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.LotnumberInterfaces;
import com.JIMS.MIS.model.Lotnumber;
@Service
public class Lotnumberservice {

	@Autowired
	private LotnumberInterfaces lotnumberrepository;
	
	public List<Lotnumber>  getLoadsByContractAndSupId(Integer contractId, Integer supId) {
	{
		return lotnumberrepository.findLoadsByContractAndSupId(contractId, supId);
	}
	}
}
