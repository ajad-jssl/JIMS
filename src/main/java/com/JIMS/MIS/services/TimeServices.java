package com.JIMS.MIS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.MIS.Repository.TimeRepository;
import com.JIMS.MIS.model.TimeModel;
 
@Service
public class TimeServices {

	  @Autowired
	    private TimeRepository TimeRepository;

	    public List<TimeModel> getweightByload_id(Integer load_id) {
	        return TimeRepository.findweightByload_id(load_id); 
	    } 
}
