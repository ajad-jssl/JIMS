package com.JIMS.integration.interfaces;


import com.JIMS.integration.entity.GPMS_clientmaster;
import com.JIMS.integration.repository.gpms_clientmasterinterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class gpmsclientmasterservice {

    @Autowired
    private gpms_clientmasterinterface repository;

    // List all clients
    public List<GPMS_clientmaster> getAllClients() {
        return repository.findAll();
    }

    // Add a new client with auto-generated client_number
    public ResponseEntity<Map<String, String>> addClient(GPMS_clientmaster client) {
        // Generate a new client number
        String newClientNumber = generateClientNumber();
        
        // Set the new client number to the client object
        client.setClient_number(newClientNumber);
        
        // Save the client to the database
        repository.save(client);
        
        // Create a response map with status and client number
        Map<String, String> response = new HashMap<>();
        response.put("status", "Success");
        response.put("client_number", newClientNumber);
        
        // Return the response as a JSON object
        return ResponseEntity.ok(response);
    }

    // Generate the next client_number
    private String generateClientNumber() {
        GPMS_clientmaster lastClient = repository.findTopByOrderByCidDesc();
        String lastClientNumber = lastClient != null ? lastClient.getClient_number() : "V000000"; // Default if no clients exist

        // Increment the last client number (assuming it follows the pattern 'v000001', 'v000002', ...)
        int lastClientNumberInt = Integer.parseInt(lastClientNumber.substring(1)); // Remove 'v' and parse number
        int newClientNumberInt = lastClientNumberInt + 1;
        
        // Format it back to the required pattern
        return "V" + String.format("%06d", newClientNumberInt);
    }

    // Update an existing client
    public String updateClient(int cid, GPMS_clientmaster clientDetails) {
        Optional<GPMS_clientmaster> existingClient = repository.findById(cid);
        if (existingClient.isPresent()) {
            GPMS_clientmaster client = existingClient.get();
            // You can add fields to be updated if required
            client.setClient_number(clientDetails.getClient_number());
            client.setName(clientDetails.getName());
            client.setContactname1(clientDetails.getContactname1());
            client.setContactname2(clientDetails.getContactname2());
            client.setContactnumber(clientDetails.getContactnumber());
            client.setContactemail(clientDetails.getContactemail());
            client.setAddress1(clientDetails.getAddress1());
            client.setCity(clientDetails.getCity());
            client.setDistrict(clientDetails.getDistrict());
            client.setState(clientDetails.getState());
            client.setPincode(clientDetails.getPincode());
            client.setRemarks(clientDetails.getRemarks());
            client.setGst(clientDetails.getGst());
            repository.save(client);
            return "Client updated successfully";
        } else {
            return "Not updated";
        }
    }

	public Optional<GPMS_clientmaster> getItemById(int id) {
		 return repository.findById(id);
	}

	public GPMS_clientmaster saveOrUpdateClient(GPMS_clientmaster updatedClient) {
		  return repository.save(updatedClient); // This will handle both insert and update automatically.
		
	}

}
