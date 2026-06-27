package com.JIMS.integration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.InvoiceExit;
import com.JIMS.MIS.services.InvoiceExitService;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class InvoiceExitController {

    @Autowired
    private InvoiceExitService InvoiceExitService;

    @GetMapping("/listvehicles")
    public ResponseEntity<List<InvoiceExit>> getvehiclebyvehicleid(@RequestParam String vechileId) {
        // Fetch contracts via the service layer
        List<InvoiceExit> vehicles = InvoiceExitService.getvehiclebyvehicleid(vechileId);
        return ResponseEntity.ok(vehicles);
    }
}
