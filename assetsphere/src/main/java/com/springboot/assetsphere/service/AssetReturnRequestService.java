package com.springboot.assetsphere.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.AssetReturnRequestDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.AssetReturnRequest;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetReturnRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

@Service
public class AssetReturnRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AssetReturnRequestService.class);

    private final AssetReturnRequestRepository assetReturnRequestRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetAllocationRepository assetAllocationRepo;
    private final AssetReturnRequestDTO assetReturnRequestDTO;

    public AssetReturnRequestService(AssetReturnRequestRepository assetReturnRequestRepo,
                                     EmployeeRepository employeeRepo,
                                     AssetAllocationRepository assetAllocationRepo,
                                     AssetReturnRequestDTO assetReturnRequestDTO) {
        this.assetReturnRequestRepo = assetReturnRequestRepo;
        this.employeeRepo = employeeRepo;
        this.assetAllocationRepo = assetAllocationRepo;
        this.assetReturnRequestDTO = assetReturnRequestDTO;
    }

    public AssetReturnRequest addAssetReturnRequest(int employeeId, int allocationId, AssetReturnRequest request) throws ResourceNotFoundException {
        logger.info("Adding asset return request for employeeId: {} and allocationId: {}", employeeId, allocationId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found, ID is invalid"));

        AssetAllocation allocation = assetAllocationRepo.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation Not Found, ID is invalid"));

        request.setEmployee(employee);
        request.setAllocation(allocation);
        request.setRequestedAt(LocalDateTime.now());

        return assetReturnRequestRepo.save(request);
    }

    public List<AssetReturnRequestDTO> getAllAssetReturnRequests(int page, int size) {
        logger.info("Fetching all asset return requests - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetReturnRequestDTO.convertAssetReturnToDto(assetReturnRequestRepo.findAll(pageable).getContent());
    }

    public List<AssetReturnRequestDTO> getByEmployeeId(int employeeId, int page, int size) {
        logger.info("Fetching asset return requests by employeeId: {}, page: {}, size: {}", employeeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetReturnRequestDTO.convertAssetReturnToDto(
                assetReturnRequestRepo.findByEmployeeId(employeeId, pageable).getContent()
        );
    }

    public List<AssetReturnRequestDTO> getByStatus(String status, int page, int size) {
        logger.info("Fetching asset return requests by status: {}, page: {}, size: {}", status, page, size);
        Pageable pageable = PageRequest.of(page, size);
        RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase());
        return assetReturnRequestDTO.convertAssetReturnToDto(
                assetReturnRequestRepo.findByStatus(requestStatus, pageable).getContent()
        );
    }
}