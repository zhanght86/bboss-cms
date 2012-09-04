<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager
				,com.frameworkset.platform.sysmgrcore.manager.OrgManager
				,com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,java.util.List,java.util.ArrayList" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    
	String roleId = (String)request.getParameter("roleId");
	if ( roleId == null )
	{
		roleId = (String) session.getAttribute("currRoleId");
	}	

	String orgId = request.getParameter("orgId");
		
	if (orgId == null)
		orgId = "0";
	OrgManager orgManager = SecurityDatabase.getOrgManager();


	List existOrg = orgManager.getOrgListOfRole(roleId);// 该角色关联机构
	List allOrg = null;
	List allOrgAfterCheck = new ArrayList();
	List existOrgAfterCheck = new ArrayList();

	allOrg = orgManager.getOrgListBySql("select * from (select o.*,1 as com_level from td_sm_Organization o where o.org_Id='" + orgId +"'"  
				+ " union select o.*,2 as com_level from td_sm_Organization o where " +
				" o.parent_Id='" + orgId + "') dd order by com_level asc,org_sn asc");
	
	//增加权限判断，如果用户无权对机构设置角色，就不显示此机构
	if(allOrg != null)
	{
		for(int k=0;k<allOrg.size();k++)
		{
			Organization organization = (Organization)allOrg.get(k);
			if(organization != null)
			{
				if(accesscontroler.checkPermission(organization.getOrgId(), "orgroleset", AccessControl.ORGUNIT_RESOURCE))
				{
					allOrgAfterCheck.add(organization);
				}
			}
		}
	}
	if(existOrg != null)
	{
		for(int k=0;k<existOrg.size();k++)
		{
			Organization organization = (Organization)existOrg.get(k);
			if(organization != null)
			{
				if(accesscontroler.checkPermission(organization.getOrgId(), "orgroleset", AccessControl.ORGUNIT_RESOURCE))
				{
					existOrgAfterCheck.add(organization);
				}
			}
		}
	}
	request.setAttribute("existOrg", existOrgAfterCheck);
	request.setAttribute("allOrg", allOrgAfterCheck);

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
function addOrg(){	
      var n=document.all("orgId").options.length-1;
   	 	

  var orgIds = "";
  for(var i=0;i<document.all("allist").options.length;i++){
    var op=document.all("allist").options[i];
    if(op.selected){
        addone(op.text,op.value,n);
        if(orgIds =="") orgIds = orgIds + op.value;
        else orgIds = orgIds + "," + op.value;
    }
  }
  
   if(orgIds != "")
    {
    	send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=add');
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
 	var orgIds = "";
	for (var m=document.all("orgId").options.length-1;m>=0;m--)
	{
		if(orgIds =="") orgIds = orgIds + document.all("orgId").options[m].value;
        else orgIds = orgIds + "," + document.all("orgId").options[m].value;
		
   		document.all("orgId").options[m]=null
	}    
	 if(orgIds != "")
    {
    	send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=delete');
    }
  
}
      
function addall(){
	var n=document.all("orgId").options.length-1;
	var p=document.all("allist").options.length-1;	
	
	var orgIds = "";
			  
    for(var i=0;i<document.all("allist").options.length;i++){
        var op=document.all("allist").options[i];
        addone(op.text,op.value,n);  
        if(orgIds =="") orgIds = orgIds + op.value;
	    else orgIds = orgIds + "," + op.value;
    }
    if(orgIds != "")
    {
    	send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=add');
    }	  
    
   
}
function deleteorg(){
 
 	var orgIds = "";
		
    for (var m=document.all("orgId").options.length-1;m>=0;m--){
	    if(document.all("orgId").options[m].selected){
      	    var op = document.all("orgId").options[m]
      	    if(orgIds =="") orgIds = orgIds + op.value;
	        else orgIds = orgIds + "," + op.value;
	        document.all("orgId").options[m]=null;
        }
    }
	if(orgIds != "")
    {
    	send_request('<%=rootpath%>/sysmanager/accessmanager/role/saveRoleOrg.jsp?roleId='+<%=roleId%>+'&orgId='+orgIds+'&tag=delete');
    }	
}
function okadd(){
	var len=document.all("orgId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("orgId").options[i].selected="true";
         }
         
         document.forms[0].action="../accessmanager/roleorg.do?method=storeRoleOrg";
         document.forms[0].submit();
}
//---------------------------------------
var http_request = false;
//function send_request(url){
//				http_request = false;
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
				document.RoleOrgForm.action = url;
				document.RoleOrgForm.target = "hiddenFrame";
				document.RoleOrgForm.submit();
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
			   	 var len=document.all("orgId").options.length;			  	 	
        		 //var roleId = document.all("roleId").value;
        		 var roleId="<%=roleId%>";
        		 //alert(roleId);
        		 var orgId=new Array(len)
        		 for (var i=0;i<len;i++){	      
        		   orgId[i]=document.all("orgId").options[i].value;
         		 }   
         		
				 send_request('<%=rootpath%>/sysmanager/accessmanager/role/savaRoleOrg.jsp?roleId='+roleId+'&orgId='+orgId);
			}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="RoleOrgForm" action="" method="post" >
<table width="100%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="45%" align="center">&nbsp;</td>
    <td width="10%" align="center">&nbsp;</td>
    <td width="45%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="45%" align="center">可选机构</td>
    <td width="10%" align="center">&nbsp;</td>
    <td width="45%" align="center">已授予机构</td>
  </tr>
  <tr class="tabletop">
    <td width="45%" align="center">&nbsp;</td>
    <td width="10%" align="center">&nbsp;</td>
    <td width="45%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right">
     <select name="allist"  multiple style="width:100%" onDBLclick="addOrg()" size="18">
		  <pg:list requestKey="allOrg">
			<option value="<pg:cell colName="orgId"/>">
			<pg:null colName="remark5"><pg:cell colName="orgName"/></pg:null>
			<pg:notnull colName="orgName">
				<pg:equal colName="remark5" value=""><pg:cell colName="orgName"/></pg:equal>
				<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
			</pg:notnull>
			</option>
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
     <select name="orgId"  multiple style="width:100%" onDBLclick="deleteorg()" size="18">
				  <pg:list requestKey="existOrg">
					
					<option value="<pg:cell colName="orgId"/>">
					<pg:null colName="remark5"><pg:cell colName="orgName"/></pg:null>
					<pg:notnull colName="remark5">
					<pg:equal colName="remark5" value=""><pg:cell colName="remark5"/></pg:equal>
					<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
					</pg:notnull>
					</option>
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

<input type="hidden" name="roleId" value="<%=roleId%>"/>

</form>
</center>
</div>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:150px;top:450px;display:none">
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
