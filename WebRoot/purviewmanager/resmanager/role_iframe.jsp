<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*,com.frameworkset.platform.config.model.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.ResourceAction"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.tag.OperList" %>
<%@page import="java.util.Map"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String resId2 = request.getParameter("resId2");
	String resTypeId2 = request.getParameter("resTypeId2");
	ResourceInfo res =  com.frameworkset.platform.config.ConfigManager.getInstance().getResources().getResourceInfoByid(resTypeId2);
	
	String resTypeName = res != null? res.getName():"";
	String title = request.getParameter("title");
	String isBatch = request.getParameter("isBatch");
	String resName2 = request.getParameter("resName2");
	
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
	
%>
<html>
<head>
<title>授予角色</title>
</head>
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		
<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>

<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<SCRIPT language="javascript">
		var api = frameElement.api, W = api.opener;
		
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
					W.$.dialog.alert("<pg:message code='sany.pdp.no.httprequest'/>");
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
						W.$.dialog.alert("<pg:message code='sany.pdp.server.error'/>");
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
						//parent.$.dialog.alert("没有角色!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
		<div id="" align="center">
			<fieldset>
				<LEGEND align=left>
					<strong><FONT size=2><pg:message code="sany.pdp.common.help"/></FONT></strong>
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
							<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.help.content.access.yes"/>
						</td>
					</tr>
				</table>
				<%} else {

			%>
				<table align=left>
					<tr>
						<td>
							<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.help.content.access.no"/>
						</td>
					</tr>
				</table>
				<%}%>
			</fieldset>
			<br/>
			<FORM name="roleList" method="post">
				<input type="hidden" name="resTypeName" value="<%=resTypeName%>" />
				<input type="hidden" name="restypeId" value="<%=resTypeId2%>" />
				<input type="hidden" name="checks" value="">
			    <input type="hidden" name="un_checks" value="">
			    <input type="hidden" name="resName2" value="<%=resName2 %>">
				<pg:listdata dataInfo="RoleList" keyName="RoleList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="15" scope="request" data="RoleList" isList="false">
				<pg:equal actual="${ResSearchList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:equal> 
					<pg:notequal actual="${ResSearchList.itemCount}"  value="0">
					<pg:param name="resTypeId2" />
					<pg:param name="resId2" />
					<pg:param name="resTypeName" />
					<pg:param name="title" />
					<pg:param name="types" value="role" />
					<pg:param name="isBatch" />
					<pg:param name="resName2" />
					

					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
						<input name="resTypeId" type="hidden" value="<%=resTypeId2%>">
						<input name="resId" type="hidden" value="<%=resId2%>">
						<input name="title" type="hidden" value="<%=title%>">
						<TR>
							<th height='20' class="headercolor">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name"/>
							<br></th>
							<th height='20' class="headercolor">
								<pg:message code="sany.pdp.resourcemanage.operation.list"/>
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
							boolean isCimAdmin = "5".equals(roleId);
						%>
							</td>
							<td align="center">
								<pg:list requestKey="operList" needClear="false" declare="false">
						<%
						//判断给定的角色是否是超级管理员角色或部门管理员角色--administrator or orgmanager
						
						if (isAdmin || isOrgmanager || isCimAdmin) {//超级管理员角色或部门管理员角色
						%>								
									<input name="opId<%=rid%>" type="checkbox" value="<%=roleId%>#*#<pg:cell colName="id"/>"
									<% 
										if(isOrgmanager || isCimAdmin){
											out.print(" title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.resource.authorize.orgadmin.no", request)+"\" ");
										}else{
											out.print(" checked title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.resource.get.admin.auto", request)+"\" ");
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
										out.print(" disabled=\"true\" title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.purview.admin", request)+"\" ");
									}else if(isUnProtected){//不受保护的资源
										out.print(" disabled=\"true\" title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.purview.resource.protect.no", request)+"\" checked ");
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
					</table>
					 <div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
					 <div align="center">
					 	<a class="bt_1" onclick="saveRoleresop('<%=rid%>')" ><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
						<a class="bt_2" onclick="closeDlg();"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
					 </div>
					</pg:notequal>
				</pg:pager>
				<iframe name="saveHidden" width="0" height="0"></iframe>
			</FORM>

		</div>
	</body>
</html>
