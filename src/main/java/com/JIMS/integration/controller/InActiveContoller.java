package com.JIMS.integration.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.MIS.model.Username;
import com.JIMS.integration.entity.InactiveUser;
import com.JIMS.integration.interfaces.Inactive_User_Services;

@CrossOrigin
@RestController
@RequestMapping("api/inactive")
public class InActiveContoller {
	
	
	
	Logger logger = LogManager.getLogger(SeriesMasterNewController.class);
	
	
	@Autowired
	private Inactive_User_Services inactive_user_services;
	
	
	@GetMapping("/is_inactive")
	public ResponseEntity<Object> getalllocedUser(){
		
		try {
			
			List<InactiveUser> allInactiveUserLoced = inactive_user_services.getAllInactiveUserLoced();
			return ResponseEntity.status(HttpStatus.OK).body(allInactiveUserLoced);
			
			
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}
	
	
	

	
	@PutMapping("/activated/{id}")
	public ResponseEntity<Object> modifyUser(
	        @PathVariable int id,
	        @RequestBody InactiveUser inactiveUser) {

	    try {

	        // Get remarks from request body
	        String remarks = inactiveUser.getStatusRemarks();
	        String modified_by = inactiveUser.getModifiedBy();
	        LocalDateTime modifiedate = inactiveUser.getModifiedDate();

	        InactiveUser updated = inactive_user_services.activateUsers(id, remarks,modified_by,modifiedate);

	        return ResponseEntity.ok(updated);

	    } catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(ex.getMessage());
	    }
	}
	
	
	@GetMapping("/is_locked")
	public ResponseEntity<Object> getLockedUser(){
		try {
			List<InactiveUser> alladminblockelement = inactive_user_services.getAlladminblockelement();
			return ResponseEntity.status(HttpStatus.OK).body(alladminblockelement);
			
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}

	@PutMapping("/unlock/{id}")
	public ResponseEntity<Object> unlocedUser(@PathVariable int id, @RequestBody InactiveUser inactiveUser) {
	    try {

	        String remarks = inactiveUser.getUnlockRemarks();
	        String modified_by = inactiveUser.getModifiedBy();
	        LocalDateTime modifiedate = inactiveUser.getModifiedDate();

	        logger.info("Unlock requested by: {}", modified_by);
	        logger.info("Unlock date: {}", modifiedate);

	        inactive_user_services.unlockuser(id, remarks, modified_by, modifiedate);

	        return ResponseEntity.ok().body("Success");

	    } catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	    }
	}
	
	
	
	
	@GetMapping("/mis/is_loced")
	public ResponseEntity<Object> lockedmisUser(){
		try {
			List<Username> getalllockedmisuser = inactive_user_services.getalllockedmisuser();
		return ResponseEntity.status(HttpStatus.OK).body(getalllockedmisuser);
			
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}
	
	
	@GetMapping("/mis/is_inactive")
	public ResponseEntity<Object> getInactivemisuser(){
		try {
			
			List<Username> inactivemisUser = inactive_user_services.getInactivemisUser();
			
			return ResponseEntity.status(HttpStatus.OK).body(inactivemisUser);
		}
	 catch(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	}
	}
	
	
	@PutMapping("mis/activated/{id}")
	public ResponseEntity<Object>    Activatedmisuser(@PathVariable Long id,@RequestBody Username remarks){
		
		try {
			
			String remarks1 = remarks.getRemarks();
			
			Username activatedMisUser = inactive_user_services.activatedMisUser(id, remarks1);
			return ResponseEntity.ok().body("success Full Activated the user");
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}
	
	@PutMapping("mis/unlock/{id}")
	public ResponseEntity<Object> UnlockUser(@PathVariable Long id,@RequestBody Username remarks){
		try {
			String remarks2 = remarks.getRemarks();
			
			Username unlockmisuser = inactive_user_services.unlockmisuser(id, remarks2);
			
			return ResponseEntity.ok().body("The User is Successfully Unlocked");
		}
		catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
		}
	}
	
	
	
	
	
	
	// Deleted Apis
	
	@PutMapping("deleted/{id}")
	public ResponseEntity<Object> DeleteUser(@PathVariable int id,@RequestBody InactiveUser users){
		
		
		try {
			
			String deleteremarks = users.getDeleteRemarks();
			String deletedby = users.getDeletedBy();
			LocalDateTime deleltedDate = users.getDeletedDate();
			
			InactiveUser deleteUsers = inactive_user_services.deleteUsers(id,deleteremarks,deletedby,deleltedDate);
			return ResponseEntity.ok().body("the User is Successfully Deleted");
			
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("deleted failed error");
		}
		
		
		
	}
	
	

}
