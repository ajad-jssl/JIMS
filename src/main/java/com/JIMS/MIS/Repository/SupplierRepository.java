package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.MIS.model.Suppliers;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Suppliers, Long> {

    @Query(value = """
        SELECT supplier_id, (code + ' ' + name) as name, suppcat_id 
        FROM Suppliers 
        WHERE suppcat_id = 21 
          AND supplier_id NOT IN (SELECT supid FROM usersupplier)
    """, nativeQuery = true)
    List<Object[]> findAvailableSuppliers();
}
