package com.JIMS.integration.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.UserPermissionTabsModel;
import com.JIMS.integration.interfaces.PageActionProjection;

import jakarta.transaction.Transactional;

@Repository
public interface UserPermissionTabsRepository extends JpaRepository<UserPermissionTabsModel, Integer> {
//
//    @Query("SELECT t.tabName FROM UserPermissionTabsModel t " +
//           "WHERE t.userId = :userId AND t.moduleId = :moduleId " +
//           "AND t.pageId = :pageId AND t.isDelete = 0")
//    List<String> findTabs(@Param("userId") String userId,
//                          @Param("moduleId") int moduleId,
//                          @Param("pageId") int pageId);

	
	

	@Query("SELECT t.tabName FROM UserPermissionTabsModel t " +
		       "WHERE t.userId = :userId " +
		       "AND t.moduleId = :moduleId " +
		       "AND t.pageId = :pageId " +   // ✅ ADD THIS
		       "AND t.isDelete = 0")
		List<String> findTabsByUserIdAndModuleIdAndPageId(
		        @Param("userId") String userId,
		        @Param("moduleId") int moduleId,
		        @Param("pageId") int pageId);
	
	@Query(
		    value = "SELECT tm.TabName " +
		            "FROM UserPermissionTabs tb " +
		            "INNER JOIN TabsMaster tm ON tb.Tabid = tm.TabID " +
		            "WHERE tb.UserID = :userId " +
		            "AND tb.pageId = :pageId " +
		            "AND tb.ModuleID = :moduleId " +
		            "AND tb.Is_Delete = 0",
		    nativeQuery = true
		)
		List<String> findTabsByUserIdAndModuleIdAndPageIdInVoceModule(
		        @Param("userId") String userId,
		        @Param("moduleId") String moduleId,
		        @Param("pageId") String pageId
		);
    @Modifying
    @Transactional
    @Query("UPDATE UserPermissionTabsModel t SET t.isDelete = 1, " +
           "t.deletedBy = :deletedBy, t.deletedDate = CURRENT_TIMESTAMP " +
           "WHERE t.userId = :userId AND t.moduleId = :moduleId " +
           "AND t.pageId = :pageId AND t.tabName = :tabName AND t.isDelete = 0")
    void softDeleted(@Param("userId") String userId,
                    @Param("moduleId") int moduleId,
                    @Param("pageId") int pageId,
                    @Param("tabName") String tabName,
                    @Param("deletedBy") String deletedBy);
    
    
    
    @Query(value = "SELECT TabID, TabName FROM TabsMaster", nativeQuery = true)
    List<Map<String, Object>> getAllTabs();

    @Query(value = "SELECT page_ID, Tab_id, Page_Name FROM TabsMasterPages WHERE Tab_id = :tabID", nativeQuery = true)
    List<Map<String, Object>> getPagesByTabID(@Param("tabID") int tabID);
    
    
    @Query(value = """
            SELECT DISTINCT
    tm.TabID,
    tm.TabName,
    tp.page_ID,
    tp.Page_Name
FROM UserPermissionTabs ut
JOIN TabsMaster tm ON ut.TabID = tm.TabID
JOIN TabsMasterPages tp ON tp.Tab_id = tm.TabID
LEFT JOIN UserTabPageActions ap 
    ON ap.TabID = tp.Tab_id 
    AND ap.TabPageID = tp.page_ID
    AND ap.UserID = ut.UserID
WHERE ut.UserID = :userId
  AND ut.Is_Delete = 0
  AND (ap.Is_Delete = 0 OR ap.Is_Delete IS NULL)
ORDER BY tm.TabID, tp.page_ID;
        """, nativeQuery = true)
        List<Object[]> findUserTabPages(@Param("userId") String userId);
    
        
        
        
        
        
        
        
        
        
        
        @Query(value = "SELECT u.ActionName " +
                "FROM UserTabPageActions u " +
                "JOIN TabsMasterPages t ON u.TabPageId = t.page_ID " +
                "WHERE u.UserID = :userId " +
                "AND u.Is_Delete = 0 " +
                "AND t.Page_Name = :pageName",
                nativeQuery = true)
        List<String> getActionsByUserAndPageName(@Param("userId") String userId,
                                                 @Param("pageName") String pageName);

        
        
        
        
