<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：流程定义管理
	作者：尹标平
	版本：1.0
	日期：2012-03-23
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程定义管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<style type="text/css">
<!--
  #rightContentDiv{
	position:absolute;
	margin-left:475px;
	margin-right:8px;
	width: 80.5%;	   
  }
  
  #leftContentDiv{
	width:450px;
	position:absolute;
	left:0px;
	padding-left:20px;  	   
  }

-->
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
       $(document).ready(function() {
      	 $("#wait").hide();
       	   queryList();     
           $('#queryButton').click(function() {
           	queryData();
           });
           
           $("#businessType").combotree({
       		url:"../businesstype/showComboxBusinessTree.page"
       		});
           
           getTreeDate();
       	   initTreeModule("");
       	   initProcdefTable();
           
       });
       
     function queryList(){	
		//$("#custombackContainer").load("queryProcessDefs.page #customContent",function(){loadjs()});
		var processKey = $("#processKey").val();
		var resourceName = $("#resourceName").val();
		var businesstype_id = $("#businessType").combotree('getValue');
		var wf_app_id = $("#wf_app_id").val();
		var processChoose = $("#processChoose").val();
		var wf_app_name = "";
		if($("#wf_app_name") && $("#wf_app_name").length > 0){
			wf_app_name = $("#wf_app_name").val();
		}
    	 $("#custombackContainer").load("queryProcessDefs.page #customContent", 
    			 {processKey:processKey, resourceName:resourceName,businesstype_id:businesstype_id,wf_app_id:wf_app_id,wf_app_name:wf_app_name,processChoose:processChoose },
    			 function(){loadjs();});
	}
	 function exportExcel(){	 
	 	var bm=$("#bm_query").val();
		var app_name_en=$("#app_name_en_query").val();
		var app_name=$("#app_name_query").val();
		var soft_level=$("#soft_level_query").val();
		var state=$("#state_query").val();
		var rd_type=$("#rd_type_query").val();
		var excelType="";
		if($("#excelType").attr("checked")){
			excelType=0;
		}
		else{
			excelType=1;
		} 	
		if(containSpecial(bm)|| containSpecial(app_name_en)||containSpecial(app_name)){
			alert("查询字符串含有非法字符集,请检查输入条件");
			return;
		}
		
		 window.location.href="exportExcel.page?bm="+encodeURI(encodeURI(bm))+"&app_name="+encodeURI(encodeURI(app_name))+"&app_name_en="+encodeURI(encodeURI(app_name_en))
    		+"&soft_level="+soft_level+"&state="+state+"&rd_type="+rd_type+"&excelType="+excelType;
   
		 return;
	 
	}	
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	
	function activateProcess(id) {
		$.ajax({
	 	 	type: "POST",
			url : "activateProcess.page",
			data :{"processId":id},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if ("success" == data) {
					$.dialog.alert('操作成功！');
					modifyQueryData();
				} else {
					$.dialog.alert('操作失败！当前流程已经为启动状态，无法重复操作');
				}
			}	
		 });
	}
	function nodelist(key){
 	   var url="<%=request.getContextPath()%>/test/showConfigActivitiNodeList.page?processKey="+key;
	 	 $.dialog({ id:'iframeNewId', title:'节点配置',width:740,height:560, content:'url:'+url});   	
    }
	function suspendProcess(id) {
		$.ajax({
	 	 	type: "POST",
			url : "suspendProcess.page",
			data :{"processId":id},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if ("success" == data) {
					$.dialog.alert('操作成功！');
					modifyQueryData();
				} else {
					$.dialog.alert('操作失败！当前流程已经为停止状态，无法重复操作');
				}
			}	
		 });
	}
	
	function viewHistoryVersion(name) {
		var url="<%=request.getContextPath()%>/workflow/repository/queryProcessHisVer.page?processName="+name;
		$.dialog({ title:'历史版本信息',width:740,height:560, content:'url:'+url});   		
	}
	
	function viewProcessInfo(processKey) {
		
		var url="<%=request.getContextPath()%>/workflow/repository/viewProcessInfo.page?processKey="+processKey;
		$.dialog({ title:'查看流程信息-'+processKey,width:1100,height:620, content:'url:'+url});   
	}
	
    function doreset(){
    	
	   	$("#reset").click();
    }
    
	function delBatch(){
		var ids="";
		 $("#tb tr:gt(0)").each(function() {
		 
                   if ($(this).find("#CK").get(0).checked == true) {
                       ids=ids+$(this).find("#key").val()+",";
                     	}
              });
              if(ids==""){
               $.dialog.alert('请选择需要删除的记录！');
               return false;
              }
              
            $.dialog.confirm('确定要删除记录吗？，删除后将不可恢复', function(){
            	$.ajax({
			 	 	type: "POST",
					url : "deleteDeploymentCascade.page",
					data :{"processDeploymentids":ids},
					dataType : 'json',
					async:false,
					beforeSend: function(XMLHttpRequest){
						 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
						},
					success : function(data){
				 		modifyQueryData();
				 		//alert("成功删除记录");
					}	
				 });
             },function(){
             		
             });         
	}

	
	var treeData = null;
    
    function getTreeDate(){
    	$.ajax({
	 	 	type: "POST",
			url : "getWfAppData.page",
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data) {
					treeData = data;
				} else {
					$.dialog.alert('查询应用菜单异常');
				}
			}	
		 });
    }
    
    function initTreeModule(app_query){
    	var treeModuleHtml = "<option value=\"\">请选择</option>";
    	if(treeData){
    		var seq = 1;
    		for(var i=0; i<treeData.length; i++){
    			
    			if(treeData[i].sso_url != "" && treeData[i].sso_url != null){
					
				}else{

					treeModuleHtml += "<option value=\""+treeData[i].id+"\">"+treeData[i].system_name+"</option>";
						seq++;	
				}
    		}
    	}
    	
    	$("#wf_app_id").html(treeModuleHtml);
    }
    
    function sortAppTree(){
    	var app_query = $("#app_query").val();
    	initTreeModule(app_query);
    }
    
    function doClickTreeNode(app_id,selectedNode){
    	
    	$("#app_tree_module").find("li").removeAttr("class");
    	$("#"+app_id).attr("class","select_links");
    	if(app_id == "allAppIdLi"){
    		$("#wf_app_id").val("");
        	
        	$("#app_query_th").html("应用:");
        	$("#wf_app_name_td").html("<input id=\"wf_app_name\" name=\"wf_app_name\" type=\"text\" value=\"\" class=\"w120\" />");
    	}else{
    		$("#wf_app_id").val(app_id);
        	
    		$("#app_query_th").html("&nbsp;");
        	$("#wf_app_name_td").html("&nbsp;");
    	}
    	
    	
    	queryList();
    }
    
	var api = frameElement.api, W = api.opener;
	
	var procData = W.porcData; 
	
	var chooseData = new Array();
	
	var chooseStr = "";
	
	function initProcdefTable(){
		if(procData != null && procData.length > 0){
			
			for(var i=0; i<procData.length; i++){
				
				var data = procData[i];
				
				if(chooseStr.indexOf(data.proc_id+"==")>=0){
					continue;
				}else{
					chooseStr += data.proc_id+"==";
					chooseData.push(data);
				}
				addProcdefTable(data);
			}
		}
	}
	
	function chooseThisData(proc_id,proc_name, business_name, wf_app_name){
		
		var data = {"proc_id":proc_id,"proc_name":proc_name, "business_name":business_name, "wf_app_name":wf_app_name};
		
		chooseData.push(data);
		
		addProcdefTable(addData);
		
	}
	
	//选择行
	function selectedSel(){
		var selectedRadio = $('input[name="CK"]:checked');
		if(selectedRadio==null||selectedRadio.length==0){
			return ;
		}
		$('input[name="CK"]:checked').each(function(){
			var business_name = $(this).parent().find("input[name=business_name]").val();
		    var proc_id = $(this).val();
			var proc_name = $(this).parent().find("input[name=proc_name]").val();
			var wf_app_name = $(this).parent().find("input[name=wf_app_name]").val();
			
			var data = {"proc_id":proc_id,"proc_name":proc_name, "business_name":business_name, "wf_app_name":wf_app_name};
			
			if(chooseStr.indexOf(proc_id)<0){
				chooseData.push(data);
				chooseStr += proc_id +"==";
				addProcdefTable(data);
			}
			
		});
		
		//W.chooseSelectedData(chooseData);
		
		//api.close();
	}
	
	function addProcdefTable(addData){
		var addRow = 
			"<tr>\n" +
			"\t<td>"+addData.wf_app_name+"<input type='hidden' name='procdef_id' value='"+ addData.proc_id +"' /></td>\n" + 
			"\t<td>"+addData.proc_name+"</td>\n" + 
			"\t<td>"+addData.business_name+"</td>\n" + 
			"\t<td><a href='#' onclick='delProcRow(this)' >删除</a></td>\n" + 
			"</tr>";
		$("#selectProcdefTable").append(addRow);
	}
	function delProcRow(delDoc){
		$(delDoc).parent().parent().remove();
		var removeProc = $(delDoc).parent().parent().find("input[name=procdef_id]").val()+"==";
		chooseStr = chooseStr.replace(removeProc,"");
	} 
	
	function delAllProcRow(){
		var titleRow = 
			"<tr>\n" +
			"\t<th>应用系统</th>\n" + 
			"\t<th>流程名称</th>\n" + 
			"\t<th>业务类型</th>\n" + 
			"\t<th>操作</th>\n" + 
			"</tr>";
		$("#selectProcdefTable").html("");
		$("#selectProcdefTable").append(titleRow);
		chooseStr = "";
		chooseData = new Array();
	}
	
	function subSelectedData(){
		
		var subData = new Array();
		
		if(chooseData != null && chooseData.length > 0){
			
			for(var i=0; i<chooseData.length; i++){
				
				var data = chooseData[i];
				
				if(chooseStr.indexOf(data.proc_id+"==")>=0){
					subData.push(data);
				}
			}
		}
		
		W.chooseSelectedData(subData);
		
		api.close();
	}
	
