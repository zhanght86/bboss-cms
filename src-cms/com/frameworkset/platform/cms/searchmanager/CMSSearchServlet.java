package com.frameworkset.platform.cms.searchmanager;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 替代站内搜索包的StartCrawlerServlet和站外搜索包中的WebSearchServlet
 * 用于定时更新索引
 * 对没一个站点更新索引
 * @author Administrator
 *
 */
public class CMSSearchServlet extends HttpServlet implements java.io.Serializable {
	/** The Date this Servlet was initialized */
//    private static Date initializedDate;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

//        response.setContentType("text/html");
//        PrintWriter out = response.getWriter();
//        out.println("<P>WebSearch</P>");
//        out.println("The Current time is: " + new Date() + "<BR>");
//        out.println("Servlet was initialized at: " + this.initializedDate + "<BR>");
//        out.flush();
//        out.close();
    }

//    public void init() throws ServletException {
//    	
////        ServletContext servletContext = servletConfig.getServletContext();
////        String conf = servletConfig.getInitParameter("configURI");
////        String path = servletContext.getRealPath(conf).replace('\\','/');
////        System.out.println("path="+path);
////        Timer timer = new Timer();
////        WebSearchTask task=new WebSearchTask(path);
////        timer.scheduleAtFixedRate(task, new Date(), 1*60*1000);
//    }

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		  //定义并启动定时器
  	  Timer timer = new Timer();
  	  CMSSearchTask task=new CMSSearchTask(config.getServletContext().getContextPath());
  	  timer.schedule(task,new Date(),1*60*1000);
  	  System.out.println("定时器启动成功！");
	}
}
