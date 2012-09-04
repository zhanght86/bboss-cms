<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.RoleOperAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%	  
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
	
		String resTypeId = request.getParameter("resTypeId");
		String resId = request.getParameter("resId");
		String checked = request.getParameter("checked");
		String title = request.getParameter("title");
		String resTypeName = request.getParameter("resTypeName");

		int usernum = Integer.parseInt(request.getParameter("usernum"));
		int rolenum = Integer.parseInt(request.getParameter("rolenum"));
		int orgnum = Integer.parseInt(request.getParameter("orgnum"));
		int allnum = usernum + rolenum + orgnum;System.out.println(usernum+"--"+rolenum+"--"+orgnum);
		
		boolean flag = false;
		
		RoleManager roleManager = SecurityDatabase.getRoleManager();
		String roleId = "";
		String restypeId = "channel";
		String roletype = "user";
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<allnum;j++)
			{
				String[] opids = request.getParameterValues("opId" + (i*allnum + j));
				System.out.println("opId" + (i*allnum + j));System.out.println(opids.length);
				for(int k=0;k<opids.length;k++)
				{
					String[] temstr = opids[k].split(";");
					roleId = temstr[0];
					opids[k] = temstr[1];
				}System.out.println(j);
				if(j<usernum)
				{
					roletype = "user";					
				}
				else if(j<usernum + rolenum && j>=usernum)
				{
					roletype = "role";
				}else
				{
					roletype = "organization";
				}
				if(i == 1)
					restypeId = "channeldoc";//写死
				flag = roleManager.grant(opids,resId,roleId,restypeId,resTypeName,roletype,"1");
			}
		}
		if(flag)
		{
%>
<SCRIPT LANGUAGE="JavaScript">
alert("保存成功！");
</SCRIPT>
<%
	}
%>
