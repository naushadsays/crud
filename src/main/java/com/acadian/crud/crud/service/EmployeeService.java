package com.acadian.crud.crud.service;

import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Constructor injection
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Read all
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Read by id
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Update
    public Employee updateEmployee(Long id, Employee updated) {
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
        employeeRepository.deleteById(id);
    }

}
