package gtu.jotm.spring;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class UserJdbcDao extends JdbcDaoSupport {
    public void addUser() {
        String SQL = "INSERT INTO user(user_id,user_name,user_password) VALUES(?,?,?)";
        Object[] params = new Object[] { "user_id", "user_name", "user_password" };
        this.getJdbcTemplate().update(SQL, params);
    }
}