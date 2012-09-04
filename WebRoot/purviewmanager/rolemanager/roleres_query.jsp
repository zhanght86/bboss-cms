<%
/*
 * <p>Title: 角色资源查询页面</p>
 * <p>Description: 角色资源查询页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-28
 * @author baowen.liu
 * @version 1.0
 *
 */
 %>
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>



<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

	String name = (String)request.getParameter("name");
	String type = (String)request.getParameter("type");
    
	//查询资源列表
	List resQueue = null;
	ResourceManager resManager = new ResourceManager();
	resQueue = resManager.getResourceInfos();
	
	request.setAttribute("resList",resQueue);


%>
<html>
	<head>    
 		<title>用户机构权限资源查询</title>
		<SCRIPT language="javascript">
		
		function sub(){
			var url="roleres_querylist.jsp?restypeId="
				+ document.all("resourcetype").value + "&resId="
				+ document.all("resId").value + "&opId="
				+ document.all("operategroup").value+ "&resName="
				+ document.all("resName").value ;
				
			//userreslist.submit();
			
			document.getElementById("getopergroup").src = url;
		}
		
		function getOperateType(id){
				getopergroup.location.href = "resChange.jsp?restypeId="+id;
		}
	
	function doreset() {
		document.all("resId").value = "";
		document.all("resName").value = "";
		document.all("resourcetype").value = "";
		document.all("operategroup").value = ""
   	}
</SCRIPT>
	</head>

	<body>
		<div style="height: 10px">&nbsp;</div>
		<div class="mcontent">
			<div id="searchblock">
		    	<div  class="search_top">
		    		<div class="right_top"></div>
		    		<div class="left_top"></div>
	      		</div>
	      		<div class="search_box">
	      			<form name = "userreslist" method="post" target="forDocList">
	      			<table width="98.4%" border="0" cellspacing="0" cellpadding="0">
	      				<tr>
	      					<td class="left_box"></td>
	      					<td>
	      						<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table2">
									<tr>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.token" />：</th>
										<td><input type="text" name="resId" size="15" class="w120" /></td>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.name" />：</th>
										<td><input type="text" name="resName" size="15" class="w120" /></td>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.type" />：</th>
										<td>
											<select class="select" id="resourcetype" name="resourcetype" onchange="getOperateType(this.options[this.selectedIndex].value)" class="w120" >
										      	<option value="">--<pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.type.select" />--</option>
										      	<pg:list requestKey="resList" needClear="false">
												    	<option value="<pg:cell colName="id"/>">
												        	<pg:cell colName="name"/>
														</option>
												</pg:list>
											</select>
										</td>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.operation" />：</th>
										<td>
											<select class="select" name="operategroup" id="operategroup" class="w120">
										      	<option value="">--<pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.operation.select" />--</option>
												
											</select>
										</td>
										<td colspan="2">
											<a href="javascript:void(0)" class="bt_1" id="search"  onclick="sub()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a> 
											<a href="javascript:void(0)" class="bt_2" id="reset1" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a> 
											<input type="reset" id="reset" style="display: none" />
										</td>
									</tr>
								</table>
	      					</td>
	      					<td class="right_box"></td>
	      				</tr>
	      			</table>
	      			</form>
	      		</div>
	      		<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
	      	</div>
		
	<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
	</body>

</html>
