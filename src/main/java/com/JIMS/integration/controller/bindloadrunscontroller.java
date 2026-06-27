package com.JIMS.integration.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.JIMS.MIS.model.bindloadruns;
import com.JIMS.MIS.services.bindloadrunsservice;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("api/loadrun")
public class bindloadrunscontroller {
    @Autowired
    private bindloadrunsservice loadRunService;

    @GetMapping("/getLoadRuns")
    public List<bindloadruns> getLoadRunsSubcon(@RequestParam("tloadId") int tloadId) {
        return loadRunService.getLoadRunsSubcon(tloadId);
    }
}
