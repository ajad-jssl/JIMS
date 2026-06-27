package com.JIMS.integration.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.Lotnumber;
import com.JIMS.MIS.services.Lotnumberservice;

@RestController
@RequestMapping("/api")
public class Lotnumbercontroller {
	@Autowired
	private Lotnumberservice lotnumberservice;
	
	@GetMapping("/listloads")
	public List<Lotnumber> getLoads(Integer contractId, Integer supId) {
	{
	return lotnumberservice.getLoadsByContractAndSupId(contractId, supId);
	}
	}
}
