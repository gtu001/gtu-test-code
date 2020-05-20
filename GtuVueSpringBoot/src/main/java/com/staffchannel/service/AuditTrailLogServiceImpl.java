package com.staffchannel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staffchannel.dao.AuditTrailLogDao;
import com.staffchannel.model.AuditTrailLog;


@Service("AuditTrailLogService")
@Transactional
public class AuditTrailLogServiceImpl implements AuditTrailLogService {

	@Autowired
	private AuditTrailLogDao dao;
	
	public AuditTrailLog findById(int id) {
		return dao.findById(id);
	}	
	
	public List<AuditTrailLog> findAllCase() {
		return dao.findAllCase();
	}
	
	public List<AuditTrailLog> findByIP(String ip) {
		return dao.findByIP(ip);
	}
	
	public List<AuditTrailLog> findByACCOUNT(String account) {
		return dao.findByACCOUNT(account);
	}
	
	public void save(AuditTrailLog auditTrailLog) {
		dao.save(auditTrailLog);
	}
	
	public void delete(int id) {
		dao.delete(id);;
	}	

}
