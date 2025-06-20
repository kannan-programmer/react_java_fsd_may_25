package com.springboot.assetsphere.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.LiquidAssetRequestDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.LiquidAssetRequest;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.LiquidAssetRequestRepository;

@Service
public class LiquidAssetRequestService {

    private static final Logger logger = LoggerFactory.getLogger(LiquidAssetRequestService.class);

    private final LiquidAssetRequestRepository liquidAssetRequestRepo;
    private final EmployeeRepository employeeRepo;
    private final LiquidAssetRequestDTO liquidAssetRequestDTO;

    public LiquidAssetRequestService(LiquidAssetRequestRepository liquidAssetRequestRepo,
                                    EmployeeRepository employeeRepo,
                                    LiquidAssetRequestDTO liquidAssetRequestDTO) {
        this.liquidAssetRequestRepo = liquidAssetRequestRepo;
        this.employeeRepo = employeeRepo;
        this.liquidAssetRequestDTO = liquidAssetRequestDTO;
    }

    public LiquidAssetRequest addLiquidAssetRequest(int employeeId, LiquidAssetRequest request) throws ResourceNotFoundException {
        logger.info("Adding liquid asset request for employee id {}", employeeId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Employee not found with ID: {}", employeeId);
                    return new ResourceNotFoundException("Employee not found with ID: " + employeeId);
                });

        request.setEmployee(employee);
        request.setSubmittedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);

        LiquidAssetRequest savedRequest = liquidAssetRequestRepo.save(request);
        logger.info("Liquid asset request saved with id {}", savedRequest.getId());
        return savedRequest;
    }

    public List<LiquidAssetRequestDTO> getAllLiquidAssetRequests(int page, int size) {
        logger.info("Fetching all liquid asset requests - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return liquidAssetRequestDTO.convertLiquidAssetRequestToDto(liquidAssetRequestRepo.findAll(pageable).getContent());
    }

    public List<LiquidAssetRequestDTO> getByEmployeeId(int employeeId, int page, int size) {
        logger.info("Fetching liquid asset requests by employee id {}, page: {}, size: {}", employeeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return liquidAssetRequestDTO.convertLiquidAssetRequestToDto(liquidAssetRequestRepo.findByEmployeeId(employeeId, pageable).getContent());
    }

    public List<LiquidAssetRequestDTO> getByStatus(String status, int page, int size) throws ResourceNotFoundException {
        logger.info("Fetching liquid asset requests by status {}, page: {}, size: {}", status, page, size);
        RequestStatus enumStatus;
        try {
            enumStatus = RequestStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request status provided: {}", status);
            throw new ResourceNotFoundException("Invalid status: " + status);
        }
        Pageable pageable = PageRequest.of(page, size);
        return liquidAssetRequestDTO.convertLiquidAssetRequestToDto(liquidAssetRequestRepo.findByStatus(enumStatus, pageable).getContent());
    }

    public List<LiquidAssetRequestDTO> getByUserEmail(String email, int page, int size) {
        logger.info("Fetching liquid asset requests by user email {}, page: {}, size: {}", email, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return liquidAssetRequestDTO.convertLiquidAssetRequestToDto(liquidAssetRequestRepo.findByEmployeeUserEmail(email, pageable).getContent());
    }

    public List<LiquidAssetRequestDTO> getByUsername(String username, int page, int size) {
        logger.info("Fetching liquid asset requests by username {}, page: {}, size: {}", username, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return liquidAssetRequestDTO.convertLiquidAssetRequestToDto(liquidAssetRequestRepo.findByEmployeeUserUsername(username, pageable).getContent());
    }
}