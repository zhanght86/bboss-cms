<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>

<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager,
				 com.frameworkset.platform.sysmgrcore.manager.UserManager,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	String QueryuserName = null;
	String QueryuserRealname = null;
	if(request.getParameter("userName")==null){
		QueryuserName = "";
	}else{
		QueryuserName = request.getParameter("userName");
	}
	if(request.getParameter("userRealname")==null){
		QueryuserRealname = "";
	}else{
		QueryuserRealname = request.getParameter("userRealname");
	}
	

    ResourceManager resManager = new ResourceManager();
    String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
%>
<html>
	<head>
		<title>离散用户管理</title>
		

<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<SCRIPT language="Javascript">
var api = frameElement.api,W = api.opener;

var isSucceed = <%=request.getParameter("delSucceed")%>;
if(isSucceed=="3"){
	$.dialog.alert("<pg:message code='sany.pdp.userorgmanager.user.delete.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	var str = document.location.href;
	parent.window.document.location.href = str.substring(0,str.indexOf("delSucceed"));
}
</SCRIPT>
<SCRIPT language="javascript">	

var isAdmin = "<%=request.getParameter("isAdmin")%>";
if(isAdmin == "true")
{
	$.dialog.alert("<pg:message code='sany.pdp.userorgmanager.user.delete.admin.no'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
}



function dealRecord(dealType) {
    var isSelect = false;
    var outMsg;
    var obj=document.getElementsByName("checkBoxOne");
	if(obj.length==0){
		$.dialog.alert("<pg:message code='sany.pdp.userorgmanager.user.delete.null'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		return;
	}   
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];
			
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		parent.$.dialog.confirm("<pg:message code='sany.pdp.dictmanager.dict.delete.confirm'/>",function(){
				 
				$.ajax({
					   type: "POST",
						url : "<%=request.getContextPath()%>/usermanager/lisanuserInfo_deletehandle.page",
						data :formToJson("#userList"),
						dataType : 'json',
						async:false,
						beforeSend: function(XMLHttpRequest){
								 
						      		blockUI();	
						      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
						      	 		 	
							},
						success : function(response){
							//去掉遮罩
							unblockUI();
							if(response.code=="success"){
								var msg = response.errormessage;
								 
								 $.dialog.alert(msg,function(){	
										 reloadusers();
										api.close();
								},api);													
							}else{
								 $.dialog.alert("操作结果："+response.errormessage,function(){	
									 
									},api);	
								 
							}
						}
					  });

	 			return true;
    		});
		} 
    }else{
    	$.dialog.alert("<pg:message code='sany.pdp.choose.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    	return false;
    }
	return false;
}

function resetwindow()
{
	document.userList.action = window.location.href;	
	document.userList.target = "";
	
	document.userList.submit();
	
}
	

function actionOnmouseover(e){	
	e.style.backgroundColor = "#8080FF";
}

function actionOnmouseup(e){
	e.style.backgroundColor = "#BEC0C2";
}


function queryUser()
{	
	//if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
	//	alert("用户名称和用户实名必须输入一个!!!");
	//	return;
	//}
	userList.action="../user/discreteUserList.jsp"
	userList.target="";
	userList.submit();	
}

//批量加入机构
function addorg(dealType) {
    var isSelect = false;
    var outMsg;
    var checks;
    var state = false;
    var obj = document.getElementsByName("checkBoxOne");
    if(obj.length==0){
    	$.dialog.alert("<pg:message code='sany.pdp.userorgmanager.user.transfer.in.null'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    	return;
    }
    for(var j=0; j < obj.length; j++){
    	if(obj[j].checked){
    		if(state){
    			checks += ","+obj[j].value;
    		}else{
    			checks = "" + obj[j].value;
    			state = true;
    		}
    	}
    }
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];	
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		if(checks != ""){
				
				var winaddorg;
				var url = "../purviewmanager/userorgmanager/user/allorg2discrete.jsp?checkBoxOne="+checks;
				//window.open(url);
				$.dialog({title:'<pg:message code="sany.pdp.userorgmanager.user.transfer.org.in"/>',width:760,height:560, content:'url:'+url,lock: true});
				
 			}else{
 				$.dialog.alert("<pg:message code='sany.pdp.userorgmanager.user.transfer.in.null'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
 				return;
 			}
		} 
    }else{
    	$.dialog.alert("<pg:message code='sany.pdp.choose.record'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    	return false;
    }
	return false;
}

<!-- gao.tang 2007.11.05 start 右键菜单跳转路径  -->
function userInfo(userId){ 
	//用户基本信息 userInfo3.jsp
		
	var winbasic;
	var url="../purviewmanager/userorgmanager/user/userInfo_lisan.jsp?userId="+userId;
	$.dialog({title:'<pg:message code="sany.pdp.sys.info.view"/>',width:760,height:560, content:'url:'+url,lock: true});


	
}
function reloadusers()
{
	document.location.href = document.location.href;
}

</SCRIPT>		
			<body>
			<div style="height: 10px">&nbsp;</div>
			<form id="userList" name="userList" method="post" >
			<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
         				<tr >
           					
           					<td colspan="4" height='30' valign='middle' align="left"><pg:message code="sany.pdp.user.login.name"/>：<input type="text" name="userName" value="<%=QueryuserName%>" >
           					<pg:message code="sany.pdp.user.real.name"/>：<input type="text" name="userRealname" value="<%=QueryuserRealname%>" ></td>
           					
           					<td height='30'valign='middle' align="center">
           						<a class="bt_1" onClick="queryUser()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>           						
           						<a class="bt_2" onClick="javascript:document.userList.userName.value='';document.userList.userRealname.value='';"><span><pg:message code="sany.pdp.common.operation.clear"/></span></a>
           					</td>
         				</tr>
         				</table>
         				</td>
         				<td class="right_box"></td>
         				</tr>
         				</table>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
			<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="javascript:dealRecord(1); return false;"><span><pg:message code="sany.pdp.common.batch.delete"/></span></a>
				<a href="javascript:void(0)" class="bt_small" id="delBatchButton" onclick="javascript:addorg(1); return false;"><span><pg:message code="sany.pdp.move.in.user"/></span></a>
				</div>
				<strong><pg:message code="sany.pdp.workflow.user.list"/></strong>
			</div>
						<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.DiscreteUserList" keyName="DiscreteUserList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="10" scope="request" data="DiscreteUserList" isList="false">
						
					
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
							<pg:header>
								<!--设置分页表头-->
								<th>
									<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')" width="10">
								</th>
								<th><pg:message code="sany.pdp.user.login.name"/>	</th>
								<th><pg:message code="sany.pdp.user.real.name"/></th>
								<th onclick="sortBy('userName')">工号</th>
								<th onclick="sortBy('userName')">身份证</th>
								<th onclick="sortBy('userName')">电话</th>		
								<th><pg:message code="sany.pdp.user.type"/></th>
								<th><pg:message code="sany.pdp.email"/></th>
								<th>密码过期时间</th>						
								
							</pg:header>
							
							<pg:param name="userName" />
							<pg:param name="userRealname" />
						<pg:notify >
							<tr>
									<td class="td_center" colspan="100"><div class="nodata">
							<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div></td>
						</pg:notify>
							<%
								ContextMenu contextmenu = new ContextMenuImpl();
					 		%>
							<!--list标签循环输出每条记录-->
							<pg:list>		
							<%	
								int userId = dataSet.getInt("userId");
											
	
								Menu menu = new Menu();
								menu.setIdentity("opuser_"+userId);
								
								
								//用户基本信息
								
				 				if (accessControl.checkPermission(resId,
	                         		"lisanuseredit", AccessControl.ORGUNIT_RESOURCE))
	            				{
									Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
									menuitem2.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.info.view", request));
									menuitem2.setLink("javascript:userInfo("+userId+")");
									menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/new.gif");
									menu.addContextMenuItem(menuitem2);
								
								contextmenu.addContextMenu(menu);
								}
								
							%>						
							
							<%if(userId!=1){%>	
								<tr>
									<td class="td_center" >
										<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="userId" defaultValue=""/>" width="10">
									</td>
									<td>
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td id="opuser_<%=userId%>" bgcolor="#F6FFEF">
										<pg:cell colName="userRealname" defaultValue="" />
									</td>			
									<td><pg:cell colName="userWorknumber"
										defaultValue="" /></td>
									<td><pg:cell colName="userIdcard"
											defaultValue="" /></td>
									<td><pg:cell colName="userMobiletel1"
											defaultValue="" /></td>								
									<td>
										<dict:itemname type="userType" expression="{userType}" />
									</td>
									<td>
										<pg:cell colName="userEmail"/>
									</td>	
									<td><pg:cell colName="passwordExpiredTime" dateformat="yyyy-MM-dd HH:mm:ss"
										defaultValue="" /></td>		
								</tr>
							<%}else{%>
								<tr>
									<td class="td_center" >
										<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="userId" defaultValue=""/>" >
									</td>
									<td>
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td id="opuser_<%=userId%>"  bgcolor="#F6FFEF">
										<pg:cell colName="userRealname" defaultValue="" />
									</td>	
									<td><pg:cell colName="userWorknumber"
										defaultValue="" /></td>
									<td><pg:cell colName="userIdcard"
											defaultValue="" /></td>
									<td><pg:cell colName="userMobiletel1"
											defaultValue="" /></td>											
									<td>
										<dict:itemname type="userType" expression="{userType}" />
									</td>
									<td>
										<pg:cell colName="userEmail"/>
									</td>	
									<td><pg:cell colName="passwordExpiredTime" dateformat="yyyy-MM-dd HH:mm:ss"
										defaultValue="" /></td>		
								</tr>
							<%}%>
							</pg:list>
							<%
							request.setAttribute("opuser",contextmenu);
							%>
							<pg:contextmenu enablecontextmenu="true" context="opuser" scope="request"/> 
							</table>
							<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
						
						</pg:pager>
						<tr><td colspan="6" align='center'><div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:330px;display:none">
					<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
					    <tr>
						    <td bgcolor=#3A6EA5>
								    <font color=#FFFFFF>...处理中...请等待...</font>
							</td>
						</tr>
					</table>
				</div></td></tr>

				  </table>
				</form>
				
 
			</body>
			<center>
</html>
