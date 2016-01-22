<%@ page language="java" pageEncoding="utf-8"%>
<%@page import=" org.frameworkset.security.session.impl.SessionHelper"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.*"%><%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%--
	描述：session管理
	作者：谭湘
	版本：1.0
	日期：2014-06-04
	 --%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkManagerAccess(request,response);
String path = request.getContextPath();     
String contextpath = path + "/purviewmanager/userorgmanager/org/";
String userNamesNo = null;
String reFlush = "false";
if (request.getAttribute("reFlush") != null) 
{
	reFlush = "true";
}
String desc = (String)request.getParameter("pager.desc");	
String intervalType = (String)request.getParameter("intervalType");
boolean isJobOpen = ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false);
%>	 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Session管理</title>
<%@ include file="/common/jsp/csscontextmenutree-lhgdialog.jsp"%>

<style type="text/css">
<!--
  #rightContentDiv{
	position:absolute;
	margin-left:220px;
	margin-right:8px;
	width: 80.5%;	   
  }
  
  #leftContentDiv{
	width:200px;
	position:absolute;
	left:0px;
	padding-left:20px;  	   
  }
.a_bg_color{
	color: white;
    cursor:hand;
	background-color: #191970
}
-->
</style>

<script type="text/javascript">

$(document).ready(function() {
	bboss.pager.pagerevent = {   
			                            beforeload:null,   
			                          afterload:null
	};   

	$("#wait").hide();
	loadorgtree();   		
	//queryList(null,false,false);   
	
	
});
       
//加载实时任务列表数据  
function queryList(appkey ,reset,loadextendattrs){
	if(reset)
		doreset();
	 
    
    
}

function loadorgtree()
{
	$("#app_tree_module").load("neworg_tree.jsp");
 
	
	
	
}

function resizetree()
{
	
	$("#orgtreeform").css({'height':($(window).height()-60)+'px','overflow':'auto'});
}
function doreset(){
	$("#reset").click();
}


//机构查询

var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
var win;

function searchorg()
{
	//winuser = window.showModalDialog("organsearch.jsp?jobId=2",
		//								 window,"dialogWidth:"+(850)+"px;dialogHeight:"+(520)+"px;help:no;scroll:auto;status:no");
	
	$.dialog({ title:'<pg:message code="sany.pdp.role.organization.query"/>',width:740,height:560, content:'url:<%=contextpath%>organsearch.jsp?jobId=2',lock: true});
}

//新增一级机构
function neworg()
{
	
  $.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.organization.new.onelevel"/>',width:740,height:300, content:'url:<%=contextpath%>new_org.jsp',lock: true});
  
	
}

function openWin(url,swidth,sheight)
{
	var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
	
	return w;
}

//离散用户处理
function openModalDialogWin(url,swidth,sheight)
{
	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.disperse.user.manage"/>',width:740,height:560, content:'url:'+url,lock: true});
	//var w = showModalDialog(url,window,"dialogWidth:"+swidth+";dialogHeight:"+sheight+";help:no;scroll:auto;status:no");
	//parent.location.href = parent.location.href ;
	//return w;
}

function reloadhref()
{
	
	try
	{
		loadorgtree();
	}
	catch(e)
	{
		alert(e);
	}
}

function reloadparenthref()
{
	loadorgtree();
	//parent.location.href = parent.location.href;
}
//机构排序
function sortOrg(orgId,orgName)
{
	//openWin("sortOrg.jsp?orgId=" + orgId + "&orgName=" + orgName,500,500);
	var url = "<%=contextpath%>sortOrg.jsp?orgId=" + orgId + "&orgName=" + orgName;
	$.dialog({ close:reloadhref,title:'<pg:message code="sany.pdp.role.organization.sort"/>',width:740,height:560, content:'url:'+url,lock: true});
	//window.location.href=window.location.href;
}

//子机构排序
function sortSonOrg(orgId,orgName)
{
	var url = "<%=contextpath%>sortOrg.jsp?orgId=" + orgId + "&orgName=" + orgName;
	$.dialog({ close:reloadparenthref,title:orgName,width:740,height:560, content:'url:'+url,lock: true});
	//openWin("sortOrg.jsp?orgId=" + orgId + "&orgName=" + orgName,500,500);
	//parent.location.reload();
}

//管理员设置
function changeOrgAdmin(orgId)
{
	//openWin("orgAdmin.jsp?orgId=" + orgId,600,500);
	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.manager.set"/>',width:740,height:560, content:'url:<%=contextpath%>orgAdmin.jsp?orgId=' + orgId,lock: true});
	//parent.location.reload();
}

