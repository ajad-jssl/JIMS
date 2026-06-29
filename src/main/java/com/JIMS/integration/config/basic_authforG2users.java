package com.JIMS.integration.config;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.JIMS.MIS.model.SecondaryLoginRequest;
import com.JIMS.MIS.model.SecondaryLogoutRequest;

import com.JIMS.integration.controller.SeriesMasterNewController;
import com.JIMS.integration.controller.UserSupplierController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;



@CrossOrigin
@RestController
@RequestMapping("/api/proxy")
public class basic_authforG2users {
	
//	    private static final String SECONDARY_API_URL =
	  @Value("${secondary.api.username}")
	    private String secondaryApiUsername;

	    @Value("${secondary.api.password}")
	    private String secondaryApiPassword;

	    
	    @Autowired
	    private UserSupplierController user_supplier;
	    
	    @Autowired
	    private JdbcTemplate jdbcTemplate;
		@Autowired
		private JwtUtil jwtUtil;
		Logger logger = LogManager.getLogger(SeriesMasterNewController.class);
		
		
		@Autowired
		@Qualifier("misDataSource")
		private DataSource misDataSource;
		
		
		@Autowired
		@Qualifier("misDataSource2")
		private DataSource misDataSource2;
		
		@Autowired
		@Qualifier("jimsDataSource")
		private DataSource jimsDataSource;
		
		
		
		
	    @Autowired
	    private ApplicationEventPublisher eventPublisher;
		
	
	    @PostMapping(
	    	    value = "/secondary-login",
	    	    consumes = MediaType.APPLICATION_JSON_VALUE,
	    	    produces = MediaType.APPLICATION_JSON_VALUE
	    	)
	    	public ResponseEntity<String> fetchSecondaryUserData(
	    	        @RequestBody SecondaryLoginRequest request,
	    	        HttpServletResponse responses) {
	    	    try {
	    	        System.out.println("Proxy called with username: " + request.getUsername());

	    	        String encryptedPassword = encryptPasswordG2.encryptPassword(request.getPassword());
	    	        request.setPassword(encryptedPassword);

	    	        Optional<Map<String, Object>> dbUserOpt =
	    	                validateUserFromDB(
	    	                        request.getUsername(),
	    	                        request.getPassword()
	    	                );

	    	        if (dbUserOpt.isPresent()) {

	    	            Map<String, Object> dbUser = dbUserOpt.get();

	    	            // USER NOT FOUND
	    	            if (Boolean.TRUE.equals(dbUser.get("user_not_found"))) {
	    	                return ResponseEntity
	    	                        .status(HttpStatus.UNAUTHORIZED)
	    	                        .body("{\"success\":false,"
	    	                                + "\"message\":\"No Account Found\"}");
	    	            }

	    	            // LOCKED
	    	            if (Boolean.TRUE.equals(dbUser.get("locked"))) {
	    	            	
	    	            	System.out.println("the account is locked ");
	    	                return ResponseEntity
	    	                        .status(HttpStatus.UNAUTHORIZED)
	    	                        .body("{\"success\":false,"
	    	                                + "\"message\":\"Your account is locked. Please contact admin.\"}");
	    	            }

	    	            // INACTIVE
	    	            if (Boolean.TRUE.equals(dbUser.get("inactive"))) {
	    	                return ResponseEntity
	    	                        .status(HttpStatus.UNAUTHORIZED)
	    	                        .body("{\"success\":false,"
	    	                                + "\"message\":\"Your account is inactive. Please contact admin.\"}");
	    	            }

	    	            // WRONG PASSWORD
	    	            if (Boolean.TRUE.equals(dbUser.get("invalid_password"))) {
	    	                Integer remainingAttempts = (Integer) dbUser.get("remaining_attempts");
	    	                return ResponseEntity
	    	                        .status(HttpStatus.UNAUTHORIZED)
	    	                        .body("{\"success\":false,"
	    	                                + "\"message\":\"Invalid password. Remaining attempts: "
	    	                                + remainingAttempts
	    	                                + " .\"}");
	    	            }

	    	            String userId   = String.valueOf(dbUser.get("user_id"));
	    	            //System.out.println("the UserId is there:"+userId);
	    	            String userName = (String) dbUser.get("user_name");
	    	            String fullName = (String) dbUser.get("user_fullname");
						
						  boolean mappedToJims = isUserMappedToJims(userId);
						  
						  if (!mappedToJims) {
						  
						  return ResponseEntity .status(HttpStatus.UNAUTHORIZED)
						  .body("{\"success\":false," + "\"message\":\"User is not mapped to JIMS\"}");
						  }
						 
	    	            
	    	            Map<String, Object> loginStatus =
	    	                    checkUserAlreadyLoggedIn(userId);

	    	            if (loginStatus != null) {

	    	                Object isLoggedInRaw = loginStatus.get("is_loggedin_new");

	    	                int isLoggedIn = 0;

	    	                if (isLoggedInRaw instanceof Boolean) {
	    	                    isLoggedIn = ((Boolean) isLoggedInRaw) ? 1 : 0;
	    	                } else if (isLoggedInRaw instanceof Number) {
	    	                    isLoggedIn = ((Number) isLoggedInRaw).intValue();
	    	                }

	    	                Timestamp lastSeen =
	    	                        (Timestamp) loginStatus.get("last_seen");

	    	                if (isLoggedIn == 1 && lastSeen != null) {

	    	                    long expiryMillis = 3 * 60 * 1000L; // 3 minutes

	    	                    long remainingMillis =
	    	                            (lastSeen.getTime() + expiryMillis)
	    	                                    - System.currentTimeMillis();

	    	                    if (remainingMillis > 0) {

	    	                        long seconds = remainingMillis / 1000;
	    	                        long minutes = seconds / 60;
	    	                        long secs = seconds % 60;

	    	                        String waitTime =
	    	                                minutes > 0
	    	                                ? minutes + " min " + secs + " sec"
	    	                                : secs + " sec";

	    	                        return ResponseEntity
	    	                                .status(HttpStatus.UNAUTHORIZED)
	    	                                .body("{\"success\":false,"
	    	                                        + "\"message\":\"User already logged in from another session. Please wait "
	    	                                        + waitTime
	    	                                        + " before logging in again.\"}");
	    	                    }

	    	             
	    	                }
	    	            }
	    	          

	    	            // Generate JWT
	    	            String accessToken  = jwtUtil.generateAccessToken(userName);
	    	            String refreshToken = jwtUtil.generateRefreshToken(userName);

	    	            Cookie accessCookie = new Cookie("access_token", accessToken);
	    	            accessCookie.setHttpOnly(true);
	    	            accessCookie.setSecure(true);
	    	            accessCookie.setPath("/");
	    	            accessCookie.setMaxAge(15 * 60);
	    	            responses.addCookie(accessCookie);

	    	            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
	    	            refreshCookie.setHttpOnly(true);
	    	            refreshCookie.setSecure(false);
	    	            refreshCookie.setPath("/");
	    	            refreshCookie.setMaxAge(8 * 60 * 60);
	    	            responses.addCookie(refreshCookie);

	    	            ObjectMapper mapper = new ObjectMapper();

	    	            Map<String, Object> enrichedResponse = new HashMap<>();
	    	            enrichedResponse.put("success",      true);
	    	            enrichedResponse.put("status",       "yes");
	    	            enrichedResponse.put("message",      "Success");
	    	            enrichedResponse.put("token_expiry", jwtUtil.getAccessTokenExpiryTime());
	    	            enrichedResponse.put("user_id",      userName);
	    	            enrichedResponse.put("username",     userName);
	    	            enrichedResponse.put("id",userId);
	    	            enrichedResponse.put("full_name",    fullName);
	    	            enrichedResponse.put("location", user_supplier.getLocation(userId));
	    	            enrichedResponse.put("EmpCode", user_supplier.getEmpCode(userId));

	    	            boolean is_subcon = user_supplier.isSCUser(userId);
	    	            if (is_subcon) {
	    	                enrichedResponse.put("factory_id", user_supplier.getFactoryByUserId(userId));
	    	            } else {
	    	                enrichedResponse.put("factory_id", user_supplier.getFactoryByG2UserId(userId));
	    	            }
	    	            enrichedResponse.put("dotnet_data", null);
	    	            updateJimsLoginStatus(userId);

	    	            return ResponseEntity.ok(mapper.writeValueAsString(enrichedResponse));
	    	        }

	    	        // DB returned empty
	    	        return ResponseEntity
	    	                .status(HttpStatus.UNAUTHORIZED)
	    	                .body("{\"success\":false,\"message\":\"Invalid username or password\"}");

	    	    } catch (Exception ex) {
	    	        ex.printStackTrace();
	    	        return ResponseEntity
	    	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	    	                .body("{\"success\":false,\"message\":\"Login failed. Please try again.\"}");
	    	    }
	    	}
		
		
		
