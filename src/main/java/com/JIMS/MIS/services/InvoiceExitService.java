package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.InvoiceExitRepository;
import com.JIMS.MIS.model.InvoiceExit;

@Service
public class InvoiceExitService {

    @Autowired
    private InvoiceExitRepository InvoiceExitRepository;

    // Fetch contracts by supplier ID
    public List<InvoiceExit> getvehiclebyvehicleid(String vechileId) {
        return InvoiceExitRepository.getvehiclebyvehicleid(vechileId);
    }
}
