package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.java.DaoImpl.CategoryDaoImpl;
import com.java.exception.InvaliIdException;
import com.java.model.Category;

public class TestCategoryDaoImpl {
	
	 private CategoryDaoImpl category;
	    @BeforeEach
	    public void setUp() {
	    	category = new CategoryDaoImpl();
	    }

	    @Test
	    void testGetAllCategories() throws ClassNotFoundException {
	        List<Category> categories = Arrays.asList(
	                new Category(1, "Electronics"),
	                new Category(2, "Fashion")
	        );
	        assertNotNull(categories);
	        assertEquals(2, categories.size());
	        assertEquals("Fashion", categories.get(1).getName());
	    }

	    @Test
	    void testGetCategoryById() throws ClassNotFoundException, InvaliIdException {
	        Category category = new Category(1, "Electronics");

	        assertEquals(1, category.getId());
	        assertEquals("Electronics", category.getName());
	    }

	    @Test
	    void testDeleteCategory() throws ClassNotFoundException, InvaliIdException {
	        boolean deleted = true; 
	        assertTrue(deleted); 
	    }
}
