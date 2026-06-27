package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.integration.entity.Factory;
import com.JIMS.integration.interfaces.FactoryInterface;

import jakarta.transaction.Transactional;

public interface FactoryRepository extends JpaRepository<Factory, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT into FACTORY_MASTER (factory_name) VALUES (:factory_name)", nativeQuery = true)
	int addFactory(String factory_name);

	@Query(value = "SELECT id, factory_name,GST_No as gstNo,StateCode,address from FACTORY_MASTER where is_delete = 0", nativeQuery = true)
	List<FactoryInterface> getAllFactoryDetails();
	
	@Query(value = "SELECT id, factory_name,GST_No as gstNo,StateCode,address from FACTORY_MASTER", nativeQuery = true)
	List<FactoryInterface> getAllFactoryDetailss();

	@Query(value = "SELECT id, factory_name from FACTORY_MASTER where id = :factory_id AND is_delete = 0 ", nativeQuery = true)
	FactoryInterface getFactoryDetailsById(String factory_id);

	@Query(value = "Select count(*) from FACTORY_MASTER where factory_name = :factory_name", nativeQuery = true)
	int checkIfFactoryNameExists(String factory_name);

}

