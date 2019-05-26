package com.gtu.rest.sqlserver.repository.interf;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.gtu.rest.sqlserver.dto.ApplicationDTO;

public interface ApplicationCTFLHRepoCustom {

    @Cacheable("findApplicationCTFLHByDynamicParam")
    List<ApplicationDTO> findApplicationByDynamicParam(Map<String, Object> queryMap);

}
