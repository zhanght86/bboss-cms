<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.config.model.ResourceInfo"%>
<%@ page import="com.frameworkset.platform.security.AccessControl
				,com.frameworkset.platform.config.ConfigManager"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

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
String isUserRes = request.getParameter("isUserRes");//控制查询条件开关,为is时,查询用户为当前用户
String userName = "";
if("is".equals(isUserRes))
	userName = accesscontroler.getUserAccount();
	
String typeName = request.getParameter("typeName");	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>    
 <title>权限资源查询</title>
<SCRIPT language="javascript">

function isUserRes(){
	
	var resId = userreslist.resId.value;
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
	var resName = userreslist.resName.value;
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
	

	
	
	var userName = "<%=userName%>";
	userreslist.action="../user/userres_querylist.jsp?opId="+document.all("operategroup").value
						+"&type=user&name="+userName+"&typeName=<%=typeName%>";
	userreslist.submit();
	
}
   
function sub(){
	
	
	//定义当前操作的对象提示
	var type = document.all("type").value;
	var typeName ;
	if(type == "user"){
		typeName = '<pg:message code="sany.pdp.sys.user"/>';
	}else if(type == "role"){
		typeName = '<pg:message code="sany.pdp.sys.role"/>';
	}else if(type == "org"){
		typeName = '<pg:message code="sany.pdp.sys.org"/>';
	}else if(type == "job"){
		typeName = '<pg:message code="sany.pdp.sys.post"/>';
	}else if(type == "orgjob"){
		typeName = '<pg:message code="sany.pdp.sys.org.post"/>';
	}
	if(document.userreslist.name.length == 0 || document.userreslist.name.value == "") 
	{
		$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.res.name.input.begin" />' + typeName + '<pg:message code="sany.pdp.personcenter.person.res.name.input.end" />');
		document.userreslist.name.focus();	
		return false;	
	}

	var resId = userreslist.resId.value;
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

	var resname = document.userreslist.resName.value;
	
	if(resname == "" || resname.length<1 || resname.replace(/\s/g,"")=="")
	{
		
	}
	
	else
	{
			var re_ =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
			if(!re_.test(resname))
			{
				$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.res.name.nospecialcharacter"/>');
				return false; 
			}
	}
	
	
	//document.all.currentname.innerHTML = "当前查询的<B>"+typeName+"</B>为："+document.all.name.value;
	if(typeName=='<pg:message code="sany.pdp.sys.user"/>'){
		typeName = "USER";
	}
	userreslist.action="../user/userres_querylist.jsp?opId="+document.all("operategroup").value+"&typeName="+typeName;
	userreslist.submit();
}

function reset_(){
	$("#operategroup").load( "../user/resChangeAjax.jsp?restypeId=");
	$("#reset").click();
}

function del(){
	var type = document.getElementsByTagName("select")[0].value;
	delop.location.href = "../resmanager/delRedundance.jsp?type="+type;
 	var typeName;
	if(type == "user"){
		typeName = '<pg:message code="sany.pdp.sys.user"/>';
	}else if(type == "role"){
		typeName = '<pg:message code="sany.pdp.sys.role"/>';
	}else if(type == "org"){
		typeName = '<pg:message code="sany.pdp.sys.org"/>';
	}else if(type == "job"){
		typeName = '<pg:message code="sany.pdp.sys.post"/>';
	}else if(type == "orgjob"){
		typeName = '<pg:message code="sany.pdp.sys.org.post"/>';
	}
	$.dialog.alert('<pg:message code="sany.pdp.personcenter.person.res.redundance.delete.begin"/> '+typeName+' <pg:message code="sany.pdp.personcenter.person.res.redundance.delete.end"/>'); 
}

function getOperateType(){
	$("#operategroup").load( "../user/resChangeAjax.jsp?restypeId="+document.all.restypeId.value);
}

function change(){
	document.all.typename.innerText = document.all.type.options[document.all.type.selectedIndex].text+'<pg:message code="sany.pdp.personcenter.person.res.type.name.end"/>';
	document.all.name.value = "";
	document.all.name.title = '<pg:message code="sany.pdp.personcenter.person.res.type.name.title"/>'+document.all.type.options[document.all.type.selectedIndex].text;
	if(document.all.type.value == "orgjob")
	{
		document.all.name.readonly = true;
	}
	else
	{
		document.all.name.readonly = false;
	}
}