</script>
</head>
	<body>
		<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
			<div id="leftContentDiv" >
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
												流程名称：
											</th>
											<td>
												<input id="resourceName" name="resourceName" type="text"
													value="" class="w120" />
											</td>
											<th><pg:message code="sany.pdp.workflow.business.type"/>：</th>
											<td>
												<select class="easyui-combotree" id='businessType' name="businesstype_id" required="false"
														style="width: 120px;">
											</td>
										</tr>
										<tr>
											<th id="app_query_th">
												应用系统:
											</th>
											<td id="wf_app_name_td">
												<select id="wf_app_id" name="wf_app_id" type="text"
													value="" class="w120" onchange="queryList();" />
											</td>
											<td style="text-align:right">
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList();"><span><pg:message code="sany.pdp.common.operation.search"/></span>
												</a>
												<input type="hidden" name="wf_app_id" id="wf_app_id" />
												<input type="hidden" name="processChoose" id="processChoose" value="Y" />
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
				<!-- <a href="#" class="bt_small" id="addButton" onclick="javascript:selectedSel();"><span>选择</span></a>  -->
				<!--  <a href="#" class="bt_small" id="exportButton"><span>导出</span></a>
				<!--<input id="excelType" type="radio" name="excelType"   checked  >2003</input>
				<input  type="radio" name="excelType"  >2007</input>-->
				</div>
				
				<strong><pg:message code="sany.pdp.workflow.manage"/></strong>			
			</div>
			<div id="custombackContainer"  style="overflow:auto">
			
			</div>	
			</div>
			<div id="rightContentDiv" style="width:400px;">
			<div id="searchblock">
				<div class="title_box">
					<div class="rightbtn">
					<a href="#" class="bt_small" id="addButton" onclick="subSelectedData();"><span>确认选择</span></a>
					<a href="#" class="bt_small" id="exportButton" onclick="delAllProcRow();"><span>全部删除</span></a>
					<!--<input id="excelType" type="radio" name="excelType"   checked  >2003</input>
					<input  type="radio" name="excelType"  >2007</input>-->
					</div>
					
					<strong>已选流程</strong>			
				</div>
				<div id="selectProcdeContainer"  style="overflow:auto">
					<table id="selectProcdefTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="table3">
						<tr>
							<th>应用系统</th>
							<th>流程名称</th>
							<th>业务类型</th>
							<th>操作</th>
						</tr>
						
					</table>
				</div>
				<br/>
				
			</div>
			
			</div>
		</div>
	</body>
</html>
