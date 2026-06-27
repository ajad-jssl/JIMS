package com.JIMS.MIS.services;

import com.JIMS.MIS.Repository.DeliverySitesInterfaces;
import com.JIMS.MIS.model.DeliverySites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliverySitesservice {

    private final DeliverySitesInterfaces deliverySitesRepository;

    @Autowired
    public DeliverySitesservice(DeliverySitesInterfaces deliverySitesRepository) {
        this.deliverySitesRepository = deliverySitesRepository;
    }

    public List<DeliverySites> getAllDeliverySites() {
        return deliverySitesRepository.findAll(); // Returns a list of all delivery sites
    }
}
