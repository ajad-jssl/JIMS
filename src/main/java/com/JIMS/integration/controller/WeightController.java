package com.JIMS.integration.controller;

import com.JIMS.MIS.model.WeightModel;
import com.JIMS.MIS.services.WeightServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WeightController {
 
	 @Autowired 
	    private WeightServices WeightServices;

	    @GetMapping("/getAssemblyStagesForLoadSubcon")
	    public ResponseEntity<List<WeightModel>> getAssemblyStagesForLoadSubcon(
	            @RequestParam("Load_Id") int Load_Id,
	            @RequestParam("Sup_Id") int Sup_Id) {

	        List<WeightModel> Weight = WeightServices.getAssemblyStagesForLoadSubcon(Load_Id,Sup_Id);//loadno
	        return ResponseEntity.ok(Weight);
	    }
}
