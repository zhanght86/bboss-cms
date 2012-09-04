<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,com.frameworkset.platform.security.AccessControl" %>
<%	
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkAccess(pageContext);
    String[] id =(String[])request.getAttribute("id");
  
    UserManager userManager = SecurityDatabase.getUserManager();
	 String usern = "";
     String userna = "";
     String userId = "";
     for(int i = 0;i < id.length; i++)
     {
     	
     	User user = userManager.getUserById(id[i]);
     	usern= user.getUserRealname();
     
     	userna += usern + ",";
     	userId +=id[i] + ",";
     }
    
     if(userna.length() > 1)
     userna = userna.substring(0,userna.length()-1);
	
	
%>
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
	    var roleId=new Array(len);
	    for (var i=0;i<len;i++){	      
	        roleId[i]=document.all("roleId").options[i].value;
	    }
	      
         document.forms[0].action="../member/memberManager.do?method=storeAllMemberRole&roleId="+roleId+"&id=<%=userId%>";
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
<form name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">

<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
 
  <tr>
  <td></td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">可选会员角色</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">给：<%=userna%>  授予的会员角色</td>
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
     <select name="roleId"  multiple style="width:80%" onDBLclick="deleterole()" size="18">
				  <pg:list requestKey="existRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	 </select>				
					
	</td>				 
				  
  </tr>
  <tr>
	  <td colspan="3" align="center">
	  <input name="add" type="button" class="input" value="确定" onclick="okadd()" >
	  <input name="Submit32" type="submit" class="input" value="返回" onClick="back()">
	  
	  </td> 
  </tr> 
  
</table>



</form>
</center>
</div>
</body>
</html>