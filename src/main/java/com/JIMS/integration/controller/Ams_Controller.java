package com.JIMS.integration.controller;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.JIMS.integration.entity.AssetFullHistory;
import com.JIMS.integration.entity.AssetSummaryDTO;
import com.JIMS.integration.entity.Asset_Entry;
import com.JIMS.integration.entity.Asset_Maker;
import com.JIMS.integration.entity.Asset_Model;
import com.JIMS.integration.entity.Asset_OwnedBy;
import com.JIMS.integration.entity.Asset_Status;
import com.JIMS.integration.entity.Asset_type;
import com.JIMS.integration.entity.ErAllocation;
import com.JIMS.integration.entity.ResponseMessage;
import com.JIMS.integration.interfaces.AssetFullHistoryService;
import com.JIMS.integration.interfaces.Asset_Allocation_Service;
import com.JIMS.integration.interfaces.Asset_Entry_Service;
import com.JIMS.integration.interfaces.Asset_Maker_Service;
import com.JIMS.integration.interfaces.Asset_Model_Service;
import com.JIMS.integration.interfaces.Asset_OwnedBy_Service;
import com.JIMS.integration.interfaces.Asset_Status_Service;
import com.JIMS.integration.interfaces.Asset_type_service;




/**
 * ✅ AMS Controller — Handles all REST endpoints related to Asset Type Management
 * Endpoints include Create, Read, Update, and Validation of Asset Types.
 */

@CrossOrigin
@RestController
@RequestMapping("api/ams") // Base URL for all APIs in this controller
public class Ams_Controller {

    // Injecting Service Layer Dependency
    @Autowired
    private Asset_type_service asset_type_service;


    @PostMapping
    public ResponseEntity<Object> insert_asset_type(@RequestBody Asset_type asset_type) {
        try {
            Asset_type save = asset_type_service.save(asset_type);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Success","Data Inserted Successfully"));
        } catch (Exception ex) {
            System.out.println(ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex);
        }
    }

    // --------------------------------------------------------------------
    // 🔹 2. READ — Get all Asset Types
    // --------------------------------------------------------------------
    @GetMapping("/getall")
    public ResponseEntity<Object> getasset_types() {
        try {
            List<Asset_type> get = asset_type_service.getall();
            return ResponseEntity.status(HttpStatus.OK).body(get);
        } catch (Exception ex) {
            System.out.println(ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error", "Failed to fetch Asset Types"));
        }
    }

    // --------------------------------------------------------------------
    // 🔹 3. READ (BY ID) — Get single Asset Type by ID
    // --------------------------------------------------------------------
    @GetMapping("/{type_id}")
    public ResponseEntity<Object> getAsset(@PathVariable Integer type_id) {
        try {
            Optional<Asset_type> getOne = asset_type_service.get(type_id);

            if (getOne.isPresent()) {
                // ✅ Asset Type found
                return ResponseEntity.ok(getOne.get());
            } else {
                // ⚠️ Asset Type not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Error", "Asset Type not found"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error", "Failed to fetch Asset Type"));
        }
    }

    // --------------------------------------------------------------------
    // 🔹 4. CHECK — Check if Asset Type already exists (used before inserting)
    // --------------------------------------------------------------------
//    @GetMapping("/check")
//    public ResponseEntity<Object> checkAssetType(@RequestParam String asset_type) {
//        try {
//            boolean exists = asset_type_service.existsByAssetType(asset_type);
//
//            // Return simple JSON response: {"exists": true/false}
//            return ResponseEntity.ok(java.util.Collections.singletonMap("exists", exists));
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseMessage("Error", "Failed to check Asset Type"));
//        }
//    

    
    
    
    
    
    
    
    
    @GetMapping("/check")
    public ResponseEntity<Object> checkAssetType(@RequestParam String asset_type) {
        try {
            List<Asset_type> existingList = asset_type_service.findByAssetType(asset_type);

            if (!existingList.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "type_id", existingList.get(0).getType_id()  // return the first match
                ));
            } else {
                return ResponseEntity.ok(Map.of("exists", false));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check Asset Type"));
        }
    }


