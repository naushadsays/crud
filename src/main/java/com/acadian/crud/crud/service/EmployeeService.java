package com.acadian.crud.crud.service;

import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.exception.EmployeeNotFoundException;
import com.acadian.crud.crud.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCacheService cache;

    public Mono<Employee> createEmployee(Employee employee) {
        log.info("Creating employee: {}", employee);
        return employeeRepository.save(employee)
                .doOnSuccess(e -> log.info("Employee Created Successfully"))
                .flatMap(cache::put)                    //cache::put is shorthand for e -> cache.put(e).
                .doOnError(e -> log.error("Error creating employee: {}", e.getMessage()));
        //.flatMap(employee1 -> cache.put(employee1));  //
    }

    public Mono<Employee> getEmployeeById(Long id) {
        log.info("Getting employee by id: {}", id);
        return cache.get(id)
                .doOnNext(e -> log.info("Employee found in cache: {}", e))
                .switchIfEmpty(employeeRepository.findById(id)
                        .doOnNext(e -> log.info("Employee fetched from DB: {}", e))
                        .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)))
                        .flatMap(cache::put))
                .doOnError(err -> log.error("Error fetching employee with id {}", id, err));
    }

    public Flux<Employee> getAllEmployees() {
        log.info("Fetching all employees from DB");
        return employeeRepository.findAll()
                .doOnComplete(() -> log.info("Fetched all employees successfully"))
                .doOnError(e -> log.error("Error fetching all employees: {}", e.getMessage()));
    }

    public Mono<Employee> updateEmployee(Long id, Employee employee) {
        log.info("Updating employee with id: {}", id);
        Mono<Employee> emp = employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)));
        return emp.flatMap(e -> {
                    e.setName(employee.getName());
                    e.setEmail(employee.getEmail());
                    e.setDepartment(employee.getDepartment());
                    return employeeRepository.save(e);
                }).flatMap(cache::put)
                .doOnSuccess(e -> log.info("Employee updated successfully: {}", e))
                .doOnError(err -> log.error("Error updating employee with id {}", id, err));
    }

    public Mono<Void> deleteEmployee(Long id) {
        log.warn("Deleting employee with id: {}", id);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)))
                .flatMap(e -> employeeRepository.deleteById(id)
                        .then(cache.delete(id)))
                .doOnSuccess(v -> log.info("Employee deleted successfully with id: {}", id))
                .doOnError(err -> log.error("Error deleting employee with id {}", id, err));
    }
}
