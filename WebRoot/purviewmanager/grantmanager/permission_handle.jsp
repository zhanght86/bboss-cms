<%
/**
 * 
 * <p>Title: 操作项授权处理页面</p>
 *
 * <p>Description: 保存-操作项授权处理页面</p>
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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			String global = request.getParameter("global");
			boolean globalTag = true;//如果是全局资源，则不刷左边的资源树
			if (global == null) {
				global = "";
			}
			if (global.equals("yes")) {
				globalTag = false;
			}

			String[] opids = request.getParameterValues("alloper");
			String resTypeId = request.getParameter("resTypeId");
			String resid = request.getParameter("resid");
			String resname = request.getParameter("resName");
			
			String roleid = request.getParameter("roleid");
			String[] roleids = roleid.split(",");
			String role_type = request.getParameter("role_type");
			
			String isBatch = request.getParameter("isBatch");
			
			//System.out.println("role_type: " + role_type);
 
			//根据角色id取角色名称
			boolean success = false;
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			if(role_type!= null && role_type.equals("user") && roleids!=null && roleids.length>1)
			{
				//对用户进行批量资源操作授予时，执行这个函数
				success = roleManager.grantRoleresopForBatch(opids, resid, roleids,resTypeId, resname, role_type, true);
			}
			else
			{
				success = roleManager.grantRoleresop(opids, resid, roleids,resTypeId, resname, role_type, true);
			}

			
			%>

<script language="javascript">
	
	if(<%=success%>){
	    parent.W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.operation.success"/>',function(){
	    	  //刷新左边的资源树
	   <% if("false".equals(isBatch)){%>
	    if(<%=globalTag%>)
	    {
	    	//刷新树父父里子--resdefault.jsp
	    	//alert(parent.parent.frames[0].location);
	    	parent.parent.frames[0].location = parent.parent.frames[0].location;
	    }else{
	    	parent.parent.location = parent.parent.location;
	    }
	    <%}%>
	    },null,"<pg:message code='sany.pdp.common.alert'/>");
	}
	else
	{
		parent.W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.operation.fail"/>',function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	}
</script>