        @Query(value = 
                "DECLARE @UserID VARCHAR(50) =:userId;\r\n"
                + "\r\n"
                + "SELECT *\r\n"
                + "FROM\r\n"
                + "(\r\n"
                + "\r\n"
                + "    \r\n"
                + "    SELECT\r\n"
                + "        upa.UserID,\r\n"
                + "        m.ModuleName,\r\n"
                + "        m.ModuleID,\r\n"
                + "       REPLACE(p.PageName, '.jsp', '') AS PageName,\r\n"
                + "        NULL AS TabName,\r\n"
                + "        NULL AS SubPageName,\r\n"
                + "\r\n"
                + "        STUFF((\r\n"
                + "            SELECT ', ' + x.ActionName\r\n"
                + "            FROM\r\n"
                + "            (\r\n"
                + "                SELECT DISTINCT upa2.ActionName,\r\n"
                + "                    CASE upa2.ActionName\r\n"
                + "                        WHEN 'Insert' THEN 1\r\n"
                + "                        WHEN 'Edit' THEN 2\r\n"
                + "                        WHEN 'view' THEN 3\r\n"
                + "                        WHEN 'Verify' THEN 4\r\n"
                + "                        WHEN 'Cancel' THEN 5\r\n"
                + "                        WHEN 'Print' THEN 6\r\n"
                + "                        WHEN 'Status' THEN 7\r\n"
                + "                        ELSE 100\r\n"
                + "                    END AS SortOrder\r\n"
                + "                FROM UserPageActions upa2\r\n"
                + "                WHERE upa2.UserID = upa.UserID\r\n"
                + "                  AND upa2.PageID = upa.PageID\r\n"
                + "                  AND upa2.Is_Deleted = 0\r\n"
                + "            ) x\r\n"
                + "            ORDER BY x.SortOrder\r\n"
                + "            FOR XML PATH(''), TYPE\r\n"
                + "        ).value('.', 'NVARCHAR(MAX)'), 1, 2, '') AS Actions\r\n"
                + "\r\n"
                + "    FROM UserPageActions upa\r\n"
                + "\r\n"
                + "    JOIN Modules m\r\n"
                + "        ON m.ModuleID = upa.ModuleID\r\n"
                + "\r\n"
                + "    JOIN Pages p\r\n"
                + "        ON p.PageID = upa.PageID\r\n"
                + "\r\n"
                + "    WHERE upa.Is_Deleted = 0\r\n"
                + "      AND upa.PageID <> 2\r\n"
                + "      AND upa.UserID = @UserID\r\n"
                + "\r\n"
                + "    GROUP BY\r\n"
                + "        upa.UserID,\r\n"
                + "        m.ModuleName,\r\n"
                + "        m.ModuleID,\r\n"
                + "        p.PageName,\r\n"
                + "        upa.PageID\r\n"
                + "\r\n"
                + "    UNION ALL\r\n"
                + "\r\n"
                + "    SELECT\r\n"
                + "        utpa.UserID,\r\n"
                + "        m.ModuleName,\r\n"
                + "        m.ModuleID,\r\n"
                + "    REPLACE(p.PageName, '.jsp', '') AS PageName,\r\n"
                + "        tm.TabName,\r\n"
                + "      REPLACE(tmp.Page_Name, '.jsp', '') AS SubPageName,\r\n"
                + "        STUFF((\r\n"
                + "            SELECT ', ' + y.ActionName\r\n"
                + "            FROM\r\n"
                + "            (\r\n"
                + "                SELECT DISTINCT utpa2.ActionName,\r\n"
                + "                    CASE utpa2.ActionName\r\n"
                + "                        WHEN 'Insert' THEN 1\r\n"
                + "                        WHEN 'Edit' THEN 2\r\n"
                + "                        WHEN 'view' THEN 3\r\n"
                + "                        WHEN 'Verify' THEN 4\r\n"
                + "                        WHEN 'Cancel' THEN 5\r\n"
                + "                        WHEN 'Print' THEN 6\r\n"
                + "                        WHEN 'Status' THEN 7\r\n"
                + "                        ELSE 100\r\n"
                + "                    END AS SortOrder\r\n"
                + "                FROM UserTabPageActions utpa2\r\n"
                + "                WHERE utpa2.UserID = utpa.UserID\r\n"
                + "                  AND utpa2.TabID = utpa.TabID\r\n"
                + "                  AND utpa2.TabPageID = utpa.TabPageID\r\n"
                + "                  AND utpa2.Is_Delete = 0\r\n"
                + "            ) y\r\n"
                + "            ORDER BY y.SortOrder\r\n"
                + "            FOR XML PATH(''), TYPE\r\n"
                + "        ).value('.', 'NVARCHAR(MAX)'), 1, 2, '') AS Actions\r\n"
                + "\r\n"
                + "    FROM UserTabPageActions utpa\r\n"
                + "\r\n"
                + "    JOIN TabsMaster tm\r\n"
                + "        ON tm.TabID = utpa.TabID\r\n"
                + "\r\n"
                + "    JOIN TabsMasterPages tmp\r\n"
                + "        ON tmp.Page_ID = utpa.TabPageID\r\n"
                + "\r\n"
                + "    JOIN Pages p\r\n"
                + "        ON p.PageID = 2\r\n"
                + "\r\n"
                + "    JOIN Modules m\r\n"
                + "        ON m.ModuleID = p.ModuleID\r\n"
                + "\r\n"
                + "    WHERE utpa.Is_Delete = 0\r\n"
                + "      AND utpa.UserID = @UserID\r\n"
                + "\r\n"
                + "    GROUP BY\r\n"
                + "        utpa.UserID,\r\n"
                + "        m.ModuleName,\r\n"
                + "        m.ModuleID,\r\n"
                + "        p.PageName,\r\n"
                + "        tm.TabName,\r\n"
                + "        tmp.Page_Name,\r\n"
                + "        utpa.TabID,\r\n"
                + "        utpa.TabPageID\r\n"
                + "\r\n"
                + ") FinalResult\r\n"
                + "\r\n"
                + "ORDER BY\r\n"
                + "    ModuleID ASC,\r\n"
                + "    PageName ASC,\r\n"
                + "    TabName ASC,\r\n"
                + "    SubPageName ASC;",
                nativeQuery = true)
                
