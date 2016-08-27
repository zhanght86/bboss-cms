<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.platform.config.model.AuthorTableInfo,
	com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.security.authorization.AuthRole,
	com.frameworkset.platform.sysmgrcore.authorization.AppAuthorizationTable,
	com.frameworkset.common.poolman.DBUtil,
	com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%			
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String uId = accesscontroler.getUserID();
			ResourceManager resManager = new ResourceManager();
			String resId = resManager
					.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
			String curOrgId = request.getParameter("orgId");
			if (curOrgId == null)
				curOrgId = (String) request.getAttribute("orgId");
			String reFlush = "false";
			if (request.getAttribute("reFlush") != null) {
				reFlush = "true";
			}

			Integer currUserId = (Integer) session.getAttribute("currUserId");
			if (currUserId == null) {
				currUserId = Integer.valueOf("-1");
			}
			//String curOrgId = (String)session.getAttribute("orgId");
			String desc = (String) request.getParameter("pager.desc");
			String intervalType = (String) request.getParameter("intervalType");
			String taxmanager = (String) request.getParameter("taxmanager");
			String ischecked = "";
			if ((String) request.getAttribute("ischecked") == null) {
				ischecked = "";
			} else {
				ischecked = (String) request.getAttribute("ischecked");
			}

			String QueryuserName = null;
			String QueryuserRealname = null;
			if (request.getParameter("userName") == null) {
				QueryuserName = "";
			} else {
				QueryuserName = request.getParameter("userName");
			}
			if (request.getParameter("userRealname") == null) {
				QueryuserRealname = "";
			} else {
				QueryuserRealname = request.getParameter("userRealname");
			}
%>
<html>
<head>
<title>属性容器</title>
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js"
	type="text/javascript"></script>
<SCRIPT language="javascript">
var tempObj = null;
function changeRowColor(obj) {
   if(obj.flag == "true")
   {
   		obj.flag = "false";
   		obj.style.removeAttribute("backgroundColor");  
	    obj.style.color = 'black';
	    tempObj = null;
	    return ;
   }
   else
   {
   		obj.flag = "true";
   		obj.style.background='#191970';   //把点到的那一行变希望的颜色; 
   		obj.style.color = 'white';
   }
   if(tempObj!=null && tempObj.flag == "true"){
        tempObj.flag = "false";
        tempObj.style.removeAttribute("backgroundColor");  
	    tempObj.style.color = 'black';
   }
   tempObj = obj;
}
function dbClickChoose(obj){
	window.returnValue = obj.id;
	window.close();
}
function returnUserName(){
	var obj = null;
	var rows = document.all.table1.rows;
	for(var i=0;rows!=null && i<rows.length;i++){
		if(rows[i].flag == "true"){
			obj = rows[i];
			break;
		}
	}
	if(obj!=null){
		
		parent.W.document.getElementById("loginName").value = obj.id;
		parent.api.close();
		
		//window.returnValue = obj.id;
		//window.close();
	}else{
		parent.W.$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.select.one"/>');
	}
}
function queryUser()
{
	var intervalType = document.all("intervalType").value;
	userList.action="userList.jsp?orgId=<%=curOrgId%>&intervalType="+intervalType;
	userList.submit();	
}
</SCRIPT>
<body>
<div class="mcontent">
	<div id="searchblock">
		<div  class="search_top">
    		<div class="right_top"></div>
    		<div class="left_top"></div>
    	</div>
    	<div class="search_box">
    		<form name="userList" target="userList" method="post">
    			<input type="hidden" name="orgId" value="<%=curOrgId%>" />
    			<table width="98.1%" border="0" cellspacing="0" cellpadding="0">
    				<tr>
    					<td class="left_box"></td>
    					<td>
    						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
    							<tr>
    								<th><pg:message code="sany.pdp.personcenter.person.name"/>：</th>
    								<td><input type="text" name="userName" value="" size="15" class="w120" /></td>
    								<th><pg:message code="sany.pdp.personcenter.person.name.real"/>：</th>
    								<td><input type="text" name="userRealname" value="" size="15" class="w120" /></td>
    								<th><pg:message code="sany.pdp.personcenter.person.taxman"/>：</th>
    								<td>
    									<select name="taxmanager" class="w120">
											<option value="2" selected>--<pg:message code="sany.pdp.common.operation.select"/>--</option>
											<option value="1"><pg:message code="sany.pdp.personcenter.person.taxman"/></option>
											<option value="0"><pg:message code="sany.pdp.personcenter.person.notaxman"/></option>
										</select>
										&nbsp;&nbsp;
										<select name="intervalType" id="intervalType" class="w120" onchange="">
											<option value="0"><pg:message code="sany.pdp.not.recursion.query"/></option>
											<option value="1"><pg:message code="sany.pdp.recursion.query"/></option>
										</select>
    								</td>
    								<th>&nbsp;</th>
    								<td>
    									<a href="javascript:void(0)" class="bt_1"  name="search"  onclick="queryUser()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
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
	<br/>
	<div id="changeColor">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserListSn" keyName="UserListSn" />
		<pg:pager maxPageItems="15" scope="request" data="UserListSn" isList="false">
			<pg:param name="orgId" />
			<pg:param name="userName" />
			<pg:param name="userRealname" />
			<pg:param name="intervalType" />
			<pg:param name="istaxmanager" />
			
			<pg:equal actual="${UserListSn.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${UserListSn.itemCount}"  value="0">
				<table  id="table1" width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" >
					<pg:header>
						<th><pg:message code="sany.pdp.personcenter.person.loginname"/></th>
						<th><pg:message code="sany.pdp.personcenter.person.name.real"/></th>
						<th><pg:message code="sany.pdp.personcenter.person.istaxman"/></th>
						<th><pg:message code="sany.pdp.user.category"/></th>
						<th><pg:message code="sany.pdp.personcenter.person.post.belong.org"/></th>
						<th><pg:message code="sany.pdp.personcenter.person.mail.location"/></th>
					</pg:header>
					
						<pg:list>
							<tr id="<pg:cell colName="userName" defaultValue="" />"  onclick="changeRowColor(this);" ondblclick="dbClickChoose(this)" flag="false">
								<td><pg:cell colName="userName" defaultValue="" /></td>
								<td><pg:cell colName="userRealname" defaultValue="" /></td>
								<td>
									<pg:equal colName="istaxmanager" value="1"><pg:message code="sany.pdp.personcenter.person.taxman"/></pg:equal>
									<pg:equal colName="istaxmanager" value="0"><pg:message code="sany.pdp.personcenter.person.notaxman"/></pg:equal>
									<pg:equal colName="istaxmanager" value=""><pg:message code="sany.pdp.personcenter.person.notaxman"/></pg:equal>
								</td>
								<td>
									<dict:itemname type="userType" expression="{userType}" />
								</td>
								<td><pg:cell colName="org_Name" defaultValue="" /></td>
								<td>
									<pg:notnull colName="userEmail"><pg:cell colName="userEmail" defaultValue=" " /></pg:notnull>
									<pg:null colName="userEmail"><pg:message code="sany.pdp.personcenter.person.mail.null"/></pg:null>
									<pg:equal colName="userEmail" value=""><pg:message code="sany.pdp.personcenter.person.mail.null"/></pg:equal>
								</td>
							</tr>
						</pg:list>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
				
				<div class="btnarea" >
	 				<a href="javascript:void(0)" class="bt_1"  name="but"  onclick="returnUserName()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a> 
				 <div>
			</pg:notequal>
		</pg:pager>
	</div>
</div>

</body>
</html>

