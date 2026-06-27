package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.Lotnumber;

import java.util.List;
@Repository
public interface LotnumberInterfaces extends JpaRepository<Lotnumber, Integer>{
@Query(value ="SELECT DISTINCT l.tload_id, l.loadno FROM Tra_Loads AS l WHERE l.contract_id = :contractId AND l.supid = :supId ORDER BY l.tload_id ",nativeQuery = true )
List<Lotnumber>  findLoadsByContractAndSupId(Integer contractId, Integer supId);
}
