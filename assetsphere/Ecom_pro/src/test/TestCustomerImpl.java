package test;

import com.java.DaoImpl.CustomerDaoImpl;
import com.java.model.Customer;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCustomerImpl  {
	
	    private CustomerDaoImpl customerDao;

	    @BeforeEach
	    void setUp() {
	        customerDao = new CustomerDaoImpl();
	    }

	    @Test
	    void testInsertCustomerWithValidData() {
	        Customer customer = new Customer(0, "Kannan", "Mumbai");
	        assertDoesNotThrow(() -> customerDao.insertCustomer(customer));
	    }

	    @Test
	    void testInsertCustomerWithNullValues() {
	        Customer customer = new Customer(0, null, null);
	        assertDoesNotThrow(() -> customerDao.insertCustomer(customer)); 
	    }

	    @Test
	    void testGetAllCustomer() {
	        assertDoesNotThrow(() -> {
	            List<Customer> list = customerDao.getAllCustomer();
	            assertNotNull(list);
	        });
	    }

	    @Test
	    void testGetCustomerById() {
	        assertDoesNotThrow(() -> {
	            Customer c = customerDao.getById(1); 
	            if (c != null) {
	                assertEquals(1, c.getId());
	            }
	        });
	    }

	    @Test
	    void testGetCustomerByInvalidId() {
	        assertDoesNotThrow(() -> {
	            Customer c = customerDao.getById(-1); 
	            assertNull(c);
	        });
	    }
	}

