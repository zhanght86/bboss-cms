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
<%@page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"%>
<%@page import="net.sf.jasperreports.engine.type.WhenNoDataTypeEnum"%>
<%@page import="com.frameworkset.orm.transaction.TransactionManager"%>
<%
	//获得报表文件的ID
	String reportId = "queryUserInfo";
	String reportName = "queryUserInfo";
	
	JasperPrint jasperPrint = null;
	String page_num = (String)request.getParameter("page");
	int pageIndex = 0;
	int lastPageIndex = 0;
	StringBuffer sbuffer = new StringBuffer();
	//重新加载时的参数
	StringBuffer paraBufStr = new StringBuffer();
   
	TransactionManager tm = new TransactionManager();
	try{
	tm.begin();
	if(page_num!=null&&page_num.length()>0)
	{
		jasperPrint = (JasperPrint)session.getAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
	}
	String strAbsPath = new File(request.getRealPath(request.getServletPath())).getParent();
		strAbsPath = strAbsPath.replace('\\','/');
		if (request.getParameter("reload") != null || jasperPrint == null)
		{
			File reportFile = new File(strAbsPath+File.separator+reportId+".jasper");
			//如果目录下没有报表运行文件
			
				
			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());
			//传入报表中的参数
			java.util.Map parameters = new java.util.HashMap();
			//取得URL带来的参数
			
			//获得POST 过来参数设置到新的params中 
			Map params = new HashMap();
			Map requestParams = request.getParameterMap();  
		    paraBufStr.append("&").append("name=").append(reportName);
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {  
			    String name = (String) iter.next();  
			    String[] values = (String[]) requestParams.get(name);  
			    String valueStr = "";  
			    for (int i = 0; i < values.length; i++) {  
			        valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";  
			    }  
			   // valueStr = new String(valueStr.getBytes("iso8859_1"),"UTF-8");     //转换字符集  
          
			    params.put(name, valueStr);
				paraBufStr.append("&").append(name+"=").append(valueStr);
			}
			session.setAttribute("paraBufStr",paraBufStr.toString());
			//如果是一个不包含查询的报表，设置WhenNoDataType
			if(jasperReport.getQuery()==null)
			{
				jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
			}
		//List<com.frameworkset.platform.sysmgrcore.entity.UserJobs> users = com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList.getSearchUser(request,"0");
		
        //jasperReport = (JasperReport)JRLoader.loadObject(strAbsPath +File.separator+ "queryUserInfo.jasper");
        
        jasperReport = (JasperReport)JRLoader.loadObject(strAbsPath +File.separator+ "queryUserInfo.jasper");
        
		jasperPrint =  JasperFillManager.fillReport(jasperReport, parameters, new com.frameworkset.platform.sysmgrcore.web.report.DataInfoJRDataSource(com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList.class,200,request));
			session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		}
		jasperPrint.setName(reportName);
		
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
		exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath()+"/jasperreport/image?image=");
		exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		
		
		exporter.setParameter(
						JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
						Boolean.TRUE); // 删除记录最下面的空行
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML,
						"");
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");

        
		exporter.exportReport();
		tm.commit();
        //sbuffer=new StringBuffer(sbuffer.toString().replace("style=\"width: 595px\"","style=\"width: 100%\""));
		   }catch(Exception e){
		   		e.printStackTrace();
		   }
		  finally{
        	 tm.release();
         }
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
				     <PARAM NAME = "ARCHIVE" VALUE  = "jasperprint.jar,jasperreports-5.5.1.jar,jasperreports-applet-5.5.1.jar,commons-logging-1.1.1.jar,commons-collections-3.1.jar" > 
				    <PARAM NAME = "type" VALUE ="application/x-java-applet;version=1.5.0"> 
				    <PARAM NAME = "REPORT_URL" VALUE ="<%=request.getContextPath() %>/jasperreport/print">   
				    <COMMENT>  
				        <EMBED    
                                type = "application/x-java-applet;version=1.5.0"    
				                CODE = "com.frameworkset.platform.epp.reportmanage.JasperReport.PrinterApplet"    
				                CODEBASE = "."  
				                 ARCHIVE = "jasperprint.jar,jasperreports-5.5.1.jar,jasperreports-applet-5.5.1.jar,commons-logging-1.1.1.jar,commons-collections-3.1.jar"
				                REPORT_URL ="<%=request.getContextPath() %>/jasperreport/print"
				                pluginspage = "http://java.sun.com/products/plugin/index.html#download" width="100%" height="100%">  
				            <NOEMBED>  
				            alt="Your browser understands the &lt;APPLET&gt; tag but isn't running the applet, for some reason."  
				            Your browser is completely ignoring the &lt;APPLET&gt; tag!   
				            </NOEMBED>  
				        </EMBED>  
				    </COMMENT>  
				   
				</OBJECT> 
	        	
	        </td>
			<!--
	        <td>
	        	<a href="showJasperReport.jsp?reload=true&raq=<%=reportId %>"><img src="reportImages/reload.GIF" border="0"></a>
	        </td>-->
			 <!-- <td>
	        	<a href="showJasperReport.jsp?reload=true<%=session.getAttribute("paraBufStr")==null?"":(String)session.getAttribute("paraBufStr") %>"><img src="reportImages/reload.GIF" border="0"></a>
	        </td> -->
	         <td>
	        	&nbsp;<a href="<%=request.getContextPath() %>/jasperreport/rtf?filename=<%=reportName %>"><img src="reportImages/newword.gif" border="0" title="导出为word"></a>	 
	        </td>
	        <td>
	        	&nbsp;<a href="<%=request.getContextPath() %>/jasperreport/xls?filename=<%=reportName %>"><img src="reportImages/newexcel.gif" border="0" title="导出为excel"></a>	        	
	        </td>
			
	        <td>
	        	&nbsp;<a href="<%=request.getContextPath() %>/jasperreport/pdf?filename=<%=reportName %>"><img src="reportImages/newpdf.gif" border="0" title="导出为PDF"></a>	        	
	        </td>
		
	        <td>
	        	&nbsp;<a href="javascript:void 0;" onclick="document.applets.report.printReport();return false;"><img src="reportImages/newprint.gif" border="0" title="打印"></a>
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
		 

	     <% 
		   //sbuffer=new StringBuffer(sbuffer.toString().replace("style=\"width: 595px\"","style=\"width: 100%\""));
		  // out.println(sbuffer.toString()); 
		  
	
		 %>	
		 <%=sbuffer.toString()%>

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

