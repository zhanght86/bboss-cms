<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="java.util.List"%>
<%
	String orgId=request.getParameter("orgId");
	String jobId=request.getParameter("jobId");
	String userId=request.getParameter("userId");
	Organization org=new Organization();
	org.setOrgId(orgId);
	UserManager userManager = SecurityDatabase.getUserManager();
			List userList = userManager.getUserList(org);
			if(userList == null || userList.size()<1){
				
			}
			request.setAttribute("userList", userList);
%>

<html>
  <head>
   <title></title>
   <SCRIPT LANGUAGE="JavaScript"> 
  	function adduser(){
		  	var curuserId1;
		  	var len = document.all("userList").options.length;
		  	if(len < 1){
		  		alert("没有选择用户！！！");
		  		return;
		  	}
 			for (var s=document.all("userList").options.length-1;s>=0;s--)
	      	if(document.all("userList").options[s].selected){
		        curuserId1 = document.all("userList").options[s].value;
		        //alert(curuserId1);
	        }
		document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=adduser&orgId=<%=orgId%>&jobId=<%=jobId%>&userId="+curuserId1;
		document.forms[0].submit();	         
	}
	
	
	function showUserList(){//用户ID
		var curuserId;
 			for (var s=document.all("userList").options.length-1;s>=0;s--)
	      	if(document.all("userList").options[s].selected){
	        curuserId = document.all("userList").options[s].value;
	        //alert(curuserId);
	        }
	    
		//document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=getUserList&curjobid="+curjobId;
		//document.forms[0].submit();	         
	}
	function returnList(){
		
	    
		document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=showOrgallJob&orgId=<%=orgId%>";
		document.forms[0].submit();	         
	}
   </script>
  </head>
  
  <body>
    <div align="center"></div>
    <form name="OrgJobForm" action="" method="post" >
    <table width="343" height="340" border="0" align="center">
      <tr>
        <td height="15"><div align="center">可选用户</div></td>
      </tr>
      <tr>
        <td height="270">
          <div align="center">
            <select name="userList" multiple style="width:30%;height:100%" size="18">
              <pg:list requestKey="userList">
              <option value="<pg:cell colName="userId"/>                  
              "> <pg:cell colName="userName" /></pg:list>
            </select>
        </div></td>
      </tr>
      <tr>
        <td height="47">
          <div align="right">
            <input name="Submit" type="button" class="input" value="返回" onclick=" returnList()">
            <input name="Submit" type="button" class="input" onClick="adduser()" value="保存">
          </div></td></tr>
  </table> 
  </form>
  <br>
  </body>
</html>

