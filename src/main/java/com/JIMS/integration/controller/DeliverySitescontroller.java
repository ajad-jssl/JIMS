package com.JIMS.integration.controller;


import com.JIMS.MIS.model.DeliverySites;
import com.JIMS.MIS.services.DeliverySitesservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/deliverysites")
public class DeliverySitescontroller {
	
    private final DeliverySitesservice deliverySitesService;

    @Autowired
    public DeliverySitescontroller(DeliverySitesservice deliverySitesService) {
        this.deliverySitesService = deliverySitesService;
    }

    @GetMapping
    public List<DeliverySites> getAllDeliverySites() {
        return deliverySitesService.getAllDeliverySites(); // Returns list of all delivery sites
    }
}
