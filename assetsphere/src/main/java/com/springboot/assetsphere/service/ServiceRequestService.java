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
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.ServiceRequest;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.ServiceRequestRepository;

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

    public ServiceRequest submitServiceRequest(int employeeId, int assetId, ServiceRequest request) throws ResourceNotFoundException {
        logger.info("submitServiceRequest started");
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + assetId));

        request.setEmployee(employee);
        request.setAsset(asset);
        request.setRequestedAt(LocalDateTime.now());
        request.setStatus(ServiceStatus.IN_PROGRESS);

        logger.info("submitServiceRequest completed");
        return serviceRequestRepo.save(request);
    }

    public List<ServiceRequestDTO> getAllServiceRequests(int page, int size) {
        logger.info("getAllServiceRequests started");
        Pageable pageable = PageRequest.of(page, size);
        List<ServiceRequestDTO> result = serviceRequestDTO.convertServiceRequestToDto(serviceRequestRepo.findAll(pageable).getContent());
        logger.info("getAllServiceRequests completed");
        return result;
    }

    public List<ServiceRequestDTO> getByEmployeeId(int employeeId, int page, int size) {
        logger.info("getByEmployeeId started");
        Pageable pageable = PageRequest.of(page, size);
        List<ServiceRequestDTO> result = serviceRequestDTO.convertServiceRequestToDto(serviceRequestRepo.findByEmployeeId(employeeId, pageable).getContent());
        logger.info("getByEmployeeId completed");
        return result;
    }

    public List<ServiceRequestDTO> getByAssetId(int assetId, int page, int size) {
        logger.info("getByAssetId started");
        Pageable pageable = PageRequest.of(page, size);
        List<ServiceRequestDTO> result = serviceRequestDTO.convertServiceRequestToDto(serviceRequestRepo.findByAssetId(assetId, pageable).getContent());
        logger.info("getByAssetId completed");
        return result;
    }

    public List<ServiceRequestDTO> getByStatus(String status, int page, int size) {
        logger.info("getByStatus started");
        Pageable pageable = PageRequest.of(page, size);
        List<ServiceRequestDTO> result = serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByStatus(ServiceStatus.valueOf(status.toUpperCase()), pageable).getContent());
        logger.info("getByStatus completed");
        return result;
    }

    public List<ServiceRequestDTO> getByUserEmail(String email, int page, int size) {
        logger.info("getByUserEmail started");
        Pageable pageable = PageRequest.of(page, size);
        List<ServiceRequestDTO> result = serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByEmployeeUserEmail(email, pageable).getContent());
        logger.info("getByUserEmail completed");
        return result;
    }

    public List<ServiceRequestDTO> getByUsername(String username, int page, int size) {
        logger.info("getByUsername started");
        Pageable pageable = PageRequest.of(page, size);
        List<ServiceRequestDTO> result = serviceRequestDTO.convertServiceRequestToDto(
                serviceRequestRepo.findByEmployeeUserUsername(username, pageable).getContent());
        logger.info("getByUsername completed");
        return result;
    }
}