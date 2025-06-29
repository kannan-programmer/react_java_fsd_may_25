package com.springboot.assetsphere.service;

import java.io.IOException;

import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.assetsphere.dto.HRDashboardStatsDto;
import com.springboot.assetsphere.dto.HrDTO;
import com.springboot.assetsphere.enums.JobTitle;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.HrRepository;
import com.springboot.assetsphere.repository.LiquidAssetRequestRepository;
import com.springboot.assetsphere.repository.ServiceRequestRepository;

import jakarta.transaction.Transactional;

@Service
public class HRService {

	private static final Logger logger = LoggerFactory.getLogger(HRService.class);
    private final HrRepository hrRepository;
    private final HrDTO hrDTO;
    private UserService userService;
    private AssetRepository assetRepo;
    private AssetRequestRepository assetRequestRepo;
    private AssetAllocationRepository allocationRepo;
    
    private ServiceRequestRepository serviceRequestRepo;
    private LiquidAssetRequestRepository liquidAssetRepo;
    private EmployeeRepository employeeRepo;




    public HRService(HrRepository hrRepository, HrDTO hrDTO, UserService userService, AssetRepository assetRepo,
			AssetRequestRepository assetRequestRepo, AssetAllocationRepository allocationRepo,
			ServiceRequestRepository serviceRequestRepo, LiquidAssetRequestRepository liquidAssetRepo,
			EmployeeRepository employeeRepo) {
		super();
		this.hrRepository = hrRepository;
		this.hrDTO = hrDTO;
		this.userService = userService;
		this.assetRepo = assetRepo;
		this.assetRequestRepo = assetRequestRepo;
		this.allocationRepo = allocationRepo;
		this.serviceRequestRepo = serviceRequestRepo;
		this.liquidAssetRepo = liquidAssetRepo;
		this.employeeRepo = employeeRepo;
	}



	public Hr addHR(Hr hr) {
    	
        if (hr.getUser() == null || hr.getUser().getUsername() == null) {
            throw new RuntimeException("User information must be provided");
        }

        User user = hr.getUser();
        Optional<User> existingUserOpt = userService.findByUsername(user.getUsername());

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
        } else {
            user.setRole(Role.HR);
            user = userService.signUp(user); 
        }

        hr.setUser(user);
        hr.setCreatedAt(LocalDate.now());
        hr.setJobTitle(JobTitle.HR_MANAGER);
        return hrRepository.save(hr);
    }



    public List<HrDTO> getAllHR() {
        List<Hr> list = hrRepository.findAll();
        return hrDTO.convertHrToDto(list);
    }

    public HrDTO getHRByUsername(String username) throws ResourceNotFoundException {
        Hr hr = hrRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("HR not found with username: " + username));
        return hrDTO.convertHrToDto(List.of(hr)).get(0);
    }

    public HrDTO getById(int id) throws ResourceNotFoundException {
        Hr hr = hrRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HR not found with ID: " + id));
        return hrDTO.convertHrToDto(List.of(hr)).get(0);
    }
    
    @Transactional
    public Hr updateHRByUsername(String username, Hr updatedHr) throws ResourceNotFoundException {
        Hr hr = hrRepository.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("HR not found with username: " + username));

        if (updatedHr.getName() != null) hr.setName(updatedHr.getName());
        if (updatedHr.getAddress() != null) hr.setAddress(updatedHr.getAddress());
        if (updatedHr.getContactNumber() != null) hr.setContactNumber(updatedHr.getContactNumber());
        if (updatedHr.getImageUrl() != null) hr.setImageUrl(updatedHr.getImageUrl());
        if (updatedHr.getGender() != null) hr.setGender(updatedHr.getGender());

        return hrRepository.save(hr);
    }

    @Transactional
    public void deleteHRByUsername(String username) throws ResourceNotFoundException {
        Hr hr = hrRepository.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("HR not found with username: " + username));
        hrRepository.delete(hr);
    }

    public Hr uploadProfilePic(MultipartFile file, String username) throws IOException, ResourceNotFoundException {
        Hr hr = hrRepository.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("HR not found for username: " + username));
        
        logger.info("Uploading profile picture for HR: " + hr.getName());

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            logger.error("Invalid file name");
            throw new RuntimeException("Invalid file name");
        }

        logger.info("Original filename: " + originalFileName);

        // Extract extension safely
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();

        // Validate extension
        if (!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
            logger.error("Extension not approved: " + extension);
            throw new RuntimeException("File Extension " + extension + " not allowed. Allowed Extensions: "
                    + List.of("jpg", "jpeg", "png", "gif", "svg"));
        }

        logger.info("Extension approved: " + extension);

        // Validate size
        long kbs = file.getSize() / 1024;
        if (kbs > 6000) {
            logger.error("File size too large: " + kbs + " KB");
            throw new RuntimeException("Image Oversized. Max allowed size is 6000 KB. Provided size: " + kbs + " KB");
        }

        logger.info("Profile Image Size: " + kbs + " KB");

        // Prepare upload folder
        String uploadFolder = "C:\\Users\\GOPALAKANNAN_N\\asset-sphere-ui\\public\\images";
        Files.createDirectories(Path.of(uploadFolder));
        logger.info("Directory ready: " + uploadFolder);

        // Save the file
        Path path = Paths.get(uploadFolder, originalFileName); // ✅ Removed the wrong "\\" entry
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Save the image filename in the database
        hr.setImageUrl(originalFileName);
        return hrRepository.save(hr);
    }

	public Hr getHRInfo(String username) throws ResourceNotFoundException {
	    return hrRepository.findByUserUsername(username)
	            .orElseThrow(() -> new ResourceNotFoundException("HR not found with username: " + username));
	}

	public HRDashboardStatsDto getDashboardStats(String username) throws ResourceNotFoundException {
	    Hr hr = getHRInfo(username); 

	    HRDashboardStatsDto dto = new HRDashboardStatsDto();

	    List<String> labels = List.of(
	        "Assets Available",
	        "Asset Requests",
	        "Assets Allocated",
	        "Service Requests",
	        "Liquid Asset Requests",
	        "Total Employees"
	    );

	    int assetsAvailable = assetRepo.countAvailableAssets();
	    int assetRequests = assetRequestRepo.countByStatus(RequestStatus.PENDING);
	    int allocatedAssets = allocationRepo.countAll();
	    int serviceRequests = serviceRequestRepo.countAll();
	    int liquidAssets = liquidAssetRepo.countAll();
	    int totalEmployees = employeeRepo.countAll();

	    System.out.println("Assets: " + assetsAvailable);
	    System.out.println("Requests: " + assetRequests);
	    System.out.println("Allocated: " + allocatedAssets);

	    List<Integer> counts = List.of(
	        assetsAvailable,
	        assetRequests,
	        allocatedAssets,
	        serviceRequests,
	        liquidAssets,
	        totalEmployees
	    );

	    dto.setCategories(labels);
	    dto.setCounts(counts);

	    return dto;
	}
}
