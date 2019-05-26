package com.gtu.rest.sqlserver.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gtu.rest.sqlserver.entity.Application;
import com.gtu.rest.sqlserver.repository.interf.ApplicationCTFLHRepoCustom;

@CacheConfig(cacheNames = "ApplicationCTFLH")
public interface ApplicationCTFLHRepo extends JpaRepository<Application, Long>, ApplicationCTFLHRepoCustom {

    @Cacheable
    Application findByApplicationIDAndCustomerID(String applicationID, String customerID);

    @Cacheable
    Application findByapplicationID(String applicationID);

}
