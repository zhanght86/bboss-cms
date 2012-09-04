<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%
	session.setAttribute("roleTabId", "4");
	String roleId = request.getParameter("roleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	String roleName = role.getRoleName();
	
%>
<html>
<head>    
  <title>属性容器</title>
   <%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
	<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
<SCRIPT LANGUAGE="JavaScript"> 
function addGroup(){	
   var n=document.all("groupId").options.length-1;
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
  //changebox();
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("groupId").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("groupId").add(op);
   
   send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleGroup.jsp?roleId='+<%=roleId%>+'&groupId='+value+'&tag=add');
}
function deleteall(){
	for (var m=document.all("groupId").options.length-1;m>=0;m--)
	{
		send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleGroup.jsp?roleId='+<%=roleId%>+'&groupId='+document.all("groupId").options[m].value+'&tag=delete');
		document.all("groupId").options[m]=null;
	}
    
    //changebox();
}
      
function addall(){
	var n=document.all("groupId").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
   //changebox();
}
function deleteGroup(){
 for (var m=document.all("groupId").options.length-1;m>=0;m--)
	      if(document.all("groupId").options[m].selected)
	      {
	      		send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleGroup.jsp?roleId='+<%=roleId%>+'&groupId='+document.all("groupId").options[m].value+'&tag=delete');
		   		document.all("groupId").options[m]=null;
	      }
        
         //changebox();
}
function okadd(){
var len=document.all("groupId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("groupId").options[i].selected="true";
         }
         document.forms[0].action="../accessmanager/roleManager.do?method=storeRoleGroup";
         document.forms[0].submit();
}
//---------------------------------------
var http_request = false;
//function send_request(url){
//			http_request = false;
//				//开始初始化XMLHttpRequest对象
//				if(window.XMLHttpRequest){//Mozilla
//					http_request = new XMLHttpRequest();
//					if(http_request.overrideMimeType){//设置MIME类别
//						http_request.overrideMimeType("text/xml");						
//					}
//				}
//				else if(window.ActiveXObject){//IE
//					try{
//						http_request = new ActiveXObject("Msxml2.XMLHTTP");
//					}catch(e){
//						try{
//							http_request = new ActiveXObject("Microsoft.XMLHTTP");							
//						}catch(e){
//						}
//					}
//				}
//				if(!http_request){
//					alert("不能创建XMLHttpRequest对象");
//					return false;
//				}
//				http_request.onreadystatechange = processRequest;
//				http_request.open("GET",url,true);
//				http_request.send(null);
//			}

			
			function send_request(url){
				document.all.divProcessing.style.display = "block";
				document.RoleGroupForm.action = url;
				document.RoleGroupForm.target = "hiddenFrame";
				document.RoleGroupForm.submit();
			}
			
			function processRequest(){
				if(http_request.readyState == 4){
					//alert(http_request.status);
					if(http_request.status == 200){
						//alert(http_request.responseText);
					}
					else{
						alert("对不起，服务器错误");
					}
				}
			}
			
			function changebox(){				 
			   	 var len=document.all("groupId").options.length;			  	 	
        		 //var roleId = document.all("roleId").value;
        		 var roleId="<%=roleId%>";
        		 //alert(roleId);
        		 var groupId=new Array(len)
        		 for (var i=0;i<len;i++){	      
        		   groupId[i]=document.all("groupId").options[i].value;
         		 }   
         		
				 send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleGroup.jsp?roleId='+roleId+'&groupId='+groupId);
			}

</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form name="RoleGroupForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">可选组</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">角色(<%=roleName%>)已授予的组</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right" >
     <select name="allist"  multiple style="width:80%" onDBLclick="addGroup()" size="18">
				  <pg:list requestKey="allGroup">
					<option value="<pg:cell colName="groupId"/>"><pg:cell colName="groupName"/></option>
				  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addGroup()"></td>
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
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleteGroup()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
     <select name="groupId"  multiple style="width:80%" onDBLclick="deleteGroup()" size="18">
				  <pg:list requestKey="existGroup">
					<option value="<pg:cell colName="groupId"/>"><pg:cell colName="groupName"/></option>
				  </pg:list>			
	 </select>				
					
	</td>				 
				  
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr><td colspan="3" align="center">
  
  
  </td> </tr>
</table>
</form>
</center>
</div>

<div id=divProcessing style="width:200px;height:30px;position:absolute;left:150;top:400px;display:none">
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
