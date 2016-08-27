<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%	
	String roleId = (String)request.getParameter("roleId");
	if ( roleId == null )
	{
		roleId = (String)request.getAttribute("roleId");
	}

%>
<html>
<head>    
  <title>属性容器</title>
<SCRIPT LANGUAGE="JavaScript"> 
function addOrg(){	
      var n=document.all("orgId").options.length-1;
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("orgId").options[i].value){
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
      
function addall(){
	var n=document.all("orgId").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
}
function deleteorg(){
 for (var m=document.all("orgId").options.length-1;m>=0;m--)
	      if(document.all("orgId").options[m].selected)
         document.all("orgId").options[m]=null;
}
function okadd(){
	var len=document.all("orgId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("orgId").options[i].selected="true";
         }
         
         document.forms[0].action="../user/userManager.do?method=storeRoleOrg";
         document.forms[0].submit();
}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="RoleOrgForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">可选机构</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">已授予机构</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right">
     <select name="allist"  multiple style="width:80%" onDBLclick="addOrg()" size="18">
		  <pg:list requestKey="allOrg">
			<option value="<pg:cell colName="orgId"/>"><pg:cell colName="orgName"/></option>
		  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addOrg()"></td>
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
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleteorg()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
     <select name="orgId"  multiple style="width:80%" onDBLclick="deleteorg()" size="18">
				  <pg:list requestKey="existOrg">
					<option value="<pg:cell colName="orgId"/>"><pg:cell colName="orgName"/></option>
				  </pg:list>			
	 </select>				
					
	</td>				 
				  
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    
  </tr>
  <tr><td colspan="3" align="center">
  <input name="add" type="button" class="input" value="确定" onclick="okadd()" >
  
  </td> </tr>
</table>

<input type="hidden" name="roleId" value="<%=roleId%>"/>

</form>
</center>
</div>
<%@ include file="../../sysMsg.jsp"%>
</body>
</html>
