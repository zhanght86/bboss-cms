<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>





<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil,com.frameworkset.common.poolman.PreparedDBUtil" %>
<%@ page import="com.frameworkset.platform.config.ConfigManager" %>

<%
 			AccessControl accesscontroler = AccessControl.getInstance();
	        accesscontroler.checkAccess(request, response);
	        String userId = accesscontroler.getUserID();
	        PreparedDBUtil db = new PreparedDBUtil();
		    db.preparedSelect("select count(1) from td_sm_user");
		    db.executePrepared();
		    String usercount = String.valueOf(db.getInt(0,0));
		    
		    String userOrgType = (String)request.getParameter("userOrgType");
		    String userName = (String)request.getParameter("userName");
		    String userRealname = (String)request.getParameter("userRealname");
		    if(userOrgType == null)
		    {
		    	userOrgType = "";
		    }
		    if(userName == null)
		    {
		    	userName = "";
		    }
		    if(userRealname == null)
		    {
		    	userRealname = "";
		    }
		    
			//当前用户是否拥有超级管理员权限与部门管理员权限
			boolean isAdminOrOrgManager = false;
			//是否是管理员
			boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(accesscontroler.getUserID());
			boolean isAdmin = accesscontroler.isAdmin();
			if(isAdmin || isOrgManager){
				isAdminOrOrgManager = true;
			}	
%>

<html >
<head>				
	<tab:tabConfig/>
		<title>属性容器</title>
		<script src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
		<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
	<script language="JavaScript" src="common.js" type="text/javascript"></script>
	

	<script src="${pageContext.request.contextPath}/include/security.js"></script>
	<SCRIPT language="javascript">	
	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	
	function getUserInfo(e,userId,qstring)
	{
		//var qstring = document.forms[0].queryString.value;	
		if (jsAccessControl.setBackColor(e))
		{		
			getPropertiesToolbar().location.href="userquery_toolbar.jsp?userId="+userId+"&qstring="+qstring;
		}
	}
	
	function getUserInfo2(e,userId,qstring)
	{
		var curruserId = "<%=userId%>";
		if (jsAccessControl.setBackColor(e))
		{		
			var url = "";
		    if(userId==curruserId) {
		    	url = "<%=request.getContextPath()%>/sysmanager/user/userInfo2.jsp?userId="+userId+"&qstring="+qstring;
		    } else {
		    	url = "<%=request.getContextPath()%>/sysmanager/user/userInfo4.jsp?userId="+userId+"&qstring="+qstring;
		    }
    		$.dialog({title:'<pg:message code="sany.pdp.sysmanager.user.info.detail"/>',width:600,height:400, content:'url:'+url});   	
		}
	}
		
	function actionOnmouseover(e){	
		e.style.backgroundColor = "#8080FF";
	}
	
	function actionOnmouseup(e){
		e.style.backgroundColor = "#BEC0C2";
	}
	
	
	function queryUser()
	{	
		//if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
			//alert("用户名称和用户实名必须输入一个!!!");
			//return;
		//}
		var userName = userList.userName.value;
		if(userName == "" || userName.length<1 || userName.replace(/\s/g,"")=="")
		{
			
		}
		else
		{
				//var re_ = /^\w+$/; 
				//if(!re_.test(userName))
				{
				//alert('登陆名称中不能有非数字、字母、下划线的字符');
				//return false; 
				}
		}
		
		
		var userRealname = userList.userRealname.value;
		if(userRealname == "" || userRealname.length<1 || userRealname.replace(/\s/g,"")=="")
			{
			
			}
		else
			{
				//var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
				//if(!re.test(userRealname))
				{
				//alert('真实名称中不能有非数字、中文、字母的字符');
				//return false; 
				}
			}
		
		
		var userOrgType = document.all("userOrgType").value;
		$.secutiry.dosubmit("userList",
				"../user/userquery_content_tab.jsp?userOrgType="+userOrgType+"&userId=<%=userId%>",
						null,
						"${pageContext.request.contextPath}");
		//userList.action="../user/userquery_content_tab.jsp?userOrgType="+userOrgType+"&userId=<%=userId%>";
		//userList.submit();	
	}
		
	function queryUserInfo(){
		var userOrgType = document.all("userOrgType").value;
		var userName = document.all("userName").value;
		var userRealname = document.all("userRealname").value;
		var url = "../user/ireport/showJasperReport.jsp?userMore=true&userOrgType="+userOrgType+"&userName="+userName+"&userRealname="+userRealname;
        window.open(url);
		//window.showModalDialog(url,window,"dialogWidth:"+(620)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
	}
	function advQueryUser()
	{	
		document.location.href="../user/advQuery.jsp"	
	}
	//---------------------------------------------------------
	function query_User()
	{	
		
		//user_List.action="../user/userquery_content_tab.jsp"
		//user_List.submit();	
		
		$.secutiry.dosubmit("user_List",
				"../user/userquery_content_tab.jsp",
						null,
						"${pageContext.request.contextPath}");
	}
	
	function cleanAll(){
		document.userList.reset();
		document.all.userName.value = "";
		document.all.userRealname.value = "";
	}
	</SCRIPT>	
