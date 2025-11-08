package com.acadian.crud.crud.controller;

import com.acadian.crud.crud.entity.Employee;
import com.acadian.crud.crud.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EmployeeControllerTest {

    private EmployeeService employeeService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Employee emp1;
    private Employee emp2;

    @BeforeEach
    void setUp() {
        employeeService = Mockito.mock(EmployeeService.class);
        EmployeeController controller = new EmployeeController(employeeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        emp1 = new Employee("Alice", "alice@example.com");
        emp1.setId(1L);
        emp2 = new Employee("Bob", "bob@example.com");
        emp2.setId(2L);
    }

    @Test
    void createEmployee_returnsCreated() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(emp1);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Employee("Alice", "alice@example.com"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));

        verify(employeeService).createEmployee(any(Employee.class));
    }

    @Test
    void getAllEmployees_returnsList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(emp1, emp2));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(employeeService).getAllEmployees();
    }

    @Test
    void getEmployeeById_found_returnsOk() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(emp1));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));

        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_notFound_returns404() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound());

        verify(employeeService).getEmployeeById(99L);
    }

    @Test
    void updateEmployee_found_returnsOk() throws Exception {
        Employee updated = new Employee("Alice Updated", "alice.new@example.com");
        updated.setId(1L);
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));

        verify(employeeService).updateEmployee(eq(1L), any(Employee.class));
    }

    @Test
    void updateEmployee_notFound_returns404() throws Exception {
        when(employeeService.updateEmployee(eq(99L), any(Employee.class))).thenThrow(new RuntimeException("Employee not found"));

        mockMvc.perform(put("/api/employees/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Employee())))
                .andExpect(status().isNotFound());

        verify(employeeService).updateEmployee(eq(99L), any(Employee.class));
    }

    @Test
    void deleteEmployee_found_returnsNoContent() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(emp1));
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService).getEmployeeById(1L);
        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void deleteEmployee_notFound_returns404() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/employees/99"))
                .andExpect(status().isNotFound());

        verify(employeeService).getEmployeeById(99L);
        verify(employeeService, never()).deleteEmployee(anyLong());
    }
}
