package com.JIMS.integration.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.TbQcWeldMst;
import com.JIMS.integration.repository.TbQcWeldMstRepository;

@Service
public class TbQcWeldMstService {

    @Autowired
    private TbQcWeldMstRepository repository;

    // CREATE (with existing duplicate logic)
    public String create(TbQcWeldMst weld) {

        boolean exists = repository.existsAny(
                weld.getFabricator_ID(),
                weld.getWelder_ID(),
                weld.getRevision(),
                weld.getPosition(),
                weld.getProcess()
        );

        if (exists) {
            return "Duplicate not allowed (Fabricator / Welder / Revision / Position / Process already exists)";
        }

        weld.setCreated_date(LocalDateTime.now());
        repository.save(weld);
        return "Weld master created successfully";
    }

    // UPDATE
    public String update(Integer id, TbQcWeldMst weld) {

        TbQcWeldMst existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        existing.setFabricator_ID(weld.getFabricator_ID());
        existing.setWelder_ID(weld.getWelder_ID());
        existing.setRevision(weld.getRevision());
        existing.setPosition(weld.getPosition());
        existing.setProcess(weld.getProcess());
        existing.setModified_by(weld.getModified_by());
        existing.setModified_date(LocalDateTime.now());

        repository.save(existing);
        return "Weld master updated successfully";
    }

    // 🔹 FETCH ALL
    public List<TbQcWeldMst> fetchAll() {
        return repository.findAll();
    }

    // 🔹 FETCH COUNTS
    public Map<String, Object> fetchCounts() {

        Object[] row = (Object[]) repository.getSummaryCounts();

        Map<String, Object> result = new HashMap<>();
        result.put("fabricatorCount", row[0]);
        result.put("welderCount", row[1]);
        result.put("revisionCount", row[2]);
        result.put("positionCount", row[3]);
        result.put("processCount", row[4]);

        return result;
    }
}
