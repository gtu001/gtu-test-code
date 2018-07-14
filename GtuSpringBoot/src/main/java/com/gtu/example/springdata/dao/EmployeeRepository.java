
package com.gtu.example.springdata.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Repository;

import com.gtu.example.springdata.entity.Employee;

@Profile("spring-data")
// @Repository//網路說這東西沒有用
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Employee findByFirstName(String firstName);

    List<Employee> findByLastName(String lastName);

    long countBylastName(String lastname);

    long deleteBylastName(String lastname);

    List<Employee> removeBylastName(String lastname);

    @Nullable
    Employee findByDescription(@Nullable String description);
    
    @Query("select u from Employee u")
    Collection<NamesOnly> findAll_nameOnly();

    interface NamesOnly {
        @Value("#{target.firstName + ' ' + target.lastName}")
        String getFullName();
    }
}
