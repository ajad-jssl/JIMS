package com.JIMS.MIS.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.Loadlistrepository;
import com.JIMS.MIS.model.Loadlist;
@Service
public class Loadlistservice {
	 
    @Autowired
    private Loadlistrepository loadlistrepository;

    public List<Loadlist> getLoadsByContractAndSupId(Integer contractId, Integer supId) {
        return loadlistrepository.findLoadsByContractAndSupId(contractId, supId);
    }
}

