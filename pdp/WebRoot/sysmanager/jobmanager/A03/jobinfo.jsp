<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ include file="../../include/global1.jsp"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(pageContext);
	
	String QueryjobName = null;
	String QueryjobDesc = null;
	if(request.getParameter("jobName")==null){
		QueryjobName = "";
	}else{
		QueryjobName = request.getParameter("jobName");
	}
	if(request.getParameter("jobDesc")==null){
		QueryjobDesc = "";
	}else{
		QueryjobDesc = request.getParameter("jobDesc");
	}
	
	String action = (String)request.getAttribute("action");
	if((action == null) || (action.equals("")))
	{
		action = request.getParameter("action");
	}
	if((action == null) || (action.equals("")))
	{
		action = "";
	}
	if((action.equals("add")) || (action.equals("delete")))
	{
		%>
		<script language="JavaScript">
			alert("岗位信息操作成功！");
		</script>
		<%
	}
	if(action.equals("delete"))
	{
		QueryjobName = "";
		QueryjobDesc = "";
	}
	
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(control.getUserID());
	boolean isAdmin = control.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}	
%>
<html>
	<head>
		<title>属性容器</title>
		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/tab.winclassic.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
<script language="JavaScript">

	function updateAfter(){
		getNavigatorContent().location.href ="../sysmanager/jobmanager/navigator_content.jsp?anchor=0&expand=0&request_scope=session"; 
	}

	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	function getJobInfo(e,jobId){
	jsAccessControl.setBackColor(e);
	document.all("selectId").value = jobId;
	}
	function createjob()
	{
		getPropertiesContent().location.href="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/addjob.jsp";   
		
	}
	
	function deletejob(){
		var jobId = document.all("selectId").value;
		if(jobId.length <1){
			alert("请选择要删除的岗位！");
			return;
		}
		if(window.confirm("你确定要删除此岗位吗？(删除后是不可以再恢复的)")){	
		document.forms[0].action="../../../jobmanager/job.do?method=deletejobid&flag=1&jobId="+jobId;
	   	document.forms[0].submit();
	   	}
	}
	
	function dealRecord(dealType) {
    var isSelect = false;
    var outMsg;
	    
    for (var i=0;i<JobForm.elements.length;i++) {
		var e = JobForm.elements[i];
			
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
        	if (confirm(outMsg)){
				JobForm.action="../../../jobmanager/job.do?method=deleteJobAll&flag=1";
				JobForm.submit();
	 			return true;
			}
		} 
    }else{
    	alert("至少要选择一条记录！");
    	return false;
    }
	return false;
}





	function getJob(){
	var jobId = document.all("selectId").value;
	window.location.href="../../../jobmanager/job.do?method=getJobInfo&jobId="+jobId;
	}
	
	function resetQuery(){
		document.JobForm.jobName.value="";
		document.JobForm.jobDesc.value="";
	}
	
	function query()
	{
		document.JobForm.action = "<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobinfo.jsp";
		document.JobForm.submit();
	}
</script>												
	<body class="contentbodymargin" scroll="no"  onLoad="<%if(action.equals("delete"))out.print("updateAfter()");%>">
	<%if(isAdminOrOrgManager){ %>
		<div id="contentborder" align="center">
			<form name="JobForm" action="" method="post">
				<table width="100%" border="0"   cellpadding="1" cellspacing="1" class="thin">
                  <tr>
                    <td class="detailtitle" width="10%"> 岗位名称 </td>
                    <td class="detailcontent" width="30%"><input type="text" name="jobName" style="width:50%" value="<%=QueryjobName%>">
                    </td>
                    <td></td>               
                  </tr>
                  <tr>
                    <td class="detailtitle"> 岗位描述 </td>
                    <td class="detailcontent"><input type="text" name="jobDesc" style="width:50%" value="<%=QueryjobDesc%>">
                    </td>  
                    <td class="detailcontent" width="15%" align="center" colspan="2">
					
                        <input name="Submit4" type="button" class="input" onclick="query()" value="查询">
                        <input name="Submit22" type="button" class="input" value="重置" onclick="resetQuery()">
                    </td>                  
                  </tr>
                </table>
				<hr width="100%">

				<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">

					<tr>
						<td height='30' class="detailtitle" align=center colspan=7>
							<b>岗位列表</b>
						</td>
					</tr>
					<pg:listdata dataInfo="JobSubList" keyName="JobList" />
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="10" scope="request" data="JobList" isList="false">
						<tr class="labeltable_middle_td">
							<!--设置分页表头-->
							<td class="headercolor" width="6%">
								<P align="left">
								<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on">
								</P>
							</td>
				    		<td height='20' class="headercolor">岗位名称</td>
				    		<input class="text" type="hidden" name="selectId">
							<td height='20' class="headercolor">岗位描述</td>
							<td height='20' class="headercolor">岗位职责</td>
							<td height='20' class="headercolor">岗位编号</td>
							<td height='20' class="headercolor">岗位任职条件</td>
							<td height='20' class="headercolor">岗位编制</td>
						</tr>
						<pg:param name="jobName" />
						<pg:param name="jobDesc" />
						<pg:param name="jobId" />
						<!--检测当前页面是否有记录-->
						<pg:notify>
							<tr height='25' class="labeltable_middle_tr_01">
								<td colspan=100 align='center'>
									暂时没有分配岗位
								</td>
							</tr>
						</pg:notify>

						<!--list标签循环输出每条记录-->
						<pg:list>
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="getJobInfo(this,'<pg:cell colName="jobId" defaultValue=""/>')" onDBLclick="getJob()" >	      				
								<td class="tablecells" nowrap="true">
								<P align="left">
									<INPUT type="checkbox" name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="jobId" defaultValue=""/>' >
								</P>
								</td>
								<td height='20' align=left class="tablecells"><pg:cell colName="jobName" defaultValue="" /></td>
								<td height='20' align=left class="tablecells"><pg:cell colName="jobDesc" defaultValue="没有描述" /></td>
								<td height='20' align=left class="tablecells"><pg:cell colName="jobFunction" defaultValue="见岗位手册" /></td>
								<td height='20' align=left class="tablecells"><pg:cell colName="jobNumber" defaultValue="编号为空adsf" /></td>
								<td height='20' align=left class="tablecells"><pg:cell colName="jobCondition" defaultValue="见岗位手册" /></td>
								<td height='20' align=left class="tablecells"><pg:cell colName="jobAmount" defaultValue="" /></td>
							</tr>
						</pg:list>
						<tr height="30" class="labeltable_middle_tr_01">
							<td colspan=100 align='right'>
							 <pg:index /> 
<!--						  <input name="Submit5" type="submit" class="input" onClick="deletejob()" value="删除">-->
							  
							</td>
						</tr>
					</pg:pager>

			  </table>

	          <div align="center">                </div>
			</form>
		</div>
<%}else{ %>
<div align="center">没有权限！请与系统管理员联系</div>
<%} %>
	</body>
</html>
