<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%@ page import="com.frameworkset.platform.cms.container.Template"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<base target="_self">
<body>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	//导成共有还是私有模板
	String expType = request.getParameter("expType");	
	String siteId = request.getParameter("siteId");
	String templateIds = request.getParameter("templateIds");
	String templateId = "";
	Template tmpl = new Template();
	String[] ids = templateIds.split(",");
	String templatePath = application.getRealPath("cms/siteResource/siteTemplate");
	templatePath = templatePath.replaceAll("\\\\","/");
	TemplateManagerImpl tmplImpl = new TemplateManagerImpl();
	 
    //生成ZIP文件
    String zipFileName = "tplt_"+System.currentTimeMillis();
    String tmplsdesc = "";
	File templateFile = new File(templatePath,zipFileName+".zip");
	templateFile.getParentFile().mkdirs();
	templateFile.createNewFile();
    
	OutputStream os = new BufferedOutputStream(new FileOutputStream(templateFile));
    
	try{
		tmplsdesc = tmplImpl.exportTmpls(request,siteId,ids,os);
		os.close();
		//记录到数据库
		tmplImpl.addExportTmplRecord(zipFileName,tmplsdesc,control.getUserID(),expType,Integer.parseInt(siteId));
	%>
	
	<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">    
        <tr>
            <td width="4%" height="30" align="right">
                <img src="../images/ico_point.gif" width="25" height="25">
            </td>
            <td height="30" class="cms_title_blue">
                  模板导出
            </td>
        </tr>
        <tr>
            <td colspan="2" style="height:9px; background-color:#3266B1"></td>
        </tr>
        <tr>
            <td colspan="2" style="height:10px; background-color:#fff"></td>
        </tr>
        <tr>
            <td colspan="2">
                <table width="100%" align="center" border="0"  bordercolor="#f0f0f0">
                    <tr>                        
                        <td height="24" align="center">
                               模板导出到siteTemplate目录成功 
                        </td>                          
                    </tr>
                    <tr>
                       <td colspan=2 height="54" align="center">
		                   <input type="button" type="button" class="cms_button" value="本地下载" onclick="downit('<%=templateFile.getName()%>')" >
		                   <input type="button" type="button" class="cms_button" value="关闭" onclick="window.close()" >
		               </td> 
                    </tr>                    
                </table>
            </td>
        </tr>
    </table>
    
    <script language="javascript">		
		
		function downit(path){
		    var path = "download.jsp?file=<%=templatePath%>/"+path;
		    window.open(path,"download","width=1,height=1,top=100001,left=100001");
		    
		}
	</script>
	<%
	}catch(Exception e){
	
	%>
	<script language="javascript">
		alert('<%=e.getMessage()%>');
	</script>
	<%
		e.printStackTrace();
	}finally{
		try{
			os.close();
		}catch(Exception e){
		}
	}
	%>
	<iframe name="download" height=0 width=0></iframe>
</body>
</html>