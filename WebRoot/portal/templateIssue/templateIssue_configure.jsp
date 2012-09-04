<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(pageContext);
	
	String root = request.getContextPath();
	
	
	String[] columnModuleValue = request.getParameterValues("columnModuleValue");
	if(columnModuleValue == null || columnModuleValue.length == 0){
		out.print("<div>应用模块发布，请选择菜单模块！</div>");
		return;		
	}
	int length = columnModuleValue.length;
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	String identity_split = CaProperties.IDENTITY_SPLIT;
	String principal_split = CaProperties.PRINCIPAL_CREDENTIAL_SPLIT; 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>MQ服务器信息列表</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="../../purviewmanager/css/windows.css" rel="stylesheet"
			type="text/css">
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
		<script language="javascript" type="text/javascript">
		
		
		function queryTest(){
			var area = document.all.areaType.value;
			window.open("<%=root%>/test/menu/testMain.jsp?areaType="+area);
		}
		
		function queryArea(i){
			document.form1.action="module_area_query.jsp?i="+i;
			document.form1.target="_blank";
			document.form1.submit();
		}
		
		function issueDo(){
			var s = document.all.portletIframeName.value;
			if(/[^\x00-\xff]/g.test(s)){
		    	//含有汉字
		    	alert(document.all.portletIframeName.cnname + ",不能填写中文字符！");
		    	document.all.portletIframeName.focus();
		    	return;
		    }
			if(validateForm(form1)){
				document.form1.action="templateIssue_configure_do.jsp";
				document.form1.target="hiddenFrame";
				document.form1.submit();
			}
		}
		
		function setRoleMapg(){
			var url = "role_mapping_set.jsp";
			var retval = window.showModalDialog(url,window,
				"dialogWidth:800px" + //screen.availWidth + 
				";dialogHeight:600px" + //screen.availHeight + 
				";help:no;scroll:auto;status:no;maximize=yes;minimize=0");
			
			var objParam = document.getElementById("hiddenrole");
			var htmlStr = '<table width="100%" height="30" border="0" cellpadding="0"'
				+ 'class="thin" bordercolor="#EEEEEE">'
				+ '<tr><td><input name="portalrole0" type="text" size="50" value="Administrator" readonly/></td>'
				+ '<td><input name="maprole0" type="text" size="50" value="administration" readonly/></td></tr>'
				+ '<tr><td><input name="portalrole1" type="text" size="50" value="Guest" readonly/></td>'
				+ '<td><input name="maprole1" type="text" size="50" value="guest" readonly/></td></tr>'
				+ '<tr><td><input name="portalrole2" type="text" size="50" value="Power User" readonly/></td>'
				+ '<td><input name="maprole2" type="text" size="50" value="power-user" readonly/></td></tr>'
				+ '<tr><td><input name="portalrole3" type="text" size="50" value="User" readonly/></td>'
				+ '<td><input name="maprole3" type="text" size="50" value="user" readonly/></td></tr>';
			if(retval!=""){
				alert(retval);
				var arrays = retval.split("<%=principal_split%>"); 
				var size = arrays.length;
				document.all.roleCount.value = size + 3;
				for(var i = 0; i < size; i++){
					var arr = arrays[i].split("<%=identity_split%>");
					htmlStr += '<tr><td><input name="portalrole' + (i+4) 
						+ '" type="text" size="50" value="' + arr[0] + '"  validator="string" cnname="portal角色名称"/></td>'
						+ '<td><input name="maprole' + (i+4)
						+ '" type="text" size="50" value="' + arr[1] + '"  validator="string" cnname="映射角色名称"/></td></tr>';
				}
			}
			htmlStr += '</table>';
			alert(htmlStr);
			objParam.innerHTML = htmlStr;
			var objParams = document.getElementById("rolesel0");
			alert(objParams);
			objParams.innerHTML = htmlStr;
		}
		

