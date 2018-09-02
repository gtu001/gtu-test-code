package com.iisigroup.ris.${DOMAIN}.${PACKAGE}.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ${chineseShortName}控制器
 * 
 * @author ${yourName}
 */
@WindowScoped
@RisExceptionCatcher
@Named("${LOWER_ID}Controller")
public class ${UPPER_ID}Controller implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    @Spring(name = "${LOWER_ID}Service")
    private transient ${UPPER_ID}Service service;

    private ${UPPER_ID}DTO dto;
    
    @Inject
    @Spring(name = "risUserUtil")
    private transient RisUserUtil risUserUtil;

    private ExecutantType executantType;

    @PostConstruct
    private void init() {
        log.debug("# init...");
        dto = new ${UPPER_ID}DTO();
        executantType = risUserUtil.getExecutant();
    }

    #foreach( $var in $CONTROLL_METHODS )
    public String $var() {
        log.debug("# $var......");
        service.$var(dto);
        return null;
    }
    #end

    public ${UPPER_ID}DTO getDto() {
        return dto;
    }

    public void setDto(${UPPER_ID}DTO dto) {
        this.dto = dto;
    }
}
