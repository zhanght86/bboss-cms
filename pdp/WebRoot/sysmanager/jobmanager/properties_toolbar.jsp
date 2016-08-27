<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager" %>
<%

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);
	
	String jobid = request.getParameter("selected");
	if(jobid==null)jobid="";
	//System.out.println("jobid=....."+jobid);
%> 
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<%
	
%>


<jsp:include page="../base/scripts/panes.jsp" />
<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">


var jobid="<%=jobid%>"; 
 if(jobid!=""){
 getPropertiesContent().location.href="../../jobmanager/job.do?method=getJobInfo&jobId="+jobid;	
 }


	function getElementByName(eName){
		var elements = document.body.all;
		for(i = 0;i< elements.length;i++){
           if (elements[i].name==eName) return elements[i];
		}
		return null;
	}
	function selectTool(toolName) {
	    var tool=getElementByName(toolName);
		if (tool) {
		    tool.onclick();
		}
	}
	
	function bussinessAction(action){
		if(jobid == ""){
			alert("请选择左边的岗位中选择一个岗位进行操作 ！");
			return;
			//getPropertiesContent().location.href="../jobmanager/A03/nojob.jsp";
			       	       
		}

		 
		if(jobid!=""){ 		
	    if(action == 1 && getPropertiesContent()){	      
			getPropertiesContent().location.href="../../jobmanager/job.do?method=getJobInfo&jobId="+jobid;       	       
	    }	   
	    else if(action == 2&& getPropertiesContent()){	//隶属机构		
			getPropertiesContent().location.href="../jobmanager/A03/jobsuborglist.jsp?jobId="+jobid;                     
	   	 }
	   	 else if(action == 3&& getPropertiesContent()){	
	   	 	getPropertiesContent().location.href="../jobmanager/A03/job2org.jsp?jobId="+jobid;                     
	   	 }
	   	  else if(action == 4&& getPropertiesContent()){	
	   	 	getPropertiesContent().location.href="../jobmanager/A03/jobSee_tab.jsp?jobId="+jobid;                     
	   	 }
	   	 
	   }
	   
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left"  nowrap class="text">业务操作</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化"  title="双击最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		<table  cellpadding=0 cellspacing=0 border=0>
		<tr>
				<%
				 if (accessControl.checkPermission("jobbaseinfo",
	                       AccessControl.WRITE_PERMISSION, AccessControl.JOB_RESOURCE))
	            {%>
				<td class=normal name="tool1" id="tools1" title="岗位基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick=bussinessAction(1) ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="添加新业务" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位基本信息&nbsp;</td></tr></table></td>
				<%}else if(accessControl.checkPermission("jobbaseinfo",
	                       AccessControl.READ_PERMISSION, AccessControl.JOB_RESOURCE)){%>
	            <td class=normal name="tool1" id="tools1" title="岗位基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this)  ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="添加新业务" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位基本信息&nbsp;</td></tr></table></td>
	            <%}%>
<!------------------------------------------------------------------------------------------------------------------>
				<%
				 if (accessControl.checkPermission("jobsubjectionorg",
	                       AccessControl.WRITE_PERMISSION, AccessControl.JOB_RESOURCE))
	            {%>
				<td class=normal name="tool2" id="tools2" title="隶属机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(2)" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="修改此业务" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;隶属机构&nbsp;</td></tr></table></td>
            	<%}else if(accessControl.checkPermission("jobsubjectionorg",
	                       AccessControl.READ_PERMISSION, AccessControl.JOB_RESOURCE)){%>
				<td class=normal name="tool2" id="tools2" title="隶属机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this)  ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="修改此业务" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;隶属机构&nbsp;</td></tr></table></td>
				<%}%>                      
<!------------------------------------------------------------------------------------------------------------------>
           	
            	 <%
				 if (accessControl.checkPermission("jobconferorg",
                       AccessControl.WRITE_PERMISSION, AccessControl.JOB_RESOURCE))
		        {%>
				<td class=normal name="tool3" id="tools3" title="岗位授予机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(3)" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="岗位授予机构" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位授予机构&nbsp;</td></tr></table></td>
		        <%}else if(accessControl.checkPermission("jobconferorg",
                       AccessControl.READ_PERMISSION, AccessControl.JOB_RESOURCE))
			    {%>
			    <td class=normal name="tool3" id="tools3" title="岗位授予机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this)><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="岗位授予机构" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位授予机构&nbsp;</td></tr></table></td>
			    <%}%>
		        
<!------------------------------------------------------------------------------------------------------------------>
		        
		         <%
				 if (accessControl.checkPermission("jobsee",
                       AccessControl.WRITE_PERMISSION, AccessControl.JOB_RESOURCE))
		        {%>
				<td class=normal name="tool4" id="tools4" title="用户任职情况" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(4)" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="用户任职情况" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户任职情况&nbsp;</td></tr></table></td>
		        <%}else if(accessControl.checkPermission("jobsee",
                       AccessControl.READ_PERMISSION, AccessControl.JOB_RESOURCE))
		        {%>
		        <td class=normal name="tool4" id="tools4" title="用户任职情况" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this)  ><table border=0 cellPadding=0 cellSpacing=0 class="text">
				  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="用户任职情况" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户任职情况&nbsp;</td></tr></table></td>
		        <%}%>
            </tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

