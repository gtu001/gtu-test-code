package com.iisigroup.ris;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@RequestScoped
// @ManagedBean(name = "servletWatcherController")
@Controller(value = "servletWatcherController")
public class ServletWatcherController implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Logger log = LoggerFactory.getLogger(getClass());

    ServletWatcherUI servletWaterUI;

    @SuppressWarnings("unused")
    @PostConstruct
    private void init() {
        log.debug("# init...");
    }

    public void execute() {
        log.debug("# execute ....");
        try {
            log.debug(Thread.currentThread().toString());
            InitServlet initServlet = new InitServlet();
            if (servletWaterUI == null) {
                servletWaterUI = new ServletWatcherUI();
                servletWaterUI.setInitServlet(initServlet);
            }
            initServlet.request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            initServlet.response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
                    .getResponse();
            servletWaterUI.setVisible(true);
            long startTime = System.currentTimeMillis();
            while (servletWaterUI.isVisible()) {
                Thread.sleep(500);
                if (System.currentTimeMillis() - startTime > 60000) {
                    log.debug("!!start swing over 60 sec, exit!!");
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        }
    }
}
