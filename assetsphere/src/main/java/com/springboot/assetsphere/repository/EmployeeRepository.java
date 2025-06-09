package com.springboot.assetsphere.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE e.user.username = ?1")
    Page<Employee> findByUsername(String username, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.user.email = ?1")
    Page<Employee> findByEmail(String email, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.jobTitle = ?1")
    Page<Employee> findByJobTitle(String jobTitle, Pageable pageable);

    Optional<Employee> findByUserUsername(String username);
}