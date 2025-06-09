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
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

@Service
public class AssetAllocationService {

    private static final Logger logger = LoggerFactory.getLogger(AssetAllocationService.class);

    private final AssetAllocationRepository assetAllocationRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final AssetAllocationDTO assetAllocationDTO;

    public AssetAllocationService(AssetAllocationRepository assetAllocationRepo,
                                   EmployeeRepository employeeRepo,
                                   AssetRepository assetRepo,
                                   AssetAllocationDTO assetAllocationDTO) {
        this.assetAllocationRepo = assetAllocationRepo;
        this.employeeRepo = employeeRepo;
        this.assetRepo = assetRepo;
        this.assetAllocationDTO = assetAllocationDTO;
    }

    public AssetAllocation addAllocation(int employeeId, int assetId, AssetAllocation assetAllocation)
            throws ResourceNotFoundException {
        logger.info("addAllocation called with employeeId={} and assetId={}", employeeId, assetId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + assetId));

        assetAllocation.setEmployee(employee);
        assetAllocation.setAsset(asset);
        assetAllocation.setAllocatedAt(LocalDate.now());

        return assetAllocationRepo.save(assetAllocation);
    }

    public List<AssetAllocationDTO> getAllAllocations(int page, int size) {
        logger.info("getAllAllocations called with page={} and size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        List<AssetAllocation> allocations = assetAllocationRepo.findAll(pageable).getContent();
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    public List<AssetAllocationDTO> getAllocationsByEmployee(int employeeId, int page, int size) {
        logger.info("getAllocationsByEmployee called with employeeId={}, page={}, size={}", employeeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        List<AssetAllocation> allocations = assetAllocationRepo.findByEmployeeId(employeeId, pageable).getContent();
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    public List<AssetAllocationDTO> getAllocationsByAsset(int assetId, int page, int size) {
        logger.info("getAllocationsByAsset called with assetId={}, page={}, size={}", assetId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        List<AssetAllocation> allocations = assetAllocationRepo.findByAssetId(assetId, pageable).getContent();
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }

    public List<AssetAllocationDTO> getAllocationsByStatus(String status, int page, int size) {
        logger.info("getAllocationsByStatus called with status={}, page={}, size={}", status, page, size);
        Pageable pageable = PageRequest.of(page, size);
        AllocationStatus allocationStatus = AllocationStatus.valueOf(status.toUpperCase());
        List<AssetAllocation> allocations = assetAllocationRepo.findByStatus(allocationStatus, pageable).getContent();
        return assetAllocationDTO.convertAssetAllocationToDto(allocations);
    }
}