<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.container.Template" %>
<%@ page import="java.util.*" %>
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader ("Expires", -1);
	//prevents caching at the proxy server
	response.setDateHeader("max-age", 0); 
%>
<%
	   TemplateManagerImpl templageManagerM = new TemplateManagerImpl();
       Template template = templageManagerM.getTemplateInfo(request.getParameter("templateId"));
 		
%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
.STYLE5 {color: #0000FF}
.STYLE6 {font-size: 14px}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 模板管理</title>
</head>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  background="../images/grayline_bg.jpg" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0">
<a id="reload" href="template_preview.jsp" style="display:none">reload...</a>
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
              <td height="77" colspan="4" valign="top" background="../images/toolbar_common_func_template.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 模板预览</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px"><div align="right"></div></td>
                </tr>
              </table></td>
              </tr>
            
            <tr>
              <td height="8" colspan="4">&nbsp;</td>
            </tr>
            <tr>
              <td width="20%"><span class="STYLE5"> &nbsp; 模板名称</span></td>
              <td width="30%"><span class="STYLE5"><%=template.getName()%></span></td>
              <td width="20%" height="25"><span class="STYLE5">&nbsp; 模板类型:
			  
			  
			  </span></td>
              <td width="30%"><span class="STYLE5">
                <%
			  if(template.getType() == 0){ out.println("站点首页模版"); }
			  if(template.getType() == 1){ out.println("频道概览模板"); }
			  if(template.getType() == 2){ out.println("文档细览模板"); }
			  %>
              </span></td>
            </tr>
            
            <!--来源于别的网站才有父站点-->
            
            <tr>
              <td><span class="STYLE5">&nbsp; 描叙:</span></td>
              <td height="22" colspan="3"><span class="STYLE5"><%=template.getDescription()%></span></td>
              </tr>
            <tr>
              <td><span class="STYLE5">&nbsp; 创建时间:</span></td>
              <td><span class="STYLE5"><%=template.getCreateTime()%></span></td>
              <td height="22" nowrap><span class="STYLE5">&nbsp; 创建者:</span></td>
              <td nowrap><span class="STYLE5"><%=template.getCreateUserName()%></span></td>
            </tr>
            <tr>
              <td height="490" colspan="4" valign="top"><p>&nbsp; 模板类容：</p>
                <p><%=template.getText()%></p>
                <p>&nbsp;</p>
                <p> templateobj.setName     (conn.getString(0,&quot;NAME&quot;));       //模板名称<br>
                  templateobj.setTempdesc (conn.getString(0,&quot;description&quot;));    //模板描述<br>
                  //templateobj.setTemptext (conn.getClob(0,&quot;TEXT&quot;).toString());<br>
                  templateobj.setType     (conn.getInt   (0,&quot;TYPE&quot;)); <br>
                  templateobj.setCreatetime(conn.getDate(0,&quot;CREATETIME&quot;)); <br>
                  //templateobj.setCreateuser(conn.getString(0,&quot;user_name&quot;)); <br>
                  templateobj.setTemplateHeader  (conn.getString(0,&quot;HEADER&quot;)); <br>
                  templateobj.setTemplateid  (conn.getInt(0,&quot;TEMPLATE_ID&quot;)); </p>                <p>&nbsp;</p></td>
              </tr>
            
            
            <tr>
              <td height="8" colspan="4">&nbsp;</td>
              </tr>
            <tr>
              <td height="22" colspan="4"><div align="center">
                <input name="btn_close" type="button" class="cms_button"  onclick="javascript:window.close();" value="关闭" />
              </div></td>
              </tr>
            <tr>
              <td height="11" colspan="4" background="../images/bottom_gray.jpg">&nbsp;</td>
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