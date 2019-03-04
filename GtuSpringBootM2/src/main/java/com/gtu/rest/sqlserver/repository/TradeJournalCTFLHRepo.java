package com.gtu.rest.sqlserver.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gtu.rest.sqlserver.entity.TradeJournal;
import com.gtu.rest.sqlserver.repository.interf.TradeJournalCTFLHRepoCustom;

@CacheConfig(cacheNames = "ApplicationCTFLH")
public interface TradeJournalCTFLHRepo extends JpaRepository<TradeJournal, Long>, TradeJournalCTFLHRepoCustom {


}