            List<Object[]> getUserAccessReport(@Param("userId") String userId);
            
            
            
            
            @Query(value="select  id, user_id,is_active,dep.Department_name from USERS_MASTER um left join TAB_DEPARTMENTS dep on um.Department  = dep.Dept_id where is_delete=0",nativeQuery = true)
            List<Object[]>getAllUser();

            
            
            
            
            @Query(value = """

                    SELECT
                        Step,
                        TransactionDate,
                        UserID,
                        ActivityType,
                        ModuleName,
                        PageID,
                        PageName,
                        TabID,
                        TabPageID,
                        MainPage,
                        TabName,
                        SubPageName,
                        ChangedAction,
                        Actions,
                        ActionStatus,
                        Username,
                        Email,
                        first_name,
                        last_name,
                        is_active,
                        is_admin_locked,
                        is_delete,
                        factory_id,
                        location,
                        report_id,
                        report_user_id,
                        Department,
                        Department_name,
                        MainRemarks,
                        performed_by_user_id

                    FROM VW_COMPLETE_USER_ACTIVITY_HISTORY

                    WHERE
                    (
                        :userId = 'ALL'
                        OR UserID = :userId
                    )

                    AND CAST(TransactionDate AS DATE)
                    BETWEEN :fromDate AND :toDate

                    ORDER BY TransactionDate asc

                    """, nativeQuery = true)

            List<Object[]> getHistoryReport(

                    @Param("userId") String userId,

                    @Param("fromDate") Date fromDate,

                    @Param("toDate") Date toDate

            );
            
            

            @Query(value = """
                SELECT 
                    pa.PageID as pageId,
                    a.ActionID as actionId,
                    a.ActionName as actionName
                FROM PageActions pa
                JOIN Actions a ON pa.ActionID = a.ActionID
                WHERE pa.PageID = :pageId
                """, nativeQuery = true)
            List<PageActionProjection> getActionsByPageId(@Param("pageId") int pageId);
            
            
            
            
            
            @Query(value = """
                    SELECT 
                        tab.PageID as pageId,
                        act.ActionName as actionName
                    FROM TabPageActions tab
                    INNER JOIN Actions act 
                        ON tab.ActionID = act.ActionID
                    WHERE tab.PageID = :pageId
                    """, nativeQuery = true)
                List<Object[]> findActionsByPageId(int pageId);
}