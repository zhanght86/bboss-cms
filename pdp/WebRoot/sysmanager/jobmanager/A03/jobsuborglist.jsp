<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.platform.sysmgrcore.manager.UserManager,java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String jobId=request.getParameter("jobId");
	JobManager jm = SecurityDatabase.getJobManager();
	Job job = jm.getJobById(jobId);
	String jobName = job.getJobName();
%>
<html>
<head>    
 <title>属性容器</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
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
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
	        	orgForm.action="<%=rootpath%>/jobmanager/job.do?method=deletesuborg";
				orgForm.submit();
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
</SCRIPT>
<!--<link rel="stylesheet" type="text/css" href=" ../../css/contentpage.css">-->
<!--<link rel="stylesheet" type="text/css" href=" ../../css/tab.winclassic.css">-->
<!--<%@ include file="/include/css.jsp"%>-->
<!--<link rel="stylesheet" type="text/css" href=" ../../sysmanager/css/contentpage.css">-->
<!--<link rel="stylesheet" type="text/css" href=" ../../sysmanager/css/tab.winclassic.css">-->
<!--<link href="../../sysmanager/css/windows.css" rel="stylesheet" type="text/css">-->
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">


<body class="contentbodymargin" scroll="no">

<div id="contentborder" align="center">
	
    <form name = "orgForm" method="post" action="">	
    <input type="hidden" name="jobId" value ="<%=request.getParameter("jobId")%>">
	<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">
	<tr><td height='40' class="detailtitle" align=center colspan=5><b>岗位(<%=jobName%>)隶属机构列表</b></td></tr>
		<pg:listdata dataInfo="JobSubList1" keyName="JobSubList1"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10"
				  scope="request"  
				  data="JobSubList1" 
				  isList="false">
			      <tr class="labeltable_middle_td">
			      <!--设置分页表头-->
			      <td class="headercolor" align=center>
				  	<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
				  </td>
				  <td height='20' class="headercolor">机构名称</td>
			   	  <td height='20' class="headercolor">机构编号</td>
				  <td height='20' class="headercolor">机构代码</td>
				  <td height='20' class="headercolor">机构描述</td>
				  			
			      </tr>
			      <pg:param name="jobId"/>
			     		      
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
							<td class="tablecells" align=left>
							<pg:null colName="remark5"><pg:cell colName="orgName"/></pg:null>
							<pg:notnull colName="remark5">
							<pg:equal colName="remark5" value=""><pg:cell colName="orgName"/></pg:equal>
							<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
							</pg:notnull>
							</td>
							<td class="tablecells" align=left><pg:cell colName="orgnumber" defaultValue=""/></td>
							<td class="tablecells" align=left><pg:cell colName="orgSn" defaultValue=""/></td>
							<td class="tablecells" align=left><pg:cell colName="orgdesc" defaultValue="没有描述"/></td>																											    					    							    				    		
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="detailcontent" colspan=4 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			      	<td class="detailcontent" align='left'>
			      	
					  	
					 <input type="button" value="删除" class="input"
								onclick="javascript:dealRecord(1); return false;">
								
				</td>
        </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>

</body>

</html>
