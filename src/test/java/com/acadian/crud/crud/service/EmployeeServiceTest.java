package com.acadian.crud.crud.service;

import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee emp1;
    private Employee emp2;

    @BeforeEach
    void setUp() {
        emp1 = new Employee("Alice", "alice@example.com");
        emp1.setId(1L);
        emp2 = new Employee("Bob", "bob@example.com");
        emp2.setId(2L);
    }

    @Test
    void createEmployee_savesAndReturns() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(emp1);

        Employee created = employeeService.createEmployee(new Employee("Alice", "alice@example.com"));

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void getAllEmployees_returnsList() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void getEmployeeById_found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp1));

        Optional<Employee> opt = employeeService.getEmployeeById(1L);

        assertTrue(opt.isPresent());
        assertEquals("Alice", opt.get().getName());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getEmployeeById_notFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> opt = employeeService.getEmployeeById(99L);

        assertFalse(opt.isPresent());
        verify(employeeRepository).findById(99L);
    }

    @Test
    void updateEmployee_found_updatesAndReturns() {
        Employee updated = new Employee("Alice Updated", "alice.new@example.com");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp1));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        Employee result = employeeService.updateEmployee(1L, updated);

        assertEquals("Alice Updated", result.getName());
        assertEquals("alice.new@example.com", result.getEmail());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void updateEmployee_notFound_throws() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> employeeService.updateEmployee(99L, new Employee()));
        assertTrue(ex.getMessage().contains("Employee not found"));
        verify(employeeRepository).findById(99L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteEmployee_callsRepository() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }
}

