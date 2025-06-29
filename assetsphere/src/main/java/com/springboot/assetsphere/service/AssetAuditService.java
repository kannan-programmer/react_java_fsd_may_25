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
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.AssetAudit;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetAuditRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetAuditService {

    private static final Logger logger = LoggerFactory.getLogger(AssetAuditService.class);

    private final AssetAuditRepository assetauditRepo;
    private final EmployeeRepository employeeRepo;
    private final AssetRepository assetRepo;
    private final AssetAuditDTO auditDto;
    private AssetAllocationRepository assetAllocateRepo;

    

   

	public AssetAuditService(AssetAuditRepository assetauditRepo, EmployeeRepository employeeRepo,
			AssetRepository assetRepo, AssetAuditDTO auditDto, AssetAllocationRepository assetAllocateRepo) {
		super();
		this.assetauditRepo = assetauditRepo;
		this.employeeRepo = employeeRepo;
		this.assetRepo = assetRepo;
		this.auditDto = auditDto;
		this.assetAllocateRepo = assetAllocateRepo;
	}

	public AssetAudit createAssetAudit(int employeeId, int assetId, AssetAudit assetAudit)
	        throws EmployeeNotFoundException, AssetNotFoundException {

	    Employee employee = employeeRepo.findById(employeeId)
	            .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found, Id Given is Invalid!"));

	    AssetAllocation assetallocation = assetAllocateRepo.findById(assetId)
	            .orElseThrow(() -> new AssetNotFoundException("Asset Allocation Not Found, Id Given is Invalid!"));

	    Asset asset = assetallocation.getAsset();
	    if (asset == null) {
	        throw new AssetNotFoundException("Associated Asset is null in AssetAllocation!");
	    }

	    assetAudit.setEmployee(employee);
	    assetAudit.setAssetallocation(assetallocation);
	    assetAudit.setAsset(asset); // ✅ Set actual asset reference
	    assetAudit.setAuditedAt(LocalDateTime.now());

	    return assetauditRepo.save(assetAudit);
	}


    public List<AssetAuditDTO> getAllAudits(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetAudit> audits = assetauditRepo.findAll(pageable);
        return auditDto.convertAssetAuditToDto(audits.getContent());
    }

    public List<AssetAuditDTO> getAuditsByEmployee(int employeeId) {
        List<AssetAudit> audits = assetauditRepo.findByEmployeeId(employeeId);
        return auditDto.convertAssetAuditToDto(audits);
    }

    public List<AssetAuditDTO> getAuditsByAsset(int assetId) {
        List<AssetAudit> audits = assetauditRepo.findByAssetId(assetId);
        return auditDto.convertAssetAuditToDto(audits);
    }

    public List<AssetAuditDTO> getAuditsByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        AuditStatus auditStatus = AuditStatus.valueOf(status.toUpperCase());
        Page<AssetAudit> audits = assetauditRepo.findByStatus(auditStatus, pageable);
        return auditDto.convertAssetAuditToDto(audits.getContent());
    }

    public AssetAudit getAuditById(int id) throws ResourceNotFoundException {
        return assetauditRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit not found"));
    }
    
    public List<AssetAuditDTO> getAuditsByUsername(String username) {
        List<AssetAudit> audits = assetauditRepo.findByEmployeeUserUsername(username);
        return auditDto.convertAssetAuditToDto(audits);
    }
    
    @Transactional
    public AssetAudit updateAuditById(int id, AssetAudit updatedAudit) throws ResourceNotFoundException {
        logger.info("Updating audit with ID: {}", id);
        AssetAudit audit = assetauditRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Audit not found with ID: " + id));

        if (updatedAudit.getStatus() != null)
            audit.setStatus(updatedAudit.getStatus());
        if (updatedAudit.getComments() != null)
            audit.setComments(updatedAudit.getComments());

        return assetauditRepo.save(audit);
    }

    @Transactional
    public void deleteAuditById(int id) throws ResourceNotFoundException {
        AssetAudit audit = assetauditRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Audit not found with ID: " + id));

        assetauditRepo.delete(audit);
    }

}
