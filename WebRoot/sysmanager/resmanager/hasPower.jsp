<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.ResourceAction"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	String resTypeId2 = request.getParameter("resTypeId2");
	String resId2 = request.getParameter("resId2");
	if(resId2 == null){
		resId2 = (String)request.getAttribute("resId2");
	}
	if(resTypeId2 == null){
		resTypeId2 = (String)request.getAttribute("resTypeId2");
	}
	
	ResourceManager resManager = new ResourceManager();
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	List roleOperList = ResourceAction.getRoleOperList(resId2, resTypeId2);
	List operList = resManager.getOperations(resTypeId2);
	List allRole = roleManager.getRoleList();
	if (operList == null) {
		operList = new ArrayList();
	}
	request.setAttribute("operList", operList);
	request.setAttribute("allRole",allRole);
	String stored = (String)request.getAttribute("stored");
	if(stored == null){
		stored = "0";
	}
	String roleId = "";
%>
<html>
	<head>
		<title>系统管理</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		<SCRIPT language="javascript">
			var resTypeId2 ="<%=resTypeId2%>";
			var resId2 ="<%=resId2%>";
			function changebox(opid){
				//alert(opid);
			}
			
			function store(){
				var s = "<%=stored%>";
				if(s != 1){
					roleList.action="../../resmanager/roleoper.do?method=storeRoleOperList";
				}else
					roleList.action="roleoper.do?method=storeRoleOperList";
				roleList.submit();	
			}
		</SCRIPT>
	</head>
	<body>
		<FORM name="roleList" method="post" >
			<table cellspacing="0" cellpadding="0" border="1" bordercolor="#EEEEEE" width=98%>
				<TR ><td colspan="2" align="center">角色及操作权限列表</td>
				</TR>
				<TR class="headercolor"><td align="center">角色名称</td><td align="center">操作列表</td>
				</TR>
				<pg:list requestKey="allRole" needClear="false">				
					<TR onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" >
						<td align="center">
							<pg:cell colName="roleName"/>
							<%
								//System.out.println("rowid=" + rowid);
								//String roleId = dataSet.getString(Integer.parseInt(rowid),"roleId");
								//System.out.println("roleId="+roleId);
								//String roleName = "";
								roleId = ((Role)allRole.get(Integer.parseInt(rowid))).getRoleId();
								//System.out.println("roleId="+roleId);
							%>
						</td>
						<td align="center">
							<pg:list requestKey="operList" needClear="false">
								<input name="opId" type="checkbox" 
								<%
						        	String opId = dataSet.getString(Integer.parseInt(rowid),"id");
						        	//System.out.print("opId="+opId);
						        	for(int i = 0; roleOperList != null && i < roleOperList.size(); i ++)
						        	{
						        		Roleresop op = (Roleresop)roleOperList.get(i);
						        		if(op.getId().getOpId().equals(opId) && op.getRole().getRoleId().equals(roleId))
						        		{
						        			out.println("checked");
						        			break;
						        		}
						        			
						        	}
						        %>						
								
								value="<%=roleId%>;<pg:cell colName="id"/>" onclick="changebox('<pg:cell colName="id"/>')">
								
								<pg:cell colName="name" />

							</pg:list>
						</td>
					</TR>
				</pg:list>
				<tr><td colspan="2" align="center"><input name="sub1" type="button" class="input" value="保存" onClick="store()">
				<input name="resTypeId" type="hidden" value="<%=resTypeId2%>">
				<input name="resId" type="hidden" value="<%=resId2%>">
				</td></tr>
			</table>
		</FORM>
		<%@include file="../sysMsg.jsp" %>
	</body>
</html>

