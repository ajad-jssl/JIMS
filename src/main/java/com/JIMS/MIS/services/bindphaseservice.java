package com.JIMS.MIS.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.bindphaseInterfaces;
import com.JIMS.MIS.model.bindphasemodel;

import java.util.List;

@Service
public class bindphaseservice {

   @Autowired
    private bindphaseInterfaces contractZoneRepository;

    public List<bindphasemodel> getContractZonesByContractIdAndSupplierId(int contractId, Long supplierId) {
        return contractZoneRepository.findContractZonesByContractIdAndSupplierId(contractId, supplierId);
    }
}