    // --------------------------------------------------------------------
    // 🔹 5. UPDATE — Update Asset Type by ID
    // --------------------------------------------------------------------
    @PutMapping("/{type_id}")
    public ResponseEntity<Object> updateAssetTypeById(
            @PathVariable int type_id,
            @RequestBody Asset_type assetTypeRequest) {

        try {
            Optional<Asset_type> existingAssetOpt = asset_type_service.get(type_id);

            if (existingAssetOpt.isPresent()) {
                Asset_type existingAsset = existingAssetOpt.get();

                // ✅ Update only non-null fields from request
                if (assetTypeRequest.getAssetType() != null) {
                    existingAsset.setAssetType(assetTypeRequest.getAssetType());
                }
                if (assetTypeRequest.getCreatedBy() != null) {
                    existingAsset.setCreatedBy(assetTypeRequest.getCreatedBy());
                }
                if (assetTypeRequest.getCreated_date() != null) {
                    existingAsset.setCreated_date(assetTypeRequest.getCreated_date());
                }
                if (assetTypeRequest.getModifiedBy() != null) {
                    existingAsset.setModifiedBy(assetTypeRequest.getModifiedBy());
                }
                if (assetTypeRequest.getModifiedDate() != null) {
                    existingAsset.setModifiedDate(assetTypeRequest.getModifiedDate());
                }

                // ✅ Save updated entity back to database
                asset_type_service.save(existingAsset);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage("Success", "Asset Type updated successfully"));
            } else {
                // ⚠️ If no record found for the given ID
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("Error", "Asset Type not found"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Error", "Failed to update Asset Type"));
        }
    }

    // --------------------------------------------------------------------
    // 🔹 END OF Asset_Type Controller
    // --------------------------------------------------------------------
    
    
    
    
    
    
    
    
    
    
    
    @Autowired
  private   Asset_Model_Service asset_model_service;
    
    
    
    
    @PostMapping("/model")
    public ResponseEntity<Object> insertIntoModel(@RequestBody Asset_Model  asset_model){
    	
    	try {
    		Asset_Model saveIntoModel = asset_model_service.saveIntoModel(asset_model);
    		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Success","Data Inserted Successfully"));
    	}
    	catch (Exception ex) {
		
    		System.out.println(ex);
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error","Data Inseting Failed"));
		}
    	
    }
    
    
    
    @GetMapping("/model/getAll")
    public ResponseEntity<Object> getAllModel(){
    	try {
    		List<Asset_Model> allModel = asset_model_service.getAllModel();
    		return ResponseEntity.status(HttpStatus.OK).body(allModel);
    	}
    	catch(Exception  ex) {
    		System.out.println(ex);
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    		
    	}
    }
    
    @GetMapping("/model/get/{Mid}")
    public ResponseEntity<Object> getOneMode(@PathVariable Integer Mid){
    	try {
    		
    		Optional<Asset_Model> oneModel = asset_model_service.getOneModel(Mid);
    		
    		return ResponseEntity.status(HttpStatus.OK).body(oneModel);
    		
    		
    	}
    	catch(Exception ex) {
    		System.out.println(ex);
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    	
    }
    
    
    
    
    
    @PutMapping("/model/{Mid}")
    public ResponseEntity<Object> updateModel(@PathVariable Integer Mid, @RequestBody Asset_Model asset_model){
    	
    	try {
    	Optional<Asset_Model> oneModel = asset_model_service.getOneModel(Mid);
    	
    if(oneModel.isPresent()) {
    	Asset_Model existingOne = oneModel.get();
    	
    	if(asset_model.getAssetModel() !=null) {
    	  existingOne.setAssetModel(asset_model.getAssetModel());
    	}
    	
    	
    	if(asset_model.getCreatedBy()!=null) {
    		existingOne.setCreatedBy(asset_model.getCreatedBy());
    		
    	}
    	if(asset_model.getCreated_date() !=null) {
    		existingOne.setCreated_date(asset_model.getCreated_date());
    	}
    	
    	if(asset_model.getModifiedDate() !=null) {
    		existingOne.setModifiedDate(asset_model.getModifiedDate());
    	}
    	if(asset_model.getModifiedBy()!=null) {
    		existingOne.setModifiedBy(asset_model.getModifiedBy());
    	}
    	
    	
    	 asset_model_service.saveIntoModel(existingOne);
    	
    	
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Sucess","Asset Model Updated Successfully"));
    	
    	}
    	catch(Exception ex) {
    		
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    		
    	}
    	
    	
    	
    }
    
    @GetMapping("/model/check")
    public ResponseEntity<Object> checkAssetModel(@RequestParam String asset_model) {
        try {
            List<Asset_Model> existingList = asset_model_service.findByAssetModel(asset_model);

            if (!existingList.isEmpty()) {
                // ✅ Model already exists — return ID of first found
                return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "Mid", existingList.get(0).getModel_id()
                ));
            } else {
                // ✅ No match found — safe to insert
                return ResponseEntity.ok(Map.of("exists", false));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check Asset Model"));
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Asset Status Controller
    
   @Autowired 
 private  Asset_Status_Service asset_status_service;
   
   
   
//    method to handled the insertion in Er_Status Table
   
   @PostMapping("/status")
   public ResponseEntity<Object> insetintoassetstatus(@RequestBody Asset_Status asset_status){
	   
	   
	   try {
		   
		   Asset_Status saveIntoStatus = asset_status_service.insetIntoAssetStaus(asset_status);
		   return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Success","Insertion of Data is Succesfully"));
		   
		   
	   }catch(Exception ex) {
		   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	   }
	   
	   
   }
   
   
   
   
   @GetMapping("/status/get/{st_id}")
   public ResponseEntity<Object> getOneStaus(@PathVariable Integer st_id){
	    
	   try {
		   
		   Optional<Asset_Status> oneStatus = asset_status_service.getOneStatus(st_id);
		   
		   return ResponseEntity.status(HttpStatus.OK).body(oneStatus);
	   }catch(Exception ex) {
		   
		   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	   }
   }
   
   
    
    @GetMapping("/status/getAll")
    public ResponseEntity<Object> getAllStatus(){
    	try {
    		List<Asset_Status> allStatus = asset_status_service.getAllStatus();
    		
    		
    		return ResponseEntity.status(HttpStatus.OK).body(allStatus);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    		
    	}
    }
    
    
    
    @PutMapping("/status/{st_id}")
    public ResponseEntity<Object> updateStatus(@PathVariable Integer st_id, @RequestBody Asset_Status asset_status) {

        try {
            Optional<Asset_Status> oneStatus = asset_status_service.getOneStatus(st_id);

            if (oneStatus.isPresent()) {

                Asset_Status existingStatus = oneStatus.get();

                // Update only if new value is not null
                if (asset_status.getAssetStatus() != null) {
                    existingStatus.setAssetStatus(asset_status.getAssetStatus());
                }
                if (asset_status.getCreated_date() != null) {
                    existingStatus.setCreated_date(asset_status.getCreated_date());
                }
                if (asset_status.getCreatedBy() != null) {
                    existingStatus.setCreatedBy(asset_status.getCreatedBy());
                }
                if (asset_status.getModifiedBy() != null) {
                    existingStatus.setModifiedBy(asset_status.getModifiedBy());
                }
                if (asset_status.getModifiedDate() != null) {
                    existingStatus.setModifiedDate(asset_status.getModifiedDate());
                }

                asset_status_service.insetIntoAssetStaus(existingStatus);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Success", "Asset Status Updated Successfully"));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed", "Asset Status Update Failed"));
        }
    }

    
    
    @GetMapping("/status/check")
    public ResponseEntity<Object> checkAssetStatus(@RequestParam String asset_status) {
        try {
            List<Asset_Status> existingList = asset_status_service.findByAssetStatus(asset_status);

            if (!existingList.isEmpty()) {
                // ✅ Model already exists — return ID of first found
                return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "Mid", existingList.get(0).getStatus_id()
                ));
            } else {
                // ✅ No match found — safe to insert
                return ResponseEntity.ok(Map.of("exists", false));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check Asset Model"));
        }
    }    
    
  
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    @Autowired
    private Asset_OwnedBy_Service asset_ownedBy_service;

    
    // ➤ Insert into Er_assetob table
    @PostMapping("/owned")
    public ResponseEntity<Object> insertOwnedBy(@RequestBody Asset_OwnedBy assetOwnedBy) {

        try {
            Asset_OwnedBy saved = asset_ownedBy_service.insertOwnedBy(assetOwnedBy);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Success", "Insertion Successful"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    
    // ➤ Get one Record by ID
    @GetMapping("/owned/get/{ob_id}")
    public ResponseEntity<Object> getOneOwnedBy(@PathVariable Integer ob_id) {

        try {
            Optional<Asset_OwnedBy> oneData = asset_ownedBy_service.getOneOwnedBy(ob_id);

            return ResponseEntity.status(HttpStatus.OK).body(oneData);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }


    // ➤ Get all records
    @GetMapping("/owned/getAll")
    public ResponseEntity<Object> getAllOwnedBy() {
        try {
            List<Asset_OwnedBy> allData = asset_ownedBy_service.getAllOwnedBy();

            return ResponseEntity.status(HttpStatus.OK).body(allData);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }


    // ➤ Update Asset Owned By (only non-null fields)
    @PutMapping("/owned/{ob_id}")
    public ResponseEntity<Object> updateOwnedBy(@PathVariable Integer ob_id,
                                                @RequestBody Asset_OwnedBy assetOwnedBy) {

        try {
            Optional<Asset_OwnedBy> existingOpt = asset_ownedBy_service.getOneOwnedBy(ob_id);

            if (existingOpt.isPresent()) {
                Asset_OwnedBy existing = existingOpt.get();

                // Update non-null values
                if (assetOwnedBy.getOwnedByDescription() != null) {
                    existing.setOwnedByDescription(assetOwnedBy.getOwnedByDescription());
                }
                if (assetOwnedBy.getCreated_date() != null) {
                    existing.setCreated_date(assetOwnedBy.getCreated_date());
                }
                if (assetOwnedBy.getCreatedBy() != null) {
                    existing.setCreatedBy(assetOwnedBy.getCreatedBy());
                }
                if (assetOwnedBy.getModifiedBy() != null) {
                    existing.setModifiedBy(assetOwnedBy.getModifiedBy());
                }
                if (assetOwnedBy.getModifiedDate() != null) {
                    existing.setModifiedDate(assetOwnedBy.getModifiedDate());
                }

                asset_ownedBy_service.insertOwnedBy(existing);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Success", "Asset OwnedBy Updated Successfully"));

        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed", "Update Failed"));
        }
    }


    // ➤ Check duplicate Description
    @GetMapping("/owned/check")
    public ResponseEntity<Object> checkOwnedBy(@RequestParam String ob_description) {
        try {
            List<Asset_OwnedBy> existingList =
                    asset_ownedBy_service.findByOwnedByDescription(ob_description);

            if (!existingList.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "exists", true,
                        "ob_id", existingList.get(0).getAsserob_id()
                ));
            } else {
                return ResponseEntity.ok(Map.of("exists", false));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check OwnedBy"));
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Autowired
    private Asset_Maker_Service assetMakerService;

    // ➤ Insert into Er_make table
    @PostMapping("/maker")
    public ResponseEntity<Object> insertMaker(@RequestBody Asset_Maker assetMaker) {
        try {
            Asset_Maker saved = assetMakerService.insertOrUpdateMaker(assetMaker);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseMessage("Success", "Insertion Successful"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    // ➤ Get one Record by ID
    @GetMapping("/maker/get/{make_id}")
    public ResponseEntity<Object> getOneMaker(@PathVariable Integer make_id) {
        try {
            Optional<Asset_Maker> oneData = assetMakerService.getOneMaker(make_id);
            return ResponseEntity.status(HttpStatus.OK).body(oneData);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    // ➤ Get all records
    @GetMapping("/maker/getAll")
    public ResponseEntity<Object> getAllMakers() {
        try {
            List<Asset_Maker> allData = assetMakerService.getAllMakers();
            return ResponseEntity.status(HttpStatus.OK).body(allData);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    // ➤ Update Maker (only non-null fields)
    @PutMapping("/maker/{make_id}")
    public ResponseEntity<Object> updateMaker(@PathVariable Integer make_id,
                                              @RequestBody Asset_Maker assetMaker) {
        try {
            Optional<Asset_Maker> existingOpt = assetMakerService.getOneMaker(make_id);

            if (existingOpt.isPresent()) {
                Asset_Maker existing = existingOpt.get();

                // Update non-null values
                if (assetMaker.getMakeDescription() != null) {
                    existing.setMakeDescription(assetMaker.getMakeDescription());
                }
                if (assetMaker.getCreatedBy() != null) {
                    existing.setCreatedBy(assetMaker.getCreatedBy());
                }
                if (assetMaker.getCreatedDate() != null) {
                    existing.setCreatedDate(assetMaker.getCreatedDate());
                }
                if (assetMaker.getModifiedBy() != null) {
                    existing.setModifiedBy(assetMaker.getModifiedBy());
                }
                if (assetMaker.getModifiedDate() != null) {
                    existing.setModifiedDate(assetMaker.getModifiedDate());
                }

                assetMakerService.insertOrUpdateMaker(existing);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Success", "Maker Updated Successfully"));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed", "Update Failed"));
        }
    }

    // ➤ Check duplicate Make Description
    @GetMapping("/maker/check")
    public ResponseEntity<Object> checkMaker(@RequestParam String make_description) {
        try {
            List<Asset_Maker> existingList =
                    assetMakerService.findByMakeDescription(make_description);

            if (!existingList.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "exists", true,
                        "make_id", existingList.get(0).getMakeId()
                ));
            } else {
                return ResponseEntity.ok(Map.of("exists", false));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check Maker"));
        }
    }
    
    
    
    
    
    
//     Asset Entry Controller
    @Autowired
 private  Asset_Entry_Service asset_entry_service;
    
    
    @PostMapping("/entry")
    public ResponseEntity<Object> insertintoassetentry(@RequestBody Asset_Entry asset_entry){
    	
    	
    	
    	try {
    		Asset_Entry insertIntoasserentry = asset_entry_service.insertIntoasserentry(asset_entry);
    		return ResponseEntity.status(HttpStatus.CREATED).body(insertIntoasserentry);
    		
    		
    		
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    @GetMapping("/entry/get/{ent_id}")
    public ResponseEntity<Object> getAssetEntry(@PathVariable Integer ent_id){
    	
    	try {
    		Optional<Asset_Entry> assetEntry = asset_entry_service.getAssetEntry(ent_id);
    		return ResponseEntity.status(HttpStatus.OK).body(assetEntry);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    @GetMapping("/entry/getAll")
    public ResponseEntity<Object> getAllAssetEntry(){
    	try {
    	List<Asset_Entry> allAssetEntry = asset_entry_service.getAllAssetEntry();
    	return ResponseEntity.status(HttpStatus.OK).body(allAssetEntry);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    @PutMapping("/entry/update/{ent_id}")
    public ResponseEntity<Object> updateAssetEntry(@PathVariable Integer ent_id,
                                                   @RequestBody Asset_Entry new_asset_entry) {

        try {
            Optional<Asset_Entry> optionalEntry = asset_entry_service.getAssetEntry(ent_id);

            if (optionalEntry.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Asset Entry not found");
            }

            Asset_Entry existing = optionalEntry.get();

            // 🔥 Update only non-null fields
            if (new_asset_entry.getTypeId() != null)
                existing.setTypeId(new_asset_entry.getTypeId());

            if (new_asset_entry.getContractId() != null)
                existing.setContractId(new_asset_entry.getContractId());

            if (new_asset_entry.getMakeId() != null)
                existing.setMakeId(new_asset_entry.getMakeId());

            if (new_asset_entry.getAssetSrNo() != null)
                existing.setAssetSrNo(new_asset_entry.getAssetSrNo());

            if (new_asset_entry.getPurchaseDate() != null)
                existing.setPurchaseDate(new_asset_entry.getPurchaseDate());

            if (new_asset_entry.getPurchaseOrder() != null)
                existing.setPurchaseOrder(new_asset_entry.getPurchaseOrder());

            if (new_asset_entry.getCost() != null)
                existing.setCost(new_asset_entry.getCost());

            if (new_asset_entry.getVendorId() != null)
                existing.setVendorId(new_asset_entry.getVendorId());

            if (new_asset_entry.getWstartDate() != null)
                existing.setWstartDate(new_asset_entry.getWstartDate());

            if (new_asset_entry.getWendDate() != null)
                existing.setWendDate(new_asset_entry.getWendDate());

            if (new_asset_entry.getStatusId() != null)
                existing.setStatusId(new_asset_entry.getStatusId());

            if (new_asset_entry.getQty() != null)
                existing.setQty(new_asset_entry.getQty());

            if (new_asset_entry.getAssertorId() != null)
                existing.setAssertorId(new_asset_entry.getAssertorId());

            if (new_asset_entry.getRemarks() != null)
                existing.setRemarks(new_asset_entry.getRemarks());

            if (new_asset_entry.getAssetNo() != null)
                existing.setAssetNo(new_asset_entry.getAssetNo());

            if (new_asset_entry.getModelId() != null)
                existing.setModelId(new_asset_entry.getModelId());

            if (new_asset_entry.getCreatedBy() != null)
                existing.setCreatedBy(new_asset_entry.getCreatedBy());

            if (new_asset_entry.getCreatedDate() != null)
                existing.setCreatedDate(new_asset_entry.getCreatedDate());

            if (new_asset_entry.getModifiedBy() != null)
                existing.setModifiedBy(new_asset_entry.getModifiedBy());

            if (new_asset_entry.getStatus() != null)
                existing.setStatus(new_asset_entry.getStatus());

            if (new_asset_entry.getAssetAmAt() != null)
                existing.setAssetAmAt(new_asset_entry.getAssetAmAt());

            // 🔥 Auto-update Modified Date
            existing.setModifiedDate(LocalDateTime.now());

            // Save
            asset_entry_service.updateAssetEntry(existing);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Success","Asset Entry Successfully updated"));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    
    
    
    @GetMapping("/entry/asset_type/{asset_type_id}")
    public ResponseEntity<Object> checkAssetType(@PathVariable Integer asset_type_id){
    	try {
    		
    	Boolean assetType = asset_entry_service.getAssetType(asset_type_id);
    	return ResponseEntity.status(HttpStatus.OK).body(assetType);
    		
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    	
    }
   
    
    
    @GetMapping("/entry/asset_model/{asset_model_id}")
    public ResponseEntity<Object> checkAssetModel(@PathVariable Integer asset_model_id){
    	
    	
    	
    	try {
    		Boolean assetMode = asset_entry_service.getAssetMode(asset_model_id);
    		return ResponseEntity.status(HttpStatus.OK).body(assetMode);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    @GetMapping("/entry/asset_status/{asset_status_id}")
    public ResponseEntity<Object> checkAssetStatus(@PathVariable Integer asset_status_id){
    	try {
    		Boolean assetStatus = asset_entry_service.getAssetStatus(asset_status_id);
    		return ResponseEntity.status(HttpStatus.OK).body(assetStatus);
    	}catch(Exception ex) {
    		
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    @GetMapping("/entry/asset_ownedBy/{owner_id}")
    public ResponseEntity<Object> checkAssetOwnerBy(@PathVariable Integer owner_id){
    	try {
    		Boolean assetOwnedBy = asset_entry_service.getAssetOnwed(owner_id);
    		return ResponseEntity.status(HttpStatus.OK).body(assetOwnedBy);
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    @GetMapping("/entry/asset_maker/{make_id}")
    public ResponseEntity<Object> checkAssetMaker(@PathVariable Integer make_id){
    	try {
    		Boolean assetMaker = asset_entry_service.getAssetMaker(make_id);
    		return ResponseEntity.status(HttpStatus.OK).body(assetMaker);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    
    
//    @GetMapping("/entry/asset_srno/{asset_sr}")
//    public ResponseEntity<Object> checkAssetSrNo(@PathVariable String asset_sr){
//    	
//    	try {
//    		Boolean duplicationAsset = asset_entry_service.duplicationAsset(asset_sr);
//    		return ResponseEntity.status(HttpStatus.OK).body(duplicationAsset);
//    		
//    	}catch(Exception ex) {
//    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
//    	}
//    }
    
    
    
    
    
    @GetMapping("/entry/asset_srno/{asset_sr}")
    public ResponseEntity<?> checkAssetSrNo(@PathVariable String asset_sr) {
        try {
            Optional<Asset_Entry> duplicateAsset = asset_entry_service.duplcationAssetSr(asset_sr);

            if (duplicateAsset.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)  // 409 Duplicate
                        .body(duplicateAsset.get());  // Return actual asset
            }

            // No duplicate found
            return ResponseEntity.ok("Available");
            
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + ex.getMessage());
        }
    }

    
    
    @GetMapping("/entry/asset/available")
    public ResponseEntity<?> getAvailableAssets() {
        try {
            return ResponseEntity.ok(asset_entry_service.getAvailableAssets());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch available assets");
        }
    }
    
    
    @GetMapping("/entry/assets")
    public List<AssetSummaryDTO> getSummary() {
        return asset_entry_service.getAssetSummary();
    }
    
    
    
    
    
    
    
    // Asset Entry History
    
//    @Autowired
//    private Asset_Entry_History_Service asset_entry_history_service;
//    
//    
//    @GetMapping("/entry/history/getAll")
//    public ResponseEntity<Object> getallassethisory(){
//    	
//    	try {
//    	 List<ErAssetEnh> allAssetEntry = asset_entry_history_service.getAllAssetEntryHistory();
//    	 return ResponseEntity.status(HttpStatus.OK).body(allAssetEntry);
//    	}
//    	catch(Exception ex) {
//    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
//    	}
//    }
    
    
    
//    @GetMapping("/entry/history/{fromDate}/{toDate}")
//    public ResponseEntity<List<ErAssetEnh>> getAllAssetHistoryBetween(
//            @PathVariable String fromDate,
//            @PathVariable String toDate) {
//
//        List<ErAssetEnh> assetHistoryFromTo =asset_entry_history_service.getAssetHistoryFromTo(fromDate, toDate);
//        return ResponseEntity.ok(assetHistoryFromTo);
//    }
//    
    
    

    
    
    
    
    // Asset Allocation 
    
    
    @Autowired
    private Asset_Allocation_Service asset_allocation_service;
    
    
    @PostMapping("/allocate")
    public ResponseEntity<Object> insertIntoassetAllocation(@RequestBody ErAllocation allocation){
    	try {
    		
    		ErAllocation insertIntoAsset = asset_allocation_service.insertIntoAsset(allocation);
    		return ResponseEntity.status(HttpStatus.OK).body(insertIntoAsset);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    @GetMapping("/allocate/getAll")
    public ResponseEntity<Object> getAllAllocation(){
    	try {
    		List<ErAllocation> allallocation = asset_allocation_service.getAllallocation();
    		return ResponseEntity.status(HttpStatus.OK).body(allallocation);
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    @GetMapping("/allocate/get/{e_id}")
    public ResponseEntity<Object> getOneAssetAllocation(@PathVariable Integer e_id){
    	try {
    		
    		Optional<ErAllocation> oneAllocation = asset_allocation_service.getOneAllocation(e_id);
    		
    		return ResponseEntity.status(HttpStatus.OK).body(oneAllocation);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    @PutMapping("/allocate/update")
    public ResponseEntity<Object> updateAssetAllocation(@RequestBody ErAllocation allocation){
        try {
            ErAllocation updated = asset_allocation_service.updateAsset(allocation);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    
    
    @GetMapping("/allocate/bycontract/{cont_id}")
    public ResponseEntity<Object> getAllContracts(@PathVariable Integer cont_id){
    	try {
    		List<ErAllocation> allocationByContract = asset_allocation_service.getAllocationByContract(cont_id);
    		return ResponseEntity.status(HttpStatus.OK).body(allocationByContract);
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    @GetMapping("/allocate/byemployee/{emp_id}")
    public ResponseEntity<Object> getAllEmlpoyee(@PathVariable Integer emp_id){
    	try {
    		List<ErAllocation> allAlocationByEmployee = asset_allocation_service.getAllAlocationByEmployee(emp_id);
    		return ResponseEntity.status(HttpStatus.OK).body(allAlocationByEmployee);
    	}
    	catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.OK).body(ex);
    	}
    }
    
    
    @GetMapping("/allocate/byassetType/{asset_id}")
    public ResponseEntity<Object> getAllAsset(@PathVariable Integer asset_id){
    	try {
    		List<ErAllocation> allAllocationByAsset = asset_allocation_service.getAllAllocationByAsset(asset_id);
    	return ResponseEntity.status(HttpStatus.OK).body(allAllocationByAsset);
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    
    @GetMapping("/allocate/getactivedetail")
    public ResponseEntity<Object> getAllAcitveDetails(){
    	try {
    		List<ErAllocation> allAllocationbynotreturn = asset_allocation_service.getAllAllocationbynotreturn();
    		return ResponseEntity.status(HttpStatus.OK).body(allAllocationbynotreturn);
    		
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
    
    
    
    
    @GetMapping("/allocate/getactiveassetentry/{asset_id}")
    public ResponseEntity<Object> getAllocationActiveAsset(@PathVariable Integer asset_id){
    	 
    	try
    	{
    		Boolean restrictupdateassetentry = asset_allocation_service.restrictupdateassetentry(asset_id);
    		
    		return ResponseEntity.status(HttpStatus.OK).body(restrictupdateassetentry);
    		
    	}catch(Exception ex) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    	}
    }
    
    
 
    
    
    
    
    
    
    
    
    
    
    
    // AssetAllocationHistory
    
    

    @Autowired
    private AssetFullHistoryService service;

    // Fetch all asset history
    @GetMapping("/history")
    public List<AssetFullHistory> getAllAssetHistory() {
        return service.getAllAssetHistory();
    }
    
    @GetMapping("/history/asc")
    public List<AssetFullHistory>getAllAssetHistoryBydesc(){
    	return service.getAllHistorybydesc();
    }
    
    @GetMapping("/history/{start}/{end}")
    public List<AssetFullHistory> getBetweenDates(
            @PathVariable("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        return service.getHistoryBetweenDates(startDate, endDate);
    }
    @GetMapping("/history/{start}")
    public List<AssetFullHistory> getFromStart(@PathVariable("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate){
    	return service.getHistoryFromDate(startDate);
    }
    @GetMapping("/history/end/{end}")
    public List<AssetFullHistory> getFromend(@PathVariable("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
    	return service.getHistoryUntilDate(endDate);
    }
}
