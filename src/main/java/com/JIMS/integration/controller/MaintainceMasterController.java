package com.JIMS.integration.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.entity.MaintenanceMachineDescription;
import com.JIMS.integration.entity.MaintenanceWorkerDesignation;
import com.JIMS.integration.entity.MaintenanceWorkerMapping;
import com.JIMS.integration.entity.MaintenancedesignationMapping;
import com.JIMS.integration.interfaces.MaintenanceWorkerMappingRequest;
import com.JIMS.integration.repository.MaintenanceMachineDescriptionRepository;
import com.JIMS.integration.repository.MaintenanceWorkerDesignationRepository;
import com.JIMS.integration.repository.MaintenanceWorkerMappingRepository;
import com.JIMS.integration.repository.maintenancedesignationMappingRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api")
public class MaintainceMasterController {

	@Autowired
	private MaintenanceMachineDescriptionRepository repository;
	
	
	@PostMapping("/machine-description/create")
	public ResponseEntity<?> createMachineDescription(
	        @RequestParam String machineDescription,
	        HttpSession session) {

	    Map<String, Object> response = new HashMap<>();

	    try {

	        if (machineDescription == null || machineDescription.trim().isEmpty()) {
	            response.put("status", false);
	            response.put("message", "Machine Description is required");
	            return ResponseEntity.badRequest().body(response);
	        }

	        if (repository.existsByMachineDescriptionIgnoreCase(machineDescription.trim())) {
	            response.put("status", false);
	            response.put("message", "Machine Category already exists");
	            return ResponseEntity.ok(response);
	        }

	        MaintenanceMachineDescription data =
	                new MaintenanceMachineDescription();

	        data.setMachineDescription(machineDescription.trim());
	        data.setStatus(true);
	        data.setCreatedDate(new Date());

	        repository.save(data);

	        response.put("status", true);
	        response.put("message", "Created Successfully");

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {

	        response.put("status", false);
	        response.put("message", e.getMessage());

	        return ResponseEntity.internalServerError().body(response);
	    }
	}
	
	
//	@PostMapping("/machine-description/update")
//	public ResponseEntity<?> updateMachineDescription(
//	        @RequestParam Integer machineDescId,
//	        @RequestParam String machineDescription) {
//
//	    Map<String, Object> response = new HashMap<>();
//
//	    try {
//
//	        Optional<MaintenanceMachineDescription> optional =
//	                repository.findById(machineDescId);
//
//	        if (!optional.isPresent()) {
//
//	            response.put("status", false);
//	            response.put("message", "Record not found");
//
//	            return ResponseEntity.ok(response);
//	        }
//
//	        MaintenanceMachineDescription data = optional.get();
//
//	        data.setMachineDescription(machineDescription.trim());
//	        data.setModifiedDate(new Date());
//
//	        repository.save(data);
//
//	        response.put("status", true);
//	        response.put("message", "Updated Successfully");
//
//	        return ResponseEntity.ok(response);
//
//	    } catch (Exception e) {
//
//	        response.put("status", false);
//	        response.put("message", e.getMessage());
//
//	        return ResponseEntity.internalServerError().body(response);
//	    }
//	}
	
	
	@PostMapping("/machine-description/update")
	public ResponseEntity<?> updateMachineDescription(
	        @RequestParam Integer machineDescId,
	        @RequestParam String machineDescription) {

	    Map<String, Object> response = new HashMap<>();

	    try {

	        Optional<MaintenanceMachineDescription> optional =
	                repository.findById(machineDescId);

	        if (!optional.isPresent()) {

	            response.put("status", false);
	            response.put("message", "Record not found");

	            return ResponseEntity.ok(response);
	        }

	        int duplicateCount =
	                repository.countDuplicateMachineDescription(
	                        machineDescription.trim(),
	                        machineDescId);

	        if (duplicateCount > 0) {

	            response.put("status", false);
	            response.put("message",
	                    "Machine Description already exists");
	            
	            

	            return ResponseEntity.ok(response);
	        }
	        
	        int transactionCount =
	                repository.countMachineTransactions(machineDescId);

	        if (transactionCount > 0) {

	            response.put("status", false);
	            response.put("message",
	                    "Transactions already exist for this Machine Description. Update is not allowed.");

	            return ResponseEntity.ok(response);
	        }


	        MaintenanceMachineDescription data = optional.get();

	        data.setMachineDescription(machineDescription.trim());
	        data.setModifiedDate(new Date());

	        repository.save(data);

	        response.put("status", true);
	        response.put("message", "Updated Successfully");

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {

	        response.put("status", false);
	        response.put("message", e.getMessage());

	        return ResponseEntity.internalServerError().body(response);
	    }
	}
	
	
	@GetMapping("/machine-description/getAll")
	public ResponseEntity<?> getAllMachineDescriptions() {

	    return ResponseEntity.ok(
	            repository.findAll()
	    );
	}
	
	
	@GetMapping("/machine-description/get/{id}")
	public ResponseEntity<?> getMachineDescription(
	        @PathVariable Integer id) {

	    return repository.findById(id)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/machine/items/{machineId}")
	public ResponseEntity<?> getMachineItems(
	        @PathVariable Integer machineId) {

	    return ResponseEntity.ok(
	            repository.getMachineItemsByMachineId(machineId));
	}
	
	
	@PostMapping("/department/create")
	public ResponseEntity<?> createDepartment(
	        @RequestParam String departmentName,@RequestParam String factory_id) {

	    Map<String,Object> response = new HashMap<>();

	    try {

	        if (departmentName == null ||
	                departmentName.trim().isEmpty()) {

	            response.put("status", false);
	            response.put("message",
	                    "MachineLocation Name is required");

	            return ResponseEntity.ok(response);
	        }

	        if (repository.checkDepartmentExists(
	                departmentName.trim(),factory_id) > 0) {

	            response.put("status", false);
	            response.put("message",
	                    "MachineLocation already exists");

	            return ResponseEntity.ok(response);
	        }

	        repository.createDepartment(
	                departmentName.trim(),
	                1,factory_id);

	        response.put("status", true);
	        response.put("message",
	                "Department Created Successfully");

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {

	        response.put("status", false);
	        response.put("message", e.getMessage());

	        return ResponseEntity.ok(response);
	    }
	}
	
	@GetMapping("/department/getAll")
	public ResponseEntity<?> getAllDepartments(
	        @RequestParam String factoryId) {

	    return ResponseEntity.ok(
	            repository.getDepartmentsByFactory(factoryId));
	}
	
	
	@GetMapping("/department/get/{id}")
	public ResponseEntity<?> getDepartmentById(
	        @PathVariable Integer id) {

	    return ResponseEntity.ok(
	    		repository.getDepartmentById(id)
	    );
	}
	
	@PostMapping("/department/update")
	public ResponseEntity<?> updateDepartment(
	        @RequestParam Integer deptId,
	        @RequestParam String departmentName) {

	    Map<String,Object> response = new HashMap<>();

	    try {

	        if (repository.checkDuplicateDepartmentForUpdate(
	                departmentName.trim(),
	                deptId) > 0) {

	            response.put("status", false);
	            response.put("message", "MachineLocation already exists");

	            return ResponseEntity.ok(response);
	        }

	        int transactionCount =
	                repository.countDepartmentTransactions(deptId);

	        if (transactionCount > 0) {

	            response.put("status", false);
	            response.put("message",
	                    "Transactions already exist for this MachineLocation. Update is not allowed.");

	            return ResponseEntity.ok(response);
	        }

	        repository.updateDepartment(
	                deptId,
	                departmentName.trim(),
	                1);

	        response.put("status", true);
	        response.put("message",
	                "Department Updated Successfully");

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {

	        response.put("status", false);
	        response.put("message", e.getMessage());

	        return ResponseEntity.ok(response);
	    }
	}
	
	
	@GetMapping("/machine/getById")
	public Map<String,Object> getMachineById(
	        @RequestParam Integer machineId) {

	    return repository.getMachineById(machineId);
	}
	@GetMapping("/machine/getAll")
	public List<Map<String, Object>> getAllMachines(
	        @RequestParam String factoryId) {

	    return repository.getAllMachines(factoryId);
	}
	@PostMapping("/machine/update")
	public ResponseEntity<?> updateMachine(
	        @RequestParam Integer machineId,
	        @RequestParam String machineCode,
	        @RequestParam String machineSubCode,
	        @RequestParam Integer machineDescriptionId,
	        @RequestParam Integer deptId,
	        @RequestParam String modelNo,
	        @RequestParam String serialNo,
	        @RequestParam(required = false) String manufacturerName,
	        @RequestParam(required = false) String poNumber,
	        @RequestParam(required = false) String purchaseDate,
	        @RequestParam(required = false) String warrantyStartDate,
	        @RequestParam(required = false) String warrantyEndDate,
	        @RequestParam(required = false) String manufactureDate,
	        @RequestParam Integer countryId,
	        @RequestParam List<Integer> itemIds,
	        @RequestParam Integer createdBy,
	        @RequestParam Integer factoryId,
	        @RequestParam String machDes) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        // Normalize empty strings to null for optional fields
	        purchaseDate       = (purchaseDate       == null || purchaseDate.trim().isEmpty())       ? null : purchaseDate;
	        warrantyStartDate  = (warrantyStartDate  == null || warrantyStartDate.trim().isEmpty())  ? null : warrantyStartDate;
	        warrantyEndDate    = (warrantyEndDate    == null || warrantyEndDate.trim().isEmpty())    ? null : warrantyEndDate;
	        manufactureDate    = (manufactureDate    == null || manufactureDate.trim().isEmpty())    ? null : manufactureDate;
	        poNumber           = (poNumber           == null || poNumber.trim().isEmpty())           ? null : poNumber;
	        manufacturerName   = (manufacturerName   == null || manufacturerName.trim().isEmpty())   ? null : manufacturerName;

	        // Duplicate checks (exclude current record)
	        if (repository.checkDuplicateMachineCode(machineCode, factoryId, machineId) > 0) {
	            response.put("status", false);
	            response.put("message", "Machine Code already exists.");
	            return ResponseEntity.ok(response);
	        }
	        if (repository.checkDuplicateMachineSubCode(machineSubCode, factoryId, machineId) > 0) {
	            response.put("status", false);
	            response.put("message", "Machine Sub Code already exists.");
	            return ResponseEntity.ok(response);
	        }
	        if (repository.checkDuplicateSerialNo(serialNo, factoryId, machineId) > 0) {
	            response.put("status", false);
	            response.put("message", "Serial No already exists.");
	            return ResponseEntity.ok(response);
	        }

	        // Update machine
	        repository.updateMachine(
	                machineId, machineCode, machineSubCode,
	                machineDescriptionId, deptId,
	                modelNo, serialNo, manufacturerName,
	                poNumber, purchaseDate,
	                warrantyStartDate, warrantyEndDate,
	                manufactureDate, countryId,
	                createdBy, machDes);

	        // Refresh items
	        repository.deactivateMachineItems(machineId);
	        for (Integer itemId : itemIds) {
	            int updated = repository.activateMachineItem(machineId, itemId);
	            if (updated == 0) {
	                repository.insertMachineItem(machineId, itemId, factoryId, createdBy);
	            }
	        }

	        response.put("status", true);
	        response.put("message", "Machine Updated Successfully");

	    } catch (Exception e) {
	        response.put("status", false);
	        response.put("message", e.getMessage());
	    }

	    return ResponseEntity.ok(response);
	}
	@PostMapping("/machine/create")
	public ResponseEntity<?> createMachine(
	        @RequestParam String  machineCode,
	        @RequestParam String  machineSubCode,
	        @RequestParam Integer machineDescriptionId,
	        @RequestParam Integer deptId,
	        @RequestParam String  modelNo,
	        @RequestParam String  serialNo,
	        @RequestParam(required = false) String  manufacturerName,
	        @RequestParam Integer factory_id,
	        @RequestParam Integer createdBy,
	        @RequestParam List<Integer> itemIds,
	        @RequestParam(required = false) String  poNumber,
	        @RequestParam(required = false) String  warrantyStartDate,
	        @RequestParam(required = false) String  warrantyEndDate,
	        @RequestParam(required = false) String  manufactureDate,
	        @RequestParam String  countryId,
	        @RequestParam(required = false) String  purchaseDate,
	        @RequestParam String machDes) {

	    Map<String, Object> response = new HashMap<>();

	    try {

	        // ── 1. Check machineCode ─────────────────────────────
	        String codeMessage = repository.checkDuplicateMachineCode(
	                machineCode, factory_id);

	        if (codeMessage != null) {
	            response.put("status",  false);
	            response.put("message", codeMessage);
	            return ResponseEntity.ok(response);
	        }

	        // ── 2. Check machineSubCode ──────────────────────────
	        String subCodeMessage = repository.checkDuplicateMachineSubCode(
	                machineSubCode, factory_id);

	        if (subCodeMessage != null) {
	            response.put("status",  false);
	            response.put("message", subCodeMessage);
	            return ResponseEntity.ok(response);
	        }

	        // ── 3. Check serialNo ────────────────────────────────
	        String serialMessage = repository.checkDuplicateSerialNo(
	                serialNo, factory_id);

	        if (serialMessage != null) {
	            response.put("status",  false);
	            response.put("message", serialMessage);
	            return ResponseEntity.ok(response);
	        }
	        
	        purchaseDate       = (purchaseDate       == null || purchaseDate.trim().isEmpty())       ? null : purchaseDate;
	        warrantyStartDate  = (warrantyStartDate  == null || warrantyStartDate.trim().isEmpty())  ? null : warrantyStartDate;
	        warrantyEndDate    = (warrantyEndDate    == null || warrantyEndDate.trim().isEmpty())    ? null : warrantyEndDate;
	        manufactureDate    = (manufactureDate    == null || manufactureDate.trim().isEmpty())    ? null : manufactureDate;
	        poNumber           = (poNumber           == null || poNumber.trim().isEmpty())           ? null : poNumber;
	        manufacturerName   = (manufacturerName   == null || manufacturerName.trim().isEmpty())   ? null : manufacturerName;

	        // ── 4. Create the machine ────────────────────────────
	        repository.createMachine(
	                machineCode,
	                machineSubCode,
	                machineDescriptionId,
	                deptId,
	                modelNo,
	                serialNo,
	                manufacturerName,
	                poNumber,
	                purchaseDate,
	                warrantyStartDate,
	                warrantyEndDate,
	                manufactureDate,
	                Integer.parseInt(countryId),
	                createdBy,
	                factory_id,
	                machDes);

	        // ── 5. Get the new machine ID ────────────────────────
	        Integer machineId =
	                repository.getLatestMachineId(machineCode, factory_id);

	        // ── 6. Insert items ──────────────────────────────────
	        for (Integer itemId : itemIds) {
	            repository.insertMachineItem(
	                    machineId,
	                    itemId,
	                    factory_id,
	                    createdBy);
	        }

	        response.put("status",  true);
	        response.put("message", "Machine Created Successfully");

	    } catch (Exception e) {
	        response.put("status",  false);
	        response.put("message", e.getMessage());
	    }

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/machine/getByDescription")
	public ResponseEntity<?> getMachinesByDescription(
	        @RequestParam Integer machineDescriptionId, @RequestParam String factoryId) {
 
	    try {
 
	        List<Map<String, Object>> machines =
	                repository.getMachinesByDescriptionId(
	                        machineDescriptionId,factoryId);
 
	        return ResponseEntity.ok(machines);
 
	    } catch (Exception e) {
 
	        return ResponseEntity.badRequest()
	                .body("Error: " + e.getMessage());
	    }
	}
	
	
	
	@Autowired
	private MaintenanceWorkerMappingRepository maintenanceWorkerMappingRepository;

	@PostMapping("/createWorkerMapping")
	public ResponseEntity<?> createWorkerMapping(
	        @RequestBody MaintenanceWorkerMappingRequest request,
	        HttpServletRequest servletRequest) {

	    try {

	    	int alreadyMapped =
	    	        maintenanceWorkerMappingRepository
	    	                .checkWorkerAlreadyMapped(
	    	                        request.getWorkerEmpCode(),
	    	                        request.getMachineCategoryId(),request.getFactoryId());

	    	if (alreadyMapped > 0) {
	    	    return ResponseEntity.badRequest()
	    	            .body("Worker is already mapped to this machine category.");
	    	}

	        MaintenanceWorkerMapping mapping =
	                new MaintenanceWorkerMapping();

	        mapping.setWorkerEmpCode(request.getWorkerEmpCode());
	        mapping.setWorkerMobileNo(request.getWorkerMobileNo());
	        mapping.setMachineCategoryId(request.getMachineCategoryId());
	        mapping.setCreatedBy(request.getCreatedBy());
	        mapping.setEmpcode(request.getEmpId());
	        mapping.setFactoryid(request.getFactoryId());
	        mapping.setEmail(request.getEmail());
	        mapping.setIsActive(true);

	        String username =
	                (String) servletRequest.getSession()
	                        .getAttribute("username");

	        //mapping.setCreatedBy(createdBy);
	        mapping.setCreatedDate(LocalDateTime.now());
	        mapping.setEmployeeDisplayName(
	                request.getEmployeeDisplayName());

	        maintenanceWorkerMappingRepository.save(mapping);
	        Long savedMappingId = mapping.getId();

	        if (request.getDesignationIds() != null) {
	            for (Integer designationId : request.getDesignationIds()) {

	                MaintenanceWorkerDesignation wd =
	                        new MaintenanceWorkerDesignation();

	                wd.setWorkerMappingId(savedMappingId);
	                wd.setDesignationId(designationId);
	                wd.setFactoryId(Integer.parseInt(
	                        String.valueOf(request.getFactoryId())));
	                wd.setCreatedBy(String.valueOf(request.getCreatedBy()));
	                wd.setCreatedDate(LocalDateTime.now());
	                wd.setStatus(true);

	                workerDesignationRepository.save(wd);
	            }
	        }

	        return ResponseEntity.ok("Worker Mapping Created Successfully");

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error : " + e.getMessage());
	    }
	}
	    
	    
	    @GetMapping("/getAllWorkerMappings")
	    public ResponseEntity<?> getAllWorkerMappings(@RequestParam String Factoryid) {

	        try {

	            List<Object[]> result =
	                    maintenanceWorkerMappingRepository
	                            .getAllWorkerMappings(Factoryid);

	            List<Map<String, Object>> response =
	                    new ArrayList<>();

	            for (Object[] row : result) {

	                Map<String, Object> map =
	                        new HashMap<>();

	                map.put("id", row[0]);
	                map.put("workerEmpCode", row[1]);
	                map.put("workerMobileNo", row[2]);
	                map.put("worker_email",  row[3]);
	                map.put("employee_name", row[4]);
	                map.put("machineDescription", row[5]);

	                response.add(map);
	            }

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            e.printStackTrace();

	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(e.getMessage());
	        }
	
	    }

	
	    
	    
	    
	    @GetMapping("/getWorkerMappingById")
	    public ResponseEntity<?> getWorkerMappingById(
	            @RequestParam Long id) {

	        try {

	            Optional<MaintenanceWorkerMapping> optional =
	                    maintenanceWorkerMappingRepository.findById(id);

	            if (!optional.isPresent()) {
	                return ResponseEntity.badRequest()
	                        .body("Worker Mapping Not Found");
	            }

	            MaintenanceWorkerMapping mapping =
	                    optional.get();

	            Map<String, Object> response =
	                    new HashMap<>();

	            response.put("id", mapping.getId());
	            response.put("empId",mapping.getEmpcode());
	            response.put("workerEmpCode",
	                    mapping.getWorkerEmpCode());
	            response.put("workerMobileNo",
	                    mapping.getWorkerMobileNo());
	            response.put("workerEmail",
	                    mapping.getEmail());
	            response.put("machineCategoryId",
	                    mapping.getMachineCategoryId());

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            e.printStackTrace();

	            return ResponseEntity.status(
	                    HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(e.getMessage());
	        }
	    }
	    
	    
	    @PostMapping("/updateWorkerMapping")
	    public ResponseEntity<?> updateWorkerMapping(
	            @RequestBody MaintenanceWorkerMappingRequest request) {

	        try {

	            Optional<MaintenanceWorkerMapping> optional =
	                    maintenanceWorkerMappingRepository
	                            .findById(request.getId());

	            if (!optional.isPresent()) {

	                return ResponseEntity.badRequest()
	                        .body("Worker Mapping Not Found");
	            }

	            int duplicateCount =
	                    maintenanceWorkerMappingRepository
	                            .checkDuplicateForUpdate(
	                                    request.getWorkerEmpCode(),
	                                    request.getMachineCategoryId(),
	                                    request.getId());

	            if (duplicateCount > 0) {

	                return ResponseEntity.badRequest()
	                        .body("Worker is already mapped to this machine category.");
	            }

	            MaintenanceWorkerMapping mapping =
	                    optional.get();

	            mapping.setEmpcode(request.getEmpId());

	            mapping.setWorkerEmpCode(
	                    request.getWorkerEmpCode());

	            mapping.setWorkerMobileNo(
	                    request.getWorkerMobileNo());
	            
	            mapping.setEmail(
	                    request.getEmail());

	            mapping.setMachineCategoryId(
	                    request.getMachineCategoryId());

	            mapping.setModifiedBy(
	                    request.getModifiedBy());

	            mapping.setModifiedDate(
	                    LocalDateTime.now());
	            mapping.setEmployeeDisplayName(
	                    request.getEmployeeDisplayName());

	            maintenanceWorkerMappingRepository
	                    .save(mapping);
	            
	            Long mappingId = mapping.getId();
	            workerDesignationRepository.deactivateByWorkerMappingId(mappingId);

	            if (request.getDesignationIds() != null) {
	                for (Integer designationId : request.getDesignationIds()) {

	                    int updated = workerDesignationRepository
	                            .activateWorkerDesignation(mappingId, designationId);

	                    if (updated == 0) {
	                        MaintenanceWorkerDesignation wd =
	                                new MaintenanceWorkerDesignation();

	                        wd.setWorkerMappingId(mappingId);
	                        wd.setDesignationId(designationId);
	                        wd.setModifiedBy(request.getModifiedBy());
	                        wd.setModifiedDate(LocalDateTime.now());
	                        wd.setFactoryId(Integer.valueOf(request.getFactoryId()));
	                        wd.setStatus(true);

	                        workerDesignationRepository.save(wd);
	                    }
	                }
	            }

	            return ResponseEntity.ok(
	                    "Worker Mapping Updated Successfully");

	        } catch (Exception e) {

	            e.printStackTrace();

	            return ResponseEntity.status(
	                    HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(e.getMessage());
	        }
	    }

		@Autowired
		private maintenancedesignationMappingRepository maintenancedesignationMappingRepository;
	    
	    @GetMapping("/getAlldesignationrMappings")
	    public ResponseEntity<?> getAlldesignationMappings() {

	        try {

	            List<Object[]> result =
	            		maintenancedesignationMappingRepository
	                            .getAlldesignationMappings();

	            List<Map<String, Object>> response =
	                    new ArrayList<>();

	            for (Object[] row : result) {

	                Map<String, Object> map =
	                        new HashMap<>();

	                map.put("DESIGNATON_ID", row[0]);
	                map.put("DESIGNATION_NAME", row[1]);
	        
	                response.add(map);
	            }

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            e.printStackTrace();

	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(e.getMessage());
	        }
	
	    }
	
	    @PostMapping("/designation/create")
	    public ResponseEntity<?> createDesignation(
	            @RequestParam String designationName,
	            @RequestParam String designationCode,
	            @RequestParam String createdBy) {

	        Map<String, Object> response = new HashMap<>();

	        try {

	            if (designationName == null || designationName.trim().isEmpty()) {
	                response.put("status", false);
	                response.put("message", "Designation Name is required");
	                return ResponseEntity.badRequest().body(response);
	            }

	            if (designationCode == null || designationCode.trim().isEmpty()) {
	                response.put("status", false);
	                response.put("message", "Designation Code is required");
	                return ResponseEntity.badRequest().body(response);
	            }

	            if (maintenancedesignationMappingRepository
	                    .existsByDesignationNameIgnoreCase(designationName.trim())) {

	                response.put("status", false);
	                response.put("message", "Designation Name already exists");
	                return ResponseEntity.ok(response);
	            }

	            if (maintenancedesignationMappingRepository
	                    .existsByDesignationCodeIgnoreCase(designationCode.trim())) {

	                response.put("status", false);
	                response.put("message", "Designation Code already exists");
	                return ResponseEntity.ok(response);
	            }

	            MaintenancedesignationMapping data =
	                    new MaintenancedesignationMapping();

	            data.setDesignationName(designationName.trim());
	            data.setDesignationCode(designationCode.trim().toUpperCase());
	            data.setCreatedBy(createdBy);
	            data.setCreatedDate(LocalDateTime.now());

	            maintenancedesignationMappingRepository.save(data);

	            response.put("status", true);
	            response.put("message", "Designation Created Successfully");

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            response.put("status", false);
	            response.put("message", e.getMessage());

	            return ResponseEntity.internalServerError().body(response);
	        }
	    }
	    
	    @GetMapping("/designation/get/{id}")
	    public ResponseEntity<?> getDesignation(
	            @PathVariable Integer id) {

	        return maintenancedesignationMappingRepository.findById(id)
	                .map(ResponseEntity::ok)
	                .orElse(ResponseEntity.notFound().build());
	    }
	    
	    @PostMapping("/designation/update")
	    public ResponseEntity<?> updateDesignation(
	            @RequestParam Integer designationId,
	            @RequestParam String designationName,
	            @RequestParam String designationCode,
	            @RequestParam String modifiedBy) {

	        Map<String, Object> response = new HashMap<>();

	        try {

	            Optional<MaintenancedesignationMapping> optional =
	                    maintenancedesignationMappingRepository.findById(designationId);

	            if (!optional.isPresent()) {

	                response.put("status", false);
	                response.put("message", "Record not found");

	                return ResponseEntity.ok(response);
	            }

	            designationName = designationName.trim();
	            designationCode = designationCode.trim();

	            // Check duplicate designation name excluding current record
	            if (maintenancedesignationMappingRepository
	                    .existsByDesignationNameIgnoreCaseAndDesignationIdNot(
	                            designationName, designationId)) {

	                response.put("status", false);
	                response.put("message", "Designation Name already exists");

	                return ResponseEntity.ok(response);
	            }

	            // Check duplicate designation code excluding current record
	            if (maintenancedesignationMappingRepository
	                    .existsByDesignationCodeIgnoreCaseAndDesignationIdNot(
	                            designationCode, designationId)) {

	                response.put("status", false);
	                response.put("message", "Designation Code already exists");

	                return ResponseEntity.ok(response);
	            }

	            MaintenancedesignationMapping data = optional.get();

	            data.setDesignationName(designationName);
	            data.setDesignationCode(designationCode.toUpperCase());
	            data.setModifiedBy(modifiedBy);
	            data.setModifiedDate(LocalDateTime.now());

	            maintenancedesignationMappingRepository.save(data);

	            response.put("status", true);
	            response.put("message", "Designation Updated Successfully");

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            response.put("status", false);
	            response.put("message", e.getMessage());

	            return ResponseEntity.internalServerError().body(response);
	        }
	    }
	    
	    
	    @Autowired
	    private MaintenanceWorkerDesignationRepository workerDesignationRepository;

	    // Add this new GET endpoint to fetch designations for a worker mapping
	    @GetMapping("/workerMapping/designations/{workerMappingId}")
	    public ResponseEntity<?> getWorkerDesignations(
	            @PathVariable Long workerMappingId) {
	        return ResponseEntity.ok(
	            workerDesignationRepository
	                .getDesignationsByWorkerMappingId(workerMappingId)
	        );
	    }
	    
}
