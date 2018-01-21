package com.iisigroup.ris;

import gtu.reflect.ToStringUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TempJsfMethod {

    private Logger log = LoggerFactory.getLogger(getClass());

    UIComponent searchUi;

    void showChildren(UIComponent baseUi) {
        if (baseUi != null && baseUi.getChildren() != null) {
            int index = 0;
            for (UIComponent ui : baseUi.getChildren()) {
                log.debug(index + " ======= " + ToStringUtil.toString(ui));
                index++;
            }
        }
    }

    void findComponent(String findComponentName, UIComponent baseUi) {
        if (baseUi == null) {
            return;
        }
        UIComponent findUi = baseUi.findComponent(findComponentName);
        if (findUi == null && baseUi.getChildren() != null) {
            for (UIComponent ui : baseUi.getChildren()) {
                //                log.debug("current id = " + baseUi.getId() + " , " + ui.getClass());
                findComponent(findComponentName, ui);
            }
        } else {
            //            log.debug("!! found : parent id = " + findUi.getParent().getId() + " , " + findUi.getParent().getClass());
            searchUi = findUi;
        }
    }

    private void createMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private String forward(String page) {
        return String.format("%s?faces-redirect=true", page);
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    private HttpServletRequest req() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
}