//机构基本信息查看
function checkOrgInfo(orgId,parentId)
{
	$.dialog({ title:'<pg:message code="sany.pdp.role.organization.infomation"/>',width:740,height:560, content:'url:<%=contextpath%>orgInfo.jsp?orgId=' + orgId + "&parentId=" + parentId,lock: true});
	//openWin("orgInfo.jsp?orgId=" + orgId + "&parentId=" + parentId,600,400);
}

//机构基本信息修改
function modifyOrgInfo(orgId,parentId)
{
	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.organization.infomation.modify"/>',width:740,height:560, content:'url:<%=contextpath%>modifyorg.jsp?orgId=' + orgId + "&parentId=" + parentId,lock: true});
	//openWin("modifyorg.jsp?orgId=" + orgId + "&parentId=" + parentId,600,400);
	//parent.location.reload();
}

//删除机构
function deleteOrg(orgId,orgName)
{
	$.dialog.confirm("<pg:message code='sany.pdp.common.operation.remove.confirm'/>", function() {
		document.forms["orgtreeform"].action = "deleteorg_do.jsp?orgId=" + orgId;
		document.forms["orgtreeform"].target = "hiddenFrame";
		document.forms["orgtreeform"].submit();
	});
}
//机构属性管理
function orgAttrs(orgId,orgName)
{
	var url="${pageContext.request.contextPath}/params/showParams.page?paramId="+orgId+"&paramType=org&handler=sys.org.paramshandler";
	$.dialog({title:'<pg:message code="sany.pdp.purviewmanager.user.attrs" />-'+orgName,width:760,height:560, content:'url:'+url,lock: true});
}

//机构转移
//修改传入的参数,加入OrgName
//2008-3-20  baowen.liu
function transferOrg(orgId,orgName)
{   
	//window.open("orgtran.jsp?orgId=" + orgId + "&orgName=" + orgName);
	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.organization.move"/>--'+orgName,width:740,height:560, content:'url:<%=contextpath%>orgtran.jsp?orgId=' + orgId + "&orgName=" + orgName,lock: true});
	//openWin("orgtran.jsp?orgId=" + orgId + "&orgName=" + orgName,400,500);
	//parent.location.reload();
}

//新增子机构
//修改传入的参数,加入OrgName
//2008-3-20  baowen.liu
function addOrgSon(orgId,orgName)
{
	//openWin("new_sonorg.jsp?orgId=" + orgId + "&orgName=" + orgName,600,400);
	//parent.location.reload();
	//$.dialog({ close:reloadhref,title:'机构排序',width:740,height:560, content:'url:'+url,lock: true});
	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.add.child.organization"/>',width:740,height:560, content:'url:<%=contextpath%>new_sonorg.jsp?orgId=' + orgId + "&orgName=" + orgName,lock: true});
}

//机构岗位设置
//新增子机构
//修改传入的参数,加入OrgName
//2008-3-20  baowen.liu
function changeOrgJob(orgId,orgName)
{
	//openWin("userjoborg.jsp?orgId=" + orgId + "&orgName=" + orgName,1000,600);
	$.dialog({ title:'<pg:message code="sany.pdp.role.organization.job.setting"/>',width:740,height:560, content:'url:<%=contextpath%>userjoborg.jsp?orgId=' + orgId + "&orgName=" + orgName,lock: true});
}

//reclaim user's resource under organization
//ge.tao
function reclaimOrgUserRes(orgId){
  //openWin("../../reclaimManager/reclaimOrgUserRes.jsp?orgId=" + orgId,500,400);
  $.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.recover"/>',width:740,height:560, content:'url:<%=contextpath%>../../reclaimManager/reclaimOrgUserRes.jsp?orgId=' + orgId,lock: true});
}

function rightRole(orgId){
	//openWin("changeRole_ajax.jsp?orgId=" + orgId ,800,600);
	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.setting"/>',width:740,height:560, content:'url:<%=contextpath%>changeRole_ajax.jsp?orgId=' + orgId,lock: true});
}

function orgResQuery(orgId){
	//var sty = "dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0";
	//window.showModalDialog("orgres_queryframe.jsp?orgId=" + orgId,window,sty);
	$.dialog({ title:'<pg:message code="sany.pdp.common.operation.search"/>',width:740,height:560, content:'url:<%=contextpath%>orgres_queryframe.jsp?orgId=' + orgId,lock: true});
	//openWin("orgres_queryframe.jsp?orgId=" + orgId ,750,500);
}

