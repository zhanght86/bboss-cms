<%
/**
 * 
 * <p>Title: 离散用户调入处理页面</p>
 *
 * <p>Description: 离散用户调入机构处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,
				 com.frameworkset.platform.sysmgrcore.manager.LogManager,
				 com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager"%>
				
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
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
	
	
	boolean state = false;
	if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){
	//调入多个机构开关为false时执行下面代码
		if(orgIdUser!=null && !"".equals(orgIdUser)){
			//boolean state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);//删除用户主机构与岗位,部门管理员
			boolean state2 = false;
			if(ConfigManager.getInstance().getConfigBooleanValue("isdelUserRes", true)){//调离用户时删除用户所有资源
				orgManager.deleteOrg_UserJob(orgIdUser, userId);
				state2 = userManager.deleteBatchUserRes(userId);
			}else{
				state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);
			}
			if(state2){
				if(!"orgunit".equals(orgIds)){//如果是将用户调入离散用户就不需要保存主机构与岗位关系
					state = userManager.storeBatchUserOrg(userId, orgId, true);//保存用户主机构与岗位
				}else{
					state = true;
				}
			}
		}else{
			state = userManager.storeBatchUserOrg(userId, orgId, true);//保存用户主机构与岗位
		}
	}else{
		if(orgIdUser!=null && !"".equals(orgIdUser)){
			if("0".equals(flag)){//flag的值为1时删除当前机构的用户
				boolean state2 = orgManager.deleteOrg_UserJob(orgIdUser, userId);
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
	
	if(orgIdUser!=null && !"".equals(orgIdUser)){
		Organization orgold = orgManager.getOrgById(orgIdUser);	
		if("orgunit".equals(orgIds)){//如果条件成立则是将用户调入离散用户
			for(int j = 0; j < userId.length; j++){
				User user = userManager.getUserById(userId[j]);
				String userName = control.getUserName();
				String operContent="";        
			    String operSource=control.getMachinedID();
			    String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
			    LogManager logManager = SecurityDatabase.getLogManager(); 
				operContent=userName + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.dispatch.out.org.to.free", new Object[] {orgold.getOrgName(), user.getUserName()}, request);
			    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
			}
		}else{
			for(int i = 0; i < orgId.length; i++){
				Organization org = orgManager.getOrgById(orgId[i]);
				//--用户调入其他机构记录日志
				for(int j = 0; j < userId.length; j++){
					User user = userManager.getUserById(userId[j]);
					String userName = control.getUserName();
					String operContent="";        
				    String operSource=control.getMachinedID();
				    String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
				    LogManager logManager = SecurityDatabase.getLogManager(); 
					operContent=userName + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.dispatch.out.org.to.org", new Object[] {orgold.getOrgName(), user.getUserName(), org.getOrgName()}, request);
				    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
			    }
			    //-------------
			}
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
			    String openModle=RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.user.manage", request);
			    LogManager logManager = SecurityDatabase.getLogManager(); 
				operContent=userName + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.dispatch.out.free.to.org", new Object[] {user.getUserName(), org.getOrgName()}, request);
			    logManager.log(control.getUserAccount() ,operContent,openModle,operSource,"");       
		    }
		    //-------------
		}
	}
%>
		
<html>
<head>
<title>调入离散用户</title>
	
<script>
	var api = parent.frameElement.api, W = api.opener;
	if(<%=state%>){
		W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		//parent.window.close();
		//parent.window.returnValue="ok";
	}else{
		W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		//alert("调动失败,请单击'删除用户冗余'按钮清除冗余，再确定调入！");
	}
</script>

</head>

<body>
</body>

</html>
