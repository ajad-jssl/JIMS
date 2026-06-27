package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VTS_vtypemodel;

@Repository
public interface VTS_vtypeinterface extends JpaRepository<VTS_vtypemodel, Integer> {

}
