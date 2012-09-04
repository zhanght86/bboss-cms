<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*" %>
<%	
	String currOrgId = (String)request.getParameter("orgId");
	if ( currOrgId == null )
	{
		currOrgId = (String)request.getAttribute("orgId");
	}
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org = orgManager.getOrgById(currOrgId);
	String orgName = org.getRemark5();
%>
<html>
<head>    
  <title>角色授予</title>
	<SCRIPT LANGUAGE="JavaScript">   
	function send_request(url){
	    document.all.divProcessing.style.display = "block";
		document.OrgJobForm.action = url;
		document.OrgJobForm.target="hiddenFrame";
		document.OrgJobForm.submit();
	}
	
	function isRecursive(){	    
		var ischeck = document.all.item("recursive").checked;
		if(ischeck)
			return "1";
		else
			return "0";	
	}
	function addone(name,value,n){
	   for(var i=n;i>=0;i--){
			if(value==document.all("roleId").options[i].value){
			  return;
			}
		}
	   var op=new Option(name,value);
	   document.all("roleId").add(op);
	   //send_request('<%=rootpath%>/sysmanager/orgmanager/storeorgrole.jsp?roleId='+value+'&orgId='+orgId+'&flag='+flag);
	}
	function addRole(){	
	    var n=document.all("roleId").options.length-1;
	    var roles = ""; 
	    var orgId = document.all("orgId").value;
		var flag = isRecursive();		
	   	for(var i=0;i<document.all("allist").options.length;i++){
		    var op=document.all("allist").options[i];
		    if(op.selected){
		        addone(op.text,op.value,n);
		        if(roles =="") roles = roles + op.value;
		        else roles = roles + "," + op.value;
		    }
		} 		
		send_request('<%=rootpath%>/sysmanager/orgmanager/storeorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);  
	}
	function addall(){		
	    var roles = "";
		var n=document.all("roleId").options.length-1;
		var p=document.all("allist").options.length-1;		  
	    for(var i=0;i<document.all("allist").options.length;i++){
	        var op=document.all("allist").options[i];
	        addone(op.text,op.value,n);  
	        if(roles =="") roles = roles + op.value;
		    else roles = roles + "," + op.value;
	    }
	    var orgId = document.all("orgId").value;
		var flag = isRecursive();
		send_request('<%=rootpath%>/sysmanager/orgmanager/storeorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);  
	}
	function deleterole(){
	    var roles = "";
		var orgId = document.all("orgId").value;
		var flag = isRecursive();
	    for (var m=document.all("roleId").options.length-1;m>=0;m--){
		    if(document.all("roleId").options[m].selected){
	      	    var op = document.all("roleId").options[m]
	      	    if(roles =="") roles = roles + op.value;
		        else roles = roles + "," + op.value;
		        document.all("roleId").options[m]=null;
	        }
	    }
	    send_request('<%=rootpath%>/sysmanager/orgmanager/deleteorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);
	 }
	function deleteall(){
		var roles = "";
		var orgId = document.all("orgId").value;
		var flag = isRecursive();
		for (var m=document.all("roleId").options.length-1;m>=0;m--){
		    var op = document.all("roleId").options[m]
      	    if(roles =="") roles = roles + op.value;
	        else roles = roles + "," + op.value;	    	
	    	document.all("roleId").options[m]=null
	    }
	    send_request('<%=rootpath%>/sysmanager/orgmanager/deleteorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);
	} 
	
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr>
  	<td width="40%" align="center">
		<input type="checkBox" name="recursive" id="recursive" >
		递归角色授予机构
	</td>
  </tr>
  <tr>
  <td></td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">可选角色</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">机构(<%=orgName%>)已授予角色</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right" >
     <select name="allist"  multiple style="width:100%" onDBLclick="addRole()" size="18" >
				  <pg:list requestKey="allRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addRole()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleterole()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
    <div style="overflow:auto;">
     <select name="roleId"  multiple style="width:100%" onDBLclick="deleterole()"size="18">
				  <pg:list requestKey="existRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	 </select>				
	</div>				
	</td>				 
				  
  </tr>
    
  
</table>
    <input type="hidden" name="orgId" value="<%=currOrgId%>"/>
</form>
</center>
</div>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:240px;top:450px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
	    <tr>
		    <td bgcolor=#3A6EA5>
			    <marquee align="middle" behavior="alternate" scrollamount="5">
				    <font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</div>
</body>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
