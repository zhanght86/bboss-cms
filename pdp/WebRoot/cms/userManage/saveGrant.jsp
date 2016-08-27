<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.RoleManagerImpl,
				com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);	
	String userId = control.getUserID();
	String currRoleId = (String)session.getAttribute("currRoleId");
	String idString = request.getParameter("resIds");//资源ID
	//System.out.println("idString = " + idString);
	List resIds = null;
	if(idString!=null && !idString.equals("")){
		String[] res = idString.trim().split(":");
		resIds = new ArrayList();
		for(int i=0;i<res.length;i++){
			String[] temps = res[i].split(";");
			resIds.add(temps);
		}
	}
	String resTypeId = request.getParameter("resTypeId");
	String roleType = request.getParameter("roleType");
	
	String[] opids = new String[]{"visible"};
	String[] roleids = new String[]{currRoleId};
	
	boolean state = new RoleManagerImpl().saveResGrant(opids,resIds,roleids,resTypeId,roleType,userId);
	
	if(state){
%>
	<SCRIPT language="javascript">
		alert("操作成功");
		parent.location = parent.location;
	</script>
<%	}else{%>
	<SCRIPT language="javascript">
	alert("操作失败");
	</script>
<%	}%>
