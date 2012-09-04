<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.ResourceAction"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.tag.OperList" %>
<%@page import="java.util.Map"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
			AccessControl accesscontroler = AccessControl.getInstance();
    		accesscontroler.checkAccess(request, response);
			String resTypeId2 = request.getParameter("resTypeId2");
			String resId2 = request.getParameter("title");//request.getParameter("resId2");
			String resTypeName = request.getParameter("resTypeName");
			String title = request.getParameter("title");
			if (resId2 == null) {
				resId2 = (String) request.getAttribute("resId2");
			}
			if (resTypeId2 == null) {
				resTypeId2 = (String) request.getAttribute("resTypeId2");
			}
			OperManager operManager = SecurityDatabase.getOperManager();
			ResourceManager resManager = new ResourceManager();
			List operList = resManager.getOperations(resTypeId2);
			if (operList == null) {
				operList = new ArrayList();
			}
			request.setAttribute("operList", operList);
			String stored = (String) request.getAttribute("stored");
			if (stored == null) {
				stored = "0";
			}
			String roleId = "";
			String rid = "";
			
			//是否批量资源授予
			String isBatch = request.getParameter("isBatch");
			String resName2 = request.getParameter("resName2");

			%>
<html>
	<head>
		<title>系统管理</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/windows.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<SCRIPT language="javascript">
			var resTypeId2 ="<%=resTypeId2%>";
			var resId2 ="<%=resId2%>";
			var http_request = false;
			//初始化，指定处理的函数，发送请求的函数
			function send_request(url){
				http_request = false;
				//开始初始化XMLHttpRequest对象
				if(window.XMLHttpRequest){//Mozilla
					http_request = new XMLHttpRequest();
					if(http_request.overrideMimeType){//设置MIME类别
						http_request.overrideMimeType("text/xml");
					}
				}
				else if(window.ActiveXObject){//IE
					try{
						http_request = new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							http_request = new ActiveXObject("Microsoft.XMLHTTP");
						}catch(e){
						}
					}
				}
				if(!http_request){
					alert("不能创建XMLHttpRequest对象");
					return false;
				}
				http_request.onreadystatechange = processRequest;
				http_request.open("GET",url,true);
				http_request.send(null);
			}

			function processRequest(){
				if(http_request.readyState == 4){                  
					if(http_request.status == 200){
						//alert(http_request.responseText);
					}
					else{
						alert("对不起，服务器错误");
					}
				}
			}

			function changebox(currCheck,rid,priority,opid){
				var resid = document.forms[0].title.value;
				var restypeid = document.forms[0].resTypeId.value;
				//alert(resid);
				//checked
				//if(currCheck.checked){
				//	send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&resTypeName=<%=resTypeName%>");
				//}
				//not checked
				//else{
				//	send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&resTypeName=<%=resTypeName%>");
				//}

				setCheck(currCheck,rid,priority);
			}

			function changebox2(opid,checked){
				//var resid = document.forms[0].title.value;
				//var restypeid = document.forms[0].resTypeId.value;
				//alert(resid)
				//send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked="+checked+"&title=<%=title%>&resTypeName=<%=resTypeName%>");
			}

			function setCheck(currCheck,rid,priority)
			{
			   	var o = document.getElementsByName(currCheck.name);
				var prioritylist = document.getElementsByName("priority"+rid);
				if (currCheck.checked==true && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;

						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1)&& priority.substring(1,2) == v.substring(1,2) )
						{
							o[i].checked=true;
							changebox2(o[i].value,1);
							//alert("3:"+o[i].value);
							//o[i].disabled=true;
						}
					}

					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(1,2) != v.substring(1,2) )
						{
							o[i].checked=false;
							changebox2(o[i].value,0);
							//alert("4:"+o[i].value);
							//o[i].disabled=false
						}
					}
				}
				if (currCheck.checked==false  && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if ( v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1) )
						{
							if ( o[i].checked==true ){
								//currCheck.checked==true;
								//o[i].disabled=false;
							}
						}
					}

				}
			}
			
			function saveRoleresop(rid){
				var checks = "";
				var un_checks = "";
				if(document.getElementsByName("opId"+rid)){
					var oplen = document.getElementsByName("opId"+rid).length;
					//alert(oplen); 
					for(var count = 0; count <= rid; count ++){
						var obj = document.getElementsByName("opId"+count);
						for(var len = 0; len < oplen; len ++){
							if(!obj[len].disabled){
								if(obj[len].checked){
									if(checks == ""){
										checks = obj[len].value;
									}else{
										checks += "^#^" + obj[len].value;
									}
								}else{
									if(un_checks==""){
										un_checks = obj[len].value;
									}else{
										un_checks += "^#^" + obj[len].value;
									}
								}
							}
						}		
					}
					//alert(checks);
					//alert(un_checks);
					document.all("checks").value = checks;
					document.all("un_checks").value = un_checks;
					document.roleList.target = "saveHidden";
					document.roleList.action = "saveRoleresop.jsp?types=role&resId=<%=resId2%>&restypeId=<%=resTypeId2%>&isBatch=<%=isBatch%>";
					document.roleList.submit();
				}else{
					alert("没有角色！");
					return false;
				}
			}

		</SCRIPT>
		<style type="text/css">
