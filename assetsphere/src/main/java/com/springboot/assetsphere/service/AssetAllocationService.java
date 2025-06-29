package com.springboot.assetsphere.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.AssetAllocationDTO;
import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetAuditRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetReturnRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetAllocationService {

    private static final Logger logger = LoggerFactory.getLogger(AssetAllocationService.class);

    private final AssetAllocationRepository assetAllocationRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final AssetAllocationDTO assetAllocationDTO;
    private AssetReturnRequestRepository assetReturnRequestRepo;
    private final AssetAuditRepository assetauditRepo;

  

  


	public AssetAllocationService(AssetAllocationRepository assetAllocationRepo, EmployeeRepository employeeRepo,
			AssetRepository assetRepo, AssetAllocationDTO assetAllocationDTO,
			AssetReturnRequestRepository assetReturnRequestRepo, AssetAuditRepository assetauditRepo) {
		super();
		this.assetAllocationRepo = assetAllocationRepo;
		this.employeeRepo = employeeRepo;
		this.assetRepo = assetRepo;
		this.assetAllocationDTO = assetAllocationDTO;
		this.assetReturnRequestRepo = assetReturnRequestRepo;
		this.assetauditRepo = assetauditRepo;
	}


	public AssetAllocation addAllocation(int employeeId, int assetId, AssetAllocation assetAllocation)
            throws EmployeeNotFoundException, AssetNotFoundException {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + assetId));

        if (asset.getStatus() == AssetStatus.ASSIGNED || asset.getStatus() == AssetStatus.IN_SERVICE) {
            throw new IllegalStateException("Asset with ID " + assetId + " is already allocated or in use.");
        }

        assetAllocation.setEmployee(employee);
        assetAllocation.setAsset(asset);
        assetAllocation.setAllocatedAt(LocalDate.now());

        asset.setStatus(AssetStatus.ASSIGNED);
        assetRepo.save(asset); 

        return assetAllocationRepo.save(assetAllocation);
    }


    public List<AssetAllocationDTO> getAllAllocations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<AssetAllocation> allocations = assetAllocationRepo.findAll(pageable).getContent();
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    public List<AssetAllocationDTO> getAllocationsByEmployee(int employeeId) {
     
        List<AssetAllocation> allocations = assetAllocationRepo.findByEmployeeId(employeeId);
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    public List<AssetAllocationDTO> getAllocationsByAsset(int assetId) {
   
        List<AssetAllocation> allocations = assetAllocationRepo.findByAssetId(assetId);
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    public List<AssetAllocationDTO> getAllocationsByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        AllocationStatus allocationStatus = AllocationStatus.valueOf(status.toUpperCase());
        List<AssetAllocation> allocations = assetAllocationRepo.findByStatus(allocationStatus, pageable).getContent();
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    public List<AssetAllocationDTO> getAllocationsByUsername(String username) {
      
        List<AssetAllocation> allocations = assetAllocationRepo.findByEmployeeUserUsername(username);
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    @Transactional
    public AssetAllocation updateAllocation(int id, AssetAllocation updatedAllocation) throws ResourceNotFoundException {
        AssetAllocation dbAllocation = assetAllocationRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invalid Allocation ID: " + id));

        if (updatedAllocation.getAllocatedAt() != null)
            dbAllocation.setAllocatedAt(updatedAllocation.getAllocatedAt());

        if (updatedAllocation.getReturnedAt() != null)
            dbAllocation.setReturnedAt(updatedAllocation.getReturnedAt());

        if (updatedAllocation.getStatus() != null)
            dbAllocation.setStatus(updatedAllocation.getStatus());

        if (updatedAllocation.getAsset() != null)
            dbAllocation.setAsset(updatedAllocation.getAsset());

        if (updatedAllocation.getEmployee() != null)
            dbAllocation.setEmployee(updatedAllocation.getEmployee());

        return assetAllocationRepo.save(dbAllocation);
    }
    
    @Transactional
    public void deleteAllocation(int id) throws ResourceNotFoundException {
        AssetAllocation allocation = assetAllocationRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset Allocation not found with ID: " + id));

        // 1. Delete dependent audits
        assetauditRepo.deleteByAssetAllocationId(id);

        // 2. Delete dependent return requests
        assetReturnRequestRepo.deleteByAllocationId(id);

        // 3. Now delete the allocation
        assetAllocationRepo.delete(allocation);
    }

}
