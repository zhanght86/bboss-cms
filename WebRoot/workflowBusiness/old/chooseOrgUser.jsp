<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择用户</title>
<%@ include file="/common/jsp/css.jsp"%>
<link href="stylesheet/common.css" rel="stylesheet" type="text/css" />
</head>
<style type="text/css">
.a_bg_color{
	color: white;
    cursor:hand;
	background-color: #191970
}
</style>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
	function query(orgId){
		$("a").removeClass("a_bg_color");
		$("a[name='"+orgId+"']").addClass("a_bg_color"); 
		$("#userlist").load("${pageContext.request.contextPath}/workflow/config/queryUsers.page?org_id="+orgId); 
	}
	function queryList(){
		var user_worknumber = $("#user_worknumber").val();
		var user_realname = $("#user_realname").val();
		var user_name = $("#user_name").val();
		if(user_worknumber==''&&user_realname==''&&user_name==''){
			alert("请输入查询条件");
			return;
		}
		$("#userlist").load("${pageContext.request.contextPath}/workflow/config/queryUsers.page?user_worknumber="+user_worknumber+"&user_realname="+user_realname+"&user_name="+user_name); 
		window.setTimeout("aotoChooseUserList()",200);
	}
	function doreset(){
		$("#user_realname").val("");
		$("#user_worknumber").val("");
		$("#user_name").val("");
	}
	function doQueryList(){
		if(event.keyCode == 13){
			queryList();
		}
	}
  function aotoChooseUserList(){
		if($("#select1").find("option").length == 1){
			moveAll($('#select1'),$('#select2'));
		}else{
			/* if($("#select1").find("option").length > 0){
			}else{
				//window.setTimeout("aotoChooseUserList()",100);
			} */
		}
	}
	
	function setUsers(){
		var usernames='' ;
		var user_realnames='';
		var user_worknumbers='';
		var opts = $("#select2").find('option');
		for (var i = 0; i < opts.length; i++) { 
			if(i!=0){
				usernames+=","+opts[i].value;
				user_worknumbers += ","+$(opts[i]).attr("worknumber");
				user_realnames+=","+opts[i].innerHTML;
			}else{
				usernames=opts[i].value;
				user_worknumbers = $(opts[i]).attr("worknumber");
				user_realnames=opts[i].innerHTML;
			}
	     } 
		$("#usernames").val(user_worknumbers);
		$("#user_realnames").val(user_realnames);
	}
	
	
	function submitData(){
		setUsers();
		var node_key = $("#node_key").val();		
		W.updateAfterChoose(node_key, $("#usernames").val(), $("#user_realnames").val() );
		api.close();
	}
	function showOrgInfo(){
		
		var opt = $("#select1").find('option:selected');
		$("#selectDetail").html("工号:"+opt.attr("worknumber")+"<br/>登录名:"+opt.val()+"<br/>组织机构："+opt.attr("orgname")+opt.attr("jobname"));
	}
	function showOrgInfo2(){
		var opt = $("#select2").find('option:selected');
		$("#selectDetail2").html("工号:"+opt.attr("worknumber")+"<br/>登录名:"+opt.val()+"<br/>组织机构："+opt.attr("orgname")+opt.attr("jobname"));
		
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
							throw "已选列表中已存在  "+m.innerHTML;
						
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
		var usernames = $("#candidateName").val();
		if(usernames){
			init(usernames);
		}
		 $("#org_tree").load("${pageContext.request.contextPath}/workflow/taskConfig/task_config_common_org_tree.jsp");
	   });
	
	function init(usernames){
		$("#chooseuserlist").load("${pageContext.request.contextPath}/workflow/queryUsersByworknums.page?worknums="+usernames); 
	}
</script>
<body class="easyui-layout">
	<input type="hidden" value="${usernames}" name="usernames" id="usernames"/>
	<input type="hidden" value="${param.node_key}" name="node_key" id="node_key"/>
	<input type="hidden" value="${user_realnames}" name="user_realnames" id="user_realnames"/>
	<input type="hidden" value="${param.candidateName}" name="candidateName" id="candidateName"/>
	<div region="west" split="true" title="组织结构"
		style="width: 200px; height: 150px; padding1: 1px; overflow: auto;" id="org_tree">
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
										<th>
												姓名：
											</th>
											<td>
												<input id="user_realname" name="user_realname" type="text"
												  onkeyup="doQueryList()" value=""  class="w120" />
											</td>
											
											<th>工号：</th>
											<td><input id="user_worknumber" name="user_worknumber" type="text"
												onkeyup="doQueryList()"	value="" class="w120"/></td>
										<!-- 
										<th>
												姓名：
											</th>
											<td>
												<input id="user_realname" name="user_realname" type="text"
													value="" class="w120" />
											</td>
										 -->
											
											<th>登录名：</th>
											<td><input id="user_name" name="user_name" type="text"
												onkeyup="doQueryList()"	value="" class="w120"/></td>
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
				<div id="userlistContainer1">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="select_table">
						<tr>
							<th width="30%" align="left">待选用户</th>
							<td width="30%">&nbsp;</td>
							<th width="30%" align="left">已选用户</th>
						</tr>
						<tr>
							<td align="left">
								<div id="userlist">
									<select name="select1" size="15" multiple="multiple" id="select1"
										style="width: 220px; height: 250px" onclick="showOrgInfo()"></select>
								</div>	
							<td align="center"><input type="button" name="button"
								id="button" value=">"
								onclick="move($('#select1'),$('#select2'))" /> <br /> <input
								type="button" name="button3" id="button3" value="<"
								onclick="move($('#select2'),$('#select1'))" /> <br /> <input
								type="button" name="button4" id="button4" value=">>"
								onclick="moveAll($('#select1'),$('#select2'))" /> <br /> <input
								type="button" name="button5" id="button5" value="<<"
								onclick="moveAll($('#select2'),$('#select1'))" /></td>
							<td align="left">
							<div id="chooseuserlist">
							  <select name="select2" size="15"
								 multiple="multiple" id="select2" style="width: 220px;height: 250px" onclick="showOrgInfo2()">	
                              </select>
                          </div>
							</td>
						</tr>
						<tr>
							<td colspan="2"><div class="treesearch" id="selectDetail">工号:</br>登录名:</br>组织机构:</div></td>
							<td colspan="2"><div class="treesearch" id="selectDetail2">工号:</br>登录名:</br>组织机构:</div></td>
						</tr>
						<tr>
							<td colspan="3" align="center" height="40"><a href="javascript:submitData()"  class="bt_1"><span>确定</span></a></td>
						</tr>
					</table>
				</div>
			</div>

</body>
</html>