package com.acadian.crud.crud.repository;

import com.acadian.crud.crud.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {


}