function chooseorgjob()
{
	if(document.all.type.value == "orgjob")
	{
		var orgjob = document.all.orgjob.value;
		orgjob = orgjob.split(":");
		
		var url="<%=request.getContextPath()%>/sysmanager/resmanager/orgJobTree.jsp?tag1=name&tag2=orgjob&defaultValue="+orgjob;
		
    	$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.res.type.post.select"/>',width:616,height:500, content:'url:'+url});   
	}
	if(document.all.type.value == "user")
	{
		var url = "<%=request.getContextPath()%>/sysmanager/resmanager/orgUserTree.jsp?tag=name";
		$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.res.type.user.select"/>',width:616,height:500, content:'url:'+url});   
	}
	if(document.all.type.value == "role")
	{
		var url = "<%=request.getContextPath()%>/sysmanager/resmanager/roleTree.jsp?tag=name";
		$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.res.type.role.select"/>',width:616,height:500, content:'url:'+url});  
	}
	if(document.all.type.value == "org")
	{
		var orgNames = document.all.name.value; 
		
		var url = "<%=request.getContextPath()%>/sysmanager/resmanager/far-outerquery/select_org.jsp?tag1=orgid__&tag2=name&orgNames="+orgNames;
		$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.res.type.org.select"/>',width:616,height:500, content:'url:'+url});  
	}
}


	
</SCRIPT>
<style>
</style>
</head>
<% if("is".equals(isUserRes)){ %>
	<body scrolling="no" onLoad="isUserRes()">
<% }else{ %>
	<body scrolling="no" onLoad="change()">
<% } %>

<div class="mcontent">
	<div id="searchblock">
		<div  class="search_top">
    		<div class="right_top"></div>
    		<div class="left_top"></div>
    	</div>
    	<div class="search_box">
    		<form name ="userreslist"  method="post" target="forDocList">
    			<table width="98.5%" border="0" cellspacing="0" cellpadding="0">
    				<tr>
      					<td class="left_box"></td>
      					<td>
      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
      							<% if("no".equals(isUserRes)){ %>
      							<tr>
      								<th><pg:message code="sany.pdp.personcenter.person.res.query.select"/></th>
      								<td>
      									<select class="select" name="type" id="type" onChange="change()" class="w120">
											<option value="user"><pg:message code="sany.pdp.sys.user"/></option>
											<option value="role"><pg:message code="sany.pdp.sys.role"/></option>
											<!-- <option value="job">岗位</option> -->
											<%
											if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", true))
											{
											%>
											<option value="org"><pg:message code="sany.pdp.sys.org"/></option>
											<%
											}
											%>
											<%
											if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", true))
											{
											%>
											<option value="orgjob"><pg:message code="sany.pdp.sys.org.post"/></option>
											<%
											}
											%>
										</select>
      								</td>
      								<th><div name="typename" id="typename" style="width:100"></div></th>
      								<td>
      									<input type="text" name="name" width="100%" id="name" readonly="true" onclick="chooseorgjob()" class="w120" 、>
										<input name="orgjob" type="hidden" value="" >
										<input name="orgid__" type="hidden" value="" >
      								</td>
      								<th><pg:message code="sany.pdp.personcenter.person.res.class"/>：</th>
      								<% 
										if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
										{
									%>
      								<td>
      									<select class="select" name="auto" id="auto" class="w120">
				      						<option value="">--<pg:message code="sany.pdp.personcenter.person.res.class"/>--</option>
											<option value="0"><pg:message code="sany.pdp.personcenter.person.res.class.system"/></option>
											<option value="1"><pg:message code="sany.pdp.personcenter.person.res.class.custom"/></option>
										</select>
      								</td>
      								<%} %>
      								<%
										}
									%>
									<th><pg:message code="sany.pdp.personcenter.person.res.token"/>：</th>
      								<td><input type="text" name="resId"  class="w120"/></td>
      							</tr>
      							<tr>
      								<th><pg:message code="sany.pdp.personcenter.person.res.name"/>：</th>
      								<td><input type="text" id="resName" name="resName" class="w120"/></td>
      								<th><pg:message code="sany.pdp.personcenter.person.res.type"/>：</th>
      								<td>
      									<select class="select" id="restypeId" name="restypeId" onChange="getOperateType()" class="w120">
									      	<option value="">--<pg:message code="sany.pdp.common.operation.select"/>--</option>
									      	<pg:list requestKey="resList" needClear="false">
											    	<option value="<pg:cell colName="id"/>">
											        	<pg:cell colName="name"/>
													</option>
											</pg:list>
										</select>
      								</td>
      								<th><pg:message code="sany.pdp.personcenter.person.res.operation.type"/>：</th>
      								<td>
      									<select class="select" name="operategroup" id="operategroup" class="w120">
				      						<option value="">--<pg:message code="sany.pdp.common.operation.select"/>--</option>
										</select>
      								</td>
      								<td colspan="2" >
										<% if("is".equals(isUserRes)){ %>
      										<a href="javascript:void(0)" class="bt_1" name="search"  onclick="isUserRes()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      						<% }else{ %>
			      							<a href="javascript:void(0)" class="bt_1" name="search"  onclick="sub()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      						<% } %>
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
	
	<div class="title_box">
		<div class="rightbtn">
			<a href="javascript:void(0)" class="bt_small" onclick="del()"><span><pg:message code="sany.pdp.personcenter.person.res.redundance.delete"/></span> </a>
		</div>
		<strong><pg:message code="sany.pdp.personcenter.person.res.list"/></strong>
	</div>
</div>

<iframe id="getopergroup" name="getopergroup" border="1" height="100" width="100"></iframe>
<iframe id="delop" name="delop"  border="0" height="0" width="0"></iframe>
</body>

</html>
