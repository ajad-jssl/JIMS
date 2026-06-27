package com.JIMS.MIS.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.bindphasemodel;

import java.util.List;

@Repository
public interface bindphaseInterfaces extends JpaRepository<bindphasemodel, Integer>{
	
	@Query(value="select distinct z.czone_id, z.contract_zone, z.zone_descr, " +
		       "CONCAT('Phase ', cast(z.contract_zone as varchar), ' - ', rtrim(z.zone_descr)) as descr, supplier_id " +
		       "from contract_zones z " +
		       "inner join Loads l on l.pzone = z.contract_zone and z.contract_id = l.contract_id " +
		       "inner join Piece_Instance i on i.load_id = l.load_id " +
		       "WHERE z.contract_id = :contractId AND supplier_id = :supplierId", nativeQuery = true)
		List<bindphasemodel> findContractZonesByContractIdAndSupplierId(@Param("contractId") int contractId, @Param("supplierId") Long supplierId);

}
