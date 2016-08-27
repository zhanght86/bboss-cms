<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
AccessControl control = AccessControl.getInstance();
control.checkAccess(request,response);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String identity_split = CaProperties.IDENTITY_SPLIT;
String principal_split = CaProperties.PRINCIPAL_CREDENTIAL_SPLIT; 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>角色映射配置页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

    <link href="<%=path%>/purviewmanager/css/windows.css" rel="stylesheet"
			type="text/css">
	<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
	<script type="text/javascript" language="Javascript">
    	var indexCount = 3;
		function config(obj,state){
			var objParam = document.getElementById("paramconfig");
			var htmlStr = '<table width="100%" height="30" border="0" cellpadding="0"'
				+ 'class="thin" bordercolor="#EEEEEE">'
				+ '<tr><th>portal中的角色名称</th><th>映射角色名称</th></tr>'
				+ '<tr><td><input name="portalrole0" type="text" size="50" value="Administrator" readonly/></td>'
				+ '<td><input name="maprole0" type="text" size="50" value="administration" readonly/></td></tr>'
				+ '<tr><td><input name="portalrole1" type="text" size="50" value="Guest" readonly/></td>'
				+ '<td><input name="maprole1" type="text" size="50" value="guest" readonly/></td></tr>'
				+ '<tr><td><input name="portalrole2" type="text" size="50" value="Power User" readonly/></td>'
				+ '<td><input name="maprole2" type="text" size="50" value="power-user" readonly/></td></tr>'
				+ '<tr><td><input name="portalrole3" type="text" size="50" value="User" readonly/></td>'
				+ '<td><input name="maprole3" type="text" size="50" value="user" readonly/></td></tr>';
			
			if(state){
				indexCount++;
			}else{
				if(obj==3){
					alert("不能再减少！");
					return false;
				}
				indexCount--;
			}
			for(var i = 4; i <= indexCount; i++){
				var portalroleValue = "";
				var maproleValue = "";
				if(i <= obj){
					var portalrole = "portalrole"+i;
					var maprole = "maprole"+i
					
					portalroleValue = document.all.item(portalrole).value;
					maproleValue = document.all.item(maprole).value;
					
				}
				htmlStr += '<tr><td><input name="portalrole' + i 
					+ '" type="text" size="50" value="' + portalroleValue + '"  validator="string" cnname="portal角色名称"/></td>'
					+ '<td><input name="maprole' + i
					+ '" type="text" size="50" value="' + maproleValue + '"  validator="string" cnname="映射角色名称"/></td></tr>';
			}
			htmlStr += '<tr><td colspan="2" align="right">' +
				'<input name="opadd" value="增加" type="button" class="input" onclick="config(' +
				indexCount + ',true)"/>' +
				'<input name="opdel" value="减少" type="button" class="input" onclick="config(' + 
				indexCount + ',false)"/>' + 
				'<input name="opsub" value="确定" type="button" class="input" onclick="sub()"/>' +
				'</td><tr>' + 
				'</table>';
			objParam.innerHTML = htmlStr;
		}
		
		function sub(){
			if(validateForm(form1)){
				var formval = "";
				for(var i = 4; i <= indexCount; i++){
					var portalrole = "portalrole"+i;
					var maprole = "maprole"+i;
					
					var portalroleValue = document.all.item(portalrole).value;
					var maproleValue = document.all.item(maprole).value;
					if(formval == "")
					{
						formval = portalroleValue + "<%=identity_split%>" + maproleValue;
					}
					else
					{
						formval += "<%=principal_split%>" + portalroleValue + "<%=identity_split%>" + maproleValue;
					}
				}
				window.returnValue = formval;
				window.close();
			}
		}
    </script>
  </head>
  
  <body class="contentbodymargin">
    <form action="" method="post" target="hiddenFrame" name="form1">
      <table width="100%" height="30" border="0" cellpadding="0"
			cellspacing="0" class="thin">
			<tr>
				<td align="center" valign="middle" colspan="2">
					角色映射配置
				</td>
			</tr>
	  </table>
	  <div id="paramconfig">
	      <table width="100%" border="0" cellpadding="1" cellspacing="0"
						class="thin" bordercolor="#EEEEEE">
			<tr>
				<th>portal中的角色名称</th>
				<th>映射角色名称</th>
			</tr>
			
			<tr>
				<td><input name="portalrole0" type="text" size="50" value="Administrator" readonly/></td>
				<td><input name="maprole0" type="text" size="50" value="administration" readonly/></td>
			</tr>
			
			<tr>
				<td><input name="portalrole1" type="text" size="50" value="Guest" readonly/></td>
				<td><input name="maprole1" type="text" size="50" value="guest" readonly/></td>
			</tr>
			
			<tr>
				<td><input name="portalrole2" type="text" size="50" value="Power User" readonly/></td>
				<td><input name="maprole2" type="text" size="50" value="power-user" readonly/></td>
			</tr>
			
			<tr>
				<td><input name="portalrole3" type="text" size="50" value="User" readonly/></td>
				<td><input name="maprole3" type="text" size="50" value="user" readonly/></td>
			</tr>
			
			<tr>
            	<td colspan="2" align="right">
            		<input name="opadd" value="增加" type="button" class="input" onclick="config(3,true)"/>
            		<input name="opdel" value="减少" type="button" class="input" onclick="config(3,false)"/>
            		<input name="opsub" value="确定" type="button" class="input" onclick="sub()"/>
            	</td>
			</tr>
	      </table>
      </div>
    </form>
    
    <iframe name="hiddenFrame" width="0" height="0"></iframe>
  </body>
</html>

