package com.staffchannel.dao;

import java.util.List;

import com.staffchannel.model.AuditTrailLog;

public interface AuditTrailLogDao {
	
	AuditTrailLog findById(int id);
	
	List<AuditTrailLog> findAllCase();
	
	List<AuditTrailLog> findByIP(String ip);	
	
	List<AuditTrailLog> findByACCOUNT(String account);	
	
	void save(AuditTrailLog auditTrailLog);
	
	void delete(int id);
	
}
