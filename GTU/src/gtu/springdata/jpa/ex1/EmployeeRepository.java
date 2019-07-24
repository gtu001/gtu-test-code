package gtu.springdata.jpa.ex1;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, BigDecimal> {
    @Modifying
    @Transactional
    @Query(value = "delete EMPLOYEE where employee_id = ?1 ", nativeQuery = true)
    void deleteByEmployeeId(String employeeId);

    @Query(value = "select * from EMPLOYEE "//
            + " where firstname like '%' || ?1 || '%' "//
            + " and description like '%' || ?2 || '%' ", //
            nativeQuery = true)
    List<Employee> qryCondition001(String firstName, String description);

    @Query(value = "select max(t.employeeId) from Employee t")
    String getMaxEmployeeId();

    @Modifying
    @Transactional
    @Query(value = "truncate table EMPLOYEE", nativeQuery = true)
    void truncateTable();

    @Query(value = "SELECT * FROM EMPLOYEE  /* #pageable# */", //
            countQuery = "select count(*) from EMPLOYEE ", nativeQuery = true)
    Page<Employee> findPageFromEmployee(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Employee set Description = ?1 "//
            + " where employee_id = ?2 "//
            , nativeQuery = true)
    int updateEmployeeDescription(String description, String employeeId);

    Optional<Employee> findByEmployeeId(BigDecimal employeeId);
}