package com.gtu.example.controller;

import java.util.List;

import javax.validation.Valid;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.example.springdata.dao_2.EmployeeJpaRepository;
import com.gtu.example.springdata.entity.Employee;

@RestController
@RequestMapping("/springdata-crud")
public class SpringDataCrudController {
    
    private static final Logger log = LoggerFactory.getLogger(SpringDataCrudController.class);

//    @Autowired
    private EmployeeJpaRepository employeeJpaRepository = Mockito.mock(EmployeeJpaRepository.class);

    @GetMapping(value = "/findAll")
    // public Page<Question> findAll(Pageable pageable) {
    // return employeeJpaRepository.findAll(pageable);
    // }
    public List<Employee> findAll() {
        return employeeJpaRepository.findAll();
    }

    @PostMapping(value = "/create")
    public Employee create(@Valid @RequestBody Employee employee) {
        return employeeJpaRepository.save(employee);
    }

    @PutMapping(value = "/update/{employeeId}")
    public Employee updateQuestion(@PathVariable Long employeeId, @Valid @RequestBody Employee employeeReq) {
        return employeeJpaRepository.findById(employeeId).map(employee -> {
            employee.setDescription(employeeReq.getDescription());
            employee.setFirstName(employeeReq.getFirstName());
            employee.setLastName(employeeReq.getLastName());
            return employeeJpaRepository.save(employee);
        }).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));// 會跳404
    }

    @DeleteMapping(value = "/delete/{employeeId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long employeeId) {
        return employeeJpaRepository.findById(employeeId).map(question -> {
            employeeJpaRepository.delete(question);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));// 會跳404
    }
}