		public Optional<Map<String, Object>> validateUserFromDB(
		        String username,
		        String password) {

		    try {

		      
		        // CREATE JDBC TEMPLATE USING MIS DATASOURCE
		      
		       // JdbcTemplate jdbcTemplate = new JdbcTemplate(misDataSource);
		        
		        JdbcTemplate jdbcTemplate = new JdbcTemplate(misDataSource);
		        String result = jdbcTemplate.queryForObject("SELECT @@SERVERNAME", String.class);
		        System.out.println("misDataSource connected to: " + result);
		        JdbcTemplate jdbcTemplate2 = new JdbcTemplate(misDataSource2);
		        String result2 = jdbcTemplate.queryForObject("SELECT @@SERVERNAME", String.class);
		        System.out.println("misDataSource2 connected to: " + result2);

		     
		        // FETCH USER DETAILS FROM DATABASE
		        // ONLY ACTIVE (deleted = 0) USERS
		       
		        String sql =
		                "SELECT user_id, user_name, user_fullname, user_password, " +
		                "locked, inactive, password_attempts " +
		                "FROM MIS.dbo.Users " +
		                "WHERE user_name = ? AND deleted = 0";

		        List<Map<String, Object>> users =
		                jdbcTemplate.queryForList(sql, username);
		        

		   

		       
		        // USER NOT FOUND
		       
		        if (users.isEmpty()) {

		            Map<String, Object> response = new HashMap<>();
		            response.put("user_not_found", true);

		            return Optional.of(response);
		        }

		        
		        // GET FIRST USER RECORD
		       
		        Map<String, Object> user = users.get(0);

		       
		        // FETCH PASSWORD FROM DATABASE
		        
		        String dbPassword =
		                user.get("user_password") == null
		                ? ""
		                : user.get("user_password").toString();

		       
		        // FETCH LOCK STATUS
		       
		        Integer locked =
		                user.get("locked") == null
		                ? 0
		                : Integer.parseInt(user.get("locked").toString());

		       
		        // FETCH INACTIVE STATUS
		        
		        Integer inactive =
		                user.get("inactive") == null
		                ? 0
		                : Integer.parseInt(user.get("inactive").toString());

		       
		        // FETCH PASSWORD ATTEMPT COUNT
		       
		        Integer attempts =
		                user.get("password_attempts") == null
		                ? 0
		                : Integer.parseInt(user.get("password_attempts").toString());

		        
		        // CHECK IF ACCOUNT IS ALREADY LOCKED
		        
		        if (locked == 1) {

		            Map<String, Object> response = new HashMap<>();
		            response.put("locked", true);

		            return Optional.of(response);
		        }

		        
		        // CHECK IF ACCOUNT IS INACTIVE
		       
		        if (inactive == 1) {

		            Map<String, Object> response = new HashMap<>();
		            response.put("inactive", true);

		            return Optional.of(response);
		        }

		       
		        // PASSWORD MATCH SUCCESS
		      
		        if (password.equals(dbPassword)) {

		            
		            // RESET PASSWORD ATTEMPTS AFTER SUCCESSFUL LOGIN
		           
		            String resetSql =
		                    "UPDATE MIS.dbo.Users " +
		                    "SET password_attempts = 0 " +
		                    "WHERE user_name = ?";

		            jdbcTemplate.update(resetSql, username);

		            jdbcTemplate2.update(resetSql, username);
		            
		            // RETURN USER DETAILS
		           
		            return Optional.of(user);
		        }

		       
		        // WRONG PASSWORD
		        // INCREMENT ATTEMPT COUNT
		       
		        attempts = attempts + 1;

		      
		        // UPDATE PASSWORD ATTEMPTS IN DATABASE
		     
		        String updateAttemptsSql =
		                "UPDATE MIS.dbo.Users " +
		                "SET password_attempts = ? " +
		                "WHERE user_name = ?";

		        jdbcTemplate.update(updateAttemptsSql, attempts, username);
		        jdbcTemplate2.update(updateAttemptsSql, attempts, username);

		      
		        // CALCULATE REMAINING ATTEMPTS
		      
		        int remainingAttempts = 5 - attempts;

		       
		        // LOCK ACCOUNT AFTER 5 FAILED ATTEMPTS
		       
		        if (attempts >= 5) {

		            String lockSql =
		                    "UPDATE MIS.dbo.Users " +
		                    "SET locked = 1 " +
		                    "WHERE user_name = ? AND ISNULL(locked,0) = 0";

		            jdbcTemplate.update(lockSql, username);
		            jdbcTemplate2.update(lockSql, username);

		            Map<String, Object> response = new HashMap<>();
		            response.put("locked", true);
		            response.put(
		                    "message",
		                    "Your account is locked due to wrong password. Please contact admin."
		            );

		            return Optional.of(response);
		        }

		      
		        // INVALID PASSWORD RESPONSE
		       
		        Map<String, Object> response = new HashMap<>();
		        response.put("invalid_password", true);
		        response.put("remaining_attempts", remainingAttempts);

		        return Optional.of(response);

		    } catch (Exception e) {

		      
		        // HANDLE EXCEPTION
		      
		        e.printStackTrace();
		    }

		   
		    // RETURN EMPTY OPTIONAL IF SOMETHING FAILS
		 
		    return Optional.empty();
		}
		
