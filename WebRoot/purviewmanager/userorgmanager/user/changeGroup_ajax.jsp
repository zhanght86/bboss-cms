<%
/*
 * <p>Title: 用户隶属用户组的操作页面</p>
 * <p>Description: 用户隶属用户组的操作页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String uid = request.getParameter("userId");
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(uid);
%>
<html>
<head>    
  <title>属性容器</title>

	<SCRIPT LANGUAGE="JavaScript"> 
		
	var api = parent.frameElement.api, W = api.opener;
	
		function send_request(url)
		{
			//document.all.divProcessing.style.display = "block";
			
			//document.all("button1").disabled = true;
			//document.all("button2").disabled = true;
			//document.all("button3").disabled = true;
			//document.all("button4").disabled = true;
			//document.all("back").disabled = true;
			
			document.OrgJobForm.action = url;
			document.OrgJobForm.target = "hiddenFrame";
			document.OrgJobForm.submit();
		}
		
		function addone(name,value,n)
		{
		   for(var i=n;i>=0;i--)
		   {
				if(value==document.all("groupId").options[i].value)
				{
				  return;
				}
			}
		   var op=new Option(name,value);
		   document.all("groupId").add(op); 
		}
		
		function addGroup()
		{
		  var len=document.all("groupId").options.length;
		  var len1=document.all("allist").options.length;
		  var exist = document.all("groupId");
		  var allexist = "";
			 if(len == 0 && len1 == 0)
			 {
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.select"/>');
				return;
			 }	
		   var groupId = "";
		   var flag1 = false;		
		   var n=document.all("groupId").options.length-1;
		   var flag ="1";	 	
		   var groupIds = "";
		   for(var i=0;i<document.all("allist").options.length;i++)
		   {   
		       var op=document.all("allist").options[i];
			   if(op.selected) 
			   {
				   var flag_ = false;
				   for(var j = 0; j < exist.length; j++){
			   			if(op.text==exist.options[j].text){
			   				flag_ = true;
			   			}
			   		}
			   		if(!flag_)
			   		{
			   			if(groupIds=="") 
				   			{
				   			groupIds = op.value;
				   			}
			   			else 
				   			{
				   			groupIds +=  "," + op.value;
				   			}
			   			
			   			addone(op.text,op.value,n);
			   		}
			   		else
			   		{
			   			if(allexist == ""){
			   				allexist = op.text;
			   			}else{
			   				allexist += "\n" + op.text;
			   			}
			   		}
			   		// groupId = op.value;
			   		// if(groupIds=="") groupIds = groupIds + groupId;
			   		// else groupIds = groupIds + "," + groupId;
			   		// addone(op.text,op.value,n);	   	     
			   }  	   
		   }
		   if(groupIds != ""){
		       send_request('saveUserGroup.jsp?userId=<%=uid%>&groupId='+groupIds+'&flag='+flag);
		   }else if(allexist != ""){
		   		W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.usergroup.set"/>'+"：\n" + allexist);
	  			return;
		   }else{
		   		W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.usergroup.add.select"/>');
		   		return;
		   }
		}
	
	function addall()
	{
	   var len=document.all("groupId").options.length;
	   var len1=document.all("allist").options.length;
	   var exist = document.all("groupId");
		  var allexist = "";
		 if(len == 0 && len1 == 0)
		 {
			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.select"/>');
			return;
		 }	
	     var groupIds = "";
	     var flag1 = false;	
		 var n=document.all("groupId").options.length-1;
		 var p=document.all("allist").options.length-1;	
		 var flag ="1";	  
	     for(var i=0;i<document.all("allist").options.length;i++){
		     var op=document.all("allist").options[i];
		     var flag_ = false;
			   for(var j = 0; j < exist.length; j++){
		   			if(op.text==exist.options[j].text){
		   				flag_ = true;
		   			}
		   		}
		   		if(!flag_)
		   		{
		   			if(groupIds=="") 
			   			{
			   			groupIds = op.value;
			   			}
		   			else 
			   			{
			   			groupIds +=  "," + op.value;
			   			}
		   			
		   			addone(op.text,op.value,n);
		   		}
		   		else
		   		{
		   			if(allexist == ""){
		   				allexist = op.text;
		   			}else{
		   				allexist += "\n" + op.text;
		   			}
		   		}
		     //if(flag1){
			//	groupIds += "," + op.value;
			//}else{
			//	groupIds += op.value;
				//flag1 = true;
			 //}   
	        // addone(op.text,op.value,n);           
	     }
	     if(groupIds != ""){
		       send_request('saveUserGroup.jsp?userId=<%=uid%>&groupId='+groupIds+'&flag='+flag);
		   }else if(allexist != ""){
		   		W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.usergroup.set"/>'+"：\n" + allexist);
	  			return;
		   }
	    // send_request('saveUserGroup.jsp?userId=<%=uid%>&groupId='+groupIds+'&flag='+flag);
	}
	
	function deleteGroup()
	{
		 var leftLen = document.all("allist").length;
		 var len=document.all("groupId").options.length;
		 if(len == 0){
			W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.usergroup.delete.null"/>');
			return;
		 }	

		 if(document.all("groupId").selectedIndex==-1){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.remove.select"/>');
				return;
			}
		 
		 var flag = "0";
		 var groupIds = "";
		 var infos = "";   
		 for (var m=document.all("groupId").options.length-1;m>=0;m--)
		 {
		 	var flag1 = false;
	        var option = document.all("groupId").options[m];	 		
	 		if(document.all("groupId").options[m].selected)
	 		{
	 			for(var i = 0; i < leftLen; i++){
	 				if(option.value==document.all("allist").options[i].value){
						groupId = option.value;	
						if(groupIds=="")
							 groupIds = groupIds + groupId;
				   		else
				   			 groupIds = groupIds + "," + groupId;		
				   			 
				        document.all("groupId").options[m]=null;
				        flag1 = true;	        		 
			        }
		        }
		        if(!flag1){
		        	if(infos==""){
		        		infos = option.text;
		        	}else{
		        		infos += "\n" + option.text; 
		        	}
		        }       
		    }	 		
	 	}
	 	if(groupIds != ""){
	 	    send_request('saveUserGroup.jsp?userId=<%=uid%>&groupId='+groupIds+'&flag='+flag);
	 	}else{
	 		if(infos!=""){
	 			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.nopurview"/>'+"：\n"+infos);
	 			return false;
	 		}
	 	}
	}
	
	function deleteall()
	{
		var leftLen = document.all("allist").length;
		var len=document.all("groupId").options.length;
		 if(len == 0)
		 {
			W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.usergroup.delete.null"/>');
			return;
		 }	
		var groupIds = "";
		var infos = "";
		for (var m=document.all("groupId").options.length-1;m>=0;m--){
			var flag1 = false;
	   		var option = document.all("groupId").options[m];
	   		for(var i = 0; i < leftLen; i++){
		   		if(option.value==document.all("allist").options[i].value){
					if(groupIds!=""){
						groupIds += "," + option.value;
					}else{
						groupIds += option.value;
					}
					document.all("groupId").options[m]=null;
					flag1 = true;
				}
			}
			if(!flag1){
				if(infos!=""){
					infos += "\n" + option.text;
				}else{
					infos = option.text;
				}
			}
	   	} 
	   	
	  // 	if(window.confirm("你确定要全部删除吗？"))
	  // 	{	
	   		if(groupIds!=""){
		   	    var flag = "0";    
		        send_request('saveUserGroup.jsp?userId=<%=uid%>&groupId='+groupIds+'&flag='+flag);
	        }else{
	        	if(infos!=""){
	        		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.nopurview"/>'+"：\n"+infos);
	 				return false;
	        	}
	        }          
	   //	}
	   	//window.location.reload();
	}
	function changebox()
	{				 
		var len=document.all("groupId").options.length;			  	 	
	    var groupId=new Array(len)
	    for (var i=0;i<len;i++)
	    {	      
	        groupId[i]=document.all("groupId").options[i].value;
	    }           		
		send_request('saveUserGroup.jsp?groupId='+groupId);
	}
	
	function closed()
	{
		parent.window.close();
		parent.window.returnValue = "ok";
	}
</SCRIPT>
 	<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
	<body class="contentbodymargin" scroll="no">
		<div id="">
			<center>
				<form name="OrgJobForm" action="" method="post" >
					<input name="userId" value="<%=uid%>" type="hidden">
						<table width="80%" border="0" cellpadding="0" cellspacing="1">
							<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						  
						  <tr class="tabletop">
						    <td width="40%" align="center">可选组</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center"><%=user.getUserRealname()%>(<%=user.getUserName()%>)已授予组</td>
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
		  	
						    <td align="center">
						    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
								      <tr>
								        <td align="center"><a class="bt_2"  onclick="addGroup()"><span>&gt;</span></a></td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
								      <tr>
								        <td align="center"><a class="bt_2"  onclick="addall()"><span>&gt;&gt;</span></a></td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
								      <tr>
								        <td align="center"><a class="bt_2" onclick="deleteall()"><span>&lt;&lt;</span></a></td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
								      <tr>
								        <td align="center"><a class="bt_2"  onclick="deleteGroup()"><span>&lt;</span></a></td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
								</table>
						   </td>
						   
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
				</table>
			</form>
		</center>
	</div>
	
	<div id=divProcessing style="width:200px;height:30px;position:absolute;left:160px;top:450px;display:none">
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
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</body>
</html>

