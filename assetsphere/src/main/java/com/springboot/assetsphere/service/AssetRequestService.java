package com.springboot.assetsphere.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.AssetRequestDTO;
import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.AssetRequest;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AssetRequestService.class);

    private final AssetRequestRepository assetRequestRepository;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final AssetAllocationRepository assetAllocationRepository;
    private final AssetRequestDTO assetRequestDTO;

    public AssetRequestService(
            AssetRequestRepository assetRequestRepository,
            EmployeeRepository employeeRepo,
            AssetRepository assetRepo,
            AssetAllocationRepository assetAllocationRepository,
            AssetRequestDTO assetRequestDTO) {
        this.assetRequestRepository = assetRequestRepository;
        this.employeeRepo = employeeRepo;
        this.assetRepo = assetRepo;
        this.assetAllocationRepository = assetAllocationRepository;
        this.assetRequestDTO = assetRequestDTO;
    }

    public AssetRequest addAssetRequest(int employeeId, int assetId, AssetRequest assetRequest) throws ResourceNotFoundException, EmployeeNotFoundException, AssetNotFoundException {
        logger.info("Adding asset request for employeeId: {} and assetId: {}", employeeId, assetId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found"));

        assetRequest.setRequestedAt(LocalDateTime.now());
        assetRequest.setEmployee(employee);
        assetRequest.setAsset(asset);

        return assetRequestRepository.save(assetRequest);
    }

    public List<AssetRequestDTO> getAllAssetRequest(int page, int size) {
        logger.info("Fetching all asset requests - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetRequestDTO.convertAssetRequestToDto(assetRequestRepository.findAll(pageable).getContent());
    }

    public List<AssetRequestDTO> getByEmployeeId(int employeeId) {
        logger.info("Fetching asset requests by employeeId: {}", employeeId);
        return assetRequestDTO.convertAssetRequestToDto(assetRequestRepository.findByEmployeeId(employeeId));
    }

    public List<AssetRequestDTO> getByAssetId(int assetId) {
        logger.info("Fetching asset requests by assetId: {}", assetId);
        return assetRequestDTO.convertAssetRequestToDto(assetRequestRepository.findByAssetId(assetId));
    }

    public List<AssetRequestDTO> getByStatus(String status) {
        logger.info("Fetching asset requests by status: {}", status);
        RequestStatus rs = RequestStatus.valueOf(status.toUpperCase());
        return assetRequestDTO.convertAssetRequestToDto(assetRequestRepository.findByStatus(rs));
    }

    public List<AssetRequestDTO> getByUsername(String username) {
        logger.info("Fetching asset requests by username: {}", username);
        return assetRequestDTO.convertAssetRequestToDto(assetRequestRepository.findByEmployeeUserUsername(username));
    }

    @Transactional
    public AssetRequest updateById(int id, AssetRequest updatedRequest) throws ResourceNotFoundException {
        AssetRequest existingRequest = assetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset request not found with ID: " + id));

        existingRequest.setStatus(updatedRequest.getStatus());
        existingRequest.setAdminComments(updatedRequest.getAdminComments());

        if (updatedRequest.getStatus() != null && updatedRequest.getStatus().toString().equalsIgnoreCase("APPROVED")) {
            AssetAllocation allocation = new AssetAllocation();
            allocation.setAsset(existingRequest.getAsset());
            allocation.setEmployee(existingRequest.getEmployee());
            allocation.setAllocatedAt(LocalDate.now());
            allocation.setStatus(AllocationStatus.ASSIGNED);
            assetAllocationRepository.save(allocation);

            Asset asset = existingRequest.getAsset();
            asset.setStatus(AssetStatus.ASSIGNED);
            assetRepo.save(asset);
        }

        return assetRequestRepository.save(existingRequest);
    }

    @Transactional
    public void deleteById(int id) throws ResourceNotFoundException {
        AssetRequest request = assetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset request not found with ID: " + id));
        assetRequestRepository.delete(request);
    }

}
