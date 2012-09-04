
<%@page import="net.sf.jasperreports.engine.JasperReport"%>
<%@page import="net.sf.jasperreports.engine.util.JRLoader"%>
<%@page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"%><%
/**
 * 项目：系统管理 
 * 描述：实现用户查询报表打印
 * 版本：1.0 
 * 日期：2007.12.29
 * 公司：三一集团信息
 * @author gao.tang
 */
%>


<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@ page import="com.frameworkset.common.poolman.PreparedDBUtil"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ page import="net.sf.jasperreports.engine.JRExporter"%>
<%@ page import="net.sf.jasperreports.engine.export.JRHtmlExporter"%>
<%@ page import="net.sf.jasperreports.engine.export.JRHtmlExporterParameter"%>
<%@ page import="net.sf.jasperreports.engine.JRExporterParameter"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.io.*,java.util.*"%>

<html>
<head>
	<title>用户信息</title>
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../../../css/treeview.css">
	<script defer>  
		function window.onload() 
		{
			 // -- advanced features
			 factory.printing.SetMarginMeasure(2); // measure margins in inches
			 //factory.printing.printer = "HP DeskJet 870C";
			 factory.printing.paperSize = "A4";
			 factory.printing.paperSource = "Manual feed";
			 factory.printing.collate = true;
			 factory.printing.copies = 1;
			 factory.printing.SetPageRange(false, 1, 1); // need pages from 1 to 3

			 // -- basic features
			 factory.printing.header = "This is MeadCo";
			 factory.printing.font = "宋体";
			 factory.printing.footer = "Advanced Printing by ScriptX";
			 factory.printing.portrait = false;
			 factory.printing.leftMargin = 1.0;
			 factory.printing.topMargin = 1.0;
			 factory.printing.rightMargin = 1.0;
			 factory.printing.bottomMargin = 1.0;
		}
	</script>

	<script language="javascript">
		function printTure() //打印函数
		{
			 document.all("dayinDiv").style.display="none";//隐藏按钮
			 factory.printing.Print(true); //调用控件打印
			 document.all("dayinDiv").style.display="";//显示
		}
		
		function printTurePre() //打印预览
		{
			 document.all("dayinDiv").style.display="none";//隐藏按钮
			 factory.printing.Preview(); //调用控件预览
			 document.all("dayinDiv").style.display="";//显示
		}
		
		function printTureSet() //设置函数
		{
			 document.all("dayinDiv").style.display="none";//隐藏按钮
			 factory.printing.PageSetup(); //调用控件设置
			 document.all("dayinDiv").style.display="";//显示
		}

		function killErrors()
		{
		    return true;
		}
		
		window.onerror = killErrors;
	</script>
</head>
<body >
	<div  id="dayinDiv" style="display:block" name="dayinDiv" align="center">
		<input type=button value="打印本页"  class="input" onclick="printTure()"> 
		<input type=button value="页面设置"  class="input" onclick="printTureSet()"> 
		<input type=button value="打印预览"  class="input" onclick="printTurePre()">
		<input type=button value="关闭"  class="input" onclick="window.close();">
	</div>

<%  
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String strAbsPath = new File(request.getRealPath(request.getServletPath())).getParent();

	strAbsPath = strAbsPath.replace('\\','/');
	List<com.frameworkset.platform.sysmgrcore.entity.User> users = com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList.getSearchUser(request,"0");
	
	try
	{
		JasperPrint jp = null;
		java.util.Map map = new java.util.HashMap();
		
		//jp = JasperFillManager.fillReport(strAbsPath + "/queryUserInfo.jasper", map, users);
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(strAbsPath + "/queryUserInfo.jasper");
		jp =  JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(users));
	    StringBuffer sout = new StringBuffer();

		try 
		{
			JRExporter exporter = new JRHtmlExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,
								  sout);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "report_image/");
			exporter.setParameter(JRHtmlExporterParameter.
								  IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								  Boolean.TRUE); // 删除记录最下面的空行
			exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
			exporter.setParameter(JRHtmlExporterParameter.
								  BETWEEN_PAGES_HTML, "");
			exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
			exporter.setParameter(JRHtmlExporterParameter.
								  IS_USING_IMAGES_TO_ALIGN,
								  Boolean.valueOf(true));
			exporter.exportReport();
			
%>



	<!--startprint-->
	<div  id="jasper" style="display:block" name="dayinDiv">
	<%  
		out.println(sout.toString());
	%>
	</div>
	
	<!--endprint-->
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

<%
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			sout.append("生成报表数据错误!");              
		}

	}
	catch (Exception e1) 
	{
        e1.printStackTrace();
		//System.out.println("final exception");
	}
	finally
	{
		
	}
%>

</body>
</html>
