package com.JIMS.integration.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import com.JIMS.integration.controller.gpms_itemsrecievedcontroller.ResponseMessage;
import com.JIMS.integration.entity.GPMS_clientmaster;
import com.JIMS.integration.interfaces.gpmsclientmasterservice;
@CrossOrigin
@RestController
@RequestMapping("/api/clients")
public class gpmsclientmastercontroller {

    @Autowired
    private gpmsclientmasterservice service;

    // Get all clients
    @GetMapping
    public List<GPMS_clientmaster> getAllClients() {
        return service.getAllClients();
    }

    // Get an item by its ID
    @GetMapping("/{id}")
    public ResponseEntity<GPMS_clientmaster> getItemById(@PathVariable int id) {
        Optional<GPMS_clientmaster> item = service.getItemById(id);
        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Add a new client
    @PostMapping
    public ResponseEntity<ResponseEntity<Map<String, String>>> addClient(@RequestBody GPMS_clientmaster client) {
        ResponseEntity<Map<String, String>> message = service.addClient(client);
        return ResponseEntity.ok(message);
    }

    // Update an existing client
    @PostMapping("/{cid}")
    public ResponseEntity<String> updateClient(@PathVariable int cid, @RequestBody GPMS_clientmaster clientDetails) {
        String message = service.updateClient(cid, clientDetails);
        return ResponseEntity.ok(message);
    }
   
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateClientById(@PathVariable int id,
                                                   @RequestBody GPMS_clientmaster client) {

        // 1️⃣ Fetch the client by DB ID
        Optional<GPMS_clientmaster> existingClientOpt = service.getItemById(id);

        if (!existingClientOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage("Error", "Client not found"));
        }

        GPMS_clientmaster existingClient = existingClientOpt.get();

        // 2️⃣ Check if this client is already used in transactions (based on CID)
        if (isClientUsedInTransactions(existingClient.getCid())) {  // ✅ use cid here
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Error",
                            "This client is already used in transactions and cannot be modified."));
        }

        // 3️⃣ Update only the provided fields
        if (client.getCid() != 0) existingClient.setCid(client.getCid());
        if (client.getClient_number() != null) existingClient.setClient_number(client.getClient_number());
        if (client.getName() != null) existingClient.setName(client.getName());
        if (client.getContactname1() != null) existingClient.setContactname1(client.getContactname1());
        if (client.getContactname2() != null) existingClient.setContactname2(client.getContactname2());
        if (client.getContactnumber() != null) existingClient.setContactnumber(client.getContactnumber());
        if (client.getContactemail() != null) existingClient.setContactemail(client.getContactemail());
        if (client.getAddress1() != null) existingClient.setAddress1(client.getAddress1());
        if (client.getCity() != null) existingClient.setCity(client.getCity());
        if (client.getDistrict() != null) existingClient.setDistrict(client.getDistrict());
        if (client.getState() != null) existingClient.setState(client.getState());
        if (client.getPincode() != null) existingClient.setPincode(client.getPincode());
        if (client.getRemarks() != null) existingClient.setRemarks(client.getRemarks());
        if (client.getGst() != null) existingClient.setGst(client.getGst());
        if (client.getCreated_by() != 0) existingClient.setCreated_by(client.getCreated_by());
        if (client.getCreated_date() != null) existingClient.setCreated_date(client.getCreated_date());
        if (client.getModified_by() != 0) existingClient.setModified_by(client.getModified_by());
        if (client.getModified_date() != null) existingClient.setModified_date(client.getModified_date());

        // 4️⃣ Save updated client
        service.saveOrUpdateClient(existingClient);

        return ResponseEntity.ok(new ResponseMessage("Success", "Record updated successfully"));
    }

    // -------------------------
    // Check if the client is used in transactions based on CID
    @Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;
    private boolean isClientUsedInTransactions(int cid) {
        String sql = "SELECT COUNT(*) FROM GPMS_tabRetGatePass WHERE sentToClient = ?";

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cid);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // true = client already used
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Object>> checkClientNameExists(
            @RequestParam("name") String name) {

        String sql =
            "SELECT COUNT(*) FROM GPMS_CLIENTMASTER WHERE LOWER(LTRIM(RTRIM(name))) = LOWER(LTRIM(RTRIM(?)))";

        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                response.put("exists", exists);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("exists", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/checkclientUsed/{cid}")
    public ResponseEntity<Map<String, Object>> checkclientUsed(@PathVariable int cid) {
        String sql = "SELECT COUNT(*) FROM GPMS_tabRetGatePass WHERE sentToClient = ?";
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cid);
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
    
    @GetMapping("/checkclientUsed/bulk")
    public ResponseEntity<Map<String, Boolean>> checkClientUsedBulk() {
        String sql = "SELECT DISTINCT sentToClient FROM GPMS_tabRetGatePass";
        Map<String, Boolean> response = new HashMap<>();

        try (Connection connection = jimsDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                response.put(String.valueOf(rs.getInt("sentToClient")), true);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

