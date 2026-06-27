package com.JIMS.integration.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.ContractModel;
import com.JIMS.MIS.services.Contractservices;
 @CrossOrigin
@RestController
@RequestMapping("/api")
public class ContractController {
	@Autowired
    private  Contractservices Contractservices;
    @GetMapping("/contractlists")
   
   ResponseEntity<List<ContractModel>> getContractsBySupplier(@RequestParam String supplierId) {
            // Fetch contracts via the service layer
            List<ContractModel> contracts = Contractservices.getContractsBySupplier(supplierId);
            return ResponseEntity.ok(contracts);
    }
}


