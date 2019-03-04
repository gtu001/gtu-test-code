package com.gtu.rest.sqlserver.repository.interf;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.gtu.rest.sqlserver.dto.TrustAcctDTO;

public interface TrustAcctCTFLHRepoCustom {

    @Cacheable("findTrustAcctCTFLHByDynamicParam")
    List<TrustAcctDTO> findTrustAcctByDynamicParam(Map<String, Object> queryMap);

}
