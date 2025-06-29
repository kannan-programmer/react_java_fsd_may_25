package com.springboot.assetsphere.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.ServiceRequestDTO;
import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.ServiceRequest;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.ServiceRequestRepository;

import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.enums.AssetStatus;
import jakarta.transaction.Transactional;

@Service
public class ServiceRequestService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestService.class);

    private final ServiceRequestRepository serviceRequestRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final ServiceRequestDTO serviceRequestDTO;

    public ServiceRequestService(ServiceRequestRepository serviceRequestRepo,
                                 EmployeeRepository employeeRepo,
                                 AssetRepository assetRepo,
                                 ServiceRequestDTO serviceRequestDTO) {
        this.serviceRequestRepo = serviceRequestRepo;
        this.employeeRepo = employeeRepo;
        this.assetRepo = assetRepo;
        this.serviceRequestDTO = serviceRequestDTO;
    }

    public ServiceRequest submitServiceRequest(int employeeId, int assetId, ServiceRequest request) throws ResourceNotFoundException, AssetNotFoundException {
        logger.info("Submitting service request");

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + assetId));

        request.setEmployee(employee);
        request.setAsset(asset);
        request.setRequestedAt(LocalDateTime.now());
        request.setStatus(ServiceStatus.IN_PROGRESS);

        return serviceRequestRepo.save(request);
    }

    public List<ServiceRequestDTO> getAllServiceRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findAll(pageable).getContent());
    }

    public List<ServiceRequestDTO> getByEmployeeId(int employeeId) {
        return serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByEmployeeId(employeeId));
    }

    public List<ServiceRequestDTO> getByAssetId(int assetId) {
        return serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByAssetId(assetId));
    }

    public List<ServiceRequestDTO> getByStatus(String status, int page, int size) throws ResourceNotFoundException {
        ServiceStatus serviceStatus;
        try {
            serviceStatus = ServiceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid service status: {}", status);
            throw new ResourceNotFoundException("Invalid status: " + status);
        }

        Pageable pageable = PageRequest.of(page, size);
        return serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByStatus(serviceStatus, pageable).getContent());
    }

    public List<ServiceRequestDTO> getByName(String name) {
        return serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByEmployeeName(name));
    }

    public List<ServiceRequestDTO> getByUsername(String username) {
        return serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByEmployeeUserUsername(username));
    }

    @Transactional
    public ServiceRequest updateById(int id, ServiceRequest updated) throws ResourceNotFoundException {
        logger.info("Updating service request with ID: {}", id);

        ServiceRequest request = serviceRequestRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found with ID: " + id));

        if (updated.getStatus() != null) {
            request.setStatus(updated.getStatus());

            if (updated.getStatus() == ServiceStatus.IN_PROGRESS) {
                if (request.getAsset() != null) {
                    request.getAsset().setStatus(AssetStatus.IN_SERVICE);
                }
            }
        }

        if (updated.getDescription() != null) {
            request.setDescription(updated.getDescription());
        }

        if (updated.getAdminComments() != null) {
            request.setAdminComments(updated.getAdminComments());
        }

        return serviceRequestRepo.save(request);
    }


    @Transactional
    public void deleteById(int id) throws ResourceNotFoundException {
        logger.info("Deleting service request with ID: {}", id);

        ServiceRequest request = serviceRequestRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found with ID: " + id));
        
        serviceRequestRepo.delete(request);
    }
}
