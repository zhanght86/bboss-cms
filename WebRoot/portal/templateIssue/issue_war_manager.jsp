<%@ page language="java" contentType="application/octet-stream; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.io.File"%>
<%@page import="com.frameworkset.platform.portal.PortalProperties"%>
<%@page import="com.frameworkset.platform.portal.PortalUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
AccessControl control = AccessControl.getInstance();
control.checkAccess(request,response);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//获取发布文件的
File[] files = PortalUtil.getFileNames(PortalProperties.PORTAL_ISSUEPATH_WAR,
					"war");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'issue_war_manager.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

    <link href="<%=path%>/purviewmanager/css/windows.css" rel="stylesheet"
			type="text/css">
    <script type="text/javascript" language="Javascript">
		function delWar(obj){
			document.form1.action="delissuewar.jsp?name="+obj;
			document.form1.target="hiddenFrame";
			document.form1.submit();
		}
		
		function downWar(obj){
			document.form1.action="export.jsp?file="+obj;
			document.form1.target="hiddenFrame";
			document.form1.submit();
		}
    </script>
  </head>
  
  <body class="contentbodymargin">
    <form action="" method="post" target="hiddenFrame" name="form1">
      <table width="100%" height="30" border="0" cellpadding="0"
				cellspacing="0" class="thin">
				<tr>
					<td align="center" valign="middle">
						发布文件管理
					</td>
				</tr>
		</table>
      <table width="100%" border="0" cellpadding="1" cellspacing="0"
					class="thin" bordercolor="#EEEEEE">
			<tr>
				<th>文件名称</th><th>发布日期</th><th>操作</th>
			</tr>
			<% 
			if(files != null && files.length > 0){
				String filename = null;
				String creatorDate = null;
				SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				for(int i = 0; i < files.length; i++){
					filename = files[i].getName();
					creatorDate = s.format(new Date(files[i].lastModified()));
			%>
			<tr>
				<td><%=filename %></td>
				<td><%=creatorDate %></td>
				<td>
					<input type="button" name="downwar" onclick="downWar('<%=filename %>')" value="下载" class="input"/>
					<input type="button" name="delwar" onclick="delWar('<%=filename %>')" value="删除"
						class="input"/>
				</td>
			</tr>
			<% 
				}
			}else{
			%>
			<tr>
				<td colspan="3" align="center">暂时没有发布的文件</td>
			</tr>
			<% 
			}
			%>
      </table>
    </form>
    
    <iframe name="hiddenFrame" width="0" height="0"></iframe>
  </body>
</html>

