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
$(document).ready(function() {
	
	$("#wait").hide();
	
	queryList();
	
	<%-- 分页查询数据--%>
 	 var url="<%=request.getContextPath()%>/workflow/businessDemo/getBusinessKeyPageList.page";
 		$("#businessKey").autocomplete(url , {
 	         minChars:1,//自动完成激活之前填入的最小字符 
 	         width:175,
 	         max:20,// 最多显示多少条记录
 	         dataType:"json",
 	         // 扩展字段
 	         extraParams: {    
 	        	businessKey: function(){return $("#businessKey").val()}
 	         },
 	         scroll : true,//是否显示滚动条
 	         matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示 
 	         cacheLength : 30, //缓存结果队列长度 
 	         valuefiled:"business_key_",// 默认显示实体哪个属性
 	         //下拉列表格式   
 	         formatItem: function(row, i, max) {
 	         	return "<I>"+row.business_key_+"</I>";
 	         },
 	         //与这些相匹配
 	         formatMatch: function(row, i, max) {
 	              return row;
 	         }
 	     }).result(function(event,row,formatted){//通过result函数可进对数据进行其他操作
 	    	 // $("#idCard").val(row.card);
 	     });

	
	 var url="<%=request.getContextPath()%>/workflow/businessDemo/getBusinessKeyList.page";
        $.post(url,{},function(data){
            initAutoComplete(data);
        },"json");
    

});

function initAutoComplete(data){
	
	$("#businessKey1").autocomplete(data , {
         minChars:1,
         width:175,
         dataType:"json",
         matchContains: true,
         cacheLength : 10,
         valuefiled:"business_key_",// 默认显示实体哪个属性
         max:30,
         //下拉列表格式   
         formatItem: function(row, i, max) {
         	return "<I>"+row.business_key_+"</I> -- <I>"+row.proc_inst_id_+"</I>";
         },
         //与这些相匹配
         formatMatch: function(row, i, max) {
              return row;
         }
     });
 }
 
//调整(修改节点处理人)
function toUdpNodeAssignee(businessKey,processKey) {
	var url=encodeURI("<%=request.getContextPath()%>/workflow/businessDemo/toNodeAssignee.page"
			+ "?businessKey="+businessKey
			+ "&processKey="+processKey);
	$.dialog({ title:'调整节点处理人',width:500,height:350, content:'url:'+url});
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
		var url= encodeURI("<%=request.getContextPath()%>/workflow/businessDemo/toworkflowMain.page?businessKey="+businessKey
		+"&processKey="+processKey);
		
		$.dialog({ id:'iframeNewId', title:'申请流程页面('+businessKey+')',width:1000,height:700, content:'url:'+url});  
	}else {
		
		var url=encodeURI("<%=request.getContextPath()%>/workflow/businessDemo/toViewTask.page?businessKey="+businessKey);
		
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
			      	业务单号<input id="businessKey" name="businessKey" type="text" class="input1" style="width: 165px;" value="" />
			      	业务单号<input id="businessKey1" name="businessKey1" type="text" class="input1" style="width: 165px;" value="" />
			    </div> 
			    <a href="javascript:void" class="bt_search" onclick="queryList();"><span>查询</span></a>
	    	</td></tr>
	    </table>
  	</div>
  
	<div class="title_1">
		演示列表
		
		<div ><a href="javascript:void" class="bt_sany" onclick="toSelectProc();">新增</a></div>
		
	</div>
		<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />		
	
	<div id="demoContainer" style="overflow:auto"></div>
			
			
</div>
	
</body>
</html>
