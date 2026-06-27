package com.JIMS.integration.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.Tra_LoadRuns;
import com.JIMS.MIS.services.Tra_LoadRunsservice;
@CrossOrigin
@RestController
@RequestMapping("api/Tra_LoadRuns")
public class Tra_LoadRunscontroller {

	private static final Logger logger = LogManager.getLogger(Tra_LoadRunscontroller.class);
	private final Tra_LoadRunsservice tra_loadrunsservice;
	
	public Tra_LoadRunscontroller(Tra_LoadRunsservice tra_loadrunsservice) { 
		super();
		this.tra_loadrunsservice=tra_loadrunsservice;
	}
	
	@GetMapping("/listTra_LoadRuns")
	private Map<String,Object> listtra_loadruns(){
		logger.info("In listtra_loadruns()....");
		return tra_loadrunsservice.listtra_loadruns();
	}
	

//	@PostMapping("/addTra_LoadRuns")
//	public Map<String,Object> addtra_loadruns(@RequestBody Tra_LoadRuns tra_LoadRuns)
//	{
//		logger.info("In addTra_Loadruns()....");
//		return tra_loadrunsservice.addtra_loadruns(tra_LoadRuns);
//	}
	
	@PostMapping("/addTra_LoadRuns")
	public Map<String, Object> addTraLoadRuns(
	        @RequestBody Tra_LoadRuns traLoadRuns,
	        @RequestParam("factory_id") int factoryId) {

	    return tra_loadrunsservice.addTraLoadRunsFactoryWise(traLoadRuns, factoryId);
	}

	
	@PostMapping("/updateTra_LoadRuns")
	public Map<String,Object> updatetra_loadruns(@RequestBody Tra_LoadRuns tra_LoadRuns) {
		logger.info("In updatetra_loadruns()....");
		return tra_loadrunsservice.updatetra_loadruns(tra_LoadRuns);
	}
	
	@DeleteMapping("/delTra_LoadRuns/{run_id}")
//	public Map<String,Object >deltra_loadruns(@RequestBody Tra_LoadRuns tra_LoadRuns ){
	private void deltra_loadruns(@PathVariable("run_id") int run_id) {
		logger.info("In deltra_loadruns....");
		tra_loadrunsservice.deletetra_loadruns(run_id);
	}
}
