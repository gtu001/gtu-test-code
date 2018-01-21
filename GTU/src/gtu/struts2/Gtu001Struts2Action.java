package com.tradevan.pbkis.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import com.tradevan.commons.collection.DataList;
import com.tradevan.commons.logging.Logger;
import com.tradevan.framework.FrameworkContext;
import com.tradevan.pbkis.PageAction;
import com.tradevan.pbkis.bean.Gtu001Struts2TestDo;
import com.tradevan.pbkis.model.Gtu001Struts2TestModel;
import com.tradevan.pbkis.util.Utils;
import com.tradevan.saab.model.SaabModelException;
import com.tradevan.taurus.xdao.XdaoException;

public class Gtu001Struts2Action extends PageAction {
    private static Logger logger = FrameworkContext.getContext().getLogger();

    private static final long serialVersionUID = -7786764822670921050L;
    private InputStream fileStream;
	private String fileName;
	private InputStream pdfInputStream;
	
	private List<Map<String, String>> mapLists;
	private int totalCount;
	private Gtu001Struts2TestDo single;
	private String updateMessage;

    private boolean tradevan;
    
	public String execute() {
		try {
            tradevan = Utils.hasPrivilege("Tradevan");
        } catch (SaabModelException e) {
            e.printStackTrace();
        }
		return SUCCESS;
	}
	
