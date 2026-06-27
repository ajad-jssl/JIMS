package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.InvoiceExit;

import java.util.List;

@Repository
public interface InvoiceExitRepository extends JpaRepository<InvoiceExit, String> {

	@Query(value = "SELECT * from invoice_exit WHERE vechile_id = :vechileId ORDER BY vechile_id", nativeQuery = true)
	List<InvoiceExit> getvehiclebyvehicleid(@Param("vechileId") String vechileId);

}
