<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager,
				com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl" %>
				
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	PurviewManager purviewManager = new PurviewManagerImpl();
	
	//授予权限给其他用户的用户ID
	String userId = request.getParameter("userId");
	//复制userId用户自身资源
	String userSelf = request.getParameter("userSelf");
	//复制userId用户角色资源
	String userRole = request.getParameter("userRole");
	//被授予权限的用户ID
	String checks = request.getParameter("checks");
	//所选复制权限用户的所属机构ID
	String curOrgId = request.getParameter("curOrgId");
	//是否复制岗位资源
	String userJob = request.getParameter("userJob");
	
	String[] checkUserIds = checks.split(",");
	
	//用户权限复制，flag=1时：当前用户的权限复制给所选用户；flag=2时：所选用户的权限复制给当前用户
	String flag = request.getParameter("flag");
	
	boolean state = false;
	if("1".equals(flag)){
		state = purviewManager.userResCopy(userId, checkUserIds, userSelf, userRole, userJob, curOrgId);
	}
	if("2".equals(flag)){
		state = purviewManager.userResCopySelf(userId, checkUserIds, userSelf, userRole, userJob, curOrgId);
	}
	
%>			
<script languge="javascript">
<%
	if(state){
%>
	alert("权限复制成功！");
<%	}else{%>
	alert("权限复制失败！");
<%	}%>
</script>
