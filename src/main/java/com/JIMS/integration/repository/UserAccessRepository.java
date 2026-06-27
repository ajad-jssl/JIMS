package com.JIMS.integration.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.User;

@Repository
public interface UserAccessRepository extends JpaRepository<User, Long> {

    @Query(value = """
        SELECT 
            m.ModuleName AS moduleName,
            p.PageName AS pageName,

            STUFF((
                SELECT ', ' + uap2.Action
                FROM UserActionPermissions uap2
                WHERE uap2.Module_ID = m.ModuleID
                  AND uap2.User_ID = up.UserID
                  AND uap2.Is_Delete = 0
                FOR XML PATH(''), TYPE
            ).value('.', 'NVARCHAR(MAX)'), 1, 2, '') AS permissions

        FROM UserPermissions up

        JOIN Pages p ON up.PageID = p.PageID
        JOIN Modules m ON p.ModuleID = m.ModuleID

        WHERE up.UserID = :userId
          AND up.Is_Delete = 0

        GROUP BY m.ModuleName, p.PageName, m.ModuleID, up.UserID
        ORDER BY m.ModuleName, p.PageName
    """, nativeQuery = true)
    List<Map<String, Object>> getUserAccessReport(@Param("userId") String userId);
}
