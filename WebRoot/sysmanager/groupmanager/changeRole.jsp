<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<html>
<head>    
  <title>属性容器</title>
<SCRIPT LANGUAGE="JavaScript"> 
function addRole(){	
      var n=document.all("roleId").options.length-1;
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
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
function deleteall(){
	for (var m=document.all("roleId").options.length-1;m>=0;m--)
    document.all("roleId").options[m]=null
}
      
function addall(){
	var n=document.all("roleId").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
}
function deleterole(){
 for (var m=document.all("roleId").options.length-1;m>=0;m--)
	      if(document.all("roleId").options[m].selected)
         document.all("roleId").options[m]=null;
}
function okadd(){
var len=document.all("roleId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("roleId").options[i].selected="true";
         }
         document.forms[0].action="groupmanager.do?method=storeGroupRole";
         document.forms[0].submit();
}
</SCRIPT>

	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="sysmanager/css/tab.winclassic.css">

<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" class="td" align="center">可选角色</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" class="td" align="center">已授予角色</td>
  </tr>
  <tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
  <tr >
     <td align="center">
     <select name="allist"  multiple style="width:80%" onDBLclick="addRole()" size="18">
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
    <td align="center">
     <select name="roleId"  multiple style="width:80%" onDBLclick="deleterole()" size="18">
				  <pg:list requestKey="existRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	 </select>				
					
	</td>				 
				  
  </tr>
  <tr class="tabletop">						    
<td  align="center">&nbsp;</td>
						  </tr>
  <tr><td colspan="3" align="center">
  <input name="add" type="button" class="input" value="确定" onclick="okadd()" >
  
  </td> </tr>
</table>
<pg:beaninfo requestKey="groupForm">
<input type="hidden"  name="groupId" value="<pg:cell colName="groupId"  defaultValue=""/>"/>
</pg:beaninfo>
</form>
</center>
</div>
<%@ include file="../sysMsg.jsp"%>
</body>
</html>

