package com.JIMS.integration.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.GPMS_returnable;


@Repository
    public interface gpms_returnableinterface extends JpaRepository<GPMS_returnable, Integer> {
        @Query("SELECT g FROM GPMS_returnable g WHERE g.GP_type = 1 AND g.security = 1 ")
	List<GPMS_returnable> findByGpTypeAndSecurityAndFactoryId(Integer factoryId);
}

