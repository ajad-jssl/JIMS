package com.JIMS.integration.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.bindloaddatamodel;
import com.JIMS.MIS.services.bindloaddataservice;
@RequestMapping("/api")
@RestController
@CrossOrigin
public class bindloaddatacontoller {

    private final bindloaddataservice loadService;

    @Autowired
    public bindloaddatacontoller(bindloaddataservice loadService) {
        this.loadService = loadService;
    }

    // Endpoint to call the stored procedure with parameters
    @GetMapping("/loaddata")
    public List<bindloaddatamodel> getLoaddata(@RequestParam("contractId") int contractId, 
    		 @RequestParam("phase") int phase,
             @RequestParam("supid") int supId,
             @RequestParam("loadid") int loadId) {
        return loadService.bindloadtosubcon(contractId, phase,supId,loadId);
    }
}
