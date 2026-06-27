package com.JIMS.integration.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JIMS.integration.entity.ErAllocation;
import com.JIMS.integration.repository.Asset_Allocation_Repo;

@Service
public class Asset_Allocation_Service {
	
	@Autowired
	private Asset_Allocation_Repo asset_allocation_repo;
	
	public ErAllocation insertIntoAsset(ErAllocation erallocation) {
		return asset_allocation_repo.save(erallocation);
	}

	public List<ErAllocation> getAllallocation(){
		return asset_allocation_repo.findAll();
	}
	
	public Optional<ErAllocation> getOneAllocation(Integer er_id) {
		return asset_allocation_repo.findById(er_id);
	}
	
	
	public ErAllocation updateAsset(ErAllocation allocation) {
        if (allocation.getAllocationId() == null) {
            throw new IllegalArgumentException("Allocation ID cannot be null for update.");
        }

        Optional<ErAllocation> existingOpt = asset_allocation_repo.findById(allocation.getAllocationId());

        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Allocation not found with ID: " + allocation.getAllocationId());
        }

        ErAllocation existing = existingOpt.get();

        // Update fields only if they are not null
        if (allocation.getTypeId() != null) existing.setTypeId(allocation.getTypeId());
        if (allocation.getModelId() != null) existing.setModelId(allocation.getModelId());
        if (allocation.getMakeId() != null) existing.setMakeId(allocation.getMakeId());
        if (allocation.getContractId() != null) existing.setContractId(allocation.getContractId());
        if (allocation.getAssetSerialNo() != null) existing.setAssetSerialNo(allocation.getAssetSerialNo());
        if (allocation.getPurchaseDate() != null) existing.setPurchaseDate(allocation.getPurchaseDate());
        if (allocation.getAllocatedQty() != null) existing.setAllocatedQty(allocation.getAllocatedQty());
        if (allocation.getAllocatedTo() != null) existing.setAllocatedTo(allocation.getAllocatedTo());
        if (allocation.getDepartment() != null) existing.setDepartment(allocation.getDepartment());
        if (allocation.getAllocatedDate() != null) existing.setAllocatedDate(allocation.getAllocatedDate());
        if (allocation.getAllocatedReturnDate() != null) existing.setAllocatedReturnDate(allocation.getAllocatedReturnDate());
        if (allocation.getExpectedReturnDate() != null) existing.setExpectedReturnDate(allocation.getExpectedReturnDate());
        if (allocation.getRemarks() != null) existing.setRemarks(allocation.getRemarks());
        if (allocation.getModifiedBy() != null) existing.setModifiedBy(allocation.getModifiedBy());
        existing.setModifiedDate(LocalDateTime.now()); // set modification timestamp

        return asset_allocation_repo.save(existing);
    }
	
	
	
	public List<ErAllocation> getAllocationByContract(Integer contractId){
		return asset_allocation_repo.findByContractId(contractId);
	}
	
	public List<ErAllocation> getAllAlocationByEmployee(Integer user_id){
		return asset_allocation_repo.findByAllocatedTo(user_id);
	}
	
	public List<ErAllocation> getAllAllocationByAsset(Integer asset_id){
		return asset_allocation_repo.findByAssetId(asset_id);
	}
	
	public List<ErAllocation> getAllAllocationbynotreturn(){
		return asset_allocation_repo.getActiveAllocation();
	}

	
	public Boolean restrictupdateassetentry(Integer asset_id) {
		return asset_allocation_repo.existsAllocated(asset_id);
	}
	
}
