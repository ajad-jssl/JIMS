package com.JIMS.MIS.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.bindloadrunsInterfaces;
import com.JIMS.MIS.model.bindloadruns;

import java.util.List;
@Service
public class bindloadrunsservice {

    @Autowired
    private bindloadrunsInterfaces loadRunRepository;

    public List<bindloadruns> getLoadRunsSubcon(int tloadId) {
        return loadRunRepository.getLoadRunsSubcon(tloadId);
    }
}