</script>
</head>

	<body>
		<form method="post" name="form1">
			<div style="display:none" id="hiddenrole">
				<tr>
					<td>fffff</td>
				</tr>
			</div>
			<input name="roleCount" type="hidden" value="3" / >
			<table width="100%" height="30" border="0" cellpadding="0"
					cellspacing="0" class="thin">
					<tr>
						<td align="center" valign="middle" colspan="2">
							应用菜单模块发布
						</td>
					</tr>
					<tr>
						<td width="120" align="right">portlet war包名称：</td>
						<td><input name="portletIframeName" type="text" value="" size="50"  validator="stringLegal" cnname="portlet war包名称"/><font color="red">*</font></td>
					</tr>
			</table>
			<div align="right">
				<tr>
				<td align="right">
					<!-- 
					input name="rolemapping" value="角色映射配置" type="button" class="input" onclick="setRoleMapg()"
					 -->
					<input name="issueAppSub" type="button" value="发布成portlet iframe war包" class="input"
						onclick="issueDo()"/>
				</td>
				</tr>
			</div>
          <input name="moduleCount" value="<%=length %>" type="hidden">
		  <% 
           	String subsystemid = null;
           	String[] columnModuleValues = null; 
           	for(int i = 0; i < length; i++){
           		columnModuleValues = columnModuleValue[i].split("\\^-\\^");
           		subsystemid = columnModuleValues[0].substring(0,columnModuleValues[0].indexOf("::")); 
          %>
          <fieldset>
            <legend ><font class="label">模块[<%=columnModuleValues[1] %>]配置</font></legend>
           
           <input name="moduleName<%=i %>" value="<%=columnModuleValues[1] %>" type="hidden">
          <table width="100%" height="30" border="0" cellpadding="0"
				cellspacing="0" class="thin">
            <tr>
            	<td>portlet唯一标识</td><td><input name="portalId<%=i %>" value="<%=i+1 %>" type="text" readonly/></td>
            	<td>portlet名称：</td><td><input name="portalName<%=i %>" value="<%=columnModuleValues[1] %>" type="text"/></td>
            	
            </tr>
            <tr>
            <td>是否多实例化：</td><td><select name="instanceable<%=i %>">
            	<option value="1" selected>多实例</option>
            	<option value="0">单实例</option>
            </select></td>
            <td>子系统id：</td><td><input name="subsystemid<%=i %>" value="<%=subsystemid %>" type="text"  readonly/>
            <select name="subsystemidstate<%=i %>">
            	<option value="1" selected>只读</option>
            	<option value="0">可修改</option>
            </select>
            </td>
            </tr>
            <tr>
            	<td >应用模块地址：</td><td colspan="3"><input size="80" name="appModuleSrc<%=i %>" value="<%=columnModuleValues[0] %>"  readonly type="text"/>
            		<select name="appModuleSrcstate<%=i %>">
		            	<option value="1" selected>只读</option>
		            	<option value="0">可修改</option>
		            </select>
            	</td>
			</tr>
			
			<tr>
            	<td >MAX-source-url：</td><td colspan="3"><input size="80" name="MAXsrc<%=i %>" value=""  type="text"/>
            		<select name="MAXsrcstate<%=i %>">
		            	<option value="1" selected>只读</option>
		            	<option value="0">可修改</option>
		            </select>		            
            	</td>
			</tr>
			
			<tr>
            	<td>Httpcontext：</td><td colspan="3"><input size="80" name="httpcontext<%=i %>" value="<%=basePath %>" type="text"/>
            		<select name="httpcontextstate<%=i %>">
		            	<option value="1" selected>只读</option>
		            	<option value="0">可修改</option>
		            </select>
            	</td>
			</tr>		
			<tr>
				<td>发布区域：</td><td colspan="1">
				<select name="issueArea<%=i %>">
					<option value="main">main</option>
					<% 
						if(columnModuleValues.length==3){
							String[] areaType = columnModuleValues[2].split(":");
							for(int j = 0; j < areaType.length; j++){
					%>
						<option value="<%=areaType[j] %>"><%=areaType[j] %></option>
					<%	
							}
						} 
					%>
					<option value="perspectiveContent" selected>perspectiveContent</option>
					
				</select>
				</td>
				<td colspan="2"><input name="" type="button" class="input" value="查看所选区域效果" 
					onClick="queryArea(<%=i %>)"/>
				</td>
              
            </tr>
            <tr >
            	<td>授权角色：</td>
            	<td colspan="3">
            		<input type="checkbox" value="administrator" name="role<%=i %>" checked>administrator
            		<input type="checkbox" value="guest" name="role<%=i %>" checked>guest
            		<input type="checkbox" value="power-user" name="role<%=i %>" checked>power-user
            		<input type="checkbox" value="user" name="role<%=i %>" checked>user
            	</td>
			</tr>
          </table>
          </fieldset>
           <br>
            <% 
            	}
            %>
          
	</form>
		<iframe name="hiddenFrame" height="0" width="0"></iframe>
	</body>
</html>

