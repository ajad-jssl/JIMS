package com.JIMS.integration.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.TypeSelection;
import com.JIMS.integration.repository.Type_Selection_Repository;

@Service
public class Type_Selection_Service {

	@Autowired
	private Type_Selection_Repository type_selection_repo;
	
	
	
	public List<TypeSelection> getAllType(){
		return type_selection_repo.findAll();
	}
	
	public Optional<TypeSelection> getTypeById(Integer id){
		return type_selection_repo.findById(id);
	}
	
	public void addType(TypeSelection typeselection) {
		type_selection_repo.save(typeselection);
	}
	
	public List<TypeSelection> checkDuplicate(String type){
		return type_selection_repo.findByTypeName(type);
	}
	
	public List<Object[]> getTypeIdAndNameOnly() {
        return type_selection_repo.findTypeIdAndNameOnly();
    }
	
	public Long gettypecount() {
		return type_selection_repo.count();
	}
	
	
	public Integer checkUsedtype(Integer type_id) {
		return type_selection_repo.CheckUsedType(type_id);
	}
	
}
