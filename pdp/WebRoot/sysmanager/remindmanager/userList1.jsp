<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>

<%			
			String displayNameInput = request.getParameter("displayNameInput");
			String displayValueInput = request.getParameter("displayValueInput");
			String mutiple = request.getParameter("mutiple") ;
			if(mutiple == null)
				mutiple = "false";
			
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
	<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		<SCRIPT LANGUAGE="JavaScript"> 
function addRole(){	
 	var selected = false;  
   for(var i=0;i < document.all("allist").options.length;i++)
   {
	   var op=document.all("allist").options[i];
	   
	   if(op.selected)
	   {
	   		addone("userIds",op.text,op.value);
	   		if(!selected)
	   			selected = true;
	    }   		    
   }
   if(!selected)
   		alert("请选择人");
}


function addone(inputname,name,value){
	
   for(var i= document.all(inputname).options.length - 1;i>=0;i--){
		if(value == document.all(inputname).options[i].value){
		  return;
		}
	}
	
   var op=new Option(name,value);
   
   op.setAttribute("optionname",name);
   document.all(inputname).add(op);
   
}


function deleteall(inputselect){
	for (var m=document.all(inputselect).options.length-1;m>=0;m--)
    document.all(inputselect).options[m]=null
}
      
function addall(){

	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone("userIds",op.text,op.value);  
   }
}
function deleterole(){
 var selected = false;
 for (var m=document.all("userIds").options.length-1;m>=0;m--)
 {
      if(document.all("userIds").options[m].selected)
      {
     		document.all("userIds").options[m]=null;
     		if(!selected)
     			selected = true;
      }
 }
 if(!selected)
   		alert("请选择要删除的人");
}
function okadd(){	
	var len=document.all("allist").options.length;	
	
	var mutiple = "<%=mutiple%>";
	var ids = "";
	var names = "";
	var flag = false;
	for (var i=0;i<len;i++){	      
         if(mutiple == "true")
         {
           	 if(document.all("allist").options[i].selected)
		     	 if(flag)
		     	 {
			         ids += "," + document.all("allist").options[i].value;
			         names += "," + document.all("allist").options[i].optionname;
				 }
				 else
				 {
					 ids += document.all("allist").options[i].value;
			         names += document.all("allist").options[i].optionname;
			         flag = true;
				 }
		 }
		 else
		 {
		 	if(document.all("allist").options[i].selected)
		 	{
					 ids += document.all("allist").options[i].value;
			         names += document.all("allist").options[i].optionname;
			         flag = true;
			         break;
			}
		 }
        
     }
         
		if(flag)	
		{
	         parent.window.opener.document.all.item("<%=displayNameInput%>").value = names;
	         parent.window.opener.document.all.item("<%=displayValueInput%>").value = ids;
	     }
	     else
	     {
	     	parent.window.opener.document.all.item("<%=displayNameInput%>").value = "";
	         parent.window.opener.document.all.item("<%=displayValueInput%>").value = "";
	     }
         parent.window.close();

         
}
function init()
{
	
         
	var displayNameInput = parent.window.opener.document.all.item("<%=displayNameInput%>").value ;

	
	var displayValueInput = parent.window.opener.document.all.item("<%=displayValueInput%>").value ;

	if(!displayNameInput || !displayValueInput)
	{
		return ;
	}

			var userNameArray = displayNameInput.split(",");		
			
			var userIdArray = displayValueInput.split(",");		
			
			deleteall("allist");
			for(var i = 0; i < userNameArray.length; i ++ )
			{
				
				addone("allist",userNameArray[i],userIdArray[i]);				
			}
	
}




</SCRIPT>
	<body class="contentbodymargin" scroll="no" onload="init();" oncontextmenu="return true;">
		<div id="contentborder">

			<form name="OrgJobForm" action="" method="post">

				<div align="center">
					<table width="80%" border="0" cellpadding="0" cellspacing="1" class="table">
						
						<tr class="tr">
							<td class="td" width="40%" align="center"><P>
								 可选人员
							</P><P></P></td>
							
							
						</tr>
						<tr class="tabletop">
							<td width="40%" align="center">
								 &nbsp;
							</td>
							
						</tr>
						<tr class="tr">
							<td class="td" align="center">
								<P align="center">
									<SELECT class="select" name="allist" id="allist" <%=mutiple.equals("true")?"multiple":""%> style="WIDTH:60%" ondblclick="addRole()" size="18">
										<pg:list requestKey="allUser" needClear="false">
											<OPTION value='<pg:cell colName="userId"/>'>
												<pg:cell colName="userRealname" />
												(
												<pg:cell colName="userName" />
												)
											</OPTION>
										</pg:list>
									</SELECT>
								</P>
							</td>							
							
						</tr>
						<tr class="tabletop">
							<td align="center">
								 &nbsp;
							</td>
						</tr>
						<tr class="tr">
							<td  class="td" align="center">
								<input name="add" type="button" class="input" value="确定" onclick="okadd()">
								<input name="add" type="button" class="input" value="放弃" onclick="parent.window.close();">

							</td>
						</tr>
					</table>
				</div>
				<div align="center"></div>
				<pg:beaninfo requestKey="userRoleForm">

					<input type="hidden" name="uid" value="<pg:cell colName="userId"  defaultValue=""/>" />
				</pg:beaninfo>
			</form>

		</div>
	</body>
</html>

