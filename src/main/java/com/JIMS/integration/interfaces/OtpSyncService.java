package com.JIMS.integration.interfaces;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JIMS.integration.repository.UserRepository;

@Service
public class OtpSyncService {

	  @Autowired
	    private UserRepository userrepo;

	    @Autowired
	    @Qualifier("misDataSource2")
	    private DataSource misDataSource2;

	    public boolean updateOtpInBothServers(
	            int passwordOtp,
	            String createdBy) {

	        try {

	            // PRIMARY DB
	            int count1 =
	                    userrepo.storeOtpTimestamp(
	                            passwordOtp,
	                            createdBy);

	            if (count1 == 0) {
	                return false;
	            }

	            // SECONDARY DB
	            JdbcTemplate jdbcTemplate =
	                    new JdbcTemplate(misDataSource2);

	            String sql =
	                    "UPDATE MIS.dbo.Users " +
	                    "SET password_otp = ?, " +
	                    "otp_timevalidation = CURRENT_TIMESTAMP " +
	                    "WHERE user_name = ? " +
	                    "AND deleted = 0";

	            int count2 =
	                    jdbcTemplate.update(
	                            sql,
	                            passwordOtp,
	                            createdBy);

	            return count2 != 0;

	        } catch (Exception e) {

	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    
	    public boolean clearOtpInBothServers(String email) {

	        try {

	            // PRIMARY DATABASE
	            int count1 =
	                    userrepo.updateOtp(email);

	            if (count1 == 0) {
	                return false;
	            }

	            // SECONDARY DATABASE
	            JdbcTemplate jdbcTemplate =
	                    new JdbcTemplate(misDataSource2);

	            String sql =
	                    "UPDATE MIS.dbo.Users " +
	                    "SET password_otp = NULL " +
	                    "WHERE user_email = ? " +
	                    "AND deleted = 0";

	            int count2 =
	                    jdbcTemplate.update(
	                            sql,
	                            email);

	            return count2 != 0;

	        } catch (Exception e) {

	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    
	    
	    
	    public boolean UpdatePasswordInBothServers(String email) {

	        try {

	            // PRIMARY DATABASE
	            int count1 =
	                    userrepo.updateOtp(email);

	            if (count1 == 0) {
	                return false;
	            }

	            // SECONDARY DATABASE
	            JdbcTemplate jdbcTemplate =
	                    new JdbcTemplate(misDataSource2);

	            String sql =
	                    "UPDATE MIS.dbo.Users " +
	                    "SET password_otp = NULL " +
	                    "WHERE user_email = ? " +
	                    "AND deleted = 0";

	            int count2 =
	                    jdbcTemplate.update(
	                            sql,
	                            email);

	            return count2 != 0;

	        } catch (Exception e) {

	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    public boolean updatePasswordInBothServers(
	            String encryptedPassword,
	            String createdBy) {

	        try {

	            // PRIMARY DATABASE
	            int count1 =
	                    userrepo.updatePasswordForUser(
	                            encryptedPassword,
	                            createdBy);

	            if (count1 == 0) {
	                return false;
	            }

	            // SECONDARY DATABASE
	            JdbcTemplate jdbcTemplate =
	                    new JdbcTemplate(misDataSource2);

	            String sql =
	                    "UPDATE MIS.dbo.Users " +
	                    "SET user_password = ?, " +
	                    "user_password_last_changed = GETDATE(), " +
	                    "password_otp = NULL, " +
	                    "otp_timevalidation = NULL " +
	                    "WHERE user_name = ? " +
	                    "AND deleted = 0";

	            int count2 =
	                    jdbcTemplate.update(
	                            sql,
	                            encryptedPassword,
	                            createdBy);

	            return count2 != 0;

	        } catch (Exception e) {

	            e.printStackTrace();
	            return false;
	        }
	    }
}