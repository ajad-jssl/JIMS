package com.JIMS.integration.interfaces;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.VTS_invoiceexitmodel;
import com.JIMS.integration.repository.VTS_invoiceexitinterface;

@Service
public class VTS_invoiceexitservice {
	 @Autowired
	    private VTS_invoiceexitinterface repository;

	    @Value("${upload.VTSPath}")
	    private String uploadPath;  // path from application.properties
	    // Add a new item or update an existing one
	    public VTS_invoiceexitmodel saveOrUpdateItem(VTS_invoiceexitmodel item) {
	        try {
	            // Handle Base64 image (if available)
	            if (item.getCapture() != null && item.getCapture().startsWith("data:image")) {
	                String base64Image = item.getCapture().split(",")[1];
	                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

	                // Ensure directory exists
	                File dir = new File(uploadPath);
	                if (!dir.exists()) dir.mkdirs();

	                // Save image
	                String fileName = "VTS" + System.currentTimeMillis() + ".png";
	                File outputFile = new File(dir, fileName);
	                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
	                    fos.write(imageBytes);
	                }

	                // Store relative path in DB
	                item.setCapture(outputFile.getAbsolutePath());
	            }
	        return repository.save(item); // This will handle both insert and update automatically.
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error saving invoice exit: " + e.getMessage());
	        }
	    }

	    // Find item by ID
	    public Optional<VTS_invoiceexitmodel> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public Page<VTS_invoiceexitmodel> getAllItems(String factoryId, Pageable pageable) {
	        return repository.getAllItems(factoryId, pageable);
	    }
	    public Page<VTS_invoiceexitmodel> searchItems(String factoryId, String search, Pageable pageable) {
	        return repository.searchItems(factoryId, search, pageable);
	    }
}

