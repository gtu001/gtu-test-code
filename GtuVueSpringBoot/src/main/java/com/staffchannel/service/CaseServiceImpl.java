package com.staffchannel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staffchannel.dao.CaseDao;
import com.staffchannel.dao.MenuDao;
import com.staffchannel.dao.UserDao;
import com.staffchannel.model.Menu;
import com.staffchannel.model.CaseFilter;
import com.staffchannel.model.CaseHeader;
import com.staffchannel.model.User;


@Service("caseService")
@Transactional
public class CaseServiceImpl implements CaseService{

	@Autowired
	private CaseDao dao;

	public CaseHeader findById(int id) {
		return dao.findById(id);
	}

	public CaseHeader findByWebApplyNo(String cWebApplyNo) {
		CaseHeader pilCaseHeader = dao.findByWebApplyNo(cWebApplyNo);
		return pilCaseHeader;
	}

	public List<CaseHeader> findAllCase() {
		return dao.findAllCase();
	}

	public List<CaseHeader> findCaseByFilter(CaseFilter caseFilter) {
		return dao.findCaseByFilter(caseFilter);
	}
	
}
