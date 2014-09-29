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

function doreset(){
	$("#reset").click();
}

function query(orgId){
	$("a").removeClass("a_bg_color");
	$("a[name='"+orgId+"']").addClass("a_bg_color"); 
		
	doreset();
		
	queryList(orgId);
		
}

function containSpecial( s ) {  
	var containSpecial = RegExp(/[(\ )(\_)(\%)]+/);      
  return ( containSpecial.test(s) );      
}
	
function queryList(orgId){
	var user_name = $("#userName").val();
	
	if(orgId == '' && user_name==''){
		alert("请输入查询条件");
		return;
	}
	
	if(containSpecial(user_name)){
		alert('查询字符串含有非法字符集,请检查输入条件！');
		return;
	}
		
	$.ajax({
		
		url: "<%=request.getContextPath()%>/workflow/config/queryUsersToJson.page",
		type: "post",
		data :{"org_id":orgId,"user_name":user_name,"pagesize":$("#rownums").val()},
		dataType:"json",			
		success: function(data){
			
			if (data != null) {
				$("#select1").find('option').remove();
				
				var optionHtml = '';
				for(var i=0; i< data.length; i++){
					var isExit =SelectIsExitItem($("#select2"),data[i].user_name);
					
					 if (!isExit) {
						optionHtml += "<option value='"+data[i].user_name+"'>"+data[i].user_realname+"</option>";
					 }
				}
				
				$("#select1").append(optionHtml);
				
				if ((orgId == null || orgId == '') && data.length == 1) {
					$("#select1").find('option').attr('selected','selected');
					move($('#select1'),$('#select2'));
				}
			}else {
				alert("无查询结果");
			}
		}
	});
	
}
	
function setUsers(){
	var usernames='' ;
	var user_realnames='';
	for (var i = 0; i < $("#select2").find('option').length; i++) { 
		if(i!=0){
			usernames+=","+$("#select2").find('option')[i].value;
			user_realnames+=","+$("#select2").find('option')[i].innerHTML;
		}else{
			usernames=$("#select2").find('option')[i].value;
			user_realnames=$("#select2").find('option')[i].innerHTML;
		}
	} 
	$("#usernames").val(usernames);
	$("#user_realnames").val(user_realnames);
}
	
	
function submitData(){
	setUsers();
	var node_key = $("#node_key").val();
	W.$("#"+node_key+"_users_id").val($("#usernames").val());
	W.$("#"+node_key+"_users_name").val($("#user_realnames").val());
	api.close();
}

function showOrgInfo(){
	var detail=$("#select1").find('option:selected').val();
	if(detail)
	$("#selectDetail").html("工号:"+$("#"+detail+"user_worknumber").val()+"<br/>登陆名:"+detail+"<br/>组织机构："+$("#"+detail+"org_name").val()+$("#"+detail+"job_name").val());
}
	
	 
/* 添加选择的项 */
function move(ObjSource, ObjTarget) {
	var sourceName =ObjSource.attr("id");
	var targetName =ObjTarget.attr("id");
	
	if (ObjSource.val() == null)
		return;
	try{
		ObjTarget.find('option').attr('selected',false);
		
		$.each(ObjSource.find('option:selected'), function(i, n) {
			
				if(SelectIsExitItem(ObjTarget,n.value) ){
					if(sourceName != 'select2')
						throw "已选列表中已存在  "+n.innerHTML;
								
				}else{
					ObjTarget.append(n);		
			    };
		});
		ObjSource.find('option:selected').remove();
	}catch(e) {    
	    alert(e);    
	}	
	
	// 工号/登录名/组织机构两边切换
	selectOption(targetName);
	selectOption(sourceName);
}

