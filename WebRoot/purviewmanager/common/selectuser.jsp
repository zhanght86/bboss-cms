<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择用户</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%
String loginName= request.getParameter("loginName");
if(loginName == null)
{
	loginName = "";
}
String loginID= request.getParameter("loginID");
if(loginID == null)
{
	loginID = "";
}
%>
</head>
<style type="text/css">
.a_bg_color{
	color: white;
    cursor:hand;
	background-color: #191970
}
</style>
<script language="javascript">
	var api = frameElement.api, W = api.opener;
	
</script>
<script type="text/javascript">

	function query(orgId){
		$("a").removeClass("a_bg_color");
		$("a[name='"+orgId+"']").addClass("a_bg_color"); 
		$("#userlist").load("<%=request.getContextPath()%>/workflow/config/queryUsers.page?org_id="+orgId); 
	}
	function queryList(){
		var user_worknumber = $("#user_worknumber").val();
		var user_realname = $("#user_realname").val();
		var user_name = $("#user_name").val();
		if(user_worknumber==''&&user_realname==''&&user_name==''){
			alert("请输入查询条件");
			return;
		}
		$("#userlist").load("<%=request.getContextPath()%>/workflow/config/queryUsers.page?user_worknumber="+user_worknumber+"&user_realname="+user_realname+"&user_name="+user_name); 
	}
	
	function submitData(){
		if($("#select1").find('option:selected').length == 0){
			$.dialog.confirm("请选择用户！",
					function(){},
					function(){},"","确认");
			return;
		}
		
		var userSel = $("#select1").find('option:selected');
	
		var usernames='';
	
		//alert(userSel);
		for (var i = 0; i < userSel.length; i++) { 
			if(i!=0){
				usernames+=","+userSel[i].value;
			}else{
				usernames=userSel[i].value;
			}
	    } 
		var win=api.config["currentwindow"];
		
		win.document.getElementById('<%=loginName%>').value=usernames;
		
		api.close();
		
	}
	//显示公司代码和用户已经选中的编码
	
	function showOrgInfo(){
		var selectuser=$("#select1").find('option:selected').val();
	
		$("#selectDetail").html("工号:"+$("#"+selectuser+"user_worknumber").val()+"<br/>登陆名:"+selectuser+"<br/>组织机构："+$("#"+selectuser+"org_name").val()+">"+$("#"+selectuser+"job_name").val());
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
	
	$(document).ready(function() {
		 $("#org_tree").load("orgtree.jsp");
	});
	
</script>
<body class="easyui-layout">
	<input type="hidden" value="${usernames }" name="usernames" id="usernames"/>
	<input type="hidden" value="${node_key }" name="node_key" id="node_key"/>
	<input type="hidden" value="${user_realnames }" name="user_realnames" id="user_realnames"/>
	<div region="west" split="true" title="组织结构"
		style="width: 350px; height: 150px; padding1: 1px; " id="org_tree">
	</div>
	<div region="center" title="用户列表" split="true"
		style="height: 800px; padding: 10px; background: #efefef;">
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
												<a href="javascript:submitData()"  class="bt_1"><span>确定</span></a>
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
				<div id="userlistContainer1">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="select_table">
						<tr>
							<th width="20%">用户</th>
						</tr>
						<tr>
							<td >
								<div id="userlist">
									<select name="select1" size="25" multiple="multiple" id="select1"
										style="width: 220px; height: 340px">
										<pg:list requestKey="userList">
											<option value="<pg:cell colName='user_name'/>">
												<pg:cell colName="user_realname" />(<pg:cell colName='user_worknumber'/>)
											</option>
										</pg:list>
									</select>
									<pg:list requestKey="userList">
										<input type="hidden" value="<pg:cell colName='user_worknumber'/>"
											id="<pg:cell colName='userName'/>user_worknumber" />
										<input type="hidden" value="<pg:cell colName='org_name'/>"
											id="<pg:cell colName='userName'/>org_name" />
										<input type="hidden" value="<pg:cell colName='job_name'/>"
											id="<pg:cell colName='userName'/>job_name" />
									</pg:list>
								</div>	
							</td>
							
							
							
						</tr>
						<tr>
							<td colspan="5"><div class="treesearch" id="selectDetail">工号:</br>登陆名:</br>组织机构:</div></td>
						</tr>
					
					</table>
				</div>
			</div>

</body>
</html>