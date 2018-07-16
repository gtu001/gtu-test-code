package com.gtu.example.springdata.dao_2;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtu.example.springdata.entity.Employee;

@Profile({ "spring-data", "servers" })

@Repository
public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {
}