package com.JIMS.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.TargetMaster;

@Repository
public interface TargetMasterRepository
        extends JpaRepository<TargetMaster, Integer> {

    Optional<TargetMaster> findByContractId(Integer contractId);
    

    @Query(value =
            "select tm.*, " +
            "case when tm.CONTRACT_ID is null " +
            "then 'TOTAL' " +
            "else con.JobCode end as jobcode " +
            "from TARGET_MASTER tm " +
            "left join Contracts con " +
            "on tm.CONTRACT_ID = con.contract_id",
            nativeQuery = true)
    List<Object[]> getTargetMasterDetails();
}