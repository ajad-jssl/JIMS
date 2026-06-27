

package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.TypeSelection;

@Repository
public interface Type_Selection_Repository extends JpaRepository<TypeSelection,Integer>{

	
	List<TypeSelection> findByTypeName(String typeName);
	
	  @Query(
		        value = "SELECT TYPE_ID, TYPE_NAME FROM TYPE_SELECTION",
		        nativeQuery = true
		    )
		    List<Object[]> findTypeIdAndNameOnly();
		    
		    
		    @Query(value=" select count(TYPE_ID) from ACTIVITY_CATEGORY_MASTER where TYPE_ID=:type_id",nativeQuery = true)
		    Integer CheckUsedType(@Param("type_id") Integer type_id);
}
