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

$(document).ready(function(){
	
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
			queryList();
		}
	});
	$("#select1").dblclick( function () { move($('#select1'),$('#select2')); });
	$("#select2").dblclick( function () { 	move($('#select2'),$('#select1')); });
	
	var usernames = $("#candidateName").val();
	if(usernames){
		init(usernames);
	}
	$("#org_tree").load("${pageContext.request.contextPath}/workflow/taskConfig/task_config_common_org_tree.jsp");

});
var api = frameElement.api, W = api.opener;
function query(orgId){
	$("a").removeClass("a_bg_color");
	$("a[name='"+orgId+"']").addClass("a_bg_color"); 
	var _url = '<%=request.getContextPath()%>/workflowBusiness/choose/queryUsersByOrgId.page';
	var datas = {		orgId : orgId	};   
	ajaxGet(_url,datas,loadSelect1);
}

function ajaxGet(_url,datas,func,func1){
	$.ajax({
		url:_url,
		type:'POST',
		dataType:'json',
		data:datas ,
		error:function(){
			alert('错误请求！');
		},
		success:function(data){
			if(data && data!=''){
				if(typeof  func == "function"){
					func(data,func1);
	 			}
			}else{
				alert("无查询结果");
			}
		}
	});
}

function loadSelect1(data,func){
	loadSelect($("#select1"),data,func)
}
function loadSelect2(data,func){
	loadSelect($("#select2"),data,func)
}

function loadSelect(_select,data,func){
	_select.empty();
	$.each( data, function(i, n){
		_select.append("<option value='"+n.userName+"' orgname='"+n.orgName+"' jobname='"	+n.job_name
				+"' userName='"+n.userName+"' userRealname='"+ n.userRealname+"'>"+n.userRealname+"("+n.job_name+")</option>");
	});
	if(typeof  func == "function"){		func();	}
}

function containSpecial( s ) {  
	var containSpecial = RegExp(/[(\ )(\_)(\%)]+/);      
  return ( containSpecial.test(s) );      
}

