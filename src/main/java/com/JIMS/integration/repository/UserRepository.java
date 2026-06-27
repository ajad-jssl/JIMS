package com.JIMS.integration.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.JIMS.integration.entity.User;
import com.JIMS.integration.interfaces.RoleAssignModule_SubModule_Actions;
import com.JIMS.integration.interfaces.UserInterface;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "SELECT u.id, u.username, u.user_id, r.role_code as role, r.role_id, u.factory_id FROM USERS_MASTER AS u "
			+ "LEFT OUTER JOIN ROLE_MASTER AS r ON r.role_id = u.role_id "
			+ "WHERE u.is_active = 1  AND u.is_admin_locked = 0 and u.is_delete =0  AND u.username = :username AND u.password = :encryptedPassword", nativeQuery = true)
	UserInterface getUserLogin(String username, String encryptedPassword);

//	@Query(value = "select is_loggedin from USERS_MASTER where user_id = :user_id and password = :encryptedPassword", nativeQuery = true)
//	boolean checkWhetherUserIsLoggedin(String user_id, String encryptedPassword);

	
	@Query(value = """
		    SELECT CASE 
		        WHEN is_loggedIn = 1 
		             AND last_seen >= DATEADD(MINUTE, -3, GETDATE())
		        THEN 1 
		        ELSE 0 
		    END
		    FROM USERS_MASTER 
		    WHERE user_id = :user_id 
		    AND password = :encryptedPassword
		    """, nativeQuery = true)
		Integer checkWhetherUserIsLoggedin(String user_id, String encryptedPassword);
	
	@Query(value = """
		    SELECT is_loggedIn, last_seen
		    FROM USERS_MASTER
		    WHERE user_id = :user_id
		    AND password = :encryptedPassword
		    """, nativeQuery = true)
		List<Object[]> getUserLoginStatus(
		    @Param("user_id") String user_id,
		    @Param("encryptedPassword") String encryptedPassword
		);
	
		
		@Query(value = """
			    SELECT COUNT(*)
			    FROM USERS_MASTER
			    WHERE user_id = :user_id
			    """, nativeQuery = true)
			int checkUserExists(@Param("user_id") String user_id);
		
	@Modifying
	@Transactional
	@Query(value = """
	    UPDATE USERS_MASTER
	    SET last_seen = GETDATE()
	    WHERE user_id = :user_id
	    """, nativeQuery = true)
	void updateLastSeen(@Param("user_id") String user_id);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER set is_loggedin = 1 ,last_login_date = GETDATE(),last_seen = GETDATE(), login_attempt_count = 0  where user_id = :user_id", nativeQuery = true)
	int updateLoggedIn(String user_id);

	@Query(value = "select is_active from USERS_MASTER where user_id = :user_id", nativeQuery = true)
	boolean isActive(String user_id);
	
	
	@Query(value = "select is_password_expired from USERS_MASTER where user_id = :user_id", nativeQuery = true)
	boolean isPasswordExpries(String user_id);
	

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET login_attempt_count = :login_attempt_count where username = :username", nativeQuery = true)
	int updateLoginCount(String username, int login_attempt_count);
	
	
	@Transactional
	@Modifying
	@Query(value = """
	    UPDATE USERS_MASTER 
	    SET login_attempt_count = ISNULL(login_attempt_count, 0) + 1
	    WHERE username = :username 
	    AND ISNULL(login_attempt_count, 0) < 5
	""", nativeQuery = true)
	int incrementLoginCount(String username);

	
	
	
	@Query(value = "select login_attempt_count from USERS_MASTER where username = :username", nativeQuery = true)
	Integer getLogInCount(String username);

	
	
	@Query(value = "select is_active from USERS_MASTER where username = :username", nativeQuery = true)
	boolean getUserStatus(String username);

	
	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER \r\n"
			+ "SET is_admin_locked = 1 \r\n"
			+ "WHERE username = :username \r\n"
			+ "AND ISNULL(is_admin_locked, 0) <> 1;", nativeQuery = true)
	int updateIsActiveBasedOnUnsuccessfullogin(String username);

	@Transactional
	@Modifying
	@Query(value = "\r\n"
			+ "UPDATE USERS_MASTER\r\n"
			+ "SET is_active = 0\r\n"
			+ "WHERE last_login_date IS NOT NULL\r\n"
			+ "AND  last_login_date <= DATEADD(\r\n"
			+ "    DAY,\r\n"
			+ "    -(SELECT CAST(CONFIG_VALUE AS INT)\r\n"
			+ "      FROM GLOBAL_TABLE\r\n"
			+ "      WHERE CONFIG_KEY = 'inactive_user_days'),\r\n"
			+ "    GETDATE()\r\n"
			+ ")", nativeQuery = true)
	int serUsersInactive();

	
	
	
	
	@Transactional
	@Modifying
	@Query(value = 
	"	   UPDATE USERS_MASTER\r\n"
	+ "SET is_password_expired = 1\r\n"
	+ "WHERE is_password_expired = 0\r\n"
	+ "AND password_modified_time IS NOT NULL\r\n"
	+ "AND password_modified_time <= DATEADD(\r\n"
	+ "    DAY,\r\n"
	+ "    -(SELECT CAST(CONFIG_VALUE AS INT)\r\n"
	+ "      FROM GLOBAL_TABLE\r\n"
	+ "      WHERE CONFIG_KEY = 'password_expiry_days'),\r\n"
	+ "    GETDATE()\r\n"
	+ ")\r\n"
	+ "",
	nativeQuery = true)
	int expireUserPasswords();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET login_attempt_count = 0 WHERE user_id = :user_id AND login_attempt_count <> 0", nativeQuery = true)
	int updateLoginCountToZero(String user_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER set is_loggedin = 0 where user_id = :user_id", nativeQuery = true)
	int userLogout(String user_id);

	@Transactional
	@Modifying
	@Query(value = """
	    INSERT INTO USERS_MASTER 
	    (
	        user_id,
	        password,
	        username,
	        email,
	        mobile_number,
	        created_by,
	        created_date,
	        password_modified_time,
	        role_id,
	        Department,
	        first_name,
	        last_name,
	        report_id,
	        location,
	        factory_id
	    ) 
	    VALUES 
	    (
	        :user_id,
	        :encryptPassword,
	        :username,
	        :email,
	        :mobile_number,
	        :created_by,
	        GETDATE(),
	        GETDATE(),
	        :role_id,
	        :Department,
	        :first_name,
	        :last_name,
	        :report_id,
	        :location,
	        :factory_id
	    )
	""", nativeQuery = true)
	int userCreateUser(
	        String user_id,
	        String encryptPassword,
	        String username,
	        String email,
	        String mobile_number,
	        String created_by,
	        String role_id,
	        String Department,
	        String first_name,
	        String last_name,
	        Integer report_id,
	        Integer location,
	        String factory_id   // ✅ NEW
	);

	
//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE USERS_MASTER SET user_id=:user_id, password=:encryptPassword, username=:username, email=:email, mobile_number=:mobile_number, modified_by=:created_by, modified_date=GETDATE(), role_id=:role_id, Department=:department_id WHERE id =:id", nativeQuery = true)
//	int userUpdateUser(String user_id, String encryptPassword, String username, String email, String mobile_number,
//	                   String created_by, String role_id, String id ,String department_id);

//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE USERS_MASTER SET user_id=:user_id, password=:encryptPassword, username=:username, email=:email, mobile_number=:mobile_number, modified_by=:created_by, modified_date=GETDATE(), role_id=:role_id, Department=:department_id WHERE id=:id", nativeQuery = true)
//	int userUpdateUserWithPassword(String user_id, String encryptPassword, String username, String email, String mobile_number,
//	                               String created_by, String role_id, String id, String department_id);

	
	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET " +
	        "user_id=:user_id, " +
	        "password=:encryptPassword, " +
	        "username=:username, " +
	        "email=:email, " +
	        "mobile_number=:mobile_number, " +
	        "modified_by=:modified_by, " +  
	        "modified_date=GETDATE(), " +
	        "password_modified_time=GETDATE(),"+
	        "role_id=:role_id, " +
	        "Department=:department, " +
	        "first_name=:first_name, " +
	        "last_name=:last_name, " +
	        "location=:location, " +
	        "report_id=:report_id, " +
	        "factory_id=:factory_id " +
	        "WHERE id=:id", nativeQuery = true)
	int userUpdateUserWithPassword(
	        String user_id,
	        String encryptPassword,
	        String username,
	        String email,
	        String mobile_number,
	        String modified_by,
	        String role_id,
	        String id,
	        String department,
	        String first_name,
	        String last_name,
	        Integer location,
	        Integer report_id,
	        String factory_id
	);

	
