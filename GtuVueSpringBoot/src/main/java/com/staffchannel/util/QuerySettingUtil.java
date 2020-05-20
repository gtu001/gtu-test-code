package com.staffchannel.util;

import java.util.Calendar;
import java.util.Date;

import com.staffchannel.model.CaseFilter;

public class QuerySettingUtil {

	public static void setDefaultQueryDate(CaseFilter caseFilter) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		Date queryStartDate = cal.getTime();
		Date queryEndDate = new Date();
		caseFilter.setStartCWebApplyDateTime(queryStartDate);
		caseFilter.setEndCWebApplyDateTime(queryEndDate);
		caseFilter.setcIdNumber("123");
	}
}
