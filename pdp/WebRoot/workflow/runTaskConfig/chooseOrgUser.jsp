<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择用户</title>
<%@ include file="/common/jsp/css.jsp"%>
<link href="stylesheet/common.css" rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
	function query(orgId){
		$("#userlistContainer").load("queryUsers.page #userlistContent",{org_id:orgId},function(){loadjs()}); 
	}
	function queryList(){
		var user_worknumber = $("#user_worknumber").val();
		var user_realname = $("#user_realname").val();
		var user_name = $("#user_name").val();
		$("#userlistContainer").load("runqueryUsers.page #userlistContent",{user_worknumber:user_worknumber,user_realname:user_realname,user_name:user_name},function (){loadjs()}); 
	}
	
	function addUserChoose(user_name,user_realname){
		
		var usernamesArr = new Array();
		usernamesArr = document.getElementById("usernames").value.split(",");
		for(i=0;i<usernamesArr.length;i++){
			if(user_name==usernamesArr[i]){
				return;
			}
		}
		if($("#usernames").val()!='null'&&$("#usernames").val()!=''){
			$("#usernames").val($("#usernames").val()+","+user_name);
			$("#user_realnames").val($("#user_realnames").val()+","+user_realname);
		}else{
			$("#usernames").val(user_name);
			$("#user_realnames").val(user_realname);
		}
		$("#chooseuserlistContainer").load("runqueryUserByNames.page #chooseuserlistContent",{usernames:$("#usernames").val()},function(){loadjs()});
	}
	
	function deleteChooseUser(user_name,user_realname){
		alert(user_name);
		alert(user_realname);
		var usernames = "";
		var userrealnames = "";
		var usernamesArr = new Array();
		usernamesArr = document.getElementById("usernames").value.split(",");
		userrealnameArr = document.getElementById("user_realnames").value.split(",");
		for(i=0;i<usernamesArr.length;i++){
			if(user_name==usernamesArr[i]){
			}else{
				if(usernames==""){
					usernames = usernamesArr[i];
				}else{
					usernames += ","+usernamesArr[i];
				}
				
			}
		}
		for(i=0;i<userrealnameArr.length;i++){
			if(user_realname==userrealnameArr[i]){
			}else{
				if(userrealnames==""){
					userrealnames = userrealnameArr[i];
				}else{
					userrealnames += ","+userrealnameArr[i];
				}
				
			}
		}
		$("#usernames").val(usernames);
		$("#user_realnames").val(userrealnames);
		$("#chooseuserlistContainer").load("runqueryUserByNames.page #chooseuserlistContent",{usernames:$("#usernames").val()},function(){loadjs()});
	}
	function submitData(){
		var node_key = $("#node_key").val();
		W.$("#"+node_key+"_users_id").val($("#usernames").val());
		W.$("#"+node_key+"_users_name").val($("#user_realnames").val());
		api.close();
	}
	$(document).ready(function() {
		 $("#userlistContainer").load("runqueryUsers.page #userlistContent",function(){loadjs()}); 
		 $("#chooseuserlistContainer").load("runqueryUserByNames.page #chooseuserlistContent",{usernames:$("#usernames").val()},function(){loadjs()}); 
	   });
</script>
<body class="easyui-layout">
	<div region="west" split="true" title="组织结构"
		style="width: 200px; height: 150px; padding1: 1px; overflow: hidden;">
	<%@ include file="orgTree.jsp"%>
	</div>
	<div region="center" title="用户列表" split="true"
		style="height: 800px; padding: 10px; background: #efefef;">
		<div class="easyui-layout" fit="true" style="background: #ccc;">
			<div region="center" style="height: 465px;">
				<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<form id="queryForm" name="queryForm">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
										<tr>
											<th>工号：</th>
											<td><input id="user_worknumber" name="user_worknumber" type="text"
													value="" class="w120"/></td>
											<th>
												姓名：
											</th>
											<td>
												<input id="user_realname" name="user_realname" type="text"
													value="" class="w120" />
											</td>
											<th>登陆名：</th>
											<td><input id="user_name" name="user_name" type="text"
													value="" class="w120"/></td>
											<th>
												&nbsp;
											</th>
											<td>
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span>查询</span>
												</a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span>
												</a>
												<input type="reset" id="reset" style="display:none"/>
											</td>
										</tr>
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
						</table>
					</form>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
				<div id="userlistContainer">
					
				</div>
			</div>
			<div region="south" split="true" style="height: 185px;">
				<input type="hidden" value="${usernames }" name="usernames" id="usernames"/>
				<input type="hidden" value="${node_key }" name="node_key" id="node_key"/>
				<input type="hidden" value="${user_realnames }" name="user_realnames" id="user_realnames"/>
				<div id="chooseuserlistContainer">
					
				</div>
				<table id="table3">
					<tr>
						<td align="right"><a href="javascript:void(0)" class="bt_1" id="addButton" onclick="submitData()"><span>确定</span></a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>

</body>
</html>