package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE e.jobTitle = ?1")
    List<Employee> findByJobTitle(String jobTitle);  

    @Query("SELECT e FROM Employee e WHERE e.user.username = ?1")
    Optional<Employee> findByUserUsername(String username);  

    @Query("SELECT COUNT(e) FROM Employee e")
    int countAll();
}
