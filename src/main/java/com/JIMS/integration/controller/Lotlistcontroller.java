package com.JIMS.integration.controller;

import com.JIMS.MIS.model.Lotlist;
import com.JIMS.MIS.services.Lotlistservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class Lotlistcontroller {

    @Autowired
    private Lotlistservice lotlistservice;

    @GetMapping("/getLoadSummarySubcon")
    public ResponseEntity<List<Lotlist>> getLoadSummarySubcon(
            @RequestParam("contractId") int contractId,
            @RequestParam("loadno") String loadno,
            @RequestParam("supid") int supid) {

        List<Lotlist> lotlists = lotlistservice.getLoadSummarySubcon(contractId,loadno, supid);//loadno
        return ResponseEntity.ok(lotlists);
    }
}
