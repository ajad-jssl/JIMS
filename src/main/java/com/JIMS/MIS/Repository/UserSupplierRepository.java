package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.UserSupplier;

@Repository
public interface UserSupplierRepository extends JpaRepository<UserSupplier, Long> {
}
