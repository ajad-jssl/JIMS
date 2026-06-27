package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.Contractlistrepository;
import com.JIMS.MIS.model.Contractlist;

@Service
public class Contractlistservice {

    @Autowired
    private Contractlistrepository contractlistrepository;

    // Fetch contracts by supplier ID
    public List<Contractlist> getContractsBySupplier(String supplierId) {
        return contractlistrepository.findContractsBySupplier(supplierId);
    }
}
