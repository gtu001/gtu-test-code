package com.gtu.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.ClassUtils;

/**
 * NamedParameterJdbcTemplate SQL方法
 * 
 * @author NT82552
 *
 */
@Configuration
public class JdbcTemplateHelper {

    // /** 自動注入NamedParameterJdbcTemplate */
    // @Autowired
    // @Qualifier("sqlserverCTFJdbcTemplate")
    // private NamedParameterJdbcTemplate jdbcTemplate;

    /** log4j2 */
    private static final Logger LOG = LogManager.getLogger(JdbcTemplateHelper.class);
    
    /**
     * 查詢
     * 
     * @param sql
     * @param parameters
     * @param clazz
     * @return
     */
    public <T> List<T> queryForList(NamedParameterJdbcTemplate jdbcTemplate, String sql, Map<String, ?> parameters, Class<T> clazz) {
        showLogForSql(sql, parameters);
        if (isSampleype(clazz)) {
            return jdbcTemplate.queryForList(sql, parameters, clazz);
        } else {
            return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<T>(clazz));
        }
    }

    /**
     * 查詢
     * 
     * @param sql
     * @param parameters
     * @param clazz
     * @return
     */
    public List<Map<String, Object>> queryForList(NamedParameterJdbcTemplate jdbcTemplate, String sql, Map<String, ?> parameters) {
        showLogForSql(sql, parameters);
        return jdbcTemplate.queryForList(sql, parameters);
    }

    /**
     * 修改
     * 
     * @param sql
     * @param parameters
     * @return
     */
    public int update(NamedParameterJdbcTemplate jdbcTemplate, String sql, Map<String, ?> parameters) {
        showLogForSql(sql, parameters);
        return executeQuery(jdbcTemplate, sql, parameters);
    }

    /**
     * 新增
     * 
     * @param sql
     * @param parameters
     * @return
     */
    public int delete(NamedParameterJdbcTemplate jdbcTemplate, String sql, Map<String, ?> parameters) {
        showLogForSql(sql, parameters);
        return executeQuery(jdbcTemplate, sql, parameters);
    }

    private int executeQuery(NamedParameterJdbcTemplate jdbcTemplate, String sql, Map<String, ?> parameters) {
        showLogForSql(sql, parameters);
        return jdbcTemplate.update(sql, parameters);
    }

    private void showLogForSql(String sql, Map<String, ?> parameter) {
        LOG.info("SQL : " + sql);
        if (parameter == null || parameter.isEmpty()) {
            LOG.info("With No Parameter");
        } else {
            for (String key : parameter.keySet()) {
                LOG.info("Params : {}, Values : {}", key, parameter.get(key));
            }
        }
    }

    private void showLogForSql(String sql, MapSqlParameterSource mapSqlParameterSource) {
        LOG.info("SQL : " + sql);
        if (mapSqlParameterSource == null) {
            LOG.info("With No Parameter");
        }
    }

    private <T> Boolean isSampleype(Class<T> clazz) {
        return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.equals(String.class) || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class);
    }
}
