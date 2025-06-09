package com.springboot.assetsphere.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.AssetTrackingDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetTracking;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetTrackingRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

@Service
public class AssetTrackingService {

    private static final Logger logger = LoggerFactory.getLogger(AssetTrackingService.class);

    private final AssetTrackingRepository assetTrackingRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final AssetTrackingDTO assetTrackingDTO;

    public AssetTrackingService(AssetTrackingRepository assetTrackingRepo,
                                EmployeeRepository employeeRepo,
                                AssetRepository assetRepo,
                                AssetTrackingDTO assetTrackingDTO) {
        this.assetTrackingRepo = assetTrackingRepo;
        this.employeeRepo = employeeRepo;
        this.assetRepo = assetRepo;
        this.assetTrackingDTO = assetTrackingDTO;
    }

    public AssetTracking addTrackingLog(int employeeId, int assetId, AssetTracking tracking) throws ResourceNotFoundException {
        logger.info("Adding asset tracking log for employeeId: {} and assetId: {}", employeeId, assetId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + assetId));

        tracking.setEmployee(employee);
        tracking.setAsset(asset);
        tracking.setTimestamp(LocalDateTime.now());

        return assetTrackingRepo.save(tracking);
    }

    public List<AssetTrackingDTO> getAllTrackingLogs(int page, int size) {
        logger.info("Fetching all asset tracking logs - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetTrackingDTO.convertAssetTrackingToDto(assetTrackingRepo.findAll(pageable).getContent());
    }

    public List<AssetTrackingDTO> getByEmployeeId(int employeeId, int page, int size) {
        logger.info("Fetching asset tracking logs by employeeId: {}, page: {}, size: {}", employeeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetTrackingDTO.convertAssetTrackingToDto(
                assetTrackingRepo.findByEmployeeId(employeeId, pageable).getContent());
    }

    public List<AssetTrackingDTO> getByAssetId(int assetId, int page, int size) {
        logger.info("Fetching asset tracking logs by assetId: {}, page: {}, size: {}", assetId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetTrackingDTO.convertAssetTrackingToDto(
                assetTrackingRepo.findByAssetId(assetId, pageable).getContent());
    }

    public List<AssetTrackingDTO> getByAction(String action, int page, int size) {
        logger.info("Fetching asset tracking logs by action: {}, page: {}, size: {}", action, page, size);
        Pageable pageable = PageRequest.of(page, size);
        RequestStatus status = RequestStatus.valueOf(action.toUpperCase());
        return assetTrackingDTO.convertAssetTrackingToDto(
                assetTrackingRepo.findByAction(status, pageable).getContent());
    }

    public List<AssetTrackingDTO> getByUserEmail(String email, int page, int size) {
        logger.info("Fetching asset tracking logs by user email: {}, page: {}, size: {}", email, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetTrackingDTO.convertAssetTrackingToDto(
                assetTrackingRepo.findByEmployeeUserEmail(email, pageable).getContent());
    }

    public List<AssetTrackingDTO> getByUsername(String username, int page, int size) {
        logger.info("Fetching asset tracking logs by username: {}, page: {}, size: {}", username, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetTrackingDTO.convertAssetTrackingToDto(
                assetTrackingRepo.findByEmployeeUserUsername(username, pageable).getContent());
    }
}