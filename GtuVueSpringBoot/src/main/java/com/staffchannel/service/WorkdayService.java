package com.staffchannel.service;

import java.util.List;

import javax.validation.Valid;

import com.staffchannel.model.Workday;


public interface WorkdayService {
	
    Workday findById(int id);
	
    Workday findByDatetime(String datetime);

	List<Workday> findByMonth(int year, int month);
	
	List<Workday> findByYear(int year);

	boolean initByYearMonth(int year, int month);
	
	boolean save(Workday workday);

    void saveList(@Valid List<Workday> workdays);
}