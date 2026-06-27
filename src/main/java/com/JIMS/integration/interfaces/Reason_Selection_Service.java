package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.ReasonSelection;
import com.JIMS.integration.repository.Reason_Selection_Repository;

@Service
public class Reason_Selection_Service {

    @Autowired
    private Reason_Selection_Repository reason_selection_repo;

    public List<ReasonSelection> getAllReason() {
        return reason_selection_repo.findAll();
    }

    public Optional<ReasonSelection> getReasonById(Integer id) {
        return reason_selection_repo.findById(id);
    }

    public void addReason(ReasonSelection reasonSelection) {
        reason_selection_repo.save(reasonSelection);
    }

    public List<ReasonSelection> checkDuplicate(String reason) {
        return reason_selection_repo.findByReasonName(reason);
    }

    public List<Object[]> getReasonIdAndNameOnly() {
        return reason_selection_repo.findReasonIdAndNameOnly();
    }

    public Long getReasonCount() {
        return reason_selection_repo.count();
    }
    
    public Integer checkReason(Integer reason_id) {
    	return reason_selection_repo.checkReason(reason_id);
    }
}
