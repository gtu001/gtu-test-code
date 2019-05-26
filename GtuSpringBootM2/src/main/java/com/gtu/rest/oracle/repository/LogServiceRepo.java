package com.gtu.rest.oracle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gtu.rest.oracle.entity.LogService;

public interface LogServiceRepo extends JpaRepository<LogService, Long>{

    List<LogService> findBytxId(Long txId);
    
}
