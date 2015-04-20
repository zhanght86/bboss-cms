<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page errorPage="error.jsp" %>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="net.sf.jasperreports.engine.util.*" %>
<%@ page import="net.sf.jasperreports.engine.export.*" %>
<%@ page import="net.sf.jasperreports.j2ee.servlets.*" %>
<%@ page import="net.sf.jasperreports.view.JasperViewer" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="com.frameworkset.common.poolman.PreparedDBUtil" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"%>

<%
		//AccessControl accesscontroler = AccessControl.getInstance();
		//accesscontroler.checkAccess(request, response);
		String strAbsPath = new File(request.getRealPath(request.getServletPath())).getParent();
		JasperPrint jasperPrint = null;
		String reportName = "queryUserInfo";
		String reportId = "queryUserInfo";
		int pageIndex = 0;
		int lastPageIndex = 0;
		StringBuffer sbuffer = new StringBuffer();
		StringBuffer paraBufStr = new StringBuffer();
		java.util.Map map = new java.util.HashMap();
	    List<com.frameworkset.platform.sysmgrcore.entity.User> users = com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList.getSearchUser(request,"0");

		strAbsPath = strAbsPath.replace('\\','/');
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(strAbsPath + "/queryUserInfo.jasper");
		jasperPrint =  JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(users));
		System.err.println(jasperPrint);
		session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		System.err.println("ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE="+ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
		jasperPrint.setName("用户管理");
		JRHtmlExporter exporter = new JRHtmlExporter();
		
		if (jasperPrint.getPages() != null)
		{
			lastPageIndex = jasperPrint.getPages().size() - 1;
		}

		String pageStr = request.getParameter("page");
		try
		{
			pageIndex = Integer.parseInt(pageStr);
		}
		catch(Exception e)
		{
		}
		
		if (pageIndex < 0)
		{
			pageIndex = 0;
		}

		if (pageIndex > lastPageIndex)
		{
			pageIndex = lastPageIndex;
		}
		
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "../jasperreport/image?image=");
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		exporter.setParameter(
						JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
						Boolean.TRUE); // 删除记录最下面的空行
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML,
						"");
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");

        
		exporter.exportReport();
        sbuffer=new StringBuffer(sbuffer.toString().replace("style=\"width: 595px\"","style=\"width: 100%\""));
		  
		  
	%>

	<html>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<head>
	  <style type="text/css">
	    a {text-decoration: none}
	  </style>
	</head>
	<body text="#000000" link="#000000" alink="#000000" vlink="#000000">
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
	  
	  <td align="left">
	    <hr size="1" color="#000000">
	    <table width="100%" cellpadding="0" cellspacing="0" border="0">
	      <tr>
	        <td>
	        	<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"  
				    name="report" width="1px" height="1px"  codebase="http://java.sun.com/update/1.5.0/jinstall-1_5_0_06-windows-i586.cab#Version=5,0,60,5">  
				       
				    <PARAM NAME = "CODE" VALUE = "com.frameworkset.platform.epp.reportmanage.JasperReport.PrinterApplet" >  
				    <PARAM NAME = "CODEBASE" VALUE = ".">
				    <PARAM NAME = "ARCHIVE" VALUE  = "jasperprint.jar,jfreechart-1.0.10.jar,jasperreports-3.1.2-applet.jar,jcommon-1.0.13.jar" >  
				    <PARAM NAME = "type" VALUE ="application/x-java-applet;version=1.5.0"> 
				    <PARAM NAME = "REPORT_URL" VALUE ="../jasperreport/print">   
				    <COMMENT>  
				        <EMBED    
                                type = "application/x-java-applet;version=1.5.0"    
				                CODE = "com.frameworkset.platform.epp.reportmanage.JasperReport.PrinterApplet"    
				                CODEBASE = "."  
				                ARCHIVE = "jasperprint.jar,jfreechart-1.0.10.jar,jasperreports-3.1.2-applet.jar,jcommon-1.0.13.jar"
				                REPORT_URL ="../servlets/jasperprint"
				                pluginspage = "http://java.sun.com/products/plugin/index.html#download" width="100%" height="100%">  
				            <NOEMBED>  
				            alt="Your browser understands the &lt;APPLET&gt; tag but isn't running the applet, for some reason."  
				            Your browser is completely ignoring the &lt;APPLET&gt; tag!   
				            </NOEMBED>  
				        </EMBED>  
				    </COMMENT>  
				   
				</OBJECT> 
	        </td>
			
			 <td>
	        	<a href="showJasperReport.jsp?reload=true<%=session.getAttribute("paraBufStr")==null?"":(String)session.getAttribute("paraBufStr") %>"><img src="reportImages/reload.GIF" border="0"></a>
	        </td>
	        <td>
	        	&nbsp;<a href="<%=request.getContextPath() %>/jasperreport/xls?filename=<%=reportName %>"><img src="reportImages/newexcel.gif" border="0" title="导出为excel"></a>	        	
	        </td>
			
	        <td>
	        	&nbsp;<a href="../../../jasperreport/pdf?filename=<%=reportName %>"><img src="reportImages/newpdf.gif" border="0" title="导出为PDF"></a>	        	
	        </td>
		
	        <td>
	        	&nbsp;<a href="javascript:void(0);" onclick="document.applets.report.printReport();return false;"><img src="reportImages/newprint.gif" border="0" title="打印"></a>
	        </td>
	        <td>&nbsp;&nbsp;&nbsp;</td>
			 
	<%
		if (pageIndex > 0)
		{
	%>
	        <td><a href="showJasperReport.jsp?page=0&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/first.GIF" border="0"></a></td>
	        <td><a href="showJasperReport.jsp?page=<%=pageIndex - 1%>&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/previous.GIF" border="0"></a></td>
	<%
		}
		else
		{
	%>
	        <td><img src="reportImages/first_grey.GIF" border="0"></td>
	        <td><img src="reportImages/previous_grey.GIF" border="0"></td>
	<%
		}

		if (pageIndex < lastPageIndex)
		{
	%>
	        <td><a href="showJasperReport.jsp?page=<%=pageIndex + 1%>&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/next.GIF" border="0"></a></td>
	        <td><a href="showJasperReport.jsp?page=<%=lastPageIndex%>&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/last.GIF" border="0"></a></td>
	<%
		}
		else
		{
	%>
	        <td><img src="reportImages/next_grey.GIF" border="0"></td>
	        <td><img src="reportImages/last_grey.GIF" border="0"></td>
	<%
		}
	%>
	        <td width="100%">&nbsp;</td>
	      </tr>
	      
	    </table>
	    <hr size="1" color="#000000">
	  </td>
	  
	</tr>
	<tr>
	  
	  <td  align="center">
		  <td  id="myTD" style="display:none"> <% out.println(sbuffer.toString()); %></td>

	     <% 
		   //sbuffer=new StringBuffer(sbuffer.toString().replace("style=\"width: 595px\"","style=\"width: 100%\""));
		   out.println(sbuffer.toString()); 
	
		 %>	

	  </td>
	  
	</tr>
	</table>
	<table>
	<tr>
	    <td style="width:90%"></td>
		<td>
			<table>
				<tr>
					 <%
						if (pageIndex > 0){
							%>
								<td><a href="showJasperReport.jsp?page=0&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/first.GIF" border="0"></a></td>
								<td><a href="showJasperReport.jsp?page=<%=pageIndex - 1%>&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/previous.GIF" border="0"></a></td>
							<%
							}else{
						%>
								<td><img src="reportImages/first_grey.GIF" border="0"></td>
								<td><img src="reportImages/previous_grey.GIF" border="0"></td>
						<%
							}
							if (pageIndex < lastPageIndex){
						%>
								<td><a href="showJasperReport.jsp?page=<%=pageIndex + 1%>&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/next.GIF" border="0"></a></td>
								<td><a href="showJasperReport.jsp?page=<%=lastPageIndex%>&raq=<%=reportId %>&filename=<%=reportName %>"><img src="reportImages/last.GIF" border="0"></a></td>
						<%
							}else{
						%>
								<td><img src="reportImages/next_grey.GIF" border="0"></td>
								<td><img src="reportImages/last_grey.GIF" border="0"></td>
						<%
							}
						%>
							</tr>
				</table>
		</td>
	</tr>	
	</table>
	</body>
	</html>

