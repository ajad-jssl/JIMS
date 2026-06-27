package com.JIMS.integration.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.UsersRepository;
import com.JIMS.MIS.model.Username;
import com.JIMS.integration.entity.InactiveUser;
import com.JIMS.integration.repository.InactiveUserRepository;

@Service
public class Inactive_User_Services {
	
	@Autowired
	private InactiveUserRepository  inactive_user_repo;
	
	public List<InactiveUser> getAllInactiveUserLoced(){
		return inactive_user_repo.findInactiveUsers();
	}
	

	
	
	public void modifiedUser(InactiveUser inactive_user) {
		 inactive_user_repo.save(inactive_user);
	}
	
	
	public InactiveUser activateUsers(int id, String remarks,String modifiedby,LocalDateTime modified_date) {

        InactiveUser user = inactive_user_repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(!user.getIsActive());
        user.setModifiedBy(modifiedby);
		user.setModifiedDate(modified_date);
        user.setStatusRemarks(remarks);

        return inactive_user_repo.save(user);  // <-- MANUAL UPDATE HERE
    }

	
	public InactiveUser unlockuser(int id , String remarks,String modifiedby,LocalDateTime modified_date) {
		InactiveUser user1 = inactive_user_repo.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
		user1.setIsAdminLocked(false);
		user1.setLoginAttempt(0);
		user1.setModifiedBy(modifiedby);
		user1.setModifiedDate(modified_date);
		user1.setUnlockRemarks(remarks);
		return inactive_user_repo.save(user1);
		
	}
	
	
	
	public List<InactiveUser> getAlladminblockelement(){
		return inactive_user_repo.findInactiveUserloked();
	}
	
	
	
	
	
	
	@Autowired
	private UsersRepository missusers;
	
	@Autowired
	@Qualifier("misDataSource2")
	private DataSource misDataSource2;
	
	public List<Username> getalllockedmisuser(){
		return missusers.findActiveUnlockedUsers();
	}
	
	
	public List<Username> getInactivemisUser(){
		return missusers.findInactiveUser();
	}
	
	
	
	public Username activatedMisUser(Long id,String remarks) {
		Username misuser = missusers.findById(id).orElseThrow(()->new RuntimeException("user Not found"));
		misuser.setInactive(0);
		misuser.setRemarks(remarks);
	return	missusers.save(misuser);
		
	}
	
	public Username unlockmisuser(Long id,String remarks) {
		Username misuser = missusers.findById(id).orElseThrow(()->new RuntimeException("user Not found"));
		misuser.setLocked(0);
		misuser.setRemarks(remarks);
		misuser.setPasswordAttempts(0);
		 updateUserInMis2(id, remarks);

		return missusers.save(misuser);
		
	}
	
	private void updateUserInMis2(Long id, String remarks) {

	    JdbcTemplate jdbcTemplate2 = new JdbcTemplate(misDataSource2);

	    String sql =
	        "UPDATE MIS.dbo.Users " +
	        "SET locked = 0, " +
	        "remarks = ?, " +
	        "password_attempts = 0 " +
	        "WHERE user_id = ?";

	    jdbcTemplate2.update(sql, remarks, id);
	}
	
	
	
	public InactiveUser deleteUsers(int id, String remarks,String modifiedby,LocalDateTime modified_date) {

        InactiveUser user = inactive_user_repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

		/* user.setIsActive(!user.getIsActive()); */
        user.setModifiedBy(modifiedby);
		user.setModifiedDate(modified_date);
        user.setDeletedBy(modifiedby);
        user.setDeletedDate(modified_date);
        user.setIsDeleted(!user.getIsDeleted());
        user.setDeleteRemarks(remarks);

        return inactive_user_repo.save(user);  // <-- MANUAL UPDATE HERE
    }
	
	
	

}
