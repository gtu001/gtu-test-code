package gtu.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * 執行SQL結果
 *
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/3/17:下午 1:51:47
 */
public class RunSQLJob extends AbstractCommonJob {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String sql = (String) context.getJobDetail().getJobDataMap().get(JobConstants.RUN_SQL);
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = (JdbcTemplate) this.getApplicationContext(context).getBean(JobConstants.BEAN_JDBC_TEMPLATE_NAME);
            List resultList = jdbcTemplate.queryForList(sql);
            setSqlData(context, JobConstants.RUN_SQL_RESULT, resultList);
            System.out.println("show sql =>"  + sql);
            System.out.println("Run Sql Job:" + resultList.size());
            // jdbcTemplate.queryForRowSet(sql)
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 儲存執行結果
     *
     * @param context
     * @param key
     * @param value
     */
    protected void setSqlData(JobExecutionContext context, String key, Object value) {
        if (context.getJobDetail().getJobDataMap().containsKey(key)) {
            context.getJobDetail().getJobDataMap().remove(key);
        }
        context.getJobDetail().getJobDataMap().put(key, value);
    }
}
