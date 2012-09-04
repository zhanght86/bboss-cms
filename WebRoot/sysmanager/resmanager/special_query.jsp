<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.frameworkset.platform.config.model.ResourceInfo"%>
<%@ page import="com.frameworkset.platform.config.ResourceInfoQueue"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager"%>
<%@ page import="com.frameworkset.platform.config.model.Operation"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);


//查询资源列表
List resQueue = null;
ResourceManager resManager = new ResourceManager();
resQueue = resManager.getResourceInfos();
 for(int i = 0; i < resQueue.size(); i ++)
        {
            ResourceInfo res = (ResourceInfo)resQueue.get(i);
            if(!res.isUsed())
                continue;
            String listId = res.getId();
            String listName = res.getName();
		 }
request.setAttribute("resList",resQueue);
%>
<html>
<head>    
 <title>属性容器</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">
function getSpecial(){
	var special = document.all.special.value;
	if(special == "unprotected"){
		//document.all.theTitle.innerHTML = "<b>系统中未受保护的资源<b>";
	}else if(special == "exclude"){
		//document.all.theTitle.innerHTML = "<b>只有超级管理员才有的资源<b>";
	}
}
function sub(){
	
	var resId = orgreslist.resId.value;
	if(resId == "" || resId.length<1 || resId.replace(/\s/g,"")=="")
	{
		
	}
	else
	{
			var re_ = /^\w+$/; 
			if(!re_.test(resId))
			{
			$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.res.token.nospecialcharacter"/>');
			return false; 
			}
	}
	var resName = orgreslist.resName.value;
	if(resName == "" || resName.length<1 || resName.replace(/\s/g,"")=="")
	{
		
	}
	else
	{
			var re_ =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
			if(!re_.test(resName))
			{
				$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.res.name.nospecialcharacter"/>');
				return false; 
			}
	}
	
		getSpecial()
		orgreslist.action = "special_querylist.jsp?special="+document.all.special.value+"&opId="+document.all.operategroup.value;
		orgreslist.submit();
}
function getOpGroup(){
	getopergroup.location.href = "../user/resChange.jsp?restypeId="+document.all.restypeId.value;
}
function reset_()
{
getopergroup.location.href = "../user/resChange.jsp?restypeId=";

$("#reset").click();
}
</SCRIPT>
<body onload="sub()">
	<div class="mcontent">
		<div id="searchblock">
		<div  class="search_top">
    		<div class="right_top"></div>
    		<div class="left_top"></div>
    	</div>
    	<div class="search_box">
    		<form  name = "orgreslist" id="orgreslist" method="post" target="querylist">
    			<table width="98.5%" border="0" cellspacing="0" cellpadding="0">
    				<tr>
      					<td class="left_box"></td>
      					<td>
      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
      							<tr>
      								<th><pg:message code="sany.pdp.sysmanager.resource.search.select"/>：</th>
      								<td>
      									<select name="special" id="special" onchange="" class="w120">
											<option value="unprotected"><pg:message code="sany.pdp.sysmanager.resource.unsafe"/></option>
											<option value="exclude"><pg:message code="sany.pdp.sysmanager.resource.admin"/></option>
										</select>
      								</td>
      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.token"/>：</th>
      								<td><input type="text" name="resId" id="resId" value="" class="w120" /></td>
      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.name"/>：</th>
      								<td><input type="text" name="resName" id="resName" value="" class="w120" /></td>
      							</tr>
      							<tr>
      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.type"/>：</th>
      								<td>
      									<select id="restypeId" name="restypeId" onchange="getOpGroup()" class="w120">
											<option value="">--<pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.type.select"/>--</option>
				      						<pg:list requestKey="resList" needClear="false">
					    					<option value="<pg:cell colName="id"/>">
					        					<pg:cell colName="name"/>
											</option>
											</pg:list>
										</select>
      								</td>
      								<th><pg:message code="sany.pdp.personcenter.person.res.operation.type"/>：</th>
      								<td>
      									<select id="operategroup" name="operategroup" class="w120">
											<option value="">--<pg:message code="sany.pdp.sysmanager.resource.operation.type.select"/>--</option>
										</select>
      								</td>
      								<th>&nbsp;</th>
      								<td>
      									<a href="javascript:void(0)" class="bt_1" name="search"  onclick="sub()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
      									<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="reset_()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
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
	</div>

<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
</body>

</html>
