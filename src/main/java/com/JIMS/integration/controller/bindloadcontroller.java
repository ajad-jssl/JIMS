package com.JIMS.integration.controller;

import com.JIMS.MIS.model.bindloadmodel;
import com.JIMS.MIS.services.bindloadservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping("/api")
@RestController
@CrossOrigin
public class bindloadcontroller {

    private final bindloadservice loadService;

    @Autowired
    public bindloadcontroller(bindloadservice loadService) {
        this.loadService = loadService;
    }

    // Endpoint to call the stored procedure with parameters
    @GetMapping("/loads")
    public List<bindloadmodel> getLoads(@RequestParam("contractId") int contractId, 
                               @RequestParam("phase") int phase, 
                               @RequestParam("supid") int supid) {
        return loadService.getLoadsToLoadSubcon(contractId, phase,supid);
    }
}
