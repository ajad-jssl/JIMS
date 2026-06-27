package com.JIMS.integration.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.VTS_incponumbermodel;

@Repository
public interface VTS_incponumberintreface extends JpaRepository<VTS_incponumbermodel, Integer> {

}
