package com.JIMS.MIS.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.keyvalueInterfaces;

@Service
public class keyvalueservice {

    @Autowired
    private keyvalueInterfaces keyValueRepository;

    // Method to update and get the key value
    public Integer updateKeyValue() {
        return keyValueRepository.updateAndSelectKeyValue();
    }
}
