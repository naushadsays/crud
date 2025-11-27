package com.acadian.crud.crud.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "employees")
public class Employee {

    @Id
    private Long id;
    private String name;
    private String email;
    private String department;

}

