<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,
				 com.frameworkset.platform.sysmgrcore.manager.LogManager,
				 com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	UserManager userManager = SecurityDatabase.getUserManager();
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	//被调出的机构id,从离散用户调入到机构时orgIdUser为空
	String orgIdUser = request.getParameter("orgId");
	String flag = request.getParameter("flag");
	
	String userIds = request.getParameter("userIds");
	//调入到的机构id
	String orgIds = request.getParameter("orgIds");
	String[] userId = userIds.split(",");
	String[] orgId = orgIds.split(",");
	
	if(orgIdUser!=null && !"".equals(orgIdUser)){
		Organization orgold = orgManager.getOrgById(orgIdUser);	
		for(int i = 0; i < orgId.length; i++){
			Organization org = orgManager.getOrgById(orgId[i]);
			//--用户调入其他机构记录日志
			for(int j = 0; j < userId.length; j++){
				User user = userManager.getUserById(userId[j]);
				String userName = control.getUserName();
				String operContent="";        
			    String operSource=control.getMachinedID();
			    String openModle="用户管理";
			    LogManager logManager = SecurityDatabase.getLogManager(); 
				operContent=userName +" 将机构【"+orgold.getOrgName()+"】 中的用户：【"+ user.getUserName()+ "】 调入到机构：【" + org.getOrgName() + "】中"; 
			    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
		    }
		    //-------------
		}
	}else{
		for(int i = 0; i < orgId.length; i++){
			Organization org = orgManager.getOrgById(orgId[i]);
			//--用户调入机构记录日志
			for(int j = 0; j < userId.length; j++){
				User user = userManager.getUserById(userId[j]);
				String userName = control.getUserName();
				String operContent="";        
			    String operSource=control.getMachinedID();
			    String openModle="用户管理";
			    LogManager logManager = SecurityDatabase.getLogManager(); 
				operContent=userName +" 将离散用户：【"+ user.getUserName()+ "】 调入到机构：【" + org.getOrgName() + "】中"; 
			    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
		    }
		    //-------------
		}
	}
	boolean state = false;
	if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){
	//调入多个机构开关为false时执行下面代码
		if(orgIdUser!=null && !"".equals(orgIdUser)){
			boolean state2 = false;
			if(ConfigManager.getInstance().getConfigBooleanValue("isdelUserRes", true)){//调离用户时删除用户所有资源
				orgManager.deleteOrg_UserJob(orgIdUser, userId);
				state2 = userManager.deleteBatchUserRes(userId);
			}else{
				state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);
			}
			
			if(state2){
				state = userManager.storeBatchUserOrg(userId, orgId, true);//保存用户主机构与岗位
			}
		}else{
			state = userManager.storeBatchUserOrg(userId, orgId, true);//保存用户主机构与岗位
		}
	}else{
		if(orgIdUser!=null && !"".equals(orgIdUser)){
			if("0".equals(flag)){//flag的值为1时保留当前机构的用户
				boolean state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);//删除用户主机构与岗位,部门管理员
				if(state2){
					state = userManager.storeBatchUserOrg(userId, orgId, false);
				}
			}else{
				state = userManager.storeBatchUserOrg(userId, orgId, false);//保存用户岗位
			}
		}else{
			state = userManager.storeBatchUserOrg(userId, orgId, true);
		}
	}
%>
		
<html>
<head>
<title>调入离散用户</title>
	
<script>
	if(<%=state%>){
		alert("调入机构成功！");
		parent.window.close();
		parent.window.returnValue="ok";
	}else{
		alert("调入机构失败,请单击'删除用户冗余'按钮清除冗余，再确定调入！");
	}
</script>

</head>

<body>
</body>

</html>
