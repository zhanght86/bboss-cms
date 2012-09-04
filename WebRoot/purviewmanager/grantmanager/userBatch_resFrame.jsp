<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@page import="com.frameworkset.platform.security.AccessControl,
			com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl,
			java.util.*,
			com.frameworkset.platform.resource.ResourceManager,
			com.frameworkset.platform.config.ConfigManager"%>
<%@page import="com.frameworkset.platform.config.model.ResourceInfo"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	 AccessControl accesscontroler = AccessControl.getInstance();
	 accesscontroler.checkManagerAccess(request,response);
	 //当前系统
	 String curSystem = accesscontroler.getCurrentSystem();
	 
	 String checks = request.getParameter("checks");
	 String[] id = checks.split(",");
     UserManager userManager = SecurityDatabase.getUserManager();
     OrgManagerImpl orgImpl = new OrgManagerImpl();	
     String idStr = "";
     String usern = "";
     String userna = "";
     for(int i = 0;i < id.length; i++)
     {
     	idStr += id[i]+",";
     	
     	User user = userManager.getUserById(id[i]);
     	usern= user.getUserRealname();
     	userna += usern + ",";
     }
	 if(idStr.length() > 1)
     idStr = idStr.substring(0,idStr.length()-1);
     if(userna.length() > 1)
     userna = userna.substring(0,userna.length()-1); 
     
     ResourceManager resManager = new ResourceManager();
	
	 List list = resManager.getResourceInfos();
	
	 //加开关，如果不允许超级管理员之外的用户进行菜单授权，当前用户又不是超级管理员，则去掉菜单项，请注意，是超级管理员，重复，不是啰嗦！
	 boolean state = ConfigManager.getInstance().getConfigBooleanValue("enablecolumngrant", true) && !accesscontroler.getUserID().equals("1");
	
	 if(list == null)
		list = new ArrayList();
	 request.setAttribute("resTypeList",list);
     
     
     //如果选择的用户有系统管理员，则弹出提示请用户重新选择。
     String orgId = request.getParameter("orgId");
     //当前用户所属机构ID
     String curUserOrgId = accesscontroler.getChargeOrgId();
     
     
     boolean tag = false;
     String adminUsers = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.authorize.resource.no", request) + " :\\n";
     String userNames = "";
     int orgManagerCount = 0;
     int noOrgManagerCount = 0;
     for(int j=0;j<id.length; j++){
		User adminUser = userManager.getUserById(id[j]);
		if("".equals(userNames)){
			userNames = adminUser.getUserRealname(); 
		}else{
			userNames += "," + adminUser.getUserRealname(); 
		}
		if(accesscontroler.isAdminByUserid(id[j])){//有超级管理员角色		    
			tag = true;						
			adminUsers += adminUser.getUserName() + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.admin.yes", request) + " \\n";
		}
		
		//没有管理员角色, 但是给自己授权
		if(accesscontroler.getUserID().equals(id[j])){
			tag = true;
			adminUsers += adminUser.getUserName() + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.authorize.to.self.no", request) + " \\n";
		}
		
		 //是部门管理员, 也不允许授权
		 // 允许部门管理员批量权限授予  baowen.liu 2008-3-21
	      boolean managerOrgs = orgImpl.isCurOrgManager(orgId, id[j]);
	      if(managerOrgs){
	      	orgManagerCount++;
	      }else{
	      	noOrgManagerCount++;
	      }
	      if(managerOrgs && curUserOrgId.equals(orgId) && !accesscontroler.isAdmin()){
	         tag = true;
		     adminUsers += adminUser.getUserName() + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.org.admin.equals", request) + " \\n";
	      }
     }
     
     if(tag)
     {
     	%>
     	<SCRIPT LANGUAGE="JavaScript">
     	var api = frameElement.api, W = api.opener;
		W.$.dialog.alert("<%=adminUsers%>", function() {
			api.close();
		}); 
		</script>
		<%
     }else if(orgManagerCount > 0 && noOrgManagerCount > 0){
     %>
     	<SCRIPT LANGUAGE="JavaScript">
     	var api = frameElement.api, W = api.opener;
		W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.user.authorize.admin.and.user.no"/>', function() {
			api.close();
		}); 
		</script>
     <%
     }else
     {
     
%>
<html>
<head>
<title>批量权限授予给用户【<%=userNames %>】</title>
<script type="text/javascript" src="../../include/tabs.js"></script>
<link type="text/css" rel="stylesheet" href="../../include/tabstyle.jsp" />
</head> 
<body>
	<div style="height: 10px">&nbsp;</div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="userbatch_resFrame" selectedTabPaneId="purview-orgunit" skin="sany">
				<pg:list requestKey="resTypeList" needClear="false">
				
	      			<pg:equal colName="auto" value="true">
	      			<%
	      			ResourceInfo resourceInfo = (ResourceInfo)dataSet.getOrigineObject();
	      			boolean isAppend = resourceInfo.containSystem(curSystem);
	      			if(isAppend){
	      			boolean isOrgManager = false;
	      			if(orgManagerCount > 0){
	      				isOrgManager = true;
	      			}
	      			String idTab = "purview-" + dataSet.getString("id");
	      			String iframeid = "iframe-" + dataSet.getString("id");
	      			String name = dataSet.getString("name");
	      			name = resourceInfo.getName(request);
	      			boolean stateOrgColumn = (idTab.trim().equals("purview-orgunit") || idTab.trim().equals("purview-column"));
	      			StringBuffer link = new StringBuffer()
	      				.append(dataSet.getString("resource")).append("?currRoleId=").append(idStr)
		      			.append("&orgId=").append(orgId)
		      			.append("&role_type=user")
		      			.append("&isBatch=true");
	      			//String link = dataSet.getString("resource")+"?userId="+idStr+"&orgId="+orgId+"&isOrgManager="+isOrgManager;
	      			System.out.println(link.toString());
	      			if(!(idTab.trim().equals("purview-column") && !accesscontroler.getUserID().equals("1")&& !state)){
	      				if(!isOrgManager && stateOrgColumn){
	      			%>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="<%=idTab%>" tabTitle="<%=name%>" lazeload="true">
						<tab:iframe id="<%=iframeid%>" src="<%=link.toString()%>" frameborder="0" scrolling="no" width="98%" height="600">
						</tab:iframe>
					</tab:tabPane>
<!-------------------------------------------------------------------------------------------------------------------------------->
						<%}else if(isOrgManager){%>
					<tab:tabPane id="<%=idTab%>" tabTitle="<%=name%>" lazeload="true">
						<tab:iframe id="<%=iframeid%>" src="<%=link.toString()%>" frameborder="0" scrolling="no" width="98%" height="600">
						</tab:iframe>
					</tab:tabPane>
					<%}} }%>
				  </pg:equal>					
	  			</pg:list>	
				</tab:tabContainer>			
			</td>
		</tr>
	</table>	
  <iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>

	<%
	}
	%>
