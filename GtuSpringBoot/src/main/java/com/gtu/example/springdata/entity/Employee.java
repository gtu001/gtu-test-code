package com.gtu.example.springdata.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
//import org.springframework.data.annotation.Id;//誤用此Id
import javax.persistence.Id;//這才對
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Profile;
//import org.springframework.data.mongodb.core.mapping.Document;

import com.gtu.example.springdata.dao_1.AddressRepository;

import net.minidev.json.annotate.JsonIgnore;

/**
 * @author wistronits
 *
 */
@Profile("spring-data")
@Entity
// @Document // for MongoDB
@Table(name = "employee_1")
public class Employee extends AuditModel {

    @Id
    @GeneratedValue // (strategy = GenerationType.IDENTITY) // hibernate 不支援
    // @SequenceGenerator(//
    // name = "question_generator", //
    // sequenceName = "question_sequence", //
    // initialValue = 1000//
    // )
    @Column(name = "employee_id")
    private Long id;

    @NotBlank
    private String firstName;
    private String lastName;

    @Size(min = 3, max = 100)
    private String description;

    @Transient
    @GtuDBRelation(setter = "setAddress", repository = "AddressRepository", method="")
    private String empAddressId;

    @OneToOne(//
            // mappedBy = "addressId", // 未知 (打開會錯)
            cascade = CascadeType.ALL, //
            orphanRemoval = true, //
            fetch = FetchType.LAZY//
    ) //
    @JoinColumn(name = "empAddressId") // 自己的欄位
    @JsonIgnore
    private Address address;

    @ManyToMany
    @JoinTable(//
            name = "work_item", //
            joinColumns = @JoinColumn(name = "work_job_id"), // 對方的
            inverseJoinColumns = @JoinColumn(name = "emp_work_id")// 對方對回來的
    ) //
    @JsonIgnore
    private List<WorkItem> workItems = new ArrayList<WorkItem>();

    public Employee() {
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

    public List<WorkItem> getWorkItems() {
        return workItems;
    }

    public void setWorkItems(List<WorkItem> workItems) {
        this.workItems = workItems;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmpAddressId() {
        return empAddressId;
    }

    public void setEmpAddressId(String empAddressId) {
        this.empAddressId = empAddressId;
    }

    // custom 用ID設定addressId
    public void setAddressByAddressId(String addressId, AddressRepository repository) {
        Optional<Address> opt = repository.findById(addressId);
        if (opt.isPresent()) {
            Address addr = opt.get();
            this.setAddress(addr);
        }
    }
}
