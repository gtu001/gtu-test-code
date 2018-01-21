package gtu.google.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.LongRange;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.gov.moi.util.DateUtil;

import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;

public class BatchMainTest {

    @Autowired
    private BatchLogDAO batchLogDAO;

    @Autowired
    @Qualifier("aeSessionFactory")
    private transient SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(BatchLog batchLog) {
        boolean result = batchLogDAO.save(batchLog);
        System.out.println((result == true ? "insert" : "update") + batchLog.getKey().getJobId() + " status " + batchLog.getStatus());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(BatchLog batchLog) {
        boolean result = batchLogDAO.remove(batchLog);
        System.out.println((result == true ? "delete" : "delete fail") + batchLog.getKey().getJobId());
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<BatchLog> search(BatchLog batchLog) {
        final Search search = new Search(BatchLog.class);
        if (null != batchLog.getKey()) {
            addCondition("key.jobId", batchLog.getKey().getJobId(), search);
            addCondition("key.fireTime", batchLog.getKey().getFireTime(), search);
            addCondition("key.fireTime", batchLog.getKey().getFireTimeRange(), search);
        }

        addCondition("completeTime", batchLog.getCompleteTime(), search);
        addCondition("isNotify", batchLog.getIsNotify(), search);
        addCondition("jobCode", batchLog.getJobCode(), search);
        addCondition("jobType", batchLog.getJobType(), search);
        addCondition("rtnCode", batchLog.getRtnCode(), search);
        addCondition("rtnMsg", batchLog.getRtnMsg(), search);
        addCondition("status", batchLog.getStatus(), search);
        addCondition("siteId", batchLog.getSiteId(), search);
        addCondition("siteId", batchLog.getNotEqualSiteId(), search, true);

        search.addSortAsc("key.fireTime");

        List<BatchLog> list = batchLogDAO.search(search);
        System.out.println("query = " + list.size());
        return list;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public String searchMin() {
        final Search search = new Search(BatchLog.class);
        search.addField("key.fireTime", Field.OP_MIN);
        List<Object> list = batchLogDAO.search(search);
        if (list.size() != 0) {
            return (String) list.get(0);
        }
        return "";
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void sessionQuery() {
        Session session = sessionFactory.openSession();
        Query query = session.createSQLQuery("select first 10  * from batchlog");
        System.out.println(query.getQueryString());
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result = (List<Map<String, Object>>) query.list();
        for (Map<String, Object> val : result) {
            System.out.println("rec : " + val);
        }
        session.flush();
        session.close();
    }

    private void addCondition(final String field, final LongRange value, final Search search) {
        if (value == null) {
            return;
        }
        String min = DateUtil.formatDate(new Date(value.getMinimumLong()), "yyyy/MM/dd HH:mm:ss");
        String max = DateUtil.formatDate(new Date(value.getMaximumLong()), "yyyy/MM/dd HH:mm:ss");
        if (null != value) {
            search.addFilterGreaterOrEqual(field, min).addFilterLessOrEqual(field, max);
        }
    }

    private void addCondition(final String field, final String value, final Search search) {
        if (!StringUtils.isEmpty(value)) {
            search.addFilterILike(field, value);
        }
    }

    private void addCondition(final String field, final String value1, final String value2, final Search search) {
        if (!StringUtils.isEmpty(value1)) {
            search.addFilterGreaterOrEqual(field, value1);
        }
        if (!StringUtils.isEmpty(value2)) {
            search.addFilterLessOrEqual(field, value2);
        }
    }

    private void addCondition(final String field, final String value, final Search search, final boolean isNotEqual) {
        if (!StringUtils.isEmpty(value)) {
            search.addFilterNotEqual(field, value);
        }
    }

    private void addOpDateTimeCondition(String field, LongRange value, Search search) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        if (null != value) {
            search.addFilterGreaterOrEqual(field, df.format(new Date(value.getMinimumLong())))//
                    .addFilterLessOrEqual(field, df.format(new Date(value.getMaximumLong())));
        }
    }

    private void addCondition(final String field, final long value, final Search search) {
        if (value > 0L) {
            search.addFilterILike(field, String.valueOf(value));
        } else {
            // hibernate googlecode的套件，數值預設會放''空字串，會丟NumberFormatexception
            search.addFilterGreaterOrEqual(field, "0").addFilterLessOrEqual(field, "9351699200000");
        }
    }

    private void addCondition(final String field, final int value, final Search search) {
        if (value > 0) {
            search.addFilterILike(field, String.valueOf(value));
        } else {
            // hibernate googlecode的套件，數值預設會放''空字串，會丟NumberFormatexception
            search.addFilterGreaterOrEqual(field, "0").addFilterLessOrEqual(field, "9");
        }
    }

    private void addCondition(final String field, final IntRange value, final Search search) {
        if (null != value) {
            search.addFilterGreaterOrEqual(field, value.getMinimumInteger()).addFilterLessOrEqual(field, value.getMaximumInteger());
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("gtu/google/dao/init_spring_file.xml");
        BatchMainTest mainTest = context.getBean(BatchMainTest.class);
        //query and save..
        BatchLog batchLog = new BatchLog();
        BatchLogKey key = batchLog.getKey();
        key.setJobId("BHFL201204011441060551");
        List<BatchLog> list = mainTest.search(batchLog);
        BatchLog batchLog2 = list.get(0);
        System.out.println(ReflectionToStringBuilder.toString(batchLog2));
        batchLog2.setJobName("轉錄申請書批次作業_test123");
        mainTest.save(batchLog2);
        //query min..
        //        System.out.println("min = " + mainTest.searchMin());
        //sql query..
        //        mainTest.sessionQuery();
        System.out.println("done...");
    }
}
