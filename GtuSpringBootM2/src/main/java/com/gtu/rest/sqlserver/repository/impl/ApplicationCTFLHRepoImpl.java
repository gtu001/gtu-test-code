package com.gtu.rest.sqlserver.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.gtu.rest.sqlserver.dto.ApplicationDTO;
import com.gtu.rest.sqlserver.repository.interf.ApplicationCTFLHRepoCustom;
import com.gtu.util.JdbcTemplateHelper;

public class ApplicationCTFLHRepoImpl implements ApplicationCTFLHRepoCustom {

    @Autowired
    private JdbcTemplateHelper sqlAction;

    @Autowired
    @Qualifier("sqlserverCTFLHistoryJdbcTemplate")
    private NamedParameterJdbcTemplate sqlserverCTFLHJdbcTemplate;

    public List<ApplicationDTO> findApplicationByDynamicParam(Map<String, Object> queryMap) {
        String type = "CTFLHistory";
        StringBuilder sb = new StringBuilder();

        sb.append("select *");
        sb.append(" from ").append(type).append(".dbo.Application a");
        sb.append(" where 1=1 ");

        String SameDay = MapUtils.getString(queryMap, "SameDay_Req");
        if (StringUtils.isNotBlank(SameDay)) {
            sb.append(" and SameDay_Req = :SameDay_Req ");
        }

        String TrustTypeExist = MapUtils.getString(queryMap, "TrustTypeExist");
        if (StringUtils.isNotBlank(TrustTypeExist)) {
            sb.append(" and ");
            sb.append("  (select a.TrustAcct from ").append(type).append(".dbo.TrustAcctPeriod)");
        }

        return sqlAction.queryForList(sqlserverCTFLHJdbcTemplate, sb.toString(), queryMap, ApplicationDTO.class);
    }
}
