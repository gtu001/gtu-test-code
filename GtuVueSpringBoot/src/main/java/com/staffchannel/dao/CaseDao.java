package com.staffchannel.dao;

import java.util.List;

import com.staffchannel.model.Menu;
import com.staffchannel.model.CaseFilter;
import com.staffchannel.model.CaseHeader;
import com.staffchannel.model.User;
import com.staffchannel.model.UserProfile;


public interface CaseDao {
	
	CaseHeader findById(int id);
	
	CaseHeader findByWebApplyNo(String cWebApplyNo);
	
	List<CaseHeader> findAllCase();
	
	List<CaseHeader> findCaseByFilter(CaseFilter caseFilter);
	
}

