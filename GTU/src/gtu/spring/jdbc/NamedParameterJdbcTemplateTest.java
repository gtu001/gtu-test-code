package gtu.spring.jdbc;

import gtu.db.DbConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class NamedParameterJdbcTemplateTest {
    

    public static void main(String[] args) {
        NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(DbConstant.getTestDataSource());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user_id", "gtu001");
        List<Map<String, Object>> list = nt.queryForList("select * from test.User_info  where user_id = :user_id", map);
        for(Map<String,Object> m : list){
            System.out.println("--" + m);
        }
        System.out.println("done..");
    }

}
