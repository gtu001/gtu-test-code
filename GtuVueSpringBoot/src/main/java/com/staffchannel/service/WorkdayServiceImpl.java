package com.staffchannel.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staffchannel.dao.WorkdayDao;
import com.staffchannel.model.Workday;

@Service("workdayService")
@Transactional
public class WorkdayServiceImpl implements WorkdayService {

    @Autowired
    private WorkdayDao dao;

    @Override
    public Workday findById(int id) {
        return dao.findById(id);
    }

    @Override
    public Workday findByDatetime(String datetime) {
        return dao.findByDatetime(datetime);
    }

    @Override
    public List<Workday> findByMonth(int year, int month) {
        return dao.findByMonth(year, month);
    }

    @Override
    public List<Workday> findByYear(int year) {
        return dao.findByYear(year);
    }

    @Override
    public boolean initByYearMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        for (int ii = 0; ii < 31; ii++) {
            Workday workday = new Workday();
            workday.setDatetime(cal.getTime());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == 1 || dayOfWeek == 7) {
                workday.setHolidayType(HolidayType.NORMAL.getLabel());
            }
            this.save(workday);
            cal.add(Calendar.DATE, 1);
            if (cal.get(Calendar.MONTH) != month - 1) {
                break;
            }
        }
        return false;
    }

    public enum HolidayType {
        NORMAL("例假日"),//
        ;

        String label;

        HolidayType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * 取得星期幾
     */
    public static String getDayOfTheWeek(Calendar cal) {
        int val = cal.get(Calendar.DAY_OF_WEEK);
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "日");
        map.put(2, "一");
        map.put(3, "二");
        map.put(4, "三");
        map.put(5, "四");
        map.put(6, "五");
        map.put(7, "六");
        return map.get(val);
    }

    @Override
    public boolean save(Workday workday) {
        return dao.save(workday);
    }

    @Override
    public void saveList(@Valid List<Workday> workdays) {
        for (Workday workday : workdays) {
            this.save(workday);
        }
    }
}
