package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.GloblaModule;

import jakarta.transaction.Transactional;

public interface GlobalModulesRepository extends JpaRepository<GloblaModule, Long> {
	List<GloblaModule> findByStatusOrderByModuleIDAsc(Integer status);

    boolean existsByModuleNameIgnoreCaseAndStatus(
            String moduleName,
            Integer status);

    boolean existsByModuleNameIgnoreCaseAndStatusAndModuleIDNot(
            String moduleName,
            Integer status,
            Long moduleID);
    
    
    
    @Query(value =
            "SELECT " +
            "pg.PageID AS pageID, " +
            "md.ModuleName AS moduleName, " +
            "pg.PageName AS pageName, " +
            "pg.DisplayName AS displayName " +
            "FROM Pages pg " +
            "INNER JOIN Modules md ON pg.ModuleID = md.ModuleID",
            nativeQuery = true)
    List<Object[]> getAllPages();
    
    
    
    @Query(value = "SELECT ModuleID, ModuleName FROM Modules", nativeQuery = true)
    List<Object[]> getAllModules();
    

    @Query(value = "select pageID,DisplayName from pages", nativeQuery = true)
    List<Object[]> getAllpagesac();
    
    
    @Query(value = "select ActionID,ActionName from Actions", nativeQuery = true)
    List<Object[]> getAllActions();
    
    
    @Query(value = """
            SELECT COUNT(*)
            FROM Pages
            WHERE
            (
                PageName = ?1
                OR DisplayName = ?2
            )
            AND ModuleID = ?3
            """, nativeQuery = true)
    int checkDuplicatePage(
            String pageName,
            String displayName,
            Integer moduleID
    );
    
    
    
    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO Pages
            (
                ModuleID,
                PageName,
                DisplayName,
                STATUS,
                CREATED_BY,
                CREATED_DATE
            )
            VALUES
            (
                ?1,
                ?2,
                ?3,
                0,
                ?4,
                GETDATE()
            )
            """, nativeQuery = true)
    void insertPage(
            Integer moduleID,
            String pageName,
            String displayName,
            String createdBy
    );
    
    
    
    @Query(value = """
            SELECT
                ModuleID,
                PageName,
                DisplayName
            FROM Pages
            WHERE PageID = ?1
            """, nativeQuery = true)
    List<Object[]> getPageById(Integer pageID);
    
    
    @Query(value = """
            SELECT COUNT(*)
            FROM Pages
            WHERE PageName = ?1
            AND PageID <> ?2
            """, nativeQuery = true)
    int checkDuplicatePageNameForUpdate(
            String pageName,
            Integer pageID
    );
    @Query(value = """
            SELECT COUNT(*)
            FROM Pages
            WHERE DisplayName = ?1
            AND PageID <> ?2
            """, nativeQuery = true)
    int checkDuplicateDisplayNameForUpdate(
            String displayName,
            Integer pageID
    );
    @Transactional
    @Modifying
    @Query(value = """
            UPDATE Pages
            SET
                ModuleID = ?1,
                PageName = ?2,
                DisplayName = ?3,
                MODIFIED_BY = ?4,
                MODIFIED_DATE = GETDATE()
            WHERE PageID = ?5
            """, nativeQuery = true)
    void updatePage(
            Integer moduleID,
            String pageName,
            String displayName,
            String modifiedBy,
            Integer pageID
    );
    
    
    @Query(value = """
    	    select 
    	        pg.ID,
    	        p.DisplayName,
    	        ac.ActionName
    	    from PageActions pg
    	    left join Pages p 
    	        on pg.PageID = p.PageID
    	    left join Actions ac 
    	        on pg.ActionID = ac.ActionID
    	""", nativeQuery = true)
    	List<Object[]> getAllPageActionsTable();
    	
    	
    	
    	// ================= INSERT PAGE ACTION =================

    	@Modifying
    	@Transactional
    	@Query(value = """
    	    
    	    INSERT INTO PageActions
    	    (
    	        PageID,
    	        ActionID,
    	        STATUS,
    	        CREATED_BY,
    	        CREATED_DATE
    	    )
    	    VALUES
    	    (
    	        :pageID,
    	        :actionID,
    	        0,
    	        :createdBy,
    	        GETDATE()
    	    )
    	    
    	    """, nativeQuery = true)
    	void insertPageAction(
    	        
    	        @Param("pageID") Integer pageID,
    	        
    	        @Param("actionID") Integer actionID,
    	        
    	        @Param("createdBy") String createdBy
    	);
    	
    	
    	// ================= CHECK DUPLICATE =================

    	@Query(value = """
    	    
    	    SELECT COUNT(*)
    	    FROM PageActions
    	    WHERE PageID = :pageID
    	    AND ActionID = :actionID
    	    
    	    """, nativeQuery = true)
    	int checkDuplicatePageAction(
    	        
    	        @Param("pageID") Integer pageID,
    	        
    	        @Param("actionID") Integer actionID
    	);
    	
    	
    	
    	@Query(value = """
    		    SELECT PageID, ActionID
    		    FROM PageActions
    		    WHERE ID = :id
    		    """, nativeQuery = true)
    		List<Object[]> getPageActionById(
    		        @Param("id") Integer id);



    		@Query(value = """
    		    SELECT COUNT(*)
    		    FROM PageActions
    		    WHERE PageID = :pageID
    		    AND ActionID = :actionID
    		    AND ID <> :id
    		    """, nativeQuery = true)
    		int checkDuplicatePageActionForUpdate(
    		        @Param("pageID") Integer pageID,
    		        @Param("actionID") Integer actionID,
    		        @Param("id") Integer id);



    		@Transactional
    		@Modifying
    		@Query(value = """
    		    UPDATE PageActions
    		    SET PageID = :pageID,
    		        ActionID = :actionID,
    		        MODIFIED_BY = :modifiedBy,
    		        MODIFIED_DATE = GETDATE()
    		    WHERE ID = :id
    		    """, nativeQuery = true)
    		void updatePageAction(
    		        @Param("pageID") Integer pageID,
    		        @Param("actionID") Integer actionID,
    		        @Param("modifiedBy") String modifiedBy,
    		        @Param("id") Integer id);
    
}
