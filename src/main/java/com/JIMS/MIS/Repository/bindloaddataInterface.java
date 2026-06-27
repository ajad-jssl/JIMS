package com.JIMS.MIS.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.bindloaddatamodel;

@Repository
public interface bindloaddataInterface extends CrudRepository<bindloaddatamodel, Integer> {
	   @Query( value = "EXEC spTRA_GetPiecesToLoadWithPieceProgressSubcon :contractId, :phase, 1, :supid ,:loadid",nativeQuery = true)
	    List<bindloaddatamodel> bindLoaddata(@Param("contractId") int contractId, 
	                                @Param("phase") int phase, 
	                                @Param("supid") int supId, 
	                                @Param("loadid") int loadId);
}