    public String queryMain() {
        logger.info("#. queryMain .s");
        try {
            Map<String,String> paramMap = getParameterMap();
            String pkStart = paramMap.get("pkStart");
            String pkEnd = paramMap.get("pkEnd");
            
            //page info
            int page = getInt(paramMap.get("page"), 1);
            int pageSize = getInt(paramMap.get("pageSize"), 5);
            int skip = getInt(paramMap.get("skip"), 0);
            int take = getInt(paramMap.get("take"), 5);
            
            //sort
            String sortField = paramMap.get("sort.field");
            String sortDir = paramMap.get("sort.dir");
            
            StringBuilder sb = new StringBuilder();
            if(StringUtils.isNotBlank(pkStart)){
                sb.append(" and PK >= '" + pkStart + "' ");
            }
            if(StringUtils.isNotBlank(pkEnd)){
                sb.append(" and PK <= '" + pkEnd + "' ");
            }
            
            String orderby = "";
            if(StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortDir)){
               orderby = " " + sortField + " " + sortDir;
            }
            
            totalCount = Gtu001Struts2TestModel.INSTANCE.queryCount("1=1" + sb);
            List<Gtu001Struts2TestDo> queryList = Gtu001Struts2TestModel.INSTANCE.queryPage(skip, pageSize, "1=1" + sb, orderby);
            mapLists = new ArrayList<Map<String, String>>();
            for (int ii = 0; ii < queryList.size(); ii++) {
                Gtu001Struts2TestDo dd = queryList.get(ii);
                Map<String, String> jsonMap = new LinkedHashMap<String, String>();
                jsonMap.put("PK", dd.getPk());
                jsonMap.put("COL1", dd.getCol1());
                jsonMap.put("COL2", dd.getCol2());
                jsonMap.put("COL3", dd.getCol3());
                jsonMap.put("COL4", dd.getCol4());
                jsonMap.put("COL5", dd.getCol5());
                jsonMap.put("COL6", dd.getCol6());
                mapLists.add(jsonMap);
                
            }
        } catch (XdaoException e) {
            addActionError(e.getMessage());
            e.printStackTrace();
        }
        logger.info("#. queryMain .e");
        return SUCCESS;
    }
    
    public String querySingle() {
        logger.info("#. querySingle .s");
        try {
            String pk = getParameterMap().get("PK");
            List<Gtu001Struts2TestDo> queryList = Gtu001Struts2TestModel.INSTANCE.query("PK = '"+ pk +"'");
            single = queryList.get(0);
        } catch (XdaoException e) {
            addActionError(e.getMessage());
            e.printStackTrace();
        }
        logger.info("#. querySingle .e");
        return "querySingle";
    }
    
    public String ajaxSave() {
        logger.info("#. ajaxSave .s");
        try {
            Map<String,String> paramMap = getParameterMap();
            String pk = paramMap.get("dialog_pk");
            String type = paramMap.get("dialog_type");
            List<Gtu001Struts2TestDo> queryList = Gtu001Struts2TestModel.INSTANCE.query("PK = '"+ pk +"'");
            Gtu001Struts2TestDo dd = getRequestBean_fromDialog(new Gtu001Struts2TestDo());
            if(!queryList.isEmpty() && "update".equals(type)){
                Gtu001Struts2TestDo fromDB = queryList.get(0);
                dd.setPk(fromDB.getPk());
                int result = Gtu001Struts2TestModel.INSTANCE.update(dd);
                updateMessage = "修改 : " + result;
            }else if("insert".equals(type)){
                int result = Gtu001Struts2TestModel.INSTANCE.insert(dd);
                updateMessage = "新增 : " + result;
            }else if("delete".equals(type)){
                int result = Gtu001Struts2TestModel.INSTANCE.delete(dd);
                updateMessage = "刪除 : " + result;
            }
            addActionMessage(updateMessage);
        } catch (XdaoException e) {
            addActionError(e.getMessage());
            updateMessage = e.getMessage();
            e.printStackTrace();
        }
        logger.info("#. ajaxSave .e");
        return "ajaxSave";
    }
    
    public String updateMain() {
        logger.info("#. updateMain .s");
        getParameterMap();
        Gtu001Struts2TestDo dd = getRequestBean(new Gtu001Struts2TestDo());
        try {
            int result = Gtu001Struts2TestModel.INSTANCE.update(dd);
            addActionMessage("修改 : " + result);
        } catch (XdaoException e) {
            addActionError(e.getMessage());
            e.printStackTrace();
        }
        logger.info("#. updateMain .e");
        return SUCCESS;
    }
    
    public String destroyMain() {
        logger.info("#. destroyMain .s");
        getParameterMap();
        Gtu001Struts2TestDo dd = getRequestBean(new Gtu001Struts2TestDo());
        try {
            int result = Gtu001Struts2TestModel.INSTANCE.delete(dd);
            addActionMessage("刪除 : " + result);
        } catch (XdaoException e) {
            addActionError(e.getMessage());
            e.printStackTrace();
        }
        logger.info("#. destroyMain .e");
        return SUCCESS;
    }
    
    public String createMain() {
        logger.info("#. createMain .s");
        getParameterMap();
        Gtu001Struts2TestDo dd = getRequestBean(new Gtu001Struts2TestDo());
        try {
            int result = Gtu001Struts2TestModel.INSTANCE.insert(dd);
            addActionMessage("新增 : " + result);
        } catch (XdaoException e) {
            addActionError(e.getMessage());
            e.printStackTrace();
        }
        logger.info("#. createMain .e");
        return SUCCESS;
    }
    
    private Map<String, String> getParameterMap(){
        Map<String, String> paramMap = new TreeMap<String,String>();
        for(Enumeration<?> enu = getHttpRequest().getParameterNames(); enu.hasMoreElements();){
            String key = (String)enu.nextElement();
            String value = getHttpRequest().getParameter(key);
            paramMap.put(key, value);
        }
        logger.debug(paramMap);
        return paramMap;
    }
    
    private Gtu001Struts2TestDo getRequestBean(Gtu001Struts2TestDo dd){
        String pk = getHttpRequest().getParameter("PK");
        String col1 = getHttpRequest().getParameter("COL1");
        String col2 = getHttpRequest().getParameter("COL2");
        String col3 = getHttpRequest().getParameter("COL3");
        String col4 = getHttpRequest().getParameter("COL4");
        String col5 = getHttpRequest().getParameter("COL5");
        String col6 = getHttpRequest().getParameter("COL6");
        dd.setPk(pk);
        dd.setCol1(col1);
        dd.setCol2(col2);
        dd.setCol3(col3);
        dd.setCol4(col4);
        dd.setCol5(col5);
        dd.setCol6(col6);
        logger.debug(ReflectionToStringBuilder.toString(dd));
        return dd;
    }
    
    private Gtu001Struts2TestDo getRequestBean_fromDialog(Gtu001Struts2TestDo dd){
        String pk = getHttpRequest().getParameter("dialog_pk");
        String col1 = getHttpRequest().getParameter("dialog_col1");
        String col2 = getHttpRequest().getParameter("dialog_col2");
        String col3 = getHttpRequest().getParameter("dialog_col3");
        String col4 = getHttpRequest().getParameter("dialog_col4");
        String col5 = getHttpRequest().getParameter("dialog_col5");
        String col6 = getHttpRequest().getParameter("dialog_col6");
        dd.setPk(pk);
        dd.setCol1(col1);
        dd.setCol2(col2);
        dd.setCol3(col3);
        dd.setCol4(col4);
        dd.setCol5(col5);
        dd.setCol6(col6);
        logger.debug(ReflectionToStringBuilder.toString(dd));
        return dd;
    }
    
    private int getInt(String value, int defaultVal){
        try{
            return Integer.parseInt(value);
        }catch(Exception ex){
            return defaultVal;
        }
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getPdfInputStream() {
        return pdfInputStream;
    }

    public void setPdfInputStream(InputStream pdfInputStream) {
        this.pdfInputStream = pdfInputStream;
    }

    public boolean isTradevan() {
        return tradevan;
    }

    public void setTradevan(boolean tradevan) {
        this.tradevan = tradevan;
    }

    public List<Map<String, String>> getMapLists() {
        return mapLists;
    }

    public void setMapLists(List<Map<String, String>> mapLists) {
        this.mapLists = mapLists;
    }

    public Gtu001Struts2TestDo getSingle() {
        return single;
    }

    public void setSingle(Gtu001Struts2TestDo single) {
        this.single = single;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
