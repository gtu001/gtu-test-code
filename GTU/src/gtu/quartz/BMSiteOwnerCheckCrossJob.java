package gtu.quartz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * CSIMS 站台負責人是否已跨區異動之通知
 * 
 * @author Troy
 */
public class BMSiteOwnerCheckCrossJob extends AbstractCommonJob{
	private Log log = LogFactory.getLog(getClass());
	
	private static final String MAIL_TEMPLATE = "BM_SITE_OWNER_CHECK_CROSS_JOB";
	
	private static String logFormat = "["+BMSiteOwnerCheckCrossJob.class.getSimpleName()+".%s] %s";

	protected void executeInternal(JobExecutionContext context) throws Exception {
		
		BeanFactory beanFactory = null;
		InternetAddress sender = null;
		JdbcTemplate jdbcTemplate = null;
		JavaMailSender mailSender = null;
		
		try {
			beanFactory = this.getApplicationContext(context);
			sender = new InternetAddress(this.getSystemMailAddr(context));
			jdbcTemplate = (JdbcTemplate) beanFactory.getBean("jdbcTemplate");
			mailSender = (JavaMailSender) beanFactory.getBean("mailSender");
		} catch (Exception e) {
			this.debug("executeInternal", e.getMessage());
			e.printStackTrace();
			return;
		}
		
		GroupTable<MailContent> table = this.fillMailContent(jdbcTemplate);
		MailSendHandler senderHandler = MailSendHandler.newInstace(mailSender, sender, log);
		
		final String subject = "CSIMS 站台負責人已跨區異動之通知";
		final String bodyHeader = "Dear all:<br/>&nbsp;&nbsp;以下人員清單站台負責人已異動, 請重新指派站台負責人";
		
		HTMLTable body = null;
		for(Iterator it = table.get().entrySet().iterator(); it.hasNext() ;){
			Map.Entry<GroupTableKey, LinkedList<MailContent>> entry = (Map.Entry<GroupTableKey, LinkedList<MailContent>>)it.next();
			
			for(MailTo mailTo : this.findMailTo(entry.getKey().getRegion(), jdbcTemplate)){
				senderHandler.addRecipientTO(mailTo.getChinesename(), mailTo.getEmail());
			}
			
			body = HTMLTable.newInstance()
					.appendTRHeader("Candidate ID","Site ID","角色","姓名");
			
			for (Iterator iit = entry.getValue().iterator(); iit.hasNext();) {
				MailContent mailContent = (MailContent)iit.next();
				
				body.appendTR(
					mailContent.getCandidateId(), 
					mailContent.getSiteId(),
					mailContent.getRole(),
					mailContent.getChinesename()
				);
			}
			
			Object[][] o = new Object[][] { 
					{ "CONTENT", bodyHeader },
					{ "BODY", body.toString() }, 
				};
			
			try{
				senderHandler.sendMail(subject, this.getMailTemplate(ArrayUtils.toMap(o), MAIL_TEMPLATE));
				this.doMailLog(senderHandler.getMessage());
			}catch(Exception e){
				this.doFailMailLog(senderHandler.getMessage(), e.getMessage());
			}
			
			senderHandler = senderHandler.newInstance();
		}
		
		this.debug("executeInternal", "done...");
	}
	
	
	private void debug(String methodname, String message){
	    this.log.debug(String.format(logFormat, methodname, message));
        System.out.println(String.format(logFormat, methodname, message));
	}
	
	
	private static class HTMLTable {
		StringBuffer html = new StringBuffer();
		
		private static final String TABLE_FORMAT = "<table border=\"1\">%s</table>";
		private static final String TR_FORMAT = "<tr>%s</tr>";
		
		private static final String TD_HEAD_FORMAT = "<th align=\"middle\" valign=\"middle\"><font face=\"Helvetica, Arial, sans-serif\"><br>%s<br/></font></th>";
		private static final String TD_FORMAT = "<td align=\"right\" valign=\"middle\"><font face=\"Helvetica, Arial, sans-serif\"><br>%s<br/></font></td>";
		
		
		private HTMLTable(){}
		
		public static HTMLTable newInstance(){
			return new HTMLTable();
		}
		
		public HTMLTable appendTRHeader(String... value){
			this.appendTR(TD_HEAD_FORMAT, value);
			return this;
		}
		
		public HTMLTable appendTR(String... value){
			this.appendTR(TD_FORMAT, value);
			return this;
		}
		
