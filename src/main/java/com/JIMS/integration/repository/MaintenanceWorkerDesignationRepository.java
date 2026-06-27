package com.JIMS.integration.repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.JIMS.integration.entity.MaintenanceWorkerDesignation;
import jakarta.transaction.Transactional;


@Repository
public interface MaintenanceWorkerDesignationRepository
        extends JpaRepository<MaintenanceWorkerDesignation, Integer> {

    // FIXED: use DESIGNATON_ID (matches your actual DB column)
    @Query(value =
        "SELECT WD.WORKER_DESIG_ID, WD.DESIGNATION_ID, " +
        "D.DESIGNATION_NAME, D.DESIGNATION_CODE " +
        "FROM MAINTENANCE_WORKER_DESIGNATION WD " +
        "JOIN MAINTENANCE_DESIGNATION D " +
        "ON WD.DESIGNATION_ID = D.DESIGNATON_ID " +  // ← typo fixed here
        "WHERE WD.WORKER_MAPPING_ID = :workerMappingId " +
        "AND WD.STATUS = 1",
        nativeQuery = true)
    List<Map<String, Object>> getDesignationsByWorkerMappingId(
            @Param("workerMappingId") Long workerMappingId);

    @Modifying
    @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORKER_DESIGNATION " +
        "SET STATUS = 0 " +
        "WHERE WORKER_MAPPING_ID = :workerMappingId",
        nativeQuery = true)
    void deactivateByWorkerMappingId(
            @Param("workerMappingId") Long workerMappingId);

    @Modifying
    @Transactional
    @Query(value =
        "UPDATE MAINTENANCE_WORKER_DESIGNATION " +
        "SET STATUS = 1 " +
        "WHERE WORKER_MAPPING_ID = :workerMappingId " +
        "AND DESIGNATION_ID = :designationId",
        nativeQuery = true)
    int activateWorkerDesignation(
            @Param("workerMappingId") Long workerMappingId,
            @Param("designationId") Integer designationId);
}