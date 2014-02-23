
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%><%
	/**
	  * 直属用户列表
	  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.RoleresopKey"%>
<%@page import="java.util.Map"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	OrgAdministratorImpl orgAdministratorImpl = new OrgAdministratorImpl();
	String curOrgId = accessControl.getChargeOrgId(); 
	String orgId=request.getParameter("orgId");
	
	//所选机构是否是当前用户所属机构
	boolean isCurOrg = curOrgId.equals(orgId);

	String resId2 = request.getParameter("resId2");//RES_ID
	String resTypeId2 = request.getParameter("resTypeId2");//RESTYPE_ID
	String resTypeName = request.getParameter("resTypeName");
	String title = request.getParameter("title");//??~~~
	String resName2 = request.getParameter("resName2");//RES_NAME
	
	//是否批量授予
	String isBatch = request.getParameter("isBatch");
	
	ResourceManager resManager = new ResourceManager();
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	 String isGlobal=request.getParameter("isGlobal");
    	if(isGlobal == null) isGlobal = "false";
	List list = isGlobal.equals("true")?resManager.getGlobalOperations(resTypeId2):resManager.getOperations(resTypeId2);
	request.setAttribute("list",list);
	
	OperManager operManager = SecurityDatabase.getOperManager();
	
%>
<html>
<head>    
 <title>用户列表</title>
 <script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
 <script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
 <script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/include/pager.js" type="text/javascript"></script>
 <script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../html/js/commontool.js"></script>
<script type="text/javascript" src="../../html/js/dialog/lhgdialog.js?self=false"></script>	
 <SCRIPT language="javascript">
 	var api = frameElement.api, W = api.opener;
	
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
	
	function saveRoleresop(obj){
		var roleIdAndOpId = obj.value;
		W.$.dialog.alert(roleIdAndOpId);
		var arr = roleIdAndOpId.split(":");
		var roleId = arr[0];
		var opid = arr[1]; 
		if(obj.checked){
			send_request("saveRoleresop.jsp?check=1&types=user&opid="+opid+"&resId=<%=resId2%>&roleId="+roleId+"&restypeId=<%=resTypeId2%>&resName=<%=resName2%>");
			W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.choose"/>');
		}else{
			W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.cancel"/>');
			send_request("saveRoleresop.jsp?check=0&types=user&opid="+opid+"&resId=<%=resId2%>&roleId="+roleId+"&restypeId=<%=resTypeId2%>&resName=<%=resName2%>");
		}
	}
	
	//批量保存
	function saveRoleresops(){
		var obj = document.getElementsByName("opId");
		//alert(obj.length);
		var checks = "";
		var un_checks = "";
		for(var count = 0; count < obj.length; count++){
			if(!obj[count].disabled){
				if(obj[count].checked){
					if(checks==""){
						checks = obj[count].value;
					}else{
						checks += "^#^" + obj[count].value;
					}
				}else{
					if(un_checks==""){
						un_checks = obj[count].value;
					}else{
						un_checks += "^#^" + obj[count].value;
					}
				}
			}
		}
		//alert("checks = " + checks);
		//alert("un_checks = " + un_checks);
		document.all("checks").value = checks;
		document.all("un_checks").value = un_checks;
		document.userForm.target = "saveHidden";
		document.userForm.action = "saveRoleresop.jsp?types=user&resId=<%=resId2%>&restypeId=<%=resTypeId2%>&isBatch=<%=isBatch%>";
		//alert(document.userForm.action);
		document.userForm.submit();
	}
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<body class="contentbodymargin" scroll="auto">

<div id="" align="center">
    <form name = "userForm" method="post" action="">	
    <input type="hidden" name="checks" value="">
    <input type="hidden" name="un_checks" value="">
    <input type="hidden" name="resName2" value="<%=resName2 %>">
    
	<input type="hidden" name="delcmsuser" value="delcmsuser" /><!--用于标识CMS中的用户管理-->
	<%
			if(request.getParameter("resName")!=null)
			{
			%>
	<div class="title_box">
	<strong>
			<%=request.getParameter("resName")==null?"":request.getParameter("resName")%>&nbsp;<pg:message code="sany.pdp.workflow.user.list"/>
	</strong>
	<%}%>
	</div>
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.OrgSubUserList" keyName="OrgSubUserList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10"
				  scope="request"  
				  data="OrgSubUserList" 
				  isList="false">
				  <pg:equal actual="${OrgSubUserList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:equal> 
					<pg:notequal actual="${OrgSubUserList.itemCount}"  value="0">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
			      <pg:header>
			      <!--设置分页表头-->
			      	<th><pg:message code="sany.pdp.user.login.name"/></th>	<input class="text" type="hidden" name="selectId">
					<th><pg:message code="sany.pdp.empower"/></th>
			      </pg:header>
			      <pg:param name="orgId"/>
				  <pg:param name="resName"/>
				  <pg:param name="resId2"/>
				  <pg:param name="resTypeId2"/>
				  <pg:param name="resTypeName"/>
				  <pg:param name="title"/>
				  <pg:param name="isBatch"/>
				  <pg:param name="types"/>
				  
			     		      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      <% 
			      	String userId = dataSet.getString("userId");
			      	boolean isOrgManager = false;
			      	if(isCurOrg){
			      		isOrgManager = orgAdministratorImpl.isOrgManager(userId);
			      	}
			      	String userName = dataSet.getString("userName");
			      	//是否拥有超级管理员角色
			      	boolean isAdminRole = accessControl.isAdmin(userName);
			      	Map hasOper = operManager.getResOperMapOfRole(userId,resId2,resTypeId2,"user");
			      	Map opMap = accessControl.getSourceUserRes_Role(orgId,userId,"",resId2,resTypeId2);
			      	//System.out.println("opMap = " + opMap.size());
			      %>
			      		<tr <%if(isOrgManager){out.print("disabled");} %> class="cms_data_tr" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'"  >	      				
					
			 				<td class="tablecells" align="center"><pg:cell colName="userName" defaultValue=""/>(<pg:cell colName="userRealname" defaultValue=""/>)</td>
						
					  		 <!-- OP_ID:id;ROLE_ID:userId -->
					  		 <td>
					  		 <pg:list requestKey="list" >
					  		 	<%
					  		 	String opid = dataSet.getString("id");
					  		 	
				  		 		String inputname = "unopId";
				  		 		String output = ""; 
				  		 		//System.out.println("userId = "+ userId+":"+userName + "   :	opid = " + opid);
					  		 	try
					  		 	{
					  		 		
					  		 		if(isAdminRole){
					  		 
					  		 			output = " title='"+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.resource.admin.title", request)+"'  checked disabled " ;
					  		 	 
					  		 		}
					  		 		else
					  		 		{
					  		 		//判断该资源是否受保护：true表示不受保护；false表示受保护的
						        	boolean isUnProtected = accessControl.isUnprotected(resId2,opid,resTypeId2);
						        	//判断是否是超级管理员拥有：true表示只有超级管理员才有权；false根据具体授权来决定
									boolean isExclude = accessControl.isExcluded(resId2,opid,resTypeId2);
									//只能是超级管理员拥有的权限
									if(isExclude){
										output = " disabled=\"true\" title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.purview.admin", request)+"\" ";
									}else if(isUnProtected){//不受保护的资源
										output = " disabled=\"true\" title=\""+RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.purview.resource.protect.no", request)+"\" checked ";
									}else{
					  		 			inputname = "opId";
					  		 			//System.out.println("else~~~~~~~~~~~~~~~~~~~~~~~");
						  		 		boolean flag = false;
					  		 		
					  		 				
				  		 				if(hasOper.size() > 0 && hasOper.containsKey(opid)){
				  		 					output = " checked ";
				  		 					flag = true;
				  		 				}
					  		 			
						  		 			
					  		 			if(opMap != null && opMap.size() > 0){
					  		 				
						  		 			String titles = (String)opMap.get(opid);
						  		 			if(titles== null || titles.equals(""))
						  		 			{
						  		 				
						  		 			}
						  		 			else
						  		 			{
						  		 				if(!flag)
						  		 				{
						  		 					output = " checked disabled ";
						  		 				}
						  		 				
						  		 				output += "title='"+titles+"'";
						  		 			}
					  		 			}
					  		 			
					  		 		}
					  		 		if(!accessControl.checkPermission(resId2,opid,resTypeId2))
						        	{
      									output += " disabled=\"true\" " ;
      								}
      								}
					  		 	}
					  		 	catch(Exception e)
					  		 	{
					  		 		e.printStackTrace();
					  		 	}
					  		 	%>
					  		 	<input name="<%=inputname %>" value='<%=userId %>#*#<pg:cell colName="id" />' type="checkbox" <%=output%>/>
					  		 	
					  		 	<pg:cell colName="name" />
					  		 </pg:list>
					  		 </td>
							
					  	</tr>			      		
			      </pg:list>
		
	</table>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
	<div align="center">
			<% 
			     	if(orgId != null && !"".equals(orgId)){
			     %>
			     <a class="bt_1" onclick="saveRoleresops()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
			     <% 
			     	}
			     %>
		</div>	
	</pg:notequal>
		</pg:pager>
	<iframe name="saveHidden" width="0" height="0"></iframe>
</form>	
</div>
</body>

</html>
