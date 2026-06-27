package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.Contractlist;
import com.JIMS.MIS.services.Contractlistservice;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class Contractlistcontroller {

    @Autowired
    private Contractlistservice contractlistservice;

    @GetMapping("/listcontracts")
    public ResponseEntity<List<Contractlist>> getContractsBySupplier(@RequestParam String supplierId) {
        // Fetch contracts via the service layer
        List<Contractlist> contracts = contractlistservice.getContractsBySupplier(supplierId);
        return ResponseEntity.ok(contracts);
    }
}
