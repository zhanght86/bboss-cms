

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%		
			AccessControl accesscontroler = AccessControl.getInstance();
	        accesscontroler.checkAccess(request, response);
			String act = (String) request.getAttribute("act");
			if (act == null)
				act = "";
			String parentId = (String) request.getAttribute("parentId");
			String groupId;
		
			if(request.getParameter("groupId") == null || request.getParameter("groupId").equals("null")){
				groupId = "0";
			}else{
				groupId = request.getParameter("groupId");
			}
			
	//------------------------------------------------------------------------
		GroupManager groupManager =  SecurityDatabase.getGroupManager();
	  	Group group = groupManager.getGroupByID(groupId); 
	    String resTypeId2 = "group";	
	    				//资源类型id
		String resId2 = groupId;	                   //资源id
		String title = group == null ? null : group.getGroupName();		  //资源名称
			
		
		if(resId2 == null){
			resId2 = (String)request.getAttribute("resId2");
		}
		if(resTypeId2 == null){
			resTypeId2 = (String)request.getAttribute("resTypeId2");
		}
		OperManager operManager = SecurityDatabase.getOperManager();
		ResourceManager resManager = new ResourceManager();
		List operList = resManager.getOperations(resTypeId2);
		
		if (operList == null) {
			operList = new ArrayList();
		}
		request.setAttribute("operList", operList);
		
		String stored = (String)request.getAttribute("stored");
		if(stored == null){
			stored = "0";
		}
		String roleId = "";
		String rid = "";
%>

