package com.gtu.rest.sqlserver.repository.interf;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.gtu.rest.sqlserver.dto.TradeJournalDTO;


public interface TradeJournalCTFLHRepoCustom {

    @Cacheable("findTradeJournalCTFLHByDynamicParam")
    List<TradeJournalDTO> findTradeJournalByDynamicParam(Map<String, Object> queryMap);
    
}
