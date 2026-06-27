package com.JIMS.integration.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.Tra_Loads;
import com.JIMS.MIS.services.Tra_Loadsservice;
@CrossOrigin
@RestController
@RequestMapping("api/Tra_Loads")
public class Tra_Loadscontroller {
	
	private static final Logger logger = LogManager.getLogger(Tra_Loadscontroller.class);
	private final Tra_Loadsservice tra_loadsservice;
	
	public Tra_Loadscontroller(Tra_Loadsservice tra_loadsservice) { 
		super();
		this.tra_loadsservice=tra_loadsservice;
	}
	
	@GetMapping("/listTra_Loads")
	public ResponseEntity<List<Tra_Loads>> findbycontractId(@RequestParam Integer contractId){
		logger.info("In listtra_loads()....");
		List<Tra_Loads> contracts =tra_loadsservice.findbycontractId(contractId);
		return ResponseEntity.ok(contracts);
	}
	

//	@PostMapping("/addTra_Loads")
//	public Map<String,Object> addtra_loads(@RequestBody Tra_Loads tra_loads)
//	{
//		logger.info("In addTra_Loads()....");
//		return tra_loadsservice.addtra_loads(tra_loads);
//	}
	
	@PostMapping("/addTra_Loads")
	public Map<String, Object> addTraLoads(
	        @RequestBody Tra_Loads traLoads,
	        @RequestParam("factory_id") int factoryId) {

	    return tra_loadsservice.addTraLoadsFactoryWise(traLoads, factoryId);
	}
	
	@PostMapping("/updateTra_Loads")
	public Map<String,Object> updatetra_loads(@RequestBody Tra_Loads tra_loads) {
		logger.info("In updatetra_loads()....");
		return tra_loadsservice.updatetra_loads(tra_loads);
	}

}
