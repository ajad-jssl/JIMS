package com.JIMS.integration.controller;


import com.JIMS.MIS.model.Hauilermodel;
import com.JIMS.MIS.services.Hauilerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin
@RestController
@RequestMapping("/api/hauliers")
public class Hauliercontroller {

    private final Hauilerservice haulierService;

    @Autowired
    public Hauliercontroller(Hauilerservice haulierService) {
        this.haulierService = haulierService;
    }

    // Get all hauliers
    @GetMapping
    public ResponseEntity<List<Hauilermodel>> getAllHauliers() {
        List<Hauilermodel> hauliers = haulierService.getAllHauliers();
        return new ResponseEntity<>(hauliers, HttpStatus.OK);
    }

    // Get haulier by ID
    @GetMapping("/{id}")
    public ResponseEntity<Hauilermodel> getHaulierById(@PathVariable Integer id) {
        Optional<Hauilermodel> haulier = haulierService.getHaulierById(id);
        return haulier.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Save a haulier
    @PostMapping
    public ResponseEntity<Hauilermodel> createHaulier(@RequestBody Hauilermodel haulier) {
        Hauilermodel savedHaulier = haulierService.saveHaulier(haulier);
        return new ResponseEntity<>(savedHaulier, HttpStatus.CREATED);
    }

    // Delete a haulier by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHaulier(@PathVariable Integer id) {
        haulierService.deleteHaulier(id);
        return ResponseEntity.noContent().build();
    }
}

