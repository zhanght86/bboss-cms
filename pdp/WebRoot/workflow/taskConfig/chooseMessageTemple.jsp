<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择消息模板</title>
<%@ include file="/common/jsp/css.jsp"%>
<link href="stylesheet/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/include/ckeditor/ckeditor.js"></script>

</head>
<%
  	request.setAttribute("templeType", request.getParameter("templeType"));
	request.setAttribute("templeId", request.getParameter("templeId"));
 %>
 
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
var editor;

$(document).ready(function() {
	
	getTempleDate();
	
	$("#templeContent").attr("disabled","disabled");
	
	<%-- 
	if ('${templeType}' == '1') {
		CKEDITOR.replace( 'templeContent', { height:'330',width:'650' } );
		
		CKEDITOR.on( 'instanceReady', function( ev ) {
			editor = ev.editor;
			editor.setReadOnly(true);
		});
	}else {
		$("#templeContent").attr("disabled","disabled");
	}
	--%>
});

var treeData = null;

function getTempleDate(){
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/templeManage/getTempleList.page?templeType=${templeType}",
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data) {
				treeData = data;
			} else {
				$.dialog.alert('查询模板列表异常');
			}
		}	
	 });
	
	initTreeModule('');
}

function initTreeModule(temple_query){
	var treeModuleHtml = "";
	if(treeData){
		for(var i=0; i<treeData.length; i++){
			if(temple_query!=null && temple_query!=""){
				if(treeData[i].templeTitle.toLowerCase().indexOf(temple_query.toLowerCase()) >= 0 ){
					treeModuleHtml += 
    					"<li id=\""+treeData[i].templeId+"\"><a href=\"#\" onclick=\"doClickTreeNode('"+treeData[i].templeId+"')\" >"+treeData[i].templeTitle
    					+"</a><span id=\"span_"+treeData[i].templeId+"\" style='display:none;'>"+treeData[i].templeContent+"</span>"
    					+"<span id=\"title_"+treeData[i].templeId+"\" style='display:none;'>"+treeData[i].templeTitle+"</span></li>";
				}
			}else{
				
				treeModuleHtml += 
					"<li id=\""+treeData[i].templeId+"\"><a href=\"#\" onclick=\"doClickTreeNode('"+treeData[i].templeId+"')\" >"+treeData[i].templeTitle
					+"</a><span id=\"span_"+treeData[i].templeId+"\" style='display:none;'>"+treeData[i].templeContent+"</span>"
					+"<span id=\"title_"+treeData[i].templeId+"\" style='display:none;'>"+treeData[i].templeTitle+"</span></li>";
			}
		}
	}
	
	treeModuleHtml += "<li id='nullLi'><a href='#' onclick=\"doClickTreeNode('nullLi')\" >无</a>"
		+"<span id='span_nullLi' style='display:none;'></span>"
		+"<span id='title_nullLi' style='display:none;'></span></li>";
	
	$("#temple_module").html(treeModuleHtml);
	
	if(temple_query == "" && '${templeId}' != ''){
		doClickTreeNode('${templeId}');
	}
	
}

function doClickTreeNode(temple_id){
	
	$("#temple_module").find("li").removeAttr("class");
	$("#"+temple_id).attr("class","select_links");
	
	$("#templeContent").html($("#span_"+temple_id).html());
	
	if ('nullLi' != temple_id) {
		$("#templeIds").val(temple_id);
	}else {
		$("#templeIds").val('');
	}
	$("#templeTile").val($("#title_"+temple_id).text());

}

function sortTempleTree(){
	var temple_query = $("#temple_query").val();
	initTreeModule(temple_query);
}

function submitData(){
	//短信模板
	if ('${templeType}' == '0') {
		//W.$("#${templeId}_messagetempleid").val($("#templeIds").val());
		//W.$("#${templeId}_messagetitle").val($("#templeTile").val());
		
		W.$("#messagetempleid").val($("#templeIds").val());
		W.$("#messagetitle").val($("#templeTile").val());
		
	}else {//邮件模板
		//W.$("#${templeId}_emailtempleid").val($("#templeIds").val());
		//W.$("#${templeId}_eamiltitle").val($("#templeTile").val());
		
		W.$("#emailtempleid").val($("#templeIds").val());
		W.$("#eamiltitle").val($("#templeTile").val());
	}
	api.close();
}

</script>
<body class="easyui-layout">
	<input type="hidden" id="templeTile" />
	<input type="hidden" id="templeIds" />
	
	<div region="west" split="true" title=""
		style="width: 230px; height: 400px; overflow: auto;" id="org_tree">
		<div class="left_menu" style="width:200px; height: 340px;">
		    <ul>
		    	<li class="select_links">
		    		<a href="javascript:void(0)">模板查询：</a><input type="input" style="width:100px;" name="temple_query" id="temple_query" onKeyUp="sortTempleTree()" />
		    		<ul style="display: block;" id="temple_module">
		    			
		    		</ul>
		    	</li>
		    </ul>
		</div>
	</div>
	
	<div region="center" title="" split="true"
		style="height: 400px; padding: 5px; background: #efefef;">
		
		<textarea rows="8" cols="50" id="templeContent" name="templeContent" 
		style="width: 650px;font-size: 12px;height:420px;"></textarea>
			
	</div>
	
	<div region="south" title="" split="false"
		style="height: 50px; padding: 10px; background: #efefef;">
		
		<div align="center">
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="submitData()"><span>确定</span></a>
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
		</div>	
	</div>
		
</body>
</html>