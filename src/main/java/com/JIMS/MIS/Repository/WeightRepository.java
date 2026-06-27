package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.WeightModel;

import java.util.List;

@Repository
public interface WeightRepository extends JpaRepository<WeightModel, Integer> {

	// Call the stored procedure using native SQL
		@Query(value = "EXEC dbo.spDTL_AssemblyStagesForLoadSubcon :Load_Id,:Sup_Id", nativeQuery = true)//loadno
		List<WeightModel> getAssemblyStagesForLoadSubcon(
		    @Param("Load_Id") int Load_Id,		  
		    @Param("Sup_Id") int Sup_Id
		); 
}
 