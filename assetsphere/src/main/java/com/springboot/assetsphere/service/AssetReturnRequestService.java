package com.springboot.assetsphere.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.AssetReturnRequestDTO;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.AssetReturnRequest;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetAuditRepository;
import com.springboot.assetsphere.repository.AssetReturnRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetReturnRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AssetReturnRequestService.class);

    private final AssetReturnRequestRepository assetReturnRequestRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetAllocationRepository assetAllocationRepo;
    private final AssetReturnRequestDTO assetReturnRequestDTO;

    public AssetReturnRequestService(
            AssetReturnRequestRepository assetReturnRequestRepo,
            EmployeeRepository employeeRepo,
            AssetAllocationRepository assetAllocationRepo,
            AssetReturnRequestDTO assetReturnRequestDTO
    ) {
        this.assetReturnRequestRepo = assetReturnRequestRepo;
        this.employeeRepo = employeeRepo;
        this.assetAllocationRepo = assetAllocationRepo;
        this.assetReturnRequestDTO = assetReturnRequestDTO;
    }

    public AssetReturnRequest addAssetReturnRequest(int employeeId, int allocationId, AssetReturnRequest request)
            throws ResourceNotFoundException, EmployeeNotFoundException {
        logger.info("Adding asset return request for employeeId: {} and allocationId: {}", employeeId, allocationId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found, ID is invalid"));

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
        return assetReturnRequestDTO.convertAssetReturnToDto(
                assetReturnRequestRepo.findAll(pageable).getContent()
        );
    }

    public List<AssetReturnRequestDTO> getByEmployeeId(int employeeId) {
        logger.info("Fetching asset return requests by employeeId: {}", employeeId);
        return assetReturnRequestDTO.convertAssetReturnToDto(
                assetReturnRequestRepo.findByEmployeeId(employeeId)
        );
    }

    public List<AssetReturnRequestDTO> getByUsername(String username) {
        logger.info("Fetching asset return requests by username: {}", username);
        List<AssetReturnRequest> requests = assetReturnRequestRepo.findByEmployeeUsername(username);
        return assetReturnRequestDTO.convertAssetReturnToDto(requests);
    }

    public List<AssetReturnRequestDTO> getByStatus(String status) {
        logger.info("Fetching asset return requests by status: {}", status);
        RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase());
        return assetReturnRequestDTO.convertAssetReturnToDto(
                assetReturnRequestRepo.findByStatus(requestStatus)
        );
    }
    
    @Transactional
    public AssetReturnRequest updateById(int requestId, AssetReturnRequest updatedRequest)
            throws ResourceNotFoundException {

        AssetReturnRequest existing = assetReturnRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Return request not found for ID: " + requestId));

        if (updatedRequest.getStatus() != null)
            existing.setStatus(updatedRequest.getStatus());

        if (updatedRequest.getAdminComments() != null)
            existing.setAdminComments(updatedRequest.getAdminComments());

        if (updatedRequest.getReason() != null)
            existing.setReason(updatedRequest.getReason());

        if (updatedRequest.getStatus() == RequestStatus.APPROVED) {
            AssetAllocation allocation = existing.getAllocation();
            existing.setResolvedAt(LocalDateTime.now());
            if (allocation == null) {
                throw new ResourceNotFoundException("No allocation linked to this return request.");
            }

            allocation.getAsset().setStatus(AssetStatus.AVAILABLE);

            existing.setAllocation(null);
            assetReturnRequestRepo.saveAndFlush(existing); // flush so that DB updates before delete

            assetAllocationRepo.delete(allocation);

        }

        return assetReturnRequestRepo.save(existing);
    }





    @Transactional
    public void deleteById(int requestId) throws ResourceNotFoundException {
        AssetReturnRequest existing = assetReturnRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Return request not found for ID: " + requestId));

        assetReturnRequestRepo.delete(existing);
    }

}
