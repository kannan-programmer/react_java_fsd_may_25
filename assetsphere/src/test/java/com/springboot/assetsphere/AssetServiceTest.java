package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.springboot.assetsphere.dto.AssetDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.repository.AssetCategoryRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.service.AssetService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

@SpringBootTest
public class AssetServiceTest {

    @Mock private AssetRepository assetRepo;
    @Mock private AssetCategoryRepository assetCategoryRepo;
    @Mock private AssetDTO assetDto;

    @InjectMocks
    private AssetService assetService;

    private AssetCategory category;
    private Asset asset;
    private List<Asset> assetList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        category = new AssetCategory();
        category.setId(1);
        category.setName("Electronics");

        asset = new Asset();
        asset.setId(101);
        asset.setAssetName("Laptop");
        asset.setModel("HP Elitebook");
        asset.setAssetValue(60000.00);
        asset.setExpiryDate(LocalDate.now().plusYears(2));
        asset.setCategory(category);
        asset.setCreatedAt(LocalDate.now());

        assetList = List.of(asset);
    }

    @AfterEach
    public void tearDown() {
        asset = null;
        category = null;
        assetList = null;
    }

    @Test
    public void testAddAssetBatch_Success() throws ResourceNotFoundException {
        when(assetCategoryRepo.findById(1)).thenReturn(Optional.of(category));
        when(assetRepo.saveAll(anyList())).thenReturn(assetList);

        assetService.addAssetBatch(assetList, 1);

        verify(assetRepo, times(1)).saveAll(assetList);
    }

    @Test
    public void testAddAssetBatch_EmptyList_ThrowsException() {
        when(assetCategoryRepo.findById(1)).thenReturn(Optional.of(category));

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
            assetService.addAssetBatch(List.of(), 1)
        );

        assertEquals("Asset list is empty", ex.getMessage());
    }

    @Test
    public void testAddAssetBatch_CategoryNotFound() {
        when(assetCategoryRepo.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
            assetService.addAssetBatch(assetList, 999)
        );

        assertEquals("Category Not Found, Id Given is Invalid!", ex.getMessage());
    }

    @Test
    public void testGetAllAsset() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<Asset> page = new PageImpl<>(assetList);

        when(assetRepo.findAll(pageable)).thenReturn(page);
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of());

        List<AssetDTO> result = assetService.getAllAsset(0, 3);
        assertNotNull(result);
    }

    @Test
    public void testGetByAssetName() {
        Pageable pageable = PageRequest.of(0, 2);
        when(assetRepo.findByAssetName("Laptop", pageable)).thenReturn(new PageImpl<>(assetList));
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of());

        List<AssetDTO> result = assetService.getByAssetName("Laptop", 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByModel() {
        Pageable pageable = PageRequest.of(0, 2);
        when(assetRepo.findByModel("HP Elitebook", pageable)).thenReturn(new PageImpl<>(assetList));
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of());

        List<AssetDTO> result = assetService.getByModel("HP Elitebook", 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByAssetValue() {
        Pageable pageable = PageRequest.of(0, 2);
        when(assetRepo.findByAssetValue(60000, pageable)).thenReturn(new PageImpl<>(assetList));
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of());

        List<AssetDTO> result = assetService.getByAssetValue(60000, 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByExpiryDate() {
        LocalDate expiry = LocalDate.now().plusYears(2);
        Pageable pageable = PageRequest.of(0, 2);
        when(assetRepo.findByExpiryDate(expiry, pageable)).thenReturn(new PageImpl<>(assetList));
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of());

        List<AssetDTO> result = assetService.getByExpiryDate(expiry, 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByCategoryName() {
        Pageable pageable = PageRequest.of(0, 2);
        when(assetRepo.findByCategoryName("Electronics", pageable)).thenReturn(new PageImpl<>(assetList));
        when(assetDto.convertAssetToDto(assetList)).thenReturn(List.of());

        List<AssetDTO> result = assetService.getByCategoryName("Electronics", 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetAssetById_Success() throws ResourceNotFoundException {
        when(assetRepo.findById(101)).thenReturn(Optional.of(asset));
        Asset result = assetService.getAssetById(101);
        assertEquals("Laptop", result.getAssetName());
    }

    @Test
    public void testGetAssetById_NotFound() {
        when(assetRepo.findById(404)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
            assetService.getAssetById(404)
        );

        assertEquals("Asset not found", ex.getMessage());
    }
}