package gtu.spring.jdbc;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.google.common.collect.ImmutableMap;

public class NamedParameterJdbcDaoSupportTest extends NamedParameterJdbcDaoSupport {

    final String noneContractQuerySql = new StringBuilder()//
            .append("\n select VENDOR_ID,                                    ")//
            .append("\n        VENDOR_NAME,                                  ")//
            .append("\n        VENDOR_ENG_SHORT_NAME,                        ")//
            .append("\n        ORG,                                          ")//
            .append("\n        ITEM_CODE,                                    ")//
            .append("\n        SERIAL_NO,                                    ")//
            .append("\n        VENDOR_ACCOUNT,                               ")//
            .append("\n        REPAIR_METHOD,                                ")//
            .append("\n        WARRANTY_EXPIRE,                              ")//
            .append("\n        IS_WARRANTY_REPAIR                            ")//
            .append("\n   from OWLET25.V_SP_RMA_VC_NONE_CONTRACT_FIND        ")//
            .append("\n  where (0 = 0)                                       ")//
            .append("\n    and (VENDOR_ID = :primaryRtkVendorId)             ")//
            .append("\n    and (ORG = :currentRmaOrg)                        ")//
            .append("\n    and (ITEM_CODE = :currentRmaItemCode)             ")//
            .append("\n    and (VENDOR_ID = :primaryRmaVendorId)             ")//
            .append("\n    and (VENDOR_ACCOUNT = :primaryRmaVendorAccount)   ")//
            .toString();

    public List findByIDRange(String primaryRtkVendorId, String primaryRmaVendorId, String primaryRmaVendorAccount, String currentRmaOrg, String currentRmaItemCode) {
        Map<String, Object> withContractQueryParameter = ImmutableMap.<String, Object> builder()//
                .put("primaryRtkVendorId", primaryRtkVendorId)//
                .put("primaryRmaVendorId", primaryRmaVendorId)//
                .put("primaryRmaVendorAccount", primaryRmaVendorAccount)//
                .put("currentRmaOrg", currentRmaOrg)//
                .put("currentRmaItemCode", currentRmaItemCode)//
                .build();//

        final NamedParameterJdbcTemplate namedParameterJdbcTemplate = getNamedParameterJdbcTemplate();

        return namedParameterJdbcTemplate.queryForList(noneContractQuerySql, withContractQueryParameter);
    }

    void addForumByNamedParams() {
        class Forum {
            String forumName;
            String forumDesc;
            //ignore getter/setter
        }

        Forum forum = new Forum();
        forum.forumName = "name";
        forum.forumDesc = "desc";

        NamedParameterJdbcTemplate namedParameterJdbcTempate = new NamedParameterJdbcTemplate(Mockito.mock(DataSource.class));

        String sql = "INSERT INTO t_forum (forum_name, forum_desc) VALUES (:forumName, :forumDesc)";
        SqlParameterSource sps = new BeanPropertySqlParameterSource(forum);
        namedParameterJdbcTempate.update(sql, sps);

        //or 

        MapSqlParameterSource sps2 = new MapSqlParameterSource()//
                .addValue("forumName", "name")//
                .addValue("forumDesc", "desc");//
        namedParameterJdbcTempate.update(sql, sps2);
    }
}