		private boolean isUserMappedToJims(String userId) {

		    // Check JIMS mapping table first
		    JdbcTemplate jimsJdbcTemplate =
		            new JdbcTemplate(jimsDataSource);

		    String jimsSql =
		            "SELECT COUNT(*) " +
		            "FROM JIMS.dbo.UserFactoryMappingJIMS " +
		            "WHERE User_Id = ?";

		    Integer jimsCount =
		            jimsJdbcTemplate.queryForObject(
		                    jimsSql,
		                    Integer.class,
		                    userId);

		    if (jimsCount != null && jimsCount > 0) {

		        System.out.println(
		                "User found in UserFactoryMappingJIMS : "
		                        + userId);

		        return true;
		    }

		    // Not found in JIMS, check usersupplier
		    JdbcTemplate misJdbcTemplate =
		            new JdbcTemplate(misDataSource);

		    String misSql =
		            "SELECT factory " +
		            "FROM MIS.dbo.usersupplier " +
		            "WHERE uid = ?";

		    List<Map<String, Object>> result =
		            misJdbcTemplate.queryForList(
		                    misSql,
		                    userId);

		    if (!result.isEmpty()) {

		        Object factory =
		                result.get(0).get("factory");

		        // factory NULL => NOT mapped
		        if (factory == null) {

		            System.out.println(
		                    "Factory is NULL. User not mapped to JIMS.");

		            return false;
		        }

		        // factory has value => mapped
		        System.out.println(
		                "Factory found : " + factory);

		        return true;
		    }

		    System.out.println(
		            "User not found in either table.");

		    return false;
		}
		
