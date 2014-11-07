<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>业务演示</title>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/html3/js/tab.js"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.css" />

<script type="text/javascript">
var ctx			=	"${pageContext.request.contextPath}";	
$(document).ready(function() {
	
	$("#wait").hide();
	
	queryList(); 
	
	 $("#businessKey").focus(function(){
		 var url="<%=request.getContextPath()%>/workflow/businessDemo/getBusinessKeyList.page";
         $.post(url,{},function(data){
             initAutoComplete(data);
         },"json");
     });

});

function initAutoComplete(data){
	
	$("#businessKey").autocomplete(data , {
         minChars:1,
         width:165,
         dataType:"json",
         matchContains: true,
         cacheLength : 10,
         matchSubset : true,
         formatItem: function(row, i, max) {
         	return "<I>"+row[0]+"</I>";
         },
         formatMatch: function(row, i, max) {
              return row[0];
         },
         formatResult: function(row) {
              return row[0];
         }
     });
 }

// 选择流程
function toSelectProc() {
	
	var url="<%=request.getContextPath()%>/workflowdemo/selectProcess.jsp?businessKey="
	+"&processKey=${processKey}";
	
	$.dialog({ id:'iframeNewId1', title:'选择流程类型',width:300,height:400, content:'url:'+url});  
	
}

//开启流程实例
function toStartProc(processKey) {
	
	var url="<%=request.getContextPath()%>/workflow/businessDemo/toworkflowMain.page?businessKey="
	+"&processKey="+processKey;
	
	$.dialog({ id:'iframeNewId', title:'申请流程页面',width:1000,height:700, content:'url:'+url});  
}


//加载实时任务列表数据  
function queryList(){
	
    $("#demoContainer").load("<%=request.getContextPath()%>/workflow/businessDemo/queryDemoData.page #demoContent", 
    	{"businessKey":$("#businessKey").val(),"processKey":"${processKey}"},function(){});
    
}

// 处理业务单号
function dealBusiness(businessKey,businessState,processKey){
	if (businessState == '0') {
		var url="<%=request.getContextPath()%>/workflow/businessDemo/toworkflowMain.page?businessKey="+businessKey
		+"&processKey="+processKey;
		
		$.dialog({ id:'iframeNewId', title:'申请流程页面('+businessKey+')',width:1000,height:700, content:'url:'+url});  
	}else {
		
		var url="<%=request.getContextPath()%>/workflow/businessDemo/toViewTask.page?businessKey="+businessKey;
		
		if (businessState == '1') {
			
			$.dialog({ id:'iframeNewId', title:'处理任务页面('+businessKey+')',width:1000,height:700, content:'url:'+url});  
			
		}else {
			
			$.dialog({ id:'iframeNewId', title:'处理任务页面('+businessKey+')',width:1000,height:700, content:'url:'+url}); 
			
		}
	}
}

function modifyQueryData(){
	$("#demoContainer").load("<%=request.getContextPath()%>/workflow/businessDemo/queryDemoData.page?"+$("#querystring").val()+" #demoContent",function(){loadjs()});
}
	
function doreset(){
	$("#reset").click();
}

</script>
</head>

<body>

<div class="content_box" >
	
	<sany:menupath menuid="processDemo"/>
		
	<div class="search">
		<table>
			<tr><td>
			    <div class="sany_li2">
			      	业务单号<input id="businessKey" name="businessKey" type="text"  class="input1" value="" />
			    </div> 
			    <a href="#" class="bt_search" onclick="queryList();"><span>查询</span></a>
	    	</td></tr>
	    </table>
  	</div>
  
	<div class="title_1">
		演示列表
		
		<div ><a href="#" class="bt_sany" onclick="toSelectProc();">新增</a></div>
		
	</div>
		<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />		
	
	<div id="demoContainer" style="overflow:auto"></div>
			
			
</div>
	
</body>
</html>
