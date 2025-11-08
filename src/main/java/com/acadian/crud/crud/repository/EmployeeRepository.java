package com.acadian.crud.crud.repository;

import com.acadian.crud.crud.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{

}