</head>
<body>
	<sany:menupath menuid="userquery"/>
	<%if(isAdminOrOrgManager){ %>
	<div class="mcontent">
		<div id="searchblock">
			<div  class="search_top" >
	    		<div class="right_top"></div>
	    		<div class="left_top"></div>
     		</div>
     		<div class="search_box">
     			<form name="userList" id="userList" method="post" >
     				<table width="98.5%" border="0" cellspacing="0" cellpadding="0">
     					<tr>
	      					<td class="left_box"></td>
	      					<td>
	      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
	      							<tr>
	      								<th><pg:message code="sany.pdp.sysmanager.user.loginname"/>：</th>
	      								<td><input type="text" name="userName" value="<%=userName%>" class="w120" /></td>
	      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.realname"/>：</th>
	      								<td><input type="text" name="userRealname" value="<%=userRealname%>" class="w120" /></td>
	      								<th><pg:message code="sany.pdp.sysmanager.user.belong.type"/>：</th>
	      								<td>
	      									<%if(ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",false)){ %>
				           					<select name="userOrgType" >     
				           						<%if("two".equals(ConfigManager.getInstance().getConfigValue("userquerytype"))){%>
				           						<option selected value="hasMainOrg"><pg:message code="sany.pdp.sysmanager.user.belong.org"/></option>
												<option value="dis"><pg:message code="sany.pdp.sysmanager.user.belong.free"/></option>  
				           						<%}else{%>      					
												<option selected value="hasMainOrg"><pg:message code="sany.pdp.sysmanager.user.belong.org.main"/></option> 
												<option value="noMainOrg"><pg:message code="sany.pdp.sysmanager.user.belong.org.nomain"/></option> 
												<option value="dis"><pg:message code="sany.pdp.sysmanager.user.belong.free"/></option>  
												<%}}%>
											</select> 
	      								</td>
	      								<th>&nbsp;</th>
	      								<td>
	      									<a href="javascript:void(0)" class="bt_1" id="search"  onclick="queryUser()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      							<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="cleanAll()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
			      							<a href="javascript:void(0)" class="bt_2" id="mimeograph" onclick="queryUserInfo()"><span><pg:message code="sany.pdp.common.operation.print"/></span> </a>
	      								</td>
	      							</tr>
	      						</table>
	      					</td>
	      					<td class="right_box"></td>
	      				 </tr>
     				</table>
     			</form>
     		</div>
     		<div class="search_bottom">
				<div class="right_bottom"></div>
				<div class="left_bottom"></div>
			</div>
		</div>
		
		<div class="title_box">
			<div class="rightbtn">
			</div>
			<strong><pg:message code="sany.pdp.workflow.user.list"/></strong>
		</div>
		
		<div id="changeColor">
			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList" keyName="UserSearchList" />
			<pg:pager maxPageItems="5" id="UserSearchList" scope="request" data="UserSearchList" isList="false">
				<pg:param name="orgId"/>
				<pg:param name="userName"/>
				<pg:param name="userRealname"/>
				<pg:param name="advQuery" />
				<pg:param name="userId" />
				<pg:param name="job_name"/>	
				<pg:param name="userOrgType"/>		
				
				
				
					<table width="98.5%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
						<pg:header>
							<th><pg:message code="sany.pdp.sysmanager.user.loginname"/></td>
							<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.realname"/></td>	
							<th onclick="sortBy('userName')">工号</th>
							<th onclick="sortBy('userName')">身份证</th>
							<th onclick="sortBy('userName')">电话</th>							
							<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.belongorg"/></td>
							<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.mobile"/></td>
							<th>密码过期时间</th>	
							<th >密码有效期</th>		
						</pg:header>
						<pg:notify>
							<div class="nodata">
							<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						</pg:notify>
						<pg:list>
							<tr onDBLclick=getUserInfo2(this,'<pg:cell colName="userId" defaultValue=""/>','<pg:querystring encode="true"/>') >
								<td>
									<pg:cell colName="userName" defaultValue="" />
								</td>
								<td>
									<pg:cell colName="userRealname" defaultValue="" />
								</td>
								<td><pg:cell colName="workNumber"
										defaultValue="" /></td>
								<td><pg:cell colName="USER_IDCARD"
										defaultValue="" /></td>
								<td><pg:cell colName="userMobiletel1"
										defaultValue="" /></td>											
								<td>
									<pg:cell colName="orgName" defaultValue="" />
								</td>
								<td height='20' class="tablecells" nowrap="nowrap" >
									<pg:notnull colName="userMobiletel1"><pg:cell colName="userMobiletel1" defaultValue=" "/></pg:notnull>
									<pg:null colName="userMobiletel1"><pg:message code="sany.pdp.sysmanager.user.phoneno.no"/></pg:null>
									<pg:equal colName="userMobiletel1" value=""><pg:message code="sany.pdp.sysmanager.user.phoneno.no"/></pg:equal>
								</td>	
								<td><pg:cell colName="passwordExpiredTime" dateformat="yyyy-MM-dd HH:mm:ss"
										defaultValue="" /></td>		
								<td><pg:cell colName="passwordDualedTime" 
										defaultValue="0" />天</td>	
								</tr>
								
						</pg:list>
					</table>
					<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			
			</pg:pager>
		</div>
	</div>
	<script>
		if("<%=userOrgType%>" != ""){
			document.all.userOrgType.value="<%=userOrgType%>";
		}
	</script>
<%}else{ %>
<div align="center"><pg:message code="sany.pdp.common.nopurview"/></div>
<%} %>

</body>
</html>
