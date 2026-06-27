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

import com.JIMS.integration.entity.VMS_visitors;
import com.JIMS.integration.repository.VMS_visitorinterface;

@Service
public class VMS_VisitorService {
	 @Autowired
	    private VMS_visitorinterface repository;

	  @Value("${upload.VMSPath}")
	    private String uploadPath;  // path from application.properties
	    // Add a new item or update an existing one
	    public VMS_visitors saveOrUpdateItem(VMS_visitors item) {
	    	 try {
		            // Handle Base64 image (if available)
		            if (item.getCapture() != null && item.getCapture().startsWith("data:image")) {
		                String base64Image = item.getCapture().split(",")[1];
		                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		                // Ensure directory exists
		                File dir = new File(uploadPath);
		                if (!dir.exists()) dir.mkdirs();

		                // Save image
		                String fileName = "VMS" + System.currentTimeMillis() + ".png";
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
	    public Optional<VMS_visitors> getItemById(int itemId) {
	        return repository.findById(itemId);
	    }
	    
	    public List<VMS_visitors> getAllItems() {
	        return repository.findAll(); 
	    }
//
//	    public List<VMS_visitors> getVisitorsByFactoryId(Integer factory_id) {
//	        return repository.findByFactoryId(factory_id);
	    
	    public Page<VMS_visitors> getVisitorsByFactoryId(
	            Integer factoryId,
	            String search,
	            Pageable pageable) {

	        return repository.getVisitorsByFactoryId(
	                factoryId,
	                search,
	                pageable);
	    }
}
