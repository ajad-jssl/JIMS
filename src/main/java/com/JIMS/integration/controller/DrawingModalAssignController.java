package com.JIMS.integration.controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.catalina.valves.LoadBalancerDrainingValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.DrawingModalAssign;
import com.JIMS.integration.entity.UserMasterDTO;
import com.JIMS.integration.interfaces.DrawingModalAssign_Service;



@CrossOrigin
@RestController
@RequestMapping("/api/modalassign")
public class DrawingModalAssignController {
	
	@Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;
	
	@GetMapping("/department18")
    public ResponseEntity<List<UserMasterDTO>> getUsersForDepartment18() {

        List<UserMasterDTO> users = new ArrayList<>();

        String sql = """
        	    SELECT us.user_id AS id,
        	           us.user_name AS user_id, us.user_name AS username  
        	     FROM JIMS.dbo.UserFactoryMappingJIMS ji
        	    LEFT JOIN mis.dbo.Users  us
        	        ON ji.User_Id = us.user_id
        	    WHERE ji.Department_Id = 18
        	    """;
        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserMasterDTO user = new UserMasterDTO();
                user.setId(rs.getInt("id"));
                user.setUserId(rs.getString("user_id"));
             user.setUsername(rs.getString("username"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

	
	
	  @Autowired
	    private DrawingModalAssign_Service drawingmodal_service;
	  
	  
	  @PostMapping
	    public ResponseEntity<Object> addModalAssign(@RequestBody DrawingModalAssign modalAssign) {
	        drawingmodal_service.saveModalAssign(modalAssign);
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body("New Modal Assignment Added Successfully");
	    }
	  
	  
	  @GetMapping("/getAll")
	    public ResponseEntity<List<DrawingModalAssign>> getAllModalAssigns() {
	        List<DrawingModalAssign> list = drawingmodal_service.getAllModalAssigns();
	        if (list.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(list);
	    }
	  
	  @GetMapping("/get/{id}")
	    public ResponseEntity<Object> getModalAssignById(@PathVariable Integer id) {
	        Optional<DrawingModalAssign> modalAssign = drawingmodal_service.getModalAssignById(id);
	        if (modalAssign.isPresent()) {
	            return ResponseEntity.ok(modalAssign.get());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Modal Assignment not found with ID: " + id);
	        }
	    }
	  @PutMapping("/modify/{id}")
	    public ResponseEntity<Object> modifyModalAssign(@PathVariable Integer id,
	                                                    @RequestBody DrawingModalAssign modalAssign) {
	        Optional<DrawingModalAssign> existing = drawingmodal_service.getModalAssignById(id);
	        
	        
	        Integer count = drawingmodal_service.isUsedModalAssign(modalAssign.getUserId(), modalAssign.getTypeId());
	        
	        System.out.println("the see what the userId ="+modalAssign.getUserId()+ "and type id ="+modalAssign.getTypeId()+"is sending and gettignteh count also" +count);
	        if(count>0) {
	        	 return ResponseEntity
		 	                .status(HttpStatus.CONFLICT)
		 	                .body("The Transcation is Already Exist for  this User");
	        }
	        

	        if (existing.isPresent()) {
	            DrawingModalAssign update = existing.get();

	            if (modalAssign.getUserId() != null) update.setUserId(modalAssign.getUserId());
	            if (modalAssign.getTypeId() != null) update.setTypeId(modalAssign.getTypeId());
	            if (modalAssign.getStatus() != null) update.setStatus(modalAssign.getStatus());
	            if (modalAssign.getCreatedBy() != null) update.setCreatedBy(modalAssign.getCreatedBy());
	            if (modalAssign.getCreatedDate() != null) update.setCreatedDate(modalAssign.getCreatedDate());
	            if (modalAssign.getModifiedBy() != null) update.setModifiedBy(modalAssign.getModifiedBy());
	            if (modalAssign.getModifiedDate() != null) update.setModifiedDate(modalAssign.getModifiedDate());
	            

	            drawingmodal_service.saveModalAssign(update);

	            return ResponseEntity.ok(update);
	        }

	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Modal Assignment not found with ID: " + id);
	    }
		/*
		 * @GetMapping("/check/user") public ResponseEntity<Map<String, Object>>
		 * checkUserDuplicate(
		 * 
		 * @RequestParam Integer userId) {
		 * 
		 * boolean exists = drawingmodal_service.isUserAlreadyAssigned(userId);
		 * 
		 * Map<String, Object> response = new HashMap<>(); response.put("exists",
		 * exists); response.put("message", exists ? "User already assigned" :
		 * "User not assigned");
		 * 
		 * return ResponseEntity.ok(response); }
		 */
	  
	  @GetMapping("/check/user-type")
	  public ResponseEntity<Map<String, Object>> checkUserTypeDuplicate(
	          @RequestParam Integer userId,
	          @RequestParam Integer typeId) {

	      boolean exists =
	              drawingmodal_service.isUserAlreadyAssignedToType(userId, typeId);

	      Map<String, Object> response = new HashMap<>();
	      response.put("exists", exists);
	      response.put(
	          "message",
	          exists
	              ? "User already assigned to this modal"
	              : "User not assigned to this modal"
	      );

	      return ResponseEntity.ok(response);
	  }
	  
	  
	  
	  @GetMapping("/count")
	    public ResponseEntity<Long> getModalAssignCount() {
	        Long count = drawingmodal_service.getModalAssignCount();
	        return ResponseEntity.ok(count);
	    }
	  
}
