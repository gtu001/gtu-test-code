/*
 * Log4jInit.java
 *
 * Created on 2007年3月26日, 下午 2:44
 */

package gtu.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 讀取設定檔啟動Log4j
 */
public class TEST extends HttpServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8966441223037652310L;

	private static final String LOG4J_FILE = "log4j.properties";

	public void init() {
		String webappHome = this.getServletContext().getRealPath("/");
		webappHome = webappHome.replaceAll("%20", " ");
		String path = webappHome + "WEB-INF" + File.separatorChar + "classes" + File.separatorChar;
		PropertyConfigurator.configure(path + LOG4J_FILE);
		System.out.println("log4j init ok");
		Logger logger = Logger.getLogger(TEST.class);
		logger.info("## log4j init success!!");
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		init();
		out.println("Log4j started!!...............................");
		out.close();
		
		System.out.println(System.getProperty("user.dir"));
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Log4j init";
	}
	// </editor-fold>
}
