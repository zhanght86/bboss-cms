<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.RoleManager,
com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String roleId = (String)request.getAttribute("roleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role =roleManager.getRoleById(roleId);
	String rolename = role.getRoleName();
%>
<html>
<head>    
  <title>属性容器</title>
	<SCRIPT LANGUAGE="JavaScript">
	
	
	
	function addRole(){	
      var n=document.all("userId").options.length-1;
   	 	
	   for(var i=0;i<document.all("allist").options.length;i++){
	   var op=document.all("allist").options[i];
	   if(op.selected)
	   addone(op.text,op.value,n);
	  }
	}
	function addone(name,value,n){
	
	   for(var i=n;i>=0;i--){
			if(value==document.all("userId").options[i].value){
			  return;
			}
		}
	   var op=new Option(name,value);
	   document.all("userId").add(op);
	   
	}
	function deleteall(){
		for (var m=document.all("userId").options.length-1;m>=0;m--)
	    document.all("userId").options[m]=null
	}
	      
	function addall(){
		var n=document.all("userId").options.length-1;
		var p=document.all("allist").options.length-1;		  
	     for(var i=0;i<document.all("allist").options.length;i++){
	     var op=document.all("allist").options[i];
	     addone(op.text,op.value,n);  
	   }
	}
	function deleterole(){
	 for (var m=document.all("userId").options.length-1;m>=0;m--)
		      if(document.all("userId").options[m].selected)
	         document.all("userId").options[m]=null;
	}
	
	function okadd(){
		var len=document.all("userId").options.length;			  	 	
	    var userId=new Array(len);
	    for (var i=0;i<len;i++){	      
	        userId[i]=document.all("userId").options[i].value;
	    }
	      
         document.forms[0].action="../member/memberManager.do?method=roleGrantMember&userId="+userId+"&roleId=<%=roleId%>";
         document.forms[0].submit();
         alert("操作成功！");
	}
	function back(){
		document.forms[0].action="../member/memberManager.do?method=getMemberList&flag=2";
		document.forms[0].submit();
	}
	
	</SCRIPT>
 <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form target="rolemember" name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<input type="hidden" name="roleId" value="<%=roleId%>" />
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
 
  <tr>
  <td></td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">可选会员</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">角色(<%=rolename%>)已授予的会员</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right" >
     <select name="allist"  multiple style="width:80%" onDBLclick="addRole()" size="18">
				  <pg:list requestKey="allRole">
					<option value="<pg:cell colName="userId"/>">
					<pg:cell colName="userRealname"/>
					(<pg:cell colName="userName"/>)
					</option>
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
     <select name="userId"  multiple style="width:80%" onDBLclick="deleterole()" size="18">
				  <pg:list requestKey="existRole">
					<option value="<pg:cell colName="userId"/>">
					<pg:cell colName="userRealname"/>
					(<pg:cell colName="userName"/>)
					</option>
				  </pg:list>			
	 </select>				
					
	</td>				 
				  
  </tr>
  <tr>
	  <td colspan="3" align="center">
	  <input name="add" type="button" class="input" value="确定" onclick="okadd()" >
	  </td> 
  </tr> 
  
</table>
</form>
<iframe name="rolemember" width="0" height="0" style="display:none"></iframe>
</center>
</div>
</body>
</html>