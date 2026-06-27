package com.JIMS.integration.controller;

import org.springframework.web.bind.annotation.*;

import com.JIMS.MIS.services.SubContractorService;

import java.util.*;
@CrossOrigin
@RestController
@RequestMapping("/api/subcontractor")
public class SubContractorController {

    private final SubContractorService service;

    public SubContractorController(SubContractorService service) {
        this.service = service;
    }

    @GetMapping("/suppliers")
    public List<Map<String, Object>> getSuppliers() {
        return service.getAvailableSuppliers();
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        return service.getAvailableUsers();
    }
    
    @GetMapping("/subconusers")
    public List<Map<String, Object>> getSubconUsers() {
        return service.getSubconUsers();
    }
    
    
    @GetMapping("/G2User")
    public List<Map<String, Object>> getG2Users() {
        return service.getG2Users();
    }
    
    @GetMapping("/contracts")
    public List<Map<String, Object>> getContracts(@RequestParam Integer supplierId) {
        return service.getContractsBySupplier(supplierId);
    }

    // 2. Get distinct pzone by contract and supplier
    @GetMapping("/pzones")
    public List<Integer> getPZones(@RequestParam String contractId, @RequestParam Integer supplierId) {
        return service.getDistinctPZones(contractId, supplierId);
    }

    // 3. Get distinct pload by contract, supplier, and pzone
    @GetMapping("/ploads")
    public List<Map<String, Object>> getPLoads(
            @RequestParam String contractId,
            @RequestParam Integer supplierId,
            @RequestParam String pzone) {
        return service.getDistinctPLoads(contractId, supplierId, pzone);
    }
    
    @GetMapping("/contractloads")
    public List<Map<String, Object>> getContractloads(
            @RequestParam String contract_id,
            @RequestParam Integer supid) {
        return service.getDistinctcontractloads(contract_id, supid);
    }

    // 4. Get detailed load pieces info
    @GetMapping("/details")
    public List<Map<String, Object>> getLoadDetails(
            @RequestParam String contractId,
            @RequestParam String pzone,
            @RequestParam String loadId,
            @RequestParam Integer supplierId) {
        return service.getLoadDetails(contractId, pzone, loadId, supplierId);
    }
    
    @GetMapping("/items")
    public List<Map<String, Object>> getPackingItems(
            @RequestParam String contractId,
            @RequestParam Integer loadId
    ) {
        return service.getPackingItems(contractId, loadId);
    }
    
    @GetMapping("/header")
    public Map<String, Object> getHeader(
            @RequestParam String contractId,
            @RequestParam Integer loadId
    ) {
        return service.getHeader(contractId, loadId);
    }
}
