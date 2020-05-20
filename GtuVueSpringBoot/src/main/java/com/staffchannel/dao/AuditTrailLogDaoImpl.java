package com.staffchannel.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.staffchannel.model.AuditTrailLog;

@Repository("AuditTrailLogDao")
public class AuditTrailLogDaoImpl extends AbstractDao<Integer, AuditTrailLog> implements AuditTrailLogDao {

	static final Logger logger = LoggerFactory.getLogger(AuditTrailLogDaoImpl.class);	
	
	public AuditTrailLog findById(int id) {
		AuditTrailLog auditTrailLog = getByKey(id);	
		return auditTrailLog;
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditTrailLog> findAllCase() {
		Criteria criteria = createEntityCriteria().addOrder(Order.desc("CREATE_DATETIME"));
		List<AuditTrailLog> caseList = (List<AuditTrailLog>) criteria.list();
		return caseList;
	}
	
	@SuppressWarnings("unchecked")	
	public List<AuditTrailLog> findByIP(String ip){	
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("IP", ip));	
		List<AuditTrailLog> caseList = (List<AuditTrailLog>) criteria.list();
		return caseList;		
	}
	
	@SuppressWarnings("unchecked")	
	public List<AuditTrailLog> findByACCOUNT(String account){	
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("Account", account));	
		List<AuditTrailLog> caseList = (List<AuditTrailLog>) criteria.list();
		return caseList;		
	}
	
	public void save(AuditTrailLog auditTrailLog) {
		persist(auditTrailLog);
	}
		
	public void delete(int id) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("id", id));
		AuditTrailLog auditTrailLog = (AuditTrailLog) criteria.uniqueResult();
		delete(auditTrailLog);
	}
	
}
