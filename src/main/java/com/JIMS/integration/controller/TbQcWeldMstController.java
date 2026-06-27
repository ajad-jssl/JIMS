package com.JIMS.integration.controller;

import java.util.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.JIMS.integration.entity.TbQcWeldMst;
import com.JIMS.integration.services.TbQcWeldMstService;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/qc/weld")
public class TbQcWeldMstController {

    @Autowired
    private TbQcWeldMstService service;

    // ✅ CORRECT FOR SPRING BOOT 3
    @Autowired
    private EntityManager entityManager;

    // ================= EXISTING APIs =================

    @PostMapping("/create")
    public String create(@RequestBody TbQcWeldMst weld) {
        return service.create(weld);
    }

    @PutMapping("/update/{id}")
    public String update(
            @PathVariable Integer id,
            @RequestBody TbQcWeldMst weld) {
        return service.update(id, weld);
    }

    @GetMapping("/fetch")
    public List<TbQcWeldMst> fetchAll() {
        return service.fetchAll();
    }

    @GetMapping("/counts")
    public Map<String, Object> fetchCounts() {
        return service.fetchCounts();
    }

    // ================= NEW API =================

    @GetMapping("/usage-status")
    public List<Map<String, Object>> fetchUsageStatus() {

        String sql =
            "SELECT m.weld_id, " +
            "       m.fabricator_ID, " +
            "       m.welder_ID, " +
            "       CASE " +
            "           WHEN ( " +
            "               (m.fabricator_ID IS NOT NULL AND EXISTS ( " +
            "                   SELECT 1 FROM tab_qc_inspection t " +
            "                   WHERE t.fab_code = m.fabricator_ID " +
            "               )) " +
            "               OR " +
            "               (m.welder_ID IS NOT NULL AND EXISTS ( " +
            "                   SELECT 1 FROM tab_qc_weld_inspection t " +
            "                   WHERE t.welder_ID = m.welder_ID " +
            "               )) " +
            "           ) THEN 1 ELSE 0 " +
            "       END AS isUsed " +
            "FROM tb_qc_weld_mst m";

        Query query = entityManager.createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("weld_id", row[0]);
            map.put("fabricator_ID", row[1]);
            map.put("welder_ID", row[2]);
            map.put("isUsed", row[3]);
            result.add(map);
        }

        return result;
    }
    
   

    @PutMapping("/update-status")
    @Transactional(transactionManager = "integrationTransactionManager")
    public Map<String, Object> updateStatus(
            @RequestParam Integer weld_id,
            @RequestParam Integer status) {

        Map<String, Object> response = new HashMap<>();

        try {

            String sql = "UPDATE tb_qc_weld_mst SET status = :status, modified_date = GETDATE() WHERE weld_id = :id";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("status", status);
            query.setParameter("id", weld_id);

            int rows = query.executeUpdate();

            response.put("success", rows > 0);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }
}
