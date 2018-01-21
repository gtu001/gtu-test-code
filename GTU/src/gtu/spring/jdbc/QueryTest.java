package gtu.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class QueryTest {

    void query_RowCallbackHandler(JdbcTemplate jdbcTemplate) {
        class MailContent {
            public MailContent(String candidateId, String siteId, String role, String chinesename, String region) {
                //TODO
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" select a.candidate_id,                                                      ");
        sql.append("        a.site_id,                                                           ");
        sql.append("        '文管負責人' role,                                                   ");
        sql.append("        v.chinesename,                                                       ");
        sql.append("        a.region                                                             ");
        sql.append("   from bm.bm_candidate_site a, owlet25.v_ow_nt_user v                       ");
        sql.append("  where a.dm_in_charge = v.name                                              ");
        sql.append("    and a.candidate_status = '2'                                             ");
        sql.append("    and a.site_status not in ('0', '5', '8')                                 ");
        sql.append("    and a.region <> v.region                                                 ");

        final List<MailContent> list = new ArrayList<MailContent>();

        jdbcTemplate.query(sql.toString(), new Object[] {}, new int[] {}, new RowCallbackHandler() {
            public void processRow(ResultSet rs) throws SQLException {
                MailContent content = new MailContent(//
                        rs.getString(1), //
                        rs.getString(2), //
                        rs.getString(3), //
                        rs.getString(4), //
                        rs.getString(5));
                list.add(content);
            }
        });
    }

    void findMailTo(String region, JdbcTemplate jdbcTemplate) {
        class MailTo {
            MailTo(String name, String email) {
                //TODO
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" select chinesename, email                                   ");
        sql.append("   from owlet25.v_ow_nt_user, powerprocess.userrolerelation  ");
        sql.append("  where region = ?                                           ");
        sql.append("    and length(deptid) = 5                                   ");
        sql.append("    and name = userid                                        ");
        sql.append("    and status = '1'                                         ");
        sql.append("    and subdept in ('ANO', 'GAS')                            ");
        sql.append("    and roleid = deptid || '_MANAGER'                        ");

        List<MailTo> list = jdbcTemplate.query(sql.toString(), new Object[] { region }, new int[] { Types.VARCHAR }, new RowMapper<MailTo>() {
            public MailTo mapRow(ResultSet rs, int index) throws SQLException {
                return new MailTo(rs.getString(1), rs.getString(2));
            }
        });
    }
}
