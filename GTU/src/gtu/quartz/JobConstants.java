package gtu.quartz;

/**
 * 定義Scheduling package下面會用到的常數 對映(beans.xml)
 *
 *
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/4/9:下午 3:27:23
 */
public class JobConstants {
    public static final String BEAN_FACTORY_NAME = "applicationContext";

    public static final String BEAN_JDBC_TEMPLATE_NAME = "jdbcTemplate";

    public static final String BEAN_CEDS_JDBC_TEMPLATE_NAME = "cedsJdbcTemplate";

    public static final String BEAN_DATASOUCE_NAME = "dataSource";

    public static final String BEAN_CEDS_DATASOUCE_NAME = "cedsDataSource";

    public static final String BEAN_MAIL_SENDER_NAME = "mailSender";

    public static final String JOB_STATUS_FIRE = "Fire";

    public static final String JOB_STATUS_PAUSED = "Paused";

    public static final String JOB_STATUS_COMPLETE = "End";

    public static final String JOB_STATUS_RESUMED = "Resumed";

    public static final String RUN_SQL = "run_sql";

    public static final String RUN_SQL_RESULT = "run_sql_result";
    
    public static final String REPORT_QUERY_PARAM = "reportqueryparam";
    
    public static final String REPORT_RESULT = "reportResult";

    public static final String REPORT_ATTACHMENT = "neededAttachement";
    
    public static final String MAIL = "send_mail";

    public static final String BEAN_WORKFLOW_MANAGER_NAME = "workFlowManager";
}
