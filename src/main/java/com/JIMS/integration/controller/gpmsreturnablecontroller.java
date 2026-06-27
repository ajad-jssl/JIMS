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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.GPMS_returnable;
import com.JIMS.integration.interfaces.gpmsreturnableservice;

@CrossOrigin
@RestController
@RequestMapping("/api/returnable")
public class gpmsreturnablecontroller {
@Autowired
private gpmsreturnableservice itemService;

@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody GPMS_returnable item) {
        try {
            GPMS_returnable savedItem = itemService.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
    }


@PutMapping("/{id}")
public ResponseEntity<Object> updateItemById(@PathVariable int id, @RequestBody GPMS_returnable item) {
	
	System.out.println("===== Request Body =====");
	System.out.println("gp_no           : " + item.getGp_no());
	System.out.println("GP_type         : " + item.getGP_type());
	System.out.println("requestedby     : " + item.getRequestedby());
	System.out.println("dept            : " + item.getDept());
	System.out.println("sent_type       : " + item.getsent_type());
	System.out.println("sentToClient    : " + item.getSentToClient());
	System.out.println("sentToemployee  : " + item.getsentToemployee());
	System.out.println("sentThrough     : " + item.getSentThrough());
	System.out.println("returnDate      : " + item.getReturnDate());
	System.out.println("note            : " + item.getNote());
	System.out.println("returnedOn      : " + item.getReturnedOn());
	System.out.println("receiver        : " + item.getReceiver());
	System.out.println("remarks         : " + item.getRemarks());
	System.out.println("encl            : " + item.getEncl());
	System.out.println("security        : " + item.getSecurity());
	System.out.println("securityout     : " + item.getSecurityout());
	System.out.println("securityoutdt   : " + item.getSecurityoutdt());
	System.out.println("createdby       : " + item.getCreatedby());
	System.out.println("createddate     : " + item.getCreateddate());
	System.out.println("project         : " + item.getProject());
	System.out.println("securityremark  : " + item.getSecurityremark());
	System.out.println("exitweight      : " + item.getExitweight());
	System.out.println("gstDCNo         : " + item.getGstDCNo());
	System.out.println("UpdatedBy       : " + item.getUpdatedBy());
	System.out.println("UpdatedDate     : " + item.getUpdatedDate());
	System.out.println("UpdateReason    : " + item.getUpdateReason());
	System.out.println("cancelstatus    : " + item.getCancelstatus());
	System.out.println("cancelreason    : " + item.getCancelreason());
	System.out.println("Factory_id      : " + item.getFactory_id());
	System.out.println("========================");
    Optional<GPMS_returnable> existingItem = itemService.getItemById(id);
    
    if (existingItem.isPresent()) {
        GPMS_returnable updatedItem = existingItem.get();
        
        // Check and update only the provided fields
        
        if (item.getGp_no() != null) {
            updatedItem.setGp_no(item.getGp_no());
        }
        if (item.getGP_type() != null) {
            updatedItem.setGP_type(item.getGP_type());
        }
        if (item.getRequestedby() != null) {
            updatedItem.setRequestedby(item.getRequestedby());
        }
        if (item.getDept() != null) {
            updatedItem.setDept(item.getDept());
        }
        if (item.getsent_type() != null) {
            updatedItem.setsent_type(item.getsent_type());
        }
        if (item.getSentToClient() != null) {
            updatedItem.setSentToClient(item.getSentToClient());
        }
        if (item.getsentToemployee() != null) {
            updatedItem.setsentToemployee(item.getsentToemployee());
        }
        if (item.getSentThrough() != null) {
            updatedItem.setSentThrough(item.getSentThrough());
        }
        if (item.getReturnDate() != null) {
            updatedItem.setReturnDate(item.getReturnDate());
        }
        if (item.getNote() != null) {
            updatedItem.setNote(item.getNote());
        }
        if (item.getReturnedOn() != null) {
            updatedItem.setReturnedOn(item.getReturnedOn());
        }
        if (item.getReceiver() != null) {
            updatedItem.setReceiver(item.getReceiver());
        }
        if (item.getRemarks() != null) {
            updatedItem.setRemarks(item.getRemarks());
        }
        if (item.getEncl() != null) {
            updatedItem.setEncl(item.getEncl());
        }
        if (item.getSecurity() != null) {
            updatedItem.setSecurity(item.getSecurity());
        }
        if (item.getSecurityout() != null) {
            updatedItem.setSecurityout(item.getSecurityout());
        }
        if (item.getSecurityoutdt() != null) {
            updatedItem.setSecurityoutdt(item.getSecurityoutdt());
        }
        if (item.getCreatedby() != null) {
            updatedItem.setCreatedby(item.getCreatedby());
        }
        if (item.getCreateddate() != null) {
            updatedItem.setCreateddate(item.getCreateddate());
        }
        if (item.getProject() != null) {
            updatedItem.setProject(item.getProject());
        }
        if (item.getSecurityremark() != null) {
            updatedItem.setSecurityremark(item.getSecurityremark());
        }
        if (item.getExitweight() != null) {
            updatedItem.setExitweight(item.getExitweight());
        }
        if (item.getGstDCNo() != null) {
            updatedItem.setGstDCNo(item.getGstDCNo());
        }
        if (item.getUpdatedBy() != null) {
            updatedItem.setUpdatedBy(item.getUpdatedBy());
        }
        if (item.getUpdatedDate() != null) {
            updatedItem.setUpdatedDate(item.getUpdatedDate());
        }
        if (item.getUpdateReason() != null) {
            updatedItem.setUpdateReason(item.getUpdateReason());
        }
        if (item.getCancelstatus() != null) {
            updatedItem.setCancelstatus(item.getCancelstatus());
        }
        if (item.getCancelreason() != null) {
            updatedItem.setCancelreason(item.getCancelreason());
        }

        System.out.println("updatedItem = " + updatedItem);
        
        // Save the updated item
        itemService.saveOrUpdateItem(updatedItem);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseMessage("Success", "Record updated successfully"));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ResponseMessage("Error", "Item not found"));
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

// Get an item by its ID
@GetMapping("/{id}")
public ResponseEntity<GPMS_returnable> getItemById(@PathVariable int id) {
    Optional<GPMS_returnable> item = itemService.getItemById(id);
    if (item.isPresent()) {
        return new ResponseEntity<>(item.get(), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

// Get all items
@GetMapping
public ResponseEntity<List<GPMS_returnable>> getAllItems() {
    List<GPMS_returnable> items = itemService.getAllItems();
    if (items.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
    }
    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
}

// Get returnable items based on GP_type = '1' and security = '1'
@GetMapping("/filter")
public ResponseEntity<List<GPMS_returnable>> getReturnablesByGpTypeAndSecurity(
        @RequestParam("factory_id") Integer factoryId) {
    List<GPMS_returnable> items = itemService.getReturnablesByGpTypeAndSecurityAndFactoryId(factoryId);
    if (items.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if no records found
    }
    return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the filtered list
}

}