package com.acadian.crud.crud.service;

import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    // Constructor injection
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create
    public Employee createEmployee(Employee employee) {
        logger.info("Creating employee: {}", employee.getName());
        return employeeRepository.save(employee);
    }

    // Read all
    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    // Read by id
    @Cacheable(value = "employees", key = "#id")
    public Optional<Employee> getEmployeeById(Long id) {
        logger.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id);
    }

    // Update
    @CachePut(value = "employees", key = "#employee.id")
    public Employee updateEmployee(Employee employee) {
        logger.warn("Updating employee with id: {}", employee.getId());
        Optional<Employee> existing = employeeRepository.findById(employee.getId());

        if (existing.isPresent()) {
            Employee emp = existing.get();
            emp.setName(employee.getName());
            emp.setEmail(employee.getEmail());
            Employee saved = employeeRepository.save(emp);
            logger.warn("Successfully updated employee with id: {}", saved.getId());
            return saved;
        } else {
            logger.error("Employee not found with id: {}", employee.getId());
            throw new RuntimeException("Employee not found with id: " + employee.getId());
        }
    }

    // Delete
    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(Long id) {
        logger.warn("Deleting employee with id: {}", id);
        employeeRepository.deleteById(id);
    }

}