function queryList(){
	var userName = $("#userName").val();
	if(userName==''){
		alert("请输入查询条件");
		return;
	}
	
	if(containSpecial(userName)){
		alert('查询字符串含有非法字符集,请检查输入条件！');
		return;
	}
	
	var _url = '<%=request.getContextPath()%>/workflowBusiness/choose/queryUsers.page';
	var datas = {"userName": userName,"pagesize":$("#rownums").val()};   
	ajaxGet(_url,datas,loadSelect1,aotoChooseUserList);
}
function doreset(){
	$("#userName").val("");
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

/*
function upMove(){
	
	if (null == $("#select2").val()) {
		alert(1);
		return false;
	}
	
	if ($("#select2 option:selected").length > 1) {
		alert('请选择一项移动');
		return false;
	}
	
	var optionIndex = $("#select2").get(0).selectedIndex;
	
	if (optionIndex > 0) {
		$("#select2 option:selected").insertBefore($("#select2 option:selected").prev("option"));
	}
	
}

function downMove(){
	if (null == $("#select2").val()) {
		alert(2);
		return false;
	}
	
	if ($("#select2 option:selected").length > 1) {
		alert('请选择一项移动');
		return false;
	}
	
	var optionLength = $("#select2")[0].options.length;
	var optionIndex = $("#select2").get(0).selectedIndex;
	
	if (optionIndex < optionLength -1) {
		$("#select2 option:selected").insertAfter($("#select2 option:selected").next("option"));
	}
}
**/

function setUsers(){
	//var usernames='' ;
	var userRealnames='';
	var usernames='';
	var opts = $("#select2").find('option');
	for (var i = 0; i < opts.length; i++) { 
		if(i!=0){
			usernames+=","+opts[i].value;
			userRealnames += ","+$(opts[i]).attr("userRealname");
		}else{
			usernames=opts[i].value;
			userRealnames = $(opts[i]).attr("userRealname");
		}
     } 
	$("#usernames").val(usernames);
	$("#userRealnames").val(userRealnames);
}


function submitData(){
	setUsers();
	var node_key = $("#node_key").val();
	var index = $("#index").val();
	if(index!=null && index==1){
		if($("#usernames").val().indexOf(',')!=-1){
			alert("只能选择一个用户");
			return;
		}
	}
	if(typeof  W.${param.callBackFunc} == "function"){
		W.${param.callBackFunc}($("#usernames").val(), $("#userRealnames").val(), node_key);
	}
	api.close();
}
function showOrgInfo(){
	var opt = $("#select1").find('option:selected');
	if(opt&&opt.val()){
		$("#selectDetail").html("工号:"+opt.val()+"<br/>登录名:"+opt.attr("userName")+"<br/>组织机构："+opt.attr("orgname"));
	}
}
function showOrgInfo2(){
	var opt = $("#select2").find('option:selected');
	if(opt&&opt.val()){
		$("#selectDetail2").html("工号:"+opt.val()+"<br/>登录名:"+opt.attr("userName")+"<br/>组织机构："+opt.attr("orgname"));
	}
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
			ObjTarget.find("option:selected").attr("selected",false);
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

function init(usernames){
	var _url = '<%=request.getContextPath()%>/workflowBusiness/choose/queryUsersByUsernames.page';
	var datas = {		usernames : usernames	};   
	ajaxGet(_url,datas,loadSelect2);
}
</script>
<body class="easyui-layout">
	<input type="hidden" value="${usernames}" name="usernames" id="usernames"/>
	<input type="hidden" value="${user_realnames}" name="userRealnames" id="userRealnames"/>
	<input type="hidden" value="${param.candidateName}" name="candidateName" id="candidateName"/>
	<input type="hidden" value="${param.node_key}" name="node_key" id="node_key"/>
	<input type="hidden" value="${param.index}" name="index" id="index"/>
	
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
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="left_box"></td>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
									<tr>
										<td><input id="userName" name="userName" type="text"
											 value="工号 / 姓名 / 域账号" onfocus="this.value='';" size="50"/> </td>
										<th>显示</th>
										<td>
											<select class="w50" id="rownums">
												<option value="50" selected>50</option>
												<option value="100">100</option>
												<option value="200">200</option>
											</select>
										</td>
										<td>
											<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span>查询</span></a>
											<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span></a>
											<input type="reset" id="reset" style="display:none"/>
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
			
			<div id="userlistContainer1">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="select_table">
					<tr>
						<th width="30%" align="left">待选用户</th>
						<td width="30%">&nbsp;</td>
						<th width="30%" align="left">已选用户</th>
					</tr>
					<tr>
						<td align="left">
							<div id="userlist">
								<select name="select1" size="15" multiple="multiple" id="select1"
									style="width: 280px; height: 250px" onclick="showOrgInfo()"></select>
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
								 multiple="multiple" id="select2" style="width: 280px;height: 250px" onclick="showOrgInfo2()">	
	                             </select>
	                        </div>
						</td>
						<td align="left">
							<input type="button" style="width: 30px; height: 40px; margin: 0px 10px 30px" value="∧" id="btnMoveUp" title="快速鍵: alt+向上" /> 
							<input type="button" style="width: 30px; height: 40px; margin: 0px 10px 30px" value="∨" id="btnMoveDown" title="快速鍵: alt+向下"/> 
						</td>
					</tr>
					<tr>
						<td ><div  id="selectDetail">工号:</br>登录名:</br>组织机构:</div></td>
						<td></td>
						<td colspan="2"><div  id="selectDetail2">工号:</br>登录名:</br>组织机构:</div></td>
					</tr>
					<tr>
						<td colspan="3" align="center" height="40"><a href="javascript:submitData()"  class="bt_1"><span>确定</span></a></td>
					</tr>
				</table>
			</div>
	</div>

</body>
</html>