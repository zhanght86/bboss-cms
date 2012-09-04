
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %> 
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager" %>
<%	
	String uid = request.getParameter("uid");
	String logid = request.getParameter("logid");
	String orgId1 = request.getParameter("orgId");
	
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org = OrgCacheManager.getInstance().getOrganization(orgId1);

	UserManager userManager = SecurityDatabase.getUserManager();
	User user = null;
	String realname = "";
	String pwd = "";
	String logname = "";
	if(uid!=null && uid.length()>0){
	    try{		    		    
		    user = userManager.getUserById(uid);		    
		    realname = user.getUserRealname();
			pwd = user.getUserPassword();
		    logname = user.getUserName();	    	        
	        //AccessControl.getInstance().login(request, response,logname, pwd);
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	

	String currentOrgId = request.getParameter("currentOrgId");
	String mainOrgId = request.getParameter("mainOrgId");
	if(currentOrgId == null){
		currentOrgId = "";
	}
	if(mainOrgId == null){
		mainOrgId = "";
	}
	
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
function addOrg(){
	//添加单个岗位
	if(document.all("allist").options.length<1){
		alert("没有岗位可以选择");
	}
	var jobId = "";
  	var n=document.all("jobId").options.length-1;  	
  	var count = 0;
  	for(var j=0;j<document.all("allist").options.length;j++){
   	var opp=document.all("allist").options[j];
	   if(opp.selected){
	   		count++;
	   }
	}
	if(count==0)
  	{
  		alert("请选择一个要添加的岗位!");
  		return;
  	}//每次添加必需要选择一个岗位,weida
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
	   var op=document.all("allist").options[i];
   
	   if(op.selected){
		   jobId = op.value;
		   var success = addone(op.text,op.value,n);
		   if(!success)
		   	return;
	   }	
  	}
  	 
     var uid="<%=uid%>";
	 var orgId1="<%=orgId1%>";
	 var flag = "1";		

	 RoleOrgForm.action = "<%=request.getContextPath()%>/sysmanager/user/savaUJO.jsp?uid="
	 							+ uid +
	 							"&jobId="
	 							+jobId+
	 							"&orgId1="
	 							+orgId1+
	 							"&flag="
	 							+flag;
	 RoleOrgForm.target = "orgjobchange";
	 RoleOrgForm.submit();
	 divProcessing.style.display = "";
	 
//	 send_request('<%=request.getContextPath()%>/sysmanager/user/savaUJO.jsp?uid='+uid+'&jobId='+jobId+'&orgId1='+orgId1+'&flag='+flag);
  
  //changebox();
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("jobId").options[i].value){
			alert("岗位" + name + "已经设置了");
		  return false;
		}
	}
   var op=new Option(name,value);
   document.all("jobId").add(op);
   return true;
   
}
function deleteall(){
	
	var thisOrgId = <%=orgId1%>;
	//weida,阻止全部调出岗位
	alert("要调出全部岗位,请直接在列表中删除用户!");
	return;

	//删除所有岗位
	var jobId = "";
	var flag1 = false;
	var len=document.all("jobId").options.length;
	 if(len == 0)
	 {
		alert("没有可删除的岗位！");
		return;
	 }	
	for (var m=document.all("jobId").options.length-1;m>=0;m--){
		var option = document.all("jobId").options[m];
		if(flag1)
		{
			
			jobId += "," + option.value;
		}
		else
		{
			jobId += option.value;
			flag1 = true;
		}
		
		document.all("jobId").options[m]=null
	}
    if(window.confirm("你确定要删除用户在机构下的所有岗位吗？（用户将调离）"))
	 {	
		 var uid="<%=uid%>";
		 var orgId1="<%=orgId1%>";
		 var flag = "0";		
	 	 send_request('<%=request.getContextPath()%>/sysmanager/user/savaUJO.jsp?uid='+uid+'&jobId='+jobId+'&orgId1='+orgId1+'&flag='+flag);
	}
    //changebox();
}
      