function grantOrgRes(orgId){
	//var url = "../../grantmanager/org_resFrame.jsp?currRoleId=" + orgId;
	//window.showModalDialog(url,window,"dialogWidth:"+screen.availWidth+";dialogHeight:"+screen.availHeight+";help:no;scroll:auto;status:no");

	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.organization.empower"/>',width:900,height:560, content:'url:<%=contextpath%>../../grantmanager/org_resFrame.jsp?currRoleId=' + orgId,lock: true});
}

//机构缓冲刷新
function refreshorgcache(){
	document.forms["orgtreeform"].action = "refreshcache_handle.jsp?flag=1";
	document.forms["orgtreeform"].target = "hiddenFrame";
	document.forms["orgtreeform"].submit();
}

//机构缓冲刷新
function refreshorgmanagercache(){
	document.forms["orgtreeform"].action = "refreshcache_handle.jsp?flag=2";
	document.forms["orgtreeform"].target = "hiddenFrame";
	document.forms["orgtreeform"].submit();
}

//权限缓冲刷新
function refreshcache(){
	document.forms["orgtreeform"].action = "refreshcache_handle.jsp?flag=3";
	document.forms["orgtreeform"].target = "hiddenFrame";
	document.forms["orgtreeform"].submit();
}

function loaduserlist(orgId )
{
	$("#orgId").val(orgId);
	$("#app_tree_module a").removeClass("a_bg_color");
	$("a[name='"+orgId+"']").addClass("a_bg_color"); 
	$("#userContainer").load("org_userlistIframe.jsp?orgId="+orgId);
}

</script>

</head>

