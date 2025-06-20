package com.springboot.assetsphere.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.AssetAuditDTO;
import com.springboot.assetsphere.enums.AuditStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAudit;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAuditRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

@Service
public class AssetAuditService {

    private static final Logger logger = LoggerFactory.getLogger(AssetAuditService.class);

    private final AssetAuditRepository assetauditRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final AssetAuditDTO auditDto;

    public AssetAuditService(AssetAuditRepository assetauditRepo, EmployeeRepository employeeRepo,
                              AssetRepository assetRepo, AssetAuditDTO auditDto) {
        this.assetauditRepo = assetauditRepo;
        this.employeeRepo = employeeRepo;
        this.assetRepo = assetRepo;
        this.auditDto = auditDto;
    }

    public AssetAudit createAssetAudit(int employeeId, int assetId, AssetAudit assetAudit) throws ResourceNotFoundException {
        logger.info("createAssetAudit called with employeeId={} and assetId={}", employeeId, assetId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found, Id Given is Invalid!"));
        Asset asset = assetRepo.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset Not Found, Id Given is Invalid!"));

        assetAudit.setEmployee(employee);
        assetAudit.setAsset(asset);
        assetAudit.setAuditedAt(LocalDateTime.now());

        return assetauditRepo.save(assetAudit);
    }

    public List<AssetAuditDTO> getAllAudits(int page, int size) {
        logger.info("getAllAudits called with page={} and size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetAudit> audits = assetauditRepo.findAll(pageable);
        return auditDto.convertAssetAuditToDto(audits.getContent());
    }

    public List<AssetAuditDTO> getAuditsByEmployee(int employeeId, int page, int size) {
        logger.info("getAuditsByEmployee called with employeeId={}, page={}, size={}", employeeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetAudit> audits = assetauditRepo.findByEmployeeId(employeeId, pageable);
        return auditDto.convertAssetAuditToDto(audits.getContent());
    }

    public List<AssetAuditDTO> getAuditsByAsset(int assetId, int page, int size) {
        logger.info("getAuditsByAsset called with assetId={}, page={}, size={}", assetId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetAudit> audits = assetauditRepo.findByAssetId(assetId, pageable);
        return auditDto.convertAssetAuditToDto(audits.getContent());
    }

    public List<AssetAuditDTO> getAuditsByStatus(String status, int page, int size) {
        logger.info("getAuditsByStatus called with status={}, page={}, size={}", status, page, size);
        Pageable pageable = PageRequest.of(page, size);
        AuditStatus auditStatus = AuditStatus.valueOf(status.toUpperCase());
        Page<AssetAudit> audits = assetauditRepo.findByStatus(auditStatus, pageable);
        return auditDto.convertAssetAuditToDto(audits.getContent());
    }

    public AssetAudit getAuditById(int id) throws ResourceNotFoundException {
        logger.info("getAuditById called with id={}", id);
        return assetauditRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit not found"));
    }
}