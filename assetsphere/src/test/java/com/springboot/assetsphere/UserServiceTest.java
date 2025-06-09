package com.springboot.assetsphere;

import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.UserRepository;
import com.springboot.assetsphere.service.UserService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeeRepository employeeRepo;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    private User user;
    private Employee employee;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setUsername("gopal");
        user.setPassword("plaintext");
        user.setRole(Role.EMPLOYEE);

        employee = new Employee();
        employee.setId(100);
        employee.setUser(user);
    }

    

    @Test
    void testSignUp() {
        String encoded = "encodedPassword";

        when(passwordEncoder.encode("plaintext")).thenReturn(encoded);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.signUp(user);

        assertNotNull(result);
        assertEquals(encoded, result.getPassword());
        assertEquals(LocalDate.now(), result.getCreatedAt());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("gopal", result.get(0).getUsername());
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.getByUsername("gopal")).thenReturn(user);

        User result = userService.getUserByUsername("gopal");

        assertNotNull(result);
        assertEquals("gopal", result.getUsername());
    }

    @Test
    void testGetUserById_Success() throws ResourceNotFoundException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("gopal", result.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99));
    }

    @Test
    void testGetUserInfo_AsEmployee() {
        user.setRole(Role.EMPLOYEE);
        when(userRepository.getByUsername("gopal")).thenReturn(user);
        when(employeeRepo.findByUserUsername("gopal")).thenReturn(Optional.of(employee));

        Object result = userService.getUserInfo("gopal");

        assertTrue(result instanceof Optional);
        assertTrue(((Optional<?>) result).isPresent());
        assertEquals(100, ((Employee) ((Optional<?>) result).get()).getId());
    }

    @Test
    void testGetUserInfo_AsHR() {
        user.setRole(Role.HR);
        when(userRepository.getByUsername("gopal")).thenReturn(user);
        when(employeeRepo.findByUserUsername("gopal")).thenReturn(Optional.of(employee));

        Object result = userService.getUserInfo("gopal");

        assertTrue(result instanceof Optional);
        assertTrue(((Optional<?>) result).isPresent());
        assertEquals(employee, ((Optional<?>) result).get());
    }

    @Test
    void testGetUserInfo_UserNotFound() {
        when(userRepository.getByUsername("unknown")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserInfo("unknown"));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void testGetUserInfo_EmployeeNotFound() {
        user.setRole(Role.IT_SUPPORT);
        when(userRepository.getByUsername("gopal")).thenReturn(user);
        when(employeeRepo.findByUserUsername("gopal")).thenReturn(null); // Note: null, not Optional.empty()

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserInfo("gopal"));
        assertTrue(ex.getMessage().contains("Employee not found"));
    }
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}