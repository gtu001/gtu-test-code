package com.staffchannel.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.staffchannel.model.CaseFilter;
import com.staffchannel.model.CaseHeader;
import com.staffchannel.service.CaseService;
import com.staffchannel.util.QuerySettingUtil;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class CaseController {
	@Autowired
	CaseService caseService;
	
	/*
	 * Annotation that identifies methods which initialize the WebDataBinder 
	 * which will be used for populating command and form object arguments of annotated handler methods.
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    dateFormat.setLenient(false);  
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
	}
	
	@RequestMapping(value = { "/DataStatuManager" }, method = RequestMethod.GET)
	public String mainPage(ModelMap model) {
		CaseFilter caseFilter = new CaseFilter();
		QuerySettingUtil.setDefaultQueryDate(caseFilter);
		model.addAttribute("caseFilter", caseFilter);
		return "caseQuery/xxxxx";
	}
	
	@RequestMapping(value = { "/caseList" }, method = RequestMethod.GET)
	public String caseList(@Valid CaseFilter caseFilter, BindingResult result,
			ModelMap model) {
		List<CaseHeader> caseList = caseService.findCaseByFilter(caseFilter);
		model.addAttribute("caseList", caseList);
		return "DataStatusManager";
	}
}