package com.acadian.crud.crud.controller;

import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    // Constructor injection
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        logger.warn("POST/Employee called with name {}", employee.getName());
        Employee created = employeeService.createEmployee(employee);
        logger.warn("Employee created with id {}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("GET/List of Employees called");
        List<Employee> list = employeeService.getAllEmployees();
        logger.info("Number of employees fetched: {}", list.size());
        return ResponseEntity.ok(list);
    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        logger.info("GET/Employee by ID called with id {}", id);
        Optional<Employee> opt = employeeService.getEmployeeById(id);
        logger.info("Employee found: {}", opt.isPresent());
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        try {
            logger.warn("PUT/updating Employee with id {}", id);
            Employee updated = employeeService.updateEmployee(id, employee);
            logger.warn("Successfully updated Employee with id {}", updated.getId());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            logger.error("Error updating Employee with id {}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.warn("DELETE/Employee called with id {}", id);
        Optional<Employee> opt = employeeService.getEmployeeById(id);
        if (opt.isPresent()) {
            employeeService.deleteEmployee(id);
            logger.warn("Successfully deleted Employee with id {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Employee with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}

