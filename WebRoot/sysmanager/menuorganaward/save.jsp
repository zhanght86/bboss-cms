<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.RoleManagerImpl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userId = accesscontroler.getUserID();
		String idString = request.getParameter("treeids");//资源ID
		List resIds = null;
		if(idString!=null && !idString.equals("")){
			String[] res = idString.trim().split(":");
			resIds = new ArrayList();
			for(int i=0;i<res.length;i++){
				String[] temps = res[i].split(";");
				resIds.add(temps);
			}
		}
			String restypeid = request.getParameter("restypeid");//资源类型ID
			String roletype = request.getParameter("roletype");//用户/角色
			String roleidsstr = request.getParameter("roleids");//用户/角色ID
			String[] roleids = null;
			if(roleidsstr!=null && !roleidsstr.equals("")){
				roleids = roleidsstr.trim().split(":");
			}
			String opidsstr = request.getParameter("opids");
			String[] opids = null;
			if(opidsstr!=null && !opidsstr.equals("")){
				//if(opidsstr.equals("readorgname"))
					//opids = new String[]{opidsstr,"read","write"};
				//else
					opids = new String[]{opidsstr};
			}
			
			
			//System.out.println("resIds: "+idString);
			//System.out.println("restypeid: "+restypeid);
			//System.out.println("roletype: "+roletype);
			//System.out.println("roleids: "+roleidsstr);
			//System.out.println("opids: "+opidsstr);
			boolean flag = new RoleManagerImpl().saveResGrant(opids,resIds,roleids,restypeid,roletype,userId);
			if(flag){
%>
				<SCRIPT language="javascript">
					alert("保存成功!")
				</SCRIPT>
<%
			}else{
%>
				<SCRIPT language="javascript">
					alert("保存失败!")
				</SCRIPT>
<%
			}
%>
<html>
	<SCRIPT language="javascript">
		function subForm(){
			if("orgunit"=="<%=restypeid%>"){
				form1.action="orgChooseTree.jsp";
				form1.target="orgChooseTree";
			}else{
				form1.action="menuTree.jsp";
				form1.target="menuTree";
			}
			form1.submit();
		}
	</SCRIPT>
	<body onload="subForm()">
		<form action="" target="" method="post" name="form1">
			<input type="hidden" id="idString" name="idString" value="<%=roleidsstr%>">
			<input type="hidden" id="type" name="type" value="<%=roletype%>">
		</form>
	</body>
</html>

