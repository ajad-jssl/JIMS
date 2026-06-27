package com.JIMS.integration.controller;

import com.JIMS.MIS.model.bindphasemodel;
import com.JIMS.MIS.services.bindphaseservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class bindphasecontroller {
    @Autowired
    private bindphaseservice contractZoneService;

    @GetMapping("/contractzones")
    public List<bindphasemodel> getContractZones(@RequestParam int contractId, @RequestParam Long supplierId) {
        return contractZoneService.getContractZonesByContractIdAndSupplierId(contractId, supplierId);
    }
}
