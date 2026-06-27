package com.JIMS.MIS.services;


import com.JIMS.MIS.Repository.HauilerInterfaces;
import com.JIMS.MIS.model.Hauilermodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Hauilerservice {

    private final HauilerInterfaces haulierRepository;

    @Autowired
    public Hauilerservice(HauilerInterfaces haulierRepository) {
        this.haulierRepository = haulierRepository;
    }

    // Get all Hauliers
    public List<Hauilermodel> getAllHauliers() {
        return haulierRepository.findAll();
    }

    // Get Haulier by ID
    public Optional<Hauilermodel> getHaulierById(Integer id) {
        return haulierRepository.findById(id);
    }

    // Save a Haulier
    public Hauilermodel saveHaulier(Hauilermodel haulier) {
        return haulierRepository.save(haulier);
    }

    // Delete Haulier by ID
    public void deleteHaulier(Integer id) {
        haulierRepository.deleteById(id);
    }
}
