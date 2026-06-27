package com.JIMS.integration.controller;

import java.sql.Connection;
import com.JIMS.integration.config.JwtUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.config.CaptchaImageServlet;
import com.JIMS.integration.config.EmailConfig;
import com.JIMS.integration.config.RateLimiterService;
import com.JIMS.integration.config.StringEncrypter;
import com.JIMS.integration.config.basic_authforG2users;
import com.JIMS.integration.config.encryptPasswordG2;
import com.JIMS.integration.interfaces.OtpSyncService;
import com.JIMS.integration.interfaces.RoleAssignModule_SubModule_Actions;
import com.JIMS.integration.interfaces.SmsService;
import com.JIMS.integration.interfaces.UserInterface;
import com.JIMS.integration.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class UserController {
	Logger logger = LogManager.getLogger(SeriesMasterNewController.class);

	@Value("${email.time-limit}")
	private long emailTimeLimit;

	public long getEmailTimeLimit() {
		return emailTimeLimit;
	}

	@Value("${login.count-limit}")
	private int loginCountLimit;

	public int getloginCountLimit() {
		return loginCountLimit;
	}

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private EmailConfig emailSender;

	// @PostMapping("/user/userlogin")
	// public @ResponseBody Map<String, Object> getUserLogin(@RequestParam String
	// username, @RequestParam String password) {

	/*
	 * Map<String, Object> userloginmap = new HashMap<>(); UserInterface
	 * logininterface = null; int updateIsActiveBasedOnLoginCount = 0; String
	 * user_id = null; try {
	 * 
	 * String encryptedPassword = StringEncrypter.testEncrypt(password);
	 * 
	 * logininterface = userrepo.getUserLogin(username, encryptedPassword);
	 * 
	 * if (logininterface == null) { int loginCount =
	 * userrepo.getLogInCount(username); // Retrieve current login attempt count
	 * loginCount++;
	 * 
	 * // Update the login count in the database userrepo.updateLoginCount(username,
	 * loginCount);
	 * 
	 * // Determine remaining attempts and provide feedback int remainingAttempts =
	 * 5 - loginCount;
	 * 
	 * if (loginCount >= 5) { updateIsActiveBasedOnLoginCount =
	 * userrepo.updateIsActiveBasedOnUnsuccessfullogin(username);
	 * userloginmap.put("message",
	 * "User is locked. Please request a password reset.");
	 * userloginmap.put("status", "no"); userloginmap.put("action", "UsersInfo"); }
	 * else { userloginmap.put("message",
	 * "Invalid username or password. Please try again.");
	 * userloginmap.put("status", "no"); userloginmap.put("loginAttempt",
	 * "Number of attempts remaining: " + remainingAttempts); }
	 * 
	 * return userloginmap; }else { user_id = logininterface.getUser_id(); }
	 * 
	 * // Check if the user is already logged in boolean is_loggedin =
	 * userrepo.checkWhetherUserIsLoggedin(user_id, encryptedPassword); if
	 * (is_loggedin) { userloginmap.put("message",
	 * "User is already logged in from another device"); userloginmap.put("status",
	 * "no"); userloginmap.put("action", "UsersInfo"); return userloginmap; }
	 * 
	 * // Check if the user is active boolean is_active =
	 * userrepo.isActive(user_id); if (!is_active) { userloginmap.put("message",
	 * "User is not active, please contact Admin"); userloginmap.put("status",
	 * "no"); userloginmap.put("action", "UsersInfo"); return userloginmap; }
	 * 
	 * // Successful login handling userloginmap.put("message", "Success");
	 * userloginmap.put("status", "yes"); userloginmap.put("action", "UsersInfo");
	 * userrepo.updateLoggedIn(user_id); List<RoleAssignModule_SubModule_Actions>
	 * listData = userrepo.getLoginRoleCredtianls(user_id); if (logininterface !=
	 * null) { userloginmap.put("data",listData); userloginmap.put("message",
	 * "Success"); userloginmap.put("status", "yes"); userloginmap.put("action",
	 * "UsersInfo");
	 * 
	 * int setLogincountToZero = userrepo.updateLoginCountToZero(user_id);
	 * ObjectMapper mapper = new ObjectMapper();
	 * userloginmap.putAll(mapper.convertValue(logininterface, new
	 * TypeReference<Map<String, Object>>() { })); mapper.clearProblemHandlers();
	 * mapper = null;
	 * 
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); // Replace with proper logging }
	 * 
	 * return userloginmap; }
	 */

//	@SuppressWarnings("unused")
//	@PostMapping("/user/userlogin")
//	public @ResponseBody Map<String, Object> getUserLogin(@RequestParam String username,
//			@RequestParam String password,HttpServletRequest request,HttpServletResponse response) {
//		logger.info("EXECUTING METHOD :: userlogin");
//		Map<String, Object> userloginmap = new HashMap<>();
//		UserInterface logininterface = null;
//		int updateIsActiveBasedOnLoginCount = 0;
//		String user_id = null;
//		int remainingAttempts = getloginCountLimit();
//		try {
//			
//			 String loginIpKey = "LOGIN_IP_" + request.getRemoteAddr();
//			 
//			 logger.info("ip address is this "+loginIpKey);
//			    boolean ipAllowed = rateLimiterService.tryConsume(loginIpKey, 4, 1);
//
//			    if (!ipAllowed) {
//			        userloginmap.put("message", "Too many login attempts from this IP. Please try again after 1 minute.");
//			        userloginmap.put("status", "no");
//			        return userloginmap;
//			    }
//			String encryptedPassword = StringEncrypter.testEncrypt(password);
//			logger.info("EXECUTING METHOD :: BEFORE USER LOGIN");
//			logininterface = userrepo.getUserLogin(username, encryptedPassword);
//			logger.info("EXECUTING METHOD ::  AFTER USER LOGIN");
//			if (logininterface == null) {
//				logger.info("EXECUTING METHOD :: BEFORE USER LOGIN COUNT");
//				Integer loginCount = userrepo.getLogInCount(username);
//				logger.info("EXECUTING METHOD :: AFTER USER LOGIN COUNT");
//				loginCount++;
//				logger.info("EXECUTING METHOD :: BEFORE USER UPDATING LOGIN COUNT");
//				userrepo.updateLoginCount(username, loginCount);
//				logger.info("EXECUTING METHOD ::  AFTER USER UPDATING LOGIN COUNT");
////				if (loginCount >= remainingAttempts) {
////					logger.info("EXECUTING METHOD ::  BEFORE USER BLOCK");
////					updateIsActiveBasedOnLoginCount = userrepo.updateIsActiveBasedOnUnsuccessfullogin(username);
////					userloginmap.put("message", "User is locked. Please contact Admin.");
////					userloginmap.put("status", "no");
////					userloginmap.put("action", "UsersInfo");
////					logger.info("EXECUTING METHOD :: AFTER USER BLOCK");
////					return userloginmap;
////				} else {
////					userloginmap.put("message", "Invalid username or password. Please try again.");
////					userloginmap.put("status", "no");
////					userloginmap.put("loginAttempt", "Number of attempts remaining: " + loginCount);
////					return userloginmap;
////				}
//				
//				if (loginCount >= remainingAttempts) {
//				    logger.info("EXECUTING METHOD ::  BEFORE USER BLOCK");
//				    updateIsActiveBasedOnLoginCount = userrepo.updateIsActiveBasedOnUnsuccessfullogin(username);
//				    userloginmap.put("message", "User is locked. Please contact Admin.");
//				    userloginmap.put("status", "no");
//				    userloginmap.put("action", "UsersInfo");
//				    logger.info("EXECUTING METHOD :: AFTER USER BLOCK");
//				    return userloginmap;
//				} else {
//				    int attemptsLeft = remainingAttempts - loginCount; // calculate remaining
//				    userloginmap.put("message", "Invalid username or password. Remaining attempts: " + attemptsLeft);
//				    userloginmap.put("status", "no");
//				    return userloginmap;
//				}
//			} else {
//				user_id = logininterface.getUser_id();
//			}
//
//			// Check if the user is already logged in
//			logger.info("EXECUTING METHOD :: BEFORE CHECK USER LOGIN");
//			boolean is_loggedin = userrepo.checkWhetherUserIsLoggedin(user_id, encryptedPassword);
//			if (is_loggedin) {
//				userloginmap.put("message", "User is already logged in from another device");
//				userloginmap.put("status", "no");
//				userloginmap.put("action", "UsersInfo");
//				return userloginmap;
//			}
//			logger.info("EXECUTING METHOD :: AFTER CHECK USER LOGIN");
//			logger.info("EXECUTING METHOD :: BEFORE CHECK USER ISACTIVE");
//			// Check if the user is active
//			boolean is_active = userrepo.isActive(user_id);
//			if (!is_active) {
//				userloginmap.put("message", "User is not active, please contact Admin");
//				userloginmap.put("status", "no");
//				userloginmap.put("action", "UsersInfo");
//				return userloginmap;
//			}
//			logger.info("EXECUTING METHOD :: AFTER CHECK USER ISACTIVE");
//			// Successful login handling
//			userloginmap.put("message", "Success");
//			userloginmap.put("status", "yes");
//			userloginmap.put("action", "UsersInfo");
//			Integer id = userrepo.getLocationIdByUserId(user_id);
//			userloginmap.put("loction_id", id != null ? id : 0);
//			userrepo.updateLoggedIn(user_id);
//			logger.info("EXECUTING METHOD :: BEFORE GET USER ROLE");
//			List<RoleAssignModule_SubModule_Actions> listData = userrepo.getLoginRoleCredtianls(user_id);
//			 Map<String, String> permissionMap = new HashMap<>();
//		        for (RoleAssignModule_SubModule_Actions result : listData) {
//		            permissionMap.put(result.getSubmodule_name(), result.getActions_list());
//		        }
//		        logger.info("EXECUTING METHOD :: AFTER GET USER ROLE");
//			if (logininterface != null) {
//				userloginmap.put("data", listData);
//				userloginmap.put("datamAP", permissionMap);
//				userloginmap.put("message", "Success");
//				userloginmap.put("status", "yes");
//				userloginmap.put("action", "UsersInfo");
//		        //  Now store login in HTTP session
////				HttpSession session = request.getSession(true);
////				session.setAttribute("isLoggedIn", true);
////				session.setAttribute("user_id", user_id);
////				session.setAttribute("username", username);
////				session.setAttribute("id", logininterface.getId());
//
//				// Force JSESSIONID cookie path to root
//				// ── Generate JWT tokens ──────────────────────────────
//				String accessToken  = jwtUtil.generateAccessToken(user_id);
//				String refreshToken = jwtUtil.generateRefreshToken(user_id);
//
//				// ── Set access_token HttpOnly cookie ─────────────────
//				Cookie accessCookie = new Cookie("access_token", accessToken);
//				accessCookie.setHttpOnly(true);
//				accessCookie.setSecure(true); // set true when HTTPS
//				accessCookie.setPath("/");
//				accessCookie.setMaxAge(15 * 60); // 15 minutes
//				response.addCookie(accessCookie);
//
//				// ── Set refresh_token HttpOnly cookie ────────────────
//				Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
//				refreshCookie.setHttpOnly(true);
//				refreshCookie.setSecure(true); // set true when HTTPS
//				refreshCookie.setPath("/");
//				refreshCookie.setMaxAge(8 * 60 * 60); // 8 hours
//				response.addCookie(refreshCookie);
//
//				// ── Add token expiry to response ─────────────────────
//				userloginmap.put("token_expiry", jwtUtil.getAccessTokenExpiryTime());
//
//				logger.info("JWT tokens generated for user: {}", user_id);
//
//				
//
//		       
//				userrepo.updateLoginCountToZero(user_id);
//				logger.info("EXECUTING METHOD ::  AFTER UPDATE USER COUNT");
//				ObjectMapper mapper = new ObjectMapper();
//				userloginmap.putAll(mapper.convertValue(logininterface, new TypeReference<Map<String, Object>>() {
//				}));
//				mapper.clearProblemHandlers();
//				mapper = null;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("ERROR IN THE METHOD FOR createTaxMastersNew ::   -> " + e.getMessage());
//		}
//		logger.info("EXECUTED METHOD :: createTaxMastersNew");
//		return userloginmap;
//	}
	
	
	@Autowired
	private basic_authforG2users secondaryAuthController;
	

	@SuppressWarnings("unused")
	@PostMapping("/user/userlogin")
	public @ResponseBody Map<String, Object> getUserLogin(@RequestParam String username, @RequestParam String password,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("EXECUTING METHOD :: userlogin");
		Map<String, Object> userloginmap = new HashMap<>();
		UserInterface logininterface = null;
		int updateIsActiveBasedOnLoginCount = 0;
		String user_id = null;
		int remainingAttempts = getloginCountLimit();
		try {

			String loginIpKey = "LOGIN_IP_" + getClientIp(request) + ":" + username;
			//boolean ipAllowed = rateLimiterService.tryConsume(loginIpKey, 7, 1);
			

			logger.info("ip address is this " + loginIpKey);
			
			String clientIp = getClientIp(request);

			logger.info("Client IP: {}", clientIp);
			
			
			
			
			boolean ipAllowed = rateLimiterService.tryConsume(loginIpKey, 7, 1);

			if (!ipAllowed) {
				userloginmap.put("message", "Too many login attempts. Please try again after 1 minute.");
				userloginmap.put("status", "no");
				userloginmap.put("user_exists", true);
				return userloginmap;
			}
			String encryptedPassword = StringEncrypter.testEncrypt(password);
			logger.info("EXECUTING METHOD :: BEFORE USER LOGIN");
			logininterface = userrepo.getUserLogin(username, encryptedPassword);
			logger.info("EXECUTING METHOD ::  AFTER USER LOGIN");

			if (logininterface == null) {

				Integer loginCount = userrepo.getLogInCount(username);

				// loginCount == null means user doesn't exist in primary DB at all
				if (loginCount == null) {
					userloginmap.put("status", "no");
					userloginmap.put("user_exists", false); // user not in primary DB
					userloginmap.put("message", "User not found in primary system.");
					return userloginmap;
				}

				logger.info("EXECUTING METHOD :: BEFORE CHECK passwrod expries");

				// User exists but wrong password
				userloginmap.put("user_exists", true); // user IS in primary DB
//				    loginCount++;
//				    userrepo.updateLoginCount(username, loginCount);

				int updatedRows = userrepo.incrementLoginCount(username);

				boolean activeStatus = userrepo.getUserStatus(username);

				System.out.println("acitve statuse is " + activeStatus);
				if (!activeStatus) {
					userloginmap.put("message", "Your account is inactive. Please contact admin.");
					userloginmap.put("status", "no");
					userloginmap.put("action", "UsersInfo");
					return userloginmap;
				}
				if (updatedRows == 0) {
					userrepo.updateIsActiveBasedOnUnsuccessfullogin(username);
					userloginmap.put("message", "User is locked. Please contact Admin.");
					userloginmap.put("status", "no");
					userloginmap.put("action", "UsersInfo");
					return userloginmap;
				} else {
					loginCount = userrepo.getLogInCount(username);

					int attemptsLeft = remainingAttempts - loginCount;

					userloginmap.put("message", "Invalid password. Remaining attempts: " + attemptsLeft);
					userloginmap.put("status", "no");
					return userloginmap;
				}
			} else {
				user_id = logininterface.getUser_id();
			}
			Boolean is_password_expired = userrepo.isPasswordExpries(username);

			if (is_password_expired) {
				userloginmap.put("message", "Your password has expired. Please reset your password.");
				userloginmap.put("user_exists", true);
				userloginmap.put("status", "no");
				userloginmap.put("action", "UsersInfo");
				return userloginmap;
			}

			// Check if the user is already logged in
			logger.info("EXECUTING METHOD :: BEFORE CHECK USER LOGIN");

			List<Object[]> loginStatusList = userrepo.getUserLoginStatus(user_id, encryptedPassword);

			if (loginStatusList != null && !loginStatusList.isEmpty()) {

				Object[] results = loginStatusList.get(0);

				Object isLoggedInRaw = results[0];
				int isLoggedIn = 0;

				if (isLoggedInRaw instanceof Boolean) {
					isLoggedIn = ((Boolean) isLoggedInRaw) ? 1 : 0;
				} else if (isLoggedInRaw instanceof Number) {
					isLoggedIn = ((Number) isLoggedInRaw).intValue();
				}

				Timestamp lastSeen = (Timestamp) results[1];
//
//				    if (isLoggedIn == 1 && lastSeen != null) {
//
//				        long expiryMillis    = 3 * 60 * 1000L;
//				        long remainingMillis = (lastSeen.getTime() + expiryMillis) - System.currentTimeMillis();
//
//				        if (remainingMillis > 0) {
//
//				            long seconds  = remainingMillis / 1000;
//				            long minutes  = seconds / 60;
//				            long secs     = seconds % 60;
//
//				            String waitTime = minutes > 0
//				                ? minutes + " min " + secs + " sec"
//				                : secs + " sec";
//
//				            userloginmap.put("message",
//				                "User already logged in from another session." +
//				                "Please wait " + waitTime + " before logging in again.");
//				            
//				            
//				            userloginmap.put("user_exists", true);
//				            userloginmap.put("status",  "no");
//				            userloginmap.put("action",  "UsersInfo");
//				            return userloginmap;
//				        }
//				        
//				    }
			}
			logger.info("EXECUTING METHOD :: AFTER CHECK USER LOGIN");
			logger.info("EXECUTING METHOD :: BEFORE CHECK USER ISACTIVE");
			// Check if the user is active

			logger.info("EXECUTING METHOD :: AFTER CHECK USER ISACTIVE");
			// Successful login handling
			userloginmap.put("message", "Success");
			userloginmap.put("status", "yes");
			userloginmap.put("action", "UsersInfo");
			Integer id = userrepo.getLocationIdByUserId(user_id);
			userloginmap.put("loction_id", id != null ? id : 0);
			userrepo.updateLoggedIn(user_id);
			logger.info("EXECUTING METHOD :: BEFORE GET USER ROLE");
			List<RoleAssignModule_SubModule_Actions> listData = userrepo.getLoginRoleCredtianls(user_id);
			Map<String, String> permissionMap = new HashMap<>();
			for (RoleAssignModule_SubModule_Actions result : listData) {
				permissionMap.put(result.getSubmodule_name(), result.getActions_list());
			}
			logger.info("EXECUTING METHOD :: AFTER GET USER ROLE");
			if (logininterface != null) {
				userloginmap.put("data", listData);
				userloginmap.put("datamAP", permissionMap);
				userloginmap.put("message", "Success");
				userloginmap.put("status", "yes");
				userloginmap.put("action", "UsersInfo");

				// Force JSESSIONID cookie path to root
				// Generate JWT tokens
				String accessToken = jwtUtil.generateAccessToken(user_id);
				String refreshToken = jwtUtil.generateRefreshToken(user_id);

				// Set access_token HttpOnly cookie
				Cookie accessCookie = new Cookie("access_token", accessToken);
				accessCookie.setHttpOnly(true);
				accessCookie.setSecure(true); // set true when HTTPS
				accessCookie.setPath("/");
				accessCookie.setMaxAge(15 * 60); // 15 minutes
				response.addCookie(accessCookie);

				// Set refresh_token HttpOnly cookie
				Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
				refreshCookie.setHttpOnly(true);
				refreshCookie.setSecure(true); // set true when HTTPS
				refreshCookie.setPath("/");
				refreshCookie.setMaxAge(8 * 60 * 60); // 8 hours
				response.addCookie(refreshCookie);

				// Add token expiry to response
				userloginmap.put("token_expiry", jwtUtil.getAccessTokenExpiryTime());

				logger.info("JWT tokens generated for user: {}", user_id);

				// userrepo.updateLoginCountToZero(user_id);
				logger.info("EXECUTING METHOD ::  AFTER UPDATE USER COUNT");
				ObjectMapper mapper = new ObjectMapper();
				userloginmap.putAll(mapper.convertValue(logininterface, new TypeReference<Map<String, Object>>() {
				}));
				mapper.clearProblemHandlers();
				mapper = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR USERLOGIN ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: USETLOGIN");
		return userloginmap;
	}
	
	
	
	
	
	private String getClientIp(HttpServletRequest request) {

	    String xfHeader = request.getHeader("X-Forwarded-For");

	    if (xfHeader != null && !xfHeader.isEmpty()) {
	        return xfHeader.split(",")[0].trim();
	    }

	    return request.getRemoteAddr();
	}

//	@PostMapping("/heartbeat")
//	public ResponseEntity<?> heartbeat(@RequestParam String user_id) {
//
//		userrepo.updateLastSeen(user_id);
//
//		return ResponseEntity.ok("alive");
//	}
	
	
	@PostMapping("/heartbeat")
	public ResponseEntity<?> heartbeat(
	        @RequestParam String user_id) {

	    try {

	        int userExists =
	                userrepo.checkUserExists(user_id);
	    //  System.out.println("teh check the userexits : "+userExists);

	        if (userExists > 0) {

	            // Primary JIMS User
	            userrepo.updateLastSeen(user_id);

	        } else {

	            // G2 User
	            updateJimsLastSeen(user_id);
	        }

	        return ResponseEntity.ok("alive");

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity
	                .internalServerError()
	                .body("failed");
	    }
	}
	private void updateJimsLastSeen(String userId) {

	    JdbcTemplate jimsJdbcTemplate =
	            new JdbcTemplate(jimsDataSource);

	    String sql =
	        "UPDATE JIMS.dbo.UserFactoryMappingJIMS " +
	        "SET last_seen = GETDATE() " +
	        "WHERE UserName = ?";

	    int count = jimsJdbcTemplate.update(sql, userId);

//	    System.out.println("JIMS update count = " + count);
//	    System.out.println("UserId = " + userId);
	}
	

	@Scheduled(cron = "0 30 6 * * ?")
	public @ResponseBody Map<String, Object> deactivateUser() {
		logger.info("EXECUTING METHOD :: deactivateUser");
		Map<String, Object> deactivateUseramap = new HashMap<String, Object>();
		int setUserInactiveRecord = 0;
		try {
			setUserInactiveRecord = userrepo.serUsersInactive();
			deactivateUseramap.put("message",
					(setUserInactiveRecord > 0) ? "Success, User is set to not_active" : "No users found");
			deactivateUseramap.put("status", (setUserInactiveRecord > 0) ? "yes" : "no");
			deactivateUseramap.put("action", "DeactiveUsersInfo");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR deactivateUser ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: createTaxMastersNew");
		return deactivateUseramap;
	}

	//

	@Scheduled(cron = "0 45 6 * * ?")
	public void expirePasswordsScheduler() {

		logger.info("EXECUTING METHOD :: expirePasswordsScheduler");

		try {

			int updated = userrepo.expireUserPasswords();

			logger.info("Expired Password Count :: " + updated);

		} catch (Exception e) {

			logger.error("ERROR IN expirePasswordsScheduler :: " + e.getMessage());

		}

	}

//	 Aready there userlougout

//	@PostMapping("/user/userlogout")
//	public @ResponseBody Map<String, Object> userLogout(@RequestParam String user_id) {
//		logger.info("EXECUTING METHOD :: userLogout");
//		Map<String, Object> userLogoutmap = new HashMap<String, Object>();
//		try {
//			logger.info("EXECUTING METHOD :: BEFORE USER LOGOUT");
//			int getUserlogoutrecord = userrepo.userLogout(user_id);
//			logger.info("EXECUTING METHOD :: AFTER USER LOGOUT");
//			userLogoutmap.put("message", (getUserlogoutrecord > 0) ? "Logout Success" : "Failed");
//			userLogoutmap.put("status", (getUserlogoutrecord > 0) ? "yes" : "no");
//			userLogoutmap.put("action", "UserLogOut");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("ERROR IN THE METHOD FOR userLogout ::   -> " + e.getMessage());
//		}
//		logger.info("EXECUTED METHOD :: userLogout");
//		return userLogoutmap;
//
//	}

//	 Ajad Added userlogout function

	@PostMapping("/user/userlogout")
	public @ResponseBody Map<String, Object> userLogout(
	        @RequestParam String user_id,
	        HttpServletResponse response) {

	    Logger logger = LogManager.getLogger(UserController.class);

	    Map<String, Object> userLogoutMap = new HashMap<>();

	    try {

	        // Clear JWT cookies
//	        Cookie accessCookie = new Cookie("access_token", "");
//	        accessCookie.setHttpOnly(true);
//	        accessCookie.setPath("/");
//	        accessCookie.setMaxAge(0);
//	        response.addCookie(accessCookie);
//
//	        Cookie refreshCookie = new Cookie("refresh_token", "");
//	        refreshCookie.setHttpOnly(true);
//	        refreshCookie.setPath("/");
//	        refreshCookie.setMaxAge(0);
//	        response.addCookie(refreshCookie);

	        int updateCount = userrepo.userLogout(user_id);

	        // If not found in USERS_MASTER,
	        // logout from UserFactoryMappingJIMS
	        if (updateCount == 0) {

	            JdbcTemplate jimsJdbcTemplate =
	                    new JdbcTemplate(jimsDataSource);

	            updateCount = jimsJdbcTemplate.update(
	                    "UPDATE JIMS.dbo.UserFactoryMappingJIMS " +
	                    "SET is_loggedin_new = 0 " +
	                    "WHERE UserName = ?",
	                    user_id
	            );
	        }

	        userLogoutMap.put(
	                "message",
	                updateCount > 0 ? "Logout Success" : "User Not Found"
	        );

	        userLogoutMap.put(
	                "status",
	                updateCount > 0 ? "yes" : "no"
	        );

	        userLogoutMap.put("action", "UserLogOut");

	    } catch (Exception e) {

	        logger.error("Logout Error", e);

	        userLogoutMap.put("message", "Logout failed");
	        userLogoutMap.put("status", "no");
	        userLogoutMap.put("action", "UserLogOut");
	    }

	    return userLogoutMap;
	}

	@PostMapping("/user/adduser")
	public @ResponseBody Map<String, Object> userCreateNew(@RequestBody Map<String, Object> requestData) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: userCreateNew");

		try {
			// Extract values from request payload
			String user_id = (String) requestData.get("user_id");
			String username = (String) requestData.get("username");
			String password = (String) requestData.get("password");
			String email = (String) requestData.get("email");
			String mobile_number = (String) requestData.get("mobile_number");
			String created_by = (String) requestData.get("created_by");
			String role_id = (String) requestData.get("role_id");
			String Department = (String) requestData.get("Department");

			// NEW fields
			String first_name = (String) requestData.get("first_name");
			String last_name = (String) requestData.get("last_name");

			// report_id and location might come as Integer or String
			Integer report_id = null;
			Integer location = null;

			if (requestData.get("report_id") != null) {
				report_id = Integer.parseInt(requestData.get("report_id").toString());
			}

			if (requestData.get("location") != null) {
				location = Integer.parseInt(requestData.get("location").toString());
			}

			// NEW field
			String factory_id = null;

			if (requestData.get("factory_id") != null) {
				factory_id = requestData.get("factory_id").toString().trim();
			}
			// Encrypt password
			String encryptPassword = StringEncrypter.testEncrypt(password);
			logger.info("EXECUTING METHOD :: BEFORE ADDING USER");

			// Call repository method
			int count = userrepo.userCreateUser(user_id, encryptPassword, username, email, mobile_number, created_by,
					role_id, Department, first_name, last_name, report_id, location, factory_id // ✅ NEW
			);

			logger.info("USER CREATION RESULT COUNT :: " + count);

			logger.info("EXECUTING METHOD :: AFTER ADDING USER");
			int emailSent = emailSender.sendEmail(email, username, password);
			// Construct the response
			response.put("message", (count > 0) ? "User Created Success" : "User Created Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("Email Sent", (emailSent > 0) ? "Mail Sent Successfully" : "Mail Sent Failed");
			response.put("action", "UserCreation");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR userCreateNew ::   -> " + e.getMessage());
		}

		logger.info("EXECUTED METHOD :: userCreateNew");
		return response;
	}

//	@PostMapping("/user/updateuser")
//	public @ResponseBody Map<String, Object> userUpdate(@RequestBody UserUpdateUser request) {
//	    logger.info("EXECUTING METHOD :: userUpdate");
//	    Map<String, Object> response = new HashMap<String, Object>();
//	    try {
//	        String encryptPassword = StringEncrypter.testEncrypt(request.getPassword());
//	        logger.info("EXECUTING METHOD :: BEFORE UPDATE USER");
//	        int count = userrepo.userUpdateUser(request.getUser_id(), encryptPassword, request.getUsername(),
//	                                            request.getEmail(), request.getMobile_number(), request.getCreated_by(),
//	                                            request.getRole_id(), request.getId(), request.getDepartment());
//	        logger.info("EXECUTING METHOD :: AFTER UPDATE USER");
//	        response.put("message", (count > 0) ? "Update User Success" : "Update User Failed");
//	        response.put("status", (count > 0) ? "yes" : "no");
//	        response.put("action", "UserUpdate");
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        logger.error("ERROR IN THE METHOD FOR userUpdate ::   -> " + e.getMessage());
//	    }
//	    logger.info("EXECUTED METHOD :: userUpdate");
//	    return response;
//	}
	
	
	


	@PostMapping("/user/updateuser")
	public @ResponseBody Map<String, Object> userUpdate(@RequestBody UserUpdateUser request) {
		logger.info("EXECUTING METHOD :: userUpdate");
		Map<String, Object> response = new HashMap<>();

		try {
			logger.info("EXECUTING METHOD :: BEFORE UPDATE USER");

			int count;
			if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
				// Encrypt and update password
				String encryptPassword = StringEncrypter.testEncrypt(request.getPassword());
				count = userrepo.userUpdateUserWithPassword(request.getUser_id(), encryptPassword,
						request.getUsername(), request.getEmail(), request.getMobile_number(), request.getModified_by(),
						request.getRole_id(), request.getId(), request.getDepartment(), request.getFirst_name(),
						request.getLast_name(), request.getLocation(), request.getReport_id(), request.getfactory_id());
			} else {
				// Update other fields only
				count = userrepo.userUpdateUserWithoutPassword(request.getUser_id(), request.getUsername(),
						request.getEmail(), request.getMobile_number(), request.getModified_by(), request.getRole_id(),
						request.getId(), request.getDepartment(), request.getFirst_name(), request.getLast_name(),
						request.getLocation(), request.getReport_id(), request.getfactory_id());
			}

			logger.info("EXECUTING METHOD :: AFTER UPDATE USER");
			response.put("message", (count > 0) ? "Update User Success" : "Update User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserUpdate");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD FOR userUpdate :: -> " + e.getMessage(), e);
			response.put("message", "An error occurred while updating user.");
			response.put("status", "error");
			response.put("action", "UserUpdate");
		}

		logger.info("EXECUTED METHOD :: userUpdate");
		return response;
	}

	@PostMapping("/user/blockuser")
	public @ResponseBody Map<String, Object> userBlock(@RequestParam String id, @RequestParam String modified_by) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: userBlock");
		try {
			logger.info("EXECUTING METHOD :: BEFORE BLOCK USER");
			int count = userrepo.userBlockUser(id, modified_by);
			logger.info("EXECUTING METHOD :: AFTER BLOCK USER");
			response.put("message", (count > 0) ? "BLOCK User Success" : "BLOCK User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserBLOCK");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR userBlock ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: userBlock");
		return response;
	}

	@PostMapping("/user/activeuser")
	public @ResponseBody Map<String, Object> userActive(@RequestParam String id, @RequestParam String modified_by) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: userActive");
		try {
			logger.info("EXECUTING METHOD :: BEFORE CHECK ACTIVE USER");
			int count = userrepo.userActive(id, modified_by);
			logger.info("EXECUTING METHOD :: AFTER CHECK ACTIVE USER");
			response.put("message", (count > 0) ? "Active User Success" : "Active User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserActive");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR userActive ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: userActive");
		return response;
	}

	@PostMapping("/user/deleteuser")
	public @ResponseBody Map<String, Object> userdelete(@RequestParam String id, @RequestParam String modified_by) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: userdelete");
		try {
			logger.info("EXECUTING METHOD :: BEFORE CHECK DELETE USER");
			int count = userrepo.userDelete(id, modified_by);
			logger.info("EXECUTING METHOD :: AFTER CHECK DELETE USER");
			response.put("message", (count > 0) ? "Delete User Success" : "Delete User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserDelete");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR userdelete ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: userdelete");
		return response;
	}

	@GetMapping("/user/listuser")
	public @ResponseBody Map<String, Object> userList() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<UserInterface> userInterface = null;
		logger.info("EXECUTING METHOD :: userList");
		try {
			logger.info("EXECUTING METHOD :: BEFORE  userList USER");
			userInterface = userrepo.listUsersss();
			logger.info("EXECUTING METHOD :: AFTER userList  USER");
			response.put("message", (userInterface != null) ? "List User Success" : "List User Failed");
			response.put("status", (userInterface != null) ? "yes" : "no");
			response.put("action", "UserList");
			response.put("Data", userInterface);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR userList ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: userList");
		return response;
	}

//	AFter Pagging Controllere

	@GetMapping("/user/listuserss")
	public @ResponseBody Map<String, Object> userList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String search) { // <-- add
																											// search
																											// param

		Map<String, Object> response = new HashMap<>();

		try {
			Pageable pageable = PageRequest.of(page, size);

			//  Pass the search parameter here
			Page<UserInterface> userPage = userrepo.listUsers(search, pageable);

			response.put("message", userPage.hasContent() ? "List User Success" : "No Users Found");
			response.put("status", userPage.hasContent() ? "yes" : "no");
			response.put("action", "UserList");
			response.put("Data", userPage.getContent());
			response.put("currentPage", userPage.getNumber());
			response.put("totalItems", userPage.getTotalElements());
			response.put("totalPages", userPage.getTotalPages());

		} catch (Exception e) {
			response.put("status", "no");
			response.put("message", "Error occurred: " + e.getMessage());
		}

		return response;
	}

	@GetMapping("/user/search")
	public @ResponseBody Map<String, Object> userSearch(@RequestParam String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("EXECUTING METHOD :: userSearch");
		UserInterface userInterface = null;
		try {
			logger.info("EXECUTING METHOD :: BEFORE  userSearch USER");
			userInterface = userrepo.serachUserById(id);
			logger.info("EXECUTING METHOD :: AFTER userSearch  USER");
			response.put("Data", userInterface);
			response.put("message", (userInterface != null) ? "Search User Success" : "Search User Failed");
			response.put("status", (userInterface != null) ? "yes" : "no");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD FOR userSearch ::   -> " + e.getMessage());
			e.printStackTrace();
		}
		logger.info("EXECUTED METHOD :: userSearch");
		return response;
	}

	@SuppressWarnings("unused")
	@GetMapping("/user/sendmailTest")
	public String userMailSend(@RequestParam String username, @RequestParam String password,
			@RequestParam String email_id) {
		logger.info("EXECUTING METHOD :: userMailSend");
		Map<String, Object> response = new HashMap<String, Object>();
		try {

			// emailSender.sendEmailLink(email_id, username, password);
			return "Email sent successfully!";

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR userMailSend ::   -> " + e.getMessage());
		}
		logger.info("EXECUTED METHOD :: userMailSend");
		return "";
	}

//	@PostMapping("/user/forgotpassword")
//	public @ResponseBody Map<String, Object> userSendEmail(@RequestParam String email_id) {
//		logger.info("EXECUTING METHOD :: userMailSend");
//		Map<String, Object> response = new HashMap<String, Object>();
//		try {
//			int checkValue = userrepo.checkEmailExits(email_id);
//			if(checkValue > 0) {
//				Random rand = new Random();
//				String created_by = userrepo.getUser_id(email_id);
//		        // Generate a random number between 100,000 and 999,999 password_otp otp_timevalidation
//		        int password_otp = 100000 + rand.nextInt(900000); 
//		        logger.info("EXECUTING METHOD :: storeOtpTimestamp");
//		       int count = userrepo.storeOtpTimestamp(password_otp,created_by);
//		       logger.info("EXECUTIED METHOD :: storeOtpTimestamp");
//		        if(count != 0) {
//		        	 logger.info("EXECUTING METHOD :: sendEmailLink");
//		        	emailSender.sendEmailLink(email_id, String.valueOf(password_otp));
//		        	 logger.info("EXECUTIED METHOD :: sendEmailLink");
//					response.put("message", "Email sent successfully!");
//					return response;
//		        }
//		        response.put("message", "MAIL NOT SENT");
//				return response;
//			}else {
//				response.put("message","Email Id is Invalid. Please provide valid Email");
//				return response;
//			}
//			
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("ERROR IN THE METHOD FOR userMailSend ::   -> " + e.getMessage());
//		}
//		logger.info("EXECUTED METHOD :: userMailSend");
//		return response;
//	}

//	 Already there forget method 

//	@Autowired
//	private RateLimiterService rateLimiterService;
//	
//	@PostMapping("/user/forgotpassword")
//	@ResponseBody
//	public Map<String, Object> userSendEmail(@RequestParam String email_id, @RequestParam String captcha_input,
//		     HttpSession session,
//            HttpServletRequest request){
//
//		logger.info("EXECUTING METHOD :: userMailSend");
//		Map<String, Object> response = new HashMap<>();
//
//		try {
//			
//			// Normalize email
//			email_id = email_id.trim().toLowerCase();
//
//			// Create captcha key using email
//			String captchaKey = "CAPTCHA_LOCK_" + email_id;
//
//			//String expectedCaptcha = (String) session.getAttribute("captcha");
//			
//			String expectedCaptcha = CaptchaImageServlet.getLatestCaptcha();
//			
//			logger.info(expectedCaptcha);
//			
//
//			if (expectedCaptcha == null || !captcha_input.equals(expectedCaptcha)) {
//
//			    boolean locked = !rateLimiterService.tryConsume(captchaKey, 3, 1);
//
//			    if (locked) {
//			        response.put("message", "Too many incorrect CAPTCHA attempts. Try again after 1 minute.");
//			        return response;
//			    }
//
//			    response.put("message", "Incorrect CAPTCHA. Please try again.");
//			    return response;
//			}
//
//
//			// NOW check OTP rate limit (ONLY after captcha success)
//			// Email Validation
//			int checkValue = userrepo.checkEmailExits(email_id);
//			int checkstatus = userrepo.checkEmailExitsStaus(email_id);
//			
//		    if(checkstatus>0) {
//		    	 response.put("message", "Your account is inactive. Please contact admin");
//				    return response;
//		    }
//			
//
//			if (checkValue > 0) {
//
//			    String clientIp = request.getRemoteAddr();
//			    String emailKey = "OTP_EMAIL_" + email_id;
//			    String ipKey = "OTP_IP_" + clientIp;
//
//			    // check OTP rate limit ONLY for valid email
//			    boolean emailAllowed = rateLimiterService.tryConsume(emailKey, 1, 1);
//			    boolean ipAllowed = rateLimiterService.tryConsume(ipKey, 1, 1);
//			    
//			    logger.info(emailAllowed);
//			    logger.info(ipAllowed);
//
//			    if (!emailAllowed) {
//			        response.put("message", "OTP already sent. Please wait 1 minute before requesting again.");
//			        return response;
//			    }
//			    if(!ipAllowed) {
//			        response.put("message", "OTP requests from your IP are limited. Please try again in 1 minute.");
//			        return response;
//			    }
//
//			    Random rand = new Random();
//			    String created_by = userrepo.getUser_id(email_id);
//
//			    int password_otp = 100000 + rand.nextInt(900000);
//
//			    int count = userrepo.storeOtpTimestamp(password_otp, created_by);
//
//			    if (count != 0) {
//			        String usernameByEmail = userrepo.findUsernameByEmail(email_id);
//			        emailSender.sendEmailLink(email_id, String.valueOf(password_otp), usernameByEmail);
//
//			        response.put("message", "Email sent successfully!");
//			        return response;
//			    } else {
//			        response.put("message", "MAIL NOT SENT");
//			        return response;
//			    }
//
//			} else {
//
//			    String clientIp = request.getRemoteAddr();
//			    String invalidEmailKey = "INVALID_EMAIL_" + clientIp;
//
//			    boolean allowed = rateLimiterService.tryConsume(invalidEmailKey, 2, 2);
//
//			    if (!allowed) {
//			        response.put("message", "Too many invalid email attempts. Please wait 2 minutes.");
//			        return response;
//			    }
//
//			    response.put("message", "Email ID is invalid. Please provide a valid email.");
//			    return response;
//			}
//
//
//		} catch (Exception e) {
//			logger.error("ERROR IN userMailSend: " + e.getMessage(), e);
//			response.put("message", "An error occurred while processing the request.");
//		}
//
//		logger.info("EXECUTED METHOD :: userMailSend");
//		return response;
//	}
//	

//	 For G2 Users

	@Autowired
	private RateLimiterService rateLimiterService;

	@Autowired
	private SmsService smsService;
	
	
	@Autowired
	@Qualifier("misDataSource2")
	private DataSource misDataSource2;
	@Autowired
	private OtpSyncService otpSyncService;

	@PostMapping("/user/forgotpassword")
	@ResponseBody
	public Map<String, Object> userSendEmail(@RequestParam String email_id, @RequestParam String captcha_input,
			HttpSession session, HttpServletRequest request) {

		logger.info("EXECUTING METHOD :: userMailSend");
		Map<String, Object> response = new HashMap<>();

		try {

			// Normalize email
			email_id = email_id.trim().toLowerCase();

			// Create captcha key using email
			String captchaKey = "CAPTCHA_LOCK_" + email_id;

			// String expectedCaptcha = (String) session.getAttribute("captcha");

			String expectedCaptcha = CaptchaImageServlet.getLatestCaptcha();

			logger.info(expectedCaptcha);

			if (expectedCaptcha == null || !captcha_input.equals(expectedCaptcha)) {

				boolean locked = !rateLimiterService.tryConsume(captchaKey, 3, 1);

				if (locked) {
					response.put("message", "Too many incorrect CAPTCHA attempts. Try again after 1 minute.");
					return response;
				}

				response.put("message", "Incorrect CAPTCHA. Please try again.");
				return response;
			}

			// NOW check OTP rate limit (ONLY after captcha success)
			// Email Validation
			int checkValue = userrepo.checkEmailExits(email_id);
			int checkstatus = userrepo.checkEmailExitsStaus(email_id);

			if (checkstatus > 0) {
				response.put("message", "Your account is inactive. Please contact admin");
				return response;
			}

			if (checkValue > 0) {

				String clientIp = request.getRemoteAddr();
				String emailKey = "OTP_EMAIL_" + email_id;
				String ipKey = "OTP_IP_" + clientIp;

				// check OTP rate limit ONLY for valid email
				boolean emailAllowed = rateLimiterService.tryConsume(emailKey, 1, 1);
				boolean ipAllowed = rateLimiterService.tryConsume(ipKey, 1, 1);

				logger.info(emailAllowed);
				logger.info(ipAllowed);

				if (!emailAllowed) {
					response.put("message", "OTP already sent. Please wait 1 minute before requesting again.");
					return response;
				}
				if (!ipAllowed) {
					response.put("message", "OTP requests from your IP are limited. Please try again in 1 minute.");
					return response;
				}

				Random rand = new Random();
				String created_by = userrepo.getUser_id(email_id);

				int password_otp = 100000 + rand.nextInt(900000);

				boolean otpUpdated =
				        otpSyncService.updateOtpInBothServers(
				                password_otp,
				                created_by);

				if (otpUpdated) {

				    String usernameByEmail =
				            userrepo.findUsernameByEmail(email_id);

				    emailSender.sendEmailLink(
				            email_id,
				            String.valueOf(password_otp),
				            usernameByEmail);

				    response.put(
				            "message",
				            "Email sent successfully!");

				} else {

				    response.put(
				            "message",
				            "MAIL NOT SENT");
				}

			} else {

				String clientIp = request.getRemoteAddr();
				String invalidEmailKey = "INVALID_EMAIL_" + clientIp;

				boolean allowed = rateLimiterService.tryConsume(invalidEmailKey, 2, 2);

				if (!allowed) {
					response.put("message", "Too many invalid email attempts. Please wait 2 minutes.");
					return response;
				}

				response.put("message", "Email ID is invalid. Please provide a valid email.");
				return response;
			}

		} catch (Exception e) {
			logger.error("ERROR IN userMailSend: " + e.getMessage(), e);
			response.put("message", "An error occurred while processing the request.");
		}

		logger.info("EXECUTED METHOD :: userMailSend");
		return response;
	}

//	@PostMapping("/user/verifyotp")
//	public @ResponseBody Map<String, Object> verifyOTP(@RequestParam String otp) {
//	    logger.info("EXECUTING METHOD :: verifyOTP");
//	    Map<String, Object> response = new HashMap<String, Object>();
//	    try {
//	        String email = userrepo.getEmailId(otp);
//	        
//	        if (email == null) {
//	            response.put("status", "no");
//	            response.put("message", "OTP IS INVALID");
//	            logger.warn("Invalid OTP received");
//	            return response;
//	        }
//	        
//	        String created_by = userrepo.getUser_id(email);
//	        UserInterface userInterface = userrepo.getOtpTimeStamp(created_by);
//	        String otpCheck = userInterface.getPassword_otp();
//	        String timeStamp = userInterface.getOtp_timevalidation();
//	        
//	        if (otpCheck == null || otpCheck.isEmpty() || timeStamp == null || timeStamp.isEmpty()) {
//	            response.put("status", "no");
//	            response.put("message", "OTP IS INVALID");
//	            return response;
//	        }
//	        
//	        // Check if OTP matches
//	        if (!otpCheck.equals(otp)) {
//	            response.put("status", "no");
//	            response.put("message", "OTP is Wrong");
//	            logger.warn("Incorrect OTP entered");
//	            return response;
//	        }
//	        
//	        // Check if OTP has expired
//	        long currentTime = System.currentTimeMillis();
//	        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//	            .appendPattern("yyyy-MM-dd HH:mm:ss")
//	            .optionalStart()
//	            .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
//	            .optionalEnd()
//	            .toFormatter();
//
//	        LocalDateTime dateTime = LocalDateTime.parse(timeStamp, formatter);
//	        long millis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//	        long timeDifference = currentTime - millis;
//	        long expiryTime = getEmailTimeLimit(); // 2 minutes in milliseconds
//	        
//	        if (timeDifference > expiryTime) {
//	            userrepo.updateOtp(email);
//	            response.put("status", "no");
//	            response.put("message", "OTP expired");
//	            logger.warn("OTP has expired");
//	            return response;
//	        }
//	        
//	        // OTP is valid
//	        response.put("status", "yes");
//	        response.put("message", "OTP verified successfully");
//	        response.put("email", email);
//	        logger.info("OTP verified successfully for email: " + email);
//	        return response;
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        logger.error("ERROR IN THE METHOD FOR verifyOTP :: -> " + e.getMessage());
//	        response.put("status", "no");
//	        response.put("message", "An error occurred during OTP verification");
//	    }
//	    
//	    logger.info("EXECUTED METHOD :: verifyOTP");
//	    return response;
//	}

//	 G2 Users

	@PostMapping("/user/verifyotp")
	public @ResponseBody Map<String, Object> verifyOTP(@RequestParam String otp) {
		logger.info("EXECUTING METHOD :: verifyOTP");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String email = userrepo.getEmailId(otp);

			if (email == null) {
				response.put("status", "no");
				response.put("message", "OTP IS INVALID");
				logger.warn("Invalid OTP received");
				return response;
			}

			String created_by = userrepo.getUser_id(email);
			UserInterface userInterface = userrepo.getOtpTimeStamp(created_by);
			String otpCheck = userInterface.getPassword_otp();
			String timeStamp = userInterface.getOtp_timevalidation();

			if (otpCheck == null || otpCheck.isEmpty() || timeStamp == null || timeStamp.isEmpty()) {
				response.put("status", "no");
				response.put("message", "OTP IS INVALID");
				return response;
			}

			// Check if OTP matches
			if (!otpCheck.equals(otp)) {
				response.put("status", "no");
				response.put("message", "OTP is Wrong");
				logger.warn("Incorrect OTP entered");
				return response;
			}

			// Check if OTP has expired
			long currentTime = System.currentTimeMillis();
			DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss")
					.optionalStart().appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true).optionalEnd()
					.toFormatter();

			LocalDateTime dateTime = LocalDateTime.parse(timeStamp, formatter);
			long millis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			long timeDifference = currentTime - millis;
			long expiryTime = getEmailTimeLimit(); // 2 minutes in milliseconds

			if (timeDifference > expiryTime) {
				//userrepo.updateOtp(email);
				
				otpSyncService.clearOtpInBothServers(email);
				response.put("status", "no");
				response.put("message", "OTP expired");
				logger.warn("OTP has expired");
				return response;
			}

			// OTP is valid
			response.put("status", "yes");
			response.put("message", "OTP verified successfully");
			response.put("email", email);
			logger.info("OTP verified successfully for email: " + email);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN THE METHOD FOR verifyOTP :: -> " + e.getMessage());
			response.put("status", "no");
			response.put("message", "An error occurred during OTP verification");
		}

		logger.info("EXECUTED METHOD :: verifyOTP");
		return response;
	}

