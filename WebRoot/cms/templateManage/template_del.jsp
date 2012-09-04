<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.lang.*"%>
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader ("Expires", -1);
	//prevents caching at the proxy server
	response.setDateHeader("max-age", 0); 	
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String userId = control.getUserID();
%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
.STYLE5 {color: #0000FF}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 模板管理</title>
</head>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  background="../images/grayline_bg.jpg" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0">
<a id="reload" href="template_import.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form action="../siteManage/site_createdo.jsp" method="post" enctype="multipart/form-data"  name="fm_site"  onsubmit="return check_form()">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  
   
	<!--来源于别的网站才有父站点-->
    <tr >
      <td height="22" style="padding-top:3px"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="2" bgcolor="D7D7D7"></td>
        </tr>
        <tr>
          <td bgcolor="D7D7D7"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            
            <!--来源于别的网站才有父站点-->
            
            <tr >
              <td height="77" valign="top" background="../images/toolbar_common_func_template.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 模板删除</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px">o<div align="right"></div></td>
                </tr>
              </table></td>
              </tr>
            
            
            <tr>
              <td height="33"><div align="center"><span class="STYLE5">
              
              <%
              	String tplId = request.getParameter("tplIds");
	int siteId = Integer.parseInt(request.getParameter("siteId"));	
	String[] tplIds = tplId.split(";"); 

	TemplateManager templateM = new TemplateManagerImpl();
	String deletedTempleteId="";
	boolean templateisuser=true;
	for(int i = 0;i<tplIds.length;i++)
	{
		try
		{	
			templateM.deleteTemplateFile(Integer.parseInt(tplIds[i]),new Integer(siteId).toString(),userId);	
		templateisuser=templateM.deleteTemplateofSite(Integer.parseInt(tplIds[i]),siteId);
		deletedTempleteId+=tplIds[i]+",";
		if(!templateisuser)
		{
		break;
		}
		}catch(TemplateManagerException ex)
		{		
		   templateisuser=false;
		   break;
		}		
	}
	if(!templateisuser)	{%>
	模板被引用，删除失败!
	<% 
	}
	else if(deletedTempleteId.length()>0)
	{ %>
	模板删除成功。
	<script language = "javascript">window.returnValue ="ok";</script>
	<% }else{%>
	模板删除失败。
	<%}%>
              </span></div></td>
              </tr>
            <tr>
              <td height="14">&nbsp;</td>
              </tr>
            
            <!--来源于别的网站才有父站点-->
            
            <tr>
              <td height="22"><div align="center">
                <input name="btn_close" type="button" class="cms_button"  onclick="javascript:window.close();" value="关闭" />
              </div></td>
              </tr>
            <tr>
              <td height="11" background="../images/bottom_gray.jpg">&nbsp;</td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td height="3" bgcolor="D7D7D7"></td>
        </tr>
      </table></td>
    </tr>
    
    
  <!--来源于别的网站才有父站点-->
</table>   
</form>

</body>
</html>