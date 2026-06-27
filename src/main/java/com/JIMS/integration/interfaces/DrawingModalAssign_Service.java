package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.DrawingModalAssign;
import com.JIMS.integration.repository.DrawingModalAssign_Repositoroy;

@Service
public class DrawingModalAssign_Service {
	

	 @Autowired
	    private DrawingModalAssign_Repositoroy drawingmodal_repo;

	    public DrawingModalAssign saveModalAssign(DrawingModalAssign entity) {
	        return drawingmodal_repo.save(entity);
	    }

	    public List<DrawingModalAssign> getAllModalAssigns() {
	        return drawingmodal_repo.findAll();
	    }

	    public Optional<DrawingModalAssign> getModalAssignById(Integer id) {
	        return drawingmodal_repo.findById(id);
	    }

	    public Long getModalAssignCount() {
	        return drawingmodal_repo.count();
	    }
		/*
		 * public boolean isUserAlreadyAssigned(Integer userId) { return
		 * drawingmodal_repo.existsByUserId(userId); }
		 */
	    public boolean isUserAlreadyAssignedToType(Integer userId, Integer typeId) {
	        return drawingmodal_repo.existsByUserIdAndTypeId(userId, typeId);
	    }
	    
	    
	    
	    public Integer isUsedModalAssign(Integer userId, Integer typeId) {
	    	return drawingmodal_repo.isUsedmodal(userId,typeId);
	    }

}