<!--
.STYLE1 {color: #0000FF}
-->
        </style>
	</head>
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder" align="center">
			<fieldset>
				<LEGEND align=left>
					<strong><FONT size=2>帮助</FONT></strong>
				</LEGEND>
				<%boolean flag;
			AccessControl accesscon = AccessControl.getInstance();
			flag = accesscon.allowIfNoRequiredRoles(resTypeId2);
			//System.out.println("hehelalal" + flag);
			if (flag == true) {

			%>
				<table>
					<tr>
						<td>
							该资源在没有授予任何角色或用户（即下面的复选框都处于未被选中状态）的情况下,允许访问该资源，一旦授给某个用户或角色后，其它角色或用户也必须授权后才能访问
						</td>
					</tr>
				</table>
				<%} else {

			%>
				<table align=left>
					<tr>
						<td>
							必须对该资源授权才可以访问该资源，在没有授权的情况下是不能访问该资源
						</td>
					</tr>
				</table>
				<%}%>
			</fieldset>
			<FORM name="roleList" method="post">
				<input type="hidden" name="resTypeName" value="<%=resTypeName%>" />
				<input type="hidden" name="restypeId" value="<%=resTypeId2%>" />
				<input type="hidden" name="checks" value="">
			    <input type="hidden" name="un_checks" value="">
			    <input type="hidden" name="resName2" value="<%=resName2 %>">
				<pg:listdata dataInfo="RoleList" keyName="RoleList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="15" scope="request" data="RoleList" isList="false">
					<pg:param name="resTypeId2" />
					<pg:param name="resId2" />
					<pg:param name="resTypeName" />
					<pg:param name="title" />
					<pg:param name="types" value="role" />
					<pg:param name="isBatch" />
					<pg:param name="resName2" />
					

					<table cellspacing="1" cellpadding="0" border="0" width=98% class="thin">
						<input name="resTypeId" type="hidden" value="<%=resTypeId2%>">
						<input name="resId" type="hidden" value="<%=resId2%>">
						<input name="title" type="hidden" value="<%=title%>">
						<TR>
							<td colspan="2" align="center">
								<span class="STYLE1"><%=resTypeName%>>><%=title%></span>&nbsp;的角色及操作权限列表(选择要授予的资源请及时保存)
							<br></td>
						</TR>

						<TR>
							<th height='20' class="headercolor">
								角色名称
							<br></th>
							<th height='20' class="headercolor">
								操作列表
							<br></th>
						</TR>
						<pg:list>
						
						<TR onMouseOver="this.className='mouseover'" onMouseOut="this.className= 'mouseout'">
							<td align="center">
								<pg:cell colName="roleName" />
						<%
							rid = rowid;
							roleId = dataSet.getString("roleId");
							//List hasOper = operManager.getOperResRoleList(roleId, title,resTypeId2);
							Map hasOper = operManager.getResOperMapOfRole(roleId,resId2,resTypeId2,"role");
							boolean isAdmin = "1".equals(roleId);
							boolean isOrgmanager = "3".equals(roleId);
						%>
							</td>
							<td align="center">
								<pg:list requestKey="operList" needClear="false" declare="false">
						<%
						//判断给定的角色是否是超级管理员角色或部门管理员角色--administrator or orgmanager
						
						if (isAdmin || isOrgmanager) {//超级管理员角色或部门管理员角色
						%>								
									<input name="opId<%=rid%>" type="checkbox" value="<%=roleId%>#*#<pg:cell colName="id"/>"
									<% 
										if(isOrgmanager){
											out.print(" title=\"不能对部门管理员进行资源授予\" ");
										}else{
											out.print(" checked title=\"超级管理员角色自动拥有该资源\" ");
										}
									%> 
									disabled />
									
						<%
						} else {//非超级管理员角色
						%>
									<input name="opId<%=rid%>" type="checkbox" value="<%=roleId%>#*#<pg:cell colName="id"/>" 
						<%
									//操作ID
						        	String opId = dataSet.getString("id");//System.out.println(resId2+"|"+opId+"|"+resTypeId2);
						        	//判断该资源是否受保护：true表示不受保护；false表示受保护的
						        	boolean isUnProtected = accesscontroler.isUnprotected(resId2,opId,resTypeId2);
						        	//判断是否是超级管理员拥有：true表示只有超级管理员才有权；false根据具体授权来决定
									boolean isExclude = accesscontroler.isExcluded(resId2,opId,resTypeId2);
									//只能是超级管理员拥有的权限
									if(isExclude){
										out.print(" disabled=\"true\" title=\"只有超级管理员才有权\" ");
									}else if(isUnProtected){//不受保护的资源
										out.print(" disabled=\"true\" title=\"未受保护的资源\" checked ");
									}else{
										//当前操作员没有权限不允许对该资源进行授权操作
							        	if(!accesscontroler.checkPermission(resId2,opId,resTypeId2))
							        	{
	      									out.print(" disabled=\"true\" " );
	      								}
						        		if(hasOper.size() > 0 && hasOper.get(opId) != null)
						        		{
						        			out.println("checked");
						        		}
					        		}
					    %>
									 />
						<% 
						}
						%>
								<pg:cell colName="name" />
									</pg:list>
								</td>
							</TR>
						</pg:list>
						<tr>
							<td colspan="2" align="right">
								 <pg:index />
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<input name="submit1" type="button" class="input" value="保存" onclick="saveRoleresop('<%=rid%>')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="col" type="button" class="input"  value="关闭" onclick="window.close();"/>
							</td>
						</tr>
					</table>
				</pg:pager>
				<iframe name="saveHidden" width="0" height="0"></iframe>
			</FORM>

		</div>
	</body>
</html>
