package com.JIMS.integration.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.modulemodel;
import com.JIMS.integration.interfaces.moduleservice;
@CrossOrigin
@RestController
@RequestMapping("/api/moudles")
public class modulecontroller {
	
	@Autowired
	private moduleservice regtypeservice;
	
	@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody modulemodel item) {
        try {
        	modulemodel savedItem = regtypeservice.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
	}
	// Helper class for structured response messages
	public static class ResponseMessage {
	    private String status;
	    private String message;

	    public ResponseMessage(String status, String message) {
	        this.status = status;
	        this.message = message;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public String getMessage() {
	        return message;
	    }
	}
	
    	@GetMapping
    	public ResponseEntity<List<modulemodel>> getAllItems() {
    	    List<modulemodel> items = regtypeservice.getAllItems();
    	    if (items.isEmpty()) {
    	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
    	    }
    	    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
    	}

    	@GetMapping("/{id}")
    	public ResponseEntity<modulemodel> getItemById(@PathVariable int id) {
    	    Optional<modulemodel> item = regtypeservice.getItemById(id);
    	    if (item.isPresent()) {
    	        return new ResponseEntity<>(item.get(), HttpStatus.OK);
    	    } else {
    	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	    }
    	}
}
