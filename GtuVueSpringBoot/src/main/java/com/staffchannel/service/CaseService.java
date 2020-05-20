package com.staffchannel.service;

import java.util.List;

import com.staffchannel.model.Menu;
import com.staffchannel.model.CaseFilter;
import com.staffchannel.model.CaseHeader;


public interface CaseService {
	
	CaseHeader findById(int id);
	
	CaseHeader findByWebApplyNo(String cWebApplyNo);

	List<CaseHeader> findCaseByFilter(CaseFilter caseFilter);
	
	List<CaseHeader> findAllCase();

}