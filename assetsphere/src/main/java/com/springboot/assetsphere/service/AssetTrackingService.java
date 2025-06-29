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
import com.springboot.assetsphere.enums.TrackingAction;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetTracking;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetTrackingRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

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

    
    public AssetTracking addTrackingLog(int employeeId, int assetId, AssetTracking tracking) throws AssetNotFoundException, EmployeeNotFoundException {
       
    	logger.info("Adding asset tracking log for employeeId: {} and assetId: {}", employeeId, assetId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + assetId));

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

    public List<AssetTrackingDTO> getByEmployeeId(int employeeId) {
        logger.info("Fetching asset tracking logs by employeeId: {}", employeeId);
        return assetTrackingDTO.convertAssetTrackingToDto(assetTrackingRepo.findByEmployeeId(employeeId));
    }

    public List<AssetTrackingDTO> getByAssetId(int assetId) {
        logger.info("Fetching asset tracking logs by assetId: {}", assetId);
        return assetTrackingDTO.convertAssetTrackingToDto(assetTrackingRepo.findByAssetId(assetId));
    }

    public List<AssetTrackingDTO> getByAction(String action) {
        logger.info("Fetching asset tracking logs by action: {}", action);
        TrackingAction status = TrackingAction.valueOf(action.toUpperCase());
        return assetTrackingDTO.convertAssetTrackingToDto(assetTrackingRepo.findByAction(status));
    }

    public List<AssetTrackingDTO> getByUsername(String username) {
        logger.info("Fetching asset tracking logs by username: {}", username);
        return assetTrackingDTO.convertAssetTrackingToDto(assetTrackingRepo.findByEmployeeUserUsername(username));
    }

    @Transactional
    public AssetTracking updateByUsername(String username, AssetTracking updatedTracking) throws ResourceNotFoundException {
        List<AssetTracking> trackingList = assetTrackingRepo.findByEmployeeUserUsername(username);
        
        if (trackingList.isEmpty()) {
            throw new ResourceNotFoundException("Tracking not found for user: " + username);
        }

        AssetTracking tracking = trackingList.get(0); // update first matching log

        if (updatedTracking.getAction() != null)
            tracking.setAction(updatedTracking.getAction());

        if (updatedTracking.getRemarks() != null)
            tracking.setRemarks(updatedTracking.getRemarks());

        return assetTrackingRepo.save(tracking);
    }


    @Transactional
    public void deleteByUsername(String username) throws ResourceNotFoundException {
        List<AssetTracking> trackingList = assetTrackingRepo.findByEmployeeUserUsername(username);
        
        if (trackingList.isEmpty()) {
            throw new ResourceNotFoundException("Tracking not found for user: " + username);
        }

        assetTrackingRepo.delete(trackingList.get(0)); 
    }

}
