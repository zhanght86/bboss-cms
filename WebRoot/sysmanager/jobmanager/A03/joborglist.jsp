<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>

<%@ page import="com.frameworkset.platform.security.AccessControl,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				 com.frameworkset.platform.sysmgrcore.manager.JobManager,
				 com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);

String jobId = request.getParameter("jobId");
JobManager jm = SecurityDatabase.getJobManager();
Job job = jm.getJobById(jobId);
String jobName = job.getJobName();

String orgName = request.getParameter("orgName");
String orgnumber = request.getParameter("orgnumber");
if(orgName == null)
{
	orgName = "";
}
if(orgnumber == null)
{
	orgnumber = "";
}
%>
<html>
<head>
<TITLE>岗位机构</TITLE>
<%@ include file="/include/css.jsp"%>
<link href="../../css/contentpage.css" rel="stylesheet" type="text/css">
<link href="../../css/tab.winclassic.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="../common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>

<script language="JavaScript">
function conferorg()
{
	//岗位授予机构
	var jobId = getPropertiesContent().document.all.item("checkBoxOne");
	var jobId1 = getPropertiesContent().document.all.item("jobId");
	
	if(jobId1!=null){
		var temp = "";
	
		if(!jobId1)
		{
			return;
		}
		if(jobId1.length)
		{
			temp = jobId1[0]; 
		}
		else
		{
			temp = jobId1; 
		}
		window.showModalDialog("<%=rootpath%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+temp.value,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
		//getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+temp.value;
		
	}else{
	if(!jobId)
	{
		
		return;
	}
	if(!jobId.length )
	{
		if(!jobId.checked)
		{
			alert("请选择要查看的岗位！");
			return;
		}
		//授权
		window.showModalDialog("<%=rootpath%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+jobId.value,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
		//getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+jobId.value;
		
		}
	
	if(jobId.length)
	{
		
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < jobId.length; i ++)
		{
			if(!jobId[i].checked)
			{
				continue;
			}
			else
			{				
				count ++;
				index = i;
			}
		}
		
		if(count == 0)
		{
			alert("请选择要查看的岗位！");
			return;
		}
		else if(count == 1)
		{
			//授权
			window.showModalDialog("<%=rootpath%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+jobId[index].value,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
			//getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+jobId[index].value;
			
		}
		else
		{
			alert("只能选择一个岗位进行查看！");
			return;
		}
	  }	
	}
}
//---------------------------------------------------------------------------------------------------------------------
function sub(){
	joborgForm.action="joborglist.jsp?jobId=<%=jobId%>";
	joborgForm.submit();
}

function userResList(orgId){
	var url = "<%=rootpath%>/sysmanager/jobmanager/A03/jobList_ajax.jsp?jobId=<%=jobId%>&orgId=" + orgId;
	//alert(url);
	window.showModalDialog(url,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
}

function resetForm(){
	document.all("orgName").value = "";
	document.all("orgnumber").value = "";
}
</script>
</head>

<body class="contentbodymargin">
<div id="contentborder" align="center">
	<form name="joborgForm" action="" method="post">
		<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">
		<input type="hidden" name="jobId" value="<%=jobId%>" />
			<tr class="labeltable_middle_td">
				<td height='20' class="tablecells">
					机构名称:<input type="text" name="orgName" value="<%=orgName%>" />
					机构编号:<input type="text" name="orgnumber" value="<%=orgnumber%>" />
				</td>
				<td height='20' class="tablecells">
					<input name="search" type="button" class="input" value="查询" onClick="sub()">
					<input name="reset" type="button" class="input" value="重设" onClick="resetForm()">
					<!--
					<input name="reset" type="button" class="input" value="岗位授予机构" onClick="conferorg()">
					-->
				</td>
			</tr>
		</table>
		<hr/>
		<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">
			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.JobOrgList" keyName="JobOrgList"/>
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="10" scope="request" data="JobOrgList" isList="false">
			<tr class="labeltable_middle_td">
				<td colspan="5" align="center"><strong>岗位(<%=jobName%>)隶属机构列表</strong></td>
			</tr>
			<tr class="labeltable_middle_td">
			<!--设置分页表头-->
				<td height='20' class="headercolor">机构名称</td>
				<td height='20' class="headercolor">机构编号</td>
				<td height='20' class="headercolor">机构排序</td>
				<td height='20' class="headercolor">机构描述</td>
				
			</tr>
			<pg:param name="jobId"/>
			<pg:param name="orgnumber"/>
			<pg:param name="orgName"/>
			<!--检测当前页面是否有记录-->
			<pg:notify>
			<tr height="18px" >
				<td class="detailcontent" colspan=100 align='center'>暂时没有资源</td>
			</tr>
			</pg:notify>
			<%ContextMenu contextmenu = new ContextMenuImpl();%>
			<!--list标签循环输出每条记录-->			      
			<pg:list>	
			<%
				
				String orgIdm = dataSet.getString("orgId");
				Menu menu = new Menu();
				menu.setIdentity("opuser_"+orgIdm);
				Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
				menuitem1.setName("角色设置");
				menuitem1.setLink("javascript:userResList('"+orgIdm+"')");
				menuitem1.setIcon("../../../sysmanager/images/rightMemu/doc_fbyl.gif");
				menu.addContextMenuItem(menuitem1);
				contextmenu.addContextMenu(menu);
			%>
			<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
				<td id="opuser_<%=orgIdm%>" class="tablecells" align=left width="50%">
					<pg:null colName="remark5">
						<pg:cell colName="orgName"/>
					</pg:null>
					<pg:notnull colName="remark5">
					   <pg:equal colName="remark5" value=""><pg:cell colName="remark5"/></pg:equal>
					   <pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
					</pg:notnull>
				</td>
				<td class="tablecells" align=left><pg:cell colName="orgnumber" defaultValue=""/></td>
				<td class="tablecells" align=left><pg:cell colName="orgSn" defaultValue=""/></td>
				<td class="tablecells" align=left>
				<pg:equal colName="orgdesc" value="">没有描述</pg:equal>
				<pg:notequal colName="orgdesc" value=""><pg:cell colName="orgdesc"/></pg:notequal>
				</td>
			</tr>
			</pg:list>
			<tr height="18px" >
				<td class="detailcontent" colspan=4 align='center'> <pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			</tr>   		
			<%
				request.setAttribute("opuser", contextmenu);  
			%>
			<pg:contextmenu enablecontextmenu="false" context="opuser" scope="request"/> 	   	      
			</pg:pager>
		</table>
	</form>
</div>
</body>
</html>
