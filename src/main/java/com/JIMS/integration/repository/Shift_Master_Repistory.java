package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.Shift_Master;

@Repository
public interface Shift_Master_Repistory extends JpaRepository<Shift_Master,Integer> {
	
	  List<Shift_Master> findByShiftCode(String shiftCode);
	  
	  @Query(
			    value = """
			        SELECT COUNT(*)
			        FROM SHIFT_MASTER
			        WHERE CODE = :shiftCode
			          AND LOCATION_ID = :locationId
			    """,
			    nativeQuery = true
			)
			long CheckforDuplicationBasedLocation(
			        @Param("shiftCode") String shiftCode,
			        @Param("locationId") int locationId
			);
	  
	  
	  
	  
	  @Query(value="select count(SHIFT_ID) FROM Jc_Drawntxn where SHIFT_ID=:shift_id",nativeQuery = true)
	  Integer CheckUsedShift(@Param("shift_id") Integer shift_id);
	  
}
