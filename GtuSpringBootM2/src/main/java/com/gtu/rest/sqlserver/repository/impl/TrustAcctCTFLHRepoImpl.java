package com.gtu.rest.sqlserver.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.gtu.rest.sqlserver.dto.TrustAcctDTO;
import com.gtu.rest.sqlserver.repository.interf.TrustAcctCTFLHRepoCustom;
import com.gtu.util.SqlAction;

public class TrustAcctCTFLHRepoImpl implements TrustAcctCTFLHRepoCustom {

    @Autowired
    private SqlAction sqlAction;

    @Autowired
    @Qualifier("sqlserverCTFLJdbcTemplate")
    private NamedParameterJdbcTemplate sqlserverCTFLJdbcTemplate;

    @Override
    public List<TrustAcctDTO> findTrustAcctByDynamicParam(Map<String, Object> queryMap) {
        String type = "CTFLHistory";

        String TrustTypeExist = MapUtils.getString(queryMap, "TrustTypeExist");

        StringBuilder sb = new StringBuilder();

        sb.append("select *");
        sb.append(" from ").append(type).append(".dbo.TrustAcct");
        sb.append(" where 1=1 ");

        if (StringUtils.isNotBlank(TrustTypeExist)) {
            sb.append(" and ");
            sb.append(TrustTypeExist);
            sb.append("  (select a.TrustAcct from ").append(type).append(".dbo.TrustAcctPeriod)");

        }

        return sqlAction.queryForList(sqlserverCTFLJdbcTemplate, sb.toString(), queryMap, TrustAcctDTO.class);
    }
}
