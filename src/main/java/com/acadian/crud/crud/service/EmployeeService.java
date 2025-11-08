package com.acadian.crud.crud.service;

import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Optional<Employee> getEmployeeById(Long id) {
        logger.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id);
    }

    // Update
    public Employee updateEmployee(Long id, Employee updated) {
        logger.warn("Updating employee with id: {}", id);
        return employeeRepository.findById(id)
            .map(existing -> {
                existing.setName(updated.getName());
                existing.setEmail(updated.getEmail());
                return employeeRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    // Delete
    public void deleteEmployee(Long id) {
        logger.warn("Deleting employee with id: {}", id);
        employeeRepository.deleteById(id);
    }

}
