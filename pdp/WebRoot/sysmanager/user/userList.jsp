<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
		com.frameworkset.platform.config.ConfigManager,
		com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%
	OrgAdministratorImpl orgAdministratorImpl = new OrgAdministratorImpl();
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	ResourceManager resManager = new ResourceManager();
    String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	String curOrgId = request.getParameter("orgId");
	if(curOrgId == null)
		curOrgId = (String)request.getAttribute("orgId");
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "true";
	}
	String userNamesNo = String.valueOf(session.getAttribute("promptString"));
	session.removeAttribute("promptString");
	System.out.println(userNamesNo);
	Integer currUserId = (Integer)session.getAttribute("currUserId");
	if(currUserId == null){
		currUserId = Integer.valueOf("-1");
	}
	//String curOrgId = (String)session.getAttribute("orgId");
	String desc = (String)request.getParameter("pager.desc");	
	String intervalType = (String)request.getParameter("intervalType");
	String taxmanager = (String)request.getParameter("taxmanager");
	String ischecked="";
	if((String)request.getAttribute("ischecked")==null){
		ischecked ="";
	}else{
		ischecked =(String)request.getAttribute("ischecked");
	}
	
	String QueryuserName = null;
	String QueryuserRealname = null;
	if(request.getParameter("userName")==null){
		QueryuserName="";
	}else{
		QueryuserName = request.getParameter("userName");
	}
	if(request.getParameter("userRealname")==null){
		QueryuserRealname="";
	}else{
		QueryuserRealname = request.getParameter("userRealname");
	}
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">
var isSucceed = '<%=request.getParameter("isSucceed")%>';
var delSucceed = '<%=request.getParameter("delSucceed")%>';
var isMain = '<%=request.getParameter("isMain")%>';
var isRes = '<%=request.getParameter("isRes")%>';
var istaxmanager = '<%=request.getParameter("istaxmanager")%>';
var taxmanagerName = '<%=request.getParameter("taxmanagerName")%>';
var userNamesNo = '<%=userNamesNo%>';
//提示新增用户是否成功
if(isSucceed=="1"){
	alert("新增用户成功！");
	var str = document.location.href;
	parent.window.document.location.href = str.substring(0,str.indexOf("isSucceed"));
}
//提示移除（删除）用户是否成功
if(delSucceed=="2"){
	if(userNamesNo != "" && userNamesNo != "null"){
		alert("以下用户删除失败: \n"+userNamesNo);
	}else{
		alert("操作成功！");
	}
	var str = document.location.href;
	parent.window.document.location.href = str.substring(0,str.indexOf("delSucceed"));
}

//税管员提示
if(istaxmanager=="true"){
	alert("用户"+taxmanagerName+"为税管员不能删除！");
}



//主机构
if(isMain=="main"){
	alert('移除不成功！当前机构是用户(<%=request.getParameter("userNameMain")%>)的主机构，当用户还有其他隶属机构时不能从当前主机构中移除，必须为用户指定一个主机构（选择隶属机构指定主机构）！');
}


var reFlush = "<%=reFlush%>";
var isSelf = "<%=request.getParameter("isSelf")%>";
if(isSelf == "true")
{
	alert("不能移除自己与系统管理员！");
}

function getUserInfo(e,userId)
{
	
}

