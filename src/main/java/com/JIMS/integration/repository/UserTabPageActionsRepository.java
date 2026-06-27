package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.UserTabPageActionsModel;

import jakarta.transaction.Transactional;

@Repository
public interface UserTabPageActionsRepository extends JpaRepository<UserTabPageActionsModel, Integer> {

    @Query("SELECT a FROM UserTabPageActionsModel a " +
           "WHERE a.userId = :userId AND a.isDelete = 0")
    List<UserTabPageActionsModel> findByUser(@Param("userId") String userId);

    
    @Modifying
    @Transactional
    @Query("UPDATE UserTabPageActionsModel a SET a.isDelete = 1, " +
           "a.deletedBy = :deletedBy, a.deletedDate = CURRENT_TIMESTAMP " +
           "WHERE a.userId = :userId " +
           "AND a.tabId = :tabId " +
           "AND a.tabPageId = :tabPageId " +
           "AND a.actionName = :actionName " +
           "AND a.isDelete = 0")
    void softDelete(@Param("userId") String userId,
                    @Param("tabId") int tabId,
                    @Param("tabPageId") int tabPageId,
                    @Param("actionName") String actionName,
                    @Param("deletedBy") String deletedBy);
}