		private void updateJimsLoginStatus(String userId) {

		    JdbcTemplate jimsJdbcTemplate = new JdbcTemplate(jimsDataSource);
		    JdbcTemplate blymisDataSource1 = new JdbcTemplate(misDataSource);
		    JdbcTemplate gujmisDataSource2 = new JdbcTemplate(misDataSource2);

		    String sql =
		        "UPDATE JIMS.dbo.UserFactoryMappingJIMS " +
		        "SET is_loggedin_new = 1, " +
		        "    last_seen = GETDATE(), " +
		        "    last_login_date = GETDATE() " +
		        "WHERE User_Id = ?";

		    jimsJdbcTemplate.update(sql, userId);
		    
		    String msql ="""
		    		
update mis.dbo.users set lastlogin=GETDATE() where user_id= ?
		    		""";
		    
		    
		  
		   blymisDataSource1.update(msql,userId);
		   gujmisDataSource2.update(msql,userId);
		    		
		}
		private Map<String, Object> checkUserAlreadyLoggedIn(String userId) {

		    JdbcTemplate jimsJdbcTemplate = new JdbcTemplate(jimsDataSource);

		    String sql =
		        "SELECT TOP 1 is_loggedin_new, last_seen " +
		        "FROM JIMS.dbo.UserFactoryMappingJIMS " +
		        "WHERE User_Id = ?";

		    List<Map<String, Object>> result =
		            jimsJdbcTemplate.queryForList(sql, userId);

		    if (result.isEmpty()) {
		        return null;
		    }

		    return result.get(0);
		}
		
