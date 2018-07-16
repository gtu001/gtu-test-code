package com.gtu.example.springdata.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
//import org.springframework.data.annotation.Id;//誤用此Id
import javax.persistence.Id;//這才對
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Profile;
//import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Profile("spring-data")
@Entity
// @Document // for MongoDB
public class Employee extends AuditModel {

    @Id
    @GeneratedValue // (strategy = GenerationType.IDENTITY) // hibernate 不支援
    // @SequenceGenerator(//
    // name = "question_generator", //
    // sequenceName = "question_sequence", //
    // initialValue = 1000//
    // )
    private Long id;

    @NotBlank
    private String firstName;
    private String lastName;

    @Size(min = 3, max = 100)
    private String description;

    @OneToMany(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private List<Address> address;

    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "manager_id", nullable = true)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    // @JsonIgnore
    // private Employee manager;

    // @ManyToMany(cascade = { CascadeType.ALL })
    // @JoinTable(name = "A_B", joinColumns = { //
    // @JoinColumn(name = "ID_A", referencedColumnName = "ID_A") }, //
    // inverseJoinColumns = { @JoinColumn(name = "ID_B", referencedColumnName =
    // "ID_B") }//
    // )
    // public List<B> lista = new ArrayList<B>();

    private Employee() {
    }

    public Employee(String firstName, String lastName, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
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

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
