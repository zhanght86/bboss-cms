<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>

<%
	String resId=request.getParameter("resId");
	session.setAttribute("resId",resId);
	String[] tmp = resId.split(":");
	String orgId = tmp[0]; //主管处室
	String jobId = tmp[1]; //主管岗位
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org =orgManager.getOrgById(orgId);
	JobManager jobManager = SecurityDatabase.getJobManager();
	Job job =jobManager.getJobById(jobId);
		
%>
<html>
<head>    
 <title>属性容器</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">
	function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
		var cbs = true;
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(cbs){
			selectAll[0].checked=true;
		}else{
			selectAll[0].checked=false;
		}
	}		
	function dealRecord(dealType) {
	    var isSelect = false;
	    var outMsg;
	    
	    for (var i=0;i<orgForm.elements.length;i++) {
			var e = orgForm.elements[i];
			
			if (e.name == 'ID'){
				if (e.checked){
			       		isSelect=true;
			       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要删除预算单位吗？";
	        	if (confirm(outMsg)){
	        	orgForm.action="<%=rootpath%>/orgmanager/org.do?method=deletecharge&resId=<%=resId%>";
				orgForm.submit();
				alert("预算单位删除成功！");
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
	function setup()
	{	
		orgForm.action="<%=request.getContextPath()%>/sysmanager/orgmanager/budgetOrg.jsp?resId=<%=resId%>"
		orgForm.submit();	
	}
	
	
</SCRIPT>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
<body class="contentbodymargin" scroll="no">

<div id="contentborder" align="center">
	
    <form name = "orgForm" method="post" action="">	
    <input type="hidden" name="jobId" value ="<%=request.getParameter("jobId")%>">
	<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">
	<tr><td height='40' class="detailtitle" align=center colspan=5><b>机构:  <%=org.getOrgName()%>（<%=job.getJobName()%>）主管的预算单位列表</b></td></tr>
		
	
		<pg:listdata dataInfo="BudgetOrgList" keyName="BudgetOrgList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10"
				  scope="request"  
				  data="BudgetOrgList" 
				  isList="false">
			      <tr class="labeltable_middle_td">
			      <!--设置分页表头-->
			      <td class="tablecells" align=center>
				  	<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
				  </td>
				  <td height='20' class="headercolor">预算单位名称</td>
			   	  <td height='20' class="headercolor">预算单位编号</td>
			   	  <td height='20' class="headercolor">预算单位显示名称</td>
			      </tr>
			      <pg:param name="resId"/>
			      <pg:param name="orgname"/>
			      <pg:param name="orgnumber"/>	      
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td class="detailcontent" colspan=100 align='center'>暂时没有机构</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      		<tr class="labeltable_middle_tr_01" onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	      				
							
			      		    <td class="tablecells" align=center><input onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="orgId" defaultValue=""/>"></td>					
							<td class="tablecells" align=center>
							<pg:null colName="remark5"><pg:cell colName="orgName"/></pg:null>
							<pg:notnull colName="remark5">
							<pg:equal colName="remark5" value=""><pg:cell colName="orgName"/></pg:equal>
							<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
							</pg:notnull>
							</td>
							<td class="tablecells" align=center><pg:cell colName="orgnumber" defaultValue=""/></td>
							<td class="tablecells" align=center><pg:cell colName="remark5" defaultValue="没有显示名称"/></td>
						</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="detailcontent" colspan=3 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			      	
			      	<td class="detailcontent" align='left'>		      	
					  	
					 <input type="button" value="删除" class="input"
								onclick="javascript:dealRecord(1); return false;">
					<input type="button" value="设置" class="input"
								onclick="setup()">								
				</td>
        </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>

</body>

</html>
