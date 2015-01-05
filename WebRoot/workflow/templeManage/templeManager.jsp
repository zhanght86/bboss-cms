<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：模板管理
	作者：谭湘
	版本：1.0
	日期：2014-06-09
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>模板管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">

$(document).ready(function() {

	$("#wait").hide();
       		
	queryList();   
       		
    $('#addButton').click(function() {
    	editTemple("","0");
    });
    
    $('#delBatchButton').click(function() {
    	delTemples();
 	});
    
    $('#paramButton').click(function() {
    	paramTempate();
 	});
           	
});
       
//加载实时任务列表数据  
function queryList(){
	var templeTitle = $("#templeTitle").val();
	var templeType = $("#templeType").val();
	var createTime1 = $("#createTime1").val();
	var createTime2 = $("#createTime2").val();
	
    $("#templeContainer").load("<%=request.getContextPath()%>/workflow/templeManage/queryTempleData.page #templeContent", 
    	{"templeTitle":templeTitle, "templeType":templeType,"createTime1":createTime1,"createTime2":createTime2},
    	function(){loadjs();});
}

function doreset(){
	$("#reset").click();
}

function editTemple(templeId,state) {
	var url="<%=request.getContextPath()%>/workflow/templeManage/editTemple.page?templeId="+templeId+"&state="+state;
	
	var title = '';
	if (state == '0') {
		title = '模板编辑';
	}else {
		title = '明细查看';
	}
	$.dialog({ title:title,width:1000,height:600, content:'url:'+url});
}

// 参数管理
function paramTempate() {
	var url="<%=request.getContextPath()%>/params/showParams.page?paramId=msg&paramType=msg&handler=sys.msg.paramshandler";
	
	$.dialog({ title:"参数管理",width:1000,height:600, content:'url:'+url});
}

function delTemples () {
    
	var ids="";
	
	$("#tb tr:gt(0)").each(function() {
		if ($(this).find("#CK").get(0).checked == true) {
             ids=ids+$(this).find("#CK").val()+",";
        }
    });
	
    if(ids==""){
       $.dialog.alert('请选择需要删除的模板！');
       return false;
    }
    
    $.dialog.confirm('确定要删除吗？', function(){
     	$.ajax({
	 	type: "POST",
	 	url : "<%=request.getContextPath()%>/workflow/templeManage/delTemples.page",
	 	data :{"templeIds":ids},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data == 'fail') {
				$.dialog.alert("模板已被引用不能被删除！",function(){});
			}else if (data == 'success') {
				queryList();
			}else {
				$.dialog.alert("删除模板出错："+data,function(){});
			}
		}	
	 });
	},function(){
	  		
	});         
    
}

</script>
</head>

<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
		<sany:menupath />
		
		<div id="rightContentDiv">
			
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
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
										<tr>
											<th>模板名称：</th>
											<td><input id="templeTitle" name="templeTitle" type="text" class="w120"/></td>
											<th>模板类型：</th>
											<td>
												<select id="templeType" name="templeType" class="select1" style="width: 125px;">
												    <option value="" selected>全部</option>
													<option value="0">短信</option>
													<option value="1">邮件</option>
												</select>
											</td>
											<th>创建时间：</th>
											<td>
												<input id="createTime1" name="createTime1" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
												 ~<input id="createTime2" name="createTime2" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
											</td>
											<td style="text-align:right">
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
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
			
			<div class="title_box">
				<div class="rightbtn">
					<a href="javascript:void" class="bt_small" id="paramButton"><span>参数管理</span></a>
					<a href="javascript:void" class="bt_small" id="addButton"><span>新增</span></a>
					<a href="javascript:void" class="bt_small" id="delBatchButton"><span>删除</span></a>
				</div>
					
				<strong>模板列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
			</div>
			
			<div id="templeContainer" style="overflow:auto"></div>
		</div>
	</div>
</body>
</html>
