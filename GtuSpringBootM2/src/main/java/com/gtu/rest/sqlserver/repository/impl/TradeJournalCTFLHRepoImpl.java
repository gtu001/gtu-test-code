package com.gtu.rest.sqlserver.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.gtu.rest.sqlserver.dto.TradeJournalDTO;
import com.gtu.rest.sqlserver.repository.interf.TradeJournalCTFLHRepoCustom;
import com.gtu.util.JdbcTemplateHelper;

public class TradeJournalCTFLHRepoImpl implements TradeJournalCTFLHRepoCustom {

    @Autowired
    private JdbcTemplateHelper sqlAction;

    @Autowired
    @Qualifier("sqlserverCTFLHistoryJdbcTemplate")
    private NamedParameterJdbcTemplate sqlserverCTFLHJdbcTemplate;

    public List<TradeJournalDTO> findTradeJournalByDynamicParam(Map<String, Object> queryMap) {
        String type = "CTFLHistory";

        String TrustTypeExist = MapUtils.getString(queryMap, "TrustTypeExist");

        StringBuilder sb = new StringBuilder();

        sb.append("select *");
        sb.append(" from ").append(type).append(".dbo.TradeJournal t");

        if (StringUtils.isNotBlank(TrustTypeExist)) {
            // TrustTypeExist
            sb.append(" and ");
            sb.append(TrustTypeExist);
            sb.append("  (select t.TrustAcct from ").append(type).append(".dbo.TrustAcctPeriod)");

        }

        sb.append(" order by OrderDate, TrustAcct");

        return sqlAction.queryForList(sqlserverCTFLHJdbcTemplate, sb.toString(), queryMap, TradeJournalDTO.class);
    }
}