<html>
<head>				
	<tab:tabConfig/>
	
		<title>用户组基本信息</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="sysmanager/css/tab.winclassic.css">	
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
    	<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="javascript">
	
		  var act="<%=act%>";		  		  
          if(act=="update"){
          	getNavigatorContent().location.href ="sysmanager/groupmanager/groupTree.jsp?collapse=<%=parentId%>&request_scope=request&selectedNode=<%=groupId%>";
          }
       		function updateAfter(){
       			getNavigatorContent().location.href ="sysmanager/groupmanager/groupTree.jsp?expand=<%=parentId%>&request_scope=request"; 
			}
			function updateloadPage()
			{
				
				loadPage();
				updateAfter();
			}
			
			function setDisable(v){
				v.disabled = false;
				if  ( document.forms[0].groupId.value != ""  || document.forms[0].parentId.value != "")
				{
					v.disabled = true;
				}
			}
			function loadPage() {
				
				var form = document.forms[0];
				var act = form.action.value; 
				if (act == 2) {
					
					form.groupId.value = "";
					form.groupName.value = "";
					form.groupDesc.value = "";					
				}
				
				if (act == 1 || act == 2) {
				}

				form.action.value = 0;
				
			}
			
			function createGroup() {
				var form = document.forms[0];				
				document.location.href="sysmanager/groupmanager/createGroup.jsp?parentId="+form.groupId.value;
				
			}
			
			function saveGroup() {
				var form = document.forms[0];
				
				if(form.groupName.value == "" || form.groupName.value.length<1 || form.groupName.value.replace(/\s/g,"")==""){
					alert("组名称必须填写");
					return;
				}
				if(form.groupDesc.value == "" || form.groupDesc.value.length<1 || form.groupName.value.replace(/\s/g,"")==""){
					alert("组说明必须填写");
					return;
				}
				if(form.parentId.value == "null" || form.parentId.value.length<1){
					alert("请先在左边选择组");
					return;
				}
				
				var gn = form.groupName.value;
				var gd = form.groupDesc.value;
				if(gn.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("组名称不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}	
				if(gd.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("组描述不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}
				if(gn.length>100)
				{
					alert("组名称过长,限制在100字符以内!");
					return;
				}
				if(gd.length>100)
				{
					alert("组描述过长,限制在100字符以内!");
					return;
				}
				
				if(form.groupId.value == "0")
					form.groupId.value =="";
				form.action.value = "1";
				form.submit();
			}
			
			function delGroup() {
				
				var form = document.forms[0];
				if (form.groupId.value != "") {			
					if (window.confirm("删除将连带删除子组并且不可恢复，你确认要删除这个用户组吗？")) {				
						form.action.value = "2";
						form.submit();
					}
				} else {
					alert("请先在左边的用户组列表中选择需要删除的组!");
				}
			}
			//--------------------------------------------------------------------------------------------------------------
			var resTypeId2 ="<%=resTypeId2%>";
			var resId2 ="<%=resId2%>";
			var http_request = false;
			//初始化，指定处理的函数，发送请求的函数
			function send_request(url){
				http_request = false;
				//开始初始化XMLHttpRequest对象
				if(window.XMLHttpRequest){//Mozilla
					http_request = new XMLHttpRequest();
					if(http_request.overrideMimeType){//设置MIME类别
						http_request.overrideMimeType("text/xml");
					}
				}
				else if(window.ActiveXObject){//IE
					try{
						http_request = new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							http_request = new ActiveXObject("Microsoft.XMLHTTP");
						}catch(e){
						}
					}
				}
				if(!http_request){
					alert("不能创建XMLHttpRequest对象");
					return false;
				}
				http_request.onreadystatechange = processRequest;
				http_request.open("GET",url,true);
				http_request.send(null);
			}

			function processRequest(){
				if(http_request.readyState == 4){  
					//alert(http_request.status);                
					if(http_request.status == 200){
						//alert(http_request.responseText);
					}
					else{
						alert("对不起，服务器错误");
					}
				}
			}
			//------------------------------------------------------------------------------------------
			function changebox(currCheck,rid,priority,opid){
				var roleList = document.roleList;
				var isRecursion = 0;
				//是否递归

				if(roleList.isRecursion.length)				
				{
				if(roleList.isRecursion[rid].checked)
						isRecursion = 1;
				}
			   else if(roleList.isRecursion.checked)
			    {
			    	isRecursion = 1;
			    }
			    

				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
				//checked
				if(currCheck.checked){
		
					send_request('<%=rootpath%>/sysmanager/groupmanager/saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion);
			
				}
				//not checked
				else{
					send_request('<%=rootpath%>/sysmanager/groupmanager/saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion);
				}

				setCheck(currCheck,rid,priority);
			}
			
			function isRecursionClick(currCheck,rid,priority){
		
				var roleList = document.roleList;
				var isRecursion = 0;
				var opid = 0;
				//是否递归

				if(roleList.isRecursion.length)				
				{
					if(roleList.isRecursion[rid].checked)
						isRecursion = 1;
				}
			    else if(roleList.isRecursion.checked)
			    {
			    	isRecursion = 1;
			    }
			    

				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
					
					
				var opers = roleList.all("opId"+rid);
				//没有复选框
  				if(!opers){  	
  					alert("您没有选择操作");
  					return;
  				}
  				var n=roleList.all("opId"+rid).length;
  				//只有一个复选框
  				if(!n){
	  				opid = roleList.all("opId"+rid).value;
	  			
					if(roleList.all("opId"+rid).checked){
						send_request('<%=rootpath%>/sysmanager/groupmanager/saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion);
					}
					else
						send_request('<%=rootpath%>/sysmanager/groupmanager/saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion);
				}
				//有多个复选框
				for(var i= 0;i<n;i++){
				
					opid = opers[i].value;
					if(opers[i].checked){						
						send_request('<%=rootpath%>/sysmanager/groupmanager/saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=1&title=<%=title%>&isRecursion="+isRecursion);
					}
					else
						send_request('<%=rootpath%>/sysmanager/groupmanager/saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked=0&title=<%=title%>&isRecursion="+isRecursion);
				}
			}
			
			function changebox2(opid,checked){
				var roleList = document.roleList;
				var resid = "<%=resId2%>";
				var restypeid = roleList.resTypeId.value;
				send_request('<%=rootpath%>/sysmanager/groupmanager/saveRoleOp.jsp?resId='+resid+'&resTypeId='+restypeid+'&opId='+opid+"&checked="+checked+"&title=<%=title%>");
			}

			function setCheck(currCheck,rid,priority)
			{
			   	var o = document.getElementsByName(currCheck.name);
				var prioritylist = document.getElementsByName("priority"+rid);
				if (currCheck.checked==true && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;

						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1)&& priority.substring(1,2) == v.substring(1,2) )
						{
							o[i].checked=true;
							changebox2(o[i].value,1);
							//alert("3:"+o[i].value);
							//o[i].disabled=true;
						}
					}

					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(1,2) != v.substring(1,2) )
						{
							o[i].checked=false;
							changebox2(o[i].value,0);
							//alert("4:"+o[i].value);
							//o[i].disabled=false
						}
					}
				}
				if (currCheck.checked==false  && priority.length >1 && (priority.match(/[0-9]/))){
					for (var i=0;i<prioritylist.length;i++){
						var v = prioritylist[i].value;
						if ( v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1) )
						{
							if ( o[i].checked==true ){
								//currCheck.checked==true;
								//o[i].disabled=false;
							}
						}
					}

				}
			}
		</script>
</head>
<body class="" onLoad="<%=act.equals("update")?"updateloadPage()":"loadPage()"%>" scroll="no">

<tab:tabContainer id="group-bar-container" selectedTabPaneId="tab-group">
	<tab:tabPane id="tab-group" tabTitle="用户组基本信息" >
		<html:form action="groupInfo.do">
				<table width="98%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
					<tr>
						<td width="16%" height="25" class="detailtitle">
							<P align="center">
								<STRONG>用户组名称*：</STRONG>
							</P>
						</td>
						<td width="907" height="25">

							<html:text property="groupName" style="width:30%" onfocus="setDisable(this)" />
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<P align="center">
								<STRONG>用户组说明*：</STRONG>
							</P>
						</td>
						<td height="25">
							<html:textarea property="groupDesc" style="width:30%" />
						</td>
					</tr>
		</table>
				<hr width="98%">
				<table width="98%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td></td>
						<td>
							<table width="60%" border="0" align="right" cellpadding="0" cellspacing="0">

								<tr>
									<td align="right">
									<%if(accesscontroler.checkPermission(groupId,
														"addgroup",
														AccessControl.GROUP_RESOURCE))
									  {%>
										<html:button property="b1" styleClass="input" value="创建" onclick="createGroup();" />
									  <%}%>	
									  <%if(accesscontroler.checkPermission(groupId,
														"editgroup",
														AccessControl.GROUP_RESOURCE))
									  {%>
									  
										<html:button property="b2" styleClass="input" value="保存" onclick="saveGroup();" />
									<%}%>		
									  <%if(accesscontroler.checkPermission(groupId,
														"deletegroup",
														AccessControl.GROUP_RESOURCE))
									  {%>
										<html:button property="b3" styleClass="input" value="删除" onclick="delGroup();" />
									<%}%>		
									</td>
								</tr>
							</table>
						</td>
					</tr>
		</table>

				<html:hidden property="groupId" />
				<html:hidden property="action" />
				<html:hidden property="parentId" />
	</html:form>
			<p></p>
	</tab:tabPane>
	</tab:tabContainer>
</body>
</html>

