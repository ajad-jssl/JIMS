package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.ActivityCategory;
import com.JIMS.integration.repository.Activity_Category_Repository;

@Service
public class Activity_Category_Service {

	@Autowired
	private Activity_Category_Repository activity_repo;
	
	
	

	public List<ActivityCategory> getAllActivity() {
        return activity_repo.findAll();
    }

    public Optional<ActivityCategory> getActivityById(Integer id) {
        return activity_repo.findById(id);
    }

    public void addActivity(ActivityCategory activityCategory) {
        activity_repo.save(activityCategory);
    }

    public List<ActivityCategory> checkDuplicate(String activityName) {
        return activity_repo.findByActivityName(activityName);
    }

    public List<Object[]> getActivityIdAndNameOnly() {
        return activity_repo.findActivityIdAndNameOnly();
    }
	
    public Long getActivityCount() {
    	return activity_repo.count();
    }
    
    public Integer checkActivityUsed(Integer activity_id) {
    	return activity_repo.checkActivityUsed(activity_id);
    }
}
