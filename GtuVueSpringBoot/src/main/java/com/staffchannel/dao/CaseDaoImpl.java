package com.staffchannel.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.staffchannel.model.Menu;
import com.staffchannel.model.CaseFilter;
import com.staffchannel.model.CaseHeader;
import com.staffchannel.model.User;
import com.staffchannel.model.UserProfile;

@Repository("caseDao")
public class CaseDaoImpl extends AbstractDao<Integer, CaseHeader> implements CaseDao {

	static final Logger logger = LoggerFactory.getLogger(CaseDaoImpl.class);

	public CaseHeader findById(int id) {
		return getByKey(id);
	}
	
	public CaseHeader findByWebApplyNo(String cWebApplyNo) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("cWebApplyNo", cWebApplyNo));
		CaseHeader caseHeader = (CaseHeader)crit.uniqueResult();
		return caseHeader;
	}
	
	@SuppressWarnings("unchecked")
	public List<CaseHeader> findCaseByFilter(CaseFilter caseFilter) {
		Criteria criteria = createEntityCriteria();
		caseFilter.addFilters(criteria);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CaseHeader> findAllCase() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<CaseHeader> caseList = (List<CaseHeader>) criteria.list();
		return caseList;
	}

}
