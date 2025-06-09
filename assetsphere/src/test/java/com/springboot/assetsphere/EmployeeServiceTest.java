package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.EmployeeDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.service.EmployeeService;
import com.springboot.assetsphere.service.UserService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private UserService userService;

    @Mock
    private EmployeeDTO employeeDTOConverter;

    @InjectMocks
    private EmployeeService employeeService;

    private AutoCloseable closeable;

    private User user;
    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("john123");
        user.setEmail("john@example.com");

        employee = new Employee();
        employee.setId(1);
        employee.setUser(user);
        employee.setJobTitle("Software Engineer");
        employee.setCreatedAt(LocalDate.now());

        employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername("john123");
        employeeDTO.setJobTitle("Software Engineer");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddEmployee_Success() {
        when(userService.getUserByUsername("john123")).thenReturn(user);
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.addEmployee(employee);

        assertNotNull(result);
        assertEquals("john123", result.getUser().getUsername());
        verify(employeeRepo).save(employee);
    }

    @Test
    void testAddEmployee_NullUser_ThrowsException() {
        Employee nullUserEmployee = new Employee();
        nullUserEmployee.setUser(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(nullUserEmployee);
        });

        assertEquals("User input or username cannot be null", ex.getMessage());
    }

    @Test
    void testAddEmployee_NullUsername_ThrowsException() {
        User noUsernameUser = new User(); // no username set
        Employee badEmployee = new Employee();
        badEmployee.setUser(noUsernameUser);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(badEmployee);
        });

        assertEquals("User input or username cannot be null", ex.getMessage());
    }

    @Test
    void testAddEmployee_UserNotFound_ThrowsException() {
        when(userService.getUserByUsername("john123")).thenReturn(null);
        Employee emp = new Employee();
        emp.setUser(user);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(emp);
        });

        assertEquals("User does not exist with username: john123", ex.getMessage());
    }

    @Test
    void testGetAllEmployee() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Employee> employees = List.of(employee);
        when(employeeRepo.findAll(pageable)).thenReturn(new PageImpl<>(employees));
        when(employeeDTOConverter.convertEmployeeToDto(employees)).thenReturn(List.of(employeeDTO));

        List<EmployeeDTO> result = employeeService.getAllEmployee(0, 5);

        assertEquals(1, result.size());
        verify(employeeRepo).findAll(pageable);
    }

    @Test
    void testGetEmployeeById_Success() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(employeeDTOConverter.convertEmployeeToDto(List.of(employee))).thenReturn(List.of(employeeDTO));

        EmployeeDTO result = employeeService.getEmployeeById(1);

        assertEquals("john123", result.getUsername());
        verify(employeeRepo).findById(1);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(99);
        });
    }

    @Test
    void testGetEmployeesByEmail() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Employee> employees = List.of(employee);
        when(employeeRepo.findByEmail("john@example.com", pageable)).thenReturn(new PageImpl<>(employees));
        when(employeeDTOConverter.convertEmployeeToDto(employees)).thenReturn(List.of(employeeDTO));

        List<EmployeeDTO> result = employeeService.getEmployeesByEmail("john@example.com", 0, 5);

        assertEquals(1, result.size());
        verify(employeeRepo).findByEmail("john@example.com", pageable);
    }

    @Test
    void testGetEmployeesByUsername() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Employee> employees = List.of(employee);
        when(employeeRepo.findByUsername("john123", pageable)).thenReturn(new PageImpl<>(employees));
        when(employeeDTOConverter.convertEmployeeToDto(employees)).thenReturn(List.of(employeeDTO));

        List<EmployeeDTO> result = employeeService.getEmployeesByUsername("john123", 0, 5);

        assertEquals(1, result.size());
        verify(employeeRepo).findByUsername("john123", pageable);
    }

    @Test
    void testGetEmployeesByJobTitle() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Employee> employees = List.of(employee);
        when(employeeRepo.findByJobTitle("Software Engineer", pageable)).thenReturn(new PageImpl<>(employees));
        when(employeeDTOConverter.convertEmployeeToDto(employees)).thenReturn(List.of(employeeDTO));

        List<EmployeeDTO> result = employeeService.getEmployeesByJobTitle("Software Engineer", 0, 5);

        assertEquals(1, result.size());
        verify(employeeRepo).findByJobTitle("Software Engineer", pageable);
    }
}