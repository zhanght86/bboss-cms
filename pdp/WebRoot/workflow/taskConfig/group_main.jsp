<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>

<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setDateHeader("max-age", 0);
			
			String groupName = request.getParameter("groupName");
			String groupDesc = request.getParameter("groupDesc");

			
			groupName = groupName == null ? "" : groupName ;
			groupDesc = groupDesc == null ? "" : groupDesc ;


		%>
<html>
<head>
<%@ include file="/common/jsp/css.jsp"%>
<link href="stylesheet/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/commontool.js"></script>	
		<title>用户组管理</title>
		<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.style3 
	{
		font-size: 14px;
		font-weight: bold;
		color: #3300FF;
	}
	.operStyle
	{
	width:17;
	height:16;
	}
</style>

<SCRIPT LANGUAGE="JavaScript">	
var api = frameElement.api, W = api.opener;
function validateInfo()
{
	var gn = groupForm.groupName.value;
	var gd = groupForm.groupDesc.value;
	var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
	
	if((gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="") 
		&& (gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
		)
	{
		$.dialog.alert("查询条件不能为空!",function(){});
		return false;
	}
	
	if(gn == "" || gn.length<1 || gn.replace(/\s/g,"")=="")
		{
		
		}
	else if(!re.test(gn))
	{
		$.dialog.alert("用户组名称不能有非数字、中文、字母的字符",function(){});
		return false; 
	}
	
	if(gd == "" || gd.length<1 || gd.replace(/\s/g,"")=="")
		{
		
		}
	else if(!re.test(gd))
	{
		$.dialog.alert("用户组描述不能有非数字、中文、字母的字符",function(){});
		return false; 
	}
	groupForm.action = "groupInfo.jsp";
	groupForm.submit();
}

  
/* 添加选择的项 */
function move(ObjSource, ObjTarget) {
	if (ObjSource.val() == null)
		return;
	try{
		$.each(ObjSource.find('option:selected'), function(i, n) {
			
				if(SelectIsExitItem(ObjTarget,n.value)){
						throw "已选列表中已存在  "+n.innerHTML;
					
				};
			
			ObjTarget.append(n);
		});
		ObjSource.find('option:selected').remove();
	}catch(e) {    
	    alert(e);    
	}	
}

/* 添加全部 */
function moveAll(ObjSource, ObjTarget) {
	try{
		$.each(ObjSource.find('option'), function(i, m) {		
				if(SelectIsExitItem(ObjTarget,m.value)){
						throw "已选列表中已存在  "+n.innerHTML;
					
				};		
		});
		ObjTarget.append(ObjSource.html());
		ObjSource.empty();
	}catch(e) {    
	    alert(e);    
	}	
}
function SelectIsExitItem(objSelect, objItemValue) {        
    var isExit = false;
    for (var i = 0; i < objSelect.find('option').length; i++) {        
        if (objSelect.find('option')[i].value == objItemValue) {        
            isExit = true;        
            break;        
         }
     }        
    return isExit;        
}

$(document).ready(function(){
	ObjSource = $('#select1');
	ObjTarget = $('#select2');
	try{
		$.each(ObjSource.find('option'), function(i, m) {		
				if(SelectIsExitItem(ObjTarget,m.value)){
					//alert(ObjSource.find('option')[i].remove());
					};		
		});
		
	}catch(e) {    
	    alert(e);    
	}	;
	
	$("#select1").click(function(){
		var detail=$("#select1").find('option:selected').val();
		if(detail)
		$("#selectDetail").html("描述："+$("#"+detail+"desc").val()+"<br/>创建人："+$("#"+detail+"owner").val());
	})
});

function setGroups(){
	var groups ;
	var groupnames;
	for (var i = 0; i < $("#select2").find('option').length; i++) { 
		if(i!=0){
			groups+=","+$("#select2").find('option')[i].value;
			groupnames+=","+$("#select2").find('option')[i].innerHTML;
		}else{
			groups=$("#select2").find('option')[i].value;
			groupnames=$("#select2").find('option')[i].innerHTML;
		}
     } 
	$("#groups").val(groups);
	$("#groupnames").val(groupnames);
}
function query(){
	setGroups();
	document.submitForm.action="toChooseGroupPage.page";
	document.submitForm.submit();
}
function submitData(){
	setGroups();
	var node_key = $("#node_key").val();
	W.$("#"+node_key+"_groups_id").val($("#groups").val());
	W.$("#"+node_key+"_groups_name").val($("#groupnames").val());
	api.close();
}

</SCRIPT>
	</head>
	<body>
	<form name="submitForm" method="post">
	<input type="hidden" name="groups" id="groups"/>
	<input type="hidden" name="groupnames" id="groupnames"/>
	<input type="hidden" value="${node_key }" name="node_key" id="node_key"/>
	<table width="700" border="0" cellspacing="0" cellpadding="0" align="center" height="100%">
		<tr>
			<td valign="top">
				<div class="treesearch">
					用户组名称：<input type="text" name="group_name" id="group_name" class="input0 w100" /> 
					用户组描述：<input type="text" name="group_desc" id="group_desc" class="input0 w100" /> 
					创建人登陆名：<input type="text" name="user_name" id="user_name" class="input0 w100" />
					<a href="javascript:query()"  class="bt_1"><span>搜索</span></a>
			  </div>
				<div class="treesearch">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="select_table">
						<tr>
							<th width="40%">待选用户组</th>
							<td width="20%">&nbsp;</td>
							<th width="40%">已选用户组</th>
						</tr>
						<tr>
							<td align="left"><select name="select1" size="15"
								multiple="multiple" id="select1" style="width:220px;height: 225px">
								<pg:list requestKey="grouplist">
									<option value="<pg:cell colName='group_name'/>"><pg:cell colName="group_name"/></option>
								</pg:list>						
							</select></td>
							<pg:list requestKey="grouplist">
							<input type="hidden" value="<pg:cell colName='group_desc'/>" id="<pg:cell colName='group_name'/>desc"/>
							<input type="hidden" value="<pg:cell colName='owner_name'/>" id="<pg:cell colName='group_name'/>owner"/>
							</pg:list>		
							<td align="center"><input type="button" name="button"
								id="button" value=">"
								onclick="move($('#select1'),$('#select2'))" /> <br /> <input
								type="button" name="button3" id="button3" value="<"
								onclick="move($('#select2'),$('#select1'))" /> <br /> <input
								type="button" name="button4" id="button4" value=">>"
								onclick="moveAll($('#select1'),$('#select2'))" /> <br /> <input
								type="button" name="button5" id="button5" value="<<"
								onclick="moveAll($('#select2'),$('#select1'))" /></td>
							<td align="right"><select name="select2" size="15"
								multiple="multiple" id="select2" style="width: 220px;height: 225px">
								<pg:list requestKey="chooseGroupList">
									<option value="<pg:cell colName='group_name'/>"><pg:cell colName="group_name"/></option>
								</pg:list>	
							</select></td>
						</tr>
						<tr>
							<td colspan="3"><div class="treesearch" id="selectDetail">描述：</div></td>
						</tr>
						<tr>
							<td colspan="3" align="center" height="40"><a href="javascript:submitData()"  class="bt_1"><span>确定</span></a></td>
						</tr>
					</table>
				</div>
		  </td>
	  </tr>
	</table>
	</form>
</body>
</html>
