<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB"%>
<%
/*
 * <p>Title: 机构下的用户的操作主页面</p>
 * <p>Description: 机构下的用户的操作主页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-20
 * @author liangbing.tao
 * @version 1.0
 */
%>
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>

<%
	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String path = request.getContextPath();
	ResourceManager resManager = new ResourceManager();
    String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	String curOrgId = request.getParameter("orgId");
	 
	if(curOrgId == null)
	{
		curOrgId = (String)request.getAttribute("orgId");
	}
	String orgpath =  FunctionDB.buildOrgPath(curOrgId);
	
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) 
	{
		reFlush = "true";
	}
	
	String userNamesNo = null;
	
	
	
	String desc = (String)request.getParameter("pager.desc");	
	String intervalType = (String)request.getParameter("intervalType");
	String ischecked="";
	
	if((String)request.getAttribute("ischecked")==null)
	{
		ischecked ="";
	}
	else
	{
		ischecked =(String)request.getAttribute("ischecked");
	}
	
	//岗位操作开关
	boolean isJobOpen = ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>属性容器</title>
		<script language="javascript">
			var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
			var win;
			var isSucceed = '<%=request.getParameter("isSucceed")%>';
			var delSucceed = '<%=request.getParameter("delSucceed")%>';
			var isMain = '<%=request.getParameter("isMain")%>';
			var isRes = '<%=request.getParameter("isRes")%>';
			var userNamesNo = '<%=userNamesNo%>';
			
			//提示新增用户是否成功
			if(isSucceed=="1")
			{
				$.dialog.alert("<pg:message code="sany.pdp.add.user.success"/>@",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				var str = document.location.href;
				parent.window.document.location.href = str.substring(0,str.indexOf("isSucceed"));
			}
			
			//提示移除（删除）用户是否成功
			if(delSucceed=="2")
			{
				if(userNamesNo != "" && userNamesNo != "null"){
					$.dialog.alert("<pg:message code='sany.pdp.some.user.delete.fail'/>: \n"+userNamesNo,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				}
				else
				{
					$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				}
				var str = document.location.href;
				parent.window.document.location.href = str.substring(0,str.indexOf("delSucceed"));
			}
			
			//主机构
			if(isMain=="main")
			{
				$.dialog.alert("<pg:message code='sany.pdp.can.not.delete.mian.organization'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			
			
			var reFlush = "<%=reFlush%>";
			var isSelf = "<%=request.getParameter("isSelf")%>";
			
			if(isSelf == "true")
			{
				$.dialog.alert("<pg:message code='sany.pdp.can.not.delete.self'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			
			function getUserInfo(e,userId)
			{
				
			}
			
			
			function roleUser(type) 
			{
				var checks = "";
			    var arr = new Array();
			    arr = window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    var count = 0;
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks=="")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    		count++;
			    	}
			    }
			    //if(count < 2){
			    //	alert("");
			    //}
			    var winRole;
			    if (checks.split(",").length > 1)
			    {
			    	if (type==1)
			    	{
			    		userList.target = "deluser";
			    		var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/alotUserRole.jsp?orgId=<%=curOrgId%>&checks="+checks;
			    		$.dialog({ close:queryUser,title:'<pg:message code="sany.pdp.sys.role.authorize"/>',width:740,height:560, content:'url:'+url,lock: true});
					} 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
				
			function sortBy(byName)
			{
				var desc = "false";
			
				if ( "<%=desc%>"  == desc ) desc = "true";
				userList.action="${pageContext.request.contextPath}/user/userList.jsp?orgId=<%=curOrgId%>&pager.offset=0&pager.sortKey="+byName+"&pager.desc="+desc;
				userList.submit();
			}
				
			function actionOnmouseover(e)
			{	
				e.style.backgroundColor = "#8080FF";
			}
			
			function actionOnmouseup(e)
			{
				e.style.backgroundColor = "#BEC0C2";
			}
			
			function newUser()
			{
				$.dialog({ close:queryUser,title:'<pg:message code="sany.pdp.add.user"/>',width:740,height:560, content:'url:<%=path%>/purviewmanager/userorgmanager/user/userInfo.jsp?orgId=<%=curOrgId%>&isNewUser=true',lock: true});
			}
			
			function queryUser()
			{	
				var intervalType = document.all("intervalType").value;
				var userName = document.all("userName").value;
				var userRealname = document.all("userRealname").value;
				var userIsvalid = document.all("userIsvalid").value;
				var userSex = document.all("userSex").value;
				var userType = document.all("userType").value;
				
				var userName = userList.userName.value;
				var userRealname = userList.userRealname.value;
				
				
				
				if(userName == "" || userName.length<1 || userName.replace(/\s/g,"")=="")
				{
					
				}
				else
				{
						var re_ = /^\w+$/; 
						if(!re_.test(userName))
						{
						$.dialog.alert('<pg:message code="sany.pdp.check.username"/>');
						return false; 
						}
				}
				
				if(userRealname == "" || userRealname.length<1 || userRealname.replace(/\s/g,"")=="")
					{
					
					}
				else
					{
						var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
						if(!re.test(userRealname))
						{
						$.dialog.alert("<pg:message code="sany.pdp.check.realname"/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						return false; 
						}
					}
				
				var tablesFrame = document.getElementsByName("orgUserList");
				tablesFrame[0].src = "org_userlistIframe.jsp?orgId=<%=curOrgId%>&intervalType="+intervalType+"&userSex="+userSex
								+"&userRealname="+encodeURIComponent(userRealname)+"&userName="+userName+"&userIsvalid="+userIsvalid+"&userType="+userType;
				//userList.action="org_userlist.jsp?orgId=<%=curOrgId%>&intervalType="+intervalType;
				//userList.submit();	
			}
			function querytype(e){
			    queryUser();
			}
			
			function advQueryUser()
			{	
				history.back();	
			}
			
			//批量岗位授予
			function jobUser(type) 
			{
			    var arr = new Array();
			    arr = window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    var checks = "";
				var count = 0;
				
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks=="")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    	}
			    }
			    
			    var winjob;
			    if (checks.split(",").length > 1)
			    {
			    	if(type==1)
			    	{
				    	var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/allJobFrame.jsp?orgId=<%=curOrgId%>&checks="+checks;
				    	$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sys.post.authorize"/>',width:760,height:560, content:'url:'+url,lock: true});
				    } 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			
			//批量机构授予
			function orgUser(type) 
			{
			        var isSelect = false;
				    var outMsg;
					var userList = document.userList; 
					
					//选择两个或更多的用户进行批量处理,da.wei
					var count = 0;
					for (var j=0;j<userList.elements.length;j++)
					{
						if(userList.elements[j].checked)
						{
							count++;
						}
					}
					if(count<2)
					{
						$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!");
						return;
					}
					   
				    for (var i=0;i<userList.elements.length;i++) 
				    {
						var e = userList.elements[i];
							
						if (e.name == 'checkBoxOne')
						{
							if (e.checked)
							{
					       		isSelect=true;
					       		break;
						    }
						}
				    }
				    if (isSelect)
				    {
				    	if (type==1)
				    	{
				    		    userList.action="${pageContext.request.contextPath}/user/userOrg.do?orgId=<%=curOrgId%>&method=getAllOrgList";
				    		    userList.submit();
							    return true;
						} 
				    }
				    else
				    {
				    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				    	return false;
				    }
					return false;
			}
			//批量资源操作授予
			function operUser(type) 
			{
			    var arr = new Array();
			    arr = document.frames[0].document.getElementsByName("checkBoxOne");
			    var checks = "";
			    var count = 0;
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks == "")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    		count++;
			    	}
			    }
			    var winRes;
			    if(checks.split(",").length > 1)
			    {
			    	if (type==1)
			    	{   
			    		var url = "${pageContext.request.contextPath}/purviewmanager/grantmanager/userBatch_resFrame.jsp?orgId=<%=curOrgId%>&checks="+checks;
			    		$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sys.purview.authorize"/>',width:760,height:560, content:'url:'+url,lock: true});
					} 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			
			//批量用户权限回收
			function purviewReclaimUsers()
			{
			    var arr = new Array();
			    arr = window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    var checks = "";
			    var count = 0;
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks == "")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    		count++;
			    	}
			    }
			    var winRes;
			    if(checks.split(",").length > 1)
			    {    
			    	
			    	
			    	 var url = "${pageContext.request.contextPath}/purviewmanager/reclaimManager/reclaimUserRes.jsp?userIds="+ checks;
						$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sys.purview.recycle"/>',width:700,height:280, content:'url:'+url,lock: true});
			    	
					 
			    }
			    else
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.choose.two.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			//var winOpen
			function orderUser()
			{
				var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/user_order_ajax.jsp?orgId=<%=curOrgId%>";
				$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.sort.user"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			var timer;
			function isClosed()
			{
			    if(winOpen.closed==true)
			    {
			        window.location.href = window.location;
			        window.clearInterval(timer);
			    }
			}
			
			function foldUser()
			{
				var win;
				//调入用户
				var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/org/folduser_frame.jsp?orgId=<%=curOrgId%>";
				$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.move.in.user.alert" arguments="<%=orgpath%>"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			function addorg(dealType) 
			{
			    var outMsg;
			    var checks = "";
			    var state = false;
			   var obj=window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
			    if(obj.length==0)
			    {
			    	$.dialog.alert("<pg:message code='sany.pdp.no.user.to.move.out' />");
			    	return;
			    }
			    for(var j=0; j < obj.length; j++)
			    {
			    	if(obj[j].checked)
			    	{
			    		if(state)
			    		{
			    			checks += ","+obj[j].value;
			    		}
			    		else
			    		{
			    			checks = "" + obj[j].value;
			    			state = true;
			    		}
			    	}
			    }
			    if(checks != "")
			    {
				    if(dealType==1)
				    {
						var winaddorg;
						var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/org/userChangeOrg.jsp?checkBoxOne="+checks + "&orgId=<%=curOrgId%>";
						$.dialog({close:queryUser,title:'<pg:message code="sany.pdp.move.out.user.alert" arguments="<%=orgpath%>"/>',width:760,height:560, content:'url:'+url,lock: true});
					}
				}
				else
				{
					$.dialog.alert("<pg:message code='sany.pdp.choose.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			    	return false;
			    }
				return false;
			}
			
			//彻底删除用户
			function quiteDelUser()
			{
				var arr = new Array();
				//var arr = $(this).contents().find(".checkBoxOne");
				var arr=window.frames["orgUserList"].document.getElementsByName("checkBoxOne");
				var checks = "";
				for(var i = 0; i < arr.length; i++)
				{
					if(arr[i].checked)
					{
						if(checks == "")
						{
							checks = arr[i].value;
						}else{
							checks += "," + arr[i].value;
						}
					}
				}
				
				if(checks == "")
				{
					$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.remove.select'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				else
				{
					outMsg = "<pg:message code='sany.pdp.delete.comfirm'/>?";
					$.dialog.confirm(outMsg,function(){
						document.userList.target = "deluser";
						document.userList.action = "../../userorgmanager/user/quiteDelUser.jsp?checks="+checks+"&orgId=<%=curOrgId%>";
						//win = window.showModelessDialog("<%=path%>/purviewmanager/common/doing.jsp","",featrue);
						document.userList.submit();
						},function(){},null,"<pg:message code='sany.pdp.common.alert'/>"
					)
				}
			}
			
			function userResList(userId)
			{
				var typeName = "USER";
				window.showModalDialog("/sysmanager/user/userres_queryframe.jsp?orgId=<%=curOrgId%>&userId="+ userId+"&typeName="+typeName,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
			}
			</SCRIPT>		
			<body >
			<div class="titlebox">
					<SPAN class=location>当前机构：<A href="javascript:void"><%=orgpath %></A></SPAN>
			</div>
			<div class="mcontent">
			
			<form name="userList" method="post" >
				<input type="hidden" name="orgId" value="<%=curOrgId%>"/>
			<div id="searchblock">
	  <div  class="search_top">
	    <div class="right_top"></div>
	    <div class="left_top"></div>
      </div>
	  <div class="search_box">
	    <table width="98.1%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td class="left_box"></td>
	        <td>
	        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
	          <tr>
	            <th width="80" height="24"><pg:message code="sany.pdp.user.login.name"/>：</th>
	            <td><input type="text" name="userName" size="17"></td>
	            <th><pg:message code="sany.pdp.user.real.name"/>：</th>
	            <td><input type="text" name="userRealname" size="17"></td>
	            <th><pg:message code="sany.pdp.user.type"/>：</th>
	            <td><dict:select type="userType" name="userType" expression="{userType}" defaultValue="" textValue="<pg:message code='sany.pdp.common.operation.select' />" style="width:115px"/>
	           	</td>
	           </tr>
	          <tr>
	            <th height="24"><pg:message code="sany.pdp.sex"/>：</th>
	            <td><dict:select  type="sex" name="userSex" expression="{userSex}" defaultValue="" textValue="<pg:message code='sany.pdp.common.operation.select'/>" style="width:115px"/></td>
	            <th><pg:message code="sany.pdp.common.status"/>：</th>
	            <td><dict:select  type="isvalid" name="userIsvalid" expression="{user_isvalid}" defaultValue="" textValue="<pg:message code='sany.pdp.common.operation.select'/>" style="width:115px" /></td>
	            <th><pg:message code="sany.pdp.query.method"/>：</th>
	            <td><select name="intervalType" class="select" style="width:115px"> 
           								<option value="0" selected><pg:message code="sany.pdp.not.recursion.query"/></option>          						
           								<option value="1"><pg:message code="sany.pdp.recursion.query"/></option>
								    </select>
				</td>
				<td>
		            <a href="#" class="bt_1" onClick="queryUser()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
		            <a href="#" class="bt_2" onClick="userList.reset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a></td>
				</td>
				</tr>				
	          </table>
	          </td>
	        <td class="right_box"></td>
          </tr>
        </table>
      </div>
	  <div  class="search_bottom">
	    <div class="right_bottom"></div>
	    <div class="left_bottom"></div>
      </div>
    </div>
        </form>
	<div class="title_box">
		<div class="rightbtn">
			<%
				if(accesscontroler.checkPermission("orgunit",
                 		"newdeluser", AccessControl.ORGUNIT_RESOURCE)){
			%>
				<a href="#" class="bt_small" onClick="newUser()"><span><pg:message code="sany.pdp.add.user"/></span></a>
				<a href="#" class="bt_small" onClick="quiteDelUser()"><span><pg:message code="sany.pdp.delete"/></span></a>
			<%
				}
			%>        						
				<a href="#" class="bt_small" onClick="orderUser()"><span><pg:message code="sany.pdp.sort.user"/></span></a>
			<%
     						if(accesscontroler.checkPermission("orgunit",
                        		"usermanager", AccessControl.ORGUNIT_RESOURCE)){
     					%>							
				<a href="#" class="bt_small" onClick="foldUser()"><span><pg:message code="sany.pdp.move.in.user"/></span></a>
				<a href="#" class="bt_small" onClick="addorg(1)"><span><pg:message code="sany.pdp.move.out.user"/></span></a>
			<%
				}
			%>
				<a href="#" class="bt_small" onClick="roleUser(1)"><span><pg:message code="sany.pdp.sys.role.authorize"/></span></a>
				<%if(isJobOpen){ %>
				<a href="#" class="bt_small" onClick="jobUser(1)"><span><pg:message code="sany.pdp.sys.post.authorize"/></span></a>
				<% }%>
			<%
     						if(accesscontroler.checkPermission("orgunit",
                        		"purset", AccessControl.ORGUNIT_RESOURCE)){
     		%>	
				<a href="#" class="bt_small" onClick="operUser(1)"><span><pg:message code="sany.pdp.sys.purview.authorize"/></span></a>
				<a href="#" class="bt_small" onClick="purviewReclaimUsers()"><span><pg:message code="sany.pdp.sys.purview.recycle"/></span></a>	
			<%
				}
			%>	
		</div>
	</div>
	<iframe name="orgUserList" id="orgUserList" src="org_userlistIframe.jsp?orgId=<%=curOrgId%>" style="width:100%" height="100%" scrolling="auto" frameborder="0"  border="0"></iframe>
		<form name="form2" method="POST"></form>
		<iframe name="deluser" height="0" width="0" border="0"></iframe>
	</div>
	</body>
</html>
