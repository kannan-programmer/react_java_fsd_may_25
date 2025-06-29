package com.springboot.assetsphere;

import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.UserRepository;
import com.springboot.assetsphere.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeeRepository employeeRepo;

    private AutoCloseable closeable;

    private User user;
    private Employee employee;

    @BeforeEach
    void init() {
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
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User savedUser = userService.signUp(user);

        assertNotNull(savedUser);
        assertEquals(encoded, savedUser.getPassword());
        assertEquals(LocalDate.now(), savedUser.getCreatedAt());
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = List.of(user);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("gopal", result.get(0).getUsername());
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.getByUsername("gopal")).thenReturn(user);

        User found = userService.getUserByUsername("gopal");

        assertNotNull(found);
        assertEquals("gopal", found.getUsername());
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
    void testGetUserInfo_UserNotFound() {
        when(userRepository.getByUsername("unknown")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserInfo("unknown"));

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @AfterEach
    void cleanup() throws Exception {
        closeable.close();
        user = null;
        employee = null;
        System.out.println("All test objects cleared");
    }
}