		private void appendTR(String tdFormat, String[] value){
			StringBuilder tr = new StringBuilder();
			for(String v : value){
				tr.append(String.format(tdFormat, v));
			}
			html.append(String.format(TR_FORMAT, tr));
		}
		
		public String toString(){
			return String.format(TABLE_FORMAT, html);
		}
	}
	
	
	private List<MailTo> findMailTo (String region, JdbcTemplate jdbcTemplate) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select chinesename, email                                   ");
		sql.append("   from owlet25.v_ow_nt_user, powerprocess.userrolerelation  ");
		sql.append("  where region = ?                                           ");
		sql.append("    and length(deptid) = 5                                   ");
		sql.append("    and name = userid                                        ");
		sql.append("    and status = '1'                                         ");
		sql.append("    and subdept in ('ANO', 'GAS')                            ");
		sql.append("    and roleid = deptid || '_MANAGER'                        ");
		
		RowMapper rowMapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int index)
					throws SQLException {
				return new MailTo(rs.getString(1), rs.getString(2));
			}
		};
		return jdbcTemplate.query(sql.toString(), new Object[]{region}, new int[]{Types.VARCHAR}, rowMapper);
	}
	
	private GroupTable<MailContent> fillMailContent (JdbcTemplate jdbcTemplate) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.candidate_id,                                                      ");
		sql.append("        a.site_id,                                                           ");
		sql.append("        '建設負責人' role,                                                   ");
		sql.append("        v.chinesename,                                                       ");
		sql.append("        a.region                                                             ");
		sql.append("   from bm.bm_candidate_site a, owlet25.v_ow_nt_user v                       ");
		sql.append("  where a.construction_in_charge = v.name                                    ");
		sql.append("    and a.candidate_status = '2'                                             ");
		sql.append("    and a.site_status not in ('0', '5', '8')                                 ");
		sql.append("    and a.region <> v.region                                                 ");
		sql.append(" union                                                                       ");
		sql.append(" select a.candidate_id,                                                      ");
		sql.append("        a.site_id,                                                           ");
		sql.append("        '維運負責人' role,                                                   ");
		sql.append("        v.chinesename,                                                       ");
		sql.append("        a.region                                                             ");
		sql.append("   from bm.bm_candidate_site a, owlet25.v_ow_nt_user v                       ");
		sql.append("  where a.maintain_in_charge = v.name                                        ");
		sql.append("    and a.candidate_status = '2'                                             ");
		sql.append("    and a.site_status not in ('0', '5', '8')                                 ");
		sql.append("    and a.region <> v.region                                                 ");
		sql.append(" union                                                                       ");
		sql.append(" select a.candidate_id, a.site_id, 'RF負責人' role, v.chinesename, a.region  ");
		sql.append("   from bm.bm_candidate_site a, owlet25.v_ow_nt_user v                       ");
		sql.append("  where a.rf_in_charge = v.name                                              ");
		sql.append("    and a.candidate_status = '2'                                             ");
		sql.append("    and a.site_status not in ('0', '5', '8')                                 ");
		sql.append("    and a.region <> v.region                                                 ");
		sql.append(" union                                                                       ");
		sql.append(" select a.candidate_id,                                                      ");
		sql.append("        a.site_id,                                                           ");
		sql.append("        '傳輸負責人' role,                                                   ");
		sql.append("        v.chinesename,                                                       ");
		sql.append("        a.region                                                             ");
		sql.append("   from bm.bm_candidate_site a, owlet25.v_ow_nt_user v                       ");
		sql.append("  where a.transmission_in_charge = v.name                                    ");
		sql.append("    and a.candidate_status = '2'                                             ");
		sql.append("    and a.site_status not in ('0', '5', '8')                                 ");
		sql.append("    and a.region <> v.region                                                 ");
		sql.append(" union                                                                       ");
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
		
		final GroupTable<MailContent> groupTable = new GroupTable<MailContent>();

		jdbcTemplate.query(sql.toString(), new Object[]{}, new int[]{}, new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException {
				MailContent content = new MailContent(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5)
					);
				groupTable.put(new GroupTableKey(rs.getString(5)), content);
			}});
		
		return groupTable;
	}
	
	private static class MailTo {
		private final String chinesename;
		private final String email;
		
		public MailTo(String chinesename, String email) {
			super();
			this.chinesename = chinesename;
			this.email = email;
		}
		public String getChinesename() {
			return chinesename;
		}
		public String getEmail() {
			return email;
		}
	}
	
	private static class MailContent {
		private final String candidateId;
		private final String siteId;
		private final String role;
		private final String chinesename;
		private final String region;
		private transient String toString;
		@Override
		public String toString() {
			if (toString == null) {
				toString = new ToStringBuilder(this).append("candidateId",
						candidateId).append("siteId", siteId).append("role",
						role).append("chinesename", chinesename).append(
						"region", region).toString();
			}
			return toString;
		}
		public MailContent(String candidateId, String siteId, String role,
				String chinesename, String region) {
			super();
			this.candidateId = candidateId;
			this.siteId = siteId;
			this.role = role;
			this.chinesename = chinesename;
			this.region = region;
		}
		public String getCandidateId() {
			return candidateId;
		}
		public String getSiteId() {
			return siteId;
		}
		public String getRole() {
			return role;
		}
		public String getChinesename() {
			return chinesename;
		}
		public String getRegion() {
			return region;
		}
	}
	
	
	/**
	 *	群組化 Table 容器
	 */
	private static class GroupTable<T> {
		private HashMap<GroupTableKey, LinkedList<T>> content = new HashMap<GroupTableKey, LinkedList<T>>();

		public void put(GroupTableKey tableKey, T mailContent) {
			LinkedList<T> list = null;
			if (content.containsKey(tableKey)) {
				list = content.get(tableKey);
			} else {
				list = new LinkedList<T>();
			}
			list.add(mailContent);
			content.put(tableKey, list);
		}

		public HashMap<GroupTableKey, LinkedList<T>> get() {
			return this.content;
		}
	}
	
	/**
	 * Table 的 key 物件
	 */
	private static class GroupTableKey {
		private final String region;
		
		public GroupTableKey(String region) {
			super();
			this.region = region;
		}
		public String getRegion() {
			return region;
		}
		
		@Override
        public int hashCode() {
            return Arrays.hashCode(new Object[] {getRegion()});
        }
        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else {
                if (object instanceof GroupTableKey) {
                	GroupTableKey that = (GroupTableKey) object;
                    return (this.getRegion() == null ? that.getRegion() == null : this.getRegion().equals(that.getRegion()));
                } else {
                    return false;
                }
            }
        }
	}
	 
	/**
	 * 將MailTable內設定組成信件內容逐筆寄出
	 */
	private static class MailSendHandler {
		
		private final JavaMailSender mailSender;
		private final InternetAddress sender;
		private final MimeMessage message;
		private final Log log;
		
		private List<InternetAddress> to = new LinkedList<InternetAddress>();
		
		private static final String CHARSET = "big5";
		
		public static MailSendHandler newInstace(JavaMailSender mailSender, InternetAddress sender, Log log) {
			return new MailSendHandler(mailSender, sender, log);
		}
		
		private MailSendHandler(JavaMailSender mailSender, InternetAddress sender, Log log) {
			super();
			this.mailSender = mailSender;
			this.sender = sender;
			this.log = log;
			this.message = mailSender.createMimeMessage();
		}
		
		/**
		 * 回傳 for mail log用
		 * @param subject
		 * @param mailContent
		 * @return
		 * @throws MessagingException 
		 */
		private void sendMail(String subject, String body) throws MessagingException{
	        try {
	        	BodyPart mdp = new MimeBodyPart();
	            mdp.setContent(body, "text/html;charset=utf-8");
	            Multipart mm = new MimeMultipart(); 
	            mm.addBodyPart(mdp);   
	            message.setContent(mm);
	            message.setSubject(subject, CHARSET);
	            if (this.to != null) {
	            	message.addRecipients(Message.RecipientType.TO, (Address[]) this.to.toArray(new Address[0]));
				}
	            message.setSender(sender);
	            mailSender.send(message);
	        } catch (MessagingException e) {
	            log.error(String.format(logFormat, "sendMail", e.getMessage()));
	            throw e;
	        }
		}
		public MimeMessage getMessage() {
            return message;
        }
        public MailSendHandler newInstance(){
		    return new MailSendHandler(mailSender, sender, log);
		}
		public void addRecipientTO(String personal, String email)
				throws Exception {
			this.addRecipient(personal, email, this.to);
		}
		private void addRecipient(String personal, String email, List recipeits) throws Exception {
			try {
				recipeits.add(new InternetAddress(email, personal, CHARSET));
			} catch (Exception e) {
				throw new Exception("Unable to instance a email address, error: " + e.getMessage(), e);
			}
		}
	}
}
