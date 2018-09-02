package gtu.quartz;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 重新包裝 QuartzJobBean介面，支援Spring功能
 * 
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/2/27:下午 5:42:07
 */
abstract class AbstractCommonJob implements Job {
    protected Log log = LogFactory.getLog(getClass());
    private static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    private static final String MAIL_TEMPLATE_DIR = "job_mail_template";
    private String regexPattern = "\\{(\\D.*?)\\}";
    protected static final String SYSTEM_MAIL_ADDR_SQL = " select label from OW_PARAM where category = 'SYSTEM_MAIL_ADDR' and VALUE = 'default'";

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            executeInternal(context);
        } catch (Exception e) {
            this.log.error("job execution message:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 取得Spring Applicationcontext
     * 
     * @param context
     * @return
     * @throws Exception
     */
    protected BeanFactory getApplicationContext(JobExecutionContext context) throws Exception {
        BeanFactory appCtx = null;
        appCtx = (BeanFactory) context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
        if (appCtx == null) {
            throw new JobExecutionException("No application context available in scheduler context for key \""
                    + APPLICATION_CONTEXT_KEY + "\"");
        }
        return appCtx;
    }

    /**
     * 取得mail sender
     * 
     * @param context
     * @return
     * @throws BeansException
     * @throws Exception
     */
    protected JavaMailSender getMailSender(JobExecutionContext context) throws BeansException, Exception {
        return (JavaMailSender) this.getApplicationContext(context).getBean(JobConstants.BEAN_MAIL_SENDER_NAME);
    }

    /**
     * 取得jdbctemplate
     * 
     * @param context
     * @param jdbcTemplateName
     * @return
     * @throws BeansException
     * @throws Exception
     */
    protected JdbcTemplate getJdbcTemplate(JobExecutionContext context, String jdbcTemplateName) throws BeansException,
            Exception {
        return (JdbcTemplate) this.getApplicationContext(context).getBean(jdbcTemplateName);
    }

    /**
     * 是否可以送Mail
     * 
     * @param context
     * @return
     * @throws BeansException
     * @throws Exception
     */
    protected boolean isSendMail(JobExecutionContext context) throws BeansException, Exception {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) this.getApplicationContext(context).getBean(
                JobConstants.BEAN_JDBC_TEMPLATE_NAME);
        return true;
    }

    /**
     * 取得系統MAIL
     * 
     * @param context
     * @return
     */
    protected String getSystemMailAddr(JobExecutionContext context) {
        try {
            return (String) this.getJdbcTemplate(context, JobConstants.BEAN_JDBC_TEMPLATE_NAME).queryForObject(
                    SYSTEM_MAIL_ADDR_SQL, String.class);
        } catch (Exception e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /**
     * 記錄mail log
     * 
     * @param message
     * @throws Exception
     */
    protected void doMailLog(MimeMessage message) {
        String from;
        String to;
        try {
            from = (String) (null != message.getFrom() && message.getFrom().length != 0 ? this
                    .getMailAddressToString(message.getFrom()) : "");
            to = this.getMailAddressToString(message.getAllRecipients());
            this.log.info("**Email處理 Start**");
            this.log.info("form: " + from);
            this.log.info("to:" + to);
            this.log.info("subject: " + message.getSubject());
            this.log.info("content: " + message.getContent());
            this.log.info("**Email處理 End**");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 記錄mail fail log
     * 
     * @param message
     *            , errMsg
     * @throws Exception
     */
    protected void doFailMailLog(MimeMessage message, String errMsg) {
        String from;
        String to;
        try {
            from = (String) (null != message.getFrom() && message.getFrom().length != 0 ? this
                    .getMailAddressToString(message.getFrom()) : "");
            to = this.getMailAddressToString(message.getAllRecipients());
            this.log.info("**Email處理 Start**");
            this.log.info("form: " + from);
            this.log.info("to:" + to);
            this.log.info("subject: " + message.getSubject());
            this.log.info("content: " + message.getContent());
            this.log.info("Failure Sending Mail:" + errMsg);
            this.log.info("**Email處理 End**");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得MailAddess to String
     * 
     * @param address
     * @return
     */
    protected String getMailAddressToString(Address[] address) {
        StringBuilder sb = new StringBuilder();
        if (null == address) {
            return "";
        }
        for (int i = 0; i < address.length; i++) {
            sb.append(address[i].toString());
            if (i != (address.length - 1)) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    protected InternetAddress[] buildInternetAddress(String mails) throws AddressException {
        String[] temp = mails.split(";");
        InternetAddress[] ia = new InternetAddress[temp.length];
        for (int i = 0; i < temp.length; i++) {
            ia[i] = new InternetAddress(temp[i]);
        }
        return ia;
    }

    /**
     * 統一寄mail的方式
     * 
     * @param mailSender
     * @param subject
     * @param mailBody
     * @param mailAddress
     * @param senderMailAddress
     */
    protected void sendMail(JavaMailSender mailSender, String subject, String mailBody, String mailAddress,
            String senderMailAddress) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setSubject(subject, "UTF-8");
            message.setRecipients(MimeMessage.RecipientType.TO, buildInternetAddress(mailAddress));
            //            message.setSender(new InternetAddress(senderMailAddress));
            message.setContent(mailBody, "text/html;charset=UTF-8");
            mailSender.send(message);
            this.doMailLog(message);
        } catch (Exception e) {
            this.doFailMailLog(message, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 讀取Mail Template
     * 
     * @return
     * @throws Exception
     */
    protected String getMailTemplate(Map map, String templateID) {
        String mailTemplate;
        try {
            mailTemplate = this.readTemplateFile(templateID);
            return this.build(mailTemplate, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String build(String mailTemplate, Map dataMap) {
        String newFormatStr = null;
        try {
            Map codeMap = new HashMap();
            int count = 1;
            newFormatStr = this.customReplace(mailTemplate, codeMap, this.regexPattern, count);
            newFormatStr = format(newFormatStr, dataMap, codeMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFormatStr;
    }

    private String format(String str, Map valueMap, Map codeMap) {
        int size = codeMap.size();
        Object[] args = new Object[size + 1];
        for (int i = 0; i < size; i++) {
            String code = (String) codeMap.get(new Integer(i + 1));
            Object o = valueMap.get(code);
            args[i + 1] = o;
        }
        return MessageFormat.format(str, args);
    }

    private String customReplace(String str, Map codeMap, String patternStr, int count) {
        Pattern p = Pattern.compile(patternStr);
        Matcher matcher = p.matcher(str);
        String r = null;
        if (matcher.find()) {
            String code = str.substring(matcher.start() + 1, matcher.end() - 1).trim();
            codeMap.put(new Integer(count), code);
            r = matcher.replaceFirst("{" + count + "}");
            return customReplace(r, codeMap, patternStr, ++count);
        } else {
            return str;
        }
    }

    protected String readTemplateFile(String templateID) throws IOException {
        Resource resource = new FileSystemResource(this.MAIL_TEMPLATE_DIR + "//" + templateID);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(resource.getFile());
            int x = fis.available();
            byte b[] = new byte[x];
            fis.read(b);
            return new String(b, "Big5");
        } catch (Exception e) {

        } finally {
            fis.close();
        }
        return null;
    }

    /**
     * Spring job真正執行點，所有Job必須實作此方法
     */
    abstract protected void executeInternal(JobExecutionContext context) throws Exception;
}
