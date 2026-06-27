package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.Lotlist;

import java.util.List;

@Repository
public interface Lotlistrepository extends JpaRepository<Lotlist, Integer> {

    // Call the stored procedure using native SQL
	@Query(value = "EXEC dbo.spTRA_GetLoadSummarySubcon :contractId,:loadno,:supid", nativeQuery = true)//loadno
	List<Lotlist> getLoadSummarySubcon(
	    @Param("contractId") int contractId,
	    @Param("loadno") String loadno,
	    @Param("supid") int supid
	);
}
