package com.JIMS.integration.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.JIMS.integration.entity.TypeSelection;
import com.JIMS.integration.interfaces.Type_Selection_Service;

@CrossOrigin
@RestController
@RequestMapping("/api/type_selection")
public class TypeSelectionMasterController {

	@Autowired
	private Type_Selection_Service type_service;
	
	@PostMapping
	public ResponseEntity<Object> addTypes(@RequestBody TypeSelection typeselection){
		
		 type_service.addType(typeselection);
		 return ResponseEntity.status(HttpStatus.CREATED).body("New Type is Added Successfully");
	}
	
	   @GetMapping("getAll")
	    public ResponseEntity<Object> getAllTypes() {
	        try {
	            List<TypeSelection> allTypes = type_service.getAllType();
	            return ResponseEntity.status(HttpStatus.OK).body(allTypes);
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	        }
	    }
	
	   
	    @GetMapping("get/{id}")
	    public ResponseEntity<Object> getTypeById(@PathVariable Integer id) {
	        try {
	            Optional<TypeSelection> typeById = type_service.getTypeById(id);

	            if (typeById.isPresent()) {
	                return ResponseEntity.status(HttpStatus.OK).body(typeById.get());
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Type not found");
	            }

	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	        }
	    }
	
	    @GetMapping("check/type")
	    public ResponseEntity<Object> checkDuplicate(@RequestParam String typeName) {

	        List<TypeSelection> duplicate = type_service.checkDuplicate(typeName);

	        if (!duplicate.isEmpty()) {
	            return ResponseEntity.ok(
	                    Map.of("exists", true, "typeId", duplicate.get(0).getTypeId())
	            );
	        } else {
	            return ResponseEntity.ok(Map.of("exists", false));
	        }
	    }
	    
	    @PutMapping("modified/{id}")
	    public ResponseEntity<Object> modifyType(
	            @PathVariable Integer id,
	            @RequestBody TypeSelection typeSelection) {

	        Optional<TypeSelection> typeById = type_service.getTypeById(id);
	        
	        int count = type_service.checkUsedtype(id);
	        
	        if(count>0) {
	        	 return ResponseEntity
	 	                .status(HttpStatus.CONFLICT)
	 	                .body("The Transcation is Already Exist for  this Type Master");
	        }

	        if (typeById.isPresent()) {

	            TypeSelection existingType = typeById.get();

	            if (typeSelection.getTypeName() != null) {
	                existingType.setTypeName(typeSelection.getTypeName());
	            }
	            if (typeSelection.getCreatedBy() != null) {
	                existingType.setCreatedBy(typeSelection.getCreatedBy());
	            }
	            if (typeSelection.getCreatedDate() != null) {
	                existingType.setCreatedDate(typeSelection.getCreatedDate());
	            }
	            if (typeSelection.getModifiedBy() != null) {
	                existingType.setModifiedBy(typeSelection.getModifiedBy());
	            }
	            if (typeSelection.getModifiedDate() != null) {
	                existingType.setModifiedDate(typeSelection.getModifiedDate());
	            }
	            if (typeSelection.getStatus() != null) {
	                existingType.setStatus(typeSelection.getStatus());
	            }

	            type_service.addType(existingType);

	            return ResponseEntity.ok(existingType);
	        }

	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Type not found with ID: " + id);
	    }
	    
	    

	    
	    
	    @GetMapping("/mapping")
	    public ResponseEntity<Object> getTypeIdAndNameOnly() {
	        try {
	            List<Object[]> list = type_service.getTypeIdAndNameOnly();
	            List<Map<String, Object>> result = new ArrayList<>();

	            for (Object[] row : list) {
	                Map<String, Object> map = new HashMap<>();
	                map.put("typeId", ((Number) row[0]).intValue());
	                map.put("typeName", (String) row[1]);
	                result.add(map);
	            }

	            return ResponseEntity.status(HttpStatus.OK).body(result);
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	        }
	    }
	    
	    @GetMapping("/count")
	    public ResponseEntity<Long> gettyppecounts(){
	    	Long gettypecount = type_service.gettypecount();
	    	return ResponseEntity.status(HttpStatus.OK).body(gettypecount);
	    }
	    
	    
	    
	    
	    
}
