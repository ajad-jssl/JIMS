package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.JIMS.MIS.model.Username;

import java.util.List;

public interface UsersRepository extends JpaRepository<Username, Long> {
	

    @Query(value = """
        SELECT user_id, user_fullname, user_name 
        FROM Users 
        WHERE user_id NOT IN (SELECT uid FROM usersupplier)
          AND deleted = 0
    """, nativeQuery = true)
    List<Object[]> findAvailableUsers();
    
    
    @Query("""
    	    SELECT u
    	    FROM Username u
    	    WHERE u.deleted = 0
    	      AND u.locked = 1
    	""")
    	List<Username> findActiveUnlockedUsers();
    @Query("""
    	    SELECT u
    	    FROM Username u
    	    WHERE u.deleted = 0
    	      AND u.inactive = 1
    	""")
    List<Username> findInactiveUser();
    
    @Query(value = """
            SELECT user_id, user_name
            FROM dbo.Users
            WHERE user_menus=',SC'
              AND deleted = 0
        """, nativeQuery = true)
        List<Object[]> findSubconUsers();
        
        
        
        
		@Query(value = """
					

				SELECT
    u.user_id,
    u.user_name
FROM MIS.dbo.Users u

LEFT JOIN JIMS.dbo.UserFactoryMappingJIMS ufm
    ON ufm.User_Id = CAST(u.user_id AS VARCHAR(50))

WHERE u.user_menus != ',SC'
    AND u.deleted = 0
    AND ufm.User_Id IS NULL

ORDER BY u.user_name;
				            """, nativeQuery = true)
		List<Object[]> findOnlyG2Users();
    
}
