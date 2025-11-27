package com.acadian.crud.crud.controller;


import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> createEmployee(@RequestBody Employee employee) {
        log.info("Post/Employees called with employee: {}", employee);
        return employeeService.createEmployee(employee);
    }

    @GetMapping("/{id}")
    public Mono<Employee> getEmployeeById(@PathVariable Long id) {
        log.info("Get/Employees/{} called", id);
        return employeeService.getEmployeeById(id);
    }

    @GetMapping
    public Flux<Employee> getAllEmployees() {
        log.info("Get/Employees called");
        return employeeService.getAllEmployees();
    }

    @PutMapping("/{id}")
    public Mono<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        log.info("Put/Employees/{} called with employee: {}", id, employee);
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable Long id) {
        log.info("Delete/Employees/{} called", id);
        return employeeService.deleteEmployee(id);
    }
}
