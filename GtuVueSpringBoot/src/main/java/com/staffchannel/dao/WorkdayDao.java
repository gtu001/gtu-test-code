package com.staffchannel.dao;

import java.util.List;

import com.staffchannel.model.Workday;


public interface WorkdayDao {
	
	Workday findById(int id);
	
	Workday findByDatetime(String datetime);
	
	List<Workday> findByMonth(int year, int month);
	
	List<Workday> findByYear(int year);

    boolean save(Workday workday);
	
}

