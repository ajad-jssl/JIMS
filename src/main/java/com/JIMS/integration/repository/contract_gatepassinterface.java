package com.JIMS.integration.repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.JIMS.integration.entity.Contract_gatepassmodel;
import com.JIMS.integration.interfaces.AdminGPListProjection;
import com.JIMS.integration.interfaces.ContractGPListProjection;

@Repository
public interface contract_gatepassinterface extends JpaRepository<Contract_gatepassmodel, Integer> {
	@Query("SELECT c FROM Contract_gatepassmodel c WHERE c.factory_id = :factoryId")
	List<Contract_gatepassmodel> getByFactoryId(@Param("factoryId") Integer factoryId);
	
	


	Optional<Contract_gatepassmodel> findByEmpIdIgnoreCase(String empId);
	
	
	
	@Query("SELECT c.cg_id as cg_id, c.Aadhar_no as aadhar_no, c.Pass_id as pass_id, " +
		       "c.contactno as contactno, c.pan_no as pan_no, c.Father_name as father_name, " +
		       "c.active as active " +
		       "FROM Contract_gatepassmodel c WHERE c.factory_id = :factoryId " +
		       "AND (:search IS NULL OR :search = '' OR " +
		       "LOWER(c.Aadhar_no) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(c.Pass_id) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(c.contactno) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(c.pan_no) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(c.Father_name) LIKE LOWER(CONCAT('%', :search, '%')))")
		Page<ContractGPListProjection> getListByFactoryId(
		    @Param("factoryId") Integer factoryId,
		    @Param("search") String search,
		    Pageable pageable
		);
	
	@Query("SELECT COUNT(c) FROM Contract_gatepassmodel c WHERE c.factory_id = :factoryId")
	long countByFactoryId(@Param("factoryId") Integer factoryId);
	
	
	
	
	@Query(value =
		       "SELECT cg_id, Pass_id as pass_id, Aadhar_no as aadhar_no, " +
		       "Emp_id as emp_id, Fname as fname, Lname as lname, active, " +

		       // 🔥 ADD THIS LINE
		       "CASE WHEN valid_till < GETDATE() THEN 1 ELSE 0 END AS is_expired " +

		       "FROM CONTRACT_GATEPASS_ENTRY " +
		       "WHERE factory_id = :factoryId " +
		       "AND (:search IS NULL OR :search = '' OR " +
		       "     Aadhar_no  LIKE CONCAT('%', :search, '%') OR " +
		       "     Pass_id    LIKE CONCAT('%', :search, '%') OR " +
		       "     Fname      LIKE CONCAT('%', :search, '%') OR " +
		       "     Lname      LIKE CONCAT('%', :search, '%') OR " +
		       "     Emp_id     LIKE CONCAT('%', :search, '%')) " +
		       "ORDER BY " +
		       "     CASE WHEN Emp_id IS NULL OR Emp_id = '' THEN 0 ELSE 1 END ASC, " +
		       "     CASE WHEN active = 1 THEN 1 ELSE 0 END ASC, " +
		       "     cg_id ASC",

		       countQuery =
		       "SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY " +
		       "WHERE factory_id = :factoryId " +
		       "AND (:search IS NULL OR :search = '' OR " +
		       "     Aadhar_no  LIKE CONCAT('%', :search, '%') OR " +
		       "     Pass_id    LIKE CONCAT('%', :search, '%') OR " +
		       "     Fname      LIKE CONCAT('%', :search, '%') OR " +
		       "     Lname      LIKE CONCAT('%', :search, '%') OR " +
		       "     Emp_id     LIKE CONCAT('%', :search, '%'))",

		       nativeQuery = true)
		Page<AdminGPListProjection> getAdminListByFactoryId(
		    @Param("factoryId") Integer factoryId,
		    @Param("search") String search,
		    Pageable pageable
		);
	
    @Modifying
    @Transactional
    @Query(value = "UPDATE CONTRACT_GATEPASS_ENTRY SET valid_till = :validupt WHERE cg_id = :cgid",
           nativeQuery = true)
    int updateValidTill(@Param("validupt") Date validupt,
                        @Param("cgid") int cgid);
	
	@Query(value="select count(Aadhar_no) from CONTRACT_GATEPASS_ENTRY where Aadhar_no=:adharno and cg_id <>:cg_id",nativeQuery = true)
	Integer duplicateadharno(String adharno,Integer cg_id);
	
	
}