/* 添加全部 */
function moveAll(ObjSource, ObjTarget) {
	var sourceName =ObjSource.attr("id");
	var targetName =ObjTarget.attr("id");
	try{
		ObjTarget.find('option').attr('selected',false);
		ObjSource.find('option').attr('selected',true);
		
		$.each(ObjSource.find('option'), function(i, m) {	
				if(SelectIsExitItem(ObjTarget,m.value) ){
					 if( sourceName != 'select2')
						throw "已选列表中已存在  "+n.innerHTML;
					
				}else{
					ObjTarget.append(m);
				}
		});
		
		//ObjTarget.append(ObjSource.html());
		ObjSource.empty();
	}catch(e) {    
	    alert(e);    
	}	
	
	// 工号/登录名/组织机构两边切换
	selectOption(targetName);
	selectOption(sourceName);
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

function getUserInfo(userName,obj){
	$.ajax({
		
		url: "<%=request.getContextPath()%>/workflow/config/getUserInfo.page",
		type: "post",
		data :{"userName":userName},
		dataType:"json",			
		success: function(data){
			if (data != null) {
				obj.html("工号:"+data.user_worknumber+"("+data.job_name+")<br/>登陆名:"+data.user_name+"<br/>组织机构："+data.org_name);
			}else {
				obj.html("工号:</br>登陆名:</br>组织机构:</div>");
			}
		}
	});
}

function selectOption(selector){
	
	var detail=$("#"+selector).find('option:selected').val();
	getUserInfo(detail,$("#"+selector+"Detail"));
}
	
$(document).ready(function() {
	
	$("#btnMoveUp,#btnMoveDown").click(function() {   
        var $opt = $("#select2 option:selected");   
        if (!$opt.length) return;   
        if ($opt.length > 1 ) {
        	alert('请选择一项移动');
        	return;   
        }
        if (this.id == "btnMoveUp") 
            $opt.prev().before($opt);   
        else 
            $opt.next().after($opt);   
        });   
	
    //按Alt加上下鍵也可以移動   
    $("#select2").keydown(function(evt) {   
        if (!evt.altKey) return;   
        var k = evt.which;
        if (k == 38) { 
        	$("#btnMoveUp").click(); 
        	return false; 
        }else { 
        	if (k == 40) { 
        		$("#btnMoveDown").click(); return false; 
        	} 
        }
    });
		 
	$("#userName").keydown(function(event){
		if(event.keyCode == 13){
			queryList('');
		}
	});
	
	 $("#org_tree").load("../taskConfig/task_config_common_org_tree.jsp");
});
</script>
<body class="easyui-layout">
	<input type="hidden" value="${usernames }" name="usernames" id="usernames"/>
	<input type="hidden" value="${node_key }" name="node_key" id="node_key"/>
	<input type="hidden" value="${user_realnames }" name="user_realnames" id="user_realnames"/>
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
											<td><input id="userName" name="userName" type="text"
											 value="工号 / 姓名 / 域账号" onfocus="this.value='';" size="50"/></td>
											<th>显示</th>
											<td>
												<select class="w50" id="rownums">
													<option value="50" selected>50</option>
													<option value="100">100</option>
													<option value="200">200</option>
												</select>
											</td>
											<th>数据</th>
											<td>
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList('')"><span>查询</span>
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
							<th width="40%">待选用户</th>
							<td width="20%">&nbsp;</td>
							<th width="40%">已选用户</th>
						</tr>
						<tr>
							<td align="left">
								<div id="userlist">
									<select name="select1" size="15" multiple="multiple" id="select1"
										style="width: 220px; height: 400px" onclick="selectOption('select1');">
									</select>
								</div>
							</td>	
							<td align="center"><input type="button" name="button"
								id="button" value=">"
								onclick="move($('#select1'),$('#select2'))" /> <br /> <input
								type="button" name="button3" id="button3" value="<"
								onclick="move($('#select2'),$('#select1'))" /> <br /> <input
								type="button" name="button4" id="button4" value=">>"
								onclick="moveAll($('#select1'),$('#select2'))" /> <br /> <input
								type="button" name="button5" id="button5" value="<<"
								onclick="moveAll($('#select2'),$('#select1'))" />
							</td>
							<td align="right">
								<select name="select2" size="25" onclick="selectOption('select2');"
								multiple="multiple" id="select2" style="width: 220px;height: 400px">
									<pg:list requestKey="chooseuserlist">
										<option value="<pg:cell colName='user_name'/>"><pg:cell colName="user_realname"/></option>
									</pg:list>	
								</select>
							</td>
							<td align="left">
								<input type="button" style="width: 30px; height: 40px; margin: 0px 10px 30px" value="∧" id="btnMoveUp" title="快速鍵: alt+向上" /> 
								<input type="button" style="width: 30px; height: 40px; margin: 0px 10px 30px" value="∨" id="btnMoveDown" title="快速鍵: alt+向下"/> 
							</td>
						</tr>
						<tr>
							<td colspan="2"><div class="treesearch" id="select1Detail">工号:</br>登陆名:</br>组织机构:</div></td>
							<td colspan="2"><div class="treesearch" id="select2Detail">工号:</br>登陆名:</br>组织机构:</div></td>
						</tr>
						<tr>
							<td colspan="3" align="center" height="40"><a href="javascript:submitData()"  class="bt_1"><span>确定</span></a></td>
						</tr>
					</table>
				</div>
			</div>

</body>
</html>