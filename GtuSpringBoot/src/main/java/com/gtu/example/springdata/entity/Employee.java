package com.gtu.example.springdata.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
//import org.springframework.data.annotation.Id;//誤用此Id
import javax.persistence.Id;//這才對
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.context.annotation.Profile;
//import org.springframework.data.mongodb.core.mapping.Document;

@Profile("spring-data")
@Entity
// @Document // for MongoDB
public class Employee {

    private @Id @GeneratedValue Long id;
    private String firstName;
    private String lastName;
    private String description;

    private String addressId;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "addressId")
    private List<Address> address;

    private Employee() {
    }

    public Employee(String firstName, String lastName, String description, String addressId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.addressId = addressId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
