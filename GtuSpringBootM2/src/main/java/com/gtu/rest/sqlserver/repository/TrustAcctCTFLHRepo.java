package com.gtu.rest.sqlserver.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gtu.rest.sqlserver.entity.TrustAcct;
import com.gtu.rest.sqlserver.repository.interf.TrustAcctCTFLHRepoCustom;

@CacheConfig(cacheNames = "TrustAcctCTFLH")
public interface TrustAcctCTFLHRepo extends JpaRepository<TrustAcct, Long> , TrustAcctCTFLHRepoCustom{

    @Cacheable
    TrustAcct findByCustomerIDAndTrustAcct(String customerID, String trustAcct);

    @Cacheable
    TrustAcct findBytrustAcct(String trustAcct);

}
