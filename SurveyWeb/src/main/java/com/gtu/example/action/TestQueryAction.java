package com.gtu.example.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.DispatchActionSupport;

import com.gtu.bo.CustomerBo;
import com.gtu.dao.impl.TestQueryDaoImpl;

public class TestQueryAction extends DispatchActionSupport {

    private static final Logger logger = Logger.getLogger(TestQueryAction.class);

    @Resource(name = "customerBo")
    private CustomerBo customerBo;

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Autowired
    private TestQueryDaoImpl testDao;

    public ActionForward test(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("## -- test");
        DynaActionForm dynaForm = (DynaActionForm) form;

        WebApplicationContext context = getContext(request);

        logger.info("dynaForm - " + dynaForm);
        logger.info("context - " + context);
        logger.info("servlet - " + request.getServletContext());
        logger.info("customerBo - " + customerBo);

        for (String bean : context.getBeanDefinitionNames()) {
            logger.info("---->" + bean);
        }

        testDao.testQuery();
        
        logger.info("===========================");

        Session session = sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery("select * from user_info");
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.list();
        for (Map<String, Object> m : list) {
            logger.info("<<" + m);
        }

        return mapping.findForward("success");
    }

    private WebApplicationContext getContext(HttpServletRequest request) {
        WebApplicationContext context = getWebApplicationContext();
        context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        return context;
    }
}