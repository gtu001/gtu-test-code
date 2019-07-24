package gtu.springdata.jpa.ex1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "EMPLOYEE")
public class Employee {
    @Id
    @JsonProperty("EMPLOYEE_ID")
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private java.math.BigDecimal employeeId;
    @JsonProperty("CREATED_AT")
    @Column(name = "CREATED_AT")
    private java.sql.Timestamp createdAt;
    @JsonProperty("DESCRIPTION")
    @Column(name = "DESCRIPTION")
    private String description;
    @JsonProperty("FIRSTNAME")
    @Column(name = "FIRSTNAME")
    private String firstname;
    @JsonProperty("UPDATED_AT")
    @Column(name = "UPDATED_AT")
    private java.sql.Date updatedAt;
    @JsonProperty("LASTNAME")
    @Column(name = "LASTNAME")
    private String lastname;
    @JsonProperty("TEST_NUM")
    @Column(name = "TEST_NUM")
    private java.math.BigDecimal testNum;

    public java.math.BigDecimal getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(java.math.BigDecimal employeeId) {
        this.employeeId = employeeId;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public java.sql.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.sql.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public java.math.BigDecimal getTestNum() {
        return testNum;
    }

    public void setTestNum(java.math.BigDecimal testNum) {
        this.testNum = testNum;
    }
}