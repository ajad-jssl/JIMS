package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.MaintenanceWorkerMapping;

@Repository
public interface MaintenanceWorkerMappingRepository
        extends JpaRepository<MaintenanceWorkerMapping, Long> {
    @Query(value =
            "SELECT COUNT(*) " +
            "FROM MaintenanceWorkerMapping " +
            "WHERE Worker_EmpCode = :workerEmpCode " +
            "AND Machine_Category_Id = :machineCategoryId " +
            "AND Is_Active = 1  and Factory_Id =:factory_id ",
            nativeQuery = true)
    int checkWorkerAlreadyMapped(
            @Param("workerEmpCode") String workerEmpCode,
            @Param("machineCategoryId") Integer machineCategoryId,@Param("factory_id") String facotry_id);
    
    
    @Query(value =
            """
    		   
		    SELECT        
               ump.Id,        
               ump.Worker_EmpCode,        
               ump.Worker_MobileNO,        
               ump.worker_email,ump.employee_display_name,   
               mainDes.MACHINE_DESCRIPTION        
               FROM MaintenanceWorkerMapping ump        
               LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION mainDes        
               ON ump.Machine_Category_Id =  mainDes.MACHINE_DESC_ID  where ump.Factory_Id=:Factoryid
    		""",
            nativeQuery = true)
    List<Object[]> getAllWorkerMappings(@Param("Factoryid") String Factoryid);
    
    

    @Query(value =
            "SELECT COUNT(*) " +
            "FROM MaintenanceWorkerMapping " +
            "WHERE Worker_EmpCode = :workerEmpCode " +
            "AND Machine_Category_Id = :machineCategoryId " +
            "AND Is_Active = 1 " +
            "AND Id <> :id",
            nativeQuery = true)
    int checkDuplicateForUpdate(
            @Param("workerEmpCode") String workerEmpCode,
            @Param("machineCategoryId") Integer machineCategoryId,
            @Param("id") Long id);

}
