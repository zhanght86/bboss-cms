<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.tag.OperList" %>
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
			 String isGlobal=request.getParameter("isGlobal");
    	if(isGlobal == null) isGlobal = "false";
			List list = isGlobal.equals("true")?resManager.getGlobalOperations(resTypeId2):resManager.getOperations(resTypeId2);
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

			%>
<html>
	<head>
		<title>系统管理</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
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
				//checked
				if(currCheck.checked){
					send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&resTypeName=<%=resTypeName%>");
				}
				//not checked
				else{
					send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&resTypeName=<%=resTypeName%>");
				}

				setCheck(currCheck,rid,priority);
			}

			function changebox2(opid,checked){
				var resid = document.forms[0].title.value;
				var restypeid = document.forms[0].resTypeId.value;
				send_request('saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked="+checked+"&title=<%=title%>&resTypeName=<%=resTypeName%>");
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
				<pg:listdata dataInfo="RoleList" keyName="RoleList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="18" scope="request" data="RoleList" isList="false">
					<pg:param name="resTypeId2" />
					<pg:param name="resId2" />
					<pg:param name="resTypeName" />
					<pg:param name="title" />

					<table cellspacing="1" cellpadding="0" border="0" width=98% class="thin">
						<input name="resTypeId" type="hidden" value="<%=resTypeId2%>">
						<input name="resId" type="hidden" value="<%=resId2%>">
						<input name="title" type="hidden" value="<%=title%>">
						<TR>
							<td colspan="2" align="center">
								<span class="STYLE1"><%=resTypeName%>>><%=title%></span>&nbsp;的角色及操作权限列表(直接选择复选框即可，系统实时保存)
							</td>
						</TR>

						<TR>
							<th height='20' class="headercolor">
								角色名称
							</th>
							<th height='20' class="headercolor">
								操作列表
							</th>
						</TR>
						<pg:list>
							<%boolean flag1;
							//判断给定的角色是否是超级管理员角色
							flag1 = accesscon.isAdministratorRole(dataSet.getString("roleName"));
							if (flag1 == true) {

							%>
							<TR onMouseOver="this.className='mouseover'" onMouseOut="this.className= 'mouseout'">
								<td align="center">
									<pg:cell colName="roleName" />
									<%rid = rowid;
									roleId = dataSet.getString("roleId");
									List hasOper = operManager.getOperResRoleList(roleId, title,
											resTypeId2);

							%>
								</td>
								<td align="center">
									<pg:list requestKey="operList" needClear="false" declare="false">
										<input name="opId<%=rid%>" type="checkbox"
											<%
						        	String opId = dataSet.getString("id");
						        	//System.out.println("opId="+opId);
						        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
						        	{
						        		//Roleresop op = (Roleresop)roleOperList.get(i);
						        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
						        		//System.out.println(i+":::opId2="+op.getId().getOpId());
						        		//System.out.println(i+":::rId="+op.getId().getRoleId());
						        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
						        		{
						        			out.println("checked");
						        			break;
						        		}

						        	}
						        %>
											value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" checked disabled="true">

										<pg:cell colName="name" />
										<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
									</pg:list>
								</td>
							</TR>
							<%} else {
									boolean flag2;
						        	//判断资源是否是未受保护的资源
									flag2 = accesscon.isUnprotected(title,resTypeId2);
									boolean flag4;
						        	//判断资源所有操作是否只有超级管理员才能访问
									flag4 = accesscon.isExcluded(title,resTypeId2);
									if(flag2==true){
							%>
								<TR onMouseOver="this.className='mouseover'" onMouseOut="this.className= 'mouseout'">
									<td align="center">
										<pg:cell colName="roleName" />
										<%rid = rowid;
									roleId = dataSet.getString("roleId");
									List hasOper = operManager.getOperResRoleList(roleId, title,
											resTypeId2);
					
									%>
									</td>
									<td align="center">
										<pg:list requestKey="operList" needClear="false" declare="false">
											<input name="opId<%=rid%>" type="checkbox"
												<%
							        	String opId = dataSet.getString("id");
							        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
							        	{
							        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
							        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
							        		{
							        			out.println("checked");
							        			break;
							        		}
							        	}
							        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" checked disabled="true">
											<pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
										</pg:list>
									</td>
								</TR>
							<%
			       			} else if(accesscon.isExcluded(title,resTypeId2)==true){%>
			       				<TR onMouseOver="this.className='mouseover'" onMouseOut="this.className= 'mouseout'">
									<td align="center">
										<pg:cell colName="roleName" />
										<%rid = rowid;
									roleId = dataSet.getString("roleId");
									List hasOper = operManager.getOperResRoleList(roleId, title,
											resTypeId2);
					
									%>
									</td>
									<td align="center">
										<pg:list requestKey="operList" needClear="false" declare="false">
											<input name="opId<%=rid%>" type="checkbox"
												<%
							        	String opId = dataSet.getString("id");
							        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
							        	{
							        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
							        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
							        		{
							        			out.println("checked");
							        			break;
							        		}
							        	}
							        %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" checked disabled="true">
											<pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
										</pg:list>
									</td>
								</TR>
			       			 <%}else{%>
			       				<TR onMouseOver="this.className='mouseover'" onMouseOut="this.className= 'mouseout'">
									<td align="center">
										<pg:cell colName="roleName" />
										<%rid = rowid;
									roleId = dataSet.getString("roleId");
									List hasOper = operManager.getOperResRoleList(roleId, title,
											resTypeId2);
					
									%>
									</td>
									<td align="center">
										<pg:list requestKey="operList" needClear="false" declare="false">
										<%
											boolean flag3;
								        	//判断资源是否是未受保护的资源
											flag3 = accesscon.isUnprotected(title, dataSet.getString("id"),resTypeId2);
											if(flag3==true){
											
										%>
											<input name="opId<%=rid%>" type="checkbox"
												<%
									        	String opId = dataSet.getString("id");
									        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
									        	{
									        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
									        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
									        		{
									        			out.println("checked");
									        			break;
									        		}
									        	}
									       		 %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" checked disabled="true">
											<pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
										<%}else if(accesscon.isExcluded(title, dataSet.getString("id"),resTypeId2)==true){%>
											<input name="opId<%=rid%>" type="checkbox"
												<%
									        	String opId = dataSet.getString("id");
									        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
									        	{
									        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
									        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
									        		{
									        			out.println("checked");
									        			break;
									        		}
									        	}
									       		 %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')"  disabled="true">
											<pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
										<%}else{%>
											<input name="opId<%=rid%>" type="checkbox"
												<%
									        	String opId = dataSet.getString("id");//System.out.println(resId2+"|"+opId+"|"+resTypeId2);
									        	if(!accesscontroler.checkPermission(resId2,opId,resTypeId2))
									        	{
			      									out.print(" disabled=\"true\"" );
			      								}
									        	for(int i = 0; hasOper != null && i < hasOper.size(); i ++)
									        	{
									        		RoleresopKey op = (RoleresopKey)hasOper.get(i);
									        		if(op.getRoleId().equals(roleId) && op.getOpId().equals(opId))
									        		{
									        			out.println("checked");
									        			break;
									        		}
									        	}
									       		 %>
												value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox(this,'<%=rid%>','<pg:cell colName="priority" defaultValue=""/>','<%=roleId%>;<pg:cell colName="id"/>')" >
											<pg:cell colName="name" />
											<input name="priority<%=rid%>" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">
											<%}%>
										</pg:list>
									</td>
								</TR>
							<%}
							}%>
						</pg:list>
						<tr>
							<td colspan="2" align="right">
								 <pg:index />
							</td>
						</tr>
					</table>
				</pg:pager>
			</FORM>

		</div>
	</body>
</html>

