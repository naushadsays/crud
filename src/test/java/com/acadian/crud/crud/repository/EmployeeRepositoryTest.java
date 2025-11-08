package com.acadian.crud.crud.repository;

import com.acadian.crud.crud.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void saveAndFindById() {
        Employee e = new Employee("Alice", "alice@example.com");
        Employee saved = employeeRepository.save(e);

        assertThat(saved.getId()).isNotNull();

        Optional<Employee> opt = employeeRepository.findById(saved.getId());
        assertThat(opt).isPresent();
        assertThat(opt.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findAll_returnsList() {
        employeeRepository.save(new Employee("A", "a@example.com"));
        employeeRepository.save(new Employee("B", "b@example.com"));

        List<Employee> list = employeeRepository.findAll();
        assertThat(list).hasSize(2);
    }

    @Test
    void deleteById_removes() {
        Employee e = employeeRepository.save(new Employee("C", "c@example.com"));
        Long id = e.getId();

        employeeRepository.deleteById(id);
        assertThat(employeeRepository.findById(id)).isEmpty();
    }
}
