package com.JIMS.integration.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.UserPermissionsmodel;

import jakarta.transaction.Transactional;
@Repository
public interface UserPermissionsinterface extends JpaRepository<UserPermissionsmodel, Long> {
	 void deleteByUserId(String userId);

	List<UserPermissionsmodel> findByUserId(String userId);
	@Query("SELECT up.pageId FROM UserPermissionsmodel up WHERE up.userId = ?1 AND up.isDelete =0")
    List<Integer> findPageIdsByUserId(String userId);
	@Query("SELECT up.pageId FROM UserPermissionsmodel up WHERE up.userId = ?1 AND up.moduleId = ?2 AND up.isDelete =0")
     List<Integer> findPageIdsByUserIdAndModuleId(String userId, Integer moduleId);
	
	
	@Query("SELECT up.pageId FROM UserPermissionsmodel up WHERE up.userId = :userId AND up.moduleId = :moduleId AND up.isDelete =0")
    List<Integer> findPageIdsByUserIdAndModuleIdss(String userId, Integer moduleId); 
	
	
	
	@Query("SELECT DISTINCT u.moduleId FROM UserPermissionsmodel u WHERE u.userId = :userId")
	List<Integer> findModuleIdsByUserId(@Param("userId") String userId);
	
	@Query(value = """
		    SELECT p.PageName, m.ModuleName,  m.ModuleID,p.DisplayName
		    FROM UserPermissions up 
		    JOIN Pages p ON up.PageID = p.PageID 
		    JOIN Modules m ON p.ModuleID = m.ModuleID 
		    WHERE up.UserID = :userId AND up.Is_Delete =0 order by m.ModuleId asc
		    """, nativeQuery = true)
		List<Object[]> findPagesWithModulesByUserId(@Param("userId") String userId);

		@Query(value = """
			    SELECT p.PageName, m.ModuleName, m.ModuleID,p.DisplayName
			    FROM UserPermissions up 
			    JOIN Pages p ON up.PageID = p.PageID 
			    JOIN Modules m ON p.ModuleID = m.ModuleID 
			    WHERE up.UserID = :userId and m.ModuleName= :moduleName and up.Is_Delete =0 order by p.pageID asc
			    """, nativeQuery = true)
			List<Object[]> findModulepagesByUserId(@Param("userId") String userId, @Param("moduleName") String moduleName);

			void deleteByUserIdAndModuleId(String userId, int moduleId);
			
			
			@Modifying
			@Transactional
			@Query("UPDATE UserPermissionsmodel u " +
			       "SET u.isDelete = 1, " +
			       "u.deletedBy = :deletedBy, " +
			       "u.deletedDate = CURRENT_TIMESTAMP " +
			       "WHERE u.userId = :userId " +
			       "AND u.moduleId = :moduleId " +
			       "AND u.pageId = :pageId " +
			       "AND u.isDelete = 0")
			int softDelete(@Param("userId") String userId,
			                @Param("moduleId") int moduleId,
			                @Param("pageId") int pageId,
			                @Param("deletedBy") String deletedBy);

			
			

}