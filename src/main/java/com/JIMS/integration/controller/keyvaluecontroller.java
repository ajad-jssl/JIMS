package com.JIMS.integration.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.services.keyvalueservice;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class keyvaluecontroller {

    @Autowired
    private keyvalueservice keyValueService;

    // Endpoint to update keyvalue and return the updated keyvalue
    @GetMapping("/updateKeyValue")
    public Integer updateKeyValue() {
        // Call the service to perform the update and get the updated value
        Integer updatedKeyValue = keyValueService.updateKeyValue();

        // Return the updated keyvalue
        return updatedKeyValue;
    }
}
