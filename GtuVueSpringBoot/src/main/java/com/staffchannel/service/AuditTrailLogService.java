package com.staffchannel.service;

import java.util.List;

import com.staffchannel.model.AuditTrailLog;


public interface AuditTrailLogService {
	
	AuditTrailLog findById(int id);
	
	List<AuditTrailLog> findAllCase(); 
	
	List<AuditTrailLog> findByIP(String ip);		
	
	List<AuditTrailLog> findByACCOUNT(String account);	
	
	void save(AuditTrailLog auditTrailLog);	
	
	void delete(int id);
	
}
