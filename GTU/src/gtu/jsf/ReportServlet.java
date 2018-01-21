package gtu.jsf;

/*
 * @(#)DataDLServlet.java        1.00 07/04/27
 *
 * Copyright (c) 2007 Universal EC, Inc.
 * All rights reserved.
 *
 * Servlet implementation class for Servlet: ReportServlet
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class for Servlet: ReportServlet
 * 
 * @version 1.00 27 April 2007
 * @author Sky Kang
 * 
 *         2009/02/02
 */
public class ReportServlet extends javax.servlet.http.HttpServlet {

    // /**
    // * 列印憑證
    // * @return
    // */
    // public String print() {
    // try {
    // Map<String,String> parameters = new HashMap<String,String>();
    // parameters.put("date","起 "+startDate+" 迄 "+endDate);
    // parameters.put("count",
    // String.valueOf(((ArrayList)getProperty(COUNT_MAP_LIST)).size()));
    // parameters.put("payAmount", payAmount);
    // parameters.put("payCount", payCount);
    // String[] detail = getShowDetail();
    // for(int ii=1;ii<=8;ii++)
    // parameters.put("s"+ii, detail[ii-1]);
    // java.util.List<Map<String,String>> rptList = new
    // ArrayList<Map<String,String>>();
    //
    // ArrayList<ArrayList<String>> arry =
    // (ArrayList<ArrayList<String>>)this.getProperty(COUNT_MAP_LIST);
    // Map<String,String> map = new HashMap<String,String>();
    // String[] field =
    // {"depKind","codnet","source","custName","custId","deduct","payCount","payAmount"};
    // for(int i=0;i<arry.size();i++){
    // ArrayList<String> ary = arry.get(i);
    // for(int ii=0;ii<field.length;ii++)
    // map.put(field[ii],checkNull(ary.get(ii)));
    // rptList.add(map);
    // map = new HashMap<String,String>();
    // }
    // fileName = "finance1.jasper";
    // SessionContext.setObject("REPORT_MAP", parameters);
    // SessionContext.setObject("REPORT_LIST", rptList);
    // SessionContext.setString("REPORT_TYPE", "JAVA_BEAN");
    // SessionContext.setString("REPORT_NAME", fileName);
    // SessionContext.setString("REPORT_SQL", null);
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // this.logActivity(getClass().getName(),Consts.logActivity.PRINT,
    // Consts.Result.SUCCESS ,"");
    // return Consts.NavigationCase.CANCEL;
    // }

    private Logger logger = Logger.getLogger(ReportServlet.class);
    public static String useLocal = new String();

