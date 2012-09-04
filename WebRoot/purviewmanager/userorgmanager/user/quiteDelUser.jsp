<%
/*
 * <p>Title: 删除用户</p>
 * <p>Description: 删除用户</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-20
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<script type="text/javascript" src="../../../include/jquery-1.4.2.min.js"></script>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.sysmgrcore.manager.LogManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.entity.User,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>

<%
	//System.out.println("彻底删除！！！！");
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	UserManager userManager = SecurityDatabase.getUserManager();
	
	String userId = request.getParameter("checks");
	String orgId = request.getParameter("orgId");
	
	String[] userIds = userId.split(",");
	String delUserIds = "";

	String userNamesNo = "";
	
		
	//日志记录start
	String curUserName = accesscontroler.getUserName();
	String operContent = "";        
    String operSource = accesscontroler.getMachinedID();
    String openModle = "用户管理";
    
    LogManager logManager = SecurityDatabase.getLogManager(); 
    OrgManager orgManager = SecurityDatabase.getOrgManager();
    
    String orgname = orgManager.getOrgById(orgId).getRemark5();
    
    long startdate = System.currentTimeMillis();
    for(int i = 0; i < userIds.length; i++){
    	User user = userManager.getUserById(userIds[i]);
    	String userOrgInfo = userManager.userOrgInfo(accesscontroler,userIds[i]);
    	if("".equals(userOrgInfo)){
    		operContent=curUserName +" 从机构： "+ orgname +" 中彻底删除了用户: "+user.getUserName(); 
    
			logManager.log(accesscontroler.getUserAccount() ,operContent,openModle,operSource,""); 
			
			if("".equals(delUserIds)){
				delUserIds = userIds[i];
			}else{
				delUserIds += "," + userIds[i];
			}
		}else{
			if("".equals(userNamesNo)){
				userNamesNo = "以下用户删除失败:\\n" + user.getUserRealname() + ":" + userOrgInfo;
			}else{
				userNamesNo += "\\n" +  user.getUserRealname() + ":" + userOrgInfo;
			}
		}
	}
	long enddate = System.currentTimeMillis();
	//日志记录end
	//System.out.println((enddate - startdate)/1000);
	String[] delUserId = delUserIds.split(",");
	
	
	boolean state = userManager.deleteBatchUser(delUserId);
	
%>

<script language="javascript">

	<%
	if("".equals(userNamesNo)){
	if(state){%>
		//parent.parent.win.close();
		$.dialog.alert("<pg:message code="sany.pdp.common.operation.success"/>",function(){
			parent.window.location.reload();
		},null,"<pg:message code='sany.pdp.common.alert'/>")
		//alert("删除用户成功！");
		
		
	<%}else{%>
		//parent.parent.win.close();
		$.dialog.alert("<pg:message code="sany.pdp.common.operation.fail.nocause"/>",function(){
			parent.window.location.reload();
		},null,"<pg:message code='sany.pdp.common.alert'/>")
		
		//parent.window.location.reload();
	<%}}else{%>
		$.dialog.alert("<%=userNamesNo%>",function(){
			parent.window.location.reload();
		},null,"<pg:message code='sany.pdp.common.alert'/>")
		
		//parent.window.location.reload();
	<%}%>
</script>
