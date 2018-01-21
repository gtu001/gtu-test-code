package com.omniwise.owlet2.scheduling.listener;

import com.omniwise.owlet2.scheduling.JobConstants;
import com.omniwise.util.excel.ExcelReporter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StatusJobListener implements JobListener {
    Log logger = LogFactory.getLog(StatusJobListener.class);

    private static final String INSERT_JOB_RESULT_SQL = "INSERT INTO OW_JOBSCHEDULE_RESULT (JOB_RESULT_ID, JOB_ID, FIRE_TIME) VALUES (OWUTIL.F_GEN_PK('OW_JOBSCHEDULE_RESULT'), ?, sysdate) ";

    private static final String INSERT_JOB_RESULT_BLOB_SQL = "INSERT INTO OW_JOBSCHEDULE_RESULT " + " (JOB_RESULT_ID, JOB_ID, FIRE_TIME,  RESULT_SIZE, RESULT_BLOB) "
            + "  VALUES (OWUTIL.F_GEN_PK('OW_JOBSCHEDULE_RESULT'), ?, sysdate, ? ,?) ";

    private static final String GET_SYS_ADMIN_MAIL_SQL = "SELECT LABEL FROM OW_PARAM OP WHERE OP.CATEGORY = 'SYS_ADMIN' AND ROWNUM = 1";

    private static final String GET_JOB_SCHEDULE_INFO_SQL = "SELECT * FROM OW_JOBSCHEDULE J WHERE J.JOB_ID = ?";

    /**
     * 取得Spring Applicationcontext
     *
     * @param context
     * @return
     * @throws Exception
     */
    protected BeanFactory getApplicationContext(JobExecutionContext context) throws Exception {
        BeanFactory appCtx = null;
        appCtx = (BeanFactory) context.getScheduler().getContext().get(JobConstants.BEAN_FACTORY_NAME);
        if (appCtx == null) {
            throw new JobExecutionException("No application context available in scheduler context for key \"" + JobConstants.BEAN_FACTORY_NAME + "\"");
        }
        return appCtx;
    }

    public void SimpleJobListener() {
    }

    private String name = null;

    public StatusJobListener(String name) {
        this.name = new String(name);
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public void jobToBeExecuted(JobExecutionContext context) {
        String jobName = context.getJobDetail().getName();
        logger.info(jobName + " is about to be executed");
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
        String jobName = context.getJobDetail().getName();
        logger.info(jobName + " was vetoed and not executed()");
    }

    /**
     * 執行完處理JOB RESULT
     */
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        String jobName = context.getJobDetail().getName();
        logger.info(jobName + " was executed");
        BeanFactory beanFactory = null;
        try {
            beanFactory = this.getApplicationContext(context);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        JdbcTemplate jdbcTemplate = (JdbcTemplate) beanFactory.getBean(JobConstants.BEAN_JDBC_TEMPLATE_NAME);
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) beanFactory.getBean(JobConstants.BEAN_MAIL_SENDER_NAME);
        // 時區會不對不使用quartz
        // Date fireTime = context.getFireTime();
        Date fireTime = new Date();
        String jobID = context.getJobDetail().getName();
        try {
            byte[] jobResult = this.getJobResult(context);
            this.insertJobResult(jdbcTemplate, jobID, fireTime, jobResult);
            this.sendMail(context, mailSender, getSystemMailAddr(jdbcTemplate), jobResult, getJobScheduleInfo(jdbcTemplate, jobID));
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    private void insertJobResult(JdbcTemplate jdbcTemplate, String jobID, Date fireTime, byte[] data) {
        int status = 0;
        if (null != data && data.length > 0) {
            status = jdbcTemplate.update(INSERT_JOB_RESULT_BLOB_SQL, new Object[]{jobID, new Integer(data.length), new SqlLobValue(data)}, new int[]{Types.VARCHAR, Types.INTEGER, Types.BLOB});
        } else {
            status = jdbcTemplate.update(INSERT_JOB_RESULT_SQL, new Object[]{jobID});
        }
        logger.info(jobID + " was executed  and  insert into result to db ");
    }

    /**
     * 取得Poi workBook's byte array to Blob
     *
     * @param jobResultList
     * @return
     * @throws IOException
     */
    private byte[] getWorkBook(List jobResultList) throws IOException {
        String[] mHead = this.getHeader((Map) jobResultList.get(0));
        ExcelReporter writer = new ExcelReporter();
        HSSFSheet sheet = writer.createSheet("Job Result");
        writer.setHeading(sheet, mHead);
        writer.setContent(sheet, jobResultList);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.write(baos);
        byte[] temp = baos.toByteArray();
        baos.close();
        return temp;
    }

    /**
     * 取得head
     *
     * @param recMap
     * @return
     */
    private String[] getHeader(Map recMap) {
        String head[] = new String[recMap.size()];
        Iterator it = recMap.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            head[i] = (String) it.next();
            i++;
        }
        return head;
    }

    /**
     * 取得Job執行結果
     *
     * @param context
     * @return
     * @throws IOException
     */
    private byte[] getJobResult(JobExecutionContext context) throws IOException {

        List resultObj = (List) context.getJobDetail().getJobDataMap().get(JobConstants.RUN_SQL_RESULT);
        if (null != resultObj) {
            context.getJobDetail().getJobDataMap().remove(JobConstants.RUN_SQL_RESULT);
            return this.getWorkBook(resultObj);
        }
        ExcelReporter writer = (ExcelReporter) context.getJobDetail().getJobDataMap().get(JobConstants.REPORT_RESULT);
        if (null != writer) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            writer.write(baos);
            byte[] temp = baos.toByteArray();
            baos.close();
            return temp;
        }
        return null;
    }

    /**
     * 通知已經執行完
     *
     * @param context
     * @param mailSender
     * @throws MessagingException
     */
    private void sendMail(JobExecutionContext context, JavaMailSenderImpl mailSender, String SystemMail, byte[] data, Map jobInfo) throws MessagingException, Exception {
        JobDetail jobDetail = context.getJobDetail();
        String[] mails = this.getMails(jobDetail);
        boolean isAttachment = "Y".equalsIgnoreCase((String) jobInfo.get("IS_ATTACHMENT")) ? true : false;
        String jobType = (String) jobInfo.get("JOB_TYPE");
        if (null == mails) {
            return;
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String chtJobName = jobDetail.getDescription();
        String jobID = jobDetail.getName();
        if ((data != null && "JOB".equalsIgnoreCase(jobType)) || (isAttachment && data != null && "REPORT".equalsIgnoreCase(jobType))) {
            ByteArrayResource bais = new ByteArrayResource(data);
            helper.addAttachment(jobID + ".xls", bais);
            logger.info(jobID + "\t mail attachment!");
        }
        //String subject = MimeUtility.encodeText("[CSIMS排程通知]" + chtJobName , "Big5", null);
        helper.setSubject("[CSIMS排程通知]" + chtJobName);
        helper.setTo(mails);
        helper.setFrom(new InternetAddress(SystemMail));
        // message.setSubject("[CSIMS排程通知]" + chtJobName, "Big5");
        // message.setSender(new InternetAddress(SystemMail));
        message.setRecipients(MimeMessage.RecipientType.TO, buildMailAddress(mails));
        StringBuffer text = new StringBuffer();
        if (null == jobInfo.get("NOTE") || "REPORT".equalsIgnoreCase(jobType)) {
            text.append(chtJobName + "(" + jobID + ") \n");
            text.append(context.getFireTime() + "  已經執行完成!\n");
            if (null != context.getNextFireTime()) {
                text.append("下一次執行時間 :" + context.getNextFireTime());
            }
        } else {
            text.append(jobInfo.get("NOTE"));
        }

        helper.setText(text.toString());
        //message.setContent(text.toString(), "text/plain;charset=big5");
        mailSender.send(message);
        logger.info(jobID + "\t send mail!");
    }

    /**
     * 取得要送出的Mail Address
     *
     * @param jobDetail
     * @return
     */
    private String[] getMails(JobDetail jobDetail) {
        return (String[]) jobDetail.getJobDataMap().get(JobConstants.MAIL);
    }

    protected String getSystemMailAddr(JdbcTemplate jdbcTemplate) {
        return (String) jdbcTemplate.queryForObject(GET_SYS_ADMIN_MAIL_SQL, String.class);
    }

    protected Map getJobScheduleInfo(JdbcTemplate jdbcTemplate, String jobId) {
        List list = jdbcTemplate.queryForList(GET_JOB_SCHEDULE_INFO_SQL, new Object[]{jobId});
        if (list.iterator().hasNext()) {
            return (Map) list.iterator().next();
        }
        return null;
    }

    protected Address[] buildMailAddress(String[] address) {
        InternetAddress[] addrs = new InternetAddress[address.length];
        for (int i = 0; i < address.length; i++) {
            try {
                addrs[i] = new InternetAddress(address[i]);
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }
        return addrs;
    }

}