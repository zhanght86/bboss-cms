<%
/*
 * <p>Title: 角色授权删除处理页面</p>
 * <p>Description: 角色授权删除处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
 %>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%
	
    String roleId = request.getParameter("roleDelId");	
    String id = request.getParameter("id");	
   // System.out.println("id" + id);
	//System.out.println("roleId" + roleId);
	boolean flag = true;
	if(roleId!=null){
		String roleIds[] =roleId.split("\\,") ;
		String idso[] =id.split("\\,") ;
		//System.out.println("liumeiyu" + roleIds[0]);
		UserManager userManager = SecurityDatabase.getUserManager();
		try{
			userManager.delAlotUserRole(idso,roleIds);
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
	 }
	 out.print(flag);
%>
