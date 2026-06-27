package com.JIMS.integration.controller;
import com.JIMS.integration.entity.GPMS_itemmaster;
import com.JIMS.integration.interfaces.gpms_itemmasterservice;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin
@RestController
@RequestMapping("/api/items")
public class gpmsitemmastercontroller {

    @Autowired
    private gpms_itemmasterservice itemService;

    @PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody GPMS_itemmaster item) {
        try {
            GPMS_itemmaster savedItem = itemService.saveOrUpdateItem(item);
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

    // Get an item by its ID
    @GetMapping("/{id}")
    public ResponseEntity<GPMS_itemmaster> getItemById(@PathVariable int id) {
        Optional<GPMS_itemmaster> item = itemService.getItemById(id);
        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Get all items
    @GetMapping
    public ResponseEntity<List<GPMS_itemmaster>> getAllItems() {
        List<GPMS_itemmaster> items = itemService.getAllItems();
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if the list is empty
        }
        return new ResponseEntity<>(items, HttpStatus.OK); // Return 200 with the list of items
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateItemById(@PathVariable int id, @RequestBody GPMS_itemmaster item) {

        // 🔍 CHECK IF ITEM IS USED IN TRANSACTIONS
        if (isItemUsedInTransactions(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Error",
                            "This item is already used in transactions and cannot be modified."));
        }

        Optional<GPMS_itemmaster> existingItem = itemService.getItemById(id);

        if (existingItem.isPresent()) {
            GPMS_itemmaster updatedItem = existingItem.get();

            if (item.getItemNo() != null) updatedItem.setItemNo(item.getItemNo());
            if (item.getUm() != null) updatedItem.setUm(item.getUm());
            if (item.getItmDescription1() != null) updatedItem.setItmDescription1(item.getItmDescription1());
            if (item.getItmDescription2() != null) updatedItem.setItmDescription2(item.getItmDescription2());
            if (item.getItmRemark() != null) updatedItem.setItmRemark(item.getItmRemark());
            if (item.getCreated_by() != 0) updatedItem.setCreated_by(item.getCreated_by());
            if (item.getCreated_date() != null) updatedItem.setCreated_date(item.getCreated_date());
            if (item.getModified_by() != 0) updatedItem.setModified_by(item.getModified_by());
            if (item.getModified_date() != null) updatedItem.setModified_date(item.getModified_date());

            itemService.saveOrUpdateItem(updatedItem);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Success", "Record updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage("Error", "Item not found"));
        }
    }
    
    @Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkItemExists(@RequestParam("itemNo") String itemNo) {
        String sql = "SELECT COUNT(*) FROM GPMS_ITEMMASTER WHERE ItemNo = ?";
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, itemNo);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                response.put("exists", count > 0);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("exists", false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("exists", false);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

 // ===============================================================
 // CHECK IF ITEM IS USED IN GPMS_tabRetGatePassdetail
 // ===============================================================
 private boolean isItemUsedInTransactions(int itemId) {
     String sql = "SELECT COUNT(*) FROM GPMS_tabRetGatePassdetail WHERE item = ?";

     try (Connection connection = jimsDataSource.getConnection();
          PreparedStatement ps = connection.prepareStatement(sql)) {

         ps.setInt(1, itemId);
         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
             return rs.getInt(1) > 0;  // true = item already used
         }

     } catch (Exception e) {
         e.printStackTrace();
     }
     return false;
 }
 
 @GetMapping("/checkItemUsed/{itemId}")
 public ResponseEntity<Map<String, Object>> checkItemUsed(@PathVariable int itemId) {
     String sql = "SELECT COUNT(*) FROM GPMS_tabRetGatePassdetail WHERE item = ?";
     Map<String, Object> response = new HashMap<>();

     try (Connection connection = jimsDataSource.getConnection();
          PreparedStatement ps = connection.prepareStatement(sql)) {

         ps.setInt(1, itemId);
         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
             response.put("exists", rs.getInt(1) > 0);
             return new ResponseEntity<>(response, HttpStatus.OK);
         }

     } catch (SQLException e) {
         e.printStackTrace();
     }

     response.put("exists", false);
     return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
 }
 
 @GetMapping("/checkItemUsed/bulk")
 public ResponseEntity<Map<String, Boolean>> checkItemUsedBulk() {
     String sql = "SELECT DISTINCT item FROM GPMS_tabRetGatePassdetail";
     Map<String, Boolean> response = new HashMap<>();

     try (Connection connection = jimsDataSource.getConnection();
          PreparedStatement ps = connection.prepareStatement(sql);
          ResultSet rs = ps.executeQuery()) {

         while (rs.next()) {
             response.put(String.valueOf(rs.getInt("item")), true);
         }

         return new ResponseEntity<>(response, HttpStatus.OK);

     } catch (SQLException e) {
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
 }

}

