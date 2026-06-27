package com.JIMS.MIS.services;

import com.JIMS.MIS.Repository.Lotlistrepository;
import com.JIMS.MIS.model.Lotlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Lotlistservice {

    @Autowired
    private Lotlistrepository lotlistrepository;

    public List<Lotlist> getLoadSummarySubcon(int contractId,String loadno,int supid) { //String loadno, 
        return lotlistrepository.getLoadSummarySubcon(contractId, loadno, supid);//loadno, 
    }
}
