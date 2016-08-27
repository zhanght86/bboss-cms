<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.security.AccessControl" %>
<%	
     String id =(String)request.getAttribute("uid");
     String username =(String)request.getAttribute("username");
     String orgId = request.getParameter("orgId");
     OrgManager orgManager = SecurityDatabase.getOrgManager();
	 Organization org = orgManager.getOrgById(orgId);	
     String orgName = org.getOrgName();
%>
<html>
<head>    
  <title>属性容器</title>
<script language="JavaScript" src="../scripts/ajax.js" type="text/javascript"></script>  
<SCRIPT LANGUAGE="JavaScript">
  
function addRole(flag){	
	if(document.all("allist").options.length<1){
   		alert("请先为机构设置岗位"); 
		return;
	}
	if(flag == 1){
	      var n=document.all("roleId").options.length-1;
	   	 	
	   for(var i=0;i<document.all("allist").options.length;i++){
	   var op=document.all("allist").options[i];
	   if(op.selected)
	   addone(op.text,op.value,n);
	  }
	   changebox();   
	  }
	  else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	}
  
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("roleId").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("roleId").add(op);
   
}
function deleteall(flag){
	var length=document.all("roleId").options.length;
	var i = 0;
	var orgId = <%=orgId%>;
	var roleDelId=new Array(length);
	if(flag == 1){
		for (var m=length-1;m>=0;m--)
		{
		      roleDelId[i++]=document.all("roleId").options[m].value;
	          document.all("roleId").options[m]=null;
	        } 
	      send_request('delUserOrgAlot.jsp?jobId='+roleDelId+'&id=<%=id%>&orgId='+orgId);    
	}
	 else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	}
}
      
function addall(flag){
	if(document.all("allist").options.length<1){
   		alert("请先为机构设置岗位"); 
   		return;
   	}
	if(flag == 1){
		var n=document.all("roleId").options.length-1;
		var p=document.all("allist").options.length-1;		  
	     for(var i=0;i<document.all("allist").options.length;i++){
	     var op=document.all("allist").options[i];
	     addone(op.text,op.value,n);  
	   }
	    changebox();    
    }
    else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	}
  
}
function deleterole(flag){
	
	var leng=document.all("roleId").options.length;
	var i = 0;
	var orgId = <%=orgId%>;
	var roleDelId=new Array();
	if(flag == 1){
		for (var m=leng-1;m>=0;m--){
		    if(document.all("roleId").options[m].selected)
		    {
		    roleDelId[i++]=document.all("roleId").options[m].value;
	        document.all("roleId").options[m]=null;
	        }
	 	}
	 	//alert("liumeiyu" + roleDelId[0]);
	 	send_request('delUserOrgAlot.jsp?jobId='+roleDelId+'&id=<%=id%>&orgId='+orgId);    
   	}
   	else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	}
}


function changebox(){				 
	var len=document.all("roleId").options.length;			  	 	
    var roleId=new Array(len);
    var orgId = <%=orgId%>;
    for (var i=0;i<len;i++){	      
        roleId[i]=document.all("roleId").options[i].value;
    }  
          		
	send_request('saveUserOrgAlot.jsp?jobId='+roleId+'&id=<%=id%>&orgId='+orgId);
}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form name="AlotUserRoleForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center" valign="top" nowrap>机构(<%=orgName%>)下可选岗位</td>
    <td width="20%" align="center" valign="top" >&nbsp;</td>
    <td width="40%" align="center" valign="top" >(<%=username%>)调入的岗位</td>
  </tr>
  
  <tr >
     <td  align="right" >
     <select name="allist"  multiple style="width:80%" 
    
                 onDBLclick="addRole(1)" 
           
     size="15">
     
				  <pg:list requestKey="allOrg">
					<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
				  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addRole(1)">              
            </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall(1)"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall(1)">  
           </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleterole(1)">
        </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
     <select name="roleId"  multiple style="width:80%" 
		onDBLclick="deleterole(1)"  size="15">
	 </select>
	 	 						
	</td>				 
	<tr>
        <td align="center">&nbsp;</td>
      </tr>
      		  
  <tr>
    <td align="center">&nbsp;</td>	
     <td align="center">&nbsp;</td>						
     <td>
		<div align="center">
<!--			<input name="button32" type="button" class="input" value="返回" onclick="javaScript:history.back()">-->
		</div>
	</td>							
  </tr>

 </table>
</form>
</center>
</div>
</body>
</html>

