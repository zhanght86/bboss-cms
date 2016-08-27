
<%@ include file="../include/global1.jsp"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%> <%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %> <%@ page import="java.util.List" %>
<%
	String orgid = request.getParameter("orgId");
	String orgName = request.getParameter("orgName");
	String uid = (String)session.getAttribute("currUserId");
	String show = (String)request.getAttribute("flag");
	if(show != null){
		if (show.equals("1")){
			orgName = "";
		}
	}
	
	Job noJob = jobManager.getJobById("1");
	String jobName = "";
	String jobid = "";
	if(noJob != null){
		jobid = noJob.getJobId();
		jobName = noJob.getJobName();
	}
	
	String sjobid = (String)session.getAttribute("jobid");
	if (orgid == null){
		orgid = "";
		}
	if(orgName == null){
			orgName = "";
			}
	
%>
<html>

<head>
<title>属性容器</title>
<script language="JavaScript"> 
var oid = parent.frame1.oid;
var uid = parent.frame1.uid;



function addorg(){	
   var n=document.all("orgId").options.length-1;   
   var key1 = document.all("orgName").value;
   var key2 = document.all("id").value;
   
   for(var i=n;i>=0;i--){
   var str = document.all("orgId").options[i].value;
   		
   		var kjobid = key2 + ";"+document.all("sjobid").value;   		
		if(str==kjobid){
			alert("岗位相同");	
		  return;
		}
	}
   
   document.forms[0].action="../user/userOrg.do?method=storeUserOrg&oid="+key2+"&uid=<%=uid%>";
   document.forms[0].submit();
}

function addleftorg(){
	
	var n=document.all("orgId").options.length-1;
	var name = "<%=orgName%>";
	var name = name + "(<%=jobName%>)";
	var value = "<%=orgid%>"+";"+"<%=sjobid%>";
	for(var i=n;i>=0;i--){
		if(value==document.all("orgId").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("orgId").add(op);
}


function addone(name,value,n){

   for(var i=n;i>=0;i--){
   var str = document.all("orgId").options[i].value;
   		//var strs = str.split(";"); 
   		var sjob = strs + document.all("sjobid").value;
   		alert(sjob);
		if(value==strs[0]){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("orgId").add(op);
   
}
function deleteall(){
	for (var m=document.all("orgId").options.length-1;m>=0;m--)
    document.all("orgId").options[m]=null
}
      

function deleteorg(){
 for (var m=document.all("orgId").options.length-1;m>=0;m--)
	     if(document.all("orgId").options[m].selected){
         	var ooid = document.all("orgId").options[m].value;
         	document.forms[0].action="../user/userOrg.do?method=deleteUserOrg&ooid="+ooid+"&uid=<%=uid%>";
         	document.forms[0].submit();
         
         }
   
}
function okadd(){
var len=document.all("orgId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("orgId").options[i].selected="true";
         }
         document.forms[0].action="../user/userOrg.do?method=storeUserOrg&oid=<%=orgid%>&uid=<%=uid%>";
         document.forms[0].submit();
}

function okdelete(){
var len=document.all("orgId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("orgId").options[i].selected="true";
         }
    if(oid.length > 1){
         document.forms[0].action="../user/userOrg.do?method=deleteUserOrg&oid="+oid+"&uid=<%=uid%>" ;
    }
    else{
    	document.forms[0].action="../user/userOrg.do?method=deleteUserOrg&oid=<%=orgid%>&uid=<%=uid%>" ;
    }
         document.forms[0].submit();
}
</script>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
</head>

<body class="contentbodymargin" >

<div id="contentborder">
	<center>
	<form name="OrgJobForm" action method="post">
		<table width="31%" border="0" cellpadding="0" cellspacing="1">
			<tr class="tabletop">
				<td width="72%" align="center">已授予机构</td>
			</tr>
			<tr class="tabletop">
				<td width="100%" align="center">
				<input name="orgName" class="input" value="<%=orgName%>" size="59">
				<input name="id" type="hidden" class="input" value="<%=orgid%>">
				<input name="sjobid" type="hidden" class="input" value="<%=sjobid%>">
				</td>
			</tr>
			<tr>
				<td>
				<p align="right">
				<select name="orgId" multiple style="width:100%" ondblclick="deleteorg()" size="18">
				  <pg:list sessionKey="existOrgSession" needClear="false">
					<option value="<pg:cell colName="orgId"/>"><pg:cell colName="orgName"/>
				</option>
				  </pg:list>			
	 			</select><input name="add" type="button" class="input" value="添加" onclick="addorg()"><input name="add1" type="button" class="input" value="删除" onclick="deleteorg()">
				</p>
				</td>
			</tr>
			<tr>
				<td colspan="3" align="center">　</td>
			</tr>
		</table>
	</form>
	</center></div>
<%@ include file="../sysMsg.jsp"%>
</body>

</html>

