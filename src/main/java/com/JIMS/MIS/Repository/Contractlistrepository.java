package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.Contractlist;

import java.util.List;

@Repository
public interface Contractlistrepository extends JpaRepository<Contractlist, Integer> {

	@Query(value = "SELECT DISTINCT c.contract_id AS contract_id, c.cname AS cname FROM VW_JSSL_SubconContractLoad c WHERE c.supplier_id = :supplierId ORDER BY c.contract_id", nativeQuery = true)
	List<Contractlist> findContractsBySupplier(@Param("supplierId") String supplierId);

}