		@PutMapping("/secondary-logout")
		public ResponseEntity<Map<String, Object>> secondaryLogout(
		        @RequestParam String user_id,
		        HttpServletResponse response) {

		    Map<String, Object> logoutMap = new HashMap<>();

		    try {

		        // Clear Access Token Cookie
//		        Cookie accessCookie = new Cookie("access_token", "");
//		        accessCookie.setHttpOnly(true);
//		        accessCookie.setPath("/");
//		        accessCookie.setMaxAge(0);
//		        response.addCookie(accessCookie);
//
//		        // Clear Refresh Token Cookie
//		        Cookie refreshCookie = new Cookie("refresh_token", "");
//		        refreshCookie.setHttpOnly(true);
//		        refreshCookie.setPath("/");
//		        refreshCookie.setMaxAge(0);
//		        response.addCookie(refreshCookie);

		        // Update login status
		        int updateCount = updateLogoutStatus(user_id);

		        logoutMap.put("message",
		                updateCount > 0 ? "Logout Success" : "Failed");

		        logoutMap.put("status",
		                updateCount > 0 ? "yes" : "no");

		        logoutMap.put("action", "UserLogOut");

		        return ResponseEntity.ok(logoutMap);

		    } catch (Exception e) {

		        e.printStackTrace();

		        logoutMap.put("message", "Logout failed");
		        logoutMap.put("status", "no");
		        logoutMap.put("action", "UserLogOut");

		        return ResponseEntity
		                .status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(logoutMap);
		    }
		}

	    
	    
	    
	    private int updateLogoutStatus(String userId) {

	        JdbcTemplate jimsJdbcTemplate =
	                new JdbcTemplate(jimsDataSource);

	        String sql =
	            "UPDATE JIMS.dbo.UserFactoryMappingJIMS " +
	            "SET is_loggedin_new = 0 " +
	            "WHERE User_Id = ?";

	        return jimsJdbcTemplate.update(sql, userId);
	    }
	    
	    


}
