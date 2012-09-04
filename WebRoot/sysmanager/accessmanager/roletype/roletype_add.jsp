<%@ page contentType="text/html; charset=UTF-8" language="java"  errorPage="" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<html>
<head>
<title>新增角色类型</title>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">		
<SCRIPT language="javascript">
function save()
{
	if(roletypeform.roletype.value=="" || roletypeform.roletype.length==0)
	{
		alert("角色类型不能为空！");
		return false;
	}
	if(roletypeform.typedesc.value=="" || roletypeform.typedesc.length==0)
	{
		alert("角色描述不能为空！");
		return false;
	}
	roletypeform.action = "roletype_add_do.jsp"
	roletypeform.target = "roletypeformF"
	roletypeform.submit();
}
</SCRIPT>
</head>
<body>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
%>
<form method="post" name="roletypeform">
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="thin"> 
		    <tr valign='top'>
           		<td height='20'valign='middle'><img src='../../images/edit.gif' width="16" height="16" border=0 align='middle'>&nbsp;<strong>新增角色类型</strong></td>
         	</tr>	       
		   <tr>
	       	   <td>角色类型:
	           	<input name="roletype" type="text"  value="" size="22" />
	           </td>					
           </tr>
           <tr>
	           <td>类型描述:
	           	<textarea name="typedesc"  rows="5" style="width:400px;"></textarea>
	           </td>					
           </tr>
           <tr>
           	   <td>
               <div align="center">
               <input name="Submit4" type="button" value="保存"  class="input"  onclick="save()">
               <input style=" cursor:hand;" name="btn_close" type="button"  class="input"  onClick="window.close();" value="关闭">
               </div>
               </td>
           </tr>    
      </table>
</form>
</body>
<iframe name="roletypeformF" width="0" height="0" style="display:none"></iframe>
</html>