//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE USERS_MASTER SET user_id=:user_id, username=:username, email=:email, mobile_number=:mobile_number, modified_by=:created_by, modified_date=GETDATE(), role_id=:role_id, Department=:department_id WHERE id=:id", nativeQuery = true)
//	int userUpdateUserWithoutPassword(String user_id, String username, String email, String mobile_number,
//	                                  String created_by, String role_id, String id, String department_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET " +
	        "user_id=:user_id, " +
	        "username=:username, " +
	        "email=:email, " +
	        "mobile_number=:mobile_number, " +
	        "modified_by=:modified_by, " +  
	        "modified_date=GETDATE(), " +
	        "role_id=:role_id, " +
	        "Department=:department, " +
	        "first_name=:first_name, " +
	        "last_name=:last_name, " +
	        "location=:location, " +
	        "report_id=:report_id, " +
	        "factory_id=:factory_id " +
	        "WHERE id=:id", nativeQuery = true)
	int userUpdateUserWithoutPassword(
	        String user_id,
	        String username,
	        String email,
	        String mobile_number,
	        String modified_by,
	        String role_id,
	        String id,
	        String department,
	        String first_name,
	        String last_name,
	        Integer location,
	        Integer report_id,
	        String factory_id
	);


	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET modified_by = :modified_by, modified_date=GETDATE(),is_admin_locked = 1 where id = :id", nativeQuery = true)
	int userBlockUser(String id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET modified_by = :modified_by, modified_date=GETDATE(),is_active = 1 where id = :id", nativeQuery = true)
	int userActive(String id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET modified_by = :modified_by, modified_date=GETDATE(),is_delete = 1 where id = :id", nativeQuery = true)
	int userDelete(String id, String modified_by);

	@Query(value = " select um.*,d.Department_name as dept1 from USERS_MASTER um full join TAB_DEPARTMENTS d on d.Dept_id=um.Department where um.is_delete = 0", nativeQuery = true)
	List<UserInterface> listUsersss();
	
	
	
//	After pagging repo
	
	
	@Query(
		    value = """
		        select um.*, d.Department_name as dept1
		        from USERS_MASTER um
		        full join TAB_DEPARTMENTS d on d.Dept_id = um.Department
		        where um.is_delete = 0
		        and (:search IS NULL OR :search = '' 
		             OR LOWER(um.first_name) LIKE LOWER(CONCAT('%', :search, '%'))
		             OR LOWER(um.last_name) LIKE LOWER(CONCAT('%', :search, '%'))
		             OR LOWER(um.username) LIKE LOWER(CONCAT('%', :search, '%'))
		             OR LOWER(d.Department_name) LIKE LOWER(CONCAT('%', :search, '%'))
		        )
		        """,
		    countQuery = """
		        select count(*)
		        from USERS_MASTER um
		        full join TAB_DEPARTMENTS d on d.Dept_id = um.Department
		        where um.is_delete = 0
		        and (:search IS NULL OR :search = '' 
		             OR LOWER(um.first_name) LIKE LOWER(CONCAT('%', :search, '%'))
		             OR LOWER(um.last_name) LIKE LOWER(CONCAT('%', :search, '%'))
		             OR LOWER(um.username) LIKE LOWER(CONCAT('%', :search, '%'))
		             OR LOWER(d.Department_name) LIKE LOWER(CONCAT('%', :search, '%'))
		        )
		        """,
		    nativeQuery = true
		)
		Page<UserInterface> listUsers(@Param("search") String search, Pageable pageable);

	@Query(value = "select um.*,d.Department_name as dept1 from USERS_MASTER um full join TAB_DEPARTMENTS d on d.Dept_id=um.Department where um.id = :id", nativeQuery = true)
	UserInterface serachUserById(String id);

	
	/*
	 * @Query(value = "SELECT \r\n" + "    um.username,\r\n" + "    um.email,\r\n" +
	 * "    rm.role_name,\r\n" + "    mm.module_id,\r\n" + "    mm.module_name,\r\n"
	 * + "    sm.id AS submodule_id,\r\n" + "    sm.submodule_name,\r\n" +
	 * "    MAX(ram.action_create) AS action_create,\r\n" +
	 * "    MAX(ram.action_delete) AS action_delete,\r\n" +
	 * "    MAX(ram.action_read) AS action_read,\r\n" +
	 * "    MAX(ram.action_reject) AS action_reject,\r\n" +
	 * "    MAX(ram.action_release) AS action_release,\r\n" +
	 * "    MAX(ram.action_update) AS action_update,\r\n" +
	 * "    MAX(ram.action_verify) AS action_verify,\r\n" +
	 * "    MAX(ram.all_actions) AS actions_list\r\n" +
	 * "FROM USERS_MASTER um   \r\n" + "INNER JOIN ROLE_MASTER rm \r\n" +
	 * "    ON rm.role_id = um.role_id  \r\n" +
	 * "INNER JOIN ROLEASSIGN_MASTER ram \r\n" +
	 * "    ON ram.role_id = um.role_id  \r\n" + "INNER JOIN MODULE_MASTER mm \r\n"
	 * + "    ON mm.module_id = ram.module_id  \r\n" +
	 * "INNER JOIN SUBMODULE_MASTER sm \r\n" +
	 * "    ON sm.module_id = mm.module_id \r\n" + "WHERE um.user_id = :user_id\r\n"
	 * + "GROUP BY \r\n" + "    um.username, \r\n" + "    um.email, \r\n" +
	 * "    rm.role_name, \r\n" + "    mm.module_id, \r\n" +
	 * "    mm.module_name, \r\n" + "    sm.id, \r\n" + "    sm.submodule_name;\r\n"
	 * + "", nativeQuery = true) List<RoleAssignModule_SubModule_Actions>
	 * getLoginRoleCredtianls(String user_id);
	 */
	
	@Query(value = "SELECT \r\n"
			+ "    um.user_id, \r\n"
			+ "    um.username, \r\n"
			+ "    um.email, \r\n"
			+ "    rr.role_name, \r\n"
			+ "    mm.module_id, \r\n"
			+ "    mm.module_name, \r\n"
			+ "    sm.id AS submodule_id, \r\n"
			+ "    sm.submodule_name, \r\n"
			+ "    STUFF(\r\n"
			+ "        (SELECT ', ' + rm2.all_actions \r\n"
			+ "         FROM ROLEASSIGN_MASTER rm2 \r\n"
			+ "         WHERE rm2.role_id = rm.role_id \r\n"
			+ "         AND rm2.module_id = rm.module_id \r\n"
			+ "         AND rm2.submodule_id = rm.submodule_id\r\n"
			+ "         FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),\r\n"
			+ "        1, 2, ''\r\n"
			+ "    ) AS actions_list \r\n"
			+ "FROM USERS_MASTER um\r\n"
			+ "INNER JOIN ROLEASSIGN_MASTER rm ON rm.role_id = um.role_id\r\n"
			+ "INNER JOIN ROLE_MASTER rr ON rr.role_id = rm.role_id\r\n"
			+ "INNER JOIN MODULE_MASTER mm ON mm.module_id = rm.module_id\r\n"
			+ "INNER JOIN SUBMODULE_MASTER sm ON sm.id = rm.submodule_id\r\n"
			+ "WHERE um.user_id = :user_id\r\n"
			+ "GROUP BY \r\n"
			+ "    um.user_id, \r\n"
			+ "    um.username, \r\n"
			+ "    um.email, \r\n"
			+ "    rr.role_name, \r\n"
			+ "    mm.module_id, \r\n"
			+ "    mm.module_name, \r\n"
			+ "    sm.id, \r\n"
			+ "    sm.submodule_name, \r\n"
			+ "    rm.role_id, \r\n"
			+ "    rm.module_id, \r\n"
			+ "    rm.submodule_id\r\n"
			+ "", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> getLoginRoleCredtianls(String user_id);

	
	@Query(value="select password from USERS_MASTER WHERE email = :email_id", nativeQuery = true)
	String getExistingPassword(String email_id);
	
//	@Query(value="select user_id from USERS_MASTER WHERE email = :email_id", nativeQuery = true)
//	String getUser_id(String email_id);

	
	
	@Query(value="select user_name  from [MIS].[dbo].[Users]  where user_email =:email_id and deleted=0", nativeQuery = true)
	String getUser_id(String email_id);
	
	@Query(value="select user_name  from [MIS].[dbo].[Users]  where user_email =:email_id and deleted=0", nativeQuery = true)
	String getid(String email_id);
	
	@Query(value="select user_password from [MIS].[dbo].[Users]  where user_email=:email_id and deleted=0", nativeQuery = true)
	String getPassword(String email_id);
	
	
//	 @Modifying
//	@Transactional
//	@Query(value="Update USERS_MASTER SET password = :confirmPassword, modified_by = :created_by,modified_date=GETDATE(),password_modified_time = GETDATE(),password_otp = 'NULL', is_loggedin = 0  where id = :created_by", nativeQuery = true)
//	int updatePasswordForUser(String confirmPassword, String created_by);
	
	
//	
//	 @Modifying
//	@Transactional
//	@Query(value="Update [MIS].[dbo].[Users] SET user_password = :confirmPassword,user_password_last_changed = GETDATE(),password_otp = 'NULL' where user_name = :created_by and deleted=0", nativeQuery = true)
//	int updatePasswordForUser(String confirmPassword, String created_by);
	
	
	@Modifying
	@Transactional
	@Query(value =
	       "UPDATE MIS.dbo.Users " +
	       "SET user_password = :confirmPassword, " +
	       "user_password_last_changed = GETDATE(), " +
	       "password_otp = NULL, " +
	       "otp_timevalidation = NULL " +
	       "WHERE user_name = :created_by " +
	       "AND deleted = 0",
	       nativeQuery = true)
	int updatePasswordForUser(
	        String confirmPassword,
	        String created_by);
	
	
	

//	 @Modifying
//	 @Transactional
//	@Query(value="Update USERS_MASTER SET password_otp = :password_otp,otp_timevalidation= CURRENT_TIMESTAMP, created_by = :created_by where user_id = :created_by", nativeQuery = true)
//	int storeOtpTimestamp(int password_otp, String created_by);
	 
	 
	 
	 @Modifying
	 @Transactional
	@Query(value="\r\n"
			+ "update [MIS].[dbo].[Users] set password_otp= :password_otp,otp_timevalidation =CURRENT_TIMESTAMP  where user_name= :created_by and deleted=0", nativeQuery = true)
	int storeOtpTimestamp(int password_otp, String created_by);

	 
	 

//	@Query( value = "select password_otp,otp_timevalidation from USERS_MASTER where user_id = :created_by",nativeQuery = true)
//	UserInterface getOtpTimeStamp(String created_by);
	 
	 
		@Query( value = "select password_otp,otp_timevalidation from [MIS].[dbo].[Users] where user_name =  :created_by",nativeQuery = true)
		UserInterface getOtpTimeStamp(String created_by);
		
		

//	@Query(value = "select count(*) from USERS_MASTER where email = :email_id  and is_delete=0", nativeQuery = true)
//	int checkEmailExits(String email_id);
	
	
	@Query(value = "select count(*) from [MIS].[dbo].[Users] where user_email =:email_id and deleted=0", nativeQuery = true)
	int checkEmailExits(String email_id);
	
	
//	@Query(value = "select count(*) from USERS_MASTER where email = :email_id  and is_active=0", nativeQuery = true)
//	int checkEmailExitsStaus(String email_id);
	
	
	@Query(value = "select count(*) from [MIS].[dbo].[Users]  where user_email =:email_id  and inactive=1 and deleted=0", nativeQuery = true)
	int checkEmailExitsStaus(String email_id);
	

//	@Query(value = "select email from USERS_MASTER where password_otp = :otp", nativeQuery = true)
//	String getEmailId(String otp);
	
	
	
	@Query(value = "select user_email from [MIS].[dbo].[Users] where password_otp = :otp and deleted=0", nativeQuery = true)
	String getEmailId(String otp);

//	 @Modifying
//	 @Transactional
//	@Query(value="Update USERS_MASTER set password_otp = 'NULL' where email = :email ",nativeQuery = true)
//	void updateOtp(String email);
	 
	
//	 @Modifying
//	 @Transactional
//	@Query(value="\r\n"
//			+ "Update [MIS].[dbo].[Users]  set password_otp = 'NULL' where user_email = :email  and deleted=0",nativeQuery = true)
//	void updateOtp(String email);
	 
	 @Modifying
	 @Transactional
	 @Query(value =
	        "UPDATE MIS.dbo.Users " +
	        "SET password_otp = NULL " +
	        "WHERE user_email = :email " +
	        "AND deleted = 0",
	        nativeQuery = true)
	 int updateOtp(String email);
	
	 
	   @Query(value = "select   user_name  from [MIS].[dbo].[Users]  where user_email =:email and deleted=0", nativeQuery = true)
	    String findUsernameByEmail(@Param("email") String email);

		
	   @Query(value="\r\n"
		   	+ "  select location from USERS_MASTER where USER_ID =:user_id",nativeQuery = true)
		   Integer getLocationIdByUserId(@Param("user_id") String user_id);
	 
		 
}
