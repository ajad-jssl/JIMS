package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.Shift_Master;
import com.JIMS.integration.repository.Shift_Master_Repistory;

@Service
public class Shift_Master_Service {
	
	@Autowired
	private Shift_Master_Repistory shift_master_repo;
	
	public List<Shift_Master> getAllShift(){
		return shift_master_repo.findAll();
	}
	
	public Optional<Shift_Master> getShiftById(Integer id){
		return shift_master_repo.findById(id);
	}

	public void addShift(Shift_Master shift_body) {
		shift_master_repo.save(shift_body);
	}
	
	public List<Shift_Master> findDuplicate(String code){
		return shift_master_repo.findByShiftCode(code);
	}
	
	public Long shiftCount() {
		return shift_master_repo.count();
	}
	
	public Integer checkUsedshift(Integer shift_id) {
		return shift_master_repo.CheckUsedShift(shift_id);
	}
	
	
	
}
