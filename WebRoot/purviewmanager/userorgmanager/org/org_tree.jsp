<%
/**
 * <p>Title: 机构树</p>
 * <p>Description: 机构树</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ include file="/common/jsp/importtaglib.jsp"%>

<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkManagerAccess(request,response);

String path = request.getContextPath();     
String contextpath = path + "/purviewmanager/userorgmanager/org/";
%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构树</title>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<script language="javascript">
//机构查询
//2008-4-2 baowen.liu
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
		window.location.href="org_tree.jsp";
	}
	catch(e)
	{
		alert(e);
	}
}

function reloadparenthref()
{
	parent.location.href = parent.location.href;
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
		document.forms[0].action = "deleteorg_do.jsp?orgId=" + orgId;
		document.forms[0].target = "hiddenFrame";
		document.forms[0].submit();
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
	$.dialog({ close:reloadparenthref,title:'<pg:message code="sany.pdp.role.organization.job.setting"/>',width:740,height:560, content:'url:<%=contextpath%>userjoborg.jsp?orgId=' + orgId + "&orgName=" + orgName,lock: true});
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
	document.forms[0].action = "refreshcache_handle.jsp?flag=1";
	document.forms[0].target = "hiddenFrame";
	document.forms[0].submit();
}

//机构缓冲刷新
function refreshorgmanagercache(){
	document.forms[0].action = "refreshcache_handle.jsp?flag=2";
	document.forms[0].target = "hiddenFrame";
	document.forms[0].submit();
}

//权限缓冲刷新
function refreshcache(){
	document.forms[0].action = "refreshcache_handle.jsp?flag=3";
	document.forms[0].target = "hiddenFrame";
	document.forms[0].submit();
}

</script>

</head>
<body class="contentbodymargin" scroll="auto">
<form name="orgtreeform" action="" method="post"  >
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
  <tr height=30>

  <td align="center">
  	<a href="#" onclick="searchorg()"><pg:message code="sany.pdp.role.organization.query"/></a>
  </td>
  
  	<%
	//判断是否是系统管理员(即是否是admin)
	//2008-4-7 baowen.liu
	if("1".equals(accessControl.getUserID()))
	{
  	%>
    <td align="center"><a href="#" onclick="neworg()"><pg:message code="sany.pdp.role.organization.new"/></a></td>
   	<%
   	}
   	%>  
  </tr>
</table>
    <table >
        <tr><td align="left">
         <tree:tree tree="org_tree_userorgmanager"
    	           node="org_tree.node"
    	           imageFolder="../../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="../user/userquery_content_tab.jsp" 
    			   target="org_userlist" 
    			   mode="static-dynamic"
    			   >     			    
    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManagerOrgTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   rootNameCode="sany.pdp.organization.tree.name"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="true" 
    	                   />					
    	</tree:tree>
         </td></tr>
    </table>
</form>

</body>
<iframe name="hiddenFrame" width=0 height=0 frameborder="0"></iframe>
</html>

