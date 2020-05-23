package com.staffchannel.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.staffchannel.model.CaseHeader;
import com.staffchannel.model.Workday;

@Repository("workdayDao")
public class WorkdayDaoImpl extends AbstractDao<Integer, Workday> implements WorkdayDao {

    static final Logger logger = LoggerFactory.getLogger(WorkdayDaoImpl.class);

    private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressWarnings("unchecked")
    public List<CaseHeader> findAllCase() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("id"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);// To avoid
                                                                     // duplicates.
        List<CaseHeader> caseList = (List<CaseHeader>) criteria.list();
        return caseList;
    }

    @Override
    public Workday findById(int id) {
        return getByKey(id);
    }

    @Override
    public Workday findByDatetime(String datetime) {
        Criteria crit = createEntityCriteria().addOrder(Order.asc("datetime"));
        Date date1 = null;
        try {
            date1 = SDF.parse(datetime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        crit.add(Restrictions.eq("datetime", date1));
        Workday workday = (Workday) crit.uniqueResult();
        return workday;
    }

    @Override
    public List<Workday> findByMonth(int year, int month) {
        Criteria crit = createEntityCriteria().addOrder(Order.asc("datetime"));
        if (month == 12) {
            month = 1;
            year++;
        }
        Date sDate = null;
        Date eDate = null;
        try {
            sDate = SDF.parse(StringUtils.leftPad("" + year, 4, '0') + "-" + StringUtils.leftPad("" + month, 2, '0') + "-" + "01");
            eDate = SDF.parse(StringUtils.leftPad("" + year, 4, '0') + "-" + StringUtils.leftPad("" + month + 1, 2, '0') + "-" + "31");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        crit.add(Restrictions.ge("datetime", sDate));
        crit.add(Restrictions.lt("datetime", eDate));
        return crit.list();
    }

    @Override
    public List<Workday> findByYear(int year) {
        Criteria crit = createEntityCriteria().addOrder(Order.asc("datetime"));
        Date sDate = null;
        Date eDate = null;
        try {
            sDate = SDF.parse(StringUtils.leftPad("" + year, 4, '0') + "-" + "01" + "-" + "01");
            eDate = SDF.parse(StringUtils.leftPad("" + year, 4, '0') + "-" + "12" + "-" + "31");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        crit.add(Restrictions.ge("datetime", sDate));
        crit.add(Restrictions.lt("datetime", eDate));
        return crit.list();
    }

    @Override
    public boolean save(Workday workday) {
        if (workday.getSeq() == null) {
            persist(workday);
        } else {
            merge(workday);
        }
        return true;
    }

}
