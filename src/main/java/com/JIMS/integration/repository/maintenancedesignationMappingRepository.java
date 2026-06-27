package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.MaintenancedesignationMapping;

@Repository
public interface maintenancedesignationMappingRepository   
extends JpaRepository<MaintenancedesignationMapping, Integer> {

    boolean existsByDesignationNameIgnoreCase(String designationName);
    
   // boolean existsByDesignationNameIgnoreCase(String designationName);

    boolean existsByDesignationCodeIgnoreCase(String designationCode);
	
    @Query(value =
            "  select * from MAINTENANCE_DESIGNATION\r\n"
            + "",
            nativeQuery = true)
    List<Object[]> getAlldesignationMappings();
    
    
    
    boolean existsByDesignationNameIgnoreCaseAndDesignationIdNot(
            String designationName,
            Integer designationId);

    boolean existsByDesignationCodeIgnoreCaseAndDesignationIdNot(
            String designationCode,
            Integer designationId);
	
}
