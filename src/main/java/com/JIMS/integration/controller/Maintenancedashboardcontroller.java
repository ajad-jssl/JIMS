package com.JIMS.integration.controller;

import com.JIMS.integration.entity.MachineListDTO;
import com.JIMS.integration.entity.MachineTicketDetailDTO;
import com.JIMS.integration.entity.MachinecategoryListDTO;
import com.JIMS.integration.entity.Maintenancecategorysummarydto;
import com.JIMS.integration.entity.TopBrokeCauseDTO;
import com.JIMS.integration.entity.MaintenanceMachineSummaryDTO;
import com.JIMS.integration.services.MaintenanceDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin
public class Maintenancedashboardcontroller {

    @Autowired
    private MaintenanceDashboardService dashboardService;

    // ------------------------------------------------------------------
    // 1. Summary grouped by machine subcode  (+ date filter)
    // ------------------------------------------------------------------
    @GetMapping("/tickets/summary/by-machine")
    public ResponseEntity<List<MaintenanceMachineSummaryDTO>> getSummaryByMachine(
            @RequestParam(required = false) Integer factoryId,
            @RequestParam(required = false) String  machineSubcode,
            @RequestParam(required = false) String  dateFrom,
            @RequestParam(required = false) String  dateTo) {

        return ResponseEntity.ok(
            dashboardService.getSummaryByMachine(factoryId, machineSubcode, dateFrom, dateTo));
    }

    // ------------------------------------------------------------------
    // 2. Grand total  (+ date filter)
    // ------------------------------------------------------------------
    @GetMapping("/tickets/summary/grand-total")
    public ResponseEntity<MaintenanceMachineSummaryDTO> getGrandTotal(
            @RequestParam(required = false) Integer factoryId,
            @RequestParam(required = false) String  machineSubcode,
            @RequestParam(required = false) String  dateFrom,
            @RequestParam(required = false) String  dateTo) {

        return ResponseEntity.ok(
            dashboardService.getGrandTotal(factoryId, machineSubcode, dateFrom, dateTo));
    }

    // ------------------------------------------------------------------
    // 3. NEW — Summary grouped by category  (for category donut)
    // ------------------------------------------------------------------
    @GetMapping("/tickets/summary/by-category")
    public ResponseEntity<List<Maintenancecategorysummarydto>> getSummaryByCategory(
            @RequestParam(required = false) Integer factoryId,
            @RequestParam(required = false) String  machineSubcode,
            @RequestParam(required = false) String  dateFrom,
            @RequestParam(required = false) String  dateTo) {

        return ResponseEntity.ok(
            dashboardService.getSummaryByCategory(factoryId, machineSubcode, dateFrom, dateTo));
    }

    // ------------------------------------------------------------------
    // 4. Machine list for dropdown
    // ------------------------------------------------------------------
    @GetMapping("/machines/list")
    public ResponseEntity<?> getMachineList(
            @RequestParam(required = false) Integer factoryId,
            @RequestParam String category) {

        List<MachineListDTO> machines = dashboardService.getMachineList(factoryId, category);
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("Data",   machines);
        response.put("Status", "success");
        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // 5. Category list for dropdown
    // ------------------------------------------------------------------
    @GetMapping("/machine_category/list")
    public ResponseEntity<?> getcategoryList(
            @RequestParam(required = false) Integer factoryId) {

        List<MachinecategoryListDTO> categories = dashboardService.getcategoryList(factoryId);
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("Data",   categories);
        response.put("Status", "success");
        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // 6. Tickets by machine  (+ date filter)
    // ------------------------------------------------------------------
    @GetMapping("/tickets/by-machine")
    public ResponseEntity<?> getTicketsByMachine(
            @RequestParam(required = false) String  machineSubcode,
            @RequestParam(required = false) Integer factoryId,
            @RequestParam(required = false) String  dateFrom,
            @RequestParam(required = false) String  dateTo) {

        List<MachineTicketDetailDTO> tickets =
            dashboardService.getTicketsByMachine(machineSubcode, factoryId, dateFrom, dateTo);
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("Data",   tickets);
        response.put("Status", "success");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tickets/top-broke-causes")
    public ResponseEntity<Map<String, List<TopBrokeCauseDTO>>> getTopBrokeCauses(
            @RequestParam(required = false) Integer factoryId,
            @RequestParam(required = false) String  dateFrom,
            @RequestParam(required = false) String  dateTo) {

        return ResponseEntity.ok(
            dashboardService.getTopBrokeCauses(factoryId, dateFrom, dateTo));
    }
}
