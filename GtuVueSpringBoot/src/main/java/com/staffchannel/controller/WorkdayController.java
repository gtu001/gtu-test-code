package com.staffchannel.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.staffchannel.model.Workday;
import com.staffchannel.service.WorkdayService;

@Controller
@RequestMapping("/workday")
@SessionAttributes("roles")
public class WorkdayController {
    @Autowired
    WorkdayService workdayService;

    /*
     * Annotation that identifies methods which initialize the WebDataBinder
     * which will be used for populating command and form object arguments of
     * annotated handler methods.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = { "/mainPage" }, method = RequestMethod.GET)
    public String mainPage(ModelMap model) {
        return mainPage(null, null, model);
    }

    @RequestMapping(value = { "/mainPage-{year}-{month}" }, method = RequestMethod.GET)
    public String mainPage(@PathVariable String year, @PathVariable String month, ModelMap model) {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH) + 1;
        if (StringUtils.isNotBlank(year)) {
            mYear = Integer.parseInt(year);
        }
        if (StringUtils.isNotBlank(month)) {
            mMonth = Integer.parseInt(month);
        }
        List<Workday> workdays = workdayService.findByMonth(mYear, mMonth);
        if (workdays.isEmpty()) {
            workdayService.initByYearMonth(mYear, mMonth);
            workdays = workdayService.findByMonth(mYear, mMonth);
        }
        model.addAttribute("workdays", workdays);
        return "workday/workdayMain";
    }

    @RequestMapping(value = { "/save" }, method = RequestMethod.POST)
    public String saveList(@Valid List<Workday> workdays, BindingResult result, ModelMap model) {
        workdayService.saveList(workdays);
        return "workday/workdayMain";
    }
}