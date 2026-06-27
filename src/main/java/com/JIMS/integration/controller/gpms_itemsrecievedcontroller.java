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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.gpms_itemsrecieved;
import com.JIMS.integration.interfaces.gpms_itemsrecievedservice;       

@CrossOrigin
@RestController
@RequestMapping("/api/itemsrecieved")
public class gpms_itemsrecievedcontroller {
	@Autowired
	private gpms_itemsrecievedservice itemService;

	@PostMapping
	    public ResponseEntity<Object> addOrUpdateItem(@RequestBody gpms_itemsrecieved item) {
	        try {
	        	gpms_itemsrecieved savedItem = itemService.saveOrUpdateItem(item);
	            return ResponseEntity.status(HttpStatus.CREATED)
	                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
	        }
	    }
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateItemById(@PathVariable int id, @RequestBody gpms_itemsrecieved item) {
	    Optional<gpms_itemsrecieved> existingItem = itemService.getItemById(id);

	    if (existingItem.isPresent()) {
	        gpms_itemsrecieved updatedItem = existingItem.get();
	        
	        // Update the fields only if the corresponding value in the request body is not null
	        if (item.getGp_no() != null) updatedItem.setGp_no(item.getGp_no());
	        if (item.getGps_entry() != null) updatedItem.setGps_entry(item.getGps_entry());
	        if (item.getReturn_qty() != 0) updatedItem.setReturn_qty(item.getReturn_qty());
	        if (item.getReceive_qty() != 0) updatedItem.setReceive_qty(item.getReceive_qty());
	        if (item.getGp_type() != 0) updatedItem.setGp_type(item.getGp_type());
	        if (item.getItemno() != 0) updatedItem.setItemno(item.getItemno());
	        if (item.getDc_ref() != null) updatedItem.setDc_ref(item.getDc_ref());
	        if (item.getSecurity_received() != 0) updatedItem.setSecurity_received(item.getSecurity_received());
	        if (item.getSecurity_remarks() != null) updatedItem.setSecurity_remarks(item.getSecurity_remarks());
	        if (item.getReturn_complete() != 0) updatedItem.setReturn_complete(item.getReturn_complete());
	        if (item.getCreated_by() != 0) updatedItem.setCreated_by(item.getCreated_by());
	        if (item.getCreated_date() != null) updatedItem.setCreated_date(item.getCreated_date());
	        if (item.getModified_by() != 0) updatedItem.setModified_by(item.getModified_by());
	        if (item.getModified_date() != null) updatedItem.setModified_date(item.getModified_date());

	        // Save the updated item
	        itemService.saveOrUpdateItem(updatedItem);

	        return ResponseEntity.status(HttpStatus.OK)
	                             .body(new ResponseMessage("Success", "Record updated successfully"));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(new ResponseMessage("Error", "Item not found"));
	    }
	}


//Helper class for structured response messages
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

//Get an item by its ID
@GetMapping("/{id}")
public ResponseEntity<gpms_itemsrecieved> getItemById(@PathVariable int id) {
 Optional<gpms_itemsrecieved> item = itemService.getItemById(id);
 if (item.isPresent()) {
     return new ResponseEntity<>(item.get(), HttpStatus.OK);
 } else {
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
 }
}

//Get all items
@GetMapping
public ResponseEntity<List<gpms_itemsrecieved>> getAllItems() {
 List<gpms_itemsrecieved> items = itemService.getAllItems();
 if (items.isEmpty()) {
     return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
 }
 return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
}
}