function dealRecord(dealType) {
    var isSelect = false;
    var outMsg;
	var userIds = "";
	    
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];
			
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		if(userIds == ""){
	       			userIds = e.value; 
	       		}else{
	       			userIds += "," + e.value;
	       		}
	       		
		    }
		}
    }
    if (userIds != ""){
    	if (dealType==1){
    		<%if(ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){%>
    		outMsg = "您确认移除吗？（移除后该用户在没有其他隶属机构的情况下将做为离散用户管理）";
        	if (confirm(outMsg)){
				userList.action="../user/userManager.do?orgId=<%=curOrgId%>&method=deleteUser&flag=3&isRes=2";
				//userList.action="../user/userList.jsp?flag=3";
				userList.target = "deluser";
				userList.submit();
	 			return true;
			}
			<%}else{%>
			if(confirm("您确认移除吗？")){
				outMsg = "该用户将变为离散用户，选择'确定'：用户拥有的角色，用户拥有的资源，用户所属的组关系，该用户的部门管理员角色、用户可管理的机构关系将被删除；选择'取消'将保留用户拥有的角色，用户拥有的资源，用户所属的组关系。而用户的部门管理员角色、用户可管理的机构关系将被删除。";
	        	if (confirm(outMsg)){
					userList.action="../user/userManager.do?orgId=<%=curOrgId%>&method=deleteUser&flag=3&isRes=3";
					//userList.action="../user/userList.jsp?flag=3";
					userList.target = "deluser";
					userList.submit();
		 			return true;
				}else{
					userList.action="../user/userManager.do?orgId=<%=curOrgId%>&method=deleteUser&flag=3&isRes=2";
					//userList.action="../user/userList.jsp?flag=3";
					userList.target = "deluser";
					userList.submit();
		 			return true;
				}
			}
			<%}%>
		} 
		if (dealType==2)//彻底删除用户,da.wei
		{
			outMsg = "您确认删除吗？（删除后该用户将不存在！）";
        	if (confirm(outMsg)){
        		var url = "../../purviewmanager/userorgmanager/user/quiteDelUser.jsp?checks="+userIds+"&orgId=<%=curOrgId%>";
				//userList.action="../user/userManager.do?orgId=<%=curOrgId%>&method=deleteUser&flag=3&isRes=4";
				userList.action = url;
				userList.target = "deluser";
				userList.submit();
	 			return true;
			}			
		}
    }else{
    	alert("至少要选择一条记录！");
    	return false;
    }
	return false;
}

