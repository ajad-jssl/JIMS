package com.JIMS.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.useractionpermissionmodel;

import jakarta.transaction.Transactional;

import java.util.List;

public interface useractinpermissioninterface extends JpaRepository<useractionpermissionmodel, Long> {
    @Query("SELECT uap.action FROM useractionpermissionmodel uap WHERE uap.userId = ?1 AND uap.moduleId = ?2 and uap.isDelete = 0")
    List<String> findActionsByUserIdAndModuleId(String userId, int moduleId);

	/* void deleteByUserId(String userId); */
    void deleteByUserIdAndModuleId(String userId, int moduleId);
    
    
    @Modifying
    @Transactional
    @Query("UPDATE useractionpermissionmodel u " +
           "SET u.isDelete = 1, " +
           "u.deletedBy = :deletedBy, " +
           "u.deletedDate = CURRENT_TIMESTAMP " +
           "WHERE u.userId = :userId " +
           "AND u.moduleId = :moduleId " +
           "AND u.action = :action " +
           "AND u.isDelete = 0")
    void softDelete(@Param("userId") String userId,
                    @Param("moduleId") int moduleId,
                    @Param("action") String action,
                    @Param("deletedBy") String deletedBy);
    
}
