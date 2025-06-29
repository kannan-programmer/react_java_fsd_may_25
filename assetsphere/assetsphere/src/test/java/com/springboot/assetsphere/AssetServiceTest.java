package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.AssetDTO;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.repository.AssetCategoryRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.service.AssetService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepo;
    @Mock
    private AssetCategoryRepository assetCategoryRepo;

    @Mock
    private AssetDTO assetDto;

    private AssetService assetService;

    private AssetCategory category;
    private Asset asset;
    private List<Asset> assetList;

    private AutoCloseable closeable;

    private MockedStatic<Files> mockedFiles;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        assetService = new AssetService(assetRepo, assetCategoryRepo, assetDto);

        mockedFiles = mockStatic(Files.class);

        category = new AssetCategory();
        category.setId(1);
        category.setName("Electronics");

        asset = new Asset();
        asset.setId(101);
        asset.setAssetNo("A101");
        asset.setAssetName("Laptop");
        asset.setModel("HP Elitebook");
        asset.setAssetValue(60000.00);
        asset.setExpiryDate(LocalDate.now().plusYears(2));
        asset.setManufacturingDate(LocalDate.now().minusYears(1));
        asset.setCreatedAt(LocalDate.now());
        asset.setImageUrl("hp.png");
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setCategory(category);

        assetList = List.of(asset);
    }

    @Test
    public void testAddAssetBatch_Success() throws ResourceNotFoundException, AssetNotFoundException {
        Asset newAsset = new Asset();
        newAsset.setAssetNo("A102");
        newAsset.setAssetName("Monitor");
        newAsset.setModel("Dell UltraSharp");
        newAsset.setAssetValue(25000.00);
        List<Asset> assetsToAdd = List.of(newAsset);

        when(assetCategoryRepo.findById(category.getId())).thenReturn(Optional.of(category));
        when(assetRepo.saveAll(anyList())).thenAnswer(invocation -> {
            List<Asset> savedList = invocation.getArgument(0);
            savedList.forEach(a -> {
                a.setId(200);
                a.setCreatedAt(LocalDate.now());
                a.setCategory(category);
            });
            return savedList;
        });

        assetService.addAssetBatch(assetsToAdd, category.getId());

        assetsToAdd.forEach(a -> {
            assertEquals(category, a.getCategory());
            assertNotNull(a.getCreatedAt());
        });
    }

    @Test
    public void testAddAssetBatch_CategoryNotFound() {
        int invalidCategoryId = 99;
        when(assetCategoryRepo.findById(invalidCategoryId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(AssetNotFoundException.class, () ->
                assetService.addAssetBatch(assetList, invalidCategoryId)
        );
        assertEquals("Category Not Found, Id Given is Invalid!", ex.getMessage());
    }

    @Test
    public void testAddAssetBatch_EmptyAssetList() {
        List<Asset> emptyList = Collections.emptyList();
        when(assetCategoryRepo.findById(category.getId())).thenReturn(Optional.of(category));

        Exception ex = assertThrows(AssetNotFoundException.class, () ->
                assetService.addAssetBatch(emptyList, category.getId())
        );
        assertEquals("Asset list is empty", ex.getMessage());
    }

    @Test
    public void testGetAllAssets_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Asset> page = new PageImpl<>(assetList, pageable, assetList.size());

        when(assetRepo.findAll(pageable)).thenReturn(page);
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of(createExpectedAssetDTO(asset)));

        List<AssetDTO> result = assetService.getAllAsset(0, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(asset.getAssetName(), result.get(0).getAssetName());
        assertEquals(asset.getCategory().getName(), result.get(0).getCategoryName());
    }

    @Test
    public void testGetAllAssets_EmptyList() {
        List<Asset> emptyList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Asset> emptyPage = new PageImpl<>(emptyList, pageable, 0);

        when(assetRepo.findAll(pageable)).thenReturn(emptyPage);
        when(assetDto.convertAssetToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetDTO> result = assetService.getAllAsset(0, 5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAssetById_Success() throws ResourceNotFoundException, AssetNotFoundException {
        when(assetRepo.findById(asset.getId())).thenReturn(Optional.of(asset));

        Asset result = assetService.getAssetById(asset.getId());

        assertNotNull(result);
        assertEquals(asset.getAssetName(), result.getAssetName());
        assertEquals(asset.getId(), result.getId());
    }

    @Test
    public void testGetAssetById_NotFound() {
        int invalidAssetId = 999;
        when(assetRepo.findById(invalidAssetId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(AssetNotFoundException.class, () ->
                assetService.getAssetById(invalidAssetId)
        );
        assertEquals("Asset not found", ex.getMessage());
    }

    @Test
    public void testGetByAssetName_Success() {
        List<Asset> foundAssets = List.of(asset);
        when(assetRepo.findByAssetName("Laptop")).thenReturn(foundAssets);
        when(assetDto.convertAssetToDto(foundAssets)).thenReturn(List.of(createExpectedAssetDTO(asset)));

        List<AssetDTO> result = assetService.getByAssetName("Laptop");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getAssetName());
    }

    @Test
    public void testGetByAssetName_NoAssetsFound() {
        List<Asset> emptyList = Collections.emptyList();
        when(assetRepo.findByAssetName("NonExistentAsset")).thenReturn(emptyList);
        when(assetDto.convertAssetToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetDTO> result = assetService.getByAssetName("NonExistentAsset");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByModel_Success() {
        List<Asset> foundAssets = List.of(asset);
        when(assetRepo.findByModel("HP Elitebook")).thenReturn(foundAssets);
        when(assetDto.convertAssetToDto(foundAssets)).thenReturn(List.of(createExpectedAssetDTO(asset)));

        List<AssetDTO> result = assetService.getByModel("HP Elitebook");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("HP Elitebook", result.get(0).getModel());
    }

    @Test
    public void testGetByStatus_Success() {
        List<Asset> foundAssets = List.of(asset);
        when(assetRepo.findByStatus(AssetStatus.AVAILABLE)).thenReturn(foundAssets);
        when(assetDto.convertAssetToDto(foundAssets)).thenReturn(List.of(createExpectedAssetDTO(asset)));

        List<AssetDTO> result = assetService.getByStatus("AVAILABLE");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AssetStatus.AVAILABLE, result.get(0).getStatus());
    }

    @Test
    public void testGetByStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                assetService.getByStatus("INVALID_STATUS"));

        assertEquals("No enum constant com.springboot.assetsphere.enums.AssetStatus.INVALID_STATUS", ex.getMessage());
    }

    @Test
    public void testGetByAssetValue_Success() {
        List<Asset> foundAssets = List.of(asset);
        when(assetRepo.findByAssetValue(60000.00)).thenReturn(foundAssets);
        when(assetDto.convertAssetToDto(foundAssets)).thenReturn(List.of(createExpectedAssetDTO(asset)));

        List<AssetDTO> result = assetService.getByAssetValue(60000.00);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(60000.00, result.get(0).getAssetValue());
    }

    @Test
    public void testGetByExpiryDate_Success() {
        List<Asset> foundAssets = List.of(asset);
        LocalDate expiryDate = LocalDate.now().plusYears(2);
        when(assetRepo.findByExpiryDate(expiryDate)).thenReturn(foundAssets);
        when(assetDto.convertAssetToDto(foundAssets)).thenReturn(List.of(createExpectedAssetDTO(asset)));

        List<AssetDTO> result = assetService.getByExpiryDate(expiryDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expiryDate, result.get(0).getExpiryDate());
    }

    @Test
    public void testGetByCategoryName_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Asset> page = new PageImpl<>(assetList, pageable, assetList.size());

        when(assetRepo.findByCategoryName("Electronics", pageable)).thenReturn(page);
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of(createExpectedAssetDTO(asset)));

        List<AssetDTO> result = assetService.getByCategoryName("Electronics", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategoryName());
    }

    @Test
    public void testAddAssetWithImage_Success() throws IOException, ResourceNotFoundException {
        MultipartFile mockFile = mock(MultipartFile.class);
        String originalFileName = "myimage.png";
        byte[] fileContent = "test image content".getBytes();
        Path expectedPath = Paths.get("C:\\Users\\GOPALAKANNAN_N\\asset-sphere-ui\\public\\images", asset.getAssetNo() + ".png");

        when(mockFile.getOriginalFilename()).thenReturn(originalFileName);
        when(mockFile.getSize()).thenReturn((long) fileContent.length);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        when(assetCategoryRepo.findById(category.getId())).thenReturn(Optional.of(category));
        when(assetRepo.save(any(Asset.class))).thenAnswer(invocation -> {
            Asset savedAsset = invocation.getArgument(0);
            savedAsset.setId(102);
            savedAsset.setCreatedAt(LocalDate.now());
            return savedAsset;
        });

        Asset assetInput = new Asset();
        assetInput.setAssetNo("A101");
        assetInput.setAssetName("Test Asset");
        assetInput.setModel("Test Model");
        assetInput.setAssetValue(1000.0);

        Asset savedAsset = assetService.addAssetWithImage(assetInput, category.getId(), mockFile);

        assertNotNull(savedAsset);
        assertEquals(102, savedAsset.getId());
        assertEquals("A101.png", savedAsset.getImageUrl());
        assertEquals(category, savedAsset.getCategory());
        assertNotNull(savedAsset.getCreatedAt());
    }

    @Test
    public void testAddAssetWithImage_CategoryNotFound() {
        MultipartFile mockFile = mock(MultipartFile.class);
        int invalidCategoryId = 99;
        when(assetCategoryRepo.findById(invalidCategoryId)).thenReturn(Optional.empty());

        Asset assetInput = new Asset();
        assetInput.setAssetNo("A999");

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
                assetService.addAssetWithImage(assetInput, invalidCategoryId, mockFile)
        );
        assertEquals("Category not found with ID: " + invalidCategoryId, ex.getMessage());
    }

    @Test
    public void testAddAssetWithImage_InvalidFileName() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("myimage");

        when(assetCategoryRepo.findById(category.getId())).thenReturn(Optional.of(category));

        Asset assetInput = new Asset();
        assetInput.setAssetNo("A101");

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                assetService.addAssetWithImage(assetInput, category.getId(), mockFile)
        );
        assertEquals("Invalid file name", ex.getMessage());
    }

    @Test
    public void testAddAssetWithImage_DisallowedExtension() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("myimage.txt");
        when(mockFile.getSize()).thenReturn(100L);
        try {
            when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test".getBytes()));
        } catch (IOException e) {
            fail("IOException during mock setup: " + e.getMessage());
        }

        when(assetCategoryRepo.findById(category.getId())).thenReturn(Optional.of(category));

        Asset assetInput = new Asset();
        assetInput.setAssetNo("A101");

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                assetService.addAssetWithImage(assetInput, category.getId(), mockFile)
        );
        assertTrue(ex.getMessage().contains("File Extension txt not allowed. Allowed: [jpg, jpeg, png, gif, svg, webp]"));
    }

    @Test
    public void testAddAssetWithImage_FileTooLarge() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("myimage.png");
        when(mockFile.getSize()).thenReturn(6001 * 1024L);
        try {
            when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[6001 * 1024]));
        } catch (IOException e) {
            fail("IOException during mock setup: " + e.getMessage());
        }

        when(assetCategoryRepo.findById(category.getId())).thenReturn(Optional.of(category));

        Asset assetInput = new Asset();
        assetInput.setAssetNo("A101");

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                assetService.addAssetWithImage(assetInput, category.getId(), mockFile)
        );
        assertTrue(ex.getMessage().contains("Image Oversized. Max allowed size is 6000 KB. Provided: 6001 KB"));
    }

    @Test
    public void testUpdateAsset_Success() throws ResourceNotFoundException, AssetNotFoundException {
        Asset existingAsset = new Asset();
        existingAsset.setId(asset.getId());
        existingAsset.setAssetName("Original Name");
        existingAsset.setModel("Original Model");
        existingAsset.setAssetValue(50000.00);
        existingAsset.setStatus(AssetStatus.AVAILABLE);
        existingAsset.setImageUrl("original.png");
        existingAsset.setManufacturingDate(LocalDate.of(2020, 1, 1));
        existingAsset.setExpiryDate(LocalDate.of(2025, 1, 1));

        Asset updatedAssetInput = new Asset();
        updatedAssetInput.setAssetName("Updated Laptop Name");
        updatedAssetInput.setModel("Dell XPS");
        updatedAssetInput.setAssetValue(70000.00);
        updatedAssetInput.setStatus(AssetStatus.IN_SERVICE);
        updatedAssetInput.setImageUrl("updated.png");
        updatedAssetInput.setManufacturingDate(LocalDate.of(2021, 5, 10));
        updatedAssetInput.setExpiryDate(LocalDate.of(2026, 5, 10));

        when(assetRepo.findById(existingAsset.getId())).thenReturn(Optional.of(existingAsset));
        when(assetRepo.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Asset result = assetService.updateAsset(existingAsset.getId(), updatedAssetInput);

        assertNotNull(result);
        assertEquals(updatedAssetInput.getAssetName(), result.getAssetName());
        assertEquals(updatedAssetInput.getModel(), result.getModel());
        assertEquals(updatedAssetInput.getAssetValue(), result.getAssetValue());
        assertEquals(updatedAssetInput.getStatus(), result.getStatus());
        assertEquals(updatedAssetInput.getImageUrl(), result.getImageUrl());
        assertEquals(updatedAssetInput.getManufacturingDate(), result.getManufacturingDate());
        assertEquals(updatedAssetInput.getExpiryDate(), result.getExpiryDate());
    }

    @Test
    public void testUpdateAsset_NotFound() {
        int invalidAssetId = 999;
        Asset updatedAsset = new Asset();
        when(assetRepo.findById(invalidAssetId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(AssetNotFoundException.class, () ->
                assetService.updateAsset(invalidAssetId, updatedAsset)
        );
        assertEquals("Asset not found with ID: " + invalidAssetId, ex.getMessage());
    }

    @Test
    public void testDeleteAsset_Success() throws ResourceNotFoundException, AssetNotFoundException {
        when(assetRepo.findById(asset.getId())).thenReturn(Optional.of(asset));
        doNothing().when(assetRepo).delete(asset);

        assetService.deleteAsset(asset.getId());
    }

    @Test
    public void testDeleteAsset_NotFound() {
        int invalidAssetId = 999;
        when(assetRepo.findById(invalidAssetId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(AssetNotFoundException.class, () ->
                assetService.deleteAsset(invalidAssetId)
        );
        assertEquals("Asset not found with ID: " + invalidAssetId, ex.getMessage());
    }

    private AssetDTO createExpectedAssetDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setAssetId(asset.getId());
        dto.setAssetNo(asset.getAssetNo());
        dto.setAssetName(asset.getAssetName());
        dto.setModel(asset.getModel());
        dto.setManufacturingDate(asset.getManufacturingDate());
        dto.setExpiryDate(asset.getExpiryDate());
        dto.setAssetValue(asset.getAssetValue());
        dto.setStatus(asset.getStatus());
        dto.setImageUrl(asset.getImageUrl());
        if (asset.getCategory() != null) {
            dto.setCategoryName(asset.getCategory().getName());
        } else {
            dto.setCategoryName(null);
        }
        return dto;
    }

    @AfterEach
    public void teardown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
        if (mockedFiles != null) {
            mockedFiles.close();
        }
        assetRepo = null;
        assetCategoryRepo = null;
        assetDto = null;
        assetService = null;
        category = null;
        asset = null;
        assetList = null;
    }
}