package com.JIMS.integration.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.MaintenanceTicket;

public interface MaintanceReportRepo extends JpaRepository<MaintenanceTicket, Integer> { 
	

    @Query(value =
        "SELECT MACHINE_ID, MACHINE_SUBCODE " +
        "FROM MAINTENANCE_MACHINE_LIST " +
        "WHERE (:factoryId = 0 OR FACTORY_ID = :factoryId) " +
        "AND (:machineDescId = 0 OR MACHINE_DESCRIPTION = :machineDescId) " +
        "ORDER BY MACHINE_SUBCODE",
        nativeQuery = true)
    List<Map<String, Object>> getMachines(
            @Param("factoryId") Integer factoryId,
            @Param("machineDescId") Integer machineDescId);
    
    
    
    
    @Query(value =
           """
    		 SELECT        
                   mtk.TICKET_ID,        
                   mtk.TICKET_NO,        
                   mcd.MACHINE_DESCRIPTION AS MACHINE_CATEGORY,        
                   mml.MACHINE_SUBCODE,        
                   mtk.EMP_NAME AS REPORTED_BY,        
                   mtk.REPORTED_DATE,        
                   mtk.PROBLEM_DESC, 
				   fac.factory_name,
                   mtk.WHAT_BROKE,        
                   CASE mtk.STATUS        
                        WHEN 1 THEN 'Open'        
                        WHEN 2 THEN 'Assigned'        
                        WHEN 3 THEN 'In Progress'        
                        WHEN 4 THEN 'Fixed'        
                        WHEN 5 THEN 'Closed'        
                        ELSE 'Unknown'        
                   END AS STATUS_NAME,        
                   CASE        
                        WHEN mtk.STATUS = 5 AND mtk.CLOSED_DATE IS NOT NULL        
                        THEN CONCAT(        
                             DATEDIFF(MINUTE, mtk.REPORTED_DATE, mtk.CLOSED_DATE) / 60,        
                             ' Hrs ',        
                             DATEDIFF(MINUTE, mtk.REPORTED_DATE, mtk.CLOSED_DATE) % 60,        
                             ' Min'        
                        )        
                        ELSE ''        
                   END AS DOWNTIME,        
                   mtk.CLOSED_BY,        
                   mtk.CLOSED_DATE,        
                   mtk.CLOSE_REMARKS        
               FROM MAINTENANCE_TICKET mtk        
               LEFT JOIN MAINTENANCE_MACHINE_LIST mml        
                      ON mtk.MACHINE_ID = mml.MACHINE_ID        
               LEFT JOIN MAINTENANCE_MACHINE_DESCRIPTION mcd        
                      ON mtk.MACHINE_DESC_ID = mcd.MACHINE_DESC_ID 
					  left join FACTORY_MASTER fac on mtk.FACTORY_ID =fac.id
               WHERE (:factoryId = 0 OR mtk.FACTORY_ID = :factoryId)        
                 AND (:machineDescId = 0 OR mtk.MACHINE_DESC_ID = :machineDescId)        
                 AND (:machineId = 0 OR mtk.MACHINE_ID = :machineId)        
                 AND CAST(mtk.REPORTED_DATE AS DATE) BETWEEN :fromDate AND :toDate        
               ORDER BY mtk.TICKET_ID DESC   
    		""",
            nativeQuery = true)
        List<Map<String, Object>> getMaintenanceDetailReport(
                @Param("factoryId") Integer factoryId,
                @Param("machineDescId") Integer machineDescId,
                @Param("machineId") Integer machineId,
                @Param("fromDate") String fromDate,
                @Param("toDate") String toDate);

}
