package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.ReasonSelection;

@Repository
public interface Reason_Selection_Repository extends JpaRepository<ReasonSelection, Integer> {

    // Find by reason name
    List<ReasonSelection> findByReasonName(String reasonName);

    // Fetch only ID and Name
    @Query(
        value = "SELECT REASON_ID, REASON_NAME FROM REASON_SELECTION",
        nativeQuery = true
    )
    List<Object[]> findReasonIdAndNameOnly();
    
    
    
    @Query(value="  select  count(REASON_ID) FROM Jc_Drawntxn where REASON_ID=:reason_id",nativeQuery = true)
    Integer checkReason(@Param("reason_id") Integer reason_id);
    
    
    
    
    
    
}
