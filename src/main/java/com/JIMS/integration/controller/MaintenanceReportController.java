package com.JIMS.integration.controller;

import com.JIMS.integration.entity.MaintenanceReportDTO;
import com.JIMS.integration.interfaces.MaintenanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/maintenance/report")
public class MaintenanceReportController {

    @Autowired
    private MaintenanceReportService maintenanceReportService;

    /**
     * GET /api/maintenance/report/all
     * Returns every maintenance report record.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllReports() {
        try {
            List<MaintenanceReportDTO> reports = maintenanceReportService.getAllReports();
            if (reports == null || reports.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No report data found.");
            }
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching report data: " + e.getMessage());
        }
    }

    /**
     * GET /api/maintenance/report/filter?fromDate=2024-01-01&toDate=2024-12-31
     * Returns records whose REPORTED_DATE falls within the supplied range.
     */
    @GetMapping("/filter")
    public ResponseEntity<?> getReportsByDateRange(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate")   String toDate) {
        try {
            List<MaintenanceReportDTO> reports =
                    maintenanceReportService.getReportsByDateRange(fromDate, toDate);
            if (reports == null || reports.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No records found for the given date range.");
            }
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching report data: " + e.getMessage());
        }
    }
}