//	@PostMapping("/user/otpvalidation")
//	public @ResponseBody Map<String, Object> userOTPValidation(
//	        @RequestParam String otp,
//	        @RequestParam String newPassword,
//	        @RequestParam String confirmPassword) {
//
//	    logger.info("EXECUTING METHOD :: userOTPValidation");
//	    Map<String, Object> response = new HashMap<String, Object>();
//
//	    try {
//	    
//	    	
//
//	        // Only check password match
//	        if (!newPassword.equals(confirmPassword)) {
//	            response.put("message", "NEW Password and Confirm Password Mismatched please give valid password");
//	            response.put("status", "no");
//	            return response;
//	        }
//
//	        //  Get email using OTP (no validation check now)
//	        String email = userrepo.getEmailId(otp);
//	        if (email == null) {
//	            response.put("message", "Invalid Request");
//	            response.put("status", "no");
//	            return response;
//	        }
//
//	        String created_by = userrepo.getid(email);
//	        
//	        String existingPassword = userrepo.getPassword(email);
//
//	        //  Encrypt password
//	        String encryptPassword = StringEncrypter.testEncrypt(newPassword);
//
//	        if(encryptPassword.equals(existingPassword)) {
//	        	 response.put("message", "You cannot set your current password as New Password. Please choose a new password.");
//		            response.put("status", "no");
//		            return response;
//	        }
//	        
//	        //  Update password
//	        userrepo.updatePasswordForUser(encryptPassword,created_by );
//
//	      
//
//	        response.put("status", "yes");
//	        response.put("message", "Password Reset Successfully.");
//	        logger.info("Password reset successfully for user: " + created_by);
//
//	        return response;
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        logger.error("ERROR IN THE METHOD FOR userOTPValidation :: -> " + e.getMessage());
//	        response.put("status", "no");
//	        response.put("message", "An error occurred during password reset");
//	    }
//
//	    logger.info("EXECUTED METHOD :: userOTPValidation");
//	    return response;
//	}

	@PostMapping("/user/otpvalidation")
	public @ResponseBody Map<String, Object> userOTPValidation(@RequestParam String otp,
			@RequestParam String newPassword, @RequestParam String confirmPassword) {

		logger.info("EXECUTING METHOD :: userOTPValidation");

		Map<String, Object> response = new HashMap<>();

		try {

			// Password match validation
			if (!newPassword.equals(confirmPassword)) {

				response.put("message", "NEW Password and Confirm Password Mismatched please give valid password");

				response.put("status", "no");

				return response;
			}

			// Get email from OTP
			String email = userrepo.getEmailId(otp);

			if (email == null) {

				response.put("message", "Invalid Request");
				response.put("status", "no");

				return response;
			}

			String created_by = userrepo.getid(email);

			// Existing encrypted password
			String existingPassword = userrepo.getPassword(email);

			// SAME ENCRYPTION USED IN LOGIN
			String encryptPassword = encryptPasswordG2.encryptPassword(newPassword);

			// Prevent same password reuse
			if (encryptPassword.equals(existingPassword)) {

				response.put("message",
						"You cannot set your current password as New Password. Please choose a new password.");

				response.put("status", "no");

				return response;
			}

			// Update password
			boolean passwordUpdated =
			        otpSyncService.updatePasswordInBothServers(
			                encryptPassword,
			                created_by);

			if (passwordUpdated) {

			    response.put("status", "yes");
			    response.put("message", "Password Reset Successfully.");

			    logger.info(
			            "Password reset successfully for user: "
			            + created_by);

			} else {

			    response.put("status", "no");
			    response.put(
			            "message",
			            "Password reset failed in one of the servers.");
			}

			return response;

		} catch (Exception e) {

			e.printStackTrace();

			logger.error("ERROR IN THE METHOD FOR userOTPValidation :: -> " + e.getMessage());

			response.put("status", "no");
			response.put("message", "An error occurred during password reset");
		}

		logger.info("EXECUTED METHOD :: userOTPValidation");

		return response;
	}

	@PostMapping("/user/resetpassword")
	public Map<String, Object> userRestPassword(

			@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword,
			@RequestParam String email_id) {

		logger.info("EXECUTING METHOD :: userRestPassword");

		Map<String, Object> response = new HashMap<>();

		try {

			int checkValue = userrepo.checkEmailExits(email_id);

			int checkstatus = userrepo.checkEmailExitsStaus(email_id);

			// CHECK INACTIVE
			if (checkstatus > 0) {

				response.put("message", "Your account is inactive. Please contact admin");

				response.put("status", "no");

				return response;
			}

			// CHECK EMAIL EXISTS
			if (checkValue <= 0) {

				response.put("message", "Email Id is Invalid. Please provide valid Email");

				response.put("status", "no");

				return response;
			}

			String existingPassword = userrepo.getPassword(email_id);

			String created_by = userrepo.getid(email_id);

			// ENCRYPT OLD PASSWORD SAME AS LOGIN
			String encryptedOldPassword = encryptPasswordG2.encryptPassword(oldPassword);

			// VALIDATE OLD PASSWORD
			if (!encryptedOldPassword.equals(existingPassword)) {

				response.put("message", "Old Password is wrong");

				response.put("status", "no");

				return response;
			}

			// CHECK NEW & CONFIRM MATCH
			if (!newPassword.equals(confirmPassword)) {

				response.put("message", "NEW Password and Confirm Password Mismatched");

				response.put("status", "no");

				return response;
			}

			// PREVENT SAME PASSWORD
			if (newPassword.equals(oldPassword)) {

				response.put("message",
						"You cannot set your current password as New Password. Please choose a new password.");

				response.put("status", "no");

				return response;
			}

			// ENCRYPT NEW PASSWORD
			String encryptPassword = encryptPasswordG2.encryptPassword(confirmPassword);

			// UPDATE PASSWORD
			boolean passwordUpdated =
			        otpSyncService.updatePasswordInBothServers(
			                encryptPassword,
			                created_by);

			if (passwordUpdated) {

			    response.put("status", "yes");
			    response.put("message", "Password Reset Successfully.");

			    logger.info(
			            "Password reset successfully for user: "
			            + created_by);

			} else {

			    response.put("status", "no");
			    response.put(
			            "message",
			            "Password reset failed in one of the servers.");
			}

		
		} catch (Exception e) {

			e.printStackTrace();

			logger.error("ERROR IN THE METHOD FOR userRestPassword :: -> " + e.getMessage());

			response.put("status", "no");

			response.put("message", "An error occurred while resetting password");
		}

		logger.info("EXECUTED METHOD :: userRestPassword");

		return response;
	}

