package gtu.springdata.jpa.ex1;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Employee1Repository extends JpaRepository<Employee1, String> {

    @Query(value = "SELECT * FROM EMPLOYEE_1  /* #pageable# */", //
            countQuery = "select count(*) from EMPLOYEE_1 ", nativeQuery = true)
    Page<Employee1> findPageFromEmployee(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update Employee1 set name = ?1 where pk1 = ?2 ")
    int updateNameByPk1(String name, String pk1);
}