package com.JIMS.MIS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JIMS.MIS.model.TimeModel;

import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<TimeModel, Integer> {

	 @Query(value = "SELECT weight,load_id FROM Loads WHERE load_id =:load_id", nativeQuery = true)
	   
	    List<TimeModel> findweightByload_id(Integer load_id);
}
