package com.JIMS.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JIMS.integration.entity.ActivityCategory;


@Repository
public interface Activity_Category_Repository extends JpaRepository<ActivityCategory, Integer> {
	List<ActivityCategory> findByActivityName(String activityName);

    @Query(value = "SELECT ACTIVITY_ID, ACTIVITY_NAME FROM ACTIVITY_CATEGORY_MASTER", nativeQuery = true)
    List<Object[]> findActivityIdAndNameOnly();
    
    
    @Query(value="select  count(ACTIVITY_CATEGORY_ID)  from Jc_Drawntxn where ACTIVITY_CATEGORY_ID=:activity_id",nativeQuery = true)
    Integer checkActivityUsed(@Param("activity_id") Integer activity_id);
}