function roleUser(type) {
        var isSelect = false;
	    var outMsg;
		var userList = document.userList;   
		
		//选择两个或更多的用户进行批量处理,da.wei
		var count = 0;
		for (var j=0;j<userList.elements.length;j++)
		{
			if(userList.elements[j].checked)
			{
				count++;
			}
		}
		if(count<2)
		{
			alert("请选择两个或更多的用户进行批量处理!");
			return;
		}
		 
	    for (var i=0;i<userList.elements.length;i++) {
			var e = userList.elements[i];
				
			if (e.name == 'checkBoxOne'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (type==1){
	    		userList.action="../user/alotUserRole.jsp?orgId=<%=curOrgId%>";
	    		userList.submit();
	    		
				return true;
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
}
	
function sortBy(byName){
	var desc = "false";

	if ( "<%=desc%>"  == desc ) desc = "true";
	userList.action="../user/userList.jsp?orgId=<%=curOrgId%>&pager.offset=0&pager.sortKey="+byName+"&pager.desc="+desc;
	userList.submit();
}
	
function actionOnmouseover(e){	
	e.style.backgroundColor = "#8080FF";
}

function actionOnmouseup(e){
	e.style.backgroundColor = "#BEC0C2";
}

function newUser()
{
	//新建用户
	//document.location.href="../user/userManager.do?method=newUser";
	var winuser;
	var urlnewUser = "../user/userInfo.jsp?orgId=<%=curOrgId%>&isNewUser=true";
	winuser = window.showModalDialog(urlnewUser,window,"dialogWidth:"+(850)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winuser=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function queryUser()
{	
	//if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
	//	alert("用户名称和用户实名必须输入一个!!!");
	//	return;
	//}
	var intervalType = document.all("intervalType").value;
	userList.action="../user/userList.jsp?orgId=<%=curOrgId%>&intervalType="+intervalType;
	userList.submit();	
}
function querytype(e){
    queryUser();
}

function advQueryUser()
{	
	history.back();	
}

//批量岗位授予
function jobUser(type) {
        var isSelect = false;
	    var outMsg;
		var userList = document.userList;  
		
		//选择两个或更多的用户进行批量处理,da.wei
		var count = 0;
		for (var j=0;j<userList.elements.length;j++)
		{
			if(userList.elements[j].checked)
			{
				count++;
			}
		}
		if(count<2)
		{
			alert("请选择两个或更多的用户进行批量处理!");
			return;
		}	
		  
	    for (var i=0;i<userList.elements.length;i++) {
			var e = userList.elements[i];
				
			if (e.name == 'checkBoxOne'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (type==1){
	    		    userList.action="../user/userJob.do?orgId=<%=curOrgId%>&method=getAllJobList&orgId=<%=curOrgId%>";
	    		    userList.submit();
				    return true;
				    } 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
}
//批量机构授予
function orgUser(type) {
        var isSelect = false;
	    var outMsg;
		var userList = document.userList; 
		
		//选择两个或更多的用户进行批量处理,da.wei
		var count = 0;
		for (var j=0;j<userList.elements.length;j++)
		{
			if(userList.elements[j].checked)
			{
				count++;
			}
		}
		if(count<2)
		{
			alert("请选择两个或更多的用户进行批量处理!");
			return;
		}
		   
	    for (var i=0;i<userList.elements.length;i++) {
			var e = userList.elements[i];
				
			if (e.name == 'checkBoxOne'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (type==1){
	    		    userList.action="../user/userOrg.do?orgId=<%=curOrgId%>&method=getAllOrgList";
	    		    userList.submit();
				    return true;
				    } 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
}
//批量资源操作授予
function operUser(type) {
        var isSelect = false;
	    var outMsg;
		var userList = document.userList;  
		
		//选择两个或更多的用户进行批量处理,da.wei
		var count = 0;
		for (var j=0;j<userList.elements.length;j++)
		{
			if(userList.elements[j].checked)
			{
				count++;
			}
		}
		if(count<2)
		{
			alert("请选择两个或更多的用户进行批量处理!");
			return;
		}
		  
	    for (var i=0;i<userList.elements.length;i++) {
			var e = userList.elements[i];
				
			if (e.name == 'checkBoxOne'){
				if (e.checked){
		       		isSelect=true;
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (type==1){
	    		    userList.action="../user/userRole.do?orgId=<%=curOrgId%>&method=getAllOperList";
	    		    userList.submit();
				    return true;
				    } 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
}
//批量资源操作回收
function purviewReclaimUsers(){
    var arr = new Array();
    arr = document.getElementsByName("checkBoxOne");
    var checks = "";
    var count = 0;
    for(var i = 0; i < arr.length; i++){
    	if(arr[i].checked){
    		if(checks == ""){
    			checks = arr[i].value;
    		}else{
    			checks += "," + arr[i].value;
    		}
    		count++;
    	}
    }
    var winRes;
    if(checks != ""){    	
		var url = "../../purviewmanager/reclaimManager/reclaimUserRes.jsp?userIds="+checks;
		//window.open(url);
		winRes = window.showModalDialog(url,window,"dialogWidth:"+(400)+"px;dialogHeight:"+(300)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
		if(winRes=="ok"){
			//window.location.reload();
		}
		 
    }else{
    	alert("至少要选择一条记录！");
    	return false;
    }
	return false;
}

var winOpen
function orderUser(){
    winOpen = window.open("about:blank","win","scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=130,left=210");                   
    document.all.form2.target = "win";
    document.all.form2.action = "user_order_ajax.jsp?orgId=<%=curOrgId%>";
    timer = window.setInterval("isClosed()",10);
    document.all.form2.submit();
}
var timer;
function isClosed(){
    if(winOpen.closed==true){
        window.location.href = window.location;
        window.clearInterval(timer);
    }
}

<!-- gao.tang 2007.11.05 start 右键菜单跳转路径  -->
function userInfo(userId){ 
	//用户基本信息 userInfo_tab.jsp
	var winbasic;
	var url="../user/userManager.do?orgId=<%=curOrgId%>&method=getUser&userId="+userId;
	winbasic = window.showModalDialog(url,window,"dialogWidth:"+(850)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winbasic=="ok"){
		document.location = document.location;
		//window.location.reload();
	}
}

function userInfoQuery(userId){ 
	//用户基本信息查看 userInfo_tab.jsp
	var winbasic;
	var url="../../purviewmanager/userorgmanager/user/userInfo_tab_query.jsp?userId="+userId;
	winbasic = window.showModalDialog(url,window,"dialogWidth:"+(850)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winbasic=="ok"){
		document.location = document.location;
		//window.location.reload();
	}
}

function subJob(userId){
	//用户隶属岗位  userjoborg.jsp
	var winsubjob;
	var url2 = "../user/userjoborg.jsp?userId="+userId+"&orgId=<%=curOrgId%>";
	winsubjob = window.showModalDialog(url2,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winsubjob=="ok"){
		document.location = document.location;
		//window.location.reload();
	}
}

function subOrg(userId){
	//用户隶属机构
	var winsuborg;
	winsuborg = window.showModalDialog("subjectionOrg.jsp?userId="+ userId + "&orgId=<%=curOrgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winsuborg=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function subTeam(userId){
	//用户隶属组
	var winsubteam;
	winsubteam = window.showModalDialog("../user/user2group.jsp?userId="+ userId + "&orgId=<%=curOrgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winsubteam=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function roleGrant(userId){
	//机构下的用户角色授予
	var winrolegrant;
	winrolegrant = window.showModalDialog("../user/userRole.do?method=getRoleList&userId="+ userId + "&orgId=<%=curOrgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(700)+"px;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winrolegrant=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function resGrant(userId){
	//用户资源操作授予
	var winresgrant;
	//window.open("../user/user_resFrame.jsp?userId="+ userId + "&orgId=<%=curOrgId%>","","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;");
	winresgrant = window.showModalDialog("../user/user_resFrame.jsp?userId="+ userId + "&orgId=<%=curOrgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winresgrant=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function purviewCopy(userId){
	//权限复制
	var winpurviewcopy;
	winpurviewcopy = window.showModalDialog("../../purviewmanager/userorgmanager/user/purview_copy.jsp?userId="+ userId + "&orgId=<%=curOrgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winpurviewcopy=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function purviewReclaim(userId){
    //权限回收
    var winpurviewReclaim;
	winpurviewReclaim = window.showModalDialog("../../purviewmanager/reclaimManager/reclaimUserRes.jsp?userIds="+ userId ,window,"dialogWidth:"+(400)+"px;dialogHeight:"+(300)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	if(winpurviewReclaim=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function userOrg(userId){
	//用户可管理的机构列表信息
	var winuserOrg;
	winuserOrg = window.showModalDialog("../user/managerOrg_list.jsp?orgId=<%=curOrgId%>&userId="+userId,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
}

//用户任职情况列表
function userHisJob(userId){
	var sty = "dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0";
	window.showModalDialog("userHisJobQuery.jsp?userId="+userId,window,sty);
}
<!-- gao.tang 2007.11.05 end    -->


function foldUser(){
	var win;
	//调入用户
	var url = "../user/folduser_frame.jsp?orgId=<%=curOrgId%>";
	var stylee = "dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0";
	//window.open(url);
	win = window.showModalDialog(url,window,stylee);
	if(win=="ok"){
		//window.location.reload();
		document.location = document.location;
	}
}

function addorg(dealType) {
    var isSelect = false;
    var outMsg;
    var checks;
    var state = false;
    var obj = document.getElementsByName("checkBoxOne");
    if(obj.length==0){
    	alert("没有可调入的用户！");
    	return;
    }
    for(var j=0; j < obj.length; j++){
    	if(obj[j].checked){
    		if(state){
    			checks += ","+obj[j].value;
    		}else{
    			checks = "" + obj[j].value;
    			state = true;
    		}
    	}
    }
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];	
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		if(checks != ""){
				var winaddorg;
				var url = "../user/userChangeOrg.jsp?checkBoxOne="+checks + "&orgId=<%=curOrgId%>";
				//window.open(url);
				winaddorg = window.showModalDialog(url,window,"dialogWidth:"+(400)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
				if(winaddorg=="ok"){
					//window.location.reload();
					document.location = document.location;
				}
 			}else{
 				alert("没有要调入的用户！");
 				return;
 			}
		} 
    }else{
    	alert("至少要选择一条记录！");
    	return false;
    }
	return false;
}

function openCSUserLogonManager(userId)
{
	var winaddorg;
	var url = "../../purviewmanager/userorgmanager/user/csUserLogonIPManager.jsp?userId="+userId;
	winaddorg = window.showModalDialog(url,window,"dialogWidth:"+(300)+"px;dialogHeight:"+(150)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	
}
</SCRIPT>		
			<body >
				<form name="userList" method="post" >
				<input type="hidden" name="orgId" value="<%=curOrgId%>"/>
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="5"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>用户列表</strong></td>
         				</tr>
         				<tr>
         					<td  colspan="5" height='30'valign='middle' align="left">
         					<%
         					if(accesscontroler.checkPermission(curOrgId,"batroleadd",AccessControl.ORGUNIT_RESOURCE))
         					{
         					%>
         					<input type="submit" value="批量角色授予" class="input" onclick="javascript:roleUser(1); return false;">
         					<%
         					}
         					if(accesscontroler.checkPermission(curOrgId,"batjobadd",AccessControl.ORGUNIT_RESOURCE))
         					{
         					%>
         					<input type="submit" value="批量岗位授予" class="input" onclick="javascript:jobUser(1); return false;">
         					<%
         					}
         					if(accesscontroler.checkPermission(curOrgId,"batorgadd",AccessControl.ORGUNIT_RESOURCE) && 
         						ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true))
         					{
         					%>
         					<input type="submit" value="批量加入机构" class="input" onclick="javascript:orgUser(1); return false;">
         					<%
         					}
         					if(accesscontroler.checkPermission("orgunit","purset",AccessControl.ORGUNIT_RESOURCE))
         					{
         					%>
         					<input type="submit" value="批量资源操作授予" class="input" onclick="javascript:operUser(1); return false;">
         					<%
         					}
         					//add by ge.tao
         					if(accesscontroler.checkPermission("orgunit","purset",AccessControl.ORGUNIT_RESOURCE))
         					{
         					%>
         					<input type="submit" value="批量权限回收" class="input" onclick="javascript:purviewReclaimUsers(); return false;">    
         					<%
         					}
         					//--end
         					if(accesscontroler.checkPermission(curOrgId,"sortuser",AccessControl.ORGUNIT_RESOURCE))
         					{
         					%>
         					<input type="submit" value="用户排序" class="input" onclick="javascript:orderUser(); return false;">
         					<%
         					}
         					%>
         					</td>
         					
         				</tr>
         				
         				<tr >
           				    <td  height='30'valign='middle' align="left">
           				     用户名称：<input type="text" name="userName" value="<%=QueryuserName%>" size="15">
           					用户实名：<input type="text" name="userRealname" value="<%=QueryuserRealname%>" size="15">
           					税管员：<select name="taxmanager" class="select">
           							<option value="2" selected>--请选择--</option>
           							<option value="1">税管员</option>
           							<option value="0">非税管员</option>
           						  </select>
           						<select name="intervalType" class="select"  onchange=""> 
           						<option value="0" >
									不递归机构查询
								</option>          						
           						<option value="1" >
									递归机构查询
								</option>
								
								</select>
							</td>							
							<td colspan="1">	           						
           					<input name="search" type="button" class="input" value="查询" onClick="queryUser()">
           					</td>
         				</tr>
         				</table>
         				
         				<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
         				<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserListSn" keyName="UserListSn" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="10" scope="request" data="UserListSn" isList="false">
							<tr>
								<!--设置分页表头-->
								<td class="headercolor" width="6%">
								<P align="left">
								<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on">
								</P>
								</td>
								<td class="headercolor" onclick="sortBy('userName')" width="15%" >用户登陆名称</td>
								<td class="headercolor" onclick="sortBy('userName')" width="15%" >用户实名</td>
								<td class="headercolor" onclick="sortBy('userName')" width="15%" >是否税管员</td>
								<td class="headercolor" onclick="sortBy('userName')" >隶属机构</td>
								<td class="headercolor" onclick="sortBy('userName')" >用户类别 </td>
								<td class="headercolor" onclick="sortBy('userName')" >邮箱地址</td>								
								
							</tr>
							<pg:param name="orgId" />
							<pg:param name="userName" />
							<pg:param name="userRealname" />
							<pg:param name="taxmanager" />
							<pg:param name="intervalType" />
							<pg:param name="istaxmanager" />
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有用户
									</td>
								</tr>
							</pg:notify>
				<!--  ----2007.10.16     chunqiu.zhao   ---------------->			
						    <%
								ContextMenu contextmenu = new ContextMenuImpl();
					 		%>

						
							<!--list标签循环输出每条记录-->
							<pg:list>
			<!--  ----2007.10.16     chunqiu.zhao   ---------------->	
						<%	
							
							
							int userId = dataSet.getInt("userId");
							String userName = dataSet.getString("userName");
							String userIdStr = String.valueOf(userId);
							//是否是管理员
							boolean isOrgManager = orgAdministratorImpl.isOrgManager(userIdStr);
							//是否拥有超级管理员角色
							boolean isRoleAdmin = accesscontroler.isAdminByUserid(userIdStr);
							String isRoleAdminOrOrgManager = "";//是否拥有超级管理员角色与部门管理员角色
							if(isOrgManager && isRoleAdmin){
								isRoleAdminOrOrgManager = "(超级管理员角色；部门管理员)";
							}else if(isRoleAdmin){
								isRoleAdminOrOrgManager = "(超级管理员角色)";
							}else if(isOrgManager){
								isRoleAdminOrOrgManager = "(部门管理员)";
							}
                            //boolean isOrgManager = orgAdministratorImpl.isOrgAdmin(String.valueOf(userId),curOrgId);
							Menu menu = new Menu();
							menu.setIdentity("opuser_"+userId);
							
							
							
							//用户基本信息，这里不做权限控制，在打开的页面控制
							Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
							menuitem2.setName("基本信息查看");
							menuitem2.setLink("javascript:userInfoQuery("+userId+")");
							menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/issue/status_reopened.gif");
							menu.addContextMenuItem(menuitem2);
							
							if (accesscontroler.checkPermission("orgunit",
	                         		"userupdate", AccessControl.ORGUNIT_RESOURCE))
	            				{
								Menu.ContextMenuItem menuitemup = new Menu.ContextMenuItem();
								menuitemup.setName("基本信息修改");
								menuitemup.setLink("javascript:userInfo("+userId+")");
								menuitemup.setIcon(request.getContextPath() +"/sysmanager/images/edit.gif");
								menu.addContextMenuItem(menuitemup);
							}
							menu.addSeperate();
							//cs用户登录管理
							Menu.ContextMenuItem menuitem_cs = new Menu.ContextMenuItem();
							menuitem_cs.setName("CS用户登录管理");
							menuitem_cs.setLink("javascript:openCSUserLogonManager("+userId+")");
							menuitem_cs.setIcon(request.getContextPath() +"/sysmanager/images/issue/status_resolved.gif");
							menu.addContextMenuItem(menuitem_cs);
							
							//用户可管理的机构
							//if(!AccessControl.isAdmin(userName)){
				 				if (accesscontroler.checkPermission(curOrgId,
	                         		"edituser", AccessControl.ORGUNIT_RESOURCE))
	            				{
									Menu.ContextMenuItem menuitem_org = new Menu.ContextMenuItem();
									menuitem_org.setName("用户可管理部门");
									menuitem_org.setLink("javascript:userOrg("+userId+")");
									menuitem_org.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_sh.gif");
									menu.addContextMenuItem(menuitem_org);
								}
							//}
							
							//用户隶属岗位
							
			 				if (accesscontroler.checkPermission(curOrgId,
                         		"userjobset", AccessControl.ORGUNIT_RESOURCE) && 
                        		!String.valueOf(userId).equals(accesscontroler.getUserID()))
            				{
								Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
								menuitem3.setName("隶属岗位");
								menuitem3.setLink("javascript:subJob("+userId+")");
								menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/tree_images/channel_closedFolder.gif");
								menu.addContextMenuItem(menuitem3);
							}
							
							//隶属机构
							if (accesscontroler.checkPermission(curOrgId,
                         		"userorgset", AccessControl.ORGUNIT_RESOURCE))
            				{
								Menu.ContextMenuItem menuitem4 = new Menu.ContextMenuItem();
								menuitem4.setName("隶属机构");
								menuitem4.setLink("javascript:subOrg("+userId+")");
								menuitem4.setIcon(request.getContextPath() +"/sysmanager/images/Groups.gif");
								menu.addContextMenuItem(menuitem4);
							}
							
							//隶属组,如果没有启用组角色
							if (ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole", true) && accesscontroler.checkPermission(curOrgId,
                         		"usergroupset", AccessControl.ORGUNIT_RESOURCE))
            				{
								Menu.ContextMenuItem menuitem5 = new Menu.ContextMenuItem();
								menuitem5.setName("隶属组");
								menuitem5.setLink("javascript:subTeam("+userId+")");
								menuitem5.setIcon(request.getContextPath() +"/sysmanager/images/online-users.gif");
								menu.addContextMenuItem(menuitem5);
							}
							
						//角色授予  
							if (accesscontroler.checkPermission(curOrgId,
                        		"userroleset", AccessControl.ORGUNIT_RESOURCE) && 
                        		!String.valueOf(userId).equals(accesscontroler.getUserID()))
           					{
								Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
								menuitem6.setName("角色授予");
								menuitem6.setLink("javascript:roleGrant("+userId+")");
								menuitem6.setIcon(request.getContextPath() +"/sysmanager/images/tool-forward16.gif");
								menu.addContextMenuItem(menuitem6);
							}
							menu.addSeperate();
							//判断是否是超级管理员
							if(!AccessControl.isAdmin(userName)){
								//用户资源列表
								Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
								menuitem1.setName("用户资源列表");
								menuitem1.setLink("javascript:userResList("+userId+")");
								menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/listQuery.gif");
								menu.addContextMenuItem(menuitem1);
								
								
								//资源操作授予
								if (accesscontroler.checkPermission("orgunit",
	                         		"purset", AccessControl.ORGUNIT_RESOURCE) && 
                        		!String.valueOf(userId).equals(accesscontroler.getUserID()) && 
                        		!AccessControl.isAdmin(userName))
	            				{
									Menu.ContextMenuItem menuitem7 = new Menu.ContextMenuItem();
									menuitem7.setName("资源操作授予");
									menuitem7.setLink("javascript:resGrant("+userId+")");
									menuitem7.setIcon(request.getContextPath() +"/sysmanager/images/tool-reply.gif");
									menu.addContextMenuItem(menuitem7);
								}
								
								
								//权限复制								
								if (!isOrgManager && accesscontroler.checkPermission("orgunit",
	                         		"purset", AccessControl.ORGUNIT_RESOURCE))
	            				{
									Menu.ContextMenuItem menuitem8 = new Menu.ContextMenuItem();
									menuitem8.setName("权限复制");
									menuitem8.setLink("javascript:purviewCopy("+userId+")");
									menuitem8.setIcon(request.getContextPath() +"/sysmanager/images/tool-reply_all.gif");
									menu.addContextMenuItem(menuitem8);
								}
								
								//权限回收
								if (accesscontroler.checkPermission("orgunit",
	                         		"purset", AccessControl.ORGUNIT_RESOURCE) && !isOrgManager)
	            				{
									Menu.ContextMenuItem menuitem8 = new Menu.ContextMenuItem();
									menuitem8.setName("权限回收");
									menuitem8.setLink("javascript:purviewReclaim("+userId+")");
									menuitem8.setIcon(request.getContextPath() +"/sysmanager/images/icons/16x16/recycle.gif");
									menu.addContextMenuItem(menuitem8);
								}
							}
							menu.addSeperate();
							Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
							menuitem9.setName("用户历史任职情况");
							menuitem9.setLink("javascript:userHisJob("+userId+")");
							menuitem9.setIcon(request.getContextPath() +"/sysmanager/images/about.gif");
							menu.addContextMenuItem(menuitem9);
							
							
							contextmenu.addContextMenu(menu);
									
							
						%>
						
			<!--  ----2007.10.16     chunqiu.zhao   ---------------->
								<%
								     //String backColor="";
								     //if (  )
								     //	backColor="bgcolor=#ff0000";
								     //}
								%>
							<%if(userId!=1){%>	
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
									<td class="tablecells" nowrap="true">
										<P align="left">
											<INPUT type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="userId" defaultValue=""/>' >
										</P>
									</td>
									<td  class="tablecells" nowrap="nowrap"  >
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td id="opuser_<%=userId%>" class="tablecells" nowrap="nowrap" bgcolor="#F6FFEF" >
										<pg:cell colName="userRealname" defaultValue="" /><%=isRoleAdminOrOrgManager %>
									</td>
									<td>
										<pg:equal colName="istaxmanager" value="1">税管员</pg:equal>
										<pg:equal colName="istaxmanager" value="0">非税管员</pg:equal>
										<pg:equal colName="istaxmanager" value="">非税管员</pg:equal>
									</td>
									<td class="tablecells" >
										<pg:cell colName="org_Name" defaultValue="" />
									</td>	
									<td class="tablecells" nowrap="nowrap" > 
										<dict:itemname type="userType" expression="{userType}" />
    								</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:notnull colName="userEmail"><pg:cell colName="userEmail" defaultValue=" "/></pg:notnull>
										<pg:null colName="userEmail">没有邮箱</pg:null>
										<pg:equal colName="userEmail" value="">没有邮箱</pg:equal>
									</td>	
								</tr>
							<%}else{%>
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
									<td class="tablecells" nowrap="true">
										<P align="left">
											<font color="#0000FF">
												<INPUT type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="userId" defaultValue=""/>' >
											</font>
										</P>
									</td>
									<td  class="tablecells" nowrap="nowrap">
										<font color="#0000FF"><pg:cell colName="userName" defaultValue="" /></font>
									</td>
									<td id="opuser_<%=userId%>" class="tablecells" nowrap="nowrap" bgcolor="#0066FF">
										<pg:cell colName="userRealname" defaultValue="" />
									</td>
									<td>
										<pg:equal colName="istaxmanager" value="1"><font color="#0000FF">税管员</font></pg:equal>
										<pg:equal colName="istaxmanager" value="0"><font color="#0000FF">非税管员</font></pg:equal>
										<pg:equal colName="istaxmanager" value=""><font color="#0000FF">非税管员</font></pg:equal>
									</td>
									<td class="tablecells" >
										<font color="#0000FF"><pg:cell colName="org_Name" defaultValue="" /></font>
									</td>	
									<td class="tablecells" nowrap="nowrap" > 
										<font color="#0000FF"><dict:itemname type="userType" expression="{userType}" /></font> 	
    								</td>
									<td class="tablecells" nowrap="nowrap"><font color="#0000FF">
										<pg:notnull colName="userEmail"><pg:cell colName="userEmail" defaultValue=" "/></pg:notnull>
										<pg:null colName="userEmail">没有邮箱</pg:null>
										<pg:equal colName="userEmail" value="">没有邮箱</pg:equal></font>
									</td>	
								</tr>
							<%}%>
							</pg:list>
<%
request.setAttribute("opuser",contextmenu);  
%>
<pg:contextmenu enablecontextmenu="true" context="opuser" scope="request"/> 
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=7 align='center' nowrap>
									 <pg:index />
								
								</td>								 
						    </tr>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=7 align='right' nowrap>
								<%
								if (accesscontroler.checkPermission("orgunit",
										"newdeluser", AccessControl.ORGUNIT_RESOURCE))
						        {%>
	           						<input name="newuser" type="button" class="input" value="新增" onClick="newUser()">	
	           					<%}%>
	           					
	           					<% if(ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",true)){ %>
									<%
									 if (accesscontroler.checkPermission(curOrgId,
						                         "deleteuser", AccessControl.ORGUNIT_RESOURCE))
						            {%>
										<input type="submit" value="移到离散用户" class="input" onclick="javascript:dealRecord(1); return false;">
									<%}%>
	                                
	           					<%}
	           					else{
									 if (accesscontroler.checkPermission("orgunit",
						                         "newdeluser", AccessControl.ORGUNIT_RESOURCE))
						            {%>
										<input type="submit" value="彻底删除用户" class="input" onclick="javascript:dealRecord(2); return false;">
									<%}
	           					}%>
	           					<%
	                                if (accesscontroler.checkPermission("orgunit",
	                                        "usermanager", AccessControl.ORGUNIT_RESOURCE))
	                                {%>	
	                                    <input name="folduser" type="button" class="input" value="用户调入机构" onClick="foldUser()">
		         	  					<input name="foldchangeuser" type="button" class="input" value="调入其他机构" onClick="addorg(1)">
		           					<%}%>
								</td>								 
						    </tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
														
							
						</pg:pager>

				  </table>
				</form>
				<form name="form2" method="POST"></form>
<script language="JavaScript">
	
	var thisUserId = "<%=currUserId.toString()%>";	
	var intervalType = "<%=intervalType%>";
	var taxmanager = "<%=taxmanager%>";
    
    var ops = document.all("intervalType");
    for(var i=0;ops != null && i<ops.options.length;i++){
        if(ops.options[i].value==intervalType) {
          ops.options[i].selected=true;
          break;
        }
    }
    var ops = document.all("taxmanager");
   
    for(var i=0;ops != null && i<ops.options.length;i++){
        if(ops.options[i].value==taxmanager) {
          ops.options[i].selected=true;
          break;
        }
    }


function userResList(userId){
		var typeName = "USER";
		window.showModalDialog("<%=rootpath%>/sysmanager/user/userres_queryframe.jsp?orgId=<%=curOrgId%>&userId="+ userId+"&typeName="+typeName,"","dialogWidth:"+(950)+"px;dialogHeight:"+(700)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
	}
	
	
	


</script>



<iframe name="deluser" height="0" width="0"></iframe>
			</body>
			<center>
</html>

