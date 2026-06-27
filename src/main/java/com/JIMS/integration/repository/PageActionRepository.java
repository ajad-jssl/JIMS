package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.UserPageAction;

import jakarta.transaction.Transactional;

@Repository
public interface PageActionRepository extends JpaRepository<UserPageAction, Integer> {

    @Query("SELECT a FROM UserPageAction a " +
           "WHERE a.userId = :userId AND a.isDeleted = 0")
    List<UserPageAction> findByUser(@Param("userId") String userId);


    @Modifying
    @Transactional
    @Query("UPDATE UserPageAction a SET a.isDeleted = 1, " +
           "a.deletedBy = :deletedBy, a.deletedDate = CURRENT_TIMESTAMP " +
           "WHERE a.userId = :userId " +
           "AND a.moduleId = :moduleId " +
           "AND a.pageId = :pageId " +
           "AND a.actionName = :actionName " +
           "AND a.isDeleted = 0")
    void softDelete(@Param("userId") String userId,
                    @Param("moduleId") int moduleId,
                    @Param("pageId") int pageId,
                    @Param("actionName") String actionName,
                    @Param("deletedBy") String deletedBy);
    
    
    
    @Query(value = """
    	    SELECT act.ActionName
    	    FROM UserPageActions act
    	    INNER JOIN pages pg ON act.PageID = pg.PageID
    	    WHERE act.UserID = :userId
    	      AND act.Is_Deleted = 0
    	      AND pg.PageName = :pageName
    	""", nativeQuery = true)
    	List<String> findActionsByUserAndPage(@Param("userId") String userId,
    	                                      @Param("pageName") String pageName);
}