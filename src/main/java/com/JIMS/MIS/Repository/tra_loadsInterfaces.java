package com.JIMS.MIS.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.Tra_Loads;

@Repository
public interface tra_loadsInterfaces extends JpaRepository<Tra_Loads, Integer> {
    @Query(value = "SELECT TOP 1 * FROM Tra_Loads WHERE contract_id = :contractId ORDER BY tload_id DESC", nativeQuery = true)
    List<Tra_Loads> findbycontractId(@Param("contractId") Integer contractId);
}
