<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@page import="com.frameworkset.common.poolman.DBUtil,com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl,java.util.*,com.frameworkset.platform.resource.ResourceManager,com.frameworkset.platform.config.ConfigManager"%>		
<%
	 AccessControl accesscontroler = AccessControl.getInstance();
	 accesscontroler.checkManagerAccess(request,response);
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
     String adminUsers = "以下用户不能进行资源授予:\\n";
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
			adminUsers += adminUser.getUserName() + " 是超级管理员\\n";
		}
		
		//没有管理员角色, 但是给自己授权
		if(accesscontroler.getUserID().equals(id[j])){
			tag = true;
			adminUsers += adminUser.getUserName() + " 不能给自己授予角色\\n";
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
		     adminUsers += adminUser.getUserName() + " 与当前用户是同等级部门管理员\\n";
	      }
     }
     
     if(tag)
     {
     	%>
     	<SCRIPT LANGUAGE="JavaScript">
		alert("<%=adminUsers%>"); 
		window.close();
		</script>
		<%
     }else if(orgManagerCount > 0 && noOrgManagerCount > 0){
     %>
     	<SCRIPT LANGUAGE="JavaScript">
		alert("部门管理员和普通用户不能一起批量权限授予！"); 
		window.close();
		</script>
     <%
     }else
     {
     
%>
<html>
<head>
<title>批量权限授予给用户【<%=userNames %>】</title>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<script type="text/javascript" src="../../../include/tabs.js"></script>
<link type="text/css" rel="stylesheet" href="../../../include/tabstyle.jsp" />
</head> 
<body bgcolor="#F7F8FC" >
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="userbatch_resFrame" selectedTabPaneId="purview-orgunit">
				<pg:list requestKey="resTypeList" needClear="false">
				
	      			<pg:equal colName="auto" value="true">
	      			<%
	      			boolean isOrgManager = false;
	      			if(orgManagerCount > 0){
	      				isOrgManager = true;
	      			}
	      			String idTab = "purview-" + dataSet.getString("id");
	      			String iframeid = "iframe-" + dataSet.getString("id");
	      			String name = dataSet.getString("name");
	      			boolean stateOrgColumn = (idTab.trim().equals("purview-orgunit") || idTab.trim().equals("purview-column"));
	      			String link = dataSet.getString("resource")+"?userId="+idStr+"&orgId="+orgId+"&isOrgManager="+isOrgManager;
	      			if(!(idTab.trim().equals("purview-column") && !accesscontroler.getUserID().equals("1"))){
	      				if(!isOrgManager && stateOrgColumn){
	      			%>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="<%=idTab%>" tabTitle="<%=name%>" lazeload="true">
						<tab:iframe id="<%=iframeid%>" src="<%=link%>" frameborder="0" scrolling="no" width="98%" height="600">
						</tab:iframe>
					</tab:tabPane>
<!-------------------------------------------------------------------------------------------------------------------------------->
						<%}else if(isOrgManager){%>
					<tab:tabPane id="<%=idTab%>" tabTitle="<%=name%>" lazeload="true">
						<tab:iframe id="<%=iframeid%>" src="<%=link%>" frameborder="0" scrolling="no" width="98%" height="600">
						</tab:iframe>
					</tab:tabPane>
					<%}} %>
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
