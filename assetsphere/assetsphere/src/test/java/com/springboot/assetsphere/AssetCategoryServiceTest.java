package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.repository.AssetCategoryRepository;
import com.springboot.assetsphere.service.AssetCategoryService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssetCategoryServiceTest {

    @Mock
    private AssetCategoryRepository assetCategoryRepo;

    @InjectMocks
    private AssetCategoryService assetCategoryService;

    private AssetCategory category1;
    private AssetCategory category2;

    @BeforeEach // before every test these objects will be created at locations in HEAP
    public void init() {
        MockitoAnnotations.openMocks(this);

        category1 = new AssetCategory();
        category1.setId(1);
        category1.setName("Electronics");
        System.out.println("Category 1 created: " + category1);

        category2 = new AssetCategory();
        category2.setId(2);
        category2.setName("Furniture");
        System.out.println("Category 2 created: " + category2);
    }

   

    @Test
    public void testAddCategory() {
        assetCategoryService.addCategory(category1);
        verify(assetCategoryRepo, times(1)).save(category1);
    }

    @Test
    public void testGetAllCategory() {
        List<AssetCategory> categoryList = Arrays.asList(category1, category2);
        when(assetCategoryRepo.findAll()).thenReturn(categoryList);

        List<AssetCategory> result = assetCategoryService.getAllCategory();
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    public void testGetCategoryById_Found() throws ResourceNotFoundException {
        when(assetCategoryRepo.findById(1)).thenReturn(Optional.of(category1));
        AssetCategory result = assetCategoryService.getCategoryById(1);
        assertEquals(category1, result);
    }

    @Test
    public void testGetCategoryById_NotFound() {
        when(assetCategoryRepo.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            assetCategoryService.getCategoryById(999);
        });

        assertEquals("Category Not Found, Id Given is Invalid!", ex.getMessage());
    }

    @Test
    public void testGetCategoryByName() {
        List<AssetCategory> categoryList = Arrays.asList(category1);
        when(assetCategoryRepo.findByName("Electronics")).thenReturn(categoryList);

        List<AssetCategory> result = assetCategoryService.getCategoryByName("Electronics");
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }
    
    @AfterEach // After each test case, the objects will be nullified and memory will be free
    public void destroy() {
        category1 = null;
        category2 = null;
        System.out.println("AssetCategory objects released");
    }
}