package com.JIMS.integration.controller;

import com.JIMS.integration.entity.Consolidated_g2reports;
import com.JIMS.integration.interfaces.Consolidated_g2reportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/jssl/consolidated-g2reports")
@CrossOrigin
public class Consolidated_g2reportsController {

    @Autowired
    private Consolidated_g2reportsService consolidated_g2reportsService;

    @GetMapping
    public ResponseEntity<Map<String, List<Consolidated_g2reports>>> getConsolidatedG2Reports(
    		@RequestParam String jobCode,
            @RequestParam(defaultValue = "0") Integer loadtype,
            @RequestParam Integer factory) {

        Map<String, List<Consolidated_g2reports>> response = new HashMap<>();

        if (factory == 0) {
            // Call service twice
            List<Consolidated_g2reports> bellary =
                    consolidated_g2reportsService.getReport(jobCode, loadtype, 1);

            List<Consolidated_g2reports> gujarat =
                    consolidated_g2reportsService.getReport(jobCode, loadtype, 2);

            response.put("bellary", bellary);
            response.put("gujarat", gujarat);

        } else if (factory == 1) {
            response.put("bellary",
                    consolidated_g2reportsService.getReport(jobCode, loadtype, 1));
            response.put("gujarat", new ArrayList<>());

        } else if (factory == 2) {
            response.put("bellary", new ArrayList<>());
            response.put("gujarat",
                    consolidated_g2reportsService.getReport(jobCode, loadtype, 2));
        }

        return ResponseEntity.ok(response);
    }
}