//	@PostMapping("/user/otpvalidation")
//	public @ResponseBody Map<String, Object> userOTPValidation(
//	        @RequestParam String otp,
//	        @RequestParam String newPassword,
//	        @RequestParam String confirmPassword) {
//
//	    logger.info("EXECUTING METHOD :: userOTPValidation");
//	    Map<String, Object> response = new HashMap<String, Object>();
//
//	    try {
//	    
//	    	
//
//	        // Only check password match
//	        if (!newPassword.equals(confirmPassword)) {
//	            response.put("message", "NEW Password and Confirm Password Mismatched please give valid password");
//	            response.put("status", "no");
//	            return response;
//	        }
//
//	        //  Get email using OTP (no validation check now)
//	        String email = userrepo.getEmailId(otp);
//	        if (email == null) {
//	            response.put("message", "Invalid Request");
//	            response.put("status", "no");
//	            return response;
//	        }
//
//	        String created_by = userrepo.getid(email);
//	        
//	        String existingPassword = userrepo.getPassword(email);
//
//	        //  Encrypt password
//	        String encryptPassword = StringEncrypter.testEncrypt(newPassword);
//
//	        if(encryptPassword.equals(existingPassword)) {
//	        	 response.put("message", "You cannot set your current password as New Password. Please choose a new password.");
//		            response.put("status", "no");
//		            return response;
//	        }
//	        
//	        //  Update password
//	        userrepo.updatePasswordForUser(encryptPassword,created_by );
//
//	      
//
//	        response.put("status", "yes");
//	        response.put("message", "Password Reset Successfully.");
//	        logger.info("Password reset successfully for user: " + created_by);
//
//	        return response;
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        logger.error("ERROR IN THE METHOD FOR userOTPValidation :: -> " + e.getMessage());
//	        response.put("status", "no");
//	        response.put("message", "An error occurred during password reset");
//	    }
//
//	    logger.info("EXECUTED METHOD :: userOTPValidation");
//	    return response;
//	}
//
//	

	@GetMapping("/user/checkSession") // ← correct mapping
	public ResponseEntity<Map<String, Object>> checkSession(HttpServletRequest request, HttpServletResponse response) {

		logger.info("EXECUTING METHOD :: checkSession");
		Map<String, Object> resp = new HashMap<>();

		// ── Read cookies ──────────────────────────────────
		Cookie[] cookies = request.getCookies();
		String accessToken = null;
		String refreshToken = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("access_token".equals(cookie.getName())) {
					accessToken = cookie.getValue();
				} else if ("refresh_token".equals(cookie.getName())) {
					refreshToken = cookie.getValue();
				}
			}
		}

		try {
			// ── 1. Validate access token ──────────────────
			if (accessToken != null && jwtUtil.validateToken(accessToken)) {
				String userId = jwtUtil.getUserId(accessToken);
				logger.info("checkSession: Valid access token → user: {}", userId);
				resp.put("status", "yes");
				resp.put("message", "Session valid");
				resp.put("user_id", userId);
				return ResponseEntity.ok(resp);
			}

			// ── 2. Access token expired → try refresh ─────
			if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
				String userId = jwtUtil.getUserId(refreshToken);
				logger.info("checkSession: Refreshing token for user: {}", userId);

				String newAccessToken = jwtUtil.generateAccessToken(userId);

				Cookie newCookie = new Cookie("access_token", newAccessToken);
				newCookie.setHttpOnly(true);
				newCookie.setSecure(false); // true on HTTPS
				newCookie.setPath("/");
				newCookie.setMaxAge(15 * 60);
				response.addCookie(newCookie);

				resp.put("status", "yes");
				resp.put("message", "Session refreshed");
				resp.put("user_id", userId);
				return ResponseEntity.ok(resp);
			}

			// ── 3. Both expired ───────────────────────────
			logger.warn("checkSession: Both tokens expired");
			resp.put("status", "no");
			resp.put("message", "Session expired");
			return ResponseEntity.status(401).body(resp);

		} catch (Exception e) {
			logger.error("checkSession error: {}", e.getMessage());
			resp.put("status", "no");
			resp.put("message", "Error validating session");
			return ResponseEntity.status(500).body(resp);
		}
	}

	@Autowired
	@Qualifier("jimsDataSource")
	private DataSource jimsDataSource;

	@PostMapping("/check-username-email")
	public Map<String, Object> checkUsernameEmail(@RequestBody Map<String, String> request) {

		Map<String, Object> response = new HashMap<>();

		String username = request.get("username");
		String email = request.get("email");

		boolean usernameExists = false;
		boolean emailExists = false;

		String sql = "SELECT username, email " + "FROM USERS_MASTER " + "WHERE is_delete = 0 "
				+ "AND ( (? IS NOT NULL AND username = ?) " + "   OR (? IS NOT NULL AND email = ?) )";

		try (Connection con = jimsDataSource.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			// username params
			pst.setString(1, username);
			pst.setString(2, username);

			// email params
			pst.setString(3, email);
			pst.setString(4, email);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {

					String dbUsername = rs.getString("username");
					String dbEmail = rs.getString("email");

					if (username != null && username.equalsIgnoreCase(dbUsername)) {
						usernameExists = true;
					}

					if (email != null && email.equalsIgnoreCase(dbEmail)) {
						emailExists = true;
					}
				}
			}

			if (usernameExists && emailExists) {
				response.put("status", "BOTH_EXISTS");
				response.put("message", "Username and Email already exist");
			} else if (usernameExists) {
				response.put("status", "USERNAME_EXISTS");
				response.put("message", "Username already exists");
			} else if (emailExists) {
				response.put("status", "EMAIL_EXISTS");
				response.put("message", "Email already exists");
			} else {
				response.put("status", "OK");
				response.put("message", "Available");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "ERROR");
			response.put("message", "Database error");
		}

		return response;
	}

}
