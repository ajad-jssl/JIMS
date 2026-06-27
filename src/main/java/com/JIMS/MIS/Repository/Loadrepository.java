package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.LoadModel;

import java.util.List;

@Repository
public interface  Loadrepository extends JpaRepository<LoadModel, Integer> {	
    @Query(value = "select distinct pload,l.load_id from dbo.Loads as l  inner join  [VW_JSSL_SubconContractLoad] as s on s.contract_id=l.contract_id and s.load_id=l.load_id where l.contract_id= :contractId  and supplier_id=:supId  order by pload", nativeQuery = true)
   
    List<LoadModel> findLoadsByContractAndSupId(Integer contractId, Integer supId);

}
