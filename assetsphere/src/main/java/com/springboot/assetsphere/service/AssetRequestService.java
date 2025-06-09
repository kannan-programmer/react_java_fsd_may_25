package com.springboot.assetsphere.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.AssetRequestDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetRequest;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

@Service
public class AssetRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AssetRequestService.class);

    private final AssetRequestRepository assetrequestRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final AssetRequestDTO assetRequestDTO;

    public AssetRequestService(AssetRequestRepository assetrequestRepo, EmployeeRepository employeeRepo,
                               AssetRepository assetRepo, AssetRequestDTO assetRequestDTO) {
        this.assetrequestRepo = assetrequestRepo;
        this.employeeRepo = employeeRepo;
        this.assetRepo = assetRepo;
        this.assetRequestDTO = assetRequestDTO;
    }

    public AssetRequest addAssetRequest(int employeeId, int assetId, AssetRequest assetRequest) throws ResourceNotFoundException {
        logger.info("Adding asset request for employeeId: {} and assetId: {}", employeeId, assetId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found, Id Given is Invalid!"));

        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset Not Found, Id Given is Invalid!"));

        assetRequest.setRequestedAt(LocalDateTime.now());
        assetRequest.setEmployee(employee);
        assetRequest.setAsset(asset);

        return assetrequestRepo.save(assetRequest);
    }

    public List<AssetRequestDTO> getAllAssetRequest(int page, int size) {
        logger.info("Fetching all asset requests - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetRequestDTO.convertAssetRequestToDto(assetrequestRepo.findAll(pageable).getContent());
    }

    public List<AssetRequestDTO> getByEmployeeId(int employeeId, int page, int size) {
        logger.info("Fetching asset requests by employeeId: {}, page: {}, size: {}", employeeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetRequestDTO.convertAssetRequestToDto(assetrequestRepo.findByEmployeeId(employeeId, pageable).getContent());
    }

    public List<AssetRequestDTO> getByAssetId(int assetId, int page, int size) {
        logger.info("Fetching asset requests by assetId: {}, page: {}, size: {}", assetId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetRequestDTO.convertAssetRequestToDto(assetrequestRepo.findByAssetId(assetId, pageable).getContent());
    }

    public List<AssetRequestDTO> getByStatus(String status, int page, int size) {
        logger.info("Fetching asset requests by status: {}, page: {}, size: {}", status, page, size);
        Pageable pageable = PageRequest.of(page, size);
        RequestStatus rs = RequestStatus.valueOf(status.toUpperCase());
        return assetRequestDTO.convertAssetRequestToDto(assetrequestRepo.findByStatus(rs, pageable).getContent());
    }
}