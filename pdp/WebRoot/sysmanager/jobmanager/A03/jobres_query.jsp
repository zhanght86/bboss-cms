
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.frameworkset.platform.config.model.ResourceInfo"%>
<%@ page import="com.frameworkset.platform.config.ResourceInfoQueue"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,
								 com.frameworkset.platform.config.model.Operation,
								 com.frameworkset.platform.resource.ResourceManager,
								 com.frameworkset.platform.sysmgrcore.entity.Job,
								 com.frameworkset.platform.sysmgrcore.manager.JobManager,
								 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);

String jobId = (String)request.getParameter("jobId");
JobManager jm = SecurityDatabase.getJobManager();
String jobName = null;
if(jobId != null && !jobId.equals("")){
	Job job = jm.getJobById(jobId);
	jobName = job.getJobName();
}

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
            
            //if(listName.equals("资源类资源")||listName.equals("用户资源")||listName.equals("用户组资源")||listName.equals("字典资源"))
            	//resQueue.remove(i);
			
		 }

request.setAttribute("resList",resQueue);

%>

<html>
<head>    
 <title>岗位【<%=jobName%>】资源权限查询</title>
<script language="JavaScript" src="../common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">
 

function sub(){
	jobreslist.action="jobres_querylist.jsp";
	jobreslist.submit();
}

function getOperateType(id){

    getopergroup.location.href = "resChange.jsp?resTypeId="+id;

}
</SCRIPT>
<%@ include file="/include/css.jsp"%>
</head>

<body scrolling="no">
<div id="contentborder" align="center">
	<form name = "jobreslist" method="post" target="forDocList">
	<input type="hidden" name="jobId" value="<%=jobId%>">
		<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">
			
			<tr>
				<td height='20'>
					资源名称：<input type="text" name="resName"/>
					资源标识：<input type="text" name="resid"/>
				    <select class="select" name="resTypeId" onchange="getOperateType(this.options[this.selectedIndex].value)">
				      	<option value="">--请选择资源类型--</option>
				      	<pg:list requestKey="resList" needClear="false">
					    	<option value="<pg:cell colName="id"/>">
					        	<pg:cell colName="name"/>
							</option>
						</pg:list>
					</select>
					<select class="select" name="opId" id="opId">
				      	<option value="">--请选择资源操作--</option>
								
					</select>
				</td>
				<td>
					<input name="search" type="button" class="input" value="查询" onClick="sub()">
					<input name="reset" type="reset" class="input" value="重设">
				</td>
				</tr>
		</table>	
	</form>	
</div>
<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
</body>

</html>

