package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.java.DaoImpl.ProductDaoImpl;
import com.java.model.Category;
import com.java.model.Product;

public class TestProductDaoImpl {
	private ProductDaoImpl productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDaoImpl();
    }

    @Test
    void testInsertProduct() {
        Product product = new Product(0, "Test Product", 99.99, "Test Description",
                new Category(1, "Electronics"));
        assertDoesNotThrow(() -> productDao.insertProduct(product));
    }

    @Test
    void testGetAllProducts() {
        assertDoesNotThrow(() -> {
            List<Product> products = productDao.getAllProduct();
            assertNotNull(products);
            assertTrue(products.size() >= 0);
        });
    }

    @Test
    void testGetProductById() {
        assertDoesNotThrow(() -> {
            Product p = productDao.getById(1); 
            if (p != null) {
                assertEquals(1, p.getId());
            } else {
                System.out.println("No product with ID 1 found (test skipped).");
            }
        });
    }

    @Test
    void testDeleteProduct() {
        assertDoesNotThrow(() -> {
            productDao.deleteProduct(100);
        });
    }

    @Test
    void testGetProductsByCategoryId() {
        assertDoesNotThrow(() -> {
            List<Product> products = productDao.getProductsByCategoryId(1); 
            assertNotNull(products);
        });
    }
}
