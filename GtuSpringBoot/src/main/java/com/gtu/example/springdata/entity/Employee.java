package com.gtu.example.springdata.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.springframework.context.annotation.Profile;
//import org.springframework.data.annotation.Id;//誤用此Id
import javax.persistence.Id;//這才對
//import org.springframework.data.mongodb.core.mapping.Document;

@Profile("spring-data")
@Entity
// @Document // for MongoDB
public class Employee {

    private @Id @GeneratedValue Long id;
    private String firstName, lastName, description;

    private Employee() {
    }

    public Employee(String firstName, String lastName, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }
}
