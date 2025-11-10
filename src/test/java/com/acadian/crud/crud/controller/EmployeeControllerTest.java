package com.acadian.crud.crud.controller;

import com.acadian.crud.crud.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerTest {
//test commit in new branch

    //End to end testing new code
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setup() {
        employee1 = new Employee("naushad", "naushad@gmail.com");
        employee2 = new Employee("shamshad", "shamshad@gmail.com");

    }

    //commenting cleanup and adding @Transactional - it will rollback after each test
//    @BeforeEach
//    void cleanUp(@Autowired JdbcTemplate jdbcTemplate) {
//        jdbcTemplate.execute("DELETE FROM employee");
//    }

    @Test
    void createEmployee_returnsCreated() throws Exception {

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("naushad"))
                .andExpect(jsonPath("$.email").value("naushad@gmail.com"));
    }

    @Test
    void creatMltipleEmployees_fetchAll() throws Exception {

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee2)))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name", anyOf(is("naushad"), is("shamshad"))))
                .andExpect(jsonPath("$[1].name", anyOf(is("naushad"), is("shamshad"))));
    }

    @Test
    void createEmployee_fetchById() throws Exception {

        String response1 = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Employee createdEmployee1 = objectMapper.readValue(response1, Employee.class);

        String response2 = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee2)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Employee createdEmployee2 = objectMapper.readValue(response2, Employee.class);


        mockMvc.perform(get("/api/employees/{id}", createdEmployee1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("naushad"));

        mockMvc.perform(get("/api/employees/{id}", createdEmployee2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("shamshad"));
    }

    @Test
    void updateEmployee_returnsUpdatedEmployee() throws Exception {

        Employee created = objectMapper.readValue(
                mockMvc.perform(post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employee1)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                Employee.class
        );

        Employee updatedData = new Employee();
        updatedData.setName("Naushad Updated");
        updatedData.setEmail("naushad.updated@gmail.com");

        Employee updated = objectMapper.readValue(
                mockMvc.perform(put("/api/employees/{id}", created.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedData)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Naushad Updated"))
                        .andExpect(jsonPath("$.email").value("naushad.updated@gmail.com"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                Employee.class
        );

        mockMvc.perform(get("/api/employees/{id}", updated.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Naushad Updated"))
                .andExpect(jsonPath("$.email").value("naushad.updated@gmail.com"));
    }

    @Test
    void deleteEmployee_removesFromDatabase() throws Exception {

        Employee created = objectMapper.readValue(
                mockMvc.perform(post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employee2)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                Employee.class
        );

        Long id = created.getId();

        mockMvc.perform(delete("/api/employees/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/employees/{id}", id))
                .andExpect(status().isNotFound());
    }


}
