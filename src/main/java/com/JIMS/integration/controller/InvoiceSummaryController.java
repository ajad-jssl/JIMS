package com.JIMS.integration.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.interfaces.InvoiceSummaryDTO;
import com.JIMS.integration.repository.InvoiceSummaryRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceSummaryController {

    @Autowired
    private InvoiceSummaryRepository invoiceSummaryRepository;

    // ─── Shared null-safe helpers ────────────────────────────────────────────

    private String getString(Object value) {
        return value == null ? "" : value.toString();
    }

    private long getLong(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
    }

    private double getDouble(Object value) {
        return value == null ? 0.0 : ((Number) value).doubleValue();
    }

    // ─── Shared row-unwrap helper ─────────────────────────────────────────────
    // Handles both Object[] and Object[][] (nested) results from JPA native queries

    private Object[] unwrapRow(Object result) {
        if (result == null) return null;

        if (result instanceof Object[]) {
            Object first = ((Object[]) result)[0];
            if (first instanceof Object[]) {
                return (Object[]) first;   // nested case
            }
            return (Object[]) result;      // normal case
        }
        throw new RuntimeException("Unexpected result type: " + result.getClass());
    }

    // ─── Shared row → map builder ─────────────────────────────────────────────

    private Map<String, Object> buildRowMap(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("invoiceType",    getString(row[0]));
        map.put("totalCount",     getLong(row[1]));
        map.put("totalAmount",    getDouble(row[2]));
        map.put("releaseCount",   getLong(row[3]));
        map.put("releaseAmount",  getDouble(row[4]));
        map.put("cancelCount",    getLong(row[5]));
        map.put("cancelAmount",   getDouble(row[6]));
        map.put("verifiedCount",  getLong(row[7]));
        map.put("verifiedAmount", getDouble(row[8]));
        return map;
    }

    // ─── Endpoints ────────────────────────────────────────────────────────────

    @GetMapping("/summary/by-type")
    public ResponseEntity<List<Map<String, Object>>> getInvoiceSummaryByType() {

        List<Object[]> results = invoiceSummaryRepository.getInvoiceSummaryByType();

        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Object[] row : results) {
            responseList.add(buildRowMap(row));
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/summary/grand-total")
    public ResponseEntity<Map<String, Object>> getGrandTotalSummary() {

        Object result = invoiceSummaryRepository.getGrandTotalSummary();

        if (result == null) {
            return ResponseEntity.noContent().build();
        }

        Object[] row = unwrapRow(result);
        if (row == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(buildRowMap(row));
    }

    @GetMapping("/summary/by-type/factory")
    public ResponseEntity<List<Map<String, Object>>> getInvoiceSummaryByTypeFacotory(
            @RequestParam String factory_id) {

        List<Object[]> results = invoiceSummaryRepository.getInvoiceSummaryByTypeFactory(factory_id);

        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Object[] row : results) {
            responseList.add(buildRowMap(row));
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/summary/grand-total/factory")
    public ResponseEntity<Map<String, Object>> getGrandTotalSummaryFactory(
            @RequestParam String factory_id) {

        Object result = invoiceSummaryRepository.getGrandTotalSummaryFactory(factory_id);

        if (result == null) {
            return ResponseEntity.noContent().build();
        }

        Object[] row = unwrapRow(result);
        if (row == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(buildRowMap(row));
    }

    @GetMapping("/summary/Contracts")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam(required = false) String factoryId,
            @RequestParam(required = false) String contractId) {

        Integer factory = (factoryId == null || factoryId.isEmpty()) ? null : Integer.parseInt(factoryId);
        String contract = (contractId == null || contractId.isEmpty()) ? null : contractId;

        List<Object[]> results = invoiceSummaryRepository.getSummaryContract(factory, contract);

        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(buildRowMap(results.get(0)));
    }

    @GetMapping("/summary/invoice-types")
    public ResponseEntity<List<Map<String, Object>>> getSummaryByInvoiceType(
            @RequestParam(required = false) String factoryId,
            @RequestParam(required = false) String contractId) {

        Integer factory = (factoryId == null || factoryId.isEmpty()) ? null : Integer.parseInt(factoryId);
        String contract = (contractId == null || contractId.isEmpty()) ? null : contractId;

        List<Object[]> results = invoiceSummaryRepository.getSummaryByInvoiceType(factory, contract);

        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Object[] row : results) {
            responseList.add(buildRowMap(row));
        }

        return ResponseEntity.ok(responseList);
    }
}