<body>
<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
	<sany:menupath />

	<div id="leftContentDiv">
			    
		<div class="left_menu" style="width:200px;">
		    <ul>
		    	<li >
		    		<a href="javascript:void(0)" onclick="searchorg()"><pg:message code="sany.pdp.role.organization.query"/></a> <%
	//判断是否是系统管理员(即是否是admin)
	//2008-4-7 baowen.liu
	if(accesscontroler.isAdmin())
	{
  	%>
     &nbsp;&nbsp;<a href="javascript:void(0)" onclick="neworg()"><pg:message code="sany.pdp.role.organization.new"/></a> 
   	<%
   	}
   	%>  
   	&nbsp;&nbsp;<a href="javascript:void(0)" id="_type_1">右键操作</a>
   	
		    		<ul style="display: block;" >
		    			<div id="app_tree_module" style="background-color: white;margin:0 auto;overflow:auto;" align="left"></div>
		    		</ul>
		    	</li>
		    </ul>
		</div>
		
	</div>
		
	<div id="rightContentDiv">
			
		 <script language="javascript">
		    
			var isSucceed = '<%=request.getParameter("isSucceed")%>';
			var delSucceed = '<%=request.getParameter("delSucceed")%>';
			var isMain = '<%=request.getParameter("isMain")%>';
			var isRes = '<%=request.getParameter("isRes")%>';
			var userNamesNo = '<%=userNamesNo%>';
			
			//提示新增用户是否成功
			if(isSucceed=="1")
			{
				$.dialog.alert("<pg:message code="sany.pdp.add.user.success"/>@",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				var str = document.location.href;
				parent.window.document.location.href = str.substring(0,str.indexOf("isSucceed"));
			}
			
			//提示移除（删除）用户是否成功
			if(delSucceed=="2")
			{
				if(userNamesNo != "" && userNamesNo != "null"){
					$.dialog.alert("<pg:message code='sany.pdp.some.user.delete.fail'/>: \n"+userNamesNo,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				}
				else
				{
					$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				}
				var str = document.location.href;
				parent.window.document.location.href = str.substring(0,str.indexOf("delSucceed"));
			}
			
			//主机构
			if(isMain=="main")
			{
				$.dialog.alert("<pg:message code='sany.pdp.can.not.delete.mian.organization'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			
			
			var reFlush = "<%=reFlush%>";
			var isSelf = "<%=request.getParameter("isSelf")%>";
			
			if(isSelf == "true")
			{
				$.dialog.alert("<pg:message code='sany.pdp.can.not.delete.self'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			
			function getUserInfo(e,userId)
			{
				
			}
			
			
			function roleUser(type) 
			{
				var checks = "";
			    var arr = new Array();
			    arr = window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    var count = 0;
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks=="")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    		count++;
			    	}
			    }
			    //if(count < 2){
			    //	alert("");
			    //}
			    var winRole;
			    if (checks.split(",").length > 1)
			    {
			    	if (type==1)
			    	{
			    		userList.target = "deluser";
			    		var curOrgId = $("#orgId").val();
			    		var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/alotUserRole.jsp?orgId="+curOrgId+"&checks="+checks;
			    		$.dialog({ close:queryUser,title:'<pg:message code="sany.pdp.sys.role.authorize"/>',width:740,height:560, content:'url:'+url,lock: true});
					} 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
				
			function sortBy(byName)
			{
				var desc = "false";
			
				if ( "<%=desc%>"  == desc ) desc = "true";
				var curOrgId = $("#orgId").val();
				userList.action="${pageContext.request.contextPath}/user/userList.jsp?orgId="+curOrgId+"&pager.offset=0&pager.sortKey="+byName+"&pager.desc="+desc;
				userList.submit();
			}
				
			function actionOnmouseover(e)
			{	
				e.style.backgroundColor = "#8080FF";
			}
			
			function actionOnmouseup(e)
			{
				e.style.backgroundColor = "#BEC0C2";
			}
			
			function newUser()
			{
				var curOrgId = $("#orgId").val();
				$.dialog({ close:queryUser,title:'<pg:message code="sany.pdp.add.user"/>',width:740,height:560, content:'url:<%=path%>/purviewmanager/userorgmanager/user/userInfo.jsp?orgId='+curOrgId+'&isNewUser=true',lock: true});
			}
			
			function queryUser()
			{	
				var intervalType = document.all("intervalType").value;
				var userName = document.all("userName").value;
				var userRealname = document.all("userRealname").value;
				var userIsvalid = document.all("userIsvalid").value;
				var userSex = document.all("userSex").value;
				var userType = document.all("userType").value;
				
				var userName = userList.userName.value;
				var userRealname = userList.userRealname.value;
				
				
				
				if(userName == "" || userName.length<1 || userName.replace(/\s/g,"")=="")
				{
					
				}
				else
				{
						var re_ = /^\w+$/; 
						if(!re_.test(userName))
						{
						$.dialog.alert('<pg:message code="sany.pdp.check.username"/>');
						return false; 
						}
				}
				
				if(userRealname == "" || userRealname.length<1 || userRealname.replace(/\s/g,"")=="")
					{
					
					}
				else
					{
						var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
						if(!re.test(userRealname))
						{
						$.dialog.alert("<pg:message code="sany.pdp.check.realname"/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						return false; 
						}
					}
				
				
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
			function jobUser(type) 
			{
			    var arr = new Array();
			    arr = window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    var checks = "";
				var count = 0;
				
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks=="")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    	}
			    }
			    
			    var winjob;
			    if (checks.split(",").length > 1)
			    {
			    	if(type==1)
			    	{
			    		var curOrgId = $("#orgId").val();
				    	var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/allJobFrame.jsp?orgId="+curOrgId+"&checks="+checks;
				    	$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sys.post.authorize"/>',width:760,height:560, content:'url:'+url,lock: true});
				    } 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			
			//批量机构授予
			function orgUser(type) 
			{
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
						$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!");
						return;
					}
					   
				    for (var i=0;i<userList.elements.length;i++) 
				    {
						var e = userList.elements[i];
							
						if (e.name == 'checkBoxOne')
						{
							if (e.checked)
							{
					       		isSelect=true;
					       		break;
						    }
						}
				    }
				    if (isSelect)
				    {
				    	if (type==1)
				    	{
				    		var curOrgId = $("#orgId").val();
				    		    userList.action="${pageContext.request.contextPath}/user/userOrg.do?orgId="+curOrgId+"&method=getAllOrgList";
				    		    userList.submit();
							    return true;
						} 
				    }
				    else
				    {
				    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				    	return false;
				    }
					return false;
			}
			//批量资源操作授予
			function operUser(type) 
			{
			    var arr = new Array();
			    arr = window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    var checks = "";
			    var count = 0;
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks == "")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    		count++;
			    	}
			    }
			    var winRes;
			    if(checks.split(",").length > 1)
			    {
			    	if (type==1)
			    	{   
			    		var curOrgId = $("#orgId").val();
			    		var url = "${pageContext.request.contextPath}/purviewmanager/grantmanager/userBatch_resFrame.jsp?orgId="+curOrgId+"&checks="+checks;
			    		$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sys.purview.authorize"/>',width:760,height:560, content:'url:'+url,lock: true});
					} 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			
			//批量用户权限回收
			function purviewReclaimUsers()
			{
			    var arr = new Array();
			    arr = window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    var checks = "";
			    var count = 0;
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks == "")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    		count++;
			    	}
			    }
			    var winRes;
			    if(checks.split(",").length > 1)
			    {    
			    	
			    	
			    	 var url = "${pageContext.request.contextPath}/purviewmanager/reclaimManager/reclaimUserRes.jsp?userIds="+ checks;
						$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sys.purview.recycle"/>',width:700,height:280, content:'url:'+url,lock: true});
			    	
					 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			//var winOpen
			function orderUser()
			{
				var curOrgId = $("#orgId").val();
				var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/user_order_ajax.jsp?orgId="+curOrgId;
				$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sort.user"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			var timer;
			function isClosed()
			{
			    if(winOpen.closed==true)
			    {
			        window.location.href = window.location;
			        window.clearInterval(timer);
			    }
			}
			
			function foldUser()
			{
				var win;
				//调入用户
				var curOrgId = $("#orgId").val();
				var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/org/folduser_frame.jsp?orgId="+curOrgId;
				var orgpath  = $("#orgpath").html();
				$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.move.in.user.alert" arguments="'+orgpath+'"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			function addorg(dealType) 
			{
			    var outMsg;
			    var checks = "";
			    var state = false;
			   var obj=window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    if(obj.length==0)
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.no.user.to.move.out' />");
			    	return;
			    }
			    for(var j=0; j < obj.length; j++)
			    {
			    	if(obj[j].checked)
			    	{
			    		if(state)
			    		{
			    			checks += ","+obj[j].value;
			    		}
			    		else
			    		{
			    			checks = "" + obj[j].value;
			    			state = true;
			    		}
			    	}
			    }
			    if(checks != "")
			    {
				    if(dealType==1)
				    {
						var winaddorg;
						var curOrgId = $("#orgId").val();
						var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/org/userChangeOrg.jsp?checkBoxOne="+checks + "&orgId="+curOrgId;
						var orgpath  = $("#orgpath").html();
						$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.move.out.user.alert" arguments="'+orgpath+'"/>',width:760,height:560, content:'url:'+url,lock: true});
					}
				}
				else
				{
					$.dialog.alert("<pg:message code='sany.pdp.choose.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			
			//彻底删除用户
			function quiteDelUser()
			{
				var arr = new Array();
				//var arr = $(this).contents().find(".checkBoxOne");
				var arr=window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
				var checks = "";
				for(var i = 0; i < arr.length; i++)
				{
					if(arr[i].checked)
					{
						if(checks == "")
						{
							checks = arr[i].value;
						}else{
							checks += "," + arr[i].value;
						}
					}
				}
				
				if(checks == "")
				{
					$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.remove.select'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				else
				{
					outMsg = "<pg:message code='sany.pdp.delete.comfirm'/>?";
					$.dialog.confirm(outMsg,function(){
						document.userList.target = "deluser";
						var curOrgId = $("#orgId").val();
						document.userList.action = "../../userorgmanager/user/quiteDelUser.jsp?checks="+checks+"&orgId="+curOrgId;
						//win = window.showModelessDialog("<%=path%>/purviewmanager/common/doing.jsp","",featrue);
						document.userList.submit();
						},function(){},null,"<pg:message code='sany.pdp.common.alert'/>"
					)
				}
			}
			
			function userResList(userId)
			{
				var typeName = "USER";
				var curOrgId = $("#orgId").val();
				window.showModalDialog("/sysmanager/user/userres_queryframe.jsp?orgId="+curOrgId+"&userId="+ userId+"&typeName="+typeName,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
			}
			</SCRIPT>

<div class="titlebox">
	<SPAN class=location>当前机构：<A href="javascript:void(0)" id="orgpath"></A></SPAN>
</div>
<div class="mcontent">

	<form name="userList" id="userList" method="post"
		action="org_userlistIframe.jsp" target="orgUserList">
		<input type="hidden" name="orgId"  />
		<div id="searchblock">
			<div class="search_top">
				<div class="right_top"></div>
				<div class="left_top"></div>
			</div>
			<div class="search_box">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="left_box"></td>
						<td>
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="table2">
								<tr>
									<th width="80" height="24"><pg:message
											code="sany.pdp.user.login.name" />：</th>
									<td><input type="text" name="userName" size="17"></td>
									<th><pg:message code="sany.pdp.user.real.name" />：</th>
									<td><input type="text" name="userRealname" size="17"></td>
									<th><pg:message code="sany.pdp.user.type" />：</th>
									<td><dict:select type="userType" name="userType"
											expression="{userType}" defaultValue=""
											textValue="<pg:message code='sany.pdp.common.operation.select' />"
											style="width:115px" /></td>
								</tr>
								<tr>
									<th height="24"><pg:message code="sany.pdp.sex" />：</th>
									<td><dict:select type="sex" name="userSex"
											expression="{userSex}" defaultValue=""
											textValue="<pg:message code='sany.pdp.common.operation.select'/>"
											style="width:115px" /></td>
									<th><pg:message code="sany.pdp.common.status" />：</th>
									<td><dict:select type="isvalid" name="userIsvalid"
											expression="{user_isvalid}" defaultValue=""
											textValue="<pg:message code='sany.pdp.common.operation.select'/>"
											style="width:115px" /></td>
									<th><pg:message code="sany.pdp.query.method" />：</th>
									<td><select name="intervalType" class="select"
										style="width: 115px">
											<option value="0" selected><pg:message
													code="sany.pdp.not.recursion.query" /></option>
											<option value="1"><pg:message
													code="sany.pdp.recursion.query" /></option>
									</select></td>

								</tr>
							</table>
						</td>
						<td class="right_box"></td>
					</tr>
				</table>
			</div>
			<div class="search_bottom">
				<div class="right_bottom"></div>
				<div class="left_bottom"></div>
			</div>
		</div>
	</form>
	<div class="title_box">
		<div class="rightbtn">
			<a href="javascript:void(0)" class="bt_1" onClick="queryUser()"><span><pg:message
						code="sany.pdp.common.operation.search" /></span></a> <a
				href="javascript:void(0)" class="bt_2" onClick="userList.reset()"><span><pg:message
						code="sany.pdp.common.operation.reset" /></span></a>
			<%
				if (accesscontroler.checkPermission("orgunit", "newdeluser", AccessControl.ORGUNIT_RESOURCE)) {
			%>
			<a href="javascript:void(0)" class="bt_small" onClick="newUser()"><span><pg:message
						code="sany.pdp.add.user" /></span></a> <a href="javascript:void(0)"
				class="bt_small" onClick="quiteDelUser()"><span><pg:message
						code="sany.pdp.delete" /></span></a>
			<%
				}
			%>
			<a href="javascript:void(0)" class="bt_small" onClick="orderUser()"><span><pg:message
						code="sany.pdp.sort.user" /></span></a>
			<%
				if (accesscontroler.checkPermission("orgunit", "usermanager", AccessControl.ORGUNIT_RESOURCE)) {
			%>
			<a href="javascript:void(0)" class="bt_small" onClick="foldUser()"><span><pg:message
						code="sany.pdp.move.in.user" /></span></a> <a href="javascript:void(0)"
				class="bt_small" onClick="addorg(1)"><span><pg:message
						code="sany.pdp.move.out.user" /></span></a>
			<%
				}
			%>
			<a href="javascript:void(0)" class="bt_small" onClick="roleUser(1)"><span><pg:message
						code="sany.pdp.sys.role.authorize" /></span></a>
			<%
				if (isJobOpen) {
			%>
			<a href="javascript:void(0)" class="bt_small" onClick="jobUser(1)"><span><pg:message
						code="sany.pdp.sys.post.authorize" /></span></a>
			<%
				}
			%>
			<%
				if (accesscontroler.checkPermission("orgunit", "purset", AccessControl.ORGUNIT_RESOURCE)) {
			%>
			<a href="javascript:void(0)" class="bt_small" onClick="operUser(1)"><span><pg:message
						code="sany.pdp.sys.purview.authorize" /></span></a> <a
				href="javascript:void(0)" class="bt_small"
				onClick="purviewReclaimUsers()"><span><pg:message
						code="sany.pdp.sys.purview.recycle" /></span></a>
			<%
				}
			%>
		</div>
	</div>
	
	<div id="userContainer" style="overflow:auto">请选择一个部门</div>
	 
	<form name="form2" method="POST"></form>
	<iframe name="deluser" height="0" width="0" border="0"></iframe>
</div>
		
		
		
	</div>
</div>
</body>
</html>
