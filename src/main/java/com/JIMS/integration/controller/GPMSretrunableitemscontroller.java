package com.JIMS.integration.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.GPMSreturnableitems;
import com.JIMS.integration.interfaces.gpms_returnableitemsservice;

@CrossOrigin
@RestController
@RequestMapping("/api/returnableitems")
public class GPMSretrunableitemscontroller {
	

    @Autowired
    private gpms_returnableitemsservice returnableitemsservice;

    @PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody GPMSreturnableitems item) {
        try {
        	GPMSreturnableitems savedItem = returnableitemsservice.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
    }
    
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
    
    @PostMapping("/{id}")
    public ResponseEntity<Object> updateItemById(@PathVariable int id, @RequestBody GPMSreturnableitems item) {
    	
    	

        System.out.println("Received Request Body: " + item);
    
        Optional<GPMSreturnableitems> existingItem = returnableitemsservice.getItemById(id);
        
        if (existingItem.isPresent()) {
            GPMSreturnableitems updatedItem = existingItem.get();
            
            // Check and update only the provided fields
            
            if (item.getGp_no() != null) {
                updatedItem.setGp_no(item.getGp_no());
            }
            if (item.getItem() != null) {
                updatedItem.setItem(item.getItem());
            }
            if (item.getItmPrice() != null) {
                updatedItem.setItmPrice(item.getItmPrice());
            }
            if (item.getItmRemarks() != null) {
                updatedItem.setItmRemarks(item.getItmRemarks());
            }
            if (item.getQty() != 0.0) { // Assuming 0.0 is the default value you want to avoid
                updatedItem.setQty(item.getQty());
            }

            if (item.getCreated_by() != null) {  // Optional check for non-default values
                updatedItem.setCreated_by(item.getCreated_by());
            }
            if (item.getCreated_date() != null) {
                updatedItem.setCreated_date(item.getCreated_date());
            }
            if (item.getModified_by() != null) {
                updatedItem.setModified_by(item.getModified_by());
            }

            if (item.getModified_date() != null) {
                updatedItem.setModified_date(item.getModified_date());
            }

            // Save the updated item
            returnableitemsservice.saveOrUpdateItem(updatedItem);
            
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new ResponseMessage("Success", "Record updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ResponseMessage("Error", "Update Not successful"));
        }
    }


    // Get an item by its ID
    @GetMapping("/{id}")
    public ResponseEntity<GPMSreturnableitems> getItemById(@PathVariable int id) {
        Optional<GPMSreturnableitems> item = returnableitemsservice.getItemById(id);
        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Get all items
    @GetMapping
    public ResponseEntity<List<GPMSreturnableitems>> getAllItems() {
        List<GPMSreturnableitems> items = returnableitemsservice.getAllItems();
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
        }
        return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
    }
    
    // Delete an item by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItemById(@PathVariable int id) {
        Optional<GPMSreturnableitems> item = returnableitemsservice.getItemById(id);
        if (item.isPresent()) {
            returnableitemsservice.deleteItemById(id); // Delete the item from the service layer
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new ResponseMessage("Success", "Record deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ResponseMessage("Error", "Item not found"));
        }
    }
    
}
