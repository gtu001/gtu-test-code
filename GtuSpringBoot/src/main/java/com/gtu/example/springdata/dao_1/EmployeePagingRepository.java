package com.gtu.example.springdata.dao_1;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gtu.example.springdata.entity.Employee;

@Profile({ "spring-data", "servers" })
public interface EmployeePagingRepository extends PagingAndSortingRepository<Employee, Long> {

    Employee findByFirstName(String firstName);

    List<Employee> findByLastName(String lastName);

    // 分業處理
    // findAll(new PageRequest(1, 20));
}