    private static final long serialVersionUID = -6085143569894431244L;

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public ReportServlet() {
        super();
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
     * HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.debug("query string = [" + request.getQueryString() + "]");
        // get info from session.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        String rptType = (String) session.getAttribute("REPORT_TYPE");
        String rptName = (String) session.getAttribute("REPORT_NAME");
        String rptSQL = (String) session.getAttribute("REPORT_SQL");
        String differentPath = (String) session.getAttribute("REPORT_PATH");
        Map rptMap = (Map) session.getAttribute("REPORT_MAP");
        List rptList = (List) session.getAttribute("REPORT_LIST");

        // generate PDF report
        byte[] aPdf = null;

        String fileName = new String();
        if ((useLocal != null) && (!useLocal.equals("")))
            fileName = useLocal;
        else if ((differentPath != null) && (!differentPath.equals("")))
            fileName = differentPath;
        else {
            fileName = request.getSession().getServletContext().getRealPath("/") + "report" + java.io.File.separator;
        }
        fileName = fileName + rptName;

        // printParameter(fileName, rptMap, rptList);

        JasperFillManager aJasperFillManager = new JasperFillManager();
        String errorCode = new String();

        if (new File(fileName).exists())
            try {
                if ("RECEIPT".equals(rptType)) {
                    aPdf = loadFromFile(rptName);
                } else {
                    JasperPrint aJasperPrint = null;
                    if ("MAP_ONLY".equals(rptType)) {
                        aJasperPrint = aJasperFillManager.fillReport(fileName, rptMap, new JREmptyDataSource());
                    } else if ("JAVA_BEAN".equals(rptType)) {
                        JRDataSource jrdsMain = new JRBeanCollectionDataSource(rptList);
                        aJasperPrint = aJasperFillManager.fillReport(fileName, rptMap, jrdsMain);
                    }
                    aPdf = JasperExportManager.exportReportToPdf(aJasperPrint);
                }
                // saveToFile("c:\\REPORTSERVLET.pdf",aPdf);
            } catch (Throwable e) {
                e.printStackTrace();
                errorCode = e.getLocalizedMessage();
            }

        ServletOutputStream aOut = null;
        try {
            String dfile = getDownloadFileName(fileName);
            if (new File(fileName).exists() == false) {
                errorCode = "Error：system can't find *.jasper file.<br>系統找不到檔案&nbsp;&nbsp;" + dfile;
                response.setHeader("Content-disposition", "inline; filename=error.htm");
                response.setContentType("text/html");
                aPdf = new StringBuffer()
                        .append("<html><head>  <meta content=text/html; charset=BIG5" + " http-equiv=content-type>"
                                + " <title></title></head><body>"
                                + "<table style=text-align: left; width: 100%; border=0"
                                + " cellpadding=2 cellspacing=2>  <tbody>    <tr>"
                                + "      <td></td>      <td></td>      <td></td>    </tr>"
                                + "    <tr>      <td></td>      <td>" + errorCode + "</td>"
                                + "      <td></td>    </tr>    <tr>      <td></td>"
                                + "      <td></td>      <td></td>    </tr>  </tbody>" + "</table><br></body></html>")
                        .toString().getBytes();
                if (aPdf != null) {
                    response.setContentLength(aPdf.length);
                    aOut = response.getOutputStream();
                    aOut.write(aPdf);
                    aOut.flush();
                }
            } else {
                response.setHeader("Content-disposition", "inline; filename=" + dfile);
                response.setContentType("application/pdf");
            }
            if (aPdf != null) {
                response.setContentLength(aPdf.length);
                aOut = response.getOutputStream();
                aOut.write(aPdf);
                aOut.flush();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            errorCode = ex.getLocalizedMessage();
        } finally {
            if (aOut != null)
                aOut.close();
        }

        // if (rptMap != null) rptMap.clear();
        // if (rptList != null) rptList.clear();
        return;

    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     * HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#destroy()
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * Load file.
     * 
     * @param fileName
     *            The file name to be given.
     * @return The byte[] to be returned.
     */
    private byte[] loadFromFile(String fileName) {
        byte[] ret = null;
        try {
            File file = new File(fileName);
            ret = new byte[(int) file.length()];
            FileInputStream in = new FileInputStream(file);
            in.read(ret);
            in.close();
        } catch (Throwable e) {
            ret = null;
        }
        return ret;
    }

    public static void saveToFile(String fileName, byte[] data) {
        try {
            java.io.FileOutputStream out = new java.io.FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private String getDownloadFileName(String strfileName) {
        if ((strfileName == null) && (strfileName.equals(""))) {
            return "Report.pdf";
        } else {
            String[] separator_symbol = { "\\", "/" };
            for (int j = 0; j < separator_symbol.length; j++) {
                int sepLen = separator_symbol[j].length();
                String separator = separator_symbol[j];
                for (int i = 0; strfileName.indexOf(separator) != -1; i++) {
                    strfileName = strfileName.substring(strfileName.indexOf(separator) + sepLen, strfileName.length());
                }
            }
        }
        return strfileName;
    }

    private void printParameter(String fileName, Map rptMap, List rptList) {
        if ((fileName != null) && (!fileName.equals(""))) {
            System.out.println("===============JASPER FILE PATH ===============");
            logger.debug(fileName);
        }
        printMap(rptMap);

        if ((rptList != null) && (rptList.size() != 0)) {
            Map tempMap = new HashMap();
            logger.debug("===================LIST ==================");
            for (int i = 0; i < rptList.size(); i++) {
                tempMap = (HashMap) rptList.get(i);
                printMap(tempMap);
            }
        }
    }

    private void printMap(Map rptMap) {
        if ((rptMap != null) && (rptMap.size() != 0)) {
            logger.debug("================MAP PARAMETER ===============");
            Set aMaps = rptMap.entrySet();
            Iterator iterator = aMaps.iterator();
            while (iterator.hasNext()) {
                Map.Entry aMapEntry = (Map.Entry) iterator.next();
                logger.debug("[" + aMapEntry.getKey() + "] == [" + aMapEntry.getValue() + "]");
            }
        }
    }

}