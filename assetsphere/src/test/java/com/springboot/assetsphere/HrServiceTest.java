package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.HRDashboardStatsDto;
import com.springboot.assetsphere.dto.HrDTO;
import com.springboot.assetsphere.enums.Gender;
import com.springboot.assetsphere.enums.JobTitle;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.*;
import com.springboot.assetsphere.service.HRService;
import com.springboot.assetsphere.service.UserService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HrServiceTest {

    @Mock private HrRepository hrRepository;
    @Mock private HrDTO hrDTO; // Mock the DTO converter
    @Mock private UserService userService;
    @Mock private MultipartFile multipartFile;

    // Mocks for Dashboard Stats
    @Mock private AssetRepository assetRepo;
    @Mock private AssetRequestRepository assetRequestRepo;
    @Mock private AssetAllocationRepository allocationRepo;
    @Mock private ServiceRequestRepository serviceRequestRepo;
    @Mock private LiquidAssetRequestRepository liquidAssetRepo;
    @Mock private EmployeeRepository employeeRepo;

    @InjectMocks
    private HRService hrService;

    private AutoCloseable closeable;
    private MockedStatic<Files> mockedFiles; 

    private User user;
    private Hr hr;
    private HrDTO hrDtoResult;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockedFiles = Mockito.mockStatic(Files.class); 

        user = new User();
        user.setUsername("hr.test@example.com");
        user.setPassword("password"); 
        user.setRole(Role.HR);

        hr = new Hr();
        hr.setId(1);
        hr.setUser(user);
        hr.setName("HR Manager");
        hr.setJobTitle(JobTitle.HR_MANAGER);
        hr.setCreatedAt(LocalDate.now());

        hrDtoResult = new HrDTO();
        hrDtoResult.setUsername("hr.test@example.com");
        hrDtoResult.setName("HR Manager");
        hrDtoResult.setJobTitle(JobTitle.HR_MANAGER);
    }

  

    // --- 1. Test addHR (Crucial method) ---
    @Test
    void testAddHR_Success_ExistingUser() {
     
        Hr inputHr = new Hr();
        inputHr.setUser(user); // Use the pre-defined user

        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(hrRepository.save(any(Hr.class))).thenReturn(hr); // Return the pre-defined hr

       
        Hr result = hrService.addHR(inputHr);

     
        assertNotNull(result);
        assertEquals(hr.getId(), result.getId());
        assertEquals("hr.test@example.com", result.getUser().getUsername());
        assertEquals(JobTitle.HR_MANAGER, result.getJobTitle());
        assertEquals(LocalDate.now(), result.getCreatedAt());

    }

    @Test
    void testAddHR_Success_NewUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("new.hr@example.com");
        newUser.setPassword("newpass");

        Hr inputHr = new Hr();
        inputHr.setUser(newUser);

        Hr savedNewHr = new Hr();
        savedNewHr.setId(2);
        savedNewHr.setUser(newUser);
        savedNewHr.setName("New HR");
        savedNewHr.setJobTitle(JobTitle.HR_MANAGER);
        savedNewHr.setCreatedAt(LocalDate.now());


        when(userService.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());
        when(userService.signUp(any(User.class))).thenAnswer(invocation -> {
            User signedUpUser = invocation.getArgument(0);
            signedUpUser.setRole(Role.HR); // Simulate setting role in signUp
            return signedUpUser;
        });
        when(hrRepository.save(any(Hr.class))).thenReturn(savedNewHr);

        // Act
        Hr result = hrService.addHR(inputHr);

        // Assert
        assertNotNull(result);
        assertEquals(savedNewHr.getId(), result.getId());
        assertEquals("new.hr@example.com", result.getUser().getUsername());
        assertEquals(Role.HR, result.getUser().getRole()); // Verify role is set
        assertEquals(JobTitle.HR_MANAGER, result.getJobTitle());
        assertEquals(LocalDate.now(), result.getCreatedAt());

    }

    @Test
    void testAddHR_NullUser_ThrowsException() {
        // Arrange
        Hr invalidHr = new Hr();
        invalidHr.setUser(null);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> hrService.addHR(invalidHr));
        assertEquals("User information must be provided", ex.getMessage());
   
    }

    @Test
    void testAddHR_NullUsername_ThrowsException() {
        // Arrange
        User userWithoutUsername = new User();
        userWithoutUsername.setUsername(null);
        Hr invalidHr = new Hr();
        invalidHr.setUser(userWithoutUsername);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> hrService.addHR(invalidHr));
        assertEquals("User information must be provided", ex.getMessage());
   
    }

    // --- 2. Test getHRByUsername (Crucial method for retrieval) ---
    @Test
    void testGetHRByUsername_Success() throws ResourceNotFoundException {
        // Arrange
        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(hr));
        when(hrDTO.convertHrToDto(List.of(hr))).thenReturn(List.of(hrDtoResult));

        // Act
        HrDTO result = hrService.getHRByUsername("hr.test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("hr.test@example.com", result.getUsername());
        assertEquals("HR Manager", result.getName());
        assertEquals(JobTitle.HR_MANAGER, result.getJobTitle());

    }

    @Test
    void testGetHRByUsername_NotFound() {
        // Arrange
        when(hrRepository.findByUserUsername("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                hrService.getHRByUsername("nonexistent@example.com"));

        assertEquals("HR not found with username: nonexistent@example.com", ex.getMessage());
   
    }

    // --- 3. Test updateHRByUsername (Crucial method for updates) ---
    @Test
    void testUpdateHRByUsername_Success_PartialUpdate() throws ResourceNotFoundException {
        // Arrange
        Hr existingHr = new Hr();
        existingHr.setId(1);
        existingHr.setUser(user);
        existingHr.setName("Old Name");
        existingHr.setContactNumber("111");
        existingHr.setAddress("Old Address");

        Hr updatedHrInput = new Hr();
        updatedHrInput.setName("New Name");
        updatedHrInput.setAddress("New Address"); // Only updating name and address

        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(existingHr));
        when(hrRepository.save(any(Hr.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved entity

        // Act
        Hr result = hrService.updateHRByUsername("hr.test@example.com", updatedHrInput);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("New Address", result.getAddress());
        assertEquals("111", result.getContactNumber()); // Should remain unchanged

    }

    @Test
    void testUpdateHRByUsername_NotFound() {
        // Arrange
        when(hrRepository.findByUserUsername("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                hrService.updateHRByUsername("nonexistent@example.com", new Hr()));

        assertEquals("HR not found with username: nonexistent@example.com", ex.getMessage());
       
    }

    // --- 4. Test deleteHRByUsername (Crucial method for deletion) ---
    @Test
    void testDeleteHRByUsername_Success() throws ResourceNotFoundException {
        // Arrange
        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(hr));
        doNothing().when(hrRepository).delete(hr);

        // Act
        hrService.deleteHRByUsername("hr.test@example.com");

    }

    @Test
    void testDeleteHRByUsername_NotFound() {
        // Arrange
        when(hrRepository.findByUserUsername("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                hrService.deleteHRByUsername("nonexistent@example.com"));

        assertEquals("HR not found with username: nonexistent@example.com", ex.getMessage());
        
    }

    // --- 5. Test uploadProfilePic (Method with complex file handling logic) ---
    @Test
    void testUploadProfilePic_Success() throws IOException, ResourceNotFoundException {
        // Arrange
        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(hr));
        when(multipartFile.getOriginalFilename()).thenReturn("profile.jpg");
        when(multipartFile.getSize()).thenReturn(1024L); // 1 KB
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));

        // Mock static Files methods
        mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
        mockedFiles.when(() -> Files.copy(any(java.io.InputStream.class), any(Path.class), any(StandardCopyOption.class)))
                .thenReturn(1024L);

        when(hrRepository.save(any(Hr.class))).thenReturn(hr);

        // Act
        Hr result = hrService.uploadProfilePic(multipartFile, "hr.test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("profile.jpg", result.getImageUrl());
         }

    @Test
    void testUploadProfilePic_HrNotFound() {
        // Arrange
        when(hrRepository.findByUserUsername("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                hrService.uploadProfilePic(multipartFile, "nonexistent@example.com"));

        assertEquals("HR not found for username: nonexistent@example.com", ex.getMessage());
      
    }

    @Test
    void testUploadProfilePic_InvalidFileName() {
        // Arrange
        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(hr));
        when(multipartFile.getOriginalFilename()).thenReturn("profile"); // No dot or extension

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                hrService.uploadProfilePic(multipartFile, "hr.test@example.com"));

        assertTrue(ex.getMessage().contains("Invalid file name"));
        
    }

    @Test
    void testUploadProfilePic_UnsupportedExtension() {
        // Arrange
        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(hr));
        when(multipartFile.getOriginalFilename()).thenReturn("profile.pdf"); // Not an allowed image extension

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                hrService.uploadProfilePic(multipartFile, "hr.test@example.com"));

        assertTrue(ex.getMessage().contains("File Extension pdf not allowed"));
        
    }

    @Test
    void testUploadProfilePic_Oversize() {
        
        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(hr));
        when(multipartFile.getOriginalFilename()).thenReturn("profile.png");
        when(multipartFile.getSize()).thenReturn(7_000_000L); // 7MB > 6MB limit

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                hrService.uploadProfilePic(multipartFile, "hr.test@example.com"));

        assertTrue(ex.getMessage().contains("Image Oversized. Max allowed size is 6000 KB"));
       
    }

    @Test
    void testGetDashboardStats_Success() throws ResourceNotFoundException {
        
        when(hrRepository.findByUserUsername("hr.test@example.com")).thenReturn(Optional.of(hr));
        when(assetRepo.countAvailableAssets()).thenReturn(100);
        when(assetRequestRepo.countByStatus(RequestStatus.PENDING)).thenReturn(10);
        when(allocationRepo.countAll()).thenReturn(50);
        when(serviceRequestRepo.countAll()).thenReturn(20);
        when(liquidAssetRepo.countAll()).thenReturn(5);
        when(employeeRepo.countAll()).thenReturn(200);

        
        HRDashboardStatsDto result = hrService.getDashboardStats("hr.test@example.com");

        assertNotNull(result);
        assertEquals(List.of(
                "Assets Available",
                "Asset Requests",
                "Assets Allocated",
                "Service Requests",
                "Liquid Asset Requests",
                "Total Employees"
        ), result.getCategories());
        assertEquals(List.of(100, 10, 50, 20, 5, 200), result.getCounts());

    }

    @Test
    void testGetDashboardStats_HrNotFound() {
        
        when(hrRepository.findByUserUsername("unknown@example.com")).thenReturn(Optional.empty());

        
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                hrService.getDashboardStats("unknown@example.com"));

        assertEquals("HR not found with username: unknown@example.com", ex.getMessage());
        
    }
    
    
    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
        if (mockedFiles != null) {
            mockedFiles.close(); // Close the static mock
        }
        user = null;
        hr = null;
        hrDtoResult = null;
    }
}