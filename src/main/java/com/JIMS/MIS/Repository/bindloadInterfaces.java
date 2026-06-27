package com.JIMS.MIS.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.bindloadmodel;

@Repository
public interface bindloadInterfaces extends JpaRepository<bindloadmodel, Integer> {
    // Query to execute the stored procedure and fetch the data
    @Query(value = "EXEC spTRA_GetLoadsToLoadSubcon :contractId, :phase, '1', :supid", nativeQuery = true)
    List<bindloadmodel> getLoadsToLoadSubcon(@Param("contractId") int contractId, 
                                    @Param("phase") int phase, 
                                    @Param("supid") int supid);
}
