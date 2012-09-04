<%@ include file="../../include/global1.jsp"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.config.ConfigManager"%>
<%

AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);

String jobid = request.getParameter("selected");
if(jobid==null)jobid="";
%> 
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../../css/toolbar.css">
<%

%>


<jsp:include page="../../base/scripts/panes.jsp" />
<script language="javascript" src="../../scripts/toolbar.js"></script>
<script language="javascript">
function jobinfo()
{
	//岗位基本信息	
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
		
		getPropertiesContent().location.href="jobmain.jsp?jobId="+temp.value;;
		
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
		getPropertiesContent().location.href="jobmain.jsp?jobId="+jobId.value;
		
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
			getPropertiesContent().location.href="jobmain.jsp?jobId="+jobId[index].value;
			
		}
		else
		{
			alert("只能选择一个岗位进行查看！");
			return;
		}
	  }	
	}
}
//------------------------------------------------------------------------------------------------------------------------------------
function suborg()
{
	//隶属机构
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
		
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobsuborglist.jsp?jobId="+temp.value;;
		
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
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobsuborglist.jsp?jobId="+jobId.value;
		
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
			getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobsuborglist.jsp?jobId="+jobId[index].value;
			
		}
		else
		{
			alert("只能选择一个岗位进行查看！");
			return;
		}
	  }	
	}
}
//------------------------------------------------------------------------------------------------------------------------------------
function roleop()
{
	//角色设置
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
		
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/joborglist.jsp?jobId="+temp.value;
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
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/joborglist.jsp?jobId="+jobId.value;
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
			getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/joborglist.jsp?jobId="+jobId[index].value;
		}
		else
		{
			alert("只能选择一个岗位进行查看！");
			return;
		}
	  }	
	}
}
//------------------------------------------------------------------------------------------------------------------------------------
function resop()
{
	//资源权限查询
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
		window.showModalDialog("<%=rootpath%>/sysmanager/jobmanager/A03/jobres_queryframe.jsp?jobId="+temp.value,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
		//getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobres_queryframe.jsp?jobId="+temp.value;
		
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
		window.showModalDialog("<%=rootpath%>/sysmanager/jobmanager/A03/jobres_queryframe.jsp?jobId="+jobId.value,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
		//getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobres_queryframe.jsp?target=blank&jobId="+jobId.value;
		
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
			window.showModalDialog("<%=rootpath%>/sysmanager/jobmanager/A03/jobres_queryframe.jsp?jobId="+jobId[index].value,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
			//getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobres_queryframe.jsp?jobId="+jobId[index].value;
			
		}
		else
		{
			alert("只能选择一个岗位进行查看！");
			return;
		}
	  }	
	}
}
//------------------------------------------------------------------------------------------------------------------------------------

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
		
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+temp.value;;
		
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
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+jobId.value;
		
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
			getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/job2org.jsp?jobId="+jobId[index].value;
			
		}
		else
		{
			alert("只能选择一个岗位进行查看！");
			return;
		}
	  }	
	}
} 
//------------------------------------------------------------------------------------------------------------------------------------
function post()
{
	//用户任职情况
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
		
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobSee_tab.jsp?jobId="+temp.value;;
		
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
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobSee_tab.jsp?jobId="+jobId.value;
		
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
			getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobSee_tab.jsp?jobId="+jobId[index].value;
			
		}
		else
		{
			alert("只能选择一个岗位进行查看！");
			return;
		}
	  }	
	}
}
var jobid="<%=jobid%>"; 
 if(jobid!=""){
	//alert(getPropertiesContent().location.href) 
 	getPropertiesContent().location.href="jobmain.jsp?jobId="+jobid;	
 }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../../images/actions.gif"></td>
		<td valign="middle"
			align="left"  nowrap class="text">业务操作</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		<table  cellpadding=0 cellspacing=0 border=0>
		<tr>
				<%
				if (accessControl.checkPermission(jobid,
	                       AccessControl.WRITE_PERMISSION, AccessControl.JOB_RESOURCE))
	            {%>
				<td class=normal name="tool1" id="tools1" title="岗位基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="jobinfo()"><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../../images/new.gif" alt="添加新业务" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位基本信息&nbsp;</td></tr></table></td>
				<%}%>
				
<!------------------------------------------------------------------------------------------------------------------>
				
				<!-- 岗位机构角色设置 改为 岗位隶属机构查询 -->
				<%
				 if (accessControl.checkPermission(jobid,
	                       "roleset", AccessControl.JOB_RESOURCE))
	            {%>
				<td class=normal name="tool5" id="tools5" title="岗位隶属机构查询" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="roleop()"><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="岗位隶属机构查询" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位隶属机构查询&nbsp;</td></tr></table></td>
				<%}%>
				
<!------------------------------------------------------------------------------------------------------------------>
				<%
				if(ConfigManager.getInstance().getConfigBooleanValue("enablejob", false))
				{
				%>
								
<!------------------------------------------------------按机构条件、角色条件查询，------------------------------------------------------------>
				<%
				 if (false && accessControl.checkPermission(jobid,
	                       "respurviewsearch", AccessControl.JOB_RESOURCE))
	            {%>
				<td class=normal name="tool6" id="tools6" title="资源权限查询" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="resop()">
				<table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="资源权限查询" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;资源权限查询&nbsp;</td></tr></table></td>
				<%}%>
<!------------------------------------------------------------------------------------------------------------------>
           	
            	<%
				 if (accessControl.checkPermission(jobid,
                       "orgset", AccessControl.JOB_RESOURCE))
		        {%>
				<td class=normal name="tool3" id="tools3" title="岗位授予机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="conferorg()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="岗位授予机构" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位授予机构&nbsp;</td></tr></table></td>
		        <%}%>
		        
<!------------------------------------------------------------------------------------------------------------------>
		         <%
				 if (accessControl.checkPermission(jobid,
                       "userholdpost", AccessControl.JOB_RESOURCE))
		        {%>
				<td class=normal name="tool4" id="tools4" title="用户任职情况" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="post()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="用户任职情况" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户任职情况&nbsp;</td></tr></table></td>
		        <%}}%>
		     
            </tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>
