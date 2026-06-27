package com.JIMS.integration.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.JIMS.integration.entity.Contract_gatepassmodel;
import com.JIMS.integration.interfaces.AdminGPListProjection;
import com.JIMS.integration.interfaces.ContractGPListProjection;
import com.JIMS.integration.interfaces.contract_gatepassservice;
import com.JIMS.integration.repository.contract_gatepassinterface;

import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;

@CrossOrigin
@RestController
@RequestMapping("/api/contractgatepass")
public class contract_gatepasscontroller {
	@Autowired
	private contract_gatepassservice contractgatepassservice;
	@PostMapping
    public ResponseEntity<Object> addOrUpdateItem(@RequestBody Contract_gatepassmodel item) {
        try {
        	Contract_gatepassmodel savedItem = contractgatepassservice.saveOrUpdateItem(item);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ResponseMessage("Success", "Record inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
        }
	}
	@PutMapping("/{cg_id}")
    public ResponseEntity<Object> updateEmployeeById(@PathVariable int cg_id, @RequestBody Contract_gatepassmodel employee) {
		
		

		
		  System.out.println("Employee Data: " + employee.toString());
		
		
        Optional<Contract_gatepassmodel> existingEmployeeOpt = contractgatepassservice.getItemById(cg_id);
        
        
        String adharno = employee.getAadhar_no();
    	        Integer  count  = contractgatepassservice.getDuplicateAdhar(adharno, cg_id);
    	        
    	        System.out.println("count:---------------------------------"+count);
    	        
    	        
    	        if(count>0) {
    	        	  return ResponseEntity.status(HttpStatus.CONFLICT)
    	  	                .body(new ResponseMessage("Success", "This Aadhaar number already exists."));
    	        }

        if (existingEmployeeOpt.isPresent()) {
        	Contract_gatepassmodel updatedEmployee = existingEmployeeOpt.get();
        	
        	
        	

        	// Update fields only if provided (non-null) in the request body
            if (employee.getPass_id() != null) {
                updatedEmployee.setPass_id(employee.getPass_id());
            }
            if (employee.getCompany_id() != null) {
                updatedEmployee.setCompany_id(employee.getCompany_id());
            }
            if (employee.getAadhar_no() != null) {
                updatedEmployee.setAadhar_no(employee.getAadhar_no());
            }
            if (employee.getFname() != null) {
                updatedEmployee.setFname(employee.getFname());
            }
            if (employee.getLname() != null) {
                updatedEmployee.setLname(employee.getLname());
            }
            if (employee.getGender() != null) {
                updatedEmployee.setGender(employee.getGender());
            }
            if (employee.getPass_issued_dt() != null) {
                updatedEmployee.setPass_issued_dt(employee.getPass_issued_dt());
            }
            if (employee.getDob() != null) {
                updatedEmployee.setDob(employee.getDob());
            }
            if (employee.getDoj() != null) {
                updatedEmployee.setDoj(employee.getDoj());
            }
            
            if (employee.getAge() != null) {
                updatedEmployee.setAge(employee.getAge());
            }
            if (employee.getValid_till() != null) {
                updatedEmployee.setValid_till(employee.getValid_till());
            }
            if (employee.getDid() != null) {
                updatedEmployee.setDid(employee.getDid());
            }
            if (employee.getDeg() != null) {
                updatedEmployee.setDeg(employee.getDeg());
            }
            if (employee.getNoj() != null) {
                updatedEmployee.setNoj(employee.getNoj());
            }
            if (employee.getMedical() != null) {
                updatedEmployee.setMedical(employee.getMedical());
            }
            if (employee.getWork_loc() != null) {
                updatedEmployee.setWork_loc(employee.getWork_loc());
            }
            if (employee.getBG_id() != null) {
                updatedEmployee.setBG_id(employee.getBG_id());
            }
            if (employee.getBus_id() != null) {
                updatedEmployee.setBus_id(employee.getBus_id());
            }
            if (employee.getVac_id() != null) {
                updatedEmployee.setVac_id(employee.getVac_id());
            }
            if (employee.getVac_date() != null) {
                updatedEmployee.setVac_date(employee.getVac_date());
            }
            if (employee.getVac2_date() != null) {
                updatedEmployee.setVac2_date(employee.getVac2_date());
            }
            if (employee.getAdmin_remarks() != null) {
                updatedEmployee.setAdmin_remarks(employee.getAdmin_remarks());
            }
            if (employee.getStatus() != null) {
                updatedEmployee.setStatus(employee.getStatus());
            }
            if (employee.getEmpId() != null) {
                updatedEmployee.setEmpId(employee.getEmpId());
            }
            if (employee.getFilepath() != null) {
                updatedEmployee.setFilepath(employee.getFilepath());
            }
            if (employee.getContactno() != null) {
                updatedEmployee.setContactno(employee.getContactno());
            }
            if (employee.getPan_no() != null) {
                updatedEmployee.setPan_no(employee.getPan_no());
            }
            if (employee.getUAN_no() != null) {
                updatedEmployee.setUAN_no(employee.getUAN_no());
            }
            if (employee.getPF_no() != null) {
                updatedEmployee.setPF_no(employee.getPF_no());
            }
            if (employee.getESIC_no() != null) {
                updatedEmployee.setESIC_no(employee.getESIC_no());
            }
            if (employee.getNominee_name() != null) {
                updatedEmployee.setNominee_name(employee.getNominee_name());
            }
            if (employee.getNRelationship() != null) {
                updatedEmployee.setNRelationship(employee.getNRelationship());
            }
            if (employee.getNDOB() != null) {
                updatedEmployee.setNDOB(employee.getNDOB());
            }
            if (employee.getNAge() != null) {
                updatedEmployee.setNAge(employee.getNAge());
            }
            if (employee.getNTotal_depandants() != null) {
                updatedEmployee.setNTotal_depandants(employee.getNTotal_depandants());
            }
            if (employee.getBank_name() != null) {
                updatedEmployee.setBank_name(employee.getBank_name());
            }
            if (employee.getBacc() != null) {
                updatedEmployee.setBacc(employee.getBacc());
            }
            if (employee.getBranch() != null) {
                updatedEmployee.setBranch(employee.getBranch());
            }
            if (employee.getIFSC() != null) {
                updatedEmployee.setIFSC(employee.getIFSC());
            }
            if (employee.getFather_name() != null) {
                updatedEmployee.setFather_name(employee.getFather_name());
            }
            if (employee.getMarital_status() != null) {
                updatedEmployee.setMarital_status(employee.getMarital_status());
            }
            if (employee.getSpouse_name() != null) {
                updatedEmployee.setSpouse_name(employee.getSpouse_name());
            }
            if (employee.getEmergency_contact_no() != null) {
                updatedEmployee.setEmergency_contact_no(employee.getEmergency_contact_no());
            }
            if (employee.getRelation() != null) {
                updatedEmployee.setRelation(employee.getRelation());
            }
            if (employee.getReligion() != null) {
                updatedEmployee.setReligion(employee.getReligion());
            }
            if (employee.getEducation() != null) {
                updatedEmployee.setEducation(employee.getEducation());
            }
            if (employee.getPermnt_add() != null) {
                updatedEmployee.setPermnt_add(employee.getPermnt_add());
            }
            if (employee.getDist() != null) {
                updatedEmployee.setDist(employee.getDist());
            }
            if (employee.getState() != null) {
                updatedEmployee.setState(employee.getState());
            }
            if (employee.getPincode() != null) {
                updatedEmployee.setPincode(employee.getPincode());
            }
            if (employee.getActive() != null) {
                updatedEmployee.setActive(employee.getActive());
            }
            if (employee.getPresent_add() != null) {
                updatedEmployee.setPresent_add(employee.getPresent_add());
            }
            if (employee.getPresent_dist() != null) {
                updatedEmployee.setPresent_dist(employee.getPresent_dist());
            }
            if (employee.getPresent_state() != null) {
                updatedEmployee.setPresent_state(employee.getPresent_state());
            }
            if (employee.getPresent_pincode() != null) {
                updatedEmployee.setPresent_pincode(employee.getPresent_pincode());
            }
            if (employee.getEmp_status() != null) {
                updatedEmployee.setEmp_status(employee.getEmp_status());
            }
            if (employee.getPrevious_exp() != null) {
                updatedEmployee.setPrevious_exp(employee.getPrevious_exp());
            }
            if (employee.getEmergency_contact_name() != null) {
                updatedEmployee.setEmergency_contact_name(employee.getEmergency_contact_name());
            }
            if (employee.getVac_status() != null) {
                updatedEmployee.setVac_status(employee.getVac_status());
            }
            if (employee.getRejoin_date() != null) {
                updatedEmployee.setRejoin_date(employee.getRejoin_date());
            }
            if (employee.getServicein_JSSL() != null) {
                updatedEmployee.setServicein_JSSL(employee.getServicein_JSSL());
            }
            if (employee.getCreated_by() != null) {
                updatedEmployee.setCreated_by(employee.getCreated_by());
            }
            if (employee.getCreated_date() != null) {
                updatedEmployee.setCreated_date(employee.getCreated_date());
            }
            if (employee.getModified_by() != null) {
                updatedEmployee.setModified_by(employee.getModified_by());
            }
            if (employee.getModified_date() != null) {
                updatedEmployee.setModified_date(employee.getModified_date());
            }
            
            if(employee.getStatus_description() !=null) {
            	updatedEmployee.setStatus_description(employee.getStatus_description());
            }
            
            if(employee.getAadhar_no() !=null) {
            	updatedEmployee.setAadhar_no(employee.getAadhar_no() );
            }
            
            System.out.println("the adhar path"+employee.getAadharFilePath());
            if(employee.getAadharFilePath() !=null) {
            	updatedEmployee.setAadharFilePath(employee.getAadharFilePath());
            	updatedEmployee.setFilepath(employee.getAadharFilePath());
            }
            if (employee.getJoinee_type() != null) {
                updatedEmployee.setJoinee_type(employee.getJoinee_type());

      
                if (employee.getJoinee_type() == 1) {
                    updatedEmployee.setRejoin_date(null);
                    updatedEmployee.setServicein_JSSL(null);
                }

                //  3. If REJOINEE → allow values
                if (employee.getJoinee_type() == 2) {
                    updatedEmployee.setRejoin_date(employee.getRejoin_date());
                    updatedEmployee.setServicein_JSSL(employee.getServicein_JSSL());
                }
            }
           contractgatepassservice.saveOrUpdateItem(updatedEmployee);
           return ResponseEntity.status(HttpStatus.OK)
	                .body(new ResponseMessage("Success", "contrat gatepass updated successfully"));

	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ResponseMessage("Error", "contrat gatepass not found"));
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
	
	@GetMapping
	public ResponseEntity<List<Contract_gatepassmodel>> getItemsByFactory(
	        @RequestParam("factory_id") Integer factoryId) {

	    List<Contract_gatepassmodel> items = contractgatepassservice.getItemsByFactory(factoryId);

	    if (items.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    return ResponseEntity.ok(items);
	}

	@Autowired
	private contract_gatepassinterface contract_repo;
	
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getListByFactory(
	        @RequestParam("factory_id") Integer factoryId,
	        @RequestParam(defaultValue = "") String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Pageable pageable = PageRequest.of(page, size, Sort.by("cg_id").descending());

	    String searchParam = (search == null || search.trim().isEmpty()) 
	                         ? null 
	                         : search.trim();

	    Page<ContractGPListProjection> pageData = 
	        contract_repo.getListByFactoryId(factoryId, searchParam, pageable);

	    // Total without filter — needed for DataTables recordsTotal
	    long totalWithoutFilter = contract_repo.countByFactoryId(factoryId);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data",            pageData.getContent());
	    response.put("currentPage",     pageData.getNumber());
	    response.put("totalItems",      pageData.getTotalElements());  // filtered count
	    response.put("totalRecords",    totalWithoutFilter);            // unfiltered count
	    response.put("totalPages",      pageData.getTotalPages());
	    response.put("pageSize",        size);

	    return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/adminlist")
	public ResponseEntity<Map<String, Object>> getAdminListByFactory(
	        @RequestParam("factory_id") Integer factoryId,
	        @RequestParam(defaultValue = "") String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    
	    Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

	    String searchParam = (search == null || search.trim().isEmpty())
	                         ? null
	                         : search.trim();

	    Page<AdminGPListProjection> pageData =
	        contract_repo.getAdminListByFactoryId(factoryId, searchParam, pageable);

	    long totalWithoutFilter = contract_repo.countByFactoryId(factoryId);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data",         pageData.getContent());
	    response.put("currentPage",  pageData.getNumber());
	    response.put("totalItems",   pageData.getTotalElements());
	    response.put("totalRecords", totalWithoutFilter);
	    response.put("totalPages",   pageData.getTotalPages());

	    return ResponseEntity.ok(response);
	}
	
	
	
	  @PutMapping("/extend-validity/{cgid}")
	    public ResponseEntity<?> extendValidity(
	            @PathVariable int cgid,
	            @RequestParam("valid_till")
	            @DateTimeFormat(pattern = "yyyy-MM-dd") Date validTill) {

	        int rows = contract_repo.updateValidTill(validTill, cgid);

	        if (rows > 0) {
	            return ResponseEntity.ok(
	                new ResponseMessage("Success", "Validity extended successfully")
	            );
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ResponseMessage("Error", "Record not found"));
	        }
	    }
	@GetMapping("/{id}")
	public ResponseEntity<Contract_gatepassmodel> getItemById(@PathVariable int id) {
	    Optional<Contract_gatepassmodel> item = contractgatepassservice.getItemById(id);
	    if (item.isPresent()) {
	        return new ResponseEntity<>(item.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	 @Autowired
	    @Qualifier("jimsDataSource")
	    private DataSource jimsDataSource;

	    // Get GP number based on GP_type and order by GP_no DESC
	    @GetMapping("/jims/autoCGNO")
	    public @ResponseBody int getvechicleData() {

	        // SQL query to get the top GP_no based on GP_type
	        String sql = "  SELECT TOP 1 cg_id as pass_id FROM [contract_gatepass_entry]  ORDER BY cg_id DESC ";
	        int count = 0;

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        
	            // Execute the query
	            ResultSet resultSet = preparedStatement.executeQuery();

	            // Check if there are results
	            if (resultSet.next()) {
	                count = resultSet.getInt(1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        // Return the result containing the GP_no
	        return count;
	        
	    }
	    
	    @GetMapping("/jims/aadharforadmin")
	    public @ResponseBody List<Map<String, String>> getContractGatepassData(
	            @RequestParam("factory_id") Integer factoryId) {

	      String sql = "SELECT cg_id, aadhar_no " +
	                "FROM [JIMS].[dbo].[CONTRACT_GATEPASS_ENTRY] " +
	                "WHERE admin_remarks IS NULL " +
	                "AND factory_id = ? " +
	                "AND active = 0";


	        List<Map<String, String>> resultList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            preparedStatement.setInt(1, factoryId);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                while (resultSet.next()) {
	                    Map<String, String> record = new HashMap<>();
	                    record.put("cg_id", resultSet.getString("cg_id"));
	                    record.put("aadhar_no", resultSet.getString("aadhar_no"));
	                    resultList.add(record);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return resultList;
	    }

	    
	    
	    @GetMapping("/check")
	    public ResponseEntity<Map<String, Object>> checkadharExists(@RequestParam("aadhar_no") String aadhar_no) {
	        Map<String, Object> response = new HashMap<>();
	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM CONTRACT_GATEPASS_ENTRY WHERE aadhar_no = ?")) {
	            ps.setString(1, aadhar_no);
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
	    
	    @Value("${upload.CONTRACTGPPath}")
	    private String uploadDir;
	    
	    @PostMapping("/upload")
	    public ResponseEntity<?> uploadFile(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam("passId") String passId) {

	        try {
	            // Validate file
	            if (file.isEmpty()) {
	                return ResponseEntity.badRequest().body("File is empty");
	            }

	            // Ensure upload directory exists
	            File directory = new File(uploadDir);
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            // Get original filename
	            String originalFilename = file.getOriginalFilename();
	            if (originalFilename == null || originalFilename.isEmpty()) {
	                return ResponseEntity.badRequest().body("Invalid filename");
	            }

	            // Extract base name and extension
	            String baseName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
	            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

	            // Generate initial file path
	            String finalFilename = originalFilename;
	            String filePath = uploadDir + finalFilename;

	            // Handle filename conflicts
	            File targetFile = new File(filePath);
	            int counter = 1;

	            while (targetFile.exists()) {
	                finalFilename = baseName + "_" + counter + extension;
	                filePath = uploadDir + finalFilename;
	                targetFile = new File(filePath);
	                counter++;
	            }

	            // Save file
	            file.transferTo(targetFile);

	            // Return file path
	            return ResponseEntity.ok().body(new UploadResponse(filePath));

	        } catch (IOException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
	        }
	    }

	    class UploadResponse {
	        private String filePath;

	        public UploadResponse(String filePath) { this.filePath = filePath; }

	        public String getFilePath() { return filePath; }
	        public void setFilePath(String filePath) { this.filePath = filePath; }
	    }

	    
	    @GetMapping("/jims/getContractGatepassDetails")
	    public @ResponseBody List<Map<String, String>> getContractGatepassDetails(
	            @RequestParam(value = "cg_id", required = false) String cgId,
	            @RequestParam(value = "factory_id", required = false) String factoryId,
	            @RequestParam(value = "filter", required = false) String filter) {

	        String baseSql ="SELECT dbo.CONTRACT_GATEPASS_ENTRY.cg_id, dbo.CONTRACT_GATEPASS_ENTRY.Pass_id, dbo.CONTRACT_GATEPASS_ENTRY.Aadhar_no, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Fname, dbo.CONTRACT_GATEPASS_ENTRY.Lname, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Gender, dbo.CONTRACT_GATEPASS_ENTRY.pass_issued_dt, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Dob, dbo.CONTRACT_GATEPASS_ENTRY.Doj, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Age, dbo.CONTRACT_GATEPASS_ENTRY.valid_till, " +
	                     "td.deptname, lm.designation, " +
	                     "dbo.CONTRACT_GATEPASS_NATURE_OF_JOB_MASTER.Nojdesc,dbo.CONTRACT_GATEPASS_Entry.emp_id ,dbo.CONTRACT_GATEPASS_Entry.company_id ," +
	                     "dbo.CONTRACT_GATEPASS_CONTRACTWORKLOCATION_MASTER.wldesc,dbo.CONTRACT_GATEPASS_Entry.medical,  " +
	                     "dbo.CONTRACT_GATEPASS_BLOODGROUP.blg, dbo.CONTRACT_GATEPASS_ENTRY.contactno, dbo.CONTRACT_GATEPASS_Entry.bus_id, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.pan_no, dbo.CONTRACT_GATEPASS_ENTRY.UAN_no, dbo.CONTRACT_GATEPASS_Entry.vac_id, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.PF_no, dbo.CONTRACT_GATEPASS_ENTRY.ESIC_no,dbo.CONTRACT_GATEPASS_Entry.vac_date, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Nominee_name, dbo.CONTRACT_GATEPASS_ENTRY.NRelationship, dbo.CONTRACT_GATEPASS_Entry.vac2_date, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.NDOB, dbo.CONTRACT_GATEPASS_ENTRY.NAge,dbo.CONTRACT_GATEPASS_Entry.Admin_remarks, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.NTotal_depandants, dbo.CONTRACT_GATEPASS_ENTRY.Bank_name, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.bacc, dbo.CONTRACT_GATEPASS_ENTRY.Branch, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.IFSC, dbo.CONTRACT_GATEPASS_ENTRY.Father_name, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Marital_status, dbo.CONTRACT_GATEPASS_ENTRY.emergency_contact_no, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Spouse_name, dbo.CONTRACT_GATEPASS_ENTRY.Relation, " +
	                     "dbo.CONTRACT_GATEPASS_RELIGION_MASTER.religiondesc AS religion, " +
	                     "dbo.CONTRACT_GATEPASS_EDUCATION_MASTER.edudesc AS education, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Permnt_add, dbo.VMS_Citymaster.City_name AS dist, " +
	                     "dbo.VMS_Statemaster.State_name AS state, dbo.CONTRACT_GATEPASS_ENTRY.pincode, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Present_add, VMS_Citymaster_1.City_name AS present_dist, " +
	                     "VMS_Statemaster_1.State_name AS present_state, dbo.CONTRACT_GATEPASS_ENTRY.present_pincode, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.previous_exp, dbo.CONTRACT_GATEPASS_ENTRY.emergency_contact_name, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.active, " + 
	                     "dbo.CONTRACT_GATEPASS_ENTRY.Vac_status, dbo.CONTRACT_GATEPASS_ENTRY.rejoin_date, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.servicein_JSSL, " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY.filepath, " +
	                    
	                     "dbo.CONTRACT_GATEPASS_ENTRY.joinee_type " +
	                     "FROM dbo.CONTRACT_GATEPASS_RELIGION_MASTER RIGHT OUTER JOIN "+
	                      "dbo.VMS_Citymaster RIGHT OUTER JOIN " +
	                     "dbo.CONTRACT_GATEPASS_ENTRY LEFT OUTER JOIN " +
	                     "dbo.CONTRACT_GATEPASS_CONTRACTWORKLOCATION_MASTER ON dbo.CONTRACT_GATEPASS_ENTRY.work_loc = dbo.CONTRACT_GATEPASS_CONTRACTWORKLOCATION_MASTER.wl_id LEFT OUTER JOIN " +
	                     "dbo.CONTRACT_GATEPASS_BLOODGROUP ON dbo.CONTRACT_GATEPASS_ENTRY.BG_id = dbo.CONTRACT_GATEPASS_BLOODGROUP.Bl_id LEFT OUTER JOIN " +
	                     "dbo.VMS_Statemaster AS VMS_Statemaster_1 ON dbo.CONTRACT_GATEPASS_ENTRY.Present_state = VMS_Statemaster_1.State_id LEFT OUTER JOIN " +
	                     "dbo.VMS_Citymaster AS VMS_Citymaster_1 ON dbo.CONTRACT_GATEPASS_ENTRY.Present_dist = VMS_Citymaster_1.City_id ON " +
	                     "dbo.VMS_Citymaster.City_id = dbo.CONTRACT_GATEPASS_ENTRY.dist LEFT OUTER JOIN " +
	                     "dbo.VMS_Statemaster ON dbo.CONTRACT_GATEPASS_ENTRY.state = dbo.VMS_Statemaster.State_id LEFT OUTER JOIN " +
	                     "dbo.CONTRACT_GATEPASS_EDUCATION_MASTER ON dbo.CONTRACT_GATEPASS_ENTRY.Education = dbo.CONTRACT_GATEPASS_EDUCATION_MASTER.edu_Id ON " +
	                     "dbo.CONTRACT_GATEPASS_RELIGION_MASTER.reli_Id = dbo.CONTRACT_GATEPASS_ENTRY.Religion LEFT OUTER JOIN " +
	                     "dbo.CONTRACT_GATEPASS_NATURE_OF_JOB_MASTER ON dbo.CONTRACT_GATEPASS_ENTRY.noj = dbo.CONTRACT_GATEPASS_NATURE_OF_JOB_MASTER.noj_id " +
	                     "LEFT OUTER JOIN JDMS.dbo.tabmstDept td ON td.did = CONTRACT_GATEPASS_ENTRY.did " +
						 "LEFT OUTER JOIN JDMS.dbo.LM_Designation lm on lm.desgid=CONTRACT_GATEPASS_ENTRY.deg ";
	                  
	        
	        String sql;
	        if (cgId != null && !cgId.isEmpty()) {
	            sql = baseSql + "WHERE (dbo.CONTRACT_GATEPASS_ENTRY.cg_id = ?) and  dbo.CONTRACT_GATEPASS_ENTRY.factory_id = ?";
	        } else if ("admin_remarks".equals(filter)) {
	            sql = baseSql + "WHERE (dbo.CONTRACT_GATEPASS_ENTRY.admin_remarks IS NOT NULL) and dbo.CONTRACT_GATEPASS_ENTRY.factory_id = ?";
	        } else {
	            sql = baseSql + "WHERE (dbo.CONTRACT_GATEPASS_ENTRY.admin_remarks IS NOT NULL) and dbo.CONTRACT_GATEPASS_ENTRY.factory_id = ?"; // Default to admin_remarks filter
	        }

	        List<Map<String, String>> contractList = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            // Set parameter for cg_id filter
	            if (cgId != null && !cgId.isEmpty()) {
	                preparedStatement.setString(1, cgId);
	                preparedStatement.setString(2, factoryId);
	              } else {
	            // Only 1 parameter:
	            // WHERE admin_remarks IS NOT NULL AND factory_id = ?
	            preparedStatement.setString(1, factoryId);
	        }
	            try (ResultSet rs = preparedStatement.executeQuery()) {
	                while (rs.next()) {
	                    Map<String, String> contractMap = new HashMap<>();
	                    contractMap.put("cg_id", rs.getString("cg_id"));
	                    contractMap.put("Pass_id", rs.getString("Pass_id"));
	                    contractMap.put("Aadhar_no", rs.getString("Aadhar_no"));
	                    contractMap.put("Fname", rs.getString("Fname"));
	                    contractMap.put("Lname", rs.getString("Lname"));
	                    contractMap.put("Gender", rs.getString("Gender"));
	                    contractMap.put("pass_issued_dt", rs.getString("pass_issued_dt"));
	                    contractMap.put("Dob", rs.getString("Dob"));
	                    contractMap.put("Doj", rs.getString("Doj"));
	                    contractMap.put("Age", rs.getString("Age"));
	                    contractMap.put("valid_till", rs.getString("valid_till"));
	                    contractMap.put("deptname", rs.getString("deptname"));
	                    contractMap.put("designation", rs.getString("designation"));
	                    contractMap.put("Nojdesc", rs.getString("Nojdesc"));
	                    contractMap.put("emp_id", rs.getString("emp_id"));
	                    contractMap.put("wldesc", rs.getString("wldesc"));
	                    contractMap.put("blg", rs.getString("blg"));
	                    contractMap.put("contactno", rs.getString("contactno"));
	                    contractMap.put("company_id", rs.getString("company_id"));
	                    contractMap.put("pan_no", rs.getString("pan_no"));
	                    contractMap.put("UAN_no", rs.getString("UAN_no"));
	                    contractMap.put("PF_no", rs.getString("PF_no"));
	                    contractMap.put("ESIC_no", rs.getString("ESIC_no"));
	                    contractMap.put("Nominee_name", rs.getString("Nominee_name"));
	                    contractMap.put("NRelationship", rs.getString("NRelationship"));
	                    contractMap.put("NDOB", rs.getString("NDOB"));
	                    contractMap.put("NAge", rs.getString("NAge"));
	                    contractMap.put("NTotal_depandants", rs.getString("NTotal_depandants"));
	                    contractMap.put("Bank_name", rs.getString("Bank_name"));
	                    contractMap.put("bacc", rs.getString("bacc"));
	                    contractMap.put("Branch", rs.getString("Branch"));
	                    contractMap.put("IFSC", rs.getString("IFSC"));
	                    contractMap.put("Father_name", rs.getString("Father_name"));
	                    contractMap.put("Marital_status", rs.getString("Marital_status"));
	                    contractMap.put("emergency_contact_no", rs.getString("emergency_contact_no"));
	                    contractMap.put("Spouse_name", rs.getString("Spouse_name"));
	                    contractMap.put("Relation", rs.getString("Relation"));
	                    contractMap.put("religion", rs.getString("religion"));
	                    contractMap.put("education", rs.getString("education"));
	                    contractMap.put("Permnt_add", rs.getString("Permnt_add"));
	                    contractMap.put("dist", rs.getString("dist"));
	                    contractMap.put("state", rs.getString("state"));
	                    contractMap.put("pincode", rs.getString("pincode"));
	                    contractMap.put("Present_add", rs.getString("Present_add"));
	                    contractMap.put("present_dist", rs.getString("present_dist"));
	                    contractMap.put("present_state", rs.getString("present_state"));
	                    contractMap.put("present_pincode", rs.getString("present_pincode"));
	                    contractMap.put("previous_exp", rs.getString("previous_exp"));
	                    contractMap.put("emergency_contact_name", rs.getString("emergency_contact_name"));
	                    contractMap.put("Vac_status", rs.getString("Vac_status"));
	                    contractMap.put("rejoin_date", rs.getString("rejoin_date"));
	                    contractMap.put("joinee_type", rs.getString("joinee_type"));
	                    contractMap.put("servicein_JSSL", rs.getString("servicein_JSSL"));
	                    contractMap.put("medical", rs.getString("medical"));
	                    contractMap.put("bus_id", rs.getString("bus_id"));
	                    contractMap.put("vac_id", rs.getString("vac_id"));
	                    contractMap.put("vac_date", rs.getString("vac_date"));
	                    contractMap.put("vac2_date", rs.getString("vac2_date"));
	                    contractMap.put("Admin_remarks", rs.getString("Admin_remarks"));
	                    contractMap.put("filepath", rs.getString("filepath"));
	                    contractMap.put("active", rs.getString("active"));
	                    contractList.add(contractMap);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Replace with proper logging (e.g., SLF4J or Log4j)
	        }

	        return contractList;
	    }
	    
	    @GetMapping("/jims/userslist")
	 		public @ResponseBody List<String> getuserslist() {
	 		    String sql = "SELECT Emp_id FROM CONTRACT_GATEPASS_ENTRY where Emp_id  is not null";
	 		    List<String> userslists = new ArrayList<>();

	 		    try (Connection connection = jimsDataSource.getConnection();
	 		         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	 		        ResultSet resultSet = preparedStatement.executeQuery();

	 		        while (resultSet.next()) {
	 		        	userslists.add(resultSet.getString("Emp_id"));
	 		        }

	 		    } catch (SQLException e) {
	 		        e.printStackTrace();
	 		    }

	 		    return userslists;
	 		}
	    
	    @GetMapping("/contractgp/filter")
	    public @ResponseBody List<Map<String, Object>> getContractGPFilter(
	            @RequestParam(value = "fromDate") String fromDate,
	            @RequestParam(value = "toDate") String toDate,
	            @RequestParam(value = "company", required = false) Integer company,
	            @RequestParam(value = "employeeId", required = false) String employeeId) {

	        List<Map<String, Object>> resultList = new ArrayList<>();

	        StringBuilder sql = new StringBuilder();
	        sql.append(" SELECT ce.Pass_id, ce.Emp_id, ce.cg_id, ce.photo_path,");
	        sql.append(" (ce.Fname + ' ' + ce.Lname) AS name, ");
	        sql.append(" CASE WHEN ce.Emp_status = 1 THEN 'Pending' ELSE '' END AS status, ");
	        sql.append(" CASE WHEN ce.Gender = 1 THEN 'Male' ELSE 'Female' END AS gender, ");
	        sql.append(" ce.Aadhar_no, ce.pass_issued_dt, ce.Dob, ce.Doj, ce.valid_till, tm.deptname, d.designation, ");
	        sql.append(" vm.Company_name, case when active IS null Or active = 0 then 'Active' else 'Inactive' end as active, ");
	        sql.append(" cb.blg, ngb.Nojdesc, ce.previous_exp, cm.Bus_desc, ce.filepath, ");
	        sql.append(" cw.wldesc, cv.Vaccine_desc, ce.Vac_date, ce.Vac2_date, f.factory_name ");
	        sql.append(" FROM CONTRACT_GATEPASS_ENTRY ce ");
	        sql.append(" LEFT JOIN VMS_Companymaster vm ON vm.Company_id = ce.company_id ");
	        sql.append(" LEFT JOIN CONTRACT_GATEPASS_BLOODGROUP cb ON cb.Bl_id = ce.BG_id ");
	        sql.append(" LEFT JOIN CONTRACT_GATEPASS_NATURE_OF_JOB_MASTER ngb ON ngb.noj_id = ce.noj ");
	        sql.append(" LEFT JOIN CONTRACT_GATEPASS_CONTRACTBUS_MASTER cm ON cm.bus_id = ce.Bus_id ");
	        sql.append(" LEFT JOIN CONTRACT_GATEPASS_CONTRACTWORKLOCATION_MASTER cw ON cw.wl_id = ce.work_loc ");
	        sql.append(" LEFT JOIN CONTRACT_GATEPASS_CONTRACTVACCINE_MASTER cv ON cv.vac_id = ce.Vac_status ");
	        sql.append(" LEFT JOIN JDMS.dbo.tabmstDept tm ON tm.did = ce.did ");
	        sql.append(" LEFT JOIN JDMS.dbo.LM_Designation d ON d.desgid = ce.deg ");
	        sql.append(" LEFT JOIN FACTORY_MASTER f ON f.id = ce.factory_id ");
	        sql.append(" WHERE ce.pass_issued_dt >= ? ");
	        sql.append(" AND ce.pass_issued_dt <= ? ");

	        List<Object> params = new ArrayList<>();
	        params.add(fromDate);
	        params.add(toDate);

	        if (company != null) {
	            sql.append(" AND ce.company_id = ? ");
	            params.add(company);
	        }

	        if (employeeId != null && !employeeId.isEmpty()) {
	            sql.append(" AND ce.Emp_id = ? ");
	            params.add(employeeId);
	        }

	        sql.append(" ORDER BY ce.pass_issued_dt DESC ");

	        try (Connection conn = jimsDataSource.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

	            for (int i = 0; i < params.size(); i++) {
	                ps.setObject(i + 1, params.get(i));
	            }

	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                Map<String, Object> row = new HashMap<>();

	                row.put("Pass_id", rs.getString("Pass_id"));
	                row.put("Emp_id", rs.getString("Emp_id"));
	                row.put("cg_id", rs.getString("cg_id"));
	                row.put("name", rs.getString("name"));
	                row.put("status", rs.getString("status"));
	                row.put("gender", rs.getString("gender"));
	                row.put("Aadhar_no", rs.getString("Aadhar_no"));
	                row.put("pass_issued_dt", rs.getString("pass_issued_dt"));
	                row.put("Dob", rs.getString("Dob"));
	                row.put("Doj", rs.getString("Doj"));
	                row.put("valid_till", rs.getString("valid_till"));
	                row.put("deptname", rs.getString("deptname"));
	                row.put("designation", rs.getString("designation"));
	                row.put("Company_name", rs.getString("Company_name"));
	                row.put("active", rs.getString("active"));
	                row.put("blg", rs.getString("blg"));
	                row.put("Nojdesc", rs.getString("Nojdesc"));
	                row.put("previous_exp", rs.getString("previous_exp"));
	                row.put("Bus_desc", rs.getString("Bus_desc"));
	                row.put("wldesc", rs.getString("wldesc"));
	                row.put("Vaccine_desc", rs.getString("Vaccine_desc"));
	                row.put("Vac_date", rs.getString("Vac_date"));
	                row.put("Vac2_date", rs.getString("Vac2_date"));
	                row.put("factory_name", rs.getString("factory_name"));
	              
	                // ✅ Extract only the filename from "filepath"
	                String fullPath = rs.getString("filepath");
	                if (fullPath != null && !fullPath.trim().isEmpty()) {
	                    String fileName = new File(fullPath).getName();
	                    row.put("filename", fileName);
	                } else {
	                    row.put("filename", "");
	                }

	                String photoFullPath = rs.getString("photo_path");
	                if (photoFullPath != null && !photoFullPath.trim().isEmpty()) {
	                    // new File() correctly handles both \ and / on any OS
	                    String photoFileName = new File(photoFullPath).getName();
	                    row.put("photo_filename", photoFileName);
	                } else {
	                    row.put("photo_filename", "");
	                }
	                row.put("photo_path", photoFullPath);
	                resultList.add(row);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return resultList;
	    }
	    
	    
		
		@Autowired
		@Qualifier("webDataSource")
		private DataSource webSmartDataSource;
	    
	    @GetMapping("/jims/deptlist")
	    public @ResponseBody List<Map<String, Object>> getDeptList() {

	        String sql = "SELECT \r\n"
	        		+ "    deptid,\r\n"
	        		+ "    deptcode + ' - ' + department AS DepartmentName\r\n"
	        		+ "FROM [WebSmart].[dbo].[Department]\r\n"
	        		+ "WHERE LocId = 3\r\n"
	        		+ "ORDER BY deptcode ASC;";

	        List<Map<String, Object>> deptlists = new ArrayList<>();

	        try (Connection connection = webSmartDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            ResultSet rs = preparedStatement.executeQuery();

	            while (rs.next()) {
	                Map<String, Object> row = new HashMap<>();
	                row.put("id", rs.getString("deptid"));
	                row.put("name", rs.getString("DepartmentName"));
	                deptlists.add(row);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return deptlists;
	    }

	    
	    @GetMapping("/jims/designationlist")
	    public @ResponseBody List<Map<String, Object>> getDesignationList() {

	        String sql = "SELECT desgid, designation FROM JDMS.dbo.LM_Designation";

	        List<Map<String, Object>> designationlists = new ArrayList<>();

	        try (Connection connection = jimsDataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            ResultSet rs = preparedStatement.executeQuery();

	            while (rs.next()) {
	                Map<String, Object> row = new HashMap<>();
	                row.put("id", rs.getInt("desgid"));
	                row.put("name", rs.getString("designation"));
	                designationlists.add(row);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return designationlists;
	    }
	    
	    @GetMapping("/downloadfile")
	    public void downloadFile(@RequestParam("filename") String fileName,
	                             HttpServletResponse response) {

	        if (fileName == null || fileName.trim().isEmpty()) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	        // ✅ Only use the filename part to prevent path traversal attacks
	        fileName = new File(fileName).getName();

	        String basePath =uploadDir;
	        File file = new File(basePath, fileName);

	        if (!file.exists() || !file.isFile()) {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            return;
	        }

	        response.setContentType("application/pdf");  // Set PDF content type
	        response.setHeader("Content-Disposition",
	                "inline; filename=\"" + file.getName() + "\"");  // Open in browser
	        response.setContentLengthLong(file.length());

	        try (FileInputStream fis = new FileInputStream(file);
	             OutputStream os = response.getOutputStream()) {

	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while ((bytesRead = fis.read(buffer)) != -1) {
	                os.write(buffer, 0, bytesRead);
	            }
	            os.flush();
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @GetMapping("/entry/employee/{empId}")
	    public ResponseEntity<?> checkEmpId(@PathVariable String empId) {
	        try {
	            // Using Optional to fetch first matching record
	            Optional<Contract_gatepassmodel> duplicateEmp = contractgatepassservice.checkDuplicateEmpId(empId);

	            if (duplicateEmp.isPresent()) {
	                return ResponseEntity
	                        .status(HttpStatus.CONFLICT)  // 409 Duplicate
	                        .body(duplicateEmp.get());    // Return actual employee
	            }

	            // No duplicate found
	            return ResponseEntity.ok("Available");
	            
	        } catch (Exception ex) {
	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Error: " + ex.getMessage());
	        }
	    }

	    @GetMapping("/viewImage")
	    public void viewImage(@RequestParam("fileName") String fileName,
	                          HttpServletResponse response) {
	        try {
	            if (fileName == null || fileName.trim().isEmpty()) {
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                return;
	            }

	            // ✅ Security: only filename, no path traversal
	            fileName = new File(fileName).getName();

	            File file = new File(uploadDir, fileName);

	            if (!file.exists() || !file.isFile()) {
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                return;
	            }

	            // ✅ Fix — Add Cross-Origin-Resource-Policy
	            // Allows cross-origin requests (port 8080 → port 8004)
	            response.setHeader("Cross-Origin-Resource-Policy", "cross-origin");

	            // ✅ Allow browser to display inline
	            response.setHeader("Content-Disposition",
	                "inline; filename=\"" + file.getName() + "\"");

	            // ✅ Cache for 1 hour
	            response.setHeader("Cache-Control", "max-age=3600");

	            // ✅ Set correct content type
	            String lowerName = fileName.toLowerCase();
	            if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
	                response.setContentType("image/jpeg");
	            } else if (lowerName.endsWith(".png")) {
	                response.setContentType("image/png");
	            } else if (lowerName.endsWith(".gif")) {
	                response.setContentType("image/gif");
	            } else {
	                response.setContentType("application/octet-stream");
	            }

	            response.setContentLengthLong(file.length());

	            try (FileInputStream fis = new FileInputStream(file);
	                 OutputStream os = response.getOutputStream()) {

	                byte[] buffer = new byte[4096];
	                int bytesRead;
	                while ((bytesRead = fis.read(buffer)) != -1) {
	                    os.write(buffer, 0, bytesRead);
	                }
	                os.flush();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }
	    

}
