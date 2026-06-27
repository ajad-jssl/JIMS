package com.JIMS.MIS.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.ContractModel;

import java.util.List;
 
@Repository

public interface ContractRepository extends JpaRepository<ContractModel, Integer> {
	  @Query(value = "select distinct contract_id as cid, cname from   [VW_JSSL_SubconContractLoad] where   supplier_id=:supplierId", nativeQuery = true)
	 
List<ContractModel> findContractsBySupplier(@Param("supplierId") String supplierId);
}
	  
	  

 