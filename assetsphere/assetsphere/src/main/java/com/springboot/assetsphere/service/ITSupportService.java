package com.springboot.assetsphere.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.assetsphere.dto.ITSupportDTO;
import com.springboot.assetsphere.enums.JobTitle;
import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.model.ITSupport;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.ITSupportRepository;

import jakarta.transaction.Transactional;

@Service
public class ITSupportService {

	private static final Logger logger = LoggerFactory.getLogger(HRService.class);
    private final ITSupportRepository itSupportRepository;
    private final ITSupportDTO itSupportDTO;
    private UserService userService;

   

    public ITSupportService(ITSupportRepository itSupportRepository, ITSupportDTO itSupportDTO,
			UserService userService) {
		super();
		this.itSupportRepository = itSupportRepository;
		this.itSupportDTO = itSupportDTO;
		this.userService = userService;
	}

    public ITSupport addITSupport(ITSupport itSupport) {
        if (itSupport.getUser() == null || itSupport.getUser().getUsername() == null) {
            throw new RuntimeException("User information must be provided for IT Support");
        }

        User user = itSupport.getUser();
        Optional<User> existingUserOpt = userService.findByUsername(user.getUsername());

        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
        } else {
            user.setRole(Role.IT_SUPPORT); 
            user = userService.signUp(user);
        }

        itSupport.setUser(user);
        itSupport.setJobTitle(JobTitle.IT_SUPPORT_ENGINEER);
        itSupport.setCreatedAt(LocalDate.now());

        return itSupportRepository.save(itSupport);
    }


    public List<ITSupportDTO> getAllITSupport() {
        List<ITSupport> list = itSupportRepository.findAll();
        return itSupportDTO.convertITSupportToDto(list);
    }

    public ITSupportDTO getITSupportByUsername(String username) throws ResourceNotFoundException {
        ITSupport it = itSupportRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("IT Support not found with username: " + username));
        return itSupportDTO.convertITSupportToDto(List.of(it)).get(0);
    }

    public ITSupportDTO getById(int id) throws ResourceNotFoundException {
        ITSupport it = itSupportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IT Support not found with ID: " + id));
        return itSupportDTO.convertITSupportToDto(List.of(it)).get(0);
    }
    
    @Transactional
    public ITSupport updateITSupportByUsername(String username, ITSupport updated) throws ResourceNotFoundException {
        ITSupport it = itSupportRepository.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("IT Support not found with username: " + username));

        if (updated.getName() != null) it.setName(updated.getName());
        if (updated.getAddress() != null) it.setAddress(updated.getAddress());
        if (updated.getContactNumber() != null) it.setContactNumber(updated.getContactNumber());
        if (updated.getImageUrl() != null) it.setImageUrl(updated.getImageUrl());
        if (updated.getGender() != null) it.setGender(updated.getGender());

        return itSupportRepository.save(it);
    }

    @Transactional
    public void deleteITSupportByUsername(String username) throws ResourceNotFoundException {
        ITSupport it = itSupportRepository.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("IT Support not found with username: " + username));
        itSupportRepository.delete(it);
    }

	public ITSupport uploadProfilePic(MultipartFile file, String username) throws ResourceNotFoundException, IOException {
		ITSupport it = itSupportRepository.findByUserUsername(username).orElseThrow(() -> new ResourceNotFoundException("Employee not found for username: " + username));;
        logger.info("This is employee --> " + it.getName());
        /* extension check: jpg,jpeg,png,gif,svg : */
        String originalFileName = file.getOriginalFilename(); // profile_pic.png
        logger.info(originalFileName.getClass().toString());

        logger.info("" + originalFileName.split("\\.").length);
        String extension = originalFileName.split("\\.")[1]; // png
        if (!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
            logger.error("extension not approved " + extension);
            throw new RuntimeException("File Extension " + extension + " not allowed " + "Allowed Extensions"
                    + List.of("jpg", "jpeg", "png", "gif", "svg"));
        }
        logger.info("extension approved " + extension);
        /* Check the file size */
        long kbs = file.getSize() / 1024;
        if (kbs > 3000) {
            logger.error("File oversize " + kbs);
            throw new RuntimeException("Image Oversized. Max allowed size is " + kbs);
        }
        logger.info("Profile Image Size " + kbs + " KBs");

        /* Check if Directory exists, else create one */
        String uploadFolder = "C:\\Users\\GOPALAKANNAN_N\\asset-sphere-ui\\public\\images";
        Files.createDirectories(Path.of(uploadFolder));
        logger.info(Path.of(uploadFolder) + " directory ready!!!");
        /* Define the full path */
        Path path = Paths.get(uploadFolder, "\\", originalFileName);
        /* Upload file in the above path */
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        /* Set url of file or image in author object */
        it.setImageUrl(originalFileName);
        /* Save author Object */
        return itSupportRepository.save(it);
	}
	}