function addall(){
	//添加所有岗位
	if(document.all("allist").options.length<1){
		alert("没有岗位可以选择");
	}
	var jobId = "";
    var flag1 = false;	
	
	var n=document.all("jobId").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     if(flag1)
		{
			
			jobId += "," + op.value;
		}
		else
		{
			jobId += op.value;
			flag1 = true;
		}
     
     addone(op.text,op.value,n);  
   }
   	 var uid="<%=uid%>";
	 var orgId1="<%=orgId1%>";
	 var flag = "1";		
	 send_request('<%=request.getContextPath()%>/sysmanager/user/savaUJO.jsp?uid='+uid+'&jobId='+jobId+'&orgId1='+orgId1+'&flag='+flag);
   
   //changebox();
}
function deleteorg(){
	
	//必须至少保留一个岗位,weida
	//var len=document.all("jobId").options.length;
	//if(len == 1)
	//{
	//	alert("必须至少保留一个岗位！");
	//	return;
	//}	

	//删除单个岗位
	if(document.all("jobId").options.length<1){
	
		alert("没有可删除的岗位！");
		return;
	}
	var uid="<%=uid%>";
	var orgId1="<%=orgId1%>";
	var flag = "0";
	
		var count =0;	
		for(var i=document.all("jobId").options.length-1;i>=0;i--)	
		{
			var option = document.all("jobId").options[i];
	      	if(!(document.all("jobId").options[i].selected))
	      	{
				count++;
	      	}
		}	
		if(orgId1 == <%=currentOrgId%> && count==0)
		{
			alert("该用户当前机构下的岗位不能全部被删除!");
			return;
		}//判断属于当前机构被选中的机构岗位是否一个都没有了,如是提示返回,weida
		if(orgId1 == <%=mainOrgId%> && count==0)
		{
			alert("该用户主机构下的岗位不能全部被删除!");
			return;
		}//判断属于当前用户的主机构被选中的机构岗位是否一个都没有了,如是提示返回,weida
	
	var jobId = "";
	var flag1 = false;
	
	for (var m=document.all("jobId").options.length-1;m>=0;m--){
		  var option = document.all("jobId").options[m];
	      if(document.all("jobId").options[m].selected){
	      	jobId = option.value;
	        document.all("jobId").options[m]=null;
	        flag1 = true;
	      }
	      
    }

		 
		
		 		
	 	 send_request('<%=request.getContextPath()%>/sysmanager/user/savaUJO.jsp?uid='+uid+'&jobId='+jobId+'&orgId1='+orgId1+'&flag='+flag);
    
         //changebox();
}

function okadd(){
	var len1=document.all("allist").options.length;	
	var len=document.all("jobId").options.length;
	
		//if((len1 ==0 && len == 0)|| len==0){
		//alert("您没有选择机构或岗位！");
		//return;
		//}		    
	alert("加入机构成功！");    
	//parent.window.opener.location = parent.window.opener.location;  
    //parent.window.close();
    parent.closed();
       
}
//---------------------------------------
var http_request = false;
function send_request(url){
				http_request = false;
				//开始初始化XMLHttpRequest对象
				if(window.XMLHttpRequest){//Mozilla
					http_request = new XMLHttpRequest();
					if(http_request.overrideMimeType){//设置MIME类别
						http_request.overrideMimeType("text/xml");						
					}
				}
				else if(window.ActiveXObject){//IE
					try{
						http_request = new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							http_request = new ActiveXObject("Microsoft.XMLHTTP");							
						}catch(e){
						}
					}
				}
				if(!http_request){
					alert("不能创建XMLHttpRequest对象");
					return false;
				}
				http_request.onreadystatechange = processRequest;
				http_request.open("GET",url,true);
				http_request.send(null);
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
			   	 var len=document.all("jobId").options.length;			  	 	
        		 //var roleId = document.all("roleId").value;
        		 var uid="<%=uid%>";
        		 var orgId1="<%=orgId1%>";
        		 //alert(roleId);
        		 var jobId=new Array(len)
        		 for (var i=0;i<len;i++){	      
        		   jobId[i]=document.all("jobId").options[i].value;
         		 }   
         		
				 send_request('<%=request.getContextPath()%>/sysmanager/user/savaUJO.jsp?uid='+uid+'&jobId='+jobId+'&orgId1='+orgId1);
			}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
  <style type="text/css">
<!--
.style1 {
	font-size: 14px;
	color: #000000;
}
-->
  </style>
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="RoleOrgForm" id="RoleOrgForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center"><span class="style1">分配岗位</span></td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">机构可选岗位</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">[<font color="blue"><%=user.getUserName()%>(<%=user.getUserRealname()%>)</font>] 在该机构下的岗位</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right">
     <select name="allist"   style="width:80%" onDBLclick="addOrg()" size="18">
		  <pg:list requestKey="allOrg">
			<option value="<pg:cell colName="jobId"/>">
			<pg:null colName="jobName"><pg:cell colName="jobName"/></pg:null>
			<pg:notnull colName="jobName">
				<pg:equal colName="jobName" value=""><pg:cell colName="jobName"/></pg:equal>
				<pg:notequal colName="jobName" value=""><pg:cell colName="jobName"/></pg:notequal>
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
     	
	<select name="jobId"  style="width:80%" ondblclick="deleteorg()" size="18">
				  <pg:list requestKey="existOrg" needClear="false">
					<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/>
				</option>
				  </pg:list>			
 			</select>				
	</td>				 
				  
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">
    </td>
    
  </tr>
  <tr><td colspan="3" align="center">
  <input name="add" type="button" class="input" value="确定" onclick="okadd()" >
  <input name="exit" type="button" class="input" value="取消" onclick="parent.closed();">
  
  </td> </tr>
  <tr>
  <td colspan="3" align="center">
  <div id=divProcessing style="width:200px;height:30px;left:200px;top:330px;display:none">
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
  </td>
  </tr>
</table>


<iframe name="orgjobchange" width="0" height="0"></iframe>
</form>
</center>

</div>
</body>
</html>
